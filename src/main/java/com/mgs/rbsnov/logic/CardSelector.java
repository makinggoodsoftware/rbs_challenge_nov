package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class CardSelector {
    private final GameAnalyser gameAnalyser;
    private final CardScorer cardScorer;
    private final HandsFactory handsFactory;

    public CardSelector(GameAnalyser gameAnalyser, CardScorer cardScorer, HandsFactory handsFactory) {
        this.gameAnalyser = gameAnalyser;
        this.cardScorer = cardScorer;
        this.handsFactory = handsFactory;
    }


    public Card bestCard(Set<Card> inPlay, Set<Card> myCards, Player forPlayer, DealInProgress dealInProgress) {
        Hands hands = handsFactory.dealCards(forPlayer, myCards, inPlay);
        if (! hands.get(forPlayer).equals(myCards)) throw new IllegalStateException();
        GameState gameState = new GameState(hands, dealInProgress);
        Map<Card, PredictedScore> predictedScores = gameAnalyser.analyse(gameState, forPlayer);
        List<CardAndScore> cardAndScores = predictedScores.entrySet().stream().
                map(cardPredictedScoreEntry ->
                        new CardAndScore(cardPredictedScoreEntry.getKey(), cardPredictedScoreEntry.getValue())
                ).
                collect(Collectors.toList());
        Collections.sort(cardAndScores, (left, right) -> {
            BigDecimal leftScore = left.predictedScore.getAveragedScore().get(forPlayer);
            BigDecimal rightScore = right.predictedScore.getAveragedScore().get(forPlayer);
            Integer leftPoints = cardScorer.score(left.card);
            Integer rightPoints = cardScorer.score(right.card);

            if (leftScore.compareTo(rightScore) != 0) return leftScore.compareTo(rightScore);
            if (leftPoints.compareTo(rightPoints) !=0 ) return leftPoints.compareTo(rightPoints);
            return left.card.getNumeration().compareTo(right.card.getNumeration());
        });
        return cardAndScores.get(0).card;
    }

    class CardAndScore {
        private final Card card;
        private final PredictedScore predictedScore;

        CardAndScore(Card card, PredictedScore predictedScore) {
            this.card = card;
            this.predictedScore = predictedScore;
        }
    }
}
