package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.Deal;
import com.mgs.rbsnov.utils.ClosureValue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CardDealsCombinator {
    private final DealRules dealRules;
    private final CardsSetBuilder cardsSetBuilder;

    public CardDealsCombinator(DealRules dealRules, CardsSetBuilder cardsSetBuilder) {
        this.dealRules = dealRules;
        this.cardsSetBuilder = cardsSetBuilder;
    }

    public Map<Card, Set<Deal>> combine(Set<Card> firstHand, Set<Card> secondHand, Set<Card> thirdHand, Set<Card> fourthHand) {
        Map<Card, Set<Deal>> deals = new HashMap<>();
        firstHand.stream().forEach((firstCardCandidate) -> {
            Set<Deal> cardDeals = new HashSet<>();
            deals.put(firstCardCandidate, cardDeals);
            ClosureValue<Set<Card>> remainingCardsSecondHand = new ClosureValue<>(cardsSetBuilder.newSet(secondHand).build());
            secondHand.stream().forEach((secondCardCandidate) -> {
                remainingCardsSecondHand.update((cards)->cardsSetBuilder.newSet(cards).remove(secondCardCandidate).build());
                if (dealRules.canFollow(firstCardCandidate, secondCardCandidate, remainingCardsSecondHand.get())) {
                    ClosureValue<Set<Card>> remainingCardsThirdHand = new ClosureValue<>(cardsSetBuilder.newSet(thirdHand).build());
                    thirdHand.stream().forEach(thirdCardCandidate -> {
                        remainingCardsThirdHand.update((cards)->cardsSetBuilder.newSet(cards).remove(thirdCardCandidate).build());
                        if (dealRules.canFollow(firstCardCandidate, thirdCardCandidate, remainingCardsThirdHand.get())) {
                            ClosureValue<Set<Card>> remainingCardsFourthHand = new ClosureValue<>(cardsSetBuilder.newSet(fourthHand).build());
                            fourthHand.stream().forEach((fourthCardCandidate) -> {
                                remainingCardsFourthHand.update((cards)->cardsSetBuilder.newSet(cards).remove(fourthCardCandidate).build());
                                if (dealRules.canFollow(firstCardCandidate, fourthCardCandidate, remainingCardsFourthHand.get())) {
                                    cardDeals.add(new Deal(
                                            firstCardCandidate,
                                            secondCardCandidate,
                                            thirdCardCandidate,
                                            fourthCardCandidate
                                    ));
                                }

                            });
                        }
                    });
                }
            });
        });
        return deals;
    }
}
