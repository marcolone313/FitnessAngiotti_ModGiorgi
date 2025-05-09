package com.oscinnovation.fitness.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class GymAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertGymAllPropertiesEquals(Gym expected, Gym actual) {
        assertGymAutoGeneratedPropertiesEquals(expected, actual);
        assertGymAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertGymAllUpdatablePropertiesEquals(Gym expected, Gym actual) {
        assertGymUpdatableFieldsEquals(expected, actual);
        assertGymUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertGymAutoGeneratedPropertiesEquals(Gym expected, Gym actual) {
        assertThat(expected)
            .as("Verify Gym auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertGymUpdatableFieldsEquals(Gym expected, Gym actual) {
        assertThat(expected)
            .as("Verify Gym relevant properties")
            .satisfies(e -> assertThat(e.getSets()).as("check sets").isEqualTo(actual.getSets()))
            .satisfies(e -> assertThat(e.getReps()).as("check reps").isEqualTo(actual.getReps()))
            .satisfies(e -> assertThat(e.getRecupero()).as("check recupero").isEqualTo(actual.getRecupero()))
            .satisfies(e -> assertThat(e.getPeso()).as("check peso").isEqualTo(actual.getPeso()))
            .satisfies(e -> assertThat(e.getCompletato()).as("check completato").isEqualTo(actual.getCompletato()))
            .satisfies(e -> assertThat(e.getSvolto()).as("check svolto").isEqualTo(actual.getSvolto()))
            .satisfies(e -> assertThat(e.getFeedback()).as("check feedback").isEqualTo(actual.getFeedback()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertGymUpdatableRelationshipsEquals(Gym expected, Gym actual) {
        assertThat(expected)
            .as("Verify Gym relationships")
            .satisfies(e ->
                assertThat(e.getAllenamentoGiornaliero()).as("check allenamentoGiornaliero").isEqualTo(actual.getAllenamentoGiornaliero())
            )
            .satisfies(e -> assertThat(e.getEsercizio()).as("check esercizio").isEqualTo(actual.getEsercizio()));
    }
}
