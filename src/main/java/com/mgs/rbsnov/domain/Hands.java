package com.mgs.rbsnov.domain;

import com.google.common.base.Objects;
import com.mgs.rbsnov.logic.HandsFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Hands {
    private final Map<Player, Set<Card>> hands = new HashMap<>();

    public Hands(Set<Card> southHand, Set<Card> westHand, Set<Card> northHand, Set<Card> eastHand) {
        hands.put(Player.SOUTH, southHand);
        hands.put(Player.WEST, westHand);
        hands.put(Player.NORTH, northHand);
        hands.put(Player.EAST, eastHand);
    }

    public Set<Card> getSouthHand() {
        return get(Player.SOUTH);
    }

    public Set<Card> getWestHand() {
        return get(Player.WEST);
    }

    public Set<Card> getNorthHand() {
        return get(Player.NORTH);
    }

    public Set<Card> getEastHand() {
        return get(Player.EAST);
    }

    public Set<Card> get(Player toGet) {
        return hands.get(toGet);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hands)) return false;
        Hands hands1 = (Hands) o;
        return Objects.equal(hands, hands1.hands);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hands);
    }

    @Override
    public String toString() {
        return "Hands{" +
                "hands=" + hands +
                '}';
    }
}
