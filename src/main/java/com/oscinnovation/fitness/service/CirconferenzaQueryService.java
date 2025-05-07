package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.*; // for static metamodels
import com.oscinnovation.fitness.domain.Circonferenza;
import com.oscinnovation.fitness.repository.CirconferenzaRepository;
import com.oscinnovation.fitness.service.criteria.CirconferenzaCriteria;
import com.oscinnovation.fitness.service.dto.CirconferenzaDTO;
import com.oscinnovation.fitness.service.mapper.CirconferenzaMapper;
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
 * Service for executing complex queries for {@link Circonferenza} entities in the database.
 * The main input is a {@link CirconferenzaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CirconferenzaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CirconferenzaQueryService extends QueryService<Circonferenza> {

    private static final Logger LOG = LoggerFactory.getLogger(CirconferenzaQueryService.class);

    private final CirconferenzaRepository circonferenzaRepository;

    private final CirconferenzaMapper circonferenzaMapper;

    public CirconferenzaQueryService(CirconferenzaRepository circonferenzaRepository, CirconferenzaMapper circonferenzaMapper) {
        this.circonferenzaRepository = circonferenzaRepository;
        this.circonferenzaMapper = circonferenzaMapper;
    }

    /**
     * Return a {@link Page} of {@link CirconferenzaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CirconferenzaDTO> findByCriteria(CirconferenzaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Circonferenza> specification = createSpecification(criteria);
        return circonferenzaRepository.findAll(specification, page).map(circonferenzaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CirconferenzaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Circonferenza> specification = createSpecification(criteria);
        return circonferenzaRepository.count(specification);
    }

    /**
     * Function to convert {@link CirconferenzaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Circonferenza> createSpecification(CirconferenzaCriteria criteria) {
        Specification<Circonferenza> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Circonferenza_.id));
            }
            if (criteria.getTorace() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTorace(), Circonferenza_.torace));
            }
            if (criteria.getBraccio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBraccio(), Circonferenza_.braccio));
            }
            if (criteria.getAvambraccio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAvambraccio(), Circonferenza_.avambraccio));
            }
            if (criteria.getOmbelico() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOmbelico(), Circonferenza_.ombelico));
            }
            if (criteria.getFianchi() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFianchi(), Circonferenza_.fianchi));
            }
            if (criteria.getSottoOmbelico() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSottoOmbelico(), Circonferenza_.sottoOmbelico));
            }
            if (criteria.getVita() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVita(), Circonferenza_.vita));
            }
            if (criteria.getCoscia() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCoscia(), Circonferenza_.coscia));
            }
            if (criteria.getMese() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMese(), Circonferenza_.mese));
            }
            if (criteria.getDataInserimento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataInserimento(), Circonferenza_.dataInserimento));
            }
            if (criteria.getClienteId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getClienteId(), root -> root.join(Circonferenza_.cliente, JoinType.LEFT).get(Cliente_.id))
                );
            }
        }
        return specification;
    }
}
