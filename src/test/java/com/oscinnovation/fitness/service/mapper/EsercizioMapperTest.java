package com.oscinnovation.fitness.service.mapper;

import static com.oscinnovation.fitness.domain.EsercizioAsserts.*;
import static com.oscinnovation.fitness.domain.EsercizioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EsercizioMapperTest {

    private EsercizioMapper esercizioMapper;

    @BeforeEach
    void setUp() {
        esercizioMapper = new EsercizioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEsercizioSample1();
        var actual = esercizioMapper.toEntity(esercizioMapper.toDto(expected));
        assertEsercizioAllPropertiesEquals(expected, actual);
    }
}
