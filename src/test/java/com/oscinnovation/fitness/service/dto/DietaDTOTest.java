package com.oscinnovation.fitness.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DietaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DietaDTO.class);
        DietaDTO dietaDTO1 = new DietaDTO();
        dietaDTO1.setId(1L);
        DietaDTO dietaDTO2 = new DietaDTO();
        assertThat(dietaDTO1).isNotEqualTo(dietaDTO2);
        dietaDTO2.setId(dietaDTO1.getId());
        assertThat(dietaDTO1).isEqualTo(dietaDTO2);
        dietaDTO2.setId(2L);
        assertThat(dietaDTO1).isNotEqualTo(dietaDTO2);
        dietaDTO1.setId(null);
        assertThat(dietaDTO1).isNotEqualTo(dietaDTO2);
    }
}
