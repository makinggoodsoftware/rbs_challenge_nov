package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.Deal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CardDealsCombinator {
    public Map<Card, Set<Deal>> combine(Set<Card> firstHand, Set<Card> secondHand, Set<Card> thirdHand, Set<Card> fourthHand) {
        Map<Card, Set<Deal>> deals = new HashMap<>();
        firstHand.stream().forEach((firstCardCandidate) -> {
            Set<Deal> cardDeals = new HashSet<>();
            deals.put(firstCardCandidate, cardDeals);
            secondHand.stream().forEach((secondCardCandidate) ->
                    thirdHand.stream().forEach((thirdCardCandidate ->
                                    fourthHand.stream().forEach((fourthCardCandidate) ->
                                            cardDeals.add(new Deal(
                                                    firstCardCandidate,
                                                    secondCardCandidate,
                                                    thirdCardCandidate,
                                                    fourthCardCandidate
                                            ))
                                    )
                            )
                    )
            );
        });
        return deals;
    }
}
