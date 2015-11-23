package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.GameScore;
import com.mgs.rbsnov.domain.PlayersScore;
import com.mgs.rbsnov.domain.RoundResult;

import java.util.List;

public class GameScorer {
    GameScore finalScores(List<RoundResult> roundResults) {
        GameScore finalScores = GameScore.zeros();

        for (RoundResult roundResult : roundResults) {
            PlayersScore cardsScore = roundResult.getFinishedDeal().getCardsScore();
            PlayersScore heartsScore = roundResult.getFinishedDeal().getHeartsScore();

            finalScores = finalScores.add (cardsScore, heartsScore);
        }

        return finalScores;
    }
}
