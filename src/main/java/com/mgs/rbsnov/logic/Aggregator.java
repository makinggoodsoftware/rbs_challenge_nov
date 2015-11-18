package com.mgs.rbsnov.logic;

@FunctionalInterface
public interface Aggregator <T, R> {
    R apply(T input, int currentIterationIndex);
}
