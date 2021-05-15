package com.thelyk.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class QpsLimiter {

    private static final Logger logger = LoggerFactory.getLogger(QpsLimiter.class);

    private final long qps;
    private final Control ctl;
    private final AtomicLong tokens = new AtomicLong();

    public QpsLimiter(long qps, Control ctl) {
        this.qps = qps;
        this.ctl = ctl;
    }

    public void startGenerateToken() {
        new Thread(() -> {
            while (ctl.isJobStart()) {
                tokens.addAndGet(qps);
                ThreadUtils.sleep(TimeUnit.SECONDS, 1L);
            }
        }, "GenerateTokenThread").start();
        logger.debug("Generate {} tokens per second", qps);
    }

    public boolean tryGetToken() {
        long counts = tokens.decrementAndGet();
        if (counts >= 0) {
            return true;
        } else {
            tokens.incrementAndGet();
            return false;
        }
    }
}
