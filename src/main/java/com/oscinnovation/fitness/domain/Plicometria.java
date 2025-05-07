package com.oscinnovation.fitness.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Plicometria.
 */
@Entity
@Table(name = "plicometria")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Plicometria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @DecimalMin(value = "0")
    @Column(name = "tricipite")
    private Float tricipite;

    @DecimalMin(value = "0")
    @Column(name = "petto")
    private Float petto;

    @DecimalMin(value = "0")
    @Column(name = "ascella")
    private Float ascella;

    @DecimalMin(value = "0")
    @Column(name = "sottoscapolare")
    private Float sottoscapolare;

    @DecimalMin(value = "0")
    @Column(name = "soprailliaca")
    private Float soprailliaca;

    @DecimalMin(value = "0")
    @Column(name = "addome")
    private Float addome;

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

    public Plicometria id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getTricipite() {
        return this.tricipite;
    }

    public Plicometria tricipite(Float tricipite) {
        this.setTricipite(tricipite);
        return this;
    }

    public void setTricipite(Float tricipite) {
        this.tricipite = tricipite;
    }

    public Float getPetto() {
        return this.petto;
    }

    public Plicometria petto(Float petto) {
        this.setPetto(petto);
        return this;
    }

    public void setPetto(Float petto) {
        this.petto = petto;
    }

    public Float getAscella() {
        return this.ascella;
    }

    public Plicometria ascella(Float ascella) {
        this.setAscella(ascella);
        return this;
    }

    public void setAscella(Float ascella) {
        this.ascella = ascella;
    }

    public Float getSottoscapolare() {
        return this.sottoscapolare;
    }

    public Plicometria sottoscapolare(Float sottoscapolare) {
        this.setSottoscapolare(sottoscapolare);
        return this;
    }

    public void setSottoscapolare(Float sottoscapolare) {
        this.sottoscapolare = sottoscapolare;
    }

    public Float getSoprailliaca() {
        return this.soprailliaca;
    }

    public Plicometria soprailliaca(Float soprailliaca) {
        this.setSoprailliaca(soprailliaca);
        return this;
    }

    public void setSoprailliaca(Float soprailliaca) {
        this.soprailliaca = soprailliaca;
    }

    public Float getAddome() {
        return this.addome;
    }

    public Plicometria addome(Float addome) {
        this.setAddome(addome);
        return this;
    }

    public void setAddome(Float addome) {
        this.addome = addome;
    }

    public Float getCoscia() {
        return this.coscia;
    }

    public Plicometria coscia(Float coscia) {
        this.setCoscia(coscia);
        return this;
    }

    public void setCoscia(Float coscia) {
        this.coscia = coscia;
    }

    public Integer getMese() {
        return this.mese;
    }

    public Plicometria mese(Integer mese) {
        this.setMese(mese);
        return this;
    }

    public void setMese(Integer mese) {
        this.mese = mese;
    }

    public LocalDate getDataInserimento() {
        return this.dataInserimento;
    }

    public Plicometria dataInserimento(LocalDate dataInserimento) {
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

    public Plicometria cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plicometria)) {
            return false;
        }
        return getId() != null && getId().equals(((Plicometria) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Plicometria{" +
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
            "}";
    }
}
