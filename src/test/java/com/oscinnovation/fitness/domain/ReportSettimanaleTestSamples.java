package com.oscinnovation.fitness.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ReportSettimanaleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReportSettimanale getReportSettimanaleSample1() {
        return new ReportSettimanale().id(1L);
    }

    public static ReportSettimanale getReportSettimanaleSample2() {
        return new ReportSettimanale().id(2L);
    }

    public static ReportSettimanale getReportSettimanaleRandomSampleGenerator() {
        return new ReportSettimanale().id(longCount.incrementAndGet());
    }
}
