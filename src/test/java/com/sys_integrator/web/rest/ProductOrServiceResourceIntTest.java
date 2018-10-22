package com.sys_integrator.web.rest;

import com.sys_integrator.TruckwashApp;

import com.sys_integrator.domain.ProductOrService;
import com.sys_integrator.repository.ProductOrServiceRepository;
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

import com.sys_integrator.domain.enumeration.UnitOfMeasure;
import com.sys_integrator.domain.enumeration.ProductOrServiceType;
import com.sys_integrator.domain.enumeration.ProductQuantityMetering;
/**
 * Test class for the ProductOrServiceResource REST controller.
 *
 * @see ProductOrServiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TruckwashApp.class)
public class ProductOrServiceResourceIntTest {

    private static final String DEFAULT_SHORT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LONG_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LONG_DESCRIPTION = "BBBBBBBBBB";

    private static final UnitOfMeasure DEFAULT_UNIT_OF_MEASHRE = UnitOfMeasure.NUMBER;
    private static final UnitOfMeasure UPDATED_UNIT_OF_MEASHRE = UnitOfMeasure.LITRE;

    private static final Double DEFAULT_PRICE_PER_UNIT = 1D;
    private static final Double UPDATED_PRICE_PER_UNIT = 2D;

    private static final ProductOrServiceType DEFAULT_TYPE = ProductOrServiceType.PRODUCT;
    private static final ProductOrServiceType UPDATED_TYPE = ProductOrServiceType.SERVICE;

    private static final Integer DEFAULT_MAX_DURATION = 1;
    private static final Integer UPDATED_MAX_DURATION = 2;

    private static final Double DEFAULT_QUANTITY_IN_STOCK = 1D;
    private static final Double UPDATED_QUANTITY_IN_STOCK = 2D;

    private static final ProductQuantityMetering DEFAULT_QUANTITY_METERING = ProductQuantityMetering.INFINITE_QTY;
    private static final ProductQuantityMetering UPDATED_QUANTITY_METERING = ProductQuantityMetering.QTY_NOT_METERED;

    private static final Integer DEFAULT_AUTO_METERING_INTERVAL = 1;
    private static final Integer UPDATED_AUTO_METERING_INTERVAL = 2;

    private static final Boolean DEFAULT_LOW_LEVEL_ALERT = false;
    private static final Boolean UPDATED_LOW_LEVEL_ALERT = true;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private ProductOrServiceRepository productOrServiceRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductOrServiceMockMvc;

    private ProductOrService productOrService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductOrServiceResource productOrServiceResource = new ProductOrServiceResource(productOrServiceRepository);
        this.restProductOrServiceMockMvc = MockMvcBuilders.standaloneSetup(productOrServiceResource)
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
    public static ProductOrService createEntity(EntityManager em) {
        ProductOrService productOrService = new ProductOrService()
            .shortDescription(DEFAULT_SHORT_DESCRIPTION)
            .longDescription(DEFAULT_LONG_DESCRIPTION)
            .unitOfMeashre(DEFAULT_UNIT_OF_MEASHRE)
            .pricePerUnit(DEFAULT_PRICE_PER_UNIT)
            .type(DEFAULT_TYPE)
            .maxDuration(DEFAULT_MAX_DURATION)
            .quantityInStock(DEFAULT_QUANTITY_IN_STOCK)
            .quantityMetering(DEFAULT_QUANTITY_METERING)
            .autoMeteringInterval(DEFAULT_AUTO_METERING_INTERVAL)
            .lowLevelAlert(DEFAULT_LOW_LEVEL_ALERT)
            .active(DEFAULT_ACTIVE);
        return productOrService;
    }

    @Before
    public void initTest() {
        productOrService = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductOrService() throws Exception {
        int databaseSizeBeforeCreate = productOrServiceRepository.findAll().size();

        // Create the ProductOrService
        restProductOrServiceMockMvc.perform(post("/api/product-or-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOrService)))
            .andExpect(status().isCreated());

        // Validate the ProductOrService in the database
        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeCreate + 1);
        ProductOrService testProductOrService = productOrServiceList.get(productOrServiceList.size() - 1);
        assertThat(testProductOrService.getShortDescription()).isEqualTo(DEFAULT_SHORT_DESCRIPTION);
        assertThat(testProductOrService.getLongDescription()).isEqualTo(DEFAULT_LONG_DESCRIPTION);
        assertThat(testProductOrService.getUnitOfMeashre()).isEqualTo(DEFAULT_UNIT_OF_MEASHRE);
        assertThat(testProductOrService.getPricePerUnit()).isEqualTo(DEFAULT_PRICE_PER_UNIT);
        assertThat(testProductOrService.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testProductOrService.getMaxDuration()).isEqualTo(DEFAULT_MAX_DURATION);
        assertThat(testProductOrService.getQuantityInStock()).isEqualTo(DEFAULT_QUANTITY_IN_STOCK);
        assertThat(testProductOrService.getQuantityMetering()).isEqualTo(DEFAULT_QUANTITY_METERING);
        assertThat(testProductOrService.getAutoMeteringInterval()).isEqualTo(DEFAULT_AUTO_METERING_INTERVAL);
        assertThat(testProductOrService.isLowLevelAlert()).isEqualTo(DEFAULT_LOW_LEVEL_ALERT);
        assertThat(testProductOrService.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createProductOrServiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productOrServiceRepository.findAll().size();

        // Create the ProductOrService with an existing ID
        productOrService.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductOrServiceMockMvc.perform(post("/api/product-or-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOrService)))
            .andExpect(status().isBadRequest());

        // Validate the ProductOrService in the database
        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProductOrServices() throws Exception {
        // Initialize the database
        productOrServiceRepository.saveAndFlush(productOrService);

        // Get all the productOrServiceList
        restProductOrServiceMockMvc.perform(get("/api/product-or-services?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOrService.getId().intValue())))
            .andExpect(jsonPath("$.[*].shortDescription").value(hasItem(DEFAULT_SHORT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].unitOfMeashre").value(hasItem(DEFAULT_UNIT_OF_MEASHRE.toString())))
            .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(DEFAULT_PRICE_PER_UNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].maxDuration").value(hasItem(DEFAULT_MAX_DURATION)))
            .andExpect(jsonPath("$.[*].quantityInStock").value(hasItem(DEFAULT_QUANTITY_IN_STOCK.doubleValue())))
            .andExpect(jsonPath("$.[*].quantityMetering").value(hasItem(DEFAULT_QUANTITY_METERING.toString())))
            .andExpect(jsonPath("$.[*].autoMeteringInterval").value(hasItem(DEFAULT_AUTO_METERING_INTERVAL)))
            .andExpect(jsonPath("$.[*].lowLevelAlert").value(hasItem(DEFAULT_LOW_LEVEL_ALERT.booleanValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getProductOrService() throws Exception {
        // Initialize the database
        productOrServiceRepository.saveAndFlush(productOrService);

        // Get the productOrService
        restProductOrServiceMockMvc.perform(get("/api/product-or-services/{id}", productOrService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productOrService.getId().intValue()))
            .andExpect(jsonPath("$.shortDescription").value(DEFAULT_SHORT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.longDescription").value(DEFAULT_LONG_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.unitOfMeashre").value(DEFAULT_UNIT_OF_MEASHRE.toString()))
            .andExpect(jsonPath("$.pricePerUnit").value(DEFAULT_PRICE_PER_UNIT.doubleValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.maxDuration").value(DEFAULT_MAX_DURATION))
            .andExpect(jsonPath("$.quantityInStock").value(DEFAULT_QUANTITY_IN_STOCK.doubleValue()))
            .andExpect(jsonPath("$.quantityMetering").value(DEFAULT_QUANTITY_METERING.toString()))
            .andExpect(jsonPath("$.autoMeteringInterval").value(DEFAULT_AUTO_METERING_INTERVAL))
            .andExpect(jsonPath("$.lowLevelAlert").value(DEFAULT_LOW_LEVEL_ALERT.booleanValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProductOrService() throws Exception {
        // Get the productOrService
        restProductOrServiceMockMvc.perform(get("/api/product-or-services/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductOrService() throws Exception {
        // Initialize the database
        productOrServiceRepository.saveAndFlush(productOrService);
        int databaseSizeBeforeUpdate = productOrServiceRepository.findAll().size();

        // Update the productOrService
        ProductOrService updatedProductOrService = productOrServiceRepository.findOne(productOrService.getId());
        // Disconnect from session so that the updates on updatedProductOrService are not directly saved in db
        em.detach(updatedProductOrService);
        updatedProductOrService
            .shortDescription(UPDATED_SHORT_DESCRIPTION)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .unitOfMeashre(UPDATED_UNIT_OF_MEASHRE)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .type(UPDATED_TYPE)
            .maxDuration(UPDATED_MAX_DURATION)
            .quantityInStock(UPDATED_QUANTITY_IN_STOCK)
            .quantityMetering(UPDATED_QUANTITY_METERING)
            .autoMeteringInterval(UPDATED_AUTO_METERING_INTERVAL)
            .lowLevelAlert(UPDATED_LOW_LEVEL_ALERT)
            .active(UPDATED_ACTIVE);

        restProductOrServiceMockMvc.perform(put("/api/product-or-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProductOrService)))
            .andExpect(status().isOk());

        // Validate the ProductOrService in the database
        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeUpdate);
        ProductOrService testProductOrService = productOrServiceList.get(productOrServiceList.size() - 1);
        assertThat(testProductOrService.getShortDescription()).isEqualTo(UPDATED_SHORT_DESCRIPTION);
        assertThat(testProductOrService.getLongDescription()).isEqualTo(UPDATED_LONG_DESCRIPTION);
        assertThat(testProductOrService.getUnitOfMeashre()).isEqualTo(UPDATED_UNIT_OF_MEASHRE);
        assertThat(testProductOrService.getPricePerUnit()).isEqualTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testProductOrService.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProductOrService.getMaxDuration()).isEqualTo(UPDATED_MAX_DURATION);
        assertThat(testProductOrService.getQuantityInStock()).isEqualTo(UPDATED_QUANTITY_IN_STOCK);
        assertThat(testProductOrService.getQuantityMetering()).isEqualTo(UPDATED_QUANTITY_METERING);
        assertThat(testProductOrService.getAutoMeteringInterval()).isEqualTo(UPDATED_AUTO_METERING_INTERVAL);
        assertThat(testProductOrService.isLowLevelAlert()).isEqualTo(UPDATED_LOW_LEVEL_ALERT);
        assertThat(testProductOrService.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingProductOrService() throws Exception {
        int databaseSizeBeforeUpdate = productOrServiceRepository.findAll().size();

        // Create the ProductOrService

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProductOrServiceMockMvc.perform(put("/api/product-or-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOrService)))
            .andExpect(status().isCreated());

        // Validate the ProductOrService in the database
        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProductOrService() throws Exception {
        // Initialize the database
        productOrServiceRepository.saveAndFlush(productOrService);
        int databaseSizeBeforeDelete = productOrServiceRepository.findAll().size();

        // Get the productOrService
        restProductOrServiceMockMvc.perform(delete("/api/product-or-services/{id}", productOrService.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductOrService.class);
        ProductOrService productOrService1 = new ProductOrService();
        productOrService1.setId(1L);
        ProductOrService productOrService2 = new ProductOrService();
        productOrService2.setId(productOrService1.getId());
        assertThat(productOrService1).isEqualTo(productOrService2);
        productOrService2.setId(2L);
        assertThat(productOrService1).isNotEqualTo(productOrService2);
        productOrService1.setId(null);
        assertThat(productOrService1).isNotEqualTo(productOrService2);
    }
}
