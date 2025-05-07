package com.oscinnovation.fitness.web.rest;

import static com.oscinnovation.fitness.domain.SchedaSettimanaleAsserts.*;
import static com.oscinnovation.fitness.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscinnovation.fitness.IntegrationTest;
import com.oscinnovation.fitness.domain.Cliente;
import com.oscinnovation.fitness.domain.SchedaSettimanale;
import com.oscinnovation.fitness.repository.SchedaSettimanaleRepository;
import com.oscinnovation.fitness.service.SchedaSettimanaleService;
import com.oscinnovation.fitness.service.dto.SchedaSettimanaleDTO;
import com.oscinnovation.fitness.service.mapper.SchedaSettimanaleMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SchedaSettimanaleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SchedaSettimanaleResourceIT {

    private static final Integer DEFAULT_ANNO = 0;
    private static final Integer UPDATED_ANNO = 1;
    private static final Integer SMALLER_ANNO = 0 - 1;

    private static final Integer DEFAULT_MESE = 1;
    private static final Integer UPDATED_MESE = 2;
    private static final Integer SMALLER_MESE = 1 - 1;

    private static final Integer DEFAULT_SETTIMANA = 1;
    private static final Integer UPDATED_SETTIMANA = 2;
    private static final Integer SMALLER_SETTIMANA = 1 - 1;

    private static final LocalDate DEFAULT_DATA_CREAZIONE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_CREAZIONE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_CREAZIONE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/scheda-settimanales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SchedaSettimanaleRepository schedaSettimanaleRepository;

    @Mock
    private SchedaSettimanaleRepository schedaSettimanaleRepositoryMock;

    @Autowired
    private SchedaSettimanaleMapper schedaSettimanaleMapper;

    @Mock
    private SchedaSettimanaleService schedaSettimanaleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchedaSettimanaleMockMvc;

    private SchedaSettimanale schedaSettimanale;

    private SchedaSettimanale insertedSchedaSettimanale;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchedaSettimanale createEntity(EntityManager em) {
        SchedaSettimanale schedaSettimanale = new SchedaSettimanale()
            .anno(DEFAULT_ANNO)
            .mese(DEFAULT_MESE)
            .settimana(DEFAULT_SETTIMANA)
            .dataCreazione(DEFAULT_DATA_CREAZIONE);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        schedaSettimanale.setCliente(cliente);
        return schedaSettimanale;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchedaSettimanale createUpdatedEntity(EntityManager em) {
        SchedaSettimanale updatedSchedaSettimanale = new SchedaSettimanale()
            .anno(UPDATED_ANNO)
            .mese(UPDATED_MESE)
            .settimana(UPDATED_SETTIMANA)
            .dataCreazione(UPDATED_DATA_CREAZIONE);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createUpdatedEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        updatedSchedaSettimanale.setCliente(cliente);
        return updatedSchedaSettimanale;
    }

    @BeforeEach
    public void initTest() {
        schedaSettimanale = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSchedaSettimanale != null) {
            schedaSettimanaleRepository.delete(insertedSchedaSettimanale);
            insertedSchedaSettimanale = null;
        }
    }

    @Test
    @Transactional
    void createSchedaSettimanale() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SchedaSettimanale
        SchedaSettimanaleDTO schedaSettimanaleDTO = schedaSettimanaleMapper.toDto(schedaSettimanale);
        var returnedSchedaSettimanaleDTO = om.readValue(
            restSchedaSettimanaleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schedaSettimanaleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SchedaSettimanaleDTO.class
        );

        // Validate the SchedaSettimanale in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSchedaSettimanale = schedaSettimanaleMapper.toEntity(returnedSchedaSettimanaleDTO);
        assertSchedaSettimanaleUpdatableFieldsEquals(returnedSchedaSettimanale, getPersistedSchedaSettimanale(returnedSchedaSettimanale));

        insertedSchedaSettimanale = returnedSchedaSettimanale;
    }

    @Test
    @Transactional
    void createSchedaSettimanaleWithExistingId() throws Exception {
        // Create the SchedaSettimanale with an existing ID
        schedaSettimanale.setId(1L);
        SchedaSettimanaleDTO schedaSettimanaleDTO = schedaSettimanaleMapper.toDto(schedaSettimanale);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchedaSettimanaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schedaSettimanaleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SchedaSettimanale in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAnnoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        schedaSettimanale.setAnno(null);

        // Create the SchedaSettimanale, which fails.
        SchedaSettimanaleDTO schedaSettimanaleDTO = schedaSettimanaleMapper.toDto(schedaSettimanale);

        restSchedaSettimanaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schedaSettimanaleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMeseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        schedaSettimanale.setMese(null);

        // Create the SchedaSettimanale, which fails.
        SchedaSettimanaleDTO schedaSettimanaleDTO = schedaSettimanaleMapper.toDto(schedaSettimanale);

        restSchedaSettimanaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schedaSettimanaleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSettimanaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        schedaSettimanale.setSettimana(null);

        // Create the SchedaSettimanale, which fails.
        SchedaSettimanaleDTO schedaSettimanaleDTO = schedaSettimanaleMapper.toDto(schedaSettimanale);

        restSchedaSettimanaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schedaSettimanaleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataCreazioneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        schedaSettimanale.setDataCreazione(null);

        // Create the SchedaSettimanale, which fails.
        SchedaSettimanaleDTO schedaSettimanaleDTO = schedaSettimanaleMapper.toDto(schedaSettimanale);

        restSchedaSettimanaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schedaSettimanaleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSchedaSettimanales() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList
        restSchedaSettimanaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedaSettimanale.getId().intValue())))
            .andExpect(jsonPath("$.[*].anno").value(hasItem(DEFAULT_ANNO)))
            .andExpect(jsonPath("$.[*].mese").value(hasItem(DEFAULT_MESE)))
            .andExpect(jsonPath("$.[*].settimana").value(hasItem(DEFAULT_SETTIMANA)))
            .andExpect(jsonPath("$.[*].dataCreazione").value(hasItem(DEFAULT_DATA_CREAZIONE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchedaSettimanalesWithEagerRelationshipsIsEnabled() throws Exception {
        when(schedaSettimanaleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchedaSettimanaleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(schedaSettimanaleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchedaSettimanalesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(schedaSettimanaleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchedaSettimanaleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(schedaSettimanaleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSchedaSettimanale() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get the schedaSettimanale
        restSchedaSettimanaleMockMvc
            .perform(get(ENTITY_API_URL_ID, schedaSettimanale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schedaSettimanale.getId().intValue()))
            .andExpect(jsonPath("$.anno").value(DEFAULT_ANNO))
            .andExpect(jsonPath("$.mese").value(DEFAULT_MESE))
            .andExpect(jsonPath("$.settimana").value(DEFAULT_SETTIMANA))
            .andExpect(jsonPath("$.dataCreazione").value(DEFAULT_DATA_CREAZIONE.toString()));
    }

    @Test
    @Transactional
    void getSchedaSettimanalesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        Long id = schedaSettimanale.getId();

        defaultSchedaSettimanaleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSchedaSettimanaleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSchedaSettimanaleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByAnnoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where anno equals to
        defaultSchedaSettimanaleFiltering("anno.equals=" + DEFAULT_ANNO, "anno.equals=" + UPDATED_ANNO);
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByAnnoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where anno in
        defaultSchedaSettimanaleFiltering("anno.in=" + DEFAULT_ANNO + "," + UPDATED_ANNO, "anno.in=" + UPDATED_ANNO);
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByAnnoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where anno is not null
        defaultSchedaSettimanaleFiltering("anno.specified=true", "anno.specified=false");
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByAnnoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where anno is greater than or equal to
        defaultSchedaSettimanaleFiltering("anno.greaterThanOrEqual=" + DEFAULT_ANNO, "anno.greaterThanOrEqual=" + UPDATED_ANNO);
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByAnnoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where anno is less than or equal to
        defaultSchedaSettimanaleFiltering("anno.lessThanOrEqual=" + DEFAULT_ANNO, "anno.lessThanOrEqual=" + SMALLER_ANNO);
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByAnnoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where anno is less than
        defaultSchedaSettimanaleFiltering("anno.lessThan=" + UPDATED_ANNO, "anno.lessThan=" + DEFAULT_ANNO);
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByAnnoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where anno is greater than
        defaultSchedaSettimanaleFiltering("anno.greaterThan=" + SMALLER_ANNO, "anno.greaterThan=" + DEFAULT_ANNO);
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByMeseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where mese equals to
        defaultSchedaSettimanaleFiltering("mese.equals=" + DEFAULT_MESE, "mese.equals=" + UPDATED_MESE);
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByMeseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where mese in
        defaultSchedaSettimanaleFiltering("mese.in=" + DEFAULT_MESE + "," + UPDATED_MESE, "mese.in=" + UPDATED_MESE);
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByMeseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where mese is not null
        defaultSchedaSettimanaleFiltering("mese.specified=true", "mese.specified=false");
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByMeseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where mese is greater than or equal to
        defaultSchedaSettimanaleFiltering("mese.greaterThanOrEqual=" + DEFAULT_MESE, "mese.greaterThanOrEqual=" + (DEFAULT_MESE + 1));
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByMeseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where mese is less than or equal to
        defaultSchedaSettimanaleFiltering("mese.lessThanOrEqual=" + DEFAULT_MESE, "mese.lessThanOrEqual=" + SMALLER_MESE);
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByMeseIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where mese is less than
        defaultSchedaSettimanaleFiltering("mese.lessThan=" + (DEFAULT_MESE + 1), "mese.lessThan=" + DEFAULT_MESE);
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByMeseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where mese is greater than
        defaultSchedaSettimanaleFiltering("mese.greaterThan=" + SMALLER_MESE, "mese.greaterThan=" + DEFAULT_MESE);
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesBySettimanaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where settimana equals to
        defaultSchedaSettimanaleFiltering("settimana.equals=" + DEFAULT_SETTIMANA, "settimana.equals=" + UPDATED_SETTIMANA);
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesBySettimanaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where settimana in
        defaultSchedaSettimanaleFiltering(
            "settimana.in=" + DEFAULT_SETTIMANA + "," + UPDATED_SETTIMANA,
            "settimana.in=" + UPDATED_SETTIMANA
        );
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesBySettimanaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where settimana is not null
        defaultSchedaSettimanaleFiltering("settimana.specified=true", "settimana.specified=false");
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesBySettimanaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where settimana is greater than or equal to
        defaultSchedaSettimanaleFiltering(
            "settimana.greaterThanOrEqual=" + DEFAULT_SETTIMANA,
            "settimana.greaterThanOrEqual=" + (DEFAULT_SETTIMANA + 1)
        );
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesBySettimanaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where settimana is less than or equal to
        defaultSchedaSettimanaleFiltering(
            "settimana.lessThanOrEqual=" + DEFAULT_SETTIMANA,
            "settimana.lessThanOrEqual=" + SMALLER_SETTIMANA
        );
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesBySettimanaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where settimana is less than
        defaultSchedaSettimanaleFiltering("settimana.lessThan=" + (DEFAULT_SETTIMANA + 1), "settimana.lessThan=" + DEFAULT_SETTIMANA);
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesBySettimanaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where settimana is greater than
        defaultSchedaSettimanaleFiltering("settimana.greaterThan=" + SMALLER_SETTIMANA, "settimana.greaterThan=" + DEFAULT_SETTIMANA);
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByDataCreazioneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where dataCreazione equals to
        defaultSchedaSettimanaleFiltering(
            "dataCreazione.equals=" + DEFAULT_DATA_CREAZIONE,
            "dataCreazione.equals=" + UPDATED_DATA_CREAZIONE
        );
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByDataCreazioneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where dataCreazione in
        defaultSchedaSettimanaleFiltering(
            "dataCreazione.in=" + DEFAULT_DATA_CREAZIONE + "," + UPDATED_DATA_CREAZIONE,
            "dataCreazione.in=" + UPDATED_DATA_CREAZIONE
        );
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByDataCreazioneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where dataCreazione is not null
        defaultSchedaSettimanaleFiltering("dataCreazione.specified=true", "dataCreazione.specified=false");
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByDataCreazioneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where dataCreazione is greater than or equal to
        defaultSchedaSettimanaleFiltering(
            "dataCreazione.greaterThanOrEqual=" + DEFAULT_DATA_CREAZIONE,
            "dataCreazione.greaterThanOrEqual=" + UPDATED_DATA_CREAZIONE
        );
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByDataCreazioneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where dataCreazione is less than or equal to
        defaultSchedaSettimanaleFiltering(
            "dataCreazione.lessThanOrEqual=" + DEFAULT_DATA_CREAZIONE,
            "dataCreazione.lessThanOrEqual=" + SMALLER_DATA_CREAZIONE
        );
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByDataCreazioneIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where dataCreazione is less than
        defaultSchedaSettimanaleFiltering(
            "dataCreazione.lessThan=" + UPDATED_DATA_CREAZIONE,
            "dataCreazione.lessThan=" + DEFAULT_DATA_CREAZIONE
        );
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByDataCreazioneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        // Get all the schedaSettimanaleList where dataCreazione is greater than
        defaultSchedaSettimanaleFiltering(
            "dataCreazione.greaterThan=" + SMALLER_DATA_CREAZIONE,
            "dataCreazione.greaterThan=" + DEFAULT_DATA_CREAZIONE
        );
    }

    @Test
    @Transactional
    void getAllSchedaSettimanalesByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);
            cliente = ClienteResourceIT.createEntity(em);
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        em.persist(cliente);
        em.flush();
        schedaSettimanale.setCliente(cliente);
        schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);
        Long clienteId = cliente.getId();
        // Get all the schedaSettimanaleList where cliente equals to clienteId
        defaultSchedaSettimanaleShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the schedaSettimanaleList where cliente equals to (clienteId + 1)
        defaultSchedaSettimanaleShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    private void defaultSchedaSettimanaleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSchedaSettimanaleShouldBeFound(shouldBeFound);
        defaultSchedaSettimanaleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSchedaSettimanaleShouldBeFound(String filter) throws Exception {
        restSchedaSettimanaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedaSettimanale.getId().intValue())))
            .andExpect(jsonPath("$.[*].anno").value(hasItem(DEFAULT_ANNO)))
            .andExpect(jsonPath("$.[*].mese").value(hasItem(DEFAULT_MESE)))
            .andExpect(jsonPath("$.[*].settimana").value(hasItem(DEFAULT_SETTIMANA)))
            .andExpect(jsonPath("$.[*].dataCreazione").value(hasItem(DEFAULT_DATA_CREAZIONE.toString())));

        // Check, that the count call also returns 1
        restSchedaSettimanaleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSchedaSettimanaleShouldNotBeFound(String filter) throws Exception {
        restSchedaSettimanaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSchedaSettimanaleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSchedaSettimanale() throws Exception {
        // Get the schedaSettimanale
        restSchedaSettimanaleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSchedaSettimanale() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the schedaSettimanale
        SchedaSettimanale updatedSchedaSettimanale = schedaSettimanaleRepository.findById(schedaSettimanale.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSchedaSettimanale are not directly saved in db
        em.detach(updatedSchedaSettimanale);
        updatedSchedaSettimanale.anno(UPDATED_ANNO).mese(UPDATED_MESE).settimana(UPDATED_SETTIMANA).dataCreazione(UPDATED_DATA_CREAZIONE);
        SchedaSettimanaleDTO schedaSettimanaleDTO = schedaSettimanaleMapper.toDto(updatedSchedaSettimanale);

        restSchedaSettimanaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schedaSettimanaleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(schedaSettimanaleDTO))
            )
            .andExpect(status().isOk());

        // Validate the SchedaSettimanale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSchedaSettimanaleToMatchAllProperties(updatedSchedaSettimanale);
    }

    @Test
    @Transactional
    void putNonExistingSchedaSettimanale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedaSettimanale.setId(longCount.incrementAndGet());

        // Create the SchedaSettimanale
        SchedaSettimanaleDTO schedaSettimanaleDTO = schedaSettimanaleMapper.toDto(schedaSettimanale);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedaSettimanaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schedaSettimanaleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(schedaSettimanaleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedaSettimanale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchedaSettimanale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedaSettimanale.setId(longCount.incrementAndGet());

        // Create the SchedaSettimanale
        SchedaSettimanaleDTO schedaSettimanaleDTO = schedaSettimanaleMapper.toDto(schedaSettimanale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedaSettimanaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(schedaSettimanaleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedaSettimanale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchedaSettimanale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedaSettimanale.setId(longCount.incrementAndGet());

        // Create the SchedaSettimanale
        SchedaSettimanaleDTO schedaSettimanaleDTO = schedaSettimanaleMapper.toDto(schedaSettimanale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedaSettimanaleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schedaSettimanaleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchedaSettimanale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchedaSettimanaleWithPatch() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the schedaSettimanale using partial update
        SchedaSettimanale partialUpdatedSchedaSettimanale = new SchedaSettimanale();
        partialUpdatedSchedaSettimanale.setId(schedaSettimanale.getId());

        partialUpdatedSchedaSettimanale
            .anno(UPDATED_ANNO)
            .mese(UPDATED_MESE)
            .settimana(UPDATED_SETTIMANA)
            .dataCreazione(UPDATED_DATA_CREAZIONE);

        restSchedaSettimanaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedaSettimanale.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSchedaSettimanale))
            )
            .andExpect(status().isOk());

        // Validate the SchedaSettimanale in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSchedaSettimanaleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSchedaSettimanale, schedaSettimanale),
            getPersistedSchedaSettimanale(schedaSettimanale)
        );
    }

    @Test
    @Transactional
    void fullUpdateSchedaSettimanaleWithPatch() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the schedaSettimanale using partial update
        SchedaSettimanale partialUpdatedSchedaSettimanale = new SchedaSettimanale();
        partialUpdatedSchedaSettimanale.setId(schedaSettimanale.getId());

        partialUpdatedSchedaSettimanale
            .anno(UPDATED_ANNO)
            .mese(UPDATED_MESE)
            .settimana(UPDATED_SETTIMANA)
            .dataCreazione(UPDATED_DATA_CREAZIONE);

        restSchedaSettimanaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedaSettimanale.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSchedaSettimanale))
            )
            .andExpect(status().isOk());

        // Validate the SchedaSettimanale in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSchedaSettimanaleUpdatableFieldsEquals(
            partialUpdatedSchedaSettimanale,
            getPersistedSchedaSettimanale(partialUpdatedSchedaSettimanale)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSchedaSettimanale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedaSettimanale.setId(longCount.incrementAndGet());

        // Create the SchedaSettimanale
        SchedaSettimanaleDTO schedaSettimanaleDTO = schedaSettimanaleMapper.toDto(schedaSettimanale);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedaSettimanaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schedaSettimanaleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(schedaSettimanaleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedaSettimanale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchedaSettimanale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedaSettimanale.setId(longCount.incrementAndGet());

        // Create the SchedaSettimanale
        SchedaSettimanaleDTO schedaSettimanaleDTO = schedaSettimanaleMapper.toDto(schedaSettimanale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedaSettimanaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(schedaSettimanaleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedaSettimanale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchedaSettimanale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedaSettimanale.setId(longCount.incrementAndGet());

        // Create the SchedaSettimanale
        SchedaSettimanaleDTO schedaSettimanaleDTO = schedaSettimanaleMapper.toDto(schedaSettimanale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedaSettimanaleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(schedaSettimanaleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchedaSettimanale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchedaSettimanale() throws Exception {
        // Initialize the database
        insertedSchedaSettimanale = schedaSettimanaleRepository.saveAndFlush(schedaSettimanale);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the schedaSettimanale
        restSchedaSettimanaleMockMvc
            .perform(delete(ENTITY_API_URL_ID, schedaSettimanale.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return schedaSettimanaleRepository.count();
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

    protected SchedaSettimanale getPersistedSchedaSettimanale(SchedaSettimanale schedaSettimanale) {
        return schedaSettimanaleRepository.findById(schedaSettimanale.getId()).orElseThrow();
    }

    protected void assertPersistedSchedaSettimanaleToMatchAllProperties(SchedaSettimanale expectedSchedaSettimanale) {
        assertSchedaSettimanaleAllPropertiesEquals(expectedSchedaSettimanale, getPersistedSchedaSettimanale(expectedSchedaSettimanale));
    }

    protected void assertPersistedSchedaSettimanaleToMatchUpdatableProperties(SchedaSettimanale expectedSchedaSettimanale) {
        assertSchedaSettimanaleAllUpdatablePropertiesEquals(
            expectedSchedaSettimanale,
            getPersistedSchedaSettimanale(expectedSchedaSettimanale)
        );
    }
}
