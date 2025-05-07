package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.Dieta;
import com.oscinnovation.fitness.repository.DietaRepository;
import com.oscinnovation.fitness.service.dto.DietaDTO;
import com.oscinnovation.fitness.service.mapper.DietaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oscinnovation.fitness.domain.Dieta}.
 */
@Service
@Transactional
public class DietaService {

    private static final Logger LOG = LoggerFactory.getLogger(DietaService.class);

    private final DietaRepository dietaRepository;

    private final DietaMapper dietaMapper;

    public DietaService(DietaRepository dietaRepository, DietaMapper dietaMapper) {
        this.dietaRepository = dietaRepository;
        this.dietaMapper = dietaMapper;
    }

    /**
     * Save a dieta.
     *
     * @param dietaDTO the entity to save.
     * @return the persisted entity.
     */
    public DietaDTO save(DietaDTO dietaDTO) {
        LOG.debug("Request to save Dieta : {}", dietaDTO);
        Dieta dieta = dietaMapper.toEntity(dietaDTO);
        dieta = dietaRepository.save(dieta);
        return dietaMapper.toDto(dieta);
    }

    /**
     * Update a dieta.
     *
     * @param dietaDTO the entity to save.
     * @return the persisted entity.
     */
    public DietaDTO update(DietaDTO dietaDTO) {
        LOG.debug("Request to update Dieta : {}", dietaDTO);
        Dieta dieta = dietaMapper.toEntity(dietaDTO);
        dieta = dietaRepository.save(dieta);
        return dietaMapper.toDto(dieta);
    }

    /**
     * Partially update a dieta.
     *
     * @param dietaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DietaDTO> partialUpdate(DietaDTO dietaDTO) {
        LOG.debug("Request to partially update Dieta : {}", dietaDTO);

        return dietaRepository
            .findById(dietaDTO.getId())
            .map(existingDieta -> {
                dietaMapper.partialUpdate(existingDieta, dietaDTO);

                return existingDieta;
            })
            .map(dietaRepository::save)
            .map(dietaMapper::toDto);
    }

    /**
     * Get all the dietas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DietaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return dietaRepository.findAllWithEagerRelationships(pageable).map(dietaMapper::toDto);
    }

    /**
     * Get one dieta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DietaDTO> findOne(Long id) {
        LOG.debug("Request to get Dieta : {}", id);
        return dietaRepository.findOneWithEagerRelationships(id).map(dietaMapper::toDto);
    }

    /**
     * Delete the dieta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Dieta : {}", id);
        dietaRepository.deleteById(id);
    }
}
