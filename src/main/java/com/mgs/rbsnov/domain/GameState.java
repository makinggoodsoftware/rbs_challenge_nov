package com.mgs.rbsnov.domain;

import com.google.common.base.Objects;

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
        return
            hands.getSouthHand().size() == 1 ||
            hands.getNorthHand().size() == 1 ||
            hands.getEastHand().size() == 1 ||
            hands.getWestHand().size() == 1 ;
    }

    public DealInProgress getDealInProgress() {
        return dealInProgress;
    }

    public Hands getAllHands() {
        return hands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameState)) return false;
        GameState gameState = (GameState) o;
        return Objects.equal(hands, gameState.hands) &&
                Objects.equal(dealInProgress, gameState.dealInProgress);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hands, dealInProgress);
    }

    @Override
    public String toString() {
        return "GameState{" +
                "hands=" + hands +
                ", dealInProgress=" + dealInProgress +
                '}';
    }
}
