package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameAnalyser {
    private final PredictedScorer predictedScorer;
    private final CardsSetBuilder cardsSetBuilder;
    private final CardsDealer cardsDealer;
    private final CardDealsCombinator cardDealsCombinator;
    private final PlayersScorer playersScorer;

    public GameAnalyser(PredictedScorer predictedScorer, CardsSetBuilder cardsSetBuilder, CardsDealer cardsDealer, CardDealsCombinator cardDealsCombinator, PlayersScorer playersScorer) {
        this.predictedScorer = predictedScorer;
        this.cardsSetBuilder = cardsSetBuilder;
        this.cardsDealer = cardsDealer;
        this.cardDealsCombinator = cardDealsCombinator;
        this.playersScorer = playersScorer;
    }

    public Map<Card, PredictedScore> analyse(GameState gameState) {
        Set<Card> myHand = gameState.getMyHand();
        Set<Card> cardsToPlay = gameState.getCardsToPlay();

        List<Set<Card>> otherPlayerCards = cardsDealer.deal(3, cardsToPlay);
        Map<Card, Set<Deal>> dealsByCard = cardDealsCombinator.combine(myHand, otherPlayerCards.get(0), otherPlayerCards.get(1), otherPlayerCards.get(2));
        Map<Card, PredictedScore> cardScores = new HashMap<>();

        for (Map.Entry<Card, Set<Deal>> dealsByCardEntry : dealsByCard.entrySet()) {
            Card thisCard = dealsByCardEntry.getKey();
            Set<Deal> possibleDeals = dealsByCardEntry.getValue();
            PredictedScorer.PredictedScoring cardScoring = predictedScorer.newScoring();

            for (Deal deal : possibleDeals) {
                cardScoring.addScore(playersScorer.score(Player.SOUTH, deal));
                if (!gameState.isLastDeal()) {
                    Set<Card> newHand = cardsSetBuilder.newSet(myHand).remove(thisCard).build();
                    Set<Card> newCardsToPlay = cardsSetBuilder.newSet(cardsToPlay).remove(deal.getCard2(), deal.getCard3(), deal.getCard4()).build();

                    GameState newGameState = new GameState(newHand, newCardsToPlay);
                    Map<Card, PredictedScore> childScores = analyse(newGameState);
                    cardScoring.addPredictedScores(childScores.values());
                }
            }

            cardScores.put(thisCard, cardScoring.build());
        }
        return cardScores;
    }

}
