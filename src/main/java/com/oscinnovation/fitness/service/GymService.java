package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.Gym;
import com.oscinnovation.fitness.repository.GymRepository;
import com.oscinnovation.fitness.service.dto.GymDTO;
import com.oscinnovation.fitness.service.mapper.GymMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oscinnovation.fitness.domain.Gym}.
 */
@Service
@Transactional
public class GymService {

    private static final Logger LOG = LoggerFactory.getLogger(GymService.class);

    private final GymRepository gymRepository;

    private final GymMapper gymMapper;

    public GymService(GymRepository gymRepository, GymMapper gymMapper) {
        this.gymRepository = gymRepository;
        this.gymMapper = gymMapper;
    }

    /**
     * Save a gym.
     *
     * @param gymDTO the entity to save.
     * @return the persisted entity.
     */
    public GymDTO save(GymDTO gymDTO) {
        LOG.debug("Request to save Gym : {}", gymDTO);
        Gym gym = gymMapper.toEntity(gymDTO);
        gym = gymRepository.save(gym);
        return gymMapper.toDto(gym);
    }

    /**
     * Update a gym.
     *
     * @param gymDTO the entity to save.
     * @return the persisted entity.
     */
    public GymDTO update(GymDTO gymDTO) {
        LOG.debug("Request to update Gym : {}", gymDTO);
        Gym gym = gymMapper.toEntity(gymDTO);
        gym = gymRepository.save(gym);
        return gymMapper.toDto(gym);
    }

    /**
     * Partially update a gym.
     *
     * @param gymDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GymDTO> partialUpdate(GymDTO gymDTO) {
        LOG.debug("Request to partially update Gym : {}", gymDTO);

        return gymRepository
            .findById(gymDTO.getId())
            .map(existingGym -> {
                gymMapper.partialUpdate(existingGym, gymDTO);

                return existingGym;
            })
            .map(gymRepository::save)
            .map(gymMapper::toDto);
    }

    /**
     * Get all the gyms with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<GymDTO> findAllWithEagerRelationships(Pageable pageable) {
        return gymRepository.findAllWithEagerRelationships(pageable).map(gymMapper::toDto);
    }

    /**
     * Get one gym by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GymDTO> findOne(Long id) {
        LOG.debug("Request to get Gym : {}", id);
        return gymRepository.findOneWithEagerRelationships(id).map(gymMapper::toDto);
    }

    /**
     * Delete the gym by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Gym : {}", id);
        gymRepository.deleteById(id);
    }
}
