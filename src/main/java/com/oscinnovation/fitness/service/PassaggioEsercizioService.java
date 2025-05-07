package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.PassaggioEsercizio;
import com.oscinnovation.fitness.repository.PassaggioEsercizioRepository;
import com.oscinnovation.fitness.service.dto.PassaggioEsercizioDTO;
import com.oscinnovation.fitness.service.mapper.PassaggioEsercizioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oscinnovation.fitness.domain.PassaggioEsercizio}.
 */
@Service
@Transactional
public class PassaggioEsercizioService {

    private static final Logger LOG = LoggerFactory.getLogger(PassaggioEsercizioService.class);

    private final PassaggioEsercizioRepository passaggioEsercizioRepository;

    private final PassaggioEsercizioMapper passaggioEsercizioMapper;

    public PassaggioEsercizioService(
        PassaggioEsercizioRepository passaggioEsercizioRepository,
        PassaggioEsercizioMapper passaggioEsercizioMapper
    ) {
        this.passaggioEsercizioRepository = passaggioEsercizioRepository;
        this.passaggioEsercizioMapper = passaggioEsercizioMapper;
    }

    /**
     * Save a passaggioEsercizio.
     *
     * @param passaggioEsercizioDTO the entity to save.
     * @return the persisted entity.
     */
    public PassaggioEsercizioDTO save(PassaggioEsercizioDTO passaggioEsercizioDTO) {
        LOG.debug("Request to save PassaggioEsercizio : {}", passaggioEsercizioDTO);
        PassaggioEsercizio passaggioEsercizio = passaggioEsercizioMapper.toEntity(passaggioEsercizioDTO);
        passaggioEsercizio = passaggioEsercizioRepository.save(passaggioEsercizio);
        return passaggioEsercizioMapper.toDto(passaggioEsercizio);
    }

    /**
     * Update a passaggioEsercizio.
     *
     * @param passaggioEsercizioDTO the entity to save.
     * @return the persisted entity.
     */
    public PassaggioEsercizioDTO update(PassaggioEsercizioDTO passaggioEsercizioDTO) {
        LOG.debug("Request to update PassaggioEsercizio : {}", passaggioEsercizioDTO);
        PassaggioEsercizio passaggioEsercizio = passaggioEsercizioMapper.toEntity(passaggioEsercizioDTO);
        passaggioEsercizio = passaggioEsercizioRepository.save(passaggioEsercizio);
        return passaggioEsercizioMapper.toDto(passaggioEsercizio);
    }

    /**
     * Partially update a passaggioEsercizio.
     *
     * @param passaggioEsercizioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PassaggioEsercizioDTO> partialUpdate(PassaggioEsercizioDTO passaggioEsercizioDTO) {
        LOG.debug("Request to partially update PassaggioEsercizio : {}", passaggioEsercizioDTO);

        return passaggioEsercizioRepository
            .findById(passaggioEsercizioDTO.getId())
            .map(existingPassaggioEsercizio -> {
                passaggioEsercizioMapper.partialUpdate(existingPassaggioEsercizio, passaggioEsercizioDTO);

                return existingPassaggioEsercizio;
            })
            .map(passaggioEsercizioRepository::save)
            .map(passaggioEsercizioMapper::toDto);
    }

    /**
     * Get all the passaggioEsercizios with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PassaggioEsercizioDTO> findAllWithEagerRelationships(Pageable pageable) {
        return passaggioEsercizioRepository.findAllWithEagerRelationships(pageable).map(passaggioEsercizioMapper::toDto);
    }

    /**
     * Get one passaggioEsercizio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PassaggioEsercizioDTO> findOne(Long id) {
        LOG.debug("Request to get PassaggioEsercizio : {}", id);
        return passaggioEsercizioRepository.findOneWithEagerRelationships(id).map(passaggioEsercizioMapper::toDto);
    }

    /**
     * Delete the passaggioEsercizio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PassaggioEsercizio : {}", id);
        passaggioEsercizioRepository.deleteById(id);
    }
}
