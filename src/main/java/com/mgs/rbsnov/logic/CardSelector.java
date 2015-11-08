package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CardSelector {
    private final CardsDealer cardsDealer;
    private final GameAnalyserII gameAnalyserII;
    private final DealInProgressFactory dealInProgressFactory;

    public CardSelector(CardsDealer cardsDealer, GameAnalyserII gameAnalyserII, DealInProgressFactory dealInProgressFactory) {
        this.cardsDealer = cardsDealer;
        this.gameAnalyserII = gameAnalyserII;
        this.dealInProgressFactory = dealInProgressFactory;
    }


    public Card bestCard(Set<Card> inPlay, Set<Card> myCards, Player forPlayer) {
        List<Set<Card>> otherPlayerCards = cardsDealer.deal(3, inPlay);
        GameState gameState = new GameState(
                new Hands(myCards, otherPlayerCards.get(0), otherPlayerCards.get(1), otherPlayerCards.get(2)),
                dealInProgressFactory.newJustStartedDeal(Player.NORTH));
        Map<Card, PredictedScore> predictedScores = gameAnalyserII.analyse(gameState, forPlayer);
        Card bestCard = null;
        PredictedScore bestScore = null;
        for (Map.Entry<Card, PredictedScore> cardPredictedScoreEntry : predictedScores.entrySet()) {
            PredictedScore thisScore = cardPredictedScoreEntry.getValue();
            if (isThisBestScore (bestScore, thisScore, forPlayer)){
                bestScore = thisScore;
                bestCard = cardPredictedScoreEntry.getKey();
            }
        }
        return bestCard;
    }

    private boolean isThisBestScore(PredictedScore bestScore, PredictedScore thisScore, Player forPlayer) {
        if (bestScore== null) return true;
        int compareToResult = thisScore.getAveragedScore().get(forPlayer).compareTo(bestScore.getAveragedScore().get(forPlayer));
        return compareToResult < 0;
    }
}
