package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class HandsFactory {
    private final CardsDealer cardsDealer;
    private final CardsSetBuilder cardsSetBuilder;

    public HandsFactory(CardsDealer cardsDealer, CardsSetBuilder cardsSetBuilder) {
        this.cardsDealer = cardsDealer;
        this.cardsSetBuilder = cardsSetBuilder;
    }

    public Hands reduce(Hands hands, Deal deal, Player startingPlayer) {
        return null;
    }

    public Hands from(List<Set<Card>> asList) {
        return null;
    }

    public Hands fromDiscards(Map<Player, DiscardResult> discards) {
        return null;
    }

    public Hands fromAllCardsShuffled (){
        return from(cardsDealer.deal(4, cardsSetBuilder.allCards()));
    }
}
