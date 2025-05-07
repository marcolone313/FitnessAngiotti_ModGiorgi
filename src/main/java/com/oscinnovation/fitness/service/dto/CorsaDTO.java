package com.oscinnovation.fitness.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

/**
 * A DTO for the {@link com.oscinnovation.fitness.domain.Corsa} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CorsaDTO implements Serializable {

    private Long id;

    @NotNull
    private Float distanzaDaPercorrere;

    private Duration tempoImpiegato;

    private Boolean svolto;

    private Boolean completato;

    @Lob
    private String feedback;

    @NotNull
    private AllenamentoGiornalieroDTO allenamentoGiornaliero;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getDistanzaDaPercorrere() {
        return distanzaDaPercorrere;
    }

    public void setDistanzaDaPercorrere(Float distanzaDaPercorrere) {
        this.distanzaDaPercorrere = distanzaDaPercorrere;
    }

    public Duration getTempoImpiegato() {
        return tempoImpiegato;
    }

    public void setTempoImpiegato(Duration tempoImpiegato) {
        this.tempoImpiegato = tempoImpiegato;
    }

    public Boolean getSvolto() {
        return svolto;
    }

    public void setSvolto(Boolean svolto) {
        this.svolto = svolto;
    }

    public Boolean getCompletato() {
        return completato;
    }

    public void setCompletato(Boolean completato) {
        this.completato = completato;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public AllenamentoGiornalieroDTO getAllenamentoGiornaliero() {
        return allenamentoGiornaliero;
    }

    public void setAllenamentoGiornaliero(AllenamentoGiornalieroDTO allenamentoGiornaliero) {
        this.allenamentoGiornaliero = allenamentoGiornaliero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CorsaDTO)) {
            return false;
        }

        CorsaDTO corsaDTO = (CorsaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, corsaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CorsaDTO{" +
            "id=" + getId() +
            ", distanzaDaPercorrere=" + getDistanzaDaPercorrere() +
            ", tempoImpiegato='" + getTempoImpiegato() + "'" +
            ", svolto='" + getSvolto() + "'" +
            ", completato='" + getCompletato() + "'" +
            ", feedback='" + getFeedback() + "'" +
            ", allenamentoGiornaliero=" + getAllenamentoGiornaliero() +
            "}";
    }
}
