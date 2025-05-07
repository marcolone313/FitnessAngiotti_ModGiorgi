package com.oscinnovation.fitness.service.criteria;

import com.oscinnovation.fitness.domain.enumeration.TipoAllenamento;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.oscinnovation.fitness.domain.AllenamentoGiornaliero} entity. This class is used
 * in {@link com.oscinnovation.fitness.web.rest.AllenamentoGiornalieroResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /allenamento-giornalieros?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AllenamentoGiornalieroCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoAllenamento
     */
    public static class TipoAllenamentoFilter extends Filter<TipoAllenamento> {

        public TipoAllenamentoFilter() {}

        public TipoAllenamentoFilter(TipoAllenamentoFilter filter) {
            super(filter);
        }

        @Override
        public TipoAllenamentoFilter copy() {
            return new TipoAllenamentoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TipoAllenamentoFilter tipo;

    private IntegerFilter giorno;

    private BooleanFilter svolto;

    private LocalDateFilter dataAllenamento;

    private LongFilter schedaSettimanaleId;

    private LongFilter circuitoId;

    private LongFilter corsaId;

    private Boolean distinct;

    public AllenamentoGiornalieroCriteria() {}

    public AllenamentoGiornalieroCriteria(AllenamentoGiornalieroCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tipo = other.optionalTipo().map(TipoAllenamentoFilter::copy).orElse(null);
        this.giorno = other.optionalGiorno().map(IntegerFilter::copy).orElse(null);
        this.svolto = other.optionalSvolto().map(BooleanFilter::copy).orElse(null);
        this.dataAllenamento = other.optionalDataAllenamento().map(LocalDateFilter::copy).orElse(null);
        this.schedaSettimanaleId = other.optionalSchedaSettimanaleId().map(LongFilter::copy).orElse(null);
        this.circuitoId = other.optionalCircuitoId().map(LongFilter::copy).orElse(null);
        this.corsaId = other.optionalCorsaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AllenamentoGiornalieroCriteria copy() {
        return new AllenamentoGiornalieroCriteria(this);
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

    public TipoAllenamentoFilter getTipo() {
        return tipo;
    }

    public Optional<TipoAllenamentoFilter> optionalTipo() {
        return Optional.ofNullable(tipo);
    }

    public TipoAllenamentoFilter tipo() {
        if (tipo == null) {
            setTipo(new TipoAllenamentoFilter());
        }
        return tipo;
    }

    public void setTipo(TipoAllenamentoFilter tipo) {
        this.tipo = tipo;
    }

    public IntegerFilter getGiorno() {
        return giorno;
    }

    public Optional<IntegerFilter> optionalGiorno() {
        return Optional.ofNullable(giorno);
    }

    public IntegerFilter giorno() {
        if (giorno == null) {
            setGiorno(new IntegerFilter());
        }
        return giorno;
    }

    public void setGiorno(IntegerFilter giorno) {
        this.giorno = giorno;
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

    public LocalDateFilter getDataAllenamento() {
        return dataAllenamento;
    }

    public Optional<LocalDateFilter> optionalDataAllenamento() {
        return Optional.ofNullable(dataAllenamento);
    }

    public LocalDateFilter dataAllenamento() {
        if (dataAllenamento == null) {
            setDataAllenamento(new LocalDateFilter());
        }
        return dataAllenamento;
    }

    public void setDataAllenamento(LocalDateFilter dataAllenamento) {
        this.dataAllenamento = dataAllenamento;
    }

    public LongFilter getSchedaSettimanaleId() {
        return schedaSettimanaleId;
    }

    public Optional<LongFilter> optionalSchedaSettimanaleId() {
        return Optional.ofNullable(schedaSettimanaleId);
    }

    public LongFilter schedaSettimanaleId() {
        if (schedaSettimanaleId == null) {
            setSchedaSettimanaleId(new LongFilter());
        }
        return schedaSettimanaleId;
    }

    public void setSchedaSettimanaleId(LongFilter schedaSettimanaleId) {
        this.schedaSettimanaleId = schedaSettimanaleId;
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

    public LongFilter getCorsaId() {
        return corsaId;
    }

    public Optional<LongFilter> optionalCorsaId() {
        return Optional.ofNullable(corsaId);
    }

    public LongFilter corsaId() {
        if (corsaId == null) {
            setCorsaId(new LongFilter());
        }
        return corsaId;
    }

    public void setCorsaId(LongFilter corsaId) {
        this.corsaId = corsaId;
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
        final AllenamentoGiornalieroCriteria that = (AllenamentoGiornalieroCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(giorno, that.giorno) &&
            Objects.equals(svolto, that.svolto) &&
            Objects.equals(dataAllenamento, that.dataAllenamento) &&
            Objects.equals(schedaSettimanaleId, that.schedaSettimanaleId) &&
            Objects.equals(circuitoId, that.circuitoId) &&
            Objects.equals(corsaId, that.corsaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipo, giorno, svolto, dataAllenamento, schedaSettimanaleId, circuitoId, corsaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AllenamentoGiornalieroCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTipo().map(f -> "tipo=" + f + ", ").orElse("") +
            optionalGiorno().map(f -> "giorno=" + f + ", ").orElse("") +
            optionalSvolto().map(f -> "svolto=" + f + ", ").orElse("") +
            optionalDataAllenamento().map(f -> "dataAllenamento=" + f + ", ").orElse("") +
            optionalSchedaSettimanaleId().map(f -> "schedaSettimanaleId=" + f + ", ").orElse("") +
            optionalCircuitoId().map(f -> "circuitoId=" + f + ", ").orElse("") +
            optionalCorsaId().map(f -> "corsaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
