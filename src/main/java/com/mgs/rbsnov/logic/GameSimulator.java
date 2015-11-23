package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.adapter.CardsAdaptor;
import com.mgs.rbsnov.domain.*;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class GameSimulator {
    private final Logger LOG = Logger.getLogger(GameSimulator.class);

    private final CardsDiscarder cardsDiscarder;
    private final RoundDeveloperFactory roundDeveloperFactory;
    private final HandsFactory handsFactory;
    private final GameScorer gameScorer;
    private final CardsSetBuilder cardsSetBuilder;

    public GameSimulator(CardsDiscarder cardsDiscarder, RoundDeveloperFactory roundDeveloperFactory, HandsFactory handsFactory, GameScorer gameScorer, CardsSetBuilder cardsSetBuilder) {
        this.cardsDiscarder = cardsDiscarder;
        this.roundDeveloperFactory = roundDeveloperFactory;
        this.handsFactory = handsFactory;
        this.gameScorer = gameScorer;
        this.cardsSetBuilder = cardsSetBuilder;
    }

    public GameResult start(Set<Card> initialHand, PlayersLogic playersLogic) {
        Set<Card> inPlay = cardsSetBuilder.newSet(cardsSetBuilder.allCards()).remove(initialHand).build();
        Hands beforeDescarding = handsFactory.dealCards(Player.SOUTH, initialHand, inPlay, new HashMap<>(), new HashMap<>());
        return playHands(playersLogic, beforeDescarding);
    }

    public GameResult start(PlayersLogic playersLogic) {
        Hands beforeDescarding = handsFactory.fromAllCardsShuffled(Player.SOUTH, new HashMap<>(), new HashMap<>());
        return playHands(playersLogic, beforeDescarding);
    }

    private GameResult playHands(PlayersLogic playersLogic, Hands beforeDescarding) {
        Map<Player, DiscardResult> discards = cardsDiscarder.discard(playersLogic, beforeDescarding);
        LOG.info("DISCARDS");
        discards.entrySet().stream().forEach(it->{
            LOG.info(it.getKey());
            if (it.getValue().isShootingTheMoon()){
                LOG.warn("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                LOG.warn("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                LOG.warn("SHOOTING THE MOON!");
                LOG.warn("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                LOG.warn("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            }
            List<Card> initialCards = sortBy(it.getValue().getInitialCards(), bySuitAndNumeration());
            List<Card> discardCards = sortBy(it.getValue().getDiscardingCards(), bySuitAndNumeration());

            LOG.info("Initial cards: " + initialCards);
            LOG.info("Discards: " + discardCards);
        });

        RoundDeveloper roundDeveloper = roundDeveloperFactory.newRoundDeveloper(playersLogic, discards);
        Hands initialHands = handsFactory.fromDiscards(discards);
        List<RoundResult> roundResults = roundDeveloper.playAllRounds(initialHands, new HashMap<>(), GameScore.zeros());

        LOG.warn("========================================================================================");
        LOG.warn("GAME FINISHED");
        GameScore finalScores = gameScorer.finalScores(roundResults);
        LOG.warn("Card scores:");
        Player.all(Player.SOUTH).stream().forEach(player -> LOG.warn(player + " " + finalScores.getPlayersScore().get(player)));
        LOG.warn("Heart scores:");
        Player.all(Player.SOUTH).stream().forEach(player -> LOG.warn(player + " " + finalScores.getHeartsScore().get(player)));
        LOG.warn("========================================================================================");

        return new GameResult(roundResults);
    }

    private List<Card> sortBy(Set<Card> initialCards, Comparator<Card> comparator) {
        return initialCards.stream().sorted(comparator).collect(Collectors.toList());
    }

    private Comparator<Card> bySuitAndNumeration() {
        return (left, right) -> {
            int suitOrder = left.getSuit().compareTo(right.getSuit());
            return suitOrder == 0 ? right.getNumeration().compareTo(left.getNumeration()) : suitOrder;
        };
    }
}
