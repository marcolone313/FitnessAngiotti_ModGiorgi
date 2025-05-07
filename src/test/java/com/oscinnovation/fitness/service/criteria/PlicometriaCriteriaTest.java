package com.oscinnovation.fitness.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PlicometriaCriteriaTest {

    @Test
    void newPlicometriaCriteriaHasAllFiltersNullTest() {
        var plicometriaCriteria = new PlicometriaCriteria();
        assertThat(plicometriaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void plicometriaCriteriaFluentMethodsCreatesFiltersTest() {
        var plicometriaCriteria = new PlicometriaCriteria();

        setAllFilters(plicometriaCriteria);

        assertThat(plicometriaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void plicometriaCriteriaCopyCreatesNullFilterTest() {
        var plicometriaCriteria = new PlicometriaCriteria();
        var copy = plicometriaCriteria.copy();

        assertThat(plicometriaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(plicometriaCriteria)
        );
    }

    @Test
    void plicometriaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var plicometriaCriteria = new PlicometriaCriteria();
        setAllFilters(plicometriaCriteria);

        var copy = plicometriaCriteria.copy();

        assertThat(plicometriaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(plicometriaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var plicometriaCriteria = new PlicometriaCriteria();

        assertThat(plicometriaCriteria).hasToString("PlicometriaCriteria{}");
    }

    private static void setAllFilters(PlicometriaCriteria plicometriaCriteria) {
        plicometriaCriteria.id();
        plicometriaCriteria.tricipite();
        plicometriaCriteria.petto();
        plicometriaCriteria.ascella();
        plicometriaCriteria.sottoscapolare();
        plicometriaCriteria.soprailliaca();
        plicometriaCriteria.addome();
        plicometriaCriteria.coscia();
        plicometriaCriteria.mese();
        plicometriaCriteria.dataInserimento();
        plicometriaCriteria.clienteId();
        plicometriaCriteria.distinct();
    }

    private static Condition<PlicometriaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTricipite()) &&
                condition.apply(criteria.getPetto()) &&
                condition.apply(criteria.getAscella()) &&
                condition.apply(criteria.getSottoscapolare()) &&
                condition.apply(criteria.getSoprailliaca()) &&
                condition.apply(criteria.getAddome()) &&
                condition.apply(criteria.getCoscia()) &&
                condition.apply(criteria.getMese()) &&
                condition.apply(criteria.getDataInserimento()) &&
                condition.apply(criteria.getClienteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PlicometriaCriteria> copyFiltersAre(PlicometriaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTricipite(), copy.getTricipite()) &&
                condition.apply(criteria.getPetto(), copy.getPetto()) &&
                condition.apply(criteria.getAscella(), copy.getAscella()) &&
                condition.apply(criteria.getSottoscapolare(), copy.getSottoscapolare()) &&
                condition.apply(criteria.getSoprailliaca(), copy.getSoprailliaca()) &&
                condition.apply(criteria.getAddome(), copy.getAddome()) &&
                condition.apply(criteria.getCoscia(), copy.getCoscia()) &&
                condition.apply(criteria.getMese(), copy.getMese()) &&
                condition.apply(criteria.getDataInserimento(), copy.getDataInserimento()) &&
                condition.apply(criteria.getClienteId(), copy.getClienteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
