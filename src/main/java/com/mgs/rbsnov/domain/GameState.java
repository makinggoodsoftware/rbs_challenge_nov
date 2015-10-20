package com.mgs.rbsnov.domain;

import java.util.Set;

public class GameState {
    private final Set<Card> cardsToPlay;
    private final Set<Card> myHand;
    private boolean lastDeal;

    public GameState(Set<Card> myHand, Set<Card> cardsToPlay) {
        this.cardsToPlay = cardsToPlay;
        this.myHand = myHand;
    }

    public Set<Card> getCardsToPlay() {
        return cardsToPlay;
    }

    public Set<Card> getMyHand() {
        return myHand;
    }

    public boolean isLastDeal() {
        return lastDeal;
    }
}
