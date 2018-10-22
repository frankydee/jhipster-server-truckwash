package com.sys_integrator.domain;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.sys_integrator.domain.enumeration.VehicleType;

/**
 * A Vehicle.
 */
@Entity
@Table(name = "vehicle")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * A short description identifying the vehicle
     * e.g. \"Lastbil 6\" or \"White Audi\"
     */
    @NotNull
    @ApiModelProperty(value = "A short description identifying the vehicle e.g. 'Lastbil 6' or 'White Audi'", required = true)
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * Vehicle registration number (a.k.a. number plate)
     */
    @NotNull
    @ApiModelProperty(value = "Vehicle registration number (a.k.a. number plate)", required = true)
    @Column(name = "registration_nummer", nullable = false)
    private String registrationNummer;

    /**
     * Vehicle Type: A car or a Van
     */
    @ApiModelProperty(value = "Vehicle Type: A car or a Van")
    @Enumerated(EnumType.STRING)
    @Column(name = "_type")
    private VehicleType type;

    /**
     * Whether it is the default vehicle for the user
     */
    @ApiModelProperty(value = "Whether it is the default vehicle for the user")
    @Column(name = "default_flag")
    private Boolean defaultFlag;

    /**
     * Sort order with which it appears on the screen
     */
    @ApiModelProperty(value = "Sort order with which it appears on the screen")
    @Column(name = "sort_order")
    private Integer sortOrder;

    /**
     * Active flag:
     * If the vehicle is active, it will appear in the
     * UI to be selected. Otherwise, it will appear only
     * against historical data (like sessions)
     */
    @ApiModelProperty(value = "Active flag: If the vehicle is active, it will appear in the UI to be selected. Otherwise, it will appear only against historical data (like sessions)")
    @Column(name = "active")
    private Boolean active;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Vehicle description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegistrationNummer() {
        return registrationNummer;
    }

    public Vehicle registrationNummer(String registrationNummer) {
        this.registrationNummer = registrationNummer;
        return this;
    }

    public void setRegistrationNummer(String registrationNummer) {
        this.registrationNummer = registrationNummer;
    }

    public VehicleType getType() {
        return type;
    }

    public Vehicle type(VehicleType type) {
        this.type = type;
        return this;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public Boolean isDefaultFlag() {
        return defaultFlag;
    }

    public Vehicle defaultFlag(Boolean defaultFlag) {
        this.defaultFlag = defaultFlag;
        return this;
    }

    public void setDefaultFlag(Boolean defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public Vehicle sortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean isActive() {
        return active;
    }

    public Vehicle active(Boolean active) {
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
        Vehicle vehicle = (Vehicle) o;
        if (vehicle.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vehicle.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Vehicle{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", registrationNummer='" + getRegistrationNummer() + "'" +
            ", type='" + getType() + "'" +
            ", defaultFlag='" + isDefaultFlag() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", active='" + isActive() + "'" +
            "}";
    }
}
