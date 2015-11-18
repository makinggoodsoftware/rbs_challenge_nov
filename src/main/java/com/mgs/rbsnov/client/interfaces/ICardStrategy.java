package com.mgs.rbsnov.client.interfaces;

import java.util.List;

import com.mgs.rbsnov.client.entities.Card;
import com.mgs.rbsnov.client.entities.GameStatus;

public interface ICardStrategy {
	List<Card> PassCards(GameStatus gameStatus);

    Card PlayCard(GameStatus gameStatus, String myTeamName);
}
