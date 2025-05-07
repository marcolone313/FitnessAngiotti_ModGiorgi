package com.oscinnovation.fitness.service.dto;

import com.oscinnovation.fitness.domain.enumeration.LivelloCorso;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.oscinnovation.fitness.domain.CorsoAcademy} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CorsoAcademyDTO implements Serializable {

    private Long id;

    @NotNull
    private LivelloCorso livello;

    @NotNull
    private String titolo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LivelloCorso getLivello() {
        return livello;
    }

    public void setLivello(LivelloCorso livello) {
        this.livello = livello;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CorsoAcademyDTO)) {
            return false;
        }

        CorsoAcademyDTO corsoAcademyDTO = (CorsoAcademyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, corsoAcademyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CorsoAcademyDTO{" +
            "id=" + getId() +
            ", livello='" + getLivello() + "'" +
            ", titolo='" + getTitolo() + "'" +
            "}";
    }
}
