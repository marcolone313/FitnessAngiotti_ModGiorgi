package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.ReportSettimanale;
import com.oscinnovation.fitness.repository.ReportSettimanaleRepository;
import com.oscinnovation.fitness.service.dto.ReportSettimanaleDTO;
import com.oscinnovation.fitness.service.mapper.ReportSettimanaleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oscinnovation.fitness.domain.ReportSettimanale}.
 */
@Service
@Transactional
public class ReportSettimanaleService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportSettimanaleService.class);

    private final ReportSettimanaleRepository reportSettimanaleRepository;

    private final ReportSettimanaleMapper reportSettimanaleMapper;

    public ReportSettimanaleService(
        ReportSettimanaleRepository reportSettimanaleRepository,
        ReportSettimanaleMapper reportSettimanaleMapper
    ) {
        this.reportSettimanaleRepository = reportSettimanaleRepository;
        this.reportSettimanaleMapper = reportSettimanaleMapper;
    }

    /**
     * Save a reportSettimanale.
     *
     * @param reportSettimanaleDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportSettimanaleDTO save(ReportSettimanaleDTO reportSettimanaleDTO) {
        LOG.debug("Request to save ReportSettimanale : {}", reportSettimanaleDTO);
        ReportSettimanale reportSettimanale = reportSettimanaleMapper.toEntity(reportSettimanaleDTO);
        reportSettimanale = reportSettimanaleRepository.save(reportSettimanale);
        return reportSettimanaleMapper.toDto(reportSettimanale);
    }

    /**
     * Update a reportSettimanale.
     *
     * @param reportSettimanaleDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportSettimanaleDTO update(ReportSettimanaleDTO reportSettimanaleDTO) {
        LOG.debug("Request to update ReportSettimanale : {}", reportSettimanaleDTO);
        ReportSettimanale reportSettimanale = reportSettimanaleMapper.toEntity(reportSettimanaleDTO);
        reportSettimanale = reportSettimanaleRepository.save(reportSettimanale);
        return reportSettimanaleMapper.toDto(reportSettimanale);
    }

    /**
     * Partially update a reportSettimanale.
     *
     * @param reportSettimanaleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportSettimanaleDTO> partialUpdate(ReportSettimanaleDTO reportSettimanaleDTO) {
        LOG.debug("Request to partially update ReportSettimanale : {}", reportSettimanaleDTO);

        return reportSettimanaleRepository
            .findById(reportSettimanaleDTO.getId())
            .map(existingReportSettimanale -> {
                reportSettimanaleMapper.partialUpdate(existingReportSettimanale, reportSettimanaleDTO);

                return existingReportSettimanale;
            })
            .map(reportSettimanaleRepository::save)
            .map(reportSettimanaleMapper::toDto);
    }

    /**
     * Get all the reportSettimanales with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReportSettimanaleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reportSettimanaleRepository.findAllWithEagerRelationships(pageable).map(reportSettimanaleMapper::toDto);
    }

    /**
     * Get one reportSettimanale by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportSettimanaleDTO> findOne(Long id) {
        LOG.debug("Request to get ReportSettimanale : {}", id);
        return reportSettimanaleRepository.findOneWithEagerRelationships(id).map(reportSettimanaleMapper::toDto);
    }

    /**
     * Delete the reportSettimanale by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReportSettimanale : {}", id);
        reportSettimanaleRepository.deleteById(id);
    }
}
