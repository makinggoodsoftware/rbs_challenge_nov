package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.utils.ClosureValue;
import com.mgs.rbsnov.domain.*;

import java.math.BigDecimal;
import java.util.Collection;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;

public class PlayersScorer {
    public static final BigDecimal PRECISSION_ZERO = new BigDecimal("0.0000000000");
    private final DealScorer dealScorer;

    public PlayersScorer(DealScorer dealScorer) {
        this.dealScorer = dealScorer;
    }

    public FinishedDeal score(Player startingPlayer, Deal deal) {
        DealScore dealScore = dealScorer.score(deal);
        int winningCardIndex = dealScore.getWinningCardIndex();
        Player winningPlayer = startingPlayer.moveClockWise(winningCardIndex);
        PlayersScore score = score(winningPlayer, dealScore.getPoints());
        return new FinishedDeal(deal, score, winningPlayer, startingPlayer);
    }

    public FinishedDeal score(DealInProgress dealInProgress) {
        if (!dealInProgress.isCompleted()) throw new IllegalStateException();

        Deal deal = new Deal(
                dealInProgress.getLeadingCard().get(),
                dealInProgress.getFollowingCards().get(0),
                dealInProgress.getFollowingCards().get(1),
                dealInProgress.getFollowingCards().get(2)
        );

        return score(dealInProgress.getStartingPlayer(), deal);
    }


    public PlayersScore score(Player toPlayer, BigDecimal points) {
        switch (toPlayer){
            case EAST:
                return new PlayersScore(ZERO, points, ZERO, ZERO);
            case SOUTH:
                return new PlayersScore(points, ZERO, ZERO, ZERO);
            case WEST:
                return new PlayersScore(ZERO, ZERO, ZERO, points);
            case NORTH:
                return new PlayersScore(ZERO, ZERO, points, ZERO);
        }

        throw new IllegalStateException();
    }

    public PlayersScore average(Collection<PlayersScore> scores) {
        ClosureValue<BigDecimal> southTotal = new ClosureValue<>(PRECISSION_ZERO);
        ClosureValue<BigDecimal> eastTotal = new ClosureValue<>(PRECISSION_ZERO);
        ClosureValue<BigDecimal> northTotal = new ClosureValue<>(PRECISSION_ZERO);
        ClosureValue<BigDecimal> westTotal = new ClosureValue<>(PRECISSION_ZERO);
        scores.forEach((score)->{
            southTotal.update((current)-> current = current.add(score.getSouthScore()));
            eastTotal.update((current)-> current = current.add(score.getEastScore()));
            northTotal.update((current)-> current = current.add(score.getNorthScore()));
            westTotal.update((current)-> current = current.add(score.getWestScore()));
        });

        BigDecimal scoresSize = BigDecimal.valueOf(scores.size());
        return new PlayersScore(
                southTotal.get().divide(scoresSize, HALF_EVEN) ,
                eastTotal.get().divide(scoresSize, HALF_EVEN),
                northTotal.get().divide(scoresSize, HALF_EVEN),
                westTotal.get().divide(scoresSize, HALF_EVEN)
        );
    }

    public PlayersScore add(PlayersScore left, PlayersScore right) {
        return new PlayersScore(
                left.getSouthScore().add(right.getSouthScore()),
                left.getEastScore().add(right.getEastScore()),
                left.getNorthScore().add(right.getNorthScore()),
                left.getWestScore().add(right.getWestScore())
        );
    }
}
