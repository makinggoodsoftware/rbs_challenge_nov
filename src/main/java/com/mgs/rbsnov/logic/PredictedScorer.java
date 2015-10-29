package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PredictedScorer {
    private final PlayersScorer playersScorer;

    public PredictedScorer(PlayersScorer playersScorer) {
        this.playersScorer = playersScorer;
    }

    public PredictedScoring newScoring() {
        return new PredictedScoring();
    }

    class PredictedScoring {
        private List<PlayersScore> scores = new ArrayList<>();

        public PredictedScoring addPredictedScores(Collection<PredictedScore> scores) {
            return this;
        }

        public PredictedScoring addScore(PlayersScore score) {
            scores.add(score);
            return this;
        }

        public PredictedScore build() {
            PlayersScore averagedScored = playersScorer.average(this.scores);
            return new PredictedScore(averagedScored);
        }
    }
}
