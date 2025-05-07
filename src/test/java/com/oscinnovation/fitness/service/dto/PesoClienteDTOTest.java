package com.oscinnovation.fitness.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PesoClienteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PesoClienteDTO.class);
        PesoClienteDTO pesoClienteDTO1 = new PesoClienteDTO();
        pesoClienteDTO1.setId(1L);
        PesoClienteDTO pesoClienteDTO2 = new PesoClienteDTO();
        assertThat(pesoClienteDTO1).isNotEqualTo(pesoClienteDTO2);
        pesoClienteDTO2.setId(pesoClienteDTO1.getId());
        assertThat(pesoClienteDTO1).isEqualTo(pesoClienteDTO2);
        pesoClienteDTO2.setId(2L);
        assertThat(pesoClienteDTO1).isNotEqualTo(pesoClienteDTO2);
        pesoClienteDTO1.setId(null);
        assertThat(pesoClienteDTO1).isNotEqualTo(pesoClienteDTO2);
    }
}
