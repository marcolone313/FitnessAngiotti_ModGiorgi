package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.AllenamentoGiornaliero;
import com.oscinnovation.fitness.repository.AllenamentoGiornalieroRepository;
import com.oscinnovation.fitness.service.dto.AllenamentoGiornalieroDTO;
import com.oscinnovation.fitness.service.mapper.AllenamentoGiornalieroMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oscinnovation.fitness.domain.AllenamentoGiornaliero}.
 */
@Service
@Transactional
public class AllenamentoGiornalieroService {

    private static final Logger LOG = LoggerFactory.getLogger(AllenamentoGiornalieroService.class);

    private final AllenamentoGiornalieroRepository allenamentoGiornalieroRepository;

    private final AllenamentoGiornalieroMapper allenamentoGiornalieroMapper;

    public AllenamentoGiornalieroService(
        AllenamentoGiornalieroRepository allenamentoGiornalieroRepository,
        AllenamentoGiornalieroMapper allenamentoGiornalieroMapper
    ) {
        this.allenamentoGiornalieroRepository = allenamentoGiornalieroRepository;
        this.allenamentoGiornalieroMapper = allenamentoGiornalieroMapper;
    }

    /**
     * Save a allenamentoGiornaliero.
     *
     * @param allenamentoGiornalieroDTO the entity to save.
     * @return the persisted entity.
     */
    public AllenamentoGiornalieroDTO save(AllenamentoGiornalieroDTO allenamentoGiornalieroDTO) {
        LOG.debug("Request to save AllenamentoGiornaliero : {}", allenamentoGiornalieroDTO);
        AllenamentoGiornaliero allenamentoGiornaliero = allenamentoGiornalieroMapper.toEntity(allenamentoGiornalieroDTO);
        allenamentoGiornaliero = allenamentoGiornalieroRepository.save(allenamentoGiornaliero);
        return allenamentoGiornalieroMapper.toDto(allenamentoGiornaliero);
    }

    /**
     * Update a allenamentoGiornaliero.
     *
     * @param allenamentoGiornalieroDTO the entity to save.
     * @return the persisted entity.
     */
    public AllenamentoGiornalieroDTO update(AllenamentoGiornalieroDTO allenamentoGiornalieroDTO) {
        LOG.debug("Request to update AllenamentoGiornaliero : {}", allenamentoGiornalieroDTO);
        AllenamentoGiornaliero allenamentoGiornaliero = allenamentoGiornalieroMapper.toEntity(allenamentoGiornalieroDTO);
        allenamentoGiornaliero = allenamentoGiornalieroRepository.save(allenamentoGiornaliero);
        return allenamentoGiornalieroMapper.toDto(allenamentoGiornaliero);
    }

    /**
     * Partially update a allenamentoGiornaliero.
     *
     * @param allenamentoGiornalieroDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AllenamentoGiornalieroDTO> partialUpdate(AllenamentoGiornalieroDTO allenamentoGiornalieroDTO) {
        LOG.debug("Request to partially update AllenamentoGiornaliero : {}", allenamentoGiornalieroDTO);

        return allenamentoGiornalieroRepository
            .findById(allenamentoGiornalieroDTO.getId())
            .map(existingAllenamentoGiornaliero -> {
                allenamentoGiornalieroMapper.partialUpdate(existingAllenamentoGiornaliero, allenamentoGiornalieroDTO);

                return existingAllenamentoGiornaliero;
            })
            .map(allenamentoGiornalieroRepository::save)
            .map(allenamentoGiornalieroMapper::toDto);
    }

    /**
     *  Get all the allenamentoGiornalieros where Circuito is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AllenamentoGiornalieroDTO> findAllWhereCircuitoIsNull() {
        LOG.debug("Request to get all allenamentoGiornalieros where Circuito is null");
        return StreamSupport.stream(allenamentoGiornalieroRepository.findAll().spliterator(), false)
            .filter(allenamentoGiornaliero -> allenamentoGiornaliero.getCircuito() == null)
            .map(allenamentoGiornalieroMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the allenamentoGiornalieros where Corsa is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AllenamentoGiornalieroDTO> findAllWhereCorsaIsNull() {
        LOG.debug("Request to get all allenamentoGiornalieros where Corsa is null");
        return StreamSupport.stream(allenamentoGiornalieroRepository.findAll().spliterator(), false)
            .filter(allenamentoGiornaliero -> allenamentoGiornaliero.getCorsa() == null)
            .map(allenamentoGiornalieroMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one allenamentoGiornaliero by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AllenamentoGiornalieroDTO> findOne(Long id) {
        LOG.debug("Request to get AllenamentoGiornaliero : {}", id);
        return allenamentoGiornalieroRepository.findById(id).map(allenamentoGiornalieroMapper::toDto);
    }

    /**
     * Delete the allenamentoGiornaliero by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AllenamentoGiornaliero : {}", id);
        allenamentoGiornalieroRepository.deleteById(id);
    }
}
