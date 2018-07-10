package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Internet;
import com.talentactor.domain.Profile;
import com.talentactor.repository.InternetRepository;
import com.talentactor.service.InternetService;
import com.talentactor.service.dto.InternetDTO;
import com.talentactor.service.mapper.InternetMapper;
import com.talentactor.web.rest.errors.ExceptionTranslator;
import com.talentactor.service.dto.InternetCriteria;
import com.talentactor.service.InternetQueryService;

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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;


import static com.talentactor.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InternetResource REST controller.
 *
 * @see InternetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentactorApp.class)
public class InternetResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTOR = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_CAMERAMAN = "AAAAAAAAAA";
    private static final String UPDATED_CAMERAMAN = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_VIDEO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_VIDEO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_VIDEO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_VIDEO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMAGEPATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGEPATH = "BBBBBBBBBB";

    private static final String DEFAULT_VIDEOPATH = "AAAAAAAAAA";
    private static final String UPDATED_VIDEOPATH = "BBBBBBBBBB";

    @Autowired
    private InternetRepository internetRepository;


    @Autowired
    private InternetMapper internetMapper;
    

    @Autowired
    private InternetService internetService;

    @Autowired
    private InternetQueryService internetQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInternetMockMvc;

    private Internet internet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InternetResource internetResource = new InternetResource(internetService, internetQueryService);
        this.restInternetMockMvc = MockMvcBuilders.standaloneSetup(internetResource)
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
    public static Internet createEntity(EntityManager em) {
        Internet internet = new Internet()
            .title(DEFAULT_TITLE)
            .director(DEFAULT_DIRECTOR)
            .cameraman(DEFAULT_CAMERAMAN)
            .link(DEFAULT_LINK)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .video(DEFAULT_VIDEO)
            .videoContentType(DEFAULT_VIDEO_CONTENT_TYPE)
            .imagepath(DEFAULT_IMAGEPATH)
            .videopath(DEFAULT_VIDEOPATH);
        return internet;
    }

    @Before
    public void initTest() {
        internet = createEntity(em);
    }

    @Test
    @Transactional
    public void createInternet() throws Exception {
        int databaseSizeBeforeCreate = internetRepository.findAll().size();

        // Create the Internet
        InternetDTO internetDTO = internetMapper.toDto(internet);
        restInternetMockMvc.perform(post("/api/internets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internetDTO)))
            .andExpect(status().isCreated());

        // Validate the Internet in the database
        List<Internet> internetList = internetRepository.findAll();
        assertThat(internetList).hasSize(databaseSizeBeforeCreate + 1);
        Internet testInternet = internetList.get(internetList.size() - 1);
        assertThat(testInternet.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testInternet.getDirector()).isEqualTo(DEFAULT_DIRECTOR);
        assertThat(testInternet.getCameraman()).isEqualTo(DEFAULT_CAMERAMAN);
        assertThat(testInternet.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testInternet.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testInternet.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testInternet.getVideo()).isEqualTo(DEFAULT_VIDEO);
        assertThat(testInternet.getVideoContentType()).isEqualTo(DEFAULT_VIDEO_CONTENT_TYPE);
        assertThat(testInternet.getImagepath()).isEqualTo(DEFAULT_IMAGEPATH);
        assertThat(testInternet.getVideopath()).isEqualTo(DEFAULT_VIDEOPATH);
    }

    @Test
    @Transactional
    public void createInternetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = internetRepository.findAll().size();

        // Create the Internet with an existing ID
        internet.setId(1L);
        InternetDTO internetDTO = internetMapper.toDto(internet);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInternetMockMvc.perform(post("/api/internets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Internet in the database
        List<Internet> internetList = internetRepository.findAll();
        assertThat(internetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = internetRepository.findAll().size();
        // set the field null
        internet.setTitle(null);

        // Create the Internet, which fails.
        InternetDTO internetDTO = internetMapper.toDto(internet);

        restInternetMockMvc.perform(post("/api/internets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internetDTO)))
            .andExpect(status().isBadRequest());

        List<Internet> internetList = internetRepository.findAll();
        assertThat(internetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInternets() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList
        restInternetMockMvc.perform(get("/api/internets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internet.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].director").value(hasItem(DEFAULT_DIRECTOR.toString())))
            .andExpect(jsonPath("$.[*].cameraman").value(hasItem(DEFAULT_CAMERAMAN.toString())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].videoContentType").value(hasItem(DEFAULT_VIDEO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].video").value(hasItem(Base64Utils.encodeToString(DEFAULT_VIDEO))))
            .andExpect(jsonPath("$.[*].imagepath").value(hasItem(DEFAULT_IMAGEPATH.toString())))
            .andExpect(jsonPath("$.[*].videopath").value(hasItem(DEFAULT_VIDEOPATH.toString())));
    }
    

    @Test
    @Transactional
    public void getInternet() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get the internet
        restInternetMockMvc.perform(get("/api/internets/{id}", internet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(internet.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.director").value(DEFAULT_DIRECTOR.toString()))
            .andExpect(jsonPath("$.cameraman").value(DEFAULT_CAMERAMAN.toString()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.videoContentType").value(DEFAULT_VIDEO_CONTENT_TYPE))
            .andExpect(jsonPath("$.video").value(Base64Utils.encodeToString(DEFAULT_VIDEO)))
            .andExpect(jsonPath("$.imagepath").value(DEFAULT_IMAGEPATH.toString()))
            .andExpect(jsonPath("$.videopath").value(DEFAULT_VIDEOPATH.toString()));
    }

    @Test
    @Transactional
    public void getAllInternetsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where title equals to DEFAULT_TITLE
        defaultInternetShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the internetList where title equals to UPDATED_TITLE
        defaultInternetShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllInternetsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultInternetShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the internetList where title equals to UPDATED_TITLE
        defaultInternetShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllInternetsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where title is not null
        defaultInternetShouldBeFound("title.specified=true");

        // Get all the internetList where title is null
        defaultInternetShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllInternetsByDirectorIsEqualToSomething() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where director equals to DEFAULT_DIRECTOR
        defaultInternetShouldBeFound("director.equals=" + DEFAULT_DIRECTOR);

        // Get all the internetList where director equals to UPDATED_DIRECTOR
        defaultInternetShouldNotBeFound("director.equals=" + UPDATED_DIRECTOR);
    }

    @Test
    @Transactional
    public void getAllInternetsByDirectorIsInShouldWork() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where director in DEFAULT_DIRECTOR or UPDATED_DIRECTOR
        defaultInternetShouldBeFound("director.in=" + DEFAULT_DIRECTOR + "," + UPDATED_DIRECTOR);

        // Get all the internetList where director equals to UPDATED_DIRECTOR
        defaultInternetShouldNotBeFound("director.in=" + UPDATED_DIRECTOR);
    }

    @Test
    @Transactional
    public void getAllInternetsByDirectorIsNullOrNotNull() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where director is not null
        defaultInternetShouldBeFound("director.specified=true");

        // Get all the internetList where director is null
        defaultInternetShouldNotBeFound("director.specified=false");
    }

    @Test
    @Transactional
    public void getAllInternetsByCameramanIsEqualToSomething() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where cameraman equals to DEFAULT_CAMERAMAN
        defaultInternetShouldBeFound("cameraman.equals=" + DEFAULT_CAMERAMAN);

        // Get all the internetList where cameraman equals to UPDATED_CAMERAMAN
        defaultInternetShouldNotBeFound("cameraman.equals=" + UPDATED_CAMERAMAN);
    }

    @Test
    @Transactional
    public void getAllInternetsByCameramanIsInShouldWork() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where cameraman in DEFAULT_CAMERAMAN or UPDATED_CAMERAMAN
        defaultInternetShouldBeFound("cameraman.in=" + DEFAULT_CAMERAMAN + "," + UPDATED_CAMERAMAN);

        // Get all the internetList where cameraman equals to UPDATED_CAMERAMAN
        defaultInternetShouldNotBeFound("cameraman.in=" + UPDATED_CAMERAMAN);
    }

    @Test
    @Transactional
    public void getAllInternetsByCameramanIsNullOrNotNull() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where cameraman is not null
        defaultInternetShouldBeFound("cameraman.specified=true");

        // Get all the internetList where cameraman is null
        defaultInternetShouldNotBeFound("cameraman.specified=false");
    }

    @Test
    @Transactional
    public void getAllInternetsByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where link equals to DEFAULT_LINK
        defaultInternetShouldBeFound("link.equals=" + DEFAULT_LINK);

        // Get all the internetList where link equals to UPDATED_LINK
        defaultInternetShouldNotBeFound("link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllInternetsByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where link in DEFAULT_LINK or UPDATED_LINK
        defaultInternetShouldBeFound("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK);

        // Get all the internetList where link equals to UPDATED_LINK
        defaultInternetShouldNotBeFound("link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllInternetsByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where link is not null
        defaultInternetShouldBeFound("link.specified=true");

        // Get all the internetList where link is null
        defaultInternetShouldNotBeFound("link.specified=false");
    }

    @Test
    @Transactional
    public void getAllInternetsByImagepathIsEqualToSomething() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where imagepath equals to DEFAULT_IMAGEPATH
        defaultInternetShouldBeFound("imagepath.equals=" + DEFAULT_IMAGEPATH);

        // Get all the internetList where imagepath equals to UPDATED_IMAGEPATH
        defaultInternetShouldNotBeFound("imagepath.equals=" + UPDATED_IMAGEPATH);
    }

    @Test
    @Transactional
    public void getAllInternetsByImagepathIsInShouldWork() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where imagepath in DEFAULT_IMAGEPATH or UPDATED_IMAGEPATH
        defaultInternetShouldBeFound("imagepath.in=" + DEFAULT_IMAGEPATH + "," + UPDATED_IMAGEPATH);

        // Get all the internetList where imagepath equals to UPDATED_IMAGEPATH
        defaultInternetShouldNotBeFound("imagepath.in=" + UPDATED_IMAGEPATH);
    }

    @Test
    @Transactional
    public void getAllInternetsByImagepathIsNullOrNotNull() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where imagepath is not null
        defaultInternetShouldBeFound("imagepath.specified=true");

        // Get all the internetList where imagepath is null
        defaultInternetShouldNotBeFound("imagepath.specified=false");
    }

    @Test
    @Transactional
    public void getAllInternetsByVideopathIsEqualToSomething() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where videopath equals to DEFAULT_VIDEOPATH
        defaultInternetShouldBeFound("videopath.equals=" + DEFAULT_VIDEOPATH);

        // Get all the internetList where videopath equals to UPDATED_VIDEOPATH
        defaultInternetShouldNotBeFound("videopath.equals=" + UPDATED_VIDEOPATH);
    }

    @Test
    @Transactional
    public void getAllInternetsByVideopathIsInShouldWork() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where videopath in DEFAULT_VIDEOPATH or UPDATED_VIDEOPATH
        defaultInternetShouldBeFound("videopath.in=" + DEFAULT_VIDEOPATH + "," + UPDATED_VIDEOPATH);

        // Get all the internetList where videopath equals to UPDATED_VIDEOPATH
        defaultInternetShouldNotBeFound("videopath.in=" + UPDATED_VIDEOPATH);
    }

    @Test
    @Transactional
    public void getAllInternetsByVideopathIsNullOrNotNull() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        // Get all the internetList where videopath is not null
        defaultInternetShouldBeFound("videopath.specified=true");

        // Get all the internetList where videopath is null
        defaultInternetShouldNotBeFound("videopath.specified=false");
    }

    @Test
    @Transactional
    public void getAllInternetsByProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile profile = ProfileResourceIntTest.createEntity(em);
        em.persist(profile);
        em.flush();
        internet.setProfile(profile);
        internetRepository.saveAndFlush(internet);
        Long profileId = profile.getId();

        // Get all the internetList where profile equals to profileId
        defaultInternetShouldBeFound("profileId.equals=" + profileId);

        // Get all the internetList where profile equals to profileId + 1
        defaultInternetShouldNotBeFound("profileId.equals=" + (profileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInternetShouldBeFound(String filter) throws Exception {
        restInternetMockMvc.perform(get("/api/internets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internet.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].director").value(hasItem(DEFAULT_DIRECTOR.toString())))
            .andExpect(jsonPath("$.[*].cameraman").value(hasItem(DEFAULT_CAMERAMAN.toString())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].videoContentType").value(hasItem(DEFAULT_VIDEO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].video").value(hasItem(Base64Utils.encodeToString(DEFAULT_VIDEO))))
            .andExpect(jsonPath("$.[*].imagepath").value(hasItem(DEFAULT_IMAGEPATH.toString())))
            .andExpect(jsonPath("$.[*].videopath").value(hasItem(DEFAULT_VIDEOPATH.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInternetShouldNotBeFound(String filter) throws Exception {
        restInternetMockMvc.perform(get("/api/internets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingInternet() throws Exception {
        // Get the internet
        restInternetMockMvc.perform(get("/api/internets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInternet() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        int databaseSizeBeforeUpdate = internetRepository.findAll().size();

        // Update the internet
        Internet updatedInternet = internetRepository.findById(internet.getId()).get();
        // Disconnect from session so that the updates on updatedInternet are not directly saved in db
        em.detach(updatedInternet);
        updatedInternet
            .title(UPDATED_TITLE)
            .director(UPDATED_DIRECTOR)
            .cameraman(UPDATED_CAMERAMAN)
            .link(UPDATED_LINK)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .video(UPDATED_VIDEO)
            .videoContentType(UPDATED_VIDEO_CONTENT_TYPE)
            .imagepath(UPDATED_IMAGEPATH)
            .videopath(UPDATED_VIDEOPATH);
        InternetDTO internetDTO = internetMapper.toDto(updatedInternet);

        restInternetMockMvc.perform(put("/api/internets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internetDTO)))
            .andExpect(status().isOk());

        // Validate the Internet in the database
        List<Internet> internetList = internetRepository.findAll();
        assertThat(internetList).hasSize(databaseSizeBeforeUpdate);
        Internet testInternet = internetList.get(internetList.size() - 1);
        assertThat(testInternet.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testInternet.getDirector()).isEqualTo(UPDATED_DIRECTOR);
        assertThat(testInternet.getCameraman()).isEqualTo(UPDATED_CAMERAMAN);
        assertThat(testInternet.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testInternet.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testInternet.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testInternet.getVideo()).isEqualTo(UPDATED_VIDEO);
        assertThat(testInternet.getVideoContentType()).isEqualTo(UPDATED_VIDEO_CONTENT_TYPE);
        assertThat(testInternet.getImagepath()).isEqualTo(UPDATED_IMAGEPATH);
        assertThat(testInternet.getVideopath()).isEqualTo(UPDATED_VIDEOPATH);
    }

    @Test
    @Transactional
    public void updateNonExistingInternet() throws Exception {
        int databaseSizeBeforeUpdate = internetRepository.findAll().size();

        // Create the Internet
        InternetDTO internetDTO = internetMapper.toDto(internet);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInternetMockMvc.perform(put("/api/internets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Internet in the database
        List<Internet> internetList = internetRepository.findAll();
        assertThat(internetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInternet() throws Exception {
        // Initialize the database
        internetRepository.saveAndFlush(internet);

        int databaseSizeBeforeDelete = internetRepository.findAll().size();

        // Get the internet
        restInternetMockMvc.perform(delete("/api/internets/{id}", internet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Internet> internetList = internetRepository.findAll();
        assertThat(internetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Internet.class);
        Internet internet1 = new Internet();
        internet1.setId(1L);
        Internet internet2 = new Internet();
        internet2.setId(internet1.getId());
        assertThat(internet1).isEqualTo(internet2);
        internet2.setId(2L);
        assertThat(internet1).isNotEqualTo(internet2);
        internet1.setId(null);
        assertThat(internet1).isNotEqualTo(internet2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InternetDTO.class);
        InternetDTO internetDTO1 = new InternetDTO();
        internetDTO1.setId(1L);
        InternetDTO internetDTO2 = new InternetDTO();
        assertThat(internetDTO1).isNotEqualTo(internetDTO2);
        internetDTO2.setId(internetDTO1.getId());
        assertThat(internetDTO1).isEqualTo(internetDTO2);
        internetDTO2.setId(2L);
        assertThat(internetDTO1).isNotEqualTo(internetDTO2);
        internetDTO1.setId(null);
        assertThat(internetDTO1).isNotEqualTo(internetDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(internetMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(internetMapper.fromId(null)).isNull();
    }
}
