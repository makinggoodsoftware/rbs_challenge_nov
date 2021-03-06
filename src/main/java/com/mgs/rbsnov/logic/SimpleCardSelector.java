package com.mgs.rbsnov.logic;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mgs.rbsnov.domain.*;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class SimpleCardSelector implements CardSelector {
    private final Logger LOG = Logger.getLogger(SimpleCardSelector.class);

    private final RunningConfiguration runningConfiguration;
    private final GameAnalyser gameAnalyser;
    private final CardScorer cardScorer;
    private final HandsFactory handsFactory;
    private final PredictedScorer predictedScorer;

    public SimpleCardSelector(RunningConfiguration runningConfiguration, GameAnalyser gameAnalyser, CardScorer cardScorer, HandsFactory handsFactory, PredictedScorer predictedScorer) {
        this.runningConfiguration = runningConfiguration;
        this.gameAnalyser = gameAnalyser;
        this.cardScorer = cardScorer;
        this.handsFactory = handsFactory;
        this.predictedScorer = predictedScorer;
    }


    @Override
    public Card bestCard(DealInProgress dealInProgress, Set<Card> inPlay, Set<Card> myCards, Map<Player, Set<Card>> knownCards, Map<Player, Set<Suit>> missingSuits, GameScore currentScore) {
        if (Thread.currentThread().isInterrupted()) {
            return null;
        }
        LOG.trace("Looking up best card with config " + runningConfiguration);
        Player thisPlayer = dealInProgress.getWaitingForPlayer().get();
        List<List<CardAndScore>> listOfScenarios = runScenarios(inPlay, myCards, dealInProgress, knownCards, missingSuits, currentScore.getHeartsScore());
        List<CardAndScore> allScores = aggregate (listOfScenarios);
        Collections.sort(allScores, (left, right) -> {
            BigDecimal leftScore = left.predictedCardsScore.get(thisPlayer);
            BigDecimal rightScore = right.predictedCardsScore.get(thisPlayer);

            Integer leftPoints = cardScorer.score(left.card);
            Integer rightPoints = cardScorer.score(right.card);

            if (leftScore.compareTo(rightScore) != 0) return leftScore.compareTo(rightScore);
            if (leftPoints.compareTo(rightPoints) !=0 ) return leftPoints.compareTo(rightPoints);
            return left.card.getNumeration().compareTo(right.card.getNumeration());
        });
        Card card = allScores.get(0).card;
        LOG.trace("Best card is " + card);
        return card;
    }

    private List<CardAndScore> aggregate(List<List<CardAndScore>> listOfScenarios) {
        Multimap<Card, PlayersScore> allScoresForCard = ArrayListMultimap.create();
        for (List<CardAndScore> scoresForScenario : listOfScenarios) {
            for (CardAndScore cardAndScore : scoresForScenario) {
                allScoresForCard.put(cardAndScore.card, cardAndScore.predictedCardsScore);
            }
        }

        List<CardAndScore> aggregates = new ArrayList<>();
        for (Map.Entry<Card, Collection<PlayersScore>> cardCollectionEntry : allScoresForCard.asMap().entrySet()) {
            Card card = cardCollectionEntry.getKey();
            Collection<PlayersScore> scores = cardCollectionEntry.getValue();
            PlayersScore predictedScore = predictedScorer.average (scores);
            aggregates.add(new CardAndScore(card, predictedScore));
        }
        return aggregates;
    }

    private List<List<CardAndScore>> runScenarios(Set<Card> inPlay, Set<Card> myCards, DealInProgress dealInProgress, Map<Player, Set<Card>> knownCards, Map<Player, Set<Suit>> missingSuits, PlayersScore heartsScore) {
        if (Thread.currentThread().isInterrupted()) {
            return new ArrayList<>();
        }
        List<List<CardAndScore>> allScoresBag = new ArrayList<>();
        ArrayList<Hands> alreadyProcessedHands = new ArrayList<>();
        for (int i = 0; i<=runningConfiguration.getScenariosToRun(); i++){
            List<CardAndScore> cardAndScores = runScenario(inPlay, myCards, dealInProgress, alreadyProcessedHands, knownCards, missingSuits, heartsScore);
            if (cardAndScores.size()>0) allScoresBag.add(cardAndScores);
        }
        return allScoresBag;
    }

    private List<CardAndScore> runScenario(Set<Card> inPlay, Set<Card> myCards, DealInProgress dealInProgress, List<Hands> alreadyProcessedHands, Map<Player, Set<Card>> knownCards, Map<Player, Set<Suit>> missingSuits, PlayersScore heartsScore) {
        if (Thread.currentThread().isInterrupted()) {
            return new ArrayList<>();
        }
        Player thisPlayer = dealInProgress.getWaitingForPlayer().get();
        Hands hands = handsFactory.dealCards(thisPlayer, myCards, inPlay, knownCards, missingSuits);
        if (alreadyProcessedHands.contains(hands)) {
            LOG.trace("Skipping already processed hand");
            return new ArrayList<>();
        }else{
            alreadyProcessedHands.add(hands);
        }

        if (! hands.get(thisPlayer).equals(myCards)) throw new IllegalStateException();
        GameState gameState = new GameState(hands, dealInProgress);
        Map<Card, PlayersScore> predictedScores = gameAnalyser.analyse(gameState, runningConfiguration.getLevelsDownDeep(), heartsScore);
        return predictedScores.entrySet().stream().
                map(cardPredictedScoreEntry ->
                        new CardAndScore(cardPredictedScoreEntry.getKey(), cardPredictedScoreEntry.getValue())
                ).
                collect(Collectors.toList());
    }

    class CardAndScore {
        private final Card card;
        private final PlayersScore predictedCardsScore;

        CardAndScore(Card card, PlayersScore predictedScore) {
            this.card = card;
            this.predictedCardsScore = predictedScore;
        }
    }
}
