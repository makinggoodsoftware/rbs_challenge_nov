package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.Deal
import com.mgs.rbsnov.domain.DealScore
import com.mgs.rbsnov.domain.Player
import com.mgs.rbsnov.domain.PlayersScore
import spock.lang.Specification
import spock.lang.Unroll

class PlayersScorerSpecification extends Specification {
    FinishedDealScorer playersScorer
    DealScorer dealScorerMock = Mock(DealScorer)
    Deal dealMock = Mock (Deal)
    DealScore dealScoreMock = Mock (DealScore)

    def "setup"() {
        playersScorer = new FinishedDealScorer(dealScorerMock, cardScorer)

        dealScorerMock.score(dealMock) >> dealScoreMock
        dealScoreMock.points >> 1
        dealScoreMock.winningCardIndex >> 1
    }

    @Unroll ("should score correctly when the starting player is #startingPlayer")
    def "should score correctly a deal"() {
        when:
        PlayersScore score = playersScorer.score(startingPlayer, dealMock)

        then:
        score.eastScore == expectedEast
        score.southScore == expectedSouth
        score.westScore == expectedWest
        score.northScore == expectedNorth

        where:
        startingPlayer  | expectedEast  | expectedSouth | expectedWest  | expectedNorth
        Player.NORTH    | 1             | 0             | 0             | 0
        Player.EAST     | 0             | 1             | 0             | 0
        Player.SOUTH    | 0             | 0             | 1             | 0
        Player.WEST     | 0             | 0             | 0             | 1
    }

    def "should average players scores" (){
        when:
        PlayersScore averaged = playersScorer.average([
                new PlayersScore(0, 1, 0, 0),
                new PlayersScore(1, 0, 0, 0),
                new PlayersScore(0, 0, 1, 0),
                new PlayersScore(0, 0, 0, 1),
        ])

        then:
        averaged.southScore == 0.25f
        averaged.eastScore == 0.25f
        averaged.northScore == 0.25f
        averaged.westScore == 0.25f
    }
}
