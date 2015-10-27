package com.mgs.rbsnov.domain;

public class PredictedScore {

    private final PlayersScore playersScore;

    public PredictedScore(PlayersScore playersScore) {
        this.playersScore = playersScore;
    }

    public int getMyScore() {
        return playersScore.getMyScore();
    }

    public int getEastScore() {
        return playersScore.getEastScore();
    }

    public int getNorthScore() {
        return playersScore.getNorthScore();
    }

    public int getWestScore() {
        return playersScore.getWestScore();
    }
}
