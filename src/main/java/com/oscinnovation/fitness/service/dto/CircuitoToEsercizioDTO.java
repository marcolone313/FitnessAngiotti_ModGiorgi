package com.oscinnovation.fitness.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.oscinnovation.fitness.domain.CircuitoToEsercizio} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CircuitoToEsercizioDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer reps;

    @NotNull
    private EsercizioDTO esercizio;

    @NotNull
    private CircuitoDTO circuito;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public EsercizioDTO getEsercizio() {
        return esercizio;
    }

    public void setEsercizio(EsercizioDTO esercizio) {
        this.esercizio = esercizio;
    }

    public CircuitoDTO getCircuito() {
        return circuito;
    }

    public void setCircuito(CircuitoDTO circuito) {
        this.circuito = circuito;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CircuitoToEsercizioDTO)) {
            return false;
        }

        CircuitoToEsercizioDTO circuitoToEsercizioDTO = (CircuitoToEsercizioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, circuitoToEsercizioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CircuitoToEsercizioDTO{" +
            "id=" + getId() +
            ", reps=" + getReps() +
            ", esercizio=" + getEsercizio() +
            ", circuito=" + getCircuito() +
            "}";
    }
}
