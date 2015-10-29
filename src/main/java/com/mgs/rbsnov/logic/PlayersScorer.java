package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.*;

import java.util.List;

public class PlayersScorer {
    private final DealScorer dealScorer;

    public PlayersScorer(DealScorer dealScorer) {
        this.dealScorer = dealScorer;
    }

    public PlayersScore score(Player startingPlayer, Deal deal) {
        DealScore dealScore = dealScorer.score(deal);
        Card winningCard = dealScore.getWinner();
        Integer winningCardIndexInDeal = deal.getCardPosition(winningCard).orElseThrow(IllegalStateException::new);
        Player winningPlayer = startingPlayer.moveClockWise(winningCardIndexInDeal);
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


    public PlayersScore average(List<PlayersScore> scores) {
        return null;
    }
}
