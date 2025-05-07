package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.ClienteToLezioneCorso;
import com.oscinnovation.fitness.repository.ClienteToLezioneCorsoRepository;
import com.oscinnovation.fitness.service.dto.ClienteToLezioneCorsoDTO;
import com.oscinnovation.fitness.service.mapper.ClienteToLezioneCorsoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oscinnovation.fitness.domain.ClienteToLezioneCorso}.
 */
@Service
@Transactional
public class ClienteToLezioneCorsoService {

    private static final Logger LOG = LoggerFactory.getLogger(ClienteToLezioneCorsoService.class);

    private final ClienteToLezioneCorsoRepository clienteToLezioneCorsoRepository;

    private final ClienteToLezioneCorsoMapper clienteToLezioneCorsoMapper;

    public ClienteToLezioneCorsoService(
        ClienteToLezioneCorsoRepository clienteToLezioneCorsoRepository,
        ClienteToLezioneCorsoMapper clienteToLezioneCorsoMapper
    ) {
        this.clienteToLezioneCorsoRepository = clienteToLezioneCorsoRepository;
        this.clienteToLezioneCorsoMapper = clienteToLezioneCorsoMapper;
    }

    /**
     * Save a clienteToLezioneCorso.
     *
     * @param clienteToLezioneCorsoDTO the entity to save.
     * @return the persisted entity.
     */
    public ClienteToLezioneCorsoDTO save(ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO) {
        LOG.debug("Request to save ClienteToLezioneCorso : {}", clienteToLezioneCorsoDTO);
        ClienteToLezioneCorso clienteToLezioneCorso = clienteToLezioneCorsoMapper.toEntity(clienteToLezioneCorsoDTO);
        clienteToLezioneCorso = clienteToLezioneCorsoRepository.save(clienteToLezioneCorso);
        return clienteToLezioneCorsoMapper.toDto(clienteToLezioneCorso);
    }

    /**
     * Update a clienteToLezioneCorso.
     *
     * @param clienteToLezioneCorsoDTO the entity to save.
     * @return the persisted entity.
     */
    public ClienteToLezioneCorsoDTO update(ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO) {
        LOG.debug("Request to update ClienteToLezioneCorso : {}", clienteToLezioneCorsoDTO);
        ClienteToLezioneCorso clienteToLezioneCorso = clienteToLezioneCorsoMapper.toEntity(clienteToLezioneCorsoDTO);
        clienteToLezioneCorso = clienteToLezioneCorsoRepository.save(clienteToLezioneCorso);
        return clienteToLezioneCorsoMapper.toDto(clienteToLezioneCorso);
    }

    /**
     * Partially update a clienteToLezioneCorso.
     *
     * @param clienteToLezioneCorsoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ClienteToLezioneCorsoDTO> partialUpdate(ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO) {
        LOG.debug("Request to partially update ClienteToLezioneCorso : {}", clienteToLezioneCorsoDTO);

        return clienteToLezioneCorsoRepository
            .findById(clienteToLezioneCorsoDTO.getId())
            .map(existingClienteToLezioneCorso -> {
                clienteToLezioneCorsoMapper.partialUpdate(existingClienteToLezioneCorso, clienteToLezioneCorsoDTO);

                return existingClienteToLezioneCorso;
            })
            .map(clienteToLezioneCorsoRepository::save)
            .map(clienteToLezioneCorsoMapper::toDto);
    }

    /**
     * Get all the clienteToLezioneCorsos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ClienteToLezioneCorsoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return clienteToLezioneCorsoRepository.findAllWithEagerRelationships(pageable).map(clienteToLezioneCorsoMapper::toDto);
    }

    /**
     * Get one clienteToLezioneCorso by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClienteToLezioneCorsoDTO> findOne(Long id) {
        LOG.debug("Request to get ClienteToLezioneCorso : {}", id);
        return clienteToLezioneCorsoRepository.findOneWithEagerRelationships(id).map(clienteToLezioneCorsoMapper::toDto);
    }

    /**
     * Delete the clienteToLezioneCorso by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ClienteToLezioneCorso : {}", id);
        clienteToLezioneCorsoRepository.deleteById(id);
    }
}
