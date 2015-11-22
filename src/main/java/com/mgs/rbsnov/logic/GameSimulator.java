package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSimulator {
    private final Logger LOG = Logger.getLogger(GameSimulator.class);

    private final CardsDiscarder cardsDiscarder;
    private final RoundDeveloperFactory roundDeveloperFactory;
    private final HandsFactory handsFactory;

    public GameSimulator(CardsDiscarder cardsDiscarder, RoundDeveloperFactory roundDeveloperFactory, HandsFactory handsFactory) {
        this.cardsDiscarder = cardsDiscarder;
        this.roundDeveloperFactory = roundDeveloperFactory;
        this.handsFactory = handsFactory;
    }

    public GameResult start(PlayersLogic playersLogic) {
        Hands beforeDescarding = handsFactory.fromAllCardsShuffled(Player.SOUTH, new HashMap<>(), new HashMap<>());
        Map<Player, DiscardResult> discards = cardsDiscarder.discard(playersLogic, beforeDescarding);

        RoundDeveloper roundDeveloper = roundDeveloperFactory.newRoundDeveloper(playersLogic, discards);
        Hands initialHands = handsFactory.fromDiscards(discards);
        List<RoundResult> roundResults = roundDeveloper.playAllRounds(initialHands, new HashMap<>());

        LOG.warn("========================================================================================");
        LOG.warn("GAME FINISHED");
        Map<Player, Integer> finalScores = new HashMap<>();
        Player.all(Player.SOUTH).stream().forEach(player -> finalScores.put(player, 0));

        for (RoundResult roundResult : roundResults) {
            PlayersScore score = roundResult.getFinishedDeal().getScore();
            Player.all(Player.SOUTH).stream().forEach(player -> {
                Integer accumulated = finalScores.get(player) + score.get(player).intValue();
                finalScores.put(player, accumulated);
            });
        }
        LOG.warn("scores:");
        Player.all(Player.SOUTH).stream().forEach(player -> LOG.warn(player + " " + finalScores.get(player)));
        LOG.warn("========================================================================================");

        return new GameResult(roundResults);
    }
}
