package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.Hands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class CardsSetBuilder {


    public CardsSetBuilderWip newSet(Set<Card> startingWith) {
        return new CardsSetBuilderWip(startingWith);
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
        return newSet(from).remove(toRemove).build();
    }

    public class CardsSetBuilderWip {
        private final Set<Card> startingWith;
        private Set<Card> toRemove = new HashSet<>();

        private CardsSetBuilderWip(Set<Card> startingWith) {
            this.startingWith = startingWith;
        }

        public CardsSetBuilderWip remove(Card... toRemove) {
            this.toRemove.addAll(Arrays.asList(toRemove));
            return this;
        }

        public Set<Card> build() {
            return startingWith.stream().
                    filter(original -> !toRemove.contains(original)).
                    collect(toSet());
        }
    }
}
