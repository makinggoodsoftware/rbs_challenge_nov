package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;

import java.util.Set;

public class DealRules {

    public boolean canFollow(Card leadingCard, Card attemptedCard, Set<Card> remainingCards) {
        boolean followsWithSameSuit = attemptedCard.getSuit() == leadingCard.getSuit();
        if (followsWithSameSuit) return true;

        long remainingWithSameSuit = remainingCards.stream().filter(card -> card.getSuit() == leadingCard.getSuit()).count();
        return remainingWithSameSuit == 0;
    }
}
