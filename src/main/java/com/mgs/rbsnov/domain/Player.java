package com.mgs.rbsnov.domain;

import com.mgs.rbsnov.utils.ClosureValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public Player previousClockwise() {
        return nextClockwise().nextClockwise().nextClockwise();
    }

    public static List<Player> all(Player startingFrom) {
        List<Player> all = new ArrayList<>();
        all.add(startingFrom);
        all.add(startingFrom.nextClockwise());
        all.add(startingFrom.nextClockwise().nextClockwise());
        all.add(startingFrom.nextClockwise().nextClockwise().nextClockwise());
        return all;
    }

    public static List<Player> except(Player startingPlayer) {
        List<Player> except = all(startingPlayer);
        except.remove(startingPlayer);
        return except;
    }
}
