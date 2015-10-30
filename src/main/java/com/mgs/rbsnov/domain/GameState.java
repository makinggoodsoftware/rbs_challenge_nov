package com.mgs.rbsnov.domain;

import java.util.Set;

public class GameState {
    private final Set<Card> southHand;
    private final Set<Card> westHand;
    private final Set<Card> northHand;
    private final Set<Card> eastHand;

    public GameState(Set<Card> southHand, Set<Card> westHand, Set<Card> northHand, Set<Card> eastHand) {
        this.southHand = southHand;
        this.westHand = westHand;
        this.northHand = northHand;
        this.eastHand = eastHand;

        if (
                southHand.size() != eastHand.size() ||
                eastHand.size() != northHand.size() ||
                northHand.size() != westHand.size() ||
                westHand.size() != southHand.size()
        ) {
            throw new IllegalStateException();
        }
    }


    public Set<Card> getWestHand() {
        return westHand;
    }

    public Set<Card> getNorthHand() {
        return northHand;
    }

    public Set<Card> getEastHand() {
        return eastHand;
    }

    public Set<Card> getSouthHand() {
        return southHand;
    }

    public boolean isLastDeal() {
        return southHand.size() == 1;
    }
}
