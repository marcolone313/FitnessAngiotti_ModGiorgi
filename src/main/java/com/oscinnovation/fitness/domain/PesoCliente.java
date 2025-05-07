package com.oscinnovation.fitness.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A PesoCliente.
 */
@Entity
@Table(name = "peso_cliente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PesoCliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "peso", nullable = false)
    private Float peso;

    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    @Column(name = "mese", nullable = false)
    private Integer mese;

    @NotNull
    @Column(name = "data_inserimento", nullable = false)
    private LocalDate dataInserimento;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PesoCliente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPeso() {
        return this.peso;
    }

    public PesoCliente peso(Float peso) {
        this.setPeso(peso);
        return this;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public Integer getMese() {
        return this.mese;
    }

    public PesoCliente mese(Integer mese) {
        this.setMese(mese);
        return this;
    }

    public void setMese(Integer mese) {
        this.mese = mese;
    }

    public LocalDate getDataInserimento() {
        return this.dataInserimento;
    }

    public PesoCliente dataInserimento(LocalDate dataInserimento) {
        this.setDataInserimento(dataInserimento);
        return this;
    }

    public void setDataInserimento(LocalDate dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public PesoCliente cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PesoCliente)) {
            return false;
        }
        return getId() != null && getId().equals(((PesoCliente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PesoCliente{" +
            "id=" + getId() +
            ", peso=" + getPeso() +
            ", mese=" + getMese() +
            ", dataInserimento='" + getDataInserimento() + "'" +
            "}";
    }
}
