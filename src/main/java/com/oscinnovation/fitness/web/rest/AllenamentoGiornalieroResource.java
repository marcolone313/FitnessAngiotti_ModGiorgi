package com.oscinnovation.fitness.web.rest;

import com.oscinnovation.fitness.repository.AllenamentoGiornalieroRepository;
import com.oscinnovation.fitness.service.AllenamentoGiornalieroQueryService;
import com.oscinnovation.fitness.service.AllenamentoGiornalieroService;
import com.oscinnovation.fitness.service.criteria.AllenamentoGiornalieroCriteria;
import com.oscinnovation.fitness.service.dto.AllenamentoGiornalieroDTO;
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
 * REST controller for managing {@link com.oscinnovation.fitness.domain.AllenamentoGiornaliero}.
 */
@RestController
@RequestMapping("/api/allenamento-giornalieros")
public class AllenamentoGiornalieroResource {

    private static final Logger LOG = LoggerFactory.getLogger(AllenamentoGiornalieroResource.class);

    private static final String ENTITY_NAME = "allenamentoGiornaliero";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AllenamentoGiornalieroService allenamentoGiornalieroService;

    private final AllenamentoGiornalieroRepository allenamentoGiornalieroRepository;

    private final AllenamentoGiornalieroQueryService allenamentoGiornalieroQueryService;

    public AllenamentoGiornalieroResource(
        AllenamentoGiornalieroService allenamentoGiornalieroService,
        AllenamentoGiornalieroRepository allenamentoGiornalieroRepository,
        AllenamentoGiornalieroQueryService allenamentoGiornalieroQueryService
    ) {
        this.allenamentoGiornalieroService = allenamentoGiornalieroService;
        this.allenamentoGiornalieroRepository = allenamentoGiornalieroRepository;
        this.allenamentoGiornalieroQueryService = allenamentoGiornalieroQueryService;
    }

    /**
     * {@code POST  /allenamento-giornalieros} : Create a new allenamentoGiornaliero.
     *
     * @param allenamentoGiornalieroDTO the allenamentoGiornalieroDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new allenamentoGiornalieroDTO, or with status {@code 400 (Bad Request)} if the allenamentoGiornaliero has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AllenamentoGiornalieroDTO> createAllenamentoGiornaliero(
        @Valid @RequestBody AllenamentoGiornalieroDTO allenamentoGiornalieroDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AllenamentoGiornaliero : {}", allenamentoGiornalieroDTO);
        if (allenamentoGiornalieroDTO.getId() != null) {
            throw new BadRequestAlertException("A new allenamentoGiornaliero cannot already have an ID", ENTITY_NAME, "idexists");
        }
        allenamentoGiornalieroDTO = allenamentoGiornalieroService.save(allenamentoGiornalieroDTO);
        return ResponseEntity.created(new URI("/api/allenamento-giornalieros/" + allenamentoGiornalieroDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, allenamentoGiornalieroDTO.getId().toString()))
            .body(allenamentoGiornalieroDTO);
    }

    /**
     * {@code PUT  /allenamento-giornalieros/:id} : Updates an existing allenamentoGiornaliero.
     *
     * @param id the id of the allenamentoGiornalieroDTO to save.
     * @param allenamentoGiornalieroDTO the allenamentoGiornalieroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allenamentoGiornalieroDTO,
     * or with status {@code 400 (Bad Request)} if the allenamentoGiornalieroDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the allenamentoGiornalieroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AllenamentoGiornalieroDTO> updateAllenamentoGiornaliero(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AllenamentoGiornalieroDTO allenamentoGiornalieroDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AllenamentoGiornaliero : {}, {}", id, allenamentoGiornalieroDTO);
        if (allenamentoGiornalieroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allenamentoGiornalieroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allenamentoGiornalieroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        allenamentoGiornalieroDTO = allenamentoGiornalieroService.update(allenamentoGiornalieroDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allenamentoGiornalieroDTO.getId().toString()))
            .body(allenamentoGiornalieroDTO);
    }

    /**
     * {@code PATCH  /allenamento-giornalieros/:id} : Partial updates given fields of an existing allenamentoGiornaliero, field will ignore if it is null
     *
     * @param id the id of the allenamentoGiornalieroDTO to save.
     * @param allenamentoGiornalieroDTO the allenamentoGiornalieroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allenamentoGiornalieroDTO,
     * or with status {@code 400 (Bad Request)} if the allenamentoGiornalieroDTO is not valid,
     * or with status {@code 404 (Not Found)} if the allenamentoGiornalieroDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the allenamentoGiornalieroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AllenamentoGiornalieroDTO> partialUpdateAllenamentoGiornaliero(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AllenamentoGiornalieroDTO allenamentoGiornalieroDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AllenamentoGiornaliero partially : {}, {}", id, allenamentoGiornalieroDTO);
        if (allenamentoGiornalieroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allenamentoGiornalieroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allenamentoGiornalieroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AllenamentoGiornalieroDTO> result = allenamentoGiornalieroService.partialUpdate(allenamentoGiornalieroDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allenamentoGiornalieroDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /allenamento-giornalieros} : get all the allenamentoGiornalieros.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of allenamentoGiornalieros in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AllenamentoGiornalieroDTO>> getAllAllenamentoGiornalieros(
        AllenamentoGiornalieroCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AllenamentoGiornalieros by criteria: {}", criteria);

        Page<AllenamentoGiornalieroDTO> page = allenamentoGiornalieroQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /allenamento-giornalieros/count} : count all the allenamentoGiornalieros.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAllenamentoGiornalieros(AllenamentoGiornalieroCriteria criteria) {
        LOG.debug("REST request to count AllenamentoGiornalieros by criteria: {}", criteria);
        return ResponseEntity.ok().body(allenamentoGiornalieroQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /allenamento-giornalieros/:id} : get the "id" allenamentoGiornaliero.
     *
     * @param id the id of the allenamentoGiornalieroDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the allenamentoGiornalieroDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AllenamentoGiornalieroDTO> getAllenamentoGiornaliero(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AllenamentoGiornaliero : {}", id);
        Optional<AllenamentoGiornalieroDTO> allenamentoGiornalieroDTO = allenamentoGiornalieroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(allenamentoGiornalieroDTO);
    }

    /**
     * {@code DELETE  /allenamento-giornalieros/:id} : delete the "id" allenamentoGiornaliero.
     *
     * @param id the id of the allenamentoGiornalieroDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllenamentoGiornaliero(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AllenamentoGiornaliero : {}", id);
        allenamentoGiornalieroService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
