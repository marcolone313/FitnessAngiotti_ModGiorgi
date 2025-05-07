package com.oscinnovation.fitness.service.criteria;

import com.oscinnovation.fitness.domain.enumeration.Voto;
import com.oscinnovation.fitness.domain.enumeration.Voto;
import com.oscinnovation.fitness.domain.enumeration.Voto;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.oscinnovation.fitness.domain.ReportSettimanale} entity. This class is used
 * in {@link com.oscinnovation.fitness.web.rest.ReportSettimanaleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /report-settimanales?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportSettimanaleCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Voto
     */
    public static class VotoFilter extends Filter<Voto> {

        public VotoFilter() {}

        public VotoFilter(VotoFilter filter) {
            super(filter);
        }

        @Override
        public VotoFilter copy() {
            return new VotoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private VotoFilter voto;

    private VotoFilter giorniDieta;

    private FloatFilter pesoMedio;

    private VotoFilter qualitaSonno;

    private DurationFilter mediaOreSonno;

    private LocalDateFilter dataCreazione;

    private LocalDateFilter dataScadenza;

    private LocalDateFilter dataCompletamento;

    private BooleanFilter puntuale;

    private LongFilter schedaSettimanaleId;

    private LongFilter clienteId;

    private Boolean distinct;

    public ReportSettimanaleCriteria() {}

    public ReportSettimanaleCriteria(ReportSettimanaleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.voto = other.optionalVoto().map(VotoFilter::copy).orElse(null);
        this.giorniDieta = other.optionalGiorniDieta().map(VotoFilter::copy).orElse(null);
        this.pesoMedio = other.optionalPesoMedio().map(FloatFilter::copy).orElse(null);
        this.qualitaSonno = other.optionalQualitaSonno().map(VotoFilter::copy).orElse(null);
        this.mediaOreSonno = other.optionalMediaOreSonno().map(DurationFilter::copy).orElse(null);
        this.dataCreazione = other.optionalDataCreazione().map(LocalDateFilter::copy).orElse(null);
        this.dataScadenza = other.optionalDataScadenza().map(LocalDateFilter::copy).orElse(null);
        this.dataCompletamento = other.optionalDataCompletamento().map(LocalDateFilter::copy).orElse(null);
        this.puntuale = other.optionalPuntuale().map(BooleanFilter::copy).orElse(null);
        this.schedaSettimanaleId = other.optionalSchedaSettimanaleId().map(LongFilter::copy).orElse(null);
        this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReportSettimanaleCriteria copy() {
        return new ReportSettimanaleCriteria(this);
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

    public VotoFilter getVoto() {
        return voto;
    }

    public Optional<VotoFilter> optionalVoto() {
        return Optional.ofNullable(voto);
    }

    public VotoFilter voto() {
        if (voto == null) {
            setVoto(new VotoFilter());
        }
        return voto;
    }

    public void setVoto(VotoFilter voto) {
        this.voto = voto;
    }

    public VotoFilter getGiorniDieta() {
        return giorniDieta;
    }

    public Optional<VotoFilter> optionalGiorniDieta() {
        return Optional.ofNullable(giorniDieta);
    }

    public VotoFilter giorniDieta() {
        if (giorniDieta == null) {
            setGiorniDieta(new VotoFilter());
        }
        return giorniDieta;
    }

    public void setGiorniDieta(VotoFilter giorniDieta) {
        this.giorniDieta = giorniDieta;
    }

    public FloatFilter getPesoMedio() {
        return pesoMedio;
    }

    public Optional<FloatFilter> optionalPesoMedio() {
        return Optional.ofNullable(pesoMedio);
    }

    public FloatFilter pesoMedio() {
        if (pesoMedio == null) {
            setPesoMedio(new FloatFilter());
        }
        return pesoMedio;
    }

    public void setPesoMedio(FloatFilter pesoMedio) {
        this.pesoMedio = pesoMedio;
    }

    public VotoFilter getQualitaSonno() {
        return qualitaSonno;
    }

    public Optional<VotoFilter> optionalQualitaSonno() {
        return Optional.ofNullable(qualitaSonno);
    }

    public VotoFilter qualitaSonno() {
        if (qualitaSonno == null) {
            setQualitaSonno(new VotoFilter());
        }
        return qualitaSonno;
    }

    public void setQualitaSonno(VotoFilter qualitaSonno) {
        this.qualitaSonno = qualitaSonno;
    }

    public DurationFilter getMediaOreSonno() {
        return mediaOreSonno;
    }

    public Optional<DurationFilter> optionalMediaOreSonno() {
        return Optional.ofNullable(mediaOreSonno);
    }

    public DurationFilter mediaOreSonno() {
        if (mediaOreSonno == null) {
            setMediaOreSonno(new DurationFilter());
        }
        return mediaOreSonno;
    }

    public void setMediaOreSonno(DurationFilter mediaOreSonno) {
        this.mediaOreSonno = mediaOreSonno;
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

    public LocalDateFilter getDataScadenza() {
        return dataScadenza;
    }

    public Optional<LocalDateFilter> optionalDataScadenza() {
        return Optional.ofNullable(dataScadenza);
    }

    public LocalDateFilter dataScadenza() {
        if (dataScadenza == null) {
            setDataScadenza(new LocalDateFilter());
        }
        return dataScadenza;
    }

    public void setDataScadenza(LocalDateFilter dataScadenza) {
        this.dataScadenza = dataScadenza;
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

    public BooleanFilter getPuntuale() {
        return puntuale;
    }

    public Optional<BooleanFilter> optionalPuntuale() {
        return Optional.ofNullable(puntuale);
    }

    public BooleanFilter puntuale() {
        if (puntuale == null) {
            setPuntuale(new BooleanFilter());
        }
        return puntuale;
    }

    public void setPuntuale(BooleanFilter puntuale) {
        this.puntuale = puntuale;
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
        final ReportSettimanaleCriteria that = (ReportSettimanaleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(voto, that.voto) &&
            Objects.equals(giorniDieta, that.giorniDieta) &&
            Objects.equals(pesoMedio, that.pesoMedio) &&
            Objects.equals(qualitaSonno, that.qualitaSonno) &&
            Objects.equals(mediaOreSonno, that.mediaOreSonno) &&
            Objects.equals(dataCreazione, that.dataCreazione) &&
            Objects.equals(dataScadenza, that.dataScadenza) &&
            Objects.equals(dataCompletamento, that.dataCompletamento) &&
            Objects.equals(puntuale, that.puntuale) &&
            Objects.equals(schedaSettimanaleId, that.schedaSettimanaleId) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            voto,
            giorniDieta,
            pesoMedio,
            qualitaSonno,
            mediaOreSonno,
            dataCreazione,
            dataScadenza,
            dataCompletamento,
            puntuale,
            schedaSettimanaleId,
            clienteId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportSettimanaleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalVoto().map(f -> "voto=" + f + ", ").orElse("") +
            optionalGiorniDieta().map(f -> "giorniDieta=" + f + ", ").orElse("") +
            optionalPesoMedio().map(f -> "pesoMedio=" + f + ", ").orElse("") +
            optionalQualitaSonno().map(f -> "qualitaSonno=" + f + ", ").orElse("") +
            optionalMediaOreSonno().map(f -> "mediaOreSonno=" + f + ", ").orElse("") +
            optionalDataCreazione().map(f -> "dataCreazione=" + f + ", ").orElse("") +
            optionalDataScadenza().map(f -> "dataScadenza=" + f + ", ").orElse("") +
            optionalDataCompletamento().map(f -> "dataCompletamento=" + f + ", ").orElse("") +
            optionalPuntuale().map(f -> "puntuale=" + f + ", ").orElse("") +
            optionalSchedaSettimanaleId().map(f -> "schedaSettimanaleId=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
