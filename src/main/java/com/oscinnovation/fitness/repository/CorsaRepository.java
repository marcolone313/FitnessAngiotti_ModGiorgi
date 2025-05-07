package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.Corsa;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Corsa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorsaRepository extends JpaRepository<Corsa, Long>, JpaSpecificationExecutor<Corsa> {}
