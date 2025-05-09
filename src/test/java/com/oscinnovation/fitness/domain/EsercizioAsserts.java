package com.oscinnovation.fitness.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class EsercizioAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEsercizioAllPropertiesEquals(Esercizio expected, Esercizio actual) {
        assertEsercizioAutoGeneratedPropertiesEquals(expected, actual);
        assertEsercizioAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEsercizioAllUpdatablePropertiesEquals(Esercizio expected, Esercizio actual) {
        assertEsercizioUpdatableFieldsEquals(expected, actual);
        assertEsercizioUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEsercizioAutoGeneratedPropertiesEquals(Esercizio expected, Esercizio actual) {
        assertThat(expected)
            .as("Verify Esercizio auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEsercizioUpdatableFieldsEquals(Esercizio expected, Esercizio actual) {
        assertThat(expected)
            .as("Verify Esercizio relevant properties")
            .satisfies(e -> assertThat(e.getNome()).as("check nome").isEqualTo(actual.getNome()))
            .satisfies(e -> assertThat(e.getTipo()).as("check tipo").isEqualTo(actual.getTipo()))
            .satisfies(e -> assertThat(e.getVideoUrl()).as("check videoUrl").isEqualTo(actual.getVideoUrl()))
            .satisfies(e -> assertThat(e.getDescrizione()).as("check descrizione").isEqualTo(actual.getDescrizione()))
            .satisfies(e -> assertThat(e.getFoto()).as("check foto").isEqualTo(actual.getFoto()))
            .satisfies(e -> assertThat(e.getFotoContentType()).as("check foto contenty type").isEqualTo(actual.getFotoContentType()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEsercizioUpdatableRelationshipsEquals(Esercizio expected, Esercizio actual) {
        // empty method
    }
}
