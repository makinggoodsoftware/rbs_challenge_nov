package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameAnalyser {
    private final PredictedScorer predictedScorer;
    private final CardsSetBuilder cardsSetBuilder;
    private final CardDealsCombinator cardDealsCombinator;
    private final PlayersScorer playersScorer;

    public GameAnalyser(PredictedScorer predictedScorer, CardsSetBuilder cardsSetBuilder, CardDealsCombinator cardDealsCombinator, PlayersScorer playersScorer) {
        this.predictedScorer = predictedScorer;
        this.cardsSetBuilder = cardsSetBuilder;
        this.cardDealsCombinator = cardDealsCombinator;
        this.playersScorer = playersScorer;
    }

    public Map<Card, PredictedScore> analyse(GameState gameState) {
        Map<Card, Set<Deal>> dealsByCard = cardDealsCombinator.combine(
                gameState.getSouthHand(),
                gameState.getWestHand(),
                gameState.getNorthHand(),
                gameState.getEastHand()
        );
        Map<Card, PredictedScore> cardScores = new HashMap<>();

        for (Map.Entry<Card, Set<Deal>> dealsByCardEntry : dealsByCard.entrySet()) {
            Card thisCard = dealsByCardEntry.getKey();
            Set<Deal> possibleDeals = dealsByCardEntry.getValue();
            PredictedScorer.PredictedScoring cardScoring = predictedScorer.newScoring();

            for (Deal deal : possibleDeals) {
                cardScoring.addScore(playersScorer.score(Player.SOUTH, deal).getScore());
                if (!gameState.isLastDeal()) {

                    GameState newGameState = new GameState(
                            null,
                            null);
                    Map<Card, PredictedScore> childScores = analyse(newGameState);
                    cardScoring.addCombinedChildrenDealScores(childScores.values());
                }
            }

            cardScores.put(thisCard, cardScoring.build());
        }
        return cardScores;
    }

}
