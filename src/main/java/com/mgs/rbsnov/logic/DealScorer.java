package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.Deal;
import com.mgs.rbsnov.domain.DealScore;

import java.math.BigDecimal;

import static java.util.stream.Stream.of;

public class DealScorer {
    private final CardScorer cardScorer;

    public DealScorer(CardScorer cardScorer) {
        this.cardScorer = cardScorer;
    }

    public DealScore score(Deal toScore) {
        Integer score = cardScorer.score(toScore.getCard1());
        DealScoreInProgress dealScoreInProgress = new DealScoreInProgress(
                toScore.getCard1(),
                0,
                BigDecimal.valueOf(score)
        );

        of(
                toScore.getCard2(),
                toScore.getCard3(),
                toScore.getCard4()
        ).forEach(dealScoreInProgress::apply);

        return dealScoreInProgress.score();
    }

    private class DealScoreInProgress {
        private Card currentWinner;
        private int winningCardIndex;
        private BigDecimal currentScore;
        private int currentIndex = 0;

        public DealScoreInProgress(Card startingWinner, int winningCardIndex, BigDecimal startingScore) {
            this.currentWinner = startingWinner;
            this.winningCardIndex = winningCardIndex;
            currentScore = startingScore;
        }

        public void addScore(BigDecimal toAdd) {
            currentScore = currentScore.add(toAdd);
            currentIndex ++;
        }

        public Card getCurrentWinner() {
            return currentWinner;
        }

        public void changeWinner(Card newWinner, BigDecimal addingScore) {
            addScore(addingScore);
            currentWinner = newWinner;
            winningCardIndex = currentIndex;
        }

        public DealScore score() {
            return new DealScore(currentWinner, winningCardIndex, currentScore);
        }

        private void apply(Card followingCard) {
            BigDecimal score = BigDecimal.valueOf(cardScorer.score(followingCard));
            if (followingCard.kills(getCurrentWinner())) {
                changeWinner(followingCard, score);
            } else  {
                addScore(score);
            }
        }
    }
}
