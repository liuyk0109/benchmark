package com.thelyk.benchmark;

import io.netty.handler.codec.http.HttpMethod;

public class HttpRequest {

    private HttpMethod method = HttpMethod.GET;
    private String url;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = HttpMethod.valueOf(method);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
