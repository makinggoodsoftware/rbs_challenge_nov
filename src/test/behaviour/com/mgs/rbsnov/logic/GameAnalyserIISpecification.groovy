package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.DealInProgress
import com.mgs.rbsnov.domain.GameState
import com.mgs.rbsnov.domain.Player
import com.mgs.rbsnov.domain.PredictedScore
import com.mgs.rbsnov.spring.Config
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource


@ContextConfiguration(classes = Config)
class GameAnalyserIISpecification extends Specification {
    @Resource
    GameAnalyserII gameAnalyserII

    def "should score cards" (){
        when:
        Map<Card, PredictedScore> predictedScore = gameAnalyserII.analyse(new GameState(hands
                ,
                new DealInProgress(leadingCard, [Card.TEN_OF_DIAMONDS, Card.ACE_OF_DIAMONDS] as Set, startginPlayer, Player.NORTH)
        ))

        then:
        predictedScore.size() == 2
    }
}
