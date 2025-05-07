package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.Corsa;
import com.oscinnovation.fitness.repository.CorsaRepository;
import com.oscinnovation.fitness.service.dto.CorsaDTO;
import com.oscinnovation.fitness.service.mapper.CorsaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oscinnovation.fitness.domain.Corsa}.
 */
@Service
@Transactional
public class CorsaService {

    private static final Logger LOG = LoggerFactory.getLogger(CorsaService.class);

    private final CorsaRepository corsaRepository;

    private final CorsaMapper corsaMapper;

    public CorsaService(CorsaRepository corsaRepository, CorsaMapper corsaMapper) {
        this.corsaRepository = corsaRepository;
        this.corsaMapper = corsaMapper;
    }

    /**
     * Save a corsa.
     *
     * @param corsaDTO the entity to save.
     * @return the persisted entity.
     */
    public CorsaDTO save(CorsaDTO corsaDTO) {
        LOG.debug("Request to save Corsa : {}", corsaDTO);
        Corsa corsa = corsaMapper.toEntity(corsaDTO);
        corsa = corsaRepository.save(corsa);
        return corsaMapper.toDto(corsa);
    }

    /**
     * Update a corsa.
     *
     * @param corsaDTO the entity to save.
     * @return the persisted entity.
     */
    public CorsaDTO update(CorsaDTO corsaDTO) {
        LOG.debug("Request to update Corsa : {}", corsaDTO);
        Corsa corsa = corsaMapper.toEntity(corsaDTO);
        corsa = corsaRepository.save(corsa);
        return corsaMapper.toDto(corsa);
    }

    /**
     * Partially update a corsa.
     *
     * @param corsaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CorsaDTO> partialUpdate(CorsaDTO corsaDTO) {
        LOG.debug("Request to partially update Corsa : {}", corsaDTO);

        return corsaRepository
            .findById(corsaDTO.getId())
            .map(existingCorsa -> {
                corsaMapper.partialUpdate(existingCorsa, corsaDTO);

                return existingCorsa;
            })
            .map(corsaRepository::save)
            .map(corsaMapper::toDto);
    }

    /**
     * Get one corsa by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CorsaDTO> findOne(Long id) {
        LOG.debug("Request to get Corsa : {}", id);
        return corsaRepository.findById(id).map(corsaMapper::toDto);
    }

    /**
     * Delete the corsa by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Corsa : {}", id);
        corsaRepository.deleteById(id);
    }
}
