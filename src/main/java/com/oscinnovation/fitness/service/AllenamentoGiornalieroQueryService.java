package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.*; // for static metamodels
import com.oscinnovation.fitness.domain.AllenamentoGiornaliero;
import com.oscinnovation.fitness.repository.AllenamentoGiornalieroRepository;
import com.oscinnovation.fitness.service.criteria.AllenamentoGiornalieroCriteria;
import com.oscinnovation.fitness.service.dto.AllenamentoGiornalieroDTO;
import com.oscinnovation.fitness.service.mapper.AllenamentoGiornalieroMapper;
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
 * Service for executing complex queries for {@link AllenamentoGiornaliero} entities in the database.
 * The main input is a {@link AllenamentoGiornalieroCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AllenamentoGiornalieroDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AllenamentoGiornalieroQueryService extends QueryService<AllenamentoGiornaliero> {

    private static final Logger LOG = LoggerFactory.getLogger(AllenamentoGiornalieroQueryService.class);

    private final AllenamentoGiornalieroRepository allenamentoGiornalieroRepository;

    private final AllenamentoGiornalieroMapper allenamentoGiornalieroMapper;

    public AllenamentoGiornalieroQueryService(
        AllenamentoGiornalieroRepository allenamentoGiornalieroRepository,
        AllenamentoGiornalieroMapper allenamentoGiornalieroMapper
    ) {
        this.allenamentoGiornalieroRepository = allenamentoGiornalieroRepository;
        this.allenamentoGiornalieroMapper = allenamentoGiornalieroMapper;
    }

    /**
     * Return a {@link Page} of {@link AllenamentoGiornalieroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AllenamentoGiornalieroDTO> findByCriteria(AllenamentoGiornalieroCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AllenamentoGiornaliero> specification = createSpecification(criteria);
        return allenamentoGiornalieroRepository.findAll(specification, page).map(allenamentoGiornalieroMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AllenamentoGiornalieroCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AllenamentoGiornaliero> specification = createSpecification(criteria);
        return allenamentoGiornalieroRepository.count(specification);
    }

    /**
     * Function to convert {@link AllenamentoGiornalieroCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AllenamentoGiornaliero> createSpecification(AllenamentoGiornalieroCriteria criteria) {
        Specification<AllenamentoGiornaliero> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AllenamentoGiornaliero_.id));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildSpecification(criteria.getTipo(), AllenamentoGiornaliero_.tipo));
            }
            if (criteria.getGiorno() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGiorno(), AllenamentoGiornaliero_.giorno));
            }
            if (criteria.getSvolto() != null) {
                specification = specification.and(buildSpecification(criteria.getSvolto(), AllenamentoGiornaliero_.svolto));
            }
            if (criteria.getDataAllenamento() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getDataAllenamento(), AllenamentoGiornaliero_.dataAllenamento)
                );
            }
            if (criteria.getSchedaSettimanaleId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSchedaSettimanaleId(), root ->
                        root.join(AllenamentoGiornaliero_.schedaSettimanale, JoinType.LEFT).get(SchedaSettimanale_.id)
                    )
                );
            }
            if (criteria.getCircuitoId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCircuitoId(), root ->
                        root.join(AllenamentoGiornaliero_.circuito, JoinType.LEFT).get(Circuito_.id)
                    )
                );
            }
            if (criteria.getCorsaId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCorsaId(), root -> root.join(AllenamentoGiornaliero_.corsa, JoinType.LEFT).get(Corsa_.id)
                    )
                );
            }
        }
        return specification;
    }
}
