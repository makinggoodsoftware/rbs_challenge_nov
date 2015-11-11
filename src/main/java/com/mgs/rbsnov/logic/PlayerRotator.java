package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Player;

public class PlayerRotator {
    public PlayerRotation clockwiseIterator(Player from) {
        return new PlayerRotation();
    }

    class PlayerRotation {
        public boolean hasNext() {
            return false;
        }

        public Player next() {
            return null;
        }
    }
}
