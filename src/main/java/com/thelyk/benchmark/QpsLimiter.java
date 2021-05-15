package com.thelyk.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class QpsLimiter {

    private static final Logger logger = LoggerFactory.getLogger(QpsLimiter.class);

    private final long ns;
    private final Control ctl;
    private final AtomicLong tokens = new AtomicLong();
    private final Object lock = new Object();

    public QpsLimiter(long qps, Control ctl) {
        ns = (long) (1e9 / qps);
        this.ctl = ctl;
    }

    public void startGenerateToken() {
        new Thread(() -> {
            while (ctl.isJobStart()) {
                tokens.incrementAndGet();
                ThreadUtils.sleep(TimeUnit.NANOSECONDS, ns);
            }
        }, "GenerateTokenThread").start();
        logger.debug("Every {} ns to generate a token", ns);
    }

    public boolean tryGetToken() {
        if (tokens.get() > 0) {
            synchronized (lock) {
                if (tokens.get() > 0) {
                    tokens.decrementAndGet();
                    return true;
                }
            }
        }
        return false;
    }
}
