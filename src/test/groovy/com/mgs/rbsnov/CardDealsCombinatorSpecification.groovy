package com.mgs.rbsnov

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.Deal
import com.mgs.rbsnov.domain.Hand
import com.mgs.rbsnov.domain.Numeration
import com.mgs.rbsnov.domain.Suit
import com.mgs.rbsnov.logic.CardDealsCombinator
import spock.lang.Specification

class CardDealsCombinatorSpecification extends Specification{
    CardDealsCombinator cardDealsCombinator

    def "setup" (){
        cardDealsCombinator = new CardDealsCombinator();
    }

    def "should explode one card combinations" (){
        given:
        Hand hand1 = new Hand ([new Card(Suit.CLUBS, Numeration.ACE)] as Set);
        Hand hand2 = new Hand ([new Card(Suit.DIAMONDS, Numeration.ACE)] as Set);
        Hand hand3 = new Hand ([new Card(Suit.HEARTS, Numeration.ACE)] as Set);
        Hand hand4 = new Hand ([new Card(Suit.SPADES, Numeration.ACE)] as Set);

        when:
        Set<Deal> deals = cardDealsCombinator.combine (hand1, hand2, hand3, hand4);

        then:
        deals.size() == 1;
        deals == [
                new Deal(
                    new Card(Suit.CLUBS, Numeration.ACE),
                    new Card(Suit.DIAMONDS, Numeration.ACE),
                    new Card(Suit.HEARTS, Numeration.ACE),
                    new Card(Suit.SPADES, Numeration.ACE)
                ),
        ] as Set
    }

    def "should explode two cards combinations" (){
        given:
        Hand hand1 = new Hand ([
                new Card(Suit.CLUBS, Numeration.ACE),
                new Card(Suit.CLUBS, Numeration.TWO),
        ] as Set);

        Hand hand2 = new Hand ([
                new Card(Suit.DIAMONDS, Numeration.ACE),
                new Card(Suit.DIAMONDS, Numeration.TWO),
        ] as Set);

        Hand hand3 = new Hand ([
                new Card(Suit.HEARTS, Numeration.ACE),
                new Card(Suit.HEARTS, Numeration.TWO),
        ] as Set);

        Hand hand4 = new Hand ([
                new Card(Suit.SPADES, Numeration.ACE),
                new Card(Suit.SPADES, Numeration.TWO),
        ] as Set);

        when:
        Set<Deal> deals = cardDealsCombinator.combine (hand1, hand2, hand3, hand4);

        then:
        deals.size() == Math.pow(2, 4);
    }

    def "should explode four cards combinations" (){
        given:
        Hand hand1 = new Hand ([
                new Card(Suit.CLUBS, Numeration.ACE),
                new Card(Suit.CLUBS, Numeration.TWO),
                new Card(Suit.CLUBS, Numeration.THREE),
                new Card(Suit.CLUBS, Numeration.FOUR),
        ] as Set);

        Hand hand2 = new Hand ([
                new Card(Suit.DIAMONDS, Numeration.ACE),
                new Card(Suit.DIAMONDS, Numeration.TWO),
                new Card(Suit.DIAMONDS, Numeration.THREE),
                new Card(Suit.DIAMONDS, Numeration.FOUR),
        ] as Set);

        Hand hand3 = new Hand ([
                new Card(Suit.HEARTS, Numeration.ACE),
                new Card(Suit.HEARTS, Numeration.TWO),
                new Card(Suit.HEARTS, Numeration.THREE),
                new Card(Suit.HEARTS, Numeration.FOUR),
        ] as Set);

        Hand hand4 = new Hand ([
                new Card(Suit.SPADES, Numeration.ACE),
                new Card(Suit.SPADES, Numeration.TWO),
                new Card(Suit.SPADES, Numeration.THREE),
                new Card(Suit.SPADES, Numeration.FOUR),
        ] as Set);

        when:
        Set<Deal> deals = cardDealsCombinator.combine (hand1, hand2, hand3, hand4);

        then:
        deals.size() == Math.pow(4, 4);
    }

