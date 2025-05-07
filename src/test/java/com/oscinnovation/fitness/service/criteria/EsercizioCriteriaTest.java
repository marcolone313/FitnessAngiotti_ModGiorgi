package com.oscinnovation.fitness.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EsercizioCriteriaTest {

    @Test
    void newEsercizioCriteriaHasAllFiltersNullTest() {
        var esercizioCriteria = new EsercizioCriteria();
        assertThat(esercizioCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void esercizioCriteriaFluentMethodsCreatesFiltersTest() {
        var esercizioCriteria = new EsercizioCriteria();

        setAllFilters(esercizioCriteria);

        assertThat(esercizioCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void esercizioCriteriaCopyCreatesNullFilterTest() {
        var esercizioCriteria = new EsercizioCriteria();
        var copy = esercizioCriteria.copy();

        assertThat(esercizioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(esercizioCriteria)
        );
    }

    @Test
    void esercizioCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var esercizioCriteria = new EsercizioCriteria();
        setAllFilters(esercizioCriteria);

        var copy = esercizioCriteria.copy();

        assertThat(esercizioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(esercizioCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var esercizioCriteria = new EsercizioCriteria();

        assertThat(esercizioCriteria).hasToString("EsercizioCriteria{}");
    }

    private static void setAllFilters(EsercizioCriteria esercizioCriteria) {
        esercizioCriteria.id();
        esercizioCriteria.nome();
        esercizioCriteria.tipo();
        esercizioCriteria.videoUrl();
        esercizioCriteria.gymId();
        esercizioCriteria.circuitoToEsercizioId();
        esercizioCriteria.passaggioEsercizioId();
        esercizioCriteria.distinct();
    }

    private static Condition<EsercizioCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNome()) &&
                condition.apply(criteria.getTipo()) &&
                condition.apply(criteria.getVideoUrl()) &&
                condition.apply(criteria.getGymId()) &&
                condition.apply(criteria.getCircuitoToEsercizioId()) &&
                condition.apply(criteria.getPassaggioEsercizioId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EsercizioCriteria> copyFiltersAre(EsercizioCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNome(), copy.getNome()) &&
                condition.apply(criteria.getTipo(), copy.getTipo()) &&
                condition.apply(criteria.getVideoUrl(), copy.getVideoUrl()) &&
                condition.apply(criteria.getGymId(), copy.getGymId()) &&
                condition.apply(criteria.getCircuitoToEsercizioId(), copy.getCircuitoToEsercizioId()) &&
                condition.apply(criteria.getPassaggioEsercizioId(), copy.getPassaggioEsercizioId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
