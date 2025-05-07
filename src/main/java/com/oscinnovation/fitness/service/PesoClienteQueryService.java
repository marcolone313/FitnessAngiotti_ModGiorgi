package com.oscinnovation.fitness.service;

import com.oscinnovation.fitness.domain.*; // for static metamodels
import com.oscinnovation.fitness.domain.PesoCliente;
import com.oscinnovation.fitness.repository.PesoClienteRepository;
import com.oscinnovation.fitness.service.criteria.PesoClienteCriteria;
import com.oscinnovation.fitness.service.dto.PesoClienteDTO;
import com.oscinnovation.fitness.service.mapper.PesoClienteMapper;
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
 * Service for executing complex queries for {@link PesoCliente} entities in the database.
 * The main input is a {@link PesoClienteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PesoClienteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PesoClienteQueryService extends QueryService<PesoCliente> {

    private static final Logger LOG = LoggerFactory.getLogger(PesoClienteQueryService.class);

    private final PesoClienteRepository pesoClienteRepository;

    private final PesoClienteMapper pesoClienteMapper;

    public PesoClienteQueryService(PesoClienteRepository pesoClienteRepository, PesoClienteMapper pesoClienteMapper) {
        this.pesoClienteRepository = pesoClienteRepository;
        this.pesoClienteMapper = pesoClienteMapper;
    }

    /**
     * Return a {@link Page} of {@link PesoClienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PesoClienteDTO> findByCriteria(PesoClienteCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PesoCliente> specification = createSpecification(criteria);
        return pesoClienteRepository.findAll(specification, page).map(pesoClienteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PesoClienteCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PesoCliente> specification = createSpecification(criteria);
        return pesoClienteRepository.count(specification);
    }

    /**
     * Function to convert {@link PesoClienteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PesoCliente> createSpecification(PesoClienteCriteria criteria) {
        Specification<PesoCliente> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PesoCliente_.id));
            }
            if (criteria.getPeso() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeso(), PesoCliente_.peso));
            }
            if (criteria.getMese() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMese(), PesoCliente_.mese));
            }
            if (criteria.getDataInserimento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataInserimento(), PesoCliente_.dataInserimento));
            }
            if (criteria.getClienteId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getClienteId(), root -> root.join(PesoCliente_.cliente, JoinType.LEFT).get(Cliente_.id))
                );
            }
        }
        return specification;
    }
}
