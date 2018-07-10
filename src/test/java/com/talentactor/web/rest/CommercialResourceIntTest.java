package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Commercial;
import com.talentactor.domain.Profile;
import com.talentactor.repository.CommercialRepository;
import com.talentactor.service.CommercialService;
import com.talentactor.service.dto.CommercialDTO;
import com.talentactor.service.mapper.CommercialMapper;
import com.talentactor.web.rest.errors.ExceptionTranslator;
import com.talentactor.service.dto.CommercialCriteria;
import com.talentactor.service.CommercialQueryService;

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
 * Test class for the CommercialResource REST controller.
 *
 * @see CommercialResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentactorApp.class)
public class CommercialResourceIntTest {

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
    private CommercialRepository commercialRepository;


    @Autowired
    private CommercialMapper commercialMapper;
    

    @Autowired
    private CommercialService commercialService;

    @Autowired
    private CommercialQueryService commercialQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCommercialMockMvc;

    private Commercial commercial;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CommercialResource commercialResource = new CommercialResource(commercialService, commercialQueryService);
        this.restCommercialMockMvc = MockMvcBuilders.standaloneSetup(commercialResource)
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
    public static Commercial createEntity(EntityManager em) {
        Commercial commercial = new Commercial()
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
        return commercial;
    }

