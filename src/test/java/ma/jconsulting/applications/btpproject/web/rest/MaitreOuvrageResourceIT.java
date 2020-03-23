package ma.jconsulting.applications.btpproject.web.rest;

import ma.jconsulting.applications.btpproject.BtpprojectApp;
import ma.jconsulting.applications.btpproject.domain.MaitreOuvrage;
import ma.jconsulting.applications.btpproject.repository.MaitreOuvrageRepository;
import ma.jconsulting.applications.btpproject.repository.search.MaitreOuvrageSearchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MaitreOuvrageResource} REST controller.
 */
@SpringBootTest(classes = BtpprojectApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class MaitreOuvrageResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSONNE = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSONNE = "BBBBBBBBBB";

    @Autowired
    private MaitreOuvrageRepository maitreOuvrageRepository;

    /**
     * This repository is mocked in the ma.jconsulting.applications.btpproject.repository.search test package.
     *
     * @see ma.jconsulting.applications.btpproject.repository.search.MaitreOuvrageSearchRepositoryMockConfiguration
     */
    @Autowired
    private MaitreOuvrageSearchRepository mockMaitreOuvrageSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaitreOuvrageMockMvc;

    private MaitreOuvrage maitreOuvrage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaitreOuvrage createEntity(EntityManager em) {
        MaitreOuvrage maitreOuvrage = new MaitreOuvrage()
            .nom(DEFAULT_NOM)
            .email(DEFAULT_EMAIL)
            .tel(DEFAULT_TEL)
            .contactPersonne(DEFAULT_CONTACT_PERSONNE);
        return maitreOuvrage;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaitreOuvrage createUpdatedEntity(EntityManager em) {
        MaitreOuvrage maitreOuvrage = new MaitreOuvrage()
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .tel(UPDATED_TEL)
            .contactPersonne(UPDATED_CONTACT_PERSONNE);
        return maitreOuvrage;
    }

    @BeforeEach
    public void initTest() {
        maitreOuvrage = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaitreOuvrage() throws Exception {
        int databaseSizeBeforeCreate = maitreOuvrageRepository.findAll().size();

        // Create the MaitreOuvrage
        restMaitreOuvrageMockMvc.perform(post("/api/maitre-ouvrages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(maitreOuvrage)))
            .andExpect(status().isCreated());

        // Validate the MaitreOuvrage in the database
        List<MaitreOuvrage> maitreOuvrageList = maitreOuvrageRepository.findAll();
        assertThat(maitreOuvrageList).hasSize(databaseSizeBeforeCreate + 1);
        MaitreOuvrage testMaitreOuvrage = maitreOuvrageList.get(maitreOuvrageList.size() - 1);
        assertThat(testMaitreOuvrage.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testMaitreOuvrage.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMaitreOuvrage.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testMaitreOuvrage.getContactPersonne()).isEqualTo(DEFAULT_CONTACT_PERSONNE);

        // Validate the MaitreOuvrage in Elasticsearch
        verify(mockMaitreOuvrageSearchRepository, times(1)).save(testMaitreOuvrage);
    }

    @Test
    @Transactional
    public void createMaitreOuvrageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = maitreOuvrageRepository.findAll().size();

        // Create the MaitreOuvrage with an existing ID
        maitreOuvrage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaitreOuvrageMockMvc.perform(post("/api/maitre-ouvrages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(maitreOuvrage)))
            .andExpect(status().isBadRequest());

        // Validate the MaitreOuvrage in the database
        List<MaitreOuvrage> maitreOuvrageList = maitreOuvrageRepository.findAll();
        assertThat(maitreOuvrageList).hasSize(databaseSizeBeforeCreate);

        // Validate the MaitreOuvrage in Elasticsearch
        verify(mockMaitreOuvrageSearchRepository, times(0)).save(maitreOuvrage);
    }


    @Test
    @Transactional
    public void getAllMaitreOuvrages() throws Exception {
        // Initialize the database
        maitreOuvrageRepository.saveAndFlush(maitreOuvrage);

        // Get all the maitreOuvrageList
        restMaitreOuvrageMockMvc.perform(get("/api/maitre-ouvrages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maitreOuvrage.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].contactPersonne").value(hasItem(DEFAULT_CONTACT_PERSONNE)));
    }
    
    @Test
    @Transactional
    public void getMaitreOuvrage() throws Exception {
        // Initialize the database
        maitreOuvrageRepository.saveAndFlush(maitreOuvrage);

        // Get the maitreOuvrage
        restMaitreOuvrageMockMvc.perform(get("/api/maitre-ouvrages/{id}", maitreOuvrage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(maitreOuvrage.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL))
            .andExpect(jsonPath("$.contactPersonne").value(DEFAULT_CONTACT_PERSONNE));
    }

    @Test
    @Transactional
    public void getNonExistingMaitreOuvrage() throws Exception {
        // Get the maitreOuvrage
        restMaitreOuvrageMockMvc.perform(get("/api/maitre-ouvrages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaitreOuvrage() throws Exception {
        // Initialize the database
        maitreOuvrageRepository.saveAndFlush(maitreOuvrage);

        int databaseSizeBeforeUpdate = maitreOuvrageRepository.findAll().size();

        // Update the maitreOuvrage
        MaitreOuvrage updatedMaitreOuvrage = maitreOuvrageRepository.findById(maitreOuvrage.getId()).get();
        // Disconnect from session so that the updates on updatedMaitreOuvrage are not directly saved in db
        em.detach(updatedMaitreOuvrage);
        updatedMaitreOuvrage
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .tel(UPDATED_TEL)
            .contactPersonne(UPDATED_CONTACT_PERSONNE);

        restMaitreOuvrageMockMvc.perform(put("/api/maitre-ouvrages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMaitreOuvrage)))
            .andExpect(status().isOk());

        // Validate the MaitreOuvrage in the database
        List<MaitreOuvrage> maitreOuvrageList = maitreOuvrageRepository.findAll();
        assertThat(maitreOuvrageList).hasSize(databaseSizeBeforeUpdate);
        MaitreOuvrage testMaitreOuvrage = maitreOuvrageList.get(maitreOuvrageList.size() - 1);
        assertThat(testMaitreOuvrage.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testMaitreOuvrage.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMaitreOuvrage.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testMaitreOuvrage.getContactPersonne()).isEqualTo(UPDATED_CONTACT_PERSONNE);

        // Validate the MaitreOuvrage in Elasticsearch
        verify(mockMaitreOuvrageSearchRepository, times(1)).save(testMaitreOuvrage);
    }

    @Test
    @Transactional
    public void updateNonExistingMaitreOuvrage() throws Exception {
        int databaseSizeBeforeUpdate = maitreOuvrageRepository.findAll().size();

        // Create the MaitreOuvrage

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaitreOuvrageMockMvc.perform(put("/api/maitre-ouvrages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(maitreOuvrage)))
            .andExpect(status().isBadRequest());

        // Validate the MaitreOuvrage in the database
        List<MaitreOuvrage> maitreOuvrageList = maitreOuvrageRepository.findAll();
        assertThat(maitreOuvrageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MaitreOuvrage in Elasticsearch
        verify(mockMaitreOuvrageSearchRepository, times(0)).save(maitreOuvrage);
    }

    @Test
    @Transactional
    public void deleteMaitreOuvrage() throws Exception {
        // Initialize the database
        maitreOuvrageRepository.saveAndFlush(maitreOuvrage);

        int databaseSizeBeforeDelete = maitreOuvrageRepository.findAll().size();

        // Delete the maitreOuvrage
        restMaitreOuvrageMockMvc.perform(delete("/api/maitre-ouvrages/{id}", maitreOuvrage.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MaitreOuvrage> maitreOuvrageList = maitreOuvrageRepository.findAll();
        assertThat(maitreOuvrageList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MaitreOuvrage in Elasticsearch
        verify(mockMaitreOuvrageSearchRepository, times(1)).deleteById(maitreOuvrage.getId());
    }

    @Test
    @Transactional
    public void searchMaitreOuvrage() throws Exception {
        // Initialize the database
        maitreOuvrageRepository.saveAndFlush(maitreOuvrage);
        when(mockMaitreOuvrageSearchRepository.search(queryStringQuery("id:" + maitreOuvrage.getId())))
            .thenReturn(Collections.singletonList(maitreOuvrage));
        // Search the maitreOuvrage
        restMaitreOuvrageMockMvc.perform(get("/api/_search/maitre-ouvrages?query=id:" + maitreOuvrage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maitreOuvrage.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].contactPersonne").value(hasItem(DEFAULT_CONTACT_PERSONNE)));
    }
}
