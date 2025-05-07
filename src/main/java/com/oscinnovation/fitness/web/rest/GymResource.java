package com.oscinnovation.fitness.web.rest;

import com.oscinnovation.fitness.repository.GymRepository;
import com.oscinnovation.fitness.service.GymQueryService;
import com.oscinnovation.fitness.service.GymService;
import com.oscinnovation.fitness.service.criteria.GymCriteria;
import com.oscinnovation.fitness.service.dto.GymDTO;
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
 * REST controller for managing {@link com.oscinnovation.fitness.domain.Gym}.
 */
@RestController
@RequestMapping("/api/gyms")
public class GymResource {

    private static final Logger LOG = LoggerFactory.getLogger(GymResource.class);

    private static final String ENTITY_NAME = "gym";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GymService gymService;

    private final GymRepository gymRepository;

    private final GymQueryService gymQueryService;

    public GymResource(GymService gymService, GymRepository gymRepository, GymQueryService gymQueryService) {
        this.gymService = gymService;
        this.gymRepository = gymRepository;
        this.gymQueryService = gymQueryService;
    }

    /**
     * {@code POST  /gyms} : Create a new gym.
     *
     * @param gymDTO the gymDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gymDTO, or with status {@code 400 (Bad Request)} if the gym has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GymDTO> createGym(@Valid @RequestBody GymDTO gymDTO) throws URISyntaxException {
        LOG.debug("REST request to save Gym : {}", gymDTO);
        if (gymDTO.getId() != null) {
            throw new BadRequestAlertException("A new gym cannot already have an ID", ENTITY_NAME, "idexists");
        }
        gymDTO = gymService.save(gymDTO);
        return ResponseEntity.created(new URI("/api/gyms/" + gymDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, gymDTO.getId().toString()))
            .body(gymDTO);
    }

    /**
     * {@code PUT  /gyms/:id} : Updates an existing gym.
     *
     * @param id the id of the gymDTO to save.
     * @param gymDTO the gymDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gymDTO,
     * or with status {@code 400 (Bad Request)} if the gymDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gymDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GymDTO> updateGym(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody GymDTO gymDTO)
        throws URISyntaxException {
        LOG.debug("REST request to update Gym : {}, {}", id, gymDTO);
        if (gymDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gymDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gymRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        gymDTO = gymService.update(gymDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gymDTO.getId().toString()))
            .body(gymDTO);
    }

    /**
     * {@code PATCH  /gyms/:id} : Partial updates given fields of an existing gym, field will ignore if it is null
     *
     * @param id the id of the gymDTO to save.
     * @param gymDTO the gymDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gymDTO,
     * or with status {@code 400 (Bad Request)} if the gymDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gymDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gymDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GymDTO> partialUpdateGym(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GymDTO gymDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Gym partially : {}, {}", id, gymDTO);
        if (gymDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gymDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gymRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GymDTO> result = gymService.partialUpdate(gymDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gymDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gyms} : get all the gyms.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gyms in body.
     */
    @GetMapping("")
    public ResponseEntity<List<GymDTO>> getAllGyms(
        GymCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Gyms by criteria: {}", criteria);

        Page<GymDTO> page = gymQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gyms/count} : count all the gyms.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countGyms(GymCriteria criteria) {
        LOG.debug("REST request to count Gyms by criteria: {}", criteria);
        return ResponseEntity.ok().body(gymQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gyms/:id} : get the "id" gym.
     *
     * @param id the id of the gymDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gymDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GymDTO> getGym(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Gym : {}", id);
        Optional<GymDTO> gymDTO = gymService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gymDTO);
    }

    /**
     * {@code DELETE  /gyms/:id} : delete the "id" gym.
     *
     * @param id the id of the gymDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGym(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Gym : {}", id);
        gymService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
