package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CardScorer {
    private final Map<Card, Integer> scores;
    private Map<Card, Integer> tempScores = new HashMap<>();

    public CardScorer(Map<Card, Integer> scores) {
        this.scores = scores;
    }

    public Integer score(Card toScore) {
        if (tempScores.containsKey(toScore)) return tempScores.get(toScore);
        return scores.getOrDefault(toScore, 0);
    }

    public void addTempScore(Card card, int score) {
        tempScores.put(card, score);
    }

    public void removeTempScores() {
        tempScores = new HashMap<>();
    }
}
