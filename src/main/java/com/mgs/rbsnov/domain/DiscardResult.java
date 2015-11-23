package com.mgs.rbsnov.domain;

import java.util.Set;

public class DiscardResult {
    private final Set<Card> initialCards;
    private final Set<Card> receivingCards;
    private final Set<Card> discardingCards;
    private final boolean shootingTheMoon;

    public DiscardResult(Set<Card> initialCards, Set<Card> receivingCards, Set<Card> discardingCards, boolean shootingTheMoon) {
        this.initialCards = initialCards;
        this.receivingCards = receivingCards;
        this.discardingCards = discardingCards;
        this.shootingTheMoon = shootingTheMoon;
    }

    public Set<Card> getInitialCards() {
        return initialCards;
    }

    public Set<Card> getReceivingCards() {
        return receivingCards;
    }

    public Set<Card> getDiscardingCards() {
        return discardingCards;
    }

    public boolean isShootingTheMoon() {
        return shootingTheMoon;
    }
}
