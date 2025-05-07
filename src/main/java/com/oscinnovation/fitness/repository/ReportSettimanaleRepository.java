package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.ReportSettimanale;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportSettimanale entity.
 */
@Repository
public interface ReportSettimanaleRepository extends JpaRepository<ReportSettimanale, Long>, JpaSpecificationExecutor<ReportSettimanale> {
    default Optional<ReportSettimanale> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReportSettimanale> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReportSettimanale> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select reportSettimanale from ReportSettimanale reportSettimanale left join fetch reportSettimanale.cliente",
        countQuery = "select count(reportSettimanale) from ReportSettimanale reportSettimanale"
    )
    Page<ReportSettimanale> findAllWithToOneRelationships(Pageable pageable);

    @Query("select reportSettimanale from ReportSettimanale reportSettimanale left join fetch reportSettimanale.cliente")
    List<ReportSettimanale> findAllWithToOneRelationships();

    @Query(
        "select reportSettimanale from ReportSettimanale reportSettimanale left join fetch reportSettimanale.cliente where reportSettimanale.id =:id"
    )
    Optional<ReportSettimanale> findOneWithToOneRelationships(@Param("id") Long id);
}
