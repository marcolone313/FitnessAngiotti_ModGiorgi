package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.*; // for static metamodels
import com.oscinnovation.fitness.domain.SchedaSettimanale;
import com.oscinnovation.fitness.repository.SchedaSettimanaleRepository;
import com.oscinnovation.fitness.service.criteria.SchedaSettimanaleCriteria;
import com.oscinnovation.fitness.service.dto.SchedaSettimanaleDTO;
import com.oscinnovation.fitness.service.mapper.SchedaSettimanaleMapper;
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
 * Service for executing complex queries for {@link SchedaSettimanale} entities in the database.
 * The main input is a {@link SchedaSettimanaleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SchedaSettimanaleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SchedaSettimanaleQueryService extends QueryService<SchedaSettimanale> {

    private static final Logger LOG = LoggerFactory.getLogger(SchedaSettimanaleQueryService.class);

    private final SchedaSettimanaleRepository schedaSettimanaleRepository;

    private final SchedaSettimanaleMapper schedaSettimanaleMapper;

    public SchedaSettimanaleQueryService(
        SchedaSettimanaleRepository schedaSettimanaleRepository,
        SchedaSettimanaleMapper schedaSettimanaleMapper
    ) {
        this.schedaSettimanaleRepository = schedaSettimanaleRepository;
        this.schedaSettimanaleMapper = schedaSettimanaleMapper;
    }

    /**
     * Return a {@link Page} of {@link SchedaSettimanaleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SchedaSettimanaleDTO> findByCriteria(SchedaSettimanaleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SchedaSettimanale> specification = createSpecification(criteria);
        return schedaSettimanaleRepository.findAll(specification, page).map(schedaSettimanaleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SchedaSettimanaleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SchedaSettimanale> specification = createSpecification(criteria);
        return schedaSettimanaleRepository.count(specification);
    }

    /**
     * Function to convert {@link SchedaSettimanaleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SchedaSettimanale> createSpecification(SchedaSettimanaleCriteria criteria) {
        Specification<SchedaSettimanale> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SchedaSettimanale_.id));
            }
            if (criteria.getAnno() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAnno(), SchedaSettimanale_.anno));
            }
            if (criteria.getMese() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMese(), SchedaSettimanale_.mese));
            }
            if (criteria.getSettimana() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSettimana(), SchedaSettimanale_.settimana));
            }
            if (criteria.getDataCreazione() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataCreazione(), SchedaSettimanale_.dataCreazione));
            }
            if (criteria.getClienteId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getClienteId(), root ->
                        root.join(SchedaSettimanale_.cliente, JoinType.LEFT).get(Cliente_.id)
                    )
                );
            }
            if (criteria.getReportSettimanaleId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getReportSettimanaleId(), root ->
                        root.join(SchedaSettimanale_.reportSettimanale, JoinType.LEFT).get(ReportSettimanale_.id)
                    )
                );
            }
        }
        return specification;
    }
}
