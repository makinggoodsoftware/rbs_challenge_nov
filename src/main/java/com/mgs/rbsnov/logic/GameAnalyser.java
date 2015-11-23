package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;
import org.apache.log4j.Logger;

import java.util.*;

import static com.mgs.rbsnov.domain.PlayersScore.shootTheMoon;

public class GameAnalyser {
    private final static Logger LOG = Logger.getLogger(GameAnalyser.class);

    private final DealsDeveloper dealsDeveloper;
    private final PredictedScorer predictedScorer;
    private final DealInProgressFactory dealInProgressFactory;
    private final HandsFactory handsFactory;

    public GameAnalyser(DealsDeveloper dealsDeveloper, PredictedScorer predictedScorer, DealInProgressFactory dealInProgressFactory, HandsFactory handsFactory) {
        this.dealsDeveloper = dealsDeveloper;
        this.predictedScorer = predictedScorer;
        this.dealInProgressFactory = dealInProgressFactory;
        this.handsFactory = handsFactory;
    }

    public Map<Card, PlayersScore> analyse(GameState gameState, int numberOfLevelsDeep, PlayersScore heartsScore){
        return analyseMaxSteps(gameState, 0, numberOfLevelsDeep, heartsScore);
    }

    private Map<Card, PlayersScore> analyseMaxSteps(GameState gameState, int stepsTaken, int numberOfLevelsDeep, PlayersScore heartsScore) {
        if (Thread.currentThread().isInterrupted()) {
            return new HashMap<>();
        }

        if (stepsTaken == numberOfLevelsDeep){
            return new HashMap<>();
        }
        Map<Card, PlayersScore> analysis = new HashMap<>();
        DealInProgress dealInProgress = gameState.getDealInProgress();
        Set<FinishedDeal> possibleDeals = dealsDeveloper.develop(dealInProgress, gameState.getAllHands(), heartsScore);
        Player thisPlayer = dealInProgress.getWaitingForPlayer().get();
        Map<Card, Set<FinishedDeal>> finishedDealsByCard = asMap(possibleDeals, thisPlayer);
        int newStepsTaken = stepsTaken + 1;
        for (Map.Entry<Card, Set<FinishedDeal>> finishedDealsByCardEntry : finishedDealsByCard.entrySet()) {
            Card card = finishedDealsByCardEntry.getKey();
            Set<FinishedDeal> thisFinishedDeals = finishedDealsByCardEntry.getValue();
            PredictedScorer.PredictedScoring predictedScoring = getPredictedScoring(gameState, thisFinishedDeals, newStepsTaken, numberOfLevelsDeep);
            analysis.put(card, predictedScoring.build());
        }
        return analysis;
    }

    private PredictedScorer.PredictedScoring getPredictedScoring(GameState gameState, Set<FinishedDeal> finishedDeals, int stepsTaken, int numberOfLevelsDeep) {
        PredictedScorer.PredictedScoring predictedScoring = predictedScorer.newScoring();
        for (FinishedDeal finishedDeal : finishedDeals) {
            Optional<Player> player = finishedDeal.hasShotTheMoon();
            if (player.isPresent()){
                LOG.info("Shooting the moon detected! " + player.get());
                predictedScoring.addScore(shootTheMoon(player.get()));
                continue;
            }

            predictedScoring.addScore(finishedDeal.getCardsScore());
            if (! gameState.isLastDeal()){
                GameState childGameState = childGameState(gameState, finishedDeal);
                Hands allHands = childGameState.getAllHands();
                if (
                        allHands.get(Player.SOUTH).size() != allHands.get(Player.NORTH).size() ||
                        allHands.get(Player.SOUTH).size() != allHands.get(Player.EAST).size() ||
                        allHands.get(Player.SOUTH).size() != allHands.get(Player.WEST).size()
                ){
                    throw new IllegalStateException();
                }
                Map<Card, PlayersScore> childAnalysis = analyseMaxSteps(childGameState, stepsTaken, numberOfLevelsDeep, finishedDeal.getHeartsScore());
                if (childAnalysis.size() > 0) predictedScoring.addCombinedChildrenDealScores(childAnalysis.values());
            }
        }
        return predictedScoring;
    }

    private GameState childGameState(GameState gameState, FinishedDeal possibleDeal) {
        int distanceToSouth = possibleDeal.getStartingPlayer().distanceTo(Player.SOUTH);
        return new GameState(
                 handsFactory.removeCards(gameState.getAllHands(),
                        possibleDeal.getDeal().getCard(relativeDistance(distanceToSouth, 0)),
                        possibleDeal.getDeal().getCard(relativeDistance(distanceToSouth, 1)),
                        possibleDeal.getDeal().getCard(relativeDistance(distanceToSouth, 2)),
                        possibleDeal.getDeal().getCard(relativeDistance(distanceToSouth, 3))
                ),
                dealInProgressFactory.newJustStartedDeal(possibleDeal.getWinningPlayer())
        );
    }

    private int relativeDistance(int distanceToSouth, int position) {
        int rawValue = distanceToSouth + position;
        return rawValue > 3 ? rawValue - 4 : rawValue;
    }

    private Map<Card, Set<FinishedDeal>> asMap(Set<FinishedDeal> finishedDeals, Player target) {
        Map<Card, Set<FinishedDeal>> asMap = new HashMap<>();
        for (FinishedDeal finishedDeal : finishedDeals) {
            Card card = finishedDeal.getCard(target);
            if (!asMap.containsKey(card)){
                asMap.put(card, new HashSet<>());
            }
            asMap.get(card).add(finishedDeal);
        }
        return asMap;
    }
}
