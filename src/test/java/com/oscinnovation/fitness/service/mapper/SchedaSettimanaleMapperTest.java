package com.oscinnovation.fitness.service.mapper;

import static com.oscinnovation.fitness.domain.SchedaSettimanaleAsserts.*;
import static com.oscinnovation.fitness.domain.SchedaSettimanaleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchedaSettimanaleMapperTest {

    private SchedaSettimanaleMapper schedaSettimanaleMapper;

    @BeforeEach
    void setUp() {
        schedaSettimanaleMapper = new SchedaSettimanaleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSchedaSettimanaleSample1();
        var actual = schedaSettimanaleMapper.toEntity(schedaSettimanaleMapper.toDto(expected));
        assertSchedaSettimanaleAllPropertiesEquals(expected, actual);
    }
}
