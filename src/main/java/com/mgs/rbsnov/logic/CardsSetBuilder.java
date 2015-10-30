package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class CardsSetBuilder {


    public CardsSetBuilderWip newSet(Set<Card> startingWith) {
        return new CardsSetBuilderWip(startingWith);
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
