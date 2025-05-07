package com.oscinnovation.fitness.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CircuitoCriteriaTest {

    @Test
    void newCircuitoCriteriaHasAllFiltersNullTest() {
        var circuitoCriteria = new CircuitoCriteria();
        assertThat(circuitoCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void circuitoCriteriaFluentMethodsCreatesFiltersTest() {
        var circuitoCriteria = new CircuitoCriteria();

        setAllFilters(circuitoCriteria);

        assertThat(circuitoCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void circuitoCriteriaCopyCreatesNullFilterTest() {
        var circuitoCriteria = new CircuitoCriteria();
        var copy = circuitoCriteria.copy();

        assertThat(circuitoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(circuitoCriteria)
        );
    }

    @Test
    void circuitoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var circuitoCriteria = new CircuitoCriteria();
        setAllFilters(circuitoCriteria);

        var copy = circuitoCriteria.copy();

        assertThat(circuitoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(circuitoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var circuitoCriteria = new CircuitoCriteria();

        assertThat(circuitoCriteria).hasToString("CircuitoCriteria{}");
    }

    private static void setAllFilters(CircuitoCriteria circuitoCriteria) {
        circuitoCriteria.id();
        circuitoCriteria.tempoLimite();
        circuitoCriteria.tempoImpiegato();
        circuitoCriteria.catenaRipetizioni();
        circuitoCriteria.circuitiCompletati();
        circuitoCriteria.svolto();
        circuitoCriteria.completato();
        circuitoCriteria.allenamentoGiornalieroId();
        circuitoCriteria.circuitoToEsercizioId();
        circuitoCriteria.distinct();
    }

    private static Condition<CircuitoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTempoLimite()) &&
                condition.apply(criteria.getTempoImpiegato()) &&
                condition.apply(criteria.getCatenaRipetizioni()) &&
                condition.apply(criteria.getCircuitiCompletati()) &&
                condition.apply(criteria.getSvolto()) &&
                condition.apply(criteria.getCompletato()) &&
                condition.apply(criteria.getAllenamentoGiornalieroId()) &&
                condition.apply(criteria.getCircuitoToEsercizioId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CircuitoCriteria> copyFiltersAre(CircuitoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTempoLimite(), copy.getTempoLimite()) &&
                condition.apply(criteria.getTempoImpiegato(), copy.getTempoImpiegato()) &&
                condition.apply(criteria.getCatenaRipetizioni(), copy.getCatenaRipetizioni()) &&
                condition.apply(criteria.getCircuitiCompletati(), copy.getCircuitiCompletati()) &&
                condition.apply(criteria.getSvolto(), copy.getSvolto()) &&
                condition.apply(criteria.getCompletato(), copy.getCompletato()) &&
                condition.apply(criteria.getAllenamentoGiornalieroId(), copy.getAllenamentoGiornalieroId()) &&
                condition.apply(criteria.getCircuitoToEsercizioId(), copy.getCircuitoToEsercizioId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
