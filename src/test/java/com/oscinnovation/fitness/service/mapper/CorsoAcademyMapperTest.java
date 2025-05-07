package com.oscinnovation.fitness.service.mapper;

import static com.oscinnovation.fitness.domain.CorsoAcademyAsserts.*;
import static com.oscinnovation.fitness.domain.CorsoAcademyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CorsoAcademyMapperTest {

    private CorsoAcademyMapper corsoAcademyMapper;

    @BeforeEach
    void setUp() {
        corsoAcademyMapper = new CorsoAcademyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCorsoAcademySample1();
        var actual = corsoAcademyMapper.toEntity(corsoAcademyMapper.toDto(expected));
        assertCorsoAcademyAllPropertiesEquals(expected, actual);
    }
}
