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

    public Hands from(List<Set<Card>> asList, Player startingFrom) {
        int baseIndex = Player.SOUTH.distanceTo(startingFrom);
        Hands hands = new Hands(
                asList.get(cycle(0 + baseIndex)),
                asList.get(cycle(1 + baseIndex)),
                asList.get(cycle(2 + baseIndex)),
                asList.get(cycle(3 + baseIndex))
        );
        return hands;
    }

    private int cycle(int from) {
        return from > 3? from % 4: from;
    }

    public Hands fromDiscards(Map<Player, DiscardResult> discards) {
        return null;
    }

    public Hands fromAllCardsShuffled (Player startingFrom){
        return from(cardsDealer.deal(4, cardsSetBuilder.allCards()), startingFrom);
    }

    public Hands removeCards(Hands allHands, Card card1, Card card2, Card card3, Card card4) {
        return new Hands(
                removeCard(allHands.getSouthHand(), card1),
                removeCard(allHands.getWestHand(), card2),
                removeCard(allHands.getNorthHand(), card3),
                removeCard(allHands.getEastHand(), card4)
        );
    }

    private Set<Card> removeCard(Set<Card> from, Card toRemove) {
        return cardsSetBuilder.newSet(from).remove(toRemove).build();
    }
}
