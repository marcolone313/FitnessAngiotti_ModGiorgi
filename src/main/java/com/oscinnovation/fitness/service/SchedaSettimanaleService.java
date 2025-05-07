package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.SchedaSettimanale;
import com.oscinnovation.fitness.repository.SchedaSettimanaleRepository;
import com.oscinnovation.fitness.service.dto.SchedaSettimanaleDTO;
import com.oscinnovation.fitness.service.mapper.SchedaSettimanaleMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oscinnovation.fitness.domain.SchedaSettimanale}.
 */
@Service
@Transactional
public class SchedaSettimanaleService {

    private static final Logger LOG = LoggerFactory.getLogger(SchedaSettimanaleService.class);

    private final SchedaSettimanaleRepository schedaSettimanaleRepository;

    private final SchedaSettimanaleMapper schedaSettimanaleMapper;

    public SchedaSettimanaleService(
        SchedaSettimanaleRepository schedaSettimanaleRepository,
        SchedaSettimanaleMapper schedaSettimanaleMapper
    ) {
        this.schedaSettimanaleRepository = schedaSettimanaleRepository;
        this.schedaSettimanaleMapper = schedaSettimanaleMapper;
    }

    /**
     * Save a schedaSettimanale.
     *
     * @param schedaSettimanaleDTO the entity to save.
     * @return the persisted entity.
     */
    public SchedaSettimanaleDTO save(SchedaSettimanaleDTO schedaSettimanaleDTO) {
        LOG.debug("Request to save SchedaSettimanale : {}", schedaSettimanaleDTO);
        SchedaSettimanale schedaSettimanale = schedaSettimanaleMapper.toEntity(schedaSettimanaleDTO);
        schedaSettimanale = schedaSettimanaleRepository.save(schedaSettimanale);
        return schedaSettimanaleMapper.toDto(schedaSettimanale);
    }

    /**
     * Update a schedaSettimanale.
     *
     * @param schedaSettimanaleDTO the entity to save.
     * @return the persisted entity.
     */
    public SchedaSettimanaleDTO update(SchedaSettimanaleDTO schedaSettimanaleDTO) {
        LOG.debug("Request to update SchedaSettimanale : {}", schedaSettimanaleDTO);
        SchedaSettimanale schedaSettimanale = schedaSettimanaleMapper.toEntity(schedaSettimanaleDTO);
        schedaSettimanale = schedaSettimanaleRepository.save(schedaSettimanale);
        return schedaSettimanaleMapper.toDto(schedaSettimanale);
    }

    /**
     * Partially update a schedaSettimanale.
     *
     * @param schedaSettimanaleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SchedaSettimanaleDTO> partialUpdate(SchedaSettimanaleDTO schedaSettimanaleDTO) {
        LOG.debug("Request to partially update SchedaSettimanale : {}", schedaSettimanaleDTO);

        return schedaSettimanaleRepository
            .findById(schedaSettimanaleDTO.getId())
            .map(existingSchedaSettimanale -> {
                schedaSettimanaleMapper.partialUpdate(existingSchedaSettimanale, schedaSettimanaleDTO);

                return existingSchedaSettimanale;
            })
            .map(schedaSettimanaleRepository::save)
            .map(schedaSettimanaleMapper::toDto);
    }

    /**
     * Get all the schedaSettimanales with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SchedaSettimanaleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return schedaSettimanaleRepository.findAllWithEagerRelationships(pageable).map(schedaSettimanaleMapper::toDto);
    }

    /**
     *  Get all the schedaSettimanales where ReportSettimanale is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SchedaSettimanaleDTO> findAllWhereReportSettimanaleIsNull() {
        LOG.debug("Request to get all schedaSettimanales where ReportSettimanale is null");
        return StreamSupport.stream(schedaSettimanaleRepository.findAll().spliterator(), false)
            .filter(schedaSettimanale -> schedaSettimanale.getReportSettimanale() == null)
            .map(schedaSettimanaleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one schedaSettimanale by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SchedaSettimanaleDTO> findOne(Long id) {
        LOG.debug("Request to get SchedaSettimanale : {}", id);
        return schedaSettimanaleRepository.findOneWithEagerRelationships(id).map(schedaSettimanaleMapper::toDto);
    }

    /**
     * Delete the schedaSettimanale by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SchedaSettimanale : {}", id);
        schedaSettimanaleRepository.deleteById(id);
    }
}
