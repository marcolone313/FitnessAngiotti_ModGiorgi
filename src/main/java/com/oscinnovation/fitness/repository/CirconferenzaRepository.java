package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.Circonferenza;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Circonferenza entity.
 */
@Repository
public interface CirconferenzaRepository extends JpaRepository<Circonferenza, Long>, JpaSpecificationExecutor<Circonferenza> {
    default Optional<Circonferenza> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Circonferenza> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Circonferenza> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select circonferenza from Circonferenza circonferenza left join fetch circonferenza.cliente",
        countQuery = "select count(circonferenza) from Circonferenza circonferenza"
    )
    Page<Circonferenza> findAllWithToOneRelationships(Pageable pageable);

    @Query("select circonferenza from Circonferenza circonferenza left join fetch circonferenza.cliente")
    List<Circonferenza> findAllWithToOneRelationships();

    @Query("select circonferenza from Circonferenza circonferenza left join fetch circonferenza.cliente where circonferenza.id =:id")
    Optional<Circonferenza> findOneWithToOneRelationships(@Param("id") Long id);
}
