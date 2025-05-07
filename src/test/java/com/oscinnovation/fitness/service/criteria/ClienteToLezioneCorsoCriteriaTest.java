package com.oscinnovation.fitness.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ClienteToLezioneCorsoCriteriaTest {

    @Test
    void newClienteToLezioneCorsoCriteriaHasAllFiltersNullTest() {
        var clienteToLezioneCorsoCriteria = new ClienteToLezioneCorsoCriteria();
        assertThat(clienteToLezioneCorsoCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void clienteToLezioneCorsoCriteriaFluentMethodsCreatesFiltersTest() {
        var clienteToLezioneCorsoCriteria = new ClienteToLezioneCorsoCriteria();

        setAllFilters(clienteToLezioneCorsoCriteria);

        assertThat(clienteToLezioneCorsoCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void clienteToLezioneCorsoCriteriaCopyCreatesNullFilterTest() {
        var clienteToLezioneCorsoCriteria = new ClienteToLezioneCorsoCriteria();
        var copy = clienteToLezioneCorsoCriteria.copy();

        assertThat(clienteToLezioneCorsoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(clienteToLezioneCorsoCriteria)
        );
    }

    @Test
    void clienteToLezioneCorsoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var clienteToLezioneCorsoCriteria = new ClienteToLezioneCorsoCriteria();
        setAllFilters(clienteToLezioneCorsoCriteria);

        var copy = clienteToLezioneCorsoCriteria.copy();

        assertThat(clienteToLezioneCorsoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(clienteToLezioneCorsoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var clienteToLezioneCorsoCriteria = new ClienteToLezioneCorsoCriteria();

        assertThat(clienteToLezioneCorsoCriteria).hasToString("ClienteToLezioneCorsoCriteria{}");
    }

    private static void setAllFilters(ClienteToLezioneCorsoCriteria clienteToLezioneCorsoCriteria) {
        clienteToLezioneCorsoCriteria.id();
        clienteToLezioneCorsoCriteria.completato();
        clienteToLezioneCorsoCriteria.dataCompletamento();
        clienteToLezioneCorsoCriteria.clienteId();
        clienteToLezioneCorsoCriteria.lezioneCorsoId();
        clienteToLezioneCorsoCriteria.distinct();
    }

    private static Condition<ClienteToLezioneCorsoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCompletato()) &&
                condition.apply(criteria.getDataCompletamento()) &&
                condition.apply(criteria.getClienteId()) &&
                condition.apply(criteria.getLezioneCorsoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ClienteToLezioneCorsoCriteria> copyFiltersAre(
        ClienteToLezioneCorsoCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCompletato(), copy.getCompletato()) &&
                condition.apply(criteria.getDataCompletamento(), copy.getDataCompletamento()) &&
                condition.apply(criteria.getClienteId(), copy.getClienteId()) &&
                condition.apply(criteria.getLezioneCorsoId(), copy.getLezioneCorsoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
