package com.sys_integrator.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sys_integrator.domain.CustomerProductOrService;

import com.sys_integrator.repository.CustomerProductOrServiceRepository;
import com.sys_integrator.web.rest.errors.BadRequestAlertException;
import com.sys_integrator.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CustomerProductOrService.
 */
@RestController
@RequestMapping("/api")
public class CustomerProductOrServiceResource {

    private final Logger log = LoggerFactory.getLogger(CustomerProductOrServiceResource.class);

    private static final String ENTITY_NAME = "customerProductOrService";

    private final CustomerProductOrServiceRepository customerProductOrServiceRepository;

    public CustomerProductOrServiceResource(CustomerProductOrServiceRepository customerProductOrServiceRepository) {
        this.customerProductOrServiceRepository = customerProductOrServiceRepository;
    }

    /**
     * POST  /customer-product-or-services : Create a new customerProductOrService.
     *
     * @param customerProductOrService the customerProductOrService to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerProductOrService, or with status 400 (Bad Request) if the customerProductOrService has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customer-product-or-services")
    @Timed
    public ResponseEntity<CustomerProductOrService> createCustomerProductOrService(@RequestBody CustomerProductOrService customerProductOrService) throws URISyntaxException {
        log.debug("REST request to save CustomerProductOrService : {}", customerProductOrService);
        if (customerProductOrService.getId() != null) {
            throw new BadRequestAlertException("A new customerProductOrService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerProductOrService result = customerProductOrServiceRepository.save(customerProductOrService);
        return ResponseEntity.created(new URI("/api/customer-product-or-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customer-product-or-services : Updates an existing customerProductOrService.
     *
     * @param customerProductOrService the customerProductOrService to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerProductOrService,
     * or with status 400 (Bad Request) if the customerProductOrService is not valid,
     * or with status 500 (Internal Server Error) if the customerProductOrService couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/customer-product-or-services")
    @Timed
    public ResponseEntity<CustomerProductOrService> updateCustomerProductOrService(@RequestBody CustomerProductOrService customerProductOrService) throws URISyntaxException {
        log.debug("REST request to update CustomerProductOrService : {}", customerProductOrService);
        if (customerProductOrService.getId() == null) {
            return createCustomerProductOrService(customerProductOrService);
        }
        CustomerProductOrService result = customerProductOrServiceRepository.save(customerProductOrService);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customerProductOrService.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customer-product-or-services : get all the customerProductOrServices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of customerProductOrServices in body
     */
    @GetMapping("/customer-product-or-services")
    @Timed
    public List<CustomerProductOrService> getAllCustomerProductOrServices() {
        log.debug("REST request to get all CustomerProductOrServices");
        return customerProductOrServiceRepository.findAll();
        }

    /**
     * GET  /customer-product-or-services/:id : get the "id" customerProductOrService.
     *
     * @param id the id of the customerProductOrService to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerProductOrService, or with status 404 (Not Found)
     */
    @GetMapping("/customer-product-or-services/{id}")
    @Timed
    public ResponseEntity<CustomerProductOrService> getCustomerProductOrService(@PathVariable Long id) {
        log.debug("REST request to get CustomerProductOrService : {}", id);
        CustomerProductOrService customerProductOrService = customerProductOrServiceRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(customerProductOrService));
    }

    /**
     * DELETE  /customer-product-or-services/:id : delete the "id" customerProductOrService.
     *
     * @param id the id of the customerProductOrService to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/customer-product-or-services/{id}")
    @Timed
    public ResponseEntity<Void> deleteCustomerProductOrService(@PathVariable Long id) {
        log.debug("REST request to delete CustomerProductOrService : {}", id);
        customerProductOrServiceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
