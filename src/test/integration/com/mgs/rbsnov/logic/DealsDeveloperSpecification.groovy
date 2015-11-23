package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.*
import com.mgs.rbsnov.spring.Config
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

import static java.math.BigDecimal.ONE
import static java.math.BigDecimal.ZERO

@ContextConfiguration(classes = Config)
class DealsDeveloperSpecification extends Specification {
    @Resource
    DealsDeveloper dealsDeveloper
    @Resource
    DealInProgressFactory dealInProgressFactory

    def "should develop 1 deal when all cards have been played" (){
        when:
        Set<FinishedDeal> finishedDeals = dealsDeveloper.develop(
                dealInProgressFactory.fourCardsDeal(Player.NORTH, Card.ACE_OF_CLUBS, Card.TWO_OF_CLUBS, Card.FIVE_OF_HEARTS, Card.EIGHT_OF_CLUBS),
                new Hands ([] as Set, [] as Set, [] as Set, [] as Set)
        )

        then:
        finishedDeals == [
                new FinishedDeal(
                        new Deal(Card.ACE_OF_CLUBS, Card.TWO_OF_CLUBS, Card.FIVE_OF_HEARTS, Card.EIGHT_OF_CLUBS),
                        new PlayersScore(ZERO, ZERO, ONE, ZERO), heartsScore,
                        Player.NORTH, winningCard,
                        Player.NORTH
                )
        ] as Set
    }

    def "should develop the last playable cards from the last player" (){
        when:
        Set<FinishedDeal> finishedDeals = dealsDeveloper.develop(
                dealInProgressFactory.threeCardsDeal(Player.NORTH, Card.TEN_OF_CLUBS, Card.TWO_OF_CLUBS, Card.FIVE_OF_HEARTS),
                new Hands ([] as Set, [Card.EIGHT_OF_CLUBS] as Set, [] as Set, [] as Set)
        )

        then:
        finishedDeals == [
                new FinishedDeal(
                        new Deal(Card.TEN_OF_CLUBS, Card.TWO_OF_CLUBS, Card.FIVE_OF_HEARTS, Card.EIGHT_OF_CLUBS),
                        new PlayersScore(ZERO, ZERO, ONE, ZERO), heartsScore,
                        Player.NORTH, winningCard,
                        Player.NORTH
                )
        ] as Set
    }


    def "should develop the only playable cards from the last player" (){
        when:
        Set<FinishedDeal> finishedDeals = dealsDeveloper.develop(
                dealInProgressFactory.threeCardsDeal(Player.NORTH, Card.TEN_OF_CLUBS, Card.TWO_OF_CLUBS, Card.FIVE_OF_HEARTS),
                new Hands (
                        [] as Set,
                        [Card.EIGHT_OF_CLUBS, Card.ACE_OF_DIAMONDS] as Set,
                        [] as Set,
                        [] as Set
                )
        )

        then:
        finishedDeals == [
                new FinishedDeal(
                        new Deal(Card.TEN_OF_CLUBS, Card.TWO_OF_CLUBS, Card.FIVE_OF_HEARTS, Card.EIGHT_OF_CLUBS),
                        new PlayersScore(ZERO, ZERO, ONE, ZERO), heartsScore,
                        Player.NORTH, winningCard,
                        Player.NORTH
                )
        ] as Set
    }

    def "should develop the only playable cards from the last two player" (){
        when:
        Set<FinishedDeal> finishedDeals = dealsDeveloper.develop(
                dealInProgressFactory.twoCardsDeal(Player.NORTH, Card.TEN_OF_CLUBS, Card.TWO_OF_CLUBS),
                new Hands (
                        [Card.THREE_OF_CLUBS, Card.ACE_OF_CLUBS] as Set,
                        [Card.EIGHT_OF_CLUBS, Card.ACE_OF_DIAMONDS] as Set,
                        [] as Set,
                        [] as Set
                )
        )

        then:
        finishedDeals == [
                new FinishedDeal(
                        new Deal(Card.TEN_OF_CLUBS, Card.TWO_OF_CLUBS, Card.THREE_OF_CLUBS, Card.EIGHT_OF_CLUBS),
                        new PlayersScore(ZERO, ZERO, ZERO, ZERO), heartsScore,
                        Player.NORTH, winningCard,
                        Player.NORTH
                ),
                new FinishedDeal(
                        new Deal(Card.TEN_OF_CLUBS, Card.TWO_OF_CLUBS, Card.ACE_OF_CLUBS, Card.EIGHT_OF_CLUBS),
                        new PlayersScore(ZERO, ZERO, ZERO, ZERO), heartsScore,
                        Player.SOUTH, winningCard,
                        Player.NORTH
                )
        ] as Set
    }

}
