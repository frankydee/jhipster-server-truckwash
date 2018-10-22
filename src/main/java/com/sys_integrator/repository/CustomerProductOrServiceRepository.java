package com.sys_integrator.repository;

import com.sys_integrator.domain.CustomerProductOrService;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CustomerProductOrService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerProductOrServiceRepository extends JpaRepository<CustomerProductOrService, Long> {

}
