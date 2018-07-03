package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Theater;
import com.talentactor.repository.TheaterRepository;
import com.talentactor.service.TheaterService;
import com.talentactor.service.dto.TheaterDTO;
import com.talentactor.service.mapper.TheaterMapper;
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
 * Test class for the TheaterResource REST controller.
 *
 * @see TheaterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentactorApp.class)
public class TheaterResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTOR = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTOR = "BBBBBBBBBB";

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
    private TheaterRepository theaterRepository;


    @Autowired
    private TheaterMapper theaterMapper;
    

    @Autowired
    private TheaterService theaterService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTheaterMockMvc;

    private Theater theater;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TheaterResource theaterResource = new TheaterResource(theaterService);
        this.restTheaterMockMvc = MockMvcBuilders.standaloneSetup(theaterResource)
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
    public static Theater createEntity(EntityManager em) {
        Theater theater = new Theater()
            .title(DEFAULT_TITLE)
            .director(DEFAULT_DIRECTOR)
            .link(DEFAULT_LINK)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .video(DEFAULT_VIDEO)
            .videoContentType(DEFAULT_VIDEO_CONTENT_TYPE)
            .imagepath(DEFAULT_IMAGEPATH)
            .videopath(DEFAULT_VIDEOPATH);
        return theater;
    }

    @Before
    public void initTest() {
        theater = createEntity(em);
    }

    @Test
    @Transactional
    public void createTheater() throws Exception {
        int databaseSizeBeforeCreate = theaterRepository.findAll().size();

        // Create the Theater
        TheaterDTO theaterDTO = theaterMapper.toDto(theater);
        restTheaterMockMvc.perform(post("/api/theaters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(theaterDTO)))
            .andExpect(status().isCreated());

        // Validate the Theater in the database
        List<Theater> theaterList = theaterRepository.findAll();
        assertThat(theaterList).hasSize(databaseSizeBeforeCreate + 1);
        Theater testTheater = theaterList.get(theaterList.size() - 1);
        assertThat(testTheater.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTheater.getDirector()).isEqualTo(DEFAULT_DIRECTOR);
        assertThat(testTheater.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testTheater.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testTheater.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testTheater.getVideo()).isEqualTo(DEFAULT_VIDEO);
        assertThat(testTheater.getVideoContentType()).isEqualTo(DEFAULT_VIDEO_CONTENT_TYPE);
        assertThat(testTheater.getImagepath()).isEqualTo(DEFAULT_IMAGEPATH);
        assertThat(testTheater.getVideopath()).isEqualTo(DEFAULT_VIDEOPATH);
    }

    @Test
    @Transactional
    public void createTheaterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = theaterRepository.findAll().size();

        // Create the Theater with an existing ID
        theater.setId(1L);
        TheaterDTO theaterDTO = theaterMapper.toDto(theater);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTheaterMockMvc.perform(post("/api/theaters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(theaterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Theater in the database
        List<Theater> theaterList = theaterRepository.findAll();
        assertThat(theaterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = theaterRepository.findAll().size();
        // set the field null
        theater.setTitle(null);

        // Create the Theater, which fails.
        TheaterDTO theaterDTO = theaterMapper.toDto(theater);

        restTheaterMockMvc.perform(post("/api/theaters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(theaterDTO)))
            .andExpect(status().isBadRequest());

        List<Theater> theaterList = theaterRepository.findAll();
        assertThat(theaterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTheaters() throws Exception {
        // Initialize the database
        theaterRepository.saveAndFlush(theater);

        // Get all the theaterList
        restTheaterMockMvc.perform(get("/api/theaters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(theater.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].director").value(hasItem(DEFAULT_DIRECTOR.toString())))
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
    public void getTheater() throws Exception {
        // Initialize the database
        theaterRepository.saveAndFlush(theater);

        // Get the theater
        restTheaterMockMvc.perform(get("/api/theaters/{id}", theater.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(theater.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.director").value(DEFAULT_DIRECTOR.toString()))
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
    public void getNonExistingTheater() throws Exception {
        // Get the theater
        restTheaterMockMvc.perform(get("/api/theaters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTheater() throws Exception {
        // Initialize the database
        theaterRepository.saveAndFlush(theater);

        int databaseSizeBeforeUpdate = theaterRepository.findAll().size();

        // Update the theater
        Theater updatedTheater = theaterRepository.findById(theater.getId()).get();
        // Disconnect from session so that the updates on updatedTheater are not directly saved in db
        em.detach(updatedTheater);
        updatedTheater
            .title(UPDATED_TITLE)
            .director(UPDATED_DIRECTOR)
            .link(UPDATED_LINK)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .video(UPDATED_VIDEO)
            .videoContentType(UPDATED_VIDEO_CONTENT_TYPE)
            .imagepath(UPDATED_IMAGEPATH)
            .videopath(UPDATED_VIDEOPATH);
        TheaterDTO theaterDTO = theaterMapper.toDto(updatedTheater);

        restTheaterMockMvc.perform(put("/api/theaters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(theaterDTO)))
            .andExpect(status().isOk());

        // Validate the Theater in the database
        List<Theater> theaterList = theaterRepository.findAll();
        assertThat(theaterList).hasSize(databaseSizeBeforeUpdate);
        Theater testTheater = theaterList.get(theaterList.size() - 1);
        assertThat(testTheater.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTheater.getDirector()).isEqualTo(UPDATED_DIRECTOR);
        assertThat(testTheater.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testTheater.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testTheater.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testTheater.getVideo()).isEqualTo(UPDATED_VIDEO);
        assertThat(testTheater.getVideoContentType()).isEqualTo(UPDATED_VIDEO_CONTENT_TYPE);
        assertThat(testTheater.getImagepath()).isEqualTo(UPDATED_IMAGEPATH);
        assertThat(testTheater.getVideopath()).isEqualTo(UPDATED_VIDEOPATH);
    }

    @Test
    @Transactional
    public void updateNonExistingTheater() throws Exception {
        int databaseSizeBeforeUpdate = theaterRepository.findAll().size();

        // Create the Theater
        TheaterDTO theaterDTO = theaterMapper.toDto(theater);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTheaterMockMvc.perform(put("/api/theaters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(theaterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Theater in the database
        List<Theater> theaterList = theaterRepository.findAll();
        assertThat(theaterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTheater() throws Exception {
        // Initialize the database
        theaterRepository.saveAndFlush(theater);

        int databaseSizeBeforeDelete = theaterRepository.findAll().size();

        // Get the theater
        restTheaterMockMvc.perform(delete("/api/theaters/{id}", theater.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Theater> theaterList = theaterRepository.findAll();
        assertThat(theaterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Theater.class);
        Theater theater1 = new Theater();
        theater1.setId(1L);
        Theater theater2 = new Theater();
        theater2.setId(theater1.getId());
        assertThat(theater1).isEqualTo(theater2);
        theater2.setId(2L);
        assertThat(theater1).isNotEqualTo(theater2);
        theater1.setId(null);
        assertThat(theater1).isNotEqualTo(theater2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TheaterDTO.class);
        TheaterDTO theaterDTO1 = new TheaterDTO();
        theaterDTO1.setId(1L);
        TheaterDTO theaterDTO2 = new TheaterDTO();
        assertThat(theaterDTO1).isNotEqualTo(theaterDTO2);
        theaterDTO2.setId(theaterDTO1.getId());
        assertThat(theaterDTO1).isEqualTo(theaterDTO2);
        theaterDTO2.setId(2L);
        assertThat(theaterDTO1).isNotEqualTo(theaterDTO2);
        theaterDTO1.setId(null);
        assertThat(theaterDTO1).isNotEqualTo(theaterDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(theaterMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(theaterMapper.fromId(null)).isNull();
    }
}
