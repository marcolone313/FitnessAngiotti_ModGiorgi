package com.oscinnovation.fitness.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oscinnovation.fitness.domain.enumeration.Voto;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;

/**
 * A ReportSettimanale.
 */
@Entity
@Table(name = "report_settimanale")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportSettimanale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "voto")
    private Voto voto;

    @Lob
    @Column(name = "commento_allenamento")
    private String commentoAllenamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "giorni_dieta")
    private Voto giorniDieta;

    @DecimalMin(value = "0")
    @Column(name = "peso_medio")
    private Float pesoMedio;

    @Enumerated(EnumType.STRING)
    @Column(name = "qualita_sonno")
    private Voto qualitaSonno;

    @Column(name = "media_ore_sonno")
    private Duration mediaOreSonno;

    @NotNull
    @Column(name = "data_creazione", nullable = false)
    private LocalDate dataCreazione;

    @NotNull
    @Column(name = "data_scadenza", nullable = false)
    private LocalDate dataScadenza;

    @Column(name = "data_completamento")
    private LocalDate dataCompletamento;

    @Column(name = "puntuale")
    private Boolean puntuale;

    @Lob
    @Column(name = "analisi_report")
    private String analisiReport;

    @JsonIgnoreProperties(value = { "cliente", "reportSettimanale" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private SchedaSettimanale schedaSettimanale;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportSettimanale id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Voto getVoto() {
        return this.voto;
    }

    public ReportSettimanale voto(Voto voto) {
        this.setVoto(voto);
        return this;
    }

    public void setVoto(Voto voto) {
        this.voto = voto;
    }

    public String getCommentoAllenamento() {
        return this.commentoAllenamento;
    }

    public ReportSettimanale commentoAllenamento(String commentoAllenamento) {
        this.setCommentoAllenamento(commentoAllenamento);
        return this;
    }

    public void setCommentoAllenamento(String commentoAllenamento) {
        this.commentoAllenamento = commentoAllenamento;
    }

    public Voto getGiorniDieta() {
        return this.giorniDieta;
    }

    public ReportSettimanale giorniDieta(Voto giorniDieta) {
        this.setGiorniDieta(giorniDieta);
        return this;
    }

    public void setGiorniDieta(Voto giorniDieta) {
        this.giorniDieta = giorniDieta;
    }

    public Float getPesoMedio() {
        return this.pesoMedio;
    }

    public ReportSettimanale pesoMedio(Float pesoMedio) {
        this.setPesoMedio(pesoMedio);
        return this;
    }

    public void setPesoMedio(Float pesoMedio) {
        this.pesoMedio = pesoMedio;
    }

    public Voto getQualitaSonno() {
        return this.qualitaSonno;
    }

    public ReportSettimanale qualitaSonno(Voto qualitaSonno) {
        this.setQualitaSonno(qualitaSonno);
        return this;
    }

    public void setQualitaSonno(Voto qualitaSonno) {
        this.qualitaSonno = qualitaSonno;
    }

    public Duration getMediaOreSonno() {
        return this.mediaOreSonno;
    }

    public ReportSettimanale mediaOreSonno(Duration mediaOreSonno) {
        this.setMediaOreSonno(mediaOreSonno);
        return this;
    }

    public void setMediaOreSonno(Duration mediaOreSonno) {
        this.mediaOreSonno = mediaOreSonno;
    }

    public LocalDate getDataCreazione() {
        return this.dataCreazione;
    }

    public ReportSettimanale dataCreazione(LocalDate dataCreazione) {
        this.setDataCreazione(dataCreazione);
        return this;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public LocalDate getDataScadenza() {
        return this.dataScadenza;
    }

    public ReportSettimanale dataScadenza(LocalDate dataScadenza) {
        this.setDataScadenza(dataScadenza);
        return this;
    }

    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public LocalDate getDataCompletamento() {
        return this.dataCompletamento;
    }

    public ReportSettimanale dataCompletamento(LocalDate dataCompletamento) {
        this.setDataCompletamento(dataCompletamento);
        return this;
    }

    public void setDataCompletamento(LocalDate dataCompletamento) {
        this.dataCompletamento = dataCompletamento;
    }

    public Boolean getPuntuale() {
        return this.puntuale;
    }

    public ReportSettimanale puntuale(Boolean puntuale) {
        this.setPuntuale(puntuale);
        return this;
    }

    public void setPuntuale(Boolean puntuale) {
        this.puntuale = puntuale;
    }

    public String getAnalisiReport() {
        return this.analisiReport;
    }

    public ReportSettimanale analisiReport(String analisiReport) {
        this.setAnalisiReport(analisiReport);
        return this;
    }

    public void setAnalisiReport(String analisiReport) {
        this.analisiReport = analisiReport;
    }

    public SchedaSettimanale getSchedaSettimanale() {
        return this.schedaSettimanale;
    }

    public void setSchedaSettimanale(SchedaSettimanale schedaSettimanale) {
        this.schedaSettimanale = schedaSettimanale;
    }

    public ReportSettimanale schedaSettimanale(SchedaSettimanale schedaSettimanale) {
        this.setSchedaSettimanale(schedaSettimanale);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ReportSettimanale cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportSettimanale)) {
            return false;
        }
        return getId() != null && getId().equals(((ReportSettimanale) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportSettimanale{" +
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
            "}";
    }
}
