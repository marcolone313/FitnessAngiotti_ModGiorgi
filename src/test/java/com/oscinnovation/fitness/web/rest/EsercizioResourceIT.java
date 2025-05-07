package com.oscinnovation.fitness.web.rest;

import static com.oscinnovation.fitness.domain.EsercizioAsserts.*;
import static com.oscinnovation.fitness.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscinnovation.fitness.IntegrationTest;
import com.oscinnovation.fitness.domain.Esercizio;
import com.oscinnovation.fitness.domain.enumeration.TipoEsercizio;
import com.oscinnovation.fitness.repository.EsercizioRepository;
import com.oscinnovation.fitness.service.dto.EsercizioDTO;
import com.oscinnovation.fitness.service.mapper.EsercizioMapper;
import jakarta.persistence.EntityManager;
import java.util.Base64;
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
 * Integration tests for the {@link EsercizioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EsercizioResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final TipoEsercizio DEFAULT_TIPO = TipoEsercizio.FULLBODY;
    private static final TipoEsercizio UPDATED_TIPO = TipoEsercizio.UPPERBODY;

    private static final String DEFAULT_VIDEO_URL = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIZIONE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIZIONE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/esercizios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EsercizioRepository esercizioRepository;

    @Autowired
    private EsercizioMapper esercizioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEsercizioMockMvc;

    private Esercizio esercizio;

    private Esercizio insertedEsercizio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Esercizio createEntity() {
        return new Esercizio()
            .nome(DEFAULT_NOME)
            .tipo(DEFAULT_TIPO)
            .videoUrl(DEFAULT_VIDEO_URL)
            .descrizione(DEFAULT_DESCRIZIONE)
            .foto(DEFAULT_FOTO)
            .fotoContentType(DEFAULT_FOTO_CONTENT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Esercizio createUpdatedEntity() {
        return new Esercizio()
            .nome(UPDATED_NOME)
            .tipo(UPDATED_TIPO)
            .videoUrl(UPDATED_VIDEO_URL)
            .descrizione(UPDATED_DESCRIZIONE)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);
    }

    @BeforeEach
    public void initTest() {
        esercizio = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEsercizio != null) {
            esercizioRepository.delete(insertedEsercizio);
            insertedEsercizio = null;
        }
    }

    @Test
    @Transactional
    void createEsercizio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Esercizio
        EsercizioDTO esercizioDTO = esercizioMapper.toDto(esercizio);
        var returnedEsercizioDTO = om.readValue(
            restEsercizioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(esercizioDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EsercizioDTO.class
        );

        // Validate the Esercizio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEsercizio = esercizioMapper.toEntity(returnedEsercizioDTO);
        assertEsercizioUpdatableFieldsEquals(returnedEsercizio, getPersistedEsercizio(returnedEsercizio));

        insertedEsercizio = returnedEsercizio;
    }

    @Test
    @Transactional
    void createEsercizioWithExistingId() throws Exception {
        // Create the Esercizio with an existing ID
        esercizio.setId(1L);
        EsercizioDTO esercizioDTO = esercizioMapper.toDto(esercizio);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEsercizioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(esercizioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Esercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        esercizio.setNome(null);

        // Create the Esercizio, which fails.
        EsercizioDTO esercizioDTO = esercizioMapper.toDto(esercizio);

        restEsercizioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(esercizioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        esercizio.setTipo(null);

        // Create the Esercizio, which fails.
        EsercizioDTO esercizioDTO = esercizioMapper.toDto(esercizio);

        restEsercizioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(esercizioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEsercizios() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        // Get all the esercizioList
        restEsercizioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(esercizio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].videoUrl").value(hasItem(DEFAULT_VIDEO_URL)))
            .andExpect(jsonPath("$.[*].descrizione").value(hasItem(DEFAULT_DESCRIZIONE.toString())))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FOTO))));
    }

    @Test
    @Transactional
    void getEsercizio() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        // Get the esercizio
        restEsercizioMockMvc
            .perform(get(ENTITY_API_URL_ID, esercizio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(esercizio.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.videoUrl").value(DEFAULT_VIDEO_URL))
            .andExpect(jsonPath("$.descrizione").value(DEFAULT_DESCRIZIONE.toString()))
            .andExpect(jsonPath("$.fotoContentType").value(DEFAULT_FOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.foto").value(Base64.getEncoder().encodeToString(DEFAULT_FOTO)));
    }

    @Test
    @Transactional
    void getEserciziosByIdFiltering() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        Long id = esercizio.getId();

        defaultEsercizioFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEsercizioFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEsercizioFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEserciziosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        // Get all the esercizioList where nome equals to
        defaultEsercizioFiltering("nome.equals=" + DEFAULT_NOME, "nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEserciziosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        // Get all the esercizioList where nome in
        defaultEsercizioFiltering("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME, "nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEserciziosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        // Get all the esercizioList where nome is not null
        defaultEsercizioFiltering("nome.specified=true", "nome.specified=false");
    }

    @Test
    @Transactional
    void getAllEserciziosByNomeContainsSomething() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        // Get all the esercizioList where nome contains
        defaultEsercizioFiltering("nome.contains=" + DEFAULT_NOME, "nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEserciziosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        // Get all the esercizioList where nome does not contain
        defaultEsercizioFiltering("nome.doesNotContain=" + UPDATED_NOME, "nome.doesNotContain=" + DEFAULT_NOME);
    }

    @Test
    @Transactional
    void getAllEserciziosByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        // Get all the esercizioList where tipo equals to
        defaultEsercizioFiltering("tipo.equals=" + DEFAULT_TIPO, "tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllEserciziosByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        // Get all the esercizioList where tipo in
        defaultEsercizioFiltering("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO, "tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllEserciziosByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        // Get all the esercizioList where tipo is not null
        defaultEsercizioFiltering("tipo.specified=true", "tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllEserciziosByVideoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        // Get all the esercizioList where videoUrl equals to
        defaultEsercizioFiltering("videoUrl.equals=" + DEFAULT_VIDEO_URL, "videoUrl.equals=" + UPDATED_VIDEO_URL);
    }

    @Test
    @Transactional
    void getAllEserciziosByVideoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        // Get all the esercizioList where videoUrl in
        defaultEsercizioFiltering("videoUrl.in=" + DEFAULT_VIDEO_URL + "," + UPDATED_VIDEO_URL, "videoUrl.in=" + UPDATED_VIDEO_URL);
    }

    @Test
    @Transactional
    void getAllEserciziosByVideoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        // Get all the esercizioList where videoUrl is not null
        defaultEsercizioFiltering("videoUrl.specified=true", "videoUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllEserciziosByVideoUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        // Get all the esercizioList where videoUrl contains
        defaultEsercizioFiltering("videoUrl.contains=" + DEFAULT_VIDEO_URL, "videoUrl.contains=" + UPDATED_VIDEO_URL);
    }

    @Test
    @Transactional
    void getAllEserciziosByVideoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        // Get all the esercizioList where videoUrl does not contain
        defaultEsercizioFiltering("videoUrl.doesNotContain=" + UPDATED_VIDEO_URL, "videoUrl.doesNotContain=" + DEFAULT_VIDEO_URL);
    }

    private void defaultEsercizioFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEsercizioShouldBeFound(shouldBeFound);
        defaultEsercizioShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEsercizioShouldBeFound(String filter) throws Exception {
        restEsercizioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(esercizio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].videoUrl").value(hasItem(DEFAULT_VIDEO_URL)))
            .andExpect(jsonPath("$.[*].descrizione").value(hasItem(DEFAULT_DESCRIZIONE.toString())))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FOTO))));

        // Check, that the count call also returns 1
        restEsercizioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEsercizioShouldNotBeFound(String filter) throws Exception {
        restEsercizioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEsercizioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEsercizio() throws Exception {
        // Get the esercizio
        restEsercizioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEsercizio() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the esercizio
        Esercizio updatedEsercizio = esercizioRepository.findById(esercizio.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEsercizio are not directly saved in db
        em.detach(updatedEsercizio);
        updatedEsercizio
            .nome(UPDATED_NOME)
            .tipo(UPDATED_TIPO)
            .videoUrl(UPDATED_VIDEO_URL)
            .descrizione(UPDATED_DESCRIZIONE)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);
        EsercizioDTO esercizioDTO = esercizioMapper.toDto(updatedEsercizio);

        restEsercizioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, esercizioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(esercizioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Esercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEsercizioToMatchAllProperties(updatedEsercizio);
    }

    @Test
    @Transactional
    void putNonExistingEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        esercizio.setId(longCount.incrementAndGet());

        // Create the Esercizio
        EsercizioDTO esercizioDTO = esercizioMapper.toDto(esercizio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEsercizioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, esercizioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(esercizioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Esercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        esercizio.setId(longCount.incrementAndGet());

        // Create the Esercizio
        EsercizioDTO esercizioDTO = esercizioMapper.toDto(esercizio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEsercizioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(esercizioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Esercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        esercizio.setId(longCount.incrementAndGet());

        // Create the Esercizio
        EsercizioDTO esercizioDTO = esercizioMapper.toDto(esercizio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEsercizioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(esercizioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Esercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEsercizioWithPatch() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the esercizio using partial update
        Esercizio partialUpdatedEsercizio = new Esercizio();
        partialUpdatedEsercizio.setId(esercizio.getId());

        partialUpdatedEsercizio
            .nome(UPDATED_NOME)
            .tipo(UPDATED_TIPO)
            .descrizione(UPDATED_DESCRIZIONE)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        restEsercizioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEsercizio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEsercizio))
            )
            .andExpect(status().isOk());

        // Validate the Esercizio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEsercizioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEsercizio, esercizio),
            getPersistedEsercizio(esercizio)
        );
    }

    @Test
    @Transactional
    void fullUpdateEsercizioWithPatch() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the esercizio using partial update
        Esercizio partialUpdatedEsercizio = new Esercizio();
        partialUpdatedEsercizio.setId(esercizio.getId());

        partialUpdatedEsercizio
            .nome(UPDATED_NOME)
            .tipo(UPDATED_TIPO)
            .videoUrl(UPDATED_VIDEO_URL)
            .descrizione(UPDATED_DESCRIZIONE)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        restEsercizioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEsercizio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEsercizio))
            )
            .andExpect(status().isOk());

        // Validate the Esercizio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEsercizioUpdatableFieldsEquals(partialUpdatedEsercizio, getPersistedEsercizio(partialUpdatedEsercizio));
    }

    @Test
    @Transactional
    void patchNonExistingEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        esercizio.setId(longCount.incrementAndGet());

        // Create the Esercizio
        EsercizioDTO esercizioDTO = esercizioMapper.toDto(esercizio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEsercizioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, esercizioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(esercizioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Esercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        esercizio.setId(longCount.incrementAndGet());

        // Create the Esercizio
        EsercizioDTO esercizioDTO = esercizioMapper.toDto(esercizio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEsercizioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(esercizioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Esercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEsercizio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        esercizio.setId(longCount.incrementAndGet());

        // Create the Esercizio
        EsercizioDTO esercizioDTO = esercizioMapper.toDto(esercizio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEsercizioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(esercizioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Esercizio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEsercizio() throws Exception {
        // Initialize the database
        insertedEsercizio = esercizioRepository.saveAndFlush(esercizio);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the esercizio
        restEsercizioMockMvc
            .perform(delete(ENTITY_API_URL_ID, esercizio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return esercizioRepository.count();
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

    protected Esercizio getPersistedEsercizio(Esercizio esercizio) {
        return esercizioRepository.findById(esercizio.getId()).orElseThrow();
    }

    protected void assertPersistedEsercizioToMatchAllProperties(Esercizio expectedEsercizio) {
        assertEsercizioAllPropertiesEquals(expectedEsercizio, getPersistedEsercizio(expectedEsercizio));
    }

    protected void assertPersistedEsercizioToMatchUpdatableProperties(Esercizio expectedEsercizio) {
        assertEsercizioAllUpdatablePropertiesEquals(expectedEsercizio, getPersistedEsercizio(expectedEsercizio));
    }
}
