package com.oscinnovation.fitness.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CorsaCriteriaTest {

    @Test
    void newCorsaCriteriaHasAllFiltersNullTest() {
        var corsaCriteria = new CorsaCriteria();
        assertThat(corsaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void corsaCriteriaFluentMethodsCreatesFiltersTest() {
        var corsaCriteria = new CorsaCriteria();

        setAllFilters(corsaCriteria);

        assertThat(corsaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void corsaCriteriaCopyCreatesNullFilterTest() {
        var corsaCriteria = new CorsaCriteria();
        var copy = corsaCriteria.copy();

        assertThat(corsaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(corsaCriteria)
        );
    }

    @Test
    void corsaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var corsaCriteria = new CorsaCriteria();
        setAllFilters(corsaCriteria);

        var copy = corsaCriteria.copy();

        assertThat(corsaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(corsaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var corsaCriteria = new CorsaCriteria();

        assertThat(corsaCriteria).hasToString("CorsaCriteria{}");
    }

    private static void setAllFilters(CorsaCriteria corsaCriteria) {
        corsaCriteria.id();
        corsaCriteria.distanzaDaPercorrere();
        corsaCriteria.tempoImpiegato();
        corsaCriteria.svolto();
        corsaCriteria.completato();
        corsaCriteria.allenamentoGiornalieroId();
        corsaCriteria.distinct();
    }

    private static Condition<CorsaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDistanzaDaPercorrere()) &&
                condition.apply(criteria.getTempoImpiegato()) &&
                condition.apply(criteria.getSvolto()) &&
                condition.apply(criteria.getCompletato()) &&
                condition.apply(criteria.getAllenamentoGiornalieroId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CorsaCriteria> copyFiltersAre(CorsaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDistanzaDaPercorrere(), copy.getDistanzaDaPercorrere()) &&
                condition.apply(criteria.getTempoImpiegato(), copy.getTempoImpiegato()) &&
                condition.apply(criteria.getSvolto(), copy.getSvolto()) &&
                condition.apply(criteria.getCompletato(), copy.getCompletato()) &&
                condition.apply(criteria.getAllenamentoGiornalieroId(), copy.getAllenamentoGiornalieroId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
