package com.mgs.rbsnov.domain;

public class RoundResult {
    private final Hands hands;
    private final FinishedDeal finishedDeal;

    public RoundResult(Hands hands, FinishedDeal finishedDeal) {

        this.hands = hands;
        this.finishedDeal = finishedDeal;
    }

    public Hands getHands() {
        return hands;
    }

    public FinishedDeal getFinishedDeal() {
        return finishedDeal;
    }
}
