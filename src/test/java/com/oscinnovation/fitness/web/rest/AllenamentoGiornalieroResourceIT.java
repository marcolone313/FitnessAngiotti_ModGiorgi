package com.oscinnovation.fitness.web.rest;

import static com.oscinnovation.fitness.domain.AllenamentoGiornalieroAsserts.*;
import static com.oscinnovation.fitness.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscinnovation.fitness.IntegrationTest;
import com.oscinnovation.fitness.domain.AllenamentoGiornaliero;
import com.oscinnovation.fitness.domain.SchedaSettimanale;
import com.oscinnovation.fitness.domain.enumeration.TipoAllenamento;
import com.oscinnovation.fitness.repository.AllenamentoGiornalieroRepository;
import com.oscinnovation.fitness.service.dto.AllenamentoGiornalieroDTO;
import com.oscinnovation.fitness.service.mapper.AllenamentoGiornalieroMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AllenamentoGiornalieroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AllenamentoGiornalieroResourceIT {

    private static final TipoAllenamento DEFAULT_TIPO = TipoAllenamento.CORSA;
    private static final TipoAllenamento UPDATED_TIPO = TipoAllenamento.GYM;

    private static final Integer DEFAULT_GIORNO = 1;
    private static final Integer UPDATED_GIORNO = 2;
    private static final Integer SMALLER_GIORNO = 1 - 1;

    private static final String DEFAULT_NOTA_TRAINER = "AAAAAAAAAA";
    private static final String UPDATED_NOTA_TRAINER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SVOLTO = false;
    private static final Boolean UPDATED_SVOLTO = true;

    private static final LocalDate DEFAULT_DATA_ALLENAMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_ALLENAMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_ALLENAMENTO = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/allenamento-giornalieros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AllenamentoGiornalieroRepository allenamentoGiornalieroRepository;

    @Autowired
    private AllenamentoGiornalieroMapper allenamentoGiornalieroMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAllenamentoGiornalieroMockMvc;

    private AllenamentoGiornaliero allenamentoGiornaliero;

    private AllenamentoGiornaliero insertedAllenamentoGiornaliero;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AllenamentoGiornaliero createEntity(EntityManager em) {
        AllenamentoGiornaliero allenamentoGiornaliero = new AllenamentoGiornaliero()
            .tipo(DEFAULT_TIPO)
            .giorno(DEFAULT_GIORNO)
            .notaTrainer(DEFAULT_NOTA_TRAINER)
            .svolto(DEFAULT_SVOLTO)
            .dataAllenamento(DEFAULT_DATA_ALLENAMENTO);
        // Add required entity
        SchedaSettimanale schedaSettimanale;
        if (TestUtil.findAll(em, SchedaSettimanale.class).isEmpty()) {
            schedaSettimanale = SchedaSettimanaleResourceIT.createEntity(em);
            em.persist(schedaSettimanale);
            em.flush();
        } else {
            schedaSettimanale = TestUtil.findAll(em, SchedaSettimanale.class).get(0);
        }
        allenamentoGiornaliero.setSchedaSettimanale(schedaSettimanale);
        return allenamentoGiornaliero;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AllenamentoGiornaliero createUpdatedEntity(EntityManager em) {
        AllenamentoGiornaliero updatedAllenamentoGiornaliero = new AllenamentoGiornaliero()
            .tipo(UPDATED_TIPO)
            .giorno(UPDATED_GIORNO)
            .notaTrainer(UPDATED_NOTA_TRAINER)
            .svolto(UPDATED_SVOLTO)
            .dataAllenamento(UPDATED_DATA_ALLENAMENTO);
        // Add required entity
        SchedaSettimanale schedaSettimanale;
        if (TestUtil.findAll(em, SchedaSettimanale.class).isEmpty()) {
            schedaSettimanale = SchedaSettimanaleResourceIT.createUpdatedEntity(em);
            em.persist(schedaSettimanale);
            em.flush();
        } else {
            schedaSettimanale = TestUtil.findAll(em, SchedaSettimanale.class).get(0);
        }
        updatedAllenamentoGiornaliero.setSchedaSettimanale(schedaSettimanale);
        return updatedAllenamentoGiornaliero;
    }

    @BeforeEach
    public void initTest() {
        allenamentoGiornaliero = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAllenamentoGiornaliero != null) {
            allenamentoGiornalieroRepository.delete(insertedAllenamentoGiornaliero);
            insertedAllenamentoGiornaliero = null;
        }
    }

    @Test
    @Transactional
    void createAllenamentoGiornaliero() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AllenamentoGiornaliero
        AllenamentoGiornalieroDTO allenamentoGiornalieroDTO = allenamentoGiornalieroMapper.toDto(allenamentoGiornaliero);
        var returnedAllenamentoGiornalieroDTO = om.readValue(
            restAllenamentoGiornalieroMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allenamentoGiornalieroDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AllenamentoGiornalieroDTO.class
        );

        // Validate the AllenamentoGiornaliero in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAllenamentoGiornaliero = allenamentoGiornalieroMapper.toEntity(returnedAllenamentoGiornalieroDTO);
        assertAllenamentoGiornalieroUpdatableFieldsEquals(
            returnedAllenamentoGiornaliero,
            getPersistedAllenamentoGiornaliero(returnedAllenamentoGiornaliero)
        );

        insertedAllenamentoGiornaliero = returnedAllenamentoGiornaliero;
    }

    @Test
    @Transactional
    void createAllenamentoGiornalieroWithExistingId() throws Exception {
        // Create the AllenamentoGiornaliero with an existing ID
        allenamentoGiornaliero.setId(1L);
        AllenamentoGiornalieroDTO allenamentoGiornalieroDTO = allenamentoGiornalieroMapper.toDto(allenamentoGiornaliero);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAllenamentoGiornalieroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allenamentoGiornalieroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AllenamentoGiornaliero in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        allenamentoGiornaliero.setTipo(null);

        // Create the AllenamentoGiornaliero, which fails.
        AllenamentoGiornalieroDTO allenamentoGiornalieroDTO = allenamentoGiornalieroMapper.toDto(allenamentoGiornaliero);

        restAllenamentoGiornalieroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allenamentoGiornalieroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGiornoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        allenamentoGiornaliero.setGiorno(null);

        // Create the AllenamentoGiornaliero, which fails.
        AllenamentoGiornalieroDTO allenamentoGiornalieroDTO = allenamentoGiornalieroMapper.toDto(allenamentoGiornaliero);

        restAllenamentoGiornalieroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allenamentoGiornalieroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalieros() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList
        restAllenamentoGiornalieroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allenamentoGiornaliero.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].giorno").value(hasItem(DEFAULT_GIORNO)))
            .andExpect(jsonPath("$.[*].notaTrainer").value(hasItem(DEFAULT_NOTA_TRAINER.toString())))
            .andExpect(jsonPath("$.[*].svolto").value(hasItem(DEFAULT_SVOLTO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataAllenamento").value(hasItem(DEFAULT_DATA_ALLENAMENTO.toString())));
    }

    @Test
    @Transactional
    void getAllenamentoGiornaliero() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get the allenamentoGiornaliero
        restAllenamentoGiornalieroMockMvc
            .perform(get(ENTITY_API_URL_ID, allenamentoGiornaliero.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(allenamentoGiornaliero.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.giorno").value(DEFAULT_GIORNO))
            .andExpect(jsonPath("$.notaTrainer").value(DEFAULT_NOTA_TRAINER.toString()))
            .andExpect(jsonPath("$.svolto").value(DEFAULT_SVOLTO.booleanValue()))
            .andExpect(jsonPath("$.dataAllenamento").value(DEFAULT_DATA_ALLENAMENTO.toString()));
    }

    @Test
    @Transactional
    void getAllenamentoGiornalierosByIdFiltering() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        Long id = allenamentoGiornaliero.getId();

        defaultAllenamentoGiornalieroFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAllenamentoGiornalieroFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAllenamentoGiornalieroFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where tipo equals to
        defaultAllenamentoGiornalieroFiltering("tipo.equals=" + DEFAULT_TIPO, "tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where tipo in
        defaultAllenamentoGiornalieroFiltering("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO, "tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where tipo is not null
        defaultAllenamentoGiornalieroFiltering("tipo.specified=true", "tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosByGiornoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where giorno equals to
        defaultAllenamentoGiornalieroFiltering("giorno.equals=" + DEFAULT_GIORNO, "giorno.equals=" + UPDATED_GIORNO);
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosByGiornoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where giorno in
        defaultAllenamentoGiornalieroFiltering("giorno.in=" + DEFAULT_GIORNO + "," + UPDATED_GIORNO, "giorno.in=" + UPDATED_GIORNO);
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosByGiornoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where giorno is not null
        defaultAllenamentoGiornalieroFiltering("giorno.specified=true", "giorno.specified=false");
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosByGiornoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where giorno is greater than or equal to
        defaultAllenamentoGiornalieroFiltering(
            "giorno.greaterThanOrEqual=" + DEFAULT_GIORNO,
            "giorno.greaterThanOrEqual=" + (DEFAULT_GIORNO + 1)
        );
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosByGiornoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where giorno is less than or equal to
        defaultAllenamentoGiornalieroFiltering("giorno.lessThanOrEqual=" + DEFAULT_GIORNO, "giorno.lessThanOrEqual=" + SMALLER_GIORNO);
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosByGiornoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where giorno is less than
        defaultAllenamentoGiornalieroFiltering("giorno.lessThan=" + (DEFAULT_GIORNO + 1), "giorno.lessThan=" + DEFAULT_GIORNO);
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosByGiornoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where giorno is greater than
        defaultAllenamentoGiornalieroFiltering("giorno.greaterThan=" + SMALLER_GIORNO, "giorno.greaterThan=" + DEFAULT_GIORNO);
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosBySvoltoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where svolto equals to
        defaultAllenamentoGiornalieroFiltering("svolto.equals=" + DEFAULT_SVOLTO, "svolto.equals=" + UPDATED_SVOLTO);
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosBySvoltoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where svolto in
        defaultAllenamentoGiornalieroFiltering("svolto.in=" + DEFAULT_SVOLTO + "," + UPDATED_SVOLTO, "svolto.in=" + UPDATED_SVOLTO);
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosBySvoltoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where svolto is not null
        defaultAllenamentoGiornalieroFiltering("svolto.specified=true", "svolto.specified=false");
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosByDataAllenamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where dataAllenamento equals to
        defaultAllenamentoGiornalieroFiltering(
            "dataAllenamento.equals=" + DEFAULT_DATA_ALLENAMENTO,
            "dataAllenamento.equals=" + UPDATED_DATA_ALLENAMENTO
        );
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosByDataAllenamentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where dataAllenamento in
        defaultAllenamentoGiornalieroFiltering(
            "dataAllenamento.in=" + DEFAULT_DATA_ALLENAMENTO + "," + UPDATED_DATA_ALLENAMENTO,
            "dataAllenamento.in=" + UPDATED_DATA_ALLENAMENTO
        );
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosByDataAllenamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where dataAllenamento is not null
        defaultAllenamentoGiornalieroFiltering("dataAllenamento.specified=true", "dataAllenamento.specified=false");
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosByDataAllenamentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where dataAllenamento is greater than or equal to
        defaultAllenamentoGiornalieroFiltering(
            "dataAllenamento.greaterThanOrEqual=" + DEFAULT_DATA_ALLENAMENTO,
            "dataAllenamento.greaterThanOrEqual=" + UPDATED_DATA_ALLENAMENTO
        );
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosByDataAllenamentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where dataAllenamento is less than or equal to
        defaultAllenamentoGiornalieroFiltering(
            "dataAllenamento.lessThanOrEqual=" + DEFAULT_DATA_ALLENAMENTO,
            "dataAllenamento.lessThanOrEqual=" + SMALLER_DATA_ALLENAMENTO
        );
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosByDataAllenamentoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where dataAllenamento is less than
        defaultAllenamentoGiornalieroFiltering(
            "dataAllenamento.lessThan=" + UPDATED_DATA_ALLENAMENTO,
            "dataAllenamento.lessThan=" + DEFAULT_DATA_ALLENAMENTO
        );
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosByDataAllenamentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        // Get all the allenamentoGiornalieroList where dataAllenamento is greater than
        defaultAllenamentoGiornalieroFiltering(
            "dataAllenamento.greaterThan=" + SMALLER_DATA_ALLENAMENTO,
            "dataAllenamento.greaterThan=" + DEFAULT_DATA_ALLENAMENTO
        );
    }

    @Test
    @Transactional
    void getAllAllenamentoGiornalierosBySchedaSettimanaleIsEqualToSomething() throws Exception {
        SchedaSettimanale schedaSettimanale;
        if (TestUtil.findAll(em, SchedaSettimanale.class).isEmpty()) {
            allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);
            schedaSettimanale = SchedaSettimanaleResourceIT.createEntity(em);
        } else {
            schedaSettimanale = TestUtil.findAll(em, SchedaSettimanale.class).get(0);
        }
        em.persist(schedaSettimanale);
        em.flush();
        allenamentoGiornaliero.setSchedaSettimanale(schedaSettimanale);
        allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);
        Long schedaSettimanaleId = schedaSettimanale.getId();
        // Get all the allenamentoGiornalieroList where schedaSettimanale equals to schedaSettimanaleId
        defaultAllenamentoGiornalieroShouldBeFound("schedaSettimanaleId.equals=" + schedaSettimanaleId);

        // Get all the allenamentoGiornalieroList where schedaSettimanale equals to (schedaSettimanaleId + 1)
        defaultAllenamentoGiornalieroShouldNotBeFound("schedaSettimanaleId.equals=" + (schedaSettimanaleId + 1));
    }

    private void defaultAllenamentoGiornalieroFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAllenamentoGiornalieroShouldBeFound(shouldBeFound);
        defaultAllenamentoGiornalieroShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAllenamentoGiornalieroShouldBeFound(String filter) throws Exception {
        restAllenamentoGiornalieroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allenamentoGiornaliero.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].giorno").value(hasItem(DEFAULT_GIORNO)))
            .andExpect(jsonPath("$.[*].notaTrainer").value(hasItem(DEFAULT_NOTA_TRAINER.toString())))
            .andExpect(jsonPath("$.[*].svolto").value(hasItem(DEFAULT_SVOLTO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataAllenamento").value(hasItem(DEFAULT_DATA_ALLENAMENTO.toString())));

        // Check, that the count call also returns 1
        restAllenamentoGiornalieroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAllenamentoGiornalieroShouldNotBeFound(String filter) throws Exception {
        restAllenamentoGiornalieroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAllenamentoGiornalieroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAllenamentoGiornaliero() throws Exception {
        // Get the allenamentoGiornaliero
        restAllenamentoGiornalieroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAllenamentoGiornaliero() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allenamentoGiornaliero
        AllenamentoGiornaliero updatedAllenamentoGiornaliero = allenamentoGiornalieroRepository
            .findById(allenamentoGiornaliero.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAllenamentoGiornaliero are not directly saved in db
        em.detach(updatedAllenamentoGiornaliero);
        updatedAllenamentoGiornaliero
            .tipo(UPDATED_TIPO)
            .giorno(UPDATED_GIORNO)
            .notaTrainer(UPDATED_NOTA_TRAINER)
            .svolto(UPDATED_SVOLTO)
            .dataAllenamento(UPDATED_DATA_ALLENAMENTO);
        AllenamentoGiornalieroDTO allenamentoGiornalieroDTO = allenamentoGiornalieroMapper.toDto(updatedAllenamentoGiornaliero);

        restAllenamentoGiornalieroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, allenamentoGiornalieroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(allenamentoGiornalieroDTO))
            )
            .andExpect(status().isOk());

        // Validate the AllenamentoGiornaliero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAllenamentoGiornalieroToMatchAllProperties(updatedAllenamentoGiornaliero);
    }

    @Test
    @Transactional
    void putNonExistingAllenamentoGiornaliero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allenamentoGiornaliero.setId(longCount.incrementAndGet());

        // Create the AllenamentoGiornaliero
        AllenamentoGiornalieroDTO allenamentoGiornalieroDTO = allenamentoGiornalieroMapper.toDto(allenamentoGiornaliero);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllenamentoGiornalieroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, allenamentoGiornalieroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(allenamentoGiornalieroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllenamentoGiornaliero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAllenamentoGiornaliero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allenamentoGiornaliero.setId(longCount.incrementAndGet());

        // Create the AllenamentoGiornaliero
        AllenamentoGiornalieroDTO allenamentoGiornalieroDTO = allenamentoGiornalieroMapper.toDto(allenamentoGiornaliero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllenamentoGiornalieroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(allenamentoGiornalieroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllenamentoGiornaliero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAllenamentoGiornaliero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allenamentoGiornaliero.setId(longCount.incrementAndGet());

        // Create the AllenamentoGiornaliero
        AllenamentoGiornalieroDTO allenamentoGiornalieroDTO = allenamentoGiornalieroMapper.toDto(allenamentoGiornaliero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllenamentoGiornalieroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allenamentoGiornalieroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AllenamentoGiornaliero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAllenamentoGiornalieroWithPatch() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allenamentoGiornaliero using partial update
        AllenamentoGiornaliero partialUpdatedAllenamentoGiornaliero = new AllenamentoGiornaliero();
        partialUpdatedAllenamentoGiornaliero.setId(allenamentoGiornaliero.getId());

        partialUpdatedAllenamentoGiornaliero
            .tipo(UPDATED_TIPO)
            .notaTrainer(UPDATED_NOTA_TRAINER)
            .svolto(UPDATED_SVOLTO)
            .dataAllenamento(UPDATED_DATA_ALLENAMENTO);

        restAllenamentoGiornalieroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllenamentoGiornaliero.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAllenamentoGiornaliero))
            )
            .andExpect(status().isOk());

        // Validate the AllenamentoGiornaliero in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAllenamentoGiornalieroUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAllenamentoGiornaliero, allenamentoGiornaliero),
            getPersistedAllenamentoGiornaliero(allenamentoGiornaliero)
        );
    }

    @Test
    @Transactional
    void fullUpdateAllenamentoGiornalieroWithPatch() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allenamentoGiornaliero using partial update
        AllenamentoGiornaliero partialUpdatedAllenamentoGiornaliero = new AllenamentoGiornaliero();
        partialUpdatedAllenamentoGiornaliero.setId(allenamentoGiornaliero.getId());

        partialUpdatedAllenamentoGiornaliero
            .tipo(UPDATED_TIPO)
            .giorno(UPDATED_GIORNO)
            .notaTrainer(UPDATED_NOTA_TRAINER)
            .svolto(UPDATED_SVOLTO)
            .dataAllenamento(UPDATED_DATA_ALLENAMENTO);

        restAllenamentoGiornalieroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllenamentoGiornaliero.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAllenamentoGiornaliero))
            )
            .andExpect(status().isOk());

        // Validate the AllenamentoGiornaliero in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAllenamentoGiornalieroUpdatableFieldsEquals(
            partialUpdatedAllenamentoGiornaliero,
            getPersistedAllenamentoGiornaliero(partialUpdatedAllenamentoGiornaliero)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAllenamentoGiornaliero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allenamentoGiornaliero.setId(longCount.incrementAndGet());

        // Create the AllenamentoGiornaliero
        AllenamentoGiornalieroDTO allenamentoGiornalieroDTO = allenamentoGiornalieroMapper.toDto(allenamentoGiornaliero);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllenamentoGiornalieroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, allenamentoGiornalieroDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(allenamentoGiornalieroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllenamentoGiornaliero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAllenamentoGiornaliero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allenamentoGiornaliero.setId(longCount.incrementAndGet());

        // Create the AllenamentoGiornaliero
        AllenamentoGiornalieroDTO allenamentoGiornalieroDTO = allenamentoGiornalieroMapper.toDto(allenamentoGiornaliero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllenamentoGiornalieroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(allenamentoGiornalieroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllenamentoGiornaliero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAllenamentoGiornaliero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allenamentoGiornaliero.setId(longCount.incrementAndGet());

        // Create the AllenamentoGiornaliero
        AllenamentoGiornalieroDTO allenamentoGiornalieroDTO = allenamentoGiornalieroMapper.toDto(allenamentoGiornaliero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllenamentoGiornalieroMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(allenamentoGiornalieroDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AllenamentoGiornaliero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAllenamentoGiornaliero() throws Exception {
        // Initialize the database
        insertedAllenamentoGiornaliero = allenamentoGiornalieroRepository.saveAndFlush(allenamentoGiornaliero);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the allenamentoGiornaliero
        restAllenamentoGiornalieroMockMvc
            .perform(delete(ENTITY_API_URL_ID, allenamentoGiornaliero.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return allenamentoGiornalieroRepository.count();
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

    protected AllenamentoGiornaliero getPersistedAllenamentoGiornaliero(AllenamentoGiornaliero allenamentoGiornaliero) {
        return allenamentoGiornalieroRepository.findById(allenamentoGiornaliero.getId()).orElseThrow();
    }

    protected void assertPersistedAllenamentoGiornalieroToMatchAllProperties(AllenamentoGiornaliero expectedAllenamentoGiornaliero) {
        assertAllenamentoGiornalieroAllPropertiesEquals(
            expectedAllenamentoGiornaliero,
            getPersistedAllenamentoGiornaliero(expectedAllenamentoGiornaliero)
        );
    }

    protected void assertPersistedAllenamentoGiornalieroToMatchUpdatableProperties(AllenamentoGiornaliero expectedAllenamentoGiornaliero) {
        assertAllenamentoGiornalieroAllUpdatablePropertiesEquals(
            expectedAllenamentoGiornaliero,
            getPersistedAllenamentoGiornaliero(expectedAllenamentoGiornaliero)
        );
    }
}
