package com.oscinnovation.fitness.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.oscinnovation.fitness.domain.CircuitoToEsercizio} entity. This class is used
 * in {@link com.oscinnovation.fitness.web.rest.CircuitoToEsercizioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /circuito-to-esercizios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CircuitoToEsercizioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter reps;

    private LongFilter esercizioId;

    private LongFilter circuitoId;

    private Boolean distinct;

    public CircuitoToEsercizioCriteria() {}

    public CircuitoToEsercizioCriteria(CircuitoToEsercizioCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.reps = other.optionalReps().map(IntegerFilter::copy).orElse(null);
        this.esercizioId = other.optionalEsercizioId().map(LongFilter::copy).orElse(null);
        this.circuitoId = other.optionalCircuitoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CircuitoToEsercizioCriteria copy() {
        return new CircuitoToEsercizioCriteria(this);
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

    public LongFilter getCircuitoId() {
        return circuitoId;
    }

    public Optional<LongFilter> optionalCircuitoId() {
        return Optional.ofNullable(circuitoId);
    }

    public LongFilter circuitoId() {
        if (circuitoId == null) {
            setCircuitoId(new LongFilter());
        }
        return circuitoId;
    }

    public void setCircuitoId(LongFilter circuitoId) {
        this.circuitoId = circuitoId;
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
        final CircuitoToEsercizioCriteria that = (CircuitoToEsercizioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reps, that.reps) &&
            Objects.equals(esercizioId, that.esercizioId) &&
            Objects.equals(circuitoId, that.circuitoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reps, esercizioId, circuitoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CircuitoToEsercizioCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalReps().map(f -> "reps=" + f + ", ").orElse("") +
            optionalEsercizioId().map(f -> "esercizioId=" + f + ", ").orElse("") +
            optionalCircuitoId().map(f -> "circuitoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
