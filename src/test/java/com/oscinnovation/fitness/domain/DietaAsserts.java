package com.oscinnovation.fitness.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class DietaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDietaAllPropertiesEquals(Dieta expected, Dieta actual) {
        assertDietaAutoGeneratedPropertiesEquals(expected, actual);
        assertDietaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDietaAllUpdatablePropertiesEquals(Dieta expected, Dieta actual) {
        assertDietaUpdatableFieldsEquals(expected, actual);
        assertDietaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDietaAutoGeneratedPropertiesEquals(Dieta expected, Dieta actual) {
        assertThat(expected)
            .as("Verify Dieta auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDietaUpdatableFieldsEquals(Dieta expected, Dieta actual) {
        assertThat(expected)
            .as("Verify Dieta relevant properties")
            .satisfies(e -> assertThat(e.getDataCreazione()).as("check dataCreazione").isEqualTo(actual.getDataCreazione()))
            .satisfies(e -> assertThat(e.getMese()).as("check mese").isEqualTo(actual.getMese()))
            .satisfies(e -> assertThat(e.getTipo()).as("check tipo").isEqualTo(actual.getTipo()))
            .satisfies(e -> assertThat(e.getMacros()).as("check macros").isEqualTo(actual.getMacros()))
            .satisfies(e -> assertThat(e.getFabbisognoCalorico()).as("check fabbisognoCalorico").isEqualTo(actual.getFabbisognoCalorico()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDietaUpdatableRelationshipsEquals(Dieta expected, Dieta actual) {
        assertThat(expected)
            .as("Verify Dieta relationships")
            .satisfies(e -> assertThat(e.getCliente()).as("check cliente").isEqualTo(actual.getCliente()));
    }
}
