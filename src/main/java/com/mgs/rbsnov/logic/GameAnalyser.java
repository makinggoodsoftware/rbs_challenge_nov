package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.*;

public class GameAnalyser {
    private final PlayersScorer playersScorer;
    private final CardsSetBuilder cardsSetBuilder;
    private final CardsDealer cardsDealer;
    private final CardDealsCombinator cardDealsCombinator;
    private final DealScorer dealScorer;

    public GameAnalyser(PlayersScorer playersScorer, CardsSetBuilder cardsSetBuilder, CardsDealer cardsDealer, CardDealsCombinator cardDealsCombinator, DealScorer dealScorer) {
        this.playersScorer = playersScorer;
        this.cardsSetBuilder = cardsSetBuilder;
        this.cardsDealer = cardsDealer;
        this.cardDealsCombinator = cardDealsCombinator;
        this.dealScorer = dealScorer;
    }

    public Map<Card, List<DealAnalysis>> analyse(GameState gameState) {
        Set<Card> myHand = gameState.getMyHand();
        Set<Card> cardsToPlay = gameState.getCardsToPlay();

        List<Set<Card>> cardSets = cardsDealer.deal(3, cardsToPlay);
        Set<Deal> deals = cardDealsCombinator.combine(myHand, cardSets.get(0), cardSets.get(1), cardSets.get(2));
        Map<Card, List<DealAnalysis>> analysers = intialize(myHand);

        for (Deal deal : deals) {
            Card sourceCard = deal.getCard1();
            DealScore dealScore = dealScorer.score(deal);
            PlayersScore thisScore = playersScorer.score(dealScore);
            PlayersScore accumulatedScore;
            if (! gameState.isLastDeal()){
                Set<Card> newHand = cardsSetBuilder.newSet(myHand).remove(sourceCard).build();
                Set<Card> newCardsToPlay = cardsSetBuilder.newSet(cardsToPlay).remove(deal.getCard2(), deal.getCard3(), deal.getCard4()).build();

                GameState newGameState = new GameState(newHand, newCardsToPlay);
                accumulatedScore = accumulate (analyse(newGameState));
            } else {
                accumulatedScore = new PlayersScore(0, 0, 0, 0);
            }

            DealAnalysis dealAnalysis = new DealAnalysis(deal, thisScore, accumulatedScore);
            analysers.get(sourceCard).add(dealAnalysis);
        }
        return analysers;
    }

    private PlayersScore accumulate(Map<Card, List<DealAnalysis>> analyse) {
        return null;
    }

    private Map<Card, List<DealAnalysis>> intialize(Set<Card> thisHand) {
        Map<Card, List<DealScore>> analysers = new HashMap<>();
        thisHand.forEach((thisCard)->analysers.put(thisCard, new ArrayList<>()));
        return analysers;
    }
}
