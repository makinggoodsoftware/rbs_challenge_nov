package interfaces;

import java.util.List;

import entities.Card;
import entities.GameStatus;

public interface ICardStrategy {
	List<Card> PassCards(GameStatus gameStatus);

    Card PlayCard(GameStatus gameStatus, String myTeamName);
}
