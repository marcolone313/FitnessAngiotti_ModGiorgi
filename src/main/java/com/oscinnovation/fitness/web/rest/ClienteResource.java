package com.oscinnovation.fitness.web.rest;

import com.oscinnovation.fitness.repository.ClienteRepository;
import com.oscinnovation.fitness.service.ClienteQueryService;
import com.oscinnovation.fitness.service.ClienteService;
import com.oscinnovation.fitness.service.criteria.ClienteCriteria;
import com.oscinnovation.fitness.service.dto.ClienteDTO;
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
 * REST controller for managing {@link com.oscinnovation.fitness.domain.Cliente}.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteResource {

    private static final Logger LOG = LoggerFactory.getLogger(ClienteResource.class);

    private static final String ENTITY_NAME = "cliente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClienteService clienteService;

    private final ClienteRepository clienteRepository;

    private final ClienteQueryService clienteQueryService;

    public ClienteResource(ClienteService clienteService, ClienteRepository clienteRepository, ClienteQueryService clienteQueryService) {
        this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
        this.clienteQueryService = clienteQueryService;
    }

    /**
     * {@code POST  /clientes} : Create a new cliente.
     *
     * @param clienteDTO the clienteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clienteDTO, or with status {@code 400 (Bad Request)} if the cliente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ClienteDTO> createCliente(@Valid @RequestBody ClienteDTO clienteDTO) throws URISyntaxException {
        LOG.debug("REST request to save Cliente : {}", clienteDTO);
        if (clienteDTO.getId() != null) {
            throw new BadRequestAlertException("A new cliente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        clienteDTO = clienteService.save(clienteDTO);
        return ResponseEntity.created(new URI("/api/clientes/" + clienteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, clienteDTO.getId().toString()))
            .body(clienteDTO);
    }

    /**
     * {@code PUT  /clientes/:id} : Updates an existing cliente.
     *
     * @param id the id of the clienteDTO to save.
     * @param clienteDTO the clienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clienteDTO,
     * or with status {@code 400 (Bad Request)} if the clienteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> updateCliente(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClienteDTO clienteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Cliente : {}, {}", id, clienteDTO);
        if (clienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        clienteDTO = clienteService.update(clienteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clienteDTO.getId().toString()))
            .body(clienteDTO);
    }

    /**
     * {@code PATCH  /clientes/:id} : Partial updates given fields of an existing cliente, field will ignore if it is null
     *
     * @param id the id of the clienteDTO to save.
     * @param clienteDTO the clienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clienteDTO,
     * or with status {@code 400 (Bad Request)} if the clienteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the clienteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the clienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClienteDTO> partialUpdateCliente(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClienteDTO clienteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Cliente partially : {}, {}", id, clienteDTO);
        if (clienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClienteDTO> result = clienteService.partialUpdate(clienteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clienteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /clientes} : get all the clientes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clientes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ClienteDTO>> getAllClientes(
        ClienteCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Clientes by criteria: {}", criteria);

        Page<ClienteDTO> page = clienteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /clientes/count} : count all the clientes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countClientes(ClienteCriteria criteria) {
        LOG.debug("REST request to count Clientes by criteria: {}", criteria);
        return ResponseEntity.ok().body(clienteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /clientes/:id} : get the "id" cliente.
     *
     * @param id the id of the clienteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clienteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getCliente(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Cliente : {}", id);
        Optional<ClienteDTO> clienteDTO = clienteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clienteDTO);
    }

    /**
     * {@code DELETE  /clientes/:id} : delete the "id" cliente.
     *
     * @param id the id of the clienteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Cliente : {}", id);
        clienteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
