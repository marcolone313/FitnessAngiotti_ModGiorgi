package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.*; // for static metamodels
import com.oscinnovation.fitness.domain.LezioneCorso;
import com.oscinnovation.fitness.repository.LezioneCorsoRepository;
import com.oscinnovation.fitness.service.criteria.LezioneCorsoCriteria;
import com.oscinnovation.fitness.service.dto.LezioneCorsoDTO;
import com.oscinnovation.fitness.service.mapper.LezioneCorsoMapper;
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
 * Service for executing complex queries for {@link LezioneCorso} entities in the database.
 * The main input is a {@link LezioneCorsoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link LezioneCorsoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LezioneCorsoQueryService extends QueryService<LezioneCorso> {

    private static final Logger LOG = LoggerFactory.getLogger(LezioneCorsoQueryService.class);

    private final LezioneCorsoRepository lezioneCorsoRepository;

    private final LezioneCorsoMapper lezioneCorsoMapper;

    public LezioneCorsoQueryService(LezioneCorsoRepository lezioneCorsoRepository, LezioneCorsoMapper lezioneCorsoMapper) {
        this.lezioneCorsoRepository = lezioneCorsoRepository;
        this.lezioneCorsoMapper = lezioneCorsoMapper;
    }

    /**
     * Return a {@link Page} of {@link LezioneCorsoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LezioneCorsoDTO> findByCriteria(LezioneCorsoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LezioneCorso> specification = createSpecification(criteria);
        return lezioneCorsoRepository.findAll(specification, page).map(lezioneCorsoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LezioneCorsoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<LezioneCorso> specification = createSpecification(criteria);
        return lezioneCorsoRepository.count(specification);
    }

    /**
     * Function to convert {@link LezioneCorsoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LezioneCorso> createSpecification(LezioneCorsoCriteria criteria) {
        Specification<LezioneCorso> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LezioneCorso_.id));
            }
            if (criteria.getTitolo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitolo(), LezioneCorso_.titolo));
            }
            if (criteria.getVideoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVideoUrl(), LezioneCorso_.videoUrl));
            }
            if (criteria.getCorsoAcademyId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCorsoAcademyId(), root ->
                        root.join(LezioneCorso_.corsoAcademy, JoinType.LEFT).get(CorsoAcademy_.id)
                    )
                );
            }
        }
        return specification;
    }
}
