package com.mgs.rbsnov.domain;

import java.util.Set;

public class GameState {
    private final Hands hands;
    private final DealInProgress dealInProgress;

    public GameState(Hands hands, DealInProgress dealInProgress) {
        this.hands = hands;
        this.dealInProgress = dealInProgress;
    }


    public Set<Card> getWestHand() {
        return hands.getWestHand();
    }

    public Set<Card> getNorthHand() {
        return hands.getNorthHand();
    }

    public Set<Card> getEastHand() {
        return hands.getEastHand();
    }

    public Set<Card> getSouthHand() {
        return hands.getSouthHand();
    }

    public boolean isLastDeal() {
        return hands.getSouthHand().size() == 1;
    }

    public DealInProgress getDealInProgress() {
        return dealInProgress;
    }

    public Hands getAllHands() {
        return hands;
    }
}
