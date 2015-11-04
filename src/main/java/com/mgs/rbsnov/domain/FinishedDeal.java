package com.mgs.rbsnov.domain;

import com.google.common.base.Objects;

public class FinishedDeal {
    private final Deal deal;
    private final PlayersScore score;
    private final Player winningPlayer;

    public FinishedDeal(Deal deal, PlayersScore score, Player winningPlayer) {
        this.deal = deal;
        this.score = score;
        this.winningPlayer = winningPlayer;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FinishedDeal)) return false;
        FinishedDeal that = (FinishedDeal) o;
        return Objects.equal(deal, that.deal) &&
                Objects.equal(score, that.score) &&
                winningPlayer == that.winningPlayer;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(deal, score, winningPlayer);
    }

    @Override
    public String toString() {
        return "FinishedDeal{" +
                "deal=" + deal +
                ", score=" + score +
                ", winningPlayer=" + winningPlayer +
                '}';
    }
}
