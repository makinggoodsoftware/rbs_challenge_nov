package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.*
import com.mgs.rbsnov.spring.Config
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration(classes = Config)
class GameAnalyserSpecification extends Specification {
    @Resource
    GameAnalyser gameAnalyser
    @Resource
    DealInProgressFactory dealInProgressFactory

    def "should score 2 cards hands" (){
        when:
        Map<Card, PlayersScore> predictedScore = gameAnalyser.analyse(new GameState(
                new Hands(
                        [Card.ACE_OF_HEARTS, Card.TWO_OF_HEARTS] as Set,
                        [Card.QUEEN_OF_HEARTS, Card.FIVE_OF_HEARTS] as Set,
                        [Card.JACK_OF_HEARTS, Card.FOUR_OF_CLUBS] as Set,
                        [Card.TWO_OF_SPADES, Card.TWO_OF_DIAMONDS] as Set
                ),
                dealInProgressFactory.newJustStartedDeal(Player.SOUTH)
        ), 2, PlayersScore.zeros())

        then:
        println(predictedScore)
        predictedScore[Card.TWO_OF_HEARTS].southScore == new BigDecimal("0.0000000000")
        predictedScore[Card.TWO_OF_HEARTS].eastScore == new BigDecimal("0.0000000000")
        predictedScore[Card.TWO_OF_HEARTS].northScore == new BigDecimal("5.0000000000")
        predictedScore[Card.TWO_OF_HEARTS].westScore == new BigDecimal("0.0000000000")
    }
//
//    def "should score 3 cards hands" (){
//        when:
//        Map<Card, PlayersScore> predictedScore = gameAnalyser.analyse(new GameState(
//                new Hands(
//                        [Card.ACE_OF_HEARTS, Card.TWO_OF_HEARTS, Card.ACE_OF_SPADES] as Set,
//                        [Card.QUEEN_OF_HEARTS, Card.FIVE_OF_HEARTS, Card.FIVE_OF_CLUBS] as Set,
//                        [Card.JACK_OF_HEARTS, Card.FOUR_OF_CLUBS, Card.FOUR_OF_DIAMONDS] as Set,
//                        [Card.TWO_OF_SPADES, Card.TWO_OF_DIAMONDS, Card.SIX_OF_HEARTS] as Set
//                ),
//                dealInProgressFactory.newJustStartedDeal(Player.SOUTH)
//        ), 3, heartsScore)
//
//        then:
//        predictedScore[Card.ACE_OF_SPADES].getSouthScore() > predictedScore[Card.TWO_OF_HEARTS].getSouthScore()
//    }
//
//    def "should score 5 cards hands" (){
//        when:
//        Map<Card, PlayersScore> predictedScore = gameAnalyser.analyse(new GameState(
//                new Hands(
//                        [Card.ACE_OF_HEARTS, Card.TWO_OF_HEARTS, Card.ACE_OF_SPADES, Card.FOUR_OF_SPADES, Card.JACK_OF_CLUBS] as Set,
//                        [Card.QUEEN_OF_HEARTS, Card.FIVE_OF_HEARTS, Card.FIVE_OF_CLUBS, Card.SEVEN_OF_CLUBS, Card.SEVEN_OF_HEARTS] as Set,
//                        [Card.JACK_OF_HEARTS, Card.FOUR_OF_CLUBS, Card.FOUR_OF_DIAMONDS, Card.FIVE_OF_DIAMONDS, Card.EIGHT_OF_HEARTS] as Set,
//                        [Card.TWO_OF_SPADES, Card.TWO_OF_DIAMONDS, Card.SIX_OF_HEARTS, Card.NINE_OF_HEARTS, Card.EIGHT_OF_CLUBS] as Set
//                ),
//                dealInProgressFactory.newJustStartedDeal(Player.SOUTH),
//        ), 3, heartsScore)
//
//        then:
//        predictedScore[Card.JACK_OF_CLUBS].getSouthScore() > predictedScore[Card.TWO_OF_HEARTS].getSouthScore()
//    }
//
//    def "should score 6 cards hands" (){
//        when:
//        Map<Card, PlayersScore> predictedScore = gameAnalyser.analyse(new GameState(
//                new Hands(
//                        [Card.ACE_OF_HEARTS, Card.TWO_OF_HEARTS, Card.ACE_OF_SPADES, Card.FOUR_OF_SPADES, Card.JACK_OF_CLUBS, Card.THREE_OF_CLUBS] as Set,
//                        [Card.QUEEN_OF_HEARTS, Card.FIVE_OF_HEARTS, Card.FIVE_OF_CLUBS, Card.SEVEN_OF_CLUBS, Card.SEVEN_OF_HEARTS, Card.THREE_OF_SPADES] as Set,
//                        [Card.JACK_OF_HEARTS, Card.FOUR_OF_CLUBS, Card.FOUR_OF_DIAMONDS, Card.FIVE_OF_DIAMONDS, Card.EIGHT_OF_HEARTS, Card.THREE_OF_HEARTS] as Set,
//                        [Card.TWO_OF_SPADES, Card.TWO_OF_DIAMONDS, Card.SIX_OF_HEARTS, Card.NINE_OF_HEARTS, Card.EIGHT_OF_CLUBS, Card.THREE_OF_DIAMONDS] as Set
//                ),
//                dealInProgressFactory.newJustStartedDeal(Player.SOUTH)
//        ), 3, heartsScore)
//
//
//        then:
//        predictedScore[Card.TWO_OF_HEARTS].getSouthScore() > predictedScore[Card.FOUR_OF_SPADES].getSouthScore()
//    }
//
//    def "should score 7 cards hands" (){
//        when:
//        Map<Card, PlayersScore> predictedScore = gameAnalyser.analyse(new GameState(
//                new Hands(
//                        [Card.ACE_OF_HEARTS, Card.TWO_OF_HEARTS, Card.ACE_OF_SPADES, Card.FOUR_OF_SPADES, Card.JACK_OF_CLUBS, Card.THREE_OF_CLUBS, Card.FIVE_OF_SPADES] as Set,
//                        [Card.QUEEN_OF_HEARTS, Card.FIVE_OF_HEARTS, Card.FIVE_OF_CLUBS, Card.SEVEN_OF_CLUBS, Card.SEVEN_OF_HEARTS, Card.THREE_OF_SPADES, Card.QUEEN_OF_SPADES] as Set,
//                        [Card.JACK_OF_HEARTS, Card.FOUR_OF_CLUBS, Card.FOUR_OF_DIAMONDS, Card.FIVE_OF_DIAMONDS, Card.EIGHT_OF_HEARTS, Card.THREE_OF_HEARTS, Card.QUEEN_OF_DIAMONDS] as Set,
//                        [Card.TWO_OF_SPADES, Card.TWO_OF_DIAMONDS, Card.SIX_OF_HEARTS, Card.NINE_OF_HEARTS, Card.EIGHT_OF_CLUBS, Card.THREE_OF_DIAMONDS, Card.SIX_OF_CLUBS] as Set
//                ),
//                dealInProgressFactory.newJustStartedDeal(Player.SOUTH)
//        ), 3, heartsScore)
//
//
//        then:
//        predictedScore[Card.TWO_OF_HEARTS].getSouthScore() > predictedScore[Card.FOUR_OF_SPADES].getSouthScore()
//    }
//
//    def "should score 8 cards hands" (){
//        when:
//        Map<Card, PlayersScore> predictedScore = gameAnalyser.analyse(new GameState(
//                new Hands(
//                        [Card.ACE_OF_HEARTS, Card.TWO_OF_HEARTS, Card.ACE_OF_SPADES, Card.FOUR_OF_SPADES, Card.JACK_OF_CLUBS, Card.THREE_OF_CLUBS, Card.FIVE_OF_SPADES, Card.ACE_OF_DIAMONDS] as Set,
//                        [Card.QUEEN_OF_HEARTS, Card.FIVE_OF_HEARTS, Card.FIVE_OF_CLUBS, Card.SEVEN_OF_CLUBS, Card.SEVEN_OF_HEARTS, Card.THREE_OF_SPADES, Card.QUEEN_OF_SPADES, Card.NINE_OF_CLUBS] as Set,
//                        [Card.JACK_OF_HEARTS, Card.FOUR_OF_CLUBS, Card.FOUR_OF_DIAMONDS, Card.FIVE_OF_DIAMONDS, Card.EIGHT_OF_HEARTS, Card.THREE_OF_HEARTS, Card.QUEEN_OF_DIAMONDS, Card.NINE_OF_SPADES] as Set,
//                        [Card.TWO_OF_SPADES, Card.TWO_OF_DIAMONDS, Card.SIX_OF_HEARTS, Card.NINE_OF_HEARTS, Card.EIGHT_OF_CLUBS, Card.THREE_OF_DIAMONDS, Card.SIX_OF_CLUBS, Card.KING_OF_CLUBS] as Set
//                ),
//                dealInProgressFactory.newJustStartedDeal(Player.SOUTH)
//        ), 3, heartsScore)
//
//
//        then:
//        predictedScore[Card.TWO_OF_HEARTS].getSouthScore() > predictedScore[Card.FOUR_OF_SPADES].getSouthScore()
//    }




}
