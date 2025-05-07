package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.CircuitoToEsercizio;
import com.oscinnovation.fitness.repository.CircuitoToEsercizioRepository;
import com.oscinnovation.fitness.service.dto.CircuitoToEsercizioDTO;
import com.oscinnovation.fitness.service.mapper.CircuitoToEsercizioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oscinnovation.fitness.domain.CircuitoToEsercizio}.
 */
@Service
@Transactional
public class CircuitoToEsercizioService {

    private static final Logger LOG = LoggerFactory.getLogger(CircuitoToEsercizioService.class);

    private final CircuitoToEsercizioRepository circuitoToEsercizioRepository;

    private final CircuitoToEsercizioMapper circuitoToEsercizioMapper;

    public CircuitoToEsercizioService(
        CircuitoToEsercizioRepository circuitoToEsercizioRepository,
        CircuitoToEsercizioMapper circuitoToEsercizioMapper
    ) {
        this.circuitoToEsercizioRepository = circuitoToEsercizioRepository;
        this.circuitoToEsercizioMapper = circuitoToEsercizioMapper;
    }

    /**
     * Save a circuitoToEsercizio.
     *
     * @param circuitoToEsercizioDTO the entity to save.
     * @return the persisted entity.
     */
    public CircuitoToEsercizioDTO save(CircuitoToEsercizioDTO circuitoToEsercizioDTO) {
        LOG.debug("Request to save CircuitoToEsercizio : {}", circuitoToEsercizioDTO);
        CircuitoToEsercizio circuitoToEsercizio = circuitoToEsercizioMapper.toEntity(circuitoToEsercizioDTO);
        circuitoToEsercizio = circuitoToEsercizioRepository.save(circuitoToEsercizio);
        return circuitoToEsercizioMapper.toDto(circuitoToEsercizio);
    }

    /**
     * Update a circuitoToEsercizio.
     *
     * @param circuitoToEsercizioDTO the entity to save.
     * @return the persisted entity.
     */
    public CircuitoToEsercizioDTO update(CircuitoToEsercizioDTO circuitoToEsercizioDTO) {
        LOG.debug("Request to update CircuitoToEsercizio : {}", circuitoToEsercizioDTO);
        CircuitoToEsercizio circuitoToEsercizio = circuitoToEsercizioMapper.toEntity(circuitoToEsercizioDTO);
        circuitoToEsercizio = circuitoToEsercizioRepository.save(circuitoToEsercizio);
        return circuitoToEsercizioMapper.toDto(circuitoToEsercizio);
    }

    /**
     * Partially update a circuitoToEsercizio.
     *
     * @param circuitoToEsercizioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CircuitoToEsercizioDTO> partialUpdate(CircuitoToEsercizioDTO circuitoToEsercizioDTO) {
        LOG.debug("Request to partially update CircuitoToEsercizio : {}", circuitoToEsercizioDTO);

        return circuitoToEsercizioRepository
            .findById(circuitoToEsercizioDTO.getId())
            .map(existingCircuitoToEsercizio -> {
                circuitoToEsercizioMapper.partialUpdate(existingCircuitoToEsercizio, circuitoToEsercizioDTO);

                return existingCircuitoToEsercizio;
            })
            .map(circuitoToEsercizioRepository::save)
            .map(circuitoToEsercizioMapper::toDto);
    }

    /**
     * Get all the circuitoToEsercizios with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CircuitoToEsercizioDTO> findAllWithEagerRelationships(Pageable pageable) {
        return circuitoToEsercizioRepository.findAllWithEagerRelationships(pageable).map(circuitoToEsercizioMapper::toDto);
    }

    /**
     * Get one circuitoToEsercizio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CircuitoToEsercizioDTO> findOne(Long id) {
        LOG.debug("Request to get CircuitoToEsercizio : {}", id);
        return circuitoToEsercizioRepository.findOneWithEagerRelationships(id).map(circuitoToEsercizioMapper::toDto);
    }

    /**
     * Delete the circuitoToEsercizio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CircuitoToEsercizio : {}", id);
        circuitoToEsercizioRepository.deleteById(id);
    }
}
