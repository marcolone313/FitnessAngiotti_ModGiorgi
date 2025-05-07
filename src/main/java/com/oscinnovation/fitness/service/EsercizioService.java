package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.Esercizio;
import com.oscinnovation.fitness.repository.EsercizioRepository;
import com.oscinnovation.fitness.service.dto.EsercizioDTO;
import com.oscinnovation.fitness.service.mapper.EsercizioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oscinnovation.fitness.domain.Esercizio}.
 */
@Service
@Transactional
public class EsercizioService {

    private static final Logger LOG = LoggerFactory.getLogger(EsercizioService.class);

    private final EsercizioRepository esercizioRepository;

    private final EsercizioMapper esercizioMapper;

    public EsercizioService(EsercizioRepository esercizioRepository, EsercizioMapper esercizioMapper) {
        this.esercizioRepository = esercizioRepository;
        this.esercizioMapper = esercizioMapper;
    }

    /**
     * Save a esercizio.
     *
     * @param esercizioDTO the entity to save.
     * @return the persisted entity.
     */
    public EsercizioDTO save(EsercizioDTO esercizioDTO) {
        LOG.debug("Request to save Esercizio : {}", esercizioDTO);
        Esercizio esercizio = esercizioMapper.toEntity(esercizioDTO);
        esercizio = esercizioRepository.save(esercizio);
        return esercizioMapper.toDto(esercizio);
    }

    /**
     * Update a esercizio.
     *
     * @param esercizioDTO the entity to save.
     * @return the persisted entity.
     */
    public EsercizioDTO update(EsercizioDTO esercizioDTO) {
        LOG.debug("Request to update Esercizio : {}", esercizioDTO);
        Esercizio esercizio = esercizioMapper.toEntity(esercizioDTO);
        esercizio = esercizioRepository.save(esercizio);
        return esercizioMapper.toDto(esercizio);
    }

    /**
     * Partially update a esercizio.
     *
     * @param esercizioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EsercizioDTO> partialUpdate(EsercizioDTO esercizioDTO) {
        LOG.debug("Request to partially update Esercizio : {}", esercizioDTO);

        return esercizioRepository
            .findById(esercizioDTO.getId())
            .map(existingEsercizio -> {
                esercizioMapper.partialUpdate(existingEsercizio, esercizioDTO);

                return existingEsercizio;
            })
            .map(esercizioRepository::save)
            .map(esercizioMapper::toDto);
    }

    /**
     * Get one esercizio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EsercizioDTO> findOne(Long id) {
        LOG.debug("Request to get Esercizio : {}", id);
        return esercizioRepository.findById(id).map(esercizioMapper::toDto);
    }

    /**
     * Delete the esercizio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Esercizio : {}", id);
        esercizioRepository.deleteById(id);
    }
}
