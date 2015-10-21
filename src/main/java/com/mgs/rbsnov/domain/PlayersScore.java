package com.mgs.rbsnov.domain;

public class PlayersScore {
    private final int myScore;
    private final int eastScore;
    private final int northScore;
    private final int westScore;

    public PlayersScore(int myScore, int eastScore, int northScore, int westScore) {
        this.myScore = myScore;
        this.eastScore = eastScore;
        this.northScore = northScore;
        this.westScore = westScore;
    }
}