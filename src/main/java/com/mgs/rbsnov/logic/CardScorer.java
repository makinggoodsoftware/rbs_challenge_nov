package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;

import java.util.Map;

public class CardScorer {
    private final Map<Card, Integer> scores;

    public CardScorer(Map<Card, Integer> scores) {
        this.scores = scores;
    }

    public int score(Card toScore) {
        return scores.getOrDefault(toScore, 0);
    }
}
