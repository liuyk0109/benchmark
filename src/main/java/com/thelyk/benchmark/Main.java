package com.thelyk.benchmark;

public class Main {

    public static void main(String[] a) throws InterruptedException {
        Args args = CmdParser.parse(a);
        if (args != null) {
            new Benchmark(args).test();
        }
    }
}
