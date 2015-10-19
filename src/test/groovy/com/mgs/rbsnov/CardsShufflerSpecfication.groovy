package com.mgs.rbsnov

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.logic.CardsShuffler
import spock.lang.Specification

class CardsShufflerSpecfication extends Specification{
    CardsShuffler cardsShuffler

    def "setup" (){
        cardsShuffler = new CardsShuffler()
    }

    def "should shuffle cards" (){
        given:
        Set<Card> original = [
                Card.ACE_OF_CLUBS,
                Card.ACE_OF_DIAMONDS,
                Card.ACE_OF_HEARTS,
                Card.ACE_OF_SPADES,
                Card.KING_OF_CLUBS,
                Card.KING_OF_DIAMONDS,
                Card.KING_OF_HEARTS,
                Card.KING_OF_SPADES,
        ] as Set

        when:
        Set<Card> shuffled1 = cardsShuffler.shuffle (original)
        Set<Card> shuffled2 = cardsShuffler.shuffle (original)
        Set<Card> shuffled3 = cardsShuffler.shuffle (original)
        Set<Card> shuffled4 = cardsShuffler.shuffle (original)
        Set<Card> shuffled5 = cardsShuffler.shuffle (original)
        Set<Card> shuffled6 = cardsShuffler.shuffle (original)
        Set<Card> shuffled7 = cardsShuffler.shuffle (original)
        Set<Card> shuffled8 = cardsShuffler.shuffle (original)

        then:
        shuffled1.size() == 8

        shuffled1 != original
        shuffled1 != shuffled2
        shuffled2 != shuffled3
        shuffled3 != shuffled4
        shuffled4 != shuffled5
        shuffled5 != shuffled6
        shuffled6 != shuffled7
        shuffled7 != shuffled8
    }
}
