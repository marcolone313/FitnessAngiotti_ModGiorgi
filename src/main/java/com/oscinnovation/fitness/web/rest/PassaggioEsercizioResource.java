package com.oscinnovation.fitness.web.rest;

import com.oscinnovation.fitness.repository.PassaggioEsercizioRepository;
import com.oscinnovation.fitness.service.PassaggioEsercizioQueryService;
import com.oscinnovation.fitness.service.PassaggioEsercizioService;
import com.oscinnovation.fitness.service.criteria.PassaggioEsercizioCriteria;
import com.oscinnovation.fitness.service.dto.PassaggioEsercizioDTO;
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
 * REST controller for managing {@link com.oscinnovation.fitness.domain.PassaggioEsercizio}.
 */
@RestController
@RequestMapping("/api/passaggio-esercizios")
public class PassaggioEsercizioResource {

    private static final Logger LOG = LoggerFactory.getLogger(PassaggioEsercizioResource.class);

    private static final String ENTITY_NAME = "passaggioEsercizio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PassaggioEsercizioService passaggioEsercizioService;

    private final PassaggioEsercizioRepository passaggioEsercizioRepository;

    private final PassaggioEsercizioQueryService passaggioEsercizioQueryService;

    public PassaggioEsercizioResource(
        PassaggioEsercizioService passaggioEsercizioService,
        PassaggioEsercizioRepository passaggioEsercizioRepository,
        PassaggioEsercizioQueryService passaggioEsercizioQueryService
    ) {
        this.passaggioEsercizioService = passaggioEsercizioService;
        this.passaggioEsercizioRepository = passaggioEsercizioRepository;
        this.passaggioEsercizioQueryService = passaggioEsercizioQueryService;
    }

    /**
     * {@code POST  /passaggio-esercizios} : Create a new passaggioEsercizio.
     *
     * @param passaggioEsercizioDTO the passaggioEsercizioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new passaggioEsercizioDTO, or with status {@code 400 (Bad Request)} if the passaggioEsercizio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PassaggioEsercizioDTO> createPassaggioEsercizio(@Valid @RequestBody PassaggioEsercizioDTO passaggioEsercizioDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PassaggioEsercizio : {}", passaggioEsercizioDTO);
        if (passaggioEsercizioDTO.getId() != null) {
            throw new BadRequestAlertException("A new passaggioEsercizio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        passaggioEsercizioDTO = passaggioEsercizioService.save(passaggioEsercizioDTO);
        return ResponseEntity.created(new URI("/api/passaggio-esercizios/" + passaggioEsercizioDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, passaggioEsercizioDTO.getId().toString()))
            .body(passaggioEsercizioDTO);
    }

    /**
     * {@code PUT  /passaggio-esercizios/:id} : Updates an existing passaggioEsercizio.
     *
     * @param id the id of the passaggioEsercizioDTO to save.
     * @param passaggioEsercizioDTO the passaggioEsercizioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passaggioEsercizioDTO,
     * or with status {@code 400 (Bad Request)} if the passaggioEsercizioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the passaggioEsercizioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PassaggioEsercizioDTO> updatePassaggioEsercizio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PassaggioEsercizioDTO passaggioEsercizioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PassaggioEsercizio : {}, {}", id, passaggioEsercizioDTO);
        if (passaggioEsercizioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, passaggioEsercizioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!passaggioEsercizioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        passaggioEsercizioDTO = passaggioEsercizioService.update(passaggioEsercizioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, passaggioEsercizioDTO.getId().toString()))
            .body(passaggioEsercizioDTO);
    }

    /**
     * {@code PATCH  /passaggio-esercizios/:id} : Partial updates given fields of an existing passaggioEsercizio, field will ignore if it is null
     *
     * @param id the id of the passaggioEsercizioDTO to save.
     * @param passaggioEsercizioDTO the passaggioEsercizioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passaggioEsercizioDTO,
     * or with status {@code 400 (Bad Request)} if the passaggioEsercizioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the passaggioEsercizioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the passaggioEsercizioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PassaggioEsercizioDTO> partialUpdatePassaggioEsercizio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PassaggioEsercizioDTO passaggioEsercizioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PassaggioEsercizio partially : {}, {}", id, passaggioEsercizioDTO);
        if (passaggioEsercizioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, passaggioEsercizioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!passaggioEsercizioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PassaggioEsercizioDTO> result = passaggioEsercizioService.partialUpdate(passaggioEsercizioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, passaggioEsercizioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /passaggio-esercizios} : get all the passaggioEsercizios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of passaggioEsercizios in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PassaggioEsercizioDTO>> getAllPassaggioEsercizios(
        PassaggioEsercizioCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PassaggioEsercizios by criteria: {}", criteria);

        Page<PassaggioEsercizioDTO> page = passaggioEsercizioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /passaggio-esercizios/count} : count all the passaggioEsercizios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPassaggioEsercizios(PassaggioEsercizioCriteria criteria) {
        LOG.debug("REST request to count PassaggioEsercizios by criteria: {}", criteria);
        return ResponseEntity.ok().body(passaggioEsercizioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /passaggio-esercizios/:id} : get the "id" passaggioEsercizio.
     *
     * @param id the id of the passaggioEsercizioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the passaggioEsercizioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PassaggioEsercizioDTO> getPassaggioEsercizio(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PassaggioEsercizio : {}", id);
        Optional<PassaggioEsercizioDTO> passaggioEsercizioDTO = passaggioEsercizioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(passaggioEsercizioDTO);
    }

    /**
     * {@code DELETE  /passaggio-esercizios/:id} : delete the "id" passaggioEsercizio.
     *
     * @param id the id of the passaggioEsercizioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassaggioEsercizio(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PassaggioEsercizio : {}", id);
        passaggioEsercizioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
