package com.mgs.rbsnov.logic;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mgs.rbsnov.domain.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class CardSelector {
    private final GameAnalyser gameAnalyser;
    private final CardScorer cardScorer;
    private final HandsFactory handsFactory;
    private final PredictedScorer predictedScorer;

    public CardSelector(GameAnalyser gameAnalyser, CardScorer cardScorer, HandsFactory handsFactory, PredictedScorer predictedScorer) {
        this.gameAnalyser = gameAnalyser;
        this.cardScorer = cardScorer;
        this.handsFactory = handsFactory;
        this.predictedScorer = predictedScorer;
    }


    public Card bestCard(DealInProgress dealInProgress, Set<Card> inPlay, Set<Card> myCards, Player forPlayer) {
        List<List<CardAndScore>> listOfScenarios = runScenarios(inPlay, myCards, forPlayer, dealInProgress);
        List<CardAndScore> allScores = aggregate (listOfScenarios);
        Collections.sort(allScores, (left, right) -> {
            BigDecimal leftScore = left.predictedScore.getAveragedScore().get(forPlayer);
            BigDecimal rightScore = right.predictedScore.getAveragedScore().get(forPlayer);
            Integer leftPoints = cardScorer.score(left.card);
            Integer rightPoints = cardScorer.score(right.card);

            if (leftScore.compareTo(rightScore) != 0) return leftScore.compareTo(rightScore);
            if (leftPoints.compareTo(rightPoints) !=0 ) return leftPoints.compareTo(rightPoints);
            return left.card.getNumeration().compareTo(right.card.getNumeration());
        });
        return allScores.get(0).card;
    }

    private List<CardAndScore> aggregate(List<List<CardAndScore>> listOfScenarios) {
        Multimap<Card, PredictedScore> allScoresForCard = ArrayListMultimap.create();
        for (List<CardAndScore> scoresForScenario : listOfScenarios) {
            for (CardAndScore cardAndScore : scoresForScenario) {
                allScoresForCard.put(cardAndScore.card, cardAndScore.predictedScore);
            }
        }

        List<CardAndScore> aggregates = new ArrayList<>();
        for (Map.Entry<Card, Collection<PredictedScore>> cardCollectionEntry : allScoresForCard.asMap().entrySet()) {
            Card card = cardCollectionEntry.getKey();
            Collection<PredictedScore> scores = cardCollectionEntry.getValue();
            PredictedScore predictedScore = predictedScorer.average (scores);
            aggregates.add(new CardAndScore(card, predictedScore));
        }
        return aggregates;
    }

    private List<List<CardAndScore>> runScenarios(Set<Card> inPlay, Set<Card> myCards, Player forPlayer, DealInProgress dealInProgress) {
        List<List<CardAndScore>> allScoresBag = new ArrayList<>();
        for (int i = 0; i<=3; i++){
            List<CardAndScore> cardAndScores = runScenario(inPlay, myCards, forPlayer, dealInProgress);
            allScoresBag.add(cardAndScores);
        }
        return allScoresBag;
    }

    private List<CardAndScore> runScenario(Set<Card> inPlay, Set<Card> myCards, Player forPlayer, DealInProgress dealInProgress) {
        Hands hands = handsFactory.dealCards(forPlayer, myCards, inPlay);
        if (! hands.get(forPlayer).equals(myCards)) throw new IllegalStateException();
        GameState gameState = new GameState(hands, dealInProgress);
        Map<Card, PredictedScore> predictedScores = gameAnalyser.analyse(gameState);
        return predictedScores.entrySet().stream().
                map(cardPredictedScoreEntry ->
                        new CardAndScore(cardPredictedScoreEntry.getKey(), cardPredictedScoreEntry.getValue())
                ).
                collect(Collectors.toList());
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
