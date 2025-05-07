package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.Plicometria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Plicometria entity.
 */
@Repository
public interface PlicometriaRepository extends JpaRepository<Plicometria, Long>, JpaSpecificationExecutor<Plicometria> {
    default Optional<Plicometria> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Plicometria> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Plicometria> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select plicometria from Plicometria plicometria left join fetch plicometria.cliente",
        countQuery = "select count(plicometria) from Plicometria plicometria"
    )
    Page<Plicometria> findAllWithToOneRelationships(Pageable pageable);

    @Query("select plicometria from Plicometria plicometria left join fetch plicometria.cliente")
    List<Plicometria> findAllWithToOneRelationships();

    @Query("select plicometria from Plicometria plicometria left join fetch plicometria.cliente where plicometria.id =:id")
    Optional<Plicometria> findOneWithToOneRelationships(@Param("id") Long id);
}
