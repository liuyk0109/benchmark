package com.thelyk.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
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
            logger.info("totalSend: {}, sendQps: {}, totalRecv: {}, recvQps: {}",
                    currTotalSend, sendQps, currTotalRecv, recvQps);
            lastTotalSend = currTotalSend;
            lastTotalRecv = currTotalRecv;
        }
        summary();
    }

    private void summary() {
        BigDecimal totalSend = BigDecimal.valueOf(counter.getTotalSend());
        BigDecimal totalRecv = BigDecimal.valueOf(counter.getTotalRecv());
        BigDecimal totalSuccess = BigDecimal.valueOf(counter.getTotalSuccess());
        BigDecimal totalFail = BigDecimal.valueOf(counter.getTotalFail());

        Duration jobDuration = Duration.ofNanos(ctl.jobDurationNano());
        Duration statisticsDuration = Duration.ofNanos(ctl.statisticsDurationNano());
        BigDecimal avgSendQps = totalSend.divide(BigDecimal.valueOf(jobDuration.getSeconds()), 2, RoundingMode.HALF_EVEN);
        BigDecimal avgRecvQps = totalRecv.divide(BigDecimal.valueOf(statisticsDuration.getSeconds()), 2, RoundingMode.HALF_EVEN);

        BigDecimal successRate = totalSuccess.divide(totalSend, 2, RoundingMode.HALF_EVEN);
        BigDecimal failRate = totalFail.divide(totalSend, 2, RoundingMode.HALF_EVEN);

        logger.info("[Summary] totalSend: {}, avgSendQps: {}, totalRecv: {}, avgRecvQps: {}, totalSuccess: {}, successRate: {}, totalFail: {}, failRate: {}",
                totalSend.longValue(), avgSendQps,
                totalRecv.longValue(), avgRecvQps,
                totalSuccess.longValue(), successRate,
                totalFail.longValue(), failRate);
    }
}
