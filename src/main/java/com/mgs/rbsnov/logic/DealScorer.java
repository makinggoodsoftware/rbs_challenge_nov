package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.Deal;
import com.mgs.rbsnov.domain.DealScore;

import static java.util.stream.Stream.of;

public class DealScorer {
    private final CardScorer cardScorer;

    public DealScorer(CardScorer cardScorer) {
        this.cardScorer = cardScorer;
    }

    public DealScore score(Deal toScore) {
        DealScoreInProgress dealScoreInProgress = new DealScoreInProgress(
                toScore.getCard1(),
                0,
                cardScorer.score(toScore.getCard1())
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
        private int currentScore;
        private int currentIndex = 0;

        public DealScoreInProgress(Card startingWinner, int winningCardIndex, int startingScore) {
            this.currentWinner = startingWinner;
            this.winningCardIndex = winningCardIndex;
            currentScore = startingScore;
        }

        public void addScore(int toAdd) {
            currentScore += toAdd;
            currentIndex ++;
        }

        public Card getCurrentWinner() {
            return currentWinner;
        }

        public void changeWinner(Card newWinner, int addingScore) {
            addScore(addingScore);
            currentWinner = newWinner;
            winningCardIndex = currentIndex;
        }

        public DealScore score() {
            return new DealScore(currentWinner, winningCardIndex, currentScore);
        }

        private void apply(Card followingCard) {
            int score = cardScorer.score(followingCard);
            if (followingCard.kills(getCurrentWinner())) {
                changeWinner(followingCard, score);
            } else  {
                addScore(score);
            }
        }
    }
}
