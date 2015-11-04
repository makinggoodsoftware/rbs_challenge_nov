package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;

import java.util.Set;
import java.util.stream.Collectors;

public class DealRules {

    public boolean canFollow(Card leadingCard, Card attemptedCard, Set<Card> remainingCards) {
        boolean followsWithSameSuit = attemptedCard.getSuit() == leadingCard.getSuit();
        if (followsWithSameSuit) return true;

        long remainingWithSameSuit = remainingCards.stream().filter(card -> card.getSuit() == leadingCard.getSuit()).count();
        return remainingWithSameSuit == 0;
    }

    public Set<Card> playableCards(Card leadingCard, Set<Card> fromCards) {
        Set<Card> filtered = fromCards.stream().filter(card ->
                canFollow(leadingCard, card, allExcept(fromCards, card))
        ).collect(Collectors.toSet());
        return filtered.size() == 0 ? fromCards: filtered;
    }

    private Set<Card> allExcept(Set<Card> fromCards, Card card) {
        return fromCards.stream().filter(it->it!=card).collect(Collectors.toSet());
    }
}
