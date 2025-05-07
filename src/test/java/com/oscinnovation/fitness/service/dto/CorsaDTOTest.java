package com.oscinnovation.fitness.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CorsaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorsaDTO.class);
        CorsaDTO corsaDTO1 = new CorsaDTO();
        corsaDTO1.setId(1L);
        CorsaDTO corsaDTO2 = new CorsaDTO();
        assertThat(corsaDTO1).isNotEqualTo(corsaDTO2);
        corsaDTO2.setId(corsaDTO1.getId());
        assertThat(corsaDTO1).isEqualTo(corsaDTO2);
        corsaDTO2.setId(2L);
        assertThat(corsaDTO1).isNotEqualTo(corsaDTO2);
        corsaDTO1.setId(null);
        assertThat(corsaDTO1).isNotEqualTo(corsaDTO2);
    }
}
