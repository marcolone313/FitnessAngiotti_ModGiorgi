package com.oscinnovation.fitness.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Dieta.
 */
@Entity
@Table(name = "dieta")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Dieta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_creazione", nullable = false)
    private LocalDate dataCreazione;

    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    @Column(name = "mese", nullable = false)
    private Integer mese;

    @NotNull
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @NotNull
    @Column(name = "macros", nullable = false)
    private String macros;

    @NotNull
    @Column(name = "fabbisogno_calorico", nullable = false)
    private String fabbisognoCalorico;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dieta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataCreazione() {
        return this.dataCreazione;
    }

    public Dieta dataCreazione(LocalDate dataCreazione) {
        this.setDataCreazione(dataCreazione);
        return this;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Integer getMese() {
        return this.mese;
    }

    public Dieta mese(Integer mese) {
        this.setMese(mese);
        return this;
    }

    public void setMese(Integer mese) {
        this.mese = mese;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Dieta tipo(String tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMacros() {
        return this.macros;
    }

    public Dieta macros(String macros) {
        this.setMacros(macros);
        return this;
    }

    public void setMacros(String macros) {
        this.macros = macros;
    }

    public String getFabbisognoCalorico() {
        return this.fabbisognoCalorico;
    }

    public Dieta fabbisognoCalorico(String fabbisognoCalorico) {
        this.setFabbisognoCalorico(fabbisognoCalorico);
        return this;
    }

    public void setFabbisognoCalorico(String fabbisognoCalorico) {
        this.fabbisognoCalorico = fabbisognoCalorico;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Dieta cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dieta)) {
            return false;
        }
        return getId() != null && getId().equals(((Dieta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dieta{" +
            "id=" + getId() +
            ", dataCreazione='" + getDataCreazione() + "'" +
            ", mese=" + getMese() +
            ", tipo='" + getTipo() + "'" +
            ", macros='" + getMacros() + "'" +
            ", fabbisognoCalorico='" + getFabbisognoCalorico() + "'" +
            "}";
    }
}
