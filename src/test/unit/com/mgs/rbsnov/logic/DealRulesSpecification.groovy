package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.Card
import spock.lang.Specification

class DealRulesSpecification extends Specification {
    DealRules dealRules = new DealRules()

    def "should allow the card when is of the same suit" (){
        expect:
        dealRules.canFollow(Card.ACE_OF_HEARTS, Card.TWO_OF_HEARTS, [Card.FOUR_OF_DIAMONDS, Card.TEN_OF_DIAMONDS] as Set)
    }

    def "should allow any card if cant play same suit" (){
        expect:
        dealRules.canFollow(Card.ACE_OF_HEARTS, Card.TWO_OF_SPADES, [Card.FOUR_OF_DIAMONDS, Card.TEN_OF_DIAMONDS] as Set)
    }

    def "should deny different suit if can play same suit" (){
        expect:
        ! dealRules.canFollow(Card.ACE_OF_HEARTS, Card.TWO_OF_SPADES, [Card.FOUR_OF_HEARTS, Card.TEN_OF_DIAMONDS] as Set)
    }

}
