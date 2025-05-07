package com.oscinnovation.fitness.domain;

import static com.oscinnovation.fitness.domain.ClienteTestSamples.*;
import static com.oscinnovation.fitness.domain.ClienteToLezioneCorsoTestSamples.*;
import static com.oscinnovation.fitness.domain.LezioneCorsoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClienteToLezioneCorsoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClienteToLezioneCorso.class);
        ClienteToLezioneCorso clienteToLezioneCorso1 = getClienteToLezioneCorsoSample1();
        ClienteToLezioneCorso clienteToLezioneCorso2 = new ClienteToLezioneCorso();
        assertThat(clienteToLezioneCorso1).isNotEqualTo(clienteToLezioneCorso2);

        clienteToLezioneCorso2.setId(clienteToLezioneCorso1.getId());
        assertThat(clienteToLezioneCorso1).isEqualTo(clienteToLezioneCorso2);

        clienteToLezioneCorso2 = getClienteToLezioneCorsoSample2();
        assertThat(clienteToLezioneCorso1).isNotEqualTo(clienteToLezioneCorso2);
    }

    @Test
    void clienteTest() {
        ClienteToLezioneCorso clienteToLezioneCorso = getClienteToLezioneCorsoRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        clienteToLezioneCorso.setCliente(clienteBack);
        assertThat(clienteToLezioneCorso.getCliente()).isEqualTo(clienteBack);

        clienteToLezioneCorso.cliente(null);
        assertThat(clienteToLezioneCorso.getCliente()).isNull();
    }

    @Test
    void lezioneCorsoTest() {
        ClienteToLezioneCorso clienteToLezioneCorso = getClienteToLezioneCorsoRandomSampleGenerator();
        LezioneCorso lezioneCorsoBack = getLezioneCorsoRandomSampleGenerator();

        clienteToLezioneCorso.setLezioneCorso(lezioneCorsoBack);
        assertThat(clienteToLezioneCorso.getLezioneCorso()).isEqualTo(lezioneCorsoBack);

        clienteToLezioneCorso.lezioneCorso(null);
        assertThat(clienteToLezioneCorso.getLezioneCorso()).isNull();
    }
}
