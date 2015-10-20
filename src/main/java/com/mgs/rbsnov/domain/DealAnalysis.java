package com.mgs.rbsnov.domain;

public class DealAnalysis {
    private final Deal deal;
    private final DealScore score;

    public DealAnalysis(Deal deal, DealScore score) {
        this.deal = deal;
        this.score = score;
    }
}
