package com.sys_integrator.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

import com.sys_integrator.domain.enumeration.UnitOfMeasure;

import com.sys_integrator.domain.enumeration.ProductOrServiceType;

import com.sys_integrator.domain.enumeration.ProductQuantityMetering;

/**
 * The ProductOrService entity
 * Can represent a product for sale (e.g. AdBlue)
 * or a service (like \"Washing a truck for 30 min\")
 */
@ApiModel(description = "The ProductOrService entity Can represent a product for sale (e.g. AdBlue) or a service (like 'Washing a truck for 30 min')")
@Entity
@Table(name = "product_or_service")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProductOrService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "long_description")
    private String longDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_of_meashre")
    private UnitOfMeasure unitOfMeashre;

    @Column(name = "price_per_unit")
    private Double pricePerUnit;

    /**
     * Is this a product or a service
     */
    @ApiModelProperty(value = "Is this a product or a service")
    @Enumerated(EnumType.STRING)
    @Column(name = "_type")
    private ProductOrServiceType type;

    /**
     * Maximum duration of the service in minutes.
     * 
     * This applies to services sold as a \"package\"
     * e.g.  30 min Truck Wash,
     * 15 min Car wash
     * 
     * If a service is sold by the minutes, this field is indicates
     * when the user will timeout.
     */
    @ApiModelProperty(value = "Maximum duration of the service in minutes. This applies to services sold as a 'package' e.g.  30 min Truck Wash, 15 min Car wash If a service is sold by the minutes, this field is indicates when the user will timeout.")
    @Column(name = "max_duration")
    private Integer maxDuration;

    /**
     * This is denormalised from the Inventory table for convenience
     * TODO:Should this be normalised here?
     */
    @ApiModelProperty(value = "This is denormalised from the Inventory table for convenience TODO:Should this be normalised here?")
    @Column(name = "quantity_in_stock")
    private Double quantityInStock;

    /**
     * Whether the Quantity in stock is being metered
     * 
     * This is applicable only for products.
     */
    @ApiModelProperty(value = "Whether the Quantity in stock is being metered This is applicable only for products.")
    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_metering")
    private ProductQuantityMetering quantityMetering;

    /**
     * Interval in minutes of how often a product quantity being auto-metered should be logged
     * (this applies especially where we have a product with a level meter)
     */
    @ApiModelProperty(value = "Interval in minutes of how often a product quantity being auto-metered should be logged (this applies especially where we have a product with a level meter)")
    @Column(name = "auto_metering_interval")
    private Integer autoMeteringInterval;

    /**
     * Quantity at which an alert will be issued
     */
    @ApiModelProperty(value = "Quantity at which an alert will be issued")
    @Column(name = "low_level_alert")
    private Boolean lowLevelAlert;

    @Column(name = "active")
    private Boolean active;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public ProductOrService shortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public ProductOrService longDescription(String longDescription) {
        this.longDescription = longDescription;
        return this;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public UnitOfMeasure getUnitOfMeashre() {
        return unitOfMeashre;
    }

    public ProductOrService unitOfMeashre(UnitOfMeasure unitOfMeashre) {
        this.unitOfMeashre = unitOfMeashre;
        return this;
    }

    public void setUnitOfMeashre(UnitOfMeasure unitOfMeashre) {
        this.unitOfMeashre = unitOfMeashre;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public ProductOrService pricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
        return this;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public ProductOrServiceType getType() {
        return type;
    }

    public ProductOrService type(ProductOrServiceType type) {
        this.type = type;
        return this;
    }

    public void setType(ProductOrServiceType type) {
        this.type = type;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public ProductOrService maxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
        return this;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Double getQuantityInStock() {
        return quantityInStock;
    }

    public ProductOrService quantityInStock(Double quantityInStock) {
        this.quantityInStock = quantityInStock;
        return this;
    }

    public void setQuantityInStock(Double quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public ProductQuantityMetering getQuantityMetering() {
        return quantityMetering;
    }

    public ProductOrService quantityMetering(ProductQuantityMetering quantityMetering) {
        this.quantityMetering = quantityMetering;
        return this;
    }

    public void setQuantityMetering(ProductQuantityMetering quantityMetering) {
        this.quantityMetering = quantityMetering;
    }

    public Integer getAutoMeteringInterval() {
        return autoMeteringInterval;
    }

    public ProductOrService autoMeteringInterval(Integer autoMeteringInterval) {
        this.autoMeteringInterval = autoMeteringInterval;
        return this;
    }

    public void setAutoMeteringInterval(Integer autoMeteringInterval) {
        this.autoMeteringInterval = autoMeteringInterval;
    }

    public Boolean isLowLevelAlert() {
        return lowLevelAlert;
    }

    public ProductOrService lowLevelAlert(Boolean lowLevelAlert) {
        this.lowLevelAlert = lowLevelAlert;
        return this;
    }

    public void setLowLevelAlert(Boolean lowLevelAlert) {
        this.lowLevelAlert = lowLevelAlert;
    }

    public Boolean isActive() {
        return active;
    }

    public ProductOrService active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        ProductOrService productOrService = (ProductOrService) o;
        if (productOrService.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productOrService.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductOrService{" +
            "id=" + getId() +
            ", shortDescription='" + getShortDescription() + "'" +
            ", longDescription='" + getLongDescription() + "'" +
            ", unitOfMeashre='" + getUnitOfMeashre() + "'" +
            ", pricePerUnit=" + getPricePerUnit() +
            ", type='" + getType() + "'" +
            ", maxDuration=" + getMaxDuration() +
            ", quantityInStock=" + getQuantityInStock() +
            ", quantityMetering='" + getQuantityMetering() + "'" +
            ", autoMeteringInterval=" + getAutoMeteringInterval() +
            ", lowLevelAlert='" + isLowLevelAlert() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
