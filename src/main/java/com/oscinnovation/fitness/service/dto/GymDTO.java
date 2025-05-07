package com.oscinnovation.fitness.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

/**
 * A DTO for the {@link com.oscinnovation.fitness.domain.Gym} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GymDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer sets;

    @NotNull
    private Integer reps;

    @NotNull
    private Duration recupero;

    @DecimalMin(value = "0")
    private Float peso;

    private Boolean completato;

    private Boolean svolto;

    @Lob
    private String feedback;

    @NotNull
    private AllenamentoGiornalieroDTO allenamentoGiornaliero;

    @NotNull
    private EsercizioDTO esercizio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Duration getRecupero() {
        return recupero;
    }

    public void setRecupero(Duration recupero) {
        this.recupero = recupero;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public Boolean getCompletato() {
        return completato;
    }

    public void setCompletato(Boolean completato) {
        this.completato = completato;
    }

    public Boolean getSvolto() {
        return svolto;
    }

    public void setSvolto(Boolean svolto) {
        this.svolto = svolto;
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

    public EsercizioDTO getEsercizio() {
        return esercizio;
    }

    public void setEsercizio(EsercizioDTO esercizio) {
        this.esercizio = esercizio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GymDTO)) {
            return false;
        }

        GymDTO gymDTO = (GymDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gymDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GymDTO{" +
            "id=" + getId() +
            ", sets=" + getSets() +
            ", reps=" + getReps() +
            ", recupero='" + getRecupero() + "'" +
            ", peso=" + getPeso() +
            ", completato='" + getCompletato() + "'" +
            ", svolto='" + getSvolto() + "'" +
            ", feedback='" + getFeedback() + "'" +
            ", allenamentoGiornaliero=" + getAllenamentoGiornaliero() +
            ", esercizio=" + getEsercizio() +
            "}";
    }
}
