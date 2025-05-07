package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.*; // for static metamodels
import com.oscinnovation.fitness.domain.Esercizio;
import com.oscinnovation.fitness.repository.EsercizioRepository;
import com.oscinnovation.fitness.service.criteria.EsercizioCriteria;
import com.oscinnovation.fitness.service.dto.EsercizioDTO;
import com.oscinnovation.fitness.service.mapper.EsercizioMapper;
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
 * Service for executing complex queries for {@link Esercizio} entities in the database.
 * The main input is a {@link EsercizioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EsercizioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EsercizioQueryService extends QueryService<Esercizio> {

    private static final Logger LOG = LoggerFactory.getLogger(EsercizioQueryService.class);

    private final EsercizioRepository esercizioRepository;

    private final EsercizioMapper esercizioMapper;

    public EsercizioQueryService(EsercizioRepository esercizioRepository, EsercizioMapper esercizioMapper) {
        this.esercizioRepository = esercizioRepository;
        this.esercizioMapper = esercizioMapper;
    }

    /**
     * Return a {@link Page} of {@link EsercizioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EsercizioDTO> findByCriteria(EsercizioCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Esercizio> specification = createSpecification(criteria);
        return esercizioRepository.findAll(specification, page).map(esercizioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EsercizioCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Esercizio> specification = createSpecification(criteria);
        return esercizioRepository.count(specification);
    }

    /**
     * Function to convert {@link EsercizioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Esercizio> createSpecification(EsercizioCriteria criteria) {
        Specification<Esercizio> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Esercizio_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Esercizio_.nome));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildSpecification(criteria.getTipo(), Esercizio_.tipo));
            }
            if (criteria.getVideoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVideoUrl(), Esercizio_.videoUrl));
            }
            if (criteria.getGymId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getGymId(), root -> root.join(Esercizio_.gyms, JoinType.LEFT).get(Gym_.id))
                );
            }
            if (criteria.getCircuitoToEsercizioId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCircuitoToEsercizioId(), root ->
                        root.join(Esercizio_.circuitoToEsercizios, JoinType.LEFT).get(CircuitoToEsercizio_.id)
                    )
                );
            }
            if (criteria.getPassaggioEsercizioId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPassaggioEsercizioId(), root ->
                        root.join(Esercizio_.passaggioEsercizios, JoinType.LEFT).get(PassaggioEsercizio_.id)
                    )
                );
            }
        }
        return specification;
    }
}
