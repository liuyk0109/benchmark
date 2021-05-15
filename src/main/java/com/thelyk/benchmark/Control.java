package com.thelyk.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Control {

    private static final Logger logger = LoggerFactory.getLogger(Control.class);

    private final Object lock = new Object();
    private volatile boolean jobStart;
    private volatile boolean statisticsStart;
    private volatile long jobStartNanoTime;
    private volatile long jobStopNanoTime;
    private volatile long statisticsStartNanoTime;
    private volatile long statisticsStopNanoTime;

    public boolean isJobStart() {
        return jobStart;
    }

    public void startJob(long testSeconds) {
        Thread thread = new Thread(() -> {
            logger.info("Sleep {} seconds then end jobs", testSeconds);
            ThreadUtils.sleep(TimeUnit.SECONDS, testSeconds);
            jobStart = false;
            jobStopNanoTime = System.nanoTime();
            synchronized (lock) {
                lock.notifyAll();
            }
        }, "JobWaitThread");
        jobStart = true;
        jobStartNanoTime = System.nanoTime();
        logger.info("Start jobs");
        thread.start();
    }

    public void waitForJobsEnd() {
        synchronized (lock) {
            while (jobStart) {
                try {
                    logger.info("Wait for jobs end");
                    lock.wait();
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
        statisticsStartNanoTime = System.nanoTime();
    }

    public void stopStatistics() {
        statisticsStart = false;
        statisticsStopNanoTime = System.nanoTime();
    }

    public long jobDurationNano() {
        if (jobStart) {
            return -1;
        }
        return jobStopNanoTime - jobStartNanoTime;
    }

    public long statisticsDurationNano() {
        if (statisticsStart) {
            return -1;
        }
        return statisticsStopNanoTime - statisticsStartNanoTime;
    }
}
