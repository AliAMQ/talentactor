package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Circus;
import com.talentactor.domain.Profile;
import com.talentactor.repository.CircusRepository;
import com.talentactor.service.CircusService;
import com.talentactor.service.dto.CircusDTO;
import com.talentactor.service.mapper.CircusMapper;
import com.talentactor.web.rest.errors.ExceptionTranslator;
import com.talentactor.service.dto.CircusCriteria;
import com.talentactor.service.CircusQueryService;

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
 * Test class for the CircusResource REST controller.
 *
 * @see CircusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentactorApp.class)
public class CircusResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private CircusRepository circusRepository;


    @Autowired
    private CircusMapper circusMapper;
    

    @Autowired
    private CircusService circusService;

    @Autowired
    private CircusQueryService circusQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCircusMockMvc;

    private Circus circus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CircusResource circusResource = new CircusResource(circusService, circusQueryService);
        this.restCircusMockMvc = MockMvcBuilders.standaloneSetup(circusResource)
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
    public static Circus createEntity(EntityManager em) {
        Circus circus = new Circus()
            .title(DEFAULT_TITLE);
        return circus;
    }

    @Before
    public void initTest() {
        circus = createEntity(em);
    }

    @Test
    @Transactional
    public void createCircus() throws Exception {
        int databaseSizeBeforeCreate = circusRepository.findAll().size();

        // Create the Circus
        CircusDTO circusDTO = circusMapper.toDto(circus);
        restCircusMockMvc.perform(post("/api/circuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(circusDTO)))
            .andExpect(status().isCreated());

        // Validate the Circus in the database
        List<Circus> circusList = circusRepository.findAll();
        assertThat(circusList).hasSize(databaseSizeBeforeCreate + 1);
        Circus testCircus = circusList.get(circusList.size() - 1);
        assertThat(testCircus.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createCircusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = circusRepository.findAll().size();

        // Create the Circus with an existing ID
        circus.setId(1L);
        CircusDTO circusDTO = circusMapper.toDto(circus);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCircusMockMvc.perform(post("/api/circuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(circusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Circus in the database
        List<Circus> circusList = circusRepository.findAll();
        assertThat(circusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = circusRepository.findAll().size();
        // set the field null
        circus.setTitle(null);

        // Create the Circus, which fails.
        CircusDTO circusDTO = circusMapper.toDto(circus);

        restCircusMockMvc.perform(post("/api/circuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(circusDTO)))
            .andExpect(status().isBadRequest());

        List<Circus> circusList = circusRepository.findAll();
        assertThat(circusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCircuses() throws Exception {
        // Initialize the database
        circusRepository.saveAndFlush(circus);

        // Get all the circusList
        restCircusMockMvc.perform(get("/api/circuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(circus.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }
    

    @Test
    @Transactional
    public void getCircus() throws Exception {
        // Initialize the database
        circusRepository.saveAndFlush(circus);

        // Get the circus
        restCircusMockMvc.perform(get("/api/circuses/{id}", circus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(circus.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getAllCircusesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        circusRepository.saveAndFlush(circus);

        // Get all the circusList where title equals to DEFAULT_TITLE
        defaultCircusShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the circusList where title equals to UPDATED_TITLE
        defaultCircusShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCircusesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        circusRepository.saveAndFlush(circus);

        // Get all the circusList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCircusShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the circusList where title equals to UPDATED_TITLE
        defaultCircusShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCircusesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        circusRepository.saveAndFlush(circus);

        // Get all the circusList where title is not null
        defaultCircusShouldBeFound("title.specified=true");

        // Get all the circusList where title is null
        defaultCircusShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllCircusesByProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile profile = ProfileResourceIntTest.createEntity(em);
        em.persist(profile);
        em.flush();
        circus.addProfile(profile);
        circusRepository.saveAndFlush(circus);
        Long profileId = profile.getId();

        // Get all the circusList where profile equals to profileId
        defaultCircusShouldBeFound("profileId.equals=" + profileId);

        // Get all the circusList where profile equals to profileId + 1
        defaultCircusShouldNotBeFound("profileId.equals=" + (profileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCircusShouldBeFound(String filter) throws Exception {
        restCircusMockMvc.perform(get("/api/circuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(circus.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCircusShouldNotBeFound(String filter) throws Exception {
        restCircusMockMvc.perform(get("/api/circuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingCircus() throws Exception {
        // Get the circus
        restCircusMockMvc.perform(get("/api/circuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCircus() throws Exception {
        // Initialize the database
        circusRepository.saveAndFlush(circus);

        int databaseSizeBeforeUpdate = circusRepository.findAll().size();

        // Update the circus
        Circus updatedCircus = circusRepository.findById(circus.getId()).get();
        // Disconnect from session so that the updates on updatedCircus are not directly saved in db
        em.detach(updatedCircus);
        updatedCircus
            .title(UPDATED_TITLE);
        CircusDTO circusDTO = circusMapper.toDto(updatedCircus);

        restCircusMockMvc.perform(put("/api/circuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(circusDTO)))
            .andExpect(status().isOk());

        // Validate the Circus in the database
        List<Circus> circusList = circusRepository.findAll();
        assertThat(circusList).hasSize(databaseSizeBeforeUpdate);
        Circus testCircus = circusList.get(circusList.size() - 1);
        assertThat(testCircus.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingCircus() throws Exception {
        int databaseSizeBeforeUpdate = circusRepository.findAll().size();

        // Create the Circus
        CircusDTO circusDTO = circusMapper.toDto(circus);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCircusMockMvc.perform(put("/api/circuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(circusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Circus in the database
        List<Circus> circusList = circusRepository.findAll();
        assertThat(circusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCircus() throws Exception {
        // Initialize the database
        circusRepository.saveAndFlush(circus);

        int databaseSizeBeforeDelete = circusRepository.findAll().size();

        // Get the circus
        restCircusMockMvc.perform(delete("/api/circuses/{id}", circus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Circus> circusList = circusRepository.findAll();
        assertThat(circusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Circus.class);
        Circus circus1 = new Circus();
        circus1.setId(1L);
        Circus circus2 = new Circus();
        circus2.setId(circus1.getId());
        assertThat(circus1).isEqualTo(circus2);
        circus2.setId(2L);
        assertThat(circus1).isNotEqualTo(circus2);
        circus1.setId(null);
        assertThat(circus1).isNotEqualTo(circus2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CircusDTO.class);
        CircusDTO circusDTO1 = new CircusDTO();
        circusDTO1.setId(1L);
        CircusDTO circusDTO2 = new CircusDTO();
        assertThat(circusDTO1).isNotEqualTo(circusDTO2);
        circusDTO2.setId(circusDTO1.getId());
        assertThat(circusDTO1).isEqualTo(circusDTO2);
        circusDTO2.setId(2L);
        assertThat(circusDTO1).isNotEqualTo(circusDTO2);
        circusDTO1.setId(null);
        assertThat(circusDTO1).isNotEqualTo(circusDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(circusMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(circusMapper.fromId(null)).isNull();
    }
}
