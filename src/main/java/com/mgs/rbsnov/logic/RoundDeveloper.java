package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;
import org.apache.log4j.Logger;

import java.util.*;

public class RoundDeveloper {
    private final Logger LOG = Logger.getLogger(ProgressiveCardSelectorFactory.class);
    
    private final DealInProgressFactory dealInProgressFactory;
    private final FinishedDealScorer finishedDealScorer;
    private final HandsFactory handsFactory;
    private final HeartRules heartRules;
    private final CardsSetBuilder cardsSetBuilder;
    private final PlayersLogic playerLogicMap;
    private final Map<Player, DiscardResult> discards;

    public RoundDeveloper(DealInProgressFactory dealInProgressFactory, FinishedDealScorer finishedDealScorer, PlayersLogic playerLogicMap, Map<Player, DiscardResult> discards, HandsFactory handsFactory, HeartRules heartRules, CardsSetBuilder cardsSetBuilder) {
        this.dealInProgressFactory = dealInProgressFactory;
        this.finishedDealScorer = finishedDealScorer;
        this.playerLogicMap = playerLogicMap;
        this.discards = discards;
        this.handsFactory = handsFactory;
        this.heartRules = heartRules;
        this.cardsSetBuilder = cardsSetBuilder;
    }

    public List<RoundResult> playAllRounds(Hands hands, Map<Player, Set<Suit>> missingSuits) {
        FinishedDeal finishedDeal;
        List<RoundResult> roundResults = new ArrayList<>();
        Player startingPlayer = heartRules.findStartingPlayer(hands);
        DealInProgress dealInProgress = dealInProgressFactory.oneCardDeal(startingPlayer, Card.TWO_OF_CLUBS);
        Hands thisHands = handsFactory.reduce(hands, startingPlayer, Card.TWO_OF_CLUBS);
        do {
            LOG.info("=====================================================================================");
            LOG.info("Playing round: ");
            LOG.info("North: " + thisHands.getNorthHand());
            LOG.info("East: " + thisHands.getEastHand());
            LOG.info("South: " + thisHands.getSouthHand());
            LOG.info("West: " + thisHands.getWestHand());
            LOG.info("=====================================================================================");
            finishedDeal = playDeal(thisHands, dealInProgress, missingSuits);
            RoundResult roundResult = new RoundResult(thisHands, finishedDeal);
            roundResults.add(roundResult);
            LOG.info("Round completed: Won by - " + finishedDeal.getWinningPlayer() + " round score: " + roundResult.getFinishedDeal().getScore());
            Hands previousHands = thisHands;
            thisHands = handsFactory.reduce(thisHands, finishedDeal.getDeal(), startingPlayer);
            if (previousHands.equals(thisHands)){
                throw new IllegalStateException();
            }
            startingPlayer = finishedDeal.getWinningPlayer();
            dealInProgress = dealInProgressFactory.newJustStartedDeal(startingPlayer);
        } while (heartRules.cardsToPlayRemaining(thisHands));
        return roundResults;
    }

    FinishedDeal playDeal(Hands hands, DealInProgress dealInProgress, Map<Player, Set<Suit>> missingSuits) {
        if (dealInProgress.isCompleted()) return finishedDealScorer.score(dealInProgress);


        Player thisPlayer = dealInProgress.getWaitingForPlayer().get();
        LOG.info("Players turn: " + thisPlayer);
        Set<Card> thisHand = hands.get(thisPlayer);
        Set<Card> inPlay = cardsSetBuilder.newEmptySet().
                add(hands.getNorthHand()).
                add(hands.getEastHand()).
                add(hands.getSouthHand()).
                add(hands.getWestHand()).
                remove(thisHand).
                build();

        if ((inPlay.size() + thisHand.size() + dealInProgress.getCardSize()) % 4 != 0){
            throw new IllegalStateException();
        }
        boolean inPlayNotUnique = inPlay.stream().anyMatch(thisHand::contains);
        if (inPlayNotUnique) throw new IllegalStateException();
        Card card = playerLogicMap.get(thisPlayer).playCard (dealInProgress, inPlay, thisHand, discards.get(thisPlayer).getDiscardingCards(), missingSuits);
        if (card == null){
            throw new IllegalStateException();
        }
        LOG.info("Card to play: " + card);
        if (! thisHand.contains(card)) throw new IllegalStateException("Player trying to play a card that he doesn't own");

        dealInProgress.getLeadingCard().ifPresent(leadingCard -> {
            Suit leadingSuit = leadingCard.getSuit();
            if (card.getSuit() != leadingSuit){
                if (missingSuits.get(thisPlayer) == null) missingSuits.put(thisPlayer, new HashSet<>());
                missingSuits.get(thisPlayer).add(leadingSuit);
                LOG.info("Adding missing suit: " + thisPlayer + " doesnt have " + leadingSuit);
            }
        });

        dealInProgress = dealInProgressFactory.next(dealInProgress, card);
        return playDeal(handsFactory.reduce(hands, thisPlayer, card), dealInProgress, missingSuits);
    }
}
