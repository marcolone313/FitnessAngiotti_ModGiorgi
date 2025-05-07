package com.oscinnovation.fitness.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EsercizioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EsercizioDTO.class);
        EsercizioDTO esercizioDTO1 = new EsercizioDTO();
        esercizioDTO1.setId(1L);
        EsercizioDTO esercizioDTO2 = new EsercizioDTO();
        assertThat(esercizioDTO1).isNotEqualTo(esercizioDTO2);
        esercizioDTO2.setId(esercizioDTO1.getId());
        assertThat(esercizioDTO1).isEqualTo(esercizioDTO2);
        esercizioDTO2.setId(2L);
        assertThat(esercizioDTO1).isNotEqualTo(esercizioDTO2);
        esercizioDTO1.setId(null);
        assertThat(esercizioDTO1).isNotEqualTo(esercizioDTO2);
    }
}
