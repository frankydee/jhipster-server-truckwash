package com.sys_integrator.web.rest;

import com.sys_integrator.TruckwashApp;

import com.sys_integrator.domain.SoldItem;
import com.sys_integrator.repository.SoldItemRepository;
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
import java.util.List;

import static com.sys_integrator.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SoldItemResource REST controller.
 *
 * @see SoldItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TruckwashApp.class)
public class SoldItemResourceIntTest {

    private static final Double DEFAULT_TOTALISER_START = 1D;
    private static final Double UPDATED_TOTALISER_START = 2D;

    private static final Double DEFAULT_TOTALISER_END = 1D;
    private static final Double UPDATED_TOTALISER_END = 2D;

    private static final Double DEFAULT_QUANTITY = 1D;
    private static final Double UPDATED_QUANTITY = 2D;

    @Autowired
    private SoldItemRepository soldItemRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSoldItemMockMvc;

    private SoldItem soldItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SoldItemResource soldItemResource = new SoldItemResource(soldItemRepository);
        this.restSoldItemMockMvc = MockMvcBuilders.standaloneSetup(soldItemResource)
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
    public static SoldItem createEntity(EntityManager em) {
        SoldItem soldItem = new SoldItem()
            .totaliserStart(DEFAULT_TOTALISER_START)
            .totaliserEnd(DEFAULT_TOTALISER_END)
            .quantity(DEFAULT_QUANTITY);
        return soldItem;
    }

    @Before
    public void initTest() {
        soldItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createSoldItem() throws Exception {
        int databaseSizeBeforeCreate = soldItemRepository.findAll().size();

        // Create the SoldItem
        restSoldItemMockMvc.perform(post("/api/sold-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(soldItem)))
            .andExpect(status().isCreated());

        // Validate the SoldItem in the database
        List<SoldItem> soldItemList = soldItemRepository.findAll();
        assertThat(soldItemList).hasSize(databaseSizeBeforeCreate + 1);
        SoldItem testSoldItem = soldItemList.get(soldItemList.size() - 1);
        assertThat(testSoldItem.getTotaliserStart()).isEqualTo(DEFAULT_TOTALISER_START);
        assertThat(testSoldItem.getTotaliserEnd()).isEqualTo(DEFAULT_TOTALISER_END);
        assertThat(testSoldItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createSoldItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = soldItemRepository.findAll().size();

        // Create the SoldItem with an existing ID
        soldItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSoldItemMockMvc.perform(post("/api/sold-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(soldItem)))
            .andExpect(status().isBadRequest());

        // Validate the SoldItem in the database
        List<SoldItem> soldItemList = soldItemRepository.findAll();
        assertThat(soldItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSoldItems() throws Exception {
        // Initialize the database
        soldItemRepository.saveAndFlush(soldItem);

        // Get all the soldItemList
        restSoldItemMockMvc.perform(get("/api/sold-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(soldItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].totaliserStart").value(hasItem(DEFAULT_TOTALISER_START.doubleValue())))
            .andExpect(jsonPath("$.[*].totaliserEnd").value(hasItem(DEFAULT_TOTALISER_END.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())));
    }

    @Test
    @Transactional
    public void getSoldItem() throws Exception {
        // Initialize the database
        soldItemRepository.saveAndFlush(soldItem);

        // Get the soldItem
        restSoldItemMockMvc.perform(get("/api/sold-items/{id}", soldItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(soldItem.getId().intValue()))
            .andExpect(jsonPath("$.totaliserStart").value(DEFAULT_TOTALISER_START.doubleValue()))
            .andExpect(jsonPath("$.totaliserEnd").value(DEFAULT_TOTALISER_END.doubleValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSoldItem() throws Exception {
        // Get the soldItem
        restSoldItemMockMvc.perform(get("/api/sold-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSoldItem() throws Exception {
        // Initialize the database
        soldItemRepository.saveAndFlush(soldItem);
        int databaseSizeBeforeUpdate = soldItemRepository.findAll().size();

        // Update the soldItem
        SoldItem updatedSoldItem = soldItemRepository.findOne(soldItem.getId());
        // Disconnect from session so that the updates on updatedSoldItem are not directly saved in db
        em.detach(updatedSoldItem);
        updatedSoldItem
            .totaliserStart(UPDATED_TOTALISER_START)
            .totaliserEnd(UPDATED_TOTALISER_END)
            .quantity(UPDATED_QUANTITY);

        restSoldItemMockMvc.perform(put("/api/sold-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSoldItem)))
            .andExpect(status().isOk());

        // Validate the SoldItem in the database
        List<SoldItem> soldItemList = soldItemRepository.findAll();
        assertThat(soldItemList).hasSize(databaseSizeBeforeUpdate);
        SoldItem testSoldItem = soldItemList.get(soldItemList.size() - 1);
        assertThat(testSoldItem.getTotaliserStart()).isEqualTo(UPDATED_TOTALISER_START);
        assertThat(testSoldItem.getTotaliserEnd()).isEqualTo(UPDATED_TOTALISER_END);
        assertThat(testSoldItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingSoldItem() throws Exception {
        int databaseSizeBeforeUpdate = soldItemRepository.findAll().size();

        // Create the SoldItem

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSoldItemMockMvc.perform(put("/api/sold-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(soldItem)))
            .andExpect(status().isCreated());

        // Validate the SoldItem in the database
        List<SoldItem> soldItemList = soldItemRepository.findAll();
        assertThat(soldItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSoldItem() throws Exception {
        // Initialize the database
        soldItemRepository.saveAndFlush(soldItem);
        int databaseSizeBeforeDelete = soldItemRepository.findAll().size();

        // Get the soldItem
        restSoldItemMockMvc.perform(delete("/api/sold-items/{id}", soldItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SoldItem> soldItemList = soldItemRepository.findAll();
        assertThat(soldItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SoldItem.class);
        SoldItem soldItem1 = new SoldItem();
        soldItem1.setId(1L);
        SoldItem soldItem2 = new SoldItem();
        soldItem2.setId(soldItem1.getId());
        assertThat(soldItem1).isEqualTo(soldItem2);
        soldItem2.setId(2L);
        assertThat(soldItem1).isNotEqualTo(soldItem2);
        soldItem1.setId(null);
        assertThat(soldItem1).isNotEqualTo(soldItem2);
    }
}
