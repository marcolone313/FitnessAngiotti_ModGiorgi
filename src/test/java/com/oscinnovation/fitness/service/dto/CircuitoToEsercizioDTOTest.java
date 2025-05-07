package com.oscinnovation.fitness.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CircuitoToEsercizioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CircuitoToEsercizioDTO.class);
        CircuitoToEsercizioDTO circuitoToEsercizioDTO1 = new CircuitoToEsercizioDTO();
        circuitoToEsercizioDTO1.setId(1L);
        CircuitoToEsercizioDTO circuitoToEsercizioDTO2 = new CircuitoToEsercizioDTO();
        assertThat(circuitoToEsercizioDTO1).isNotEqualTo(circuitoToEsercizioDTO2);
        circuitoToEsercizioDTO2.setId(circuitoToEsercizioDTO1.getId());
        assertThat(circuitoToEsercizioDTO1).isEqualTo(circuitoToEsercizioDTO2);
        circuitoToEsercizioDTO2.setId(2L);
        assertThat(circuitoToEsercizioDTO1).isNotEqualTo(circuitoToEsercizioDTO2);
        circuitoToEsercizioDTO1.setId(null);
        assertThat(circuitoToEsercizioDTO1).isNotEqualTo(circuitoToEsercizioDTO2);
    }
}
