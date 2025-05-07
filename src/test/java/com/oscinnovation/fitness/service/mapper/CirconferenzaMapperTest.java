package com.oscinnovation.fitness.service.mapper;

import static com.oscinnovation.fitness.domain.CirconferenzaAsserts.*;
import static com.oscinnovation.fitness.domain.CirconferenzaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CirconferenzaMapperTest {

    private CirconferenzaMapper circonferenzaMapper;

    @BeforeEach
    void setUp() {
        circonferenzaMapper = new CirconferenzaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCirconferenzaSample1();
        var actual = circonferenzaMapper.toEntity(circonferenzaMapper.toDto(expected));
        assertCirconferenzaAllPropertiesEquals(expected, actual);
    }
}
