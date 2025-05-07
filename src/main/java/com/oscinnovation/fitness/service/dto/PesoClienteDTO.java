package com.oscinnovation.fitness.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.oscinnovation.fitness.domain.PesoCliente} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PesoClienteDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private Float peso;

    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    private Integer mese;

    @NotNull
    private LocalDate dataInserimento;

    @NotNull
    private ClienteDTO cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public Integer getMese() {
        return mese;
    }

    public void setMese(Integer mese) {
        this.mese = mese;
    }

    public LocalDate getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(LocalDate dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PesoClienteDTO)) {
            return false;
        }

        PesoClienteDTO pesoClienteDTO = (PesoClienteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pesoClienteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PesoClienteDTO{" +
            "id=" + getId() +
            ", peso=" + getPeso() +
            ", mese=" + getMese() +
            ", dataInserimento='" + getDataInserimento() + "'" +
            ", cliente=" + getCliente() +
            "}";
    }
}
