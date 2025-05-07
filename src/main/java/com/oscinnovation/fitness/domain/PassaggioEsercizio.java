package com.oscinnovation.fitness.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A PassaggioEsercizio.
 */
@Entity
@Table(name = "passaggio_esercizio")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PassaggioEsercizio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "indice", nullable = false)
    private Integer indice;

    @Lob
    @Column(name = "descrizione", nullable = false)
    private String descrizione;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "gyms", "circuitoToEsercizios", "passaggioEsercizios" }, allowSetters = true)
    private Esercizio esercizio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PassaggioEsercizio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIndice() {
        return this.indice;
    }

    public PassaggioEsercizio indice(Integer indice) {
        this.setIndice(indice);
        return this;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public PassaggioEsercizio descrizione(String descrizione) {
        this.setDescrizione(descrizione);
        return this;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Esercizio getEsercizio() {
        return this.esercizio;
    }

    public void setEsercizio(Esercizio esercizio) {
        this.esercizio = esercizio;
    }

    public PassaggioEsercizio esercizio(Esercizio esercizio) {
        this.setEsercizio(esercizio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PassaggioEsercizio)) {
            return false;
        }
        return getId() != null && getId().equals(((PassaggioEsercizio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PassaggioEsercizio{" +
            "id=" + getId() +
            ", indice=" + getIndice() +
            ", descrizione='" + getDescrizione() + "'" +
            "}";
    }
}
