package com.sys_integrator.repository;

import com.sys_integrator.domain.SessionLog;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SessionLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionLogRepository extends JpaRepository<SessionLog, Long> {

}
