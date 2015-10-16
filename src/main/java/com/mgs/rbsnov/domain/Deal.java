package com.mgs.rbsnov.domain;

import com.google.common.base.Objects;

import static com.google.common.base.Objects.*;

public class Deal {
    private final Card card1;
    private final Card card2;
    private final Card card3;
    private final Card card4;

    public Deal(Card card1, Card card2, Card card3, Card card4) {
        this.card1 = card1;
        this.card2 = card2;
        this.card3 = card3;
        this.card4 = card4;
    }

    public Card getCard1() {
        return card1;
    }

    public Card getCard2() {
        return card2;
    }

    public Card getCard3() {
        return card3;
    }

    public Card getCard4() {
        return card4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deal)) return false;
        Deal deal = (Deal) o;
        return equal(card1, deal.card1) &&
                equal(card2, deal.card2) &&
                equal(card3, deal.card3) &&
                equal(card4, deal.card4);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(card1, card2, card3, card4);
    }

    @Override
    public String toString() {
        return "Deal{" +
                "card1=" + card1 +
                ", card2=" + card2 +
                ", card3=" + card3 +
                ", card4=" + card4 +
                '}';
    }
}
