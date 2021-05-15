package com.thelyk.benchmark;

public class Main {

    public static void main(String[] a) throws InterruptedException {
        Args args = resolveArgs(a);
        new Benchmark(args).test();
    }

    private static Args resolveArgs(String[] a) {
        // TODO resolve args
        Args args = new Args();
        args.setQps(3L);
        args.setTestSeconds(30);
        return args;
    }
}
