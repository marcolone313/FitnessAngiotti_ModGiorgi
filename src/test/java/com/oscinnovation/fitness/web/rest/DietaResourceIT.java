package com.oscinnovation.fitness.web.rest;

import static com.oscinnovation.fitness.domain.DietaAsserts.*;
import static com.oscinnovation.fitness.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscinnovation.fitness.IntegrationTest;
import com.oscinnovation.fitness.domain.Cliente;
import com.oscinnovation.fitness.domain.Dieta;
import com.oscinnovation.fitness.repository.DietaRepository;
import com.oscinnovation.fitness.service.DietaService;
import com.oscinnovation.fitness.service.dto.DietaDTO;
import com.oscinnovation.fitness.service.mapper.DietaMapper;
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
 * Integration tests for the {@link DietaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DietaResourceIT {

    private static final LocalDate DEFAULT_DATA_CREAZIONE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_CREAZIONE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_CREAZIONE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_MESE = 1;
    private static final Integer UPDATED_MESE = 2;
    private static final Integer SMALLER_MESE = 1 - 1;

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final String DEFAULT_MACROS = "AAAAAAAAAA";
    private static final String UPDATED_MACROS = "BBBBBBBBBB";

    private static final String DEFAULT_FABBISOGNO_CALORICO = "AAAAAAAAAA";
    private static final String UPDATED_FABBISOGNO_CALORICO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dietas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DietaRepository dietaRepository;

    @Mock
    private DietaRepository dietaRepositoryMock;

    @Autowired
    private DietaMapper dietaMapper;

    @Mock
    private DietaService dietaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDietaMockMvc;

    private Dieta dieta;

    private Dieta insertedDieta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dieta createEntity(EntityManager em) {
        Dieta dieta = new Dieta()
            .dataCreazione(DEFAULT_DATA_CREAZIONE)
            .mese(DEFAULT_MESE)
            .tipo(DEFAULT_TIPO)
            .macros(DEFAULT_MACROS)
            .fabbisognoCalorico(DEFAULT_FABBISOGNO_CALORICO);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        dieta.setCliente(cliente);
        return dieta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dieta createUpdatedEntity(EntityManager em) {
        Dieta updatedDieta = new Dieta()
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .mese(UPDATED_MESE)
            .tipo(UPDATED_TIPO)
            .macros(UPDATED_MACROS)
            .fabbisognoCalorico(UPDATED_FABBISOGNO_CALORICO);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createUpdatedEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        updatedDieta.setCliente(cliente);
        return updatedDieta;
    }

    @BeforeEach
    public void initTest() {
        dieta = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDieta != null) {
            dietaRepository.delete(insertedDieta);
            insertedDieta = null;
        }
    }

    @Test
    @Transactional
    void createDieta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Dieta
        DietaDTO dietaDTO = dietaMapper.toDto(dieta);
        var returnedDietaDTO = om.readValue(
            restDietaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dietaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DietaDTO.class
        );

        // Validate the Dieta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDieta = dietaMapper.toEntity(returnedDietaDTO);
        assertDietaUpdatableFieldsEquals(returnedDieta, getPersistedDieta(returnedDieta));

        insertedDieta = returnedDieta;
    }

    @Test
    @Transactional
    void createDietaWithExistingId() throws Exception {
        // Create the Dieta with an existing ID
        dieta.setId(1L);
        DietaDTO dietaDTO = dietaMapper.toDto(dieta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDietaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dietaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dieta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataCreazioneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dieta.setDataCreazione(null);

        // Create the Dieta, which fails.
        DietaDTO dietaDTO = dietaMapper.toDto(dieta);

        restDietaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dietaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMeseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dieta.setMese(null);

        // Create the Dieta, which fails.
        DietaDTO dietaDTO = dietaMapper.toDto(dieta);

        restDietaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dietaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dieta.setTipo(null);

        // Create the Dieta, which fails.
        DietaDTO dietaDTO = dietaMapper.toDto(dieta);

        restDietaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dietaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMacrosIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dieta.setMacros(null);

        // Create the Dieta, which fails.
        DietaDTO dietaDTO = dietaMapper.toDto(dieta);

        restDietaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dietaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFabbisognoCaloricoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dieta.setFabbisognoCalorico(null);

        // Create the Dieta, which fails.
        DietaDTO dietaDTO = dietaMapper.toDto(dieta);

        restDietaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dietaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDietas() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList
        restDietaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dieta.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataCreazione").value(hasItem(DEFAULT_DATA_CREAZIONE.toString())))
            .andExpect(jsonPath("$.[*].mese").value(hasItem(DEFAULT_MESE)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].macros").value(hasItem(DEFAULT_MACROS)))
            .andExpect(jsonPath("$.[*].fabbisognoCalorico").value(hasItem(DEFAULT_FABBISOGNO_CALORICO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDietasWithEagerRelationshipsIsEnabled() throws Exception {
        when(dietaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDietaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dietaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDietasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(dietaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDietaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(dietaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDieta() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get the dieta
        restDietaMockMvc
            .perform(get(ENTITY_API_URL_ID, dieta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dieta.getId().intValue()))
            .andExpect(jsonPath("$.dataCreazione").value(DEFAULT_DATA_CREAZIONE.toString()))
            .andExpect(jsonPath("$.mese").value(DEFAULT_MESE))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.macros").value(DEFAULT_MACROS))
            .andExpect(jsonPath("$.fabbisognoCalorico").value(DEFAULT_FABBISOGNO_CALORICO));
    }

    @Test
    @Transactional
    void getDietasByIdFiltering() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        Long id = dieta.getId();

        defaultDietaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDietaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDietaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDietasByDataCreazioneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where dataCreazione equals to
        defaultDietaFiltering("dataCreazione.equals=" + DEFAULT_DATA_CREAZIONE, "dataCreazione.equals=" + UPDATED_DATA_CREAZIONE);
    }

    @Test
    @Transactional
    void getAllDietasByDataCreazioneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where dataCreazione in
        defaultDietaFiltering(
            "dataCreazione.in=" + DEFAULT_DATA_CREAZIONE + "," + UPDATED_DATA_CREAZIONE,
            "dataCreazione.in=" + UPDATED_DATA_CREAZIONE
        );
    }

    @Test
    @Transactional
    void getAllDietasByDataCreazioneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where dataCreazione is not null
        defaultDietaFiltering("dataCreazione.specified=true", "dataCreazione.specified=false");
    }

    @Test
    @Transactional
    void getAllDietasByDataCreazioneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where dataCreazione is greater than or equal to
        defaultDietaFiltering(
            "dataCreazione.greaterThanOrEqual=" + DEFAULT_DATA_CREAZIONE,
            "dataCreazione.greaterThanOrEqual=" + UPDATED_DATA_CREAZIONE
        );
    }

    @Test
    @Transactional
    void getAllDietasByDataCreazioneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where dataCreazione is less than or equal to
        defaultDietaFiltering(
            "dataCreazione.lessThanOrEqual=" + DEFAULT_DATA_CREAZIONE,
            "dataCreazione.lessThanOrEqual=" + SMALLER_DATA_CREAZIONE
        );
    }

    @Test
    @Transactional
    void getAllDietasByDataCreazioneIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where dataCreazione is less than
        defaultDietaFiltering("dataCreazione.lessThan=" + UPDATED_DATA_CREAZIONE, "dataCreazione.lessThan=" + DEFAULT_DATA_CREAZIONE);
    }

    @Test
    @Transactional
    void getAllDietasByDataCreazioneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where dataCreazione is greater than
        defaultDietaFiltering("dataCreazione.greaterThan=" + SMALLER_DATA_CREAZIONE, "dataCreazione.greaterThan=" + DEFAULT_DATA_CREAZIONE);
    }

    @Test
    @Transactional
    void getAllDietasByMeseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where mese equals to
        defaultDietaFiltering("mese.equals=" + DEFAULT_MESE, "mese.equals=" + UPDATED_MESE);
    }

    @Test
    @Transactional
    void getAllDietasByMeseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where mese in
        defaultDietaFiltering("mese.in=" + DEFAULT_MESE + "," + UPDATED_MESE, "mese.in=" + UPDATED_MESE);
    }

    @Test
    @Transactional
    void getAllDietasByMeseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where mese is not null
        defaultDietaFiltering("mese.specified=true", "mese.specified=false");
    }

    @Test
    @Transactional
    void getAllDietasByMeseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where mese is greater than or equal to
        defaultDietaFiltering("mese.greaterThanOrEqual=" + DEFAULT_MESE, "mese.greaterThanOrEqual=" + (DEFAULT_MESE + 1));
    }

    @Test
    @Transactional
    void getAllDietasByMeseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where mese is less than or equal to
        defaultDietaFiltering("mese.lessThanOrEqual=" + DEFAULT_MESE, "mese.lessThanOrEqual=" + SMALLER_MESE);
    }

    @Test
    @Transactional
    void getAllDietasByMeseIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where mese is less than
        defaultDietaFiltering("mese.lessThan=" + (DEFAULT_MESE + 1), "mese.lessThan=" + DEFAULT_MESE);
    }

    @Test
    @Transactional
    void getAllDietasByMeseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where mese is greater than
        defaultDietaFiltering("mese.greaterThan=" + SMALLER_MESE, "mese.greaterThan=" + DEFAULT_MESE);
    }

    @Test
    @Transactional
    void getAllDietasByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where tipo equals to
        defaultDietaFiltering("tipo.equals=" + DEFAULT_TIPO, "tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllDietasByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where tipo in
        defaultDietaFiltering("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO, "tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllDietasByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where tipo is not null
        defaultDietaFiltering("tipo.specified=true", "tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllDietasByTipoContainsSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where tipo contains
        defaultDietaFiltering("tipo.contains=" + DEFAULT_TIPO, "tipo.contains=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllDietasByTipoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where tipo does not contain
        defaultDietaFiltering("tipo.doesNotContain=" + UPDATED_TIPO, "tipo.doesNotContain=" + DEFAULT_TIPO);
    }

    @Test
    @Transactional
    void getAllDietasByMacrosIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where macros equals to
        defaultDietaFiltering("macros.equals=" + DEFAULT_MACROS, "macros.equals=" + UPDATED_MACROS);
    }

    @Test
    @Transactional
    void getAllDietasByMacrosIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where macros in
        defaultDietaFiltering("macros.in=" + DEFAULT_MACROS + "," + UPDATED_MACROS, "macros.in=" + UPDATED_MACROS);
    }

    @Test
    @Transactional
    void getAllDietasByMacrosIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where macros is not null
        defaultDietaFiltering("macros.specified=true", "macros.specified=false");
    }

    @Test
    @Transactional
    void getAllDietasByMacrosContainsSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where macros contains
        defaultDietaFiltering("macros.contains=" + DEFAULT_MACROS, "macros.contains=" + UPDATED_MACROS);
    }

    @Test
    @Transactional
    void getAllDietasByMacrosNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where macros does not contain
        defaultDietaFiltering("macros.doesNotContain=" + UPDATED_MACROS, "macros.doesNotContain=" + DEFAULT_MACROS);
    }

    @Test
    @Transactional
    void getAllDietasByFabbisognoCaloricoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where fabbisognoCalorico equals to
        defaultDietaFiltering(
            "fabbisognoCalorico.equals=" + DEFAULT_FABBISOGNO_CALORICO,
            "fabbisognoCalorico.equals=" + UPDATED_FABBISOGNO_CALORICO
        );
    }

    @Test
    @Transactional
    void getAllDietasByFabbisognoCaloricoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where fabbisognoCalorico in
        defaultDietaFiltering(
            "fabbisognoCalorico.in=" + DEFAULT_FABBISOGNO_CALORICO + "," + UPDATED_FABBISOGNO_CALORICO,
            "fabbisognoCalorico.in=" + UPDATED_FABBISOGNO_CALORICO
        );
    }

    @Test
    @Transactional
    void getAllDietasByFabbisognoCaloricoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where fabbisognoCalorico is not null
        defaultDietaFiltering("fabbisognoCalorico.specified=true", "fabbisognoCalorico.specified=false");
    }

    @Test
    @Transactional
    void getAllDietasByFabbisognoCaloricoContainsSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where fabbisognoCalorico contains
        defaultDietaFiltering(
            "fabbisognoCalorico.contains=" + DEFAULT_FABBISOGNO_CALORICO,
            "fabbisognoCalorico.contains=" + UPDATED_FABBISOGNO_CALORICO
        );
    }

    @Test
    @Transactional
    void getAllDietasByFabbisognoCaloricoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where fabbisognoCalorico does not contain
        defaultDietaFiltering(
            "fabbisognoCalorico.doesNotContain=" + UPDATED_FABBISOGNO_CALORICO,
            "fabbisognoCalorico.doesNotContain=" + DEFAULT_FABBISOGNO_CALORICO
        );
    }

    @Test
    @Transactional
    void getAllDietasByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            dietaRepository.saveAndFlush(dieta);
            cliente = ClienteResourceIT.createEntity(em);
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        em.persist(cliente);
        em.flush();
        dieta.setCliente(cliente);
        dietaRepository.saveAndFlush(dieta);
        Long clienteId = cliente.getId();
        // Get all the dietaList where cliente equals to clienteId
        defaultDietaShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the dietaList where cliente equals to (clienteId + 1)
        defaultDietaShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    private void defaultDietaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDietaShouldBeFound(shouldBeFound);
        defaultDietaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDietaShouldBeFound(String filter) throws Exception {
        restDietaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dieta.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataCreazione").value(hasItem(DEFAULT_DATA_CREAZIONE.toString())))
            .andExpect(jsonPath("$.[*].mese").value(hasItem(DEFAULT_MESE)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].macros").value(hasItem(DEFAULT_MACROS)))
            .andExpect(jsonPath("$.[*].fabbisognoCalorico").value(hasItem(DEFAULT_FABBISOGNO_CALORICO)));

        // Check, that the count call also returns 1
        restDietaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDietaShouldNotBeFound(String filter) throws Exception {
        restDietaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDietaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDieta() throws Exception {
        // Get the dieta
        restDietaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDieta() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dieta
        Dieta updatedDieta = dietaRepository.findById(dieta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDieta are not directly saved in db
        em.detach(updatedDieta);
        updatedDieta
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .mese(UPDATED_MESE)
            .tipo(UPDATED_TIPO)
            .macros(UPDATED_MACROS)
            .fabbisognoCalorico(UPDATED_FABBISOGNO_CALORICO);
        DietaDTO dietaDTO = dietaMapper.toDto(updatedDieta);

        restDietaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dietaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dietaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Dieta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDietaToMatchAllProperties(updatedDieta);
    }

    @Test
    @Transactional
    void putNonExistingDieta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dieta.setId(longCount.incrementAndGet());

        // Create the Dieta
        DietaDTO dietaDTO = dietaMapper.toDto(dieta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDietaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dietaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dietaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dieta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDieta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dieta.setId(longCount.incrementAndGet());

        // Create the Dieta
        DietaDTO dietaDTO = dietaMapper.toDto(dieta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDietaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dietaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dieta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDieta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dieta.setId(longCount.incrementAndGet());

        // Create the Dieta
        DietaDTO dietaDTO = dietaMapper.toDto(dieta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDietaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dietaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dieta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDietaWithPatch() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dieta using partial update
        Dieta partialUpdatedDieta = new Dieta();
        partialUpdatedDieta.setId(dieta.getId());

        partialUpdatedDieta.tipo(UPDATED_TIPO).macros(UPDATED_MACROS).fabbisognoCalorico(UPDATED_FABBISOGNO_CALORICO);

        restDietaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDieta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDieta))
            )
            .andExpect(status().isOk());

        // Validate the Dieta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDietaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDieta, dieta), getPersistedDieta(dieta));
    }

    @Test
    @Transactional
    void fullUpdateDietaWithPatch() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dieta using partial update
        Dieta partialUpdatedDieta = new Dieta();
        partialUpdatedDieta.setId(dieta.getId());

        partialUpdatedDieta
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .mese(UPDATED_MESE)
            .tipo(UPDATED_TIPO)
            .macros(UPDATED_MACROS)
            .fabbisognoCalorico(UPDATED_FABBISOGNO_CALORICO);

        restDietaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDieta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDieta))
            )
            .andExpect(status().isOk());

        // Validate the Dieta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDietaUpdatableFieldsEquals(partialUpdatedDieta, getPersistedDieta(partialUpdatedDieta));
    }

    @Test
    @Transactional
    void patchNonExistingDieta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dieta.setId(longCount.incrementAndGet());

        // Create the Dieta
        DietaDTO dietaDTO = dietaMapper.toDto(dieta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDietaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dietaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dietaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dieta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDieta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dieta.setId(longCount.incrementAndGet());

        // Create the Dieta
        DietaDTO dietaDTO = dietaMapper.toDto(dieta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDietaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dietaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dieta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDieta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dieta.setId(longCount.incrementAndGet());

        // Create the Dieta
        DietaDTO dietaDTO = dietaMapper.toDto(dieta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDietaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dietaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dieta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDieta() throws Exception {
        // Initialize the database
        insertedDieta = dietaRepository.saveAndFlush(dieta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dieta
        restDietaMockMvc
            .perform(delete(ENTITY_API_URL_ID, dieta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dietaRepository.count();
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

    protected Dieta getPersistedDieta(Dieta dieta) {
        return dietaRepository.findById(dieta.getId()).orElseThrow();
    }

    protected void assertPersistedDietaToMatchAllProperties(Dieta expectedDieta) {
        assertDietaAllPropertiesEquals(expectedDieta, getPersistedDieta(expectedDieta));
    }

    protected void assertPersistedDietaToMatchUpdatableProperties(Dieta expectedDieta) {
        assertDietaAllUpdatablePropertiesEquals(expectedDieta, getPersistedDieta(expectedDieta));
    }
}
