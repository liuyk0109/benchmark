package com.thelyk.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Statistics implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Statistics.class);

    private final Counter counter;
    private final Control ctl;

    public Statistics(Counter counter, Control ctl) {
        this.counter = counter;
        this.ctl = ctl;
    }

    @Override
    public void run() {
        long lastTotalSend = 0L;
        long lastTotalRecv = 0L;
        while (ctl.isStatisticsStart()) {
            ThreadUtils.sleep(TimeUnit.SECONDS, 1L);
            long currTotalSend = counter.getTotalSend();
            long currTotalRecv = counter.getTotalRecv();
            long sendQps = currTotalSend - lastTotalSend;
            long recvQps = currTotalRecv - lastTotalRecv;
            logger.info(String.format("totalSend: %d, sendQps: %d, totalRecv: %d, recvQps: %d",
                    currTotalSend, sendQps, currTotalSend, recvQps));
            lastTotalSend = currTotalSend;
            lastTotalRecv = currTotalRecv;
        }
    }
}
