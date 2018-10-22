package com.sys_integrator.repository;

import com.sys_integrator.domain.ProductOrService;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProductOrService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductOrServiceRepository extends JpaRepository<ProductOrService, Long> {

}
