package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.LezioneCorso;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LezioneCorso entity.
 */
@Repository
public interface LezioneCorsoRepository extends JpaRepository<LezioneCorso, Long>, JpaSpecificationExecutor<LezioneCorso> {
    default Optional<LezioneCorso> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<LezioneCorso> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<LezioneCorso> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select lezioneCorso from LezioneCorso lezioneCorso left join fetch lezioneCorso.corsoAcademy",
        countQuery = "select count(lezioneCorso) from LezioneCorso lezioneCorso"
    )
    Page<LezioneCorso> findAllWithToOneRelationships(Pageable pageable);

    @Query("select lezioneCorso from LezioneCorso lezioneCorso left join fetch lezioneCorso.corsoAcademy")
    List<LezioneCorso> findAllWithToOneRelationships();

    @Query("select lezioneCorso from LezioneCorso lezioneCorso left join fetch lezioneCorso.corsoAcademy where lezioneCorso.id =:id")
    Optional<LezioneCorso> findOneWithToOneRelationships(@Param("id") Long id);
}
