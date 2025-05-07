package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.CircuitoToEsercizio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CircuitoToEsercizio entity.
 */
@Repository
public interface CircuitoToEsercizioRepository
    extends JpaRepository<CircuitoToEsercizio, Long>, JpaSpecificationExecutor<CircuitoToEsercizio> {
    default Optional<CircuitoToEsercizio> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CircuitoToEsercizio> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CircuitoToEsercizio> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select circuitoToEsercizio from CircuitoToEsercizio circuitoToEsercizio left join fetch circuitoToEsercizio.esercizio",
        countQuery = "select count(circuitoToEsercizio) from CircuitoToEsercizio circuitoToEsercizio"
    )
    Page<CircuitoToEsercizio> findAllWithToOneRelationships(Pageable pageable);

    @Query("select circuitoToEsercizio from CircuitoToEsercizio circuitoToEsercizio left join fetch circuitoToEsercizio.esercizio")
    List<CircuitoToEsercizio> findAllWithToOneRelationships();

    @Query(
        "select circuitoToEsercizio from CircuitoToEsercizio circuitoToEsercizio left join fetch circuitoToEsercizio.esercizio where circuitoToEsercizio.id =:id"
    )
    Optional<CircuitoToEsercizio> findOneWithToOneRelationships(@Param("id") Long id);
}
