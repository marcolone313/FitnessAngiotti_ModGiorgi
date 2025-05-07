package com.oscinnovation.fitness.service.criteria;

import com.oscinnovation.fitness.domain.enumeration.LivelloCorso;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.oscinnovation.fitness.domain.CorsoAcademy} entity. This class is used
 * in {@link com.oscinnovation.fitness.web.rest.CorsoAcademyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /corso-academies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CorsoAcademyCriteria implements Serializable, Criteria {

    /**
     * Class for filtering LivelloCorso
     */
    public static class LivelloCorsoFilter extends Filter<LivelloCorso> {

        public LivelloCorsoFilter() {}

        public LivelloCorsoFilter(LivelloCorsoFilter filter) {
            super(filter);
        }

        @Override
        public LivelloCorsoFilter copy() {
            return new LivelloCorsoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LivelloCorsoFilter livello;

    private StringFilter titolo;

    private LongFilter lezioneCorsoId;

    private Boolean distinct;

    public CorsoAcademyCriteria() {}

    public CorsoAcademyCriteria(CorsoAcademyCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.livello = other.optionalLivello().map(LivelloCorsoFilter::copy).orElse(null);
        this.titolo = other.optionalTitolo().map(StringFilter::copy).orElse(null);
        this.lezioneCorsoId = other.optionalLezioneCorsoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CorsoAcademyCriteria copy() {
        return new CorsoAcademyCriteria(this);
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

    public LivelloCorsoFilter getLivello() {
        return livello;
    }

    public Optional<LivelloCorsoFilter> optionalLivello() {
        return Optional.ofNullable(livello);
    }

    public LivelloCorsoFilter livello() {
        if (livello == null) {
            setLivello(new LivelloCorsoFilter());
        }
        return livello;
    }

    public void setLivello(LivelloCorsoFilter livello) {
        this.livello = livello;
    }

    public StringFilter getTitolo() {
        return titolo;
    }

    public Optional<StringFilter> optionalTitolo() {
        return Optional.ofNullable(titolo);
    }

    public StringFilter titolo() {
        if (titolo == null) {
            setTitolo(new StringFilter());
        }
        return titolo;
    }

    public void setTitolo(StringFilter titolo) {
        this.titolo = titolo;
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
        final CorsoAcademyCriteria that = (CorsoAcademyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(livello, that.livello) &&
            Objects.equals(titolo, that.titolo) &&
            Objects.equals(lezioneCorsoId, that.lezioneCorsoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, livello, titolo, lezioneCorsoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CorsoAcademyCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLivello().map(f -> "livello=" + f + ", ").orElse("") +
            optionalTitolo().map(f -> "titolo=" + f + ", ").orElse("") +
            optionalLezioneCorsoId().map(f -> "lezioneCorsoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
