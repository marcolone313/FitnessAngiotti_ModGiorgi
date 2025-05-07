package com.oscinnovation.fitness.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PesoClienteCriteriaTest {

    @Test
    void newPesoClienteCriteriaHasAllFiltersNullTest() {
        var pesoClienteCriteria = new PesoClienteCriteria();
        assertThat(pesoClienteCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void pesoClienteCriteriaFluentMethodsCreatesFiltersTest() {
        var pesoClienteCriteria = new PesoClienteCriteria();

        setAllFilters(pesoClienteCriteria);

        assertThat(pesoClienteCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void pesoClienteCriteriaCopyCreatesNullFilterTest() {
        var pesoClienteCriteria = new PesoClienteCriteria();
        var copy = pesoClienteCriteria.copy();

        assertThat(pesoClienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(pesoClienteCriteria)
        );
    }

    @Test
    void pesoClienteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pesoClienteCriteria = new PesoClienteCriteria();
        setAllFilters(pesoClienteCriteria);

        var copy = pesoClienteCriteria.copy();

        assertThat(pesoClienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(pesoClienteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pesoClienteCriteria = new PesoClienteCriteria();

        assertThat(pesoClienteCriteria).hasToString("PesoClienteCriteria{}");
    }

    private static void setAllFilters(PesoClienteCriteria pesoClienteCriteria) {
        pesoClienteCriteria.id();
        pesoClienteCriteria.peso();
        pesoClienteCriteria.mese();
        pesoClienteCriteria.dataInserimento();
        pesoClienteCriteria.clienteId();
        pesoClienteCriteria.distinct();
    }

    private static Condition<PesoClienteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPeso()) &&
                condition.apply(criteria.getMese()) &&
                condition.apply(criteria.getDataInserimento()) &&
                condition.apply(criteria.getClienteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PesoClienteCriteria> copyFiltersAre(PesoClienteCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPeso(), copy.getPeso()) &&
                condition.apply(criteria.getMese(), copy.getMese()) &&
                condition.apply(criteria.getDataInserimento(), copy.getDataInserimento()) &&
                condition.apply(criteria.getClienteId(), copy.getClienteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
