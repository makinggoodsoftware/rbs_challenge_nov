package com.mgs.rbsnov.logic

import com.google.common.base.Stopwatch
import spock.lang.Specification

import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit
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

    def "should never take longer than the time out" (){
        when:
        List<Long> executionTimes = new ArrayList<>();
        timedBoxedExecutor = new TimedBoxedAggregator(
                new Aggregator<Integer, Integer>() {
                    @Override
                    Integer apply(Integer o, int index) {
                        Thread.sleep(30 * (index * ThreadLocalRandom.current().nextInt(0, 20 + 1)));
                        return index
                    }
                },
                new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    Integer apply(Integer prev, Integer o) {
                        return o
                    }
                },
                1000
        )

        then:
        (1..1000).each {currentIt ->
            Stopwatch stopwatch = Stopwatch.createStarted();
            timedBoxedExecutor.execute(3) == 2
            Long lasted = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
            if (lasted > 1000 + 500) throw new IllegalStateException("Took too much [" + currentIt + "]" + lasted + "ms ")
        }
        true
    }

}
