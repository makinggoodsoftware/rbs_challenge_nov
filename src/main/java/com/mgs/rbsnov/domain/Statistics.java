package com.mgs.rbsnov.domain;

import java.util.Set;

public class Statistics {
    private final Set<Card> cards;

    public Statistics(Set<Card> cards) {
        this.cards = cards;
    }

    public Set<Card> getCards() {
        return cards;
    }
}
