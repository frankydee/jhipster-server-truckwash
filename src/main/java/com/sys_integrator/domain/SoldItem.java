package com.sys_integrator.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A SoldItem.
 */
@Entity
@Table(name = "sold_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SoldItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "totaliser_start")
    private Double totaliserStart;

    @Column(name = "totaliser_end")
    private Double totaliserEnd;

    @Column(name = "quantity")
    private Double quantity;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotaliserStart() {
        return totaliserStart;
    }

    public SoldItem totaliserStart(Double totaliserStart) {
        this.totaliserStart = totaliserStart;
        return this;
    }

    public void setTotaliserStart(Double totaliserStart) {
        this.totaliserStart = totaliserStart;
    }

    public Double getTotaliserEnd() {
        return totaliserEnd;
    }

    public SoldItem totaliserEnd(Double totaliserEnd) {
        this.totaliserEnd = totaliserEnd;
        return this;
    }

    public void setTotaliserEnd(Double totaliserEnd) {
        this.totaliserEnd = totaliserEnd;
    }

    public Double getQuantity() {
        return quantity;
    }

    public SoldItem quantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
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
        SoldItem soldItem = (SoldItem) o;
        if (soldItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), soldItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SoldItem{" +
            "id=" + getId() +
            ", totaliserStart=" + getTotaliserStart() +
            ", totaliserEnd=" + getTotaliserEnd() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