    def "should explode eight cards combinations" (){
        given:
        Hand hand1 = new Hand ([
                new Card(Suit.CLUBS, Numeration.ACE),
                new Card(Suit.CLUBS, Numeration.TWO),
                new Card(Suit.CLUBS, Numeration.THREE),
                new Card(Suit.CLUBS, Numeration.FOUR),
                new Card(Suit.CLUBS, Numeration.FIVE),
                new Card(Suit.CLUBS, Numeration.SIX),
                new Card(Suit.CLUBS, Numeration.SEVEN),
                new Card(Suit.CLUBS, Numeration.EIGHT),
        ] as Set);

        Hand hand2 = new Hand ([
                new Card(Suit.DIAMONDS, Numeration.ACE),
                new Card(Suit.DIAMONDS, Numeration.TWO),
                new Card(Suit.DIAMONDS, Numeration.THREE),
                new Card(Suit.DIAMONDS, Numeration.FOUR),
                new Card(Suit.DIAMONDS, Numeration.FIVE),
                new Card(Suit.DIAMONDS, Numeration.SIX),
                new Card(Suit.DIAMONDS, Numeration.SEVEN),
                new Card(Suit.DIAMONDS, Numeration.EIGHT),
        ] as Set);

        Hand hand3 = new Hand ([
                new Card(Suit.HEARTS, Numeration.ACE),
                new Card(Suit.HEARTS, Numeration.TWO),
                new Card(Suit.HEARTS, Numeration.THREE),
                new Card(Suit.HEARTS, Numeration.FOUR),
                new Card(Suit.HEARTS, Numeration.FIVE),
                new Card(Suit.HEARTS, Numeration.SIX),
                new Card(Suit.HEARTS, Numeration.SEVEN),
                new Card(Suit.HEARTS, Numeration.EIGHT),
        ] as Set);

        Hand hand4 = new Hand ([
                new Card(Suit.SPADES, Numeration.ACE),
                new Card(Suit.SPADES, Numeration.TWO),
                new Card(Suit.SPADES, Numeration.THREE),
                new Card(Suit.SPADES, Numeration.FOUR),
                new Card(Suit.SPADES, Numeration.FIVE),
                new Card(Suit.SPADES, Numeration.SIX),
                new Card(Suit.SPADES, Numeration.SEVEN),
                new Card(Suit.SPADES, Numeration.EIGHT),
        ] as Set);

        when:
        Set<Deal> deals = cardDealsCombinator.combine (hand1, hand2, hand3, hand4);

        then:
        deals.size() == Math.pow(8, 4);
    }

    def "should explode all cards combinations" (){
        given:
        Hand hand1 = new Hand ([
                new Card(Suit.CLUBS, Numeration.ACE),
                new Card(Suit.CLUBS, Numeration.TWO),
                new Card(Suit.CLUBS, Numeration.THREE),
                new Card(Suit.CLUBS, Numeration.FOUR),
                new Card(Suit.CLUBS, Numeration.FIVE),
                new Card(Suit.CLUBS, Numeration.SIX),
                new Card(Suit.CLUBS, Numeration.SEVEN),
                new Card(Suit.CLUBS, Numeration.EIGHT),
                new Card(Suit.CLUBS, Numeration.NINE),
                new Card(Suit.CLUBS, Numeration.TEN),
                new Card(Suit.CLUBS, Numeration.JACK),
                new Card(Suit.CLUBS, Numeration.QUEEN),
                new Card(Suit.CLUBS, Numeration.KING),
        ] as Set);

        Hand hand2 = new Hand ([
                new Card(Suit.DIAMONDS, Numeration.ACE),
                new Card(Suit.DIAMONDS, Numeration.TWO),
                new Card(Suit.DIAMONDS, Numeration.THREE),
                new Card(Suit.DIAMONDS, Numeration.FOUR),
                new Card(Suit.DIAMONDS, Numeration.FIVE),
                new Card(Suit.DIAMONDS, Numeration.SIX),
                new Card(Suit.DIAMONDS, Numeration.SEVEN),
                new Card(Suit.DIAMONDS, Numeration.EIGHT),
                new Card(Suit.DIAMONDS, Numeration.NINE),
                new Card(Suit.DIAMONDS, Numeration.TEN),
                new Card(Suit.DIAMONDS, Numeration.JACK),
                new Card(Suit.DIAMONDS, Numeration.QUEEN),
                new Card(Suit.DIAMONDS, Numeration.KING),
        ] as Set);

        Hand hand3 = new Hand ([
                new Card(Suit.HEARTS, Numeration.ACE),
                new Card(Suit.HEARTS, Numeration.TWO),
                new Card(Suit.HEARTS, Numeration.THREE),
                new Card(Suit.HEARTS, Numeration.FOUR),
                new Card(Suit.HEARTS, Numeration.FIVE),
                new Card(Suit.HEARTS, Numeration.SIX),
                new Card(Suit.HEARTS, Numeration.SEVEN),
                new Card(Suit.HEARTS, Numeration.EIGHT),
                new Card(Suit.HEARTS, Numeration.NINE),
                new Card(Suit.HEARTS, Numeration.TEN),
                new Card(Suit.HEARTS, Numeration.JACK),
                new Card(Suit.HEARTS, Numeration.QUEEN),
                new Card(Suit.HEARTS, Numeration.KING),
        ] as Set);

        Hand hand4 = new Hand ([
                new Card(Suit.SPADES, Numeration.ACE),
                new Card(Suit.SPADES, Numeration.TWO),
                new Card(Suit.SPADES, Numeration.THREE),
                new Card(Suit.SPADES, Numeration.FOUR),
                new Card(Suit.SPADES, Numeration.FIVE),
                new Card(Suit.SPADES, Numeration.SIX),
                new Card(Suit.SPADES, Numeration.SEVEN),
                new Card(Suit.SPADES, Numeration.EIGHT),
                new Card(Suit.SPADES, Numeration.NINE),
                new Card(Suit.SPADES, Numeration.TEN),
                new Card(Suit.SPADES, Numeration.JACK),
                new Card(Suit.SPADES, Numeration.QUEEN),
                new Card(Suit.SPADES, Numeration.KING),
        ] as Set);

        when:
        Set<Deal> deals = cardDealsCombinator.combine (hand1, hand2, hand3, hand4);

        then:
        deals.size() == Math.pow(13, 4);
    }


}
