package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;

public class ProgressiveCardSelector implements CardSelector {
    private final Logger LOG = Logger.getLogger(ProgressiveCardSelector.class);

    private final TimedBoxedAggregator<BestCardBundle, Card> aggregator;

    public ProgressiveCardSelector(TimedBoxedAggregator<BestCardBundle, Card> aggregator) {
        this.aggregator = aggregator;
    }

    @Override
    public Card bestCard(DealInProgress dealInProgress, Set<Card> inPlay, Set<Card> myCards, Map<Player, Set<Card>> knownCards, Map<Player, Set<Suit>> missingSuits, GameScore currentScore) {
        LOG.trace("starting the progressive best card search");
        BestCardBundle input = new BestCardBundle(dealInProgress, inPlay, myCards, knownCards, missingSuits, currentScore);
        return aggregator.execute(input);
    }

    public class BestCardBundle {
        private final DealInProgress dealInProgress;
        private final Set<Card> inPlay;
        private final Set<Card> myCards;
        private final Map<Player, Set<Card>> knownCards;
        private final Map<Player, Set<Suit>> missingSuits;
        private final GameScore currentScore;

        public BestCardBundle(DealInProgress dealInProgress, Set<Card> inPlay, Set<Card> myCards, Map<Player, Set<Card>> knownCards, Map<Player, Set<Suit>> missingSuits, GameScore currentScore) {
            this.dealInProgress = dealInProgress;
            this.inPlay = inPlay;
            this.myCards = myCards;
            this.knownCards = knownCards;
            this.missingSuits = missingSuits;
            this.currentScore = currentScore;
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

        public Map<Player, Set<Card>> getKnownCards() {
            return knownCards;
        }

        public Map<Player, Set<Suit>> getMissingSuits() {
            return missingSuits;
        }

        public GameScore getCurrentScore() {
            return currentScore;
        }
    }
}
