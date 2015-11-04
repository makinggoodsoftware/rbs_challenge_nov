package com.mgs.rbsnov.domain;

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

}
