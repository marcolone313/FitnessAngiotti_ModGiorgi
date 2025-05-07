package com.oscinnovation.fitness.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;

/**
 * A Corsa.
 */
@Entity
@Table(name = "corsa")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Corsa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "distanza_da_percorrere", nullable = false)
    private Float distanzaDaPercorrere;

    @Column(name = "tempo_impiegato")
    private Duration tempoImpiegato;

    @Column(name = "svolto")
    private Boolean svolto;

    @Column(name = "completato")
    private Boolean completato;

    @Lob
    @Column(name = "feedback")
    private String feedback;

    @JsonIgnoreProperties(value = { "schedaSettimanale", "circuito", "corsa" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private AllenamentoGiornaliero allenamentoGiornaliero;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Corsa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getDistanzaDaPercorrere() {
        return this.distanzaDaPercorrere;
    }

    public Corsa distanzaDaPercorrere(Float distanzaDaPercorrere) {
        this.setDistanzaDaPercorrere(distanzaDaPercorrere);
        return this;
    }

    public void setDistanzaDaPercorrere(Float distanzaDaPercorrere) {
        this.distanzaDaPercorrere = distanzaDaPercorrere;
    }

    public Duration getTempoImpiegato() {
        return this.tempoImpiegato;
    }

    public Corsa tempoImpiegato(Duration tempoImpiegato) {
        this.setTempoImpiegato(tempoImpiegato);
        return this;
    }

    public void setTempoImpiegato(Duration tempoImpiegato) {
        this.tempoImpiegato = tempoImpiegato;
    }

    public Boolean getSvolto() {
        return this.svolto;
    }

    public Corsa svolto(Boolean svolto) {
        this.setSvolto(svolto);
        return this;
    }

    public void setSvolto(Boolean svolto) {
        this.svolto = svolto;
    }

    public Boolean getCompletato() {
        return this.completato;
    }

    public Corsa completato(Boolean completato) {
        this.setCompletato(completato);
        return this;
    }

    public void setCompletato(Boolean completato) {
        this.completato = completato;
    }

    public String getFeedback() {
        return this.feedback;
    }

    public Corsa feedback(String feedback) {
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

    public Corsa allenamentoGiornaliero(AllenamentoGiornaliero allenamentoGiornaliero) {
        this.setAllenamentoGiornaliero(allenamentoGiornaliero);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Corsa)) {
            return false;
        }
        return getId() != null && getId().equals(((Corsa) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Corsa{" +
            "id=" + getId() +
            ", distanzaDaPercorrere=" + getDistanzaDaPercorrere() +
            ", tempoImpiegato='" + getTempoImpiegato() + "'" +
            ", svolto='" + getSvolto() + "'" +
            ", completato='" + getCompletato() + "'" +
            ", feedback='" + getFeedback() + "'" +
            "}";
    }
}
