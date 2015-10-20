package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.Deal;
import com.mgs.rbsnov.domain.Hand;

import java.util.HashSet;
import java.util.Set;

public class CardDealsCombinator {
    public Set<Deal> combine(Set<Card> firstHand, Set<Card> secondHand, Set<Card> thirdHand, Set<Card> fourthHand) {
        Set<Deal> deals = new HashSet<>();
        firstHand.stream().forEach((firstCardCandidate)->
                secondHand.stream().forEach((secondCardCandidate)->
                        thirdHand.stream().forEach((thirdCardCandidate->
                                fourthHand.stream().forEach((fourthCardCandidate)->
                                        deals.add(new Deal(
                                                firstCardCandidate,
                                                secondCardCandidate,
                                                thirdCardCandidate,
                                                fourthCardCandidate
                                        ))
                                )
                        )
                )
        ));
        return deals;
    }
}
