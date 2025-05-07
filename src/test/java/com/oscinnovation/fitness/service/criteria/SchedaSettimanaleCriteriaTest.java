package com.oscinnovation.fitness.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SchedaSettimanaleCriteriaTest {

    @Test
    void newSchedaSettimanaleCriteriaHasAllFiltersNullTest() {
        var schedaSettimanaleCriteria = new SchedaSettimanaleCriteria();
        assertThat(schedaSettimanaleCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void schedaSettimanaleCriteriaFluentMethodsCreatesFiltersTest() {
        var schedaSettimanaleCriteria = new SchedaSettimanaleCriteria();

        setAllFilters(schedaSettimanaleCriteria);

        assertThat(schedaSettimanaleCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void schedaSettimanaleCriteriaCopyCreatesNullFilterTest() {
        var schedaSettimanaleCriteria = new SchedaSettimanaleCriteria();
        var copy = schedaSettimanaleCriteria.copy();

        assertThat(schedaSettimanaleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(schedaSettimanaleCriteria)
        );
    }

    @Test
    void schedaSettimanaleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var schedaSettimanaleCriteria = new SchedaSettimanaleCriteria();
        setAllFilters(schedaSettimanaleCriteria);

        var copy = schedaSettimanaleCriteria.copy();

        assertThat(schedaSettimanaleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(schedaSettimanaleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var schedaSettimanaleCriteria = new SchedaSettimanaleCriteria();

        assertThat(schedaSettimanaleCriteria).hasToString("SchedaSettimanaleCriteria{}");
    }

    private static void setAllFilters(SchedaSettimanaleCriteria schedaSettimanaleCriteria) {
        schedaSettimanaleCriteria.id();
        schedaSettimanaleCriteria.anno();
        schedaSettimanaleCriteria.mese();
        schedaSettimanaleCriteria.settimana();
        schedaSettimanaleCriteria.dataCreazione();
        schedaSettimanaleCriteria.clienteId();
        schedaSettimanaleCriteria.reportSettimanaleId();
        schedaSettimanaleCriteria.distinct();
    }

    private static Condition<SchedaSettimanaleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAnno()) &&
                condition.apply(criteria.getMese()) &&
                condition.apply(criteria.getSettimana()) &&
                condition.apply(criteria.getDataCreazione()) &&
                condition.apply(criteria.getClienteId()) &&
                condition.apply(criteria.getReportSettimanaleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SchedaSettimanaleCriteria> copyFiltersAre(
        SchedaSettimanaleCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAnno(), copy.getAnno()) &&
                condition.apply(criteria.getMese(), copy.getMese()) &&
                condition.apply(criteria.getSettimana(), copy.getSettimana()) &&
                condition.apply(criteria.getDataCreazione(), copy.getDataCreazione()) &&
                condition.apply(criteria.getClienteId(), copy.getClienteId()) &&
                condition.apply(criteria.getReportSettimanaleId(), copy.getReportSettimanaleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
