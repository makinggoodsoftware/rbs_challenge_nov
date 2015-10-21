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
        public void addIntermediateDealScore(Deal deal, Map<Card, PredictedScore> childDealsScore) {

        }

        public void addFinalDealScore(Deal deal) {

        }

        public PredictedScore build() {
            return null;
        }
    }
}
