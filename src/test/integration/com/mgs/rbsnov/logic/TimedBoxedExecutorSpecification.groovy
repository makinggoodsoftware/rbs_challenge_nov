package com.mgs.rbsnov.logic

import spock.lang.Specification

import java.util.concurrent.Executors
import java.util.function.Function

class TimedBoxedExecutorSpecification extends Specification {
    TimedBoxedExecutor timedBoxedExecutor

    def "should time box the execution with a timeout" (){
        given:
        timedBoxedExecutor = new TimedBoxedExecutor(
                new Function<Integer, Integer>() {
                    @Override
                    Integer apply(Integer o) {
                        Thread.sleep(120);
                        return o
                    }
                },
                new Function<Integer, Integer>() {
                    @Override
                    Integer apply(Integer o) {
                        return -1
                    }
                },
                100,
                Executors.newSingleThreadExecutor()
        )

        expect:
        timedBoxedExecutor.execute(3) == -1
    }

    def "should not time out" (){
        given:
        timedBoxedExecutor = new TimedBoxedExecutor(
                new Function<Integer, Integer>() {
                    @Override
                    Integer apply(Integer o) {
                        return o
                    }
                },
                new Function<Integer, Integer>() {
                    @Override
                    Integer apply(Integer o) {
                        return -1
                    }
                },
                100,
                Executors.newSingleThreadExecutor()
        )

        expect:
        timedBoxedExecutor.execute(3) == 3
    }
}
