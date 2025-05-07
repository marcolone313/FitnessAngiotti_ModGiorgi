package com.oscinnovation.fitness.web.rest;

import static com.oscinnovation.fitness.domain.GymAsserts.*;
import static com.oscinnovation.fitness.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscinnovation.fitness.IntegrationTest;
import com.oscinnovation.fitness.domain.AllenamentoGiornaliero;
import com.oscinnovation.fitness.domain.Esercizio;
import com.oscinnovation.fitness.domain.Gym;
import com.oscinnovation.fitness.repository.GymRepository;
import com.oscinnovation.fitness.service.GymService;
import com.oscinnovation.fitness.service.dto.GymDTO;
import com.oscinnovation.fitness.service.mapper.GymMapper;
import jakarta.persistence.EntityManager;
import java.time.Duration;
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
 * Integration tests for the {@link GymResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GymResourceIT {

    private static final Integer DEFAULT_SETS = 1;
    private static final Integer UPDATED_SETS = 2;
    private static final Integer SMALLER_SETS = 1 - 1;

    private static final Integer DEFAULT_REPS = 1;
    private static final Integer UPDATED_REPS = 2;
    private static final Integer SMALLER_REPS = 1 - 1;

    private static final Duration DEFAULT_RECUPERO = Duration.ofHours(6);
    private static final Duration UPDATED_RECUPERO = Duration.ofHours(12);
    private static final Duration SMALLER_RECUPERO = Duration.ofHours(5);

    private static final Float DEFAULT_PESO = 0F;
    private static final Float UPDATED_PESO = 1F;
    private static final Float SMALLER_PESO = 0F - 1F;

    private static final Boolean DEFAULT_COMPLETATO = false;
    private static final Boolean UPDATED_COMPLETATO = true;

    private static final Boolean DEFAULT_SVOLTO = false;
    private static final Boolean UPDATED_SVOLTO = true;

    private static final String DEFAULT_FEEDBACK = "AAAAAAAAAA";
    private static final String UPDATED_FEEDBACK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/gyms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GymRepository gymRepository;

    @Mock
    private GymRepository gymRepositoryMock;

    @Autowired
    private GymMapper gymMapper;

    @Mock
    private GymService gymServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGymMockMvc;

    private Gym gym;

    private Gym insertedGym;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gym createEntity(EntityManager em) {
        Gym gym = new Gym()
            .sets(DEFAULT_SETS)
            .reps(DEFAULT_REPS)
            .recupero(DEFAULT_RECUPERO)
            .peso(DEFAULT_PESO)
            .completato(DEFAULT_COMPLETATO)
            .svolto(DEFAULT_SVOLTO)
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
        gym.setAllenamentoGiornaliero(allenamentoGiornaliero);
        // Add required entity
        Esercizio esercizio;
        if (TestUtil.findAll(em, Esercizio.class).isEmpty()) {
            esercizio = EsercizioResourceIT.createEntity();
            em.persist(esercizio);
            em.flush();
        } else {
            esercizio = TestUtil.findAll(em, Esercizio.class).get(0);
        }
        gym.setEsercizio(esercizio);
        return gym;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gym createUpdatedEntity(EntityManager em) {
        Gym updatedGym = new Gym()
            .sets(UPDATED_SETS)
            .reps(UPDATED_REPS)
            .recupero(UPDATED_RECUPERO)
            .peso(UPDATED_PESO)
            .completato(UPDATED_COMPLETATO)
            .svolto(UPDATED_SVOLTO)
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
        updatedGym.setAllenamentoGiornaliero(allenamentoGiornaliero);
        // Add required entity
        Esercizio esercizio;
        if (TestUtil.findAll(em, Esercizio.class).isEmpty()) {
            esercizio = EsercizioResourceIT.createUpdatedEntity();
            em.persist(esercizio);
            em.flush();
        } else {
            esercizio = TestUtil.findAll(em, Esercizio.class).get(0);
        }
        updatedGym.setEsercizio(esercizio);
        return updatedGym;
    }

    @BeforeEach
    public void initTest() {
        gym = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedGym != null) {
            gymRepository.delete(insertedGym);
            insertedGym = null;
        }
    }

    @Test
    @Transactional
    void createGym() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Gym
        GymDTO gymDTO = gymMapper.toDto(gym);
        var returnedGymDTO = om.readValue(
            restGymMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gymDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GymDTO.class
        );

        // Validate the Gym in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGym = gymMapper.toEntity(returnedGymDTO);
        assertGymUpdatableFieldsEquals(returnedGym, getPersistedGym(returnedGym));

        insertedGym = returnedGym;
    }

    @Test
    @Transactional
    void createGymWithExistingId() throws Exception {
        // Create the Gym with an existing ID
        gym.setId(1L);
        GymDTO gymDTO = gymMapper.toDto(gym);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGymMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gymDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Gym in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSetsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        gym.setSets(null);

        // Create the Gym, which fails.
        GymDTO gymDTO = gymMapper.toDto(gym);

        restGymMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gymDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRepsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        gym.setReps(null);

        // Create the Gym, which fails.
        GymDTO gymDTO = gymMapper.toDto(gym);

        restGymMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gymDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecuperoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        gym.setRecupero(null);

        // Create the Gym, which fails.
        GymDTO gymDTO = gymMapper.toDto(gym);

        restGymMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gymDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGyms() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList
        restGymMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gym.getId().intValue())))
            .andExpect(jsonPath("$.[*].sets").value(hasItem(DEFAULT_SETS)))
            .andExpect(jsonPath("$.[*].reps").value(hasItem(DEFAULT_REPS)))
            .andExpect(jsonPath("$.[*].recupero").value(hasItem(DEFAULT_RECUPERO.toString())))
            .andExpect(jsonPath("$.[*].peso").value(hasItem(DEFAULT_PESO.doubleValue())))
            .andExpect(jsonPath("$.[*].completato").value(hasItem(DEFAULT_COMPLETATO.booleanValue())))
            .andExpect(jsonPath("$.[*].svolto").value(hasItem(DEFAULT_SVOLTO.booleanValue())))
            .andExpect(jsonPath("$.[*].feedback").value(hasItem(DEFAULT_FEEDBACK.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGymsWithEagerRelationshipsIsEnabled() throws Exception {
        when(gymServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGymMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(gymServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGymsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(gymServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGymMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(gymRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getGym() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get the gym
        restGymMockMvc
            .perform(get(ENTITY_API_URL_ID, gym.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gym.getId().intValue()))
            .andExpect(jsonPath("$.sets").value(DEFAULT_SETS))
            .andExpect(jsonPath("$.reps").value(DEFAULT_REPS))
            .andExpect(jsonPath("$.recupero").value(DEFAULT_RECUPERO.toString()))
            .andExpect(jsonPath("$.peso").value(DEFAULT_PESO.doubleValue()))
            .andExpect(jsonPath("$.completato").value(DEFAULT_COMPLETATO.booleanValue()))
            .andExpect(jsonPath("$.svolto").value(DEFAULT_SVOLTO.booleanValue()))
            .andExpect(jsonPath("$.feedback").value(DEFAULT_FEEDBACK.toString()));
    }

    @Test
    @Transactional
    void getGymsByIdFiltering() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        Long id = gym.getId();

        defaultGymFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultGymFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultGymFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGymsBySetsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where sets equals to
        defaultGymFiltering("sets.equals=" + DEFAULT_SETS, "sets.equals=" + UPDATED_SETS);
    }

    @Test
    @Transactional
    void getAllGymsBySetsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where sets in
        defaultGymFiltering("sets.in=" + DEFAULT_SETS + "," + UPDATED_SETS, "sets.in=" + UPDATED_SETS);
    }

    @Test
    @Transactional
    void getAllGymsBySetsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where sets is not null
        defaultGymFiltering("sets.specified=true", "sets.specified=false");
    }

    @Test
    @Transactional
    void getAllGymsBySetsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where sets is greater than or equal to
        defaultGymFiltering("sets.greaterThanOrEqual=" + DEFAULT_SETS, "sets.greaterThanOrEqual=" + UPDATED_SETS);
    }

    @Test
    @Transactional
    void getAllGymsBySetsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where sets is less than or equal to
        defaultGymFiltering("sets.lessThanOrEqual=" + DEFAULT_SETS, "sets.lessThanOrEqual=" + SMALLER_SETS);
    }

    @Test
    @Transactional
    void getAllGymsBySetsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where sets is less than
        defaultGymFiltering("sets.lessThan=" + UPDATED_SETS, "sets.lessThan=" + DEFAULT_SETS);
    }

    @Test
    @Transactional
    void getAllGymsBySetsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where sets is greater than
        defaultGymFiltering("sets.greaterThan=" + SMALLER_SETS, "sets.greaterThan=" + DEFAULT_SETS);
    }

    @Test
    @Transactional
    void getAllGymsByRepsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where reps equals to
        defaultGymFiltering("reps.equals=" + DEFAULT_REPS, "reps.equals=" + UPDATED_REPS);
    }

    @Test
    @Transactional
    void getAllGymsByRepsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where reps in
        defaultGymFiltering("reps.in=" + DEFAULT_REPS + "," + UPDATED_REPS, "reps.in=" + UPDATED_REPS);
    }

    @Test
    @Transactional
    void getAllGymsByRepsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where reps is not null
        defaultGymFiltering("reps.specified=true", "reps.specified=false");
    }

    @Test
    @Transactional
    void getAllGymsByRepsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where reps is greater than or equal to
        defaultGymFiltering("reps.greaterThanOrEqual=" + DEFAULT_REPS, "reps.greaterThanOrEqual=" + UPDATED_REPS);
    }

    @Test
    @Transactional
    void getAllGymsByRepsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where reps is less than or equal to
        defaultGymFiltering("reps.lessThanOrEqual=" + DEFAULT_REPS, "reps.lessThanOrEqual=" + SMALLER_REPS);
    }

    @Test
    @Transactional
    void getAllGymsByRepsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where reps is less than
        defaultGymFiltering("reps.lessThan=" + UPDATED_REPS, "reps.lessThan=" + DEFAULT_REPS);
    }

    @Test
    @Transactional
    void getAllGymsByRepsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where reps is greater than
        defaultGymFiltering("reps.greaterThan=" + SMALLER_REPS, "reps.greaterThan=" + DEFAULT_REPS);
    }

    @Test
    @Transactional
    void getAllGymsByRecuperoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where recupero equals to
        defaultGymFiltering("recupero.equals=" + DEFAULT_RECUPERO, "recupero.equals=" + UPDATED_RECUPERO);
    }

    @Test
    @Transactional
    void getAllGymsByRecuperoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where recupero in
        defaultGymFiltering("recupero.in=" + DEFAULT_RECUPERO + "," + UPDATED_RECUPERO, "recupero.in=" + UPDATED_RECUPERO);
    }

    @Test
    @Transactional
    void getAllGymsByRecuperoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where recupero is not null
        defaultGymFiltering("recupero.specified=true", "recupero.specified=false");
    }

    @Test
    @Transactional
    void getAllGymsByRecuperoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where recupero is greater than or equal to
        defaultGymFiltering("recupero.greaterThanOrEqual=" + DEFAULT_RECUPERO, "recupero.greaterThanOrEqual=" + UPDATED_RECUPERO);
    }

    @Test
    @Transactional
    void getAllGymsByRecuperoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where recupero is less than or equal to
        defaultGymFiltering("recupero.lessThanOrEqual=" + DEFAULT_RECUPERO, "recupero.lessThanOrEqual=" + SMALLER_RECUPERO);
    }

    @Test
    @Transactional
    void getAllGymsByRecuperoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where recupero is less than
        defaultGymFiltering("recupero.lessThan=" + UPDATED_RECUPERO, "recupero.lessThan=" + DEFAULT_RECUPERO);
    }

    @Test
    @Transactional
    void getAllGymsByRecuperoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where recupero is greater than
        defaultGymFiltering("recupero.greaterThan=" + SMALLER_RECUPERO, "recupero.greaterThan=" + DEFAULT_RECUPERO);
    }

    @Test
    @Transactional
    void getAllGymsByPesoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where peso equals to
        defaultGymFiltering("peso.equals=" + DEFAULT_PESO, "peso.equals=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    void getAllGymsByPesoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where peso in
        defaultGymFiltering("peso.in=" + DEFAULT_PESO + "," + UPDATED_PESO, "peso.in=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    void getAllGymsByPesoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where peso is not null
        defaultGymFiltering("peso.specified=true", "peso.specified=false");
    }

    @Test
    @Transactional
    void getAllGymsByPesoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where peso is greater than or equal to
        defaultGymFiltering("peso.greaterThanOrEqual=" + DEFAULT_PESO, "peso.greaterThanOrEqual=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    void getAllGymsByPesoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where peso is less than or equal to
        defaultGymFiltering("peso.lessThanOrEqual=" + DEFAULT_PESO, "peso.lessThanOrEqual=" + SMALLER_PESO);
    }

    @Test
    @Transactional
    void getAllGymsByPesoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where peso is less than
        defaultGymFiltering("peso.lessThan=" + UPDATED_PESO, "peso.lessThan=" + DEFAULT_PESO);
    }

    @Test
    @Transactional
    void getAllGymsByPesoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where peso is greater than
        defaultGymFiltering("peso.greaterThan=" + SMALLER_PESO, "peso.greaterThan=" + DEFAULT_PESO);
    }

    @Test
    @Transactional
    void getAllGymsByCompletatoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where completato equals to
        defaultGymFiltering("completato.equals=" + DEFAULT_COMPLETATO, "completato.equals=" + UPDATED_COMPLETATO);
    }

    @Test
    @Transactional
    void getAllGymsByCompletatoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where completato in
        defaultGymFiltering("completato.in=" + DEFAULT_COMPLETATO + "," + UPDATED_COMPLETATO, "completato.in=" + UPDATED_COMPLETATO);
    }

    @Test
    @Transactional
    void getAllGymsByCompletatoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where completato is not null
        defaultGymFiltering("completato.specified=true", "completato.specified=false");
    }

    @Test
    @Transactional
    void getAllGymsBySvoltoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where svolto equals to
        defaultGymFiltering("svolto.equals=" + DEFAULT_SVOLTO, "svolto.equals=" + UPDATED_SVOLTO);
    }

    @Test
    @Transactional
    void getAllGymsBySvoltoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where svolto in
        defaultGymFiltering("svolto.in=" + DEFAULT_SVOLTO + "," + UPDATED_SVOLTO, "svolto.in=" + UPDATED_SVOLTO);
    }

    @Test
    @Transactional
    void getAllGymsBySvoltoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        // Get all the gymList where svolto is not null
        defaultGymFiltering("svolto.specified=true", "svolto.specified=false");
    }

    @Test
    @Transactional
    void getAllGymsByAllenamentoGiornalieroIsEqualToSomething() throws Exception {
        AllenamentoGiornaliero allenamentoGiornaliero;
        if (TestUtil.findAll(em, AllenamentoGiornaliero.class).isEmpty()) {
            gymRepository.saveAndFlush(gym);
            allenamentoGiornaliero = AllenamentoGiornalieroResourceIT.createEntity(em);
        } else {
            allenamentoGiornaliero = TestUtil.findAll(em, AllenamentoGiornaliero.class).get(0);
        }
        em.persist(allenamentoGiornaliero);
        em.flush();
        gym.setAllenamentoGiornaliero(allenamentoGiornaliero);
        gymRepository.saveAndFlush(gym);
        Long allenamentoGiornalieroId = allenamentoGiornaliero.getId();
        // Get all the gymList where allenamentoGiornaliero equals to allenamentoGiornalieroId
        defaultGymShouldBeFound("allenamentoGiornalieroId.equals=" + allenamentoGiornalieroId);

        // Get all the gymList where allenamentoGiornaliero equals to (allenamentoGiornalieroId + 1)
        defaultGymShouldNotBeFound("allenamentoGiornalieroId.equals=" + (allenamentoGiornalieroId + 1));
    }

    @Test
    @Transactional
    void getAllGymsByEsercizioIsEqualToSomething() throws Exception {
        Esercizio esercizio;
        if (TestUtil.findAll(em, Esercizio.class).isEmpty()) {
            gymRepository.saveAndFlush(gym);
            esercizio = EsercizioResourceIT.createEntity();
        } else {
            esercizio = TestUtil.findAll(em, Esercizio.class).get(0);
        }
        em.persist(esercizio);
        em.flush();
        gym.setEsercizio(esercizio);
        gymRepository.saveAndFlush(gym);
        Long esercizioId = esercizio.getId();
        // Get all the gymList where esercizio equals to esercizioId
        defaultGymShouldBeFound("esercizioId.equals=" + esercizioId);

        // Get all the gymList where esercizio equals to (esercizioId + 1)
        defaultGymShouldNotBeFound("esercizioId.equals=" + (esercizioId + 1));
    }

    private void defaultGymFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultGymShouldBeFound(shouldBeFound);
        defaultGymShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGymShouldBeFound(String filter) throws Exception {
        restGymMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gym.getId().intValue())))
            .andExpect(jsonPath("$.[*].sets").value(hasItem(DEFAULT_SETS)))
            .andExpect(jsonPath("$.[*].reps").value(hasItem(DEFAULT_REPS)))
            .andExpect(jsonPath("$.[*].recupero").value(hasItem(DEFAULT_RECUPERO.toString())))
            .andExpect(jsonPath("$.[*].peso").value(hasItem(DEFAULT_PESO.doubleValue())))
            .andExpect(jsonPath("$.[*].completato").value(hasItem(DEFAULT_COMPLETATO.booleanValue())))
            .andExpect(jsonPath("$.[*].svolto").value(hasItem(DEFAULT_SVOLTO.booleanValue())))
            .andExpect(jsonPath("$.[*].feedback").value(hasItem(DEFAULT_FEEDBACK.toString())));

        // Check, that the count call also returns 1
        restGymMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGymShouldNotBeFound(String filter) throws Exception {
        restGymMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGymMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGym() throws Exception {
        // Get the gym
        restGymMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGym() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gym
        Gym updatedGym = gymRepository.findById(gym.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGym are not directly saved in db
        em.detach(updatedGym);
        updatedGym
            .sets(UPDATED_SETS)
            .reps(UPDATED_REPS)
            .recupero(UPDATED_RECUPERO)
            .peso(UPDATED_PESO)
            .completato(UPDATED_COMPLETATO)
            .svolto(UPDATED_SVOLTO)
            .feedback(UPDATED_FEEDBACK);
        GymDTO gymDTO = gymMapper.toDto(updatedGym);

        restGymMockMvc
            .perform(put(ENTITY_API_URL_ID, gymDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gymDTO)))
            .andExpect(status().isOk());

        // Validate the Gym in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGymToMatchAllProperties(updatedGym);
    }

    @Test
    @Transactional
    void putNonExistingGym() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gym.setId(longCount.incrementAndGet());

        // Create the Gym
        GymDTO gymDTO = gymMapper.toDto(gym);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGymMockMvc
            .perform(put(ENTITY_API_URL_ID, gymDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gymDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Gym in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGym() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gym.setId(longCount.incrementAndGet());

        // Create the Gym
        GymDTO gymDTO = gymMapper.toDto(gym);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGymMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(gymDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gym in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGym() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gym.setId(longCount.incrementAndGet());

        // Create the Gym
        GymDTO gymDTO = gymMapper.toDto(gym);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGymMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gymDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gym in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGymWithPatch() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gym using partial update
        Gym partialUpdatedGym = new Gym();
        partialUpdatedGym.setId(gym.getId());

        partialUpdatedGym.sets(UPDATED_SETS).recupero(UPDATED_RECUPERO);

        restGymMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGym.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGym))
            )
            .andExpect(status().isOk());

        // Validate the Gym in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGymUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedGym, gym), getPersistedGym(gym));
    }

    @Test
    @Transactional
    void fullUpdateGymWithPatch() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gym using partial update
        Gym partialUpdatedGym = new Gym();
        partialUpdatedGym.setId(gym.getId());

        partialUpdatedGym
            .sets(UPDATED_SETS)
            .reps(UPDATED_REPS)
            .recupero(UPDATED_RECUPERO)
            .peso(UPDATED_PESO)
            .completato(UPDATED_COMPLETATO)
            .svolto(UPDATED_SVOLTO)
            .feedback(UPDATED_FEEDBACK);

        restGymMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGym.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGym))
            )
            .andExpect(status().isOk());

        // Validate the Gym in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGymUpdatableFieldsEquals(partialUpdatedGym, getPersistedGym(partialUpdatedGym));
    }

    @Test
    @Transactional
    void patchNonExistingGym() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gym.setId(longCount.incrementAndGet());

        // Create the Gym
        GymDTO gymDTO = gymMapper.toDto(gym);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGymMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gymDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(gymDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gym in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGym() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gym.setId(longCount.incrementAndGet());

        // Create the Gym
        GymDTO gymDTO = gymMapper.toDto(gym);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGymMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(gymDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gym in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGym() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gym.setId(longCount.incrementAndGet());

        // Create the Gym
        GymDTO gymDTO = gymMapper.toDto(gym);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGymMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(gymDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gym in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGym() throws Exception {
        // Initialize the database
        insertedGym = gymRepository.saveAndFlush(gym);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the gym
        restGymMockMvc.perform(delete(ENTITY_API_URL_ID, gym.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return gymRepository.count();
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

    protected Gym getPersistedGym(Gym gym) {
        return gymRepository.findById(gym.getId()).orElseThrow();
    }

    protected void assertPersistedGymToMatchAllProperties(Gym expectedGym) {
        assertGymAllPropertiesEquals(expectedGym, getPersistedGym(expectedGym));
    }

    protected void assertPersistedGymToMatchUpdatableProperties(Gym expectedGym) {
        assertGymAllUpdatablePropertiesEquals(expectedGym, getPersistedGym(expectedGym));
    }
}
