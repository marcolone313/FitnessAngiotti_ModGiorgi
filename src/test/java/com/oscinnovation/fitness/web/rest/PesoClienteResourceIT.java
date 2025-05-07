package com.oscinnovation.fitness.web.rest;

import static com.oscinnovation.fitness.domain.PesoClienteAsserts.*;
import static com.oscinnovation.fitness.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscinnovation.fitness.IntegrationTest;
import com.oscinnovation.fitness.domain.Cliente;
import com.oscinnovation.fitness.domain.PesoCliente;
import com.oscinnovation.fitness.repository.PesoClienteRepository;
import com.oscinnovation.fitness.service.PesoClienteService;
import com.oscinnovation.fitness.service.dto.PesoClienteDTO;
import com.oscinnovation.fitness.service.mapper.PesoClienteMapper;
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
 * Integration tests for the {@link PesoClienteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PesoClienteResourceIT {

    private static final Float DEFAULT_PESO = 0F;
    private static final Float UPDATED_PESO = 1F;
    private static final Float SMALLER_PESO = 0F - 1F;

    private static final Integer DEFAULT_MESE = 1;
    private static final Integer UPDATED_MESE = 2;
    private static final Integer SMALLER_MESE = 1 - 1;

    private static final LocalDate DEFAULT_DATA_INSERIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_INSERIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_INSERIMENTO = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/peso-clientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PesoClienteRepository pesoClienteRepository;

    @Mock
    private PesoClienteRepository pesoClienteRepositoryMock;

    @Autowired
    private PesoClienteMapper pesoClienteMapper;

    @Mock
    private PesoClienteService pesoClienteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPesoClienteMockMvc;

    private PesoCliente pesoCliente;

    private PesoCliente insertedPesoCliente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PesoCliente createEntity(EntityManager em) {
        PesoCliente pesoCliente = new PesoCliente().peso(DEFAULT_PESO).mese(DEFAULT_MESE).dataInserimento(DEFAULT_DATA_INSERIMENTO);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        pesoCliente.setCliente(cliente);
        return pesoCliente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PesoCliente createUpdatedEntity(EntityManager em) {
        PesoCliente updatedPesoCliente = new PesoCliente().peso(UPDATED_PESO).mese(UPDATED_MESE).dataInserimento(UPDATED_DATA_INSERIMENTO);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createUpdatedEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        updatedPesoCliente.setCliente(cliente);
        return updatedPesoCliente;
    }

    @BeforeEach
    public void initTest() {
        pesoCliente = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPesoCliente != null) {
            pesoClienteRepository.delete(insertedPesoCliente);
            insertedPesoCliente = null;
        }
    }

    @Test
    @Transactional
    void createPesoCliente() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PesoCliente
        PesoClienteDTO pesoClienteDTO = pesoClienteMapper.toDto(pesoCliente);
        var returnedPesoClienteDTO = om.readValue(
            restPesoClienteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pesoClienteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PesoClienteDTO.class
        );

        // Validate the PesoCliente in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPesoCliente = pesoClienteMapper.toEntity(returnedPesoClienteDTO);
        assertPesoClienteUpdatableFieldsEquals(returnedPesoCliente, getPersistedPesoCliente(returnedPesoCliente));

        insertedPesoCliente = returnedPesoCliente;
    }

    @Test
    @Transactional
    void createPesoClienteWithExistingId() throws Exception {
        // Create the PesoCliente with an existing ID
        pesoCliente.setId(1L);
        PesoClienteDTO pesoClienteDTO = pesoClienteMapper.toDto(pesoCliente);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPesoClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pesoClienteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PesoCliente in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPesoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pesoCliente.setPeso(null);

        // Create the PesoCliente, which fails.
        PesoClienteDTO pesoClienteDTO = pesoClienteMapper.toDto(pesoCliente);

        restPesoClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pesoClienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMeseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pesoCliente.setMese(null);

        // Create the PesoCliente, which fails.
        PesoClienteDTO pesoClienteDTO = pesoClienteMapper.toDto(pesoCliente);

        restPesoClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pesoClienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataInserimentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pesoCliente.setDataInserimento(null);

        // Create the PesoCliente, which fails.
        PesoClienteDTO pesoClienteDTO = pesoClienteMapper.toDto(pesoCliente);

        restPesoClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pesoClienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPesoClientes() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList
        restPesoClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pesoCliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].peso").value(hasItem(DEFAULT_PESO.doubleValue())))
            .andExpect(jsonPath("$.[*].mese").value(hasItem(DEFAULT_MESE)))
            .andExpect(jsonPath("$.[*].dataInserimento").value(hasItem(DEFAULT_DATA_INSERIMENTO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPesoClientesWithEagerRelationshipsIsEnabled() throws Exception {
        when(pesoClienteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPesoClienteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pesoClienteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPesoClientesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pesoClienteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPesoClienteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pesoClienteRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPesoCliente() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get the pesoCliente
        restPesoClienteMockMvc
            .perform(get(ENTITY_API_URL_ID, pesoCliente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pesoCliente.getId().intValue()))
            .andExpect(jsonPath("$.peso").value(DEFAULT_PESO.doubleValue()))
            .andExpect(jsonPath("$.mese").value(DEFAULT_MESE))
            .andExpect(jsonPath("$.dataInserimento").value(DEFAULT_DATA_INSERIMENTO.toString()));
    }

    @Test
    @Transactional
    void getPesoClientesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        Long id = pesoCliente.getId();

        defaultPesoClienteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPesoClienteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPesoClienteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPesoClientesByPesoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where peso equals to
        defaultPesoClienteFiltering("peso.equals=" + DEFAULT_PESO, "peso.equals=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    void getAllPesoClientesByPesoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where peso in
        defaultPesoClienteFiltering("peso.in=" + DEFAULT_PESO + "," + UPDATED_PESO, "peso.in=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    void getAllPesoClientesByPesoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where peso is not null
        defaultPesoClienteFiltering("peso.specified=true", "peso.specified=false");
    }

    @Test
    @Transactional
    void getAllPesoClientesByPesoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where peso is greater than or equal to
        defaultPesoClienteFiltering("peso.greaterThanOrEqual=" + DEFAULT_PESO, "peso.greaterThanOrEqual=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    void getAllPesoClientesByPesoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where peso is less than or equal to
        defaultPesoClienteFiltering("peso.lessThanOrEqual=" + DEFAULT_PESO, "peso.lessThanOrEqual=" + SMALLER_PESO);
    }

    @Test
    @Transactional
    void getAllPesoClientesByPesoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where peso is less than
        defaultPesoClienteFiltering("peso.lessThan=" + UPDATED_PESO, "peso.lessThan=" + DEFAULT_PESO);
    }

    @Test
    @Transactional
    void getAllPesoClientesByPesoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where peso is greater than
        defaultPesoClienteFiltering("peso.greaterThan=" + SMALLER_PESO, "peso.greaterThan=" + DEFAULT_PESO);
    }

    @Test
    @Transactional
    void getAllPesoClientesByMeseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where mese equals to
        defaultPesoClienteFiltering("mese.equals=" + DEFAULT_MESE, "mese.equals=" + UPDATED_MESE);
    }

    @Test
    @Transactional
    void getAllPesoClientesByMeseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where mese in
        defaultPesoClienteFiltering("mese.in=" + DEFAULT_MESE + "," + UPDATED_MESE, "mese.in=" + UPDATED_MESE);
    }

    @Test
    @Transactional
    void getAllPesoClientesByMeseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where mese is not null
        defaultPesoClienteFiltering("mese.specified=true", "mese.specified=false");
    }

    @Test
    @Transactional
    void getAllPesoClientesByMeseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where mese is greater than or equal to
        defaultPesoClienteFiltering("mese.greaterThanOrEqual=" + DEFAULT_MESE, "mese.greaterThanOrEqual=" + (DEFAULT_MESE + 1));
    }

    @Test
    @Transactional
    void getAllPesoClientesByMeseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where mese is less than or equal to
        defaultPesoClienteFiltering("mese.lessThanOrEqual=" + DEFAULT_MESE, "mese.lessThanOrEqual=" + SMALLER_MESE);
    }

    @Test
    @Transactional
    void getAllPesoClientesByMeseIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where mese is less than
        defaultPesoClienteFiltering("mese.lessThan=" + (DEFAULT_MESE + 1), "mese.lessThan=" + DEFAULT_MESE);
    }

    @Test
    @Transactional
    void getAllPesoClientesByMeseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where mese is greater than
        defaultPesoClienteFiltering("mese.greaterThan=" + SMALLER_MESE, "mese.greaterThan=" + DEFAULT_MESE);
    }

    @Test
    @Transactional
    void getAllPesoClientesByDataInserimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where dataInserimento equals to
        defaultPesoClienteFiltering(
            "dataInserimento.equals=" + DEFAULT_DATA_INSERIMENTO,
            "dataInserimento.equals=" + UPDATED_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPesoClientesByDataInserimentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where dataInserimento in
        defaultPesoClienteFiltering(
            "dataInserimento.in=" + DEFAULT_DATA_INSERIMENTO + "," + UPDATED_DATA_INSERIMENTO,
            "dataInserimento.in=" + UPDATED_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPesoClientesByDataInserimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where dataInserimento is not null
        defaultPesoClienteFiltering("dataInserimento.specified=true", "dataInserimento.specified=false");
    }

    @Test
    @Transactional
    void getAllPesoClientesByDataInserimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where dataInserimento is greater than or equal to
        defaultPesoClienteFiltering(
            "dataInserimento.greaterThanOrEqual=" + DEFAULT_DATA_INSERIMENTO,
            "dataInserimento.greaterThanOrEqual=" + UPDATED_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPesoClientesByDataInserimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where dataInserimento is less than or equal to
        defaultPesoClienteFiltering(
            "dataInserimento.lessThanOrEqual=" + DEFAULT_DATA_INSERIMENTO,
            "dataInserimento.lessThanOrEqual=" + SMALLER_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPesoClientesByDataInserimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where dataInserimento is less than
        defaultPesoClienteFiltering(
            "dataInserimento.lessThan=" + UPDATED_DATA_INSERIMENTO,
            "dataInserimento.lessThan=" + DEFAULT_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPesoClientesByDataInserimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        // Get all the pesoClienteList where dataInserimento is greater than
        defaultPesoClienteFiltering(
            "dataInserimento.greaterThan=" + SMALLER_DATA_INSERIMENTO,
            "dataInserimento.greaterThan=" + DEFAULT_DATA_INSERIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPesoClientesByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            pesoClienteRepository.saveAndFlush(pesoCliente);
            cliente = ClienteResourceIT.createEntity(em);
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        em.persist(cliente);
        em.flush();
        pesoCliente.setCliente(cliente);
        pesoClienteRepository.saveAndFlush(pesoCliente);
        Long clienteId = cliente.getId();
        // Get all the pesoClienteList where cliente equals to clienteId
        defaultPesoClienteShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the pesoClienteList where cliente equals to (clienteId + 1)
        defaultPesoClienteShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    private void defaultPesoClienteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPesoClienteShouldBeFound(shouldBeFound);
        defaultPesoClienteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPesoClienteShouldBeFound(String filter) throws Exception {
        restPesoClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pesoCliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].peso").value(hasItem(DEFAULT_PESO.doubleValue())))
            .andExpect(jsonPath("$.[*].mese").value(hasItem(DEFAULT_MESE)))
            .andExpect(jsonPath("$.[*].dataInserimento").value(hasItem(DEFAULT_DATA_INSERIMENTO.toString())));

        // Check, that the count call also returns 1
        restPesoClienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPesoClienteShouldNotBeFound(String filter) throws Exception {
        restPesoClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPesoClienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPesoCliente() throws Exception {
        // Get the pesoCliente
        restPesoClienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPesoCliente() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pesoCliente
        PesoCliente updatedPesoCliente = pesoClienteRepository.findById(pesoCliente.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPesoCliente are not directly saved in db
        em.detach(updatedPesoCliente);
        updatedPesoCliente.peso(UPDATED_PESO).mese(UPDATED_MESE).dataInserimento(UPDATED_DATA_INSERIMENTO);
        PesoClienteDTO pesoClienteDTO = pesoClienteMapper.toDto(updatedPesoCliente);

        restPesoClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pesoClienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pesoClienteDTO))
            )
            .andExpect(status().isOk());

        // Validate the PesoCliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPesoClienteToMatchAllProperties(updatedPesoCliente);
    }

    @Test
    @Transactional
    void putNonExistingPesoCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pesoCliente.setId(longCount.incrementAndGet());

        // Create the PesoCliente
        PesoClienteDTO pesoClienteDTO = pesoClienteMapper.toDto(pesoCliente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPesoClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pesoClienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pesoClienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PesoCliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPesoCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pesoCliente.setId(longCount.incrementAndGet());

        // Create the PesoCliente
        PesoClienteDTO pesoClienteDTO = pesoClienteMapper.toDto(pesoCliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPesoClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pesoClienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PesoCliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPesoCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pesoCliente.setId(longCount.incrementAndGet());

        // Create the PesoCliente
        PesoClienteDTO pesoClienteDTO = pesoClienteMapper.toDto(pesoCliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPesoClienteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pesoClienteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PesoCliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePesoClienteWithPatch() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pesoCliente using partial update
        PesoCliente partialUpdatedPesoCliente = new PesoCliente();
        partialUpdatedPesoCliente.setId(pesoCliente.getId());

        partialUpdatedPesoCliente.peso(UPDATED_PESO).dataInserimento(UPDATED_DATA_INSERIMENTO);

        restPesoClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPesoCliente.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPesoCliente))
            )
            .andExpect(status().isOk());

        // Validate the PesoCliente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPesoClienteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPesoCliente, pesoCliente),
            getPersistedPesoCliente(pesoCliente)
        );
    }

    @Test
    @Transactional
    void fullUpdatePesoClienteWithPatch() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pesoCliente using partial update
        PesoCliente partialUpdatedPesoCliente = new PesoCliente();
        partialUpdatedPesoCliente.setId(pesoCliente.getId());

        partialUpdatedPesoCliente.peso(UPDATED_PESO).mese(UPDATED_MESE).dataInserimento(UPDATED_DATA_INSERIMENTO);

        restPesoClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPesoCliente.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPesoCliente))
            )
            .andExpect(status().isOk());

        // Validate the PesoCliente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPesoClienteUpdatableFieldsEquals(partialUpdatedPesoCliente, getPersistedPesoCliente(partialUpdatedPesoCliente));
    }

    @Test
    @Transactional
    void patchNonExistingPesoCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pesoCliente.setId(longCount.incrementAndGet());

        // Create the PesoCliente
        PesoClienteDTO pesoClienteDTO = pesoClienteMapper.toDto(pesoCliente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPesoClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pesoClienteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pesoClienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PesoCliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPesoCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pesoCliente.setId(longCount.incrementAndGet());

        // Create the PesoCliente
        PesoClienteDTO pesoClienteDTO = pesoClienteMapper.toDto(pesoCliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPesoClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pesoClienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PesoCliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPesoCliente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pesoCliente.setId(longCount.incrementAndGet());

        // Create the PesoCliente
        PesoClienteDTO pesoClienteDTO = pesoClienteMapper.toDto(pesoCliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPesoClienteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pesoClienteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PesoCliente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePesoCliente() throws Exception {
        // Initialize the database
        insertedPesoCliente = pesoClienteRepository.saveAndFlush(pesoCliente);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pesoCliente
        restPesoClienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, pesoCliente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pesoClienteRepository.count();
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

    protected PesoCliente getPersistedPesoCliente(PesoCliente pesoCliente) {
        return pesoClienteRepository.findById(pesoCliente.getId()).orElseThrow();
    }

    protected void assertPersistedPesoClienteToMatchAllProperties(PesoCliente expectedPesoCliente) {
        assertPesoClienteAllPropertiesEquals(expectedPesoCliente, getPersistedPesoCliente(expectedPesoCliente));
    }

    protected void assertPersistedPesoClienteToMatchUpdatableProperties(PesoCliente expectedPesoCliente) {
        assertPesoClienteAllUpdatablePropertiesEquals(expectedPesoCliente, getPersistedPesoCliente(expectedPesoCliente));
    }
}
