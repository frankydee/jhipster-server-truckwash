package com.sys_integrator.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sys_integrator.domain.SessionLog;

import com.sys_integrator.repository.SessionLogRepository;
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
 * REST controller for managing SessionLog.
 */
@RestController
@RequestMapping("/api")
public class SessionLogResource {

    private final Logger log = LoggerFactory.getLogger(SessionLogResource.class);

    private static final String ENTITY_NAME = "sessionLog";

    private final SessionLogRepository sessionLogRepository;

    public SessionLogResource(SessionLogRepository sessionLogRepository) {
        this.sessionLogRepository = sessionLogRepository;
    }

    /**
     * POST  /session-logs : Create a new sessionLog.
     *
     * @param sessionLog the sessionLog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sessionLog, or with status 400 (Bad Request) if the sessionLog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/session-logs")
    @Timed
    public ResponseEntity<SessionLog> createSessionLog(@RequestBody SessionLog sessionLog) throws URISyntaxException {
        log.debug("REST request to save SessionLog : {}", sessionLog);
        if (sessionLog.getId() != null) {
            throw new BadRequestAlertException("A new sessionLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SessionLog result = sessionLogRepository.save(sessionLog);
        return ResponseEntity.created(new URI("/api/session-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /session-logs : Updates an existing sessionLog.
     *
     * @param sessionLog the sessionLog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sessionLog,
     * or with status 400 (Bad Request) if the sessionLog is not valid,
     * or with status 500 (Internal Server Error) if the sessionLog couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/session-logs")
    @Timed
    public ResponseEntity<SessionLog> updateSessionLog(@RequestBody SessionLog sessionLog) throws URISyntaxException {
        log.debug("REST request to update SessionLog : {}", sessionLog);
        if (sessionLog.getId() == null) {
            return createSessionLog(sessionLog);
        }
        SessionLog result = sessionLogRepository.save(sessionLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sessionLog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /session-logs : get all the sessionLogs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sessionLogs in body
     */
    @GetMapping("/session-logs")
    @Timed
    public List<SessionLog> getAllSessionLogs() {
        log.debug("REST request to get all SessionLogs");
        return sessionLogRepository.findAll();
        }

    /**
     * GET  /session-logs/:id : get the "id" sessionLog.
     *
     * @param id the id of the sessionLog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sessionLog, or with status 404 (Not Found)
     */
    @GetMapping("/session-logs/{id}")
    @Timed
    public ResponseEntity<SessionLog> getSessionLog(@PathVariable Long id) {
        log.debug("REST request to get SessionLog : {}", id);
        SessionLog sessionLog = sessionLogRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sessionLog));
    }

    /**
     * DELETE  /session-logs/:id : delete the "id" sessionLog.
     *
     * @param id the id of the sessionLog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/session-logs/{id}")
    @Timed
    public ResponseEntity<Void> deleteSessionLog(@PathVariable Long id) {
        log.debug("REST request to delete SessionLog : {}", id);
        sessionLogRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
