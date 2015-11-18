package com.mgs.rbsnov.adapter;

import com.mgs.rbsnov.client.entities.Card;
import com.mgs.rbsnov.client.entities.GameStatus;
import com.mgs.rbsnov.client.interfaces.ICardStrategy;
import com.mgs.rbsnov.domain.DealInProgress;
import com.mgs.rbsnov.logic.PlayerLogic;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AdapterCardStrategy implements ICardStrategy{
    private final PlayerLogic playerLogic;
    private final CardsAdaptor cardsAdaptor;

    public AdapterCardStrategy(PlayerLogic playerLogic, CardsAdaptor cardsAdaptor) {
        this.playerLogic = playerLogic;
        this.cardsAdaptor = cardsAdaptor;
    }

    @Override
    public List<Card> PassCards(GameStatus gameStatus) {
        Set<com.mgs.rbsnov.domain.Card> cards = cardsAdaptor.extractMyHand(gameStatus);
        Set<com.mgs.rbsnov.domain.Card> domainDiscards = playerLogic.discard(cards);
        return cardsAdaptor.toExternalHand (domainDiscards);
    }

    @Override
    public Card PlayCard(GameStatus gameStatus, String myTeamName) {
        DealInProgress dealInProgress = cardsAdaptor.deal(gameStatus);
        Set<com.mgs.rbsnov.domain.Card> inPlay = cardsAdaptor.inPlay(gameStatus, dealInProgress);
        com.mgs.rbsnov.domain.Card card = playerLogic.playCard(
                dealInProgress,
                inPlay,
                cardsAdaptor.extractMyHand(gameStatus),
                null
        );
        return cardsAdaptor.toCardExt(card);
    }
}
