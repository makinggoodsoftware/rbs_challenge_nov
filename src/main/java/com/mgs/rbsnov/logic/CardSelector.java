package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.GameState;
import com.mgs.rbsnov.domain.PredictedScore;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CardSelector {
    private final GameAnalyser gameAnalyser;
    private final CardsDealer cardsDealer;

    public CardSelector(GameAnalyser gameAnalyser, CardsDealer cardsDealer) {
        this.gameAnalyser = gameAnalyser;
        this.cardsDealer = cardsDealer;
    }


    public Card bestCard(Set<Card> inPlay, Set<Card> myCards) {
        List<Set<Card>> otherPlayerCards = cardsDealer.deal(3, inPlay);
        GameState gameState = new GameState(
                null,
                null);
        Map<Card, PredictedScore> predictedScores = gameAnalyser.analyse(gameState);
        return null;
    }
}
