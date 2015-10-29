package com.mgs.rbsnov.domain;

public class DealScore {
    private final Card winner;
    private final int winningCardIndex;
    private final int points;

    public DealScore(Card winner, int winningCardIndex, int points) {
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

    public int getPoints() {
        return points;
    }
}
