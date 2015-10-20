package com.mgs.rbsnov

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.Deal
import com.mgs.rbsnov.domain.Numeration
import com.mgs.rbsnov.domain.Suit
import com.mgs.rbsnov.logic.CardDealsCombinator
import spock.lang.Specification

class CardDealsCombinatorSpecification extends Specification{
    CardDealsCombinator cardDealsCombinator

    def "setup" (){
        cardDealsCombinator = new CardDealsCombinator()
    }

    def "should explode one card combinations" (){
        given:
        Set<Card> hand1 = [Card.from(Suit.CLUBS, Numeration.ACE)] as Set
        Set<Card> hand2 = [Card.from(Suit.DIAMONDS, Numeration.ACE)] as Set
        Set<Card> hand3 = [Card.from(Suit.HEARTS, Numeration.ACE)] as Set
        Set<Card> hand4 = [Card.from(Suit.SPADES, Numeration.ACE)] as Set

        when:
        Set<Deal> deals = cardDealsCombinator.combine (hand1, hand2, hand3, hand4)

        then:
        deals.size() == 1;
        deals == [
                new Deal(
                    Card.from(Suit.CLUBS, Numeration.ACE),
                    Card.from(Suit.DIAMONDS, Numeration.ACE),
                    Card.from(Suit.HEARTS, Numeration.ACE),
                    Card.from(Suit.SPADES, Numeration.ACE)
                ),
        ] as Set
    }

    def "should explode two cards combinations" (){
        given:
        Set<Card> hand1 = [
                Card.from(Suit.CLUBS, Numeration.ACE),
                Card.from(Suit.CLUBS, Numeration.TWO),
        ] as Set

        Set<Card> hand2 = [
                Card.from(Suit.DIAMONDS, Numeration.ACE),
                Card.from(Suit.DIAMONDS, Numeration.TWO),
        ] as Set

        Set<Card> hand3 = [
                Card.from(Suit.HEARTS, Numeration.ACE),
                Card.from(Suit.HEARTS, Numeration.TWO),
        ] as Set

        Set<Card> hand4 = [
                Card.from(Suit.SPADES, Numeration.ACE),
                Card.from(Suit.SPADES, Numeration.TWO),
        ] as Set

        when:
        Set<Deal> deals = cardDealsCombinator.combine (hand1, hand2, hand3, hand4);

        then:
        deals.size() == Math.pow(2, 4)
    }

    def "should explode four cards combinations" (){
        given:
        Set<Card> hand1 = [
                Card.from(Suit.CLUBS, Numeration.ACE),
                Card.from(Suit.CLUBS, Numeration.TWO),
                Card.from(Suit.CLUBS, Numeration.THREE),
                Card.from(Suit.CLUBS, Numeration.FOUR),
        ] as Set

        Set<Card> hand2 = [
                Card.from(Suit.DIAMONDS, Numeration.ACE),
                Card.from(Suit.DIAMONDS, Numeration.TWO),
                Card.from(Suit.DIAMONDS, Numeration.THREE),
                Card.from(Suit.DIAMONDS, Numeration.FOUR),
        ] as Set

        Set<Card> hand3 = [
                Card.from(Suit.HEARTS, Numeration.ACE),
                Card.from(Suit.HEARTS, Numeration.TWO),
                Card.from(Suit.HEARTS, Numeration.THREE),
                Card.from(Suit.HEARTS, Numeration.FOUR),
        ] as Set

        Set<Card> hand4 = [
                Card.from(Suit.SPADES, Numeration.ACE),
                Card.from(Suit.SPADES, Numeration.TWO),
                Card.from(Suit.SPADES, Numeration.THREE),
                Card.from(Suit.SPADES, Numeration.FOUR),
        ] as Set

        when:
        Set<Deal> deals = cardDealsCombinator.combine (hand1, hand2, hand3, hand4)

        then:
        deals.size() == Math.pow(4, 4)
    }

    def "should explode eight cards combinations" (){
        given:
        Set<Card> hand1 = [
                Card.from(Suit.CLUBS, Numeration.ACE),
                Card.from(Suit.CLUBS, Numeration.TWO),
                Card.from(Suit.CLUBS, Numeration.THREE),
                Card.from(Suit.CLUBS, Numeration.FOUR),
                Card.from(Suit.CLUBS, Numeration.FIVE),
                Card.from(Suit.CLUBS, Numeration.SIX),
                Card.from(Suit.CLUBS, Numeration.SEVEN),
                Card.from(Suit.CLUBS, Numeration.EIGHT),
        ] as Set

        Set<Card> hand2 = [
                Card.from(Suit.DIAMONDS, Numeration.ACE),
                Card.from(Suit.DIAMONDS, Numeration.TWO),
                Card.from(Suit.DIAMONDS, Numeration.THREE),
                Card.from(Suit.DIAMONDS, Numeration.FOUR),
                Card.from(Suit.DIAMONDS, Numeration.FIVE),
                Card.from(Suit.DIAMONDS, Numeration.SIX),
                Card.from(Suit.DIAMONDS, Numeration.SEVEN),
                Card.from(Suit.DIAMONDS, Numeration.EIGHT),
        ] as Set

        Set<Card> hand3 = [
                Card.from(Suit.HEARTS, Numeration.ACE),
                Card.from(Suit.HEARTS, Numeration.TWO),
                Card.from(Suit.HEARTS, Numeration.THREE),
                Card.from(Suit.HEARTS, Numeration.FOUR),
                Card.from(Suit.HEARTS, Numeration.FIVE),
                Card.from(Suit.HEARTS, Numeration.SIX),
                Card.from(Suit.HEARTS, Numeration.SEVEN),
                Card.from(Suit.HEARTS, Numeration.EIGHT),
        ] as Set

        Set<Card> hand4 = [
                Card.from(Suit.SPADES, Numeration.ACE),
                Card.from(Suit.SPADES, Numeration.TWO),
                Card.from(Suit.SPADES, Numeration.THREE),
                Card.from(Suit.SPADES, Numeration.FOUR),
                Card.from(Suit.SPADES, Numeration.FIVE),
                Card.from(Suit.SPADES, Numeration.SIX),
                Card.from(Suit.SPADES, Numeration.SEVEN),
                Card.from(Suit.SPADES, Numeration.EIGHT),
        ] as Set

        when:
        Set<Deal> deals = cardDealsCombinator.combine (hand1, hand2, hand3, hand4)

        then:
        deals.size() == Math.pow(8, 4)
    }

