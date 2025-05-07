package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.*; // for static metamodels
import com.oscinnovation.fitness.domain.CorsoAcademy;
import com.oscinnovation.fitness.repository.CorsoAcademyRepository;
import com.oscinnovation.fitness.service.criteria.CorsoAcademyCriteria;
import com.oscinnovation.fitness.service.dto.CorsoAcademyDTO;
import com.oscinnovation.fitness.service.mapper.CorsoAcademyMapper;
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
 * Service for executing complex queries for {@link CorsoAcademy} entities in the database.
 * The main input is a {@link CorsoAcademyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CorsoAcademyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CorsoAcademyQueryService extends QueryService<CorsoAcademy> {

    private static final Logger LOG = LoggerFactory.getLogger(CorsoAcademyQueryService.class);

    private final CorsoAcademyRepository corsoAcademyRepository;

    private final CorsoAcademyMapper corsoAcademyMapper;

    public CorsoAcademyQueryService(CorsoAcademyRepository corsoAcademyRepository, CorsoAcademyMapper corsoAcademyMapper) {
        this.corsoAcademyRepository = corsoAcademyRepository;
        this.corsoAcademyMapper = corsoAcademyMapper;
    }

    /**
     * Return a {@link Page} of {@link CorsoAcademyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CorsoAcademyDTO> findByCriteria(CorsoAcademyCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CorsoAcademy> specification = createSpecification(criteria);
        return corsoAcademyRepository.findAll(specification, page).map(corsoAcademyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CorsoAcademyCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CorsoAcademy> specification = createSpecification(criteria);
        return corsoAcademyRepository.count(specification);
    }

    /**
     * Function to convert {@link CorsoAcademyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CorsoAcademy> createSpecification(CorsoAcademyCriteria criteria) {
        Specification<CorsoAcademy> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CorsoAcademy_.id));
            }
            if (criteria.getLivello() != null) {
                specification = specification.and(buildSpecification(criteria.getLivello(), CorsoAcademy_.livello));
            }
            if (criteria.getTitolo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitolo(), CorsoAcademy_.titolo));
            }
            if (criteria.getLezioneCorsoId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getLezioneCorsoId(), root ->
                        root.join(CorsoAcademy_.lezioneCorsos, JoinType.LEFT).get(LezioneCorso_.id)
                    )
                );
            }
        }
        return specification;
    }
}
