package com.oscinnovation.fitness.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GymDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GymDTO.class);
        GymDTO gymDTO1 = new GymDTO();
        gymDTO1.setId(1L);
        GymDTO gymDTO2 = new GymDTO();
        assertThat(gymDTO1).isNotEqualTo(gymDTO2);
        gymDTO2.setId(gymDTO1.getId());
        assertThat(gymDTO1).isEqualTo(gymDTO2);
        gymDTO2.setId(2L);
        assertThat(gymDTO1).isNotEqualTo(gymDTO2);
        gymDTO1.setId(null);
        assertThat(gymDTO1).isNotEqualTo(gymDTO2);
    }
}
