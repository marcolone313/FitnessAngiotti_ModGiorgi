package com.oscinnovation.fitness.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LezioneCorsoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LezioneCorsoDTO.class);
        LezioneCorsoDTO lezioneCorsoDTO1 = new LezioneCorsoDTO();
        lezioneCorsoDTO1.setId(1L);
        LezioneCorsoDTO lezioneCorsoDTO2 = new LezioneCorsoDTO();
        assertThat(lezioneCorsoDTO1).isNotEqualTo(lezioneCorsoDTO2);
        lezioneCorsoDTO2.setId(lezioneCorsoDTO1.getId());
        assertThat(lezioneCorsoDTO1).isEqualTo(lezioneCorsoDTO2);
        lezioneCorsoDTO2.setId(2L);
        assertThat(lezioneCorsoDTO1).isNotEqualTo(lezioneCorsoDTO2);
        lezioneCorsoDTO1.setId(null);
        assertThat(lezioneCorsoDTO1).isNotEqualTo(lezioneCorsoDTO2);
    }
}
