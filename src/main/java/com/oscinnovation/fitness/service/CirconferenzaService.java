package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.Circonferenza;
import com.oscinnovation.fitness.repository.CirconferenzaRepository;
import com.oscinnovation.fitness.service.dto.CirconferenzaDTO;
import com.oscinnovation.fitness.service.mapper.CirconferenzaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oscinnovation.fitness.domain.Circonferenza}.
 */
@Service
@Transactional
public class CirconferenzaService {

    private static final Logger LOG = LoggerFactory.getLogger(CirconferenzaService.class);

    private final CirconferenzaRepository circonferenzaRepository;

    private final CirconferenzaMapper circonferenzaMapper;

    public CirconferenzaService(CirconferenzaRepository circonferenzaRepository, CirconferenzaMapper circonferenzaMapper) {
        this.circonferenzaRepository = circonferenzaRepository;
        this.circonferenzaMapper = circonferenzaMapper;
    }

    /**
     * Save a circonferenza.
     *
     * @param circonferenzaDTO the entity to save.
     * @return the persisted entity.
     */
    public CirconferenzaDTO save(CirconferenzaDTO circonferenzaDTO) {
        LOG.debug("Request to save Circonferenza : {}", circonferenzaDTO);
        Circonferenza circonferenza = circonferenzaMapper.toEntity(circonferenzaDTO);
        circonferenza = circonferenzaRepository.save(circonferenza);
        return circonferenzaMapper.toDto(circonferenza);
    }

    /**
     * Update a circonferenza.
     *
     * @param circonferenzaDTO the entity to save.
     * @return the persisted entity.
     */
    public CirconferenzaDTO update(CirconferenzaDTO circonferenzaDTO) {
        LOG.debug("Request to update Circonferenza : {}", circonferenzaDTO);
        Circonferenza circonferenza = circonferenzaMapper.toEntity(circonferenzaDTO);
        circonferenza = circonferenzaRepository.save(circonferenza);
        return circonferenzaMapper.toDto(circonferenza);
    }

    /**
     * Partially update a circonferenza.
     *
     * @param circonferenzaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CirconferenzaDTO> partialUpdate(CirconferenzaDTO circonferenzaDTO) {
        LOG.debug("Request to partially update Circonferenza : {}", circonferenzaDTO);

        return circonferenzaRepository
            .findById(circonferenzaDTO.getId())
            .map(existingCirconferenza -> {
                circonferenzaMapper.partialUpdate(existingCirconferenza, circonferenzaDTO);

                return existingCirconferenza;
            })
            .map(circonferenzaRepository::save)
            .map(circonferenzaMapper::toDto);
    }

    /**
     * Get all the circonferenzas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CirconferenzaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return circonferenzaRepository.findAllWithEagerRelationships(pageable).map(circonferenzaMapper::toDto);
    }

    /**
     * Get one circonferenza by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CirconferenzaDTO> findOne(Long id) {
        LOG.debug("Request to get Circonferenza : {}", id);
        return circonferenzaRepository.findOneWithEagerRelationships(id).map(circonferenzaMapper::toDto);
    }

    /**
     * Delete the circonferenza by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Circonferenza : {}", id);
        circonferenzaRepository.deleteById(id);
    }
}
