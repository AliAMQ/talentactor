package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Role;
import com.talentactor.domain.Profile;
import com.talentactor.domain.Project;
import com.talentactor.repository.RoleRepository;
import com.talentactor.service.RoleService;
import com.talentactor.service.dto.RoleDTO;
import com.talentactor.service.mapper.RoleMapper;
import com.talentactor.web.rest.errors.ExceptionTranslator;
import com.talentactor.service.dto.RoleCriteria;
import com.talentactor.service.RoleQueryService;

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
 * Test class for the RoleResource REST controller.
 *
 * @see RoleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentactorApp.class)
public class RoleResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private RoleMapper roleMapper;
    

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleQueryService roleQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRoleMockMvc;

    private Role role;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoleResource roleResource = new RoleResource(roleService, roleQueryService);
        this.restRoleMockMvc = MockMvcBuilders.standaloneSetup(roleResource)
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
    public static Role createEntity(EntityManager em) {
        Role role = new Role()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION);
        return role;
    }

    @Before
    public void initTest() {
        role = createEntity(em);
    }

    @Test
    @Transactional
    public void createRole() throws Exception {
        int databaseSizeBeforeCreate = roleRepository.findAll().size();

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);
        restRoleMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleDTO)))
            .andExpect(status().isCreated());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate + 1);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRole.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createRoleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roleRepository.findAll().size();

        // Create the Role with an existing ID
        role.setId(1L);
        RoleDTO roleDTO = roleMapper.toDto(role);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = roleRepository.findAll().size();
        // set the field null
        role.setTitle(null);

        // Create the Role, which fails.
        RoleDTO roleDTO = roleMapper.toDto(role);

        restRoleMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleDTO)))
            .andExpect(status().isBadRequest());

        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRoles() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList
        restRoleMockMvc.perform(get("/api/roles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(role.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    

    @Test
    @Transactional
    public void getRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get the role
        restRoleMockMvc.perform(get("/api/roles/{id}", role.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(role.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllRolesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where title equals to DEFAULT_TITLE
        defaultRoleShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the roleList where title equals to UPDATED_TITLE
        defaultRoleShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllRolesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultRoleShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the roleList where title equals to UPDATED_TITLE
        defaultRoleShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllRolesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where title is not null
        defaultRoleShouldBeFound("title.specified=true");

        // Get all the roleList where title is null
        defaultRoleShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllRolesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where description equals to DEFAULT_DESCRIPTION
        defaultRoleShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the roleList where description equals to UPDATED_DESCRIPTION
        defaultRoleShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllRolesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRoleShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the roleList where description equals to UPDATED_DESCRIPTION
        defaultRoleShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllRolesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList where description is not null
        defaultRoleShouldBeFound("description.specified=true");

        // Get all the roleList where description is null
        defaultRoleShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllRolesByProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile profile = ProfileResourceIntTest.createEntity(em);
        em.persist(profile);
        em.flush();
        role.setProfile(profile);
        roleRepository.saveAndFlush(role);
        Long profileId = profile.getId();

        // Get all the roleList where profile equals to profileId
        defaultRoleShouldBeFound("profileId.equals=" + profileId);

        // Get all the roleList where profile equals to profileId + 1
        defaultRoleShouldNotBeFound("profileId.equals=" + (profileId + 1));
    }


    @Test
    @Transactional
    public void getAllRolesByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        role.setProject(project);
        roleRepository.saveAndFlush(role);
        Long projectId = project.getId();

        // Get all the roleList where project equals to projectId
        defaultRoleShouldBeFound("projectId.equals=" + projectId);

        // Get all the roleList where project equals to projectId + 1
        defaultRoleShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRoleShouldBeFound(String filter) throws Exception {
        restRoleMockMvc.perform(get("/api/roles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(role.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRoleShouldNotBeFound(String filter) throws Exception {
        restRoleMockMvc.perform(get("/api/roles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingRole() throws Exception {
        // Get the role
        restRoleMockMvc.perform(get("/api/roles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Update the role
        Role updatedRole = roleRepository.findById(role.getId()).get();
        // Disconnect from session so that the updates on updatedRole are not directly saved in db
        em.detach(updatedRole);
        updatedRole
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION);
        RoleDTO roleDTO = roleMapper.toDto(updatedRole);

        restRoleMockMvc.perform(put("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleDTO)))
            .andExpect(status().isOk());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRole.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRoleMockMvc.perform(put("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        int databaseSizeBeforeDelete = roleRepository.findAll().size();

        // Get the role
        restRoleMockMvc.perform(delete("/api/roles/{id}", role.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Role.class);
        Role role1 = new Role();
        role1.setId(1L);
        Role role2 = new Role();
        role2.setId(role1.getId());
        assertThat(role1).isEqualTo(role2);
        role2.setId(2L);
        assertThat(role1).isNotEqualTo(role2);
        role1.setId(null);
        assertThat(role1).isNotEqualTo(role2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleDTO.class);
        RoleDTO roleDTO1 = new RoleDTO();
        roleDTO1.setId(1L);
        RoleDTO roleDTO2 = new RoleDTO();
        assertThat(roleDTO1).isNotEqualTo(roleDTO2);
        roleDTO2.setId(roleDTO1.getId());
        assertThat(roleDTO1).isEqualTo(roleDTO2);
        roleDTO2.setId(2L);
        assertThat(roleDTO1).isNotEqualTo(roleDTO2);
        roleDTO1.setId(null);
        assertThat(roleDTO1).isNotEqualTo(roleDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(roleMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(roleMapper.fromId(null)).isNull();
    }
}
