package com.sys_integrator.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A LogEntry.
 */
@Entity
@Table(name = "log_entry")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LogEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "_time")
    private LocalDate time;

    @Column(name = "message")
    private String message;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTime() {
        return time;
    }

    public LogEntry time(LocalDate time) {
        this.time = time;
        return this;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public LogEntry message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
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
        LogEntry logEntry = (LogEntry) o;
        if (logEntry.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), logEntry.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LogEntry{" +
            "id=" + getId() +
            ", time='" + getTime() + "'" +
            ", message='" + getMessage() + "'" +
            "}";
    }
}
