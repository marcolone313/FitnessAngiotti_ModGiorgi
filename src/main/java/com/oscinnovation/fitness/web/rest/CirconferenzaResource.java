package com.oscinnovation.fitness.web.rest;

import com.oscinnovation.fitness.repository.CirconferenzaRepository;
import com.oscinnovation.fitness.service.CirconferenzaQueryService;
import com.oscinnovation.fitness.service.CirconferenzaService;
import com.oscinnovation.fitness.service.criteria.CirconferenzaCriteria;
import com.oscinnovation.fitness.service.dto.CirconferenzaDTO;
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
 * REST controller for managing {@link com.oscinnovation.fitness.domain.Circonferenza}.
 */
@RestController
@RequestMapping("/api/circonferenzas")
public class CirconferenzaResource {

    private static final Logger LOG = LoggerFactory.getLogger(CirconferenzaResource.class);

    private static final String ENTITY_NAME = "circonferenza";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CirconferenzaService circonferenzaService;

    private final CirconferenzaRepository circonferenzaRepository;

    private final CirconferenzaQueryService circonferenzaQueryService;

    public CirconferenzaResource(
        CirconferenzaService circonferenzaService,
        CirconferenzaRepository circonferenzaRepository,
        CirconferenzaQueryService circonferenzaQueryService
    ) {
        this.circonferenzaService = circonferenzaService;
        this.circonferenzaRepository = circonferenzaRepository;
        this.circonferenzaQueryService = circonferenzaQueryService;
    }

    /**
     * {@code POST  /circonferenzas} : Create a new circonferenza.
     *
     * @param circonferenzaDTO the circonferenzaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new circonferenzaDTO, or with status {@code 400 (Bad Request)} if the circonferenza has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CirconferenzaDTO> createCirconferenza(@Valid @RequestBody CirconferenzaDTO circonferenzaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Circonferenza : {}", circonferenzaDTO);
        if (circonferenzaDTO.getId() != null) {
            throw new BadRequestAlertException("A new circonferenza cannot already have an ID", ENTITY_NAME, "idexists");
        }
        circonferenzaDTO = circonferenzaService.save(circonferenzaDTO);
        return ResponseEntity.created(new URI("/api/circonferenzas/" + circonferenzaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, circonferenzaDTO.getId().toString()))
            .body(circonferenzaDTO);
    }

    /**
     * {@code PUT  /circonferenzas/:id} : Updates an existing circonferenza.
     *
     * @param id the id of the circonferenzaDTO to save.
     * @param circonferenzaDTO the circonferenzaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated circonferenzaDTO,
     * or with status {@code 400 (Bad Request)} if the circonferenzaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the circonferenzaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CirconferenzaDTO> updateCirconferenza(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CirconferenzaDTO circonferenzaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Circonferenza : {}, {}", id, circonferenzaDTO);
        if (circonferenzaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, circonferenzaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!circonferenzaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        circonferenzaDTO = circonferenzaService.update(circonferenzaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, circonferenzaDTO.getId().toString()))
            .body(circonferenzaDTO);
    }

    /**
     * {@code PATCH  /circonferenzas/:id} : Partial updates given fields of an existing circonferenza, field will ignore if it is null
     *
     * @param id the id of the circonferenzaDTO to save.
     * @param circonferenzaDTO the circonferenzaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated circonferenzaDTO,
     * or with status {@code 400 (Bad Request)} if the circonferenzaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the circonferenzaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the circonferenzaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CirconferenzaDTO> partialUpdateCirconferenza(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CirconferenzaDTO circonferenzaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Circonferenza partially : {}, {}", id, circonferenzaDTO);
        if (circonferenzaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, circonferenzaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!circonferenzaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CirconferenzaDTO> result = circonferenzaService.partialUpdate(circonferenzaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, circonferenzaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /circonferenzas} : get all the circonferenzas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of circonferenzas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CirconferenzaDTO>> getAllCirconferenzas(
        CirconferenzaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Circonferenzas by criteria: {}", criteria);

        Page<CirconferenzaDTO> page = circonferenzaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /circonferenzas/count} : count all the circonferenzas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCirconferenzas(CirconferenzaCriteria criteria) {
        LOG.debug("REST request to count Circonferenzas by criteria: {}", criteria);
        return ResponseEntity.ok().body(circonferenzaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /circonferenzas/:id} : get the "id" circonferenza.
     *
     * @param id the id of the circonferenzaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the circonferenzaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CirconferenzaDTO> getCirconferenza(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Circonferenza : {}", id);
        Optional<CirconferenzaDTO> circonferenzaDTO = circonferenzaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(circonferenzaDTO);
    }

    /**
     * {@code DELETE  /circonferenzas/:id} : delete the "id" circonferenza.
     *
     * @param id the id of the circonferenzaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCirconferenza(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Circonferenza : {}", id);
        circonferenzaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
