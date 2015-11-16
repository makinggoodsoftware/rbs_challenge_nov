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
        List<RoundResult> roundResults = new ArrayList<>();
        Player startingPlayer = heartRules.findStartingPlayer(hands);
        FinishedDeal finishedDeal;
        do {
            finishedDeal = playRound(hands, startingPlayer);
            roundResults.add(new RoundResult(hands, finishedDeal));
            hands = handsFactory.reduce(hands, finishedDeal.getDeal(), startingPlayer);
            startingPlayer = finishedDeal.getWinningPlayer();
        } while (heartRules.cardsToPlayRemaining(hands));
        return roundResults;
    }

    FinishedDeal playRound(Hands hands, Player startingPlayer) {
        PlayerRotator.PlayerRotation playerRotation = playerRotator.clockwiseIterator(startingPlayer);
        DealInProgress dealInProgress = dealInProgressFactory.newJustStartedDeal(startingPlayer);
        while(playerRotation.hasNext()){
            Player thisPlayer = playerRotation.next();
            Set<Card> inPlay = cardsSetBuilder.newEmptySet().
                    add(hands.get(thisPlayer.moveClockWise(1))).
                    add(hands.get(thisPlayer.moveClockWise(2))).
                    add(hands.get(thisPlayer.moveClockWise(3))).
                    build();
            Card card = playerLogicMap.get(thisPlayer).playCard (dealInProgress,  inPlay, hands.get(thisPlayer), discards.get(thisPlayer));
            dealInProgressFactory.next(dealInProgress, card);
        }
        return playersScorer.score(dealInProgress);
    }


}
