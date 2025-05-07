package com.oscinnovation.fitness.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oscinnovation.fitness.domain.enumeration.TipoEsercizio;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Esercizio.
 */
@Entity
@Table(name = "esercizio")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Esercizio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoEsercizio tipo;

    @Column(name = "video_url")
    private String videoUrl;

    @Lob
    @Column(name = "descrizione")
    private String descrizione;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "foto_content_type")
    private String fotoContentType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "esercizio")
    @JsonIgnoreProperties(value = { "allenamentoGiornaliero", "esercizio" }, allowSetters = true)
    private Set<Gym> gyms = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "esercizio")
    @JsonIgnoreProperties(value = { "esercizio", "circuito" }, allowSetters = true)
    private Set<CircuitoToEsercizio> circuitoToEsercizios = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "esercizio")
    @JsonIgnoreProperties(value = { "esercizio" }, allowSetters = true)
    private Set<PassaggioEsercizio> passaggioEsercizios = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Esercizio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Esercizio nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoEsercizio getTipo() {
        return this.tipo;
    }

    public Esercizio tipo(TipoEsercizio tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoEsercizio tipo) {
        this.tipo = tipo;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public Esercizio videoUrl(String videoUrl) {
        this.setVideoUrl(videoUrl);
        return this;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public Esercizio descrizione(String descrizione) {
        this.setDescrizione(descrizione);
        return this;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public byte[] getFoto() {
        return this.foto;
    }

    public Esercizio foto(byte[] foto) {
        this.setFoto(foto);
        return this;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return this.fotoContentType;
    }

    public Esercizio fotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
        return this;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    public Set<Gym> getGyms() {
        return this.gyms;
    }

    public void setGyms(Set<Gym> gyms) {
        if (this.gyms != null) {
            this.gyms.forEach(i -> i.setEsercizio(null));
        }
        if (gyms != null) {
            gyms.forEach(i -> i.setEsercizio(this));
        }
        this.gyms = gyms;
    }

    public Esercizio gyms(Set<Gym> gyms) {
        this.setGyms(gyms);
        return this;
    }

    public Esercizio addGym(Gym gym) {
        this.gyms.add(gym);
        gym.setEsercizio(this);
        return this;
    }

    public Esercizio removeGym(Gym gym) {
        this.gyms.remove(gym);
        gym.setEsercizio(null);
        return this;
    }

    public Set<CircuitoToEsercizio> getCircuitoToEsercizios() {
        return this.circuitoToEsercizios;
    }

    public void setCircuitoToEsercizios(Set<CircuitoToEsercizio> circuitoToEsercizios) {
        if (this.circuitoToEsercizios != null) {
            this.circuitoToEsercizios.forEach(i -> i.setEsercizio(null));
        }
        if (circuitoToEsercizios != null) {
            circuitoToEsercizios.forEach(i -> i.setEsercizio(this));
        }
        this.circuitoToEsercizios = circuitoToEsercizios;
    }

    public Esercizio circuitoToEsercizios(Set<CircuitoToEsercizio> circuitoToEsercizios) {
        this.setCircuitoToEsercizios(circuitoToEsercizios);
        return this;
    }

    public Esercizio addCircuitoToEsercizio(CircuitoToEsercizio circuitoToEsercizio) {
        this.circuitoToEsercizios.add(circuitoToEsercizio);
        circuitoToEsercizio.setEsercizio(this);
        return this;
    }

    public Esercizio removeCircuitoToEsercizio(CircuitoToEsercizio circuitoToEsercizio) {
        this.circuitoToEsercizios.remove(circuitoToEsercizio);
        circuitoToEsercizio.setEsercizio(null);
        return this;
    }

    public Set<PassaggioEsercizio> getPassaggioEsercizios() {
        return this.passaggioEsercizios;
    }

    public void setPassaggioEsercizios(Set<PassaggioEsercizio> passaggioEsercizios) {
        if (this.passaggioEsercizios != null) {
            this.passaggioEsercizios.forEach(i -> i.setEsercizio(null));
        }
        if (passaggioEsercizios != null) {
            passaggioEsercizios.forEach(i -> i.setEsercizio(this));
        }
        this.passaggioEsercizios = passaggioEsercizios;
    }

    public Esercizio passaggioEsercizios(Set<PassaggioEsercizio> passaggioEsercizios) {
        this.setPassaggioEsercizios(passaggioEsercizios);
        return this;
    }

    public Esercizio addPassaggioEsercizio(PassaggioEsercizio passaggioEsercizio) {
        this.passaggioEsercizios.add(passaggioEsercizio);
        passaggioEsercizio.setEsercizio(this);
        return this;
    }

    public Esercizio removePassaggioEsercizio(PassaggioEsercizio passaggioEsercizio) {
        this.passaggioEsercizios.remove(passaggioEsercizio);
        passaggioEsercizio.setEsercizio(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Esercizio)) {
            return false;
        }
        return getId() != null && getId().equals(((Esercizio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Esercizio{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", videoUrl='" + getVideoUrl() + "'" +
            ", descrizione='" + getDescrizione() + "'" +
            ", foto='" + getFoto() + "'" +
            ", fotoContentType='" + getFotoContentType() + "'" +
            "}";
    }
}
