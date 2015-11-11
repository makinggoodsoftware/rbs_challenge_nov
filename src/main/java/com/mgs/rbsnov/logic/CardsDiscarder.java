package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CardsDiscarder {
    private final PlayerRotator playerRotator;

    public CardsDiscarder(PlayerRotator playerRotator) {
        this.playerRotator = playerRotator;
    }

    Map<Player, DiscardResult> discard(PlayersLogic playersLogic, Hands initialHands) {
        Map<Player, DiscardResult> discardResult = new HashMap<>();
        Map<Player, Set<Card>> discards = discardingCards(playersLogic, initialHands);
        PlayerRotator.PlayerRotation playerRotation = playerRotator.clockwiseIterator(Player.SOUTH);
        while (playerRotation.hasNext()){
            Player thisPlayer = playerRotation.next();
            Set<Card> initialCards = initialHands.get(thisPlayer);
            Set<Card> receivingCards = discards.get(thisPlayer.nextClockwise());
            Set<Card> discardingCards = discards.get(thisPlayer);

            discardResult.put(thisPlayer, new DiscardResult(initialCards, receivingCards, discardingCards));
        }
        return discardResult;
    }

    Map<Player, Set<Card>> discardingCards(PlayersLogic playersLogic, Hands initialHands) {
        Map<Player, Set<Card>> passingCards = new HashMap<>();
        PlayerRotator.PlayerRotation playerRotation = playerRotator.clockwiseIterator(Player.SOUTH);
        while (playerRotation.hasNext()){
            Player discardingPlayer = playerRotation.next();
            Set<Card> discardingPlayerCards = initialHands.get(discardingPlayer);
            Set<Card> discardedCards = playersLogic.get(discardingPlayer).discard(discardingPlayerCards);
            if (discardedCards.size() != 3) throw new IllegalStateException();
            passingCards.put(discardingPlayer, discardedCards);
        }
        return passingCards;
    }
}
