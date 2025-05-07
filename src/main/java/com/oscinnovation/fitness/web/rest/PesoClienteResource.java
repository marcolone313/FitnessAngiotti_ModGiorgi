package com.oscinnovation.fitness.web.rest;

import com.oscinnovation.fitness.repository.PesoClienteRepository;
import com.oscinnovation.fitness.service.PesoClienteQueryService;
import com.oscinnovation.fitness.service.PesoClienteService;
import com.oscinnovation.fitness.service.criteria.PesoClienteCriteria;
import com.oscinnovation.fitness.service.dto.PesoClienteDTO;
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
 * REST controller for managing {@link com.oscinnovation.fitness.domain.PesoCliente}.
 */
@RestController
@RequestMapping("/api/peso-clientes")
public class PesoClienteResource {

    private static final Logger LOG = LoggerFactory.getLogger(PesoClienteResource.class);

    private static final String ENTITY_NAME = "pesoCliente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PesoClienteService pesoClienteService;

    private final PesoClienteRepository pesoClienteRepository;

    private final PesoClienteQueryService pesoClienteQueryService;

    public PesoClienteResource(
        PesoClienteService pesoClienteService,
        PesoClienteRepository pesoClienteRepository,
        PesoClienteQueryService pesoClienteQueryService
    ) {
        this.pesoClienteService = pesoClienteService;
        this.pesoClienteRepository = pesoClienteRepository;
        this.pesoClienteQueryService = pesoClienteQueryService;
    }

    /**
     * {@code POST  /peso-clientes} : Create a new pesoCliente.
     *
     * @param pesoClienteDTO the pesoClienteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pesoClienteDTO, or with status {@code 400 (Bad Request)} if the pesoCliente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PesoClienteDTO> createPesoCliente(@Valid @RequestBody PesoClienteDTO pesoClienteDTO) throws URISyntaxException {
        LOG.debug("REST request to save PesoCliente : {}", pesoClienteDTO);
        if (pesoClienteDTO.getId() != null) {
            throw new BadRequestAlertException("A new pesoCliente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pesoClienteDTO = pesoClienteService.save(pesoClienteDTO);
        return ResponseEntity.created(new URI("/api/peso-clientes/" + pesoClienteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pesoClienteDTO.getId().toString()))
            .body(pesoClienteDTO);
    }

    /**
     * {@code PUT  /peso-clientes/:id} : Updates an existing pesoCliente.
     *
     * @param id the id of the pesoClienteDTO to save.
     * @param pesoClienteDTO the pesoClienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pesoClienteDTO,
     * or with status {@code 400 (Bad Request)} if the pesoClienteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pesoClienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PesoClienteDTO> updatePesoCliente(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PesoClienteDTO pesoClienteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PesoCliente : {}, {}", id, pesoClienteDTO);
        if (pesoClienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pesoClienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pesoClienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pesoClienteDTO = pesoClienteService.update(pesoClienteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pesoClienteDTO.getId().toString()))
            .body(pesoClienteDTO);
    }

    /**
     * {@code PATCH  /peso-clientes/:id} : Partial updates given fields of an existing pesoCliente, field will ignore if it is null
     *
     * @param id the id of the pesoClienteDTO to save.
     * @param pesoClienteDTO the pesoClienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pesoClienteDTO,
     * or with status {@code 400 (Bad Request)} if the pesoClienteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pesoClienteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pesoClienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PesoClienteDTO> partialUpdatePesoCliente(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PesoClienteDTO pesoClienteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PesoCliente partially : {}, {}", id, pesoClienteDTO);
        if (pesoClienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pesoClienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pesoClienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PesoClienteDTO> result = pesoClienteService.partialUpdate(pesoClienteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pesoClienteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /peso-clientes} : get all the pesoClientes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pesoClientes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PesoClienteDTO>> getAllPesoClientes(
        PesoClienteCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PesoClientes by criteria: {}", criteria);

        Page<PesoClienteDTO> page = pesoClienteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /peso-clientes/count} : count all the pesoClientes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPesoClientes(PesoClienteCriteria criteria) {
        LOG.debug("REST request to count PesoClientes by criteria: {}", criteria);
        return ResponseEntity.ok().body(pesoClienteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /peso-clientes/:id} : get the "id" pesoCliente.
     *
     * @param id the id of the pesoClienteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pesoClienteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PesoClienteDTO> getPesoCliente(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PesoCliente : {}", id);
        Optional<PesoClienteDTO> pesoClienteDTO = pesoClienteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pesoClienteDTO);
    }

    /**
     * {@code DELETE  /peso-clientes/:id} : delete the "id" pesoCliente.
     *
     * @param id the id of the pesoClienteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePesoCliente(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PesoCliente : {}", id);
        pesoClienteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
