package com.oscinnovation.fitness.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.oscinnovation.fitness.domain.ClienteToLezioneCorso} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteToLezioneCorsoDTO implements Serializable {

    private Long id;

    private Boolean completato;

    private LocalDate dataCompletamento;

    @NotNull
    private ClienteDTO cliente;

    @NotNull
    private LezioneCorsoDTO lezioneCorso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getCompletato() {
        return completato;
    }

    public void setCompletato(Boolean completato) {
        this.completato = completato;
    }

    public LocalDate getDataCompletamento() {
        return dataCompletamento;
    }

    public void setDataCompletamento(LocalDate dataCompletamento) {
        this.dataCompletamento = dataCompletamento;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public LezioneCorsoDTO getLezioneCorso() {
        return lezioneCorso;
    }

    public void setLezioneCorso(LezioneCorsoDTO lezioneCorso) {
        this.lezioneCorso = lezioneCorso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClienteToLezioneCorsoDTO)) {
            return false;
        }

        ClienteToLezioneCorsoDTO clienteToLezioneCorsoDTO = (ClienteToLezioneCorsoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clienteToLezioneCorsoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteToLezioneCorsoDTO{" +
            "id=" + getId() +
            ", completato='" + getCompletato() + "'" +
            ", dataCompletamento='" + getDataCompletamento() + "'" +
            ", cliente=" + getCliente() +
            ", lezioneCorso=" + getLezioneCorso() +
            "}";
    }
}
