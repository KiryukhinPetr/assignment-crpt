package com.crpt.services;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

public class ThreadSafeSImpleCache<K, V> {
    private ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();
    private ExecutorService executor
            = Executors.newSingleThreadExecutor();

    public Future<V> compute(K k, Function<K, V> f) {
        return executor.submit(() ->
                cache.computeIfAbsent(k, (Function<? super K, ? extends V>) f.apply(k))
        );

    }
}
