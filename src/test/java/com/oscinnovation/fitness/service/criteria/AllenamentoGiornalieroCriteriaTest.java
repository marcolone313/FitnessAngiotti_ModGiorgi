package com.oscinnovation.fitness.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AllenamentoGiornalieroCriteriaTest {

    @Test
    void newAllenamentoGiornalieroCriteriaHasAllFiltersNullTest() {
        var allenamentoGiornalieroCriteria = new AllenamentoGiornalieroCriteria();
        assertThat(allenamentoGiornalieroCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void allenamentoGiornalieroCriteriaFluentMethodsCreatesFiltersTest() {
        var allenamentoGiornalieroCriteria = new AllenamentoGiornalieroCriteria();

        setAllFilters(allenamentoGiornalieroCriteria);

        assertThat(allenamentoGiornalieroCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void allenamentoGiornalieroCriteriaCopyCreatesNullFilterTest() {
        var allenamentoGiornalieroCriteria = new AllenamentoGiornalieroCriteria();
        var copy = allenamentoGiornalieroCriteria.copy();

        assertThat(allenamentoGiornalieroCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(allenamentoGiornalieroCriteria)
        );
    }

    @Test
    void allenamentoGiornalieroCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var allenamentoGiornalieroCriteria = new AllenamentoGiornalieroCriteria();
        setAllFilters(allenamentoGiornalieroCriteria);

        var copy = allenamentoGiornalieroCriteria.copy();

        assertThat(allenamentoGiornalieroCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(allenamentoGiornalieroCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var allenamentoGiornalieroCriteria = new AllenamentoGiornalieroCriteria();

        assertThat(allenamentoGiornalieroCriteria).hasToString("AllenamentoGiornalieroCriteria{}");
    }

    private static void setAllFilters(AllenamentoGiornalieroCriteria allenamentoGiornalieroCriteria) {
        allenamentoGiornalieroCriteria.id();
        allenamentoGiornalieroCriteria.tipo();
        allenamentoGiornalieroCriteria.giorno();
        allenamentoGiornalieroCriteria.svolto();
        allenamentoGiornalieroCriteria.dataAllenamento();
        allenamentoGiornalieroCriteria.schedaSettimanaleId();
        allenamentoGiornalieroCriteria.circuitoId();
        allenamentoGiornalieroCriteria.corsaId();
        allenamentoGiornalieroCriteria.distinct();
    }

    private static Condition<AllenamentoGiornalieroCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTipo()) &&
                condition.apply(criteria.getGiorno()) &&
                condition.apply(criteria.getSvolto()) &&
                condition.apply(criteria.getDataAllenamento()) &&
                condition.apply(criteria.getSchedaSettimanaleId()) &&
                condition.apply(criteria.getCircuitoId()) &&
                condition.apply(criteria.getCorsaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AllenamentoGiornalieroCriteria> copyFiltersAre(
        AllenamentoGiornalieroCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTipo(), copy.getTipo()) &&
                condition.apply(criteria.getGiorno(), copy.getGiorno()) &&
                condition.apply(criteria.getSvolto(), copy.getSvolto()) &&
                condition.apply(criteria.getDataAllenamento(), copy.getDataAllenamento()) &&
                condition.apply(criteria.getSchedaSettimanaleId(), copy.getSchedaSettimanaleId()) &&
                condition.apply(criteria.getCircuitoId(), copy.getCircuitoId()) &&
                condition.apply(criteria.getCorsaId(), copy.getCorsaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
