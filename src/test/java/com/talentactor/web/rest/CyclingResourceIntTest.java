package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Cycling;
import com.talentactor.repository.CyclingRepository;
import com.talentactor.service.CyclingService;
import com.talentactor.service.dto.CyclingDTO;
import com.talentactor.service.mapper.CyclingMapper;
import com.talentactor.web.rest.errors.ExceptionTranslator;

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


import static com.talentactor.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CyclingResource REST controller.
 *
 * @see CyclingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentactorApp.class)
public class CyclingResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private CyclingRepository cyclingRepository;


    @Autowired
    private CyclingMapper cyclingMapper;
    

    @Autowired
    private CyclingService cyclingService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCyclingMockMvc;

    private Cycling cycling;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CyclingResource cyclingResource = new CyclingResource(cyclingService);
        this.restCyclingMockMvc = MockMvcBuilders.standaloneSetup(cyclingResource)
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
    public static Cycling createEntity(EntityManager em) {
        Cycling cycling = new Cycling()
            .title(DEFAULT_TITLE);
        return cycling;
    }

    @Before
    public void initTest() {
        cycling = createEntity(em);
    }

    @Test
    @Transactional
    public void createCycling() throws Exception {
        int databaseSizeBeforeCreate = cyclingRepository.findAll().size();

        // Create the Cycling
        CyclingDTO cyclingDTO = cyclingMapper.toDto(cycling);
        restCyclingMockMvc.perform(post("/api/cyclings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cyclingDTO)))
            .andExpect(status().isCreated());

        // Validate the Cycling in the database
        List<Cycling> cyclingList = cyclingRepository.findAll();
        assertThat(cyclingList).hasSize(databaseSizeBeforeCreate + 1);
        Cycling testCycling = cyclingList.get(cyclingList.size() - 1);
        assertThat(testCycling.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createCyclingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cyclingRepository.findAll().size();

        // Create the Cycling with an existing ID
        cycling.setId(1L);
        CyclingDTO cyclingDTO = cyclingMapper.toDto(cycling);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCyclingMockMvc.perform(post("/api/cyclings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cyclingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cycling in the database
        List<Cycling> cyclingList = cyclingRepository.findAll();
        assertThat(cyclingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = cyclingRepository.findAll().size();
        // set the field null
        cycling.setTitle(null);

        // Create the Cycling, which fails.
        CyclingDTO cyclingDTO = cyclingMapper.toDto(cycling);

        restCyclingMockMvc.perform(post("/api/cyclings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cyclingDTO)))
            .andExpect(status().isBadRequest());

        List<Cycling> cyclingList = cyclingRepository.findAll();
        assertThat(cyclingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCyclings() throws Exception {
        // Initialize the database
        cyclingRepository.saveAndFlush(cycling);

        // Get all the cyclingList
        restCyclingMockMvc.perform(get("/api/cyclings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cycling.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }
    

    @Test
    @Transactional
    public void getCycling() throws Exception {
        // Initialize the database
        cyclingRepository.saveAndFlush(cycling);

        // Get the cycling
        restCyclingMockMvc.perform(get("/api/cyclings/{id}", cycling.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cycling.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCycling() throws Exception {
        // Get the cycling
        restCyclingMockMvc.perform(get("/api/cyclings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCycling() throws Exception {
        // Initialize the database
        cyclingRepository.saveAndFlush(cycling);

        int databaseSizeBeforeUpdate = cyclingRepository.findAll().size();

        // Update the cycling
        Cycling updatedCycling = cyclingRepository.findById(cycling.getId()).get();
        // Disconnect from session so that the updates on updatedCycling are not directly saved in db
        em.detach(updatedCycling);
        updatedCycling
            .title(UPDATED_TITLE);
        CyclingDTO cyclingDTO = cyclingMapper.toDto(updatedCycling);

        restCyclingMockMvc.perform(put("/api/cyclings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cyclingDTO)))
            .andExpect(status().isOk());

        // Validate the Cycling in the database
        List<Cycling> cyclingList = cyclingRepository.findAll();
        assertThat(cyclingList).hasSize(databaseSizeBeforeUpdate);
        Cycling testCycling = cyclingList.get(cyclingList.size() - 1);
        assertThat(testCycling.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingCycling() throws Exception {
        int databaseSizeBeforeUpdate = cyclingRepository.findAll().size();

        // Create the Cycling
        CyclingDTO cyclingDTO = cyclingMapper.toDto(cycling);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCyclingMockMvc.perform(put("/api/cyclings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cyclingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cycling in the database
        List<Cycling> cyclingList = cyclingRepository.findAll();
        assertThat(cyclingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCycling() throws Exception {
        // Initialize the database
        cyclingRepository.saveAndFlush(cycling);

        int databaseSizeBeforeDelete = cyclingRepository.findAll().size();

        // Get the cycling
        restCyclingMockMvc.perform(delete("/api/cyclings/{id}", cycling.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cycling> cyclingList = cyclingRepository.findAll();
        assertThat(cyclingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cycling.class);
        Cycling cycling1 = new Cycling();
        cycling1.setId(1L);
        Cycling cycling2 = new Cycling();
        cycling2.setId(cycling1.getId());
        assertThat(cycling1).isEqualTo(cycling2);
        cycling2.setId(2L);
        assertThat(cycling1).isNotEqualTo(cycling2);
        cycling1.setId(null);
        assertThat(cycling1).isNotEqualTo(cycling2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CyclingDTO.class);
        CyclingDTO cyclingDTO1 = new CyclingDTO();
        cyclingDTO1.setId(1L);
        CyclingDTO cyclingDTO2 = new CyclingDTO();
        assertThat(cyclingDTO1).isNotEqualTo(cyclingDTO2);
        cyclingDTO2.setId(cyclingDTO1.getId());
        assertThat(cyclingDTO1).isEqualTo(cyclingDTO2);
        cyclingDTO2.setId(2L);
        assertThat(cyclingDTO1).isNotEqualTo(cyclingDTO2);
        cyclingDTO1.setId(null);
        assertThat(cyclingDTO1).isNotEqualTo(cyclingDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cyclingMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cyclingMapper.fromId(null)).isNull();
    }
}
