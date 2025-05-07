package com.oscinnovation.fitness.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.oscinnovation.fitness.domain.Cliente} entity. This class is used
 * in {@link com.oscinnovation.fitness.web.rest.ClienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter cognome;

    private LocalDateFilter dataDiNascita;

    private StringFilter codiceFiscale;

    private StringFilter codiceCliente;

    private StringFilter email;

    private StringFilter telefono;

    private LongFilter userId;

    private Boolean distinct;

    public ClienteCriteria() {}

    public ClienteCriteria(ClienteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nome = other.optionalNome().map(StringFilter::copy).orElse(null);
        this.cognome = other.optionalCognome().map(StringFilter::copy).orElse(null);
        this.dataDiNascita = other.optionalDataDiNascita().map(LocalDateFilter::copy).orElse(null);
        this.codiceFiscale = other.optionalCodiceFiscale().map(StringFilter::copy).orElse(null);
        this.codiceCliente = other.optionalCodiceCliente().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.telefono = other.optionalTelefono().map(StringFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ClienteCriteria copy() {
        return new ClienteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNome() {
        return nome;
    }

    public Optional<StringFilter> optionalNome() {
        return Optional.ofNullable(nome);
    }

    public StringFilter nome() {
        if (nome == null) {
            setNome(new StringFilter());
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getCognome() {
        return cognome;
    }

    public Optional<StringFilter> optionalCognome() {
        return Optional.ofNullable(cognome);
    }

    public StringFilter cognome() {
        if (cognome == null) {
            setCognome(new StringFilter());
        }
        return cognome;
    }

    public void setCognome(StringFilter cognome) {
        this.cognome = cognome;
    }

    public LocalDateFilter getDataDiNascita() {
        return dataDiNascita;
    }

    public Optional<LocalDateFilter> optionalDataDiNascita() {
        return Optional.ofNullable(dataDiNascita);
    }

    public LocalDateFilter dataDiNascita() {
        if (dataDiNascita == null) {
            setDataDiNascita(new LocalDateFilter());
        }
        return dataDiNascita;
    }

    public void setDataDiNascita(LocalDateFilter dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public StringFilter getCodiceFiscale() {
        return codiceFiscale;
    }

    public Optional<StringFilter> optionalCodiceFiscale() {
        return Optional.ofNullable(codiceFiscale);
    }

    public StringFilter codiceFiscale() {
        if (codiceFiscale == null) {
            setCodiceFiscale(new StringFilter());
        }
        return codiceFiscale;
    }

    public void setCodiceFiscale(StringFilter codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public StringFilter getCodiceCliente() {
        return codiceCliente;
    }

    public Optional<StringFilter> optionalCodiceCliente() {
        return Optional.ofNullable(codiceCliente);
    }

    public StringFilter codiceCliente() {
        if (codiceCliente == null) {
            setCodiceCliente(new StringFilter());
        }
        return codiceCliente;
    }

    public void setCodiceCliente(StringFilter codiceCliente) {
        this.codiceCliente = codiceCliente;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTelefono() {
        return telefono;
    }

    public Optional<StringFilter> optionalTelefono() {
        return Optional.ofNullable(telefono);
    }

    public StringFilter telefono() {
        if (telefono == null) {
            setTelefono(new StringFilter());
        }
        return telefono;
    }

    public void setTelefono(StringFilter telefono) {
        this.telefono = telefono;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClienteCriteria that = (ClienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(cognome, that.cognome) &&
            Objects.equals(dataDiNascita, that.dataDiNascita) &&
            Objects.equals(codiceFiscale, that.codiceFiscale) &&
            Objects.equals(codiceCliente, that.codiceCliente) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, cognome, dataDiNascita, codiceFiscale, codiceCliente, email, telefono, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNome().map(f -> "nome=" + f + ", ").orElse("") +
            optionalCognome().map(f -> "cognome=" + f + ", ").orElse("") +
            optionalDataDiNascita().map(f -> "dataDiNascita=" + f + ", ").orElse("") +
            optionalCodiceFiscale().map(f -> "codiceFiscale=" + f + ", ").orElse("") +
            optionalCodiceCliente().map(f -> "codiceCliente=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalTelefono().map(f -> "telefono=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
