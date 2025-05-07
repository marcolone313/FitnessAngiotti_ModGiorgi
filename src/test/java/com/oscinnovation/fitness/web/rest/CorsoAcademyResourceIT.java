package com.oscinnovation.fitness.web.rest;

import static com.oscinnovation.fitness.domain.CorsoAcademyAsserts.*;
import static com.oscinnovation.fitness.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscinnovation.fitness.IntegrationTest;
import com.oscinnovation.fitness.domain.CorsoAcademy;
import com.oscinnovation.fitness.domain.enumeration.LivelloCorso;
import com.oscinnovation.fitness.repository.CorsoAcademyRepository;
import com.oscinnovation.fitness.service.dto.CorsoAcademyDTO;
import com.oscinnovation.fitness.service.mapper.CorsoAcademyMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CorsoAcademyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CorsoAcademyResourceIT {

    private static final LivelloCorso DEFAULT_LIVELLO = LivelloCorso.PRINCIPIANTE;
    private static final LivelloCorso UPDATED_LIVELLO = LivelloCorso.INTERMEDIO;

    private static final String DEFAULT_TITOLO = "AAAAAAAAAA";
    private static final String UPDATED_TITOLO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/corso-academies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CorsoAcademyRepository corsoAcademyRepository;

    @Autowired
    private CorsoAcademyMapper corsoAcademyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCorsoAcademyMockMvc;

    private CorsoAcademy corsoAcademy;

    private CorsoAcademy insertedCorsoAcademy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CorsoAcademy createEntity() {
        return new CorsoAcademy().livello(DEFAULT_LIVELLO).titolo(DEFAULT_TITOLO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CorsoAcademy createUpdatedEntity() {
        return new CorsoAcademy().livello(UPDATED_LIVELLO).titolo(UPDATED_TITOLO);
    }

    @BeforeEach
    public void initTest() {
        corsoAcademy = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCorsoAcademy != null) {
            corsoAcademyRepository.delete(insertedCorsoAcademy);
            insertedCorsoAcademy = null;
        }
    }

    @Test
    @Transactional
    void createCorsoAcademy() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CorsoAcademy
        CorsoAcademyDTO corsoAcademyDTO = corsoAcademyMapper.toDto(corsoAcademy);
        var returnedCorsoAcademyDTO = om.readValue(
            restCorsoAcademyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(corsoAcademyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CorsoAcademyDTO.class
        );

        // Validate the CorsoAcademy in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCorsoAcademy = corsoAcademyMapper.toEntity(returnedCorsoAcademyDTO);
        assertCorsoAcademyUpdatableFieldsEquals(returnedCorsoAcademy, getPersistedCorsoAcademy(returnedCorsoAcademy));

        insertedCorsoAcademy = returnedCorsoAcademy;
    }

    @Test
    @Transactional
    void createCorsoAcademyWithExistingId() throws Exception {
        // Create the CorsoAcademy with an existing ID
        corsoAcademy.setId(1L);
        CorsoAcademyDTO corsoAcademyDTO = corsoAcademyMapper.toDto(corsoAcademy);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorsoAcademyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(corsoAcademyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CorsoAcademy in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLivelloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        corsoAcademy.setLivello(null);

        // Create the CorsoAcademy, which fails.
        CorsoAcademyDTO corsoAcademyDTO = corsoAcademyMapper.toDto(corsoAcademy);

        restCorsoAcademyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(corsoAcademyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitoloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        corsoAcademy.setTitolo(null);

        // Create the CorsoAcademy, which fails.
        CorsoAcademyDTO corsoAcademyDTO = corsoAcademyMapper.toDto(corsoAcademy);

        restCorsoAcademyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(corsoAcademyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCorsoAcademies() throws Exception {
        // Initialize the database
        insertedCorsoAcademy = corsoAcademyRepository.saveAndFlush(corsoAcademy);

        // Get all the corsoAcademyList
        restCorsoAcademyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corsoAcademy.getId().intValue())))
            .andExpect(jsonPath("$.[*].livello").value(hasItem(DEFAULT_LIVELLO.toString())))
            .andExpect(jsonPath("$.[*].titolo").value(hasItem(DEFAULT_TITOLO)));
    }

    @Test
    @Transactional
    void getCorsoAcademy() throws Exception {
        // Initialize the database
        insertedCorsoAcademy = corsoAcademyRepository.saveAndFlush(corsoAcademy);

        // Get the corsoAcademy
        restCorsoAcademyMockMvc
            .perform(get(ENTITY_API_URL_ID, corsoAcademy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(corsoAcademy.getId().intValue()))
            .andExpect(jsonPath("$.livello").value(DEFAULT_LIVELLO.toString()))
            .andExpect(jsonPath("$.titolo").value(DEFAULT_TITOLO));
    }

    @Test
    @Transactional
    void getCorsoAcademiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCorsoAcademy = corsoAcademyRepository.saveAndFlush(corsoAcademy);

        Long id = corsoAcademy.getId();

        defaultCorsoAcademyFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCorsoAcademyFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCorsoAcademyFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCorsoAcademiesByLivelloIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorsoAcademy = corsoAcademyRepository.saveAndFlush(corsoAcademy);

        // Get all the corsoAcademyList where livello equals to
        defaultCorsoAcademyFiltering("livello.equals=" + DEFAULT_LIVELLO, "livello.equals=" + UPDATED_LIVELLO);
    }

    @Test
    @Transactional
    void getAllCorsoAcademiesByLivelloIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorsoAcademy = corsoAcademyRepository.saveAndFlush(corsoAcademy);

        // Get all the corsoAcademyList where livello in
        defaultCorsoAcademyFiltering("livello.in=" + DEFAULT_LIVELLO + "," + UPDATED_LIVELLO, "livello.in=" + UPDATED_LIVELLO);
    }

    @Test
    @Transactional
    void getAllCorsoAcademiesByLivelloIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorsoAcademy = corsoAcademyRepository.saveAndFlush(corsoAcademy);

        // Get all the corsoAcademyList where livello is not null
        defaultCorsoAcademyFiltering("livello.specified=true", "livello.specified=false");
    }

    @Test
    @Transactional
    void getAllCorsoAcademiesByTitoloIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorsoAcademy = corsoAcademyRepository.saveAndFlush(corsoAcademy);

        // Get all the corsoAcademyList where titolo equals to
        defaultCorsoAcademyFiltering("titolo.equals=" + DEFAULT_TITOLO, "titolo.equals=" + UPDATED_TITOLO);
    }

    @Test
    @Transactional
    void getAllCorsoAcademiesByTitoloIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorsoAcademy = corsoAcademyRepository.saveAndFlush(corsoAcademy);

        // Get all the corsoAcademyList where titolo in
        defaultCorsoAcademyFiltering("titolo.in=" + DEFAULT_TITOLO + "," + UPDATED_TITOLO, "titolo.in=" + UPDATED_TITOLO);
    }

    @Test
    @Transactional
    void getAllCorsoAcademiesByTitoloIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorsoAcademy = corsoAcademyRepository.saveAndFlush(corsoAcademy);

        // Get all the corsoAcademyList where titolo is not null
        defaultCorsoAcademyFiltering("titolo.specified=true", "titolo.specified=false");
    }

    @Test
    @Transactional
    void getAllCorsoAcademiesByTitoloContainsSomething() throws Exception {
        // Initialize the database
        insertedCorsoAcademy = corsoAcademyRepository.saveAndFlush(corsoAcademy);

        // Get all the corsoAcademyList where titolo contains
        defaultCorsoAcademyFiltering("titolo.contains=" + DEFAULT_TITOLO, "titolo.contains=" + UPDATED_TITOLO);
    }

    @Test
    @Transactional
    void getAllCorsoAcademiesByTitoloNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCorsoAcademy = corsoAcademyRepository.saveAndFlush(corsoAcademy);

        // Get all the corsoAcademyList where titolo does not contain
        defaultCorsoAcademyFiltering("titolo.doesNotContain=" + UPDATED_TITOLO, "titolo.doesNotContain=" + DEFAULT_TITOLO);
    }

    private void defaultCorsoAcademyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCorsoAcademyShouldBeFound(shouldBeFound);
        defaultCorsoAcademyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCorsoAcademyShouldBeFound(String filter) throws Exception {
        restCorsoAcademyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corsoAcademy.getId().intValue())))
            .andExpect(jsonPath("$.[*].livello").value(hasItem(DEFAULT_LIVELLO.toString())))
            .andExpect(jsonPath("$.[*].titolo").value(hasItem(DEFAULT_TITOLO)));

        // Check, that the count call also returns 1
        restCorsoAcademyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCorsoAcademyShouldNotBeFound(String filter) throws Exception {
        restCorsoAcademyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCorsoAcademyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCorsoAcademy() throws Exception {
        // Get the corsoAcademy
        restCorsoAcademyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCorsoAcademy() throws Exception {
        // Initialize the database
        insertedCorsoAcademy = corsoAcademyRepository.saveAndFlush(corsoAcademy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the corsoAcademy
        CorsoAcademy updatedCorsoAcademy = corsoAcademyRepository.findById(corsoAcademy.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCorsoAcademy are not directly saved in db
        em.detach(updatedCorsoAcademy);
        updatedCorsoAcademy.livello(UPDATED_LIVELLO).titolo(UPDATED_TITOLO);
        CorsoAcademyDTO corsoAcademyDTO = corsoAcademyMapper.toDto(updatedCorsoAcademy);

        restCorsoAcademyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, corsoAcademyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(corsoAcademyDTO))
            )
            .andExpect(status().isOk());

        // Validate the CorsoAcademy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCorsoAcademyToMatchAllProperties(updatedCorsoAcademy);
    }

    @Test
    @Transactional
    void putNonExistingCorsoAcademy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corsoAcademy.setId(longCount.incrementAndGet());

        // Create the CorsoAcademy
        CorsoAcademyDTO corsoAcademyDTO = corsoAcademyMapper.toDto(corsoAcademy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorsoAcademyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, corsoAcademyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(corsoAcademyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CorsoAcademy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCorsoAcademy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corsoAcademy.setId(longCount.incrementAndGet());

        // Create the CorsoAcademy
        CorsoAcademyDTO corsoAcademyDTO = corsoAcademyMapper.toDto(corsoAcademy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorsoAcademyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(corsoAcademyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CorsoAcademy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCorsoAcademy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corsoAcademy.setId(longCount.incrementAndGet());

        // Create the CorsoAcademy
        CorsoAcademyDTO corsoAcademyDTO = corsoAcademyMapper.toDto(corsoAcademy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorsoAcademyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(corsoAcademyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CorsoAcademy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCorsoAcademyWithPatch() throws Exception {
        // Initialize the database
        insertedCorsoAcademy = corsoAcademyRepository.saveAndFlush(corsoAcademy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the corsoAcademy using partial update
        CorsoAcademy partialUpdatedCorsoAcademy = new CorsoAcademy();
        partialUpdatedCorsoAcademy.setId(corsoAcademy.getId());

        partialUpdatedCorsoAcademy.livello(UPDATED_LIVELLO);

        restCorsoAcademyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorsoAcademy.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCorsoAcademy))
            )
            .andExpect(status().isOk());

        // Validate the CorsoAcademy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCorsoAcademyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCorsoAcademy, corsoAcademy),
            getPersistedCorsoAcademy(corsoAcademy)
        );
    }

    @Test
    @Transactional
    void fullUpdateCorsoAcademyWithPatch() throws Exception {
        // Initialize the database
        insertedCorsoAcademy = corsoAcademyRepository.saveAndFlush(corsoAcademy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the corsoAcademy using partial update
        CorsoAcademy partialUpdatedCorsoAcademy = new CorsoAcademy();
        partialUpdatedCorsoAcademy.setId(corsoAcademy.getId());

        partialUpdatedCorsoAcademy.livello(UPDATED_LIVELLO).titolo(UPDATED_TITOLO);

        restCorsoAcademyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorsoAcademy.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCorsoAcademy))
            )
            .andExpect(status().isOk());

        // Validate the CorsoAcademy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCorsoAcademyUpdatableFieldsEquals(partialUpdatedCorsoAcademy, getPersistedCorsoAcademy(partialUpdatedCorsoAcademy));
    }

    @Test
    @Transactional
    void patchNonExistingCorsoAcademy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corsoAcademy.setId(longCount.incrementAndGet());

        // Create the CorsoAcademy
        CorsoAcademyDTO corsoAcademyDTO = corsoAcademyMapper.toDto(corsoAcademy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorsoAcademyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, corsoAcademyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(corsoAcademyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CorsoAcademy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCorsoAcademy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corsoAcademy.setId(longCount.incrementAndGet());

        // Create the CorsoAcademy
        CorsoAcademyDTO corsoAcademyDTO = corsoAcademyMapper.toDto(corsoAcademy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorsoAcademyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(corsoAcademyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CorsoAcademy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCorsoAcademy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corsoAcademy.setId(longCount.incrementAndGet());

        // Create the CorsoAcademy
        CorsoAcademyDTO corsoAcademyDTO = corsoAcademyMapper.toDto(corsoAcademy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorsoAcademyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(corsoAcademyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CorsoAcademy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCorsoAcademy() throws Exception {
        // Initialize the database
        insertedCorsoAcademy = corsoAcademyRepository.saveAndFlush(corsoAcademy);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the corsoAcademy
        restCorsoAcademyMockMvc
            .perform(delete(ENTITY_API_URL_ID, corsoAcademy.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return corsoAcademyRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CorsoAcademy getPersistedCorsoAcademy(CorsoAcademy corsoAcademy) {
        return corsoAcademyRepository.findById(corsoAcademy.getId()).orElseThrow();
    }

    protected void assertPersistedCorsoAcademyToMatchAllProperties(CorsoAcademy expectedCorsoAcademy) {
        assertCorsoAcademyAllPropertiesEquals(expectedCorsoAcademy, getPersistedCorsoAcademy(expectedCorsoAcademy));
    }

    protected void assertPersistedCorsoAcademyToMatchUpdatableProperties(CorsoAcademy expectedCorsoAcademy) {
        assertCorsoAcademyAllUpdatablePropertiesEquals(expectedCorsoAcademy, getPersistedCorsoAcademy(expectedCorsoAcademy));
    }
}
