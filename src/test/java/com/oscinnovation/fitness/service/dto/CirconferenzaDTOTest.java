package com.oscinnovation.fitness.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CirconferenzaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CirconferenzaDTO.class);
        CirconferenzaDTO circonferenzaDTO1 = new CirconferenzaDTO();
        circonferenzaDTO1.setId(1L);
        CirconferenzaDTO circonferenzaDTO2 = new CirconferenzaDTO();
        assertThat(circonferenzaDTO1).isNotEqualTo(circonferenzaDTO2);
        circonferenzaDTO2.setId(circonferenzaDTO1.getId());
        assertThat(circonferenzaDTO1).isEqualTo(circonferenzaDTO2);
        circonferenzaDTO2.setId(2L);
        assertThat(circonferenzaDTO1).isNotEqualTo(circonferenzaDTO2);
        circonferenzaDTO1.setId(null);
        assertThat(circonferenzaDTO1).isNotEqualTo(circonferenzaDTO2);
    }
}
