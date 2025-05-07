package com.oscinnovation.fitness.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PassaggioEsercizioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PassaggioEsercizioDTO.class);
        PassaggioEsercizioDTO passaggioEsercizioDTO1 = new PassaggioEsercizioDTO();
        passaggioEsercizioDTO1.setId(1L);
        PassaggioEsercizioDTO passaggioEsercizioDTO2 = new PassaggioEsercizioDTO();
        assertThat(passaggioEsercizioDTO1).isNotEqualTo(passaggioEsercizioDTO2);
        passaggioEsercizioDTO2.setId(passaggioEsercizioDTO1.getId());
        assertThat(passaggioEsercizioDTO1).isEqualTo(passaggioEsercizioDTO2);
        passaggioEsercizioDTO2.setId(2L);
        assertThat(passaggioEsercizioDTO1).isNotEqualTo(passaggioEsercizioDTO2);
        passaggioEsercizioDTO1.setId(null);
        assertThat(passaggioEsercizioDTO1).isNotEqualTo(passaggioEsercizioDTO2);
    }
}
