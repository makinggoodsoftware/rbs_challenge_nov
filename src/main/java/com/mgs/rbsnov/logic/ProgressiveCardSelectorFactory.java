package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.function.BiFunction;

public class ProgressiveCardSelectorFactory {
    private final Logger LOG = Logger.getLogger(ProgressiveCardSelectorFactory.class);
    private final Map<Integer, CardSelector> selectorsByProgressionIndex;

    public ProgressiveCardSelectorFactory(Map<Integer, CardSelector> selectorsByProgressionIndex) {
        this.selectorsByProgressionIndex = selectorsByProgressionIndex;
    }

    public ProgressiveCardSelector create (int timeoutMs){
        TimedBoxedAggregator<ProgressiveCardSelector.BestCardBundle, Card> aggregator = new TimedBoxedAggregator<>(
                executor(),
                aggregator(),
                timeoutMs
        );
        return new ProgressiveCardSelector(aggregator);
    }

    private Aggregator<ProgressiveCardSelector.BestCardBundle, Card> executor() {
        return (input, currentIterationIndex) -> {
            CardSelector cardSelector = selectorsByProgressionIndex.get(currentIterationIndex);
            if (cardSelector == null){
                LOG.trace("There are no more card selectors configured for: " + currentIterationIndex);
                try {
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    //ignore
                }
                throw new IllegalStateException("Should never reach this point");
            }
            return cardSelector.bestCard(
                    input.getDealInProgress(),
                    input.getInPlay(),
                    input.getMyCards(),
                    input.getKnownCards(),
                    input.getMissingSuits()
            );
        };
    }

    private BiFunction<Card, Card, Card> aggregator() {
        return (previous, current) -> {
            if (Thread.currentThread().isInterrupted()) {
                return current;
            }
            if (current == null) return previous;
            if (previous != current) LOG.info("Changing opinions, substituting: " + previous + " with: " + current);
            return current;
        };
    }
}
