package com.oscinnovation.fitness.web.rest;

import static com.oscinnovation.fitness.domain.CircuitoToEsercizioAsserts.*;
import static com.oscinnovation.fitness.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscinnovation.fitness.IntegrationTest;
import com.oscinnovation.fitness.domain.Circuito;
import com.oscinnovation.fitness.domain.CircuitoToEsercizio;
import com.oscinnovation.fitness.domain.Esercizio;
import com.oscinnovation.fitness.repository.CircuitoToEsercizioRepository;
import com.oscinnovation.fitness.service.CircuitoToEsercizioService;
import com.oscinnovation.fitness.service.dto.CircuitoToEsercizioDTO;
import com.oscinnovation.fitness.service.mapper.CircuitoToEsercizioMapper;
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
 * Integration tests for the {@link CircuitoToEsercizioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CircuitoToEsercizioResourceIT {

    private static final Integer DEFAULT_REPS = 1;
    private static final Integer UPDATED_REPS = 2;
    private static final Integer SMALLER_REPS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/circuito-to-esercizios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CircuitoToEsercizioRepository circuitoToEsercizioRepository;

    @Mock
    private CircuitoToEsercizioRepository circuitoToEsercizioRepositoryMock;

    @Autowired
    private CircuitoToEsercizioMapper circuitoToEsercizioMapper;

    @Mock
    private CircuitoToEsercizioService circuitoToEsercizioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCircuitoToEsercizioMockMvc;

    private CircuitoToEsercizio circuitoToEsercizio;

    private CircuitoToEsercizio insertedCircuitoToEsercizio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CircuitoToEsercizio createEntity(EntityManager em) {
        CircuitoToEsercizio circuitoToEsercizio = new CircuitoToEsercizio().reps(DEFAULT_REPS);
        // Add required entity
        Esercizio esercizio;
        if (TestUtil.findAll(em, Esercizio.class).isEmpty()) {
            esercizio = EsercizioResourceIT.createEntity();
            em.persist(esercizio);
            em.flush();
        } else {
            esercizio = TestUtil.findAll(em, Esercizio.class).get(0);
        }
        circuitoToEsercizio.setEsercizio(esercizio);
        // Add required entity
        Circuito circuito;
        if (TestUtil.findAll(em, Circuito.class).isEmpty()) {
            circuito = CircuitoResourceIT.createEntity(em);
            em.persist(circuito);
            em.flush();
        } else {
            circuito = TestUtil.findAll(em, Circuito.class).get(0);
        }
        circuitoToEsercizio.setCircuito(circuito);
        return circuitoToEsercizio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CircuitoToEsercizio createUpdatedEntity(EntityManager em) {
        CircuitoToEsercizio updatedCircuitoToEsercizio = new CircuitoToEsercizio().reps(UPDATED_REPS);
        // Add required entity
        Esercizio esercizio;
        if (TestUtil.findAll(em, Esercizio.class).isEmpty()) {
            esercizio = EsercizioResourceIT.createUpdatedEntity();
            em.persist(esercizio);
            em.flush();
        } else {
            esercizio = TestUtil.findAll(em, Esercizio.class).get(0);
        }
        updatedCircuitoToEsercizio.setEsercizio(esercizio);
        // Add required entity
        Circuito circuito;
        if (TestUtil.findAll(em, Circuito.class).isEmpty()) {
            circuito = CircuitoResourceIT.createUpdatedEntity(em);
            em.persist(circuito);
            em.flush();
        } else {
            circuito = TestUtil.findAll(em, Circuito.class).get(0);
        }
        updatedCircuitoToEsercizio.setCircuito(circuito);
        return updatedCircuitoToEsercizio;
    }

    @BeforeEach
    public void initTest() {
        circuitoToEsercizio = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCircuitoToEsercizio != null) {
            circuitoToEsercizioRepository.delete(insertedCircuitoToEsercizio);
            insertedCircuitoToEsercizio = null;
        }
    }

    @Test
    @Transactional
    void createCircuitoToEsercizio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CircuitoToEsercizio
        CircuitoToEsercizioDTO circuitoToEsercizioDTO = circuitoToEsercizioMapper.toDto(circuitoToEsercizio);
        var returnedCircuitoToEsercizioDTO = om.readValue(
            restCircuitoToEsercizioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(circuitoToEsercizioDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CircuitoToEsercizioDTO.class
        );

        // Validate the CircuitoToEsercizio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCircuitoToEsercizio = circuitoToEsercizioMapper.toEntity(returnedCircuitoToEsercizioDTO);
        assertCircuitoToEsercizioUpdatableFieldsEquals(
            returnedCircuitoToEsercizio,
            getPersistedCircuitoToEsercizio(returnedCircuitoToEsercizio)
        );

        insertedCircuitoToEsercizio = returnedCircuitoToEsercizio;
    }

    @Test
    @Transactional
    void createCircuitoToEsercizioWithExistingId() throws Exception {
        // Create the CircuitoToEsercizio with an existing ID
        circuitoToEsercizio.setId(1L);
        CircuitoToEsercizioDTO circuitoToEsercizioDTO = circuitoToEsercizioMapper.toDto(circuitoToEsercizio);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCircuitoToEsercizioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(circuitoToEsercizioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CircuitoToEsercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRepsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        circuitoToEsercizio.setReps(null);

        // Create the CircuitoToEsercizio, which fails.
        CircuitoToEsercizioDTO circuitoToEsercizioDTO = circuitoToEsercizioMapper.toDto(circuitoToEsercizio);

        restCircuitoToEsercizioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(circuitoToEsercizioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCircuitoToEsercizios() throws Exception {
        // Initialize the database
        insertedCircuitoToEsercizio = circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);

        // Get all the circuitoToEsercizioList
        restCircuitoToEsercizioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(circuitoToEsercizio.getId().intValue())))
            .andExpect(jsonPath("$.[*].reps").value(hasItem(DEFAULT_REPS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCircuitoToEserciziosWithEagerRelationshipsIsEnabled() throws Exception {
        when(circuitoToEsercizioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCircuitoToEsercizioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(circuitoToEsercizioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCircuitoToEserciziosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(circuitoToEsercizioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCircuitoToEsercizioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(circuitoToEsercizioRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCircuitoToEsercizio() throws Exception {
        // Initialize the database
        insertedCircuitoToEsercizio = circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);

        // Get the circuitoToEsercizio
        restCircuitoToEsercizioMockMvc
            .perform(get(ENTITY_API_URL_ID, circuitoToEsercizio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(circuitoToEsercizio.getId().intValue()))
            .andExpect(jsonPath("$.reps").value(DEFAULT_REPS));
    }

    @Test
    @Transactional
    void getCircuitoToEserciziosByIdFiltering() throws Exception {
        // Initialize the database
        insertedCircuitoToEsercizio = circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);

        Long id = circuitoToEsercizio.getId();

        defaultCircuitoToEsercizioFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCircuitoToEsercizioFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCircuitoToEsercizioFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCircuitoToEserciziosByRepsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCircuitoToEsercizio = circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);

        // Get all the circuitoToEsercizioList where reps equals to
        defaultCircuitoToEsercizioFiltering("reps.equals=" + DEFAULT_REPS, "reps.equals=" + UPDATED_REPS);
    }

    @Test
    @Transactional
    void getAllCircuitoToEserciziosByRepsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCircuitoToEsercizio = circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);

        // Get all the circuitoToEsercizioList where reps in
        defaultCircuitoToEsercizioFiltering("reps.in=" + DEFAULT_REPS + "," + UPDATED_REPS, "reps.in=" + UPDATED_REPS);
    }

    @Test
    @Transactional
    void getAllCircuitoToEserciziosByRepsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCircuitoToEsercizio = circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);

        // Get all the circuitoToEsercizioList where reps is not null
        defaultCircuitoToEsercizioFiltering("reps.specified=true", "reps.specified=false");
    }

    @Test
    @Transactional
    void getAllCircuitoToEserciziosByRepsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCircuitoToEsercizio = circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);

        // Get all the circuitoToEsercizioList where reps is greater than or equal to
        defaultCircuitoToEsercizioFiltering("reps.greaterThanOrEqual=" + DEFAULT_REPS, "reps.greaterThanOrEqual=" + UPDATED_REPS);
    }

    @Test
    @Transactional
    void getAllCircuitoToEserciziosByRepsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCircuitoToEsercizio = circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);

        // Get all the circuitoToEsercizioList where reps is less than or equal to
        defaultCircuitoToEsercizioFiltering("reps.lessThanOrEqual=" + DEFAULT_REPS, "reps.lessThanOrEqual=" + SMALLER_REPS);
    }

    @Test
    @Transactional
    void getAllCircuitoToEserciziosByRepsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCircuitoToEsercizio = circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);

        // Get all the circuitoToEsercizioList where reps is less than
        defaultCircuitoToEsercizioFiltering("reps.lessThan=" + UPDATED_REPS, "reps.lessThan=" + DEFAULT_REPS);
    }

    @Test
    @Transactional
    void getAllCircuitoToEserciziosByRepsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCircuitoToEsercizio = circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);

        // Get all the circuitoToEsercizioList where reps is greater than
        defaultCircuitoToEsercizioFiltering("reps.greaterThan=" + SMALLER_REPS, "reps.greaterThan=" + DEFAULT_REPS);
    }

    @Test
    @Transactional
    void getAllCircuitoToEserciziosByEsercizioIsEqualToSomething() throws Exception {
        Esercizio esercizio;
        if (TestUtil.findAll(em, Esercizio.class).isEmpty()) {
            circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);
            esercizio = EsercizioResourceIT.createEntity();
        } else {
            esercizio = TestUtil.findAll(em, Esercizio.class).get(0);
        }
        em.persist(esercizio);
        em.flush();
        circuitoToEsercizio.setEsercizio(esercizio);
        circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);
        Long esercizioId = esercizio.getId();
        // Get all the circuitoToEsercizioList where esercizio equals to esercizioId
        defaultCircuitoToEsercizioShouldBeFound("esercizioId.equals=" + esercizioId);

        // Get all the circuitoToEsercizioList where esercizio equals to (esercizioId + 1)
        defaultCircuitoToEsercizioShouldNotBeFound("esercizioId.equals=" + (esercizioId + 1));
    }

    @Test
    @Transactional
    void getAllCircuitoToEserciziosByCircuitoIsEqualToSomething() throws Exception {
        Circuito circuito;
        if (TestUtil.findAll(em, Circuito.class).isEmpty()) {
            circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);
            circuito = CircuitoResourceIT.createEntity(em);
        } else {
            circuito = TestUtil.findAll(em, Circuito.class).get(0);
        }
        em.persist(circuito);
        em.flush();
        circuitoToEsercizio.setCircuito(circuito);
        circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);
        Long circuitoId = circuito.getId();
        // Get all the circuitoToEsercizioList where circuito equals to circuitoId
        defaultCircuitoToEsercizioShouldBeFound("circuitoId.equals=" + circuitoId);

        // Get all the circuitoToEsercizioList where circuito equals to (circuitoId + 1)
        defaultCircuitoToEsercizioShouldNotBeFound("circuitoId.equals=" + (circuitoId + 1));
    }

    private void defaultCircuitoToEsercizioFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCircuitoToEsercizioShouldBeFound(shouldBeFound);
        defaultCircuitoToEsercizioShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCircuitoToEsercizioShouldBeFound(String filter) throws Exception {
        restCircuitoToEsercizioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(circuitoToEsercizio.getId().intValue())))
            .andExpect(jsonPath("$.[*].reps").value(hasItem(DEFAULT_REPS)));

        // Check, that the count call also returns 1
        restCircuitoToEsercizioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCircuitoToEsercizioShouldNotBeFound(String filter) throws Exception {
        restCircuitoToEsercizioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCircuitoToEsercizioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCircuitoToEsercizio() throws Exception {
        // Get the circuitoToEsercizio
        restCircuitoToEsercizioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCircuitoToEsercizio() throws Exception {
        // Initialize the database
        insertedCircuitoToEsercizio = circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the circuitoToEsercizio
        CircuitoToEsercizio updatedCircuitoToEsercizio = circuitoToEsercizioRepository.findById(circuitoToEsercizio.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCircuitoToEsercizio are not directly saved in db
        em.detach(updatedCircuitoToEsercizio);
        updatedCircuitoToEsercizio.reps(UPDATED_REPS);
        CircuitoToEsercizioDTO circuitoToEsercizioDTO = circuitoToEsercizioMapper.toDto(updatedCircuitoToEsercizio);

        restCircuitoToEsercizioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, circuitoToEsercizioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(circuitoToEsercizioDTO))
            )
            .andExpect(status().isOk());

        // Validate the CircuitoToEsercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCircuitoToEsercizioToMatchAllProperties(updatedCircuitoToEsercizio);
    }

    @Test
    @Transactional
    void putNonExistingCircuitoToEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circuitoToEsercizio.setId(longCount.incrementAndGet());

        // Create the CircuitoToEsercizio
        CircuitoToEsercizioDTO circuitoToEsercizioDTO = circuitoToEsercizioMapper.toDto(circuitoToEsercizio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCircuitoToEsercizioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, circuitoToEsercizioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(circuitoToEsercizioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CircuitoToEsercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCircuitoToEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circuitoToEsercizio.setId(longCount.incrementAndGet());

        // Create the CircuitoToEsercizio
        CircuitoToEsercizioDTO circuitoToEsercizioDTO = circuitoToEsercizioMapper.toDto(circuitoToEsercizio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCircuitoToEsercizioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(circuitoToEsercizioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CircuitoToEsercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCircuitoToEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circuitoToEsercizio.setId(longCount.incrementAndGet());

        // Create the CircuitoToEsercizio
        CircuitoToEsercizioDTO circuitoToEsercizioDTO = circuitoToEsercizioMapper.toDto(circuitoToEsercizio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCircuitoToEsercizioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(circuitoToEsercizioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CircuitoToEsercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCircuitoToEsercizioWithPatch() throws Exception {
        // Initialize the database
        insertedCircuitoToEsercizio = circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the circuitoToEsercizio using partial update
        CircuitoToEsercizio partialUpdatedCircuitoToEsercizio = new CircuitoToEsercizio();
        partialUpdatedCircuitoToEsercizio.setId(circuitoToEsercizio.getId());

        partialUpdatedCircuitoToEsercizio.reps(UPDATED_REPS);

        restCircuitoToEsercizioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCircuitoToEsercizio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCircuitoToEsercizio))
            )
            .andExpect(status().isOk());

        // Validate the CircuitoToEsercizio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCircuitoToEsercizioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCircuitoToEsercizio, circuitoToEsercizio),
            getPersistedCircuitoToEsercizio(circuitoToEsercizio)
        );
    }

    @Test
    @Transactional
    void fullUpdateCircuitoToEsercizioWithPatch() throws Exception {
        // Initialize the database
        insertedCircuitoToEsercizio = circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the circuitoToEsercizio using partial update
        CircuitoToEsercizio partialUpdatedCircuitoToEsercizio = new CircuitoToEsercizio();
        partialUpdatedCircuitoToEsercizio.setId(circuitoToEsercizio.getId());

        partialUpdatedCircuitoToEsercizio.reps(UPDATED_REPS);

        restCircuitoToEsercizioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCircuitoToEsercizio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCircuitoToEsercizio))
            )
            .andExpect(status().isOk());

        // Validate the CircuitoToEsercizio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCircuitoToEsercizioUpdatableFieldsEquals(
            partialUpdatedCircuitoToEsercizio,
            getPersistedCircuitoToEsercizio(partialUpdatedCircuitoToEsercizio)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCircuitoToEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circuitoToEsercizio.setId(longCount.incrementAndGet());

        // Create the CircuitoToEsercizio
        CircuitoToEsercizioDTO circuitoToEsercizioDTO = circuitoToEsercizioMapper.toDto(circuitoToEsercizio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCircuitoToEsercizioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, circuitoToEsercizioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(circuitoToEsercizioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CircuitoToEsercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCircuitoToEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circuitoToEsercizio.setId(longCount.incrementAndGet());

        // Create the CircuitoToEsercizio
        CircuitoToEsercizioDTO circuitoToEsercizioDTO = circuitoToEsercizioMapper.toDto(circuitoToEsercizio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCircuitoToEsercizioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(circuitoToEsercizioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CircuitoToEsercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCircuitoToEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circuitoToEsercizio.setId(longCount.incrementAndGet());

        // Create the CircuitoToEsercizio
        CircuitoToEsercizioDTO circuitoToEsercizioDTO = circuitoToEsercizioMapper.toDto(circuitoToEsercizio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCircuitoToEsercizioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(circuitoToEsercizioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CircuitoToEsercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCircuitoToEsercizio() throws Exception {
        // Initialize the database
        insertedCircuitoToEsercizio = circuitoToEsercizioRepository.saveAndFlush(circuitoToEsercizio);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the circuitoToEsercizio
        restCircuitoToEsercizioMockMvc
            .perform(delete(ENTITY_API_URL_ID, circuitoToEsercizio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return circuitoToEsercizioRepository.count();
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

    protected CircuitoToEsercizio getPersistedCircuitoToEsercizio(CircuitoToEsercizio circuitoToEsercizio) {
        return circuitoToEsercizioRepository.findById(circuitoToEsercizio.getId()).orElseThrow();
    }

    protected void assertPersistedCircuitoToEsercizioToMatchAllProperties(CircuitoToEsercizio expectedCircuitoToEsercizio) {
        assertCircuitoToEsercizioAllPropertiesEquals(
            expectedCircuitoToEsercizio,
            getPersistedCircuitoToEsercizio(expectedCircuitoToEsercizio)
        );
    }

    protected void assertPersistedCircuitoToEsercizioToMatchUpdatableProperties(CircuitoToEsercizio expectedCircuitoToEsercizio) {
        assertCircuitoToEsercizioAllUpdatablePropertiesEquals(
            expectedCircuitoToEsercizio,
            getPersistedCircuitoToEsercizio(expectedCircuitoToEsercizio)
        );
    }
}