    @Before
    public void initTest() {
        commercial = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommercial() throws Exception {
        int databaseSizeBeforeCreate = commercialRepository.findAll().size();

        // Create the Commercial
        CommercialDTO commercialDTO = commercialMapper.toDto(commercial);
        restCommercialMockMvc.perform(post("/api/commercials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commercialDTO)))
            .andExpect(status().isCreated());

        // Validate the Commercial in the database
        List<Commercial> commercialList = commercialRepository.findAll();
        assertThat(commercialList).hasSize(databaseSizeBeforeCreate + 1);
        Commercial testCommercial = commercialList.get(commercialList.size() - 1);
        assertThat(testCommercial.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCommercial.getDirector()).isEqualTo(DEFAULT_DIRECTOR);
        assertThat(testCommercial.getCameraman()).isEqualTo(DEFAULT_CAMERAMAN);
        assertThat(testCommercial.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testCommercial.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testCommercial.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testCommercial.getVideo()).isEqualTo(DEFAULT_VIDEO);
        assertThat(testCommercial.getVideoContentType()).isEqualTo(DEFAULT_VIDEO_CONTENT_TYPE);
        assertThat(testCommercial.getImagepath()).isEqualTo(DEFAULT_IMAGEPATH);
        assertThat(testCommercial.getVideopath()).isEqualTo(DEFAULT_VIDEOPATH);
    }

    @Test
    @Transactional
    public void createCommercialWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commercialRepository.findAll().size();

        // Create the Commercial with an existing ID
        commercial.setId(1L);
        CommercialDTO commercialDTO = commercialMapper.toDto(commercial);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommercialMockMvc.perform(post("/api/commercials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commercialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Commercial in the database
        List<Commercial> commercialList = commercialRepository.findAll();
        assertThat(commercialList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = commercialRepository.findAll().size();
        // set the field null
        commercial.setTitle(null);

        // Create the Commercial, which fails.
        CommercialDTO commercialDTO = commercialMapper.toDto(commercial);

        restCommercialMockMvc.perform(post("/api/commercials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commercialDTO)))
            .andExpect(status().isBadRequest());

        List<Commercial> commercialList = commercialRepository.findAll();
        assertThat(commercialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommercials() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList
        restCommercialMockMvc.perform(get("/api/commercials?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commercial.getId().intValue())))
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
    public void getCommercial() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get the commercial
        restCommercialMockMvc.perform(get("/api/commercials/{id}", commercial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(commercial.getId().intValue()))
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
    public void getAllCommercialsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where title equals to DEFAULT_TITLE
        defaultCommercialShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the commercialList where title equals to UPDATED_TITLE
        defaultCommercialShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCommercialsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCommercialShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the commercialList where title equals to UPDATED_TITLE
        defaultCommercialShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCommercialsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where title is not null
        defaultCommercialShouldBeFound("title.specified=true");

        // Get all the commercialList where title is null
        defaultCommercialShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommercialsByDirectorIsEqualToSomething() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where director equals to DEFAULT_DIRECTOR
        defaultCommercialShouldBeFound("director.equals=" + DEFAULT_DIRECTOR);

        // Get all the commercialList where director equals to UPDATED_DIRECTOR
        defaultCommercialShouldNotBeFound("director.equals=" + UPDATED_DIRECTOR);
    }

    @Test
    @Transactional
    public void getAllCommercialsByDirectorIsInShouldWork() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where director in DEFAULT_DIRECTOR or UPDATED_DIRECTOR
        defaultCommercialShouldBeFound("director.in=" + DEFAULT_DIRECTOR + "," + UPDATED_DIRECTOR);

        // Get all the commercialList where director equals to UPDATED_DIRECTOR
        defaultCommercialShouldNotBeFound("director.in=" + UPDATED_DIRECTOR);
    }

    @Test
    @Transactional
    public void getAllCommercialsByDirectorIsNullOrNotNull() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where director is not null
        defaultCommercialShouldBeFound("director.specified=true");

        // Get all the commercialList where director is null
        defaultCommercialShouldNotBeFound("director.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommercialsByCameramanIsEqualToSomething() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where cameraman equals to DEFAULT_CAMERAMAN
        defaultCommercialShouldBeFound("cameraman.equals=" + DEFAULT_CAMERAMAN);

        // Get all the commercialList where cameraman equals to UPDATED_CAMERAMAN
        defaultCommercialShouldNotBeFound("cameraman.equals=" + UPDATED_CAMERAMAN);
    }

    @Test
    @Transactional
    public void getAllCommercialsByCameramanIsInShouldWork() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where cameraman in DEFAULT_CAMERAMAN or UPDATED_CAMERAMAN
        defaultCommercialShouldBeFound("cameraman.in=" + DEFAULT_CAMERAMAN + "," + UPDATED_CAMERAMAN);

        // Get all the commercialList where cameraman equals to UPDATED_CAMERAMAN
        defaultCommercialShouldNotBeFound("cameraman.in=" + UPDATED_CAMERAMAN);
    }

    @Test
    @Transactional
    public void getAllCommercialsByCameramanIsNullOrNotNull() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where cameraman is not null
        defaultCommercialShouldBeFound("cameraman.specified=true");

        // Get all the commercialList where cameraman is null
        defaultCommercialShouldNotBeFound("cameraman.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommercialsByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where link equals to DEFAULT_LINK
        defaultCommercialShouldBeFound("link.equals=" + DEFAULT_LINK);

        // Get all the commercialList where link equals to UPDATED_LINK
        defaultCommercialShouldNotBeFound("link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllCommercialsByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where link in DEFAULT_LINK or UPDATED_LINK
        defaultCommercialShouldBeFound("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK);

        // Get all the commercialList where link equals to UPDATED_LINK
        defaultCommercialShouldNotBeFound("link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllCommercialsByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where link is not null
        defaultCommercialShouldBeFound("link.specified=true");

        // Get all the commercialList where link is null
        defaultCommercialShouldNotBeFound("link.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommercialsByImagepathIsEqualToSomething() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where imagepath equals to DEFAULT_IMAGEPATH
        defaultCommercialShouldBeFound("imagepath.equals=" + DEFAULT_IMAGEPATH);

        // Get all the commercialList where imagepath equals to UPDATED_IMAGEPATH
        defaultCommercialShouldNotBeFound("imagepath.equals=" + UPDATED_IMAGEPATH);
    }

    @Test
    @Transactional
    public void getAllCommercialsByImagepathIsInShouldWork() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where imagepath in DEFAULT_IMAGEPATH or UPDATED_IMAGEPATH
        defaultCommercialShouldBeFound("imagepath.in=" + DEFAULT_IMAGEPATH + "," + UPDATED_IMAGEPATH);

        // Get all the commercialList where imagepath equals to UPDATED_IMAGEPATH
        defaultCommercialShouldNotBeFound("imagepath.in=" + UPDATED_IMAGEPATH);
    }

    @Test
    @Transactional
    public void getAllCommercialsByImagepathIsNullOrNotNull() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where imagepath is not null
        defaultCommercialShouldBeFound("imagepath.specified=true");

        // Get all the commercialList where imagepath is null
        defaultCommercialShouldNotBeFound("imagepath.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommercialsByVideopathIsEqualToSomething() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where videopath equals to DEFAULT_VIDEOPATH
        defaultCommercialShouldBeFound("videopath.equals=" + DEFAULT_VIDEOPATH);

        // Get all the commercialList where videopath equals to UPDATED_VIDEOPATH
        defaultCommercialShouldNotBeFound("videopath.equals=" + UPDATED_VIDEOPATH);
    }

    @Test
    @Transactional
    public void getAllCommercialsByVideopathIsInShouldWork() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where videopath in DEFAULT_VIDEOPATH or UPDATED_VIDEOPATH
        defaultCommercialShouldBeFound("videopath.in=" + DEFAULT_VIDEOPATH + "," + UPDATED_VIDEOPATH);

        // Get all the commercialList where videopath equals to UPDATED_VIDEOPATH
        defaultCommercialShouldNotBeFound("videopath.in=" + UPDATED_VIDEOPATH);
    }

    @Test
    @Transactional
    public void getAllCommercialsByVideopathIsNullOrNotNull() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        // Get all the commercialList where videopath is not null
        defaultCommercialShouldBeFound("videopath.specified=true");

        // Get all the commercialList where videopath is null
        defaultCommercialShouldNotBeFound("videopath.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommercialsByProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile profile = ProfileResourceIntTest.createEntity(em);
        em.persist(profile);
        em.flush();
        commercial.setProfile(profile);
        commercialRepository.saveAndFlush(commercial);
        Long profileId = profile.getId();

        // Get all the commercialList where profile equals to profileId
        defaultCommercialShouldBeFound("profileId.equals=" + profileId);

        // Get all the commercialList where profile equals to profileId + 1
        defaultCommercialShouldNotBeFound("profileId.equals=" + (profileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCommercialShouldBeFound(String filter) throws Exception {
        restCommercialMockMvc.perform(get("/api/commercials?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commercial.getId().intValue())))
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
    private void defaultCommercialShouldNotBeFound(String filter) throws Exception {
        restCommercialMockMvc.perform(get("/api/commercials?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingCommercial() throws Exception {
        // Get the commercial
        restCommercialMockMvc.perform(get("/api/commercials/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommercial() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        int databaseSizeBeforeUpdate = commercialRepository.findAll().size();

        // Update the commercial
        Commercial updatedCommercial = commercialRepository.findById(commercial.getId()).get();
        // Disconnect from session so that the updates on updatedCommercial are not directly saved in db
        em.detach(updatedCommercial);
        updatedCommercial
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
        CommercialDTO commercialDTO = commercialMapper.toDto(updatedCommercial);

        restCommercialMockMvc.perform(put("/api/commercials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commercialDTO)))
            .andExpect(status().isOk());

        // Validate the Commercial in the database
        List<Commercial> commercialList = commercialRepository.findAll();
        assertThat(commercialList).hasSize(databaseSizeBeforeUpdate);
        Commercial testCommercial = commercialList.get(commercialList.size() - 1);
        assertThat(testCommercial.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCommercial.getDirector()).isEqualTo(UPDATED_DIRECTOR);
        assertThat(testCommercial.getCameraman()).isEqualTo(UPDATED_CAMERAMAN);
        assertThat(testCommercial.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testCommercial.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCommercial.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testCommercial.getVideo()).isEqualTo(UPDATED_VIDEO);
        assertThat(testCommercial.getVideoContentType()).isEqualTo(UPDATED_VIDEO_CONTENT_TYPE);
        assertThat(testCommercial.getImagepath()).isEqualTo(UPDATED_IMAGEPATH);
        assertThat(testCommercial.getVideopath()).isEqualTo(UPDATED_VIDEOPATH);
    }

    @Test
    @Transactional
    public void updateNonExistingCommercial() throws Exception {
        int databaseSizeBeforeUpdate = commercialRepository.findAll().size();

        // Create the Commercial
        CommercialDTO commercialDTO = commercialMapper.toDto(commercial);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCommercialMockMvc.perform(put("/api/commercials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commercialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Commercial in the database
        List<Commercial> commercialList = commercialRepository.findAll();
        assertThat(commercialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCommercial() throws Exception {
        // Initialize the database
        commercialRepository.saveAndFlush(commercial);

        int databaseSizeBeforeDelete = commercialRepository.findAll().size();

        // Get the commercial
        restCommercialMockMvc.perform(delete("/api/commercials/{id}", commercial.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Commercial> commercialList = commercialRepository.findAll();
        assertThat(commercialList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commercial.class);
        Commercial commercial1 = new Commercial();
        commercial1.setId(1L);
        Commercial commercial2 = new Commercial();
        commercial2.setId(commercial1.getId());
        assertThat(commercial1).isEqualTo(commercial2);
        commercial2.setId(2L);
        assertThat(commercial1).isNotEqualTo(commercial2);
        commercial1.setId(null);
        assertThat(commercial1).isNotEqualTo(commercial2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommercialDTO.class);
        CommercialDTO commercialDTO1 = new CommercialDTO();
        commercialDTO1.setId(1L);
        CommercialDTO commercialDTO2 = new CommercialDTO();
        assertThat(commercialDTO1).isNotEqualTo(commercialDTO2);
        commercialDTO2.setId(commercialDTO1.getId());
        assertThat(commercialDTO1).isEqualTo(commercialDTO2);
        commercialDTO2.setId(2L);
        assertThat(commercialDTO1).isNotEqualTo(commercialDTO2);
        commercialDTO1.setId(null);
        assertThat(commercialDTO1).isNotEqualTo(commercialDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(commercialMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(commercialMapper.fromId(null)).isNull();
    }
}
