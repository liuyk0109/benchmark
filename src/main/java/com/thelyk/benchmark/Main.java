package com.thelyk.benchmark;

public class Main {

    public static void main(String[] a) throws InterruptedException {
        Args args = resolveArgs(a);
        new Benchmark(args).test();
    }

    private static Args resolveArgs(String[] a) {
        // TODO resolve args
        Args args = new Args();
        args.setQps(100L);
        args.setTestSeconds(10);
        HttpRequest request = new HttpRequest();
        request.setMethod("GET");
        request.setUrl("http://127.0.0.1:8080/helloReactor");
        args.setHttpRequest(request);
        return args;
    }
}
