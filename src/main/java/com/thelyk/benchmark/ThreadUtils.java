package com.thelyk.benchmark;

import java.util.concurrent.TimeUnit;

public class ThreadUtils {

    public static void sleep(TimeUnit timeUnit, long timeout) {
        try {
            timeUnit.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
