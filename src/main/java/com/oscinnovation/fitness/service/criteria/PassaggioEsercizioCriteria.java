package com.oscinnovation.fitness.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.oscinnovation.fitness.domain.PassaggioEsercizio} entity. This class is used
 * in {@link com.oscinnovation.fitness.web.rest.PassaggioEsercizioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /passaggio-esercizios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PassaggioEsercizioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter indice;

    private LongFilter esercizioId;

    private Boolean distinct;

    public PassaggioEsercizioCriteria() {}

    public PassaggioEsercizioCriteria(PassaggioEsercizioCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.indice = other.optionalIndice().map(IntegerFilter::copy).orElse(null);
        this.esercizioId = other.optionalEsercizioId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PassaggioEsercizioCriteria copy() {
        return new PassaggioEsercizioCriteria(this);
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

    public IntegerFilter getIndice() {
        return indice;
    }

    public Optional<IntegerFilter> optionalIndice() {
        return Optional.ofNullable(indice);
    }

    public IntegerFilter indice() {
        if (indice == null) {
            setIndice(new IntegerFilter());
        }
        return indice;
    }

    public void setIndice(IntegerFilter indice) {
        this.indice = indice;
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
        final PassaggioEsercizioCriteria that = (PassaggioEsercizioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(indice, that.indice) &&
            Objects.equals(esercizioId, that.esercizioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, indice, esercizioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PassaggioEsercizioCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIndice().map(f -> "indice=" + f + ", ").orElse("") +
            optionalEsercizioId().map(f -> "esercizioId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
