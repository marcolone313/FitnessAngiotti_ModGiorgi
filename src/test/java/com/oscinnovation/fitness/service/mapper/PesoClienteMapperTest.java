package com.oscinnovation.fitness.service.mapper;

import static com.oscinnovation.fitness.domain.PesoClienteAsserts.*;
import static com.oscinnovation.fitness.domain.PesoClienteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PesoClienteMapperTest {

    private PesoClienteMapper pesoClienteMapper;

    @BeforeEach
    void setUp() {
        pesoClienteMapper = new PesoClienteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPesoClienteSample1();
        var actual = pesoClienteMapper.toEntity(pesoClienteMapper.toDto(expected));
        assertPesoClienteAllPropertiesEquals(expected, actual);
    }
}
