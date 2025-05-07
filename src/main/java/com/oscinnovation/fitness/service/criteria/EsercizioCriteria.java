package com.oscinnovation.fitness.service.criteria;

import com.oscinnovation.fitness.domain.enumeration.TipoEsercizio;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.oscinnovation.fitness.domain.Esercizio} entity. This class is used
 * in {@link com.oscinnovation.fitness.web.rest.EsercizioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /esercizios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EsercizioCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoEsercizio
     */
    public static class TipoEsercizioFilter extends Filter<TipoEsercizio> {

        public TipoEsercizioFilter() {}

        public TipoEsercizioFilter(TipoEsercizioFilter filter) {
            super(filter);
        }

        @Override
        public TipoEsercizioFilter copy() {
            return new TipoEsercizioFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private TipoEsercizioFilter tipo;

    private StringFilter videoUrl;

    private LongFilter gymId;

    private LongFilter circuitoToEsercizioId;

    private LongFilter passaggioEsercizioId;

    private Boolean distinct;

    public EsercizioCriteria() {}

    public EsercizioCriteria(EsercizioCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nome = other.optionalNome().map(StringFilter::copy).orElse(null);
        this.tipo = other.optionalTipo().map(TipoEsercizioFilter::copy).orElse(null);
        this.videoUrl = other.optionalVideoUrl().map(StringFilter::copy).orElse(null);
        this.gymId = other.optionalGymId().map(LongFilter::copy).orElse(null);
        this.circuitoToEsercizioId = other.optionalCircuitoToEsercizioId().map(LongFilter::copy).orElse(null);
        this.passaggioEsercizioId = other.optionalPassaggioEsercizioId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EsercizioCriteria copy() {
        return new EsercizioCriteria(this);
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

    public StringFilter getNome() {
        return nome;
    }

    public Optional<StringFilter> optionalNome() {
        return Optional.ofNullable(nome);
    }

    public StringFilter nome() {
        if (nome == null) {
            setNome(new StringFilter());
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public TipoEsercizioFilter getTipo() {
        return tipo;
    }

    public Optional<TipoEsercizioFilter> optionalTipo() {
        return Optional.ofNullable(tipo);
    }

    public TipoEsercizioFilter tipo() {
        if (tipo == null) {
            setTipo(new TipoEsercizioFilter());
        }
        return tipo;
    }

    public void setTipo(TipoEsercizioFilter tipo) {
        this.tipo = tipo;
    }

    public StringFilter getVideoUrl() {
        return videoUrl;
    }

    public Optional<StringFilter> optionalVideoUrl() {
        return Optional.ofNullable(videoUrl);
    }

    public StringFilter videoUrl() {
        if (videoUrl == null) {
            setVideoUrl(new StringFilter());
        }
        return videoUrl;
    }

    public void setVideoUrl(StringFilter videoUrl) {
        this.videoUrl = videoUrl;
    }

    public LongFilter getGymId() {
        return gymId;
    }

    public Optional<LongFilter> optionalGymId() {
        return Optional.ofNullable(gymId);
    }

    public LongFilter gymId() {
        if (gymId == null) {
            setGymId(new LongFilter());
        }
        return gymId;
    }

    public void setGymId(LongFilter gymId) {
        this.gymId = gymId;
    }

    public LongFilter getCircuitoToEsercizioId() {
        return circuitoToEsercizioId;
    }

    public Optional<LongFilter> optionalCircuitoToEsercizioId() {
        return Optional.ofNullable(circuitoToEsercizioId);
    }

    public LongFilter circuitoToEsercizioId() {
        if (circuitoToEsercizioId == null) {
            setCircuitoToEsercizioId(new LongFilter());
        }
        return circuitoToEsercizioId;
    }

    public void setCircuitoToEsercizioId(LongFilter circuitoToEsercizioId) {
        this.circuitoToEsercizioId = circuitoToEsercizioId;
    }

    public LongFilter getPassaggioEsercizioId() {
        return passaggioEsercizioId;
    }

    public Optional<LongFilter> optionalPassaggioEsercizioId() {
        return Optional.ofNullable(passaggioEsercizioId);
    }

    public LongFilter passaggioEsercizioId() {
        if (passaggioEsercizioId == null) {
            setPassaggioEsercizioId(new LongFilter());
        }
        return passaggioEsercizioId;
    }

    public void setPassaggioEsercizioId(LongFilter passaggioEsercizioId) {
        this.passaggioEsercizioId = passaggioEsercizioId;
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
        final EsercizioCriteria that = (EsercizioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(videoUrl, that.videoUrl) &&
            Objects.equals(gymId, that.gymId) &&
            Objects.equals(circuitoToEsercizioId, that.circuitoToEsercizioId) &&
            Objects.equals(passaggioEsercizioId, that.passaggioEsercizioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, tipo, videoUrl, gymId, circuitoToEsercizioId, passaggioEsercizioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EsercizioCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNome().map(f -> "nome=" + f + ", ").orElse("") +
            optionalTipo().map(f -> "tipo=" + f + ", ").orElse("") +
            optionalVideoUrl().map(f -> "videoUrl=" + f + ", ").orElse("") +
            optionalGymId().map(f -> "gymId=" + f + ", ").orElse("") +
            optionalCircuitoToEsercizioId().map(f -> "circuitoToEsercizioId=" + f + ", ").orElse("") +
            optionalPassaggioEsercizioId().map(f -> "passaggioEsercizioId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
