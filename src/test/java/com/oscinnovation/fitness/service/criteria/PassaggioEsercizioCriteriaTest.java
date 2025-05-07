package com.oscinnovation.fitness.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PassaggioEsercizioCriteriaTest {

    @Test
    void newPassaggioEsercizioCriteriaHasAllFiltersNullTest() {
        var passaggioEsercizioCriteria = new PassaggioEsercizioCriteria();
        assertThat(passaggioEsercizioCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void passaggioEsercizioCriteriaFluentMethodsCreatesFiltersTest() {
        var passaggioEsercizioCriteria = new PassaggioEsercizioCriteria();

        setAllFilters(passaggioEsercizioCriteria);

        assertThat(passaggioEsercizioCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void passaggioEsercizioCriteriaCopyCreatesNullFilterTest() {
        var passaggioEsercizioCriteria = new PassaggioEsercizioCriteria();
        var copy = passaggioEsercizioCriteria.copy();

        assertThat(passaggioEsercizioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(passaggioEsercizioCriteria)
        );
    }

    @Test
    void passaggioEsercizioCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var passaggioEsercizioCriteria = new PassaggioEsercizioCriteria();
        setAllFilters(passaggioEsercizioCriteria);

        var copy = passaggioEsercizioCriteria.copy();

        assertThat(passaggioEsercizioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(passaggioEsercizioCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var passaggioEsercizioCriteria = new PassaggioEsercizioCriteria();

        assertThat(passaggioEsercizioCriteria).hasToString("PassaggioEsercizioCriteria{}");
    }

    private static void setAllFilters(PassaggioEsercizioCriteria passaggioEsercizioCriteria) {
        passaggioEsercizioCriteria.id();
        passaggioEsercizioCriteria.indice();
        passaggioEsercizioCriteria.esercizioId();
        passaggioEsercizioCriteria.distinct();
    }

    private static Condition<PassaggioEsercizioCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getIndice()) &&
                condition.apply(criteria.getEsercizioId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PassaggioEsercizioCriteria> copyFiltersAre(
        PassaggioEsercizioCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getIndice(), copy.getIndice()) &&
                condition.apply(criteria.getEsercizioId(), copy.getEsercizioId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
