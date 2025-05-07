package com.oscinnovation.fitness.domain;

import static com.oscinnovation.fitness.domain.CirconferenzaTestSamples.*;
import static com.oscinnovation.fitness.domain.ClienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CirconferenzaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Circonferenza.class);
        Circonferenza circonferenza1 = getCirconferenzaSample1();
        Circonferenza circonferenza2 = new Circonferenza();
        assertThat(circonferenza1).isNotEqualTo(circonferenza2);

        circonferenza2.setId(circonferenza1.getId());
        assertThat(circonferenza1).isEqualTo(circonferenza2);

        circonferenza2 = getCirconferenzaSample2();
        assertThat(circonferenza1).isNotEqualTo(circonferenza2);
    }

    @Test
    void clienteTest() {
        Circonferenza circonferenza = getCirconferenzaRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        circonferenza.setCliente(clienteBack);
        assertThat(circonferenza.getCliente()).isEqualTo(clienteBack);

        circonferenza.cliente(null);
        assertThat(circonferenza.getCliente()).isNull();
    }
}
