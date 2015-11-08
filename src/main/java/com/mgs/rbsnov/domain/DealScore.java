package com.mgs.rbsnov.domain;

import java.math.BigDecimal;

public class DealScore {
    private final Card winner;
    private final int winningCardIndex;
    private final BigDecimal points;

    public DealScore(Card winner, int winningCardIndex, BigDecimal points) {
        this.winner = winner;
        this.winningCardIndex = winningCardIndex;
        this.points = points;
    }

    public int getWinningCardIndex() {
        return winningCardIndex;
    }

    public Card getWinner() {
        return winner;
    }

    public BigDecimal getPoints() {
        return points;
    }
}
