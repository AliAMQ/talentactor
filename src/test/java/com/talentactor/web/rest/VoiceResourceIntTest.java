package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Voice;
import com.talentactor.domain.Profile;
import com.talentactor.repository.VoiceRepository;
import com.talentactor.service.VoiceService;
import com.talentactor.service.dto.VoiceDTO;
import com.talentactor.service.mapper.VoiceMapper;
import com.talentactor.web.rest.errors.ExceptionTranslator;
import com.talentactor.service.dto.VoiceCriteria;
import com.talentactor.service.VoiceQueryService;

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
 * Test class for the VoiceResource REST controller.
 *
 * @see VoiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentactorApp.class)
public class VoiceResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTOR = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final byte[] DEFAULT_VIDEO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_VIDEO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_VIDEO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_VIDEO_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_AUDIO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_AUDIO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_AUDIO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_AUDIO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_VIDEOPATH = "AAAAAAAAAA";
    private static final String UPDATED_VIDEOPATH = "BBBBBBBBBB";

    private static final String DEFAULT_AUDIOPATH = "AAAAAAAAAA";
    private static final String UPDATED_AUDIOPATH = "BBBBBBBBBB";

    @Autowired
    private VoiceRepository voiceRepository;


    @Autowired
    private VoiceMapper voiceMapper;
    

    @Autowired
    private VoiceService voiceService;

    @Autowired
    private VoiceQueryService voiceQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVoiceMockMvc;

    private Voice voice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VoiceResource voiceResource = new VoiceResource(voiceService, voiceQueryService);
        this.restVoiceMockMvc = MockMvcBuilders.standaloneSetup(voiceResource)
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
    public static Voice createEntity(EntityManager em) {
        Voice voice = new Voice()
            .title(DEFAULT_TITLE)
            .director(DEFAULT_DIRECTOR)
            .link(DEFAULT_LINK)
            .video(DEFAULT_VIDEO)
            .videoContentType(DEFAULT_VIDEO_CONTENT_TYPE)
            .audio(DEFAULT_AUDIO)
            .audioContentType(DEFAULT_AUDIO_CONTENT_TYPE)
            .videopath(DEFAULT_VIDEOPATH)
            .audiopath(DEFAULT_AUDIOPATH);
        return voice;
    }

    @Before
    public void initTest() {
        voice = createEntity(em);
    }

    @Test
    @Transactional
    public void createVoice() throws Exception {
        int databaseSizeBeforeCreate = voiceRepository.findAll().size();

        // Create the Voice
        VoiceDTO voiceDTO = voiceMapper.toDto(voice);
        restVoiceMockMvc.perform(post("/api/voices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voiceDTO)))
            .andExpect(status().isCreated());

        // Validate the Voice in the database
        List<Voice> voiceList = voiceRepository.findAll();
        assertThat(voiceList).hasSize(databaseSizeBeforeCreate + 1);
        Voice testVoice = voiceList.get(voiceList.size() - 1);
        assertThat(testVoice.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testVoice.getDirector()).isEqualTo(DEFAULT_DIRECTOR);
        assertThat(testVoice.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testVoice.getVideo()).isEqualTo(DEFAULT_VIDEO);
        assertThat(testVoice.getVideoContentType()).isEqualTo(DEFAULT_VIDEO_CONTENT_TYPE);
        assertThat(testVoice.getAudio()).isEqualTo(DEFAULT_AUDIO);
        assertThat(testVoice.getAudioContentType()).isEqualTo(DEFAULT_AUDIO_CONTENT_TYPE);
        assertThat(testVoice.getVideopath()).isEqualTo(DEFAULT_VIDEOPATH);
        assertThat(testVoice.getAudiopath()).isEqualTo(DEFAULT_AUDIOPATH);
    }

    @Test
    @Transactional
    public void createVoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = voiceRepository.findAll().size();

        // Create the Voice with an existing ID
        voice.setId(1L);
        VoiceDTO voiceDTO = voiceMapper.toDto(voice);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoiceMockMvc.perform(post("/api/voices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Voice in the database
        List<Voice> voiceList = voiceRepository.findAll();
        assertThat(voiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = voiceRepository.findAll().size();
        // set the field null
        voice.setTitle(null);

        // Create the Voice, which fails.
        VoiceDTO voiceDTO = voiceMapper.toDto(voice);

        restVoiceMockMvc.perform(post("/api/voices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voiceDTO)))
            .andExpect(status().isBadRequest());

        List<Voice> voiceList = voiceRepository.findAll();
        assertThat(voiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVoices() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        // Get all the voiceList
        restVoiceMockMvc.perform(get("/api/voices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voice.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].director").value(hasItem(DEFAULT_DIRECTOR.toString())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())))
            .andExpect(jsonPath("$.[*].videoContentType").value(hasItem(DEFAULT_VIDEO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].video").value(hasItem(Base64Utils.encodeToString(DEFAULT_VIDEO))))
            .andExpect(jsonPath("$.[*].audioContentType").value(hasItem(DEFAULT_AUDIO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].audio").value(hasItem(Base64Utils.encodeToString(DEFAULT_AUDIO))))
            .andExpect(jsonPath("$.[*].videopath").value(hasItem(DEFAULT_VIDEOPATH.toString())))
            .andExpect(jsonPath("$.[*].audiopath").value(hasItem(DEFAULT_AUDIOPATH.toString())));
    }
    

    @Test
    @Transactional
    public void getVoice() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        // Get the voice
        restVoiceMockMvc.perform(get("/api/voices/{id}", voice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(voice.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.director").value(DEFAULT_DIRECTOR.toString()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()))
            .andExpect(jsonPath("$.videoContentType").value(DEFAULT_VIDEO_CONTENT_TYPE))
            .andExpect(jsonPath("$.video").value(Base64Utils.encodeToString(DEFAULT_VIDEO)))
            .andExpect(jsonPath("$.audioContentType").value(DEFAULT_AUDIO_CONTENT_TYPE))
            .andExpect(jsonPath("$.audio").value(Base64Utils.encodeToString(DEFAULT_AUDIO)))
            .andExpect(jsonPath("$.videopath").value(DEFAULT_VIDEOPATH.toString()))
            .andExpect(jsonPath("$.audiopath").value(DEFAULT_AUDIOPATH.toString()));
    }

    @Test
    @Transactional
    public void getAllVoicesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        // Get all the voiceList where title equals to DEFAULT_TITLE
        defaultVoiceShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the voiceList where title equals to UPDATED_TITLE
        defaultVoiceShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllVoicesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        // Get all the voiceList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultVoiceShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the voiceList where title equals to UPDATED_TITLE
        defaultVoiceShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllVoicesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        // Get all the voiceList where title is not null
        defaultVoiceShouldBeFound("title.specified=true");

        // Get all the voiceList where title is null
        defaultVoiceShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllVoicesByDirectorIsEqualToSomething() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        // Get all the voiceList where director equals to DEFAULT_DIRECTOR
        defaultVoiceShouldBeFound("director.equals=" + DEFAULT_DIRECTOR);

        // Get all the voiceList where director equals to UPDATED_DIRECTOR
        defaultVoiceShouldNotBeFound("director.equals=" + UPDATED_DIRECTOR);
    }

    @Test
    @Transactional
    public void getAllVoicesByDirectorIsInShouldWork() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        // Get all the voiceList where director in DEFAULT_DIRECTOR or UPDATED_DIRECTOR
        defaultVoiceShouldBeFound("director.in=" + DEFAULT_DIRECTOR + "," + UPDATED_DIRECTOR);

        // Get all the voiceList where director equals to UPDATED_DIRECTOR
        defaultVoiceShouldNotBeFound("director.in=" + UPDATED_DIRECTOR);
    }

    @Test
    @Transactional
    public void getAllVoicesByDirectorIsNullOrNotNull() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        // Get all the voiceList where director is not null
        defaultVoiceShouldBeFound("director.specified=true");

        // Get all the voiceList where director is null
        defaultVoiceShouldNotBeFound("director.specified=false");
    }

    @Test
    @Transactional
    public void getAllVoicesByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        // Get all the voiceList where link equals to DEFAULT_LINK
        defaultVoiceShouldBeFound("link.equals=" + DEFAULT_LINK);

        // Get all the voiceList where link equals to UPDATED_LINK
        defaultVoiceShouldNotBeFound("link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllVoicesByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        // Get all the voiceList where link in DEFAULT_LINK or UPDATED_LINK
        defaultVoiceShouldBeFound("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK);

        // Get all the voiceList where link equals to UPDATED_LINK
        defaultVoiceShouldNotBeFound("link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllVoicesByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        // Get all the voiceList where link is not null
        defaultVoiceShouldBeFound("link.specified=true");

        // Get all the voiceList where link is null
        defaultVoiceShouldNotBeFound("link.specified=false");
    }

    @Test
    @Transactional
    public void getAllVoicesByVideopathIsEqualToSomething() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        // Get all the voiceList where videopath equals to DEFAULT_VIDEOPATH
        defaultVoiceShouldBeFound("videopath.equals=" + DEFAULT_VIDEOPATH);

        // Get all the voiceList where videopath equals to UPDATED_VIDEOPATH
        defaultVoiceShouldNotBeFound("videopath.equals=" + UPDATED_VIDEOPATH);
    }

    @Test
    @Transactional
    public void getAllVoicesByVideopathIsInShouldWork() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        // Get all the voiceList where videopath in DEFAULT_VIDEOPATH or UPDATED_VIDEOPATH
        defaultVoiceShouldBeFound("videopath.in=" + DEFAULT_VIDEOPATH + "," + UPDATED_VIDEOPATH);

        // Get all the voiceList where videopath equals to UPDATED_VIDEOPATH
        defaultVoiceShouldNotBeFound("videopath.in=" + UPDATED_VIDEOPATH);
    }

    @Test
    @Transactional
    public void getAllVoicesByVideopathIsNullOrNotNull() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        // Get all the voiceList where videopath is not null
        defaultVoiceShouldBeFound("videopath.specified=true");

        // Get all the voiceList where videopath is null
        defaultVoiceShouldNotBeFound("videopath.specified=false");
    }

    @Test
    @Transactional
    public void getAllVoicesByAudiopathIsEqualToSomething() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        // Get all the voiceList where audiopath equals to DEFAULT_AUDIOPATH
        defaultVoiceShouldBeFound("audiopath.equals=" + DEFAULT_AUDIOPATH);

        // Get all the voiceList where audiopath equals to UPDATED_AUDIOPATH
        defaultVoiceShouldNotBeFound("audiopath.equals=" + UPDATED_AUDIOPATH);
    }

    @Test
    @Transactional
    public void getAllVoicesByAudiopathIsInShouldWork() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        // Get all the voiceList where audiopath in DEFAULT_AUDIOPATH or UPDATED_AUDIOPATH
        defaultVoiceShouldBeFound("audiopath.in=" + DEFAULT_AUDIOPATH + "," + UPDATED_AUDIOPATH);

        // Get all the voiceList where audiopath equals to UPDATED_AUDIOPATH
        defaultVoiceShouldNotBeFound("audiopath.in=" + UPDATED_AUDIOPATH);
    }

    @Test
    @Transactional
    public void getAllVoicesByAudiopathIsNullOrNotNull() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        // Get all the voiceList where audiopath is not null
        defaultVoiceShouldBeFound("audiopath.specified=true");

        // Get all the voiceList where audiopath is null
        defaultVoiceShouldNotBeFound("audiopath.specified=false");
    }

    @Test
    @Transactional
    public void getAllVoicesByProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile profile = ProfileResourceIntTest.createEntity(em);
        em.persist(profile);
        em.flush();
        voice.setProfile(profile);
        voiceRepository.saveAndFlush(voice);
        Long profileId = profile.getId();

        // Get all the voiceList where profile equals to profileId
        defaultVoiceShouldBeFound("profileId.equals=" + profileId);

        // Get all the voiceList where profile equals to profileId + 1
        defaultVoiceShouldNotBeFound("profileId.equals=" + (profileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultVoiceShouldBeFound(String filter) throws Exception {
        restVoiceMockMvc.perform(get("/api/voices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voice.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].director").value(hasItem(DEFAULT_DIRECTOR.toString())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())))
            .andExpect(jsonPath("$.[*].videoContentType").value(hasItem(DEFAULT_VIDEO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].video").value(hasItem(Base64Utils.encodeToString(DEFAULT_VIDEO))))
            .andExpect(jsonPath("$.[*].audioContentType").value(hasItem(DEFAULT_AUDIO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].audio").value(hasItem(Base64Utils.encodeToString(DEFAULT_AUDIO))))
            .andExpect(jsonPath("$.[*].videopath").value(hasItem(DEFAULT_VIDEOPATH.toString())))
            .andExpect(jsonPath("$.[*].audiopath").value(hasItem(DEFAULT_AUDIOPATH.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultVoiceShouldNotBeFound(String filter) throws Exception {
        restVoiceMockMvc.perform(get("/api/voices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingVoice() throws Exception {
        // Get the voice
        restVoiceMockMvc.perform(get("/api/voices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVoice() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        int databaseSizeBeforeUpdate = voiceRepository.findAll().size();

        // Update the voice
        Voice updatedVoice = voiceRepository.findById(voice.getId()).get();
        // Disconnect from session so that the updates on updatedVoice are not directly saved in db
        em.detach(updatedVoice);
        updatedVoice
            .title(UPDATED_TITLE)
            .director(UPDATED_DIRECTOR)
            .link(UPDATED_LINK)
            .video(UPDATED_VIDEO)
            .videoContentType(UPDATED_VIDEO_CONTENT_TYPE)
            .audio(UPDATED_AUDIO)
            .audioContentType(UPDATED_AUDIO_CONTENT_TYPE)
            .videopath(UPDATED_VIDEOPATH)
            .audiopath(UPDATED_AUDIOPATH);
        VoiceDTO voiceDTO = voiceMapper.toDto(updatedVoice);

        restVoiceMockMvc.perform(put("/api/voices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voiceDTO)))
            .andExpect(status().isOk());

        // Validate the Voice in the database
        List<Voice> voiceList = voiceRepository.findAll();
        assertThat(voiceList).hasSize(databaseSizeBeforeUpdate);
        Voice testVoice = voiceList.get(voiceList.size() - 1);
        assertThat(testVoice.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVoice.getDirector()).isEqualTo(UPDATED_DIRECTOR);
        assertThat(testVoice.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testVoice.getVideo()).isEqualTo(UPDATED_VIDEO);
        assertThat(testVoice.getVideoContentType()).isEqualTo(UPDATED_VIDEO_CONTENT_TYPE);
        assertThat(testVoice.getAudio()).isEqualTo(UPDATED_AUDIO);
        assertThat(testVoice.getAudioContentType()).isEqualTo(UPDATED_AUDIO_CONTENT_TYPE);
        assertThat(testVoice.getVideopath()).isEqualTo(UPDATED_VIDEOPATH);
        assertThat(testVoice.getAudiopath()).isEqualTo(UPDATED_AUDIOPATH);
    }

    @Test
    @Transactional
    public void updateNonExistingVoice() throws Exception {
        int databaseSizeBeforeUpdate = voiceRepository.findAll().size();

        // Create the Voice
        VoiceDTO voiceDTO = voiceMapper.toDto(voice);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVoiceMockMvc.perform(put("/api/voices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Voice in the database
        List<Voice> voiceList = voiceRepository.findAll();
        assertThat(voiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVoice() throws Exception {
        // Initialize the database
        voiceRepository.saveAndFlush(voice);

        int databaseSizeBeforeDelete = voiceRepository.findAll().size();

        // Get the voice
        restVoiceMockMvc.perform(delete("/api/voices/{id}", voice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Voice> voiceList = voiceRepository.findAll();
        assertThat(voiceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Voice.class);
        Voice voice1 = new Voice();
        voice1.setId(1L);
        Voice voice2 = new Voice();
        voice2.setId(voice1.getId());
        assertThat(voice1).isEqualTo(voice2);
        voice2.setId(2L);
        assertThat(voice1).isNotEqualTo(voice2);
        voice1.setId(null);
        assertThat(voice1).isNotEqualTo(voice2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VoiceDTO.class);
        VoiceDTO voiceDTO1 = new VoiceDTO();
        voiceDTO1.setId(1L);
        VoiceDTO voiceDTO2 = new VoiceDTO();
        assertThat(voiceDTO1).isNotEqualTo(voiceDTO2);
        voiceDTO2.setId(voiceDTO1.getId());
        assertThat(voiceDTO1).isEqualTo(voiceDTO2);
        voiceDTO2.setId(2L);
        assertThat(voiceDTO1).isNotEqualTo(voiceDTO2);
        voiceDTO1.setId(null);
        assertThat(voiceDTO1).isNotEqualTo(voiceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(voiceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(voiceMapper.fromId(null)).isNull();
    }
}
