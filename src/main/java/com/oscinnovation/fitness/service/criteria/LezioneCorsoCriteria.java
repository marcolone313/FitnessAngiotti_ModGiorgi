package com.oscinnovation.fitness.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.oscinnovation.fitness.domain.LezioneCorso} entity. This class is used
 * in {@link com.oscinnovation.fitness.web.rest.LezioneCorsoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lezione-corsos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LezioneCorsoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titolo;

    private StringFilter videoUrl;

    private LongFilter corsoAcademyId;

    private Boolean distinct;

    public LezioneCorsoCriteria() {}

    public LezioneCorsoCriteria(LezioneCorsoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.titolo = other.optionalTitolo().map(StringFilter::copy).orElse(null);
        this.videoUrl = other.optionalVideoUrl().map(StringFilter::copy).orElse(null);
        this.corsoAcademyId = other.optionalCorsoAcademyId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public LezioneCorsoCriteria copy() {
        return new LezioneCorsoCriteria(this);
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

    public LongFilter getCorsoAcademyId() {
        return corsoAcademyId;
    }

    public Optional<LongFilter> optionalCorsoAcademyId() {
        return Optional.ofNullable(corsoAcademyId);
    }

    public LongFilter corsoAcademyId() {
        if (corsoAcademyId == null) {
            setCorsoAcademyId(new LongFilter());
        }
        return corsoAcademyId;
    }

    public void setCorsoAcademyId(LongFilter corsoAcademyId) {
        this.corsoAcademyId = corsoAcademyId;
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
        final LezioneCorsoCriteria that = (LezioneCorsoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titolo, that.titolo) &&
            Objects.equals(videoUrl, that.videoUrl) &&
            Objects.equals(corsoAcademyId, that.corsoAcademyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titolo, videoUrl, corsoAcademyId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LezioneCorsoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitolo().map(f -> "titolo=" + f + ", ").orElse("") +
            optionalVideoUrl().map(f -> "videoUrl=" + f + ", ").orElse("") +
            optionalCorsoAcademyId().map(f -> "corsoAcademyId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
