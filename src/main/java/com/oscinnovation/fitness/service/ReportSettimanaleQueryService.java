package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.*; // for static metamodels
import com.oscinnovation.fitness.domain.ReportSettimanale;
import com.oscinnovation.fitness.repository.ReportSettimanaleRepository;
import com.oscinnovation.fitness.service.criteria.ReportSettimanaleCriteria;
import com.oscinnovation.fitness.service.dto.ReportSettimanaleDTO;
import com.oscinnovation.fitness.service.mapper.ReportSettimanaleMapper;
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
 * Service for executing complex queries for {@link ReportSettimanale} entities in the database.
 * The main input is a {@link ReportSettimanaleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReportSettimanaleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportSettimanaleQueryService extends QueryService<ReportSettimanale> {

    private static final Logger LOG = LoggerFactory.getLogger(ReportSettimanaleQueryService.class);

    private final ReportSettimanaleRepository reportSettimanaleRepository;

    private final ReportSettimanaleMapper reportSettimanaleMapper;

    public ReportSettimanaleQueryService(
        ReportSettimanaleRepository reportSettimanaleRepository,
        ReportSettimanaleMapper reportSettimanaleMapper
    ) {
        this.reportSettimanaleRepository = reportSettimanaleRepository;
        this.reportSettimanaleMapper = reportSettimanaleMapper;
    }

    /**
     * Return a {@link Page} of {@link ReportSettimanaleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportSettimanaleDTO> findByCriteria(ReportSettimanaleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReportSettimanale> specification = createSpecification(criteria);
        return reportSettimanaleRepository.findAll(specification, page).map(reportSettimanaleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportSettimanaleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReportSettimanale> specification = createSpecification(criteria);
        return reportSettimanaleRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportSettimanaleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReportSettimanale> createSpecification(ReportSettimanaleCriteria criteria) {
        Specification<ReportSettimanale> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReportSettimanale_.id));
            }
            if (criteria.getVoto() != null) {
                specification = specification.and(buildSpecification(criteria.getVoto(), ReportSettimanale_.voto));
            }
            if (criteria.getGiorniDieta() != null) {
                specification = specification.and(buildSpecification(criteria.getGiorniDieta(), ReportSettimanale_.giorniDieta));
            }
            if (criteria.getPesoMedio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPesoMedio(), ReportSettimanale_.pesoMedio));
            }
            if (criteria.getQualitaSonno() != null) {
                specification = specification.and(buildSpecification(criteria.getQualitaSonno(), ReportSettimanale_.qualitaSonno));
            }
            if (criteria.getMediaOreSonno() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMediaOreSonno(), ReportSettimanale_.mediaOreSonno));
            }
            if (criteria.getDataCreazione() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataCreazione(), ReportSettimanale_.dataCreazione));
            }
            if (criteria.getDataScadenza() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataScadenza(), ReportSettimanale_.dataScadenza));
            }
            if (criteria.getDataCompletamento() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getDataCompletamento(), ReportSettimanale_.dataCompletamento)
                );
            }
            if (criteria.getPuntuale() != null) {
                specification = specification.and(buildSpecification(criteria.getPuntuale(), ReportSettimanale_.puntuale));
            }
            if (criteria.getSchedaSettimanaleId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSchedaSettimanaleId(), root ->
                        root.join(ReportSettimanale_.schedaSettimanale, JoinType.LEFT).get(SchedaSettimanale_.id)
                    )
                );
            }
            if (criteria.getClienteId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getClienteId(), root ->
                        root.join(ReportSettimanale_.cliente, JoinType.LEFT).get(Cliente_.id)
                    )
                );
            }
        }
        return specification;
    }
}
