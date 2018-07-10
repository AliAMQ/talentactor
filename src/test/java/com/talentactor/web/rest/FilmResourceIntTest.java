package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Film;
import com.talentactor.domain.Profile;
import com.talentactor.repository.FilmRepository;
import com.talentactor.service.FilmService;
import com.talentactor.service.dto.FilmDTO;
import com.talentactor.service.mapper.FilmMapper;
import com.talentactor.web.rest.errors.ExceptionTranslator;
import com.talentactor.service.dto.FilmCriteria;
import com.talentactor.service.FilmQueryService;

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
 * Test class for the FilmResource REST controller.
 *
 * @see FilmResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentactorApp.class)
public class FilmResourceIntTest {

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
    private FilmRepository filmRepository;


    @Autowired
    private FilmMapper filmMapper;
    

    @Autowired
    private FilmService filmService;

    @Autowired
    private FilmQueryService filmQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFilmMockMvc;

    private Film film;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FilmResource filmResource = new FilmResource(filmService, filmQueryService);
        this.restFilmMockMvc = MockMvcBuilders.standaloneSetup(filmResource)
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
    public static Film createEntity(EntityManager em) {
        Film film = new Film()
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
        return film;
    }

    @Before
    public void initTest() {
        film = createEntity(em);
    }

