package com.oscinnovation.fitness.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.oscinnovation.fitness.domain.Plicometria} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlicometriaDTO implements Serializable {

    private Long id;

    @DecimalMin(value = "0")
    private Float tricipite;

    @DecimalMin(value = "0")
    private Float petto;

    @DecimalMin(value = "0")
    private Float ascella;

    @DecimalMin(value = "0")
    private Float sottoscapolare;

    @DecimalMin(value = "0")
    private Float soprailliaca;

    @DecimalMin(value = "0")
    private Float addome;

    @DecimalMin(value = "0")
    private Float coscia;

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

    public Float getTricipite() {
        return tricipite;
    }

    public void setTricipite(Float tricipite) {
        this.tricipite = tricipite;
    }

    public Float getPetto() {
        return petto;
    }

    public void setPetto(Float petto) {
        this.petto = petto;
    }

    public Float getAscella() {
        return ascella;
    }

    public void setAscella(Float ascella) {
        this.ascella = ascella;
    }

    public Float getSottoscapolare() {
        return sottoscapolare;
    }

    public void setSottoscapolare(Float sottoscapolare) {
        this.sottoscapolare = sottoscapolare;
    }

    public Float getSoprailliaca() {
        return soprailliaca;
    }

    public void setSoprailliaca(Float soprailliaca) {
        this.soprailliaca = soprailliaca;
    }

    public Float getAddome() {
        return addome;
    }

    public void setAddome(Float addome) {
        this.addome = addome;
    }

    public Float getCoscia() {
        return coscia;
    }

    public void setCoscia(Float coscia) {
        this.coscia = coscia;
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
        if (!(o instanceof PlicometriaDTO)) {
            return false;
        }

        PlicometriaDTO plicometriaDTO = (PlicometriaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, plicometriaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlicometriaDTO{" +
            "id=" + getId() +
            ", tricipite=" + getTricipite() +
            ", petto=" + getPetto() +
            ", ascella=" + getAscella() +
            ", sottoscapolare=" + getSottoscapolare() +
            ", soprailliaca=" + getSoprailliaca() +
            ", addome=" + getAddome() +
            ", coscia=" + getCoscia() +
            ", mese=" + getMese() +
            ", dataInserimento='" + getDataInserimento() + "'" +
            ", cliente=" + getCliente() +
            "}";
    }
}
