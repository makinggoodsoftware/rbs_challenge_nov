package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.List;
import java.util.Map;

public class GameSimulator {
    private final CardsDiscarder cardsDiscarder;
    private final RoundDeveloperFactory roundDeveloperFactory;
    private final HandsFactory handsFactory;

    public GameSimulator(CardsDiscarder cardsDiscarder, RoundDeveloperFactory roundDeveloperFactory, HandsFactory handsFactory) {
        this.cardsDiscarder = cardsDiscarder;
        this.roundDeveloperFactory = roundDeveloperFactory;
        this.handsFactory = handsFactory;
    }

    public GameResult start(PlayersLogic playersLogic) {
        Hands beforeDescarding = handsFactory.fromAllCardsShuffled();
        Map<Player, DiscardResult> discards = cardsDiscarder.discard(playersLogic, beforeDescarding);

        RoundDeveloper roundDeveloper = roundDeveloperFactory.newRoundDeveloper(playersLogic, discards);
        Hands initialHands = handsFactory.fromDiscards(discards);
        List<RoundResult> roundResults = roundDeveloper.playAllRounds(initialHands);

        return new GameResult(roundResults);
    }



}
