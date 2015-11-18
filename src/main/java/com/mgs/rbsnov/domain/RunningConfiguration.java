package com.mgs.rbsnov.domain;

public class RunningConfiguration {
    private final int scenariosToRun;
    private final int levelsDownDeep;

    public RunningConfiguration(int scenariosToRun, int levelsDownDeep) {
        this.scenariosToRun = scenariosToRun;
        this.levelsDownDeep = levelsDownDeep;
    }

    public int getScenariosToRun() {
        return scenariosToRun;
    }

    public int getLevelsDownDeep() {
        return levelsDownDeep;
    }

    @Override
    public String toString() {
        return "RunningConfiguration{" +
                "scenariosToRun=" + scenariosToRun +
                ", levelsDownDeep=" + levelsDownDeep +
                '}';
    }
}
