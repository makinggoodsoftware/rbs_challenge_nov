package com.mgs.rbsnov.adapter;

import com.mgs.rbsnov.client.entities.Card;
import com.mgs.rbsnov.client.entities.GameStatus;
import com.mgs.rbsnov.client.interfaces.ICardStrategy;
import com.mgs.rbsnov.logic.PlayerLogic;

import java.util.List;

public class AdapterCardStrategy implements ICardStrategy{
    private final PlayerLogic playerLogic;
    private final CardsAdaptor cardsAdaptor;

    public AdapterCardStrategy(PlayerLogic playerLogic, CardsAdaptor cardsAdaptor) {
        this.playerLogic = playerLogic;
        this.cardsAdaptor = cardsAdaptor;
    }

    @Override
    public List<Card> PassCards(GameStatus gameStatus) {
        return playerLogic.discard(cardsAdaptor.extractMyHand(gameStatus));
    }

    @Override
    public Card PlayCard(GameStatus gameStatus, String myTeamName) {
        return null;
    }
}
