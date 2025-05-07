package com.oscinnovation.fitness.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LezioneCorsoCriteriaTest {

    @Test
    void newLezioneCorsoCriteriaHasAllFiltersNullTest() {
        var lezioneCorsoCriteria = new LezioneCorsoCriteria();
        assertThat(lezioneCorsoCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void lezioneCorsoCriteriaFluentMethodsCreatesFiltersTest() {
        var lezioneCorsoCriteria = new LezioneCorsoCriteria();

        setAllFilters(lezioneCorsoCriteria);

        assertThat(lezioneCorsoCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void lezioneCorsoCriteriaCopyCreatesNullFilterTest() {
        var lezioneCorsoCriteria = new LezioneCorsoCriteria();
        var copy = lezioneCorsoCriteria.copy();

        assertThat(lezioneCorsoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(lezioneCorsoCriteria)
        );
    }

    @Test
    void lezioneCorsoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var lezioneCorsoCriteria = new LezioneCorsoCriteria();
        setAllFilters(lezioneCorsoCriteria);

        var copy = lezioneCorsoCriteria.copy();

        assertThat(lezioneCorsoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(lezioneCorsoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var lezioneCorsoCriteria = new LezioneCorsoCriteria();

        assertThat(lezioneCorsoCriteria).hasToString("LezioneCorsoCriteria{}");
    }

    private static void setAllFilters(LezioneCorsoCriteria lezioneCorsoCriteria) {
        lezioneCorsoCriteria.id();
        lezioneCorsoCriteria.titolo();
        lezioneCorsoCriteria.videoUrl();
        lezioneCorsoCriteria.corsoAcademyId();
        lezioneCorsoCriteria.distinct();
    }

    private static Condition<LezioneCorsoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitolo()) &&
                condition.apply(criteria.getVideoUrl()) &&
                condition.apply(criteria.getCorsoAcademyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LezioneCorsoCriteria> copyFiltersAre(
        LezioneCorsoCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitolo(), copy.getTitolo()) &&
                condition.apply(criteria.getVideoUrl(), copy.getVideoUrl()) &&
                condition.apply(criteria.getCorsoAcademyId(), copy.getCorsoAcademyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
