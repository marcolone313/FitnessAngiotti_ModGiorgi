package com.oscinnovation.fitness.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlicometriaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlicometriaDTO.class);
        PlicometriaDTO plicometriaDTO1 = new PlicometriaDTO();
        plicometriaDTO1.setId(1L);
        PlicometriaDTO plicometriaDTO2 = new PlicometriaDTO();
        assertThat(plicometriaDTO1).isNotEqualTo(plicometriaDTO2);
        plicometriaDTO2.setId(plicometriaDTO1.getId());
        assertThat(plicometriaDTO1).isEqualTo(plicometriaDTO2);
        plicometriaDTO2.setId(2L);
        assertThat(plicometriaDTO1).isNotEqualTo(plicometriaDTO2);
        plicometriaDTO1.setId(null);
        assertThat(plicometriaDTO1).isNotEqualTo(plicometriaDTO2);
    }
}
