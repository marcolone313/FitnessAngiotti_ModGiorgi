package com.oscinnovation.fitness.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CirconferenzaCriteriaTest {

    @Test
    void newCirconferenzaCriteriaHasAllFiltersNullTest() {
        var circonferenzaCriteria = new CirconferenzaCriteria();
        assertThat(circonferenzaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void circonferenzaCriteriaFluentMethodsCreatesFiltersTest() {
        var circonferenzaCriteria = new CirconferenzaCriteria();

        setAllFilters(circonferenzaCriteria);

        assertThat(circonferenzaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void circonferenzaCriteriaCopyCreatesNullFilterTest() {
        var circonferenzaCriteria = new CirconferenzaCriteria();
        var copy = circonferenzaCriteria.copy();

        assertThat(circonferenzaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(circonferenzaCriteria)
        );
    }

    @Test
    void circonferenzaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var circonferenzaCriteria = new CirconferenzaCriteria();
        setAllFilters(circonferenzaCriteria);

        var copy = circonferenzaCriteria.copy();

        assertThat(circonferenzaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(circonferenzaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var circonferenzaCriteria = new CirconferenzaCriteria();

        assertThat(circonferenzaCriteria).hasToString("CirconferenzaCriteria{}");
    }

    private static void setAllFilters(CirconferenzaCriteria circonferenzaCriteria) {
        circonferenzaCriteria.id();
        circonferenzaCriteria.torace();
        circonferenzaCriteria.braccio();
        circonferenzaCriteria.avambraccio();
        circonferenzaCriteria.ombelico();
        circonferenzaCriteria.fianchi();
        circonferenzaCriteria.sottoOmbelico();
        circonferenzaCriteria.vita();
        circonferenzaCriteria.coscia();
        circonferenzaCriteria.mese();
        circonferenzaCriteria.dataInserimento();
        circonferenzaCriteria.clienteId();
        circonferenzaCriteria.distinct();
    }

    private static Condition<CirconferenzaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTorace()) &&
                condition.apply(criteria.getBraccio()) &&
                condition.apply(criteria.getAvambraccio()) &&
                condition.apply(criteria.getOmbelico()) &&
                condition.apply(criteria.getFianchi()) &&
                condition.apply(criteria.getSottoOmbelico()) &&
                condition.apply(criteria.getVita()) &&
                condition.apply(criteria.getCoscia()) &&
                condition.apply(criteria.getMese()) &&
                condition.apply(criteria.getDataInserimento()) &&
                condition.apply(criteria.getClienteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CirconferenzaCriteria> copyFiltersAre(
        CirconferenzaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTorace(), copy.getTorace()) &&
                condition.apply(criteria.getBraccio(), copy.getBraccio()) &&
                condition.apply(criteria.getAvambraccio(), copy.getAvambraccio()) &&
                condition.apply(criteria.getOmbelico(), copy.getOmbelico()) &&
                condition.apply(criteria.getFianchi(), copy.getFianchi()) &&
                condition.apply(criteria.getSottoOmbelico(), copy.getSottoOmbelico()) &&
                condition.apply(criteria.getVita(), copy.getVita()) &&
                condition.apply(criteria.getCoscia(), copy.getCoscia()) &&
                condition.apply(criteria.getMese(), copy.getMese()) &&
                condition.apply(criteria.getDataInserimento(), copy.getDataInserimento()) &&
                condition.apply(criteria.getClienteId(), copy.getClienteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
