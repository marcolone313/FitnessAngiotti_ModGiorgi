package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.PassaggioEsercizio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PassaggioEsercizio entity.
 */
@Repository
public interface PassaggioEsercizioRepository
    extends JpaRepository<PassaggioEsercizio, Long>, JpaSpecificationExecutor<PassaggioEsercizio> {
    default Optional<PassaggioEsercizio> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PassaggioEsercizio> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PassaggioEsercizio> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select passaggioEsercizio from PassaggioEsercizio passaggioEsercizio left join fetch passaggioEsercizio.esercizio",
        countQuery = "select count(passaggioEsercizio) from PassaggioEsercizio passaggioEsercizio"
    )
    Page<PassaggioEsercizio> findAllWithToOneRelationships(Pageable pageable);

    @Query("select passaggioEsercizio from PassaggioEsercizio passaggioEsercizio left join fetch passaggioEsercizio.esercizio")
    List<PassaggioEsercizio> findAllWithToOneRelationships();

    @Query(
        "select passaggioEsercizio from PassaggioEsercizio passaggioEsercizio left join fetch passaggioEsercizio.esercizio where passaggioEsercizio.id =:id"
    )
    Optional<PassaggioEsercizio> findOneWithToOneRelationships(@Param("id") Long id);
}
