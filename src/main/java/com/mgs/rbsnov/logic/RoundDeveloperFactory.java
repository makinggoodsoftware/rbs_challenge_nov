package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.DiscardResult;
import com.mgs.rbsnov.domain.Player;
import com.mgs.rbsnov.domain.PlayersLogic;

import java.util.Map;

public class RoundDeveloperFactory {
    private final DealInProgressFactory dealInProgressFactory;
    private final FinishedDealScorer finishedDealScorer;
    private final HandsFactory handsFactory;
    private final HeartRules heartRules;
    private final CardsSetBuilder cardsSetBuilder;
    private final CardScorer cardScorer;

    public RoundDeveloperFactory(DealInProgressFactory dealInProgressFactory, FinishedDealScorer finishedDealScorer, HandsFactory handsFactory, HeartRules heartRules, CardsSetBuilder cardsSetBuilder, CardScorer cardScorer) {
        this.dealInProgressFactory = dealInProgressFactory;
        this.finishedDealScorer = finishedDealScorer;
        this.handsFactory = handsFactory;
        this.heartRules = heartRules;
        this.cardsSetBuilder = cardsSetBuilder;
        this.cardScorer = cardScorer;
    }

    public RoundDeveloper newRoundDeveloper(PlayersLogic playerLogicMap, Map<Player, DiscardResult> discards) {
        return new RoundDeveloper(dealInProgressFactory, finishedDealScorer, playerLogicMap, discards, handsFactory, heartRules, cardsSetBuilder);
    }
}
