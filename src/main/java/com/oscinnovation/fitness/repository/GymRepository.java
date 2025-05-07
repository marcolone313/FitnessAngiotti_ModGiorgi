package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.Gym;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Gym entity.
 */
@Repository
public interface GymRepository extends JpaRepository<Gym, Long>, JpaSpecificationExecutor<Gym> {
    default Optional<Gym> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Gym> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Gym> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select gym from Gym gym left join fetch gym.esercizio", countQuery = "select count(gym) from Gym gym")
    Page<Gym> findAllWithToOneRelationships(Pageable pageable);

    @Query("select gym from Gym gym left join fetch gym.esercizio")
    List<Gym> findAllWithToOneRelationships();

    @Query("select gym from Gym gym left join fetch gym.esercizio where gym.id =:id")
    Optional<Gym> findOneWithToOneRelationships(@Param("id") Long id);
}
