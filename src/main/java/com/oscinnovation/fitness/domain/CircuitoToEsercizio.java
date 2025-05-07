package com.oscinnovation.fitness.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A CircuitoToEsercizio.
 */
@Entity
@Table(name = "circuito_to_esercizio")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CircuitoToEsercizio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "reps", nullable = false)
    private Integer reps;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "gyms", "circuitoToEsercizios", "passaggioEsercizios" }, allowSetters = true)
    private Esercizio esercizio;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "allenamentoGiornaliero", "circuitoToEsercizios" }, allowSetters = true)
    private Circuito circuito;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CircuitoToEsercizio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReps() {
        return this.reps;
    }

    public CircuitoToEsercizio reps(Integer reps) {
        this.setReps(reps);
        return this;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Esercizio getEsercizio() {
        return this.esercizio;
    }

    public void setEsercizio(Esercizio esercizio) {
        this.esercizio = esercizio;
    }

    public CircuitoToEsercizio esercizio(Esercizio esercizio) {
        this.setEsercizio(esercizio);
        return this;
    }

    public Circuito getCircuito() {
        return this.circuito;
    }

    public void setCircuito(Circuito circuito) {
        this.circuito = circuito;
    }

    public CircuitoToEsercizio circuito(Circuito circuito) {
        this.setCircuito(circuito);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CircuitoToEsercizio)) {
            return false;
        }
        return getId() != null && getId().equals(((CircuitoToEsercizio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CircuitoToEsercizio{" +
            "id=" + getId() +
            ", reps=" + getReps() +
            "}";
    }
}
