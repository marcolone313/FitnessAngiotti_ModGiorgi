package com.oscinnovation.fitness.service.dto;

import com.oscinnovation.fitness.domain.enumeration.TipoEsercizio;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.oscinnovation.fitness.domain.Esercizio} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EsercizioDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private TipoEsercizio tipo;

    private String videoUrl;

    @Lob
    private String descrizione;

    @Lob
    private byte[] foto;

    private String fotoContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoEsercizio getTipo() {
        return tipo;
    }

    public void setTipo(TipoEsercizio tipo) {
        this.tipo = tipo;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return fotoContentType;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EsercizioDTO)) {
            return false;
        }

        EsercizioDTO esercizioDTO = (EsercizioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, esercizioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EsercizioDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", videoUrl='" + getVideoUrl() + "'" +
            ", descrizione='" + getDescrizione() + "'" +
            ", foto='" + getFoto() + "'" +
            "}";
    }
}
