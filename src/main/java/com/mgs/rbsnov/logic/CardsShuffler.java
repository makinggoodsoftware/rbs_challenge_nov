package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CardsShuffler {
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public Set<Card> shuffle(Set<Card> original) {
        Card[] cardsArray = new Card[original.size()];
        cardsArray = original.toArray(cardsArray);
        int index;
        Card toShuffle;
        for (int i = original.size() - 1; i >= 0; i--) {
            index = RANDOM.nextInt(i + 1);
            toShuffle = cardsArray[index];
            cardsArray[index] = cardsArray[i];
            cardsArray[i] = toShuffle;
        }

        Set<Card> shuffled = new HashSet<>(original.size());
        Collections.addAll(shuffled, cardsArray);
        return shuffled;
    }
}
