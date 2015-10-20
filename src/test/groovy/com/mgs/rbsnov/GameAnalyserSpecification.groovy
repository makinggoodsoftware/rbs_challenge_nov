package com.mgs.rbsnov

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.Numeration
import com.mgs.rbsnov.domain.Suit
import com.mgs.rbsnov.logic.GameAnalyser
import spock.lang.Specification

import static com.mgs.rbsnov.domain.Card.from

class GameAnalyserSpecification extends Specification{
    GameAnalyser bestPlayDeveloper

    def "setup" (){
        bestPlayDeveloper = new GameAnalyser(personsSscorer, cards, cardsDealer, cardDealsCombinator, dealScorer)
    }

    def "should develop plays" () {
        given:
        Set<Card> inPlayCards = [
                from(Suit.SPADES, Numeration.ACE),
                from(Suit.CLUBS, Numeration.ACE),
                from(Suit.DIAMONDS, Numeration.ACE),
                from(Suit.HEARTS, Numeration.ACE),
                from(Suit.SPADES, Numeration.TWO),
                from(Suit.CLUBS, Numeration.TWO),
                from(Suit.DIAMONDS, Numeration.TWO),
                from(Suit.HEARTS, Numeration.TWO),
        ] as Set

        Set<Card> thisHand = [
                from(Suit.HEARTS, Numeration.ACE),
                from(Suit.SPADES, Numeration.TWO),
        ] as Set

        when:
        def stats = bestPlayDeveloper.analyse (thisHand, inPlayCards)

        then:
        stats.size() == 2
        stats[0].cards == [
            from(Suit.SPADES, Numeration.TWO)
        ]
        stats[1].cards == [
            from(Suit.HEARTS, Numeration.ACE)
        ]
    }
}
