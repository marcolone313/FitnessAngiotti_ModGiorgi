package com.oscinnovation.fitness.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;

/**
 * A Gym.
 */
@Entity
@Table(name = "gym")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Gym implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "sets", nullable = false)
    private Integer sets;

    @NotNull
    @Column(name = "reps", nullable = false)
    private Integer reps;

    @NotNull
    @Column(name = "recupero", nullable = false)
    private Duration recupero;

    @DecimalMin(value = "0")
    @Column(name = "peso")
    private Float peso;

    @Column(name = "completato")
    private Boolean completato;

    @Column(name = "svolto")
    private Boolean svolto;

    @Lob
    @Column(name = "feedback")
    private String feedback;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "schedaSettimanale", "circuito", "corsa" }, allowSetters = true)
    private AllenamentoGiornaliero allenamentoGiornaliero;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "gyms", "circuitoToEsercizios", "passaggioEsercizios" }, allowSetters = true)
    private Esercizio esercizio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Gym id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSets() {
        return this.sets;
    }

    public Gym sets(Integer sets) {
        this.setSets(sets);
        return this;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public Integer getReps() {
        return this.reps;
    }

    public Gym reps(Integer reps) {
        this.setReps(reps);
        return this;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Duration getRecupero() {
        return this.recupero;
    }

    public Gym recupero(Duration recupero) {
        this.setRecupero(recupero);
        return this;
    }

    public void setRecupero(Duration recupero) {
        this.recupero = recupero;
    }

    public Float getPeso() {
        return this.peso;
    }

    public Gym peso(Float peso) {
        this.setPeso(peso);
        return this;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public Boolean getCompletato() {
        return this.completato;
    }

    public Gym completato(Boolean completato) {
        this.setCompletato(completato);
        return this;
    }

    public void setCompletato(Boolean completato) {
        this.completato = completato;
    }

    public Boolean getSvolto() {
        return this.svolto;
    }

    public Gym svolto(Boolean svolto) {
        this.setSvolto(svolto);
        return this;
    }

    public void setSvolto(Boolean svolto) {
        this.svolto = svolto;
    }

    public String getFeedback() {
        return this.feedback;
    }

    public Gym feedback(String feedback) {
        this.setFeedback(feedback);
        return this;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public AllenamentoGiornaliero getAllenamentoGiornaliero() {
        return this.allenamentoGiornaliero;
    }

    public void setAllenamentoGiornaliero(AllenamentoGiornaliero allenamentoGiornaliero) {
        this.allenamentoGiornaliero = allenamentoGiornaliero;
    }

    public Gym allenamentoGiornaliero(AllenamentoGiornaliero allenamentoGiornaliero) {
        this.setAllenamentoGiornaliero(allenamentoGiornaliero);
        return this;
    }

    public Esercizio getEsercizio() {
        return this.esercizio;
    }

    public void setEsercizio(Esercizio esercizio) {
        this.esercizio = esercizio;
    }

    public Gym esercizio(Esercizio esercizio) {
        this.setEsercizio(esercizio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gym)) {
            return false;
        }
        return getId() != null && getId().equals(((Gym) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Gym{" +
            "id=" + getId() +
            ", sets=" + getSets() +
            ", reps=" + getReps() +
            ", recupero='" + getRecupero() + "'" +
            ", peso=" + getPeso() +
            ", completato='" + getCompletato() + "'" +
            ", svolto='" + getSvolto() + "'" +
            ", feedback='" + getFeedback() + "'" +
            "}";
    }
}
