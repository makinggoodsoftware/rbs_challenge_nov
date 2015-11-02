package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.Deal
import com.mgs.rbsnov.domain.Numeration
import com.mgs.rbsnov.domain.Suit
import spock.lang.Specification

import static com.mgs.rbsnov.domain.Card.ACE_OF_CLUBS
import static com.mgs.rbsnov.domain.Card.EIGHT_OF_CLUBS
import static com.mgs.rbsnov.domain.Card.FIVE_OF_CLUBS
import static com.mgs.rbsnov.domain.Card.FOUR_OF_CLUBS
import static com.mgs.rbsnov.domain.Card.JACK_OF_CLUBS
import static com.mgs.rbsnov.domain.Card.KING_OF_CLUBS
import static com.mgs.rbsnov.domain.Card.NINE_OF_CLUBS
import static com.mgs.rbsnov.domain.Card.QUEEN_OF_CLUBS
import static com.mgs.rbsnov.domain.Card.SEVEN_OF_CLUBS
import static com.mgs.rbsnov.domain.Card.SIX_OF_CLUBS
import static com.mgs.rbsnov.domain.Card.TEN_OF_CLUBS
import static com.mgs.rbsnov.domain.Card.THREE_OF_CLUBS
import static com.mgs.rbsnov.domain.Card.TWO_OF_CLUBS

class CardDealsCombinatorSpecification extends Specification{
    CardDealsCombinator cardDealsCombinator
    DealRules dealRulesMock = Mock()
    CardsSetBuilder cardsSetBuilderMock = Mock()
    CardsSetBuilder.CardsSetBuilderWip cardsSetBuilderWipMock = Mock()
    Set<Card> cardSetMock = Mock()

    def "setup" (){
        cardDealsCombinator = new CardDealsCombinator(dealRulesMock, cardsSetBuilderMock)
        cardsSetBuilderMock.newSet(_) >> cardsSetBuilderWipMock
        cardsSetBuilderWipMock.build() >> cardSetMock
        cardsSetBuilderWipMock.remove(_) >> cardsSetBuilderWipMock
        dealRulesMock.canFollow(_, _, cardSetMock) >> true
    }

    def "should explode one card combinations" (){
        given:
        Set<Card> hand1 = [Card.from(Suit.CLUBS, Numeration.ACE)] as Set
        Set<Card> hand2 = [Card.from(Suit.DIAMONDS, Numeration.ACE)] as Set
        Set<Card> hand3 = [Card.from(Suit.HEARTS, Numeration.ACE)] as Set
        Set<Card> hand4 = [Card.from(Suit.SPADES, Numeration.ACE)] as Set

        when:
        Map<Card, Set<Deal>> deals = cardDealsCombinator.combine (hand1, hand2, hand3, hand4)

        then:
        deals.size() == 1;
        deals == [
            (Card.from(Suit.CLUBS, Numeration.ACE)):
                    [
                        new Deal(
                            Card.from(Suit.CLUBS, Numeration.ACE),
                            Card.from(Suit.DIAMONDS, Numeration.ACE),
                            Card.from(Suit.HEARTS, Numeration.ACE),
                            Card.from(Suit.SPADES, Numeration.ACE)
                        ),
                    ] as Set
        ]
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
        Map<Card, Set<Deal>> deals = cardDealsCombinator.combine (hand1, hand2, hand3, hand4)

        then:
        deals[ACE_OF_CLUBS].size() == (Math.pow(4, 4) / 4).toInteger()
        deals[TWO_OF_CLUBS].size() == (Math.pow(4, 4) / 4).toInteger()
        deals[THREE_OF_CLUBS].size() == (Math.pow(4, 4) / 4).toInteger()
        deals[FOUR_OF_CLUBS].size() == (Math.pow(4, 4) / 4).toInteger()
        deals[ACE_OF_CLUBS] !=  deals[TWO_OF_CLUBS]
        deals[TWO_OF_CLUBS] !=  deals[THREE_OF_CLUBS]
        deals[THREE_OF_CLUBS] !=  deals[FOUR_OF_CLUBS]
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
        Map<Card, Set<Deal>> deals = cardDealsCombinator.combine (hand1, hand2, hand3, hand4)

        then:
        deals[ACE_OF_CLUBS].size() == (Math.pow(13, 4) / 13).toInteger()
        deals[TWO_OF_CLUBS].size() == (Math.pow(13, 4) / 13).toInteger()
        deals[THREE_OF_CLUBS].size() == (Math.pow(13, 4) / 13).toInteger()
        deals[FOUR_OF_CLUBS].size() == (Math.pow(13, 4) / 13).toInteger()
        deals[FIVE_OF_CLUBS].size() == (Math.pow(13, 4) / 13).toInteger()
        deals[SIX_OF_CLUBS].size() == (Math.pow(13, 4) / 13).toInteger()
        deals[SEVEN_OF_CLUBS].size() == (Math.pow(13, 4) / 13).toInteger()
        deals[EIGHT_OF_CLUBS].size() == (Math.pow(13, 4) / 13).toInteger()
        deals[NINE_OF_CLUBS].size() == (Math.pow(13, 4) / 13).toInteger()
        deals[TEN_OF_CLUBS].size() == (Math.pow(13, 4) / 13).toInteger()
        deals[JACK_OF_CLUBS].size() == (Math.pow(13, 4) / 13).toInteger()
        deals[QUEEN_OF_CLUBS].size() == (Math.pow(13, 4) / 13).toInteger()
        deals[KING_OF_CLUBS].size() == (Math.pow(13, 4) / 13).toInteger()
        deals[ACE_OF_CLUBS] !=  deals[TWO_OF_CLUBS]
        deals[TWO_OF_CLUBS] !=  deals[THREE_OF_CLUBS]
        deals[THREE_OF_CLUBS] !=  deals[FOUR_OF_CLUBS]
        deals[FOUR_OF_CLUBS] !=  deals[FIVE_OF_CLUBS]
        deals[FIVE_OF_CLUBS] !=  deals[SIX_OF_CLUBS]
        deals[SIX_OF_CLUBS] !=  deals[SEVEN_OF_CLUBS]
        deals[SEVEN_OF_CLUBS] !=  deals[EIGHT_OF_CLUBS]
        deals[EIGHT_OF_CLUBS] !=  deals[NINE_OF_CLUBS]
        deals[NINE_OF_CLUBS] !=  deals[TEN_OF_CLUBS]
        deals[TEN_OF_CLUBS] !=  deals[JACK_OF_CLUBS]
        deals[JACK_OF_CLUBS] !=  deals[QUEEN_OF_CLUBS]
        deals[QUEEN_OF_CLUBS] !=  deals[KING_OF_CLUBS]
    }


}
