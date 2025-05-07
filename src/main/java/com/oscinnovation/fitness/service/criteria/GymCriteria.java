package com.oscinnovation.fitness.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.oscinnovation.fitness.domain.Gym} entity. This class is used
 * in {@link com.oscinnovation.fitness.web.rest.GymResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gyms?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GymCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter sets;

    private IntegerFilter reps;

    private DurationFilter recupero;

    private FloatFilter peso;

    private BooleanFilter completato;

    private BooleanFilter svolto;

    private LongFilter allenamentoGiornalieroId;

    private LongFilter esercizioId;

    private Boolean distinct;

    public GymCriteria() {}

    public GymCriteria(GymCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sets = other.optionalSets().map(IntegerFilter::copy).orElse(null);
        this.reps = other.optionalReps().map(IntegerFilter::copy).orElse(null);
        this.recupero = other.optionalRecupero().map(DurationFilter::copy).orElse(null);
        this.peso = other.optionalPeso().map(FloatFilter::copy).orElse(null);
        this.completato = other.optionalCompletato().map(BooleanFilter::copy).orElse(null);
        this.svolto = other.optionalSvolto().map(BooleanFilter::copy).orElse(null);
        this.allenamentoGiornalieroId = other.optionalAllenamentoGiornalieroId().map(LongFilter::copy).orElse(null);
        this.esercizioId = other.optionalEsercizioId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public GymCriteria copy() {
        return new GymCriteria(this);
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

    public IntegerFilter getSets() {
        return sets;
    }

    public Optional<IntegerFilter> optionalSets() {
        return Optional.ofNullable(sets);
    }

    public IntegerFilter sets() {
        if (sets == null) {
            setSets(new IntegerFilter());
        }
        return sets;
    }

    public void setSets(IntegerFilter sets) {
        this.sets = sets;
    }

    public IntegerFilter getReps() {
        return reps;
    }

    public Optional<IntegerFilter> optionalReps() {
        return Optional.ofNullable(reps);
    }

    public IntegerFilter reps() {
        if (reps == null) {
            setReps(new IntegerFilter());
        }
        return reps;
    }

    public void setReps(IntegerFilter reps) {
        this.reps = reps;
    }

    public DurationFilter getRecupero() {
        return recupero;
    }

    public Optional<DurationFilter> optionalRecupero() {
        return Optional.ofNullable(recupero);
    }

    public DurationFilter recupero() {
        if (recupero == null) {
            setRecupero(new DurationFilter());
        }
        return recupero;
    }

    public void setRecupero(DurationFilter recupero) {
        this.recupero = recupero;
    }

    public FloatFilter getPeso() {
        return peso;
    }

    public Optional<FloatFilter> optionalPeso() {
        return Optional.ofNullable(peso);
    }

    public FloatFilter peso() {
        if (peso == null) {
            setPeso(new FloatFilter());
        }
        return peso;
    }

    public void setPeso(FloatFilter peso) {
        this.peso = peso;
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

    public LongFilter getEsercizioId() {
        return esercizioId;
    }

    public Optional<LongFilter> optionalEsercizioId() {
        return Optional.ofNullable(esercizioId);
    }

    public LongFilter esercizioId() {
        if (esercizioId == null) {
            setEsercizioId(new LongFilter());
        }
        return esercizioId;
    }

    public void setEsercizioId(LongFilter esercizioId) {
        this.esercizioId = esercizioId;
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
        final GymCriteria that = (GymCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sets, that.sets) &&
            Objects.equals(reps, that.reps) &&
            Objects.equals(recupero, that.recupero) &&
            Objects.equals(peso, that.peso) &&
            Objects.equals(completato, that.completato) &&
            Objects.equals(svolto, that.svolto) &&
            Objects.equals(allenamentoGiornalieroId, that.allenamentoGiornalieroId) &&
            Objects.equals(esercizioId, that.esercizioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sets, reps, recupero, peso, completato, svolto, allenamentoGiornalieroId, esercizioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GymCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSets().map(f -> "sets=" + f + ", ").orElse("") +
            optionalReps().map(f -> "reps=" + f + ", ").orElse("") +
            optionalRecupero().map(f -> "recupero=" + f + ", ").orElse("") +
            optionalPeso().map(f -> "peso=" + f + ", ").orElse("") +
            optionalCompletato().map(f -> "completato=" + f + ", ").orElse("") +
            optionalSvolto().map(f -> "svolto=" + f + ", ").orElse("") +
            optionalAllenamentoGiornalieroId().map(f -> "allenamentoGiornalieroId=" + f + ", ").orElse("") +
            optionalEsercizioId().map(f -> "esercizioId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
