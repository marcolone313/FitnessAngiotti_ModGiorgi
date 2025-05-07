package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.LezioneCorso;
import com.oscinnovation.fitness.repository.LezioneCorsoRepository;
import com.oscinnovation.fitness.service.dto.LezioneCorsoDTO;
import com.oscinnovation.fitness.service.mapper.LezioneCorsoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oscinnovation.fitness.domain.LezioneCorso}.
 */
@Service
@Transactional
public class LezioneCorsoService {

    private static final Logger LOG = LoggerFactory.getLogger(LezioneCorsoService.class);

    private final LezioneCorsoRepository lezioneCorsoRepository;

    private final LezioneCorsoMapper lezioneCorsoMapper;

    public LezioneCorsoService(LezioneCorsoRepository lezioneCorsoRepository, LezioneCorsoMapper lezioneCorsoMapper) {
        this.lezioneCorsoRepository = lezioneCorsoRepository;
        this.lezioneCorsoMapper = lezioneCorsoMapper;
    }

    /**
     * Save a lezioneCorso.
     *
     * @param lezioneCorsoDTO the entity to save.
     * @return the persisted entity.
     */
    public LezioneCorsoDTO save(LezioneCorsoDTO lezioneCorsoDTO) {
        LOG.debug("Request to save LezioneCorso : {}", lezioneCorsoDTO);
        LezioneCorso lezioneCorso = lezioneCorsoMapper.toEntity(lezioneCorsoDTO);
        lezioneCorso = lezioneCorsoRepository.save(lezioneCorso);
        return lezioneCorsoMapper.toDto(lezioneCorso);
    }

    /**
     * Update a lezioneCorso.
     *
     * @param lezioneCorsoDTO the entity to save.
     * @return the persisted entity.
     */
    public LezioneCorsoDTO update(LezioneCorsoDTO lezioneCorsoDTO) {
        LOG.debug("Request to update LezioneCorso : {}", lezioneCorsoDTO);
        LezioneCorso lezioneCorso = lezioneCorsoMapper.toEntity(lezioneCorsoDTO);
        lezioneCorso = lezioneCorsoRepository.save(lezioneCorso);
        return lezioneCorsoMapper.toDto(lezioneCorso);
    }

    /**
     * Partially update a lezioneCorso.
     *
     * @param lezioneCorsoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LezioneCorsoDTO> partialUpdate(LezioneCorsoDTO lezioneCorsoDTO) {
        LOG.debug("Request to partially update LezioneCorso : {}", lezioneCorsoDTO);

        return lezioneCorsoRepository
            .findById(lezioneCorsoDTO.getId())
            .map(existingLezioneCorso -> {
                lezioneCorsoMapper.partialUpdate(existingLezioneCorso, lezioneCorsoDTO);

                return existingLezioneCorso;
            })
            .map(lezioneCorsoRepository::save)
            .map(lezioneCorsoMapper::toDto);
    }

    /**
     * Get all the lezioneCorsos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<LezioneCorsoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return lezioneCorsoRepository.findAllWithEagerRelationships(pageable).map(lezioneCorsoMapper::toDto);
    }

    /**
     * Get one lezioneCorso by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LezioneCorsoDTO> findOne(Long id) {
        LOG.debug("Request to get LezioneCorso : {}", id);
        return lezioneCorsoRepository.findOneWithEagerRelationships(id).map(lezioneCorsoMapper::toDto);
    }

    /**
     * Delete the lezioneCorso by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete LezioneCorso : {}", id);
        lezioneCorsoRepository.deleteById(id);
    }
}
