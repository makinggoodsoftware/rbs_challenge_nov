package com.mgs.rbsnov.logic;

import com.google.common.collect.ImmutableList;
import com.mgs.rbsnov.domain.Card;

import java.util.*;

public class CardsShuffler {
    public ImmutableList<Card> shuffle(Set<Card> original) {
        ArrayList<Card> copy = new ArrayList<>(original);
        Collections.shuffle(copy);
        ImmutableList.Builder<Card> builder = ImmutableList.builder();
        return builder.addAll(copy).build();
    }
}
