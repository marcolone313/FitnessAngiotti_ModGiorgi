package com.oscinnovation.fitness.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.oscinnovation.fitness.domain.ClienteToLezioneCorso} entity. This class is used
 * in {@link com.oscinnovation.fitness.web.rest.ClienteToLezioneCorsoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cliente-to-lezione-corsos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteToLezioneCorsoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter completato;

    private LocalDateFilter dataCompletamento;

    private LongFilter clienteId;

    private LongFilter lezioneCorsoId;

    private Boolean distinct;

    public ClienteToLezioneCorsoCriteria() {}

    public ClienteToLezioneCorsoCriteria(ClienteToLezioneCorsoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.completato = other.optionalCompletato().map(BooleanFilter::copy).orElse(null);
        this.dataCompletamento = other.optionalDataCompletamento().map(LocalDateFilter::copy).orElse(null);
        this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
        this.lezioneCorsoId = other.optionalLezioneCorsoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ClienteToLezioneCorsoCriteria copy() {
        return new ClienteToLezioneCorsoCriteria(this);
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

    public LocalDateFilter getDataCompletamento() {
        return dataCompletamento;
    }

    public Optional<LocalDateFilter> optionalDataCompletamento() {
        return Optional.ofNullable(dataCompletamento);
    }

    public LocalDateFilter dataCompletamento() {
        if (dataCompletamento == null) {
            setDataCompletamento(new LocalDateFilter());
        }
        return dataCompletamento;
    }

    public void setDataCompletamento(LocalDateFilter dataCompletamento) {
        this.dataCompletamento = dataCompletamento;
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

    public LongFilter getLezioneCorsoId() {
        return lezioneCorsoId;
    }

    public Optional<LongFilter> optionalLezioneCorsoId() {
        return Optional.ofNullable(lezioneCorsoId);
    }

    public LongFilter lezioneCorsoId() {
        if (lezioneCorsoId == null) {
            setLezioneCorsoId(new LongFilter());
        }
        return lezioneCorsoId;
    }

    public void setLezioneCorsoId(LongFilter lezioneCorsoId) {
        this.lezioneCorsoId = lezioneCorsoId;
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
        final ClienteToLezioneCorsoCriteria that = (ClienteToLezioneCorsoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(completato, that.completato) &&
            Objects.equals(dataCompletamento, that.dataCompletamento) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(lezioneCorsoId, that.lezioneCorsoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, completato, dataCompletamento, clienteId, lezioneCorsoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteToLezioneCorsoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCompletato().map(f -> "completato=" + f + ", ").orElse("") +
            optionalDataCompletamento().map(f -> "dataCompletamento=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalLezioneCorsoId().map(f -> "lezioneCorsoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
