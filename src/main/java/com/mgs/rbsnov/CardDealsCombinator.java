package com.mgs.rbsnov;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CardDealsCombinator {
    public List<Deal> combine(Set<Card> fromCards) {
        List<Deal> deals = new ArrayList<>();
        Iterator<Card> iterator = fromCards.iterator();
        deals.add(new Deal(
                iterator.next(),
                iterator.next(),
                iterator.next(),
                iterator.next()
        ));
        return deals;
    }
}
