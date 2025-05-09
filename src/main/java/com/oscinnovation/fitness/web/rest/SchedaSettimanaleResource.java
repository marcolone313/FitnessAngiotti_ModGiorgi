package com.oscinnovation.fitness.web.rest;

import com.oscinnovation.fitness.repository.SchedaSettimanaleRepository;
import com.oscinnovation.fitness.service.SchedaSettimanaleQueryService;
import com.oscinnovation.fitness.service.SchedaSettimanaleService;
import com.oscinnovation.fitness.service.criteria.SchedaSettimanaleCriteria;
import com.oscinnovation.fitness.service.dto.SchedaSettimanaleDTO;
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
 * REST controller for managing {@link com.oscinnovation.fitness.domain.SchedaSettimanale}.
 */
@RestController
@RequestMapping("/api/scheda-settimanales")
public class SchedaSettimanaleResource {

    private static final Logger LOG = LoggerFactory.getLogger(SchedaSettimanaleResource.class);

    private static final String ENTITY_NAME = "schedaSettimanale";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchedaSettimanaleService schedaSettimanaleService;

    private final SchedaSettimanaleRepository schedaSettimanaleRepository;

    private final SchedaSettimanaleQueryService schedaSettimanaleQueryService;

    public SchedaSettimanaleResource(
        SchedaSettimanaleService schedaSettimanaleService,
        SchedaSettimanaleRepository schedaSettimanaleRepository,
        SchedaSettimanaleQueryService schedaSettimanaleQueryService
    ) {
        this.schedaSettimanaleService = schedaSettimanaleService;
        this.schedaSettimanaleRepository = schedaSettimanaleRepository;
        this.schedaSettimanaleQueryService = schedaSettimanaleQueryService;
    }

    /**
     * {@code POST  /scheda-settimanales} : Create a new schedaSettimanale.
     *
     * @param schedaSettimanaleDTO the schedaSettimanaleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schedaSettimanaleDTO, or with status {@code 400 (Bad Request)} if the schedaSettimanale has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SchedaSettimanaleDTO> createSchedaSettimanale(@Valid @RequestBody SchedaSettimanaleDTO schedaSettimanaleDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SchedaSettimanale : {}", schedaSettimanaleDTO);
        if (schedaSettimanaleDTO.getId() != null) {
            throw new BadRequestAlertException("A new schedaSettimanale cannot already have an ID", ENTITY_NAME, "idexists");
        }
        schedaSettimanaleDTO = schedaSettimanaleService.save(schedaSettimanaleDTO);
        return ResponseEntity.created(new URI("/api/scheda-settimanales/" + schedaSettimanaleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, schedaSettimanaleDTO.getId().toString()))
            .body(schedaSettimanaleDTO);
    }

    /**
     * {@code PUT  /scheda-settimanales/:id} : Updates an existing schedaSettimanale.
     *
     * @param id the id of the schedaSettimanaleDTO to save.
     * @param schedaSettimanaleDTO the schedaSettimanaleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schedaSettimanaleDTO,
     * or with status {@code 400 (Bad Request)} if the schedaSettimanaleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schedaSettimanaleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SchedaSettimanaleDTO> updateSchedaSettimanale(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SchedaSettimanaleDTO schedaSettimanaleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SchedaSettimanale : {}, {}", id, schedaSettimanaleDTO);
        if (schedaSettimanaleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schedaSettimanaleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schedaSettimanaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        schedaSettimanaleDTO = schedaSettimanaleService.update(schedaSettimanaleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schedaSettimanaleDTO.getId().toString()))
            .body(schedaSettimanaleDTO);
    }

    /**
     * {@code PATCH  /scheda-settimanales/:id} : Partial updates given fields of an existing schedaSettimanale, field will ignore if it is null
     *
     * @param id the id of the schedaSettimanaleDTO to save.
     * @param schedaSettimanaleDTO the schedaSettimanaleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schedaSettimanaleDTO,
     * or with status {@code 400 (Bad Request)} if the schedaSettimanaleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the schedaSettimanaleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the schedaSettimanaleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SchedaSettimanaleDTO> partialUpdateSchedaSettimanale(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SchedaSettimanaleDTO schedaSettimanaleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SchedaSettimanale partially : {}, {}", id, schedaSettimanaleDTO);
        if (schedaSettimanaleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schedaSettimanaleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schedaSettimanaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SchedaSettimanaleDTO> result = schedaSettimanaleService.partialUpdate(schedaSettimanaleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schedaSettimanaleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /scheda-settimanales} : get all the schedaSettimanales.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schedaSettimanales in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SchedaSettimanaleDTO>> getAllSchedaSettimanales(
        SchedaSettimanaleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SchedaSettimanales by criteria: {}", criteria);

        Page<SchedaSettimanaleDTO> page = schedaSettimanaleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /scheda-settimanales/count} : count all the schedaSettimanales.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSchedaSettimanales(SchedaSettimanaleCriteria criteria) {
        LOG.debug("REST request to count SchedaSettimanales by criteria: {}", criteria);
        return ResponseEntity.ok().body(schedaSettimanaleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /scheda-settimanales/:id} : get the "id" schedaSettimanale.
     *
     * @param id the id of the schedaSettimanaleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schedaSettimanaleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SchedaSettimanaleDTO> getSchedaSettimanale(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SchedaSettimanale : {}", id);
        Optional<SchedaSettimanaleDTO> schedaSettimanaleDTO = schedaSettimanaleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(schedaSettimanaleDTO);
    }

    /**
     * {@code DELETE  /scheda-settimanales/:id} : delete the "id" schedaSettimanale.
     *
     * @param id the id of the schedaSettimanaleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedaSettimanale(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SchedaSettimanale : {}", id);
        schedaSettimanaleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/{id}/export")
    public ResponseEntity<SchedaSettimanaleDTO> exportSchedaSettimanale(@PathVariable Long id) {
        Optional<SchedaSettimanaleDTO> optionalScheda = schedaSettimanaleService.findOne(id);

        if (optionalScheda.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        SchedaSettimanaleDTO scheda = optionalScheda.get();

        // Escludere il cliente dalla risposta
        scheda.setCliente(null);

        return ResponseEntity.ok(scheda);
    }

    @PostMapping("/import")
    public ResponseEntity<SchedaSettimanaleDTO> importSchedaSettimanale(@Valid @RequestBody SchedaSettimanaleDTO schedaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to import SchedaSettimanale : {}", schedaDTO);

        SchedaSettimanaleDTO result = schedaSettimanaleService.save(schedaDTO);
        return ResponseEntity.created(new URI("/api/scheda-settimanales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
