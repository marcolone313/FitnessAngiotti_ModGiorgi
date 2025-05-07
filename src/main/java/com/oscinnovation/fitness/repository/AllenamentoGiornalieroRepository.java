package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.AllenamentoGiornaliero;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AllenamentoGiornaliero entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AllenamentoGiornalieroRepository
    extends JpaRepository<AllenamentoGiornaliero, Long>, JpaSpecificationExecutor<AllenamentoGiornaliero> {}
