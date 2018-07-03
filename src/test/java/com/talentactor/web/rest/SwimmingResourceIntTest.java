package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Swimming;
import com.talentactor.repository.SwimmingRepository;
import com.talentactor.service.SwimmingService;
import com.talentactor.service.dto.SwimmingDTO;
import com.talentactor.service.mapper.SwimmingMapper;
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
 * Test class for the SwimmingResource REST controller.
 *
 * @see SwimmingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentactorApp.class)
public class SwimmingResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private SwimmingRepository swimmingRepository;


    @Autowired
    private SwimmingMapper swimmingMapper;
    

    @Autowired
    private SwimmingService swimmingService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSwimmingMockMvc;

    private Swimming swimming;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SwimmingResource swimmingResource = new SwimmingResource(swimmingService);
        this.restSwimmingMockMvc = MockMvcBuilders.standaloneSetup(swimmingResource)
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
    public static Swimming createEntity(EntityManager em) {
        Swimming swimming = new Swimming()
            .title(DEFAULT_TITLE);
        return swimming;
    }

    @Before
    public void initTest() {
        swimming = createEntity(em);
    }

    @Test
    @Transactional
    public void createSwimming() throws Exception {
        int databaseSizeBeforeCreate = swimmingRepository.findAll().size();

        // Create the Swimming
        SwimmingDTO swimmingDTO = swimmingMapper.toDto(swimming);
        restSwimmingMockMvc.perform(post("/api/swimmings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(swimmingDTO)))
            .andExpect(status().isCreated());

        // Validate the Swimming in the database
        List<Swimming> swimmingList = swimmingRepository.findAll();
        assertThat(swimmingList).hasSize(databaseSizeBeforeCreate + 1);
        Swimming testSwimming = swimmingList.get(swimmingList.size() - 1);
        assertThat(testSwimming.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createSwimmingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = swimmingRepository.findAll().size();

        // Create the Swimming with an existing ID
        swimming.setId(1L);
        SwimmingDTO swimmingDTO = swimmingMapper.toDto(swimming);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSwimmingMockMvc.perform(post("/api/swimmings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(swimmingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Swimming in the database
        List<Swimming> swimmingList = swimmingRepository.findAll();
        assertThat(swimmingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = swimmingRepository.findAll().size();
        // set the field null
        swimming.setTitle(null);

        // Create the Swimming, which fails.
        SwimmingDTO swimmingDTO = swimmingMapper.toDto(swimming);

        restSwimmingMockMvc.perform(post("/api/swimmings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(swimmingDTO)))
            .andExpect(status().isBadRequest());

        List<Swimming> swimmingList = swimmingRepository.findAll();
        assertThat(swimmingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSwimmings() throws Exception {
        // Initialize the database
        swimmingRepository.saveAndFlush(swimming);

        // Get all the swimmingList
        restSwimmingMockMvc.perform(get("/api/swimmings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(swimming.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }
    

    @Test
    @Transactional
    public void getSwimming() throws Exception {
        // Initialize the database
        swimmingRepository.saveAndFlush(swimming);

        // Get the swimming
        restSwimmingMockMvc.perform(get("/api/swimmings/{id}", swimming.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(swimming.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingSwimming() throws Exception {
        // Get the swimming
        restSwimmingMockMvc.perform(get("/api/swimmings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSwimming() throws Exception {
        // Initialize the database
        swimmingRepository.saveAndFlush(swimming);

        int databaseSizeBeforeUpdate = swimmingRepository.findAll().size();

        // Update the swimming
        Swimming updatedSwimming = swimmingRepository.findById(swimming.getId()).get();
        // Disconnect from session so that the updates on updatedSwimming are not directly saved in db
        em.detach(updatedSwimming);
        updatedSwimming
            .title(UPDATED_TITLE);
        SwimmingDTO swimmingDTO = swimmingMapper.toDto(updatedSwimming);

        restSwimmingMockMvc.perform(put("/api/swimmings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(swimmingDTO)))
            .andExpect(status().isOk());

        // Validate the Swimming in the database
        List<Swimming> swimmingList = swimmingRepository.findAll();
        assertThat(swimmingList).hasSize(databaseSizeBeforeUpdate);
        Swimming testSwimming = swimmingList.get(swimmingList.size() - 1);
        assertThat(testSwimming.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingSwimming() throws Exception {
        int databaseSizeBeforeUpdate = swimmingRepository.findAll().size();

        // Create the Swimming
        SwimmingDTO swimmingDTO = swimmingMapper.toDto(swimming);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSwimmingMockMvc.perform(put("/api/swimmings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(swimmingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Swimming in the database
        List<Swimming> swimmingList = swimmingRepository.findAll();
        assertThat(swimmingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSwimming() throws Exception {
        // Initialize the database
        swimmingRepository.saveAndFlush(swimming);

        int databaseSizeBeforeDelete = swimmingRepository.findAll().size();

        // Get the swimming
        restSwimmingMockMvc.perform(delete("/api/swimmings/{id}", swimming.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Swimming> swimmingList = swimmingRepository.findAll();
        assertThat(swimmingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Swimming.class);
        Swimming swimming1 = new Swimming();
        swimming1.setId(1L);
        Swimming swimming2 = new Swimming();
        swimming2.setId(swimming1.getId());
        assertThat(swimming1).isEqualTo(swimming2);
        swimming2.setId(2L);
        assertThat(swimming1).isNotEqualTo(swimming2);
        swimming1.setId(null);
        assertThat(swimming1).isNotEqualTo(swimming2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SwimmingDTO.class);
        SwimmingDTO swimmingDTO1 = new SwimmingDTO();
        swimmingDTO1.setId(1L);
        SwimmingDTO swimmingDTO2 = new SwimmingDTO();
        assertThat(swimmingDTO1).isNotEqualTo(swimmingDTO2);
        swimmingDTO2.setId(swimmingDTO1.getId());
        assertThat(swimmingDTO1).isEqualTo(swimmingDTO2);
        swimmingDTO2.setId(2L);
        assertThat(swimmingDTO1).isNotEqualTo(swimmingDTO2);
        swimmingDTO1.setId(null);
        assertThat(swimmingDTO1).isNotEqualTo(swimmingDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(swimmingMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(swimmingMapper.fromId(null)).isNull();
    }
}
