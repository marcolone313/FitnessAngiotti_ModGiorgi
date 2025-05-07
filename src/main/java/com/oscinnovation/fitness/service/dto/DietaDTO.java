package com.oscinnovation.fitness.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.oscinnovation.fitness.domain.Dieta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DietaDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dataCreazione;

    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    private Integer mese;

    @NotNull
    private String tipo;

    @NotNull
    private String macros;

    @NotNull
    private String fabbisognoCalorico;

    @NotNull
    private ClienteDTO cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Integer getMese() {
        return mese;
    }

    public void setMese(Integer mese) {
        this.mese = mese;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMacros() {
        return macros;
    }

    public void setMacros(String macros) {
        this.macros = macros;
    }

    public String getFabbisognoCalorico() {
        return fabbisognoCalorico;
    }

    public void setFabbisognoCalorico(String fabbisognoCalorico) {
        this.fabbisognoCalorico = fabbisognoCalorico;
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
        if (!(o instanceof DietaDTO)) {
            return false;
        }

        DietaDTO dietaDTO = (DietaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dietaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DietaDTO{" +
            "id=" + getId() +
            ", dataCreazione='" + getDataCreazione() + "'" +
            ", mese=" + getMese() +
            ", tipo='" + getTipo() + "'" +
            ", macros='" + getMacros() + "'" +
            ", fabbisognoCalorico='" + getFabbisognoCalorico() + "'" +
            ", cliente=" + getCliente() +
            "}";
    }
}
