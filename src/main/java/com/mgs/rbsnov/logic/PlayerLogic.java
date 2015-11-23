package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerLogic {
    private final CardSelector cardSelector;
    private final CardRiskEvaluator cardRiskEvaluator;

    public PlayerLogic(CardSelector cardSelector, CardRiskEvaluator cardRiskEvaluator) {
        this.cardSelector = cardSelector;
        this.cardRiskEvaluator = cardRiskEvaluator;
    }

    public DiscardDecision discard(Set<Card> cards) {
        if (cardRiskEvaluator.shouldShootTheMoon(cards)){
            return new DiscardDecision(shootTheMoon (cards), true);
        } else {
            return new DiscardDecision(lowestScore (cards), false);
        }

    }

    private Set<Card> lowestScore(Set<Card> cards) {
        return score(cards, -1);
    }

    private Set<Card> shootTheMoon(Set<Card> cards) {
        return score(cards, 1);
    }

    private Set<Card> score(Set<Card> cards, int multiplier) {
        Map<Suit, List<Card>> bySuit = cards.stream().collect(Collectors.groupingBy(card -> card.getSuit()));
        return cards.stream().
                sorted((left, right) ->
                        cardRiskEvaluator.evaluate(left, bySuit).compareTo(cardRiskEvaluator.evaluate(right, bySuit)) * multiplier
                ).
                limit(3).collect(Collectors.toSet());
    }

    public Card playCard(
            DealInProgress dealInProgress,
            Set<Card> inPlay,
            Set<Card> inHand,
            Set<Card> discards,
            Map<Player, Set<Suit>> missingSuits,
            GameScore currentScore,
            boolean shootingTheMoon) {
        Map<Player, Set<Card>> discardMap = new HashMap<>();
        discardMap.put(dealInProgress.getWaitingForPlayer().get().previousClockwise(), discards);
        return cardSelector.bestCard(dealInProgress, inPlay, inHand, discardMap, missingSuits, currentScore);
    }
}