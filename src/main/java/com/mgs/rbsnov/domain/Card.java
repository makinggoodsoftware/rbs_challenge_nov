package com.mgs.rbsnov.domain;

public enum Card {
    JACK_OF_HEARTS (Suit.HEARTS, Numeration.JACK),
    QUEEN_OF_HEARTS (Suit.HEARTS, Numeration.QUEEN),
    KING_OF_HEARTS (Suit.HEARTS, Numeration.KING),
    ACE_OF_HEARTS (Suit.HEARTS, Numeration.ACE),
    TWO_OF_HEARTS (Suit.HEARTS, Numeration.TWO),
    THREE_OF_HEARTS (Suit.HEARTS, Numeration.THREE),
    FOUR_OF_HEARTS (Suit.HEARTS, Numeration.FOUR),
    FIVE_OF_HEARTS (Suit.HEARTS, Numeration.FIVE),
    SIX_OF_HEARTS (Suit.HEARTS, Numeration.SIX),
    SEVEN_OF_HEARTS (Suit.HEARTS, Numeration.SEVEN),
    EIGHT_OF_HEARTS (Suit.HEARTS, Numeration.EIGHT),
    NINE_OF_HEARTS (Suit.HEARTS, Numeration.NINE),
    TEN_OF_HEARTS (Suit.HEARTS, Numeration.TEN),

    JACK_OF_DIAMONDS (Suit.DIAMONDS, Numeration.JACK),
    QUEEN_OF_DIAMONDS (Suit.DIAMONDS, Numeration.QUEEN),
    KING_OF_DIAMONDS (Suit.DIAMONDS, Numeration.KING),
    ACE_OF_DIAMONDS (Suit.DIAMONDS, Numeration.ACE),
    TWO_OF_DIAMONDS (Suit.DIAMONDS, Numeration.TWO),
    THREE_OF_DIAMONDS (Suit.DIAMONDS, Numeration.THREE),
    FOUR_OF_DIAMONDS (Suit.DIAMONDS, Numeration.FOUR),
    FIVE_OF_DIAMONDS (Suit.DIAMONDS, Numeration.FIVE),
    SIX_OF_DIAMONDS (Suit.DIAMONDS, Numeration.SIX),
    SEVEN_OF_DIAMONDS (Suit.DIAMONDS, Numeration.SEVEN),
    EIGHT_OF_DIAMONDS (Suit.DIAMONDS, Numeration.EIGHT),
    NINE_OF_DIAMONDS (Suit.DIAMONDS, Numeration.NINE),
    TEN_OF_DIAMONDS (Suit.DIAMONDS, Numeration.TEN),

    JACK_OF_CLUBS (Suit.CLUBS, Numeration.JACK),
    QUEEN_OF_CLUBS (Suit.CLUBS, Numeration.QUEEN),
    KING_OF_CLUBS (Suit.CLUBS, Numeration.KING),
    ACE_OF_CLUBS (Suit.CLUBS, Numeration.ACE),
    TWO_OF_CLUBS (Suit.CLUBS, Numeration.TWO),
    THREE_OF_CLUBS (Suit.CLUBS, Numeration.THREE),
    FOUR_OF_CLUBS (Suit.CLUBS, Numeration.FOUR),
    FIVE_OF_CLUBS (Suit.CLUBS, Numeration.FIVE),
    SIX_OF_CLUBS (Suit.CLUBS, Numeration.SIX),
    SEVEN_OF_CLUBS (Suit.CLUBS, Numeration.SEVEN),
    EIGHT_OF_CLUBS (Suit.CLUBS, Numeration.EIGHT),
    NINE_OF_CLUBS (Suit.CLUBS, Numeration.NINE),
    TEN_OF_CLUBS (Suit.CLUBS, Numeration.TEN),

    JACK_OF_SPADES (Suit.SPADES, Numeration.JACK),
    QUEEN_OF_SPADES (Suit.SPADES, Numeration.QUEEN),
    KING_OF_SPADES (Suit.SPADES, Numeration.KING),
    ACE_OF_SPADES (Suit.SPADES, Numeration.ACE),
    TWO_OF_SPADES (Suit.SPADES, Numeration.TWO),
    THREE_OF_SPADES (Suit.SPADES, Numeration.THREE),
    FOUR_OF_SPADES (Suit.SPADES, Numeration.FOUR),
    FIVE_OF_SPADES (Suit.SPADES, Numeration.FIVE),
    SIX_OF_SPADES (Suit.SPADES, Numeration.SIX),
    SEVEN_OF_SPADES (Suit.SPADES, Numeration.SEVEN),
    EIGHT_OF_SPADES (Suit.SPADES, Numeration.EIGHT),
    NINE_OF_SPADES (Suit.SPADES, Numeration.NINE),
    TEN_OF_SPADES (Suit.SPADES, Numeration.TEN);

    private final Numeration numeration;
    private final Suit suit;

    Card(Suit suit, Numeration numeration) {
        this.numeration = numeration;
        this.suit = suit;
    }

    public static Card from (Suit suit, Numeration numeration){
        for (Card card : values()) {
            if (card.getSuit() == suit && card.getNumeration() == numeration){
                return card;
            }
        }
        throw new IllegalStateException("Should find any card!");
    }

    public Numeration getNumeration() {
        return numeration;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return "Card{" +
                "numeration=" + numeration +
                ", suit=" + suit +
                '}';
    }

    public boolean kills(Card other) {
        return (getSuit() == other.getSuit()) && numeration.higherThan(other.getNumeration());
    }
}
