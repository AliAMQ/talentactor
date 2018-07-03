package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Combat;
import com.talentactor.repository.CombatRepository;
import com.talentactor.service.CombatService;
import com.talentactor.service.dto.CombatDTO;
import com.talentactor.service.mapper.CombatMapper;
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

import javax.persistence.EntityManager;
import java.util.List;


import static com.talentactor.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CombatResource REST controller.
 *
 * @see CombatResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentactorApp.class)
public class CombatResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private CombatRepository combatRepository;


    @Autowired
    private CombatMapper combatMapper;
    

    @Autowired
    private CombatService combatService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCombatMockMvc;

    private Combat combat;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CombatResource combatResource = new CombatResource(combatService);
        this.restCombatMockMvc = MockMvcBuilders.standaloneSetup(combatResource)
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
    public static Combat createEntity(EntityManager em) {
        Combat combat = new Combat()
            .title(DEFAULT_TITLE);
        return combat;
    }

    @Before
    public void initTest() {
        combat = createEntity(em);
    }

    @Test
    @Transactional
    public void createCombat() throws Exception {
        int databaseSizeBeforeCreate = combatRepository.findAll().size();

        // Create the Combat
        CombatDTO combatDTO = combatMapper.toDto(combat);
        restCombatMockMvc.perform(post("/api/combats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(combatDTO)))
            .andExpect(status().isCreated());

        // Validate the Combat in the database
        List<Combat> combatList = combatRepository.findAll();
        assertThat(combatList).hasSize(databaseSizeBeforeCreate + 1);
        Combat testCombat = combatList.get(combatList.size() - 1);
        assertThat(testCombat.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createCombatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = combatRepository.findAll().size();

        // Create the Combat with an existing ID
        combat.setId(1L);
        CombatDTO combatDTO = combatMapper.toDto(combat);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCombatMockMvc.perform(post("/api/combats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(combatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Combat in the database
        List<Combat> combatList = combatRepository.findAll();
        assertThat(combatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = combatRepository.findAll().size();
        // set the field null
        combat.setTitle(null);

        // Create the Combat, which fails.
        CombatDTO combatDTO = combatMapper.toDto(combat);

        restCombatMockMvc.perform(post("/api/combats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(combatDTO)))
            .andExpect(status().isBadRequest());

        List<Combat> combatList = combatRepository.findAll();
        assertThat(combatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCombats() throws Exception {
        // Initialize the database
        combatRepository.saveAndFlush(combat);

        // Get all the combatList
        restCombatMockMvc.perform(get("/api/combats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(combat.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }
    

    @Test
    @Transactional
    public void getCombat() throws Exception {
        // Initialize the database
        combatRepository.saveAndFlush(combat);

        // Get the combat
        restCombatMockMvc.perform(get("/api/combats/{id}", combat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(combat.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCombat() throws Exception {
        // Get the combat
        restCombatMockMvc.perform(get("/api/combats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCombat() throws Exception {
        // Initialize the database
        combatRepository.saveAndFlush(combat);

        int databaseSizeBeforeUpdate = combatRepository.findAll().size();

        // Update the combat
        Combat updatedCombat = combatRepository.findById(combat.getId()).get();
        // Disconnect from session so that the updates on updatedCombat are not directly saved in db
        em.detach(updatedCombat);
        updatedCombat
            .title(UPDATED_TITLE);
        CombatDTO combatDTO = combatMapper.toDto(updatedCombat);

        restCombatMockMvc.perform(put("/api/combats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(combatDTO)))
            .andExpect(status().isOk());

        // Validate the Combat in the database
        List<Combat> combatList = combatRepository.findAll();
        assertThat(combatList).hasSize(databaseSizeBeforeUpdate);
        Combat testCombat = combatList.get(combatList.size() - 1);
        assertThat(testCombat.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingCombat() throws Exception {
        int databaseSizeBeforeUpdate = combatRepository.findAll().size();

        // Create the Combat
        CombatDTO combatDTO = combatMapper.toDto(combat);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCombatMockMvc.perform(put("/api/combats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(combatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Combat in the database
        List<Combat> combatList = combatRepository.findAll();
        assertThat(combatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCombat() throws Exception {
        // Initialize the database
        combatRepository.saveAndFlush(combat);

        int databaseSizeBeforeDelete = combatRepository.findAll().size();

        // Get the combat
        restCombatMockMvc.perform(delete("/api/combats/{id}", combat.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Combat> combatList = combatRepository.findAll();
        assertThat(combatList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Combat.class);
        Combat combat1 = new Combat();
        combat1.setId(1L);
        Combat combat2 = new Combat();
        combat2.setId(combat1.getId());
        assertThat(combat1).isEqualTo(combat2);
        combat2.setId(2L);
        assertThat(combat1).isNotEqualTo(combat2);
        combat1.setId(null);
        assertThat(combat1).isNotEqualTo(combat2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CombatDTO.class);
        CombatDTO combatDTO1 = new CombatDTO();
        combatDTO1.setId(1L);
        CombatDTO combatDTO2 = new CombatDTO();
        assertThat(combatDTO1).isNotEqualTo(combatDTO2);
        combatDTO2.setId(combatDTO1.getId());
        assertThat(combatDTO1).isEqualTo(combatDTO2);
        combatDTO2.setId(2L);
        assertThat(combatDTO1).isNotEqualTo(combatDTO2);
        combatDTO1.setId(null);
        assertThat(combatDTO1).isNotEqualTo(combatDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(combatMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(combatMapper.fromId(null)).isNull();
    }
}
