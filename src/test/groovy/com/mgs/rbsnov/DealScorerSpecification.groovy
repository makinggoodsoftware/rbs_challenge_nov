package com.mgs.rbsnov

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.Deal
import com.mgs.rbsnov.domain.DealScore
import com.mgs.rbsnov.domain.Numeration
import com.mgs.rbsnov.domain.Suit
import com.mgs.rbsnov.logic.CardScorer
import com.mgs.rbsnov.logic.DealScorer
import spock.lang.Specification

class DealScorerSpecification extends Specification{
    int EIGHT_SPADES_SCORE = 5
    int ACE_SPADES_SCORE = 2
    DealScorer dealScorer
    CardScorer cardScorer = Mock(CardScorer)

    def "setup" (){
        dealScorer = new DealScorer(cardScorer)
        cardScorer.score (Card.from(Suit.SPADES, Numeration.EIGHT)) >> EIGHT_SPADES_SCORE
        cardScorer.score (Card.from(Suit.SPADES, Numeration.ACE)) >> ACE_SPADES_SCORE
    }

    def "should score the deal when using all the same suits" (){
        given:
        Deal deal = new Deal(
                Card.from(Suit.SPADES, Numeration.EIGHT),
                Card.from(Suit.SPADES, Numeration.FOUR),
                Card.from(Suit.SPADES, Numeration.ACE),
                Card.from(Suit.SPADES, Numeration.KING),
        )

        when:
        DealScore dealScore = dealScorer.score (deal)

        then:
        dealScore.winner == Card.from(Suit.SPADES, Numeration.ACE)
        dealScore.points == EIGHT_SPADES_SCORE + ACE_SPADES_SCORE
    }


    def "should score the deal when using all different suits" (){
        given:
        Deal deal = new Deal(
                Card.from(Suit.SPADES, Numeration.EIGHT),
                Card.from(Suit.CLUBS, Numeration.FOUR),
                Card.from(Suit.HEARTS, Numeration.ACE),
                Card.from(Suit.DIAMONDS, Numeration.KING),
        )

        when:
        DealScore dealScore = dealScorer.score (deal)

        then:
        dealScore.winner == Card.from(Suit.SPADES, Numeration.EIGHT)
        dealScore.points == EIGHT_SPADES_SCORE
    }

    def "should score the deal when combining different suits" (){
        given:
        Deal deal = new Deal(
                Card.from(Suit.SPADES, Numeration.KING),
                Card.from(Suit.CLUBS, Numeration.FOUR),
                Card.from(Suit.SPADES, Numeration.ACE),
                Card.from(Suit.DIAMONDS, Numeration.KING),
        )

        when:
        DealScore dealScore = dealScorer.score (deal)

        then:
        dealScore.winner == Card.from(Suit.SPADES, Numeration.ACE)
        dealScore.points == ACE_SPADES_SCORE
    }


}
