package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Player;

public class PlayerRotator {
    public PlayerRotation clockwiseIterator(Player from) {
        return new PlayerRotation(from);
    }

    class PlayerRotation {
        private Player from;
        private int iteratations = 0;

        public PlayerRotation(Player from) {
            this.from = from;
        }

        public boolean hasNext() {
            return iteratations < 4;
        }

        public Player next() {
            Player next = from;
            this.from = next.nextClockwise();
            this.iteratations ++;
            return next;
        }
    }
}
