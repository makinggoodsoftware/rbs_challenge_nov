package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameAnalyserII {
    private final DealsDeveloper dealsDeveloper;
    private final PredictedScorer predictedScorer;
    private final GameStateFactory gameStateFactory;

    public GameAnalyserII(DealsDeveloper dealsDeveloper, PredictedScorer predictedScorer, GameStateFactory gameStateFactory) {
        this.dealsDeveloper = dealsDeveloper;
        this.predictedScorer = predictedScorer;
        this.gameStateFactory = gameStateFactory;
    }

    public Map<Card, PredictedScore> analyse(GameState gameState){
        Map<Card, PredictedScore> analysis = new HashMap<>();
        DealInProgress dealInProgress = gameState.getDealInProgress();
        Set<FinishedDeal> possibleDeals = dealsDeveloper.develop(dealInProgress, gameState.getAllHands());
        Map<Card, Set<FinishedDeal>> possibleDealsByCard = asMap(possibleDeals);
        for (Map.Entry<Card, Set<FinishedDeal>> finishedDealsByCardEntry : possibleDealsByCard.entrySet()) {
            Card card = finishedDealsByCardEntry.getKey();
            Set<FinishedDeal> thisPossibleDeals = finishedDealsByCardEntry.getValue();
            PredictedScorer.PredictedScoring predictedScoring = getPredictedScoring(gameState, thisPossibleDeals);
            analysis.put(card, predictedScoring.build());
        }
        return analysis;
    }

    private PredictedScorer.PredictedScoring getPredictedScoring(GameState gameState, Set<FinishedDeal> possibleDeals) {
        PredictedScorer.PredictedScoring predictedScoring = predictedScorer.newScoring();
        for (FinishedDeal possibleDeal : possibleDeals) {
            predictedScoring.addScore(possibleDeal.getScore());
            Set<GameState> childGameStates = gameStateFactory.subsets(gameState, possibleDeal);
            for (GameState childGameState : childGameStates) {
                Map<Card, PredictedScore> childAnalysis = analyse(childGameState);
                predictedScoring.addCombinedChildrenDealScores(childAnalysis.values());
            }
        }
        return predictedScoring;
    }

    private Map<Card, Set<FinishedDeal>> asMap(Set<FinishedDeal> finishedDeals) {
        return null;
    }
}
