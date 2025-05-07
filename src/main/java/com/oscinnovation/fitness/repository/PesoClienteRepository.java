package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.PesoCliente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PesoCliente entity.
 */
@Repository
public interface PesoClienteRepository extends JpaRepository<PesoCliente, Long>, JpaSpecificationExecutor<PesoCliente> {
    default Optional<PesoCliente> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PesoCliente> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PesoCliente> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select pesoCliente from PesoCliente pesoCliente left join fetch pesoCliente.cliente",
        countQuery = "select count(pesoCliente) from PesoCliente pesoCliente"
    )
    Page<PesoCliente> findAllWithToOneRelationships(Pageable pageable);

    @Query("select pesoCliente from PesoCliente pesoCliente left join fetch pesoCliente.cliente")
    List<PesoCliente> findAllWithToOneRelationships();

    @Query("select pesoCliente from PesoCliente pesoCliente left join fetch pesoCliente.cliente where pesoCliente.id =:id")
    Optional<PesoCliente> findOneWithToOneRelationships(@Param("id") Long id);
}
