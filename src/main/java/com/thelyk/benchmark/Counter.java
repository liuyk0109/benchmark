package com.thelyk.benchmark;

import java.util.concurrent.atomic.LongAdder;

public class Counter {

    private final LongAdder totalSend = new LongAdder();
    private final LongAdder totalRecv = new LongAdder();
    private final LongAdder totalSuccess = new LongAdder();
    private final LongAdder totalFail = new LongAdder();

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

    public long getTotalSuccess() {
        return totalSuccess.longValue();
    }

    public void incrSuccess() {
        totalSuccess.increment();
    }

    public long getTotalFail() {
        return totalFail.longValue();
    }

    public void incrFail() {
        totalFail.increment();
    }
}
