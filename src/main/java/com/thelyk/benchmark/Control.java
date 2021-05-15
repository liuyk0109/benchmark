package com.thelyk.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Control {

    private static final Logger logger = LoggerFactory.getLogger(Control.class);

    private final Object lock = new Object();
    private volatile boolean jobStart;
    private volatile boolean statisticsStart;
    private long testSeconds;

    public boolean isJobStart() {
        return jobStart;
    }

    public void startJob() {
        Thread thread = new Thread(() -> {
            logger.info(String.format("Sleep %d seconds then end jobs", testSeconds));
            ThreadUtils.sleep(TimeUnit.SECONDS, testSeconds);
            jobStart = false;
            synchronized (lock) {
                lock.notifyAll();
            }
        }, "JobWaitThread");
        jobStart = true;
        logger.info("Start jobs");
        thread.start();
    }

    public void waitForJobsEnd() {
        synchronized (lock) {
            while (jobStart) {
                try {
                    logger.info("Wait for jobs end");
                    lock.wait();
                    logger.info("Jobs end");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    public boolean isStatisticsStart() {
        return statisticsStart;
    }

    public void startStatistics() {
        statisticsStart = true;
    }

    public void stopStatistics() {
        statisticsStart = false;
    }

    public void setTestSeconds(long testSeconds) {
        this.testSeconds = testSeconds;
    }
}
