package com.oscinnovation.fitness.web.rest;

import com.oscinnovation.fitness.repository.CircuitoToEsercizioRepository;
import com.oscinnovation.fitness.service.CircuitoToEsercizioQueryService;
import com.oscinnovation.fitness.service.CircuitoToEsercizioService;
import com.oscinnovation.fitness.service.criteria.CircuitoToEsercizioCriteria;
import com.oscinnovation.fitness.service.dto.CircuitoToEsercizioDTO;
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
 * REST controller for managing {@link com.oscinnovation.fitness.domain.CircuitoToEsercizio}.
 */
@RestController
@RequestMapping("/api/circuito-to-esercizios")
public class CircuitoToEsercizioResource {

    private static final Logger LOG = LoggerFactory.getLogger(CircuitoToEsercizioResource.class);

    private static final String ENTITY_NAME = "circuitoToEsercizio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CircuitoToEsercizioService circuitoToEsercizioService;

    private final CircuitoToEsercizioRepository circuitoToEsercizioRepository;

    private final CircuitoToEsercizioQueryService circuitoToEsercizioQueryService;

    public CircuitoToEsercizioResource(
        CircuitoToEsercizioService circuitoToEsercizioService,
        CircuitoToEsercizioRepository circuitoToEsercizioRepository,
        CircuitoToEsercizioQueryService circuitoToEsercizioQueryService
    ) {
        this.circuitoToEsercizioService = circuitoToEsercizioService;
        this.circuitoToEsercizioRepository = circuitoToEsercizioRepository;
        this.circuitoToEsercizioQueryService = circuitoToEsercizioQueryService;
    }

    /**
     * {@code POST  /circuito-to-esercizios} : Create a new circuitoToEsercizio.
     *
     * @param circuitoToEsercizioDTO the circuitoToEsercizioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new circuitoToEsercizioDTO, or with status {@code 400 (Bad Request)} if the circuitoToEsercizio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CircuitoToEsercizioDTO> createCircuitoToEsercizio(
        @Valid @RequestBody CircuitoToEsercizioDTO circuitoToEsercizioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save CircuitoToEsercizio : {}", circuitoToEsercizioDTO);
        if (circuitoToEsercizioDTO.getId() != null) {
            throw new BadRequestAlertException("A new circuitoToEsercizio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        circuitoToEsercizioDTO = circuitoToEsercizioService.save(circuitoToEsercizioDTO);
        return ResponseEntity.created(new URI("/api/circuito-to-esercizios/" + circuitoToEsercizioDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, circuitoToEsercizioDTO.getId().toString()))
            .body(circuitoToEsercizioDTO);
    }

    /**
     * {@code PUT  /circuito-to-esercizios/:id} : Updates an existing circuitoToEsercizio.
     *
     * @param id the id of the circuitoToEsercizioDTO to save.
     * @param circuitoToEsercizioDTO the circuitoToEsercizioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated circuitoToEsercizioDTO,
     * or with status {@code 400 (Bad Request)} if the circuitoToEsercizioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the circuitoToEsercizioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CircuitoToEsercizioDTO> updateCircuitoToEsercizio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CircuitoToEsercizioDTO circuitoToEsercizioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CircuitoToEsercizio : {}, {}", id, circuitoToEsercizioDTO);
        if (circuitoToEsercizioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, circuitoToEsercizioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!circuitoToEsercizioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        circuitoToEsercizioDTO = circuitoToEsercizioService.update(circuitoToEsercizioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, circuitoToEsercizioDTO.getId().toString()))
            .body(circuitoToEsercizioDTO);
    }

    /**
     * {@code PATCH  /circuito-to-esercizios/:id} : Partial updates given fields of an existing circuitoToEsercizio, field will ignore if it is null
     *
     * @param id the id of the circuitoToEsercizioDTO to save.
     * @param circuitoToEsercizioDTO the circuitoToEsercizioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated circuitoToEsercizioDTO,
     * or with status {@code 400 (Bad Request)} if the circuitoToEsercizioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the circuitoToEsercizioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the circuitoToEsercizioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CircuitoToEsercizioDTO> partialUpdateCircuitoToEsercizio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CircuitoToEsercizioDTO circuitoToEsercizioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CircuitoToEsercizio partially : {}, {}", id, circuitoToEsercizioDTO);
        if (circuitoToEsercizioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, circuitoToEsercizioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!circuitoToEsercizioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CircuitoToEsercizioDTO> result = circuitoToEsercizioService.partialUpdate(circuitoToEsercizioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, circuitoToEsercizioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /circuito-to-esercizios} : get all the circuitoToEsercizios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of circuitoToEsercizios in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CircuitoToEsercizioDTO>> getAllCircuitoToEsercizios(
        CircuitoToEsercizioCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CircuitoToEsercizios by criteria: {}", criteria);

        Page<CircuitoToEsercizioDTO> page = circuitoToEsercizioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /circuito-to-esercizios/count} : count all the circuitoToEsercizios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCircuitoToEsercizios(CircuitoToEsercizioCriteria criteria) {
        LOG.debug("REST request to count CircuitoToEsercizios by criteria: {}", criteria);
        return ResponseEntity.ok().body(circuitoToEsercizioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /circuito-to-esercizios/:id} : get the "id" circuitoToEsercizio.
     *
     * @param id the id of the circuitoToEsercizioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the circuitoToEsercizioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CircuitoToEsercizioDTO> getCircuitoToEsercizio(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CircuitoToEsercizio : {}", id);
        Optional<CircuitoToEsercizioDTO> circuitoToEsercizioDTO = circuitoToEsercizioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(circuitoToEsercizioDTO);
    }

    /**
     * {@code DELETE  /circuito-to-esercizios/:id} : delete the "id" circuitoToEsercizio.
     *
     * @param id the id of the circuitoToEsercizioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCircuitoToEsercizio(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CircuitoToEsercizio : {}", id);
        circuitoToEsercizioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
