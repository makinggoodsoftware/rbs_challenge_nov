package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.Map;
import java.util.Set;

public interface CardSelector {
    Card bestCard(DealInProgress dealInProgress, Set<Card> inPlay, Set<Card> myCards, Map<Player, Set<Card>> knownCards, Map<Player, Set<Suit>> missingSuits, GameScore currentScore);
}
