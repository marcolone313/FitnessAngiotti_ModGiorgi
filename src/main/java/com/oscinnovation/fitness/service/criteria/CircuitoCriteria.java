package com.oscinnovation.fitness.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.oscinnovation.fitness.domain.Circuito} entity. This class is used
 * in {@link com.oscinnovation.fitness.web.rest.CircuitoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /circuitos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CircuitoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DurationFilter tempoLimite;

    private DurationFilter tempoImpiegato;

    private StringFilter catenaRipetizioni;

    private IntegerFilter circuitiCompletati;

    private BooleanFilter svolto;

    private BooleanFilter completato;

    private LongFilter allenamentoGiornalieroId;

    private LongFilter circuitoToEsercizioId;

    private Boolean distinct;

    public CircuitoCriteria() {}

    public CircuitoCriteria(CircuitoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tempoLimite = other.optionalTempoLimite().map(DurationFilter::copy).orElse(null);
        this.tempoImpiegato = other.optionalTempoImpiegato().map(DurationFilter::copy).orElse(null);
        this.catenaRipetizioni = other.optionalCatenaRipetizioni().map(StringFilter::copy).orElse(null);
        this.circuitiCompletati = other.optionalCircuitiCompletati().map(IntegerFilter::copy).orElse(null);
        this.svolto = other.optionalSvolto().map(BooleanFilter::copy).orElse(null);
        this.completato = other.optionalCompletato().map(BooleanFilter::copy).orElse(null);
        this.allenamentoGiornalieroId = other.optionalAllenamentoGiornalieroId().map(LongFilter::copy).orElse(null);
        this.circuitoToEsercizioId = other.optionalCircuitoToEsercizioId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CircuitoCriteria copy() {
        return new CircuitoCriteria(this);
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

    public DurationFilter getTempoLimite() {
        return tempoLimite;
    }

    public Optional<DurationFilter> optionalTempoLimite() {
        return Optional.ofNullable(tempoLimite);
    }

    public DurationFilter tempoLimite() {
        if (tempoLimite == null) {
            setTempoLimite(new DurationFilter());
        }
        return tempoLimite;
    }

    public void setTempoLimite(DurationFilter tempoLimite) {
        this.tempoLimite = tempoLimite;
    }

    public DurationFilter getTempoImpiegato() {
        return tempoImpiegato;
    }

    public Optional<DurationFilter> optionalTempoImpiegato() {
        return Optional.ofNullable(tempoImpiegato);
    }

    public DurationFilter tempoImpiegato() {
        if (tempoImpiegato == null) {
            setTempoImpiegato(new DurationFilter());
        }
        return tempoImpiegato;
    }

    public void setTempoImpiegato(DurationFilter tempoImpiegato) {
        this.tempoImpiegato = tempoImpiegato;
    }

    public StringFilter getCatenaRipetizioni() {
        return catenaRipetizioni;
    }

    public Optional<StringFilter> optionalCatenaRipetizioni() {
        return Optional.ofNullable(catenaRipetizioni);
    }

    public StringFilter catenaRipetizioni() {
        if (catenaRipetizioni == null) {
            setCatenaRipetizioni(new StringFilter());
        }
        return catenaRipetizioni;
    }

    public void setCatenaRipetizioni(StringFilter catenaRipetizioni) {
        this.catenaRipetizioni = catenaRipetizioni;
    }

    public IntegerFilter getCircuitiCompletati() {
        return circuitiCompletati;
    }

    public Optional<IntegerFilter> optionalCircuitiCompletati() {
        return Optional.ofNullable(circuitiCompletati);
    }

    public IntegerFilter circuitiCompletati() {
        if (circuitiCompletati == null) {
            setCircuitiCompletati(new IntegerFilter());
        }
        return circuitiCompletati;
    }

    public void setCircuitiCompletati(IntegerFilter circuitiCompletati) {
        this.circuitiCompletati = circuitiCompletati;
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

    public LongFilter getAllenamentoGiornalieroId() {
        return allenamentoGiornalieroId;
    }

    public Optional<LongFilter> optionalAllenamentoGiornalieroId() {
        return Optional.ofNullable(allenamentoGiornalieroId);
    }

    public LongFilter allenamentoGiornalieroId() {
        if (allenamentoGiornalieroId == null) {
            setAllenamentoGiornalieroId(new LongFilter());
        }
        return allenamentoGiornalieroId;
    }

    public void setAllenamentoGiornalieroId(LongFilter allenamentoGiornalieroId) {
        this.allenamentoGiornalieroId = allenamentoGiornalieroId;
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
        final CircuitoCriteria that = (CircuitoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tempoLimite, that.tempoLimite) &&
            Objects.equals(tempoImpiegato, that.tempoImpiegato) &&
            Objects.equals(catenaRipetizioni, that.catenaRipetizioni) &&
            Objects.equals(circuitiCompletati, that.circuitiCompletati) &&
            Objects.equals(svolto, that.svolto) &&
            Objects.equals(completato, that.completato) &&
            Objects.equals(allenamentoGiornalieroId, that.allenamentoGiornalieroId) &&
            Objects.equals(circuitoToEsercizioId, that.circuitoToEsercizioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tempoLimite,
            tempoImpiegato,
            catenaRipetizioni,
            circuitiCompletati,
            svolto,
            completato,
            allenamentoGiornalieroId,
            circuitoToEsercizioId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CircuitoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTempoLimite().map(f -> "tempoLimite=" + f + ", ").orElse("") +
            optionalTempoImpiegato().map(f -> "tempoImpiegato=" + f + ", ").orElse("") +
            optionalCatenaRipetizioni().map(f -> "catenaRipetizioni=" + f + ", ").orElse("") +
            optionalCircuitiCompletati().map(f -> "circuitiCompletati=" + f + ", ").orElse("") +
            optionalSvolto().map(f -> "svolto=" + f + ", ").orElse("") +
            optionalCompletato().map(f -> "completato=" + f + ", ").orElse("") +
            optionalAllenamentoGiornalieroId().map(f -> "allenamentoGiornalieroId=" + f + ", ").orElse("") +
            optionalCircuitoToEsercizioId().map(f -> "circuitoToEsercizioId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
