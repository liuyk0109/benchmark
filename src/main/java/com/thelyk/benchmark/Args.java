package com.thelyk.benchmark;

public class Args {

    private long qps;
    private long testSeconds;
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
