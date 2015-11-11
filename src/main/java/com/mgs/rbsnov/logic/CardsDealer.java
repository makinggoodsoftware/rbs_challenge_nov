package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;

import java.util.List;
import java.util.Set;

public class CardsDealer {
    private final CardsShuffler cardsShuffler;
    private final SetRotatorFactory setRotatorFactory;

    public CardsDealer(CardsShuffler cardsShuffler, SetRotatorFactory setRotatorFactory) {
        this.cardsShuffler = cardsShuffler;
        this.setRotatorFactory = setRotatorFactory;
    }

    public List<Set<Card>> deal(int numberOfSets, Set<Card> fromCards) {
        SetRotator<Card> setRotator = setRotatorFactory.newSetRotator(numberOfSets, Card.class);
        cardsShuffler.shuffle(fromCards).forEach(setRotator::accept);
        return setRotator.getSets();
    }

}
