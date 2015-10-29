package com.mgs.rbsnov.domain;

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
}
