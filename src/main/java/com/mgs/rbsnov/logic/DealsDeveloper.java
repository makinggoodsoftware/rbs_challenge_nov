package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.*;

public class DealsDeveloper {
    private final DealRules dealRules;
    private final FinishedDealScorer finishedDealScorer;
    private final DealInProgressFactory dealInProgressFactory;
    private final CardsFilter cardsFilter;

    public DealsDeveloper(DealRules dealRules, FinishedDealScorer finishedDealScorer, DealInProgressFactory dealInProgressFactory, CardsFilter cardsFilter) {
        this.dealRules = dealRules;
        this.finishedDealScorer = finishedDealScorer;
        this.dealInProgressFactory = dealInProgressFactory;
        this.cardsFilter = cardsFilter;
    }


    public Set<FinishedDeal> develop(DealInProgress dealInProgress, Hands hands, PlayersScore heartsScore) {
        if (Thread.currentThread().isInterrupted()) {
            return new HashSet<>();
        }
        if (dealInProgress.isCompleted()) {
            HashSet<FinishedDeal> finishedDeal = new HashSet<>();
            FinishedDeal score = finishedDealScorer.score(heartsScore, dealInProgress);
            finishedDeal.add(score);
            return finishedDeal;
        }

        Set<FinishedDeal> allDeals = new HashSet<>();
        Player waitingForPlayer = dealInProgress.getWaitingForPlayer().get();
        Set<Card> allCards = hands.get(waitingForPlayer);
        Set<Card> playableCards = playableCards(dealInProgress, allCards);
        if (playableCards.size() == 0){
            throw new IllegalStateException();
        }
        Set<Card> filteredCards = cardsFilter.bestCards (dealInProgress, playableCards);
        for (Card playableCard : filteredCards) {
            DealInProgress nextDeal = dealInProgressFactory.next(dealInProgress, playableCard);
            allDeals.addAll(develop(nextDeal, hands, heartsScore));
        }
        return allDeals;
    }

    private Set<Card> playableCards(DealInProgress dealInProgress, Set<Card> allCards) {
        Set<Card> playableCards;
        if (dealInProgress.hasStarted()) {
            playableCards = dealRules.playableCards(dealInProgress.getLeadingCard().get(), allCards);
        } else {
            playableCards = allCards;
        }
        return playableCards;
    }
}
