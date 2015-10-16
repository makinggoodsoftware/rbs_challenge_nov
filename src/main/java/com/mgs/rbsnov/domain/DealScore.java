package com.mgs.rbsnov.domain;

public class DealScore {
    private final Card winner;
    private final int points;

    public DealScore(Card winner, int points) {
        this.winner = winner;
        this.points = points;
    }

    public Card getWinner() {
        return winner;
    }

    public int getPoints() {
        return points;
    }
}
