package com.oscinnovation.fitness.web.rest;

import com.oscinnovation.fitness.repository.CorsoAcademyRepository;
import com.oscinnovation.fitness.service.CorsoAcademyQueryService;
import com.oscinnovation.fitness.service.CorsoAcademyService;
import com.oscinnovation.fitness.service.criteria.CorsoAcademyCriteria;
import com.oscinnovation.fitness.service.dto.CorsoAcademyDTO;
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
 * REST controller for managing {@link com.oscinnovation.fitness.domain.CorsoAcademy}.
 */
@RestController
@RequestMapping("/api/corso-academies")
public class CorsoAcademyResource {

    private static final Logger LOG = LoggerFactory.getLogger(CorsoAcademyResource.class);

    private static final String ENTITY_NAME = "corsoAcademy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CorsoAcademyService corsoAcademyService;

    private final CorsoAcademyRepository corsoAcademyRepository;

    private final CorsoAcademyQueryService corsoAcademyQueryService;

    public CorsoAcademyResource(
        CorsoAcademyService corsoAcademyService,
        CorsoAcademyRepository corsoAcademyRepository,
        CorsoAcademyQueryService corsoAcademyQueryService
    ) {
        this.corsoAcademyService = corsoAcademyService;
        this.corsoAcademyRepository = corsoAcademyRepository;
        this.corsoAcademyQueryService = corsoAcademyQueryService;
    }

    /**
     * {@code POST  /corso-academies} : Create a new corsoAcademy.
     *
     * @param corsoAcademyDTO the corsoAcademyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new corsoAcademyDTO, or with status {@code 400 (Bad Request)} if the corsoAcademy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CorsoAcademyDTO> createCorsoAcademy(@Valid @RequestBody CorsoAcademyDTO corsoAcademyDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CorsoAcademy : {}", corsoAcademyDTO);
        if (corsoAcademyDTO.getId() != null) {
            throw new BadRequestAlertException("A new corsoAcademy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        corsoAcademyDTO = corsoAcademyService.save(corsoAcademyDTO);
        return ResponseEntity.created(new URI("/api/corso-academies/" + corsoAcademyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, corsoAcademyDTO.getId().toString()))
            .body(corsoAcademyDTO);
    }

    /**
     * {@code PUT  /corso-academies/:id} : Updates an existing corsoAcademy.
     *
     * @param id the id of the corsoAcademyDTO to save.
     * @param corsoAcademyDTO the corsoAcademyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated corsoAcademyDTO,
     * or with status {@code 400 (Bad Request)} if the corsoAcademyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the corsoAcademyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CorsoAcademyDTO> updateCorsoAcademy(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CorsoAcademyDTO corsoAcademyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CorsoAcademy : {}, {}", id, corsoAcademyDTO);
        if (corsoAcademyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, corsoAcademyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!corsoAcademyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        corsoAcademyDTO = corsoAcademyService.update(corsoAcademyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, corsoAcademyDTO.getId().toString()))
            .body(corsoAcademyDTO);
    }

    /**
     * {@code PATCH  /corso-academies/:id} : Partial updates given fields of an existing corsoAcademy, field will ignore if it is null
     *
     * @param id the id of the corsoAcademyDTO to save.
     * @param corsoAcademyDTO the corsoAcademyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated corsoAcademyDTO,
     * or with status {@code 400 (Bad Request)} if the corsoAcademyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the corsoAcademyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the corsoAcademyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CorsoAcademyDTO> partialUpdateCorsoAcademy(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CorsoAcademyDTO corsoAcademyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CorsoAcademy partially : {}, {}", id, corsoAcademyDTO);
        if (corsoAcademyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, corsoAcademyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!corsoAcademyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CorsoAcademyDTO> result = corsoAcademyService.partialUpdate(corsoAcademyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, corsoAcademyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /corso-academies} : get all the corsoAcademies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of corsoAcademies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CorsoAcademyDTO>> getAllCorsoAcademies(
        CorsoAcademyCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CorsoAcademies by criteria: {}", criteria);

        Page<CorsoAcademyDTO> page = corsoAcademyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /corso-academies/count} : count all the corsoAcademies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCorsoAcademies(CorsoAcademyCriteria criteria) {
        LOG.debug("REST request to count CorsoAcademies by criteria: {}", criteria);
        return ResponseEntity.ok().body(corsoAcademyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /corso-academies/:id} : get the "id" corsoAcademy.
     *
     * @param id the id of the corsoAcademyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the corsoAcademyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CorsoAcademyDTO> getCorsoAcademy(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CorsoAcademy : {}", id);
        Optional<CorsoAcademyDTO> corsoAcademyDTO = corsoAcademyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(corsoAcademyDTO);
    }

    /**
     * {@code DELETE  /corso-academies/:id} : delete the "id" corsoAcademy.
     *
     * @param id the id of the corsoAcademyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCorsoAcademy(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CorsoAcademy : {}", id);
        corsoAcademyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
