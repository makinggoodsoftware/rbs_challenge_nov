package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.Hands;
import com.mgs.rbsnov.domain.Player;

public class HeartRules {
    private final PlayerRotator playerRotator;

    public HeartRules(PlayerRotator playerRotator) {
        this.playerRotator = playerRotator;
    }

    public boolean cardsToPlayRemaining(Hands hands) {
        return hands.get(Player.SOUTH).size() > 0;
    }


    public Player findStartingPlayer(Hands hands) {
        PlayerRotator.PlayerRotation playerRotation = playerRotator.clockwiseIterator(Player.SOUTH);
        while (playerRotation.hasNext()){
            Player next = playerRotation.next();
            if (hands.get(next).contains(Card.TWO_OF_CLUBS)) return next;
        }

        throw new IllegalStateException();
    }
}
