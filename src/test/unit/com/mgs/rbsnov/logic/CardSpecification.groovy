package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.Card
import spock.lang.Specification

class CardSpecification extends Specification{
    def "should check that there are 53 different cards from A to K of 4 different clubs, with no dups" (){
        expect:
        ['JACK', 'QUEEN', 'KING', 'ACE', 'TWO', 'THREE', 'FOUR', 'FIVE', 'SIX', 'SEVEN', 'EIGHT', 'NINE', 'TEN'].each { numerationStr ->
            ['HEARTS', 'SPADES', 'CLUBS', 'DIAMONDS'].each { suitStr ->
                String enumName = numerationStr + "_OF_" + suitStr
                Card returned = Card.valueOf(enumName)
                assert returned.getNumeration().toString() == numerationStr
                assert returned.getSuit().toString() == suitStr
            }
        }

        Card.values().length == 52
    }
}
