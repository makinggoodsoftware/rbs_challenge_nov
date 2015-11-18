package com.mgs.rbsnov.spring;

import com.google.common.collect.ImmutableMap;
import com.mgs.rbsnov.adapter.AdapterCardStrategy;
import com.mgs.rbsnov.adapter.CardsAdaptor;
import com.mgs.rbsnov.client.interfaces.ICardStrategy;
import com.mgs.rbsnov.client.util.Settings;
import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.CardRiskConfiguration;
import com.mgs.rbsnov.domain.RunningConfiguration;
import com.mgs.rbsnov.logic.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public com.mgs.rbsnov.client.app.Player player (){
        try {
            Settings.init();
            return new com.mgs.rbsnov.client.app.Player("Yogis", "eastex01", cardStrategy());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public CardRiskConfiguration cardRiskConfiguration() {
        return new CardRiskConfiguration(
                50,
                40,
                45,
                2,
                30,
                20,
                10);
    }

    @Bean
    public CardSelector cardSelector (){
        return progressiveCardSelectorFactory().create(4500);
    }

    @Bean
    public ProgressiveCardSelectorFactory progressiveCardSelectorFactory (){
        return new ProgressiveCardSelectorFactory(new ImmutableMap.Builder<Integer, CardSelector> ().
                put(0, new SimpleCardSelector(
                        new RunningConfiguration(1, 2),
                        gameAnalyserII(),
                        cardScorer(),
                        handsFactory(),
                        predictedScorer())
                ).
                put(1, new SimpleCardSelector(
                        new RunningConfiguration(5, 2),
                        gameAnalyserII(),
                        cardScorer(),
                        handsFactory(),
                        predictedScorer())
                ).
                put(2, new SimpleCardSelector(
                        new RunningConfiguration(3, 4),
                        gameAnalyserII(),
                        cardScorer(),
                        handsFactory(),
                        predictedScorer())
                ).
                put(3, new SimpleCardSelector(
                        new RunningConfiguration(5, 5),
                        gameAnalyserII(),
                        cardScorer(),
                        handsFactory(),
                        predictedScorer())
                ).
                put(4, new SimpleCardSelector(
                        new RunningConfiguration(50, 6),
                        gameAnalyserII(),
                        cardScorer(),
                        handsFactory(),
                        predictedScorer())
                ).
                build());
    }

    @Bean
    public ICardStrategy cardStrategy() {
        return new AdapterCardStrategy(playerLogic(), cardsAdaptor(), cardScorer());
    }

    @Bean
    public CardsAdaptor cardsAdaptor() {
        return new CardsAdaptor(dealInProgressFactory(), cardsSetBuilder());
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
    public GameAnalyser gameAnalyserII(){
        return new GameAnalyser(
                gameDeveloper(),
                predictedScorer(),
                dealInProgressFactory(),
                handsFactory());
    }

    @Bean
    public GameSimulator gameSimulator(){
        return new GameSimulator(
                cardsDiscarded(),
                roundDeveloperFactory(),
                handsFactory()
        );
    }

    @Bean
    public RoundDeveloperFactory roundDeveloperFactory() {
        return new RoundDeveloperFactory(
                playerRotator(),
                dealInProgressFactory(),
                playersScorer(),
                handsFactory(),
                heartRules(),
                cardsSetBuilder()
        );
    }

    @Bean
    public HeartRules heartRules() {
        return new HeartRules(playerRotator());
    }

    @Bean
    public CardsDiscarder cardsDiscarded() {
        return new CardsDiscarder(playerRotator());
    }

    @Bean
    public PlayerRotator playerRotator() {
        return new PlayerRotator();
    }

    @Bean
    public PlayerLogic playerLogic() {
        return new PlayerLogic(cardSelector(), cardsRiskEvaluator());
    }

    @Bean
    public CardRiskEvaluator cardsRiskEvaluator() {
        return new CardRiskEvaluator(cardScorer(), cardRiskConfiguration());
    }

    @Bean
    public HandsFactory handsFactory() {
        return new HandsFactory(cardsDealer(), cardsSetBuilder());
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
