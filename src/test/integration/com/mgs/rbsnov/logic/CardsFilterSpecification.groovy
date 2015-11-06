package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.Player
import com.mgs.rbsnov.spring.Config
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration(classes = Config)
class CardsFilterSpecification extends Specification {
    @Resource
    CardsFilter cardsFilter
    @Resource
    DealInProgressFactory dealInProgressFactory
    @Resource
    CardScorer cardScorer

    def "cleanup" (){
        cardScorer.removeTempScores()
    }

    def "discarding: should play biggest value of positive and zero points" (){
        when:
        Set<Card> bestCards = cardsFilter.bestCards(
                dealInProgressFactory.oneCardDeal(Player.SOUTH, Card.EIGHT_OF_CLUBS),
                [Card.EIGHT_OF_DIAMONDS, Card.EIGHT_OF_HEARTS, Card.TWO_OF_HEARTS] as Set
        )

        then:
        bestCards == [Card.EIGHT_OF_HEARTS, Card.EIGHT_OF_DIAMONDS] as Set
    }

    def "discarding: should get rid of the QS if not leading the deal" (){
        when:
        Set<Card> bestCards = cardsFilter.bestCards(
                dealInProgressFactory.oneCardDeal(Player.SOUTH, Card.EIGHT_OF_CLUBS),
                [Card.EIGHT_OF_DIAMONDS, Card.EIGHT_OF_HEARTS, Card.TWO_OF_HEARTS, Card.QUEEN_OF_SPADES] as Set
        )

        then:
        bestCards == [Card.QUEEN_OF_SPADES] as Set
    }

    def "discarding: should not give points away" () {
        given:
        cardScorer.addTempScore (Card.EIGHT_OF_DIAMONDS, -1)

        when:
        Set<Card> bestCards = cardsFilter.bestCards(
                dealInProgressFactory.oneCardDeal(Player.SOUTH, Card.EIGHT_OF_CLUBS),
                [Card.EIGHT_OF_DIAMONDS, Card.EIGHT_OF_HEARTS, Card.TWO_OF_HEARTS] as Set
        )

        then:
        bestCards == [Card.EIGHT_OF_HEARTS] as Set
    }
}
