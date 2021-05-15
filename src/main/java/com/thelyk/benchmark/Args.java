package com.thelyk.benchmark;

public class Args {

    private long qps;
    private long testSeconds;
    private HttpRequest httpRequest;

    public long getQps() {
        return qps;
    }

    public void setQps(long qps) {
        this.qps = qps;
    }

    public long getTestSeconds() {
        return testSeconds;
    }

    public void setTestSeconds(long testSeconds) {
        this.testSeconds = testSeconds;
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }
}
