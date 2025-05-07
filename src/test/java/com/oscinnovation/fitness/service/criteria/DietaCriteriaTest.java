package com.oscinnovation.fitness.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DietaCriteriaTest {

    @Test
    void newDietaCriteriaHasAllFiltersNullTest() {
        var dietaCriteria = new DietaCriteria();
        assertThat(dietaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void dietaCriteriaFluentMethodsCreatesFiltersTest() {
        var dietaCriteria = new DietaCriteria();

        setAllFilters(dietaCriteria);

        assertThat(dietaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void dietaCriteriaCopyCreatesNullFilterTest() {
        var dietaCriteria = new DietaCriteria();
        var copy = dietaCriteria.copy();

        assertThat(dietaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(dietaCriteria)
        );
    }

    @Test
    void dietaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var dietaCriteria = new DietaCriteria();
        setAllFilters(dietaCriteria);

        var copy = dietaCriteria.copy();

        assertThat(dietaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(dietaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var dietaCriteria = new DietaCriteria();

        assertThat(dietaCriteria).hasToString("DietaCriteria{}");
    }

    private static void setAllFilters(DietaCriteria dietaCriteria) {
        dietaCriteria.id();
        dietaCriteria.dataCreazione();
        dietaCriteria.mese();
        dietaCriteria.tipo();
        dietaCriteria.macros();
        dietaCriteria.fabbisognoCalorico();
        dietaCriteria.clienteId();
        dietaCriteria.distinct();
    }

    private static Condition<DietaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDataCreazione()) &&
                condition.apply(criteria.getMese()) &&
                condition.apply(criteria.getTipo()) &&
                condition.apply(criteria.getMacros()) &&
                condition.apply(criteria.getFabbisognoCalorico()) &&
                condition.apply(criteria.getClienteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DietaCriteria> copyFiltersAre(DietaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDataCreazione(), copy.getDataCreazione()) &&
                condition.apply(criteria.getMese(), copy.getMese()) &&
                condition.apply(criteria.getTipo(), copy.getTipo()) &&
                condition.apply(criteria.getMacros(), copy.getMacros()) &&
                condition.apply(criteria.getFabbisognoCalorico(), copy.getFabbisognoCalorico()) &&
                condition.apply(criteria.getClienteId(), copy.getClienteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
