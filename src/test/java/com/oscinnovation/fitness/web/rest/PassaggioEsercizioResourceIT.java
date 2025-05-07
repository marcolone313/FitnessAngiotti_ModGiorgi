package com.oscinnovation.fitness.web.rest;

import static com.oscinnovation.fitness.domain.PassaggioEsercizioAsserts.*;
import static com.oscinnovation.fitness.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscinnovation.fitness.IntegrationTest;
import com.oscinnovation.fitness.domain.Esercizio;
import com.oscinnovation.fitness.domain.PassaggioEsercizio;
import com.oscinnovation.fitness.repository.PassaggioEsercizioRepository;
import com.oscinnovation.fitness.service.PassaggioEsercizioService;
import com.oscinnovation.fitness.service.dto.PassaggioEsercizioDTO;
import com.oscinnovation.fitness.service.mapper.PassaggioEsercizioMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PassaggioEsercizioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PassaggioEsercizioResourceIT {

    private static final Integer DEFAULT_INDICE = 1;
    private static final Integer UPDATED_INDICE = 2;
    private static final Integer SMALLER_INDICE = 1 - 1;

    private static final String DEFAULT_DESCRIZIONE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIZIONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/passaggio-esercizios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PassaggioEsercizioRepository passaggioEsercizioRepository;

    @Mock
    private PassaggioEsercizioRepository passaggioEsercizioRepositoryMock;

    @Autowired
    private PassaggioEsercizioMapper passaggioEsercizioMapper;

    @Mock
    private PassaggioEsercizioService passaggioEsercizioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPassaggioEsercizioMockMvc;

    private PassaggioEsercizio passaggioEsercizio;

    private PassaggioEsercizio insertedPassaggioEsercizio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PassaggioEsercizio createEntity(EntityManager em) {
        PassaggioEsercizio passaggioEsercizio = new PassaggioEsercizio().indice(DEFAULT_INDICE).descrizione(DEFAULT_DESCRIZIONE);
        // Add required entity
        Esercizio esercizio;
        if (TestUtil.findAll(em, Esercizio.class).isEmpty()) {
            esercizio = EsercizioResourceIT.createEntity();
            em.persist(esercizio);
            em.flush();
        } else {
            esercizio = TestUtil.findAll(em, Esercizio.class).get(0);
        }
        passaggioEsercizio.setEsercizio(esercizio);
        return passaggioEsercizio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PassaggioEsercizio createUpdatedEntity(EntityManager em) {
        PassaggioEsercizio updatedPassaggioEsercizio = new PassaggioEsercizio().indice(UPDATED_INDICE).descrizione(UPDATED_DESCRIZIONE);
        // Add required entity
        Esercizio esercizio;
        if (TestUtil.findAll(em, Esercizio.class).isEmpty()) {
            esercizio = EsercizioResourceIT.createUpdatedEntity();
            em.persist(esercizio);
            em.flush();
        } else {
            esercizio = TestUtil.findAll(em, Esercizio.class).get(0);
        }
        updatedPassaggioEsercizio.setEsercizio(esercizio);
        return updatedPassaggioEsercizio;
    }

    @BeforeEach
    public void initTest() {
        passaggioEsercizio = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPassaggioEsercizio != null) {
            passaggioEsercizioRepository.delete(insertedPassaggioEsercizio);
            insertedPassaggioEsercizio = null;
        }
    }

    @Test
    @Transactional
    void createPassaggioEsercizio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PassaggioEsercizio
        PassaggioEsercizioDTO passaggioEsercizioDTO = passaggioEsercizioMapper.toDto(passaggioEsercizio);
        var returnedPassaggioEsercizioDTO = om.readValue(
            restPassaggioEsercizioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passaggioEsercizioDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PassaggioEsercizioDTO.class
        );

        // Validate the PassaggioEsercizio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPassaggioEsercizio = passaggioEsercizioMapper.toEntity(returnedPassaggioEsercizioDTO);
        assertPassaggioEsercizioUpdatableFieldsEquals(
            returnedPassaggioEsercizio,
            getPersistedPassaggioEsercizio(returnedPassaggioEsercizio)
        );

        insertedPassaggioEsercizio = returnedPassaggioEsercizio;
    }

    @Test
    @Transactional
    void createPassaggioEsercizioWithExistingId() throws Exception {
        // Create the PassaggioEsercizio with an existing ID
        passaggioEsercizio.setId(1L);
        PassaggioEsercizioDTO passaggioEsercizioDTO = passaggioEsercizioMapper.toDto(passaggioEsercizio);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPassaggioEsercizioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passaggioEsercizioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PassaggioEsercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIndiceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        passaggioEsercizio.setIndice(null);

        // Create the PassaggioEsercizio, which fails.
        PassaggioEsercizioDTO passaggioEsercizioDTO = passaggioEsercizioMapper.toDto(passaggioEsercizio);

        restPassaggioEsercizioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passaggioEsercizioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPassaggioEsercizios() throws Exception {
        // Initialize the database
        insertedPassaggioEsercizio = passaggioEsercizioRepository.saveAndFlush(passaggioEsercizio);

        // Get all the passaggioEsercizioList
        restPassaggioEsercizioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passaggioEsercizio.getId().intValue())))
            .andExpect(jsonPath("$.[*].indice").value(hasItem(DEFAULT_INDICE)))
            .andExpect(jsonPath("$.[*].descrizione").value(hasItem(DEFAULT_DESCRIZIONE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPassaggioEserciziosWithEagerRelationshipsIsEnabled() throws Exception {
        when(passaggioEsercizioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPassaggioEsercizioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(passaggioEsercizioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPassaggioEserciziosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(passaggioEsercizioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPassaggioEsercizioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(passaggioEsercizioRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPassaggioEsercizio() throws Exception {
        // Initialize the database
        insertedPassaggioEsercizio = passaggioEsercizioRepository.saveAndFlush(passaggioEsercizio);

        // Get the passaggioEsercizio
        restPassaggioEsercizioMockMvc
            .perform(get(ENTITY_API_URL_ID, passaggioEsercizio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(passaggioEsercizio.getId().intValue()))
            .andExpect(jsonPath("$.indice").value(DEFAULT_INDICE))
            .andExpect(jsonPath("$.descrizione").value(DEFAULT_DESCRIZIONE.toString()));
    }

    @Test
    @Transactional
    void getPassaggioEserciziosByIdFiltering() throws Exception {
        // Initialize the database
        insertedPassaggioEsercizio = passaggioEsercizioRepository.saveAndFlush(passaggioEsercizio);

        Long id = passaggioEsercizio.getId();

        defaultPassaggioEsercizioFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPassaggioEsercizioFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPassaggioEsercizioFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPassaggioEserciziosByIndiceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPassaggioEsercizio = passaggioEsercizioRepository.saveAndFlush(passaggioEsercizio);

        // Get all the passaggioEsercizioList where indice equals to
        defaultPassaggioEsercizioFiltering("indice.equals=" + DEFAULT_INDICE, "indice.equals=" + UPDATED_INDICE);
    }

    @Test
    @Transactional
    void getAllPassaggioEserciziosByIndiceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPassaggioEsercizio = passaggioEsercizioRepository.saveAndFlush(passaggioEsercizio);

        // Get all the passaggioEsercizioList where indice in
        defaultPassaggioEsercizioFiltering("indice.in=" + DEFAULT_INDICE + "," + UPDATED_INDICE, "indice.in=" + UPDATED_INDICE);
    }

    @Test
    @Transactional
    void getAllPassaggioEserciziosByIndiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPassaggioEsercizio = passaggioEsercizioRepository.saveAndFlush(passaggioEsercizio);

        // Get all the passaggioEsercizioList where indice is not null
        defaultPassaggioEsercizioFiltering("indice.specified=true", "indice.specified=false");
    }

    @Test
    @Transactional
    void getAllPassaggioEserciziosByIndiceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPassaggioEsercizio = passaggioEsercizioRepository.saveAndFlush(passaggioEsercizio);

        // Get all the passaggioEsercizioList where indice is greater than or equal to
        defaultPassaggioEsercizioFiltering("indice.greaterThanOrEqual=" + DEFAULT_INDICE, "indice.greaterThanOrEqual=" + UPDATED_INDICE);
    }

    @Test
    @Transactional
    void getAllPassaggioEserciziosByIndiceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPassaggioEsercizio = passaggioEsercizioRepository.saveAndFlush(passaggioEsercizio);

        // Get all the passaggioEsercizioList where indice is less than or equal to
        defaultPassaggioEsercizioFiltering("indice.lessThanOrEqual=" + DEFAULT_INDICE, "indice.lessThanOrEqual=" + SMALLER_INDICE);
    }

    @Test
    @Transactional
    void getAllPassaggioEserciziosByIndiceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPassaggioEsercizio = passaggioEsercizioRepository.saveAndFlush(passaggioEsercizio);

        // Get all the passaggioEsercizioList where indice is less than
        defaultPassaggioEsercizioFiltering("indice.lessThan=" + UPDATED_INDICE, "indice.lessThan=" + DEFAULT_INDICE);
    }

    @Test
    @Transactional
    void getAllPassaggioEserciziosByIndiceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPassaggioEsercizio = passaggioEsercizioRepository.saveAndFlush(passaggioEsercizio);

        // Get all the passaggioEsercizioList where indice is greater than
        defaultPassaggioEsercizioFiltering("indice.greaterThan=" + SMALLER_INDICE, "indice.greaterThan=" + DEFAULT_INDICE);
    }

    @Test
    @Transactional
    void getAllPassaggioEserciziosByEsercizioIsEqualToSomething() throws Exception {
        Esercizio esercizio;
        if (TestUtil.findAll(em, Esercizio.class).isEmpty()) {
            passaggioEsercizioRepository.saveAndFlush(passaggioEsercizio);
            esercizio = EsercizioResourceIT.createEntity();
        } else {
            esercizio = TestUtil.findAll(em, Esercizio.class).get(0);
        }
        em.persist(esercizio);
        em.flush();
        passaggioEsercizio.setEsercizio(esercizio);
        passaggioEsercizioRepository.saveAndFlush(passaggioEsercizio);
        Long esercizioId = esercizio.getId();
        // Get all the passaggioEsercizioList where esercizio equals to esercizioId
        defaultPassaggioEsercizioShouldBeFound("esercizioId.equals=" + esercizioId);

        // Get all the passaggioEsercizioList where esercizio equals to (esercizioId + 1)
        defaultPassaggioEsercizioShouldNotBeFound("esercizioId.equals=" + (esercizioId + 1));
    }

    private void defaultPassaggioEsercizioFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPassaggioEsercizioShouldBeFound(shouldBeFound);
        defaultPassaggioEsercizioShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPassaggioEsercizioShouldBeFound(String filter) throws Exception {
        restPassaggioEsercizioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passaggioEsercizio.getId().intValue())))
            .andExpect(jsonPath("$.[*].indice").value(hasItem(DEFAULT_INDICE)))
            .andExpect(jsonPath("$.[*].descrizione").value(hasItem(DEFAULT_DESCRIZIONE.toString())));

        // Check, that the count call also returns 1
        restPassaggioEsercizioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPassaggioEsercizioShouldNotBeFound(String filter) throws Exception {
        restPassaggioEsercizioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPassaggioEsercizioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPassaggioEsercizio() throws Exception {
        // Get the passaggioEsercizio
        restPassaggioEsercizioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPassaggioEsercizio() throws Exception {
        // Initialize the database
        insertedPassaggioEsercizio = passaggioEsercizioRepository.saveAndFlush(passaggioEsercizio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the passaggioEsercizio
        PassaggioEsercizio updatedPassaggioEsercizio = passaggioEsercizioRepository.findById(passaggioEsercizio.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPassaggioEsercizio are not directly saved in db
        em.detach(updatedPassaggioEsercizio);
        updatedPassaggioEsercizio.indice(UPDATED_INDICE).descrizione(UPDATED_DESCRIZIONE);
        PassaggioEsercizioDTO passaggioEsercizioDTO = passaggioEsercizioMapper.toDto(updatedPassaggioEsercizio);

        restPassaggioEsercizioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, passaggioEsercizioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(passaggioEsercizioDTO))
            )
            .andExpect(status().isOk());

        // Validate the PassaggioEsercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPassaggioEsercizioToMatchAllProperties(updatedPassaggioEsercizio);
    }

    @Test
    @Transactional
    void putNonExistingPassaggioEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passaggioEsercizio.setId(longCount.incrementAndGet());

        // Create the PassaggioEsercizio
        PassaggioEsercizioDTO passaggioEsercizioDTO = passaggioEsercizioMapper.toDto(passaggioEsercizio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassaggioEsercizioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, passaggioEsercizioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(passaggioEsercizioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PassaggioEsercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPassaggioEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passaggioEsercizio.setId(longCount.incrementAndGet());

        // Create the PassaggioEsercizio
        PassaggioEsercizioDTO passaggioEsercizioDTO = passaggioEsercizioMapper.toDto(passaggioEsercizio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassaggioEsercizioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(passaggioEsercizioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PassaggioEsercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPassaggioEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passaggioEsercizio.setId(longCount.incrementAndGet());

        // Create the PassaggioEsercizio
        PassaggioEsercizioDTO passaggioEsercizioDTO = passaggioEsercizioMapper.toDto(passaggioEsercizio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassaggioEsercizioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passaggioEsercizioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PassaggioEsercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePassaggioEsercizioWithPatch() throws Exception {
        // Initialize the database
        insertedPassaggioEsercizio = passaggioEsercizioRepository.saveAndFlush(passaggioEsercizio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the passaggioEsercizio using partial update
        PassaggioEsercizio partialUpdatedPassaggioEsercizio = new PassaggioEsercizio();
        partialUpdatedPassaggioEsercizio.setId(passaggioEsercizio.getId());

        partialUpdatedPassaggioEsercizio.indice(UPDATED_INDICE);

        restPassaggioEsercizioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPassaggioEsercizio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPassaggioEsercizio))
            )
            .andExpect(status().isOk());

        // Validate the PassaggioEsercizio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPassaggioEsercizioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPassaggioEsercizio, passaggioEsercizio),
            getPersistedPassaggioEsercizio(passaggioEsercizio)
        );
    }

    @Test
    @Transactional
    void fullUpdatePassaggioEsercizioWithPatch() throws Exception {
        // Initialize the database
        insertedPassaggioEsercizio = passaggioEsercizioRepository.saveAndFlush(passaggioEsercizio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the passaggioEsercizio using partial update
        PassaggioEsercizio partialUpdatedPassaggioEsercizio = new PassaggioEsercizio();
        partialUpdatedPassaggioEsercizio.setId(passaggioEsercizio.getId());

        partialUpdatedPassaggioEsercizio.indice(UPDATED_INDICE).descrizione(UPDATED_DESCRIZIONE);

        restPassaggioEsercizioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPassaggioEsercizio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPassaggioEsercizio))
            )
            .andExpect(status().isOk());

        // Validate the PassaggioEsercizio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPassaggioEsercizioUpdatableFieldsEquals(
            partialUpdatedPassaggioEsercizio,
            getPersistedPassaggioEsercizio(partialUpdatedPassaggioEsercizio)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPassaggioEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passaggioEsercizio.setId(longCount.incrementAndGet());

        // Create the PassaggioEsercizio
        PassaggioEsercizioDTO passaggioEsercizioDTO = passaggioEsercizioMapper.toDto(passaggioEsercizio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassaggioEsercizioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, passaggioEsercizioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(passaggioEsercizioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PassaggioEsercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPassaggioEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passaggioEsercizio.setId(longCount.incrementAndGet());

        // Create the PassaggioEsercizio
        PassaggioEsercizioDTO passaggioEsercizioDTO = passaggioEsercizioMapper.toDto(passaggioEsercizio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassaggioEsercizioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(passaggioEsercizioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PassaggioEsercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPassaggioEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passaggioEsercizio.setId(longCount.incrementAndGet());

        // Create the PassaggioEsercizio
        PassaggioEsercizioDTO passaggioEsercizioDTO = passaggioEsercizioMapper.toDto(passaggioEsercizio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassaggioEsercizioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(passaggioEsercizioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PassaggioEsercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePassaggioEsercizio() throws Exception {
        // Initialize the database
        insertedPassaggioEsercizio = passaggioEsercizioRepository.saveAndFlush(passaggioEsercizio);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the passaggioEsercizio
        restPassaggioEsercizioMockMvc
            .perform(delete(ENTITY_API_URL_ID, passaggioEsercizio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return passaggioEsercizioRepository.count();
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

    protected PassaggioEsercizio getPersistedPassaggioEsercizio(PassaggioEsercizio passaggioEsercizio) {
        return passaggioEsercizioRepository.findById(passaggioEsercizio.getId()).orElseThrow();
    }

    protected void assertPersistedPassaggioEsercizioToMatchAllProperties(PassaggioEsercizio expectedPassaggioEsercizio) {
        assertPassaggioEsercizioAllPropertiesEquals(expectedPassaggioEsercizio, getPersistedPassaggioEsercizio(expectedPassaggioEsercizio));
    }

    protected void assertPersistedPassaggioEsercizioToMatchUpdatableProperties(PassaggioEsercizio expectedPassaggioEsercizio) {
        assertPassaggioEsercizioAllUpdatablePropertiesEquals(
            expectedPassaggioEsercizio,
            getPersistedPassaggioEsercizio(expectedPassaggioEsercizio)
        );
    }
}
