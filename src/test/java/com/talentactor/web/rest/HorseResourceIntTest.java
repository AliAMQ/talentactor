package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Horse;
import com.talentactor.domain.Profile;
import com.talentactor.repository.HorseRepository;
import com.talentactor.service.HorseService;
import com.talentactor.service.dto.HorseDTO;
import com.talentactor.service.mapper.HorseMapper;
import com.talentactor.web.rest.errors.ExceptionTranslator;
import com.talentactor.service.dto.HorseCriteria;
import com.talentactor.service.HorseQueryService;

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
 * Test class for the HorseResource REST controller.
 *
 * @see HorseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentactorApp.class)
public class HorseResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private HorseRepository horseRepository;


    @Autowired
    private HorseMapper horseMapper;
    

    @Autowired
    private HorseService horseService;

    @Autowired
    private HorseQueryService horseQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHorseMockMvc;

    private Horse horse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HorseResource horseResource = new HorseResource(horseService, horseQueryService);
        this.restHorseMockMvc = MockMvcBuilders.standaloneSetup(horseResource)
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
    public static Horse createEntity(EntityManager em) {
        Horse horse = new Horse()
            .title(DEFAULT_TITLE);
        return horse;
    }

    @Before
    public void initTest() {
        horse = createEntity(em);
    }

    @Test
    @Transactional
    public void createHorse() throws Exception {
        int databaseSizeBeforeCreate = horseRepository.findAll().size();

        // Create the Horse
        HorseDTO horseDTO = horseMapper.toDto(horse);
        restHorseMockMvc.perform(post("/api/horses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horseDTO)))
            .andExpect(status().isCreated());

        // Validate the Horse in the database
        List<Horse> horseList = horseRepository.findAll();
        assertThat(horseList).hasSize(databaseSizeBeforeCreate + 1);
        Horse testHorse = horseList.get(horseList.size() - 1);
        assertThat(testHorse.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createHorseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = horseRepository.findAll().size();

        // Create the Horse with an existing ID
        horse.setId(1L);
        HorseDTO horseDTO = horseMapper.toDto(horse);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHorseMockMvc.perform(post("/api/horses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Horse in the database
        List<Horse> horseList = horseRepository.findAll();
        assertThat(horseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = horseRepository.findAll().size();
        // set the field null
        horse.setTitle(null);

        // Create the Horse, which fails.
        HorseDTO horseDTO = horseMapper.toDto(horse);

        restHorseMockMvc.perform(post("/api/horses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horseDTO)))
            .andExpect(status().isBadRequest());

        List<Horse> horseList = horseRepository.findAll();
        assertThat(horseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHorses() throws Exception {
        // Initialize the database
        horseRepository.saveAndFlush(horse);

        // Get all the horseList
        restHorseMockMvc.perform(get("/api/horses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(horse.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }
    

    @Test
    @Transactional
    public void getHorse() throws Exception {
        // Initialize the database
        horseRepository.saveAndFlush(horse);

        // Get the horse
        restHorseMockMvc.perform(get("/api/horses/{id}", horse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(horse.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getAllHorsesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        horseRepository.saveAndFlush(horse);

        // Get all the horseList where title equals to DEFAULT_TITLE
        defaultHorseShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the horseList where title equals to UPDATED_TITLE
        defaultHorseShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllHorsesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        horseRepository.saveAndFlush(horse);

        // Get all the horseList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultHorseShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the horseList where title equals to UPDATED_TITLE
        defaultHorseShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllHorsesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        horseRepository.saveAndFlush(horse);

        // Get all the horseList where title is not null
        defaultHorseShouldBeFound("title.specified=true");

        // Get all the horseList where title is null
        defaultHorseShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllHorsesByProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile profile = ProfileResourceIntTest.createEntity(em);
        em.persist(profile);
        em.flush();
        horse.addProfile(profile);
        horseRepository.saveAndFlush(horse);
        Long profileId = profile.getId();

        // Get all the horseList where profile equals to profileId
        defaultHorseShouldBeFound("profileId.equals=" + profileId);

        // Get all the horseList where profile equals to profileId + 1
        defaultHorseShouldNotBeFound("profileId.equals=" + (profileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultHorseShouldBeFound(String filter) throws Exception {
        restHorseMockMvc.perform(get("/api/horses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(horse.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultHorseShouldNotBeFound(String filter) throws Exception {
        restHorseMockMvc.perform(get("/api/horses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingHorse() throws Exception {
        // Get the horse
        restHorseMockMvc.perform(get("/api/horses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHorse() throws Exception {
        // Initialize the database
        horseRepository.saveAndFlush(horse);

        int databaseSizeBeforeUpdate = horseRepository.findAll().size();

        // Update the horse
        Horse updatedHorse = horseRepository.findById(horse.getId()).get();
        // Disconnect from session so that the updates on updatedHorse are not directly saved in db
        em.detach(updatedHorse);
        updatedHorse
            .title(UPDATED_TITLE);
        HorseDTO horseDTO = horseMapper.toDto(updatedHorse);

        restHorseMockMvc.perform(put("/api/horses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horseDTO)))
            .andExpect(status().isOk());

        // Validate the Horse in the database
        List<Horse> horseList = horseRepository.findAll();
        assertThat(horseList).hasSize(databaseSizeBeforeUpdate);
        Horse testHorse = horseList.get(horseList.size() - 1);
        assertThat(testHorse.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingHorse() throws Exception {
        int databaseSizeBeforeUpdate = horseRepository.findAll().size();

        // Create the Horse
        HorseDTO horseDTO = horseMapper.toDto(horse);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHorseMockMvc.perform(put("/api/horses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Horse in the database
        List<Horse> horseList = horseRepository.findAll();
        assertThat(horseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHorse() throws Exception {
        // Initialize the database
        horseRepository.saveAndFlush(horse);

        int databaseSizeBeforeDelete = horseRepository.findAll().size();

        // Get the horse
        restHorseMockMvc.perform(delete("/api/horses/{id}", horse.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Horse> horseList = horseRepository.findAll();
        assertThat(horseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Horse.class);
        Horse horse1 = new Horse();
        horse1.setId(1L);
        Horse horse2 = new Horse();
        horse2.setId(horse1.getId());
        assertThat(horse1).isEqualTo(horse2);
        horse2.setId(2L);
        assertThat(horse1).isNotEqualTo(horse2);
        horse1.setId(null);
        assertThat(horse1).isNotEqualTo(horse2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HorseDTO.class);
        HorseDTO horseDTO1 = new HorseDTO();
        horseDTO1.setId(1L);
        HorseDTO horseDTO2 = new HorseDTO();
        assertThat(horseDTO1).isNotEqualTo(horseDTO2);
        horseDTO2.setId(horseDTO1.getId());
        assertThat(horseDTO1).isEqualTo(horseDTO2);
        horseDTO2.setId(2L);
        assertThat(horseDTO1).isNotEqualTo(horseDTO2);
        horseDTO1.setId(null);
        assertThat(horseDTO1).isNotEqualTo(horseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(horseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(horseMapper.fromId(null)).isNull();
    }
}
