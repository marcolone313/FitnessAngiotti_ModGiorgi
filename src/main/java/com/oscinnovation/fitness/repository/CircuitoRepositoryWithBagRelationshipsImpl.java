package com.oscinnovation.fitness.repository;

import com.oscinnovation.fitness.domain.Circuito;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CircuitoRepositoryWithBagRelationshipsImpl implements CircuitoRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String CIRCUITOS_PARAMETER = "circuitos";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Circuito> fetchBagRelationships(Optional<Circuito> circuito) {
        return circuito.map(this::fetchEsercizios);
    }

    @Override
    public Page<Circuito> fetchBagRelationships(Page<Circuito> circuitos) {
        return new PageImpl<>(fetchBagRelationships(circuitos.getContent()), circuitos.getPageable(), circuitos.getTotalElements());
    }

    @Override
    public List<Circuito> fetchBagRelationships(List<Circuito> circuitos) {
        return Optional.of(circuitos).map(this::fetchEsercizios).orElse(Collections.emptyList());
    }

    Circuito fetchEsercizios(Circuito result) {
        return entityManager
            .createQuery(
                "select circuito from Circuito circuito left join fetch circuito.esercizios where circuito.id = :id",
                Circuito.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Circuito> fetchEsercizios(List<Circuito> circuitos) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, circuitos.size()).forEach(index -> order.put(circuitos.get(index).getId(), index));
        List<Circuito> result = entityManager
            .createQuery(
                "select circuito from Circuito circuito left join fetch circuito.esercizios where circuito in :circuitos",
                Circuito.class
            )
            .setParameter(CIRCUITOS_PARAMETER, circuitos)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
