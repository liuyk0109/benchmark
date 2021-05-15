package com.thelyk.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Benchmark {

    private static final Logger logger = LoggerFactory.getLogger(Benchmark.class);

    private final ExecutorService jobThreadPool;
    private final Counter counter;
    private final Control ctl;
    private final QpsLimiter limiter;

    public Benchmark(Args args) {
        this.jobThreadPool = Executors.newCachedThreadPool();
        this.counter = new Counter();
        this.ctl = new Control();
        ctl.setTestSeconds(args.getTestSeconds());
        limiter = new QpsLimiter(args.getQps(), ctl);
    }

    public void test() throws InterruptedException {
        logger.info("Start benchmark");
        Thread statisticsThread = new Thread(new Statistics(counter, ctl), "StatisticsThread");
        Thread workThread = new Thread(new Job(jobThreadPool, counter, ctl, limiter), "WorkThread");
        ctl.startStatistics();
        ctl.startJob();
        ThreadUtils.sleep(TimeUnit.SECONDS, 1L);
        statisticsThread.start();
        workThread.start();
        limiter.startGenerateToken();

        ctl.waitForJobsEnd();
        workThread.join();
        jobThreadPool.shutdown();
        while (!jobThreadPool.isTerminated()) {
            jobThreadPool.awaitTermination(1L, TimeUnit.SECONDS);
        }
        ctl.stopStatistics();
        statisticsThread.join();
        logger.info("End benchmark");
    }
}
