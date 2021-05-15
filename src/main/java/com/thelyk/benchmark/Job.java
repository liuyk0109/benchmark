package com.thelyk.benchmark;

import java.util.concurrent.ExecutorService;

public class Job implements Runnable {

    private final ExecutorService jobThreadPool;
    private final Counter counter;
    private final Control ctl;
    private final QpsLimiter limiter;

    public Job(ExecutorService jobThreadPool, Counter counter, Control ctl, QpsLimiter limiter) {
        this.jobThreadPool = jobThreadPool;
        this.counter = counter;
        this.ctl = ctl;
        this.limiter = limiter;
    }

    @Override
    public void run() {
        while (ctl.isJobStart()) {
            if (limiter.tryGetToken()) {
                counter.incrSend();
                jobThreadPool.submit(() -> {
                    doJob();
                    counter.incrRecv();
                });
            }
        }
    }

    private void doJob() {
        // TODO send http request
    }
}
