package com.oscinnovation.fitness.service.mapper;

import static com.oscinnovation.fitness.domain.LezioneCorsoAsserts.*;
import static com.oscinnovation.fitness.domain.LezioneCorsoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LezioneCorsoMapperTest {

    private LezioneCorsoMapper lezioneCorsoMapper;

    @BeforeEach
    void setUp() {
        lezioneCorsoMapper = new LezioneCorsoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLezioneCorsoSample1();
        var actual = lezioneCorsoMapper.toEntity(lezioneCorsoMapper.toDto(expected));
        assertLezioneCorsoAllPropertiesEquals(expected, actual);
    }
}
