package com.oscinnovation.fitness.web.rest;

import com.oscinnovation.fitness.repository.LezioneCorsoRepository;
import com.oscinnovation.fitness.service.LezioneCorsoQueryService;
import com.oscinnovation.fitness.service.LezioneCorsoService;
import com.oscinnovation.fitness.service.criteria.LezioneCorsoCriteria;
import com.oscinnovation.fitness.service.dto.LezioneCorsoDTO;
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
 * REST controller for managing {@link com.oscinnovation.fitness.domain.LezioneCorso}.
 */
@RestController
@RequestMapping("/api/lezione-corsos")
public class LezioneCorsoResource {

    private static final Logger LOG = LoggerFactory.getLogger(LezioneCorsoResource.class);

    private static final String ENTITY_NAME = "lezioneCorso";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LezioneCorsoService lezioneCorsoService;

    private final LezioneCorsoRepository lezioneCorsoRepository;

    private final LezioneCorsoQueryService lezioneCorsoQueryService;

    public LezioneCorsoResource(
        LezioneCorsoService lezioneCorsoService,
        LezioneCorsoRepository lezioneCorsoRepository,
        LezioneCorsoQueryService lezioneCorsoQueryService
    ) {
        this.lezioneCorsoService = lezioneCorsoService;
        this.lezioneCorsoRepository = lezioneCorsoRepository;
        this.lezioneCorsoQueryService = lezioneCorsoQueryService;
    }

    /**
     * {@code POST  /lezione-corsos} : Create a new lezioneCorso.
     *
     * @param lezioneCorsoDTO the lezioneCorsoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lezioneCorsoDTO, or with status {@code 400 (Bad Request)} if the lezioneCorso has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LezioneCorsoDTO> createLezioneCorso(@Valid @RequestBody LezioneCorsoDTO lezioneCorsoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save LezioneCorso : {}", lezioneCorsoDTO);
        if (lezioneCorsoDTO.getId() != null) {
            throw new BadRequestAlertException("A new lezioneCorso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        lezioneCorsoDTO = lezioneCorsoService.save(lezioneCorsoDTO);
        return ResponseEntity.created(new URI("/api/lezione-corsos/" + lezioneCorsoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, lezioneCorsoDTO.getId().toString()))
            .body(lezioneCorsoDTO);
    }

    /**
     * {@code PUT  /lezione-corsos/:id} : Updates an existing lezioneCorso.
     *
     * @param id the id of the lezioneCorsoDTO to save.
     * @param lezioneCorsoDTO the lezioneCorsoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lezioneCorsoDTO,
     * or with status {@code 400 (Bad Request)} if the lezioneCorsoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lezioneCorsoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LezioneCorsoDTO> updateLezioneCorso(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LezioneCorsoDTO lezioneCorsoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LezioneCorso : {}, {}", id, lezioneCorsoDTO);
        if (lezioneCorsoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lezioneCorsoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lezioneCorsoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        lezioneCorsoDTO = lezioneCorsoService.update(lezioneCorsoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lezioneCorsoDTO.getId().toString()))
            .body(lezioneCorsoDTO);
    }

    /**
     * {@code PATCH  /lezione-corsos/:id} : Partial updates given fields of an existing lezioneCorso, field will ignore if it is null
     *
     * @param id the id of the lezioneCorsoDTO to save.
     * @param lezioneCorsoDTO the lezioneCorsoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lezioneCorsoDTO,
     * or with status {@code 400 (Bad Request)} if the lezioneCorsoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lezioneCorsoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lezioneCorsoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LezioneCorsoDTO> partialUpdateLezioneCorso(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LezioneCorsoDTO lezioneCorsoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LezioneCorso partially : {}, {}", id, lezioneCorsoDTO);
        if (lezioneCorsoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lezioneCorsoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lezioneCorsoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LezioneCorsoDTO> result = lezioneCorsoService.partialUpdate(lezioneCorsoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lezioneCorsoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lezione-corsos} : get all the lezioneCorsos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lezioneCorsos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LezioneCorsoDTO>> getAllLezioneCorsos(
        LezioneCorsoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get LezioneCorsos by criteria: {}", criteria);

        Page<LezioneCorsoDTO> page = lezioneCorsoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lezione-corsos/count} : count all the lezioneCorsos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countLezioneCorsos(LezioneCorsoCriteria criteria) {
        LOG.debug("REST request to count LezioneCorsos by criteria: {}", criteria);
        return ResponseEntity.ok().body(lezioneCorsoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lezione-corsos/:id} : get the "id" lezioneCorso.
     *
     * @param id the id of the lezioneCorsoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lezioneCorsoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LezioneCorsoDTO> getLezioneCorso(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LezioneCorso : {}", id);
        Optional<LezioneCorsoDTO> lezioneCorsoDTO = lezioneCorsoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lezioneCorsoDTO);
    }

    /**
     * {@code DELETE  /lezione-corsos/:id} : delete the "id" lezioneCorso.
     *
     * @param id the id of the lezioneCorsoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLezioneCorso(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LezioneCorso : {}", id);
        lezioneCorsoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
