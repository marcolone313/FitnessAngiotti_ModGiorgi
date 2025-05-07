package com.oscinnovation.fitness.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.oscinnovation.fitness.domain.PesoCliente} entity. This class is used
 * in {@link com.oscinnovation.fitness.web.rest.PesoClienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /peso-clientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PesoClienteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter peso;

    private IntegerFilter mese;

    private LocalDateFilter dataInserimento;

    private LongFilter clienteId;

    private Boolean distinct;

    public PesoClienteCriteria() {}

    public PesoClienteCriteria(PesoClienteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.peso = other.optionalPeso().map(FloatFilter::copy).orElse(null);
        this.mese = other.optionalMese().map(IntegerFilter::copy).orElse(null);
        this.dataInserimento = other.optionalDataInserimento().map(LocalDateFilter::copy).orElse(null);
        this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PesoClienteCriteria copy() {
        return new PesoClienteCriteria(this);
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

    public LocalDateFilter getDataInserimento() {
        return dataInserimento;
    }

    public Optional<LocalDateFilter> optionalDataInserimento() {
        return Optional.ofNullable(dataInserimento);
    }

    public LocalDateFilter dataInserimento() {
        if (dataInserimento == null) {
            setDataInserimento(new LocalDateFilter());
        }
        return dataInserimento;
    }

    public void setDataInserimento(LocalDateFilter dataInserimento) {
        this.dataInserimento = dataInserimento;
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
        final PesoClienteCriteria that = (PesoClienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(peso, that.peso) &&
            Objects.equals(mese, that.mese) &&
            Objects.equals(dataInserimento, that.dataInserimento) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, peso, mese, dataInserimento, clienteId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PesoClienteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPeso().map(f -> "peso=" + f + ", ").orElse("") +
            optionalMese().map(f -> "mese=" + f + ", ").orElse("") +
            optionalDataInserimento().map(f -> "dataInserimento=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
