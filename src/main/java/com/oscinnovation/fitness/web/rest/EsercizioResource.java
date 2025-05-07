package com.oscinnovation.fitness.web.rest;

import com.oscinnovation.fitness.repository.EsercizioRepository;
import com.oscinnovation.fitness.service.EsercizioQueryService;
import com.oscinnovation.fitness.service.EsercizioService;
import com.oscinnovation.fitness.service.criteria.EsercizioCriteria;
import com.oscinnovation.fitness.service.dto.EsercizioDTO;
import com.oscinnovation.fitness.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.oscinnovation.fitness.domain.Esercizio}.
 */
@RestController
@RequestMapping("/api/esercizios")
public class EsercizioResource {

    private static final Logger LOG = LoggerFactory.getLogger(EsercizioResource.class);

    private static final String ENTITY_NAME = "esercizio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EsercizioService esercizioService;

    private final EsercizioRepository esercizioRepository;

    private final EsercizioQueryService esercizioQueryService;

    public EsercizioResource(
        EsercizioService esercizioService,
        EsercizioRepository esercizioRepository,
        EsercizioQueryService esercizioQueryService
    ) {
        this.esercizioService = esercizioService;
        this.esercizioRepository = esercizioRepository;
        this.esercizioQueryService = esercizioQueryService;
    }

    /**
     * {@code POST  /esercizios} : Create a new esercizio.
     *
     * @param esercizioDTO the esercizioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new esercizioDTO, or with status {@code 400 (Bad Request)} if the esercizio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EsercizioDTO> createEsercizio(@Valid @RequestBody EsercizioDTO esercizioDTO) throws URISyntaxException {
        LOG.debug("REST request to save Esercizio : {}", esercizioDTO);
        if (esercizioDTO.getId() != null) {
            throw new BadRequestAlertException("A new esercizio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        esercizioDTO = esercizioService.save(esercizioDTO);
        return ResponseEntity.created(new URI("/api/esercizios/" + esercizioDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, esercizioDTO.getId().toString()))
            .body(esercizioDTO);
    }

    /**
     * {@code PUT  /esercizios/:id} : Updates an existing esercizio.
     *
     * @param id the id of the esercizioDTO to save.
     * @param esercizioDTO the esercizioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated esercizioDTO,
     * or with status {@code 400 (Bad Request)} if the esercizioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the esercizioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EsercizioDTO> updateEsercizio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EsercizioDTO esercizioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Esercizio : {}, {}", id, esercizioDTO);
        if (esercizioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, esercizioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!esercizioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        esercizioDTO = esercizioService.update(esercizioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, esercizioDTO.getId().toString()))
            .body(esercizioDTO);
    }

    /**
     * {@code PATCH  /esercizios/:id} : Partial updates given fields of an existing esercizio, field will ignore if it is null
     *
     * @param id the id of the esercizioDTO to save.
     * @param esercizioDTO the esercizioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated esercizioDTO,
     * or with status {@code 400 (Bad Request)} if the esercizioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the esercizioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the esercizioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EsercizioDTO> partialUpdateEsercizio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EsercizioDTO esercizioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Esercizio partially : {}, {}", id, esercizioDTO);
        if (esercizioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, esercizioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!esercizioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EsercizioDTO> result = esercizioService.partialUpdate(esercizioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, esercizioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /esercizios} : get all the esercizios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of esercizios in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EsercizioDTO>> getAllEsercizios(
        EsercizioCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Esercizios by criteria: {}", criteria);

        Page<EsercizioDTO> page = esercizioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /esercizios/count} : count all the esercizios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEsercizios(EsercizioCriteria criteria) {
        LOG.debug("REST request to count Esercizios by criteria: {}", criteria);
        return ResponseEntity.ok().body(esercizioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /esercizios/:id} : get the "id" esercizio.
     *
     * @param id the id of the esercizioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the esercizioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EsercizioDTO> getEsercizio(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Esercizio : {}", id);
        Optional<EsercizioDTO> esercizioDTO = esercizioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(esercizioDTO);
    }

    /**
     * {@code DELETE  /esercizios/:id} : delete the "id" esercizio.
     *
     * @param id the id of the esercizioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEsercizio(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Esercizio : {}", id);
        esercizioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
