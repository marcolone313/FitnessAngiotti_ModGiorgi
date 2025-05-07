package com.oscinnovation.fitness.service.dto;

import com.oscinnovation.fitness.domain.enumeration.TipoAllenamento;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.oscinnovation.fitness.domain.AllenamentoGiornaliero} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AllenamentoGiornalieroDTO implements Serializable {

    private Long id;

    @NotNull
    private TipoAllenamento tipo;

    @NotNull
    @Min(value = 1)
    @Max(value = 6)
    private Integer giorno;

    @Lob
    private String notaTrainer;

    private Boolean svolto;

    private LocalDate dataAllenamento;

    @NotNull
    private SchedaSettimanaleDTO schedaSettimanale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoAllenamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoAllenamento tipo) {
        this.tipo = tipo;
    }

    public Integer getGiorno() {
        return giorno;
    }

    public void setGiorno(Integer giorno) {
        this.giorno = giorno;
    }

    public String getNotaTrainer() {
        return notaTrainer;
    }

    public void setNotaTrainer(String notaTrainer) {
        this.notaTrainer = notaTrainer;
    }

    public Boolean getSvolto() {
        return svolto;
    }

    public void setSvolto(Boolean svolto) {
        this.svolto = svolto;
    }

    public LocalDate getDataAllenamento() {
        return dataAllenamento;
    }

    public void setDataAllenamento(LocalDate dataAllenamento) {
        this.dataAllenamento = dataAllenamento;
    }

    public SchedaSettimanaleDTO getSchedaSettimanale() {
        return schedaSettimanale;
    }

    public void setSchedaSettimanale(SchedaSettimanaleDTO schedaSettimanale) {
        this.schedaSettimanale = schedaSettimanale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AllenamentoGiornalieroDTO)) {
            return false;
        }

        AllenamentoGiornalieroDTO allenamentoGiornalieroDTO = (AllenamentoGiornalieroDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, allenamentoGiornalieroDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AllenamentoGiornalieroDTO{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", giorno=" + getGiorno() +
            ", notaTrainer='" + getNotaTrainer() + "'" +
            ", svolto='" + getSvolto() + "'" +
            ", dataAllenamento='" + getDataAllenamento() + "'" +
            ", schedaSettimanale=" + getSchedaSettimanale() +
            "}";
    }
}
