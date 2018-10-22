package com.sys_integrator.web.rest;

import com.sys_integrator.TruckwashApp;

import com.sys_integrator.domain.Session;
import com.sys_integrator.repository.SessionRepository;
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

import com.sys_integrator.domain.enumeration.SessionEndingCode;
import com.sys_integrator.domain.enumeration.SessionStatus;
import com.sys_integrator.domain.enumeration.LoggingLevel;
/**
 * Test class for the SessionResource REST controller.
 *
 * @see SessionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TruckwashApp.class)
public class SessionResourceIntTest {

    private static final LocalDate DEFAULT_START_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final SessionEndingCode DEFAULT_ENDING_CODE = SessionEndingCode.SYSTEM_TIMEOUT;
    private static final SessionEndingCode UPDATED_ENDING_CODE = SessionEndingCode.SERVICE_TIME_LIMIT;

    private static final SessionStatus DEFAULT_STATUS = SessionStatus.IN_PROGRESS;
    private static final SessionStatus UPDATED_STATUS = SessionStatus.COMPLETED;

    private static final LoggingLevel DEFAULT_LOGGING_LEVEL = LoggingLevel.ERROR;
    private static final LoggingLevel UPDATED_LOGGING_LEVEL = LoggingLevel.WARNING;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSessionMockMvc;

    private Session session;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SessionResource sessionResource = new SessionResource(sessionRepository);
        this.restSessionMockMvc = MockMvcBuilders.standaloneSetup(sessionResource)
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
    public static Session createEntity(EntityManager em) {
        Session session = new Session()
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .endingCode(DEFAULT_ENDING_CODE)
            .status(DEFAULT_STATUS)
            .loggingLevel(DEFAULT_LOGGING_LEVEL);
        return session;
    }

    @Before
    public void initTest() {
        session = createEntity(em);
    }

    @Test
    @Transactional
    public void createSession() throws Exception {
        int databaseSizeBeforeCreate = sessionRepository.findAll().size();

        // Create the Session
        restSessionMockMvc.perform(post("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isCreated());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeCreate + 1);
        Session testSession = sessionList.get(sessionList.size() - 1);
        assertThat(testSession.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testSession.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testSession.getEndingCode()).isEqualTo(DEFAULT_ENDING_CODE);
        assertThat(testSession.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSession.getLoggingLevel()).isEqualTo(DEFAULT_LOGGING_LEVEL);
    }

    @Test
    @Transactional
    public void createSessionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sessionRepository.findAll().size();

        // Create the Session with an existing ID
        session.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionMockMvc.perform(post("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSessions() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get all the sessionList
        restSessionMockMvc.perform(get("/api/sessions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(session.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].endingCode").value(hasItem(DEFAULT_ENDING_CODE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].loggingLevel").value(hasItem(DEFAULT_LOGGING_LEVEL.toString())));
    }

    @Test
    @Transactional
    public void getSession() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get the session
        restSessionMockMvc.perform(get("/api/sessions/{id}", session.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(session.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.endingCode").value(DEFAULT_ENDING_CODE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.loggingLevel").value(DEFAULT_LOGGING_LEVEL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSession() throws Exception {
        // Get the session
        restSessionMockMvc.perform(get("/api/sessions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSession() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);
        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();

        // Update the session
        Session updatedSession = sessionRepository.findOne(session.getId());
        // Disconnect from session so that the updates on updatedSession are not directly saved in db
        em.detach(updatedSession);
        updatedSession
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .endingCode(UPDATED_ENDING_CODE)
            .status(UPDATED_STATUS)
            .loggingLevel(UPDATED_LOGGING_LEVEL);

        restSessionMockMvc.perform(put("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSession)))
            .andExpect(status().isOk());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
        Session testSession = sessionList.get(sessionList.size() - 1);
        assertThat(testSession.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testSession.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testSession.getEndingCode()).isEqualTo(UPDATED_ENDING_CODE);
        assertThat(testSession.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSession.getLoggingLevel()).isEqualTo(UPDATED_LOGGING_LEVEL);
    }

    @Test
    @Transactional
    public void updateNonExistingSession() throws Exception {
        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();

        // Create the Session

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSessionMockMvc.perform(put("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isCreated());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSession() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);
        int databaseSizeBeforeDelete = sessionRepository.findAll().size();

        // Get the session
        restSessionMockMvc.perform(delete("/api/sessions/{id}", session.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Session.class);
        Session session1 = new Session();
        session1.setId(1L);
        Session session2 = new Session();
        session2.setId(session1.getId());
        assertThat(session1).isEqualTo(session2);
        session2.setId(2L);
        assertThat(session1).isNotEqualTo(session2);
        session1.setId(null);
        assertThat(session1).isNotEqualTo(session2);
    }
}
