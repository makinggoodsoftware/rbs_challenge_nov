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
        if (cardScorer.score(toCompare)<0) return basicRisk(toCompare);

        return basicRisk(toCompare) + suitScore(cards.get(toCompare.getSuit()).size());
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

    public Integer suitScore(Integer numberOfCards) {
        if (numberOfCards==1) return cardRiskConfiguration.getSuitScoreJustOne();
        if (numberOfCards==2) return cardRiskConfiguration.getSuitScoreJustTwo();
        if (numberOfCards==3) return cardRiskConfiguration.getSuitScoreJustThree();
        return 0;
    }
}
