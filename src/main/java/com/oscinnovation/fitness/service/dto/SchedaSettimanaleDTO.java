package com.oscinnovation.fitness.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.oscinnovation.fitness.domain.SchedaSettimanale} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SchedaSettimanaleDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    private Integer anno;

    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    private Integer mese;

    @NotNull
    @Min(value = 1)
    @Max(value = 4)
    private Integer settimana;

    @NotNull
    private LocalDate dataCreazione;

    @NotNull
    private ClienteDTO cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Integer getMese() {
        return mese;
    }

    public void setMese(Integer mese) {
        this.mese = mese;
    }

    public Integer getSettimana() {
        return settimana;
    }

    public void setSettimana(Integer settimana) {
        this.settimana = settimana;
    }

    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
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
        if (!(o instanceof SchedaSettimanaleDTO)) {
            return false;
        }

        SchedaSettimanaleDTO schedaSettimanaleDTO = (SchedaSettimanaleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, schedaSettimanaleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchedaSettimanaleDTO{" +
            "id=" + getId() +
            ", anno=" + getAnno() +
            ", mese=" + getMese() +
            ", settimana=" + getSettimana() +
            ", dataCreazione='" + getDataCreazione() + "'" +
            ", cliente=" + getCliente() +
            "}";
    }
}
