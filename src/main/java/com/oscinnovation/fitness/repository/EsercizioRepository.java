package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.Esercizio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Esercizio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EsercizioRepository extends JpaRepository<Esercizio, Long>, JpaSpecificationExecutor<Esercizio> {}
