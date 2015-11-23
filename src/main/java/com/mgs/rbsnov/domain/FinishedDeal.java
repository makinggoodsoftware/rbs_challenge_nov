package com.mgs.rbsnov.domain;

import com.google.common.base.Objects;

import java.util.Optional;

public class FinishedDeal {
    private final Deal deal;
    private final PlayersScore cardsScore;
    private final PlayersScore heartsScore;
    private final Player winningPlayer;
    private final Player startingPlayer;

    public FinishedDeal(Deal deal, PlayersScore cardsScore, PlayersScore heartsScore, Player winningPlayer, Player startingPlayer) {
        this.deal = deal;
        this.cardsScore = cardsScore;
        this.heartsScore = heartsScore;
        this.winningPlayer = winningPlayer;
        this.startingPlayer = startingPlayer;
    }

    public PlayersScore getCardsScore() {
        return cardsScore;
    }

    public Deal getDeal() {
        return deal;
    }

    public Player getWinningPlayer() {
        return winningPlayer;
    }

    public Card getWinningCard() {
        return getCard(getWinningPlayer());
    }

    public Card getCard(Player target) {
        int distance = startingPlayer.distanceTo(target);
        return deal.getCard(distance);
    }

    public Player getStartingPlayer() {
        return startingPlayer;
    }

    public Optional<Player> hasShotTheMoon() {
        return  heartsScore.getSouthScore().intValue() == 26 ? Optional.of(Player.SOUTH) :
                heartsScore.getEastScore().intValue() == 26 ? Optional.of(Player.EAST) :
                heartsScore.getNorthScore().intValue() == 26 ? Optional.of(Player.NORTH) :
                heartsScore.getWestScore().intValue() == 26 ? Optional.of(Player.WEST) :
                Optional.empty();
    }

    public PlayersScore getHeartsScore() {
        return heartsScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FinishedDeal)) return false;
        FinishedDeal that = (FinishedDeal) o;
        return Objects.equal(deal, that.deal) &&
                Objects.equal(cardsScore, that.cardsScore) &&
                Objects.equal(heartsScore, that.heartsScore) &&
                winningPlayer == that.winningPlayer &&
                startingPlayer == that.startingPlayer;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(deal, cardsScore, heartsScore, winningPlayer, startingPlayer);
    }

    @Override
    public String toString() {
        return "FinishedDeal{" +
                "deal=" + deal +
                ", cardsScore=" + cardsScore +
                ", heartsScore=" + heartsScore +
                ", winningPlayer=" + winningPlayer +
                ", startingPlayer=" + startingPlayer +
                '}';
    }
}
