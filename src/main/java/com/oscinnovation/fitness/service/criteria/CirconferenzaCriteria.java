package com.oscinnovation.fitness.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.oscinnovation.fitness.domain.Circonferenza} entity. This class is used
 * in {@link com.oscinnovation.fitness.web.rest.CirconferenzaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /circonferenzas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CirconferenzaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter torace;

    private FloatFilter braccio;

    private FloatFilter avambraccio;

    private FloatFilter ombelico;

    private FloatFilter fianchi;

    private FloatFilter sottoOmbelico;

    private FloatFilter vita;

    private FloatFilter coscia;

    private IntegerFilter mese;

    private LocalDateFilter dataInserimento;

    private LongFilter clienteId;

    private Boolean distinct;

    public CirconferenzaCriteria() {}

    public CirconferenzaCriteria(CirconferenzaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.torace = other.optionalTorace().map(FloatFilter::copy).orElse(null);
        this.braccio = other.optionalBraccio().map(FloatFilter::copy).orElse(null);
        this.avambraccio = other.optionalAvambraccio().map(FloatFilter::copy).orElse(null);
        this.ombelico = other.optionalOmbelico().map(FloatFilter::copy).orElse(null);
        this.fianchi = other.optionalFianchi().map(FloatFilter::copy).orElse(null);
        this.sottoOmbelico = other.optionalSottoOmbelico().map(FloatFilter::copy).orElse(null);
        this.vita = other.optionalVita().map(FloatFilter::copy).orElse(null);
        this.coscia = other.optionalCoscia().map(FloatFilter::copy).orElse(null);
        this.mese = other.optionalMese().map(IntegerFilter::copy).orElse(null);
        this.dataInserimento = other.optionalDataInserimento().map(LocalDateFilter::copy).orElse(null);
        this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CirconferenzaCriteria copy() {
        return new CirconferenzaCriteria(this);
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

    public FloatFilter getTorace() {
        return torace;
    }

    public Optional<FloatFilter> optionalTorace() {
        return Optional.ofNullable(torace);
    }

    public FloatFilter torace() {
        if (torace == null) {
            setTorace(new FloatFilter());
        }
        return torace;
    }

    public void setTorace(FloatFilter torace) {
        this.torace = torace;
    }

    public FloatFilter getBraccio() {
        return braccio;
    }

    public Optional<FloatFilter> optionalBraccio() {
        return Optional.ofNullable(braccio);
    }

    public FloatFilter braccio() {
        if (braccio == null) {
            setBraccio(new FloatFilter());
        }
        return braccio;
    }

    public void setBraccio(FloatFilter braccio) {
        this.braccio = braccio;
    }

    public FloatFilter getAvambraccio() {
        return avambraccio;
    }

    public Optional<FloatFilter> optionalAvambraccio() {
        return Optional.ofNullable(avambraccio);
    }

    public FloatFilter avambraccio() {
        if (avambraccio == null) {
            setAvambraccio(new FloatFilter());
        }
        return avambraccio;
    }

    public void setAvambraccio(FloatFilter avambraccio) {
        this.avambraccio = avambraccio;
    }

    public FloatFilter getOmbelico() {
        return ombelico;
    }

    public Optional<FloatFilter> optionalOmbelico() {
        return Optional.ofNullable(ombelico);
    }

    public FloatFilter ombelico() {
        if (ombelico == null) {
            setOmbelico(new FloatFilter());
        }
        return ombelico;
    }

    public void setOmbelico(FloatFilter ombelico) {
        this.ombelico = ombelico;
    }

    public FloatFilter getFianchi() {
        return fianchi;
    }

    public Optional<FloatFilter> optionalFianchi() {
        return Optional.ofNullable(fianchi);
    }

    public FloatFilter fianchi() {
        if (fianchi == null) {
            setFianchi(new FloatFilter());
        }
        return fianchi;
    }

    public void setFianchi(FloatFilter fianchi) {
        this.fianchi = fianchi;
    }

    public FloatFilter getSottoOmbelico() {
        return sottoOmbelico;
    }

    public Optional<FloatFilter> optionalSottoOmbelico() {
        return Optional.ofNullable(sottoOmbelico);
    }

    public FloatFilter sottoOmbelico() {
        if (sottoOmbelico == null) {
            setSottoOmbelico(new FloatFilter());
        }
        return sottoOmbelico;
    }

    public void setSottoOmbelico(FloatFilter sottoOmbelico) {
        this.sottoOmbelico = sottoOmbelico;
    }

    public FloatFilter getVita() {
        return vita;
    }

    public Optional<FloatFilter> optionalVita() {
        return Optional.ofNullable(vita);
    }

    public FloatFilter vita() {
        if (vita == null) {
            setVita(new FloatFilter());
        }
        return vita;
    }

    public void setVita(FloatFilter vita) {
        this.vita = vita;
    }

    public FloatFilter getCoscia() {
        return coscia;
    }

    public Optional<FloatFilter> optionalCoscia() {
        return Optional.ofNullable(coscia);
    }

    public FloatFilter coscia() {
        if (coscia == null) {
            setCoscia(new FloatFilter());
        }
        return coscia;
    }

    public void setCoscia(FloatFilter coscia) {
        this.coscia = coscia;
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
        final CirconferenzaCriteria that = (CirconferenzaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(torace, that.torace) &&
            Objects.equals(braccio, that.braccio) &&
            Objects.equals(avambraccio, that.avambraccio) &&
            Objects.equals(ombelico, that.ombelico) &&
            Objects.equals(fianchi, that.fianchi) &&
            Objects.equals(sottoOmbelico, that.sottoOmbelico) &&
            Objects.equals(vita, that.vita) &&
            Objects.equals(coscia, that.coscia) &&
            Objects.equals(mese, that.mese) &&
            Objects.equals(dataInserimento, that.dataInserimento) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            torace,
            braccio,
            avambraccio,
            ombelico,
            fianchi,
            sottoOmbelico,
            vita,
            coscia,
            mese,
            dataInserimento,
            clienteId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CirconferenzaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTorace().map(f -> "torace=" + f + ", ").orElse("") +
            optionalBraccio().map(f -> "braccio=" + f + ", ").orElse("") +
            optionalAvambraccio().map(f -> "avambraccio=" + f + ", ").orElse("") +
            optionalOmbelico().map(f -> "ombelico=" + f + ", ").orElse("") +
            optionalFianchi().map(f -> "fianchi=" + f + ", ").orElse("") +
            optionalSottoOmbelico().map(f -> "sottoOmbelico=" + f + ", ").orElse("") +
            optionalVita().map(f -> "vita=" + f + ", ").orElse("") +
            optionalCoscia().map(f -> "coscia=" + f + ", ").orElse("") +
            optionalMese().map(f -> "mese=" + f + ", ").orElse("") +
            optionalDataInserimento().map(f -> "dataInserimento=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
