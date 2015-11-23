package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.utils.ClosureValue;
import com.mgs.rbsnov.domain.*;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Collection;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;

public class FinishedDealScorer {
    private static final Logger LOG = Logger.getLogger(FinishedDealScorer.class);
    public static final BigDecimal PRECISSION_ZERO = new BigDecimal("0.00000000");
    private final DealScorer dealScorer;
    private final CardScorer cardScorer;

    public FinishedDealScorer(DealScorer dealScorer, CardScorer cardScorer) {
        this.dealScorer = dealScorer;
        this.cardScorer = cardScorer;
    }

    public FinishedDeal score(PlayersScore currentScore, DealInProgress dealInProgress) {
        if (!dealInProgress.isCompleted()) throw new IllegalStateException();

        Deal deal = new Deal(
                dealInProgress.getLeadingCard().get(),
                dealInProgress.getFollowingCards().get(0),
                dealInProgress.getFollowingCards().get(1),
                dealInProgress.getFollowingCards().get(2)
        );

        return score(currentScore, dealInProgress.getStartingPlayer(), deal);
    }

    public FinishedDeal score(PlayersScore currentScore, Player startingPlayer, Deal deal) {
        DealScore dealScore = dealScorer.score(deal);
        int winningCardIndex = dealScore.getWinningCardIndex();
        Player winningPlayer = startingPlayer.moveClockWise(winningCardIndex);
        PlayersScore score = score(winningPlayer, dealScore.getPoints());

        PlayersScore heartsScore = addHearts(currentScore, winningPlayer, deal);
        return new FinishedDeal(deal, score, heartsScore, winningPlayer, startingPlayer);
    }

    private PlayersScore addHearts(PlayersScore currentScore, Player winningPlayer, Deal deal) {
        int totalScore = 0;
        for (int i=0; i<4; i++){
            Card card = deal.getCard(i);
            if (card == Card.QUEEN_OF_SPADES || card.getSuit() == Suit.HEARTS) totalScore += cardScorer.score(card);
        }

        if (totalScore == 0) return currentScore;
//        LOG.info("Adding to hearts score: " + winningCard);
        BigDecimal score = BigDecimal.valueOf(totalScore);
        PlayersScore playersScore = new PlayersScore(
                winningPlayer == Player.SOUTH ? currentScore.getSouthScore().add(score) : currentScore.get(Player.SOUTH),
                winningPlayer == Player.EAST ? currentScore.getEastScore().add(score) : currentScore.get(Player.EAST),
                winningPlayer == Player.NORTH ? currentScore.getNorthScore().add(score) : currentScore.get(Player.NORTH),
                winningPlayer == Player.WEST ? currentScore.getWestScore().add(score) : currentScore.get(Player.WEST)
        );
        return playersScore;
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
