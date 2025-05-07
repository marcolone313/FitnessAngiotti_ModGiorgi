package com.oscinnovation.fitness.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CircuitoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CircuitoDTO.class);
        CircuitoDTO circuitoDTO1 = new CircuitoDTO();
        circuitoDTO1.setId(1L);
        CircuitoDTO circuitoDTO2 = new CircuitoDTO();
        assertThat(circuitoDTO1).isNotEqualTo(circuitoDTO2);
        circuitoDTO2.setId(circuitoDTO1.getId());
        assertThat(circuitoDTO1).isEqualTo(circuitoDTO2);
        circuitoDTO2.setId(2L);
        assertThat(circuitoDTO1).isNotEqualTo(circuitoDTO2);
        circuitoDTO1.setId(null);
        assertThat(circuitoDTO1).isNotEqualTo(circuitoDTO2);
    }
}
