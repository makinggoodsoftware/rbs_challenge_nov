package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.DealInProgress;

import java.util.Set;

public interface CardSelector {
    Card bestCard(DealInProgress dealInProgress, Set<Card> inPlay, Set<Card> myCards);
}
