package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.Collection;
import java.util.Map;

public class PredictedScorer {
    private final PlayersScorer playersScorer;

    public PredictedScorer(PlayersScorer playersScorer) {
        this.playersScorer = playersScorer;
    }

    public PredictedScoring newScoring() {
        return new PredictedScoring();
    }

    class PredictedScoring {
        private PlayersScore playersScore = new PlayersScore(0, 0, 0, 0);

        public PredictedScoring addPredictedScores(Collection<PredictedScore> scores) {
            return this;
        }

        public PredictedScoring addScore(PlayersScore score) {
            playersScorer.add(score);
            return this;
        }

        public PredictedScore build() {
            return new PredictedScore(playersScore);
        }
    }
}
