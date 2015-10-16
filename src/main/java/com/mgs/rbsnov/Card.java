package com.mgs.rbsnov;

import com.google.common.base.Objects;

public class Card {
    private final Numeration numeration;
    private final Suit suit;

    public Card(Suit suit, Numeration numeration) {
        this.numeration = numeration;
        this.suit = suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return numeration == card.numeration &&
                suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numeration, suit);
    }

    @Override
    public String toString() {
        return "Card{" +
                "numeration=" + numeration +
                ", suit=" + suit +
                '}';
    }
}
