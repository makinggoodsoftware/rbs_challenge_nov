package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.*;

public class DealsDeveloper {
    private final DealRules dealRules;
    private final PlayersScorer playersScorer;
    private final DealInProgressFactory dealInProgressFactory;

    public DealsDeveloper(DealRules dealRules, PlayersScorer playersScorer, DealInProgressFactory dealInProgressFactory) {
        this.dealRules = dealRules;
        this.playersScorer = playersScorer;
        this.dealInProgressFactory = dealInProgressFactory;
    }


    public Set<FinishedDeal> develop(DealInProgress dealInProgress, Hands hands) {
        if (dealInProgress.isCompleted()) {
            HashSet<FinishedDeal> finishedDeal = new HashSet<>();
            finishedDeal.add(playersScorer.score(dealInProgress));
            return finishedDeal;
        }

        Set<FinishedDeal> allDeals = new HashSet<>();
        Player waitingForPlayer = dealInProgress.getWaitingForPlayer().get();
        Set<Card> allCards = hands.get(waitingForPlayer);
        Set<Card> playableCards = playableCards(dealInProgress, allCards);
        for (Card playableCard : playableCards) {
            DealInProgress nextDeal = dealInProgressFactory.next(dealInProgress, playableCard);
            allDeals.addAll(develop(nextDeal, hands));
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
