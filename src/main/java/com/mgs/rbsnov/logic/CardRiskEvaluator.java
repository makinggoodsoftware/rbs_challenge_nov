package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.CardRiskConfiguration;
import com.mgs.rbsnov.domain.Suit;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CardRiskEvaluator {
    private final CardScorer cardScorer;
    private final CardRiskConfiguration cardRiskConfiguration;
    private final Integer shootTheMoonThreshold;

    public CardRiskEvaluator(CardScorer cardScorer, CardRiskConfiguration cardRiskConfiguration, Integer shootTheMoonThreshold) {
        this.cardScorer = cardScorer;
        this.cardRiskConfiguration = cardRiskConfiguration;
        this.shootTheMoonThreshold = shootTheMoonThreshold;
    }

    public Integer evaluate(Card toCompare, Map<Suit, ? extends Collection<Card>> cards) {
        Integer basicRisk = basicRisk(toCompare);
        if (cardScorer.score(toCompare)<0) return basicRisk;
        if (toCompare.getSuit() == Suit.HEARTS || toCompare.getSuit() == Suit.SPADES) return basicRisk;

        return basicRisk + suitScore(cards.get(toCompare.getSuit()).size());
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

    public boolean shouldShootTheMoon(Set<Card> cards) {
        int totalShootTheMoonScore = 0;
        for (Card card : cards) {
            totalShootTheMoonScore += shootTheMoonScore (card);
        }
        return totalShootTheMoonScore > shootTheMoonThreshold;
    }

    private int shootTheMoonScore(Card card) {
        if (card == Card.QUEEN_OF_SPADES) return 30;
        if (card == Card.KING_OF_SPADES) return 20;
        if (card == Card.ACE_OF_SPADES) return 25;

        int baseValue = card.getNumeration().getValue();

        return card.getSuit() == Suit.HEARTS ?
                baseValue * 2 :
                baseValue;
    }
}
