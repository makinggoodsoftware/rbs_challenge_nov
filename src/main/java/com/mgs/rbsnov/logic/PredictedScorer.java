package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
        private List<PlayersScore> allChildScores = new ArrayList<>();

        public PredictedScoring addCombinedChildrenDealScores(Collection<PredictedScore> scores) {
            allChildScores.addAll(scores.stream().map(PredictedScore::getAveragedScore).collect(toList()));
            return this;
        }

        public PredictedScoring addScore(PlayersScore score) {
            scores.add(score);
            return this;
        }

        public PredictedScore build() {
            if (scores.size() == 0 && allChildScores.size() == 0) throw new IllegalStateException();

            PlayersScore averagedScored = playersScorer.average(this.scores);
            PlayersScore finalScore = averagedScored;
            if (allChildScores.size() > 0){
                PlayersScore predictedAverage = playersScorer.average(allChildScores);
                finalScore = playersScorer.add (averagedScored, predictedAverage);
            }
            return new PredictedScore(finalScore);
        }
    }
}
