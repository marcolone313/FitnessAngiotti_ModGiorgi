package com.oscinnovation.fitness.service.mapper;

import static com.oscinnovation.fitness.domain.CorsaAsserts.*;
import static com.oscinnovation.fitness.domain.CorsaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CorsaMapperTest {

    private CorsaMapper corsaMapper;

    @BeforeEach
    void setUp() {
        corsaMapper = new CorsaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCorsaSample1();
        var actual = corsaMapper.toEntity(corsaMapper.toDto(expected));
        assertCorsaAllPropertiesEquals(expected, actual);
    }
}
