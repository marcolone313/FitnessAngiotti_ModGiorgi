package com.oscinnovation.fitness.web.rest;

import static com.oscinnovation.fitness.domain.CircuitoAsserts.*;
import static com.oscinnovation.fitness.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscinnovation.fitness.IntegrationTest;
import com.oscinnovation.fitness.domain.AllenamentoGiornaliero;
import com.oscinnovation.fitness.domain.Circuito;
import com.oscinnovation.fitness.repository.CircuitoRepository;
import com.oscinnovation.fitness.service.dto.CircuitoDTO;
import com.oscinnovation.fitness.service.mapper.CircuitoMapper;
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
 * Integration tests for the {@link CircuitoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CircuitoResourceIT {

    private static final Duration DEFAULT_TEMPO_LIMITE = Duration.ofHours(6);
    private static final Duration UPDATED_TEMPO_LIMITE = Duration.ofHours(12);
    private static final Duration SMALLER_TEMPO_LIMITE = Duration.ofHours(5);

    private static final Duration DEFAULT_TEMPO_IMPIEGATO = Duration.ofHours(6);
    private static final Duration UPDATED_TEMPO_IMPIEGATO = Duration.ofHours(12);
    private static final Duration SMALLER_TEMPO_IMPIEGATO = Duration.ofHours(5);

    private static final String DEFAULT_CATENA_RIPETIZIONI = "AAAAAAAAAA";
    private static final String UPDATED_CATENA_RIPETIZIONI = "BBBBBBBBBB";

    private static final Integer DEFAULT_CIRCUITI_COMPLETATI = 1;
    private static final Integer UPDATED_CIRCUITI_COMPLETATI = 2;
    private static final Integer SMALLER_CIRCUITI_COMPLETATI = 1 - 1;

    private static final Boolean DEFAULT_SVOLTO = false;
    private static final Boolean UPDATED_SVOLTO = true;

    private static final Boolean DEFAULT_COMPLETATO = false;
    private static final Boolean UPDATED_COMPLETATO = true;

    private static final String DEFAULT_FEEDBACK = "AAAAAAAAAA";
    private static final String UPDATED_FEEDBACK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/circuitos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CircuitoRepository circuitoRepository;

    @Autowired
    private CircuitoMapper circuitoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCircuitoMockMvc;

    private Circuito circuito;

    private Circuito insertedCircuito;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Circuito createEntity(EntityManager em) {
        Circuito circuito = new Circuito()
            .tempoLimite(DEFAULT_TEMPO_LIMITE)
            .tempoImpiegato(DEFAULT_TEMPO_IMPIEGATO)
            .catenaRipetizioni(DEFAULT_CATENA_RIPETIZIONI)
            .circuitiCompletati(DEFAULT_CIRCUITI_COMPLETATI)
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
        circuito.setAllenamentoGiornaliero(allenamentoGiornaliero);
        return circuito;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Circuito createUpdatedEntity(EntityManager em) {
        Circuito updatedCircuito = new Circuito()
            .tempoLimite(UPDATED_TEMPO_LIMITE)
            .tempoImpiegato(UPDATED_TEMPO_IMPIEGATO)
            .catenaRipetizioni(UPDATED_CATENA_RIPETIZIONI)
            .circuitiCompletati(UPDATED_CIRCUITI_COMPLETATI)
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
        updatedCircuito.setAllenamentoGiornaliero(allenamentoGiornaliero);
        return updatedCircuito;
    }

    @BeforeEach
    public void initTest() {
        circuito = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCircuito != null) {
            circuitoRepository.delete(insertedCircuito);
            insertedCircuito = null;
        }
    }

    @Test
    @Transactional
    void createCircuito() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Circuito
        CircuitoDTO circuitoDTO = circuitoMapper.toDto(circuito);
        var returnedCircuitoDTO = om.readValue(
            restCircuitoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(circuitoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CircuitoDTO.class
        );

        // Validate the Circuito in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCircuito = circuitoMapper.toEntity(returnedCircuitoDTO);
        assertCircuitoUpdatableFieldsEquals(returnedCircuito, getPersistedCircuito(returnedCircuito));

        insertedCircuito = returnedCircuito;
    }

    @Test
    @Transactional
    void createCircuitoWithExistingId() throws Exception {
        // Create the Circuito with an existing ID
        circuito.setId(1L);
        CircuitoDTO circuitoDTO = circuitoMapper.toDto(circuito);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCircuitoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(circuitoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Circuito in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCircuitos() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList
        restCircuitoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(circuito.getId().intValue())))
            .andExpect(jsonPath("$.[*].tempoLimite").value(hasItem(DEFAULT_TEMPO_LIMITE.toString())))
            .andExpect(jsonPath("$.[*].tempoImpiegato").value(hasItem(DEFAULT_TEMPO_IMPIEGATO.toString())))
            .andExpect(jsonPath("$.[*].catenaRipetizioni").value(hasItem(DEFAULT_CATENA_RIPETIZIONI)))
            .andExpect(jsonPath("$.[*].circuitiCompletati").value(hasItem(DEFAULT_CIRCUITI_COMPLETATI)))
            .andExpect(jsonPath("$.[*].svolto").value(hasItem(DEFAULT_SVOLTO.booleanValue())))
            .andExpect(jsonPath("$.[*].completato").value(hasItem(DEFAULT_COMPLETATO.booleanValue())))
            .andExpect(jsonPath("$.[*].feedback").value(hasItem(DEFAULT_FEEDBACK.toString())));
    }

    @Test
    @Transactional
    void getCircuito() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get the circuito
        restCircuitoMockMvc
            .perform(get(ENTITY_API_URL_ID, circuito.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(circuito.getId().intValue()))
            .andExpect(jsonPath("$.tempoLimite").value(DEFAULT_TEMPO_LIMITE.toString()))
            .andExpect(jsonPath("$.tempoImpiegato").value(DEFAULT_TEMPO_IMPIEGATO.toString()))
            .andExpect(jsonPath("$.catenaRipetizioni").value(DEFAULT_CATENA_RIPETIZIONI))
            .andExpect(jsonPath("$.circuitiCompletati").value(DEFAULT_CIRCUITI_COMPLETATI))
            .andExpect(jsonPath("$.svolto").value(DEFAULT_SVOLTO.booleanValue()))
            .andExpect(jsonPath("$.completato").value(DEFAULT_COMPLETATO.booleanValue()))
            .andExpect(jsonPath("$.feedback").value(DEFAULT_FEEDBACK.toString()));
    }

    @Test
    @Transactional
    void getCircuitosByIdFiltering() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        Long id = circuito.getId();

        defaultCircuitoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCircuitoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCircuitoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCircuitosByTempoLimiteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where tempoLimite equals to
        defaultCircuitoFiltering("tempoLimite.equals=" + DEFAULT_TEMPO_LIMITE, "tempoLimite.equals=" + UPDATED_TEMPO_LIMITE);
    }

    @Test
    @Transactional
    void getAllCircuitosByTempoLimiteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where tempoLimite in
        defaultCircuitoFiltering(
            "tempoLimite.in=" + DEFAULT_TEMPO_LIMITE + "," + UPDATED_TEMPO_LIMITE,
            "tempoLimite.in=" + UPDATED_TEMPO_LIMITE
        );
    }

    @Test
    @Transactional
    void getAllCircuitosByTempoLimiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where tempoLimite is not null
        defaultCircuitoFiltering("tempoLimite.specified=true", "tempoLimite.specified=false");
    }

    @Test
    @Transactional
    void getAllCircuitosByTempoLimiteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where tempoLimite is greater than or equal to
        defaultCircuitoFiltering(
            "tempoLimite.greaterThanOrEqual=" + DEFAULT_TEMPO_LIMITE,
            "tempoLimite.greaterThanOrEqual=" + UPDATED_TEMPO_LIMITE
        );
    }

    @Test
    @Transactional
    void getAllCircuitosByTempoLimiteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where tempoLimite is less than or equal to
        defaultCircuitoFiltering(
            "tempoLimite.lessThanOrEqual=" + DEFAULT_TEMPO_LIMITE,
            "tempoLimite.lessThanOrEqual=" + SMALLER_TEMPO_LIMITE
        );
    }

    @Test
    @Transactional
    void getAllCircuitosByTempoLimiteIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where tempoLimite is less than
        defaultCircuitoFiltering("tempoLimite.lessThan=" + UPDATED_TEMPO_LIMITE, "tempoLimite.lessThan=" + DEFAULT_TEMPO_LIMITE);
    }

    @Test
    @Transactional
    void getAllCircuitosByTempoLimiteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where tempoLimite is greater than
        defaultCircuitoFiltering("tempoLimite.greaterThan=" + SMALLER_TEMPO_LIMITE, "tempoLimite.greaterThan=" + DEFAULT_TEMPO_LIMITE);
    }

    @Test
    @Transactional
    void getAllCircuitosByTempoImpiegatoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where tempoImpiegato equals to
        defaultCircuitoFiltering("tempoImpiegato.equals=" + DEFAULT_TEMPO_IMPIEGATO, "tempoImpiegato.equals=" + UPDATED_TEMPO_IMPIEGATO);
    }

    @Test
    @Transactional
    void getAllCircuitosByTempoImpiegatoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where tempoImpiegato in
        defaultCircuitoFiltering(
            "tempoImpiegato.in=" + DEFAULT_TEMPO_IMPIEGATO + "," + UPDATED_TEMPO_IMPIEGATO,
            "tempoImpiegato.in=" + UPDATED_TEMPO_IMPIEGATO
        );
    }

    @Test
    @Transactional
    void getAllCircuitosByTempoImpiegatoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where tempoImpiegato is not null
        defaultCircuitoFiltering("tempoImpiegato.specified=true", "tempoImpiegato.specified=false");
    }

    @Test
    @Transactional
    void getAllCircuitosByTempoImpiegatoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where tempoImpiegato is greater than or equal to
        defaultCircuitoFiltering(
            "tempoImpiegato.greaterThanOrEqual=" + DEFAULT_TEMPO_IMPIEGATO,
            "tempoImpiegato.greaterThanOrEqual=" + UPDATED_TEMPO_IMPIEGATO
        );
    }

    @Test
    @Transactional
    void getAllCircuitosByTempoImpiegatoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where tempoImpiegato is less than or equal to
        defaultCircuitoFiltering(
            "tempoImpiegato.lessThanOrEqual=" + DEFAULT_TEMPO_IMPIEGATO,
            "tempoImpiegato.lessThanOrEqual=" + SMALLER_TEMPO_IMPIEGATO
        );
    }

    @Test
    @Transactional
    void getAllCircuitosByTempoImpiegatoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where tempoImpiegato is less than
        defaultCircuitoFiltering(
            "tempoImpiegato.lessThan=" + UPDATED_TEMPO_IMPIEGATO,
            "tempoImpiegato.lessThan=" + DEFAULT_TEMPO_IMPIEGATO
        );
    }

    @Test
    @Transactional
    void getAllCircuitosByTempoImpiegatoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where tempoImpiegato is greater than
        defaultCircuitoFiltering(
            "tempoImpiegato.greaterThan=" + SMALLER_TEMPO_IMPIEGATO,
            "tempoImpiegato.greaterThan=" + DEFAULT_TEMPO_IMPIEGATO
        );
    }

    @Test
    @Transactional
    void getAllCircuitosByCatenaRipetizioniIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where catenaRipetizioni equals to
        defaultCircuitoFiltering(
            "catenaRipetizioni.equals=" + DEFAULT_CATENA_RIPETIZIONI,
            "catenaRipetizioni.equals=" + UPDATED_CATENA_RIPETIZIONI
        );
    }

    @Test
    @Transactional
    void getAllCircuitosByCatenaRipetizioniIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where catenaRipetizioni in
        defaultCircuitoFiltering(
            "catenaRipetizioni.in=" + DEFAULT_CATENA_RIPETIZIONI + "," + UPDATED_CATENA_RIPETIZIONI,
            "catenaRipetizioni.in=" + UPDATED_CATENA_RIPETIZIONI
        );
    }

    @Test
    @Transactional
    void getAllCircuitosByCatenaRipetizioniIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where catenaRipetizioni is not null
        defaultCircuitoFiltering("catenaRipetizioni.specified=true", "catenaRipetizioni.specified=false");
    }

    @Test
    @Transactional
    void getAllCircuitosByCatenaRipetizioniContainsSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where catenaRipetizioni contains
        defaultCircuitoFiltering(
            "catenaRipetizioni.contains=" + DEFAULT_CATENA_RIPETIZIONI,
            "catenaRipetizioni.contains=" + UPDATED_CATENA_RIPETIZIONI
        );
    }

    @Test
    @Transactional
    void getAllCircuitosByCatenaRipetizioniNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where catenaRipetizioni does not contain
        defaultCircuitoFiltering(
            "catenaRipetizioni.doesNotContain=" + UPDATED_CATENA_RIPETIZIONI,
            "catenaRipetizioni.doesNotContain=" + DEFAULT_CATENA_RIPETIZIONI
        );
    }

    @Test
    @Transactional
    void getAllCircuitosByCircuitiCompletatiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where circuitiCompletati equals to
        defaultCircuitoFiltering(
            "circuitiCompletati.equals=" + DEFAULT_CIRCUITI_COMPLETATI,
            "circuitiCompletati.equals=" + UPDATED_CIRCUITI_COMPLETATI
        );
    }

    @Test
    @Transactional
    void getAllCircuitosByCircuitiCompletatiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where circuitiCompletati in
        defaultCircuitoFiltering(
            "circuitiCompletati.in=" + DEFAULT_CIRCUITI_COMPLETATI + "," + UPDATED_CIRCUITI_COMPLETATI,
            "circuitiCompletati.in=" + UPDATED_CIRCUITI_COMPLETATI
        );
    }

    @Test
    @Transactional
    void getAllCircuitosByCircuitiCompletatiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where circuitiCompletati is not null
        defaultCircuitoFiltering("circuitiCompletati.specified=true", "circuitiCompletati.specified=false");
    }

    @Test
    @Transactional
    void getAllCircuitosByCircuitiCompletatiIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where circuitiCompletati is greater than or equal to
        defaultCircuitoFiltering(
            "circuitiCompletati.greaterThanOrEqual=" + DEFAULT_CIRCUITI_COMPLETATI,
            "circuitiCompletati.greaterThanOrEqual=" + UPDATED_CIRCUITI_COMPLETATI
        );
    }

    @Test
    @Transactional
    void getAllCircuitosByCircuitiCompletatiIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where circuitiCompletati is less than or equal to
        defaultCircuitoFiltering(
            "circuitiCompletati.lessThanOrEqual=" + DEFAULT_CIRCUITI_COMPLETATI,
            "circuitiCompletati.lessThanOrEqual=" + SMALLER_CIRCUITI_COMPLETATI
        );
    }

    @Test
    @Transactional
    void getAllCircuitosByCircuitiCompletatiIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where circuitiCompletati is less than
        defaultCircuitoFiltering(
            "circuitiCompletati.lessThan=" + UPDATED_CIRCUITI_COMPLETATI,
            "circuitiCompletati.lessThan=" + DEFAULT_CIRCUITI_COMPLETATI
        );
    }

    @Test
    @Transactional
    void getAllCircuitosByCircuitiCompletatiIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where circuitiCompletati is greater than
        defaultCircuitoFiltering(
            "circuitiCompletati.greaterThan=" + SMALLER_CIRCUITI_COMPLETATI,
            "circuitiCompletati.greaterThan=" + DEFAULT_CIRCUITI_COMPLETATI
        );
    }

    @Test
    @Transactional
    void getAllCircuitosBySvoltoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where svolto equals to
        defaultCircuitoFiltering("svolto.equals=" + DEFAULT_SVOLTO, "svolto.equals=" + UPDATED_SVOLTO);
    }

    @Test
    @Transactional
    void getAllCircuitosBySvoltoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where svolto in
        defaultCircuitoFiltering("svolto.in=" + DEFAULT_SVOLTO + "," + UPDATED_SVOLTO, "svolto.in=" + UPDATED_SVOLTO);
    }

    @Test
    @Transactional
    void getAllCircuitosBySvoltoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where svolto is not null
        defaultCircuitoFiltering("svolto.specified=true", "svolto.specified=false");
    }

    @Test
    @Transactional
    void getAllCircuitosByCompletatoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where completato equals to
        defaultCircuitoFiltering("completato.equals=" + DEFAULT_COMPLETATO, "completato.equals=" + UPDATED_COMPLETATO);
    }

    @Test
    @Transactional
    void getAllCircuitosByCompletatoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where completato in
        defaultCircuitoFiltering("completato.in=" + DEFAULT_COMPLETATO + "," + UPDATED_COMPLETATO, "completato.in=" + UPDATED_COMPLETATO);
    }

    @Test
    @Transactional
    void getAllCircuitosByCompletatoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        // Get all the circuitoList where completato is not null
        defaultCircuitoFiltering("completato.specified=true", "completato.specified=false");
    }

    @Test
    @Transactional
    void getAllCircuitosByAllenamentoGiornalieroIsEqualToSomething() throws Exception {
        // Get already existing entity
        AllenamentoGiornaliero allenamentoGiornaliero = circuito.getAllenamentoGiornaliero();
        circuitoRepository.saveAndFlush(circuito);
        Long allenamentoGiornalieroId = allenamentoGiornaliero.getId();
        // Get all the circuitoList where allenamentoGiornaliero equals to allenamentoGiornalieroId
        defaultCircuitoShouldBeFound("allenamentoGiornalieroId.equals=" + allenamentoGiornalieroId);

        // Get all the circuitoList where allenamentoGiornaliero equals to (allenamentoGiornalieroId + 1)
        defaultCircuitoShouldNotBeFound("allenamentoGiornalieroId.equals=" + (allenamentoGiornalieroId + 1));
    }

    private void defaultCircuitoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCircuitoShouldBeFound(shouldBeFound);
        defaultCircuitoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCircuitoShouldBeFound(String filter) throws Exception {
        restCircuitoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(circuito.getId().intValue())))
            .andExpect(jsonPath("$.[*].tempoLimite").value(hasItem(DEFAULT_TEMPO_LIMITE.toString())))
            .andExpect(jsonPath("$.[*].tempoImpiegato").value(hasItem(DEFAULT_TEMPO_IMPIEGATO.toString())))
            .andExpect(jsonPath("$.[*].catenaRipetizioni").value(hasItem(DEFAULT_CATENA_RIPETIZIONI)))
            .andExpect(jsonPath("$.[*].circuitiCompletati").value(hasItem(DEFAULT_CIRCUITI_COMPLETATI)))
            .andExpect(jsonPath("$.[*].svolto").value(hasItem(DEFAULT_SVOLTO.booleanValue())))
            .andExpect(jsonPath("$.[*].completato").value(hasItem(DEFAULT_COMPLETATO.booleanValue())))
            .andExpect(jsonPath("$.[*].feedback").value(hasItem(DEFAULT_FEEDBACK.toString())));

        // Check, that the count call also returns 1
        restCircuitoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCircuitoShouldNotBeFound(String filter) throws Exception {
        restCircuitoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCircuitoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCircuito() throws Exception {
        // Get the circuito
        restCircuitoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCircuito() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the circuito
        Circuito updatedCircuito = circuitoRepository.findById(circuito.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCircuito are not directly saved in db
        em.detach(updatedCircuito);
        updatedCircuito
            .tempoLimite(UPDATED_TEMPO_LIMITE)
            .tempoImpiegato(UPDATED_TEMPO_IMPIEGATO)
            .catenaRipetizioni(UPDATED_CATENA_RIPETIZIONI)
            .circuitiCompletati(UPDATED_CIRCUITI_COMPLETATI)
            .svolto(UPDATED_SVOLTO)
            .completato(UPDATED_COMPLETATO)
            .feedback(UPDATED_FEEDBACK);
        CircuitoDTO circuitoDTO = circuitoMapper.toDto(updatedCircuito);

        restCircuitoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, circuitoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(circuitoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Circuito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCircuitoToMatchAllProperties(updatedCircuito);
    }

    @Test
    @Transactional
    void putNonExistingCircuito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circuito.setId(longCount.incrementAndGet());

        // Create the Circuito
        CircuitoDTO circuitoDTO = circuitoMapper.toDto(circuito);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCircuitoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, circuitoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(circuitoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Circuito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCircuito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circuito.setId(longCount.incrementAndGet());

        // Create the Circuito
        CircuitoDTO circuitoDTO = circuitoMapper.toDto(circuito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCircuitoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(circuitoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Circuito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCircuito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circuito.setId(longCount.incrementAndGet());

        // Create the Circuito
        CircuitoDTO circuitoDTO = circuitoMapper.toDto(circuito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCircuitoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(circuitoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Circuito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCircuitoWithPatch() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the circuito using partial update
        Circuito partialUpdatedCircuito = new Circuito();
        partialUpdatedCircuito.setId(circuito.getId());

        restCircuitoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCircuito.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCircuito))
            )
            .andExpect(status().isOk());

        // Validate the Circuito in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCircuitoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCircuito, circuito), getPersistedCircuito(circuito));
    }

    @Test
    @Transactional
    void fullUpdateCircuitoWithPatch() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the circuito using partial update
        Circuito partialUpdatedCircuito = new Circuito();
        partialUpdatedCircuito.setId(circuito.getId());

        partialUpdatedCircuito
            .tempoLimite(UPDATED_TEMPO_LIMITE)
            .tempoImpiegato(UPDATED_TEMPO_IMPIEGATO)
            .catenaRipetizioni(UPDATED_CATENA_RIPETIZIONI)
            .circuitiCompletati(UPDATED_CIRCUITI_COMPLETATI)
            .svolto(UPDATED_SVOLTO)
            .completato(UPDATED_COMPLETATO)
            .feedback(UPDATED_FEEDBACK);

        restCircuitoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCircuito.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCircuito))
            )
            .andExpect(status().isOk());

        // Validate the Circuito in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCircuitoUpdatableFieldsEquals(partialUpdatedCircuito, getPersistedCircuito(partialUpdatedCircuito));
    }

    @Test
    @Transactional
    void patchNonExistingCircuito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circuito.setId(longCount.incrementAndGet());

        // Create the Circuito
        CircuitoDTO circuitoDTO = circuitoMapper.toDto(circuito);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCircuitoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, circuitoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(circuitoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Circuito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCircuito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circuito.setId(longCount.incrementAndGet());

        // Create the Circuito
        CircuitoDTO circuitoDTO = circuitoMapper.toDto(circuito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCircuitoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(circuitoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Circuito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCircuito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circuito.setId(longCount.incrementAndGet());

        // Create the Circuito
        CircuitoDTO circuitoDTO = circuitoMapper.toDto(circuito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCircuitoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(circuitoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Circuito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCircuito() throws Exception {
        // Initialize the database
        insertedCircuito = circuitoRepository.saveAndFlush(circuito);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the circuito
        restCircuitoMockMvc
            .perform(delete(ENTITY_API_URL_ID, circuito.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return circuitoRepository.count();
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

    protected Circuito getPersistedCircuito(Circuito circuito) {
        return circuitoRepository.findById(circuito.getId()).orElseThrow();
    }

    protected void assertPersistedCircuitoToMatchAllProperties(Circuito expectedCircuito) {
        assertCircuitoAllPropertiesEquals(expectedCircuito, getPersistedCircuito(expectedCircuito));
    }

    protected void assertPersistedCircuitoToMatchUpdatableProperties(Circuito expectedCircuito) {
        assertCircuitoAllUpdatablePropertiesEquals(expectedCircuito, getPersistedCircuito(expectedCircuito));
    }
}
