package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.Dieta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Dieta entity.
 */
@Repository
public interface DietaRepository extends JpaRepository<Dieta, Long>, JpaSpecificationExecutor<Dieta> {
    default Optional<Dieta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Dieta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Dieta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select dieta from Dieta dieta left join fetch dieta.cliente", countQuery = "select count(dieta) from Dieta dieta")
    Page<Dieta> findAllWithToOneRelationships(Pageable pageable);

    @Query("select dieta from Dieta dieta left join fetch dieta.cliente")
    List<Dieta> findAllWithToOneRelationships();

    @Query("select dieta from Dieta dieta left join fetch dieta.cliente where dieta.id =:id")
    Optional<Dieta> findOneWithToOneRelationships(@Param("id") Long id);
}
