package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.Card
import spock.lang.Specification

class CardsSetBuilderSpecification extends Specification {
    CardsSetBuilder cardsSetBuilder = new CardsSetBuilder()

    def "should build a set" (){
        when:
        Set<Card> newCards = cardsSetBuilder.
                newSet([
                        Card.ACE_OF_SPADES,
                        Card.ACE_OF_DIAMONDS,
                        Card.ACE_OF_HEARTS
                ] as Set).
                remove(Card.ACE_OF_SPADES).
                remove(Card.ACE_OF_DIAMONDS).
                build()

        then:
        newCards == [Card.ACE_OF_HEARTS] as Set

    }

    def "should create a set containing all cards" (){
        expect:
        cardsSetBuilder.allCards().size() == 52
    }

}
