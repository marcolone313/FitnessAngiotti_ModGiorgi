package com.oscinnovation.fitness.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CorsoAcademyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorsoAcademyDTO.class);
        CorsoAcademyDTO corsoAcademyDTO1 = new CorsoAcademyDTO();
        corsoAcademyDTO1.setId(1L);
        CorsoAcademyDTO corsoAcademyDTO2 = new CorsoAcademyDTO();
        assertThat(corsoAcademyDTO1).isNotEqualTo(corsoAcademyDTO2);
        corsoAcademyDTO2.setId(corsoAcademyDTO1.getId());
        assertThat(corsoAcademyDTO1).isEqualTo(corsoAcademyDTO2);
        corsoAcademyDTO2.setId(2L);
        assertThat(corsoAcademyDTO1).isNotEqualTo(corsoAcademyDTO2);
        corsoAcademyDTO1.setId(null);
        assertThat(corsoAcademyDTO1).isNotEqualTo(corsoAcademyDTO2);
    }
}
