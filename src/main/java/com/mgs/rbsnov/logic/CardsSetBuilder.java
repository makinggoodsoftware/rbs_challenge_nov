package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.Hands;
import com.mgs.rbsnov.domain.Suit;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

public class CardsSetBuilder {
    public CardsSetBuilderWip newSet(Set<Card> startingWith) {
        return new CardsSetBuilderWip(startingWith);
    }

    public CardsSetBuilderWip newEmptySet() {
        return newSet(new HashSet<>());
    }

    public Set<Card> allCards() {
        Set<Card> asSet = new HashSet<>();
        Card[] values = Card.values();
        asSet.addAll(asList(values));
        return asSet;
    }

    public class CardsSetBuilderWip {
        private final Set<Card> startingWith;
        private Set<Card> toRemove = new HashSet<>();
        private Set<Card> toAdd = new HashSet<>();

        private CardsSetBuilderWip(Set<Card> startingWith) {
            this.startingWith = startingWith;
        }

        public CardsSetBuilderWip remove(Card... toRemove) {
            this.toRemove.addAll(asList(toRemove));
            return this;
        }

        public CardsSetBuilderWip remove(Collection toRemove) {
            this.toRemove.addAll(toRemove);
            return this;
        }

        public Set<Card> build() {
            Set<Card> finished = new HashSet<>();
            finished.addAll(startingWith);
            finished.addAll(this.toAdd);
            finished = finished.stream().
                    filter(original -> !toRemove.contains(original)).
                    collect(toSet());
            return finished;
        }

        public CardsSetBuilderWip add(Collection<Card> toAdd) {
            this.toAdd.addAll(toAdd);
            return this;
        }
    }
}
