package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.*; // for static metamodels
import com.oscinnovation.fitness.domain.Corsa;
import com.oscinnovation.fitness.repository.CorsaRepository;
import com.oscinnovation.fitness.service.criteria.CorsaCriteria;
import com.oscinnovation.fitness.service.dto.CorsaDTO;
import com.oscinnovation.fitness.service.mapper.CorsaMapper;
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
 * Service for executing complex queries for {@link Corsa} entities in the database.
 * The main input is a {@link CorsaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CorsaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CorsaQueryService extends QueryService<Corsa> {

    private static final Logger LOG = LoggerFactory.getLogger(CorsaQueryService.class);

    private final CorsaRepository corsaRepository;

    private final CorsaMapper corsaMapper;

    public CorsaQueryService(CorsaRepository corsaRepository, CorsaMapper corsaMapper) {
        this.corsaRepository = corsaRepository;
        this.corsaMapper = corsaMapper;
    }

    /**
     * Return a {@link Page} of {@link CorsaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CorsaDTO> findByCriteria(CorsaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Corsa> specification = createSpecification(criteria);
        return corsaRepository.findAll(specification, page).map(corsaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CorsaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Corsa> specification = createSpecification(criteria);
        return corsaRepository.count(specification);
    }

    /**
     * Function to convert {@link CorsaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Corsa> createSpecification(CorsaCriteria criteria) {
        Specification<Corsa> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Corsa_.id));
            }
            if (criteria.getDistanzaDaPercorrere() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDistanzaDaPercorrere(), Corsa_.distanzaDaPercorrere));
            }
            if (criteria.getTempoImpiegato() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTempoImpiegato(), Corsa_.tempoImpiegato));
            }
            if (criteria.getSvolto() != null) {
                specification = specification.and(buildSpecification(criteria.getSvolto(), Corsa_.svolto));
            }
            if (criteria.getCompletato() != null) {
                specification = specification.and(buildSpecification(criteria.getCompletato(), Corsa_.completato));
            }
            if (criteria.getAllenamentoGiornalieroId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAllenamentoGiornalieroId(), root ->
                        root.join(Corsa_.allenamentoGiornaliero, JoinType.LEFT).get(AllenamentoGiornaliero_.id)
                    )
                );
            }
        }
        return specification;
    }
}
