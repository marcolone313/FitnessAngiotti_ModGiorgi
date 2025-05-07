package com.oscinnovation.fitness.domain;

import static com.oscinnovation.fitness.domain.ClienteTestSamples.*;
import static com.oscinnovation.fitness.domain.PlicometriaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlicometriaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plicometria.class);
        Plicometria plicometria1 = getPlicometriaSample1();
        Plicometria plicometria2 = new Plicometria();
        assertThat(plicometria1).isNotEqualTo(plicometria2);

        plicometria2.setId(plicometria1.getId());
        assertThat(plicometria1).isEqualTo(plicometria2);

        plicometria2 = getPlicometriaSample2();
        assertThat(plicometria1).isNotEqualTo(plicometria2);
    }

    @Test
    void clienteTest() {
        Plicometria plicometria = getPlicometriaRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        plicometria.setCliente(clienteBack);
        assertThat(plicometria.getCliente()).isEqualTo(clienteBack);

        plicometria.cliente(null);
        assertThat(plicometria.getCliente()).isNull();
    }
}
