package com.oscinnovation.fitness.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AllenamentoGiornalieroDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AllenamentoGiornalieroDTO.class);
        AllenamentoGiornalieroDTO allenamentoGiornalieroDTO1 = new AllenamentoGiornalieroDTO();
        allenamentoGiornalieroDTO1.setId(1L);
        AllenamentoGiornalieroDTO allenamentoGiornalieroDTO2 = new AllenamentoGiornalieroDTO();
        assertThat(allenamentoGiornalieroDTO1).isNotEqualTo(allenamentoGiornalieroDTO2);
        allenamentoGiornalieroDTO2.setId(allenamentoGiornalieroDTO1.getId());
        assertThat(allenamentoGiornalieroDTO1).isEqualTo(allenamentoGiornalieroDTO2);
        allenamentoGiornalieroDTO2.setId(2L);
        assertThat(allenamentoGiornalieroDTO1).isNotEqualTo(allenamentoGiornalieroDTO2);
        allenamentoGiornalieroDTO1.setId(null);
        assertThat(allenamentoGiornalieroDTO1).isNotEqualTo(allenamentoGiornalieroDTO2);
    }
}
