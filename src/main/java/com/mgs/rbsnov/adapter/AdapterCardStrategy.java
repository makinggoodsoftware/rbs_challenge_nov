package com.mgs.rbsnov.adapter;

import com.mgs.rbsnov.client.entities.Card;
import com.mgs.rbsnov.client.entities.CardPoint;
import com.mgs.rbsnov.client.entities.Deal;
import com.mgs.rbsnov.client.entities.GameStatus;
import com.mgs.rbsnov.client.interfaces.ICardStrategy;
import com.mgs.rbsnov.domain.DealInProgress;
import com.mgs.rbsnov.logic.CardScorer;
import com.mgs.rbsnov.logic.PlayerLogic;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Set;

public class AdapterCardStrategy implements ICardStrategy {
    private final static Logger LOGGER = Logger.getLogger(AdapterCardStrategy.class);
    private final PlayerLogic playerLogic;
    private final CardsAdaptor cardsAdaptor;
    private final CardScorer cardScorer;

    private int latestRoundId = -1;
    private boolean cardsPassed = false;

    public AdapterCardStrategy(PlayerLogic playerLogic, CardsAdaptor cardsAdaptor, CardScorer cardScorer) {
        this.playerLogic = playerLogic;
        this.cardsAdaptor = cardsAdaptor;
        this.cardScorer = cardScorer;
    }

    @Override
    public List<Card> PassCards(GameStatus gameStatus) {
        if (latestRoundId > -1) {
            LOGGER.info("Starting new round");
            cardsPassed = false;
        }

        if (cardsPassed) return null;

        cardScorer.removeTempScores();
        List<CardPoint> cardPoints = gameStatus.getRoundParameters().getCardPoints();
        for (CardPoint cardPoint : cardPoints) {
            com.mgs.rbsnov.domain.Card card = cardsAdaptor.toDomainCard(cardPoint.getCard());
            cardScorer.addTempScore(card, cardPoint.getPoint());
        }

        Set<com.mgs.rbsnov.domain.Card> cards = cardsAdaptor.extractMyHand(gameStatus);
        LOGGER.info("Passing cards from: " + cards);
        Set<com.mgs.rbsnov.domain.Card> domainDiscards = playerLogic.discard(cards);
        LOGGER.info("Cards to pass: " + domainDiscards);
        cardsPassed = true;
        latestRoundId = -1;
        return cardsAdaptor.toExternalHand(domainDiscards);
    }

    @Override
    public Card PlayCard(GameStatus gameStatus, String myTeamName) {
        Deal myInProgressDeal = gameStatus.getMyInProgressDeal();
        int currentDeal = myInProgressDeal.getDealNumber();
        if (currentDeal <= latestRoundId) return null;


        this.latestRoundId = currentDeal;
        DealInProgress dealInProgress = cardsAdaptor.deal(gameStatus);
        LOGGER.info("Playing cards, deal in progress: " + dealInProgress);
        Set<com.mgs.rbsnov.domain.Card> myHand = cardsAdaptor.extractMyHand(gameStatus);
        Set<com.mgs.rbsnov.domain.Card> inPlay = cardsAdaptor.inPlay(gameStatus, dealInProgress, myHand);

        if ((inPlay.size() + myHand.size() + dealInProgress.getCardSize()) % 4 != 0) {
            throw new IllegalStateException();
        }
        com.mgs.rbsnov.domain.Card card = playerLogic.playCard(
                dealInProgress,
                inPlay,
                myHand,
                cardsAdaptor.discardedCards(gameStatus),
                cardsAdaptor.missingDeals (gameStatus.getMyGameDeals(), myInProgressDeal),
                cardsAdaptor.currentScore (gameStatus.getMyGameDeals())
        );

        LOGGER.info("Playing card " + card);
        return cardsAdaptor.toCardExt(card);
    }
}
