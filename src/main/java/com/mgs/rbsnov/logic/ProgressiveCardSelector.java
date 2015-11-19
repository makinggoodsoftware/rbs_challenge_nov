package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.DealInProgress;
import org.apache.log4j.Logger;

import java.util.Set;

public class ProgressiveCardSelector implements CardSelector {
    private final Logger LOG = Logger.getLogger(ProgressiveCardSelector.class);

    private final TimedBoxedAggregator<BestCardBundle, Card> aggregator;

    public ProgressiveCardSelector(TimedBoxedAggregator<BestCardBundle, Card> aggregator) {
        this.aggregator = aggregator;
    }

    @Override
    public Card bestCard(DealInProgress dealInProgress, Set<Card> inPlay, Set<Card> myCards) {
        LOG.trace("starting the progressive best card search");
        BestCardBundle input = new BestCardBundle(dealInProgress, inPlay, myCards);
        return aggregator.execute(input);
    }

    public class BestCardBundle {
        private final DealInProgress dealInProgress;
        private final Set<Card> inPlay;
        private final Set<Card> myCards;

        public BestCardBundle(DealInProgress dealInProgress, Set<Card> inPlay, Set<Card> myCards) {
            this.dealInProgress = dealInProgress;
            this.inPlay = inPlay;
            this.myCards = myCards;
        }

        public DealInProgress getDealInProgress() {
            return dealInProgress;
        }

        public Set<Card> getInPlay() {
            return inPlay;
        }

        public Set<Card> getMyCards() {
            return myCards;
        }
    }
}