package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.CorsoAcademy;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CorsoAcademy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorsoAcademyRepository extends JpaRepository<CorsoAcademy, Long>, JpaSpecificationExecutor<CorsoAcademy> {}
