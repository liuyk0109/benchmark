package com.thelyk.benchmark;

import java.util.concurrent.atomic.LongAdder;

public class Counter {

    private final LongAdder totalSend = new LongAdder();
    private final LongAdder totalRecv = new LongAdder();

    public long getTotalSend() {
        return totalSend.longValue();
    }

    public void incrSend() {
        totalSend.increment();
    }

    public long getTotalRecv() {
        return totalRecv.longValue();
    }

    public void incrRecv() {
        totalRecv.increment();
    }
}
