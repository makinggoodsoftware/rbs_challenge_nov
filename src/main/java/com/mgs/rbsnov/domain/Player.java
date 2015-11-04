package com.mgs.rbsnov.domain;

import com.mgs.rbsnov.utils.ClosureValue;

import java.util.stream.IntStream;

public enum Player {
    SOUTH, EAST, NORTH, WEST;

    public Player nextClockwise (){
        switch (this){
            case SOUTH:
                return WEST;
            case WEST:
                return NORTH;
            case NORTH:
                return EAST;
            case EAST:
                return SOUTH;
        }
        throw new IllegalStateException();
    }

    public int distanceTo(Player toPlayer) {
        if (this == toPlayer) return 0;
        Player itPlayer = this;
        for (int i=1; i<=3; i++){
            itPlayer = itPlayer.nextClockwise();
            if (itPlayer == toPlayer) return i;
        }
        throw new IllegalStateException("Can't find the distance to: " + toPlayer + " from: " + this);
    }

    public Player moveClockWise(Integer positions) {
        int jumps = positions % 4;
        ClosureValue<Player> startingFrom = new ClosureValue<>(this);
        IntStream.range(0, jumps).forEach((i)-> startingFrom.update(Player::nextClockwise));
        return startingFrom.get();
    }
}
