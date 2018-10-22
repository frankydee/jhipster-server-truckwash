package com.sys_integrator.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.sys_integrator.domain.enumeration.SessionEndingCode;

import com.sys_integrator.domain.enumeration.SessionStatus;

import com.sys_integrator.domain.enumeration.LoggingLevel;

/**
 * A Session.
 */
@Entity
@Table(name = "session")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time")
    private LocalDate startTime;

    @Column(name = "end_time")
    private LocalDate endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "ending_code")
    private SessionEndingCode endingCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SessionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "logging_level")
    private LoggingLevel loggingLevel;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public Session startTime(LocalDate startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public Session endTime(LocalDate endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public SessionEndingCode getEndingCode() {
        return endingCode;
    }

    public Session endingCode(SessionEndingCode endingCode) {
        this.endingCode = endingCode;
        return this;
    }

    public void setEndingCode(SessionEndingCode endingCode) {
        this.endingCode = endingCode;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public Session status(SessionStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public LoggingLevel getLoggingLevel() {
        return loggingLevel;
    }

    public Session loggingLevel(LoggingLevel loggingLevel) {
        this.loggingLevel = loggingLevel;
        return this;
    }

    public void setLoggingLevel(LoggingLevel loggingLevel) {
        this.loggingLevel = loggingLevel;
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
        Session session = (Session) o;
        if (session.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), session.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Session{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", endingCode='" + getEndingCode() + "'" +
            ", status='" + getStatus() + "'" +
            ", loggingLevel='" + getLoggingLevel() + "'" +
            "}";
    }
}
