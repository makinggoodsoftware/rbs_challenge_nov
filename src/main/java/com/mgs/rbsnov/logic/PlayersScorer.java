package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.utils.ClosureValue;
import com.mgs.rbsnov.domain.*;

import java.util.Collection;
import java.util.List;

public class PlayersScorer {
    private final DealScorer dealScorer;

    public PlayersScorer(DealScorer dealScorer) {
        this.dealScorer = dealScorer;
    }

    public PlayersScore score(Player startingPlayer, Deal deal) {
        DealScore dealScore = dealScorer.score(deal);
        int winningCardIndex = dealScore.getWinningCardIndex();
        Player winningPlayer = startingPlayer.moveClockWise(winningCardIndex);
        return score (winningPlayer, dealScore.getPoints());
    }

    public PlayersScore score(Player toPlayer, int points) {
        switch (toPlayer){
            case EAST:
                return new PlayersScore(0, points, 0, 0);
            case SOUTH:
                return new PlayersScore(points, 0, 0, 0);
            case WEST:
                return new PlayersScore(0, 0, 0, points);
            case NORTH:
                return new PlayersScore(0, 0, points, 0);
        }

        throw new IllegalStateException();
    }


    public PlayersScore average(Collection<PlayersScore> scores) {
        ClosureValue<Float> southTotal = new ClosureValue<>(0.0f);
        ClosureValue<Float> eastTotal = new ClosureValue<>(0.0f);
        ClosureValue<Float> northTotal = new ClosureValue<>(0.0f);
        ClosureValue<Float> westTotal = new ClosureValue<>(0.0f);
        scores.forEach((score)->{
            southTotal.update((current)-> current+=score.getSouthScore());
            eastTotal.update((current)-> current+=score.getEastScore());
            northTotal.update((current)-> current+=score.getNorthScore());
            westTotal.update((current)-> current+=score.getWestScore());
        });

        return new PlayersScore(
                southTotal.get() / scores.size(),
                eastTotal.get() / scores.size(),
                northTotal.get() / scores.size(),
                westTotal.get() / scores.size()
        );
    }

    public PlayersScore add(PlayersScore left, PlayersScore right) {
        return new PlayersScore(
                left.getSouthScore() + right.getSouthScore(),
                left.getEastScore() + right.getEastScore(),
                left.getNorthScore() + right.getNorthScore(),
                left.getWestScore() + right.getWestScore()
        );
    }
}
