package org.amateras_smp.amatweaks.impl.util;

import java.util.LinkedList;

public class LimitedQueue<E> extends LinkedList<E> {
    private int maxSize;

    public LimitedQueue(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize is lower than 1.");
        }
        this.maxSize = maxSize;
    }

    public void setMaxSize(int newSize) {
        if (newSize <= 0) {
            throw new IllegalArgumentException("maxSize is lower than 1.");
        }
        this.maxSize = newSize;

        while (size() > maxSize) {
            poll();
        }
    }

    @Override
    public boolean add(E element) {
        System.out.println("limited list : add() called");
        if (size() >= maxSize) {
            poll();
        }
        return super.add(element);
    }

    @Override
    public boolean offer(E element) {
        if (size() >= maxSize) {
            poll();
        }
        return super.offer(element);
    }
    // addは例外を投げるがofferは例外を投げない。必ず要素挿入時にサイズのチェックを行うようにしてあるのでofferじゃなくてaddだけ使えば基本は大丈夫
}

