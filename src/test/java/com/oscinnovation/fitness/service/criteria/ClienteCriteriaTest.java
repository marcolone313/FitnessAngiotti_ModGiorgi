package com.oscinnovation.fitness.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ClienteCriteriaTest {

    @Test
    void newClienteCriteriaHasAllFiltersNullTest() {
        var clienteCriteria = new ClienteCriteria();
        assertThat(clienteCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void clienteCriteriaFluentMethodsCreatesFiltersTest() {
        var clienteCriteria = new ClienteCriteria();

        setAllFilters(clienteCriteria);

        assertThat(clienteCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void clienteCriteriaCopyCreatesNullFilterTest() {
        var clienteCriteria = new ClienteCriteria();
        var copy = clienteCriteria.copy();

        assertThat(clienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(clienteCriteria)
        );
    }

    @Test
    void clienteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var clienteCriteria = new ClienteCriteria();
        setAllFilters(clienteCriteria);

        var copy = clienteCriteria.copy();

        assertThat(clienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(clienteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var clienteCriteria = new ClienteCriteria();

        assertThat(clienteCriteria).hasToString("ClienteCriteria{}");
    }

    private static void setAllFilters(ClienteCriteria clienteCriteria) {
        clienteCriteria.id();
        clienteCriteria.nome();
        clienteCriteria.cognome();
        clienteCriteria.dataDiNascita();
        clienteCriteria.codiceFiscale();
        clienteCriteria.codiceCliente();
        clienteCriteria.email();
        clienteCriteria.telefono();
        clienteCriteria.userId();
        clienteCriteria.distinct();
    }

    private static Condition<ClienteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNome()) &&
                condition.apply(criteria.getCognome()) &&
                condition.apply(criteria.getDataDiNascita()) &&
                condition.apply(criteria.getCodiceFiscale()) &&
                condition.apply(criteria.getCodiceCliente()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getTelefono()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ClienteCriteria> copyFiltersAre(ClienteCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNome(), copy.getNome()) &&
                condition.apply(criteria.getCognome(), copy.getCognome()) &&
                condition.apply(criteria.getDataDiNascita(), copy.getDataDiNascita()) &&
                condition.apply(criteria.getCodiceFiscale(), copy.getCodiceFiscale()) &&
                condition.apply(criteria.getCodiceCliente(), copy.getCodiceCliente()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getTelefono(), copy.getTelefono()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
