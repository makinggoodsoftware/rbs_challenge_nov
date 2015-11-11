package com.mgs.rbsnov.domain;

import java.util.List;

public class GameResult {
    private List<RoundResult> roundResults;

    public GameResult(List<RoundResult> roundResults) {
        this.roundResults = roundResults;
    }

    public List<RoundResult> getRoundResults() {
        return roundResults;
    }
}
