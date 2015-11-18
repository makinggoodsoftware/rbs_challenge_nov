package com.mgs.rbsnov.logic;

import org.apache.log4j.Logger;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class TimedBoxedExecutor <T, R> {
    private static final Logger LOG = Logger.getLogger(TimedBoxedExecutor.class);
    private final Consumer<T> toExecute;
    private final Function<T, R> onTimeout;
    private final int timeoutMs;
    private final ExecutorService executor;

    public TimedBoxedExecutor(Consumer<T> toExecute, Function<T, R> onTimeout, int timeoutMs, ExecutorService executor) {
        this.toExecute = toExecute;
        this.onTimeout = onTimeout;
        this.timeoutMs = timeoutMs;
        this.executor = executor;
    }

    public R execute (T input){
        try {
            Future<R> future = executor.submit(dummyFunction(toExecute, input));
            future.get(timeoutMs, TimeUnit.MILLISECONDS);
            throw new IllegalStateException("Should run forever!");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            LOG.warn("timeout!");
            return onTimeout.apply(input);
        }
    }

    private Callable<R> dummyFunction(Consumer<T> toExecute, T input) {
        return () -> {
            toExecute.accept(input);
            throw new IllegalStateException("Should run forever!");
        };
    }
}
