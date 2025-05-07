package com.oscinnovation.fitness.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A LezioneCorso.
 */
@Entity
@Table(name = "lezione_corso")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LezioneCorso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "titolo", nullable = false)
    private String titolo;

    @Lob
    @Column(name = "descrizione")
    private String descrizione;

    @Lob
    @Column(name = "punti_focali")
    private String puntiFocali;

    @Column(name = "video_url")
    private String videoUrl;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "lezioneCorsos" }, allowSetters = true)
    private CorsoAcademy corsoAcademy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LezioneCorso id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return this.titolo;
    }

    public LezioneCorso titolo(String titolo) {
        this.setTitolo(titolo);
        return this;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public LezioneCorso descrizione(String descrizione) {
        this.setDescrizione(descrizione);
        return this;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getPuntiFocali() {
        return this.puntiFocali;
    }

    public LezioneCorso puntiFocali(String puntiFocali) {
        this.setPuntiFocali(puntiFocali);
        return this;
    }

    public void setPuntiFocali(String puntiFocali) {
        this.puntiFocali = puntiFocali;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public LezioneCorso videoUrl(String videoUrl) {
        this.setVideoUrl(videoUrl);
        return this;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public CorsoAcademy getCorsoAcademy() {
        return this.corsoAcademy;
    }

    public void setCorsoAcademy(CorsoAcademy corsoAcademy) {
        this.corsoAcademy = corsoAcademy;
    }

    public LezioneCorso corsoAcademy(CorsoAcademy corsoAcademy) {
        this.setCorsoAcademy(corsoAcademy);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LezioneCorso)) {
            return false;
        }
        return getId() != null && getId().equals(((LezioneCorso) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LezioneCorso{" +
            "id=" + getId() +
            ", titolo='" + getTitolo() + "'" +
            ", descrizione='" + getDescrizione() + "'" +
            ", puntiFocali='" + getPuntiFocali() + "'" +
            ", videoUrl='" + getVideoUrl() + "'" +
            "}";
    }
}
