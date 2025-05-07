package com.oscinnovation.fitness.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchedaSettimanaleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchedaSettimanaleDTO.class);
        SchedaSettimanaleDTO schedaSettimanaleDTO1 = new SchedaSettimanaleDTO();
        schedaSettimanaleDTO1.setId(1L);
        SchedaSettimanaleDTO schedaSettimanaleDTO2 = new SchedaSettimanaleDTO();
        assertThat(schedaSettimanaleDTO1).isNotEqualTo(schedaSettimanaleDTO2);
        schedaSettimanaleDTO2.setId(schedaSettimanaleDTO1.getId());
        assertThat(schedaSettimanaleDTO1).isEqualTo(schedaSettimanaleDTO2);
        schedaSettimanaleDTO2.setId(2L);
        assertThat(schedaSettimanaleDTO1).isNotEqualTo(schedaSettimanaleDTO2);
        schedaSettimanaleDTO1.setId(null);
        assertThat(schedaSettimanaleDTO1).isNotEqualTo(schedaSettimanaleDTO2);
    }
}
