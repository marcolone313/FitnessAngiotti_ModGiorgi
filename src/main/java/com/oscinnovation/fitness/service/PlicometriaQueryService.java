package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.*; // for static metamodels
import com.oscinnovation.fitness.domain.Plicometria;
import com.oscinnovation.fitness.repository.PlicometriaRepository;
import com.oscinnovation.fitness.service.criteria.PlicometriaCriteria;
import com.oscinnovation.fitness.service.dto.PlicometriaDTO;
import com.oscinnovation.fitness.service.mapper.PlicometriaMapper;
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
 * Service for executing complex queries for {@link Plicometria} entities in the database.
 * The main input is a {@link PlicometriaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PlicometriaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlicometriaQueryService extends QueryService<Plicometria> {

    private static final Logger LOG = LoggerFactory.getLogger(PlicometriaQueryService.class);

    private final PlicometriaRepository plicometriaRepository;

    private final PlicometriaMapper plicometriaMapper;

    public PlicometriaQueryService(PlicometriaRepository plicometriaRepository, PlicometriaMapper plicometriaMapper) {
        this.plicometriaRepository = plicometriaRepository;
        this.plicometriaMapper = plicometriaMapper;
    }

    /**
     * Return a {@link Page} of {@link PlicometriaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlicometriaDTO> findByCriteria(PlicometriaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Plicometria> specification = createSpecification(criteria);
        return plicometriaRepository.findAll(specification, page).map(plicometriaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlicometriaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Plicometria> specification = createSpecification(criteria);
        return plicometriaRepository.count(specification);
    }

    /**
     * Function to convert {@link PlicometriaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Plicometria> createSpecification(PlicometriaCriteria criteria) {
        Specification<Plicometria> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Plicometria_.id));
            }
            if (criteria.getTricipite() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTricipite(), Plicometria_.tricipite));
            }
            if (criteria.getPetto() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPetto(), Plicometria_.petto));
            }
            if (criteria.getAscella() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAscella(), Plicometria_.ascella));
            }
            if (criteria.getSottoscapolare() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSottoscapolare(), Plicometria_.sottoscapolare));
            }
            if (criteria.getSoprailliaca() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSoprailliaca(), Plicometria_.soprailliaca));
            }
            if (criteria.getAddome() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAddome(), Plicometria_.addome));
            }
            if (criteria.getCoscia() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCoscia(), Plicometria_.coscia));
            }
            if (criteria.getMese() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMese(), Plicometria_.mese));
            }
            if (criteria.getDataInserimento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataInserimento(), Plicometria_.dataInserimento));
            }
            if (criteria.getClienteId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getClienteId(), root -> root.join(Plicometria_.cliente, JoinType.LEFT).get(Cliente_.id))
                );
            }
        }
        return specification;
    }
}
