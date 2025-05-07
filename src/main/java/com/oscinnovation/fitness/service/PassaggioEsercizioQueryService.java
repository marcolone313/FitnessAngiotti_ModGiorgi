package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.*; // for static metamodels
import com.oscinnovation.fitness.domain.PassaggioEsercizio;
import com.oscinnovation.fitness.repository.PassaggioEsercizioRepository;
import com.oscinnovation.fitness.service.criteria.PassaggioEsercizioCriteria;
import com.oscinnovation.fitness.service.dto.PassaggioEsercizioDTO;
import com.oscinnovation.fitness.service.mapper.PassaggioEsercizioMapper;
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
 * Service for executing complex queries for {@link PassaggioEsercizio} entities in the database.
 * The main input is a {@link PassaggioEsercizioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PassaggioEsercizioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PassaggioEsercizioQueryService extends QueryService<PassaggioEsercizio> {

    private static final Logger LOG = LoggerFactory.getLogger(PassaggioEsercizioQueryService.class);

    private final PassaggioEsercizioRepository passaggioEsercizioRepository;

    private final PassaggioEsercizioMapper passaggioEsercizioMapper;

    public PassaggioEsercizioQueryService(
        PassaggioEsercizioRepository passaggioEsercizioRepository,
        PassaggioEsercizioMapper passaggioEsercizioMapper
    ) {
        this.passaggioEsercizioRepository = passaggioEsercizioRepository;
        this.passaggioEsercizioMapper = passaggioEsercizioMapper;
    }

    /**
     * Return a {@link Page} of {@link PassaggioEsercizioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PassaggioEsercizioDTO> findByCriteria(PassaggioEsercizioCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PassaggioEsercizio> specification = createSpecification(criteria);
        return passaggioEsercizioRepository.findAll(specification, page).map(passaggioEsercizioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PassaggioEsercizioCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PassaggioEsercizio> specification = createSpecification(criteria);
        return passaggioEsercizioRepository.count(specification);
    }

    /**
     * Function to convert {@link PassaggioEsercizioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PassaggioEsercizio> createSpecification(PassaggioEsercizioCriteria criteria) {
        Specification<PassaggioEsercizio> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PassaggioEsercizio_.id));
            }
            if (criteria.getIndice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIndice(), PassaggioEsercizio_.indice));
            }
            if (criteria.getEsercizioId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getEsercizioId(), root ->
                        root.join(PassaggioEsercizio_.esercizio, JoinType.LEFT).get(Esercizio_.id)
                    )
                );
            }
        }
        return specification;
    }
}
