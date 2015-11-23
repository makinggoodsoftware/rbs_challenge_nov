package com.mgs.rbsnov.domain;

public class GameScore {
    private final PlayersScore playersScore;
    private final PlayersScore heartsScore;

    public static GameScore zeros() {
        return new GameScore(PlayersScore.zeros(), PlayersScore.zeros());
    }

    public GameScore(PlayersScore playersScore, PlayersScore heartsScore) {
        this.playersScore = playersScore;
        this.heartsScore = heartsScore;
    }

    public PlayersScore getPlayersScore() {
        return playersScore;
    }

    public PlayersScore getHeartsScore() {
        return heartsScore;
    }
}
