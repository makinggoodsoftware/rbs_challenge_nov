package com.mgs.rbsnov.domain;

public class CardRiskConfiguration {
    private final int queenOfSpadesRisk;
    private final int kingOfSpadesRisk;
    private final int aceOfSpadesRisk;
    private final int positivieScoringBaseCardMultiplier;

    public CardRiskConfiguration(int queenOfSpadesRisk, int kingOfSpadesRisk, int aceOfSpadesRisk, int positivieScoringBaseCardMultiplier) {
        this.queenOfSpadesRisk = queenOfSpadesRisk;
        this.kingOfSpadesRisk = kingOfSpadesRisk;
        this.aceOfSpadesRisk = aceOfSpadesRisk;
        this.positivieScoringBaseCardMultiplier = positivieScoringBaseCardMultiplier;
    }

    public int getQueenOfSpadesRisk() {
        return queenOfSpadesRisk;
    }

    public int getKingOfSpadesRisk() {
        return kingOfSpadesRisk;
    }

    public int getAceOfSpadesRisk() {
        return aceOfSpadesRisk;
    }

    public int getPositivieScoringBaseCardMultiplier() {
        return positivieScoringBaseCardMultiplier;
    }
}
