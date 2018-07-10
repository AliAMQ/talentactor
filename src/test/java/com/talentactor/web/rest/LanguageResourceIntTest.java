package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Language;
import com.talentactor.domain.Profile;
import com.talentactor.repository.LanguageRepository;
import com.talentactor.service.LanguageService;
import com.talentactor.service.dto.LanguageDTO;
import com.talentactor.service.mapper.LanguageMapper;
import com.talentactor.web.rest.errors.ExceptionTranslator;
import com.talentactor.service.dto.LanguageCriteria;
import com.talentactor.service.LanguageQueryService;

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
 * Test class for the LanguageResource REST controller.
 *
 * @see LanguageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentactorApp.class)
public class LanguageResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private LanguageRepository languageRepository;


    @Autowired
    private LanguageMapper languageMapper;
    

    @Autowired
    private LanguageService languageService;

    @Autowired
    private LanguageQueryService languageQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLanguageMockMvc;

    private Language language;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LanguageResource languageResource = new LanguageResource(languageService, languageQueryService);
        this.restLanguageMockMvc = MockMvcBuilders.standaloneSetup(languageResource)
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
    public static Language createEntity(EntityManager em) {
        Language language = new Language()
            .title(DEFAULT_TITLE);
        return language;
    }

    @Before
    public void initTest() {
        language = createEntity(em);
    }

    @Test
    @Transactional
    public void createLanguage() throws Exception {
        int databaseSizeBeforeCreate = languageRepository.findAll().size();

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);
        restLanguageMockMvc.perform(post("/api/languages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(languageDTO)))
            .andExpect(status().isCreated());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeCreate + 1);
        Language testLanguage = languageList.get(languageList.size() - 1);
        assertThat(testLanguage.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createLanguageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = languageRepository.findAll().size();

        // Create the Language with an existing ID
        language.setId(1L);
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLanguageMockMvc.perform(post("/api/languages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(languageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = languageRepository.findAll().size();
        // set the field null
        language.setTitle(null);

        // Create the Language, which fails.
        LanguageDTO languageDTO = languageMapper.toDto(language);

        restLanguageMockMvc.perform(post("/api/languages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(languageDTO)))
            .andExpect(status().isBadRequest());

        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLanguages() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList
        restLanguageMockMvc.perform(get("/api/languages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(language.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }
    

    @Test
    @Transactional
    public void getLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get the language
        restLanguageMockMvc.perform(get("/api/languages/{id}", language.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(language.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getAllLanguagesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where title equals to DEFAULT_TITLE
        defaultLanguageShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the languageList where title equals to UPDATED_TITLE
        defaultLanguageShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllLanguagesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultLanguageShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the languageList where title equals to UPDATED_TITLE
        defaultLanguageShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllLanguagesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where title is not null
        defaultLanguageShouldBeFound("title.specified=true");

        // Get all the languageList where title is null
        defaultLanguageShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllLanguagesByProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile profile = ProfileResourceIntTest.createEntity(em);
        em.persist(profile);
        em.flush();
        language.addProfile(profile);
        languageRepository.saveAndFlush(language);
        Long profileId = profile.getId();

        // Get all the languageList where profile equals to profileId
        defaultLanguageShouldBeFound("profileId.equals=" + profileId);

        // Get all the languageList where profile equals to profileId + 1
        defaultLanguageShouldNotBeFound("profileId.equals=" + (profileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultLanguageShouldBeFound(String filter) throws Exception {
        restLanguageMockMvc.perform(get("/api/languages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(language.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultLanguageShouldNotBeFound(String filter) throws Exception {
        restLanguageMockMvc.perform(get("/api/languages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingLanguage() throws Exception {
        // Get the language
        restLanguageMockMvc.perform(get("/api/languages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        int databaseSizeBeforeUpdate = languageRepository.findAll().size();

        // Update the language
        Language updatedLanguage = languageRepository.findById(language.getId()).get();
        // Disconnect from session so that the updates on updatedLanguage are not directly saved in db
        em.detach(updatedLanguage);
        updatedLanguage
            .title(UPDATED_TITLE);
        LanguageDTO languageDTO = languageMapper.toDto(updatedLanguage);

        restLanguageMockMvc.perform(put("/api/languages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(languageDTO)))
            .andExpect(status().isOk());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
        Language testLanguage = languageList.get(languageList.size() - 1);
        assertThat(testLanguage.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingLanguage() throws Exception {
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLanguageMockMvc.perform(put("/api/languages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(languageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        int databaseSizeBeforeDelete = languageRepository.findAll().size();

        // Get the language
        restLanguageMockMvc.perform(delete("/api/languages/{id}", language.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Language.class);
        Language language1 = new Language();
        language1.setId(1L);
        Language language2 = new Language();
        language2.setId(language1.getId());
        assertThat(language1).isEqualTo(language2);
        language2.setId(2L);
        assertThat(language1).isNotEqualTo(language2);
        language1.setId(null);
        assertThat(language1).isNotEqualTo(language2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LanguageDTO.class);
        LanguageDTO languageDTO1 = new LanguageDTO();
        languageDTO1.setId(1L);
        LanguageDTO languageDTO2 = new LanguageDTO();
        assertThat(languageDTO1).isNotEqualTo(languageDTO2);
        languageDTO2.setId(languageDTO1.getId());
        assertThat(languageDTO1).isEqualTo(languageDTO2);
        languageDTO2.setId(2L);
        assertThat(languageDTO1).isNotEqualTo(languageDTO2);
        languageDTO1.setId(null);
        assertThat(languageDTO1).isNotEqualTo(languageDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(languageMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(languageMapper.fromId(null)).isNull();
    }
}
