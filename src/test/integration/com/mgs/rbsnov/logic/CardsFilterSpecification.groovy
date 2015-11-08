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

    def "discarding: if forced should give lowest value minus point card" () {
        given:
        cardScorer.addTempScore (Card.EIGHT_OF_DIAMONDS, -1)
        cardScorer.addTempScore (Card.SIX_OF_DIAMONDS, -1)

        when:
        Set<Card> bestCards = cardsFilter.bestCards(
                dealInProgressFactory.oneCardDeal(Player.SOUTH, Card.EIGHT_OF_CLUBS),
                [Card.EIGHT_OF_DIAMONDS, Card.SIX_OF_DIAMONDS] as Set
        )

        then:
        bestCards == [Card.SIX_OF_DIAMONDS] as Set
    }

    def "following suit: if it can't kill should return highest value" (){
        when:
        Set<Card> bestCards = cardsFilter.bestCards(
                dealInProgressFactory.oneCardDeal(Player.SOUTH, Card.EIGHT_OF_CLUBS),
                [Card.SIX_OF_CLUBS, Card.FOUR_OF_CLUBS, Card.TWO_OF_HEARTS] as Set
        )

        then:
        bestCards == [Card.SIX_OF_CLUBS] as Set
    }

    def "following suit: should process the max killing one, the lowest killing one and the highest non killing one. IF NOT HEARTS" (){
        when:
        Set<Card> bestCards = cardsFilter.bestCards(
                dealInProgressFactory.oneCardDeal(Player.SOUTH, Card.FIVE_OF_CLUBS),
                [Card.ACE_OF_CLUBS, Card.JACK_OF_CLUBS, Card.SIX_OF_CLUBS, Card.FOUR_OF_CLUBS, Card.TWO_OF_CLUBS] as Set
        )

        then:
        bestCards == [Card.ACE_OF_CLUBS, Card.SIX_OF_CLUBS, Card.FOUR_OF_CLUBS] as Set
    }

    def "following suit: should process the max killing one, the lowest killing one. IF NOT HEARTS" (){
        when:
        Set<Card> bestCards = cardsFilter.bestCards(
                dealInProgressFactory.oneCardDeal(Player.SOUTH, Card.FIVE_OF_CLUBS),
                [Card.ACE_OF_CLUBS, Card.JACK_OF_CLUBS, Card.SIX_OF_CLUBS] as Set
        )

        then:
        bestCards == [Card.ACE_OF_CLUBS, Card.SIX_OF_CLUBS] as Set
    }

    def "following hearts: should throw the lowest non killing heart" (){
        when:
        Set<Card> bestCards = cardsFilter.bestCards(
                dealInProgressFactory.oneCardDeal(Player.SOUTH, Card.FIVE_OF_HEARTS),
                [Card.ACE_OF_HEARTS, Card.TWO_OF_HEARTS, Card.FOUR_OF_HEARTS] as Set
        )

        then:
        bestCards == [Card.FOUR_OF_HEARTS] as Set
    }

    def "leading deal: should only consider lowest HEART"(){
        when:
        Set<Card> bestCards = cardsFilter.bestCards(
                dealInProgressFactory.newJustStartedDeal(Player.SOUTH),
                [Card.ACE_OF_HEARTS, Card.TWO_OF_HEARTS, Card.FOUR_OF_HEARTS] as Set
        )

        then:
        bestCards == [Card.TWO_OF_HEARTS] as Set
    }

    def "leading deal: should only consider highest and lowest of non scoring SUITS"(){
        when:
        Set<Card> bestCards = cardsFilter.bestCards(
                dealInProgressFactory.newJustStartedDeal(Player.SOUTH),
                [Card.ACE_OF_DIAMONDS, Card.TWO_OF_DIAMONDS, Card.FOUR_OF_DIAMONDS] as Set
        )

        then:
        bestCards == [Card.ACE_OF_DIAMONDS, Card.FOUR_OF_DIAMONDS] as Set
    }

}
