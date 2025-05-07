package com.oscinnovation.fitness.service.mapper;

import static com.oscinnovation.fitness.domain.DietaAsserts.*;
import static com.oscinnovation.fitness.domain.DietaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DietaMapperTest {

    private DietaMapper dietaMapper;

    @BeforeEach
    void setUp() {
        dietaMapper = new DietaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDietaSample1();
        var actual = dietaMapper.toEntity(dietaMapper.toDto(expected));
        assertDietaAllPropertiesEquals(expected, actual);
    }
}
