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

    def "setup" (){
        println "Warming up"
    }

    def "should score 2 cards hands" (){
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
        println(predictedScore)
        predictedScore[Card.TWO_OF_HEARTS].averagedScore.southScore == new BigDecimal("0.0000000000")
        predictedScore[Card.TWO_OF_HEARTS].averagedScore.eastScore == new BigDecimal("0.0000000000")
        predictedScore[Card.TWO_OF_HEARTS].averagedScore.northScore == new BigDecimal("5.0000000000")
        predictedScore[Card.TWO_OF_HEARTS].averagedScore.westScore == new BigDecimal("0.0000000000")

    }

    def "should score 3 cards hands" (){
        when:
        Map<Card, PredictedScore> predictedScore = gameAnalyserII.analyse(new GameState(
                new Hands(
                        [Card.ACE_OF_HEARTS, Card.TWO_OF_HEARTS, Card.ACE_OF_SPADES] as Set,
                        [Card.QUEEN_OF_HEARTS, Card.FIVE_OF_HEARTS, Card.FIVE_OF_CLUBS] as Set,
                        [Card.JACK_OF_HEARTS, Card.FOUR_OF_CLUBS, Card.FOUR_OF_DIAMONDS] as Set,
                        [Card.TWO_OF_SPADES, Card.TWO_OF_DIAMONDS, Card.SIX_OF_HEARTS] as Set
                ),
                dealInProgressFactory.newJustStartedDeal(Player.SOUTH)
        ), Player.SOUTH)

        then:
        predictedScore[Card.ACE_OF_SPADES].averagedScore.getSouthScore() > predictedScore[Card.TWO_OF_HEARTS].averagedScore.getSouthScore()
    }

    def "should score 5 cards hands" (){
        when:
        Map<Card, PredictedScore> predictedScore = gameAnalyserII.analyse(new GameState(
                new Hands(
                        [Card.ACE_OF_HEARTS, Card.TWO_OF_HEARTS, Card.ACE_OF_SPADES, Card.FOUR_OF_SPADES, Card.JACK_OF_CLUBS] as Set,
                        [Card.QUEEN_OF_HEARTS, Card.FIVE_OF_HEARTS, Card.FIVE_OF_CLUBS, Card.SEVEN_OF_CLUBS, Card.SEVEN_OF_HEARTS] as Set,
                        [Card.JACK_OF_HEARTS, Card.FOUR_OF_CLUBS, Card.FOUR_OF_DIAMONDS, Card.FIVE_OF_DIAMONDS, Card.EIGHT_OF_HEARTS] as Set,
                        [Card.TWO_OF_SPADES, Card.TWO_OF_DIAMONDS, Card.SIX_OF_HEARTS, Card.NINE_OF_HEARTS, Card.EIGHT_OF_CLUBS] as Set
                ),
                dealInProgressFactory.newJustStartedDeal(Player.SOUTH)
        ), Player.SOUTH)

        then:
        predictedScore[Card.ACE_OF_SPADES].averagedScore.getSouthScore() > predictedScore[Card.TWO_OF_HEARTS].averagedScore.getSouthScore()
    }

    def "should score 6 cards hands" (){
        when:
        Map<Card, PredictedScore> predictedScore = gameAnalyserII.analyse(new GameState(
                new Hands(
                        [Card.ACE_OF_HEARTS, Card.TWO_OF_HEARTS, Card.ACE_OF_SPADES, Card.FOUR_OF_SPADES, Card.JACK_OF_CLUBS, Card.THREE_OF_CLUBS] as Set,
                        [Card.QUEEN_OF_HEARTS, Card.FIVE_OF_HEARTS, Card.FIVE_OF_CLUBS, Card.SEVEN_OF_CLUBS, Card.SEVEN_OF_HEARTS, Card.THREE_OF_SPADES] as Set,
                        [Card.JACK_OF_HEARTS, Card.FOUR_OF_CLUBS, Card.FOUR_OF_DIAMONDS, Card.FIVE_OF_DIAMONDS, Card.EIGHT_OF_HEARTS, Card.THREE_OF_HEARTS] as Set,
                        [Card.TWO_OF_SPADES, Card.TWO_OF_DIAMONDS, Card.SIX_OF_HEARTS, Card.NINE_OF_HEARTS, Card.EIGHT_OF_CLUBS, Card.THREE_OF_DIAMONDS] as Set
                ),
                dealInProgressFactory.newJustStartedDeal(Player.SOUTH)
        ), Player.SOUTH)


        then:
        predictedScore[Card.TWO_OF_HEARTS].averagedScore.getSouthScore() > predictedScore[Card.ACE_OF_SPADES].averagedScore.getSouthScore()
    }


}
