package com.mgs.rbsnov.domain;

import com.google.common.base.Objects;

public class FinishedDeal {
    private final Deal deal;
    private final PlayersScore score;
    private final Player winningPlayer;
    private final Player startingPlayer;

    public FinishedDeal(Deal deal, PlayersScore score, Player winningPlayer, Player startingPlayer) {
        this.deal = deal;
        this.score = score;
        this.winningPlayer = winningPlayer;
        this.startingPlayer = startingPlayer;
    }

    public PlayersScore getScore() {
        return score;
    }

    public Deal getDeal() {
        return deal;
    }

    public Player getWinningPlayer() {
        return winningPlayer;
    }

    public Card getCard(Player target) {
        int distance = startingPlayer.distanceTo(target);
        return deal.getCard(distance);
    }

    public Player getStartingPlayer() {
        return startingPlayer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FinishedDeal)) return false;
        FinishedDeal that = (FinishedDeal) o;
        return Objects.equal(deal, that.deal) &&
                Objects.equal(score, that.score) &&
                winningPlayer == that.winningPlayer &&
                startingPlayer == that.startingPlayer;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(deal, score, winningPlayer, startingPlayer);
    }

    @Override
    public String toString() {
        return "FinishedDeal{" +
                "deal=" + deal +
                ", score=" + score +
                ", winningPlayer=" + winningPlayer +
                ", startingPlayer=" + startingPlayer +
                '}';
    }
}
