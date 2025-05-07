package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.Circuito;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Circuito entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CircuitoRepository extends JpaRepository<Circuito, Long>, JpaSpecificationExecutor<Circuito> {}
