package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.PesoCliente;
import com.oscinnovation.fitness.repository.PesoClienteRepository;
import com.oscinnovation.fitness.service.dto.PesoClienteDTO;
import com.oscinnovation.fitness.service.mapper.PesoClienteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oscinnovation.fitness.domain.PesoCliente}.
 */
@Service
@Transactional
public class PesoClienteService {

    private static final Logger LOG = LoggerFactory.getLogger(PesoClienteService.class);

    private final PesoClienteRepository pesoClienteRepository;

    private final PesoClienteMapper pesoClienteMapper;

    public PesoClienteService(PesoClienteRepository pesoClienteRepository, PesoClienteMapper pesoClienteMapper) {
        this.pesoClienteRepository = pesoClienteRepository;
        this.pesoClienteMapper = pesoClienteMapper;
    }

    /**
     * Save a pesoCliente.
     *
     * @param pesoClienteDTO the entity to save.
     * @return the persisted entity.
     */
    public PesoClienteDTO save(PesoClienteDTO pesoClienteDTO) {
        LOG.debug("Request to save PesoCliente : {}", pesoClienteDTO);
        PesoCliente pesoCliente = pesoClienteMapper.toEntity(pesoClienteDTO);
        pesoCliente = pesoClienteRepository.save(pesoCliente);
        return pesoClienteMapper.toDto(pesoCliente);
    }

    /**
     * Update a pesoCliente.
     *
     * @param pesoClienteDTO the entity to save.
     * @return the persisted entity.
     */
    public PesoClienteDTO update(PesoClienteDTO pesoClienteDTO) {
        LOG.debug("Request to update PesoCliente : {}", pesoClienteDTO);
        PesoCliente pesoCliente = pesoClienteMapper.toEntity(pesoClienteDTO);
        pesoCliente = pesoClienteRepository.save(pesoCliente);
        return pesoClienteMapper.toDto(pesoCliente);
    }

    /**
     * Partially update a pesoCliente.
     *
     * @param pesoClienteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PesoClienteDTO> partialUpdate(PesoClienteDTO pesoClienteDTO) {
        LOG.debug("Request to partially update PesoCliente : {}", pesoClienteDTO);

        return pesoClienteRepository
            .findById(pesoClienteDTO.getId())
            .map(existingPesoCliente -> {
                pesoClienteMapper.partialUpdate(existingPesoCliente, pesoClienteDTO);

                return existingPesoCliente;
            })
            .map(pesoClienteRepository::save)
            .map(pesoClienteMapper::toDto);
    }

    /**
     * Get all the pesoClientes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PesoClienteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pesoClienteRepository.findAllWithEagerRelationships(pageable).map(pesoClienteMapper::toDto);
    }

    /**
     * Get one pesoCliente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PesoClienteDTO> findOne(Long id) {
        LOG.debug("Request to get PesoCliente : {}", id);
        return pesoClienteRepository.findOneWithEagerRelationships(id).map(pesoClienteMapper::toDto);
    }

    /**
     * Delete the pesoCliente by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PesoCliente : {}", id);
        pesoClienteRepository.deleteById(id);
    }
}
