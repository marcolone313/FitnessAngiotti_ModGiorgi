package com.oscinnovation.fitness.domain;

import static com.oscinnovation.fitness.domain.CorsoAcademyTestSamples.*;
import static com.oscinnovation.fitness.domain.LezioneCorsoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CorsoAcademyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorsoAcademy.class);
        CorsoAcademy corsoAcademy1 = getCorsoAcademySample1();
        CorsoAcademy corsoAcademy2 = new CorsoAcademy();
        assertThat(corsoAcademy1).isNotEqualTo(corsoAcademy2);

        corsoAcademy2.setId(corsoAcademy1.getId());
        assertThat(corsoAcademy1).isEqualTo(corsoAcademy2);

        corsoAcademy2 = getCorsoAcademySample2();
        assertThat(corsoAcademy1).isNotEqualTo(corsoAcademy2);
    }

    @Test
    void lezioneCorsoTest() {
        CorsoAcademy corsoAcademy = getCorsoAcademyRandomSampleGenerator();
        LezioneCorso lezioneCorsoBack = getLezioneCorsoRandomSampleGenerator();

        corsoAcademy.addLezioneCorso(lezioneCorsoBack);
        assertThat(corsoAcademy.getLezioneCorsos()).containsOnly(lezioneCorsoBack);
        assertThat(lezioneCorsoBack.getCorsoAcademy()).isEqualTo(corsoAcademy);

        corsoAcademy.removeLezioneCorso(lezioneCorsoBack);
        assertThat(corsoAcademy.getLezioneCorsos()).doesNotContain(lezioneCorsoBack);
        assertThat(lezioneCorsoBack.getCorsoAcademy()).isNull();

        corsoAcademy.lezioneCorsos(new HashSet<>(Set.of(lezioneCorsoBack)));
        assertThat(corsoAcademy.getLezioneCorsos()).containsOnly(lezioneCorsoBack);
        assertThat(lezioneCorsoBack.getCorsoAcademy()).isEqualTo(corsoAcademy);

        corsoAcademy.setLezioneCorsos(new HashSet<>());
        assertThat(corsoAcademy.getLezioneCorsos()).doesNotContain(lezioneCorsoBack);
        assertThat(lezioneCorsoBack.getCorsoAcademy()).isNull();
    }
}
