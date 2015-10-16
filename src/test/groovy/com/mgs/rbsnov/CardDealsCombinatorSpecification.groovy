package com.mgs.rbsnov

import spock.lang.Specification

class CardDealsCombinatorSpecification extends Specification{
    CardDealsCombinator cardDealsCombinator

    def "setup" (){
        cardDealsCombinator = new CardDealsCombinator();
    }

    def "should explode simplest card combination" (){
        given:
        Hand hand1 = new Hand ([new Card(Suit.CLUBS, Numeration.ACE)] as Set);
        Hand hand2 = new Hand ([new Card(Suit.DIAMONDS, Numeration.ACE)] as Set);
        Hand hand3 = new Hand ([new Card(Suit.HEARTS, Numeration.ACE)] as Set);
        Hand hand4 = new Hand ([new Card(Suit.SPADES, Numeration.ACE)] as Set);

        when:
        List<Deal> deals = cardDealsCombinator.combine (hand1, hand2, hand3, hand4);

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
