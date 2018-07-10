package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Profile;
import com.talentactor.domain.User;
import com.talentactor.domain.Role;
import com.talentactor.domain.Film;
import com.talentactor.domain.Television;
import com.talentactor.domain.Internet;
import com.talentactor.domain.Commercial;
import com.talentactor.domain.Print;
import com.talentactor.domain.Theater;
import com.talentactor.domain.Voice;
import com.talentactor.domain.Skill;
import com.talentactor.domain.Sport;
import com.talentactor.domain.Swimming;
import com.talentactor.domain.Combat;
import com.talentactor.domain.Language;
import com.talentactor.domain.Instrument;
import com.talentactor.domain.Weapon;
import com.talentactor.domain.Cycling;
import com.talentactor.domain.Circus;
import com.talentactor.domain.Horse;
import com.talentactor.repository.ProfileRepository;
import com.talentactor.service.ProfileService;
import com.talentactor.service.dto.ProfileDTO;
import com.talentactor.service.mapper.ProfileMapper;
import com.talentactor.web.rest.errors.ExceptionTranslator;
import com.talentactor.service.dto.ProfileCriteria;
import com.talentactor.service.ProfileQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


import static com.talentactor.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.talentactor.domain.enumeration.State;
/**
 * Test class for the ProfileResource REST controller.
 *
 * @see ProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentactorApp.class)
public class ProfileResourceIntTest {

    private static final State DEFAULT_STATE = State.AK;
    private static final State UPDATED_STATE = State.AL;

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SINCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SINCE = LocalDate.now(ZoneId.systemDefault());

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_VIDEO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_VIDEO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_VIDEO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_VIDEO_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_AUDIO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_AUDIO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_AUDIO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_AUDIO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMAGEPATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGEPATH = "BBBBBBBBBB";

    private static final String DEFAULT_VIDEOPATH = "AAAAAAAAAA";
    private static final String UPDATED_VIDEOPATH = "BBBBBBBBBB";

    private static final String DEFAULT_AUDIOPATH = "AAAAAAAAAA";
    private static final String UPDATED_AUDIOPATH = "BBBBBBBBBB";

    @Autowired
    private ProfileRepository profileRepository;
    @Mock
    private ProfileRepository profileRepositoryMock;

    @Autowired
    private ProfileMapper profileMapper;
    
    @Mock
    private ProfileService profileServiceMock;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileQueryService profileQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProfileMockMvc;

    private Profile profile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfileResource profileResource = new ProfileResource(profileService, profileQueryService);
        this.restProfileMockMvc = MockMvcBuilders.standaloneSetup(profileResource)
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
    public static Profile createEntity(EntityManager em) {
        Profile profile = new Profile()
            .state(DEFAULT_STATE)
            .city(DEFAULT_CITY)
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE)
            .since(DEFAULT_SINCE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .video(DEFAULT_VIDEO)
            .videoContentType(DEFAULT_VIDEO_CONTENT_TYPE)
            .audio(DEFAULT_AUDIO)
            .audioContentType(DEFAULT_AUDIO_CONTENT_TYPE)
            .imagepath(DEFAULT_IMAGEPATH)
            .videopath(DEFAULT_VIDEOPATH)
            .audiopath(DEFAULT_AUDIOPATH);
        return profile;
    }

    @Before
    public void initTest() {
        profile = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfile() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isCreated());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate + 1);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testProfile.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testProfile.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testProfile.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testProfile.getSince()).isEqualTo(DEFAULT_SINCE);
        assertThat(testProfile.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testProfile.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testProfile.getVideo()).isEqualTo(DEFAULT_VIDEO);
        assertThat(testProfile.getVideoContentType()).isEqualTo(DEFAULT_VIDEO_CONTENT_TYPE);
        assertThat(testProfile.getAudio()).isEqualTo(DEFAULT_AUDIO);
        assertThat(testProfile.getAudioContentType()).isEqualTo(DEFAULT_AUDIO_CONTENT_TYPE);
        assertThat(testProfile.getImagepath()).isEqualTo(DEFAULT_IMAGEPATH);
        assertThat(testProfile.getVideopath()).isEqualTo(DEFAULT_VIDEOPATH);
        assertThat(testProfile.getAudiopath()).isEqualTo(DEFAULT_AUDIOPATH);
    }

    @Test
    @Transactional
    public void createProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile with an existing ID
        profile.setId(1L);
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProfiles() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].since").value(hasItem(DEFAULT_SINCE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].videoContentType").value(hasItem(DEFAULT_VIDEO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].video").value(hasItem(Base64Utils.encodeToString(DEFAULT_VIDEO))))
            .andExpect(jsonPath("$.[*].audioContentType").value(hasItem(DEFAULT_AUDIO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].audio").value(hasItem(Base64Utils.encodeToString(DEFAULT_AUDIO))))
            .andExpect(jsonPath("$.[*].imagepath").value(hasItem(DEFAULT_IMAGEPATH.toString())))
            .andExpect(jsonPath("$.[*].videopath").value(hasItem(DEFAULT_VIDEOPATH.toString())))
            .andExpect(jsonPath("$.[*].audiopath").value(hasItem(DEFAULT_AUDIOPATH.toString())));
    }
    
    public void getAllProfilesWithEagerRelationshipsIsEnabled() throws Exception {
        ProfileResource profileResource = new ProfileResource(profileServiceMock, profileQueryService);
        when(profileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restProfileMockMvc = MockMvcBuilders.standaloneSetup(profileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restProfileMockMvc.perform(get("/api/profiles?eagerload=true"))
        .andExpect(status().isOk());

        verify(profileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllProfilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        ProfileResource profileResource = new ProfileResource(profileServiceMock, profileQueryService);
            when(profileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restProfileMockMvc = MockMvcBuilders.standaloneSetup(profileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restProfileMockMvc.perform(get("/api/profiles?eagerload=true"))
        .andExpect(status().isOk());

            verify(profileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", profile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profile.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.since").value(DEFAULT_SINCE.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.videoContentType").value(DEFAULT_VIDEO_CONTENT_TYPE))
            .andExpect(jsonPath("$.video").value(Base64Utils.encodeToString(DEFAULT_VIDEO)))
            .andExpect(jsonPath("$.audioContentType").value(DEFAULT_AUDIO_CONTENT_TYPE))
            .andExpect(jsonPath("$.audio").value(Base64Utils.encodeToString(DEFAULT_AUDIO)))
            .andExpect(jsonPath("$.imagepath").value(DEFAULT_IMAGEPATH.toString()))
            .andExpect(jsonPath("$.videopath").value(DEFAULT_VIDEOPATH.toString()))
            .andExpect(jsonPath("$.audiopath").value(DEFAULT_AUDIOPATH.toString()));
    }

    @Test
    @Transactional
    public void getAllProfilesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where state equals to DEFAULT_STATE
        defaultProfileShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the profileList where state equals to UPDATED_STATE
        defaultProfileShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllProfilesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where state in DEFAULT_STATE or UPDATED_STATE
        defaultProfileShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the profileList where state equals to UPDATED_STATE
        defaultProfileShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllProfilesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where state is not null
        defaultProfileShouldBeFound("state.specified=true");

        // Get all the profileList where state is null
        defaultProfileShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where city equals to DEFAULT_CITY
        defaultProfileShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the profileList where city equals to UPDATED_CITY
        defaultProfileShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllProfilesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where city in DEFAULT_CITY or UPDATED_CITY
        defaultProfileShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the profileList where city equals to UPDATED_CITY
        defaultProfileShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllProfilesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where city is not null
        defaultProfileShouldBeFound("city.specified=true");

        // Get all the profileList where city is null
        defaultProfileShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where address equals to DEFAULT_ADDRESS
        defaultProfileShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the profileList where address equals to UPDATED_ADDRESS
        defaultProfileShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllProfilesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultProfileShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the profileList where address equals to UPDATED_ADDRESS
        defaultProfileShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllProfilesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where address is not null
        defaultProfileShouldBeFound("address.specified=true");

        // Get all the profileList where address is null
        defaultProfileShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phone equals to DEFAULT_PHONE
        defaultProfileShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the profileList where phone equals to UPDATED_PHONE
        defaultProfileShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllProfilesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultProfileShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the profileList where phone equals to UPDATED_PHONE
        defaultProfileShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllProfilesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phone is not null
        defaultProfileShouldBeFound("phone.specified=true");

        // Get all the profileList where phone is null
        defaultProfileShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesBySinceIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where since equals to DEFAULT_SINCE
        defaultProfileShouldBeFound("since.equals=" + DEFAULT_SINCE);

        // Get all the profileList where since equals to UPDATED_SINCE
        defaultProfileShouldNotBeFound("since.equals=" + UPDATED_SINCE);
    }

    @Test
    @Transactional
    public void getAllProfilesBySinceIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where since in DEFAULT_SINCE or UPDATED_SINCE
        defaultProfileShouldBeFound("since.in=" + DEFAULT_SINCE + "," + UPDATED_SINCE);

        // Get all the profileList where since equals to UPDATED_SINCE
        defaultProfileShouldNotBeFound("since.in=" + UPDATED_SINCE);
    }

    @Test
    @Transactional
    public void getAllProfilesBySinceIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where since is not null
        defaultProfileShouldBeFound("since.specified=true");

        // Get all the profileList where since is null
        defaultProfileShouldNotBeFound("since.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesBySinceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where since greater than or equals to DEFAULT_SINCE
        defaultProfileShouldBeFound("since.greaterOrEqualThan=" + DEFAULT_SINCE);

        // Get all the profileList where since greater than or equals to UPDATED_SINCE
        defaultProfileShouldNotBeFound("since.greaterOrEqualThan=" + UPDATED_SINCE);
    }

    @Test
    @Transactional
    public void getAllProfilesBySinceIsLessThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where since less than or equals to DEFAULT_SINCE
        defaultProfileShouldNotBeFound("since.lessThan=" + DEFAULT_SINCE);

        // Get all the profileList where since less than or equals to UPDATED_SINCE
        defaultProfileShouldBeFound("since.lessThan=" + UPDATED_SINCE);
    }


    @Test
    @Transactional
    public void getAllProfilesByImagepathIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where imagepath equals to DEFAULT_IMAGEPATH
        defaultProfileShouldBeFound("imagepath.equals=" + DEFAULT_IMAGEPATH);

        // Get all the profileList where imagepath equals to UPDATED_IMAGEPATH
        defaultProfileShouldNotBeFound("imagepath.equals=" + UPDATED_IMAGEPATH);
    }

    @Test
    @Transactional
    public void getAllProfilesByImagepathIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where imagepath in DEFAULT_IMAGEPATH or UPDATED_IMAGEPATH
        defaultProfileShouldBeFound("imagepath.in=" + DEFAULT_IMAGEPATH + "," + UPDATED_IMAGEPATH);

        // Get all the profileList where imagepath equals to UPDATED_IMAGEPATH
        defaultProfileShouldNotBeFound("imagepath.in=" + UPDATED_IMAGEPATH);
    }

    @Test
    @Transactional
    public void getAllProfilesByImagepathIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where imagepath is not null
        defaultProfileShouldBeFound("imagepath.specified=true");

        // Get all the profileList where imagepath is null
        defaultProfileShouldNotBeFound("imagepath.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByVideopathIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where videopath equals to DEFAULT_VIDEOPATH
        defaultProfileShouldBeFound("videopath.equals=" + DEFAULT_VIDEOPATH);

        // Get all the profileList where videopath equals to UPDATED_VIDEOPATH
        defaultProfileShouldNotBeFound("videopath.equals=" + UPDATED_VIDEOPATH);
    }

    @Test
    @Transactional
    public void getAllProfilesByVideopathIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where videopath in DEFAULT_VIDEOPATH or UPDATED_VIDEOPATH
        defaultProfileShouldBeFound("videopath.in=" + DEFAULT_VIDEOPATH + "," + UPDATED_VIDEOPATH);

        // Get all the profileList where videopath equals to UPDATED_VIDEOPATH
        defaultProfileShouldNotBeFound("videopath.in=" + UPDATED_VIDEOPATH);
    }

    @Test
    @Transactional
    public void getAllProfilesByVideopathIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where videopath is not null
        defaultProfileShouldBeFound("videopath.specified=true");

        // Get all the profileList where videopath is null
        defaultProfileShouldNotBeFound("videopath.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByAudiopathIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where audiopath equals to DEFAULT_AUDIOPATH
        defaultProfileShouldBeFound("audiopath.equals=" + DEFAULT_AUDIOPATH);

        // Get all the profileList where audiopath equals to UPDATED_AUDIOPATH
        defaultProfileShouldNotBeFound("audiopath.equals=" + UPDATED_AUDIOPATH);
    }

    @Test
    @Transactional
    public void getAllProfilesByAudiopathIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where audiopath in DEFAULT_AUDIOPATH or UPDATED_AUDIOPATH
        defaultProfileShouldBeFound("audiopath.in=" + DEFAULT_AUDIOPATH + "," + UPDATED_AUDIOPATH);

        // Get all the profileList where audiopath equals to UPDATED_AUDIOPATH
        defaultProfileShouldNotBeFound("audiopath.in=" + UPDATED_AUDIOPATH);
    }

    @Test
    @Transactional
    public void getAllProfilesByAudiopathIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where audiopath is not null
        defaultProfileShouldBeFound("audiopath.specified=true");

        // Get all the profileList where audiopath is null
        defaultProfileShouldNotBeFound("audiopath.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        profile.setUser(user);
        profileRepository.saveAndFlush(profile);
        Long userId = user.getId();

        // Get all the profileList where user equals to userId
        defaultProfileShouldBeFound("userId.equals=" + userId);

        // Get all the profileList where user equals to userId + 1
        defaultProfileShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        Role role = RoleResourceIntTest.createEntity(em);
        em.persist(role);
        em.flush();
        profile.addRole(role);
        profileRepository.saveAndFlush(profile);
        Long roleId = role.getId();

        // Get all the profileList where role equals to roleId
        defaultProfileShouldBeFound("roleId.equals=" + roleId);

        // Get all the profileList where role equals to roleId + 1
        defaultProfileShouldNotBeFound("roleId.equals=" + (roleId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByFilmIsEqualToSomething() throws Exception {
        // Initialize the database
        Film film = FilmResourceIntTest.createEntity(em);
        em.persist(film);
        em.flush();
        profile.addFilm(film);
        profileRepository.saveAndFlush(profile);
        Long filmId = film.getId();

        // Get all the profileList where film equals to filmId
        defaultProfileShouldBeFound("filmId.equals=" + filmId);

        // Get all the profileList where film equals to filmId + 1
        defaultProfileShouldNotBeFound("filmId.equals=" + (filmId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByTelevisionIsEqualToSomething() throws Exception {
        // Initialize the database
        Television television = TelevisionResourceIntTest.createEntity(em);
        em.persist(television);
        em.flush();
        profile.addTelevision(television);
        profileRepository.saveAndFlush(profile);
        Long televisionId = television.getId();

        // Get all the profileList where television equals to televisionId
        defaultProfileShouldBeFound("televisionId.equals=" + televisionId);

        // Get all the profileList where television equals to televisionId + 1
        defaultProfileShouldNotBeFound("televisionId.equals=" + (televisionId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByInternetIsEqualToSomething() throws Exception {
        // Initialize the database
        Internet internet = InternetResourceIntTest.createEntity(em);
        em.persist(internet);
        em.flush();
        profile.addInternet(internet);
        profileRepository.saveAndFlush(profile);
        Long internetId = internet.getId();

        // Get all the profileList where internet equals to internetId
        defaultProfileShouldBeFound("internetId.equals=" + internetId);

        // Get all the profileList where internet equals to internetId + 1
        defaultProfileShouldNotBeFound("internetId.equals=" + (internetId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByCommercialIsEqualToSomething() throws Exception {
        // Initialize the database
        Commercial commercial = CommercialResourceIntTest.createEntity(em);
        em.persist(commercial);
        em.flush();
        profile.addCommercial(commercial);
        profileRepository.saveAndFlush(profile);
        Long commercialId = commercial.getId();

        // Get all the profileList where commercial equals to commercialId
        defaultProfileShouldBeFound("commercialId.equals=" + commercialId);

        // Get all the profileList where commercial equals to commercialId + 1
        defaultProfileShouldNotBeFound("commercialId.equals=" + (commercialId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByPrintIsEqualToSomething() throws Exception {
        // Initialize the database
        Print print = PrintResourceIntTest.createEntity(em);
        em.persist(print);
        em.flush();
        profile.addPrint(print);
        profileRepository.saveAndFlush(profile);
        Long printId = print.getId();

        // Get all the profileList where print equals to printId
        defaultProfileShouldBeFound("printId.equals=" + printId);

        // Get all the profileList where print equals to printId + 1
        defaultProfileShouldNotBeFound("printId.equals=" + (printId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByTheaterIsEqualToSomething() throws Exception {
        // Initialize the database
        Theater theater = TheaterResourceIntTest.createEntity(em);
        em.persist(theater);
        em.flush();
        profile.addTheater(theater);
        profileRepository.saveAndFlush(profile);
        Long theaterId = theater.getId();

        // Get all the profileList where theater equals to theaterId
        defaultProfileShouldBeFound("theaterId.equals=" + theaterId);

        // Get all the profileList where theater equals to theaterId + 1
        defaultProfileShouldNotBeFound("theaterId.equals=" + (theaterId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByVoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        Voice voice = VoiceResourceIntTest.createEntity(em);
        em.persist(voice);
        em.flush();
        profile.addVoice(voice);
        profileRepository.saveAndFlush(profile);
        Long voiceId = voice.getId();

        // Get all the profileList where voice equals to voiceId
        defaultProfileShouldBeFound("voiceId.equals=" + voiceId);

        // Get all the profileList where voice equals to voiceId + 1
        defaultProfileShouldNotBeFound("voiceId.equals=" + (voiceId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesBySkillIsEqualToSomething() throws Exception {
        // Initialize the database
        Skill skill = SkillResourceIntTest.createEntity(em);
        em.persist(skill);
        em.flush();
        profile.addSkill(skill);
        profileRepository.saveAndFlush(profile);
        Long skillId = skill.getId();

        // Get all the profileList where skill equals to skillId
        defaultProfileShouldBeFound("skillId.equals=" + skillId);

        // Get all the profileList where skill equals to skillId + 1
        defaultProfileShouldNotBeFound("skillId.equals=" + (skillId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesBySportIsEqualToSomething() throws Exception {
        // Initialize the database
        Sport sport = SportResourceIntTest.createEntity(em);
        em.persist(sport);
        em.flush();
        profile.addSport(sport);
        profileRepository.saveAndFlush(profile);
        Long sportId = sport.getId();

        // Get all the profileList where sport equals to sportId
        defaultProfileShouldBeFound("sportId.equals=" + sportId);

        // Get all the profileList where sport equals to sportId + 1
        defaultProfileShouldNotBeFound("sportId.equals=" + (sportId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesBySwimmingIsEqualToSomething() throws Exception {
        // Initialize the database
        Swimming swimming = SwimmingResourceIntTest.createEntity(em);
        em.persist(swimming);
        em.flush();
        profile.addSwimming(swimming);
        profileRepository.saveAndFlush(profile);
        Long swimmingId = swimming.getId();

        // Get all the profileList where swimming equals to swimmingId
        defaultProfileShouldBeFound("swimmingId.equals=" + swimmingId);

        // Get all the profileList where swimming equals to swimmingId + 1
        defaultProfileShouldNotBeFound("swimmingId.equals=" + (swimmingId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByCombatIsEqualToSomething() throws Exception {
        // Initialize the database
        Combat combat = CombatResourceIntTest.createEntity(em);
        em.persist(combat);
        em.flush();
        profile.addCombat(combat);
        profileRepository.saveAndFlush(profile);
        Long combatId = combat.getId();

        // Get all the profileList where combat equals to combatId
        defaultProfileShouldBeFound("combatId.equals=" + combatId);

        // Get all the profileList where combat equals to combatId + 1
        defaultProfileShouldNotBeFound("combatId.equals=" + (combatId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        Language language = LanguageResourceIntTest.createEntity(em);
        em.persist(language);
        em.flush();
        profile.addLanguage(language);
        profileRepository.saveAndFlush(profile);
        Long languageId = language.getId();

        // Get all the profileList where language equals to languageId
        defaultProfileShouldBeFound("languageId.equals=" + languageId);

        // Get all the profileList where language equals to languageId + 1
        defaultProfileShouldNotBeFound("languageId.equals=" + (languageId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByInstrumentIsEqualToSomething() throws Exception {
        // Initialize the database
        Instrument instrument = InstrumentResourceIntTest.createEntity(em);
        em.persist(instrument);
        em.flush();
        profile.addInstrument(instrument);
        profileRepository.saveAndFlush(profile);
        Long instrumentId = instrument.getId();

        // Get all the profileList where instrument equals to instrumentId
        defaultProfileShouldBeFound("instrumentId.equals=" + instrumentId);

        // Get all the profileList where instrument equals to instrumentId + 1
        defaultProfileShouldNotBeFound("instrumentId.equals=" + (instrumentId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByWeaponIsEqualToSomething() throws Exception {
        // Initialize the database
        Weapon weapon = WeaponResourceIntTest.createEntity(em);
        em.persist(weapon);
        em.flush();
        profile.addWeapon(weapon);
        profileRepository.saveAndFlush(profile);
        Long weaponId = weapon.getId();

        // Get all the profileList where weapon equals to weaponId
        defaultProfileShouldBeFound("weaponId.equals=" + weaponId);

        // Get all the profileList where weapon equals to weaponId + 1
        defaultProfileShouldNotBeFound("weaponId.equals=" + (weaponId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByCyclingIsEqualToSomething() throws Exception {
        // Initialize the database
        Cycling cycling = CyclingResourceIntTest.createEntity(em);
        em.persist(cycling);
        em.flush();
        profile.addCycling(cycling);
        profileRepository.saveAndFlush(profile);
        Long cyclingId = cycling.getId();

        // Get all the profileList where cycling equals to cyclingId
        defaultProfileShouldBeFound("cyclingId.equals=" + cyclingId);

        // Get all the profileList where cycling equals to cyclingId + 1
        defaultProfileShouldNotBeFound("cyclingId.equals=" + (cyclingId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByCircusIsEqualToSomething() throws Exception {
        // Initialize the database
        Circus circus = CircusResourceIntTest.createEntity(em);
        em.persist(circus);
        em.flush();
        profile.addCircus(circus);
        profileRepository.saveAndFlush(profile);
        Long circusId = circus.getId();

        // Get all the profileList where circus equals to circusId
        defaultProfileShouldBeFound("circusId.equals=" + circusId);

        // Get all the profileList where circus equals to circusId + 1
        defaultProfileShouldNotBeFound("circusId.equals=" + (circusId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByHorseIsEqualToSomething() throws Exception {
        // Initialize the database
        Horse horse = HorseResourceIntTest.createEntity(em);
        em.persist(horse);
        em.flush();
        profile.addHorse(horse);
        profileRepository.saveAndFlush(profile);
        Long horseId = horse.getId();

        // Get all the profileList where horse equals to horseId
        defaultProfileShouldBeFound("horseId.equals=" + horseId);

        // Get all the profileList where horse equals to horseId + 1
        defaultProfileShouldNotBeFound("horseId.equals=" + (horseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProfileShouldBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].since").value(hasItem(DEFAULT_SINCE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].videoContentType").value(hasItem(DEFAULT_VIDEO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].video").value(hasItem(Base64Utils.encodeToString(DEFAULT_VIDEO))))
            .andExpect(jsonPath("$.[*].audioContentType").value(hasItem(DEFAULT_AUDIO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].audio").value(hasItem(Base64Utils.encodeToString(DEFAULT_AUDIO))))
            .andExpect(jsonPath("$.[*].imagepath").value(hasItem(DEFAULT_IMAGEPATH.toString())))
            .andExpect(jsonPath("$.[*].videopath").value(hasItem(DEFAULT_VIDEOPATH.toString())))
            .andExpect(jsonPath("$.[*].audiopath").value(hasItem(DEFAULT_AUDIOPATH.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProfileShouldNotBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingProfile() throws Exception {
        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Update the profile
        Profile updatedProfile = profileRepository.findById(profile.getId()).get();
        // Disconnect from session so that the updates on updatedProfile are not directly saved in db
        em.detach(updatedProfile);
        updatedProfile
            .state(UPDATED_STATE)
            .city(UPDATED_CITY)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .since(UPDATED_SINCE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .video(UPDATED_VIDEO)
            .videoContentType(UPDATED_VIDEO_CONTENT_TYPE)
            .audio(UPDATED_AUDIO)
            .audioContentType(UPDATED_AUDIO_CONTENT_TYPE)
            .imagepath(UPDATED_IMAGEPATH)
            .videopath(UPDATED_VIDEOPATH)
            .audiopath(UPDATED_AUDIOPATH);
        ProfileDTO profileDTO = profileMapper.toDto(updatedProfile);

        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isOk());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testProfile.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testProfile.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testProfile.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testProfile.getSince()).isEqualTo(UPDATED_SINCE);
        assertThat(testProfile.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProfile.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testProfile.getVideo()).isEqualTo(UPDATED_VIDEO);
        assertThat(testProfile.getVideoContentType()).isEqualTo(UPDATED_VIDEO_CONTENT_TYPE);
        assertThat(testProfile.getAudio()).isEqualTo(UPDATED_AUDIO);
        assertThat(testProfile.getAudioContentType()).isEqualTo(UPDATED_AUDIO_CONTENT_TYPE);
        assertThat(testProfile.getImagepath()).isEqualTo(UPDATED_IMAGEPATH);
        assertThat(testProfile.getVideopath()).isEqualTo(UPDATED_VIDEOPATH);
        assertThat(testProfile.getAudiopath()).isEqualTo(UPDATED_AUDIOPATH);
    }

    @Test
    @Transactional
    public void updateNonExistingProfile() throws Exception {
        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeDelete = profileRepository.findAll().size();

        // Get the profile
        restProfileMockMvc.perform(delete("/api/profiles/{id}", profile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profile.class);
        Profile profile1 = new Profile();
        profile1.setId(1L);
        Profile profile2 = new Profile();
        profile2.setId(profile1.getId());
        assertThat(profile1).isEqualTo(profile2);
        profile2.setId(2L);
        assertThat(profile1).isNotEqualTo(profile2);
        profile1.setId(null);
        assertThat(profile1).isNotEqualTo(profile2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileDTO.class);
        ProfileDTO profileDTO1 = new ProfileDTO();
        profileDTO1.setId(1L);
        ProfileDTO profileDTO2 = new ProfileDTO();
        assertThat(profileDTO1).isNotEqualTo(profileDTO2);
        profileDTO2.setId(profileDTO1.getId());
        assertThat(profileDTO1).isEqualTo(profileDTO2);
        profileDTO2.setId(2L);
        assertThat(profileDTO1).isNotEqualTo(profileDTO2);
        profileDTO1.setId(null);
        assertThat(profileDTO1).isNotEqualTo(profileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(profileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(profileMapper.fromId(null)).isNull();
    }
}
