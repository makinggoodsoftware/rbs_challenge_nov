package com.mgs.rbsnov.adapter;

import com.mgs.rbsnov.client.entities.Deal;
import com.mgs.rbsnov.client.entities.DealCard;
import com.mgs.rbsnov.client.entities.GameStatus;
import com.mgs.rbsnov.client.enums.SuitType;
import com.mgs.rbsnov.domain.*;
import com.mgs.rbsnov.logic.CardsSetBuilder;
import com.mgs.rbsnov.logic.DealInProgressFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class CardsAdaptor {
    private final DealInProgressFactory dealInProgressFactory;
    private final CardsSetBuilder cardsSetBuilder;

    public CardsAdaptor(DealInProgressFactory dealInProgressFactory, CardsSetBuilder cardsSetBuilder) {
        this.dealInProgressFactory = dealInProgressFactory;
        this.cardsSetBuilder = cardsSetBuilder;
    }

    public Set<Card> extractMyHand(GameStatus gameStatus) {
        return gameStatus.getMyCurrentHand().stream().
                map(this::toDomainCard).
                collect(Collectors.toSet());
    }

    public Card toDomainCard(com.mgs.rbsnov.client.entities.Card extCard) {
        return Card.from(suit(extCard.getSuit()), numeration(extCard.getNumber()));
    }

    private Numeration numeration(int number) {
        switch (number){
            case 14: return Numeration.ACE;
            case 13: return Numeration.KING;
            case 12: return Numeration.QUEEN;
            case 11: return Numeration.JACK;
            case 10: return Numeration.TEN;
            case 9: return Numeration.NINE;
            case 8: return Numeration.EIGHT;
            case 7: return Numeration.SEVEN;
            case 6: return Numeration.SIX;
            case 5: return Numeration.FIVE;
            case 4: return Numeration.FOUR;
            case 3: return Numeration.THREE;
            case 2: return Numeration.TWO;
        }
        throw new IllegalStateException();
    }

    private Suit suit(SuitType suitExt) {
        switch (suitExt){
            case Club:
                return Suit.CLUBS;
            case Diamond:
                return Suit.DIAMONDS;
            case Heart:
                return Suit.HEARTS;
            case Spade:
                return Suit.SPADES;
        }
        throw new IllegalStateException();
    }

    public List<com.mgs.rbsnov.client.entities.Card> toExternalHand(Set<Card> domainDiscards) {
        return domainDiscards.stream().map(this::toCardExt).collect(toList());
    }

    public com.mgs.rbsnov.client.entities.Card toCardExt(Card cardDomain) {
        com.mgs.rbsnov.client.entities.Card card = new com.mgs.rbsnov.client.entities.Card();
        card.setSuit(suitExt(cardDomain.getSuit()));
        card.setNumber(numberExt(cardDomain.getNumeration()));
        return card;
    }

    private int numberExt(Numeration numeration) {
        return numeration.getValue() + 1;
    }

    private SuitType suitExt(Suit suit) {
        switch (suit){
            case CLUBS:
                return SuitType.Club;
            case DIAMONDS:
                return SuitType.Diamond;
            case HEARTS:
                return SuitType.Heart;
            case SPADES:
                return SuitType.Spade;
        }
        throw new IllegalStateException();
    }

    public DealInProgress deal(GameStatus gameStatus) {
        List<DealCard> dealCards = gameStatus.getMyInProgressDeal().getDealCards();
        int dealSize = dealCards.size();
        switch (dealSize){
            case 0: return dealInProgressFactory.newJustStartedDeal(Player.SOUTH);
            case 1: return dealInProgressFactory.oneCardDeal(
                    Player.SOUTH.previousClockwise(),
                    toDomainCard(dealCards.get(0).getCard())
            );
            case 2: return dealInProgressFactory.twoCardsDeal(
                    Player.SOUTH.previousClockwise().previousClockwise(),
                    toDomainCard(dealCards.get(0).getCard()),
                    toDomainCard(dealCards.get(1).getCard())
            );
            case 3: return dealInProgressFactory.threeCardsDeal(
                    Player.SOUTH.previousClockwise().previousClockwise().previousClockwise(),
                    toDomainCard(dealCards.get(0).getCard()),
                    toDomainCard(dealCards.get(1).getCard()),
                    toDomainCard(dealCards.get(2).getCard())
            );
        }
        throw new IllegalStateException();
    }

    public Set<Card> inPlay(GameStatus gameStatus, DealInProgress dealInProgress) {
        CardsSetBuilder.CardsSetBuilderWip inPlayBuilder = cardsSetBuilder.newSet(cardsSetBuilder.allCards());
        List<com.mgs.rbsnov.client.entities.Deal> myGameDeals = gameStatus.getMyGameDeals();
        for (Deal myGameDeal : myGameDeals) {
            List<DealCard> dealCards = myGameDeal.getDealCards();
            for (DealCard dealCard : dealCards) {
                inPlayBuilder.remove(toDomainCard(dealCard.getCard()));
            }
        }

        List<Card> followingCards = dealInProgress.getFollowingCards();
        followingCards.forEach(inPlayBuilder::remove);
        return inPlayBuilder.build();
    }
}
