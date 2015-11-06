package com.mgs.rbsnov.spring;

import com.google.common.collect.ImmutableMap;
import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.logic.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public CardSelector cardSelector (){
        return new CardSelector(gameAnalyser(), cardsDealer());
    }

    @Bean
    public GameAnalyser gameAnalyser() {
        return new GameAnalyser(
                predictedScorer(),
                cardsSetBuilder(),
                cardDealsCombinator(),
                playersScorer()
        );
    }

    @Bean
    public PlayersScorer playersScorer() {
        return new PlayersScorer(dealScorer());
    }

    @Bean
    public DealScorer dealScorer() {
        return new DealScorer(cardScorer());
    }

    @Bean
    public CardScorer cardScorer() {
        return new CardScorer(new ImmutableMap.Builder<Card, Integer>().
                put(Card.ACE_OF_HEARTS, 1).
                put(Card.TWO_OF_HEARTS, 1).
                put(Card.THREE_OF_HEARTS, 1).
                put(Card.FOUR_OF_HEARTS, 1).
                put(Card.FIVE_OF_HEARTS, 1).
                put(Card.SIX_OF_HEARTS, 1).
                put(Card.SEVEN_OF_HEARTS, 1).
                put(Card.EIGHT_OF_HEARTS, 1).
                put(Card.NINE_OF_HEARTS, 1).
                put(Card.TEN_OF_HEARTS, 1).
                put(Card.JACK_OF_HEARTS, 1).
                put(Card.QUEEN_OF_HEARTS, 1).
                put(Card.KING_OF_HEARTS, 1).
                put(Card.QUEEN_OF_SPADES, 13).
                build());
    }

    @Bean
    public CardDealsCombinator cardDealsCombinator() {
        return new CardDealsCombinator(dealRules(), cardsSetBuilder());
    }

    @Bean
    public DealRules dealRules() {
        return new DealRules();
    }

    @Bean
    public CardsDealer cardsDealer() {
        return new CardsDealer(
                cardsShuffler(),
                setRotationFactory()
        );
    }

    @Bean
    public GameAnalyserII gameAnalyserII(){
        return new GameAnalyserII(
                gameDeveloper(),
                predictedScorer(),
                dealInProgressFactory(),
                cardsSetBuilder());
    }

    @Bean
    public GameStateFactory gameStateFactory() {
        return new GameStateFactory();
    }

    @Bean
    public DealsDeveloper gameDeveloper() {
        return new DealsDeveloper(dealRules(), playersScorer(), dealInProgressFactory(), cardsFilter ());
    }

    @Bean
    public CardsFilter cardsFilter(){
        return new CardsFilter(cardScorer());
    }

    @Bean
    public DealInProgressFactory dealInProgressFactory() {
        return new DealInProgressFactory();
    }

    @Bean
    public SetRotatorFactory setRotationFactory() {
        return new SetRotatorFactory();
    }

    @Bean
    public CardsShuffler cardsShuffler() {
        return new CardsShuffler();
    }

    @Bean
    public CardsSetBuilder cardsSetBuilder() {
        return new CardsSetBuilder();
    }

    @Bean
    public PredictedScorer predictedScorer() {
        return new PredictedScorer(playersScorer());
    }
}
