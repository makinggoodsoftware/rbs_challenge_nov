package com.mgs.rbsnov.domain;

import java.util.Set;

public class Hand {
    private final Set<Card> cards;


    public Hand(Set<Card> cards) {
        this.cards = cards;
    }

    public Set<Card> getCards() {
        return cards;
    }
}
