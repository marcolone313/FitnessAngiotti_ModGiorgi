package com.oscinnovation.fitness.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClienteToLezioneCorsoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClienteToLezioneCorsoDTO.class);
        ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO1 = new ClienteToLezioneCorsoDTO();
        clienteToLezioneCorsoDTO1.setId(1L);
        ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO2 = new ClienteToLezioneCorsoDTO();
        assertThat(clienteToLezioneCorsoDTO1).isNotEqualTo(clienteToLezioneCorsoDTO2);
        clienteToLezioneCorsoDTO2.setId(clienteToLezioneCorsoDTO1.getId());
        assertThat(clienteToLezioneCorsoDTO1).isEqualTo(clienteToLezioneCorsoDTO2);
        clienteToLezioneCorsoDTO2.setId(2L);
        assertThat(clienteToLezioneCorsoDTO1).isNotEqualTo(clienteToLezioneCorsoDTO2);
        clienteToLezioneCorsoDTO1.setId(null);
        assertThat(clienteToLezioneCorsoDTO1).isNotEqualTo(clienteToLezioneCorsoDTO2);
    }
}
