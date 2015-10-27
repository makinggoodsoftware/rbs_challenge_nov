package com.mgs.rbsnov

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.Deal
import com.mgs.rbsnov.domain.PredictedScore
import com.mgs.rbsnov.logic.PredictedScorer
import spock.lang.Specification

class PredictedScorerSpecification extends Specification{
    PredictedScorer predictedScorer

    def "setup" () {
        predictedScorer = new PredictedScorer()
    }


    def "should add final node score" () {
        when:
        PredictedScore predictedScore = predictedScorer.newScoring().addFinalDealScore(new Deal(
                Card.ACE_OF_CLUBS,
                Card.FIVE_OF_HEARTS,
                Card.EIGHT_OF_CLUBS,
                Card.FOUR_OF_CLUBS
        )).build();

        then:
        predictedScore.myScore == 1
        predictedScore.eastScore == 0
        predictedScore.northScore == 0
        predictedScore.westScore == 0
    }
}
