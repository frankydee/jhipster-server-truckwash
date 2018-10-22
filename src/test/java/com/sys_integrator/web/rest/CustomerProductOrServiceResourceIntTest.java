package com.sys_integrator.web.rest;

import com.sys_integrator.TruckwashApp;

import com.sys_integrator.domain.CustomerProductOrService;
import com.sys_integrator.repository.CustomerProductOrServiceRepository;
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
 * Test class for the CustomerProductOrServiceResource REST controller.
 *
 * @see CustomerProductOrServiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TruckwashApp.class)
public class CustomerProductOrServiceResourceIntTest {

    private static final Integer DEFAULT_PERCENT_DISCOUNT = 1;
    private static final Integer UPDATED_PERCENT_DISCOUNT = 2;

    private static final Integer DEFAULT_OVERRIDING_PRICE_PER_UNIT = 1;
    private static final Integer UPDATED_OVERRIDING_PRICE_PER_UNIT = 2;

    @Autowired
    private CustomerProductOrServiceRepository customerProductOrServiceRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCustomerProductOrServiceMockMvc;

    private CustomerProductOrService customerProductOrService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerProductOrServiceResource customerProductOrServiceResource = new CustomerProductOrServiceResource(customerProductOrServiceRepository);
        this.restCustomerProductOrServiceMockMvc = MockMvcBuilders.standaloneSetup(customerProductOrServiceResource)
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
    public static CustomerProductOrService createEntity(EntityManager em) {
        CustomerProductOrService customerProductOrService = new CustomerProductOrService()
            .percentDiscount(DEFAULT_PERCENT_DISCOUNT)
            .overridingPricePerUnit(DEFAULT_OVERRIDING_PRICE_PER_UNIT);
        return customerProductOrService;
    }

