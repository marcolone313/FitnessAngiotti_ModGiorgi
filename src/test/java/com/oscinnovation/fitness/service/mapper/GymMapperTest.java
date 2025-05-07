package com.oscinnovation.fitness.service.mapper;

import static com.oscinnovation.fitness.domain.GymAsserts.*;
import static com.oscinnovation.fitness.domain.GymTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GymMapperTest {

    private GymMapper gymMapper;

    @BeforeEach
    void setUp() {
        gymMapper = new GymMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGymSample1();
        var actual = gymMapper.toEntity(gymMapper.toDto(expected));
        assertGymAllPropertiesEquals(expected, actual);
    }
}
