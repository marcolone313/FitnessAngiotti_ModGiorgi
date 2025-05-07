package com.oscinnovation.fitness.service.mapper;

import static com.oscinnovation.fitness.domain.ReportSettimanaleAsserts.*;
import static com.oscinnovation.fitness.domain.ReportSettimanaleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportSettimanaleMapperTest {

    private ReportSettimanaleMapper reportSettimanaleMapper;

    @BeforeEach
    void setUp() {
        reportSettimanaleMapper = new ReportSettimanaleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportSettimanaleSample1();
        var actual = reportSettimanaleMapper.toEntity(reportSettimanaleMapper.toDto(expected));
        assertReportSettimanaleAllPropertiesEquals(expected, actual);
    }
}
