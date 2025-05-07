package com.oscinnovation.fitness.web.rest;

import com.oscinnovation.fitness.repository.CorsaRepository;
import com.oscinnovation.fitness.service.CorsaQueryService;
import com.oscinnovation.fitness.service.CorsaService;
import com.oscinnovation.fitness.service.criteria.CorsaCriteria;
import com.oscinnovation.fitness.service.dto.CorsaDTO;
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
 * REST controller for managing {@link com.oscinnovation.fitness.domain.Corsa}.
 */
@RestController
@RequestMapping("/api/corsas")
public class CorsaResource {

    private static final Logger LOG = LoggerFactory.getLogger(CorsaResource.class);

    private static final String ENTITY_NAME = "corsa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CorsaService corsaService;

    private final CorsaRepository corsaRepository;

    private final CorsaQueryService corsaQueryService;

    public CorsaResource(CorsaService corsaService, CorsaRepository corsaRepository, CorsaQueryService corsaQueryService) {
        this.corsaService = corsaService;
        this.corsaRepository = corsaRepository;
        this.corsaQueryService = corsaQueryService;
    }

    /**
     * {@code POST  /corsas} : Create a new corsa.
     *
     * @param corsaDTO the corsaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new corsaDTO, or with status {@code 400 (Bad Request)} if the corsa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CorsaDTO> createCorsa(@Valid @RequestBody CorsaDTO corsaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Corsa : {}", corsaDTO);
        if (corsaDTO.getId() != null) {
            throw new BadRequestAlertException("A new corsa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        corsaDTO = corsaService.save(corsaDTO);
        return ResponseEntity.created(new URI("/api/corsas/" + corsaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, corsaDTO.getId().toString()))
            .body(corsaDTO);
    }

    /**
     * {@code PUT  /corsas/:id} : Updates an existing corsa.
     *
     * @param id the id of the corsaDTO to save.
     * @param corsaDTO the corsaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated corsaDTO,
     * or with status {@code 400 (Bad Request)} if the corsaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the corsaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CorsaDTO> updateCorsa(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CorsaDTO corsaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Corsa : {}, {}", id, corsaDTO);
        if (corsaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, corsaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!corsaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        corsaDTO = corsaService.update(corsaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, corsaDTO.getId().toString()))
            .body(corsaDTO);
    }

    /**
     * {@code PATCH  /corsas/:id} : Partial updates given fields of an existing corsa, field will ignore if it is null
     *
     * @param id the id of the corsaDTO to save.
     * @param corsaDTO the corsaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated corsaDTO,
     * or with status {@code 400 (Bad Request)} if the corsaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the corsaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the corsaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CorsaDTO> partialUpdateCorsa(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CorsaDTO corsaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Corsa partially : {}, {}", id, corsaDTO);
        if (corsaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, corsaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!corsaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CorsaDTO> result = corsaService.partialUpdate(corsaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, corsaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /corsas} : get all the corsas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of corsas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CorsaDTO>> getAllCorsas(
        CorsaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Corsas by criteria: {}", criteria);

        Page<CorsaDTO> page = corsaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /corsas/count} : count all the corsas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCorsas(CorsaCriteria criteria) {
        LOG.debug("REST request to count Corsas by criteria: {}", criteria);
        return ResponseEntity.ok().body(corsaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /corsas/:id} : get the "id" corsa.
     *
     * @param id the id of the corsaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the corsaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CorsaDTO> getCorsa(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Corsa : {}", id);
        Optional<CorsaDTO> corsaDTO = corsaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(corsaDTO);
    }

    /**
     * {@code DELETE  /corsas/:id} : delete the "id" corsa.
     *
     * @param id the id of the corsaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCorsa(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Corsa : {}", id);
        corsaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
