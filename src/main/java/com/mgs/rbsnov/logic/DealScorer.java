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
        private int currentScore;

        public DealScoreInProgress(Card startingWinner, int startingScore) {
            this.currentWinner = startingWinner;
            currentScore = startingScore;
        }

        public void addScore(int toAdd) {
            currentScore += toAdd;
        }

        public Card getCurrentWinner() {
            return currentWinner;
        }

        public void changeWinner(Card newWinner, int addingScore) {
            currentWinner = newWinner;
            addScore(addingScore);
        }

        public DealScore score() {
            return new DealScore(currentWinner, currentScore);
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
