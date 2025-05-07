package com.oscinnovation.fitness.service.mapper;

import static com.oscinnovation.fitness.domain.PassaggioEsercizioAsserts.*;
import static com.oscinnovation.fitness.domain.PassaggioEsercizioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PassaggioEsercizioMapperTest {

    private PassaggioEsercizioMapper passaggioEsercizioMapper;

    @BeforeEach
    void setUp() {
        passaggioEsercizioMapper = new PassaggioEsercizioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPassaggioEsercizioSample1();
        var actual = passaggioEsercizioMapper.toEntity(passaggioEsercizioMapper.toDto(expected));
        assertPassaggioEsercizioAllPropertiesEquals(expected, actual);
    }
}
