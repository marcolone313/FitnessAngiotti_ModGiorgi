package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.*; // for static metamodels
import com.oscinnovation.fitness.domain.CircuitoToEsercizio;
import com.oscinnovation.fitness.repository.CircuitoToEsercizioRepository;
import com.oscinnovation.fitness.service.criteria.CircuitoToEsercizioCriteria;
import com.oscinnovation.fitness.service.dto.CircuitoToEsercizioDTO;
import com.oscinnovation.fitness.service.mapper.CircuitoToEsercizioMapper;
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
 * Service for executing complex queries for {@link CircuitoToEsercizio} entities in the database.
 * The main input is a {@link CircuitoToEsercizioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CircuitoToEsercizioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CircuitoToEsercizioQueryService extends QueryService<CircuitoToEsercizio> {

    private static final Logger LOG = LoggerFactory.getLogger(CircuitoToEsercizioQueryService.class);

    private final CircuitoToEsercizioRepository circuitoToEsercizioRepository;

    private final CircuitoToEsercizioMapper circuitoToEsercizioMapper;

    public CircuitoToEsercizioQueryService(
        CircuitoToEsercizioRepository circuitoToEsercizioRepository,
        CircuitoToEsercizioMapper circuitoToEsercizioMapper
    ) {
        this.circuitoToEsercizioRepository = circuitoToEsercizioRepository;
        this.circuitoToEsercizioMapper = circuitoToEsercizioMapper;
    }

    /**
     * Return a {@link Page} of {@link CircuitoToEsercizioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CircuitoToEsercizioDTO> findByCriteria(CircuitoToEsercizioCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CircuitoToEsercizio> specification = createSpecification(criteria);
        return circuitoToEsercizioRepository.findAll(specification, page).map(circuitoToEsercizioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CircuitoToEsercizioCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CircuitoToEsercizio> specification = createSpecification(criteria);
        return circuitoToEsercizioRepository.count(specification);
    }

    /**
     * Function to convert {@link CircuitoToEsercizioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CircuitoToEsercizio> createSpecification(CircuitoToEsercizioCriteria criteria) {
        Specification<CircuitoToEsercizio> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CircuitoToEsercizio_.id));
            }
            if (criteria.getReps() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReps(), CircuitoToEsercizio_.reps));
            }
            if (criteria.getEsercizioId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getEsercizioId(), root ->
                        root.join(CircuitoToEsercizio_.esercizio, JoinType.LEFT).get(Esercizio_.id)
                    )
                );
            }
            if (criteria.getCircuitoId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCircuitoId(), root ->
                        root.join(CircuitoToEsercizio_.circuito, JoinType.LEFT).get(Circuito_.id)
                    )
                );
            }
        }
        return specification;
    }
}
