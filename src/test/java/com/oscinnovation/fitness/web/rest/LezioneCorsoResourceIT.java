package com.oscinnovation.fitness.web.rest;

import static com.oscinnovation.fitness.domain.LezioneCorsoAsserts.*;
import static com.oscinnovation.fitness.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscinnovation.fitness.IntegrationTest;
import com.oscinnovation.fitness.domain.CorsoAcademy;
import com.oscinnovation.fitness.domain.LezioneCorso;
import com.oscinnovation.fitness.repository.LezioneCorsoRepository;
import com.oscinnovation.fitness.service.LezioneCorsoService;
import com.oscinnovation.fitness.service.dto.LezioneCorsoDTO;
import com.oscinnovation.fitness.service.mapper.LezioneCorsoMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link LezioneCorsoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LezioneCorsoResourceIT {

    private static final String DEFAULT_TITOLO = "AAAAAAAAAA";
    private static final String UPDATED_TITOLO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIZIONE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIZIONE = "BBBBBBBBBB";

    private static final String DEFAULT_PUNTI_FOCALI = "AAAAAAAAAA";
    private static final String UPDATED_PUNTI_FOCALI = "BBBBBBBBBB";

    private static final String DEFAULT_VIDEO_URL = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/lezione-corsos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LezioneCorsoRepository lezioneCorsoRepository;

    @Mock
    private LezioneCorsoRepository lezioneCorsoRepositoryMock;

    @Autowired
    private LezioneCorsoMapper lezioneCorsoMapper;

    @Mock
    private LezioneCorsoService lezioneCorsoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLezioneCorsoMockMvc;

    private LezioneCorso lezioneCorso;

    private LezioneCorso insertedLezioneCorso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LezioneCorso createEntity(EntityManager em) {
        LezioneCorso lezioneCorso = new LezioneCorso()
            .titolo(DEFAULT_TITOLO)
            .descrizione(DEFAULT_DESCRIZIONE)
            .puntiFocali(DEFAULT_PUNTI_FOCALI)
            .videoUrl(DEFAULT_VIDEO_URL);
        // Add required entity
        CorsoAcademy corsoAcademy;
        if (TestUtil.findAll(em, CorsoAcademy.class).isEmpty()) {
            corsoAcademy = CorsoAcademyResourceIT.createEntity();
            em.persist(corsoAcademy);
            em.flush();
        } else {
            corsoAcademy = TestUtil.findAll(em, CorsoAcademy.class).get(0);
        }
        lezioneCorso.setCorsoAcademy(corsoAcademy);
        return lezioneCorso;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LezioneCorso createUpdatedEntity(EntityManager em) {
        LezioneCorso updatedLezioneCorso = new LezioneCorso()
            .titolo(UPDATED_TITOLO)
            .descrizione(UPDATED_DESCRIZIONE)
            .puntiFocali(UPDATED_PUNTI_FOCALI)
            .videoUrl(UPDATED_VIDEO_URL);
        // Add required entity
        CorsoAcademy corsoAcademy;
        if (TestUtil.findAll(em, CorsoAcademy.class).isEmpty()) {
            corsoAcademy = CorsoAcademyResourceIT.createUpdatedEntity();
            em.persist(corsoAcademy);
            em.flush();
        } else {
            corsoAcademy = TestUtil.findAll(em, CorsoAcademy.class).get(0);
        }
        updatedLezioneCorso.setCorsoAcademy(corsoAcademy);
        return updatedLezioneCorso;
    }

    @BeforeEach
    public void initTest() {
        lezioneCorso = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedLezioneCorso != null) {
            lezioneCorsoRepository.delete(insertedLezioneCorso);
            insertedLezioneCorso = null;
        }
    }

    @Test
    @Transactional
    void createLezioneCorso() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LezioneCorso
        LezioneCorsoDTO lezioneCorsoDTO = lezioneCorsoMapper.toDto(lezioneCorso);
        var returnedLezioneCorsoDTO = om.readValue(
            restLezioneCorsoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lezioneCorsoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LezioneCorsoDTO.class
        );

        // Validate the LezioneCorso in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLezioneCorso = lezioneCorsoMapper.toEntity(returnedLezioneCorsoDTO);
        assertLezioneCorsoUpdatableFieldsEquals(returnedLezioneCorso, getPersistedLezioneCorso(returnedLezioneCorso));

        insertedLezioneCorso = returnedLezioneCorso;
    }

    @Test
    @Transactional
    void createLezioneCorsoWithExistingId() throws Exception {
        // Create the LezioneCorso with an existing ID
        lezioneCorso.setId(1L);
        LezioneCorsoDTO lezioneCorsoDTO = lezioneCorsoMapper.toDto(lezioneCorso);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLezioneCorsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lezioneCorsoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LezioneCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitoloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        lezioneCorso.setTitolo(null);

        // Create the LezioneCorso, which fails.
        LezioneCorsoDTO lezioneCorsoDTO = lezioneCorsoMapper.toDto(lezioneCorso);

        restLezioneCorsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lezioneCorsoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLezioneCorsos() throws Exception {
        // Initialize the database
        insertedLezioneCorso = lezioneCorsoRepository.saveAndFlush(lezioneCorso);

        // Get all the lezioneCorsoList
        restLezioneCorsoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lezioneCorso.getId().intValue())))
            .andExpect(jsonPath("$.[*].titolo").value(hasItem(DEFAULT_TITOLO)))
            .andExpect(jsonPath("$.[*].descrizione").value(hasItem(DEFAULT_DESCRIZIONE.toString())))
            .andExpect(jsonPath("$.[*].puntiFocali").value(hasItem(DEFAULT_PUNTI_FOCALI.toString())))
            .andExpect(jsonPath("$.[*].videoUrl").value(hasItem(DEFAULT_VIDEO_URL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLezioneCorsosWithEagerRelationshipsIsEnabled() throws Exception {
        when(lezioneCorsoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLezioneCorsoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(lezioneCorsoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLezioneCorsosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(lezioneCorsoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLezioneCorsoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(lezioneCorsoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLezioneCorso() throws Exception {
        // Initialize the database
        insertedLezioneCorso = lezioneCorsoRepository.saveAndFlush(lezioneCorso);

        // Get the lezioneCorso
        restLezioneCorsoMockMvc
            .perform(get(ENTITY_API_URL_ID, lezioneCorso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lezioneCorso.getId().intValue()))
            .andExpect(jsonPath("$.titolo").value(DEFAULT_TITOLO))
            .andExpect(jsonPath("$.descrizione").value(DEFAULT_DESCRIZIONE.toString()))
            .andExpect(jsonPath("$.puntiFocali").value(DEFAULT_PUNTI_FOCALI.toString()))
            .andExpect(jsonPath("$.videoUrl").value(DEFAULT_VIDEO_URL));
    }

    @Test
    @Transactional
    void getLezioneCorsosByIdFiltering() throws Exception {
        // Initialize the database
        insertedLezioneCorso = lezioneCorsoRepository.saveAndFlush(lezioneCorso);

        Long id = lezioneCorso.getId();

        defaultLezioneCorsoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLezioneCorsoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLezioneCorsoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLezioneCorsosByTitoloIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLezioneCorso = lezioneCorsoRepository.saveAndFlush(lezioneCorso);

        // Get all the lezioneCorsoList where titolo equals to
        defaultLezioneCorsoFiltering("titolo.equals=" + DEFAULT_TITOLO, "titolo.equals=" + UPDATED_TITOLO);
    }

    @Test
    @Transactional
    void getAllLezioneCorsosByTitoloIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLezioneCorso = lezioneCorsoRepository.saveAndFlush(lezioneCorso);

        // Get all the lezioneCorsoList where titolo in
        defaultLezioneCorsoFiltering("titolo.in=" + DEFAULT_TITOLO + "," + UPDATED_TITOLO, "titolo.in=" + UPDATED_TITOLO);
    }

    @Test
    @Transactional
    void getAllLezioneCorsosByTitoloIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLezioneCorso = lezioneCorsoRepository.saveAndFlush(lezioneCorso);

        // Get all the lezioneCorsoList where titolo is not null
        defaultLezioneCorsoFiltering("titolo.specified=true", "titolo.specified=false");
    }

    @Test
    @Transactional
    void getAllLezioneCorsosByTitoloContainsSomething() throws Exception {
        // Initialize the database
        insertedLezioneCorso = lezioneCorsoRepository.saveAndFlush(lezioneCorso);

        // Get all the lezioneCorsoList where titolo contains
        defaultLezioneCorsoFiltering("titolo.contains=" + DEFAULT_TITOLO, "titolo.contains=" + UPDATED_TITOLO);
    }

    @Test
    @Transactional
    void getAllLezioneCorsosByTitoloNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLezioneCorso = lezioneCorsoRepository.saveAndFlush(lezioneCorso);

        // Get all the lezioneCorsoList where titolo does not contain
        defaultLezioneCorsoFiltering("titolo.doesNotContain=" + UPDATED_TITOLO, "titolo.doesNotContain=" + DEFAULT_TITOLO);
    }

    @Test
    @Transactional
    void getAllLezioneCorsosByVideoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLezioneCorso = lezioneCorsoRepository.saveAndFlush(lezioneCorso);

        // Get all the lezioneCorsoList where videoUrl equals to
        defaultLezioneCorsoFiltering("videoUrl.equals=" + DEFAULT_VIDEO_URL, "videoUrl.equals=" + UPDATED_VIDEO_URL);
    }

    @Test
    @Transactional
    void getAllLezioneCorsosByVideoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLezioneCorso = lezioneCorsoRepository.saveAndFlush(lezioneCorso);

        // Get all the lezioneCorsoList where videoUrl in
        defaultLezioneCorsoFiltering("videoUrl.in=" + DEFAULT_VIDEO_URL + "," + UPDATED_VIDEO_URL, "videoUrl.in=" + UPDATED_VIDEO_URL);
    }

    @Test
    @Transactional
    void getAllLezioneCorsosByVideoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLezioneCorso = lezioneCorsoRepository.saveAndFlush(lezioneCorso);

        // Get all the lezioneCorsoList where videoUrl is not null
        defaultLezioneCorsoFiltering("videoUrl.specified=true", "videoUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllLezioneCorsosByVideoUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedLezioneCorso = lezioneCorsoRepository.saveAndFlush(lezioneCorso);

        // Get all the lezioneCorsoList where videoUrl contains
        defaultLezioneCorsoFiltering("videoUrl.contains=" + DEFAULT_VIDEO_URL, "videoUrl.contains=" + UPDATED_VIDEO_URL);
    }

    @Test
    @Transactional
    void getAllLezioneCorsosByVideoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLezioneCorso = lezioneCorsoRepository.saveAndFlush(lezioneCorso);

        // Get all the lezioneCorsoList where videoUrl does not contain
        defaultLezioneCorsoFiltering("videoUrl.doesNotContain=" + UPDATED_VIDEO_URL, "videoUrl.doesNotContain=" + DEFAULT_VIDEO_URL);
    }

    @Test
    @Transactional
    void getAllLezioneCorsosByCorsoAcademyIsEqualToSomething() throws Exception {
        CorsoAcademy corsoAcademy;
        if (TestUtil.findAll(em, CorsoAcademy.class).isEmpty()) {
            lezioneCorsoRepository.saveAndFlush(lezioneCorso);
            corsoAcademy = CorsoAcademyResourceIT.createEntity();
        } else {
            corsoAcademy = TestUtil.findAll(em, CorsoAcademy.class).get(0);
        }
        em.persist(corsoAcademy);
        em.flush();
        lezioneCorso.setCorsoAcademy(corsoAcademy);
        lezioneCorsoRepository.saveAndFlush(lezioneCorso);
        Long corsoAcademyId = corsoAcademy.getId();
        // Get all the lezioneCorsoList where corsoAcademy equals to corsoAcademyId
        defaultLezioneCorsoShouldBeFound("corsoAcademyId.equals=" + corsoAcademyId);

        // Get all the lezioneCorsoList where corsoAcademy equals to (corsoAcademyId + 1)
        defaultLezioneCorsoShouldNotBeFound("corsoAcademyId.equals=" + (corsoAcademyId + 1));
    }

    private void defaultLezioneCorsoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLezioneCorsoShouldBeFound(shouldBeFound);
        defaultLezioneCorsoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLezioneCorsoShouldBeFound(String filter) throws Exception {
        restLezioneCorsoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lezioneCorso.getId().intValue())))
            .andExpect(jsonPath("$.[*].titolo").value(hasItem(DEFAULT_TITOLO)))
            .andExpect(jsonPath("$.[*].descrizione").value(hasItem(DEFAULT_DESCRIZIONE.toString())))
            .andExpect(jsonPath("$.[*].puntiFocali").value(hasItem(DEFAULT_PUNTI_FOCALI.toString())))
            .andExpect(jsonPath("$.[*].videoUrl").value(hasItem(DEFAULT_VIDEO_URL)));

        // Check, that the count call also returns 1
        restLezioneCorsoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLezioneCorsoShouldNotBeFound(String filter) throws Exception {
        restLezioneCorsoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLezioneCorsoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLezioneCorso() throws Exception {
        // Get the lezioneCorso
        restLezioneCorsoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLezioneCorso() throws Exception {
        // Initialize the database
        insertedLezioneCorso = lezioneCorsoRepository.saveAndFlush(lezioneCorso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lezioneCorso
        LezioneCorso updatedLezioneCorso = lezioneCorsoRepository.findById(lezioneCorso.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLezioneCorso are not directly saved in db
        em.detach(updatedLezioneCorso);
        updatedLezioneCorso
            .titolo(UPDATED_TITOLO)
            .descrizione(UPDATED_DESCRIZIONE)
            .puntiFocali(UPDATED_PUNTI_FOCALI)
            .videoUrl(UPDATED_VIDEO_URL);
        LezioneCorsoDTO lezioneCorsoDTO = lezioneCorsoMapper.toDto(updatedLezioneCorso);

        restLezioneCorsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lezioneCorsoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lezioneCorsoDTO))
            )
            .andExpect(status().isOk());

        // Validate the LezioneCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLezioneCorsoToMatchAllProperties(updatedLezioneCorso);
    }

    @Test
    @Transactional
    void putNonExistingLezioneCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lezioneCorso.setId(longCount.incrementAndGet());

        // Create the LezioneCorso
        LezioneCorsoDTO lezioneCorsoDTO = lezioneCorsoMapper.toDto(lezioneCorso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLezioneCorsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lezioneCorsoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lezioneCorsoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LezioneCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLezioneCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lezioneCorso.setId(longCount.incrementAndGet());

        // Create the LezioneCorso
        LezioneCorsoDTO lezioneCorsoDTO = lezioneCorsoMapper.toDto(lezioneCorso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLezioneCorsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lezioneCorsoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LezioneCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLezioneCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lezioneCorso.setId(longCount.incrementAndGet());

        // Create the LezioneCorso
        LezioneCorsoDTO lezioneCorsoDTO = lezioneCorsoMapper.toDto(lezioneCorso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLezioneCorsoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lezioneCorsoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LezioneCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLezioneCorsoWithPatch() throws Exception {
        // Initialize the database
        insertedLezioneCorso = lezioneCorsoRepository.saveAndFlush(lezioneCorso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lezioneCorso using partial update
        LezioneCorso partialUpdatedLezioneCorso = new LezioneCorso();
        partialUpdatedLezioneCorso.setId(lezioneCorso.getId());

        partialUpdatedLezioneCorso.titolo(UPDATED_TITOLO).descrizione(UPDATED_DESCRIZIONE).puntiFocali(UPDATED_PUNTI_FOCALI);

        restLezioneCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLezioneCorso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLezioneCorso))
            )
            .andExpect(status().isOk());

        // Validate the LezioneCorso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLezioneCorsoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLezioneCorso, lezioneCorso),
            getPersistedLezioneCorso(lezioneCorso)
        );
    }

    @Test
    @Transactional
    void fullUpdateLezioneCorsoWithPatch() throws Exception {
        // Initialize the database
        insertedLezioneCorso = lezioneCorsoRepository.saveAndFlush(lezioneCorso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lezioneCorso using partial update
        LezioneCorso partialUpdatedLezioneCorso = new LezioneCorso();
        partialUpdatedLezioneCorso.setId(lezioneCorso.getId());

        partialUpdatedLezioneCorso
            .titolo(UPDATED_TITOLO)
            .descrizione(UPDATED_DESCRIZIONE)
            .puntiFocali(UPDATED_PUNTI_FOCALI)
            .videoUrl(UPDATED_VIDEO_URL);

        restLezioneCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLezioneCorso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLezioneCorso))
            )
            .andExpect(status().isOk());

        // Validate the LezioneCorso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLezioneCorsoUpdatableFieldsEquals(partialUpdatedLezioneCorso, getPersistedLezioneCorso(partialUpdatedLezioneCorso));
    }

    @Test
    @Transactional
    void patchNonExistingLezioneCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lezioneCorso.setId(longCount.incrementAndGet());

        // Create the LezioneCorso
        LezioneCorsoDTO lezioneCorsoDTO = lezioneCorsoMapper.toDto(lezioneCorso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLezioneCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lezioneCorsoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(lezioneCorsoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LezioneCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLezioneCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lezioneCorso.setId(longCount.incrementAndGet());

        // Create the LezioneCorso
        LezioneCorsoDTO lezioneCorsoDTO = lezioneCorsoMapper.toDto(lezioneCorso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLezioneCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(lezioneCorsoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LezioneCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLezioneCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lezioneCorso.setId(longCount.incrementAndGet());

        // Create the LezioneCorso
        LezioneCorsoDTO lezioneCorsoDTO = lezioneCorsoMapper.toDto(lezioneCorso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLezioneCorsoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(lezioneCorsoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LezioneCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLezioneCorso() throws Exception {
        // Initialize the database
        insertedLezioneCorso = lezioneCorsoRepository.saveAndFlush(lezioneCorso);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the lezioneCorso
        restLezioneCorsoMockMvc
            .perform(delete(ENTITY_API_URL_ID, lezioneCorso.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return lezioneCorsoRepository.count();
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

    protected LezioneCorso getPersistedLezioneCorso(LezioneCorso lezioneCorso) {
        return lezioneCorsoRepository.findById(lezioneCorso.getId()).orElseThrow();
    }

    protected void assertPersistedLezioneCorsoToMatchAllProperties(LezioneCorso expectedLezioneCorso) {
        assertLezioneCorsoAllPropertiesEquals(expectedLezioneCorso, getPersistedLezioneCorso(expectedLezioneCorso));
    }

    protected void assertPersistedLezioneCorsoToMatchUpdatableProperties(LezioneCorso expectedLezioneCorso) {
        assertLezioneCorsoAllUpdatablePropertiesEquals(expectedLezioneCorso, getPersistedLezioneCorso(expectedLezioneCorso));
    }
}
