package com.sys_integrator.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A company.
 * <p>
 * Currently, only companies can buy products
 * and services from Tvatthallen (because only companies
 * can be invoiced),every user/customer has to belong to a
 * company.
 * </p>
 */
@ApiModel(description = "A company.<p>Currently, only companies can buy products and services from Tvatthallen (because only companies can be invoiced),every user/customer has to belong to a company.</p>")
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the company
     */
    @NotNull
    @ApiModelProperty(value = "The name of the company", required = true)
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * The company registration number
     */
    @ApiModelProperty(value = "The company registration number")
    @Column(name = "registration_number")
    private Integer registrationNumber;

    /**
     * External unique identifier for that company
     * in the invoicing system
     */
    @ApiModelProperty(value = "External unique identifier for that company in the invoicing system")
    @Column(name = "external_reference_id")
    private String externalReferenceId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Company name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRegistrationNumber() {
        return registrationNumber;
    }

    public Company registrationNumber(Integer registrationNumber) {
        this.registrationNumber = registrationNumber;
        return this;
    }

    public void setRegistrationNumber(Integer registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getExternalReferenceId() {
        return externalReferenceId;
    }

    public Company externalReferenceId(String externalReferenceId) {
        this.externalReferenceId = externalReferenceId;
        return this;
    }

    public void setExternalReferenceId(String externalReferenceId) {
        this.externalReferenceId = externalReferenceId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Company company = (Company) o;
        if (company.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), company.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", registrationNumber=" + getRegistrationNumber() +
            ", externalReferenceId='" + getExternalReferenceId() + "'" +
            "}";
    }
}
