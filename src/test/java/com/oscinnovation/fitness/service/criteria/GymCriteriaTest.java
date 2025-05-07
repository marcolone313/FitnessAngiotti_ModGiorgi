package com.oscinnovation.fitness.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class GymCriteriaTest {

    @Test
    void newGymCriteriaHasAllFiltersNullTest() {
        var gymCriteria = new GymCriteria();
        assertThat(gymCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void gymCriteriaFluentMethodsCreatesFiltersTest() {
        var gymCriteria = new GymCriteria();

        setAllFilters(gymCriteria);

        assertThat(gymCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void gymCriteriaCopyCreatesNullFilterTest() {
        var gymCriteria = new GymCriteria();
        var copy = gymCriteria.copy();

        assertThat(gymCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(gymCriteria)
        );
    }

    @Test
    void gymCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var gymCriteria = new GymCriteria();
        setAllFilters(gymCriteria);

        var copy = gymCriteria.copy();

        assertThat(gymCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(gymCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var gymCriteria = new GymCriteria();

        assertThat(gymCriteria).hasToString("GymCriteria{}");
    }

    private static void setAllFilters(GymCriteria gymCriteria) {
        gymCriteria.id();
        gymCriteria.sets();
        gymCriteria.reps();
        gymCriteria.recupero();
        gymCriteria.peso();
        gymCriteria.completato();
        gymCriteria.svolto();
        gymCriteria.allenamentoGiornalieroId();
        gymCriteria.esercizioId();
        gymCriteria.distinct();
    }

    private static Condition<GymCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSets()) &&
                condition.apply(criteria.getReps()) &&
                condition.apply(criteria.getRecupero()) &&
                condition.apply(criteria.getPeso()) &&
                condition.apply(criteria.getCompletato()) &&
                condition.apply(criteria.getSvolto()) &&
                condition.apply(criteria.getAllenamentoGiornalieroId()) &&
                condition.apply(criteria.getEsercizioId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<GymCriteria> copyFiltersAre(GymCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSets(), copy.getSets()) &&
                condition.apply(criteria.getReps(), copy.getReps()) &&
                condition.apply(criteria.getRecupero(), copy.getRecupero()) &&
                condition.apply(criteria.getPeso(), copy.getPeso()) &&
                condition.apply(criteria.getCompletato(), copy.getCompletato()) &&
                condition.apply(criteria.getSvolto(), copy.getSvolto()) &&
                condition.apply(criteria.getAllenamentoGiornalieroId(), copy.getAllenamentoGiornalieroId()) &&
                condition.apply(criteria.getEsercizioId(), copy.getEsercizioId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
