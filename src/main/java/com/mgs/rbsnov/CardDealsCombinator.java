package com.mgs.rbsnov;

import java.util.ArrayList;
import java.util.List;

public class CardDealsCombinator {
    public List<Deal> combine(Hand firstHand, Hand secondHand, Hand thirdHand, Hand fourthHand) {
        List<Deal> deals = new ArrayList<>();
        deals.add(new Deal(
                firstHand.getCards().iterator().next(),
                secondHand.getCards().iterator().next(),
                thirdHand.getCards().iterator().next(),
                fourthHand.getCards().iterator().next()
        ));
        return deals;
    }
}
