package com.oscinnovation.fitness.service.mapper;

import static com.oscinnovation.fitness.domain.AllenamentoGiornalieroAsserts.*;
import static com.oscinnovation.fitness.domain.AllenamentoGiornalieroTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllenamentoGiornalieroMapperTest {

    private AllenamentoGiornalieroMapper allenamentoGiornalieroMapper;

    @BeforeEach
    void setUp() {
        allenamentoGiornalieroMapper = new AllenamentoGiornalieroMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAllenamentoGiornalieroSample1();
        var actual = allenamentoGiornalieroMapper.toEntity(allenamentoGiornalieroMapper.toDto(expected));
        assertAllenamentoGiornalieroAllPropertiesEquals(expected, actual);
    }
}
