package com.oscinnovation.fitness.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.oscinnovation.fitness.domain.Circonferenza} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CirconferenzaDTO implements Serializable {

    private Long id;

    @DecimalMin(value = "0")
    private Float torace;

    @DecimalMin(value = "0")
    private Float braccio;

    @DecimalMin(value = "0")
    private Float avambraccio;

    @DecimalMin(value = "0")
    private Float ombelico;

    @DecimalMin(value = "0")
    private Float fianchi;

    @DecimalMin(value = "0")
    private Float sottoOmbelico;

    @DecimalMin(value = "0")
    private Float vita;

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

    public Float getTorace() {
        return torace;
    }

    public void setTorace(Float torace) {
        this.torace = torace;
    }

    public Float getBraccio() {
        return braccio;
    }

    public void setBraccio(Float braccio) {
        this.braccio = braccio;
    }

    public Float getAvambraccio() {
        return avambraccio;
    }

    public void setAvambraccio(Float avambraccio) {
        this.avambraccio = avambraccio;
    }

    public Float getOmbelico() {
        return ombelico;
    }

    public void setOmbelico(Float ombelico) {
        this.ombelico = ombelico;
    }

    public Float getFianchi() {
        return fianchi;
    }

    public void setFianchi(Float fianchi) {
        this.fianchi = fianchi;
    }

    public Float getSottoOmbelico() {
        return sottoOmbelico;
    }

    public void setSottoOmbelico(Float sottoOmbelico) {
        this.sottoOmbelico = sottoOmbelico;
    }

    public Float getVita() {
        return vita;
    }

    public void setVita(Float vita) {
        this.vita = vita;
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
        if (!(o instanceof CirconferenzaDTO)) {
            return false;
        }

        CirconferenzaDTO circonferenzaDTO = (CirconferenzaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, circonferenzaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CirconferenzaDTO{" +
            "id=" + getId() +
            ", torace=" + getTorace() +
            ", braccio=" + getBraccio() +
            ", avambraccio=" + getAvambraccio() +
            ", ombelico=" + getOmbelico() +
            ", fianchi=" + getFianchi() +
            ", sottoOmbelico=" + getSottoOmbelico() +
            ", vita=" + getVita() +
            ", coscia=" + getCoscia() +
            ", mese=" + getMese() +
            ", dataInserimento='" + getDataInserimento() + "'" +
            ", cliente=" + getCliente() +
            "}";
    }
}
