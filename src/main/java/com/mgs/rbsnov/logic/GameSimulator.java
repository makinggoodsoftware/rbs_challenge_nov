package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class GameSimulator {
    private final Logger LOG = Logger.getLogger(GameSimulator.class);

    private final CardsDiscarder cardsDiscarder;
    private final RoundDeveloperFactory roundDeveloperFactory;
    private final HandsFactory handsFactory;

    public GameSimulator(CardsDiscarder cardsDiscarder, RoundDeveloperFactory roundDeveloperFactory, HandsFactory handsFactory) {
        this.cardsDiscarder = cardsDiscarder;
        this.roundDeveloperFactory = roundDeveloperFactory;
        this.handsFactory = handsFactory;
    }

    public GameResult start(PlayersLogic playersLogic) {
        Hands beforeDescarding = handsFactory.fromAllCardsShuffled(Player.SOUTH, new HashMap<>(), new HashMap<>());
        Map<Player, DiscardResult> discards = cardsDiscarder.discard(playersLogic, beforeDescarding);
        LOG.info("DISCARDS");
        discards.entrySet().stream().forEach(it->{
            LOG.info(it.getKey());
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
        Map<Player, Integer> finalScores = new HashMap<>();
        Player.all(Player.SOUTH).stream().forEach(player -> finalScores.put(player, 0));

        for (RoundResult roundResult : roundResults) {
            PlayersScore score = roundResult.getFinishedDeal().getCardsScore();
            Player.all(Player.SOUTH).stream().forEach(player -> {
                Integer accumulated = finalScores.get(player) + score.get(player).intValue();
                finalScores.put(player, accumulated);
            });
        }
        LOG.warn("scores:");
        Player.all(Player.SOUTH).stream().forEach(player -> LOG.warn(player + " " + finalScores.get(player)));
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
