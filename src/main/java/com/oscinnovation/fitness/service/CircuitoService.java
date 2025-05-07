package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.Circuito;
import com.oscinnovation.fitness.repository.CircuitoRepository;
import com.oscinnovation.fitness.service.dto.CircuitoDTO;
import com.oscinnovation.fitness.service.mapper.CircuitoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oscinnovation.fitness.domain.Circuito}.
 */
@Service
@Transactional
public class CircuitoService {

    private static final Logger LOG = LoggerFactory.getLogger(CircuitoService.class);

    private final CircuitoRepository circuitoRepository;

    private final CircuitoMapper circuitoMapper;

    public CircuitoService(CircuitoRepository circuitoRepository, CircuitoMapper circuitoMapper) {
        this.circuitoRepository = circuitoRepository;
        this.circuitoMapper = circuitoMapper;
    }

    /**
     * Save a circuito.
     *
     * @param circuitoDTO the entity to save.
     * @return the persisted entity.
     */
    public CircuitoDTO save(CircuitoDTO circuitoDTO) {
        LOG.debug("Request to save Circuito : {}", circuitoDTO);
        Circuito circuito = circuitoMapper.toEntity(circuitoDTO);
        circuito = circuitoRepository.save(circuito);
        return circuitoMapper.toDto(circuito);
    }

    /**
     * Update a circuito.
     *
     * @param circuitoDTO the entity to save.
     * @return the persisted entity.
     */
    public CircuitoDTO update(CircuitoDTO circuitoDTO) {
        LOG.debug("Request to update Circuito : {}", circuitoDTO);
        Circuito circuito = circuitoMapper.toEntity(circuitoDTO);
        circuito = circuitoRepository.save(circuito);
        return circuitoMapper.toDto(circuito);
    }

    /**
     * Partially update a circuito.
     *
     * @param circuitoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CircuitoDTO> partialUpdate(CircuitoDTO circuitoDTO) {
        LOG.debug("Request to partially update Circuito : {}", circuitoDTO);

        return circuitoRepository
            .findById(circuitoDTO.getId())
            .map(existingCircuito -> {
                circuitoMapper.partialUpdate(existingCircuito, circuitoDTO);

                return existingCircuito;
            })
            .map(circuitoRepository::save)
            .map(circuitoMapper::toDto);
    }

    /**
     * Get one circuito by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CircuitoDTO> findOne(Long id) {
        LOG.debug("Request to get Circuito : {}", id);
        return circuitoRepository.findById(id).map(circuitoMapper::toDto);
    }

    /**
     * Delete the circuito by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Circuito : {}", id);
        circuitoRepository.deleteById(id);
    }
}
