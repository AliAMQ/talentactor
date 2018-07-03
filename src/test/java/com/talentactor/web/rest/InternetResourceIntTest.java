package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Internet;
import com.talentactor.repository.InternetRepository;
import com.talentactor.service.InternetService;
import com.talentactor.service.dto.InternetDTO;
import com.talentactor.service.mapper.InternetMapper;
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
        final InternetResource internetResource = new InternetResource(internetService);
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
