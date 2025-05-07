package com.oscinnovation.fitness.web.rest;

import static com.oscinnovation.fitness.domain.PlicometriaAsserts.*;
import static com.oscinnovation.fitness.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscinnovation.fitness.IntegrationTest;
import com.oscinnovation.fitness.domain.Cliente;
import com.oscinnovation.fitness.domain.Plicometria;
import com.oscinnovation.fitness.repository.PlicometriaRepository;
import com.oscinnovation.fitness.service.PlicometriaService;
import com.oscinnovation.fitness.service.dto.PlicometriaDTO;
import com.oscinnovation.fitness.service.mapper.PlicometriaMapper;
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
 * Integration tests for the {@link PlicometriaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PlicometriaResourceIT {

    private static final Float DEFAULT_TRICIPITE = 0F;
    private static final Float UPDATED_TRICIPITE = 1F;
    private static final Float SMALLER_TRICIPITE = 0F - 1F;

    private static final Float DEFAULT_PETTO = 0F;
    private static final Float UPDATED_PETTO = 1F;
    private static final Float SMALLER_PETTO = 0F - 1F;

    private static final Float DEFAULT_ASCELLA = 0F;
    private static final Float UPDATED_ASCELLA = 1F;
    private static final Float SMALLER_ASCELLA = 0F - 1F;

    private static final Float DEFAULT_SOTTOSCAPOLARE = 0F;
    private static final Float UPDATED_SOTTOSCAPOLARE = 1F;
    private static final Float SMALLER_SOTTOSCAPOLARE = 0F - 1F;

    private static final Float DEFAULT_SOPRAILLIACA = 0F;
    private static final Float UPDATED_SOPRAILLIACA = 1F;
    private static final Float SMALLER_SOPRAILLIACA = 0F - 1F;

    private static final Float DEFAULT_ADDOME = 0F;
    private static final Float UPDATED_ADDOME = 1F;
    private static final Float SMALLER_ADDOME = 0F - 1F;

    private static final Float DEFAULT_COSCIA = 0F;
    private static final Float UPDATED_COSCIA = 1F;
    private static final Float SMALLER_COSCIA = 0F - 1F;

    private static final Integer DEFAULT_MESE = 1;
    private static final Integer UPDATED_MESE = 2;
    private static final Integer SMALLER_MESE = 1 - 1;

    private static final LocalDate DEFAULT_DATA_INSERIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_INSERIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_INSERIMENTO = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/plicometrias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlicometriaRepository plicometriaRepository;

    @Mock
    private PlicometriaRepository plicometriaRepositoryMock;

    @Autowired
    private PlicometriaMapper plicometriaMapper;

    @Mock
    private PlicometriaService plicometriaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlicometriaMockMvc;

    private Plicometria plicometria;

    private Plicometria insertedPlicometria;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plicometria createEntity(EntityManager em) {
        Plicometria plicometria = new Plicometria()
            .tricipite(DEFAULT_TRICIPITE)
            .petto(DEFAULT_PETTO)
            .ascella(DEFAULT_ASCELLA)
            .sottoscapolare(DEFAULT_SOTTOSCAPOLARE)
            .soprailliaca(DEFAULT_SOPRAILLIACA)
            .addome(DEFAULT_ADDOME)
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
        plicometria.setCliente(cliente);
        return plicometria;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plicometria createUpdatedEntity(EntityManager em) {
        Plicometria updatedPlicometria = new Plicometria()
            .tricipite(UPDATED_TRICIPITE)
            .petto(UPDATED_PETTO)
            .ascella(UPDATED_ASCELLA)
            .sottoscapolare(UPDATED_SOTTOSCAPOLARE)
            .soprailliaca(UPDATED_SOPRAILLIACA)
            .addome(UPDATED_ADDOME)
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
        updatedPlicometria.setCliente(cliente);
        return updatedPlicometria;
    }

    @BeforeEach
    public void initTest() {
        plicometria = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPlicometria != null) {
            plicometriaRepository.delete(insertedPlicometria);
            insertedPlicometria = null;
        }
    }

    @Test
    @Transactional
    void createPlicometria() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Plicometria
        PlicometriaDTO plicometriaDTO = plicometriaMapper.toDto(plicometria);
        var returnedPlicometriaDTO = om.readValue(
            restPlicometriaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(plicometriaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PlicometriaDTO.class
        );

        // Validate the Plicometria in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPlicometria = plicometriaMapper.toEntity(returnedPlicometriaDTO);
        assertPlicometriaUpdatableFieldsEquals(returnedPlicometria, getPersistedPlicometria(returnedPlicometria));

        insertedPlicometria = returnedPlicometria;
    }

    @Test
    @Transactional
    void createPlicometriaWithExistingId() throws Exception {
        // Create the Plicometria with an existing ID
        plicometria.setId(1L);
        PlicometriaDTO plicometriaDTO = plicometriaMapper.toDto(plicometria);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlicometriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(plicometriaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Plicometria in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMeseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        plicometria.setMese(null);

        // Create the Plicometria, which fails.
        PlicometriaDTO plicometriaDTO = plicometriaMapper.toDto(plicometria);

        restPlicometriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(plicometriaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataInserimentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        plicometria.setDataInserimento(null);

        // Create the Plicometria, which fails.
        PlicometriaDTO plicometriaDTO = plicometriaMapper.toDto(plicometria);

        restPlicometriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(plicometriaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlicometrias() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList
        restPlicometriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plicometria.getId().intValue())))
            .andExpect(jsonPath("$.[*].tricipite").value(hasItem(DEFAULT_TRICIPITE.doubleValue())))
            .andExpect(jsonPath("$.[*].petto").value(hasItem(DEFAULT_PETTO.doubleValue())))
            .andExpect(jsonPath("$.[*].ascella").value(hasItem(DEFAULT_ASCELLA.doubleValue())))
            .andExpect(jsonPath("$.[*].sottoscapolare").value(hasItem(DEFAULT_SOTTOSCAPOLARE.doubleValue())))
            .andExpect(jsonPath("$.[*].soprailliaca").value(hasItem(DEFAULT_SOPRAILLIACA.doubleValue())))
            .andExpect(jsonPath("$.[*].addome").value(hasItem(DEFAULT_ADDOME.doubleValue())))
            .andExpect(jsonPath("$.[*].coscia").value(hasItem(DEFAULT_COSCIA.doubleValue())))
            .andExpect(jsonPath("$.[*].mese").value(hasItem(DEFAULT_MESE)))
            .andExpect(jsonPath("$.[*].dataInserimento").value(hasItem(DEFAULT_DATA_INSERIMENTO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlicometriasWithEagerRelationshipsIsEnabled() throws Exception {
        when(plicometriaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlicometriaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(plicometriaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlicometriasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(plicometriaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlicometriaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(plicometriaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPlicometria() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get the plicometria
        restPlicometriaMockMvc
            .perform(get(ENTITY_API_URL_ID, plicometria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plicometria.getId().intValue()))
            .andExpect(jsonPath("$.tricipite").value(DEFAULT_TRICIPITE.doubleValue()))
            .andExpect(jsonPath("$.petto").value(DEFAULT_PETTO.doubleValue()))
            .andExpect(jsonPath("$.ascella").value(DEFAULT_ASCELLA.doubleValue()))
            .andExpect(jsonPath("$.sottoscapolare").value(DEFAULT_SOTTOSCAPOLARE.doubleValue()))
            .andExpect(jsonPath("$.soprailliaca").value(DEFAULT_SOPRAILLIACA.doubleValue()))
            .andExpect(jsonPath("$.addome").value(DEFAULT_ADDOME.doubleValue()))
            .andExpect(jsonPath("$.coscia").value(DEFAULT_COSCIA.doubleValue()))
            .andExpect(jsonPath("$.mese").value(DEFAULT_MESE))
            .andExpect(jsonPath("$.dataInserimento").value(DEFAULT_DATA_INSERIMENTO.toString()));
    }

    @Test
    @Transactional
    void getPlicometriasByIdFiltering() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        Long id = plicometria.getId();

        defaultPlicometriaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPlicometriaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPlicometriaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPlicometriasByTricipiteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where tricipite equals to
        defaultPlicometriaFiltering("tricipite.equals=" + DEFAULT_TRICIPITE, "tricipite.equals=" + UPDATED_TRICIPITE);
    }

    @Test
    @Transactional
    void getAllPlicometriasByTricipiteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where tricipite in
        defaultPlicometriaFiltering("tricipite.in=" + DEFAULT_TRICIPITE + "," + UPDATED_TRICIPITE, "tricipite.in=" + UPDATED_TRICIPITE);
    }

    @Test
    @Transactional
    void getAllPlicometriasByTricipiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where tricipite is not null
        defaultPlicometriaFiltering("tricipite.specified=true", "tricipite.specified=false");
    }

    @Test
    @Transactional
    void getAllPlicometriasByTricipiteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where tricipite is greater than or equal to
        defaultPlicometriaFiltering(
            "tricipite.greaterThanOrEqual=" + DEFAULT_TRICIPITE,
            "tricipite.greaterThanOrEqual=" + UPDATED_TRICIPITE
        );
    }

    @Test
    @Transactional
    void getAllPlicometriasByTricipiteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where tricipite is less than or equal to
        defaultPlicometriaFiltering("tricipite.lessThanOrEqual=" + DEFAULT_TRICIPITE, "tricipite.lessThanOrEqual=" + SMALLER_TRICIPITE);
    }

    @Test
    @Transactional
    void getAllPlicometriasByTricipiteIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where tricipite is less than
        defaultPlicometriaFiltering("tricipite.lessThan=" + UPDATED_TRICIPITE, "tricipite.lessThan=" + DEFAULT_TRICIPITE);
    }

    @Test
    @Transactional
    void getAllPlicometriasByTricipiteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where tricipite is greater than
        defaultPlicometriaFiltering("tricipite.greaterThan=" + SMALLER_TRICIPITE, "tricipite.greaterThan=" + DEFAULT_TRICIPITE);
    }

    @Test
    @Transactional
    void getAllPlicometriasByPettoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where petto equals to
        defaultPlicometriaFiltering("petto.equals=" + DEFAULT_PETTO, "petto.equals=" + UPDATED_PETTO);
    }

    @Test
    @Transactional
    void getAllPlicometriasByPettoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where petto in
        defaultPlicometriaFiltering("petto.in=" + DEFAULT_PETTO + "," + UPDATED_PETTO, "petto.in=" + UPDATED_PETTO);
    }

    @Test
    @Transactional
    void getAllPlicometriasByPettoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where petto is not null
        defaultPlicometriaFiltering("petto.specified=true", "petto.specified=false");
    }

    @Test
    @Transactional
    void getAllPlicometriasByPettoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where petto is greater than or equal to
        defaultPlicometriaFiltering("petto.greaterThanOrEqual=" + DEFAULT_PETTO, "petto.greaterThanOrEqual=" + UPDATED_PETTO);
    }

    @Test
    @Transactional
    void getAllPlicometriasByPettoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where petto is less than or equal to
        defaultPlicometriaFiltering("petto.lessThanOrEqual=" + DEFAULT_PETTO, "petto.lessThanOrEqual=" + SMALLER_PETTO);
    }

    @Test
    @Transactional
    void getAllPlicometriasByPettoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where petto is less than
        defaultPlicometriaFiltering("petto.lessThan=" + UPDATED_PETTO, "petto.lessThan=" + DEFAULT_PETTO);
    }

    @Test
    @Transactional
    void getAllPlicometriasByPettoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where petto is greater than
        defaultPlicometriaFiltering("petto.greaterThan=" + SMALLER_PETTO, "petto.greaterThan=" + DEFAULT_PETTO);
    }

    @Test
    @Transactional
    void getAllPlicometriasByAscellaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where ascella equals to
        defaultPlicometriaFiltering("ascella.equals=" + DEFAULT_ASCELLA, "ascella.equals=" + UPDATED_ASCELLA);
    }

    @Test
    @Transactional
    void getAllPlicometriasByAscellaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where ascella in
        defaultPlicometriaFiltering("ascella.in=" + DEFAULT_ASCELLA + "," + UPDATED_ASCELLA, "ascella.in=" + UPDATED_ASCELLA);
    }

    @Test
    @Transactional
    void getAllPlicometriasByAscellaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where ascella is not null
        defaultPlicometriaFiltering("ascella.specified=true", "ascella.specified=false");
    }

    @Test
    @Transactional
    void getAllPlicometriasByAscellaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where ascella is greater than or equal to
        defaultPlicometriaFiltering("ascella.greaterThanOrEqual=" + DEFAULT_ASCELLA, "ascella.greaterThanOrEqual=" + UPDATED_ASCELLA);
    }

    @Test
    @Transactional
    void getAllPlicometriasByAscellaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where ascella is less than or equal to
        defaultPlicometriaFiltering("ascella.lessThanOrEqual=" + DEFAULT_ASCELLA, "ascella.lessThanOrEqual=" + SMALLER_ASCELLA);
    }

    @Test
    @Transactional
    void getAllPlicometriasByAscellaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where ascella is less than
        defaultPlicometriaFiltering("ascella.lessThan=" + UPDATED_ASCELLA, "ascella.lessThan=" + DEFAULT_ASCELLA);
    }

    @Test
    @Transactional
    void getAllPlicometriasByAscellaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where ascella is greater than
        defaultPlicometriaFiltering("ascella.greaterThan=" + SMALLER_ASCELLA, "ascella.greaterThan=" + DEFAULT_ASCELLA);
    }

    @Test
    @Transactional
    void getAllPlicometriasBySottoscapolareIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where sottoscapolare equals to
        defaultPlicometriaFiltering("sottoscapolare.equals=" + DEFAULT_SOTTOSCAPOLARE, "sottoscapolare.equals=" + UPDATED_SOTTOSCAPOLARE);
    }

    @Test
    @Transactional
    void getAllPlicometriasBySottoscapolareIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where sottoscapolare in
        defaultPlicometriaFiltering(
            "sottoscapolare.in=" + DEFAULT_SOTTOSCAPOLARE + "," + UPDATED_SOTTOSCAPOLARE,
            "sottoscapolare.in=" + UPDATED_SOTTOSCAPOLARE
        );
    }

    @Test
    @Transactional
    void getAllPlicometriasBySottoscapolareIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where sottoscapolare is not null
        defaultPlicometriaFiltering("sottoscapolare.specified=true", "sottoscapolare.specified=false");
    }

    @Test
    @Transactional
    void getAllPlicometriasBySottoscapolareIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where sottoscapolare is greater than or equal to
        defaultPlicometriaFiltering(
            "sottoscapolare.greaterThanOrEqual=" + DEFAULT_SOTTOSCAPOLARE,
            "sottoscapolare.greaterThanOrEqual=" + UPDATED_SOTTOSCAPOLARE
        );
    }

    @Test
    @Transactional
    void getAllPlicometriasBySottoscapolareIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where sottoscapolare is less than or equal to
        defaultPlicometriaFiltering(
            "sottoscapolare.lessThanOrEqual=" + DEFAULT_SOTTOSCAPOLARE,
            "sottoscapolare.lessThanOrEqual=" + SMALLER_SOTTOSCAPOLARE
        );
    }

    @Test
    @Transactional
    void getAllPlicometriasBySottoscapolareIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where sottoscapolare is less than
        defaultPlicometriaFiltering(
            "sottoscapolare.lessThan=" + UPDATED_SOTTOSCAPOLARE,
            "sottoscapolare.lessThan=" + DEFAULT_SOTTOSCAPOLARE
        );
    }

    @Test
    @Transactional
    void getAllPlicometriasBySottoscapolareIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where sottoscapolare is greater than
        defaultPlicometriaFiltering(
            "sottoscapolare.greaterThan=" + SMALLER_SOTTOSCAPOLARE,
            "sottoscapolare.greaterThan=" + DEFAULT_SOTTOSCAPOLARE
        );
    }

    @Test
    @Transactional
    void getAllPlicometriasBySoprailliacaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where soprailliaca equals to
        defaultPlicometriaFiltering("soprailliaca.equals=" + DEFAULT_SOPRAILLIACA, "soprailliaca.equals=" + UPDATED_SOPRAILLIACA);
    }

    @Test
    @Transactional
    void getAllPlicometriasBySoprailliacaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where soprailliaca in
        defaultPlicometriaFiltering(
            "soprailliaca.in=" + DEFAULT_SOPRAILLIACA + "," + UPDATED_SOPRAILLIACA,
            "soprailliaca.in=" + UPDATED_SOPRAILLIACA
        );
    }

    @Test
    @Transactional
    void getAllPlicometriasBySoprailliacaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where soprailliaca is not null
        defaultPlicometriaFiltering("soprailliaca.specified=true", "soprailliaca.specified=false");
    }

    @Test
    @Transactional
    void getAllPlicometriasBySoprailliacaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where soprailliaca is greater than or equal to
        defaultPlicometriaFiltering(
            "soprailliaca.greaterThanOrEqual=" + DEFAULT_SOPRAILLIACA,
            "soprailliaca.greaterThanOrEqual=" + UPDATED_SOPRAILLIACA
        );
    }

    @Test
    @Transactional
    void getAllPlicometriasBySoprailliacaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where soprailliaca is less than or equal to
        defaultPlicometriaFiltering(
            "soprailliaca.lessThanOrEqual=" + DEFAULT_SOPRAILLIACA,
            "soprailliaca.lessThanOrEqual=" + SMALLER_SOPRAILLIACA
        );
    }

    @Test
    @Transactional
    void getAllPlicometriasBySoprailliacaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where soprailliaca is less than
        defaultPlicometriaFiltering("soprailliaca.lessThan=" + UPDATED_SOPRAILLIACA, "soprailliaca.lessThan=" + DEFAULT_SOPRAILLIACA);
    }

    @Test
    @Transactional
    void getAllPlicometriasBySoprailliacaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where soprailliaca is greater than
        defaultPlicometriaFiltering("soprailliaca.greaterThan=" + SMALLER_SOPRAILLIACA, "soprailliaca.greaterThan=" + DEFAULT_SOPRAILLIACA);
    }

    @Test
    @Transactional
    void getAllPlicometriasByAddomeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where addome equals to
        defaultPlicometriaFiltering("addome.equals=" + DEFAULT_ADDOME, "addome.equals=" + UPDATED_ADDOME);
    }

    @Test
    @Transactional
    void getAllPlicometriasByAddomeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where addome in
        defaultPlicometriaFiltering("addome.in=" + DEFAULT_ADDOME + "," + UPDATED_ADDOME, "addome.in=" + UPDATED_ADDOME);
    }

    @Test
    @Transactional
    void getAllPlicometriasByAddomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where addome is not null
        defaultPlicometriaFiltering("addome.specified=true", "addome.specified=false");
    }

    @Test
    @Transactional
    void getAllPlicometriasByAddomeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where addome is greater than or equal to
        defaultPlicometriaFiltering("addome.greaterThanOrEqual=" + DEFAULT_ADDOME, "addome.greaterThanOrEqual=" + UPDATED_ADDOME);
    }

    @Test
    @Transactional
    void getAllPlicometriasByAddomeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where addome is less than or equal to
        defaultPlicometriaFiltering("addome.lessThanOrEqual=" + DEFAULT_ADDOME, "addome.lessThanOrEqual=" + SMALLER_ADDOME);
    }

    @Test
    @Transactional
    void getAllPlicometriasByAddomeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where addome is less than
        defaultPlicometriaFiltering("addome.lessThan=" + UPDATED_ADDOME, "addome.lessThan=" + DEFAULT_ADDOME);
    }

    @Test
    @Transactional
    void getAllPlicometriasByAddomeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where addome is greater than
        defaultPlicometriaFiltering("addome.greaterThan=" + SMALLER_ADDOME, "addome.greaterThan=" + DEFAULT_ADDOME);
    }

    @Test
    @Transactional
    void getAllPlicometriasByCosciaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where coscia equals to
        defaultPlicometriaFiltering("coscia.equals=" + DEFAULT_COSCIA, "coscia.equals=" + UPDATED_COSCIA);
    }

    @Test
    @Transactional
    void getAllPlicometriasByCosciaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where coscia in
        defaultPlicometriaFiltering("coscia.in=" + DEFAULT_COSCIA + "," + UPDATED_COSCIA, "coscia.in=" + UPDATED_COSCIA);
    }

    @Test
    @Transactional
    void getAllPlicometriasByCosciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where coscia is not null
        defaultPlicometriaFiltering("coscia.specified=true", "coscia.specified=false");
    }

    @Test
    @Transactional
    void getAllPlicometriasByCosciaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where coscia is greater than or equal to
        defaultPlicometriaFiltering("coscia.greaterThanOrEqual=" + DEFAULT_COSCIA, "coscia.greaterThanOrEqual=" + UPDATED_COSCIA);
    }

    @Test
    @Transactional
    void getAllPlicometriasByCosciaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where coscia is less than or equal to
        defaultPlicometriaFiltering("coscia.lessThanOrEqual=" + DEFAULT_COSCIA, "coscia.lessThanOrEqual=" + SMALLER_COSCIA);
    }

    @Test
    @Transactional
    void getAllPlicometriasByCosciaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where coscia is less than
        defaultPlicometriaFiltering("coscia.lessThan=" + UPDATED_COSCIA, "coscia.lessThan=" + DEFAULT_COSCIA);
    }

    @Test
    @Transactional
    void getAllPlicometriasByCosciaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where coscia is greater than
        defaultPlicometriaFiltering("coscia.greaterThan=" + SMALLER_COSCIA, "coscia.greaterThan=" + DEFAULT_COSCIA);
    }

    @Test
    @Transactional
    void getAllPlicometriasByMeseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where mese equals to
        defaultPlicometriaFiltering("mese.equals=" + DEFAULT_MESE, "mese.equals=" + UPDATED_MESE);
    }

    @Test
    @Transactional
    void getAllPlicometriasByMeseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where mese in
        defaultPlicometriaFiltering("mese.in=" + DEFAULT_MESE + "," + UPDATED_MESE, "mese.in=" + UPDATED_MESE);
    }

    @Test
    @Transactional
    void getAllPlicometriasByMeseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where mese is not null
        defaultPlicometriaFiltering("mese.specified=true", "mese.specified=false");
    }

    @Test
    @Transactional
    void getAllPlicometriasByMeseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where mese is greater than or equal to
        defaultPlicometriaFiltering("mese.greaterThanOrEqual=" + DEFAULT_MESE, "mese.greaterThanOrEqual=" + (DEFAULT_MESE + 1));
    }

    @Test
    @Transactional
    void getAllPlicometriasByMeseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where mese is less than or equal to
        defaultPlicometriaFiltering("mese.lessThanOrEqual=" + DEFAULT_MESE, "mese.lessThanOrEqual=" + SMALLER_MESE);
    }

    @Test
    @Transactional
    void getAllPlicometriasByMeseIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where mese is less than
        defaultPlicometriaFiltering("mese.lessThan=" + (DEFAULT_MESE + 1), "mese.lessThan=" + DEFAULT_MESE);
    }

    @Test
    @Transactional
    void getAllPlicometriasByMeseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where mese is greater than
        defaultPlicometriaFiltering("mese.greaterThan=" + SMALLER_MESE, "mese.greaterThan=" + DEFAULT_MESE);
    }

    @Test
    @Transactional
    void getAllPlicometriasByDataInserimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where dataInserimento equals to
        defaultPlicometriaFiltering(
            "dataInserimento.equals=" + DEFAULT_DATA_INSERIMENTO,
            "dataInserimento.equals=" + UPDATED_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPlicometriasByDataInserimentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where dataInserimento in
        defaultPlicometriaFiltering(
            "dataInserimento.in=" + DEFAULT_DATA_INSERIMENTO + "," + UPDATED_DATA_INSERIMENTO,
            "dataInserimento.in=" + UPDATED_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPlicometriasByDataInserimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where dataInserimento is not null
        defaultPlicometriaFiltering("dataInserimento.specified=true", "dataInserimento.specified=false");
    }

    @Test
    @Transactional
    void getAllPlicometriasByDataInserimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where dataInserimento is greater than or equal to
        defaultPlicometriaFiltering(
            "dataInserimento.greaterThanOrEqual=" + DEFAULT_DATA_INSERIMENTO,
            "dataInserimento.greaterThanOrEqual=" + UPDATED_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPlicometriasByDataInserimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where dataInserimento is less than or equal to
        defaultPlicometriaFiltering(
            "dataInserimento.lessThanOrEqual=" + DEFAULT_DATA_INSERIMENTO,
            "dataInserimento.lessThanOrEqual=" + SMALLER_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPlicometriasByDataInserimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where dataInserimento is less than
        defaultPlicometriaFiltering(
            "dataInserimento.lessThan=" + UPDATED_DATA_INSERIMENTO,
            "dataInserimento.lessThan=" + DEFAULT_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPlicometriasByDataInserimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        // Get all the plicometriaList where dataInserimento is greater than
        defaultPlicometriaFiltering(
            "dataInserimento.greaterThan=" + SMALLER_DATA_INSERIMENTO,
            "dataInserimento.greaterThan=" + DEFAULT_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPlicometriasByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            plicometriaRepository.saveAndFlush(plicometria);
            cliente = ClienteResourceIT.createEntity(em);
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        em.persist(cliente);
        em.flush();
        plicometria.setCliente(cliente);
        plicometriaRepository.saveAndFlush(plicometria);
        Long clienteId = cliente.getId();
        // Get all the plicometriaList where cliente equals to clienteId
        defaultPlicometriaShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the plicometriaList where cliente equals to (clienteId + 1)
        defaultPlicometriaShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    private void defaultPlicometriaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPlicometriaShouldBeFound(shouldBeFound);
        defaultPlicometriaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlicometriaShouldBeFound(String filter) throws Exception {
        restPlicometriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plicometria.getId().intValue())))
            .andExpect(jsonPath("$.[*].tricipite").value(hasItem(DEFAULT_TRICIPITE.doubleValue())))
            .andExpect(jsonPath("$.[*].petto").value(hasItem(DEFAULT_PETTO.doubleValue())))
            .andExpect(jsonPath("$.[*].ascella").value(hasItem(DEFAULT_ASCELLA.doubleValue())))
            .andExpect(jsonPath("$.[*].sottoscapolare").value(hasItem(DEFAULT_SOTTOSCAPOLARE.doubleValue())))
            .andExpect(jsonPath("$.[*].soprailliaca").value(hasItem(DEFAULT_SOPRAILLIACA.doubleValue())))
            .andExpect(jsonPath("$.[*].addome").value(hasItem(DEFAULT_ADDOME.doubleValue())))
            .andExpect(jsonPath("$.[*].coscia").value(hasItem(DEFAULT_COSCIA.doubleValue())))
            .andExpect(jsonPath("$.[*].mese").value(hasItem(DEFAULT_MESE)))
            .andExpect(jsonPath("$.[*].dataInserimento").value(hasItem(DEFAULT_DATA_INSERIMENTO.toString())));

        // Check, that the count call also returns 1
        restPlicometriaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlicometriaShouldNotBeFound(String filter) throws Exception {
        restPlicometriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlicometriaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPlicometria() throws Exception {
        // Get the plicometria
        restPlicometriaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlicometria() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the plicometria
        Plicometria updatedPlicometria = plicometriaRepository.findById(plicometria.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlicometria are not directly saved in db
        em.detach(updatedPlicometria);
        updatedPlicometria
            .tricipite(UPDATED_TRICIPITE)
            .petto(UPDATED_PETTO)
            .ascella(UPDATED_ASCELLA)
            .sottoscapolare(UPDATED_SOTTOSCAPOLARE)
            .soprailliaca(UPDATED_SOPRAILLIACA)
            .addome(UPDATED_ADDOME)
            .coscia(UPDATED_COSCIA)
            .mese(UPDATED_MESE)
            .dataInserimento(UPDATED_DATA_INSERIMENTO);
        PlicometriaDTO plicometriaDTO = plicometriaMapper.toDto(updatedPlicometria);

        restPlicometriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, plicometriaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(plicometriaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Plicometria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlicometriaToMatchAllProperties(updatedPlicometria);
    }

    @Test
    @Transactional
    void putNonExistingPlicometria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plicometria.setId(longCount.incrementAndGet());

        // Create the Plicometria
        PlicometriaDTO plicometriaDTO = plicometriaMapper.toDto(plicometria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlicometriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, plicometriaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(plicometriaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plicometria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlicometria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plicometria.setId(longCount.incrementAndGet());

        // Create the Plicometria
        PlicometriaDTO plicometriaDTO = plicometriaMapper.toDto(plicometria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlicometriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(plicometriaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plicometria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlicometria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plicometria.setId(longCount.incrementAndGet());

        // Create the Plicometria
        PlicometriaDTO plicometriaDTO = plicometriaMapper.toDto(plicometria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlicometriaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(plicometriaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plicometria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlicometriaWithPatch() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the plicometria using partial update
        Plicometria partialUpdatedPlicometria = new Plicometria();
        partialUpdatedPlicometria.setId(plicometria.getId());

        partialUpdatedPlicometria
            .tricipite(UPDATED_TRICIPITE)
            .petto(UPDATED_PETTO)
            .sottoscapolare(UPDATED_SOTTOSCAPOLARE)
            .soprailliaca(UPDATED_SOPRAILLIACA)
            .coscia(UPDATED_COSCIA);

        restPlicometriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlicometria.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlicometria))
            )
            .andExpect(status().isOk());

        // Validate the Plicometria in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlicometriaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPlicometria, plicometria),
            getPersistedPlicometria(plicometria)
        );
    }

    @Test
    @Transactional
    void fullUpdatePlicometriaWithPatch() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the plicometria using partial update
        Plicometria partialUpdatedPlicometria = new Plicometria();
        partialUpdatedPlicometria.setId(plicometria.getId());

        partialUpdatedPlicometria
            .tricipite(UPDATED_TRICIPITE)
            .petto(UPDATED_PETTO)
            .ascella(UPDATED_ASCELLA)
            .sottoscapolare(UPDATED_SOTTOSCAPOLARE)
            .soprailliaca(UPDATED_SOPRAILLIACA)
            .addome(UPDATED_ADDOME)
            .coscia(UPDATED_COSCIA)
            .mese(UPDATED_MESE)
            .dataInserimento(UPDATED_DATA_INSERIMENTO);

        restPlicometriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlicometria.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlicometria))
            )
            .andExpect(status().isOk());

        // Validate the Plicometria in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlicometriaUpdatableFieldsEquals(partialUpdatedPlicometria, getPersistedPlicometria(partialUpdatedPlicometria));
    }

    @Test
    @Transactional
    void patchNonExistingPlicometria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plicometria.setId(longCount.incrementAndGet());

        // Create the Plicometria
        PlicometriaDTO plicometriaDTO = plicometriaMapper.toDto(plicometria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlicometriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, plicometriaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(plicometriaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plicometria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlicometria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plicometria.setId(longCount.incrementAndGet());

        // Create the Plicometria
        PlicometriaDTO plicometriaDTO = plicometriaMapper.toDto(plicometria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlicometriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(plicometriaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plicometria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlicometria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plicometria.setId(longCount.incrementAndGet());

        // Create the Plicometria
        PlicometriaDTO plicometriaDTO = plicometriaMapper.toDto(plicometria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlicometriaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(plicometriaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plicometria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlicometria() throws Exception {
        // Initialize the database
        insertedPlicometria = plicometriaRepository.saveAndFlush(plicometria);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the plicometria
        restPlicometriaMockMvc
            .perform(delete(ENTITY_API_URL_ID, plicometria.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return plicometriaRepository.count();
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

    protected Plicometria getPersistedPlicometria(Plicometria plicometria) {
        return plicometriaRepository.findById(plicometria.getId()).orElseThrow();
    }

    protected void assertPersistedPlicometriaToMatchAllProperties(Plicometria expectedPlicometria) {
        assertPlicometriaAllPropertiesEquals(expectedPlicometria, getPersistedPlicometria(expectedPlicometria));
    }

    protected void assertPersistedPlicometriaToMatchUpdatableProperties(Plicometria expectedPlicometria) {
        assertPlicometriaAllUpdatablePropertiesEquals(expectedPlicometria, getPersistedPlicometria(expectedPlicometria));
    }
}
