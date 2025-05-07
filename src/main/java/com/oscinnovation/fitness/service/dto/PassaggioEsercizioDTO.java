package com.oscinnovation.fitness.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.oscinnovation.fitness.domain.PassaggioEsercizio} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PassaggioEsercizioDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    private Integer indice;

    @Lob
    private String descrizione;

    @NotNull
    private EsercizioDTO esercizio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public EsercizioDTO getEsercizio() {
        return esercizio;
    }

    public void setEsercizio(EsercizioDTO esercizio) {
        this.esercizio = esercizio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PassaggioEsercizioDTO)) {
            return false;
        }

        PassaggioEsercizioDTO passaggioEsercizioDTO = (PassaggioEsercizioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, passaggioEsercizioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PassaggioEsercizioDTO{" +
            "id=" + getId() +
            ", indice=" + getIndice() +
            ", descrizione='" + getDescrizione() + "'" +
            ", esercizio=" + getEsercizio() +
            "}";
    }
}
