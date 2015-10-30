package com.mgs.rbsnov.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetRotatorFactory {
    @SuppressWarnings("UnusedParameters")
    public <C> SetRotator<C> newSetRotator(int numberOfPlayers, Class<C> type) {
        List<Set<C>> toRotate = new ArrayList<>();
        for (int i=0; i<numberOfPlayers; i++){
            toRotate.add(new HashSet<>());
        }
        return new SetRotator<>(toRotate);
    }
}
