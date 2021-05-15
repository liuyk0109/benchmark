package com.thelyk.benchmark;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

public class Job implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Job.class);

    private final HttpClient httpClient;
    private final Counter counter;
    private final Control ctl;
    private final QpsLimiter limiter;
    private final HttpRequest httpRequest;

    public Job(HttpClient httpClient, Counter counter, Control ctl, QpsLimiter limiter, HttpRequest httpRequest) {
        this.httpClient = httpClient;
        this.counter = counter;
        this.ctl = ctl;
        this.limiter = limiter;
        this.httpRequest = httpRequest;
    }

    @Override
    public void run() {
        while (ctl.isJobStart()) {
            if (limiter.tryGetToken()) {
                doJob();
//                counter.incrSend();
//                counter.incrRecv();
            }
        }
        logger.info("Stop send");
    }

    private void doJob() {
        httpClient.doOnRequest((request, connection) -> counter.incrSend())
                .doOnResponse((response, connection) -> counter.incrRecv())
                .doOnError((request, throwable) -> counter.incrFail(), (response, throwable) -> counter.incrFail())
                .request(httpRequest.getMethod())
                .uri(httpRequest.getUrl())
                .response()
                .timeout(Duration.ofSeconds(45L))
                .subscribe((response) -> {
                    int statusCode = response.status().code();
                    if (statusCode < HttpResponseStatus.OK.code() || statusCode >= HttpResponseStatus.BAD_REQUEST.code()) {
                        counter.incrFail();
                    } else {
                        counter.incrSuccess();
                    }
                });
    }
}
