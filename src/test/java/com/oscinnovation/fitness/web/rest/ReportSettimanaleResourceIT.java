package com.oscinnovation.fitness.web.rest;

import static com.oscinnovation.fitness.domain.ReportSettimanaleAsserts.*;
import static com.oscinnovation.fitness.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscinnovation.fitness.IntegrationTest;
import com.oscinnovation.fitness.domain.Cliente;
import com.oscinnovation.fitness.domain.ReportSettimanale;
import com.oscinnovation.fitness.domain.SchedaSettimanale;
import com.oscinnovation.fitness.domain.enumeration.Voto;
import com.oscinnovation.fitness.domain.enumeration.Voto;
import com.oscinnovation.fitness.domain.enumeration.Voto;
import com.oscinnovation.fitness.repository.ReportSettimanaleRepository;
import com.oscinnovation.fitness.service.ReportSettimanaleService;
import com.oscinnovation.fitness.service.dto.ReportSettimanaleDTO;
import com.oscinnovation.fitness.service.mapper.ReportSettimanaleMapper;
import jakarta.persistence.EntityManager;
import java.time.Duration;
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
 * Integration tests for the {@link ReportSettimanaleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReportSettimanaleResourceIT {

    private static final Voto DEFAULT_VOTO = Voto.INSUFFICIENTE;
    private static final Voto UPDATED_VOTO = Voto.SUFFICIENTE;

    private static final String DEFAULT_COMMENTO_ALLENAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTO_ALLENAMENTO = "BBBBBBBBBB";

    private static final Voto DEFAULT_GIORNI_DIETA = Voto.INSUFFICIENTE;
    private static final Voto UPDATED_GIORNI_DIETA = Voto.SUFFICIENTE;

    private static final Float DEFAULT_PESO_MEDIO = 0F;
    private static final Float UPDATED_PESO_MEDIO = 1F;
    private static final Float SMALLER_PESO_MEDIO = 0F - 1F;

    private static final Voto DEFAULT_QUALITA_SONNO = Voto.INSUFFICIENTE;
    private static final Voto UPDATED_QUALITA_SONNO = Voto.SUFFICIENTE;

    private static final Duration DEFAULT_MEDIA_ORE_SONNO = Duration.ofHours(6);
    private static final Duration UPDATED_MEDIA_ORE_SONNO = Duration.ofHours(12);
    private static final Duration SMALLER_MEDIA_ORE_SONNO = Duration.ofHours(5);

    private static final LocalDate DEFAULT_DATA_CREAZIONE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_CREAZIONE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_CREAZIONE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATA_SCADENZA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_SCADENZA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_SCADENZA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATA_COMPLETAMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_COMPLETAMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_COMPLETAMENTO = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_PUNTUALE = false;
    private static final Boolean UPDATED_PUNTUALE = true;

    private static final String DEFAULT_ANALISI_REPORT = "AAAAAAAAAA";
    private static final String UPDATED_ANALISI_REPORT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-settimanales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportSettimanaleRepository reportSettimanaleRepository;

    @Mock
    private ReportSettimanaleRepository reportSettimanaleRepositoryMock;

    @Autowired
    private ReportSettimanaleMapper reportSettimanaleMapper;

    @Mock
    private ReportSettimanaleService reportSettimanaleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportSettimanaleMockMvc;

    private ReportSettimanale reportSettimanale;

    private ReportSettimanale insertedReportSettimanale;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportSettimanale createEntity(EntityManager em) {
        ReportSettimanale reportSettimanale = new ReportSettimanale()
            .voto(DEFAULT_VOTO)
            .commentoAllenamento(DEFAULT_COMMENTO_ALLENAMENTO)
            .giorniDieta(DEFAULT_GIORNI_DIETA)
            .pesoMedio(DEFAULT_PESO_MEDIO)
            .qualitaSonno(DEFAULT_QUALITA_SONNO)
            .mediaOreSonno(DEFAULT_MEDIA_ORE_SONNO)
            .dataCreazione(DEFAULT_DATA_CREAZIONE)
            .dataScadenza(DEFAULT_DATA_SCADENZA)
            .dataCompletamento(DEFAULT_DATA_COMPLETAMENTO)
            .puntuale(DEFAULT_PUNTUALE)
            .analisiReport(DEFAULT_ANALISI_REPORT);
        // Add required entity
        SchedaSettimanale schedaSettimanale;
        if (TestUtil.findAll(em, SchedaSettimanale.class).isEmpty()) {
            schedaSettimanale = SchedaSettimanaleResourceIT.createEntity(em);
            em.persist(schedaSettimanale);
            em.flush();
        } else {
            schedaSettimanale = TestUtil.findAll(em, SchedaSettimanale.class).get(0);
        }
        reportSettimanale.setSchedaSettimanale(schedaSettimanale);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        reportSettimanale.setCliente(cliente);
        return reportSettimanale;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportSettimanale createUpdatedEntity(EntityManager em) {
        ReportSettimanale updatedReportSettimanale = new ReportSettimanale()
            .voto(UPDATED_VOTO)
            .commentoAllenamento(UPDATED_COMMENTO_ALLENAMENTO)
            .giorniDieta(UPDATED_GIORNI_DIETA)
            .pesoMedio(UPDATED_PESO_MEDIO)
            .qualitaSonno(UPDATED_QUALITA_SONNO)
            .mediaOreSonno(UPDATED_MEDIA_ORE_SONNO)
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .dataScadenza(UPDATED_DATA_SCADENZA)
            .dataCompletamento(UPDATED_DATA_COMPLETAMENTO)
            .puntuale(UPDATED_PUNTUALE)
            .analisiReport(UPDATED_ANALISI_REPORT);
        // Add required entity
        SchedaSettimanale schedaSettimanale;
        if (TestUtil.findAll(em, SchedaSettimanale.class).isEmpty()) {
            schedaSettimanale = SchedaSettimanaleResourceIT.createUpdatedEntity(em);
            em.persist(schedaSettimanale);
            em.flush();
        } else {
            schedaSettimanale = TestUtil.findAll(em, SchedaSettimanale.class).get(0);
        }
        updatedReportSettimanale.setSchedaSettimanale(schedaSettimanale);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createUpdatedEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        updatedReportSettimanale.setCliente(cliente);
        return updatedReportSettimanale;
    }

    @BeforeEach
    public void initTest() {
        reportSettimanale = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedReportSettimanale != null) {
            reportSettimanaleRepository.delete(insertedReportSettimanale);
            insertedReportSettimanale = null;
        }
    }

    @Test
    @Transactional
    void createReportSettimanale() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportSettimanale
        ReportSettimanaleDTO reportSettimanaleDTO = reportSettimanaleMapper.toDto(reportSettimanale);
        var returnedReportSettimanaleDTO = om.readValue(
            restReportSettimanaleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportSettimanaleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportSettimanaleDTO.class
        );

        // Validate the ReportSettimanale in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportSettimanale = reportSettimanaleMapper.toEntity(returnedReportSettimanaleDTO);
        assertReportSettimanaleUpdatableFieldsEquals(returnedReportSettimanale, getPersistedReportSettimanale(returnedReportSettimanale));

        insertedReportSettimanale = returnedReportSettimanale;
    }

    @Test
    @Transactional
    void createReportSettimanaleWithExistingId() throws Exception {
        // Create the ReportSettimanale with an existing ID
        reportSettimanale.setId(1L);
        ReportSettimanaleDTO reportSettimanaleDTO = reportSettimanaleMapper.toDto(reportSettimanale);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportSettimanaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportSettimanaleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportSettimanale in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataCreazioneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportSettimanale.setDataCreazione(null);

        // Create the ReportSettimanale, which fails.
        ReportSettimanaleDTO reportSettimanaleDTO = reportSettimanaleMapper.toDto(reportSettimanale);

        restReportSettimanaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportSettimanaleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataScadenzaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportSettimanale.setDataScadenza(null);

        // Create the ReportSettimanale, which fails.
        ReportSettimanaleDTO reportSettimanaleDTO = reportSettimanaleMapper.toDto(reportSettimanale);

        restReportSettimanaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportSettimanaleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportSettimanales() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList
        restReportSettimanaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportSettimanale.getId().intValue())))
            .andExpect(jsonPath("$.[*].voto").value(hasItem(DEFAULT_VOTO.toString())))
            .andExpect(jsonPath("$.[*].commentoAllenamento").value(hasItem(DEFAULT_COMMENTO_ALLENAMENTO.toString())))
            .andExpect(jsonPath("$.[*].giorniDieta").value(hasItem(DEFAULT_GIORNI_DIETA.toString())))
            .andExpect(jsonPath("$.[*].pesoMedio").value(hasItem(DEFAULT_PESO_MEDIO.doubleValue())))
            .andExpect(jsonPath("$.[*].qualitaSonno").value(hasItem(DEFAULT_QUALITA_SONNO.toString())))
            .andExpect(jsonPath("$.[*].mediaOreSonno").value(hasItem(DEFAULT_MEDIA_ORE_SONNO.toString())))
            .andExpect(jsonPath("$.[*].dataCreazione").value(hasItem(DEFAULT_DATA_CREAZIONE.toString())))
            .andExpect(jsonPath("$.[*].dataScadenza").value(hasItem(DEFAULT_DATA_SCADENZA.toString())))
            .andExpect(jsonPath("$.[*].dataCompletamento").value(hasItem(DEFAULT_DATA_COMPLETAMENTO.toString())))
            .andExpect(jsonPath("$.[*].puntuale").value(hasItem(DEFAULT_PUNTUALE.booleanValue())))
            .andExpect(jsonPath("$.[*].analisiReport").value(hasItem(DEFAULT_ANALISI_REPORT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportSettimanalesWithEagerRelationshipsIsEnabled() throws Exception {
        when(reportSettimanaleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReportSettimanaleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reportSettimanaleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportSettimanalesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reportSettimanaleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReportSettimanaleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reportSettimanaleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReportSettimanale() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get the reportSettimanale
        restReportSettimanaleMockMvc
            .perform(get(ENTITY_API_URL_ID, reportSettimanale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportSettimanale.getId().intValue()))
            .andExpect(jsonPath("$.voto").value(DEFAULT_VOTO.toString()))
            .andExpect(jsonPath("$.commentoAllenamento").value(DEFAULT_COMMENTO_ALLENAMENTO.toString()))
            .andExpect(jsonPath("$.giorniDieta").value(DEFAULT_GIORNI_DIETA.toString()))
            .andExpect(jsonPath("$.pesoMedio").value(DEFAULT_PESO_MEDIO.doubleValue()))
            .andExpect(jsonPath("$.qualitaSonno").value(DEFAULT_QUALITA_SONNO.toString()))
            .andExpect(jsonPath("$.mediaOreSonno").value(DEFAULT_MEDIA_ORE_SONNO.toString()))
            .andExpect(jsonPath("$.dataCreazione").value(DEFAULT_DATA_CREAZIONE.toString()))
            .andExpect(jsonPath("$.dataScadenza").value(DEFAULT_DATA_SCADENZA.toString()))
            .andExpect(jsonPath("$.dataCompletamento").value(DEFAULT_DATA_COMPLETAMENTO.toString()))
            .andExpect(jsonPath("$.puntuale").value(DEFAULT_PUNTUALE.booleanValue()))
            .andExpect(jsonPath("$.analisiReport").value(DEFAULT_ANALISI_REPORT.toString()));
    }

    @Test
    @Transactional
    void getReportSettimanalesByIdFiltering() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        Long id = reportSettimanale.getId();

        defaultReportSettimanaleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReportSettimanaleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReportSettimanaleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByVotoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where voto equals to
        defaultReportSettimanaleFiltering("voto.equals=" + DEFAULT_VOTO, "voto.equals=" + UPDATED_VOTO);
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByVotoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where voto in
        defaultReportSettimanaleFiltering("voto.in=" + DEFAULT_VOTO + "," + UPDATED_VOTO, "voto.in=" + UPDATED_VOTO);
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByVotoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where voto is not null
        defaultReportSettimanaleFiltering("voto.specified=true", "voto.specified=false");
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByGiorniDietaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where giorniDieta equals to
        defaultReportSettimanaleFiltering("giorniDieta.equals=" + DEFAULT_GIORNI_DIETA, "giorniDieta.equals=" + UPDATED_GIORNI_DIETA);
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByGiorniDietaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where giorniDieta in
        defaultReportSettimanaleFiltering(
            "giorniDieta.in=" + DEFAULT_GIORNI_DIETA + "," + UPDATED_GIORNI_DIETA,
            "giorniDieta.in=" + UPDATED_GIORNI_DIETA
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByGiorniDietaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where giorniDieta is not null
        defaultReportSettimanaleFiltering("giorniDieta.specified=true", "giorniDieta.specified=false");
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByPesoMedioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where pesoMedio equals to
        defaultReportSettimanaleFiltering("pesoMedio.equals=" + DEFAULT_PESO_MEDIO, "pesoMedio.equals=" + UPDATED_PESO_MEDIO);
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByPesoMedioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where pesoMedio in
        defaultReportSettimanaleFiltering(
            "pesoMedio.in=" + DEFAULT_PESO_MEDIO + "," + UPDATED_PESO_MEDIO,
            "pesoMedio.in=" + UPDATED_PESO_MEDIO
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByPesoMedioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where pesoMedio is not null
        defaultReportSettimanaleFiltering("pesoMedio.specified=true", "pesoMedio.specified=false");
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByPesoMedioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where pesoMedio is greater than or equal to
        defaultReportSettimanaleFiltering(
            "pesoMedio.greaterThanOrEqual=" + DEFAULT_PESO_MEDIO,
            "pesoMedio.greaterThanOrEqual=" + UPDATED_PESO_MEDIO
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByPesoMedioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where pesoMedio is less than or equal to
        defaultReportSettimanaleFiltering(
            "pesoMedio.lessThanOrEqual=" + DEFAULT_PESO_MEDIO,
            "pesoMedio.lessThanOrEqual=" + SMALLER_PESO_MEDIO
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByPesoMedioIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where pesoMedio is less than
        defaultReportSettimanaleFiltering("pesoMedio.lessThan=" + UPDATED_PESO_MEDIO, "pesoMedio.lessThan=" + DEFAULT_PESO_MEDIO);
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByPesoMedioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where pesoMedio is greater than
        defaultReportSettimanaleFiltering("pesoMedio.greaterThan=" + SMALLER_PESO_MEDIO, "pesoMedio.greaterThan=" + DEFAULT_PESO_MEDIO);
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByQualitaSonnoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where qualitaSonno equals to
        defaultReportSettimanaleFiltering("qualitaSonno.equals=" + DEFAULT_QUALITA_SONNO, "qualitaSonno.equals=" + UPDATED_QUALITA_SONNO);
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByQualitaSonnoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where qualitaSonno in
        defaultReportSettimanaleFiltering(
            "qualitaSonno.in=" + DEFAULT_QUALITA_SONNO + "," + UPDATED_QUALITA_SONNO,
            "qualitaSonno.in=" + UPDATED_QUALITA_SONNO
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByQualitaSonnoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where qualitaSonno is not null
        defaultReportSettimanaleFiltering("qualitaSonno.specified=true", "qualitaSonno.specified=false");
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByMediaOreSonnoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where mediaOreSonno equals to
        defaultReportSettimanaleFiltering(
            "mediaOreSonno.equals=" + DEFAULT_MEDIA_ORE_SONNO,
            "mediaOreSonno.equals=" + UPDATED_MEDIA_ORE_SONNO
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByMediaOreSonnoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where mediaOreSonno in
        defaultReportSettimanaleFiltering(
            "mediaOreSonno.in=" + DEFAULT_MEDIA_ORE_SONNO + "," + UPDATED_MEDIA_ORE_SONNO,
            "mediaOreSonno.in=" + UPDATED_MEDIA_ORE_SONNO
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByMediaOreSonnoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where mediaOreSonno is not null
        defaultReportSettimanaleFiltering("mediaOreSonno.specified=true", "mediaOreSonno.specified=false");
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByMediaOreSonnoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where mediaOreSonno is greater than or equal to
        defaultReportSettimanaleFiltering(
            "mediaOreSonno.greaterThanOrEqual=" + DEFAULT_MEDIA_ORE_SONNO,
            "mediaOreSonno.greaterThanOrEqual=" + UPDATED_MEDIA_ORE_SONNO
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByMediaOreSonnoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where mediaOreSonno is less than or equal to
        defaultReportSettimanaleFiltering(
            "mediaOreSonno.lessThanOrEqual=" + DEFAULT_MEDIA_ORE_SONNO,
            "mediaOreSonno.lessThanOrEqual=" + SMALLER_MEDIA_ORE_SONNO
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByMediaOreSonnoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where mediaOreSonno is less than
        defaultReportSettimanaleFiltering(
            "mediaOreSonno.lessThan=" + UPDATED_MEDIA_ORE_SONNO,
            "mediaOreSonno.lessThan=" + DEFAULT_MEDIA_ORE_SONNO
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByMediaOreSonnoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where mediaOreSonno is greater than
        defaultReportSettimanaleFiltering(
            "mediaOreSonno.greaterThan=" + SMALLER_MEDIA_ORE_SONNO,
            "mediaOreSonno.greaterThan=" + DEFAULT_MEDIA_ORE_SONNO
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataCreazioneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataCreazione equals to
        defaultReportSettimanaleFiltering(
            "dataCreazione.equals=" + DEFAULT_DATA_CREAZIONE,
            "dataCreazione.equals=" + UPDATED_DATA_CREAZIONE
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataCreazioneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataCreazione in
        defaultReportSettimanaleFiltering(
            "dataCreazione.in=" + DEFAULT_DATA_CREAZIONE + "," + UPDATED_DATA_CREAZIONE,
            "dataCreazione.in=" + UPDATED_DATA_CREAZIONE
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataCreazioneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataCreazione is not null
        defaultReportSettimanaleFiltering("dataCreazione.specified=true", "dataCreazione.specified=false");
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataCreazioneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataCreazione is greater than or equal to
        defaultReportSettimanaleFiltering(
            "dataCreazione.greaterThanOrEqual=" + DEFAULT_DATA_CREAZIONE,
            "dataCreazione.greaterThanOrEqual=" + UPDATED_DATA_CREAZIONE
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataCreazioneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataCreazione is less than or equal to
        defaultReportSettimanaleFiltering(
            "dataCreazione.lessThanOrEqual=" + DEFAULT_DATA_CREAZIONE,
            "dataCreazione.lessThanOrEqual=" + SMALLER_DATA_CREAZIONE
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataCreazioneIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataCreazione is less than
        defaultReportSettimanaleFiltering(
            "dataCreazione.lessThan=" + UPDATED_DATA_CREAZIONE,
            "dataCreazione.lessThan=" + DEFAULT_DATA_CREAZIONE
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataCreazioneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataCreazione is greater than
        defaultReportSettimanaleFiltering(
            "dataCreazione.greaterThan=" + SMALLER_DATA_CREAZIONE,
            "dataCreazione.greaterThan=" + DEFAULT_DATA_CREAZIONE
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataScadenzaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataScadenza equals to
        defaultReportSettimanaleFiltering("dataScadenza.equals=" + DEFAULT_DATA_SCADENZA, "dataScadenza.equals=" + UPDATED_DATA_SCADENZA);
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataScadenzaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataScadenza in
        defaultReportSettimanaleFiltering(
            "dataScadenza.in=" + DEFAULT_DATA_SCADENZA + "," + UPDATED_DATA_SCADENZA,
            "dataScadenza.in=" + UPDATED_DATA_SCADENZA
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataScadenzaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataScadenza is not null
        defaultReportSettimanaleFiltering("dataScadenza.specified=true", "dataScadenza.specified=false");
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataScadenzaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataScadenza is greater than or equal to
        defaultReportSettimanaleFiltering(
            "dataScadenza.greaterThanOrEqual=" + DEFAULT_DATA_SCADENZA,
            "dataScadenza.greaterThanOrEqual=" + UPDATED_DATA_SCADENZA
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataScadenzaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataScadenza is less than or equal to
        defaultReportSettimanaleFiltering(
            "dataScadenza.lessThanOrEqual=" + DEFAULT_DATA_SCADENZA,
            "dataScadenza.lessThanOrEqual=" + SMALLER_DATA_SCADENZA
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataScadenzaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataScadenza is less than
        defaultReportSettimanaleFiltering(
            "dataScadenza.lessThan=" + UPDATED_DATA_SCADENZA,
            "dataScadenza.lessThan=" + DEFAULT_DATA_SCADENZA
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataScadenzaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataScadenza is greater than
        defaultReportSettimanaleFiltering(
            "dataScadenza.greaterThan=" + SMALLER_DATA_SCADENZA,
            "dataScadenza.greaterThan=" + DEFAULT_DATA_SCADENZA
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataCompletamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataCompletamento equals to
        defaultReportSettimanaleFiltering(
            "dataCompletamento.equals=" + DEFAULT_DATA_COMPLETAMENTO,
            "dataCompletamento.equals=" + UPDATED_DATA_COMPLETAMENTO
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataCompletamentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataCompletamento in
        defaultReportSettimanaleFiltering(
            "dataCompletamento.in=" + DEFAULT_DATA_COMPLETAMENTO + "," + UPDATED_DATA_COMPLETAMENTO,
            "dataCompletamento.in=" + UPDATED_DATA_COMPLETAMENTO
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataCompletamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataCompletamento is not null
        defaultReportSettimanaleFiltering("dataCompletamento.specified=true", "dataCompletamento.specified=false");
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataCompletamentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataCompletamento is greater than or equal to
        defaultReportSettimanaleFiltering(
            "dataCompletamento.greaterThanOrEqual=" + DEFAULT_DATA_COMPLETAMENTO,
            "dataCompletamento.greaterThanOrEqual=" + UPDATED_DATA_COMPLETAMENTO
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataCompletamentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataCompletamento is less than or equal to
        defaultReportSettimanaleFiltering(
            "dataCompletamento.lessThanOrEqual=" + DEFAULT_DATA_COMPLETAMENTO,
            "dataCompletamento.lessThanOrEqual=" + SMALLER_DATA_COMPLETAMENTO
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataCompletamentoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataCompletamento is less than
        defaultReportSettimanaleFiltering(
            "dataCompletamento.lessThan=" + UPDATED_DATA_COMPLETAMENTO,
            "dataCompletamento.lessThan=" + DEFAULT_DATA_COMPLETAMENTO
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByDataCompletamentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where dataCompletamento is greater than
        defaultReportSettimanaleFiltering(
            "dataCompletamento.greaterThan=" + SMALLER_DATA_COMPLETAMENTO,
            "dataCompletamento.greaterThan=" + DEFAULT_DATA_COMPLETAMENTO
        );
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByPuntualeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where puntuale equals to
        defaultReportSettimanaleFiltering("puntuale.equals=" + DEFAULT_PUNTUALE, "puntuale.equals=" + UPDATED_PUNTUALE);
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByPuntualeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where puntuale in
        defaultReportSettimanaleFiltering("puntuale.in=" + DEFAULT_PUNTUALE + "," + UPDATED_PUNTUALE, "puntuale.in=" + UPDATED_PUNTUALE);
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByPuntualeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        // Get all the reportSettimanaleList where puntuale is not null
        defaultReportSettimanaleFiltering("puntuale.specified=true", "puntuale.specified=false");
    }

    @Test
    @Transactional
    void getAllReportSettimanalesBySchedaSettimanaleIsEqualToSomething() throws Exception {
        // Get already existing entity
        SchedaSettimanale schedaSettimanale = reportSettimanale.getSchedaSettimanale();
        reportSettimanaleRepository.saveAndFlush(reportSettimanale);
        Long schedaSettimanaleId = schedaSettimanale.getId();
        // Get all the reportSettimanaleList where schedaSettimanale equals to schedaSettimanaleId
        defaultReportSettimanaleShouldBeFound("schedaSettimanaleId.equals=" + schedaSettimanaleId);

        // Get all the reportSettimanaleList where schedaSettimanale equals to (schedaSettimanaleId + 1)
        defaultReportSettimanaleShouldNotBeFound("schedaSettimanaleId.equals=" + (schedaSettimanaleId + 1));
    }

    @Test
    @Transactional
    void getAllReportSettimanalesByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            reportSettimanaleRepository.saveAndFlush(reportSettimanale);
            cliente = ClienteResourceIT.createEntity(em);
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        em.persist(cliente);
        em.flush();
        reportSettimanale.setCliente(cliente);
        reportSettimanaleRepository.saveAndFlush(reportSettimanale);
        Long clienteId = cliente.getId();
        // Get all the reportSettimanaleList where cliente equals to clienteId
        defaultReportSettimanaleShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the reportSettimanaleList where cliente equals to (clienteId + 1)
        defaultReportSettimanaleShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    private void defaultReportSettimanaleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReportSettimanaleShouldBeFound(shouldBeFound);
        defaultReportSettimanaleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportSettimanaleShouldBeFound(String filter) throws Exception {
        restReportSettimanaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportSettimanale.getId().intValue())))
            .andExpect(jsonPath("$.[*].voto").value(hasItem(DEFAULT_VOTO.toString())))
            .andExpect(jsonPath("$.[*].commentoAllenamento").value(hasItem(DEFAULT_COMMENTO_ALLENAMENTO.toString())))
            .andExpect(jsonPath("$.[*].giorniDieta").value(hasItem(DEFAULT_GIORNI_DIETA.toString())))
            .andExpect(jsonPath("$.[*].pesoMedio").value(hasItem(DEFAULT_PESO_MEDIO.doubleValue())))
            .andExpect(jsonPath("$.[*].qualitaSonno").value(hasItem(DEFAULT_QUALITA_SONNO.toString())))
            .andExpect(jsonPath("$.[*].mediaOreSonno").value(hasItem(DEFAULT_MEDIA_ORE_SONNO.toString())))
            .andExpect(jsonPath("$.[*].dataCreazione").value(hasItem(DEFAULT_DATA_CREAZIONE.toString())))
            .andExpect(jsonPath("$.[*].dataScadenza").value(hasItem(DEFAULT_DATA_SCADENZA.toString())))
            .andExpect(jsonPath("$.[*].dataCompletamento").value(hasItem(DEFAULT_DATA_COMPLETAMENTO.toString())))
            .andExpect(jsonPath("$.[*].puntuale").value(hasItem(DEFAULT_PUNTUALE.booleanValue())))
            .andExpect(jsonPath("$.[*].analisiReport").value(hasItem(DEFAULT_ANALISI_REPORT.toString())));

        // Check, that the count call also returns 1
        restReportSettimanaleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportSettimanaleShouldNotBeFound(String filter) throws Exception {
        restReportSettimanaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportSettimanaleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReportSettimanale() throws Exception {
        // Get the reportSettimanale
        restReportSettimanaleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportSettimanale() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportSettimanale
        ReportSettimanale updatedReportSettimanale = reportSettimanaleRepository.findById(reportSettimanale.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportSettimanale are not directly saved in db
        em.detach(updatedReportSettimanale);
        updatedReportSettimanale
            .voto(UPDATED_VOTO)
            .commentoAllenamento(UPDATED_COMMENTO_ALLENAMENTO)
            .giorniDieta(UPDATED_GIORNI_DIETA)
            .pesoMedio(UPDATED_PESO_MEDIO)
            .qualitaSonno(UPDATED_QUALITA_SONNO)
            .mediaOreSonno(UPDATED_MEDIA_ORE_SONNO)
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .dataScadenza(UPDATED_DATA_SCADENZA)
            .dataCompletamento(UPDATED_DATA_COMPLETAMENTO)
            .puntuale(UPDATED_PUNTUALE)
            .analisiReport(UPDATED_ANALISI_REPORT);
        ReportSettimanaleDTO reportSettimanaleDTO = reportSettimanaleMapper.toDto(updatedReportSettimanale);

        restReportSettimanaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportSettimanaleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportSettimanaleDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportSettimanale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportSettimanaleToMatchAllProperties(updatedReportSettimanale);
    }

    @Test
    @Transactional
    void putNonExistingReportSettimanale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSettimanale.setId(longCount.incrementAndGet());

        // Create the ReportSettimanale
        ReportSettimanaleDTO reportSettimanaleDTO = reportSettimanaleMapper.toDto(reportSettimanale);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportSettimanaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportSettimanaleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportSettimanaleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportSettimanale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportSettimanale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSettimanale.setId(longCount.incrementAndGet());

        // Create the ReportSettimanale
        ReportSettimanaleDTO reportSettimanaleDTO = reportSettimanaleMapper.toDto(reportSettimanale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportSettimanaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportSettimanaleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportSettimanale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportSettimanale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSettimanale.setId(longCount.incrementAndGet());

        // Create the ReportSettimanale
        ReportSettimanaleDTO reportSettimanaleDTO = reportSettimanaleMapper.toDto(reportSettimanale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportSettimanaleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportSettimanaleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportSettimanale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportSettimanaleWithPatch() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportSettimanale using partial update
        ReportSettimanale partialUpdatedReportSettimanale = new ReportSettimanale();
        partialUpdatedReportSettimanale.setId(reportSettimanale.getId());

        partialUpdatedReportSettimanale
            .voto(UPDATED_VOTO)
            .commentoAllenamento(UPDATED_COMMENTO_ALLENAMENTO)
            .giorniDieta(UPDATED_GIORNI_DIETA)
            .qualitaSonno(UPDATED_QUALITA_SONNO)
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .dataCompletamento(UPDATED_DATA_COMPLETAMENTO)
            .puntuale(UPDATED_PUNTUALE);

        restReportSettimanaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportSettimanale.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportSettimanale))
            )
            .andExpect(status().isOk());

        // Validate the ReportSettimanale in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportSettimanaleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportSettimanale, reportSettimanale),
            getPersistedReportSettimanale(reportSettimanale)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportSettimanaleWithPatch() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportSettimanale using partial update
        ReportSettimanale partialUpdatedReportSettimanale = new ReportSettimanale();
        partialUpdatedReportSettimanale.setId(reportSettimanale.getId());

        partialUpdatedReportSettimanale
            .voto(UPDATED_VOTO)
            .commentoAllenamento(UPDATED_COMMENTO_ALLENAMENTO)
            .giorniDieta(UPDATED_GIORNI_DIETA)
            .pesoMedio(UPDATED_PESO_MEDIO)
            .qualitaSonno(UPDATED_QUALITA_SONNO)
            .mediaOreSonno(UPDATED_MEDIA_ORE_SONNO)
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .dataScadenza(UPDATED_DATA_SCADENZA)
            .dataCompletamento(UPDATED_DATA_COMPLETAMENTO)
            .puntuale(UPDATED_PUNTUALE)
            .analisiReport(UPDATED_ANALISI_REPORT);

        restReportSettimanaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportSettimanale.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportSettimanale))
            )
            .andExpect(status().isOk());

        // Validate the ReportSettimanale in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportSettimanaleUpdatableFieldsEquals(
            partialUpdatedReportSettimanale,
            getPersistedReportSettimanale(partialUpdatedReportSettimanale)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReportSettimanale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSettimanale.setId(longCount.incrementAndGet());

        // Create the ReportSettimanale
        ReportSettimanaleDTO reportSettimanaleDTO = reportSettimanaleMapper.toDto(reportSettimanale);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportSettimanaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportSettimanaleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportSettimanaleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportSettimanale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportSettimanale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSettimanale.setId(longCount.incrementAndGet());

        // Create the ReportSettimanale
        ReportSettimanaleDTO reportSettimanaleDTO = reportSettimanaleMapper.toDto(reportSettimanale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportSettimanaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportSettimanaleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportSettimanale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportSettimanale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSettimanale.setId(longCount.incrementAndGet());

        // Create the ReportSettimanale
        ReportSettimanaleDTO reportSettimanaleDTO = reportSettimanaleMapper.toDto(reportSettimanale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportSettimanaleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportSettimanaleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportSettimanale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportSettimanale() throws Exception {
        // Initialize the database
        insertedReportSettimanale = reportSettimanaleRepository.saveAndFlush(reportSettimanale);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportSettimanale
        restReportSettimanaleMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportSettimanale.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportSettimanaleRepository.count();
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

    protected ReportSettimanale getPersistedReportSettimanale(ReportSettimanale reportSettimanale) {
        return reportSettimanaleRepository.findById(reportSettimanale.getId()).orElseThrow();
    }

    protected void assertPersistedReportSettimanaleToMatchAllProperties(ReportSettimanale expectedReportSettimanale) {
        assertReportSettimanaleAllPropertiesEquals(expectedReportSettimanale, getPersistedReportSettimanale(expectedReportSettimanale));
    }

    protected void assertPersistedReportSettimanaleToMatchUpdatableProperties(ReportSettimanale expectedReportSettimanale) {
        assertReportSettimanaleAllUpdatablePropertiesEquals(
            expectedReportSettimanale,
            getPersistedReportSettimanale(expectedReportSettimanale)
        );
    }
}
