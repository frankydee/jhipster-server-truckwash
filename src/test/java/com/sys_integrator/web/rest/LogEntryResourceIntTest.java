package com.sys_integrator.web.rest;

import com.sys_integrator.TruckwashApp;

import com.sys_integrator.domain.LogEntry;
import com.sys_integrator.repository.LogEntryRepository;
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

/**
 * Test class for the LogEntryResource REST controller.
 *
 * @see LogEntryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TruckwashApp.class)
public class LogEntryResourceIntTest {

    private static final LocalDate DEFAULT_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    @Autowired
    private LogEntryRepository logEntryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLogEntryMockMvc;

    private LogEntry logEntry;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LogEntryResource logEntryResource = new LogEntryResource(logEntryRepository);
        this.restLogEntryMockMvc = MockMvcBuilders.standaloneSetup(logEntryResource)
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
    public static LogEntry createEntity(EntityManager em) {
        LogEntry logEntry = new LogEntry()
            .time(DEFAULT_TIME)
            .message(DEFAULT_MESSAGE);
        return logEntry;
    }

    @Before
    public void initTest() {
        logEntry = createEntity(em);
    }

    @Test
    @Transactional
    public void createLogEntry() throws Exception {
        int databaseSizeBeforeCreate = logEntryRepository.findAll().size();

        // Create the LogEntry
        restLogEntryMockMvc.perform(post("/api/log-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(logEntry)))
            .andExpect(status().isCreated());

        // Validate the LogEntry in the database
        List<LogEntry> logEntryList = logEntryRepository.findAll();
        assertThat(logEntryList).hasSize(databaseSizeBeforeCreate + 1);
        LogEntry testLogEntry = logEntryList.get(logEntryList.size() - 1);
        assertThat(testLogEntry.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testLogEntry.getMessage()).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    public void createLogEntryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = logEntryRepository.findAll().size();

        // Create the LogEntry with an existing ID
        logEntry.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLogEntryMockMvc.perform(post("/api/log-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(logEntry)))
            .andExpect(status().isBadRequest());

        // Validate the LogEntry in the database
        List<LogEntry> logEntryList = logEntryRepository.findAll();
        assertThat(logEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLogEntries() throws Exception {
        // Initialize the database
        logEntryRepository.saveAndFlush(logEntry);

        // Get all the logEntryList
        restLogEntryMockMvc.perform(get("/api/log-entries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(logEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }

    @Test
    @Transactional
    public void getLogEntry() throws Exception {
        // Initialize the database
        logEntryRepository.saveAndFlush(logEntry);

        // Get the logEntry
        restLogEntryMockMvc.perform(get("/api/log-entries/{id}", logEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(logEntry.getId().intValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLogEntry() throws Exception {
        // Get the logEntry
        restLogEntryMockMvc.perform(get("/api/log-entries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLogEntry() throws Exception {
        // Initialize the database
        logEntryRepository.saveAndFlush(logEntry);
        int databaseSizeBeforeUpdate = logEntryRepository.findAll().size();

        // Update the logEntry
        LogEntry updatedLogEntry = logEntryRepository.findOne(logEntry.getId());
        // Disconnect from session so that the updates on updatedLogEntry are not directly saved in db
        em.detach(updatedLogEntry);
        updatedLogEntry
            .time(UPDATED_TIME)
            .message(UPDATED_MESSAGE);

        restLogEntryMockMvc.perform(put("/api/log-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLogEntry)))
            .andExpect(status().isOk());

        // Validate the LogEntry in the database
        List<LogEntry> logEntryList = logEntryRepository.findAll();
        assertThat(logEntryList).hasSize(databaseSizeBeforeUpdate);
        LogEntry testLogEntry = logEntryList.get(logEntryList.size() - 1);
        assertThat(testLogEntry.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testLogEntry.getMessage()).isEqualTo(UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingLogEntry() throws Exception {
        int databaseSizeBeforeUpdate = logEntryRepository.findAll().size();

        // Create the LogEntry

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLogEntryMockMvc.perform(put("/api/log-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(logEntry)))
            .andExpect(status().isCreated());

        // Validate the LogEntry in the database
        List<LogEntry> logEntryList = logEntryRepository.findAll();
        assertThat(logEntryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLogEntry() throws Exception {
        // Initialize the database
        logEntryRepository.saveAndFlush(logEntry);
        int databaseSizeBeforeDelete = logEntryRepository.findAll().size();

        // Get the logEntry
        restLogEntryMockMvc.perform(delete("/api/log-entries/{id}", logEntry.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LogEntry> logEntryList = logEntryRepository.findAll();
        assertThat(logEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LogEntry.class);
        LogEntry logEntry1 = new LogEntry();
        logEntry1.setId(1L);
        LogEntry logEntry2 = new LogEntry();
        logEntry2.setId(logEntry1.getId());
        assertThat(logEntry1).isEqualTo(logEntry2);
        logEntry2.setId(2L);
        assertThat(logEntry1).isNotEqualTo(logEntry2);
        logEntry1.setId(null);
        assertThat(logEntry1).isNotEqualTo(logEntry2);
    }
}
