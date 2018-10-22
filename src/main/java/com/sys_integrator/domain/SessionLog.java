package com.sys_integrator.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.sys_integrator.domain.enumeration.LogType;

/**
 * A SessionLog.
 */
@Entity
@Table(name = "session_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SessionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "log_type")
    private LogType logType;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "file_path")
    private String filePath;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LogType getLogType() {
        return logType;
    }

    public SessionLog logType(LogType logType) {
        this.logType = logType;
        return this;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public SessionLog createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public SessionLog filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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
        SessionLog sessionLog = (SessionLog) o;
        if (sessionLog.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sessionLog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SessionLog{" +
            "id=" + getId() +
            ", logType='" + getLogType() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", filePath='" + getFilePath() + "'" +
            "}";
    }
}
