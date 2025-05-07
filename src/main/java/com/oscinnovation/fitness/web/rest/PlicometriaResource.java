package com.oscinnovation.fitness.web.rest;

import com.oscinnovation.fitness.repository.PlicometriaRepository;
import com.oscinnovation.fitness.service.PlicometriaQueryService;
import com.oscinnovation.fitness.service.PlicometriaService;
import com.oscinnovation.fitness.service.criteria.PlicometriaCriteria;
import com.oscinnovation.fitness.service.dto.PlicometriaDTO;
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
 * REST controller for managing {@link com.oscinnovation.fitness.domain.Plicometria}.
 */
@RestController
@RequestMapping("/api/plicometrias")
public class PlicometriaResource {

    private static final Logger LOG = LoggerFactory.getLogger(PlicometriaResource.class);

    private static final String ENTITY_NAME = "plicometria";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlicometriaService plicometriaService;

    private final PlicometriaRepository plicometriaRepository;

    private final PlicometriaQueryService plicometriaQueryService;

    public PlicometriaResource(
        PlicometriaService plicometriaService,
        PlicometriaRepository plicometriaRepository,
        PlicometriaQueryService plicometriaQueryService
    ) {
        this.plicometriaService = plicometriaService;
        this.plicometriaRepository = plicometriaRepository;
        this.plicometriaQueryService = plicometriaQueryService;
    }

    /**
     * {@code POST  /plicometrias} : Create a new plicometria.
     *
     * @param plicometriaDTO the plicometriaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plicometriaDTO, or with status {@code 400 (Bad Request)} if the plicometria has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PlicometriaDTO> createPlicometria(@Valid @RequestBody PlicometriaDTO plicometriaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Plicometria : {}", plicometriaDTO);
        if (plicometriaDTO.getId() != null) {
            throw new BadRequestAlertException("A new plicometria cannot already have an ID", ENTITY_NAME, "idexists");
        }
        plicometriaDTO = plicometriaService.save(plicometriaDTO);
        return ResponseEntity.created(new URI("/api/plicometrias/" + plicometriaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, plicometriaDTO.getId().toString()))
            .body(plicometriaDTO);
    }

    /**
     * {@code PUT  /plicometrias/:id} : Updates an existing plicometria.
     *
     * @param id the id of the plicometriaDTO to save.
     * @param plicometriaDTO the plicometriaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plicometriaDTO,
     * or with status {@code 400 (Bad Request)} if the plicometriaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plicometriaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlicometriaDTO> updatePlicometria(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlicometriaDTO plicometriaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Plicometria : {}, {}", id, plicometriaDTO);
        if (plicometriaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plicometriaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!plicometriaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        plicometriaDTO = plicometriaService.update(plicometriaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plicometriaDTO.getId().toString()))
            .body(plicometriaDTO);
    }

    /**
     * {@code PATCH  /plicometrias/:id} : Partial updates given fields of an existing plicometria, field will ignore if it is null
     *
     * @param id the id of the plicometriaDTO to save.
     * @param plicometriaDTO the plicometriaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plicometriaDTO,
     * or with status {@code 400 (Bad Request)} if the plicometriaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the plicometriaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the plicometriaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlicometriaDTO> partialUpdatePlicometria(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlicometriaDTO plicometriaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Plicometria partially : {}, {}", id, plicometriaDTO);
        if (plicometriaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plicometriaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!plicometriaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlicometriaDTO> result = plicometriaService.partialUpdate(plicometriaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plicometriaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /plicometrias} : get all the plicometrias.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plicometrias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PlicometriaDTO>> getAllPlicometrias(
        PlicometriaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Plicometrias by criteria: {}", criteria);

        Page<PlicometriaDTO> page = plicometriaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plicometrias/count} : count all the plicometrias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPlicometrias(PlicometriaCriteria criteria) {
        LOG.debug("REST request to count Plicometrias by criteria: {}", criteria);
        return ResponseEntity.ok().body(plicometriaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /plicometrias/:id} : get the "id" plicometria.
     *
     * @param id the id of the plicometriaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plicometriaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlicometriaDTO> getPlicometria(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Plicometria : {}", id);
        Optional<PlicometriaDTO> plicometriaDTO = plicometriaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(plicometriaDTO);
    }

    /**
     * {@code DELETE  /plicometrias/:id} : delete the "id" plicometria.
     *
     * @param id the id of the plicometriaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlicometria(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Plicometria : {}", id);
        plicometriaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
