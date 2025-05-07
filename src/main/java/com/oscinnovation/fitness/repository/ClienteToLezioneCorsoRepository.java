package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.ClienteToLezioneCorso;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ClienteToLezioneCorso entity.
 */
@Repository
public interface ClienteToLezioneCorsoRepository
    extends JpaRepository<ClienteToLezioneCorso, Long>, JpaSpecificationExecutor<ClienteToLezioneCorso> {
    default Optional<ClienteToLezioneCorso> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ClienteToLezioneCorso> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ClienteToLezioneCorso> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select clienteToLezioneCorso from ClienteToLezioneCorso clienteToLezioneCorso left join fetch clienteToLezioneCorso.cliente left join fetch clienteToLezioneCorso.lezioneCorso",
        countQuery = "select count(clienteToLezioneCorso) from ClienteToLezioneCorso clienteToLezioneCorso"
    )
    Page<ClienteToLezioneCorso> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select clienteToLezioneCorso from ClienteToLezioneCorso clienteToLezioneCorso left join fetch clienteToLezioneCorso.cliente left join fetch clienteToLezioneCorso.lezioneCorso"
    )
    List<ClienteToLezioneCorso> findAllWithToOneRelationships();

    @Query(
        "select clienteToLezioneCorso from ClienteToLezioneCorso clienteToLezioneCorso left join fetch clienteToLezioneCorso.cliente left join fetch clienteToLezioneCorso.lezioneCorso where clienteToLezioneCorso.id =:id"
    )
    Optional<ClienteToLezioneCorso> findOneWithToOneRelationships(@Param("id") Long id);
}
