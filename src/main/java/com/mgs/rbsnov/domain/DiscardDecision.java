package com.mgs.rbsnov.domain;

import java.util.Set;

public class DiscardDecision {
    private final Set<Card> toDiscard;
    private final boolean shootingTheMoon;

    public DiscardDecision(Set<Card> toDiscard, boolean shootingTheMoon) {
        this.toDiscard = toDiscard;
        this.shootingTheMoon = shootingTheMoon;
    }

    public Set<Card> getToDiscard() {
        return toDiscard;
    }

    public boolean isShootingTheMoon() {
        return shootingTheMoon;
    }
}
