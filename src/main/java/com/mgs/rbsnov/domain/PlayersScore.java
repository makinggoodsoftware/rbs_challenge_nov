package com.mgs.rbsnov.domain;

import com.google.common.base.Objects;

public class PlayersScore {
    private final float southScore;
    private final float eastScore;
    private final float northScore;
    private final float westScore;

    public PlayersScore(float southScore, float eastScore, float northScore, float westScore) {
        this.southScore = southScore;
        this.eastScore = eastScore;
        this.northScore = northScore;
        this.westScore = westScore;
    }

    public float getSouthScore() {
        return southScore;
    }

    public float getEastScore() {
        return eastScore;
    }

    public float getNorthScore() {
        return northScore;
    }

    public float getWestScore() {
        return westScore;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayersScore)) return false;
        PlayersScore that = (PlayersScore) o;
        return Float.compare(that.southScore, southScore) == 0 &&
                Float.compare(that.eastScore, eastScore) == 0 &&
                Float.compare(that.northScore, northScore) == 0 &&
                Float.compare(that.westScore, westScore) == 0;
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
}
