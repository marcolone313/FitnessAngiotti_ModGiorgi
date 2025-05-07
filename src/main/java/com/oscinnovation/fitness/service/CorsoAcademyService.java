package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.CorsoAcademy;
import com.oscinnovation.fitness.repository.CorsoAcademyRepository;
import com.oscinnovation.fitness.service.dto.CorsoAcademyDTO;
import com.oscinnovation.fitness.service.mapper.CorsoAcademyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oscinnovation.fitness.domain.CorsoAcademy}.
 */
@Service
@Transactional
public class CorsoAcademyService {

    private static final Logger LOG = LoggerFactory.getLogger(CorsoAcademyService.class);

    private final CorsoAcademyRepository corsoAcademyRepository;

    private final CorsoAcademyMapper corsoAcademyMapper;

    public CorsoAcademyService(CorsoAcademyRepository corsoAcademyRepository, CorsoAcademyMapper corsoAcademyMapper) {
        this.corsoAcademyRepository = corsoAcademyRepository;
        this.corsoAcademyMapper = corsoAcademyMapper;
    }

    /**
     * Save a corsoAcademy.
     *
     * @param corsoAcademyDTO the entity to save.
     * @return the persisted entity.
     */
    public CorsoAcademyDTO save(CorsoAcademyDTO corsoAcademyDTO) {
        LOG.debug("Request to save CorsoAcademy : {}", corsoAcademyDTO);
        CorsoAcademy corsoAcademy = corsoAcademyMapper.toEntity(corsoAcademyDTO);
        corsoAcademy = corsoAcademyRepository.save(corsoAcademy);
        return corsoAcademyMapper.toDto(corsoAcademy);
    }

    /**
     * Update a corsoAcademy.
     *
     * @param corsoAcademyDTO the entity to save.
     * @return the persisted entity.
     */
    public CorsoAcademyDTO update(CorsoAcademyDTO corsoAcademyDTO) {
        LOG.debug("Request to update CorsoAcademy : {}", corsoAcademyDTO);
        CorsoAcademy corsoAcademy = corsoAcademyMapper.toEntity(corsoAcademyDTO);
        corsoAcademy = corsoAcademyRepository.save(corsoAcademy);
        return corsoAcademyMapper.toDto(corsoAcademy);
    }

    /**
     * Partially update a corsoAcademy.
     *
     * @param corsoAcademyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CorsoAcademyDTO> partialUpdate(CorsoAcademyDTO corsoAcademyDTO) {
        LOG.debug("Request to partially update CorsoAcademy : {}", corsoAcademyDTO);

        return corsoAcademyRepository
            .findById(corsoAcademyDTO.getId())
            .map(existingCorsoAcademy -> {
                corsoAcademyMapper.partialUpdate(existingCorsoAcademy, corsoAcademyDTO);

                return existingCorsoAcademy;
            })
            .map(corsoAcademyRepository::save)
            .map(corsoAcademyMapper::toDto);
    }

    /**
     * Get one corsoAcademy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CorsoAcademyDTO> findOne(Long id) {
        LOG.debug("Request to get CorsoAcademy : {}", id);
        return corsoAcademyRepository.findById(id).map(corsoAcademyMapper::toDto);
    }

    /**
     * Delete the corsoAcademy by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CorsoAcademy : {}", id);
        corsoAcademyRepository.deleteById(id);
    }
}
