package com.oscinnovation.fitness.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportSettimanaleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportSettimanaleDTO.class);
        ReportSettimanaleDTO reportSettimanaleDTO1 = new ReportSettimanaleDTO();
        reportSettimanaleDTO1.setId(1L);
        ReportSettimanaleDTO reportSettimanaleDTO2 = new ReportSettimanaleDTO();
        assertThat(reportSettimanaleDTO1).isNotEqualTo(reportSettimanaleDTO2);
        reportSettimanaleDTO2.setId(reportSettimanaleDTO1.getId());
        assertThat(reportSettimanaleDTO1).isEqualTo(reportSettimanaleDTO2);
        reportSettimanaleDTO2.setId(2L);
        assertThat(reportSettimanaleDTO1).isNotEqualTo(reportSettimanaleDTO2);
        reportSettimanaleDTO1.setId(null);
        assertThat(reportSettimanaleDTO1).isNotEqualTo(reportSettimanaleDTO2);
    }
}
