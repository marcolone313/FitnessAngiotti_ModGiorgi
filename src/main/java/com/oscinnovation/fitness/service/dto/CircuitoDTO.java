package com.oscinnovation.fitness.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

/**
 * A DTO for the {@link com.oscinnovation.fitness.domain.Circuito} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CircuitoDTO implements Serializable {

    private Long id;

    private Duration tempoLimite;

    private Duration tempoImpiegato;

    private String catenaRipetizioni;

    private Integer circuitiCompletati;

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

    public Duration getTempoLimite() {
        return tempoLimite;
    }

    public void setTempoLimite(Duration tempoLimite) {
        this.tempoLimite = tempoLimite;
    }

    public Duration getTempoImpiegato() {
        return tempoImpiegato;
    }

    public void setTempoImpiegato(Duration tempoImpiegato) {
        this.tempoImpiegato = tempoImpiegato;
    }

    public String getCatenaRipetizioni() {
        return catenaRipetizioni;
    }

    public void setCatenaRipetizioni(String catenaRipetizioni) {
        this.catenaRipetizioni = catenaRipetizioni;
    }

    public Integer getCircuitiCompletati() {
        return circuitiCompletati;
    }

    public void setCircuitiCompletati(Integer circuitiCompletati) {
        this.circuitiCompletati = circuitiCompletati;
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
        if (!(o instanceof CircuitoDTO)) {
            return false;
        }

        CircuitoDTO circuitoDTO = (CircuitoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, circuitoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CircuitoDTO{" +
            "id=" + getId() +
            ", tempoLimite='" + getTempoLimite() + "'" +
            ", tempoImpiegato='" + getTempoImpiegato() + "'" +
            ", catenaRipetizioni='" + getCatenaRipetizioni() + "'" +
            ", circuitiCompletati=" + getCircuitiCompletati() +
            ", svolto='" + getSvolto() + "'" +
            ", completato='" + getCompletato() + "'" +
            ", feedback='" + getFeedback() + "'" +
            ", allenamentoGiornaliero=" + getAllenamentoGiornaliero() +
            "}";
    }
}
