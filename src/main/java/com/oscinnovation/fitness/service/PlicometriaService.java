package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.Plicometria;
import com.oscinnovation.fitness.repository.PlicometriaRepository;
import com.oscinnovation.fitness.service.dto.PlicometriaDTO;
import com.oscinnovation.fitness.service.mapper.PlicometriaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oscinnovation.fitness.domain.Plicometria}.
 */
@Service
@Transactional
public class PlicometriaService {

    private static final Logger LOG = LoggerFactory.getLogger(PlicometriaService.class);

    private final PlicometriaRepository plicometriaRepository;

    private final PlicometriaMapper plicometriaMapper;

    public PlicometriaService(PlicometriaRepository plicometriaRepository, PlicometriaMapper plicometriaMapper) {
        this.plicometriaRepository = plicometriaRepository;
        this.plicometriaMapper = plicometriaMapper;
    }

    /**
     * Save a plicometria.
     *
     * @param plicometriaDTO the entity to save.
     * @return the persisted entity.
     */
    public PlicometriaDTO save(PlicometriaDTO plicometriaDTO) {
        LOG.debug("Request to save Plicometria : {}", plicometriaDTO);
        Plicometria plicometria = plicometriaMapper.toEntity(plicometriaDTO);
        plicometria = plicometriaRepository.save(plicometria);
        return plicometriaMapper.toDto(plicometria);
    }

    /**
     * Update a plicometria.
     *
     * @param plicometriaDTO the entity to save.
     * @return the persisted entity.
     */
    public PlicometriaDTO update(PlicometriaDTO plicometriaDTO) {
        LOG.debug("Request to update Plicometria : {}", plicometriaDTO);
        Plicometria plicometria = plicometriaMapper.toEntity(plicometriaDTO);
        plicometria = plicometriaRepository.save(plicometria);
        return plicometriaMapper.toDto(plicometria);
    }

    /**
     * Partially update a plicometria.
     *
     * @param plicometriaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PlicometriaDTO> partialUpdate(PlicometriaDTO plicometriaDTO) {
        LOG.debug("Request to partially update Plicometria : {}", plicometriaDTO);

        return plicometriaRepository
            .findById(plicometriaDTO.getId())
            .map(existingPlicometria -> {
                plicometriaMapper.partialUpdate(existingPlicometria, plicometriaDTO);

                return existingPlicometria;
            })
            .map(plicometriaRepository::save)
            .map(plicometriaMapper::toDto);
    }

    /**
     * Get all the plicometrias with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PlicometriaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return plicometriaRepository.findAllWithEagerRelationships(pageable).map(plicometriaMapper::toDto);
    }

    /**
     * Get one plicometria by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlicometriaDTO> findOne(Long id) {
        LOG.debug("Request to get Plicometria : {}", id);
        return plicometriaRepository.findOneWithEagerRelationships(id).map(plicometriaMapper::toDto);
    }

    /**
     * Delete the plicometria by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Plicometria : {}", id);
        plicometriaRepository.deleteById(id);
    }
}
