package com.oscinnovation.fitness.service.mapper;

import static com.oscinnovation.fitness.domain.CircuitoToEsercizioAsserts.*;
import static com.oscinnovation.fitness.domain.CircuitoToEsercizioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CircuitoToEsercizioMapperTest {

    private CircuitoToEsercizioMapper circuitoToEsercizioMapper;

    @BeforeEach
    void setUp() {
        circuitoToEsercizioMapper = new CircuitoToEsercizioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCircuitoToEsercizioSample1();
        var actual = circuitoToEsercizioMapper.toEntity(circuitoToEsercizioMapper.toDto(expected));
        assertCircuitoToEsercizioAllPropertiesEquals(expected, actual);
    }
}
