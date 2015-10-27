package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.Deal;
import com.mgs.rbsnov.domain.PredictedScore;

import java.util.Map;

public class PredictedScorer {
    public PredictedScoring newScoring() {
        return null;
    }

    class PredictedScoring {
        public PredictedScoring addIntermediateDealScore(Deal deal, Map<Card, PredictedScore> childDealsScore) {
            return this;
        }

        public PredictedScoring addFinalDealScore(Deal deal) {
            return this;
        }

        public PredictedScore build() {
            return null;
        }
    }
}
