package com.oscinnovation.fitness.web.rest;

import static com.oscinnovation.fitness.domain.ClienteToLezioneCorsoAsserts.*;
import static com.oscinnovation.fitness.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscinnovation.fitness.IntegrationTest;
import com.oscinnovation.fitness.domain.Cliente;
import com.oscinnovation.fitness.domain.ClienteToLezioneCorso;
import com.oscinnovation.fitness.domain.LezioneCorso;
import com.oscinnovation.fitness.repository.ClienteToLezioneCorsoRepository;
import com.oscinnovation.fitness.service.ClienteToLezioneCorsoService;
import com.oscinnovation.fitness.service.dto.ClienteToLezioneCorsoDTO;
import com.oscinnovation.fitness.service.mapper.ClienteToLezioneCorsoMapper;
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
 * Integration tests for the {@link ClienteToLezioneCorsoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ClienteToLezioneCorsoResourceIT {

    private static final Boolean DEFAULT_COMPLETATO = false;
    private static final Boolean UPDATED_COMPLETATO = true;

    private static final LocalDate DEFAULT_DATA_COMPLETAMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_COMPLETAMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_COMPLETAMENTO = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/cliente-to-lezione-corsos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClienteToLezioneCorsoRepository clienteToLezioneCorsoRepository;

    @Mock
    private ClienteToLezioneCorsoRepository clienteToLezioneCorsoRepositoryMock;

    @Autowired
    private ClienteToLezioneCorsoMapper clienteToLezioneCorsoMapper;

    @Mock
    private ClienteToLezioneCorsoService clienteToLezioneCorsoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClienteToLezioneCorsoMockMvc;

    private ClienteToLezioneCorso clienteToLezioneCorso;

    private ClienteToLezioneCorso insertedClienteToLezioneCorso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClienteToLezioneCorso createEntity(EntityManager em) {
        ClienteToLezioneCorso clienteToLezioneCorso = new ClienteToLezioneCorso()
            .completato(DEFAULT_COMPLETATO)
            .dataCompletamento(DEFAULT_DATA_COMPLETAMENTO);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        clienteToLezioneCorso.setCliente(cliente);
        // Add required entity
        LezioneCorso lezioneCorso;
        if (TestUtil.findAll(em, LezioneCorso.class).isEmpty()) {
            lezioneCorso = LezioneCorsoResourceIT.createEntity(em);
            em.persist(lezioneCorso);
            em.flush();
        } else {
            lezioneCorso = TestUtil.findAll(em, LezioneCorso.class).get(0);
        }
        clienteToLezioneCorso.setLezioneCorso(lezioneCorso);
        return clienteToLezioneCorso;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClienteToLezioneCorso createUpdatedEntity(EntityManager em) {
        ClienteToLezioneCorso updatedClienteToLezioneCorso = new ClienteToLezioneCorso()
            .completato(UPDATED_COMPLETATO)
            .dataCompletamento(UPDATED_DATA_COMPLETAMENTO);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createUpdatedEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        updatedClienteToLezioneCorso.setCliente(cliente);
        // Add required entity
        LezioneCorso lezioneCorso;
        if (TestUtil.findAll(em, LezioneCorso.class).isEmpty()) {
            lezioneCorso = LezioneCorsoResourceIT.createUpdatedEntity(em);
            em.persist(lezioneCorso);
            em.flush();
        } else {
            lezioneCorso = TestUtil.findAll(em, LezioneCorso.class).get(0);
        }
        updatedClienteToLezioneCorso.setLezioneCorso(lezioneCorso);
        return updatedClienteToLezioneCorso;
    }

    @BeforeEach
    public void initTest() {
        clienteToLezioneCorso = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedClienteToLezioneCorso != null) {
            clienteToLezioneCorsoRepository.delete(insertedClienteToLezioneCorso);
            insertedClienteToLezioneCorso = null;
        }
    }

    @Test
    @Transactional
    void createClienteToLezioneCorso() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ClienteToLezioneCorso
        ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO = clienteToLezioneCorsoMapper.toDto(clienteToLezioneCorso);
        var returnedClienteToLezioneCorsoDTO = om.readValue(
            restClienteToLezioneCorsoMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteToLezioneCorsoDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClienteToLezioneCorsoDTO.class
        );

        // Validate the ClienteToLezioneCorso in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClienteToLezioneCorso = clienteToLezioneCorsoMapper.toEntity(returnedClienteToLezioneCorsoDTO);
        assertClienteToLezioneCorsoUpdatableFieldsEquals(
            returnedClienteToLezioneCorso,
            getPersistedClienteToLezioneCorso(returnedClienteToLezioneCorso)
        );

        insertedClienteToLezioneCorso = returnedClienteToLezioneCorso;
    }

    @Test
    @Transactional
    void createClienteToLezioneCorsoWithExistingId() throws Exception {
        // Create the ClienteToLezioneCorso with an existing ID
        clienteToLezioneCorso.setId(1L);
        ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO = clienteToLezioneCorsoMapper.toDto(clienteToLezioneCorso);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClienteToLezioneCorsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteToLezioneCorsoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClienteToLezioneCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllClienteToLezioneCorsos() throws Exception {
        // Initialize the database
        insertedClienteToLezioneCorso = clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);

        // Get all the clienteToLezioneCorsoList
        restClienteToLezioneCorsoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clienteToLezioneCorso.getId().intValue())))
            .andExpect(jsonPath("$.[*].completato").value(hasItem(DEFAULT_COMPLETATO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataCompletamento").value(hasItem(DEFAULT_DATA_COMPLETAMENTO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClienteToLezioneCorsosWithEagerRelationshipsIsEnabled() throws Exception {
        when(clienteToLezioneCorsoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClienteToLezioneCorsoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(clienteToLezioneCorsoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClienteToLezioneCorsosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(clienteToLezioneCorsoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClienteToLezioneCorsoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(clienteToLezioneCorsoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getClienteToLezioneCorso() throws Exception {
        // Initialize the database
        insertedClienteToLezioneCorso = clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);

        // Get the clienteToLezioneCorso
        restClienteToLezioneCorsoMockMvc
            .perform(get(ENTITY_API_URL_ID, clienteToLezioneCorso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clienteToLezioneCorso.getId().intValue()))
            .andExpect(jsonPath("$.completato").value(DEFAULT_COMPLETATO.booleanValue()))
            .andExpect(jsonPath("$.dataCompletamento").value(DEFAULT_DATA_COMPLETAMENTO.toString()));
    }

    @Test
    @Transactional
    void getClienteToLezioneCorsosByIdFiltering() throws Exception {
        // Initialize the database
        insertedClienteToLezioneCorso = clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);

        Long id = clienteToLezioneCorso.getId();

        defaultClienteToLezioneCorsoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultClienteToLezioneCorsoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultClienteToLezioneCorsoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClienteToLezioneCorsosByCompletatoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClienteToLezioneCorso = clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);

        // Get all the clienteToLezioneCorsoList where completato equals to
        defaultClienteToLezioneCorsoFiltering("completato.equals=" + DEFAULT_COMPLETATO, "completato.equals=" + UPDATED_COMPLETATO);
    }

    @Test
    @Transactional
    void getAllClienteToLezioneCorsosByCompletatoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClienteToLezioneCorso = clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);

        // Get all the clienteToLezioneCorsoList where completato in
        defaultClienteToLezioneCorsoFiltering(
            "completato.in=" + DEFAULT_COMPLETATO + "," + UPDATED_COMPLETATO,
            "completato.in=" + UPDATED_COMPLETATO
        );
    }

    @Test
    @Transactional
    void getAllClienteToLezioneCorsosByCompletatoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClienteToLezioneCorso = clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);

        // Get all the clienteToLezioneCorsoList where completato is not null
        defaultClienteToLezioneCorsoFiltering("completato.specified=true", "completato.specified=false");
    }

    @Test
    @Transactional
    void getAllClienteToLezioneCorsosByDataCompletamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClienteToLezioneCorso = clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);

        // Get all the clienteToLezioneCorsoList where dataCompletamento equals to
        defaultClienteToLezioneCorsoFiltering(
            "dataCompletamento.equals=" + DEFAULT_DATA_COMPLETAMENTO,
            "dataCompletamento.equals=" + UPDATED_DATA_COMPLETAMENTO
        );
    }

    @Test
    @Transactional
    void getAllClienteToLezioneCorsosByDataCompletamentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClienteToLezioneCorso = clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);

        // Get all the clienteToLezioneCorsoList where dataCompletamento in
        defaultClienteToLezioneCorsoFiltering(
            "dataCompletamento.in=" + DEFAULT_DATA_COMPLETAMENTO + "," + UPDATED_DATA_COMPLETAMENTO,
            "dataCompletamento.in=" + UPDATED_DATA_COMPLETAMENTO
        );
    }

    @Test
    @Transactional
    void getAllClienteToLezioneCorsosByDataCompletamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClienteToLezioneCorso = clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);

        // Get all the clienteToLezioneCorsoList where dataCompletamento is not null
        defaultClienteToLezioneCorsoFiltering("dataCompletamento.specified=true", "dataCompletamento.specified=false");
    }

    @Test
    @Transactional
    void getAllClienteToLezioneCorsosByDataCompletamentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClienteToLezioneCorso = clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);

        // Get all the clienteToLezioneCorsoList where dataCompletamento is greater than or equal to
        defaultClienteToLezioneCorsoFiltering(
            "dataCompletamento.greaterThanOrEqual=" + DEFAULT_DATA_COMPLETAMENTO,
            "dataCompletamento.greaterThanOrEqual=" + UPDATED_DATA_COMPLETAMENTO
        );
    }

    @Test
    @Transactional
    void getAllClienteToLezioneCorsosByDataCompletamentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClienteToLezioneCorso = clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);

        // Get all the clienteToLezioneCorsoList where dataCompletamento is less than or equal to
        defaultClienteToLezioneCorsoFiltering(
            "dataCompletamento.lessThanOrEqual=" + DEFAULT_DATA_COMPLETAMENTO,
            "dataCompletamento.lessThanOrEqual=" + SMALLER_DATA_COMPLETAMENTO
        );
    }

    @Test
    @Transactional
    void getAllClienteToLezioneCorsosByDataCompletamentoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedClienteToLezioneCorso = clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);

        // Get all the clienteToLezioneCorsoList where dataCompletamento is less than
        defaultClienteToLezioneCorsoFiltering(
            "dataCompletamento.lessThan=" + UPDATED_DATA_COMPLETAMENTO,
            "dataCompletamento.lessThan=" + DEFAULT_DATA_COMPLETAMENTO
        );
    }

    @Test
    @Transactional
    void getAllClienteToLezioneCorsosByDataCompletamentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedClienteToLezioneCorso = clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);

        // Get all the clienteToLezioneCorsoList where dataCompletamento is greater than
        defaultClienteToLezioneCorsoFiltering(
            "dataCompletamento.greaterThan=" + SMALLER_DATA_COMPLETAMENTO,
            "dataCompletamento.greaterThan=" + DEFAULT_DATA_COMPLETAMENTO
        );
    }

    @Test
    @Transactional
    void getAllClienteToLezioneCorsosByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);
            cliente = ClienteResourceIT.createEntity(em);
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        em.persist(cliente);
        em.flush();
        clienteToLezioneCorso.setCliente(cliente);
        clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);
        Long clienteId = cliente.getId();
        // Get all the clienteToLezioneCorsoList where cliente equals to clienteId
        defaultClienteToLezioneCorsoShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the clienteToLezioneCorsoList where cliente equals to (clienteId + 1)
        defaultClienteToLezioneCorsoShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    @Test
    @Transactional
    void getAllClienteToLezioneCorsosByLezioneCorsoIsEqualToSomething() throws Exception {
        LezioneCorso lezioneCorso;
        if (TestUtil.findAll(em, LezioneCorso.class).isEmpty()) {
            clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);
            lezioneCorso = LezioneCorsoResourceIT.createEntity(em);
        } else {
            lezioneCorso = TestUtil.findAll(em, LezioneCorso.class).get(0);
        }
        em.persist(lezioneCorso);
        em.flush();
        clienteToLezioneCorso.setLezioneCorso(lezioneCorso);
        clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);
        Long lezioneCorsoId = lezioneCorso.getId();
        // Get all the clienteToLezioneCorsoList where lezioneCorso equals to lezioneCorsoId
        defaultClienteToLezioneCorsoShouldBeFound("lezioneCorsoId.equals=" + lezioneCorsoId);

        // Get all the clienteToLezioneCorsoList where lezioneCorso equals to (lezioneCorsoId + 1)
        defaultClienteToLezioneCorsoShouldNotBeFound("lezioneCorsoId.equals=" + (lezioneCorsoId + 1));
    }

    private void defaultClienteToLezioneCorsoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultClienteToLezioneCorsoShouldBeFound(shouldBeFound);
        defaultClienteToLezioneCorsoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClienteToLezioneCorsoShouldBeFound(String filter) throws Exception {
        restClienteToLezioneCorsoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clienteToLezioneCorso.getId().intValue())))
            .andExpect(jsonPath("$.[*].completato").value(hasItem(DEFAULT_COMPLETATO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataCompletamento").value(hasItem(DEFAULT_DATA_COMPLETAMENTO.toString())));

        // Check, that the count call also returns 1
        restClienteToLezioneCorsoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClienteToLezioneCorsoShouldNotBeFound(String filter) throws Exception {
        restClienteToLezioneCorsoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClienteToLezioneCorsoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClienteToLezioneCorso() throws Exception {
        // Get the clienteToLezioneCorso
        restClienteToLezioneCorsoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClienteToLezioneCorso() throws Exception {
        // Initialize the database
        insertedClienteToLezioneCorso = clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clienteToLezioneCorso
        ClienteToLezioneCorso updatedClienteToLezioneCorso = clienteToLezioneCorsoRepository
            .findById(clienteToLezioneCorso.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedClienteToLezioneCorso are not directly saved in db
        em.detach(updatedClienteToLezioneCorso);
        updatedClienteToLezioneCorso.completato(UPDATED_COMPLETATO).dataCompletamento(UPDATED_DATA_COMPLETAMENTO);
        ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO = clienteToLezioneCorsoMapper.toDto(updatedClienteToLezioneCorso);

        restClienteToLezioneCorsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clienteToLezioneCorsoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clienteToLezioneCorsoDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClienteToLezioneCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClienteToLezioneCorsoToMatchAllProperties(updatedClienteToLezioneCorso);
    }

    @Test
    @Transactional
    void putNonExistingClienteToLezioneCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clienteToLezioneCorso.setId(longCount.incrementAndGet());

        // Create the ClienteToLezioneCorso
        ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO = clienteToLezioneCorsoMapper.toDto(clienteToLezioneCorso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteToLezioneCorsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clienteToLezioneCorsoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clienteToLezioneCorsoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClienteToLezioneCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClienteToLezioneCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clienteToLezioneCorso.setId(longCount.incrementAndGet());

        // Create the ClienteToLezioneCorso
        ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO = clienteToLezioneCorsoMapper.toDto(clienteToLezioneCorso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteToLezioneCorsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clienteToLezioneCorsoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClienteToLezioneCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClienteToLezioneCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clienteToLezioneCorso.setId(longCount.incrementAndGet());

        // Create the ClienteToLezioneCorso
        ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO = clienteToLezioneCorsoMapper.toDto(clienteToLezioneCorso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteToLezioneCorsoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteToLezioneCorsoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClienteToLezioneCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClienteToLezioneCorsoWithPatch() throws Exception {
        // Initialize the database
        insertedClienteToLezioneCorso = clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clienteToLezioneCorso using partial update
        ClienteToLezioneCorso partialUpdatedClienteToLezioneCorso = new ClienteToLezioneCorso();
        partialUpdatedClienteToLezioneCorso.setId(clienteToLezioneCorso.getId());

        partialUpdatedClienteToLezioneCorso.dataCompletamento(UPDATED_DATA_COMPLETAMENTO);

        restClienteToLezioneCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClienteToLezioneCorso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClienteToLezioneCorso))
            )
            .andExpect(status().isOk());

        // Validate the ClienteToLezioneCorso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClienteToLezioneCorsoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedClienteToLezioneCorso, clienteToLezioneCorso),
            getPersistedClienteToLezioneCorso(clienteToLezioneCorso)
        );
    }

    @Test
    @Transactional
    void fullUpdateClienteToLezioneCorsoWithPatch() throws Exception {
        // Initialize the database
        insertedClienteToLezioneCorso = clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clienteToLezioneCorso using partial update
        ClienteToLezioneCorso partialUpdatedClienteToLezioneCorso = new ClienteToLezioneCorso();
        partialUpdatedClienteToLezioneCorso.setId(clienteToLezioneCorso.getId());

        partialUpdatedClienteToLezioneCorso.completato(UPDATED_COMPLETATO).dataCompletamento(UPDATED_DATA_COMPLETAMENTO);

        restClienteToLezioneCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClienteToLezioneCorso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClienteToLezioneCorso))
            )
            .andExpect(status().isOk());

        // Validate the ClienteToLezioneCorso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClienteToLezioneCorsoUpdatableFieldsEquals(
            partialUpdatedClienteToLezioneCorso,
            getPersistedClienteToLezioneCorso(partialUpdatedClienteToLezioneCorso)
        );
    }

    @Test
    @Transactional
    void patchNonExistingClienteToLezioneCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clienteToLezioneCorso.setId(longCount.incrementAndGet());

        // Create the ClienteToLezioneCorso
        ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO = clienteToLezioneCorsoMapper.toDto(clienteToLezioneCorso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteToLezioneCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clienteToLezioneCorsoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clienteToLezioneCorsoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClienteToLezioneCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClienteToLezioneCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clienteToLezioneCorso.setId(longCount.incrementAndGet());

        // Create the ClienteToLezioneCorso
        ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO = clienteToLezioneCorsoMapper.toDto(clienteToLezioneCorso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteToLezioneCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clienteToLezioneCorsoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClienteToLezioneCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClienteToLezioneCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clienteToLezioneCorso.setId(longCount.incrementAndGet());

        // Create the ClienteToLezioneCorso
        ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO = clienteToLezioneCorsoMapper.toDto(clienteToLezioneCorso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteToLezioneCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(clienteToLezioneCorsoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClienteToLezioneCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClienteToLezioneCorso() throws Exception {
        // Initialize the database
        insertedClienteToLezioneCorso = clienteToLezioneCorsoRepository.saveAndFlush(clienteToLezioneCorso);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the clienteToLezioneCorso
        restClienteToLezioneCorsoMockMvc
            .perform(delete(ENTITY_API_URL_ID, clienteToLezioneCorso.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return clienteToLezioneCorsoRepository.count();
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

    protected ClienteToLezioneCorso getPersistedClienteToLezioneCorso(ClienteToLezioneCorso clienteToLezioneCorso) {
        return clienteToLezioneCorsoRepository.findById(clienteToLezioneCorso.getId()).orElseThrow();
    }

    protected void assertPersistedClienteToLezioneCorsoToMatchAllProperties(ClienteToLezioneCorso expectedClienteToLezioneCorso) {
        assertClienteToLezioneCorsoAllPropertiesEquals(
            expectedClienteToLezioneCorso,
            getPersistedClienteToLezioneCorso(expectedClienteToLezioneCorso)
        );
    }

    protected void assertPersistedClienteToLezioneCorsoToMatchUpdatableProperties(ClienteToLezioneCorso expectedClienteToLezioneCorso) {
        assertClienteToLezioneCorsoAllUpdatablePropertiesEquals(
            expectedClienteToLezioneCorso,
            getPersistedClienteToLezioneCorso(expectedClienteToLezioneCorso)
        );
    }
}
