package com.mgs.rbsnov.adapter;

import com.mgs.rbsnov.client.entities.Card;
import com.mgs.rbsnov.client.entities.GameStatus;
import com.mgs.rbsnov.client.interfaces.ICardStrategy;
import com.mgs.rbsnov.domain.DealInProgress;
import com.mgs.rbsnov.logic.PlayerLogic;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Set;

public class AdapterCardStrategy implements ICardStrategy {
    private final static Logger LOGGER = Logger.getLogger(AdapterCardStrategy.class);
    private final PlayerLogic playerLogic;
    private final CardsAdaptor cardsAdaptor;
    private int latestRoundId = -1;
    private boolean cardsPassed = false;

    public AdapterCardStrategy(PlayerLogic playerLogic, CardsAdaptor cardsAdaptor) {
        this.playerLogic = playerLogic;
        this.cardsAdaptor = cardsAdaptor;
    }

    @Override
    public List<Card> PassCards(GameStatus gameStatus) {
        if (latestRoundId > -1) {
            LOGGER.info("Starting new round");
            latestRoundId = -1;
            cardsPassed = false;
        }

        if (cardsPassed) return null;

        Set<com.mgs.rbsnov.domain.Card> cards = cardsAdaptor.extractMyHand(gameStatus);
        LOGGER.info("Passing cards from: " + cards);
        Set<com.mgs.rbsnov.domain.Card> domainDiscards = playerLogic.discard(cards);
        LOGGER.info("Cards to pass: " + domainDiscards);
        cardsPassed = true;
        return cardsAdaptor.toExternalHand(domainDiscards);
    }

    @Override
    public Card PlayCard(GameStatus gameStatus, String myTeamName) {
        int currentDeal = gameStatus.getMyInProgressDeal().getDealNumber();
        if (currentDeal <= latestRoundId) return null;


        this.latestRoundId = currentDeal;
        DealInProgress dealInProgress = cardsAdaptor.deal(gameStatus);
        LOGGER.info("Playing cards, deal in progress: " + dealInProgress);
        Set<com.mgs.rbsnov.domain.Card> inPlay = cardsAdaptor.inPlay(gameStatus, dealInProgress);
        com.mgs.rbsnov.domain.Card card = playerLogic.playCard(
                dealInProgress,
                inPlay,
                cardsAdaptor.extractMyHand(gameStatus),
                null
        );
        LOGGER.info("Playing card " + card);
        return cardsAdaptor.toCardExt(card);
    }
}
