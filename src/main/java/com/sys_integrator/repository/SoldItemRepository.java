package com.sys_integrator.repository;

import com.sys_integrator.domain.SoldItem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SoldItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SoldItemRepository extends JpaRepository<SoldItem, Long> {

}
