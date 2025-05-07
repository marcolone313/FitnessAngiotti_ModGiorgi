package com.oscinnovation.fitness.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.oscinnovation.fitness.domain.LezioneCorso} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LezioneCorsoDTO implements Serializable {

    private Long id;

    @NotNull
    private String titolo;

    @Lob
    private String descrizione;

    @Lob
    private String puntiFocali;

    private String videoUrl;

    @NotNull
    private CorsoAcademyDTO corsoAcademy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getPuntiFocali() {
        return puntiFocali;
    }

    public void setPuntiFocali(String puntiFocali) {
        this.puntiFocali = puntiFocali;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public CorsoAcademyDTO getCorsoAcademy() {
        return corsoAcademy;
    }

    public void setCorsoAcademy(CorsoAcademyDTO corsoAcademy) {
        this.corsoAcademy = corsoAcademy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LezioneCorsoDTO)) {
            return false;
        }

        LezioneCorsoDTO lezioneCorsoDTO = (LezioneCorsoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lezioneCorsoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LezioneCorsoDTO{" +
            "id=" + getId() +
            ", titolo='" + getTitolo() + "'" +
            ", descrizione='" + getDescrizione() + "'" +
            ", puntiFocali='" + getPuntiFocali() + "'" +
            ", videoUrl='" + getVideoUrl() + "'" +
            ", corsoAcademy=" + getCorsoAcademy() +
            "}";
    }
}
