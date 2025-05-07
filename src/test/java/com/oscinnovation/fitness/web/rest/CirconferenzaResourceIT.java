package com.oscinnovation.fitness.web.rest;

import static com.oscinnovation.fitness.domain.CirconferenzaAsserts.*;
import static com.oscinnovation.fitness.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscinnovation.fitness.IntegrationTest;
import com.oscinnovation.fitness.domain.Circonferenza;
import com.oscinnovation.fitness.domain.Cliente;
import com.oscinnovation.fitness.repository.CirconferenzaRepository;
import com.oscinnovation.fitness.service.CirconferenzaService;
import com.oscinnovation.fitness.service.dto.CirconferenzaDTO;
import com.oscinnovation.fitness.service.mapper.CirconferenzaMapper;
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
 * Integration tests for the {@link CirconferenzaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CirconferenzaResourceIT {

    private static final Float DEFAULT_TORACE = 0F;
    private static final Float UPDATED_TORACE = 1F;
    private static final Float SMALLER_TORACE = 0F - 1F;

    private static final Float DEFAULT_BRACCIO = 0F;
    private static final Float UPDATED_BRACCIO = 1F;
    private static final Float SMALLER_BRACCIO = 0F - 1F;

    private static final Float DEFAULT_AVAMBRACCIO = 0F;
    private static final Float UPDATED_AVAMBRACCIO = 1F;
    private static final Float SMALLER_AVAMBRACCIO = 0F - 1F;

    private static final Float DEFAULT_OMBELICO = 0F;
    private static final Float UPDATED_OMBELICO = 1F;
    private static final Float SMALLER_OMBELICO = 0F - 1F;

    private static final Float DEFAULT_FIANCHI = 0F;
    private static final Float UPDATED_FIANCHI = 1F;
    private static final Float SMALLER_FIANCHI = 0F - 1F;

    private static final Float DEFAULT_SOTTO_OMBELICO = 0F;
    private static final Float UPDATED_SOTTO_OMBELICO = 1F;
    private static final Float SMALLER_SOTTO_OMBELICO = 0F - 1F;

    private static final Float DEFAULT_VITA = 0F;
    private static final Float UPDATED_VITA = 1F;
    private static final Float SMALLER_VITA = 0F - 1F;

    private static final Float DEFAULT_COSCIA = 0F;
    private static final Float UPDATED_COSCIA = 1F;
    private static final Float SMALLER_COSCIA = 0F - 1F;

    private static final Integer DEFAULT_MESE = 1;
    private static final Integer UPDATED_MESE = 2;
    private static final Integer SMALLER_MESE = 1 - 1;

    private static final LocalDate DEFAULT_DATA_INSERIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_INSERIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_INSERIMENTO = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/circonferenzas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CirconferenzaRepository circonferenzaRepository;

    @Mock
    private CirconferenzaRepository circonferenzaRepositoryMock;

    @Autowired
    private CirconferenzaMapper circonferenzaMapper;

    @Mock
    private CirconferenzaService circonferenzaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCirconferenzaMockMvc;

    private Circonferenza circonferenza;

    private Circonferenza insertedCirconferenza;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Circonferenza createEntity(EntityManager em) {
        Circonferenza circonferenza = new Circonferenza()
            .torace(DEFAULT_TORACE)
            .braccio(DEFAULT_BRACCIO)
            .avambraccio(DEFAULT_AVAMBRACCIO)
            .ombelico(DEFAULT_OMBELICO)
            .fianchi(DEFAULT_FIANCHI)
            .sottoOmbelico(DEFAULT_SOTTO_OMBELICO)
            .vita(DEFAULT_VITA)
            .coscia(DEFAULT_COSCIA)
            .mese(DEFAULT_MESE)
            .dataInserimento(DEFAULT_DATA_INSERIMENTO);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        circonferenza.setCliente(cliente);
        return circonferenza;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Circonferenza createUpdatedEntity(EntityManager em) {
        Circonferenza updatedCirconferenza = new Circonferenza()
            .torace(UPDATED_TORACE)
            .braccio(UPDATED_BRACCIO)
            .avambraccio(UPDATED_AVAMBRACCIO)
            .ombelico(UPDATED_OMBELICO)
            .fianchi(UPDATED_FIANCHI)
            .sottoOmbelico(UPDATED_SOTTO_OMBELICO)
            .vita(UPDATED_VITA)
            .coscia(UPDATED_COSCIA)
            .mese(UPDATED_MESE)
            .dataInserimento(UPDATED_DATA_INSERIMENTO);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createUpdatedEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        updatedCirconferenza.setCliente(cliente);
        return updatedCirconferenza;
    }

    @BeforeEach
    public void initTest() {
        circonferenza = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCirconferenza != null) {
            circonferenzaRepository.delete(insertedCirconferenza);
            insertedCirconferenza = null;
        }
    }

    @Test
    @Transactional
    void createCirconferenza() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Circonferenza
        CirconferenzaDTO circonferenzaDTO = circonferenzaMapper.toDto(circonferenza);
        var returnedCirconferenzaDTO = om.readValue(
            restCirconferenzaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(circonferenzaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CirconferenzaDTO.class
        );

        // Validate the Circonferenza in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCirconferenza = circonferenzaMapper.toEntity(returnedCirconferenzaDTO);
        assertCirconferenzaUpdatableFieldsEquals(returnedCirconferenza, getPersistedCirconferenza(returnedCirconferenza));

        insertedCirconferenza = returnedCirconferenza;
    }

    @Test
    @Transactional
    void createCirconferenzaWithExistingId() throws Exception {
        // Create the Circonferenza with an existing ID
        circonferenza.setId(1L);
        CirconferenzaDTO circonferenzaDTO = circonferenzaMapper.toDto(circonferenza);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCirconferenzaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(circonferenzaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Circonferenza in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMeseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        circonferenza.setMese(null);

        // Create the Circonferenza, which fails.
        CirconferenzaDTO circonferenzaDTO = circonferenzaMapper.toDto(circonferenza);

        restCirconferenzaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(circonferenzaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataInserimentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        circonferenza.setDataInserimento(null);

        // Create the Circonferenza, which fails.
        CirconferenzaDTO circonferenzaDTO = circonferenzaMapper.toDto(circonferenza);

        restCirconferenzaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(circonferenzaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCirconferenzas() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList
        restCirconferenzaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(circonferenza.getId().intValue())))
            .andExpect(jsonPath("$.[*].torace").value(hasItem(DEFAULT_TORACE.doubleValue())))
            .andExpect(jsonPath("$.[*].braccio").value(hasItem(DEFAULT_BRACCIO.doubleValue())))
            .andExpect(jsonPath("$.[*].avambraccio").value(hasItem(DEFAULT_AVAMBRACCIO.doubleValue())))
            .andExpect(jsonPath("$.[*].ombelico").value(hasItem(DEFAULT_OMBELICO.doubleValue())))
            .andExpect(jsonPath("$.[*].fianchi").value(hasItem(DEFAULT_FIANCHI.doubleValue())))
            .andExpect(jsonPath("$.[*].sottoOmbelico").value(hasItem(DEFAULT_SOTTO_OMBELICO.doubleValue())))
            .andExpect(jsonPath("$.[*].vita").value(hasItem(DEFAULT_VITA.doubleValue())))
            .andExpect(jsonPath("$.[*].coscia").value(hasItem(DEFAULT_COSCIA.doubleValue())))
            .andExpect(jsonPath("$.[*].mese").value(hasItem(DEFAULT_MESE)))
            .andExpect(jsonPath("$.[*].dataInserimento").value(hasItem(DEFAULT_DATA_INSERIMENTO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCirconferenzasWithEagerRelationshipsIsEnabled() throws Exception {
        when(circonferenzaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCirconferenzaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(circonferenzaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCirconferenzasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(circonferenzaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCirconferenzaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(circonferenzaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCirconferenza() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get the circonferenza
        restCirconferenzaMockMvc
            .perform(get(ENTITY_API_URL_ID, circonferenza.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(circonferenza.getId().intValue()))
            .andExpect(jsonPath("$.torace").value(DEFAULT_TORACE.doubleValue()))
            .andExpect(jsonPath("$.braccio").value(DEFAULT_BRACCIO.doubleValue()))
            .andExpect(jsonPath("$.avambraccio").value(DEFAULT_AVAMBRACCIO.doubleValue()))
            .andExpect(jsonPath("$.ombelico").value(DEFAULT_OMBELICO.doubleValue()))
            .andExpect(jsonPath("$.fianchi").value(DEFAULT_FIANCHI.doubleValue()))
            .andExpect(jsonPath("$.sottoOmbelico").value(DEFAULT_SOTTO_OMBELICO.doubleValue()))
            .andExpect(jsonPath("$.vita").value(DEFAULT_VITA.doubleValue()))
            .andExpect(jsonPath("$.coscia").value(DEFAULT_COSCIA.doubleValue()))
            .andExpect(jsonPath("$.mese").value(DEFAULT_MESE))
            .andExpect(jsonPath("$.dataInserimento").value(DEFAULT_DATA_INSERIMENTO.toString()));
    }

    @Test
    @Transactional
    void getCirconferenzasByIdFiltering() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        Long id = circonferenza.getId();

        defaultCirconferenzaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCirconferenzaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCirconferenzaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByToraceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where torace equals to
        defaultCirconferenzaFiltering("torace.equals=" + DEFAULT_TORACE, "torace.equals=" + UPDATED_TORACE);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByToraceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where torace in
        defaultCirconferenzaFiltering("torace.in=" + DEFAULT_TORACE + "," + UPDATED_TORACE, "torace.in=" + UPDATED_TORACE);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByToraceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where torace is not null
        defaultCirconferenzaFiltering("torace.specified=true", "torace.specified=false");
    }

    @Test
    @Transactional
    void getAllCirconferenzasByToraceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where torace is greater than or equal to
        defaultCirconferenzaFiltering("torace.greaterThanOrEqual=" + DEFAULT_TORACE, "torace.greaterThanOrEqual=" + UPDATED_TORACE);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByToraceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where torace is less than or equal to
        defaultCirconferenzaFiltering("torace.lessThanOrEqual=" + DEFAULT_TORACE, "torace.lessThanOrEqual=" + SMALLER_TORACE);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByToraceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where torace is less than
        defaultCirconferenzaFiltering("torace.lessThan=" + UPDATED_TORACE, "torace.lessThan=" + DEFAULT_TORACE);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByToraceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where torace is greater than
        defaultCirconferenzaFiltering("torace.greaterThan=" + SMALLER_TORACE, "torace.greaterThan=" + DEFAULT_TORACE);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByBraccioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where braccio equals to
        defaultCirconferenzaFiltering("braccio.equals=" + DEFAULT_BRACCIO, "braccio.equals=" + UPDATED_BRACCIO);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByBraccioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where braccio in
        defaultCirconferenzaFiltering("braccio.in=" + DEFAULT_BRACCIO + "," + UPDATED_BRACCIO, "braccio.in=" + UPDATED_BRACCIO);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByBraccioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where braccio is not null
        defaultCirconferenzaFiltering("braccio.specified=true", "braccio.specified=false");
    }

    @Test
    @Transactional
    void getAllCirconferenzasByBraccioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where braccio is greater than or equal to
        defaultCirconferenzaFiltering("braccio.greaterThanOrEqual=" + DEFAULT_BRACCIO, "braccio.greaterThanOrEqual=" + UPDATED_BRACCIO);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByBraccioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where braccio is less than or equal to
        defaultCirconferenzaFiltering("braccio.lessThanOrEqual=" + DEFAULT_BRACCIO, "braccio.lessThanOrEqual=" + SMALLER_BRACCIO);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByBraccioIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where braccio is less than
        defaultCirconferenzaFiltering("braccio.lessThan=" + UPDATED_BRACCIO, "braccio.lessThan=" + DEFAULT_BRACCIO);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByBraccioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where braccio is greater than
        defaultCirconferenzaFiltering("braccio.greaterThan=" + SMALLER_BRACCIO, "braccio.greaterThan=" + DEFAULT_BRACCIO);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByAvambraccioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where avambraccio equals to
        defaultCirconferenzaFiltering("avambraccio.equals=" + DEFAULT_AVAMBRACCIO, "avambraccio.equals=" + UPDATED_AVAMBRACCIO);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByAvambraccioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where avambraccio in
        defaultCirconferenzaFiltering(
            "avambraccio.in=" + DEFAULT_AVAMBRACCIO + "," + UPDATED_AVAMBRACCIO,
            "avambraccio.in=" + UPDATED_AVAMBRACCIO
        );
    }

    @Test
    @Transactional
    void getAllCirconferenzasByAvambraccioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where avambraccio is not null
        defaultCirconferenzaFiltering("avambraccio.specified=true", "avambraccio.specified=false");
    }

    @Test
    @Transactional
    void getAllCirconferenzasByAvambraccioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where avambraccio is greater than or equal to
        defaultCirconferenzaFiltering(
            "avambraccio.greaterThanOrEqual=" + DEFAULT_AVAMBRACCIO,
            "avambraccio.greaterThanOrEqual=" + UPDATED_AVAMBRACCIO
        );
    }

    @Test
    @Transactional
    void getAllCirconferenzasByAvambraccioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where avambraccio is less than or equal to
        defaultCirconferenzaFiltering(
            "avambraccio.lessThanOrEqual=" + DEFAULT_AVAMBRACCIO,
            "avambraccio.lessThanOrEqual=" + SMALLER_AVAMBRACCIO
        );
    }

    @Test
    @Transactional
    void getAllCirconferenzasByAvambraccioIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where avambraccio is less than
        defaultCirconferenzaFiltering("avambraccio.lessThan=" + UPDATED_AVAMBRACCIO, "avambraccio.lessThan=" + DEFAULT_AVAMBRACCIO);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByAvambraccioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where avambraccio is greater than
        defaultCirconferenzaFiltering("avambraccio.greaterThan=" + SMALLER_AVAMBRACCIO, "avambraccio.greaterThan=" + DEFAULT_AVAMBRACCIO);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByOmbelicoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where ombelico equals to
        defaultCirconferenzaFiltering("ombelico.equals=" + DEFAULT_OMBELICO, "ombelico.equals=" + UPDATED_OMBELICO);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByOmbelicoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where ombelico in
        defaultCirconferenzaFiltering("ombelico.in=" + DEFAULT_OMBELICO + "," + UPDATED_OMBELICO, "ombelico.in=" + UPDATED_OMBELICO);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByOmbelicoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where ombelico is not null
        defaultCirconferenzaFiltering("ombelico.specified=true", "ombelico.specified=false");
    }

    @Test
    @Transactional
    void getAllCirconferenzasByOmbelicoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where ombelico is greater than or equal to
        defaultCirconferenzaFiltering("ombelico.greaterThanOrEqual=" + DEFAULT_OMBELICO, "ombelico.greaterThanOrEqual=" + UPDATED_OMBELICO);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByOmbelicoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where ombelico is less than or equal to
        defaultCirconferenzaFiltering("ombelico.lessThanOrEqual=" + DEFAULT_OMBELICO, "ombelico.lessThanOrEqual=" + SMALLER_OMBELICO);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByOmbelicoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where ombelico is less than
        defaultCirconferenzaFiltering("ombelico.lessThan=" + UPDATED_OMBELICO, "ombelico.lessThan=" + DEFAULT_OMBELICO);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByOmbelicoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where ombelico is greater than
        defaultCirconferenzaFiltering("ombelico.greaterThan=" + SMALLER_OMBELICO, "ombelico.greaterThan=" + DEFAULT_OMBELICO);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByFianchiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where fianchi equals to
        defaultCirconferenzaFiltering("fianchi.equals=" + DEFAULT_FIANCHI, "fianchi.equals=" + UPDATED_FIANCHI);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByFianchiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where fianchi in
        defaultCirconferenzaFiltering("fianchi.in=" + DEFAULT_FIANCHI + "," + UPDATED_FIANCHI, "fianchi.in=" + UPDATED_FIANCHI);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByFianchiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where fianchi is not null
        defaultCirconferenzaFiltering("fianchi.specified=true", "fianchi.specified=false");
    }

    @Test
    @Transactional
    void getAllCirconferenzasByFianchiIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where fianchi is greater than or equal to
        defaultCirconferenzaFiltering("fianchi.greaterThanOrEqual=" + DEFAULT_FIANCHI, "fianchi.greaterThanOrEqual=" + UPDATED_FIANCHI);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByFianchiIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where fianchi is less than or equal to
        defaultCirconferenzaFiltering("fianchi.lessThanOrEqual=" + DEFAULT_FIANCHI, "fianchi.lessThanOrEqual=" + SMALLER_FIANCHI);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByFianchiIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where fianchi is less than
        defaultCirconferenzaFiltering("fianchi.lessThan=" + UPDATED_FIANCHI, "fianchi.lessThan=" + DEFAULT_FIANCHI);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByFianchiIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where fianchi is greater than
        defaultCirconferenzaFiltering("fianchi.greaterThan=" + SMALLER_FIANCHI, "fianchi.greaterThan=" + DEFAULT_FIANCHI);
    }

    @Test
    @Transactional
    void getAllCirconferenzasBySottoOmbelicoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where sottoOmbelico equals to
        defaultCirconferenzaFiltering("sottoOmbelico.equals=" + DEFAULT_SOTTO_OMBELICO, "sottoOmbelico.equals=" + UPDATED_SOTTO_OMBELICO);
    }

    @Test
    @Transactional
    void getAllCirconferenzasBySottoOmbelicoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where sottoOmbelico in
        defaultCirconferenzaFiltering(
            "sottoOmbelico.in=" + DEFAULT_SOTTO_OMBELICO + "," + UPDATED_SOTTO_OMBELICO,
            "sottoOmbelico.in=" + UPDATED_SOTTO_OMBELICO
        );
    }

    @Test
    @Transactional
    void getAllCirconferenzasBySottoOmbelicoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where sottoOmbelico is not null
        defaultCirconferenzaFiltering("sottoOmbelico.specified=true", "sottoOmbelico.specified=false");
    }

    @Test
    @Transactional
    void getAllCirconferenzasBySottoOmbelicoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where sottoOmbelico is greater than or equal to
        defaultCirconferenzaFiltering(
            "sottoOmbelico.greaterThanOrEqual=" + DEFAULT_SOTTO_OMBELICO,
            "sottoOmbelico.greaterThanOrEqual=" + UPDATED_SOTTO_OMBELICO
        );
    }

    @Test
    @Transactional
    void getAllCirconferenzasBySottoOmbelicoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where sottoOmbelico is less than or equal to
        defaultCirconferenzaFiltering(
            "sottoOmbelico.lessThanOrEqual=" + DEFAULT_SOTTO_OMBELICO,
            "sottoOmbelico.lessThanOrEqual=" + SMALLER_SOTTO_OMBELICO
        );
    }

    @Test
    @Transactional
    void getAllCirconferenzasBySottoOmbelicoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where sottoOmbelico is less than
        defaultCirconferenzaFiltering(
            "sottoOmbelico.lessThan=" + UPDATED_SOTTO_OMBELICO,
            "sottoOmbelico.lessThan=" + DEFAULT_SOTTO_OMBELICO
        );
    }

    @Test
    @Transactional
    void getAllCirconferenzasBySottoOmbelicoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where sottoOmbelico is greater than
        defaultCirconferenzaFiltering(
            "sottoOmbelico.greaterThan=" + SMALLER_SOTTO_OMBELICO,
            "sottoOmbelico.greaterThan=" + DEFAULT_SOTTO_OMBELICO
        );
    }

    @Test
    @Transactional
    void getAllCirconferenzasByVitaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where vita equals to
        defaultCirconferenzaFiltering("vita.equals=" + DEFAULT_VITA, "vita.equals=" + UPDATED_VITA);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByVitaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where vita in
        defaultCirconferenzaFiltering("vita.in=" + DEFAULT_VITA + "," + UPDATED_VITA, "vita.in=" + UPDATED_VITA);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByVitaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where vita is not null
        defaultCirconferenzaFiltering("vita.specified=true", "vita.specified=false");
    }

    @Test
    @Transactional
    void getAllCirconferenzasByVitaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where vita is greater than or equal to
        defaultCirconferenzaFiltering("vita.greaterThanOrEqual=" + DEFAULT_VITA, "vita.greaterThanOrEqual=" + UPDATED_VITA);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByVitaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where vita is less than or equal to
        defaultCirconferenzaFiltering("vita.lessThanOrEqual=" + DEFAULT_VITA, "vita.lessThanOrEqual=" + SMALLER_VITA);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByVitaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where vita is less than
        defaultCirconferenzaFiltering("vita.lessThan=" + UPDATED_VITA, "vita.lessThan=" + DEFAULT_VITA);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByVitaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where vita is greater than
        defaultCirconferenzaFiltering("vita.greaterThan=" + SMALLER_VITA, "vita.greaterThan=" + DEFAULT_VITA);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByCosciaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where coscia equals to
        defaultCirconferenzaFiltering("coscia.equals=" + DEFAULT_COSCIA, "coscia.equals=" + UPDATED_COSCIA);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByCosciaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where coscia in
        defaultCirconferenzaFiltering("coscia.in=" + DEFAULT_COSCIA + "," + UPDATED_COSCIA, "coscia.in=" + UPDATED_COSCIA);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByCosciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where coscia is not null
        defaultCirconferenzaFiltering("coscia.specified=true", "coscia.specified=false");
    }

    @Test
    @Transactional
    void getAllCirconferenzasByCosciaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where coscia is greater than or equal to
        defaultCirconferenzaFiltering("coscia.greaterThanOrEqual=" + DEFAULT_COSCIA, "coscia.greaterThanOrEqual=" + UPDATED_COSCIA);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByCosciaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where coscia is less than or equal to
        defaultCirconferenzaFiltering("coscia.lessThanOrEqual=" + DEFAULT_COSCIA, "coscia.lessThanOrEqual=" + SMALLER_COSCIA);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByCosciaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where coscia is less than
        defaultCirconferenzaFiltering("coscia.lessThan=" + UPDATED_COSCIA, "coscia.lessThan=" + DEFAULT_COSCIA);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByCosciaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where coscia is greater than
        defaultCirconferenzaFiltering("coscia.greaterThan=" + SMALLER_COSCIA, "coscia.greaterThan=" + DEFAULT_COSCIA);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByMeseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where mese equals to
        defaultCirconferenzaFiltering("mese.equals=" + DEFAULT_MESE, "mese.equals=" + UPDATED_MESE);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByMeseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where mese in
        defaultCirconferenzaFiltering("mese.in=" + DEFAULT_MESE + "," + UPDATED_MESE, "mese.in=" + UPDATED_MESE);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByMeseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where mese is not null
        defaultCirconferenzaFiltering("mese.specified=true", "mese.specified=false");
    }

    @Test
    @Transactional
    void getAllCirconferenzasByMeseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where mese is greater than or equal to
        defaultCirconferenzaFiltering("mese.greaterThanOrEqual=" + DEFAULT_MESE, "mese.greaterThanOrEqual=" + (DEFAULT_MESE + 1));
    }

    @Test
    @Transactional
    void getAllCirconferenzasByMeseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where mese is less than or equal to
        defaultCirconferenzaFiltering("mese.lessThanOrEqual=" + DEFAULT_MESE, "mese.lessThanOrEqual=" + SMALLER_MESE);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByMeseIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where mese is less than
        defaultCirconferenzaFiltering("mese.lessThan=" + (DEFAULT_MESE + 1), "mese.lessThan=" + DEFAULT_MESE);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByMeseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where mese is greater than
        defaultCirconferenzaFiltering("mese.greaterThan=" + SMALLER_MESE, "mese.greaterThan=" + DEFAULT_MESE);
    }

    @Test
    @Transactional
    void getAllCirconferenzasByDataInserimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where dataInserimento equals to
        defaultCirconferenzaFiltering(
            "dataInserimento.equals=" + DEFAULT_DATA_INSERIMENTO,
            "dataInserimento.equals=" + UPDATED_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllCirconferenzasByDataInserimentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where dataInserimento in
        defaultCirconferenzaFiltering(
            "dataInserimento.in=" + DEFAULT_DATA_INSERIMENTO + "," + UPDATED_DATA_INSERIMENTO,
            "dataInserimento.in=" + UPDATED_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllCirconferenzasByDataInserimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where dataInserimento is not null
        defaultCirconferenzaFiltering("dataInserimento.specified=true", "dataInserimento.specified=false");
    }

    @Test
    @Transactional
    void getAllCirconferenzasByDataInserimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where dataInserimento is greater than or equal to
        defaultCirconferenzaFiltering(
            "dataInserimento.greaterThanOrEqual=" + DEFAULT_DATA_INSERIMENTO,
            "dataInserimento.greaterThanOrEqual=" + UPDATED_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllCirconferenzasByDataInserimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where dataInserimento is less than or equal to
        defaultCirconferenzaFiltering(
            "dataInserimento.lessThanOrEqual=" + DEFAULT_DATA_INSERIMENTO,
            "dataInserimento.lessThanOrEqual=" + SMALLER_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllCirconferenzasByDataInserimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where dataInserimento is less than
        defaultCirconferenzaFiltering(
            "dataInserimento.lessThan=" + UPDATED_DATA_INSERIMENTO,
            "dataInserimento.lessThan=" + DEFAULT_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllCirconferenzasByDataInserimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        // Get all the circonferenzaList where dataInserimento is greater than
        defaultCirconferenzaFiltering(
            "dataInserimento.greaterThan=" + SMALLER_DATA_INSERIMENTO,
            "dataInserimento.greaterThan=" + DEFAULT_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllCirconferenzasByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            circonferenzaRepository.saveAndFlush(circonferenza);
            cliente = ClienteResourceIT.createEntity(em);
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        em.persist(cliente);
        em.flush();
        circonferenza.setCliente(cliente);
        circonferenzaRepository.saveAndFlush(circonferenza);
        Long clienteId = cliente.getId();
        // Get all the circonferenzaList where cliente equals to clienteId
        defaultCirconferenzaShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the circonferenzaList where cliente equals to (clienteId + 1)
        defaultCirconferenzaShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    private void defaultCirconferenzaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCirconferenzaShouldBeFound(shouldBeFound);
        defaultCirconferenzaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCirconferenzaShouldBeFound(String filter) throws Exception {
        restCirconferenzaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(circonferenza.getId().intValue())))
            .andExpect(jsonPath("$.[*].torace").value(hasItem(DEFAULT_TORACE.doubleValue())))
            .andExpect(jsonPath("$.[*].braccio").value(hasItem(DEFAULT_BRACCIO.doubleValue())))
            .andExpect(jsonPath("$.[*].avambraccio").value(hasItem(DEFAULT_AVAMBRACCIO.doubleValue())))
            .andExpect(jsonPath("$.[*].ombelico").value(hasItem(DEFAULT_OMBELICO.doubleValue())))
            .andExpect(jsonPath("$.[*].fianchi").value(hasItem(DEFAULT_FIANCHI.doubleValue())))
            .andExpect(jsonPath("$.[*].sottoOmbelico").value(hasItem(DEFAULT_SOTTO_OMBELICO.doubleValue())))
            .andExpect(jsonPath("$.[*].vita").value(hasItem(DEFAULT_VITA.doubleValue())))
            .andExpect(jsonPath("$.[*].coscia").value(hasItem(DEFAULT_COSCIA.doubleValue())))
            .andExpect(jsonPath("$.[*].mese").value(hasItem(DEFAULT_MESE)))
            .andExpect(jsonPath("$.[*].dataInserimento").value(hasItem(DEFAULT_DATA_INSERIMENTO.toString())));

        // Check, that the count call also returns 1
        restCirconferenzaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCirconferenzaShouldNotBeFound(String filter) throws Exception {
        restCirconferenzaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCirconferenzaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCirconferenza() throws Exception {
        // Get the circonferenza
        restCirconferenzaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCirconferenza() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the circonferenza
        Circonferenza updatedCirconferenza = circonferenzaRepository.findById(circonferenza.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCirconferenza are not directly saved in db
        em.detach(updatedCirconferenza);
        updatedCirconferenza
            .torace(UPDATED_TORACE)
            .braccio(UPDATED_BRACCIO)
            .avambraccio(UPDATED_AVAMBRACCIO)
            .ombelico(UPDATED_OMBELICO)
            .fianchi(UPDATED_FIANCHI)
            .sottoOmbelico(UPDATED_SOTTO_OMBELICO)
            .vita(UPDATED_VITA)
            .coscia(UPDATED_COSCIA)
            .mese(UPDATED_MESE)
            .dataInserimento(UPDATED_DATA_INSERIMENTO);
        CirconferenzaDTO circonferenzaDTO = circonferenzaMapper.toDto(updatedCirconferenza);

        restCirconferenzaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, circonferenzaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(circonferenzaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Circonferenza in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCirconferenzaToMatchAllProperties(updatedCirconferenza);
    }

    @Test
    @Transactional
    void putNonExistingCirconferenza() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circonferenza.setId(longCount.incrementAndGet());

        // Create the Circonferenza
        CirconferenzaDTO circonferenzaDTO = circonferenzaMapper.toDto(circonferenza);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCirconferenzaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, circonferenzaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(circonferenzaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Circonferenza in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCirconferenza() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circonferenza.setId(longCount.incrementAndGet());

        // Create the Circonferenza
        CirconferenzaDTO circonferenzaDTO = circonferenzaMapper.toDto(circonferenza);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCirconferenzaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(circonferenzaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Circonferenza in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCirconferenza() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circonferenza.setId(longCount.incrementAndGet());

        // Create the Circonferenza
        CirconferenzaDTO circonferenzaDTO = circonferenzaMapper.toDto(circonferenza);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCirconferenzaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(circonferenzaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Circonferenza in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCirconferenzaWithPatch() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the circonferenza using partial update
        Circonferenza partialUpdatedCirconferenza = new Circonferenza();
        partialUpdatedCirconferenza.setId(circonferenza.getId());

        partialUpdatedCirconferenza
            .torace(UPDATED_TORACE)
            .braccio(UPDATED_BRACCIO)
            .avambraccio(UPDATED_AVAMBRACCIO)
            .sottoOmbelico(UPDATED_SOTTO_OMBELICO)
            .coscia(UPDATED_COSCIA)
            .dataInserimento(UPDATED_DATA_INSERIMENTO);

        restCirconferenzaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCirconferenza.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCirconferenza))
            )
            .andExpect(status().isOk());

        // Validate the Circonferenza in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCirconferenzaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCirconferenza, circonferenza),
            getPersistedCirconferenza(circonferenza)
        );
    }

    @Test
    @Transactional
    void fullUpdateCirconferenzaWithPatch() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the circonferenza using partial update
        Circonferenza partialUpdatedCirconferenza = new Circonferenza();
        partialUpdatedCirconferenza.setId(circonferenza.getId());

        partialUpdatedCirconferenza
            .torace(UPDATED_TORACE)
            .braccio(UPDATED_BRACCIO)
            .avambraccio(UPDATED_AVAMBRACCIO)
            .ombelico(UPDATED_OMBELICO)
            .fianchi(UPDATED_FIANCHI)
            .sottoOmbelico(UPDATED_SOTTO_OMBELICO)
            .vita(UPDATED_VITA)
            .coscia(UPDATED_COSCIA)
            .mese(UPDATED_MESE)
            .dataInserimento(UPDATED_DATA_INSERIMENTO);

        restCirconferenzaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCirconferenza.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCirconferenza))
            )
            .andExpect(status().isOk());

        // Validate the Circonferenza in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCirconferenzaUpdatableFieldsEquals(partialUpdatedCirconferenza, getPersistedCirconferenza(partialUpdatedCirconferenza));
    }

    @Test
    @Transactional
    void patchNonExistingCirconferenza() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circonferenza.setId(longCount.incrementAndGet());

        // Create the Circonferenza
        CirconferenzaDTO circonferenzaDTO = circonferenzaMapper.toDto(circonferenza);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCirconferenzaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, circonferenzaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(circonferenzaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Circonferenza in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCirconferenza() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circonferenza.setId(longCount.incrementAndGet());

        // Create the Circonferenza
        CirconferenzaDTO circonferenzaDTO = circonferenzaMapper.toDto(circonferenza);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCirconferenzaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(circonferenzaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Circonferenza in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCirconferenza() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        circonferenza.setId(longCount.incrementAndGet());

        // Create the Circonferenza
        CirconferenzaDTO circonferenzaDTO = circonferenzaMapper.toDto(circonferenza);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCirconferenzaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(circonferenzaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Circonferenza in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCirconferenza() throws Exception {
        // Initialize the database
        insertedCirconferenza = circonferenzaRepository.saveAndFlush(circonferenza);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the circonferenza
        restCirconferenzaMockMvc
            .perform(delete(ENTITY_API_URL_ID, circonferenza.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return circonferenzaRepository.count();
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

    protected Circonferenza getPersistedCirconferenza(Circonferenza circonferenza) {
        return circonferenzaRepository.findById(circonferenza.getId()).orElseThrow();
    }

    protected void assertPersistedCirconferenzaToMatchAllProperties(Circonferenza expectedCirconferenza) {
        assertCirconferenzaAllPropertiesEquals(expectedCirconferenza, getPersistedCirconferenza(expectedCirconferenza));
    }

    protected void assertPersistedCirconferenzaToMatchUpdatableProperties(Circonferenza expectedCirconferenza) {
        assertCirconferenzaAllUpdatablePropertiesEquals(expectedCirconferenza, getPersistedCirconferenza(expectedCirconferenza));
    }
}
