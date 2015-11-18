package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.utils.ClosureValue;
import org.apache.log4j.Logger;

import java.util.concurrent.*;
import java.util.function.BiFunction;

public class TimedBoxedAggregator<T, R> {
    private static final Logger LOG = Logger.getLogger(TimedBoxedAggregator.class);
    private final Aggregator<T, R> toExecute;
    private final BiFunction<R, R, R> aggregator;
    private final int timeoutMs;
    private final ExecutorService executor;

    public TimedBoxedAggregator(Aggregator<T, R> toExecute, BiFunction<R, R, R> aggregator, int timeoutMs, ExecutorService executor) {
        this.toExecute = toExecute;
        this.aggregator = aggregator;
        this.timeoutMs = timeoutMs;
        this.executor = executor;
    }

    public R execute (T input){
        ClosureValue<R> closureValue = ClosureValue.empty();
        try {
            Future<R> future = executor.submit(() -> {
                //noinspection InfiniteLoopStatement
                for (int i = 0; true; i++){
                    R result = toExecute.apply(input, i);
                    closureValue.update(current->aggregator.apply(current, result));
                }
            });
            future.get(timeoutMs, TimeUnit.MILLISECONDS);
            throw new IllegalStateException("Should run forever!");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            LOG.warn("timeout!");
            return closureValue.get();
        }
    }

}
