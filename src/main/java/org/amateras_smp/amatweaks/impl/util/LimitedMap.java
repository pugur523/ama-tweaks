package org.amateras_smp.amatweaks.impl.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LimitedMap<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;

    public LimitedMap(int maxSize) {
        super(10, 0.8f, true);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}

