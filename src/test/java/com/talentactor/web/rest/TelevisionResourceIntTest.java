package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Television;
import com.talentactor.domain.Profile;
import com.talentactor.repository.TelevisionRepository;
import com.talentactor.service.TelevisionService;
import com.talentactor.service.dto.TelevisionDTO;
import com.talentactor.service.mapper.TelevisionMapper;
import com.talentactor.web.rest.errors.ExceptionTranslator;
import com.talentactor.service.dto.TelevisionCriteria;
import com.talentactor.service.TelevisionQueryService;

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
 * Test class for the TelevisionResource REST controller.
 *
 * @see TelevisionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentactorApp.class)
public class TelevisionResourceIntTest {

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
    private TelevisionRepository televisionRepository;


    @Autowired
    private TelevisionMapper televisionMapper;
    

    @Autowired
    private TelevisionService televisionService;

    @Autowired
    private TelevisionQueryService televisionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTelevisionMockMvc;

    private Television television;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TelevisionResource televisionResource = new TelevisionResource(televisionService, televisionQueryService);
        this.restTelevisionMockMvc = MockMvcBuilders.standaloneSetup(televisionResource)
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
    public static Television createEntity(EntityManager em) {
        Television television = new Television()
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
        return television;
    }

    @Before
    public void initTest() {
        television = createEntity(em);
    }

