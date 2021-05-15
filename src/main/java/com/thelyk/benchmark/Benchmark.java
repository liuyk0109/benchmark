package com.thelyk.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.concurrent.TimeUnit;

public class Benchmark {

    private static final Logger logger = LoggerFactory.getLogger(Benchmark.class);

    private final Args args;
    private final Counter counter;
    private final Control ctl;
    private final QpsLimiter limiter;
    private final HttpRequest httpRequest;
    private final HttpClient httpClient;

    public Benchmark(Args args) {
        this.args = args;
        this.counter = new Counter();
        this.ctl = new Control();
        limiter = new QpsLimiter(args.getQps(), ctl);
        httpRequest = args.getHttpRequest();
        httpClient = HttpClient.create(ConnectionProvider.create("BenchmarkHttpClient", Integer.MAX_VALUE));
    }

    public void test() throws InterruptedException {
        logger.info("Start benchmark");
        Thread statisticsThread = new Thread(new Statistics(counter, ctl), "StatisticsThread");
        Thread workThread = new Thread(new Job(httpClient, counter, ctl, limiter, httpRequest), "WorkThread");
        ctl.startStatistics();
        ctl.startJob(args.getTestSeconds());
        statisticsThread.start();
        workThread.start();
        limiter.startGenerateToken();

        ctl.waitForJobsEnd();
        workThread.join();
        while (counter.getTotalSend() != counter.getTotalRecv()) {
            ThreadUtils.sleep(TimeUnit.SECONDS, 1L);
        }
        ctl.stopStatistics();
        statisticsThread.join();
        logger.info("End benchmark");
    }
}
