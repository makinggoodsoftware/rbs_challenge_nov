package com.mgs.rbsnov.domain;

public class PredictedScore {
    private final PlayersScore averagedScore;


    public PredictedScore(PlayersScore averagedScore) {
        this.averagedScore = averagedScore;
    }

    public PlayersScore getAveragedScore() {
        return averagedScore;
    }
}
