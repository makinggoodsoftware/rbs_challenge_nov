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

    public Set<Card> discard(Set<Card> cards) {
        Map<Suit, List<Card>> bySuit = cards.stream().collect(Collectors.groupingBy(card -> card.getSuit()));
        return cards.stream().
                sorted((left, right) ->
                    - cardRiskEvaluator.evaluate(left, bySuit).compareTo(cardRiskEvaluator.evaluate(right, bySuit))
                ).
                limit(3).collect(Collectors.toSet());
    }

    public Card playCard(DealInProgress dealInProgress, Set<Card> inPlay, Set<Card> from, Set<Card> discards, Map<Player, Set<Suit>> missingSuits) {
        Map<Player, Set<Card>> discardMap = new HashMap<>();
        discardMap.put(dealInProgress.getWaitingForPlayer().get().previousClockwise(), discards);
        return cardSelector.bestCard(dealInProgress, inPlay, from, discardMap, missingSuits);
    }
}