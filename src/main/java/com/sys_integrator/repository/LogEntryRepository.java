package com.sys_integrator.repository;

import com.sys_integrator.domain.LogEntry;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the LogEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {

}
