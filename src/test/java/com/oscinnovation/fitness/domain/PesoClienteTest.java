package com.oscinnovation.fitness.domain;

import static com.oscinnovation.fitness.domain.ClienteTestSamples.*;
import static com.oscinnovation.fitness.domain.PesoClienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PesoClienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PesoCliente.class);
        PesoCliente pesoCliente1 = getPesoClienteSample1();
        PesoCliente pesoCliente2 = new PesoCliente();
        assertThat(pesoCliente1).isNotEqualTo(pesoCliente2);

        pesoCliente2.setId(pesoCliente1.getId());
        assertThat(pesoCliente1).isEqualTo(pesoCliente2);

        pesoCliente2 = getPesoClienteSample2();
        assertThat(pesoCliente1).isNotEqualTo(pesoCliente2);
    }

    @Test
    void clienteTest() {
        PesoCliente pesoCliente = getPesoClienteRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        pesoCliente.setCliente(clienteBack);
        assertThat(pesoCliente.getCliente()).isEqualTo(clienteBack);

        pesoCliente.cliente(null);
        assertThat(pesoCliente.getCliente()).isNull();
    }
}
