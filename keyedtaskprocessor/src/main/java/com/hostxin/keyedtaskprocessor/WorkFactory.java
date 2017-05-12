
package com.hostxin.keyedtaskprocessor;


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkFactory
    implements ThreadFactory {
    private final AtomicInteger mAutomic = new AtomicInteger();
    private final String mName;

    public WorkFactory(String name) {
        this.mName = name;
        this.mAutomic.set(0);
    }

    public final Thread newThread(Runnable runnable) {
        int n = this.mAutomic.incrementAndGet();
        Thread thread = new Thread(runnable, this.mName + "-" + n);
        return thread;
    }
}

