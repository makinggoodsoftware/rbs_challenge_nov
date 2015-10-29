package com.mgs.rbsnov;

import java.util.function.Function;

public class ClosureValue<T> {
    private T value;

    public ClosureValue(T value) {
        this.value = value;
    }

    public void update (Function<T, T> updater){
        this.value = updater.apply(this.value);
    }

    public T get() {
        return value;
    }
}
