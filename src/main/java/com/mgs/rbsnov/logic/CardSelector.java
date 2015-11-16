package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class CardSelector {
    private final CardsDealer cardsDealer;
    private final GameAnalyser gameAnalyser;
    private final CardScorer cardScorer;

    public CardSelector(CardsDealer cardsDealer, GameAnalyser gameAnalyser, CardScorer cardScorer) {
        this.cardsDealer = cardsDealer;
        this.gameAnalyser = gameAnalyser;
        this.cardScorer = cardScorer;
    }


    public Card bestCard(Set<Card> inPlay, Set<Card> myCards, Player forPlayer, DealInProgress dealInProgress) {
        List<Set<Card>> otherPlayerCards = cardsDealer.deal(3, inPlay);
        GameState gameState = new GameState(
                new Hands(myCards, otherPlayerCards.get(0), otherPlayerCards.get(1), otherPlayerCards.get(2)),
                dealInProgress);
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
