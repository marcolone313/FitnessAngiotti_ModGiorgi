package com.oscinnovation.fitness.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.oscinnovation.fitness.domain.SchedaSettimanale} entity. This class is used
 * in {@link com.oscinnovation.fitness.web.rest.SchedaSettimanaleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /scheda-settimanales?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SchedaSettimanaleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter anno;

    private IntegerFilter mese;

    private IntegerFilter settimana;

    private LocalDateFilter dataCreazione;

    private LongFilter clienteId;

    private LongFilter reportSettimanaleId;

    private Boolean distinct;

    public SchedaSettimanaleCriteria() {}

    public SchedaSettimanaleCriteria(SchedaSettimanaleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.anno = other.optionalAnno().map(IntegerFilter::copy).orElse(null);
        this.mese = other.optionalMese().map(IntegerFilter::copy).orElse(null);
        this.settimana = other.optionalSettimana().map(IntegerFilter::copy).orElse(null);
        this.dataCreazione = other.optionalDataCreazione().map(LocalDateFilter::copy).orElse(null);
        this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
        this.reportSettimanaleId = other.optionalReportSettimanaleId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SchedaSettimanaleCriteria copy() {
        return new SchedaSettimanaleCriteria(this);
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

    public IntegerFilter getAnno() {
        return anno;
    }

    public Optional<IntegerFilter> optionalAnno() {
        return Optional.ofNullable(anno);
    }

    public IntegerFilter anno() {
        if (anno == null) {
            setAnno(new IntegerFilter());
        }
        return anno;
    }

    public void setAnno(IntegerFilter anno) {
        this.anno = anno;
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

    public IntegerFilter getSettimana() {
        return settimana;
    }

    public Optional<IntegerFilter> optionalSettimana() {
        return Optional.ofNullable(settimana);
    }

    public IntegerFilter settimana() {
        if (settimana == null) {
            setSettimana(new IntegerFilter());
        }
        return settimana;
    }

    public void setSettimana(IntegerFilter settimana) {
        this.settimana = settimana;
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

    public LongFilter getReportSettimanaleId() {
        return reportSettimanaleId;
    }

    public Optional<LongFilter> optionalReportSettimanaleId() {
        return Optional.ofNullable(reportSettimanaleId);
    }

    public LongFilter reportSettimanaleId() {
        if (reportSettimanaleId == null) {
            setReportSettimanaleId(new LongFilter());
        }
        return reportSettimanaleId;
    }

    public void setReportSettimanaleId(LongFilter reportSettimanaleId) {
        this.reportSettimanaleId = reportSettimanaleId;
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
        final SchedaSettimanaleCriteria that = (SchedaSettimanaleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(anno, that.anno) &&
            Objects.equals(mese, that.mese) &&
            Objects.equals(settimana, that.settimana) &&
            Objects.equals(dataCreazione, that.dataCreazione) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(reportSettimanaleId, that.reportSettimanaleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, anno, mese, settimana, dataCreazione, clienteId, reportSettimanaleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchedaSettimanaleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAnno().map(f -> "anno=" + f + ", ").orElse("") +
            optionalMese().map(f -> "mese=" + f + ", ").orElse("") +
            optionalSettimana().map(f -> "settimana=" + f + ", ").orElse("") +
            optionalDataCreazione().map(f -> "dataCreazione=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalReportSettimanaleId().map(f -> "reportSettimanaleId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
