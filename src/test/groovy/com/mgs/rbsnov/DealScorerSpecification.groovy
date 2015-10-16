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
        cardScorer.score (new Card(Suit.SPADES, Numeration.EIGHT)) >> EIGHT_SPADES_SCORE
        cardScorer.score (new Card(Suit.SPADES, Numeration.ACE)) >> ACE_SPADES_SCORE
    }

    def "should score the deal when using different suits" (){
        given:
        Deal deal = new Deal(
                new Card(Suit.SPADES, Numeration.EIGHT),
                new Card(Suit.SPADES, Numeration.FOUR),
                new Card(Suit.SPADES, Numeration.ACE),
                new Card(Suit.SPADES, Numeration.KING),
        )

        when:
        DealScore dealScore = dealScorer.score (deal)

        then:
        dealScore.winner == new Card(Suit.SPADES, Numeration.ACE)
        dealScore.points == EIGHT_SPADES_SCORE + ACE_SPADES_SCORE
    }
}
