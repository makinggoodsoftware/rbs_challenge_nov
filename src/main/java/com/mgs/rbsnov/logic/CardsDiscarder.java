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
        Map<Player, DiscardDecision> discards = discardingCards(playersLogic, initialHands);
        PlayerRotator.PlayerRotation playerRotation = playerRotator.clockwiseIterator(Player.SOUTH);
        while (playerRotation.hasNext()){
            Player thisPlayer = playerRotation.next();
            Set<Card> initialCards = initialHands.get(thisPlayer);
            DiscardDecision receivingDiscardDecision = discards.get(thisPlayer.nextClockwise());
            DiscardDecision discardingDecision = discards.get(thisPlayer);

            discardResult.put(thisPlayer, new DiscardResult(initialCards, receivingDiscardDecision.getToDiscard(), discardingDecision.getToDiscard(), discardingDecision.isShootingTheMoon()));
        }
        return discardResult;
    }

    Map<Player, DiscardDecision> discardingCards(PlayersLogic playersLogic, Hands initialHands) {
        Map<Player, DiscardDecision> passingCards = new HashMap<>();
        PlayerRotator.PlayerRotation playerRotation = playerRotator.clockwiseIterator(Player.SOUTH);
        while (playerRotation.hasNext()){
            Player discardingPlayer = playerRotation.next();
            Set<Card> discardingPlayerCards = initialHands.get(discardingPlayer);
            DiscardDecision discardDecision = playersLogic.get(discardingPlayer).discard(discardingPlayerCards);
            if (discardDecision.getToDiscard().size() != 3) throw new IllegalStateException();
            passingCards.put(discardingPlayer, discardDecision);
        }
        return passingCards;
    }
}
