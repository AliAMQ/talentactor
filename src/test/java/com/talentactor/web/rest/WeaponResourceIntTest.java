package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Weapon;
import com.talentactor.repository.WeaponRepository;
import com.talentactor.service.WeaponService;
import com.talentactor.service.dto.WeaponDTO;
import com.talentactor.service.mapper.WeaponMapper;
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
 * Test class for the WeaponResource REST controller.
 *
 * @see WeaponResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentactorApp.class)
public class WeaponResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private WeaponRepository weaponRepository;


    @Autowired
    private WeaponMapper weaponMapper;
    

    @Autowired
    private WeaponService weaponService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWeaponMockMvc;

    private Weapon weapon;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WeaponResource weaponResource = new WeaponResource(weaponService);
        this.restWeaponMockMvc = MockMvcBuilders.standaloneSetup(weaponResource)
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
    public static Weapon createEntity(EntityManager em) {
        Weapon weapon = new Weapon()
            .title(DEFAULT_TITLE);
        return weapon;
    }

    @Before
    public void initTest() {
        weapon = createEntity(em);
    }

    @Test
    @Transactional
    public void createWeapon() throws Exception {
        int databaseSizeBeforeCreate = weaponRepository.findAll().size();

        // Create the Weapon
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);
        restWeaponMockMvc.perform(post("/api/weapons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weaponDTO)))
            .andExpect(status().isCreated());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeCreate + 1);
        Weapon testWeapon = weaponList.get(weaponList.size() - 1);
        assertThat(testWeapon.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createWeaponWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = weaponRepository.findAll().size();

        // Create the Weapon with an existing ID
        weapon.setId(1L);
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeaponMockMvc.perform(post("/api/weapons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weaponDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = weaponRepository.findAll().size();
        // set the field null
        weapon.setTitle(null);

        // Create the Weapon, which fails.
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        restWeaponMockMvc.perform(post("/api/weapons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weaponDTO)))
            .andExpect(status().isBadRequest());

        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWeapons() throws Exception {
        // Initialize the database
        weaponRepository.saveAndFlush(weapon);

        // Get all the weaponList
        restWeaponMockMvc.perform(get("/api/weapons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weapon.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }
    

    @Test
    @Transactional
    public void getWeapon() throws Exception {
        // Initialize the database
        weaponRepository.saveAndFlush(weapon);

        // Get the weapon
        restWeaponMockMvc.perform(get("/api/weapons/{id}", weapon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(weapon.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingWeapon() throws Exception {
        // Get the weapon
        restWeaponMockMvc.perform(get("/api/weapons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeapon() throws Exception {
        // Initialize the database
        weaponRepository.saveAndFlush(weapon);

        int databaseSizeBeforeUpdate = weaponRepository.findAll().size();

        // Update the weapon
        Weapon updatedWeapon = weaponRepository.findById(weapon.getId()).get();
        // Disconnect from session so that the updates on updatedWeapon are not directly saved in db
        em.detach(updatedWeapon);
        updatedWeapon
            .title(UPDATED_TITLE);
        WeaponDTO weaponDTO = weaponMapper.toDto(updatedWeapon);

        restWeaponMockMvc.perform(put("/api/weapons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weaponDTO)))
            .andExpect(status().isOk());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
        Weapon testWeapon = weaponList.get(weaponList.size() - 1);
        assertThat(testWeapon.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingWeapon() throws Exception {
        int databaseSizeBeforeUpdate = weaponRepository.findAll().size();

        // Create the Weapon
        WeaponDTO weaponDTO = weaponMapper.toDto(weapon);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWeaponMockMvc.perform(put("/api/weapons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weaponDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Weapon in the database
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWeapon() throws Exception {
        // Initialize the database
        weaponRepository.saveAndFlush(weapon);

        int databaseSizeBeforeDelete = weaponRepository.findAll().size();

        // Get the weapon
        restWeaponMockMvc.perform(delete("/api/weapons/{id}", weapon.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Weapon> weaponList = weaponRepository.findAll();
        assertThat(weaponList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Weapon.class);
        Weapon weapon1 = new Weapon();
        weapon1.setId(1L);
        Weapon weapon2 = new Weapon();
        weapon2.setId(weapon1.getId());
        assertThat(weapon1).isEqualTo(weapon2);
        weapon2.setId(2L);
        assertThat(weapon1).isNotEqualTo(weapon2);
        weapon1.setId(null);
        assertThat(weapon1).isNotEqualTo(weapon2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeaponDTO.class);
        WeaponDTO weaponDTO1 = new WeaponDTO();
        weaponDTO1.setId(1L);
        WeaponDTO weaponDTO2 = new WeaponDTO();
        assertThat(weaponDTO1).isNotEqualTo(weaponDTO2);
        weaponDTO2.setId(weaponDTO1.getId());
        assertThat(weaponDTO1).isEqualTo(weaponDTO2);
        weaponDTO2.setId(2L);
        assertThat(weaponDTO1).isNotEqualTo(weaponDTO2);
        weaponDTO1.setId(null);
        assertThat(weaponDTO1).isNotEqualTo(weaponDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(weaponMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(weaponMapper.fromId(null)).isNull();
    }
}
