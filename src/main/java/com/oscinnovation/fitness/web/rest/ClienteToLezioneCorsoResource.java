package com.oscinnovation.fitness.web.rest;

import com.oscinnovation.fitness.repository.ClienteToLezioneCorsoRepository;
import com.oscinnovation.fitness.service.ClienteToLezioneCorsoQueryService;
import com.oscinnovation.fitness.service.ClienteToLezioneCorsoService;
import com.oscinnovation.fitness.service.criteria.ClienteToLezioneCorsoCriteria;
import com.oscinnovation.fitness.service.dto.ClienteToLezioneCorsoDTO;
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
 * REST controller for managing {@link com.oscinnovation.fitness.domain.ClienteToLezioneCorso}.
 */
@RestController
@RequestMapping("/api/cliente-to-lezione-corsos")
public class ClienteToLezioneCorsoResource {

    private static final Logger LOG = LoggerFactory.getLogger(ClienteToLezioneCorsoResource.class);

    private static final String ENTITY_NAME = "clienteToLezioneCorso";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClienteToLezioneCorsoService clienteToLezioneCorsoService;

    private final ClienteToLezioneCorsoRepository clienteToLezioneCorsoRepository;

    private final ClienteToLezioneCorsoQueryService clienteToLezioneCorsoQueryService;

    public ClienteToLezioneCorsoResource(
        ClienteToLezioneCorsoService clienteToLezioneCorsoService,
        ClienteToLezioneCorsoRepository clienteToLezioneCorsoRepository,
        ClienteToLezioneCorsoQueryService clienteToLezioneCorsoQueryService
    ) {
        this.clienteToLezioneCorsoService = clienteToLezioneCorsoService;
        this.clienteToLezioneCorsoRepository = clienteToLezioneCorsoRepository;
        this.clienteToLezioneCorsoQueryService = clienteToLezioneCorsoQueryService;
    }

    /**
     * {@code POST  /cliente-to-lezione-corsos} : Create a new clienteToLezioneCorso.
     *
     * @param clienteToLezioneCorsoDTO the clienteToLezioneCorsoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clienteToLezioneCorsoDTO, or with status {@code 400 (Bad Request)} if the clienteToLezioneCorso has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ClienteToLezioneCorsoDTO> createClienteToLezioneCorso(
        @Valid @RequestBody ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ClienteToLezioneCorso : {}", clienteToLezioneCorsoDTO);
        if (clienteToLezioneCorsoDTO.getId() != null) {
            throw new BadRequestAlertException("A new clienteToLezioneCorso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        clienteToLezioneCorsoDTO = clienteToLezioneCorsoService.save(clienteToLezioneCorsoDTO);
        return ResponseEntity.created(new URI("/api/cliente-to-lezione-corsos/" + clienteToLezioneCorsoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, clienteToLezioneCorsoDTO.getId().toString()))
            .body(clienteToLezioneCorsoDTO);
    }

    /**
     * {@code PUT  /cliente-to-lezione-corsos/:id} : Updates an existing clienteToLezioneCorso.
     *
     * @param id the id of the clienteToLezioneCorsoDTO to save.
     * @param clienteToLezioneCorsoDTO the clienteToLezioneCorsoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clienteToLezioneCorsoDTO,
     * or with status {@code 400 (Bad Request)} if the clienteToLezioneCorsoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clienteToLezioneCorsoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteToLezioneCorsoDTO> updateClienteToLezioneCorso(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ClienteToLezioneCorso : {}, {}", id, clienteToLezioneCorsoDTO);
        if (clienteToLezioneCorsoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clienteToLezioneCorsoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clienteToLezioneCorsoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        clienteToLezioneCorsoDTO = clienteToLezioneCorsoService.update(clienteToLezioneCorsoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clienteToLezioneCorsoDTO.getId().toString()))
            .body(clienteToLezioneCorsoDTO);
    }

    /**
     * {@code PATCH  /cliente-to-lezione-corsos/:id} : Partial updates given fields of an existing clienteToLezioneCorso, field will ignore if it is null
     *
     * @param id the id of the clienteToLezioneCorsoDTO to save.
     * @param clienteToLezioneCorsoDTO the clienteToLezioneCorsoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clienteToLezioneCorsoDTO,
     * or with status {@code 400 (Bad Request)} if the clienteToLezioneCorsoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the clienteToLezioneCorsoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the clienteToLezioneCorsoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClienteToLezioneCorsoDTO> partialUpdateClienteToLezioneCorso(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ClienteToLezioneCorso partially : {}, {}", id, clienteToLezioneCorsoDTO);
        if (clienteToLezioneCorsoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clienteToLezioneCorsoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clienteToLezioneCorsoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClienteToLezioneCorsoDTO> result = clienteToLezioneCorsoService.partialUpdate(clienteToLezioneCorsoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clienteToLezioneCorsoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cliente-to-lezione-corsos} : get all the clienteToLezioneCorsos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clienteToLezioneCorsos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ClienteToLezioneCorsoDTO>> getAllClienteToLezioneCorsos(
        ClienteToLezioneCorsoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ClienteToLezioneCorsos by criteria: {}", criteria);

        Page<ClienteToLezioneCorsoDTO> page = clienteToLezioneCorsoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cliente-to-lezione-corsos/count} : count all the clienteToLezioneCorsos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countClienteToLezioneCorsos(ClienteToLezioneCorsoCriteria criteria) {
        LOG.debug("REST request to count ClienteToLezioneCorsos by criteria: {}", criteria);
        return ResponseEntity.ok().body(clienteToLezioneCorsoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cliente-to-lezione-corsos/:id} : get the "id" clienteToLezioneCorso.
     *
     * @param id the id of the clienteToLezioneCorsoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clienteToLezioneCorsoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteToLezioneCorsoDTO> getClienteToLezioneCorso(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ClienteToLezioneCorso : {}", id);
        Optional<ClienteToLezioneCorsoDTO> clienteToLezioneCorsoDTO = clienteToLezioneCorsoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clienteToLezioneCorsoDTO);
    }

    /**
     * {@code DELETE  /cliente-to-lezione-corsos/:id} : delete the "id" clienteToLezioneCorso.
     *
     * @param id the id of the clienteToLezioneCorsoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClienteToLezioneCorso(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ClienteToLezioneCorso : {}", id);
        clienteToLezioneCorsoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
