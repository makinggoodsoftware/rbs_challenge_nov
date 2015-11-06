package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CardScorer {
    private final Map<Card, Integer> scores;
    private final Map<Card, Integer> tempScores = new HashMap<>();

    public CardScorer(Map<Card, Integer> scores) {
        this.scores = scores;
    }

    public int score(Card toScore) {
        if (tempScores.containsKey(toScore)) return tempScores.get(toScore);
        return scores.getOrDefault(toScore, 0);
    }

    public void addTempScore(Card card, int score) {
        tempScores.put(card, score);
    }

    public void removeTempScores() {
        Set<Card> cards = tempScores.keySet();
        for (Card card : cards) {
            tempScores.remove(card);
        }
    }
}
