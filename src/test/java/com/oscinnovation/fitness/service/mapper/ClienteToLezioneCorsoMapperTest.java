package com.oscinnovation.fitness.service.mapper;

import static com.oscinnovation.fitness.domain.ClienteToLezioneCorsoAsserts.*;
import static com.oscinnovation.fitness.domain.ClienteToLezioneCorsoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClienteToLezioneCorsoMapperTest {

    private ClienteToLezioneCorsoMapper clienteToLezioneCorsoMapper;

    @BeforeEach
    void setUp() {
        clienteToLezioneCorsoMapper = new ClienteToLezioneCorsoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getClienteToLezioneCorsoSample1();
        var actual = clienteToLezioneCorsoMapper.toEntity(clienteToLezioneCorsoMapper.toDto(expected));
        assertClienteToLezioneCorsoAllPropertiesEquals(expected, actual);
    }
}
