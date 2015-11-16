package com.mgs.rbsnov.domain;

public enum  Numeration {
    JACK(10), QUEEN(11), KING(12), ACE(13),
    TWO(1), THREE(2), FOUR(3), FIVE(4), SIX(5), SEVEN(6), EIGHT(7), NINE(8), TEN(9);

    private final int value;

    Numeration(int value) {
        this.value = value;
    }

    public boolean higherThan(Numeration numeration) {
        return value > numeration.value;
    }

    public int getValue() {
        return value;
    }
}
