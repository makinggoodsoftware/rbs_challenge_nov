package com.mgs.rbsnov.logic

import spock.lang.Specification

import java.util.function.BiFunction

class TimedBoxedAggregatorSpecification extends Specification {
    TimedBoxedAggregator timedBoxedExecutor

    def "should time box the execution with a timeout" (){
        given:
        timedBoxedExecutor = new TimedBoxedAggregator(
                new Aggregator<Integer, Integer>() {
                    @Override
                    Integer apply(Integer o, int index) {
                        Thread.sleep(120);
                        return 0
                    }
                },
                new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    Integer apply(Integer prev, Integer o) {
                        throw new IllegalStateException("Should never get called")
                    }
                },
                100
        )

        expect:
        timedBoxedExecutor.execute(3) == null
    }

    def "should not time out" (){
        given:
        timedBoxedExecutor = new TimedBoxedAggregator(
                new Aggregator<Integer, Integer>() {
                    @Override
                    Integer apply(Integer o, int index) {
                        Thread.sleep(30);
                        return index
                    }
                },
                new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    Integer apply(Integer prev, Integer o) {
                        return o
                    }
                },
                100
        )

        expect:
        timedBoxedExecutor.execute(3) == 2
    }
}
