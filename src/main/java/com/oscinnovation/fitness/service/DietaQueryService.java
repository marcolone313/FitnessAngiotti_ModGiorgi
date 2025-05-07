package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.*; // for static metamodels
import com.oscinnovation.fitness.domain.Dieta;
import com.oscinnovation.fitness.repository.DietaRepository;
import com.oscinnovation.fitness.service.criteria.DietaCriteria;
import com.oscinnovation.fitness.service.dto.DietaDTO;
import com.oscinnovation.fitness.service.mapper.DietaMapper;
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
 * Service for executing complex queries for {@link Dieta} entities in the database.
 * The main input is a {@link DietaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DietaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DietaQueryService extends QueryService<Dieta> {

    private static final Logger LOG = LoggerFactory.getLogger(DietaQueryService.class);

    private final DietaRepository dietaRepository;

    private final DietaMapper dietaMapper;

    public DietaQueryService(DietaRepository dietaRepository, DietaMapper dietaMapper) {
        this.dietaRepository = dietaRepository;
        this.dietaMapper = dietaMapper;
    }

    /**
     * Return a {@link Page} of {@link DietaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DietaDTO> findByCriteria(DietaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Dieta> specification = createSpecification(criteria);
        return dietaRepository.findAll(specification, page).map(dietaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DietaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Dieta> specification = createSpecification(criteria);
        return dietaRepository.count(specification);
    }

    /**
     * Function to convert {@link DietaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Dieta> createSpecification(DietaCriteria criteria) {
        Specification<Dieta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Dieta_.id));
            }
            if (criteria.getDataCreazione() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataCreazione(), Dieta_.dataCreazione));
            }
            if (criteria.getMese() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMese(), Dieta_.mese));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTipo(), Dieta_.tipo));
            }
            if (criteria.getMacros() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMacros(), Dieta_.macros));
            }
            if (criteria.getFabbisognoCalorico() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFabbisognoCalorico(), Dieta_.fabbisognoCalorico));
            }
            if (criteria.getClienteId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getClienteId(), root -> root.join(Dieta_.cliente, JoinType.LEFT).get(Cliente_.id))
                );
            }
        }
        return specification;
    }
}
