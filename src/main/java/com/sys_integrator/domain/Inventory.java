package com.sys_integrator.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import com.sys_integrator.domain.enumeration.ProductInventoryEntry;

/**
 * Recording of a product inventory
 */
@ApiModel(description = "Recording of a product inventory")
@Entity
@Table(name = "inventory")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Time and date when the reading was taken
     */
    @ApiModelProperty(value = "Time and date when the reading was taken")
    @Column(name = "reading_time")
    private LocalDate readingTime;

    /**
     * Raw reading from the sensor (only applicable when entry type = AUTO)
     */
    @ApiModelProperty(value = "Raw reading from the sensor (only applicable when entry type = AUTO)")
    @Column(name = "senror_reading", precision=10, scale=2)
    private BigDecimal senrorReading;

    /**
     * Quantity either entered manually or calculated from a sensor reading
     */
    @ApiModelProperty(value = "Quantity either entered manually or calculated from a sensor reading")
    @Column(name = "quantity")
    private Double quantity;

    /**
     * Is this a manual or automatic entry?
     */
    @ApiModelProperty(value = "Is this a manual or automatic entry?")
    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type")
    private ProductInventoryEntry entryType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReadingTime() {
        return readingTime;
    }

    public Inventory readingTime(LocalDate readingTime) {
        this.readingTime = readingTime;
        return this;
    }

    public void setReadingTime(LocalDate readingTime) {
        this.readingTime = readingTime;
    }

    public BigDecimal getSenrorReading() {
        return senrorReading;
    }

    public Inventory senrorReading(BigDecimal senrorReading) {
        this.senrorReading = senrorReading;
        return this;
    }

    public void setSenrorReading(BigDecimal senrorReading) {
        this.senrorReading = senrorReading;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Inventory quantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public ProductInventoryEntry getEntryType() {
        return entryType;
    }

    public Inventory entryType(ProductInventoryEntry entryType) {
        this.entryType = entryType;
        return this;
    }

    public void setEntryType(ProductInventoryEntry entryType) {
        this.entryType = entryType;
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
        Inventory inventory = (Inventory) o;
        if (inventory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), inventory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Inventory{" +
            "id=" + getId() +
            ", readingTime='" + getReadingTime() + "'" +
            ", senrorReading=" + getSenrorReading() +
            ", quantity=" + getQuantity() +
            ", entryType='" + getEntryType() + "'" +
            "}";
    }
}
