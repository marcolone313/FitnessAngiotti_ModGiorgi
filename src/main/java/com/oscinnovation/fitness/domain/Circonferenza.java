package com.oscinnovation.fitness.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Circonferenza.
 */
@Entity
@Table(name = "circonferenza")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Circonferenza implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @DecimalMin(value = "0")
    @Column(name = "torace")
    private Float torace;

    @DecimalMin(value = "0")
    @Column(name = "braccio")
    private Float braccio;

    @DecimalMin(value = "0")
    @Column(name = "avambraccio")
    private Float avambraccio;

    @DecimalMin(value = "0")
    @Column(name = "ombelico")
    private Float ombelico;

    @DecimalMin(value = "0")
    @Column(name = "fianchi")
    private Float fianchi;

    @DecimalMin(value = "0")
    @Column(name = "sotto_ombelico")
    private Float sottoOmbelico;

    @DecimalMin(value = "0")
    @Column(name = "vita")
    private Float vita;

    @DecimalMin(value = "0")
    @Column(name = "coscia")
    private Float coscia;

    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    @Column(name = "mese", nullable = false)
    private Integer mese;

    @NotNull
    @Column(name = "data_inserimento", nullable = false)
    private LocalDate dataInserimento;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Circonferenza id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getTorace() {
        return this.torace;
    }

    public Circonferenza torace(Float torace) {
        this.setTorace(torace);
        return this;
    }

    public void setTorace(Float torace) {
        this.torace = torace;
    }

    public Float getBraccio() {
        return this.braccio;
    }

    public Circonferenza braccio(Float braccio) {
        this.setBraccio(braccio);
        return this;
    }

    public void setBraccio(Float braccio) {
        this.braccio = braccio;
    }

    public Float getAvambraccio() {
        return this.avambraccio;
    }

    public Circonferenza avambraccio(Float avambraccio) {
        this.setAvambraccio(avambraccio);
        return this;
    }

    public void setAvambraccio(Float avambraccio) {
        this.avambraccio = avambraccio;
    }

    public Float getOmbelico() {
        return this.ombelico;
    }

    public Circonferenza ombelico(Float ombelico) {
        this.setOmbelico(ombelico);
        return this;
    }

    public void setOmbelico(Float ombelico) {
        this.ombelico = ombelico;
    }

    public Float getFianchi() {
        return this.fianchi;
    }

    public Circonferenza fianchi(Float fianchi) {
        this.setFianchi(fianchi);
        return this;
    }

    public void setFianchi(Float fianchi) {
        this.fianchi = fianchi;
    }

    public Float getSottoOmbelico() {
        return this.sottoOmbelico;
    }

    public Circonferenza sottoOmbelico(Float sottoOmbelico) {
        this.setSottoOmbelico(sottoOmbelico);
        return this;
    }

    public void setSottoOmbelico(Float sottoOmbelico) {
        this.sottoOmbelico = sottoOmbelico;
    }

    public Float getVita() {
        return this.vita;
    }

    public Circonferenza vita(Float vita) {
        this.setVita(vita);
        return this;
    }

    public void setVita(Float vita) {
        this.vita = vita;
    }

    public Float getCoscia() {
        return this.coscia;
    }

    public Circonferenza coscia(Float coscia) {
        this.setCoscia(coscia);
        return this;
    }

    public void setCoscia(Float coscia) {
        this.coscia = coscia;
    }

    public Integer getMese() {
        return this.mese;
    }

    public Circonferenza mese(Integer mese) {
        this.setMese(mese);
        return this;
    }

    public void setMese(Integer mese) {
        this.mese = mese;
    }

    public LocalDate getDataInserimento() {
        return this.dataInserimento;
    }

    public Circonferenza dataInserimento(LocalDate dataInserimento) {
        this.setDataInserimento(dataInserimento);
        return this;
    }

    public void setDataInserimento(LocalDate dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Circonferenza cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Circonferenza)) {
            return false;
        }
        return getId() != null && getId().equals(((Circonferenza) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Circonferenza{" +
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
            "}";
    }
}
