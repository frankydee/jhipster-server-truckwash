package com.sys_integrator.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sys_integrator.domain.ProductOrService;

import com.sys_integrator.repository.ProductOrServiceRepository;
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
 * REST controller for managing ProductOrService.
 */
@RestController
@RequestMapping("/api")
public class ProductOrServiceResource {

    private final Logger log = LoggerFactory.getLogger(ProductOrServiceResource.class);

    private static final String ENTITY_NAME = "productOrService";

    private final ProductOrServiceRepository productOrServiceRepository;

    public ProductOrServiceResource(ProductOrServiceRepository productOrServiceRepository) {
        this.productOrServiceRepository = productOrServiceRepository;
    }

    /**
     * POST  /product-or-services : Create a new productOrService.
     *
     * @param productOrService the productOrService to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productOrService, or with status 400 (Bad Request) if the productOrService has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/product-or-services")
    @Timed
    public ResponseEntity<ProductOrService> createProductOrService(@RequestBody ProductOrService productOrService) throws URISyntaxException {
        log.debug("REST request to save ProductOrService : {}", productOrService);
        if (productOrService.getId() != null) {
            throw new BadRequestAlertException("A new productOrService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductOrService result = productOrServiceRepository.save(productOrService);
        return ResponseEntity.created(new URI("/api/product-or-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-or-services : Updates an existing productOrService.
     *
     * @param productOrService the productOrService to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productOrService,
     * or with status 400 (Bad Request) if the productOrService is not valid,
     * or with status 500 (Internal Server Error) if the productOrService couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/product-or-services")
    @Timed
    public ResponseEntity<ProductOrService> updateProductOrService(@RequestBody ProductOrService productOrService) throws URISyntaxException {
        log.debug("REST request to update ProductOrService : {}", productOrService);
        if (productOrService.getId() == null) {
            return createProductOrService(productOrService);
        }
        ProductOrService result = productOrServiceRepository.save(productOrService);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productOrService.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-or-services : get all the productOrServices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productOrServices in body
     */
    @GetMapping("/product-or-services")
    @Timed
    public List<ProductOrService> getAllProductOrServices() {
        log.debug("REST request to get all ProductOrServices");
        return productOrServiceRepository.findAll();
        }

    /**
     * GET  /product-or-services/:id : get the "id" productOrService.
     *
     * @param id the id of the productOrService to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productOrService, or with status 404 (Not Found)
     */
    @GetMapping("/product-or-services/{id}")
    @Timed
    public ResponseEntity<ProductOrService> getProductOrService(@PathVariable Long id) {
        log.debug("REST request to get ProductOrService : {}", id);
        ProductOrService productOrService = productOrServiceRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(productOrService));
    }

    /**
     * DELETE  /product-or-services/:id : delete the "id" productOrService.
     *
     * @param id the id of the productOrService to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/product-or-services/{id}")
    @Timed
    public ResponseEntity<Void> deleteProductOrService(@PathVariable Long id) {
        log.debug("REST request to delete ProductOrService : {}", id);
        productOrServiceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
