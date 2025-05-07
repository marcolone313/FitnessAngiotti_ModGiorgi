package com.oscinnovation.fitness.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * A Circuito.
 */
@Entity
@Table(name = "circuito")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Circuito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tempo_limite")
    private Duration tempoLimite;

    @Column(name = "tempo_impiegato")
    private Duration tempoImpiegato;

    @Column(name = "catena_ripetizioni")
    private String catenaRipetizioni;

    @Column(name = "circuiti_completati")
    private Integer circuitiCompletati;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "circuito")
    @JsonIgnoreProperties(value = { "esercizio", "circuito" }, allowSetters = true)
    private Set<CircuitoToEsercizio> circuitoToEsercizios = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Circuito id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Duration getTempoLimite() {
        return this.tempoLimite;
    }

    public Circuito tempoLimite(Duration tempoLimite) {
        this.setTempoLimite(tempoLimite);
        return this;
    }

    public void setTempoLimite(Duration tempoLimite) {
        this.tempoLimite = tempoLimite;
    }

    public Duration getTempoImpiegato() {
        return this.tempoImpiegato;
    }

    public Circuito tempoImpiegato(Duration tempoImpiegato) {
        this.setTempoImpiegato(tempoImpiegato);
        return this;
    }

    public void setTempoImpiegato(Duration tempoImpiegato) {
        this.tempoImpiegato = tempoImpiegato;
    }

    public String getCatenaRipetizioni() {
        return this.catenaRipetizioni;
    }

    public Circuito catenaRipetizioni(String catenaRipetizioni) {
        this.setCatenaRipetizioni(catenaRipetizioni);
        return this;
    }

    public void setCatenaRipetizioni(String catenaRipetizioni) {
        this.catenaRipetizioni = catenaRipetizioni;
    }

    public Integer getCircuitiCompletati() {
        return this.circuitiCompletati;
    }

    public Circuito circuitiCompletati(Integer circuitiCompletati) {
        this.setCircuitiCompletati(circuitiCompletati);
        return this;
    }

    public void setCircuitiCompletati(Integer circuitiCompletati) {
        this.circuitiCompletati = circuitiCompletati;
    }

    public Boolean getSvolto() {
        return this.svolto;
    }

    public Circuito svolto(Boolean svolto) {
        this.setSvolto(svolto);
        return this;
    }

    public void setSvolto(Boolean svolto) {
        this.svolto = svolto;
    }

    public Boolean getCompletato() {
        return this.completato;
    }

    public Circuito completato(Boolean completato) {
        this.setCompletato(completato);
        return this;
    }

    public void setCompletato(Boolean completato) {
        this.completato = completato;
    }

    public String getFeedback() {
        return this.feedback;
    }

    public Circuito feedback(String feedback) {
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

    public Circuito allenamentoGiornaliero(AllenamentoGiornaliero allenamentoGiornaliero) {
        this.setAllenamentoGiornaliero(allenamentoGiornaliero);
        return this;
    }

    public Set<CircuitoToEsercizio> getCircuitoToEsercizios() {
        return this.circuitoToEsercizios;
    }

    public void setCircuitoToEsercizios(Set<CircuitoToEsercizio> circuitoToEsercizios) {
        if (this.circuitoToEsercizios != null) {
            this.circuitoToEsercizios.forEach(i -> i.setCircuito(null));
        }
        if (circuitoToEsercizios != null) {
            circuitoToEsercizios.forEach(i -> i.setCircuito(this));
        }
        this.circuitoToEsercizios = circuitoToEsercizios;
    }

    public Circuito circuitoToEsercizios(Set<CircuitoToEsercizio> circuitoToEsercizios) {
        this.setCircuitoToEsercizios(circuitoToEsercizios);
        return this;
    }

    public Circuito addCircuitoToEsercizio(CircuitoToEsercizio circuitoToEsercizio) {
        this.circuitoToEsercizios.add(circuitoToEsercizio);
        circuitoToEsercizio.setCircuito(this);
        return this;
    }

    public Circuito removeCircuitoToEsercizio(CircuitoToEsercizio circuitoToEsercizio) {
        this.circuitoToEsercizios.remove(circuitoToEsercizio);
        circuitoToEsercizio.setCircuito(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Circuito)) {
            return false;
        }
        return getId() != null && getId().equals(((Circuito) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Circuito{" +
            "id=" + getId() +
            ", tempoLimite='" + getTempoLimite() + "'" +
            ", tempoImpiegato='" + getTempoImpiegato() + "'" +
            ", catenaRipetizioni='" + getCatenaRipetizioni() + "'" +
            ", circuitiCompletati=" + getCircuitiCompletati() +
            ", svolto='" + getSvolto() + "'" +
            ", completato='" + getCompletato() + "'" +
            ", feedback='" + getFeedback() + "'" +
            "}";
    }
}
