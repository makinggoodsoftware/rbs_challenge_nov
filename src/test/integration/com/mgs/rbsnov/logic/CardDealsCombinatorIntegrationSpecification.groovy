package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.Deal
import com.mgs.rbsnov.spring.Config
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration(classes = Config)
class CardDealsCombinatorIntegrationSpecification extends Specification {
    @Resource
    CardDealsCombinator cardDealsCombinator

    def "should combine cards" (){
        given:
        Set<Card> firstHand = [Card.ACE_OF_CLUBS, Card.TWO_OF_CLUBS]
        Set<Card> secondHand = [Card.FOUR_OF_CLUBS, Card.ACE_OF_HEARTS]
        Set<Card> thirdHand = [Card.FIVE_OF_CLUBS, Card.SIX_OF_CLUBS]
        Set<Card> fourthHand = [Card.EIGHT_OF_DIAMONDS, Card.SIX_OF_SPADES]

        when:
        Map<Card, Set<Deal>> deals = cardDealsCombinator.combine(firstHand, secondHand, thirdHand, fourthHand)

        then:
        deals.size() > 0
    }
}
