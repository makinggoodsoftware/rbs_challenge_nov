package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoundDeveloper {
    private final PlayerRotator playerRotator;
    private final DealInProgressFactory dealInProgressFactory;
    private final PlayersScorer playersScorer;
    private final HandsFactory handsFactory;
    private final HeartRules heartRules;
    private final CardsSetBuilder cardsSetBuilder;
    private final PlayersLogic playerLogicMap;
    private final Map<Player, DiscardResult> discards;

    public RoundDeveloper(PlayerRotator playerRotator, DealInProgressFactory dealInProgressFactory, PlayersScorer playersScorer, PlayersLogic playerLogicMap, Map<Player, DiscardResult> discards, HandsFactory handsFactory, HeartRules heartRules, CardsSetBuilder cardsSetBuilder) {
        this.playerRotator = playerRotator;
        this.dealInProgressFactory = dealInProgressFactory;
        this.playersScorer = playersScorer;
        this.playerLogicMap = playerLogicMap;
        this.discards = discards;
        this.handsFactory = handsFactory;
        this.heartRules = heartRules;
        this.cardsSetBuilder = cardsSetBuilder;
    }

    public List<RoundResult> playAllRounds(Hands hands) {
        FinishedDeal finishedDeal;
        List<RoundResult> roundResults = new ArrayList<>();
        Player startingPlayer = heartRules.findStartingPlayer(hands);
        DealInProgress dealInProgress = dealInProgressFactory.oneCardDeal(startingPlayer, Card.TWO_OF_CLUBS);
        Hands thisHands = handsFactory.reduce(hands, startingPlayer, Card.TWO_OF_CLUBS);
        do {
            System.out.println("=====================================================================================");
            System.out.println("Playing round: ");
            System.out.println("North: " + thisHands.getNorthHand());
            System.out.println("East: " + thisHands.getEastHand());
            System.out.println("South: " + thisHands.getSouthHand());
            System.out.println("West: " + thisHands.getWestHand());
            System.out.println("=====================================================================================");
            finishedDeal = playDeal(thisHands, dealInProgress);
            RoundResult roundResult = new RoundResult(thisHands, finishedDeal);
            roundResults.add(roundResult);
            System.out.println("Round completed: Won by - " + finishedDeal.getWinningPlayer() + " round score: " + roundResult.getFinishedDeal().getScore());
            Hands previousHands = thisHands;
            thisHands = handsFactory.reduce(thisHands, finishedDeal.getDeal(), startingPlayer);
            if (previousHands.equals(thisHands)){
                throw new IllegalStateException();
            }
            startingPlayer = finishedDeal.getWinningPlayer();
            dealInProgress = dealInProgressFactory.newJustStartedDeal(startingPlayer);
        } while (heartRules.cardsToPlayRemaining(hands));
        return roundResults;
    }

    FinishedDeal playDeal(Hands hands, DealInProgress dealInProgress) {
        if (dealInProgress.isCompleted()) return playersScorer.score(dealInProgress);


        Player thisPlayer = dealInProgress.getWaitingForPlayer().get();
        System.out.println("    Players turn: " + thisPlayer);
        Set<Card> thisHand = hands.get(thisPlayer);
        Set<Card> inPlay = cardsSetBuilder.newEmptySet().
                add(hands.getNorthHand()).
                add(hands.getEastHand()).
                add(hands.getSouthHand()).
                add(hands.getWestHand()).
                remove(thisHand).
                build();
        boolean inPlayNotUnique = inPlay.stream().anyMatch(thisHand::contains);
        if (inPlayNotUnique) throw new IllegalStateException();
        Card card = playerLogicMap.get(thisPlayer).playCard (dealInProgress, inPlay, thisHand, discards.get(thisPlayer));
        System.out.println("    Card to play: " + card);
        if (! thisHand.contains(card)) throw new IllegalStateException("Player trying to play a card that he doesn't own");
        dealInProgress = dealInProgressFactory.next(dealInProgress, card);
        return playDeal(handsFactory.reduce(hands, thisPlayer, card), dealInProgress);
    }


}
