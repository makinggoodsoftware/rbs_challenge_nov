package com.mgs.rbsnov

import com.mgs.rbsnov.domain.PlayersScore
import com.mgs.rbsnov.domain.PredictedScore
import com.mgs.rbsnov.logic.PredictedScorer
import spock.lang.Specification

class PredictedScorerSpecification extends Specification{
    PredictedScorer predictedScorer

    def "setup" () {
        predictedScorer = new PredictedScorer(playersScorer)
    }

    def "should add final node score" () {
        when:
        PredictedScore predictedScore = predictedScorer.newScoring().
                addScore(new PlayersScore(1, 0, 0, 1)).
                addScore(new PlayersScore(0, 0, 0, 2)).
                addScore(new PlayersScore(0, 1, 0, 1)).
                addScore(new PlayersScore(0, 2, 0, 0)).
                build()

        then:
        predictedScore.averagedScore.southScore == 0.25f
        predictedScore.averagedScore.eastScore == 0.75f
        predictedScore.averagedScore.northScore == 0f
        predictedScore.averagedScore.westScore == 1.0f
    }
}
