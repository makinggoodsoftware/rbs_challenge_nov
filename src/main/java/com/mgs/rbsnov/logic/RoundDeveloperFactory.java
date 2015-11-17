package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.DiscardResult;
import com.mgs.rbsnov.domain.Player;
import com.mgs.rbsnov.domain.PlayersLogic;

import java.util.Map;

public class RoundDeveloperFactory {private final PlayerRotator playerRotator;
    private final DealInProgressFactory dealInProgressFactory;
    private final PlayersScorer playersScorer;
    private final HandsFactory handsFactory;
    private final HeartRules heartRules;
    private final CardsSetBuilder cardsSetBuilder;

    public RoundDeveloperFactory(PlayerRotator playerRotator, DealInProgressFactory dealInProgressFactory, PlayersScorer playersScorer, HandsFactory handsFactory, HeartRules heartRules, CardsSetBuilder cardsSetBuilder) {
        this.playerRotator = playerRotator;
        this.dealInProgressFactory = dealInProgressFactory;
        this.playersScorer = playersScorer;
        this.handsFactory = handsFactory;
        this.heartRules = heartRules;
        this.cardsSetBuilder = cardsSetBuilder;
    }

    public RoundDeveloper newRoundDeveloper(PlayersLogic playerLogicMap, Map<Player, DiscardResult> discards) {
        return new RoundDeveloper(playerRotator, dealInProgressFactory, playersScorer, playerLogicMap, discards, handsFactory, heartRules, cardsSetBuilder);
    }
}