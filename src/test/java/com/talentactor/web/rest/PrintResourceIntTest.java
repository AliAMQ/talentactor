package com.talentactor.web.rest;

import com.talentactor.TalentactorApp;

import com.talentactor.domain.Print;
import com.talentactor.repository.PrintRepository;
import com.talentactor.service.PrintService;
import com.talentactor.service.dto.PrintDTO;
import com.talentactor.service.mapper.PrintMapper;
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
 * Test class for the PrintResource REST controller.
 *
 * @see PrintResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalentactorApp.class)
public class PrintResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PHOTOGRAPHER = "AAAAAAAAAA";
    private static final String UPDATED_PHOTOGRAPHER = "BBBBBBBBBB";

    private static final String DEFAULT_HAIR = "AAAAAAAAAA";
    private static final String UPDATED_HAIR = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMAGEPATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGEPATH = "BBBBBBBBBB";

    @Autowired
    private PrintRepository printRepository;


    @Autowired
    private PrintMapper printMapper;
    

    @Autowired
    private PrintService printService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPrintMockMvc;

    private Print print;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PrintResource printResource = new PrintResource(printService);
        this.restPrintMockMvc = MockMvcBuilders.standaloneSetup(printResource)
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
    public static Print createEntity(EntityManager em) {
        Print print = new Print()
            .title(DEFAULT_TITLE)
            .photographer(DEFAULT_PHOTOGRAPHER)
            .hair(DEFAULT_HAIR)
            .link(DEFAULT_LINK)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .imagepath(DEFAULT_IMAGEPATH);
        return print;
    }

    @Before
    public void initTest() {
        print = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrint() throws Exception {
        int databaseSizeBeforeCreate = printRepository.findAll().size();

        // Create the Print
        PrintDTO printDTO = printMapper.toDto(print);
        restPrintMockMvc.perform(post("/api/prints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(printDTO)))
            .andExpect(status().isCreated());

        // Validate the Print in the database
        List<Print> printList = printRepository.findAll();
        assertThat(printList).hasSize(databaseSizeBeforeCreate + 1);
        Print testPrint = printList.get(printList.size() - 1);
        assertThat(testPrint.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPrint.getPhotographer()).isEqualTo(DEFAULT_PHOTOGRAPHER);
        assertThat(testPrint.getHair()).isEqualTo(DEFAULT_HAIR);
        assertThat(testPrint.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testPrint.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testPrint.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testPrint.getImagepath()).isEqualTo(DEFAULT_IMAGEPATH);
    }

    @Test
    @Transactional
    public void createPrintWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = printRepository.findAll().size();

        // Create the Print with an existing ID
        print.setId(1L);
        PrintDTO printDTO = printMapper.toDto(print);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrintMockMvc.perform(post("/api/prints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(printDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Print in the database
        List<Print> printList = printRepository.findAll();
        assertThat(printList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = printRepository.findAll().size();
        // set the field null
        print.setTitle(null);

        // Create the Print, which fails.
        PrintDTO printDTO = printMapper.toDto(print);

        restPrintMockMvc.perform(post("/api/prints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(printDTO)))
            .andExpect(status().isBadRequest());

        List<Print> printList = printRepository.findAll();
        assertThat(printList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrints() throws Exception {
        // Initialize the database
        printRepository.saveAndFlush(print);

        // Get all the printList
        restPrintMockMvc.perform(get("/api/prints?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(print.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].photographer").value(hasItem(DEFAULT_PHOTOGRAPHER.toString())))
            .andExpect(jsonPath("$.[*].hair").value(hasItem(DEFAULT_HAIR.toString())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].imagepath").value(hasItem(DEFAULT_IMAGEPATH.toString())));
    }
    

    @Test
    @Transactional
    public void getPrint() throws Exception {
        // Initialize the database
        printRepository.saveAndFlush(print);

        // Get the print
        restPrintMockMvc.perform(get("/api/prints/{id}", print.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(print.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.photographer").value(DEFAULT_PHOTOGRAPHER.toString()))
            .andExpect(jsonPath("$.hair").value(DEFAULT_HAIR.toString()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.imagepath").value(DEFAULT_IMAGEPATH.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingPrint() throws Exception {
        // Get the print
        restPrintMockMvc.perform(get("/api/prints/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrint() throws Exception {
        // Initialize the database
        printRepository.saveAndFlush(print);

        int databaseSizeBeforeUpdate = printRepository.findAll().size();

        // Update the print
        Print updatedPrint = printRepository.findById(print.getId()).get();
        // Disconnect from session so that the updates on updatedPrint are not directly saved in db
        em.detach(updatedPrint);
        updatedPrint
            .title(UPDATED_TITLE)
            .photographer(UPDATED_PHOTOGRAPHER)
            .hair(UPDATED_HAIR)
            .link(UPDATED_LINK)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .imagepath(UPDATED_IMAGEPATH);
        PrintDTO printDTO = printMapper.toDto(updatedPrint);

        restPrintMockMvc.perform(put("/api/prints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(printDTO)))
            .andExpect(status().isOk());

        // Validate the Print in the database
        List<Print> printList = printRepository.findAll();
        assertThat(printList).hasSize(databaseSizeBeforeUpdate);
        Print testPrint = printList.get(printList.size() - 1);
        assertThat(testPrint.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPrint.getPhotographer()).isEqualTo(UPDATED_PHOTOGRAPHER);
        assertThat(testPrint.getHair()).isEqualTo(UPDATED_HAIR);
        assertThat(testPrint.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testPrint.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPrint.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testPrint.getImagepath()).isEqualTo(UPDATED_IMAGEPATH);
    }

    @Test
    @Transactional
    public void updateNonExistingPrint() throws Exception {
        int databaseSizeBeforeUpdate = printRepository.findAll().size();

        // Create the Print
        PrintDTO printDTO = printMapper.toDto(print);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPrintMockMvc.perform(put("/api/prints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(printDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Print in the database
        List<Print> printList = printRepository.findAll();
        assertThat(printList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePrint() throws Exception {
        // Initialize the database
        printRepository.saveAndFlush(print);

        int databaseSizeBeforeDelete = printRepository.findAll().size();

        // Get the print
        restPrintMockMvc.perform(delete("/api/prints/{id}", print.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Print> printList = printRepository.findAll();
        assertThat(printList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Print.class);
        Print print1 = new Print();
        print1.setId(1L);
        Print print2 = new Print();
        print2.setId(print1.getId());
        assertThat(print1).isEqualTo(print2);
        print2.setId(2L);
        assertThat(print1).isNotEqualTo(print2);
        print1.setId(null);
        assertThat(print1).isNotEqualTo(print2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrintDTO.class);
        PrintDTO printDTO1 = new PrintDTO();
        printDTO1.setId(1L);
        PrintDTO printDTO2 = new PrintDTO();
        assertThat(printDTO1).isNotEqualTo(printDTO2);
        printDTO2.setId(printDTO1.getId());
        assertThat(printDTO1).isEqualTo(printDTO2);
        printDTO2.setId(2L);
        assertThat(printDTO1).isNotEqualTo(printDTO2);
        printDTO1.setId(null);
        assertThat(printDTO1).isNotEqualTo(printDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(printMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(printMapper.fromId(null)).isNull();
    }
}
