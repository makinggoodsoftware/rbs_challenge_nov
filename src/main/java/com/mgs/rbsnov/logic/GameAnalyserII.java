package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.*;

public class GameAnalyserII {
    private final DealsDeveloper dealsDeveloper;
    private final PredictedScorer predictedScorer;
    private final DealInProgressFactory dealInProgressFactory;
    private final CardsSetBuilder cardsSetBuilder;

    public GameAnalyserII(DealsDeveloper dealsDeveloper, PredictedScorer predictedScorer, DealInProgressFactory dealInProgressFactory, CardsSetBuilder cardsSetBuilder) {
        this.dealsDeveloper = dealsDeveloper;
        this.predictedScorer = predictedScorer;
        this.dealInProgressFactory = dealInProgressFactory;
        this.cardsSetBuilder = cardsSetBuilder;
    }

    public Map<Card, PredictedScore> analyse(GameState gameState, Player target){
        Map<Card, PredictedScore> analysis = new HashMap<>();
        DealInProgress dealInProgress = gameState.getDealInProgress();
        Set<FinishedDeal> possibleDeals = dealsDeveloper.develop(dealInProgress, gameState.getAllHands());
        Map<Card, Set<FinishedDeal>> possibleDealsByCard = asMap(possibleDeals, target);
        for (Map.Entry<Card, Set<FinishedDeal>> finishedDealsByCardEntry : possibleDealsByCard.entrySet()) {
            Card card = finishedDealsByCardEntry.getKey();
            Set<FinishedDeal> thisPossibleDeals = finishedDealsByCardEntry.getValue();
            PredictedScorer.PredictedScoring predictedScoring = getPredictedScoring(gameState, thisPossibleDeals, target);
            analysis.put(card, predictedScoring.build());
        }
        return analysis;
    }

    private PredictedScorer.PredictedScoring getPredictedScoring(GameState gameState, Set<FinishedDeal> possibleDeals, Player target) {
        PredictedScorer.PredictedScoring predictedScoring = predictedScorer.newScoring();
        for (FinishedDeal possibleDeal : possibleDeals) {
            predictedScoring.addScore(possibleDeal.getScore());
            if (! gameState.isLastDeal()){
                GameState childGameState = childGameState(gameState, possibleDeal);
                Map<Card, PredictedScore> childAnalysis = analyse(childGameState, target);
                predictedScoring.addCombinedChildrenDealScores(childAnalysis.values());
            }
        }
        return predictedScoring;
    }

    private GameState childGameState(GameState gameState, FinishedDeal possibleDeal) {
        int distanceToSouth = possibleDeal.getStartingPlayer().distanceTo(Player.SOUTH);
        GameState newGameState = new GameState(
                cardsSetBuilder.removeCards(gameState.getAllHands(),
                        possibleDeal.getDeal().getCard(relativeDistance(distanceToSouth, 0)),
                        possibleDeal.getDeal().getCard(relativeDistance(distanceToSouth, 1)),
                        possibleDeal.getDeal().getCard(relativeDistance(distanceToSouth, 2)),
                        possibleDeal.getDeal().getCard(relativeDistance(distanceToSouth, 3))
                ),
                dealInProgressFactory.newJustStartedDeal(possibleDeal.getWinningPlayer())
        );
        if (newGameState.getAllHands().getNorthHand().size() == gameState.getAllHands().getNorthHand().size()){
            throw new IllegalStateException();
        }
        return newGameState;
    }

    private int relativeDistance(int distanceToSouth, int position) {
        int rawValue = distanceToSouth + position;
        return rawValue > 3 ? rawValue - 4 : rawValue;
    }

    private Map<Card, Set<FinishedDeal>> asMap(Set<FinishedDeal> finishedDeals, Player target) {
        Map<Card, Set<FinishedDeal>> asMap = new HashMap<>();
        for (FinishedDeal finishedDeal : finishedDeals) {
            Card card = finishedDeal.getCard(target);
            if (!asMap.containsKey(card)){
                asMap.put(card, new HashSet<>());
            }
            asMap.get(card).add(finishedDeal);
        }
        return asMap;
    }
}
