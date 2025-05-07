package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.*; // for static metamodels
import com.oscinnovation.fitness.domain.Gym;
import com.oscinnovation.fitness.repository.GymRepository;
import com.oscinnovation.fitness.service.criteria.GymCriteria;
import com.oscinnovation.fitness.service.dto.GymDTO;
import com.oscinnovation.fitness.service.mapper.GymMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Gym} entities in the database.
 * The main input is a {@link GymCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link GymDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GymQueryService extends QueryService<Gym> {

    private static final Logger LOG = LoggerFactory.getLogger(GymQueryService.class);

    private final GymRepository gymRepository;

    private final GymMapper gymMapper;

    public GymQueryService(GymRepository gymRepository, GymMapper gymMapper) {
        this.gymRepository = gymRepository;
        this.gymMapper = gymMapper;
    }

    /**
     * Return a {@link Page} of {@link GymDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GymDTO> findByCriteria(GymCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Gym> specification = createSpecification(criteria);
        return gymRepository.findAll(specification, page).map(gymMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GymCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Gym> specification = createSpecification(criteria);
        return gymRepository.count(specification);
    }

    /**
     * Function to convert {@link GymCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Gym> createSpecification(GymCriteria criteria) {
        Specification<Gym> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Gym_.id));
            }
            if (criteria.getSets() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSets(), Gym_.sets));
            }
            if (criteria.getReps() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReps(), Gym_.reps));
            }
            if (criteria.getRecupero() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRecupero(), Gym_.recupero));
            }
            if (criteria.getPeso() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeso(), Gym_.peso));
            }
            if (criteria.getCompletato() != null) {
                specification = specification.and(buildSpecification(criteria.getCompletato(), Gym_.completato));
            }
            if (criteria.getSvolto() != null) {
                specification = specification.and(buildSpecification(criteria.getSvolto(), Gym_.svolto));
            }
            if (criteria.getAllenamentoGiornalieroId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAllenamentoGiornalieroId(), root ->
                        root.join(Gym_.allenamentoGiornaliero, JoinType.LEFT).get(AllenamentoGiornaliero_.id)
                    )
                );
            }
            if (criteria.getEsercizioId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getEsercizioId(), root -> root.join(Gym_.esercizio, JoinType.LEFT).get(Esercizio_.id))
                );
            }
        }
        return specification;
    }
}
