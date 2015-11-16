package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.CardRiskConfiguration
import com.mgs.rbsnov.domain.Numeration
import spock.lang.Specification

import static com.mgs.rbsnov.domain.Card.*

class CardRiskEvaluatorSpecification extends Specification {
    public static final int QUEEN_OF_SPADES_RISK = 50
    public static final int KING_OF_SPADES_RISK = 30
    public static final int ACE_OF_SPADES_RISK = 40
    public static final int POSITIVE_SCORING_BASE_CARD_MULTIPLIER = 2

    CardScorer cardScorer = Mock(CardScorer)
    CardRiskConfiguration cardRiskConfiguration = Mock (CardRiskConfiguration)
    CardRiskEvaluator cardRiskEvaluator

    def "setup" (){
        cardRiskConfiguration.queenOfSpadesRisk >> QUEEN_OF_SPADES_RISK
        cardRiskConfiguration.kingOfSpadesRisk >> KING_OF_SPADES_RISK
        cardRiskConfiguration.aceOfSpadesRisk >> ACE_OF_SPADES_RISK
        cardRiskConfiguration.positivieScoringBaseCardMultiplier >> POSITIVE_SCORING_BASE_CARD_MULTIPLIER

        cardScorer.score(TEN_OF_SPADES) >> 0
        cardScorer.score(TEN_OF_DIAMONDS) >> 0
        cardScorer.score(TEN_OF_HEARTS) >> 1
        cardScorer.score(TEN_OF_CLUBS) >> -1

        cardRiskEvaluator = new CardRiskEvaluator(cardScorer, cardRiskConfiguration)
    }

    def "QS base risk" (){
        expect:
        cardRiskEvaluator.basicRisk(QUEEN_OF_SPADES) == QUEEN_OF_SPADES_RISK
    }

    def "KS base risk" (){
        expect:
        cardRiskEvaluator.basicRisk(KING_OF_SPADES) == KING_OF_SPADES_RISK
    }

    def "AS base risk" (){
        expect:
        cardRiskEvaluator.basicRisk(ACE_OF_SPADES) == ACE_OF_SPADES_RISK
    }


    def "xS base risk" (){
        expect:
        cardRiskEvaluator.basicRisk(TEN_OF_SPADES) == Numeration.TEN.value
    }

    def "positive points (ie hearts) base risk" (){
        expect:
        cardRiskEvaluator.basicRisk(TEN_OF_HEARTS) == Numeration.TEN.value * POSITIVE_SCORING_BASE_CARD_MULTIPLIER
    }

    def "neutral points (ie diamonds) base risk" (){
        expect:
        cardRiskEvaluator.basicRisk(TEN_OF_DIAMONDS) == Numeration.TEN.value
    }

    def "negative points (ie clubs) base risk" (){
        expect:
        cardRiskEvaluator.basicRisk(TEN_OF_CLUBS) == - Numeration.TEN.value
    }


}
