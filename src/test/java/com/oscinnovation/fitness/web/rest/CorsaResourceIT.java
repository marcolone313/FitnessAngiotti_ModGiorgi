package com.oscinnovation.fitness.web.rest;

import static com.oscinnovation.fitness.domain.CorsaAsserts.*;
import static com.oscinnovation.fitness.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscinnovation.fitness.IntegrationTest;
import com.oscinnovation.fitness.domain.AllenamentoGiornaliero;
import com.oscinnovation.fitness.domain.Corsa;
import com.oscinnovation.fitness.repository.CorsaRepository;
import com.oscinnovation.fitness.service.dto.CorsaDTO;
import com.oscinnovation.fitness.service.mapper.CorsaMapper;
import jakarta.persistence.EntityManager;
import java.time.Duration;
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
 * Integration tests for the {@link CorsaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CorsaResourceIT {

    private static final Float DEFAULT_DISTANZA_DA_PERCORRERE = 1F;
    private static final Float UPDATED_DISTANZA_DA_PERCORRERE = 2F;
    private static final Float SMALLER_DISTANZA_DA_PERCORRERE = 1F - 1F;

    private static final Duration DEFAULT_TEMPO_IMPIEGATO = Duration.ofHours(6);
    private static final Duration UPDATED_TEMPO_IMPIEGATO = Duration.ofHours(12);
    private static final Duration SMALLER_TEMPO_IMPIEGATO = Duration.ofHours(5);

    private static final Boolean DEFAULT_SVOLTO = false;
    private static final Boolean UPDATED_SVOLTO = true;

    private static final Boolean DEFAULT_COMPLETATO = false;
    private static final Boolean UPDATED_COMPLETATO = true;

    private static final String DEFAULT_FEEDBACK = "AAAAAAAAAA";
    private static final String UPDATED_FEEDBACK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/corsas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CorsaRepository corsaRepository;

    @Autowired
    private CorsaMapper corsaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCorsaMockMvc;

    private Corsa corsa;

    private Corsa insertedCorsa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Corsa createEntity(EntityManager em) {
        Corsa corsa = new Corsa()
            .distanzaDaPercorrere(DEFAULT_DISTANZA_DA_PERCORRERE)
            .tempoImpiegato(DEFAULT_TEMPO_IMPIEGATO)
            .svolto(DEFAULT_SVOLTO)
            .completato(DEFAULT_COMPLETATO)
            .feedback(DEFAULT_FEEDBACK);
        // Add required entity
        AllenamentoGiornaliero allenamentoGiornaliero;
        if (TestUtil.findAll(em, AllenamentoGiornaliero.class).isEmpty()) {
            allenamentoGiornaliero = AllenamentoGiornalieroResourceIT.createEntity(em);
            em.persist(allenamentoGiornaliero);
            em.flush();
        } else {
            allenamentoGiornaliero = TestUtil.findAll(em, AllenamentoGiornaliero.class).get(0);
        }
        corsa.setAllenamentoGiornaliero(allenamentoGiornaliero);
        return corsa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Corsa createUpdatedEntity(EntityManager em) {
        Corsa updatedCorsa = new Corsa()
            .distanzaDaPercorrere(UPDATED_DISTANZA_DA_PERCORRERE)
            .tempoImpiegato(UPDATED_TEMPO_IMPIEGATO)
            .svolto(UPDATED_SVOLTO)
            .completato(UPDATED_COMPLETATO)
            .feedback(UPDATED_FEEDBACK);
        // Add required entity
        AllenamentoGiornaliero allenamentoGiornaliero;
        if (TestUtil.findAll(em, AllenamentoGiornaliero.class).isEmpty()) {
            allenamentoGiornaliero = AllenamentoGiornalieroResourceIT.createUpdatedEntity(em);
            em.persist(allenamentoGiornaliero);
            em.flush();
        } else {
            allenamentoGiornaliero = TestUtil.findAll(em, AllenamentoGiornaliero.class).get(0);
        }
        updatedCorsa.setAllenamentoGiornaliero(allenamentoGiornaliero);
        return updatedCorsa;
    }

    @BeforeEach
    public void initTest() {
        corsa = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCorsa != null) {
            corsaRepository.delete(insertedCorsa);
            insertedCorsa = null;
        }
    }

    @Test
    @Transactional
    void createCorsa() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Corsa
        CorsaDTO corsaDTO = corsaMapper.toDto(corsa);
        var returnedCorsaDTO = om.readValue(
            restCorsaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(corsaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CorsaDTO.class
        );

        // Validate the Corsa in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCorsa = corsaMapper.toEntity(returnedCorsaDTO);
        assertCorsaUpdatableFieldsEquals(returnedCorsa, getPersistedCorsa(returnedCorsa));

        insertedCorsa = returnedCorsa;
    }

    @Test
    @Transactional
    void createCorsaWithExistingId() throws Exception {
        // Create the Corsa with an existing ID
        corsa.setId(1L);
        CorsaDTO corsaDTO = corsaMapper.toDto(corsa);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorsaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(corsaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Corsa in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDistanzaDaPercorrereIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        corsa.setDistanzaDaPercorrere(null);

        // Create the Corsa, which fails.
        CorsaDTO corsaDTO = corsaMapper.toDto(corsa);

        restCorsaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(corsaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCorsas() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList
        restCorsaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corsa.getId().intValue())))
            .andExpect(jsonPath("$.[*].distanzaDaPercorrere").value(hasItem(DEFAULT_DISTANZA_DA_PERCORRERE.doubleValue())))
            .andExpect(jsonPath("$.[*].tempoImpiegato").value(hasItem(DEFAULT_TEMPO_IMPIEGATO.toString())))
            .andExpect(jsonPath("$.[*].svolto").value(hasItem(DEFAULT_SVOLTO.booleanValue())))
            .andExpect(jsonPath("$.[*].completato").value(hasItem(DEFAULT_COMPLETATO.booleanValue())))
            .andExpect(jsonPath("$.[*].feedback").value(hasItem(DEFAULT_FEEDBACK.toString())));
    }

    @Test
    @Transactional
    void getCorsa() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get the corsa
        restCorsaMockMvc
            .perform(get(ENTITY_API_URL_ID, corsa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(corsa.getId().intValue()))
            .andExpect(jsonPath("$.distanzaDaPercorrere").value(DEFAULT_DISTANZA_DA_PERCORRERE.doubleValue()))
            .andExpect(jsonPath("$.tempoImpiegato").value(DEFAULT_TEMPO_IMPIEGATO.toString()))
            .andExpect(jsonPath("$.svolto").value(DEFAULT_SVOLTO.booleanValue()))
            .andExpect(jsonPath("$.completato").value(DEFAULT_COMPLETATO.booleanValue()))
            .andExpect(jsonPath("$.feedback").value(DEFAULT_FEEDBACK.toString()));
    }

    @Test
    @Transactional
    void getCorsasByIdFiltering() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        Long id = corsa.getId();

        defaultCorsaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCorsaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCorsaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCorsasByDistanzaDaPercorrereIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where distanzaDaPercorrere equals to
        defaultCorsaFiltering(
            "distanzaDaPercorrere.equals=" + DEFAULT_DISTANZA_DA_PERCORRERE,
            "distanzaDaPercorrere.equals=" + UPDATED_DISTANZA_DA_PERCORRERE
        );
    }

    @Test
    @Transactional
    void getAllCorsasByDistanzaDaPercorrereIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where distanzaDaPercorrere in
        defaultCorsaFiltering(
            "distanzaDaPercorrere.in=" + DEFAULT_DISTANZA_DA_PERCORRERE + "," + UPDATED_DISTANZA_DA_PERCORRERE,
            "distanzaDaPercorrere.in=" + UPDATED_DISTANZA_DA_PERCORRERE
        );
    }

    @Test
    @Transactional
    void getAllCorsasByDistanzaDaPercorrereIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where distanzaDaPercorrere is not null
        defaultCorsaFiltering("distanzaDaPercorrere.specified=true", "distanzaDaPercorrere.specified=false");
    }

    @Test
    @Transactional
    void getAllCorsasByDistanzaDaPercorrereIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where distanzaDaPercorrere is greater than or equal to
        defaultCorsaFiltering(
            "distanzaDaPercorrere.greaterThanOrEqual=" + DEFAULT_DISTANZA_DA_PERCORRERE,
            "distanzaDaPercorrere.greaterThanOrEqual=" + UPDATED_DISTANZA_DA_PERCORRERE
        );
    }

    @Test
    @Transactional
    void getAllCorsasByDistanzaDaPercorrereIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where distanzaDaPercorrere is less than or equal to
        defaultCorsaFiltering(
            "distanzaDaPercorrere.lessThanOrEqual=" + DEFAULT_DISTANZA_DA_PERCORRERE,
            "distanzaDaPercorrere.lessThanOrEqual=" + SMALLER_DISTANZA_DA_PERCORRERE
        );
    }

    @Test
    @Transactional
    void getAllCorsasByDistanzaDaPercorrereIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where distanzaDaPercorrere is less than
        defaultCorsaFiltering(
            "distanzaDaPercorrere.lessThan=" + UPDATED_DISTANZA_DA_PERCORRERE,
            "distanzaDaPercorrere.lessThan=" + DEFAULT_DISTANZA_DA_PERCORRERE
        );
    }

    @Test
    @Transactional
    void getAllCorsasByDistanzaDaPercorrereIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where distanzaDaPercorrere is greater than
        defaultCorsaFiltering(
            "distanzaDaPercorrere.greaterThan=" + SMALLER_DISTANZA_DA_PERCORRERE,
            "distanzaDaPercorrere.greaterThan=" + DEFAULT_DISTANZA_DA_PERCORRERE
        );
    }

    @Test
    @Transactional
    void getAllCorsasByTempoImpiegatoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where tempoImpiegato equals to
        defaultCorsaFiltering("tempoImpiegato.equals=" + DEFAULT_TEMPO_IMPIEGATO, "tempoImpiegato.equals=" + UPDATED_TEMPO_IMPIEGATO);
    }

    @Test
    @Transactional
    void getAllCorsasByTempoImpiegatoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where tempoImpiegato in
        defaultCorsaFiltering(
            "tempoImpiegato.in=" + DEFAULT_TEMPO_IMPIEGATO + "," + UPDATED_TEMPO_IMPIEGATO,
            "tempoImpiegato.in=" + UPDATED_TEMPO_IMPIEGATO
        );
    }

    @Test
    @Transactional
    void getAllCorsasByTempoImpiegatoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where tempoImpiegato is not null
        defaultCorsaFiltering("tempoImpiegato.specified=true", "tempoImpiegato.specified=false");
    }

    @Test
    @Transactional
    void getAllCorsasByTempoImpiegatoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where tempoImpiegato is greater than or equal to
        defaultCorsaFiltering(
            "tempoImpiegato.greaterThanOrEqual=" + DEFAULT_TEMPO_IMPIEGATO,
            "tempoImpiegato.greaterThanOrEqual=" + UPDATED_TEMPO_IMPIEGATO
        );
    }

    @Test
    @Transactional
    void getAllCorsasByTempoImpiegatoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where tempoImpiegato is less than or equal to
        defaultCorsaFiltering(
            "tempoImpiegato.lessThanOrEqual=" + DEFAULT_TEMPO_IMPIEGATO,
            "tempoImpiegato.lessThanOrEqual=" + SMALLER_TEMPO_IMPIEGATO
        );
    }

    @Test
    @Transactional
    void getAllCorsasByTempoImpiegatoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where tempoImpiegato is less than
        defaultCorsaFiltering("tempoImpiegato.lessThan=" + UPDATED_TEMPO_IMPIEGATO, "tempoImpiegato.lessThan=" + DEFAULT_TEMPO_IMPIEGATO);
    }

    @Test
    @Transactional
    void getAllCorsasByTempoImpiegatoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where tempoImpiegato is greater than
        defaultCorsaFiltering(
            "tempoImpiegato.greaterThan=" + SMALLER_TEMPO_IMPIEGATO,
            "tempoImpiegato.greaterThan=" + DEFAULT_TEMPO_IMPIEGATO
        );
    }

    @Test
    @Transactional
    void getAllCorsasBySvoltoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where svolto equals to
        defaultCorsaFiltering("svolto.equals=" + DEFAULT_SVOLTO, "svolto.equals=" + UPDATED_SVOLTO);
    }

    @Test
    @Transactional
    void getAllCorsasBySvoltoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where svolto in
        defaultCorsaFiltering("svolto.in=" + DEFAULT_SVOLTO + "," + UPDATED_SVOLTO, "svolto.in=" + UPDATED_SVOLTO);
    }

    @Test
    @Transactional
    void getAllCorsasBySvoltoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where svolto is not null
        defaultCorsaFiltering("svolto.specified=true", "svolto.specified=false");
    }

    @Test
    @Transactional
    void getAllCorsasByCompletatoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where completato equals to
        defaultCorsaFiltering("completato.equals=" + DEFAULT_COMPLETATO, "completato.equals=" + UPDATED_COMPLETATO);
    }

    @Test
    @Transactional
    void getAllCorsasByCompletatoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where completato in
        defaultCorsaFiltering("completato.in=" + DEFAULT_COMPLETATO + "," + UPDATED_COMPLETATO, "completato.in=" + UPDATED_COMPLETATO);
    }

    @Test
    @Transactional
    void getAllCorsasByCompletatoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        // Get all the corsaList where completato is not null
        defaultCorsaFiltering("completato.specified=true", "completato.specified=false");
    }

    @Test
    @Transactional
    void getAllCorsasByAllenamentoGiornalieroIsEqualToSomething() throws Exception {
        // Get already existing entity
        AllenamentoGiornaliero allenamentoGiornaliero = corsa.getAllenamentoGiornaliero();
        corsaRepository.saveAndFlush(corsa);
        Long allenamentoGiornalieroId = allenamentoGiornaliero.getId();
        // Get all the corsaList where allenamentoGiornaliero equals to allenamentoGiornalieroId
        defaultCorsaShouldBeFound("allenamentoGiornalieroId.equals=" + allenamentoGiornalieroId);

        // Get all the corsaList where allenamentoGiornaliero equals to (allenamentoGiornalieroId + 1)
        defaultCorsaShouldNotBeFound("allenamentoGiornalieroId.equals=" + (allenamentoGiornalieroId + 1));
    }

    private void defaultCorsaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCorsaShouldBeFound(shouldBeFound);
        defaultCorsaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCorsaShouldBeFound(String filter) throws Exception {
        restCorsaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corsa.getId().intValue())))
            .andExpect(jsonPath("$.[*].distanzaDaPercorrere").value(hasItem(DEFAULT_DISTANZA_DA_PERCORRERE.doubleValue())))
            .andExpect(jsonPath("$.[*].tempoImpiegato").value(hasItem(DEFAULT_TEMPO_IMPIEGATO.toString())))
            .andExpect(jsonPath("$.[*].svolto").value(hasItem(DEFAULT_SVOLTO.booleanValue())))
            .andExpect(jsonPath("$.[*].completato").value(hasItem(DEFAULT_COMPLETATO.booleanValue())))
            .andExpect(jsonPath("$.[*].feedback").value(hasItem(DEFAULT_FEEDBACK.toString())));

        // Check, that the count call also returns 1
        restCorsaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCorsaShouldNotBeFound(String filter) throws Exception {
        restCorsaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCorsaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCorsa() throws Exception {
        // Get the corsa
        restCorsaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCorsa() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the corsa
        Corsa updatedCorsa = corsaRepository.findById(corsa.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCorsa are not directly saved in db
        em.detach(updatedCorsa);
        updatedCorsa
            .distanzaDaPercorrere(UPDATED_DISTANZA_DA_PERCORRERE)
            .tempoImpiegato(UPDATED_TEMPO_IMPIEGATO)
            .svolto(UPDATED_SVOLTO)
            .completato(UPDATED_COMPLETATO)
            .feedback(UPDATED_FEEDBACK);
        CorsaDTO corsaDTO = corsaMapper.toDto(updatedCorsa);

        restCorsaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, corsaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(corsaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Corsa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCorsaToMatchAllProperties(updatedCorsa);
    }

    @Test
    @Transactional
    void putNonExistingCorsa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corsa.setId(longCount.incrementAndGet());

        // Create the Corsa
        CorsaDTO corsaDTO = corsaMapper.toDto(corsa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorsaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, corsaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(corsaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corsa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCorsa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corsa.setId(longCount.incrementAndGet());

        // Create the Corsa
        CorsaDTO corsaDTO = corsaMapper.toDto(corsa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorsaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(corsaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corsa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCorsa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corsa.setId(longCount.incrementAndGet());

        // Create the Corsa
        CorsaDTO corsaDTO = corsaMapper.toDto(corsa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorsaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(corsaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Corsa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCorsaWithPatch() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the corsa using partial update
        Corsa partialUpdatedCorsa = new Corsa();
        partialUpdatedCorsa.setId(corsa.getId());

        partialUpdatedCorsa.tempoImpiegato(UPDATED_TEMPO_IMPIEGATO);

        restCorsaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorsa.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCorsa))
            )
            .andExpect(status().isOk());

        // Validate the Corsa in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCorsaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCorsa, corsa), getPersistedCorsa(corsa));
    }

    @Test
    @Transactional
    void fullUpdateCorsaWithPatch() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the corsa using partial update
        Corsa partialUpdatedCorsa = new Corsa();
        partialUpdatedCorsa.setId(corsa.getId());

        partialUpdatedCorsa
            .distanzaDaPercorrere(UPDATED_DISTANZA_DA_PERCORRERE)
            .tempoImpiegato(UPDATED_TEMPO_IMPIEGATO)
            .svolto(UPDATED_SVOLTO)
            .completato(UPDATED_COMPLETATO)
            .feedback(UPDATED_FEEDBACK);

        restCorsaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorsa.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCorsa))
            )
            .andExpect(status().isOk());

        // Validate the Corsa in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCorsaUpdatableFieldsEquals(partialUpdatedCorsa, getPersistedCorsa(partialUpdatedCorsa));
    }

    @Test
    @Transactional
    void patchNonExistingCorsa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corsa.setId(longCount.incrementAndGet());

        // Create the Corsa
        CorsaDTO corsaDTO = corsaMapper.toDto(corsa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorsaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, corsaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(corsaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corsa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCorsa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corsa.setId(longCount.incrementAndGet());

        // Create the Corsa
        CorsaDTO corsaDTO = corsaMapper.toDto(corsa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorsaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(corsaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corsa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCorsa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corsa.setId(longCount.incrementAndGet());

        // Create the Corsa
        CorsaDTO corsaDTO = corsaMapper.toDto(corsa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorsaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(corsaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Corsa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCorsa() throws Exception {
        // Initialize the database
        insertedCorsa = corsaRepository.saveAndFlush(corsa);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the corsa
        restCorsaMockMvc
            .perform(delete(ENTITY_API_URL_ID, corsa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return corsaRepository.count();
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

    protected Corsa getPersistedCorsa(Corsa corsa) {
        return corsaRepository.findById(corsa.getId()).orElseThrow();
    }

    protected void assertPersistedCorsaToMatchAllProperties(Corsa expectedCorsa) {
        assertCorsaAllPropertiesEquals(expectedCorsa, getPersistedCorsa(expectedCorsa));
    }

    protected void assertPersistedCorsaToMatchUpdatableProperties(Corsa expectedCorsa) {
        assertCorsaAllUpdatablePropertiesEquals(expectedCorsa, getPersistedCorsa(expectedCorsa));
    }
}
