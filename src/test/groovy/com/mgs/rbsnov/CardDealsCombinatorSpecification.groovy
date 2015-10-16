package com.mgs.rbsnov

import spock.lang.Specification

class CardDealsCombinatorSpecification extends Specification{
    CardDealsCombinator cardDealsCombinator

    def "setup" (){
        cardDealsCombinator = new CardDealsCombinator();
    }

    def "should explode card combinations" (){
        given:
        Set<Card> inGameCards = [
                new Card(Suit.CLUBS, Numeration.ACE),
                new Card(Suit.DIAMONDS, Numeration.ACE),
                new Card(Suit.HEARTS, Numeration.ACE),
                new Card(Suit.SPADES, Numeration.ACE)
        ]

        when:
        List<Deal> deals = cardDealsCombinator.combine (inGameCards);

        then:
        deals == [
                new Deal(
                    new Card(Suit.CLUBS, Numeration.ACE),
                    new Card(Suit.DIAMONDS, Numeration.ACE),
                    new Card(Suit.HEARTS, Numeration.ACE),
                    new Card(Suit.SPADES, Numeration.ACE)
                ),
        ]
    }
}
