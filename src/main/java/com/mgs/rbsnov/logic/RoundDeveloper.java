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
            finishedDeal = playRound(thisHands, dealInProgress);
            RoundResult roundResult = new RoundResult(thisHands, finishedDeal);
            roundResults.add(roundResult);
            System.out.println("Round completed: Won by - " + finishedDeal.getWinningPlayer() + " round score: " + roundResult.getFinishedDeal().getScore());
            thisHands = handsFactory.reduce(thisHands, finishedDeal.getDeal(), startingPlayer);
            startingPlayer = finishedDeal.getWinningPlayer();
        } while (heartRules.cardsToPlayRemaining(hands));
        return roundResults;
    }

    FinishedDeal playRound(Hands hands, DealInProgress dealInProgress) {
        System.out.println("=====================================================================================");
        System.out.println("Playing round: ");
        System.out.println("North: " + hands.getNorthHand());
        System.out.println("East: " + hands.getEastHand());
        System.out.println("South: " + hands.getSouthHand());
        System.out.println("West: " + hands.getWestHand());
        System.out.println("=====================================================================================");

        PlayerRotator.PlayerRotation playerRotation = playerRotator.clockwiseIterator(dealInProgress.getWaitingForPlayer().get());
        while(! dealInProgress.isCompleted()){
            Player thisPlayer = playerRotation.next();
            System.out.println("    Players turn: " + thisPlayer);
            Set<Card> inPlay = cardsSetBuilder.newEmptySet().
                    add(hands.get(thisPlayer.moveClockWise(1))).
                    add(hands.get(thisPlayer.moveClockWise(2))).
                    add(hands.get(thisPlayer.moveClockWise(3))).
                    build();
            Card card = playerLogicMap.get(thisPlayer).playCard (dealInProgress,  inPlay, hands.get(thisPlayer), discards.get(thisPlayer));
            System.out.println("    Card to play: " + card);
            dealInProgress = dealInProgressFactory.next(dealInProgress, card);
        }
        return playersScorer.score(dealInProgress);
    }


}
