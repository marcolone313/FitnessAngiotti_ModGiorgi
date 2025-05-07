package com.oscinnovation.fitness.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReportSettimanaleCriteriaTest {

    @Test
    void newReportSettimanaleCriteriaHasAllFiltersNullTest() {
        var reportSettimanaleCriteria = new ReportSettimanaleCriteria();
        assertThat(reportSettimanaleCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void reportSettimanaleCriteriaFluentMethodsCreatesFiltersTest() {
        var reportSettimanaleCriteria = new ReportSettimanaleCriteria();

        setAllFilters(reportSettimanaleCriteria);

        assertThat(reportSettimanaleCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void reportSettimanaleCriteriaCopyCreatesNullFilterTest() {
        var reportSettimanaleCriteria = new ReportSettimanaleCriteria();
        var copy = reportSettimanaleCriteria.copy();

        assertThat(reportSettimanaleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(reportSettimanaleCriteria)
        );
    }

    @Test
    void reportSettimanaleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reportSettimanaleCriteria = new ReportSettimanaleCriteria();
        setAllFilters(reportSettimanaleCriteria);

        var copy = reportSettimanaleCriteria.copy();

        assertThat(reportSettimanaleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(reportSettimanaleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reportSettimanaleCriteria = new ReportSettimanaleCriteria();

        assertThat(reportSettimanaleCriteria).hasToString("ReportSettimanaleCriteria{}");
    }

    private static void setAllFilters(ReportSettimanaleCriteria reportSettimanaleCriteria) {
        reportSettimanaleCriteria.id();
        reportSettimanaleCriteria.voto();
        reportSettimanaleCriteria.giorniDieta();
        reportSettimanaleCriteria.pesoMedio();
        reportSettimanaleCriteria.qualitaSonno();
        reportSettimanaleCriteria.mediaOreSonno();
        reportSettimanaleCriteria.dataCreazione();
        reportSettimanaleCriteria.dataScadenza();
        reportSettimanaleCriteria.dataCompletamento();
        reportSettimanaleCriteria.puntuale();
        reportSettimanaleCriteria.schedaSettimanaleId();
        reportSettimanaleCriteria.clienteId();
        reportSettimanaleCriteria.distinct();
    }

    private static Condition<ReportSettimanaleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getVoto()) &&
                condition.apply(criteria.getGiorniDieta()) &&
                condition.apply(criteria.getPesoMedio()) &&
                condition.apply(criteria.getQualitaSonno()) &&
                condition.apply(criteria.getMediaOreSonno()) &&
                condition.apply(criteria.getDataCreazione()) &&
                condition.apply(criteria.getDataScadenza()) &&
                condition.apply(criteria.getDataCompletamento()) &&
                condition.apply(criteria.getPuntuale()) &&
                condition.apply(criteria.getSchedaSettimanaleId()) &&
                condition.apply(criteria.getClienteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReportSettimanaleCriteria> copyFiltersAre(
        ReportSettimanaleCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getVoto(), copy.getVoto()) &&
                condition.apply(criteria.getGiorniDieta(), copy.getGiorniDieta()) &&
                condition.apply(criteria.getPesoMedio(), copy.getPesoMedio()) &&
                condition.apply(criteria.getQualitaSonno(), copy.getQualitaSonno()) &&
                condition.apply(criteria.getMediaOreSonno(), copy.getMediaOreSonno()) &&
                condition.apply(criteria.getDataCreazione(), copy.getDataCreazione()) &&
                condition.apply(criteria.getDataScadenza(), copy.getDataScadenza()) &&
                condition.apply(criteria.getDataCompletamento(), copy.getDataCompletamento()) &&
                condition.apply(criteria.getPuntuale(), copy.getPuntuale()) &&
                condition.apply(criteria.getSchedaSettimanaleId(), copy.getSchedaSettimanaleId()) &&
                condition.apply(criteria.getClienteId(), copy.getClienteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
