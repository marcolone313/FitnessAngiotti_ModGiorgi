package com.oscinnovation.fitness.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CircuitoToEsercizioCriteriaTest {

    @Test
    void newCircuitoToEsercizioCriteriaHasAllFiltersNullTest() {
        var circuitoToEsercizioCriteria = new CircuitoToEsercizioCriteria();
        assertThat(circuitoToEsercizioCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void circuitoToEsercizioCriteriaFluentMethodsCreatesFiltersTest() {
        var circuitoToEsercizioCriteria = new CircuitoToEsercizioCriteria();

        setAllFilters(circuitoToEsercizioCriteria);

        assertThat(circuitoToEsercizioCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void circuitoToEsercizioCriteriaCopyCreatesNullFilterTest() {
        var circuitoToEsercizioCriteria = new CircuitoToEsercizioCriteria();
        var copy = circuitoToEsercizioCriteria.copy();

        assertThat(circuitoToEsercizioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(circuitoToEsercizioCriteria)
        );
    }

    @Test
    void circuitoToEsercizioCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var circuitoToEsercizioCriteria = new CircuitoToEsercizioCriteria();
        setAllFilters(circuitoToEsercizioCriteria);

        var copy = circuitoToEsercizioCriteria.copy();

        assertThat(circuitoToEsercizioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(circuitoToEsercizioCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var circuitoToEsercizioCriteria = new CircuitoToEsercizioCriteria();

        assertThat(circuitoToEsercizioCriteria).hasToString("CircuitoToEsercizioCriteria{}");
    }

    private static void setAllFilters(CircuitoToEsercizioCriteria circuitoToEsercizioCriteria) {
        circuitoToEsercizioCriteria.id();
        circuitoToEsercizioCriteria.reps();
        circuitoToEsercizioCriteria.esercizioId();
        circuitoToEsercizioCriteria.circuitoId();
        circuitoToEsercizioCriteria.distinct();
    }

    private static Condition<CircuitoToEsercizioCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getReps()) &&
                condition.apply(criteria.getEsercizioId()) &&
                condition.apply(criteria.getCircuitoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CircuitoToEsercizioCriteria> copyFiltersAre(
        CircuitoToEsercizioCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getReps(), copy.getReps()) &&
                condition.apply(criteria.getEsercizioId(), copy.getEsercizioId()) &&
                condition.apply(criteria.getCircuitoId(), copy.getCircuitoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
