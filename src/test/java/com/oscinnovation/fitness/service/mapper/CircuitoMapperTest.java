package com.oscinnovation.fitness.service.mapper;

import static com.oscinnovation.fitness.domain.CircuitoAsserts.*;
import static com.oscinnovation.fitness.domain.CircuitoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CircuitoMapperTest {

    private CircuitoMapper circuitoMapper;

    @BeforeEach
    void setUp() {
        circuitoMapper = new CircuitoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCircuitoSample1();
        var actual = circuitoMapper.toEntity(circuitoMapper.toDto(expected));
        assertCircuitoAllPropertiesEquals(expected, actual);
    }
}
