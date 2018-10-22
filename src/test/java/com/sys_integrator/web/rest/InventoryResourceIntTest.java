package com.sys_integrator.web.rest;

import com.sys_integrator.TruckwashApp;

import com.sys_integrator.domain.Inventory;
import com.sys_integrator.repository.InventoryRepository;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.sys_integrator.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sys_integrator.domain.enumeration.ProductInventoryEntry;
/**
 * Test class for the InventoryResource REST controller.
 *
 * @see InventoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TruckwashApp.class)
public class InventoryResourceIntTest {

    private static final LocalDate DEFAULT_READING_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_READING_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_SENROR_READING = new BigDecimal(1);
    private static final BigDecimal UPDATED_SENROR_READING = new BigDecimal(2);

    private static final Double DEFAULT_QUANTITY = 1D;
    private static final Double UPDATED_QUANTITY = 2D;

    private static final ProductInventoryEntry DEFAULT_ENTRY_TYPE = ProductInventoryEntry.MANUAL;
    private static final ProductInventoryEntry UPDATED_ENTRY_TYPE = ProductInventoryEntry.AUTO;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInventoryMockMvc;

    private Inventory inventory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InventoryResource inventoryResource = new InventoryResource(inventoryRepository);
        this.restInventoryMockMvc = MockMvcBuilders.standaloneSetup(inventoryResource)
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
    public static Inventory createEntity(EntityManager em) {
        Inventory inventory = new Inventory()
            .readingTime(DEFAULT_READING_TIME)
            .senrorReading(DEFAULT_SENROR_READING)
            .quantity(DEFAULT_QUANTITY)
            .entryType(DEFAULT_ENTRY_TYPE);
        return inventory;
    }

    @Before
    public void initTest() {
        inventory = createEntity(em);
    }

    @Test
    @Transactional
    public void createInventory() throws Exception {
        int databaseSizeBeforeCreate = inventoryRepository.findAll().size();

        // Create the Inventory
        restInventoryMockMvc.perform(post("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventory)))
            .andExpect(status().isCreated());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeCreate + 1);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getReadingTime()).isEqualTo(DEFAULT_READING_TIME);
        assertThat(testInventory.getSenrorReading()).isEqualTo(DEFAULT_SENROR_READING);
        assertThat(testInventory.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testInventory.getEntryType()).isEqualTo(DEFAULT_ENTRY_TYPE);
    }

    @Test
    @Transactional
    public void createInventoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inventoryRepository.findAll().size();

        // Create the Inventory with an existing ID
        inventory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventoryMockMvc.perform(post("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventory)))
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllInventories() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList
        restInventoryMockMvc.perform(get("/api/inventories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].readingTime").value(hasItem(DEFAULT_READING_TIME.toString())))
            .andExpect(jsonPath("$.[*].senrorReading").value(hasItem(DEFAULT_SENROR_READING.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].entryType").value(hasItem(DEFAULT_ENTRY_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getInventory() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get the inventory
        restInventoryMockMvc.perform(get("/api/inventories/{id}", inventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(inventory.getId().intValue()))
            .andExpect(jsonPath("$.readingTime").value(DEFAULT_READING_TIME.toString()))
            .andExpect(jsonPath("$.senrorReading").value(DEFAULT_SENROR_READING.intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.entryType").value(DEFAULT_ENTRY_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInventory() throws Exception {
        // Get the inventory
        restInventoryMockMvc.perform(get("/api/inventories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInventory() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();

        // Update the inventory
        Inventory updatedInventory = inventoryRepository.findOne(inventory.getId());
        // Disconnect from session so that the updates on updatedInventory are not directly saved in db
        em.detach(updatedInventory);
        updatedInventory
            .readingTime(UPDATED_READING_TIME)
            .senrorReading(UPDATED_SENROR_READING)
            .quantity(UPDATED_QUANTITY)
            .entryType(UPDATED_ENTRY_TYPE);

        restInventoryMockMvc.perform(put("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInventory)))
            .andExpect(status().isOk());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getReadingTime()).isEqualTo(UPDATED_READING_TIME);
        assertThat(testInventory.getSenrorReading()).isEqualTo(UPDATED_SENROR_READING);
        assertThat(testInventory.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInventory.getEntryType()).isEqualTo(UPDATED_ENTRY_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();

        // Create the Inventory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInventoryMockMvc.perform(put("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventory)))
            .andExpect(status().isCreated());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInventory() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);
        int databaseSizeBeforeDelete = inventoryRepository.findAll().size();

        // Get the inventory
        restInventoryMockMvc.perform(delete("/api/inventories/{id}", inventory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inventory.class);
        Inventory inventory1 = new Inventory();
        inventory1.setId(1L);
        Inventory inventory2 = new Inventory();
        inventory2.setId(inventory1.getId());
        assertThat(inventory1).isEqualTo(inventory2);
        inventory2.setId(2L);
        assertThat(inventory1).isNotEqualTo(inventory2);
        inventory1.setId(null);
        assertThat(inventory1).isNotEqualTo(inventory2);
    }
}
