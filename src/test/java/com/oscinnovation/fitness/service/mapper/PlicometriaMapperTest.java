package com.oscinnovation.fitness.service.mapper;

import static com.oscinnovation.fitness.domain.PlicometriaAsserts.*;
import static com.oscinnovation.fitness.domain.PlicometriaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlicometriaMapperTest {

    private PlicometriaMapper plicometriaMapper;

    @BeforeEach
    void setUp() {
        plicometriaMapper = new PlicometriaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPlicometriaSample1();
        var actual = plicometriaMapper.toEntity(plicometriaMapper.toDto(expected));
        assertPlicometriaAllPropertiesEquals(expected, actual);
    }
}
