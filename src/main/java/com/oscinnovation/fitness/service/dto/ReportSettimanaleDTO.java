package com.oscinnovation.fitness.service.dto;

import com.oscinnovation.fitness.domain.enumeration.Voto;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.oscinnovation.fitness.domain.ReportSettimanale} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportSettimanaleDTO implements Serializable {

    private Long id;

    private Voto voto;

    @Lob
    private String commentoAllenamento;

    private Voto giorniDieta;

    @DecimalMin(value = "0")
    private Float pesoMedio;

    private Voto qualitaSonno;

    private Duration mediaOreSonno;

    @NotNull
    private LocalDate dataCreazione;

    @NotNull
    private LocalDate dataScadenza;

    private LocalDate dataCompletamento;

    private Boolean puntuale;

    @Lob
    private String analisiReport;

    @NotNull
    private SchedaSettimanaleDTO schedaSettimanale;

    @NotNull
    private ClienteDTO cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Voto getVoto() {
        return voto;
    }

    public void setVoto(Voto voto) {
        this.voto = voto;
    }

    public String getCommentoAllenamento() {
        return commentoAllenamento;
    }

    public void setCommentoAllenamento(String commentoAllenamento) {
        this.commentoAllenamento = commentoAllenamento;
    }

    public Voto getGiorniDieta() {
        return giorniDieta;
    }

    public void setGiorniDieta(Voto giorniDieta) {
        this.giorniDieta = giorniDieta;
    }

    public Float getPesoMedio() {
        return pesoMedio;
    }

    public void setPesoMedio(Float pesoMedio) {
        this.pesoMedio = pesoMedio;
    }

    public Voto getQualitaSonno() {
        return qualitaSonno;
    }

    public void setQualitaSonno(Voto qualitaSonno) {
        this.qualitaSonno = qualitaSonno;
    }

    public Duration getMediaOreSonno() {
        return mediaOreSonno;
    }

    public void setMediaOreSonno(Duration mediaOreSonno) {
        this.mediaOreSonno = mediaOreSonno;
    }

    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public LocalDate getDataCompletamento() {
        return dataCompletamento;
    }

    public void setDataCompletamento(LocalDate dataCompletamento) {
        this.dataCompletamento = dataCompletamento;
    }

    public Boolean getPuntuale() {
        return puntuale;
    }

    public void setPuntuale(Boolean puntuale) {
        this.puntuale = puntuale;
    }

    public String getAnalisiReport() {
        return analisiReport;
    }

    public void setAnalisiReport(String analisiReport) {
        this.analisiReport = analisiReport;
    }

    public SchedaSettimanaleDTO getSchedaSettimanale() {
        return schedaSettimanale;
    }

    public void setSchedaSettimanale(SchedaSettimanaleDTO schedaSettimanale) {
        this.schedaSettimanale = schedaSettimanale;
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
        if (!(o instanceof ReportSettimanaleDTO)) {
            return false;
        }

        ReportSettimanaleDTO reportSettimanaleDTO = (ReportSettimanaleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportSettimanaleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportSettimanaleDTO{" +
            "id=" + getId() +
            ", voto='" + getVoto() + "'" +
            ", commentoAllenamento='" + getCommentoAllenamento() + "'" +
            ", giorniDieta='" + getGiorniDieta() + "'" +
            ", pesoMedio=" + getPesoMedio() +
            ", qualitaSonno='" + getQualitaSonno() + "'" +
            ", mediaOreSonno='" + getMediaOreSonno() + "'" +
            ", dataCreazione='" + getDataCreazione() + "'" +
            ", dataScadenza='" + getDataScadenza() + "'" +
            ", dataCompletamento='" + getDataCompletamento() + "'" +
            ", puntuale='" + getPuntuale() + "'" +
            ", analisiReport='" + getAnalisiReport() + "'" +
            ", schedaSettimanale=" + getSchedaSettimanale() +
            ", cliente=" + getCliente() +
            "}";
    }
}
