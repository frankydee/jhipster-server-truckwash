package com.sys_integrator.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sys_integrator.domain.SoldItem;

import com.sys_integrator.repository.SoldItemRepository;
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
 * REST controller for managing SoldItem.
 */
@RestController
@RequestMapping("/api")
public class SoldItemResource {

    private final Logger log = LoggerFactory.getLogger(SoldItemResource.class);

    private static final String ENTITY_NAME = "soldItem";

    private final SoldItemRepository soldItemRepository;

    public SoldItemResource(SoldItemRepository soldItemRepository) {
        this.soldItemRepository = soldItemRepository;
    }

    /**
     * POST  /sold-items : Create a new soldItem.
     *
     * @param soldItem the soldItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new soldItem, or with status 400 (Bad Request) if the soldItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sold-items")
    @Timed
    public ResponseEntity<SoldItem> createSoldItem(@RequestBody SoldItem soldItem) throws URISyntaxException {
        log.debug("REST request to save SoldItem : {}", soldItem);
        if (soldItem.getId() != null) {
            throw new BadRequestAlertException("A new soldItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SoldItem result = soldItemRepository.save(soldItem);
        return ResponseEntity.created(new URI("/api/sold-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sold-items : Updates an existing soldItem.
     *
     * @param soldItem the soldItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated soldItem,
     * or with status 400 (Bad Request) if the soldItem is not valid,
     * or with status 500 (Internal Server Error) if the soldItem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sold-items")
    @Timed
    public ResponseEntity<SoldItem> updateSoldItem(@RequestBody SoldItem soldItem) throws URISyntaxException {
        log.debug("REST request to update SoldItem : {}", soldItem);
        if (soldItem.getId() == null) {
            return createSoldItem(soldItem);
        }
        SoldItem result = soldItemRepository.save(soldItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, soldItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sold-items : get all the soldItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of soldItems in body
     */
    @GetMapping("/sold-items")
    @Timed
    public List<SoldItem> getAllSoldItems() {
        log.debug("REST request to get all SoldItems");
        return soldItemRepository.findAll();
        }

    /**
     * GET  /sold-items/:id : get the "id" soldItem.
     *
     * @param id the id of the soldItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the soldItem, or with status 404 (Not Found)
     */
    @GetMapping("/sold-items/{id}")
    @Timed
    public ResponseEntity<SoldItem> getSoldItem(@PathVariable Long id) {
        log.debug("REST request to get SoldItem : {}", id);
        SoldItem soldItem = soldItemRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(soldItem));
    }

    /**
     * DELETE  /sold-items/:id : delete the "id" soldItem.
     *
     * @param id the id of the soldItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sold-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteSoldItem(@PathVariable Long id) {
        log.debug("REST request to delete SoldItem : {}", id);
        soldItemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
