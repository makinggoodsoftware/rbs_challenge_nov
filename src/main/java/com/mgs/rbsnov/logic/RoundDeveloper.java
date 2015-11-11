package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoundDeveloper {
    private final PlayerRotator playerRotator;
    private final DealInProgressFactory dealInProgressFactory;
    private final PlayersScorer playersScorer;
    private final Map<Player, PlayerLogic> playerLogicMap;
    private final Map<Player, DiscardResult> discards;
    private final HandsFactory handsFactory;

    public RoundDeveloper(PlayerRotator playerRotator, DealInProgressFactory dealInProgressFactory, PlayersScorer playersScorer, Map<Player, PlayerLogic> playerLogicMap, Map<Player, DiscardResult> discards, HandsFactory handsFactory) {
        this.playerRotator = playerRotator;
        this.dealInProgressFactory = dealInProgressFactory;
        this.playersScorer = playersScorer;
        this.playerLogicMap = playerLogicMap;
        this.discards = discards;
        this.handsFactory = handsFactory;
    }

    public List<RoundResult> playAllRounds(Hands hands) {
        List<RoundResult> roundResults = new ArrayList<>();
        Player startingPlayer = findStartingPlayer(hands);
        FinishedDeal finishedDeal;
        do {
            finishedDeal = playRound(hands, startingPlayer);
            roundResults.add(new RoundResult(hands, finishedDeal));
            hands = handsFactory.reduce(hands, finishedDeal.getDeal(), startingPlayer);
            startingPlayer = finishedDeal.getWinningPlayer();
        } while (cardsToPlayRemaining(hands));
        return roundResults;
    }

    FinishedDeal playRound(Hands hands, Player startingPlayer) {
        PlayerRotator.PlayerRotation playerRotation = playerRotator.clockwiseIterator(startingPlayer);
        DealInProgress dealInProgress = dealInProgressFactory.newJustStartedDeal(startingPlayer);
        while(playerRotation.hasNext()){
            Player thisPlayer = playerRotation.next();
            Card card = playerLogicMap.get(thisPlayer).playCard (dealInProgress, hands.get(thisPlayer), discards.get(thisPlayer));
            dealInProgressFactory.next(dealInProgress, card);
        }
        return playersScorer.score(dealInProgress);
    }

    private boolean cardsToPlayRemaining(Hands hands) {
        return false;
    }


    private Player findStartingPlayer(Hands discards) {
        return null;
    }
}
