package com.thelyk.benchmark;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

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
        ConnectionProvider provider = ConnectionProvider.builder("BenchmarkHttpClient")
                .maxConnections(1000)
                .build();
        httpClient = HttpClient.create(provider);
    }

    public void test() throws InterruptedException {
        logger.info("Start benchmark");
        Thread statisticsThread = new Thread(new Statistics(counter, ctl), "StatisticsThread");
        Thread workThread = new Thread(new Job(httpClient, counter, ctl, limiter, httpRequest), "WorkThread");
        ctl.startStatistics();
        ctl.startJob(args.getTestSeconds());
        limiter.startGenerateToken();
        statisticsThread.start();
        workThread.start();

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
