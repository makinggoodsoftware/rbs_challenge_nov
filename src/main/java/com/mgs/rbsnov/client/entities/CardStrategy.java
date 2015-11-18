package com.mgs.rbsnov.client.entities;

import com.mgs.rbsnov.client.interfaces.ICardStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardStrategy implements ICardStrategy {
	 

	public List<Card> PassCards(GameStatus gameStatus) {
		
		 // This will pass the first 3 cards in your hand
		return gameStatus.getMyInitialHand().subList(0,3);
	}

	public Card PlayCard(GameStatus gameStatus, String myTeamName) {
		//Below mention code is just an example and its really an ugly strategy to win the competition. You would definitely like to improve this?
        // Hints
        // 1. https://en.wikibooks.org/wiki/Card_Games/Hearts/Strategy
        // 2. http://boardgames.about.com/od/hearts/a/Basic-Hearts-Strategy.htm
        // 3. http://www.dummies.com/how-to/content/playing-hearts-game-strategies.html
		 List<Card> temp = new ArrayList<Card>();

         for (Card card : gameStatus.getMyCurrentHand())
         {
             if (card.getSuit().equals(gameStatus.getMyInProgressDeal().getSuitType()))
                 temp.add(card);
         }

         //check if there are any cards which have the same suit type as of the current deal
         if (temp.size() > 0)
         {
       	  Collections.sort(temp, new CustomComparator());             
             return temp.get(0);
         }
         else
         { 
        	 List<Card> currentHand = new ArrayList<Card>(); 
        	currentHand.addAll(gameStatus.getMyCurrentHand());
             //find the lowest of other cards
       	  	Collections.sort(currentHand, new CustomComparator());              
            return currentHand.get(0);
         }
	}

}
