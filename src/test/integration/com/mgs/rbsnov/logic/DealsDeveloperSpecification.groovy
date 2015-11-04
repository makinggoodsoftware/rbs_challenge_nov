package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.Deal
import com.mgs.rbsnov.domain.DealInProgress
import com.mgs.rbsnov.domain.FinishedDeal
import com.mgs.rbsnov.domain.Hands
import com.mgs.rbsnov.domain.Player
import com.mgs.rbsnov.domain.PlayersScore
import com.mgs.rbsnov.spring.Config
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

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
                        new PlayersScore(0, 0, 1, 0),
                        Player.NORTH
                )
        ] as Set
    }

    def "should develop all playable cards from the last player" (){
        when:
        Set<FinishedDeal> finishedDeals = dealsDeveloper.develop(
                dealInProgressFactory.threeCardsDeal(Player.NORTH, Card.TEN_OF_CLUBS, Card.TWO_OF_CLUBS, Card.FIVE_OF_HEARTS),
                new Hands ([] as Set, [] as Set, [] as Set, [] as Set)
        )

        then:
        finishedDeals == [
                new FinishedDeal(
                        new Deal(Card.ACE_OF_CLUBS, Card.TWO_OF_CLUBS, Card.FIVE_OF_HEARTS, Card.EIGHT_OF_CLUBS),
                        new PlayersScore(0, 0, 1, 0),
                        Player.NORTH
                )
        ] as Set
    }

}
