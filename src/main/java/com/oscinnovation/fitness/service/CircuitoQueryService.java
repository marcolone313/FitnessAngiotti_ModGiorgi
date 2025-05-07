package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.*; // for static metamodels
import com.oscinnovation.fitness.domain.Circuito;
import com.oscinnovation.fitness.repository.CircuitoRepository;
import com.oscinnovation.fitness.service.criteria.CircuitoCriteria;
import com.oscinnovation.fitness.service.dto.CircuitoDTO;
import com.oscinnovation.fitness.service.mapper.CircuitoMapper;
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
 * Service for executing complex queries for {@link Circuito} entities in the database.
 * The main input is a {@link CircuitoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CircuitoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CircuitoQueryService extends QueryService<Circuito> {

    private static final Logger LOG = LoggerFactory.getLogger(CircuitoQueryService.class);

    private final CircuitoRepository circuitoRepository;

    private final CircuitoMapper circuitoMapper;

    public CircuitoQueryService(CircuitoRepository circuitoRepository, CircuitoMapper circuitoMapper) {
        this.circuitoRepository = circuitoRepository;
        this.circuitoMapper = circuitoMapper;
    }

    /**
     * Return a {@link Page} of {@link CircuitoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CircuitoDTO> findByCriteria(CircuitoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Circuito> specification = createSpecification(criteria);
        return circuitoRepository.findAll(specification, page).map(circuitoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CircuitoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Circuito> specification = createSpecification(criteria);
        return circuitoRepository.count(specification);
    }

    /**
     * Function to convert {@link CircuitoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Circuito> createSpecification(CircuitoCriteria criteria) {
        Specification<Circuito> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Circuito_.id));
            }
            if (criteria.getTempoLimite() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTempoLimite(), Circuito_.tempoLimite));
            }
            if (criteria.getTempoImpiegato() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTempoImpiegato(), Circuito_.tempoImpiegato));
            }
            if (criteria.getCatenaRipetizioni() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCatenaRipetizioni(), Circuito_.catenaRipetizioni));
            }
            if (criteria.getCircuitiCompletati() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCircuitiCompletati(), Circuito_.circuitiCompletati));
            }
            if (criteria.getSvolto() != null) {
                specification = specification.and(buildSpecification(criteria.getSvolto(), Circuito_.svolto));
            }
            if (criteria.getCompletato() != null) {
                specification = specification.and(buildSpecification(criteria.getCompletato(), Circuito_.completato));
            }
            if (criteria.getAllenamentoGiornalieroId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAllenamentoGiornalieroId(), root ->
                        root.join(Circuito_.allenamentoGiornaliero, JoinType.LEFT).get(AllenamentoGiornaliero_.id)
                    )
                );
            }
            if (criteria.getCircuitoToEsercizioId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCircuitoToEsercizioId(), root ->
                        root.join(Circuito_.circuitoToEsercizios, JoinType.LEFT).get(CircuitoToEsercizio_.id)
                    )
                );
            }
        }
        return specification;
    }
}
