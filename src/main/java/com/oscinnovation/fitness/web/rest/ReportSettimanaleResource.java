package com.oscinnovation.fitness.web.rest;

import com.oscinnovation.fitness.repository.ReportSettimanaleRepository;
import com.oscinnovation.fitness.service.ReportSettimanaleQueryService;
import com.oscinnovation.fitness.service.ReportSettimanaleService;
import com.oscinnovation.fitness.service.criteria.ReportSettimanaleCriteria;
import com.oscinnovation.fitness.service.dto.ReportSettimanaleDTO;
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
 * REST controller for managing {@link com.oscinnovation.fitness.domain.ReportSettimanale}.
 */
@RestController
@RequestMapping("/api/report-settimanales")
public class ReportSettimanaleResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReportSettimanaleResource.class);

    private static final String ENTITY_NAME = "reportSettimanale";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportSettimanaleService reportSettimanaleService;

    private final ReportSettimanaleRepository reportSettimanaleRepository;

    private final ReportSettimanaleQueryService reportSettimanaleQueryService;

    public ReportSettimanaleResource(
        ReportSettimanaleService reportSettimanaleService,
        ReportSettimanaleRepository reportSettimanaleRepository,
        ReportSettimanaleQueryService reportSettimanaleQueryService
    ) {
        this.reportSettimanaleService = reportSettimanaleService;
        this.reportSettimanaleRepository = reportSettimanaleRepository;
        this.reportSettimanaleQueryService = reportSettimanaleQueryService;
    }

    /**
     * {@code POST  /report-settimanales} : Create a new reportSettimanale.
     *
     * @param reportSettimanaleDTO the reportSettimanaleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportSettimanaleDTO, or with status {@code 400 (Bad Request)} if the reportSettimanale has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportSettimanaleDTO> createReportSettimanale(@Valid @RequestBody ReportSettimanaleDTO reportSettimanaleDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ReportSettimanale : {}", reportSettimanaleDTO);
        if (reportSettimanaleDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportSettimanale cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportSettimanaleDTO = reportSettimanaleService.save(reportSettimanaleDTO);
        return ResponseEntity.created(new URI("/api/report-settimanales/" + reportSettimanaleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportSettimanaleDTO.getId().toString()))
            .body(reportSettimanaleDTO);
    }

    /**
     * {@code PUT  /report-settimanales/:id} : Updates an existing reportSettimanale.
     *
     * @param id the id of the reportSettimanaleDTO to save.
     * @param reportSettimanaleDTO the reportSettimanaleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportSettimanaleDTO,
     * or with status {@code 400 (Bad Request)} if the reportSettimanaleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportSettimanaleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportSettimanaleDTO> updateReportSettimanale(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportSettimanaleDTO reportSettimanaleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReportSettimanale : {}, {}", id, reportSettimanaleDTO);
        if (reportSettimanaleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportSettimanaleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportSettimanaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportSettimanaleDTO = reportSettimanaleService.update(reportSettimanaleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportSettimanaleDTO.getId().toString()))
            .body(reportSettimanaleDTO);
    }

    /**
     * {@code PATCH  /report-settimanales/:id} : Partial updates given fields of an existing reportSettimanale, field will ignore if it is null
     *
     * @param id the id of the reportSettimanaleDTO to save.
     * @param reportSettimanaleDTO the reportSettimanaleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportSettimanaleDTO,
     * or with status {@code 400 (Bad Request)} if the reportSettimanaleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportSettimanaleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportSettimanaleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportSettimanaleDTO> partialUpdateReportSettimanale(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportSettimanaleDTO reportSettimanaleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReportSettimanale partially : {}, {}", id, reportSettimanaleDTO);
        if (reportSettimanaleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportSettimanaleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportSettimanaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportSettimanaleDTO> result = reportSettimanaleService.partialUpdate(reportSettimanaleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportSettimanaleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-settimanales} : get all the reportSettimanales.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportSettimanales in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReportSettimanaleDTO>> getAllReportSettimanales(
        ReportSettimanaleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReportSettimanales by criteria: {}", criteria);

        Page<ReportSettimanaleDTO> page = reportSettimanaleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /report-settimanales/count} : count all the reportSettimanales.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReportSettimanales(ReportSettimanaleCriteria criteria) {
        LOG.debug("REST request to count ReportSettimanales by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportSettimanaleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /report-settimanales/:id} : get the "id" reportSettimanale.
     *
     * @param id the id of the reportSettimanaleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportSettimanaleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportSettimanaleDTO> getReportSettimanale(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReportSettimanale : {}", id);
        Optional<ReportSettimanaleDTO> reportSettimanaleDTO = reportSettimanaleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportSettimanaleDTO);
    }

    /**
     * {@code DELETE  /report-settimanales/:id} : delete the "id" reportSettimanale.
     *
     * @param id the id of the reportSettimanaleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportSettimanale(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReportSettimanale : {}", id);
        reportSettimanaleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
