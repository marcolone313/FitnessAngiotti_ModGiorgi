package com.oscinnovation.fitness.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.oscinnovation.fitness.domain.Dieta} entity. This class is used
 * in {@link com.oscinnovation.fitness.web.rest.DietaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /dietas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DietaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dataCreazione;

    private IntegerFilter mese;

    private StringFilter tipo;

    private StringFilter macros;

    private StringFilter fabbisognoCalorico;

    private LongFilter clienteId;

    private Boolean distinct;

    public DietaCriteria() {}

    public DietaCriteria(DietaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dataCreazione = other.optionalDataCreazione().map(LocalDateFilter::copy).orElse(null);
        this.mese = other.optionalMese().map(IntegerFilter::copy).orElse(null);
        this.tipo = other.optionalTipo().map(StringFilter::copy).orElse(null);
        this.macros = other.optionalMacros().map(StringFilter::copy).orElse(null);
        this.fabbisognoCalorico = other.optionalFabbisognoCalorico().map(StringFilter::copy).orElse(null);
        this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DietaCriteria copy() {
        return new DietaCriteria(this);
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

    public LocalDateFilter getDataCreazione() {
        return dataCreazione;
    }

    public Optional<LocalDateFilter> optionalDataCreazione() {
        return Optional.ofNullable(dataCreazione);
    }

    public LocalDateFilter dataCreazione() {
        if (dataCreazione == null) {
            setDataCreazione(new LocalDateFilter());
        }
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateFilter dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public IntegerFilter getMese() {
        return mese;
    }

    public Optional<IntegerFilter> optionalMese() {
        return Optional.ofNullable(mese);
    }

    public IntegerFilter mese() {
        if (mese == null) {
            setMese(new IntegerFilter());
        }
        return mese;
    }

    public void setMese(IntegerFilter mese) {
        this.mese = mese;
    }

    public StringFilter getTipo() {
        return tipo;
    }

    public Optional<StringFilter> optionalTipo() {
        return Optional.ofNullable(tipo);
    }

    public StringFilter tipo() {
        if (tipo == null) {
            setTipo(new StringFilter());
        }
        return tipo;
    }

    public void setTipo(StringFilter tipo) {
        this.tipo = tipo;
    }

    public StringFilter getMacros() {
        return macros;
    }

    public Optional<StringFilter> optionalMacros() {
        return Optional.ofNullable(macros);
    }

    public StringFilter macros() {
        if (macros == null) {
            setMacros(new StringFilter());
        }
        return macros;
    }

    public void setMacros(StringFilter macros) {
        this.macros = macros;
    }

    public StringFilter getFabbisognoCalorico() {
        return fabbisognoCalorico;
    }

    public Optional<StringFilter> optionalFabbisognoCalorico() {
        return Optional.ofNullable(fabbisognoCalorico);
    }

    public StringFilter fabbisognoCalorico() {
        if (fabbisognoCalorico == null) {
            setFabbisognoCalorico(new StringFilter());
        }
        return fabbisognoCalorico;
    }

    public void setFabbisognoCalorico(StringFilter fabbisognoCalorico) {
        this.fabbisognoCalorico = fabbisognoCalorico;
    }

    public LongFilter getClienteId() {
        return clienteId;
    }

    public Optional<LongFilter> optionalClienteId() {
        return Optional.ofNullable(clienteId);
    }

    public LongFilter clienteId() {
        if (clienteId == null) {
            setClienteId(new LongFilter());
        }
        return clienteId;
    }

    public void setClienteId(LongFilter clienteId) {
        this.clienteId = clienteId;
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
        final DietaCriteria that = (DietaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataCreazione, that.dataCreazione) &&
            Objects.equals(mese, that.mese) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(macros, that.macros) &&
            Objects.equals(fabbisognoCalorico, that.fabbisognoCalorico) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataCreazione, mese, tipo, macros, fabbisognoCalorico, clienteId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DietaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDataCreazione().map(f -> "dataCreazione=" + f + ", ").orElse("") +
            optionalMese().map(f -> "mese=" + f + ", ").orElse("") +
            optionalTipo().map(f -> "tipo=" + f + ", ").orElse("") +
            optionalMacros().map(f -> "macros=" + f + ", ").orElse("") +
            optionalFabbisognoCalorico().map(f -> "fabbisognoCalorico=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
