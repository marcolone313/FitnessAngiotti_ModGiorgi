package com.oscinnovation.fitness.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.oscinnovation.fitness.domain.Corsa} entity. This class is used
 * in {@link com.oscinnovation.fitness.web.rest.CorsaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /corsas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CorsaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter distanzaDaPercorrere;

    private DurationFilter tempoImpiegato;

    private BooleanFilter svolto;

    private BooleanFilter completato;

    private LongFilter allenamentoGiornalieroId;

    private Boolean distinct;

    public CorsaCriteria() {}

    public CorsaCriteria(CorsaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.distanzaDaPercorrere = other.optionalDistanzaDaPercorrere().map(FloatFilter::copy).orElse(null);
        this.tempoImpiegato = other.optionalTempoImpiegato().map(DurationFilter::copy).orElse(null);
        this.svolto = other.optionalSvolto().map(BooleanFilter::copy).orElse(null);
        this.completato = other.optionalCompletato().map(BooleanFilter::copy).orElse(null);
        this.allenamentoGiornalieroId = other.optionalAllenamentoGiornalieroId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CorsaCriteria copy() {
        return new CorsaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public FloatFilter getDistanzaDaPercorrere() {
        return distanzaDaPercorrere;
    }

    public Optional<FloatFilter> optionalDistanzaDaPercorrere() {
        return Optional.ofNullable(distanzaDaPercorrere);
    }

    public FloatFilter distanzaDaPercorrere() {
        if (distanzaDaPercorrere == null) {
            setDistanzaDaPercorrere(new FloatFilter());
        }
        return distanzaDaPercorrere;
    }

    public void setDistanzaDaPercorrere(FloatFilter distanzaDaPercorrere) {
        this.distanzaDaPercorrere = distanzaDaPercorrere;
    }

    public DurationFilter getTempoImpiegato() {
        return tempoImpiegato;
    }

    public Optional<DurationFilter> optionalTempoImpiegato() {
        return Optional.ofNullable(tempoImpiegato);
    }

    public DurationFilter tempoImpiegato() {
        if (tempoImpiegato == null) {
            setTempoImpiegato(new DurationFilter());
        }
        return tempoImpiegato;
    }

    public void setTempoImpiegato(DurationFilter tempoImpiegato) {
        this.tempoImpiegato = tempoImpiegato;
    }

    public BooleanFilter getSvolto() {
        return svolto;
    }

    public Optional<BooleanFilter> optionalSvolto() {
        return Optional.ofNullable(svolto);
    }

    public BooleanFilter svolto() {
        if (svolto == null) {
            setSvolto(new BooleanFilter());
        }
        return svolto;
    }

    public void setSvolto(BooleanFilter svolto) {
        this.svolto = svolto;
    }

    public BooleanFilter getCompletato() {
        return completato;
    }

    public Optional<BooleanFilter> optionalCompletato() {
        return Optional.ofNullable(completato);
    }

    public BooleanFilter completato() {
        if (completato == null) {
            setCompletato(new BooleanFilter());
        }
        return completato;
    }

    public void setCompletato(BooleanFilter completato) {
        this.completato = completato;
    }

    public LongFilter getAllenamentoGiornalieroId() {
        return allenamentoGiornalieroId;
    }

    public Optional<LongFilter> optionalAllenamentoGiornalieroId() {
        return Optional.ofNullable(allenamentoGiornalieroId);
    }

    public LongFilter allenamentoGiornalieroId() {
        if (allenamentoGiornalieroId == null) {
            setAllenamentoGiornalieroId(new LongFilter());
        }
        return allenamentoGiornalieroId;
    }

    public void setAllenamentoGiornalieroId(LongFilter allenamentoGiornalieroId) {
        this.allenamentoGiornalieroId = allenamentoGiornalieroId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CorsaCriteria that = (CorsaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(distanzaDaPercorrere, that.distanzaDaPercorrere) &&
            Objects.equals(tempoImpiegato, that.tempoImpiegato) &&
            Objects.equals(svolto, that.svolto) &&
            Objects.equals(completato, that.completato) &&
            Objects.equals(allenamentoGiornalieroId, that.allenamentoGiornalieroId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, distanzaDaPercorrere, tempoImpiegato, svolto, completato, allenamentoGiornalieroId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CorsaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDistanzaDaPercorrere().map(f -> "distanzaDaPercorrere=" + f + ", ").orElse("") +
            optionalTempoImpiegato().map(f -> "tempoImpiegato=" + f + ", ").orElse("") +
            optionalSvolto().map(f -> "svolto=" + f + ", ").orElse("") +
            optionalCompletato().map(f -> "completato=" + f + ", ").orElse("") +
            optionalAllenamentoGiornalieroId().map(f -> "allenamentoGiornalieroId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
