package com.oscinnovation.fitness.web.rest;

import com.oscinnovation.fitness.repository.CircuitoRepository;
import com.oscinnovation.fitness.service.CircuitoQueryService;
import com.oscinnovation.fitness.service.CircuitoService;
import com.oscinnovation.fitness.service.criteria.CircuitoCriteria;
import com.oscinnovation.fitness.service.dto.CircuitoDTO;
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
 * REST controller for managing {@link com.oscinnovation.fitness.domain.Circuito}.
 */
@RestController
@RequestMapping("/api/circuitos")
public class CircuitoResource {

    private static final Logger LOG = LoggerFactory.getLogger(CircuitoResource.class);

    private static final String ENTITY_NAME = "circuito";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CircuitoService circuitoService;

    private final CircuitoRepository circuitoRepository;

    private final CircuitoQueryService circuitoQueryService;

    public CircuitoResource(
        CircuitoService circuitoService,
        CircuitoRepository circuitoRepository,
        CircuitoQueryService circuitoQueryService
    ) {
        this.circuitoService = circuitoService;
        this.circuitoRepository = circuitoRepository;
        this.circuitoQueryService = circuitoQueryService;
    }

    /**
     * {@code POST  /circuitos} : Create a new circuito.
     *
     * @param circuitoDTO the circuitoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new circuitoDTO, or with status {@code 400 (Bad Request)} if the circuito has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CircuitoDTO> createCircuito(@Valid @RequestBody CircuitoDTO circuitoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Circuito : {}", circuitoDTO);
        if (circuitoDTO.getId() != null) {
            throw new BadRequestAlertException("A new circuito cannot already have an ID", ENTITY_NAME, "idexists");
        }
        circuitoDTO = circuitoService.save(circuitoDTO);
        return ResponseEntity.created(new URI("/api/circuitos/" + circuitoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, circuitoDTO.getId().toString()))
            .body(circuitoDTO);
    }

    /**
     * {@code PUT  /circuitos/:id} : Updates an existing circuito.
     *
     * @param id the id of the circuitoDTO to save.
     * @param circuitoDTO the circuitoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated circuitoDTO,
     * or with status {@code 400 (Bad Request)} if the circuitoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the circuitoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CircuitoDTO> updateCircuito(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CircuitoDTO circuitoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Circuito : {}, {}", id, circuitoDTO);
        if (circuitoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, circuitoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!circuitoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        circuitoDTO = circuitoService.update(circuitoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, circuitoDTO.getId().toString()))
            .body(circuitoDTO);
    }

    /**
     * {@code PATCH  /circuitos/:id} : Partial updates given fields of an existing circuito, field will ignore if it is null
     *
     * @param id the id of the circuitoDTO to save.
     * @param circuitoDTO the circuitoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated circuitoDTO,
     * or with status {@code 400 (Bad Request)} if the circuitoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the circuitoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the circuitoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CircuitoDTO> partialUpdateCircuito(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CircuitoDTO circuitoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Circuito partially : {}, {}", id, circuitoDTO);
        if (circuitoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, circuitoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!circuitoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CircuitoDTO> result = circuitoService.partialUpdate(circuitoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, circuitoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /circuitos} : get all the circuitos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of circuitos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CircuitoDTO>> getAllCircuitos(
        CircuitoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Circuitos by criteria: {}", criteria);

        Page<CircuitoDTO> page = circuitoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /circuitos/count} : count all the circuitos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCircuitos(CircuitoCriteria criteria) {
        LOG.debug("REST request to count Circuitos by criteria: {}", criteria);
        return ResponseEntity.ok().body(circuitoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /circuitos/:id} : get the "id" circuito.
     *
     * @param id the id of the circuitoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the circuitoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CircuitoDTO> getCircuito(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Circuito : {}", id);
        Optional<CircuitoDTO> circuitoDTO = circuitoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(circuitoDTO);
    }

    /**
     * {@code DELETE  /circuitos/:id} : delete the "id" circuito.
     *
     * @param id the id of the circuitoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCircuito(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Circuito : {}", id);
        circuitoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
