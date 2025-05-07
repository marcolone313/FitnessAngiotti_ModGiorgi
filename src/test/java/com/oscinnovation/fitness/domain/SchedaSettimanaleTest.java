package com.oscinnovation.fitness.domain;

import static com.oscinnovation.fitness.domain.ClienteTestSamples.*;
import static com.oscinnovation.fitness.domain.ReportSettimanaleTestSamples.*;
import static com.oscinnovation.fitness.domain.SchedaSettimanaleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchedaSettimanaleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchedaSettimanale.class);
        SchedaSettimanale schedaSettimanale1 = getSchedaSettimanaleSample1();
        SchedaSettimanale schedaSettimanale2 = new SchedaSettimanale();
        assertThat(schedaSettimanale1).isNotEqualTo(schedaSettimanale2);

        schedaSettimanale2.setId(schedaSettimanale1.getId());
        assertThat(schedaSettimanale1).isEqualTo(schedaSettimanale2);

        schedaSettimanale2 = getSchedaSettimanaleSample2();
        assertThat(schedaSettimanale1).isNotEqualTo(schedaSettimanale2);
    }

    @Test
    void clienteTest() {
        SchedaSettimanale schedaSettimanale = getSchedaSettimanaleRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        schedaSettimanale.setCliente(clienteBack);
        assertThat(schedaSettimanale.getCliente()).isEqualTo(clienteBack);

        schedaSettimanale.cliente(null);
        assertThat(schedaSettimanale.getCliente()).isNull();
    }

    @Test
    void reportSettimanaleTest() {
        SchedaSettimanale schedaSettimanale = getSchedaSettimanaleRandomSampleGenerator();
        ReportSettimanale reportSettimanaleBack = getReportSettimanaleRandomSampleGenerator();

        schedaSettimanale.setReportSettimanale(reportSettimanaleBack);
        assertThat(schedaSettimanale.getReportSettimanale()).isEqualTo(reportSettimanaleBack);
        assertThat(reportSettimanaleBack.getSchedaSettimanale()).isEqualTo(schedaSettimanale);

        schedaSettimanale.reportSettimanale(null);
        assertThat(schedaSettimanale.getReportSettimanale()).isNull();
        assertThat(reportSettimanaleBack.getSchedaSettimanale()).isNull();
    }
}
