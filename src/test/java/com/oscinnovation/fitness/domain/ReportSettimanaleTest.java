package com.oscinnovation.fitness.domain;

import static com.oscinnovation.fitness.domain.ClienteTestSamples.*;
import static com.oscinnovation.fitness.domain.ReportSettimanaleTestSamples.*;
import static com.oscinnovation.fitness.domain.SchedaSettimanaleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportSettimanaleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportSettimanale.class);
        ReportSettimanale reportSettimanale1 = getReportSettimanaleSample1();
        ReportSettimanale reportSettimanale2 = new ReportSettimanale();
        assertThat(reportSettimanale1).isNotEqualTo(reportSettimanale2);

        reportSettimanale2.setId(reportSettimanale1.getId());
        assertThat(reportSettimanale1).isEqualTo(reportSettimanale2);

        reportSettimanale2 = getReportSettimanaleSample2();
        assertThat(reportSettimanale1).isNotEqualTo(reportSettimanale2);
    }

    @Test
    void schedaSettimanaleTest() {
        ReportSettimanale reportSettimanale = getReportSettimanaleRandomSampleGenerator();
        SchedaSettimanale schedaSettimanaleBack = getSchedaSettimanaleRandomSampleGenerator();

        reportSettimanale.setSchedaSettimanale(schedaSettimanaleBack);
        assertThat(reportSettimanale.getSchedaSettimanale()).isEqualTo(schedaSettimanaleBack);

        reportSettimanale.schedaSettimanale(null);
        assertThat(reportSettimanale.getSchedaSettimanale()).isNull();
    }

    @Test
    void clienteTest() {
        ReportSettimanale reportSettimanale = getReportSettimanaleRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        reportSettimanale.setCliente(clienteBack);
        assertThat(reportSettimanale.getCliente()).isEqualTo(clienteBack);

        reportSettimanale.cliente(null);
        assertThat(reportSettimanale.getCliente()).isNull();
    }
}
