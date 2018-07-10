package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Instrument;
import com.talentactor.domain.Profile;
import com.talentactor.repository.InstrumentRepository;
import com.talentactor.service.InstrumentService;
import com.talentactor.service.dto.InstrumentDTO;
import com.talentactor.service.mapper.InstrumentMapper;
import com.talentactor.web.rest.errors.ExceptionTranslator;
import com.talentactor.service.dto.InstrumentCriteria;
import com.talentactor.service.InstrumentQueryService;

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
 * Test class for the InstrumentResource REST controller.
 *
 * @see InstrumentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentactorApp.class)
public class InstrumentResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private InstrumentRepository instrumentRepository;


    @Autowired
    private InstrumentMapper instrumentMapper;
    

    @Autowired
    private InstrumentService instrumentService;

    @Autowired
    private InstrumentQueryService instrumentQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInstrumentMockMvc;

    private Instrument instrument;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InstrumentResource instrumentResource = new InstrumentResource(instrumentService, instrumentQueryService);
        this.restInstrumentMockMvc = MockMvcBuilders.standaloneSetup(instrumentResource)
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
    public static Instrument createEntity(EntityManager em) {
        Instrument instrument = new Instrument()
            .title(DEFAULT_TITLE);
        return instrument;
    }

    @Before
    public void initTest() {
        instrument = createEntity(em);
    }

    @Test
    @Transactional
    public void createInstrument() throws Exception {
        int databaseSizeBeforeCreate = instrumentRepository.findAll().size();

        // Create the Instrument
        InstrumentDTO instrumentDTO = instrumentMapper.toDto(instrument);
        restInstrumentMockMvc.perform(post("/api/instruments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instrumentDTO)))
            .andExpect(status().isCreated());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeCreate + 1);
        Instrument testInstrument = instrumentList.get(instrumentList.size() - 1);
        assertThat(testInstrument.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createInstrumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = instrumentRepository.findAll().size();

        // Create the Instrument with an existing ID
        instrument.setId(1L);
        InstrumentDTO instrumentDTO = instrumentMapper.toDto(instrument);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstrumentMockMvc.perform(post("/api/instruments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instrumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = instrumentRepository.findAll().size();
        // set the field null
        instrument.setTitle(null);

        // Create the Instrument, which fails.
        InstrumentDTO instrumentDTO = instrumentMapper.toDto(instrument);

        restInstrumentMockMvc.perform(post("/api/instruments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instrumentDTO)))
            .andExpect(status().isBadRequest());

        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstruments() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        // Get all the instrumentList
        restInstrumentMockMvc.perform(get("/api/instruments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instrument.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }
    

    @Test
    @Transactional
    public void getInstrument() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        // Get the instrument
        restInstrumentMockMvc.perform(get("/api/instruments/{id}", instrument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(instrument.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getAllInstrumentsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        // Get all the instrumentList where title equals to DEFAULT_TITLE
        defaultInstrumentShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the instrumentList where title equals to UPDATED_TITLE
        defaultInstrumentShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllInstrumentsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        // Get all the instrumentList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultInstrumentShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the instrumentList where title equals to UPDATED_TITLE
        defaultInstrumentShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllInstrumentsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        // Get all the instrumentList where title is not null
        defaultInstrumentShouldBeFound("title.specified=true");

        // Get all the instrumentList where title is null
        defaultInstrumentShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllInstrumentsByProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile profile = ProfileResourceIntTest.createEntity(em);
        em.persist(profile);
        em.flush();
        instrument.addProfile(profile);
        instrumentRepository.saveAndFlush(instrument);
        Long profileId = profile.getId();

        // Get all the instrumentList where profile equals to profileId
        defaultInstrumentShouldBeFound("profileId.equals=" + profileId);

        // Get all the instrumentList where profile equals to profileId + 1
        defaultInstrumentShouldNotBeFound("profileId.equals=" + (profileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInstrumentShouldBeFound(String filter) throws Exception {
        restInstrumentMockMvc.perform(get("/api/instruments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instrument.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInstrumentShouldNotBeFound(String filter) throws Exception {
        restInstrumentMockMvc.perform(get("/api/instruments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingInstrument() throws Exception {
        // Get the instrument
        restInstrumentMockMvc.perform(get("/api/instruments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstrument() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        int databaseSizeBeforeUpdate = instrumentRepository.findAll().size();

        // Update the instrument
        Instrument updatedInstrument = instrumentRepository.findById(instrument.getId()).get();
        // Disconnect from session so that the updates on updatedInstrument are not directly saved in db
        em.detach(updatedInstrument);
        updatedInstrument
            .title(UPDATED_TITLE);
        InstrumentDTO instrumentDTO = instrumentMapper.toDto(updatedInstrument);

        restInstrumentMockMvc.perform(put("/api/instruments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instrumentDTO)))
            .andExpect(status().isOk());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
        Instrument testInstrument = instrumentList.get(instrumentList.size() - 1);
        assertThat(testInstrument.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingInstrument() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRepository.findAll().size();

        // Create the Instrument
        InstrumentDTO instrumentDTO = instrumentMapper.toDto(instrument);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInstrumentMockMvc.perform(put("/api/instruments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instrumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInstrument() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        int databaseSizeBeforeDelete = instrumentRepository.findAll().size();

        // Get the instrument
        restInstrumentMockMvc.perform(delete("/api/instruments/{id}", instrument.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Instrument.class);
        Instrument instrument1 = new Instrument();
        instrument1.setId(1L);
        Instrument instrument2 = new Instrument();
        instrument2.setId(instrument1.getId());
        assertThat(instrument1).isEqualTo(instrument2);
        instrument2.setId(2L);
        assertThat(instrument1).isNotEqualTo(instrument2);
        instrument1.setId(null);
        assertThat(instrument1).isNotEqualTo(instrument2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InstrumentDTO.class);
        InstrumentDTO instrumentDTO1 = new InstrumentDTO();
        instrumentDTO1.setId(1L);
        InstrumentDTO instrumentDTO2 = new InstrumentDTO();
        assertThat(instrumentDTO1).isNotEqualTo(instrumentDTO2);
        instrumentDTO2.setId(instrumentDTO1.getId());
        assertThat(instrumentDTO1).isEqualTo(instrumentDTO2);
        instrumentDTO2.setId(2L);
        assertThat(instrumentDTO1).isNotEqualTo(instrumentDTO2);
        instrumentDTO1.setId(null);
        assertThat(instrumentDTO1).isNotEqualTo(instrumentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(instrumentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(instrumentMapper.fromId(null)).isNull();
    }
}