    def "should explode all cards combinations" (){
        given:
        Set<Card> hand1 = [
                Card.from(Suit.CLUBS, Numeration.ACE),
                Card.from(Suit.CLUBS, Numeration.TWO),
                Card.from(Suit.CLUBS, Numeration.THREE),
                Card.from(Suit.CLUBS, Numeration.FOUR),
                Card.from(Suit.CLUBS, Numeration.FIVE),
                Card.from(Suit.CLUBS, Numeration.SIX),
                Card.from(Suit.CLUBS, Numeration.SEVEN),
                Card.from(Suit.CLUBS, Numeration.EIGHT),
                Card.from(Suit.CLUBS, Numeration.NINE),
                Card.from(Suit.CLUBS, Numeration.TEN),
                Card.from(Suit.CLUBS, Numeration.JACK),
                Card.from(Suit.CLUBS, Numeration.QUEEN),
                Card.from(Suit.CLUBS, Numeration.KING),
        ] as Set

        Set<Card> hand2 = [
                Card.from(Suit.DIAMONDS, Numeration.ACE),
                Card.from(Suit.DIAMONDS, Numeration.TWO),
                Card.from(Suit.DIAMONDS, Numeration.THREE),
                Card.from(Suit.DIAMONDS, Numeration.FOUR),
                Card.from(Suit.DIAMONDS, Numeration.FIVE),
                Card.from(Suit.DIAMONDS, Numeration.SIX),
                Card.from(Suit.DIAMONDS, Numeration.SEVEN),
                Card.from(Suit.DIAMONDS, Numeration.EIGHT),
                Card.from(Suit.DIAMONDS, Numeration.NINE),
                Card.from(Suit.DIAMONDS, Numeration.TEN),
                Card.from(Suit.DIAMONDS, Numeration.JACK),
                Card.from(Suit.DIAMONDS, Numeration.QUEEN),
                Card.from(Suit.DIAMONDS, Numeration.KING),
        ] as Set

        Set<Card> hand3 = [
                Card.from(Suit.HEARTS, Numeration.ACE),
                Card.from(Suit.HEARTS, Numeration.TWO),
                Card.from(Suit.HEARTS, Numeration.THREE),
                Card.from(Suit.HEARTS, Numeration.FOUR),
                Card.from(Suit.HEARTS, Numeration.FIVE),
                Card.from(Suit.HEARTS, Numeration.SIX),
                Card.from(Suit.HEARTS, Numeration.SEVEN),
                Card.from(Suit.HEARTS, Numeration.EIGHT),
                Card.from(Suit.HEARTS, Numeration.NINE),
                Card.from(Suit.HEARTS, Numeration.TEN),
                Card.from(Suit.HEARTS, Numeration.JACK),
                Card.from(Suit.HEARTS, Numeration.QUEEN),
                Card.from(Suit.HEARTS, Numeration.KING),
        ] as Set

        Set<Card> hand4 = [
                Card.from(Suit.SPADES, Numeration.ACE),
                Card.from(Suit.SPADES, Numeration.TWO),
                Card.from(Suit.SPADES, Numeration.THREE),
                Card.from(Suit.SPADES, Numeration.FOUR),
                Card.from(Suit.SPADES, Numeration.FIVE),
                Card.from(Suit.SPADES, Numeration.SIX),
                Card.from(Suit.SPADES, Numeration.SEVEN),
                Card.from(Suit.SPADES, Numeration.EIGHT),
                Card.from(Suit.SPADES, Numeration.NINE),
                Card.from(Suit.SPADES, Numeration.TEN),
                Card.from(Suit.SPADES, Numeration.JACK),
                Card.from(Suit.SPADES, Numeration.QUEEN),
                Card.from(Suit.SPADES, Numeration.KING),
        ] as Set

        when:
        Set<Deal> deals = cardDealsCombinator.combine (hand1, hand2, hand3, hand4)

        then:
        deals.size() == Math.pow(13, 4)
    }


}
