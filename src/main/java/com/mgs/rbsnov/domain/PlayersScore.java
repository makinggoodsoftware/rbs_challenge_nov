package com.mgs.rbsnov.domain;

import com.google.common.base.Objects;

import java.math.BigDecimal;

public class PlayersScore {
    private final BigDecimal southScore;
    private final BigDecimal eastScore;
    private final BigDecimal northScore;
    private final BigDecimal westScore;

    public static PlayersScore zeros() {
        return new PlayersScore(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );
    }

    public PlayersScore(BigDecimal southScore, BigDecimal eastScore, BigDecimal northScore, BigDecimal westScore) {
        this.southScore = southScore;
        this.eastScore = eastScore;
        this.northScore = northScore;
        this.westScore = westScore;
    }

    public static PlayersScore shootTheMoon(Player player) {
        return new PlayersScore(
                player == Player.SOUTH ? BigDecimal.ZERO : new BigDecimal("26.00000"),
                player == Player.EAST ? BigDecimal.ZERO : new BigDecimal("26.00000"),
                player == Player.NORTH ? BigDecimal.ZERO : new BigDecimal("26.00000"),
                player == Player.WEST ? BigDecimal.ZERO : new BigDecimal("26.00000")
        );
    }

    public BigDecimal getSouthScore() {
        return southScore;
    }

    public BigDecimal getEastScore() {
        return eastScore;
    }


    public BigDecimal getNorthScore() {
        return northScore;
    }

    public BigDecimal getWestScore() {
        return westScore;
    }

    public BigDecimal get(Player forPlayer) {
        switch (forPlayer){
            case EAST:
                return getEastScore();
            case NORTH:
                return getNorthScore();
            case WEST:
                return getWestScore();
            case SOUTH:
                return getSouthScore();
        }
        throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayersScore)) return false;
        PlayersScore that = (PlayersScore) o;
        return Objects.equal(southScore, that.southScore) &&
                Objects.equal(eastScore, that.eastScore) &&
                Objects.equal(northScore, that.northScore) &&
                Objects.equal(westScore, that.westScore);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(southScore, eastScore, northScore, westScore);
    }

    @Override
    public String toString() {
        return "PlayersScore{" +
                "southScore=" + southScore +
                ", eastScore=" + eastScore +
                ", northScore=" + northScore +
                ", westScore=" + westScore +
                '}';
    }

    public boolean anyHigherThan(int maxValue) {
        return getSouthScore().intValue() > maxValue || getEastScore().intValue() > maxValue|| getNorthScore().intValue() > maxValue || getWestScore().intValue() > maxValue;
    }
}
