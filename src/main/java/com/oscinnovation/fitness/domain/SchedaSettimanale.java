package com.oscinnovation.fitness.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A SchedaSettimanale.
 */
@Entity
@Table(name = "scheda_settimanale")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SchedaSettimanale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "anno", nullable = false)
    private Integer anno;

    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    @Column(name = "mese", nullable = false)
    private Integer mese;

    @NotNull
    @Min(value = 1)
    @Max(value = 4)
    @Column(name = "settimana", nullable = false)
    private Integer settimana;

    @NotNull
    @Column(name = "data_creazione", nullable = false)
    private LocalDate dataCreazione;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Cliente cliente;

    @JsonIgnoreProperties(value = { "schedaSettimanale", "cliente" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "schedaSettimanale")
    private ReportSettimanale reportSettimanale;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SchedaSettimanale id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnno() {
        return this.anno;
    }

    public SchedaSettimanale anno(Integer anno) {
        this.setAnno(anno);
        return this;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Integer getMese() {
        return this.mese;
    }

    public SchedaSettimanale mese(Integer mese) {
        this.setMese(mese);
        return this;
    }

    public void setMese(Integer mese) {
        this.mese = mese;
    }

    public Integer getSettimana() {
        return this.settimana;
    }

    public SchedaSettimanale settimana(Integer settimana) {
        this.setSettimana(settimana);
        return this;
    }

    public void setSettimana(Integer settimana) {
        this.settimana = settimana;
    }

    public LocalDate getDataCreazione() {
        return this.dataCreazione;
    }

    public SchedaSettimanale dataCreazione(LocalDate dataCreazione) {
        this.setDataCreazione(dataCreazione);
        return this;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public SchedaSettimanale cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public ReportSettimanale getReportSettimanale() {
        return this.reportSettimanale;
    }

    public void setReportSettimanale(ReportSettimanale reportSettimanale) {
        if (this.reportSettimanale != null) {
            this.reportSettimanale.setSchedaSettimanale(null);
        }
        if (reportSettimanale != null) {
            reportSettimanale.setSchedaSettimanale(this);
        }
        this.reportSettimanale = reportSettimanale;
    }

    public SchedaSettimanale reportSettimanale(ReportSettimanale reportSettimanale) {
        this.setReportSettimanale(reportSettimanale);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchedaSettimanale)) {
            return false;
        }
        return getId() != null && getId().equals(((SchedaSettimanale) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchedaSettimanale{" +
            "id=" + getId() +
            ", anno=" + getAnno() +
            ", mese=" + getMese() +
            ", settimana=" + getSettimana() +
            ", dataCreazione='" + getDataCreazione() + "'" +
            "}";
    }
}
