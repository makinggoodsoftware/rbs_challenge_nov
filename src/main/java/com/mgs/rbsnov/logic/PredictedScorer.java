package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class PredictedScorer {
    private final FinishedDealScorer finishedDealScorer;

    public PredictedScorer(FinishedDealScorer finishedDealScorer) {
        this.finishedDealScorer = finishedDealScorer;
    }

    public PredictedScoring newScoring() {
        return new PredictedScoring();
    }

    public PlayersScore average(Collection<PlayersScore> scores) {
        PredictedScoring predictedScoring = newScoring();
        scores.forEach(predictedScoring::addScore);
        return predictedScoring.build();
    }

    class PredictedScoring {
        private List<PlayersScore> scores = new ArrayList<>();
        private List<PlayersScore> allChildScores = new ArrayList<>();

        public PredictedScoring addCombinedChildrenDealScores(Collection<PlayersScore> scores) {
            allChildScores.addAll(scores);
            return this;
        }

        public PredictedScoring addScore(PlayersScore score) {
            scores.add(score);
            return this;
        }

        public PlayersScore build() {
            if (scores.size() == 0 && allChildScores.size() == 0) throw new IllegalStateException();

            PlayersScore averagedScored = finishedDealScorer.average(this.scores);
            PlayersScore finalScore = averagedScored;
            if (allChildScores.size() > 0){
                PlayersScore predictedAverage = finishedDealScorer.average(allChildScores);
                finalScore = finishedDealScorer.add (averagedScored, predictedAverage);
            }
            return finalScore;
        }
    }
}
