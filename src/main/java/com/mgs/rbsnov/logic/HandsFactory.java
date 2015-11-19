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
        int baseIndex = Player.SOUTH.distanceTo(startingPlayer);
        return new Hands(
                removeCard(hands.getSouthHand(), deal.getCard(cycle(0 - baseIndex))),
                removeCard(hands.getWestHand(), deal.getCard(cycle(1 - baseIndex))),
                removeCard(hands.getNorthHand(), deal.getCard(cycle(2 - baseIndex))),
                removeCard(hands.getEastHand(), deal.getCard(cycle(3 - baseIndex)))
        );
    }

    public Hands from(Map<Player, Set<Card>> asMap) {
        Hands hands = new Hands(
                asMap.get(Player.SOUTH),
                asMap.get(Player.WEST),
                asMap.get(Player.NORTH),
                asMap.get(Player.EAST)
        );
        return hands;
    }

    private int cycle(int from) {
        if (from<0) return cycle(4+from);
        return from > 3? from % 4: from;
    }

    public Hands fromDiscards(Map<Player, DiscardResult> discards) {
        return new Hands(
                buildHand(discards.get(Player.SOUTH)),
                buildHand(discards.get(Player.WEST)),
                buildHand(discards.get(Player.NORTH)),
                buildHand(discards.get(Player.EAST))
        );
    }

    private Set<Card> buildHand(DiscardResult discardResult) {
        return cardsSetBuilder.
                newSet(discardResult.getInitialCards()).
                remove(discardResult.getDiscardingCards()).
                add(discardResult.getReceivingCards()).
                build();
    }

    public Hands fromAllCardsShuffled (Player startingFrom){
        Map<Player, Set<Card>> deal = cardsDealer.deal(Player.all(startingFrom), cardsSetBuilder.allCards());
        return from(deal);
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

    public Hands reduce(Hands hands, Player toReduce, Card toRemove) {
        return new Hands(
                (toReduce == Player.SOUTH) ? removeCard(hands.getSouthHand(), toRemove) : hands.getSouthHand(),
                (toReduce == Player.WEST) ? removeCard(hands.getWestHand(), toRemove) : hands.getWestHand(),
                (toReduce == Player.NORTH) ? removeCard(hands.getNorthHand(), toRemove) : hands.getNorthHand(),
                (toReduce == Player.EAST) ? removeCard(hands.getEastHand(), toRemove) : hands.getEastHand()
        );
    }

    public Hands dealCards(Player startingPlayer, Set<Card> startingHand, Set<Card> inPlay) {
        Map<Player, Set<Card>> playerCards = cardsDealer.deal(Player.except(startingPlayer), inPlay);
        playerCards.put(startingPlayer, startingHand);
        return from(playerCards);
    }
}
