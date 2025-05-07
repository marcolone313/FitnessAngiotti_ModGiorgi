package com.oscinnovation.fitness.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oscinnovation.fitness.domain.enumeration.LivelloCorso;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A CorsoAcademy.
 */
@Entity
@Table(name = "corso_academy")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CorsoAcademy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "livello", nullable = false)
    private LivelloCorso livello;

    @NotNull
    @Column(name = "titolo", nullable = false)
    private String titolo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "corsoAcademy")
    @JsonIgnoreProperties(value = { "corsoAcademy" }, allowSetters = true)
    private Set<LezioneCorso> lezioneCorsos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CorsoAcademy id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LivelloCorso getLivello() {
        return this.livello;
    }

    public CorsoAcademy livello(LivelloCorso livello) {
        this.setLivello(livello);
        return this;
    }

    public void setLivello(LivelloCorso livello) {
        this.livello = livello;
    }

    public String getTitolo() {
        return this.titolo;
    }

    public CorsoAcademy titolo(String titolo) {
        this.setTitolo(titolo);
        return this;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public Set<LezioneCorso> getLezioneCorsos() {
        return this.lezioneCorsos;
    }

    public void setLezioneCorsos(Set<LezioneCorso> lezioneCorsos) {
        if (this.lezioneCorsos != null) {
            this.lezioneCorsos.forEach(i -> i.setCorsoAcademy(null));
        }
        if (lezioneCorsos != null) {
            lezioneCorsos.forEach(i -> i.setCorsoAcademy(this));
        }
        this.lezioneCorsos = lezioneCorsos;
    }

    public CorsoAcademy lezioneCorsos(Set<LezioneCorso> lezioneCorsos) {
        this.setLezioneCorsos(lezioneCorsos);
        return this;
    }

    public CorsoAcademy addLezioneCorso(LezioneCorso lezioneCorso) {
        this.lezioneCorsos.add(lezioneCorso);
        lezioneCorso.setCorsoAcademy(this);
        return this;
    }

    public CorsoAcademy removeLezioneCorso(LezioneCorso lezioneCorso) {
        this.lezioneCorsos.remove(lezioneCorso);
        lezioneCorso.setCorsoAcademy(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CorsoAcademy)) {
            return false;
        }
        return getId() != null && getId().equals(((CorsoAcademy) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CorsoAcademy{" +
            "id=" + getId() +
            ", livello='" + getLivello() + "'" +
            ", titolo='" + getTitolo() + "'" +
            "}";
    }
}
