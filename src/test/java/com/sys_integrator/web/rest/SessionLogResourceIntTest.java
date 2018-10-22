package com.sys_integrator.web.rest;

import com.sys_integrator.TruckwashApp;

import com.sys_integrator.domain.SessionLog;
import com.sys_integrator.repository.SessionLogRepository;
import com.sys_integrator.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.sys_integrator.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sys_integrator.domain.enumeration.LogType;
/**
 * Test class for the SessionLogResource REST controller.
 *
 * @see SessionLogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TruckwashApp.class)
public class SessionLogResourceIntTest {

    private static final LogType DEFAULT_LOG_TYPE = LogType.DB;
    private static final LogType UPDATED_LOG_TYPE = LogType.FILE;

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    @Autowired
    private SessionLogRepository sessionLogRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSessionLogMockMvc;

    private SessionLog sessionLog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SessionLogResource sessionLogResource = new SessionLogResource(sessionLogRepository);
        this.restSessionLogMockMvc = MockMvcBuilders.standaloneSetup(sessionLogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionLog createEntity(EntityManager em) {
        SessionLog sessionLog = new SessionLog()
            .logType(DEFAULT_LOG_TYPE)
            .createDate(DEFAULT_CREATE_DATE)
            .filePath(DEFAULT_FILE_PATH);
        return sessionLog;
    }

    @Before
    public void initTest() {
        sessionLog = createEntity(em);
    }

    @Test
    @Transactional
    public void createSessionLog() throws Exception {
        int databaseSizeBeforeCreate = sessionLogRepository.findAll().size();

        // Create the SessionLog
        restSessionLogMockMvc.perform(post("/api/session-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sessionLog)))
            .andExpect(status().isCreated());

        // Validate the SessionLog in the database
        List<SessionLog> sessionLogList = sessionLogRepository.findAll();
        assertThat(sessionLogList).hasSize(databaseSizeBeforeCreate + 1);
        SessionLog testSessionLog = sessionLogList.get(sessionLogList.size() - 1);
        assertThat(testSessionLog.getLogType()).isEqualTo(DEFAULT_LOG_TYPE);
        assertThat(testSessionLog.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSessionLog.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
    }

    @Test
    @Transactional
    public void createSessionLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sessionLogRepository.findAll().size();

        // Create the SessionLog with an existing ID
        sessionLog.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionLogMockMvc.perform(post("/api/session-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sessionLog)))
            .andExpect(status().isBadRequest());

        // Validate the SessionLog in the database
        List<SessionLog> sessionLogList = sessionLogRepository.findAll();
        assertThat(sessionLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSessionLogs() throws Exception {
        // Initialize the database
        sessionLogRepository.saveAndFlush(sessionLog);

        // Get all the sessionLogList
        restSessionLogMockMvc.perform(get("/api/session-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].logType").value(hasItem(DEFAULT_LOG_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH.toString())));
    }

    @Test
    @Transactional
    public void getSessionLog() throws Exception {
        // Initialize the database
        sessionLogRepository.saveAndFlush(sessionLog);

        // Get the sessionLog
        restSessionLogMockMvc.perform(get("/api/session-logs/{id}", sessionLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sessionLog.getId().intValue()))
            .andExpect(jsonPath("$.logType").value(DEFAULT_LOG_TYPE.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSessionLog() throws Exception {
        // Get the sessionLog
        restSessionLogMockMvc.perform(get("/api/session-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSessionLog() throws Exception {
        // Initialize the database
        sessionLogRepository.saveAndFlush(sessionLog);
        int databaseSizeBeforeUpdate = sessionLogRepository.findAll().size();

        // Update the sessionLog
        SessionLog updatedSessionLog = sessionLogRepository.findOne(sessionLog.getId());
        // Disconnect from session so that the updates on updatedSessionLog are not directly saved in db
        em.detach(updatedSessionLog);
        updatedSessionLog
            .logType(UPDATED_LOG_TYPE)
            .createDate(UPDATED_CREATE_DATE)
            .filePath(UPDATED_FILE_PATH);

        restSessionLogMockMvc.perform(put("/api/session-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSessionLog)))
            .andExpect(status().isOk());

        // Validate the SessionLog in the database
        List<SessionLog> sessionLogList = sessionLogRepository.findAll();
        assertThat(sessionLogList).hasSize(databaseSizeBeforeUpdate);
        SessionLog testSessionLog = sessionLogList.get(sessionLogList.size() - 1);
        assertThat(testSessionLog.getLogType()).isEqualTo(UPDATED_LOG_TYPE);
        assertThat(testSessionLog.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSessionLog.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    public void updateNonExistingSessionLog() throws Exception {
        int databaseSizeBeforeUpdate = sessionLogRepository.findAll().size();

        // Create the SessionLog

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSessionLogMockMvc.perform(put("/api/session-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sessionLog)))
            .andExpect(status().isCreated());

        // Validate the SessionLog in the database
        List<SessionLog> sessionLogList = sessionLogRepository.findAll();
        assertThat(sessionLogList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSessionLog() throws Exception {
        // Initialize the database
        sessionLogRepository.saveAndFlush(sessionLog);
        int databaseSizeBeforeDelete = sessionLogRepository.findAll().size();

        // Get the sessionLog
        restSessionLogMockMvc.perform(delete("/api/session-logs/{id}", sessionLog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SessionLog> sessionLogList = sessionLogRepository.findAll();
        assertThat(sessionLogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionLog.class);
        SessionLog sessionLog1 = new SessionLog();
        sessionLog1.setId(1L);
        SessionLog sessionLog2 = new SessionLog();
        sessionLog2.setId(sessionLog1.getId());
        assertThat(sessionLog1).isEqualTo(sessionLog2);
        sessionLog2.setId(2L);
        assertThat(sessionLog1).isNotEqualTo(sessionLog2);
        sessionLog1.setId(null);
        assertThat(sessionLog1).isNotEqualTo(sessionLog2);
    }
}
