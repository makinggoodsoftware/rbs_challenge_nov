package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.PlayersScore
import com.mgs.rbsnov.domain.PredictedScore
import com.mgs.rbsnov.spring.Config
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration(classes = Config)
class PredictedScorerSpecification extends Specification{
    @Resource
    PredictedScorer predictedScorer

    def "should add final node scores" () {
        when:
        PredictedScore predictedScore = predictedScorer.newScoring().
                addScore(new PlayersScore(1, 0, 0, 1)).
                addScore(new PlayersScore(0, 0, 0, 2)).
                addScore(new PlayersScore(0, 1, 0, 1)).
                addScore(new PlayersScore(0, 2, 0, 0)).
                build()

        then:
        predictedScore.averagedScore.southScore == 0.25
        predictedScore.averagedScore.eastScore == 0.75
        predictedScore.averagedScore.northScore == 0.0
        predictedScore.averagedScore.westScore == 1.0
    }

    def "should add final and predicted scores" () {
        when:
        PredictedScore predictedScore = predictedScorer.newScoring().
                addScore(new PlayersScore(1, 0, 0, 1)).
                addCombinedChildrenDealScores([
                        new PredictedScore(
                                new PlayersScore(0, 0, 0, 1)
                        ),
                        new PredictedScore(
                                new PlayersScore(0, 1, 0, 0)
                        )
                ]).
                build()

        then:
        predictedScore.averagedScore.southScore == 1.0
        predictedScore.averagedScore.eastScore == 0.5
        predictedScore.averagedScore.northScore == 0.0
        predictedScore.averagedScore.westScore == 1.5
    }
}
