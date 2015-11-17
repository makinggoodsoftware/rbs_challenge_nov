package com.mgs.rbsnov.domain;

public class CardRiskConfiguration {
    private final int queenOfSpadesRisk;
    private final int kingOfSpadesRisk;
    private final int aceOfSpadesRisk;
    private final int positivieScoringBaseCardMultiplier;
    private final int suitScoreJustOne;
    private final int suitScoreJustTwo;
    private final int suitScoreJustThree;

    public CardRiskConfiguration(int queenOfSpadesRisk, int kingOfSpadesRisk, int aceOfSpadesRisk, int positivieScoringBaseCardMultiplier, int suitScoreJustOne, int suitScoreJustTwo, int suitScoreJustThree) {
        this.queenOfSpadesRisk = queenOfSpadesRisk;
        this.kingOfSpadesRisk = kingOfSpadesRisk;
        this.aceOfSpadesRisk = aceOfSpadesRisk;
        this.positivieScoringBaseCardMultiplier = positivieScoringBaseCardMultiplier;
        this.suitScoreJustOne = suitScoreJustOne;
        this.suitScoreJustTwo = suitScoreJustTwo;
        this.suitScoreJustThree = suitScoreJustThree;
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

    public int getSuitScoreJustOne() {
        return suitScoreJustOne;
    }

    public int getSuitScoreJustTwo() {
        return suitScoreJustTwo;
    }

    public int getSuitScoreJustThree() {
        return suitScoreJustThree;
    }
}
