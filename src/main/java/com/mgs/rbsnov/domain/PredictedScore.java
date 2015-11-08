package com.mgs.rbsnov.domain;

import com.google.common.base.Objects;

public class PredictedScore {
    private final PlayersScore averagedScore;

    public PredictedScore(PlayersScore averagedScore) {
        this.averagedScore = averagedScore;
    }

    public PlayersScore getAveragedScore() {
        return averagedScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PredictedScore)) return false;
        PredictedScore that = (PredictedScore) o;
        return Objects.equal(averagedScore, that.averagedScore);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(averagedScore);
    }

    @Override
    public String toString() {
        return "PredictedScore{" +
                "averagedScore=" + averagedScore +
                '}';
    }
}
