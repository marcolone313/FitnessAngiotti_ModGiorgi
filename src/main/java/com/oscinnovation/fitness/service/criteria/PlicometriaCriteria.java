package com.oscinnovation.fitness.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.oscinnovation.fitness.domain.Plicometria} entity. This class is used
 * in {@link com.oscinnovation.fitness.web.rest.PlicometriaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /plicometrias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlicometriaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter tricipite;

    private FloatFilter petto;

    private FloatFilter ascella;

    private FloatFilter sottoscapolare;

    private FloatFilter soprailliaca;

    private FloatFilter addome;

    private FloatFilter coscia;

    private IntegerFilter mese;

    private LocalDateFilter dataInserimento;

    private LongFilter clienteId;

    private Boolean distinct;

    public PlicometriaCriteria() {}

    public PlicometriaCriteria(PlicometriaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tricipite = other.optionalTricipite().map(FloatFilter::copy).orElse(null);
        this.petto = other.optionalPetto().map(FloatFilter::copy).orElse(null);
        this.ascella = other.optionalAscella().map(FloatFilter::copy).orElse(null);
        this.sottoscapolare = other.optionalSottoscapolare().map(FloatFilter::copy).orElse(null);
        this.soprailliaca = other.optionalSoprailliaca().map(FloatFilter::copy).orElse(null);
        this.addome = other.optionalAddome().map(FloatFilter::copy).orElse(null);
        this.coscia = other.optionalCoscia().map(FloatFilter::copy).orElse(null);
        this.mese = other.optionalMese().map(IntegerFilter::copy).orElse(null);
        this.dataInserimento = other.optionalDataInserimento().map(LocalDateFilter::copy).orElse(null);
        this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PlicometriaCriteria copy() {
        return new PlicometriaCriteria(this);
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

    public FloatFilter getTricipite() {
        return tricipite;
    }

    public Optional<FloatFilter> optionalTricipite() {
        return Optional.ofNullable(tricipite);
    }

    public FloatFilter tricipite() {
        if (tricipite == null) {
            setTricipite(new FloatFilter());
        }
        return tricipite;
    }

    public void setTricipite(FloatFilter tricipite) {
        this.tricipite = tricipite;
    }

    public FloatFilter getPetto() {
        return petto;
    }

    public Optional<FloatFilter> optionalPetto() {
        return Optional.ofNullable(petto);
    }

    public FloatFilter petto() {
        if (petto == null) {
            setPetto(new FloatFilter());
        }
        return petto;
    }

    public void setPetto(FloatFilter petto) {
        this.petto = petto;
    }

    public FloatFilter getAscella() {
        return ascella;
    }

    public Optional<FloatFilter> optionalAscella() {
        return Optional.ofNullable(ascella);
    }

    public FloatFilter ascella() {
        if (ascella == null) {
            setAscella(new FloatFilter());
        }
        return ascella;
    }

    public void setAscella(FloatFilter ascella) {
        this.ascella = ascella;
    }

    public FloatFilter getSottoscapolare() {
        return sottoscapolare;
    }

    public Optional<FloatFilter> optionalSottoscapolare() {
        return Optional.ofNullable(sottoscapolare);
    }

    public FloatFilter sottoscapolare() {
        if (sottoscapolare == null) {
            setSottoscapolare(new FloatFilter());
        }
        return sottoscapolare;
    }

    public void setSottoscapolare(FloatFilter sottoscapolare) {
        this.sottoscapolare = sottoscapolare;
    }

    public FloatFilter getSoprailliaca() {
        return soprailliaca;
    }

    public Optional<FloatFilter> optionalSoprailliaca() {
        return Optional.ofNullable(soprailliaca);
    }

    public FloatFilter soprailliaca() {
        if (soprailliaca == null) {
            setSoprailliaca(new FloatFilter());
        }
        return soprailliaca;
    }

    public void setSoprailliaca(FloatFilter soprailliaca) {
        this.soprailliaca = soprailliaca;
    }

    public FloatFilter getAddome() {
        return addome;
    }

    public Optional<FloatFilter> optionalAddome() {
        return Optional.ofNullable(addome);
    }

    public FloatFilter addome() {
        if (addome == null) {
            setAddome(new FloatFilter());
        }
        return addome;
    }

    public void setAddome(FloatFilter addome) {
        this.addome = addome;
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
        final PlicometriaCriteria that = (PlicometriaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tricipite, that.tricipite) &&
            Objects.equals(petto, that.petto) &&
            Objects.equals(ascella, that.ascella) &&
            Objects.equals(sottoscapolare, that.sottoscapolare) &&
            Objects.equals(soprailliaca, that.soprailliaca) &&
            Objects.equals(addome, that.addome) &&
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
            tricipite,
            petto,
            ascella,
            sottoscapolare,
            soprailliaca,
            addome,
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
        return "PlicometriaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTricipite().map(f -> "tricipite=" + f + ", ").orElse("") +
            optionalPetto().map(f -> "petto=" + f + ", ").orElse("") +
            optionalAscella().map(f -> "ascella=" + f + ", ").orElse("") +
            optionalSottoscapolare().map(f -> "sottoscapolare=" + f + ", ").orElse("") +
            optionalSoprailliaca().map(f -> "soprailliaca=" + f + ", ").orElse("") +
            optionalAddome().map(f -> "addome=" + f + ", ").orElse("") +
            optionalCoscia().map(f -> "coscia=" + f + ", ").orElse("") +
            optionalMese().map(f -> "mese=" + f + ", ").orElse("") +
            optionalDataInserimento().map(f -> "dataInserimento=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