    @Test
    @Transactional
    public void createFilm() throws Exception {
        int databaseSizeBeforeCreate = filmRepository.findAll().size();

        // Create the Film
        FilmDTO filmDTO = filmMapper.toDto(film);
        restFilmMockMvc.perform(post("/api/films")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(filmDTO)))
            .andExpect(status().isCreated());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeCreate + 1);
        Film testFilm = filmList.get(filmList.size() - 1);
        assertThat(testFilm.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFilm.getDirector()).isEqualTo(DEFAULT_DIRECTOR);
        assertThat(testFilm.getCameraman()).isEqualTo(DEFAULT_CAMERAMAN);
        assertThat(testFilm.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testFilm.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testFilm.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testFilm.getVideo()).isEqualTo(DEFAULT_VIDEO);
        assertThat(testFilm.getVideoContentType()).isEqualTo(DEFAULT_VIDEO_CONTENT_TYPE);
        assertThat(testFilm.getImagepath()).isEqualTo(DEFAULT_IMAGEPATH);
        assertThat(testFilm.getVideopath()).isEqualTo(DEFAULT_VIDEOPATH);
    }

    @Test
    @Transactional
    public void createFilmWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = filmRepository.findAll().size();

        // Create the Film with an existing ID
        film.setId(1L);
        FilmDTO filmDTO = filmMapper.toDto(film);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilmMockMvc.perform(post("/api/films")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(filmDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = filmRepository.findAll().size();
        // set the field null
        film.setTitle(null);

        // Create the Film, which fails.
        FilmDTO filmDTO = filmMapper.toDto(film);

        restFilmMockMvc.perform(post("/api/films")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(filmDTO)))
            .andExpect(status().isBadRequest());

        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFilms() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList
        restFilmMockMvc.perform(get("/api/films?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(film.getId().intValue())))
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
    public void getFilm() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get the film
        restFilmMockMvc.perform(get("/api/films/{id}", film.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(film.getId().intValue()))
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
    public void getAllFilmsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where title equals to DEFAULT_TITLE
        defaultFilmShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the filmList where title equals to UPDATED_TITLE
        defaultFilmShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllFilmsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultFilmShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the filmList where title equals to UPDATED_TITLE
        defaultFilmShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllFilmsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where title is not null
        defaultFilmShouldBeFound("title.specified=true");

        // Get all the filmList where title is null
        defaultFilmShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllFilmsByDirectorIsEqualToSomething() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where director equals to DEFAULT_DIRECTOR
        defaultFilmShouldBeFound("director.equals=" + DEFAULT_DIRECTOR);

        // Get all the filmList where director equals to UPDATED_DIRECTOR
        defaultFilmShouldNotBeFound("director.equals=" + UPDATED_DIRECTOR);
    }

    @Test
    @Transactional
    public void getAllFilmsByDirectorIsInShouldWork() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where director in DEFAULT_DIRECTOR or UPDATED_DIRECTOR
        defaultFilmShouldBeFound("director.in=" + DEFAULT_DIRECTOR + "," + UPDATED_DIRECTOR);

        // Get all the filmList where director equals to UPDATED_DIRECTOR
        defaultFilmShouldNotBeFound("director.in=" + UPDATED_DIRECTOR);
    }

    @Test
    @Transactional
    public void getAllFilmsByDirectorIsNullOrNotNull() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where director is not null
        defaultFilmShouldBeFound("director.specified=true");

        // Get all the filmList where director is null
        defaultFilmShouldNotBeFound("director.specified=false");
    }

    @Test
    @Transactional
    public void getAllFilmsByCameramanIsEqualToSomething() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where cameraman equals to DEFAULT_CAMERAMAN
        defaultFilmShouldBeFound("cameraman.equals=" + DEFAULT_CAMERAMAN);

        // Get all the filmList where cameraman equals to UPDATED_CAMERAMAN
        defaultFilmShouldNotBeFound("cameraman.equals=" + UPDATED_CAMERAMAN);
    }

    @Test
    @Transactional
    public void getAllFilmsByCameramanIsInShouldWork() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where cameraman in DEFAULT_CAMERAMAN or UPDATED_CAMERAMAN
        defaultFilmShouldBeFound("cameraman.in=" + DEFAULT_CAMERAMAN + "," + UPDATED_CAMERAMAN);

        // Get all the filmList where cameraman equals to UPDATED_CAMERAMAN
        defaultFilmShouldNotBeFound("cameraman.in=" + UPDATED_CAMERAMAN);
    }

    @Test
    @Transactional
    public void getAllFilmsByCameramanIsNullOrNotNull() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where cameraman is not null
        defaultFilmShouldBeFound("cameraman.specified=true");

        // Get all the filmList where cameraman is null
        defaultFilmShouldNotBeFound("cameraman.specified=false");
    }

    @Test
    @Transactional
    public void getAllFilmsByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where link equals to DEFAULT_LINK
        defaultFilmShouldBeFound("link.equals=" + DEFAULT_LINK);

        // Get all the filmList where link equals to UPDATED_LINK
        defaultFilmShouldNotBeFound("link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllFilmsByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where link in DEFAULT_LINK or UPDATED_LINK
        defaultFilmShouldBeFound("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK);

        // Get all the filmList where link equals to UPDATED_LINK
        defaultFilmShouldNotBeFound("link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllFilmsByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where link is not null
        defaultFilmShouldBeFound("link.specified=true");

        // Get all the filmList where link is null
        defaultFilmShouldNotBeFound("link.specified=false");
    }

    @Test
    @Transactional
    public void getAllFilmsByImagepathIsEqualToSomething() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where imagepath equals to DEFAULT_IMAGEPATH
        defaultFilmShouldBeFound("imagepath.equals=" + DEFAULT_IMAGEPATH);

        // Get all the filmList where imagepath equals to UPDATED_IMAGEPATH
        defaultFilmShouldNotBeFound("imagepath.equals=" + UPDATED_IMAGEPATH);
    }

    @Test
    @Transactional
    public void getAllFilmsByImagepathIsInShouldWork() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where imagepath in DEFAULT_IMAGEPATH or UPDATED_IMAGEPATH
        defaultFilmShouldBeFound("imagepath.in=" + DEFAULT_IMAGEPATH + "," + UPDATED_IMAGEPATH);

        // Get all the filmList where imagepath equals to UPDATED_IMAGEPATH
        defaultFilmShouldNotBeFound("imagepath.in=" + UPDATED_IMAGEPATH);
    }

    @Test
    @Transactional
    public void getAllFilmsByImagepathIsNullOrNotNull() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where imagepath is not null
        defaultFilmShouldBeFound("imagepath.specified=true");

        // Get all the filmList where imagepath is null
        defaultFilmShouldNotBeFound("imagepath.specified=false");
    }

    @Test
    @Transactional
    public void getAllFilmsByVideopathIsEqualToSomething() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where videopath equals to DEFAULT_VIDEOPATH
        defaultFilmShouldBeFound("videopath.equals=" + DEFAULT_VIDEOPATH);

        // Get all the filmList where videopath equals to UPDATED_VIDEOPATH
        defaultFilmShouldNotBeFound("videopath.equals=" + UPDATED_VIDEOPATH);
    }

    @Test
    @Transactional
    public void getAllFilmsByVideopathIsInShouldWork() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where videopath in DEFAULT_VIDEOPATH or UPDATED_VIDEOPATH
        defaultFilmShouldBeFound("videopath.in=" + DEFAULT_VIDEOPATH + "," + UPDATED_VIDEOPATH);

        // Get all the filmList where videopath equals to UPDATED_VIDEOPATH
        defaultFilmShouldNotBeFound("videopath.in=" + UPDATED_VIDEOPATH);
    }

    @Test
    @Transactional
    public void getAllFilmsByVideopathIsNullOrNotNull() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList where videopath is not null
        defaultFilmShouldBeFound("videopath.specified=true");

        // Get all the filmList where videopath is null
        defaultFilmShouldNotBeFound("videopath.specified=false");
    }

    @Test
    @Transactional
    public void getAllFilmsByProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile profile = ProfileResourceIntTest.createEntity(em);
        em.persist(profile);
        em.flush();
        film.setProfile(profile);
        filmRepository.saveAndFlush(film);
        Long profileId = profile.getId();

        // Get all the filmList where profile equals to profileId
        defaultFilmShouldBeFound("profileId.equals=" + profileId);

        // Get all the filmList where profile equals to profileId + 1
        defaultFilmShouldNotBeFound("profileId.equals=" + (profileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultFilmShouldBeFound(String filter) throws Exception {
        restFilmMockMvc.perform(get("/api/films?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(film.getId().intValue())))
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
    private void defaultFilmShouldNotBeFound(String filter) throws Exception {
        restFilmMockMvc.perform(get("/api/films?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingFilm() throws Exception {
        // Get the film
        restFilmMockMvc.perform(get("/api/films/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFilm() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        int databaseSizeBeforeUpdate = filmRepository.findAll().size();

        // Update the film
        Film updatedFilm = filmRepository.findById(film.getId()).get();
        // Disconnect from session so that the updates on updatedFilm are not directly saved in db
        em.detach(updatedFilm);
        updatedFilm
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
        FilmDTO filmDTO = filmMapper.toDto(updatedFilm);

        restFilmMockMvc.perform(put("/api/films")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(filmDTO)))
            .andExpect(status().isOk());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
        Film testFilm = filmList.get(filmList.size() - 1);
        assertThat(testFilm.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFilm.getDirector()).isEqualTo(UPDATED_DIRECTOR);
        assertThat(testFilm.getCameraman()).isEqualTo(UPDATED_CAMERAMAN);
        assertThat(testFilm.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testFilm.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testFilm.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testFilm.getVideo()).isEqualTo(UPDATED_VIDEO);
        assertThat(testFilm.getVideoContentType()).isEqualTo(UPDATED_VIDEO_CONTENT_TYPE);
        assertThat(testFilm.getImagepath()).isEqualTo(UPDATED_IMAGEPATH);
        assertThat(testFilm.getVideopath()).isEqualTo(UPDATED_VIDEOPATH);
    }

    @Test
    @Transactional
    public void updateNonExistingFilm() throws Exception {
        int databaseSizeBeforeUpdate = filmRepository.findAll().size();

        // Create the Film
        FilmDTO filmDTO = filmMapper.toDto(film);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFilmMockMvc.perform(put("/api/films")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(filmDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFilm() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        int databaseSizeBeforeDelete = filmRepository.findAll().size();

        // Get the film
        restFilmMockMvc.perform(delete("/api/films/{id}", film.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Film.class);
        Film film1 = new Film();
        film1.setId(1L);
        Film film2 = new Film();
        film2.setId(film1.getId());
        assertThat(film1).isEqualTo(film2);
        film2.setId(2L);
        assertThat(film1).isNotEqualTo(film2);
        film1.setId(null);
        assertThat(film1).isNotEqualTo(film2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FilmDTO.class);
        FilmDTO filmDTO1 = new FilmDTO();
        filmDTO1.setId(1L);
        FilmDTO filmDTO2 = new FilmDTO();
        assertThat(filmDTO1).isNotEqualTo(filmDTO2);
        filmDTO2.setId(filmDTO1.getId());
        assertThat(filmDTO1).isEqualTo(filmDTO2);
        filmDTO2.setId(2L);
        assertThat(filmDTO1).isNotEqualTo(filmDTO2);
        filmDTO1.setId(null);
        assertThat(filmDTO1).isNotEqualTo(filmDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(filmMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(filmMapper.fromId(null)).isNull();
    }
}
