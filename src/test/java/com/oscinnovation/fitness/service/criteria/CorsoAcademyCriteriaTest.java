package com.oscinnovation.fitness.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CorsoAcademyCriteriaTest {

    @Test
    void newCorsoAcademyCriteriaHasAllFiltersNullTest() {
        var corsoAcademyCriteria = new CorsoAcademyCriteria();
        assertThat(corsoAcademyCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void corsoAcademyCriteriaFluentMethodsCreatesFiltersTest() {
        var corsoAcademyCriteria = new CorsoAcademyCriteria();

        setAllFilters(corsoAcademyCriteria);

        assertThat(corsoAcademyCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void corsoAcademyCriteriaCopyCreatesNullFilterTest() {
        var corsoAcademyCriteria = new CorsoAcademyCriteria();
        var copy = corsoAcademyCriteria.copy();

        assertThat(corsoAcademyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(corsoAcademyCriteria)
        );
    }

    @Test
    void corsoAcademyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var corsoAcademyCriteria = new CorsoAcademyCriteria();
        setAllFilters(corsoAcademyCriteria);

        var copy = corsoAcademyCriteria.copy();

        assertThat(corsoAcademyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(corsoAcademyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var corsoAcademyCriteria = new CorsoAcademyCriteria();

        assertThat(corsoAcademyCriteria).hasToString("CorsoAcademyCriteria{}");
    }

    private static void setAllFilters(CorsoAcademyCriteria corsoAcademyCriteria) {
        corsoAcademyCriteria.id();
        corsoAcademyCriteria.livello();
        corsoAcademyCriteria.titolo();
        corsoAcademyCriteria.lezioneCorsoId();
        corsoAcademyCriteria.distinct();
    }

    private static Condition<CorsoAcademyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLivello()) &&
                condition.apply(criteria.getTitolo()) &&
                condition.apply(criteria.getLezioneCorsoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CorsoAcademyCriteria> copyFiltersAre(
        CorsoAcademyCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLivello(), copy.getLivello()) &&
                condition.apply(criteria.getTitolo(), copy.getTitolo()) &&
                condition.apply(criteria.getLezioneCorsoId(), copy.getLezioneCorsoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