    @Before
    public void initTest() {
        customerProductOrService = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomerProductOrService() throws Exception {
        int databaseSizeBeforeCreate = customerProductOrServiceRepository.findAll().size();

        // Create the CustomerProductOrService
        restCustomerProductOrServiceMockMvc.perform(post("/api/customer-product-or-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerProductOrService)))
            .andExpect(status().isCreated());

        // Validate the CustomerProductOrService in the database
        List<CustomerProductOrService> customerProductOrServiceList = customerProductOrServiceRepository.findAll();
        assertThat(customerProductOrServiceList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerProductOrService testCustomerProductOrService = customerProductOrServiceList.get(customerProductOrServiceList.size() - 1);
        assertThat(testCustomerProductOrService.getPercentDiscount()).isEqualTo(DEFAULT_PERCENT_DISCOUNT);
        assertThat(testCustomerProductOrService.getOverridingPricePerUnit()).isEqualTo(DEFAULT_OVERRIDING_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    public void createCustomerProductOrServiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerProductOrServiceRepository.findAll().size();

        // Create the CustomerProductOrService with an existing ID
        customerProductOrService.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerProductOrServiceMockMvc.perform(post("/api/customer-product-or-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerProductOrService)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerProductOrService in the database
        List<CustomerProductOrService> customerProductOrServiceList = customerProductOrServiceRepository.findAll();
        assertThat(customerProductOrServiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCustomerProductOrServices() throws Exception {
        // Initialize the database
        customerProductOrServiceRepository.saveAndFlush(customerProductOrService);

        // Get all the customerProductOrServiceList
        restCustomerProductOrServiceMockMvc.perform(get("/api/customer-product-or-services?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerProductOrService.getId().intValue())))
            .andExpect(jsonPath("$.[*].percentDiscount").value(hasItem(DEFAULT_PERCENT_DISCOUNT)))
            .andExpect(jsonPath("$.[*].overridingPricePerUnit").value(hasItem(DEFAULT_OVERRIDING_PRICE_PER_UNIT)));
    }

    @Test
    @Transactional
    public void getCustomerProductOrService() throws Exception {
        // Initialize the database
        customerProductOrServiceRepository.saveAndFlush(customerProductOrService);

        // Get the customerProductOrService
        restCustomerProductOrServiceMockMvc.perform(get("/api/customer-product-or-services/{id}", customerProductOrService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerProductOrService.getId().intValue()))
            .andExpect(jsonPath("$.percentDiscount").value(DEFAULT_PERCENT_DISCOUNT))
            .andExpect(jsonPath("$.overridingPricePerUnit").value(DEFAULT_OVERRIDING_PRICE_PER_UNIT));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerProductOrService() throws Exception {
        // Get the customerProductOrService
        restCustomerProductOrServiceMockMvc.perform(get("/api/customer-product-or-services/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerProductOrService() throws Exception {
        // Initialize the database
        customerProductOrServiceRepository.saveAndFlush(customerProductOrService);
        int databaseSizeBeforeUpdate = customerProductOrServiceRepository.findAll().size();

        // Update the customerProductOrService
        CustomerProductOrService updatedCustomerProductOrService = customerProductOrServiceRepository.findOne(customerProductOrService.getId());
        // Disconnect from session so that the updates on updatedCustomerProductOrService are not directly saved in db
        em.detach(updatedCustomerProductOrService);
        updatedCustomerProductOrService
            .percentDiscount(UPDATED_PERCENT_DISCOUNT)
            .overridingPricePerUnit(UPDATED_OVERRIDING_PRICE_PER_UNIT);

        restCustomerProductOrServiceMockMvc.perform(put("/api/customer-product-or-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomerProductOrService)))
            .andExpect(status().isOk());

        // Validate the CustomerProductOrService in the database
        List<CustomerProductOrService> customerProductOrServiceList = customerProductOrServiceRepository.findAll();
        assertThat(customerProductOrServiceList).hasSize(databaseSizeBeforeUpdate);
        CustomerProductOrService testCustomerProductOrService = customerProductOrServiceList.get(customerProductOrServiceList.size() - 1);
        assertThat(testCustomerProductOrService.getPercentDiscount()).isEqualTo(UPDATED_PERCENT_DISCOUNT);
        assertThat(testCustomerProductOrService.getOverridingPricePerUnit()).isEqualTo(UPDATED_OVERRIDING_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomerProductOrService() throws Exception {
        int databaseSizeBeforeUpdate = customerProductOrServiceRepository.findAll().size();

        // Create the CustomerProductOrService

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCustomerProductOrServiceMockMvc.perform(put("/api/customer-product-or-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerProductOrService)))
            .andExpect(status().isCreated());

        // Validate the CustomerProductOrService in the database
        List<CustomerProductOrService> customerProductOrServiceList = customerProductOrServiceRepository.findAll();
        assertThat(customerProductOrServiceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCustomerProductOrService() throws Exception {
        // Initialize the database
        customerProductOrServiceRepository.saveAndFlush(customerProductOrService);
        int databaseSizeBeforeDelete = customerProductOrServiceRepository.findAll().size();

        // Get the customerProductOrService
        restCustomerProductOrServiceMockMvc.perform(delete("/api/customer-product-or-services/{id}", customerProductOrService.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomerProductOrService> customerProductOrServiceList = customerProductOrServiceRepository.findAll();
        assertThat(customerProductOrServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerProductOrService.class);
        CustomerProductOrService customerProductOrService1 = new CustomerProductOrService();
        customerProductOrService1.setId(1L);
        CustomerProductOrService customerProductOrService2 = new CustomerProductOrService();
        customerProductOrService2.setId(customerProductOrService1.getId());
        assertThat(customerProductOrService1).isEqualTo(customerProductOrService2);
        customerProductOrService2.setId(2L);
        assertThat(customerProductOrService1).isNotEqualTo(customerProductOrService2);
        customerProductOrService1.setId(null);
        assertThat(customerProductOrService1).isNotEqualTo(customerProductOrService2);
    }
}
