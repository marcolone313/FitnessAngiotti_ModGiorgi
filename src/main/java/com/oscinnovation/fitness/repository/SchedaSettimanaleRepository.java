package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.SchedaSettimanale;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SchedaSettimanale entity.
 */
@Repository
public interface SchedaSettimanaleRepository extends JpaRepository<SchedaSettimanale, Long>, JpaSpecificationExecutor<SchedaSettimanale> {
    default Optional<SchedaSettimanale> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SchedaSettimanale> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SchedaSettimanale> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select schedaSettimanale from SchedaSettimanale schedaSettimanale left join fetch schedaSettimanale.cliente",
        countQuery = "select count(schedaSettimanale) from SchedaSettimanale schedaSettimanale"
    )
    Page<SchedaSettimanale> findAllWithToOneRelationships(Pageable pageable);

    @Query("select schedaSettimanale from SchedaSettimanale schedaSettimanale left join fetch schedaSettimanale.cliente")
    List<SchedaSettimanale> findAllWithToOneRelationships();

    @Query(
        "select schedaSettimanale from SchedaSettimanale schedaSettimanale left join fetch schedaSettimanale.cliente where schedaSettimanale.id =:id"
    )
    Optional<SchedaSettimanale> findOneWithToOneRelationships(@Param("id") Long id);
}
