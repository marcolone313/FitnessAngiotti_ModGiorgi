package com.oscinnovation.fitness.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oscinnovation.fitness.domain.enumeration.TipoAllenamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A AllenamentoGiornaliero.
 */
@Entity
@Table(name = "allenamento_giornaliero")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AllenamentoGiornaliero implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoAllenamento tipo;

    @NotNull
    @Min(value = 1)
    @Max(value = 6)
    @Column(name = "giorno", nullable = false)
    private Integer giorno;

    @Lob
    @Column(name = "nota_trainer")
    private String notaTrainer;

    @Column(name = "svolto")
    private Boolean svolto;

    @Column(name = "data_allenamento")
    private LocalDate dataAllenamento;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cliente", "reportSettimanale" }, allowSetters = true)
    private SchedaSettimanale schedaSettimanale;

    @JsonIgnoreProperties(value = { "allenamentoGiornaliero", "circuitoToEsercizios" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "allenamentoGiornaliero")
    private Circuito circuito;

    @JsonIgnoreProperties(value = { "allenamentoGiornaliero" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "allenamentoGiornaliero")
    private Corsa corsa;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AllenamentoGiornaliero id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoAllenamento getTipo() {
        return this.tipo;
    }

    public AllenamentoGiornaliero tipo(TipoAllenamento tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoAllenamento tipo) {
        this.tipo = tipo;
    }

    public Integer getGiorno() {
        return this.giorno;
    }

    public AllenamentoGiornaliero giorno(Integer giorno) {
        this.setGiorno(giorno);
        return this;
    }

    public void setGiorno(Integer giorno) {
        this.giorno = giorno;
    }

    public String getNotaTrainer() {
        return this.notaTrainer;
    }

    public AllenamentoGiornaliero notaTrainer(String notaTrainer) {
        this.setNotaTrainer(notaTrainer);
        return this;
    }

    public void setNotaTrainer(String notaTrainer) {
        this.notaTrainer = notaTrainer;
    }

    public Boolean getSvolto() {
        return this.svolto;
    }

    public AllenamentoGiornaliero svolto(Boolean svolto) {
        this.setSvolto(svolto);
        return this;
    }

    public void setSvolto(Boolean svolto) {
        this.svolto = svolto;
    }

    public LocalDate getDataAllenamento() {
        return this.dataAllenamento;
    }

    public AllenamentoGiornaliero dataAllenamento(LocalDate dataAllenamento) {
        this.setDataAllenamento(dataAllenamento);
        return this;
    }

    public void setDataAllenamento(LocalDate dataAllenamento) {
        this.dataAllenamento = dataAllenamento;
    }

    public SchedaSettimanale getSchedaSettimanale() {
        return this.schedaSettimanale;
    }

    public void setSchedaSettimanale(SchedaSettimanale schedaSettimanale) {
        this.schedaSettimanale = schedaSettimanale;
    }

    public AllenamentoGiornaliero schedaSettimanale(SchedaSettimanale schedaSettimanale) {
        this.setSchedaSettimanale(schedaSettimanale);
        return this;
    }

    public Circuito getCircuito() {
        return this.circuito;
    }

    public void setCircuito(Circuito circuito) {
        if (this.circuito != null) {
            this.circuito.setAllenamentoGiornaliero(null);
        }
        if (circuito != null) {
            circuito.setAllenamentoGiornaliero(this);
        }
        this.circuito = circuito;
    }

    public AllenamentoGiornaliero circuito(Circuito circuito) {
        this.setCircuito(circuito);
        return this;
    }

    public Corsa getCorsa() {
        return this.corsa;
    }

    public void setCorsa(Corsa corsa) {
        if (this.corsa != null) {
            this.corsa.setAllenamentoGiornaliero(null);
        }
        if (corsa != null) {
            corsa.setAllenamentoGiornaliero(this);
        }
        this.corsa = corsa;
    }

    public AllenamentoGiornaliero corsa(Corsa corsa) {
        this.setCorsa(corsa);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AllenamentoGiornaliero)) {
            return false;
        }
        return getId() != null && getId().equals(((AllenamentoGiornaliero) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AllenamentoGiornaliero{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", giorno=" + getGiorno() +
            ", notaTrainer='" + getNotaTrainer() + "'" +
            ", svolto='" + getSvolto() + "'" +
            ", dataAllenamento='" + getDataAllenamento() + "'" +
            "}";
    }
}