    @Test
    @Transactional
    public void createTelevision() throws Exception {
        int databaseSizeBeforeCreate = televisionRepository.findAll().size();

        // Create the Television
        TelevisionDTO televisionDTO = televisionMapper.toDto(television);
        restTelevisionMockMvc.perform(post("/api/televisions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(televisionDTO)))
            .andExpect(status().isCreated());

        // Validate the Television in the database
        List<Television> televisionList = televisionRepository.findAll();
        assertThat(televisionList).hasSize(databaseSizeBeforeCreate + 1);
        Television testTelevision = televisionList.get(televisionList.size() - 1);
        assertThat(testTelevision.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTelevision.getDirector()).isEqualTo(DEFAULT_DIRECTOR);
        assertThat(testTelevision.getCameraman()).isEqualTo(DEFAULT_CAMERAMAN);
        assertThat(testTelevision.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testTelevision.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testTelevision.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testTelevision.getVideo()).isEqualTo(DEFAULT_VIDEO);
        assertThat(testTelevision.getVideoContentType()).isEqualTo(DEFAULT_VIDEO_CONTENT_TYPE);
        assertThat(testTelevision.getImagepath()).isEqualTo(DEFAULT_IMAGEPATH);
        assertThat(testTelevision.getVideopath()).isEqualTo(DEFAULT_VIDEOPATH);
    }

    @Test
    @Transactional
    public void createTelevisionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = televisionRepository.findAll().size();

        // Create the Television with an existing ID
        television.setId(1L);
        TelevisionDTO televisionDTO = televisionMapper.toDto(television);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTelevisionMockMvc.perform(post("/api/televisions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(televisionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Television in the database
        List<Television> televisionList = televisionRepository.findAll();
        assertThat(televisionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = televisionRepository.findAll().size();
        // set the field null
        television.setTitle(null);

        // Create the Television, which fails.
        TelevisionDTO televisionDTO = televisionMapper.toDto(television);

        restTelevisionMockMvc.perform(post("/api/televisions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(televisionDTO)))
            .andExpect(status().isBadRequest());

        List<Television> televisionList = televisionRepository.findAll();
        assertThat(televisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTelevisions() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList
        restTelevisionMockMvc.perform(get("/api/televisions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(television.getId().intValue())))
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
    public void getTelevision() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get the television
        restTelevisionMockMvc.perform(get("/api/televisions/{id}", television.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(television.getId().intValue()))
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
    public void getAllTelevisionsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where title equals to DEFAULT_TITLE
        defaultTelevisionShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the televisionList where title equals to UPDATED_TITLE
        defaultTelevisionShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllTelevisionsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultTelevisionShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the televisionList where title equals to UPDATED_TITLE
        defaultTelevisionShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllTelevisionsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where title is not null
        defaultTelevisionShouldBeFound("title.specified=true");

        // Get all the televisionList where title is null
        defaultTelevisionShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelevisionsByDirectorIsEqualToSomething() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where director equals to DEFAULT_DIRECTOR
        defaultTelevisionShouldBeFound("director.equals=" + DEFAULT_DIRECTOR);

        // Get all the televisionList where director equals to UPDATED_DIRECTOR
        defaultTelevisionShouldNotBeFound("director.equals=" + UPDATED_DIRECTOR);
    }

    @Test
    @Transactional
    public void getAllTelevisionsByDirectorIsInShouldWork() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where director in DEFAULT_DIRECTOR or UPDATED_DIRECTOR
        defaultTelevisionShouldBeFound("director.in=" + DEFAULT_DIRECTOR + "," + UPDATED_DIRECTOR);

        // Get all the televisionList where director equals to UPDATED_DIRECTOR
        defaultTelevisionShouldNotBeFound("director.in=" + UPDATED_DIRECTOR);
    }

    @Test
    @Transactional
    public void getAllTelevisionsByDirectorIsNullOrNotNull() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where director is not null
        defaultTelevisionShouldBeFound("director.specified=true");

        // Get all the televisionList where director is null
        defaultTelevisionShouldNotBeFound("director.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelevisionsByCameramanIsEqualToSomething() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where cameraman equals to DEFAULT_CAMERAMAN
        defaultTelevisionShouldBeFound("cameraman.equals=" + DEFAULT_CAMERAMAN);

        // Get all the televisionList where cameraman equals to UPDATED_CAMERAMAN
        defaultTelevisionShouldNotBeFound("cameraman.equals=" + UPDATED_CAMERAMAN);
    }

    @Test
    @Transactional
    public void getAllTelevisionsByCameramanIsInShouldWork() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where cameraman in DEFAULT_CAMERAMAN or UPDATED_CAMERAMAN
        defaultTelevisionShouldBeFound("cameraman.in=" + DEFAULT_CAMERAMAN + "," + UPDATED_CAMERAMAN);

        // Get all the televisionList where cameraman equals to UPDATED_CAMERAMAN
        defaultTelevisionShouldNotBeFound("cameraman.in=" + UPDATED_CAMERAMAN);
    }

    @Test
    @Transactional
    public void getAllTelevisionsByCameramanIsNullOrNotNull() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where cameraman is not null
        defaultTelevisionShouldBeFound("cameraman.specified=true");

        // Get all the televisionList where cameraman is null
        defaultTelevisionShouldNotBeFound("cameraman.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelevisionsByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where link equals to DEFAULT_LINK
        defaultTelevisionShouldBeFound("link.equals=" + DEFAULT_LINK);

        // Get all the televisionList where link equals to UPDATED_LINK
        defaultTelevisionShouldNotBeFound("link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllTelevisionsByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where link in DEFAULT_LINK or UPDATED_LINK
        defaultTelevisionShouldBeFound("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK);

        // Get all the televisionList where link equals to UPDATED_LINK
        defaultTelevisionShouldNotBeFound("link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllTelevisionsByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where link is not null
        defaultTelevisionShouldBeFound("link.specified=true");

        // Get all the televisionList where link is null
        defaultTelevisionShouldNotBeFound("link.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelevisionsByImagepathIsEqualToSomething() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where imagepath equals to DEFAULT_IMAGEPATH
        defaultTelevisionShouldBeFound("imagepath.equals=" + DEFAULT_IMAGEPATH);

        // Get all the televisionList where imagepath equals to UPDATED_IMAGEPATH
        defaultTelevisionShouldNotBeFound("imagepath.equals=" + UPDATED_IMAGEPATH);
    }

    @Test
    @Transactional
    public void getAllTelevisionsByImagepathIsInShouldWork() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where imagepath in DEFAULT_IMAGEPATH or UPDATED_IMAGEPATH
        defaultTelevisionShouldBeFound("imagepath.in=" + DEFAULT_IMAGEPATH + "," + UPDATED_IMAGEPATH);

        // Get all the televisionList where imagepath equals to UPDATED_IMAGEPATH
        defaultTelevisionShouldNotBeFound("imagepath.in=" + UPDATED_IMAGEPATH);
    }

    @Test
    @Transactional
    public void getAllTelevisionsByImagepathIsNullOrNotNull() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where imagepath is not null
        defaultTelevisionShouldBeFound("imagepath.specified=true");

        // Get all the televisionList where imagepath is null
        defaultTelevisionShouldNotBeFound("imagepath.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelevisionsByVideopathIsEqualToSomething() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where videopath equals to DEFAULT_VIDEOPATH
        defaultTelevisionShouldBeFound("videopath.equals=" + DEFAULT_VIDEOPATH);

        // Get all the televisionList where videopath equals to UPDATED_VIDEOPATH
        defaultTelevisionShouldNotBeFound("videopath.equals=" + UPDATED_VIDEOPATH);
    }

    @Test
    @Transactional
    public void getAllTelevisionsByVideopathIsInShouldWork() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where videopath in DEFAULT_VIDEOPATH or UPDATED_VIDEOPATH
        defaultTelevisionShouldBeFound("videopath.in=" + DEFAULT_VIDEOPATH + "," + UPDATED_VIDEOPATH);

        // Get all the televisionList where videopath equals to UPDATED_VIDEOPATH
        defaultTelevisionShouldNotBeFound("videopath.in=" + UPDATED_VIDEOPATH);
    }

    @Test
    @Transactional
    public void getAllTelevisionsByVideopathIsNullOrNotNull() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        // Get all the televisionList where videopath is not null
        defaultTelevisionShouldBeFound("videopath.specified=true");

        // Get all the televisionList where videopath is null
        defaultTelevisionShouldNotBeFound("videopath.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelevisionsByProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile profile = ProfileResourceIntTest.createEntity(em);
        em.persist(profile);
        em.flush();
        television.setProfile(profile);
        televisionRepository.saveAndFlush(television);
        Long profileId = profile.getId();

        // Get all the televisionList where profile equals to profileId
        defaultTelevisionShouldBeFound("profileId.equals=" + profileId);

        // Get all the televisionList where profile equals to profileId + 1
        defaultTelevisionShouldNotBeFound("profileId.equals=" + (profileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTelevisionShouldBeFound(String filter) throws Exception {
        restTelevisionMockMvc.perform(get("/api/televisions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(television.getId().intValue())))
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
    private void defaultTelevisionShouldNotBeFound(String filter) throws Exception {
        restTelevisionMockMvc.perform(get("/api/televisions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingTelevision() throws Exception {
        // Get the television
        restTelevisionMockMvc.perform(get("/api/televisions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTelevision() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        int databaseSizeBeforeUpdate = televisionRepository.findAll().size();

        // Update the television
        Television updatedTelevision = televisionRepository.findById(television.getId()).get();
        // Disconnect from session so that the updates on updatedTelevision are not directly saved in db
        em.detach(updatedTelevision);
        updatedTelevision
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
        TelevisionDTO televisionDTO = televisionMapper.toDto(updatedTelevision);

        restTelevisionMockMvc.perform(put("/api/televisions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(televisionDTO)))
            .andExpect(status().isOk());

        // Validate the Television in the database
        List<Television> televisionList = televisionRepository.findAll();
        assertThat(televisionList).hasSize(databaseSizeBeforeUpdate);
        Television testTelevision = televisionList.get(televisionList.size() - 1);
        assertThat(testTelevision.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTelevision.getDirector()).isEqualTo(UPDATED_DIRECTOR);
        assertThat(testTelevision.getCameraman()).isEqualTo(UPDATED_CAMERAMAN);
        assertThat(testTelevision.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testTelevision.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testTelevision.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testTelevision.getVideo()).isEqualTo(UPDATED_VIDEO);
        assertThat(testTelevision.getVideoContentType()).isEqualTo(UPDATED_VIDEO_CONTENT_TYPE);
        assertThat(testTelevision.getImagepath()).isEqualTo(UPDATED_IMAGEPATH);
        assertThat(testTelevision.getVideopath()).isEqualTo(UPDATED_VIDEOPATH);
    }

    @Test
    @Transactional
    public void updateNonExistingTelevision() throws Exception {
        int databaseSizeBeforeUpdate = televisionRepository.findAll().size();

        // Create the Television
        TelevisionDTO televisionDTO = televisionMapper.toDto(television);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTelevisionMockMvc.perform(put("/api/televisions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(televisionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Television in the database
        List<Television> televisionList = televisionRepository.findAll();
        assertThat(televisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTelevision() throws Exception {
        // Initialize the database
        televisionRepository.saveAndFlush(television);

        int databaseSizeBeforeDelete = televisionRepository.findAll().size();

        // Get the television
        restTelevisionMockMvc.perform(delete("/api/televisions/{id}", television.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Television> televisionList = televisionRepository.findAll();
        assertThat(televisionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Television.class);
        Television television1 = new Television();
        television1.setId(1L);
        Television television2 = new Television();
        television2.setId(television1.getId());
        assertThat(television1).isEqualTo(television2);
        television2.setId(2L);
        assertThat(television1).isNotEqualTo(television2);
        television1.setId(null);
        assertThat(television1).isNotEqualTo(television2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TelevisionDTO.class);
        TelevisionDTO televisionDTO1 = new TelevisionDTO();
        televisionDTO1.setId(1L);
        TelevisionDTO televisionDTO2 = new TelevisionDTO();
        assertThat(televisionDTO1).isNotEqualTo(televisionDTO2);
        televisionDTO2.setId(televisionDTO1.getId());
        assertThat(televisionDTO1).isEqualTo(televisionDTO2);
        televisionDTO2.setId(2L);
        assertThat(televisionDTO1).isNotEqualTo(televisionDTO2);
        televisionDTO1.setId(null);
        assertThat(televisionDTO1).isNotEqualTo(televisionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(televisionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(televisionMapper.fromId(null)).isNull();
    }
}
