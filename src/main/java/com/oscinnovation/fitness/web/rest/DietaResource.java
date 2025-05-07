package com.oscinnovation.fitness.web.rest;

import com.oscinnovation.fitness.repository.DietaRepository;
import com.oscinnovation.fitness.service.DietaQueryService;
import com.oscinnovation.fitness.service.DietaService;
import com.oscinnovation.fitness.service.criteria.DietaCriteria;
import com.oscinnovation.fitness.service.dto.DietaDTO;
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
 * REST controller for managing {@link com.oscinnovation.fitness.domain.Dieta}.
 */
@RestController
@RequestMapping("/api/dietas")
public class DietaResource {

    private static final Logger LOG = LoggerFactory.getLogger(DietaResource.class);

    private static final String ENTITY_NAME = "dieta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DietaService dietaService;

    private final DietaRepository dietaRepository;

    private final DietaQueryService dietaQueryService;

    public DietaResource(DietaService dietaService, DietaRepository dietaRepository, DietaQueryService dietaQueryService) {
        this.dietaService = dietaService;
        this.dietaRepository = dietaRepository;
        this.dietaQueryService = dietaQueryService;
    }

    /**
     * {@code POST  /dietas} : Create a new dieta.
     *
     * @param dietaDTO the dietaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dietaDTO, or with status {@code 400 (Bad Request)} if the dieta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DietaDTO> createDieta(@Valid @RequestBody DietaDTO dietaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Dieta : {}", dietaDTO);
        if (dietaDTO.getId() != null) {
            throw new BadRequestAlertException("A new dieta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dietaDTO = dietaService.save(dietaDTO);
        return ResponseEntity.created(new URI("/api/dietas/" + dietaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, dietaDTO.getId().toString()))
            .body(dietaDTO);
    }

    /**
     * {@code PUT  /dietas/:id} : Updates an existing dieta.
     *
     * @param id the id of the dietaDTO to save.
     * @param dietaDTO the dietaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dietaDTO,
     * or with status {@code 400 (Bad Request)} if the dietaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dietaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DietaDTO> updateDieta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DietaDTO dietaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Dieta : {}, {}", id, dietaDTO);
        if (dietaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dietaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dietaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dietaDTO = dietaService.update(dietaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dietaDTO.getId().toString()))
            .body(dietaDTO);
    }

    /**
     * {@code PATCH  /dietas/:id} : Partial updates given fields of an existing dieta, field will ignore if it is null
     *
     * @param id the id of the dietaDTO to save.
     * @param dietaDTO the dietaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dietaDTO,
     * or with status {@code 400 (Bad Request)} if the dietaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dietaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dietaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DietaDTO> partialUpdateDieta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DietaDTO dietaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Dieta partially : {}, {}", id, dietaDTO);
        if (dietaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dietaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dietaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DietaDTO> result = dietaService.partialUpdate(dietaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dietaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dietas} : get all the dietas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dietas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DietaDTO>> getAllDietas(
        DietaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Dietas by criteria: {}", criteria);

        Page<DietaDTO> page = dietaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dietas/count} : count all the dietas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDietas(DietaCriteria criteria) {
        LOG.debug("REST request to count Dietas by criteria: {}", criteria);
        return ResponseEntity.ok().body(dietaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dietas/:id} : get the "id" dieta.
     *
     * @param id the id of the dietaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dietaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DietaDTO> getDieta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Dieta : {}", id);
        Optional<DietaDTO> dietaDTO = dietaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dietaDTO);
    }

    /**
     * {@code DELETE  /dietas/:id} : delete the "id" dieta.
     *
     * @param id the id of the dietaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDieta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Dieta : {}", id);
        dietaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
