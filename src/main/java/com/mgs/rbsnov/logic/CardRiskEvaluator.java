package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.CardRiskConfiguration;
import com.mgs.rbsnov.domain.Suit;

import java.util.Collection;
import java.util.Map;

public class CardRiskEvaluator {
    private final CardScorer cardScorer;
    private final CardRiskConfiguration cardRiskConfiguration;

    public CardRiskEvaluator(CardScorer cardScorer, CardRiskConfiguration cardRiskConfiguration) {
        this.cardScorer = cardScorer;
        this.cardRiskConfiguration = cardRiskConfiguration;
    }

    public Integer evaluate(Card toCompare, Map<Suit, ? extends Collection<Card>> cards) {
        if (!cards.get(toCompare.getSuit()).contains(toCompare)) throw new IllegalArgumentException();

        return cardScorer.score(toCompare);
    }

    public Integer basicRisk(Card toCompare) {
        if (toCompare == Card.QUEEN_OF_SPADES) return cardRiskConfiguration.getQueenOfSpadesRisk();
        if (toCompare == Card.KING_OF_SPADES) return cardRiskConfiguration.getKingOfSpadesRisk();
        if (toCompare == Card.ACE_OF_SPADES) return cardRiskConfiguration.getAceOfSpadesRisk();

        int cardValue = toCompare.getNumeration().getValue();
        Integer score = cardScorer.score(toCompare);
        return score > 0 ?
                cardValue * cardRiskConfiguration.getPositivieScoringBaseCardMultiplier() :
                score == 0 ?
                    cardValue :
                    - cardValue;
    }
}
