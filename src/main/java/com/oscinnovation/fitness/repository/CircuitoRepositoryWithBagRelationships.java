package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.Circuito;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CircuitoRepositoryWithBagRelationships {
    Optional<Circuito> fetchBagRelationships(Optional<Circuito> circuito);

    List<Circuito> fetchBagRelationships(List<Circuito> circuitos);

    Page<Circuito> fetchBagRelationships(Page<Circuito> circuitos);
}
