package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Deal;
import com.mgs.rbsnov.domain.Hand;

import java.util.HashSet;
import java.util.Set;

public class CardDealsCombinator {
    public Set<Deal> combine(Hand firstHand, Hand secondHand, Hand thirdHand, Hand fourthHand) {
        Set<Deal> deals = new HashSet<>();
        firstHand.getCards().stream().forEach((firstCardCandidate)->
                secondHand.getCards().stream().forEach((secondCardCandidate)->
                        thirdHand.getCards().stream().forEach((thirdCardCandidate->
                                fourthHand.getCards().stream().forEach((fourthCardCandidate)->
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
