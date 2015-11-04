package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.*
import com.mgs.rbsnov.spring.Config
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration(classes = Config)
class GameAnalyserIISpecification extends Specification {
    @Resource
    GameAnalyserII gameAnalyserII
    @Resource
    DealInProgressFactory dealInProgressFactory

    def "should score cards" (){
        when:
        Map<Card, PredictedScore> predictedScore = gameAnalyserII.analyse(new GameState(
                new Hands(
                        [Card.ACE_OF_HEARTS, Card.TWO_OF_HEARTS] as Set,
                        [Card.QUEEN_OF_HEARTS, Card.FIVE_OF_HEARTS] as Set,
                        [Card.JACK_OF_HEARTS, Card.FOUR_OF_CLUBS] as Set,
                        [Card.TWO_OF_SPADES, Card.TWO_OF_DIAMONDS] as Set
                ),
                dealInProgressFactory.newJustStartedDeal(Player.SOUTH)
        ), Player.SOUTH)

        then:
        predictedScore.size() == 2
    }
}
