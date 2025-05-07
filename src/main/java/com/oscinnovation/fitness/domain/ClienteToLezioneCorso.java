package com.oscinnovation.fitness.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A ClienteToLezioneCorso.
 */
@Entity
@Table(name = "cliente_to_lezione_corso")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteToLezioneCorso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "completato")
    private Boolean completato;

    @Column(name = "data_completamento")
    private LocalDate dataCompletamento;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Cliente cliente;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "corsoAcademy" }, allowSetters = true)
    private LezioneCorso lezioneCorso;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClienteToLezioneCorso id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getCompletato() {
        return this.completato;
    }

    public ClienteToLezioneCorso completato(Boolean completato) {
        this.setCompletato(completato);
        return this;
    }

    public void setCompletato(Boolean completato) {
        this.completato = completato;
    }

    public LocalDate getDataCompletamento() {
        return this.dataCompletamento;
    }

    public ClienteToLezioneCorso dataCompletamento(LocalDate dataCompletamento) {
        this.setDataCompletamento(dataCompletamento);
        return this;
    }

    public void setDataCompletamento(LocalDate dataCompletamento) {
        this.dataCompletamento = dataCompletamento;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ClienteToLezioneCorso cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public LezioneCorso getLezioneCorso() {
        return this.lezioneCorso;
    }

    public void setLezioneCorso(LezioneCorso lezioneCorso) {
        this.lezioneCorso = lezioneCorso;
    }

    public ClienteToLezioneCorso lezioneCorso(LezioneCorso lezioneCorso) {
        this.setLezioneCorso(lezioneCorso);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClienteToLezioneCorso)) {
            return false;
        }
        return getId() != null && getId().equals(((ClienteToLezioneCorso) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteToLezioneCorso{" +
            "id=" + getId() +
            ", completato='" + getCompletato() + "'" +
            ", dataCompletamento='" + getDataCompletamento() + "'" +
            "}";
    }
}
