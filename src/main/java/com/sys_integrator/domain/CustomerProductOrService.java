package com.sys_integrator.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A CustomerProductOrService.
 */
@Entity
@Table(name = "customer_product_or_service")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CustomerProductOrService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "percent_discount")
    private Integer percentDiscount;

    @Column(name = "overriding_price_per_unit")
    private Integer overridingPricePerUnit;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPercentDiscount() {
        return percentDiscount;
    }

    public CustomerProductOrService percentDiscount(Integer percentDiscount) {
        this.percentDiscount = percentDiscount;
        return this;
    }

    public void setPercentDiscount(Integer percentDiscount) {
        this.percentDiscount = percentDiscount;
    }

    public Integer getOverridingPricePerUnit() {
        return overridingPricePerUnit;
    }

    public CustomerProductOrService overridingPricePerUnit(Integer overridingPricePerUnit) {
        this.overridingPricePerUnit = overridingPricePerUnit;
        return this;
    }

    public void setOverridingPricePerUnit(Integer overridingPricePerUnit) {
        this.overridingPricePerUnit = overridingPricePerUnit;
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
        CustomerProductOrService customerProductOrService = (CustomerProductOrService) o;
        if (customerProductOrService.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerProductOrService.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerProductOrService{" +
            "id=" + getId() +
            ", percentDiscount=" + getPercentDiscount() +
            ", overridingPricePerUnit=" + getOverridingPricePerUnit() +
            "}";
    }
}
