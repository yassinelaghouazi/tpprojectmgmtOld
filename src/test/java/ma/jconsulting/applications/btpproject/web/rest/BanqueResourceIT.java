package ma.jconsulting.applications.btpproject.web.rest;

import ma.jconsulting.applications.btpproject.BtpprojectApp;
import ma.jconsulting.applications.btpproject.domain.Banque;
import ma.jconsulting.applications.btpproject.repository.BanqueRepository;
import ma.jconsulting.applications.btpproject.repository.search.BanqueSearchRepository;

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
 * Integration tests for the {@link BanqueResource} REST controller.
 */
@SpringBootTest(classes = BtpprojectApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class BanqueResourceIT {

    private static final String DEFAULT_BANQUE = "AAAAAAAAAA";
    private static final String UPDATED_BANQUE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_AGENCE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_AGENCE = "BBBBBBBBBB";

    @Autowired
    private BanqueRepository banqueRepository;

    /**
     * This repository is mocked in the ma.jconsulting.applications.btpproject.repository.search test package.
     *
     * @see ma.jconsulting.applications.btpproject.repository.search.BanqueSearchRepositoryMockConfiguration
     */
    @Autowired
    private BanqueSearchRepository mockBanqueSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBanqueMockMvc;

    private Banque banque;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Banque createEntity(EntityManager em) {
        Banque banque = new Banque()
            .banque(DEFAULT_BANQUE)
            .contactEmail(DEFAULT_CONTACT_EMAIL)
            .contactTel(DEFAULT_CONTACT_TEL)
            .adresseAgence(DEFAULT_ADRESSE_AGENCE);
        return banque;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Banque createUpdatedEntity(EntityManager em) {
        Banque banque = new Banque()
            .banque(UPDATED_BANQUE)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .contactTel(UPDATED_CONTACT_TEL)
            .adresseAgence(UPDATED_ADRESSE_AGENCE);
        return banque;
    }

    @BeforeEach
    public void initTest() {
        banque = createEntity(em);
    }

    @Test
    @Transactional
    public void createBanque() throws Exception {
        int databaseSizeBeforeCreate = banqueRepository.findAll().size();

        // Create the Banque
        restBanqueMockMvc.perform(post("/api/banques")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(banque)))
            .andExpect(status().isCreated());

        // Validate the Banque in the database
        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeCreate + 1);
        Banque testBanque = banqueList.get(banqueList.size() - 1);
        assertThat(testBanque.getBanque()).isEqualTo(DEFAULT_BANQUE);
        assertThat(testBanque.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testBanque.getContactTel()).isEqualTo(DEFAULT_CONTACT_TEL);
        assertThat(testBanque.getAdresseAgence()).isEqualTo(DEFAULT_ADRESSE_AGENCE);

        // Validate the Banque in Elasticsearch
        verify(mockBanqueSearchRepository, times(1)).save(testBanque);
    }

    @Test
    @Transactional
    public void createBanqueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = banqueRepository.findAll().size();

        // Create the Banque with an existing ID
        banque.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBanqueMockMvc.perform(post("/api/banques")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(banque)))
            .andExpect(status().isBadRequest());

        // Validate the Banque in the database
        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeCreate);

        // Validate the Banque in Elasticsearch
        verify(mockBanqueSearchRepository, times(0)).save(banque);
    }


    @Test
    @Transactional
    public void checkBanqueIsRequired() throws Exception {
        int databaseSizeBeforeTest = banqueRepository.findAll().size();
        // set the field null
        banque.setBanque(null);

        // Create the Banque, which fails.

        restBanqueMockMvc.perform(post("/api/banques")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(banque)))
            .andExpect(status().isBadRequest());

        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBanques() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList
        restBanqueMockMvc.perform(get("/api/banques?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banque.getId().intValue())))
            .andExpect(jsonPath("$.[*].banque").value(hasItem(DEFAULT_BANQUE)))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].contactTel").value(hasItem(DEFAULT_CONTACT_TEL)))
            .andExpect(jsonPath("$.[*].adresseAgence").value(hasItem(DEFAULT_ADRESSE_AGENCE)));
    }
    
    @Test
    @Transactional
    public void getBanque() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get the banque
        restBanqueMockMvc.perform(get("/api/banques/{id}", banque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(banque.getId().intValue()))
            .andExpect(jsonPath("$.banque").value(DEFAULT_BANQUE))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL))
            .andExpect(jsonPath("$.contactTel").value(DEFAULT_CONTACT_TEL))
            .andExpect(jsonPath("$.adresseAgence").value(DEFAULT_ADRESSE_AGENCE));
    }

    @Test
    @Transactional
    public void getNonExistingBanque() throws Exception {
        // Get the banque
        restBanqueMockMvc.perform(get("/api/banques/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBanque() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        int databaseSizeBeforeUpdate = banqueRepository.findAll().size();

        // Update the banque
        Banque updatedBanque = banqueRepository.findById(banque.getId()).get();
        // Disconnect from session so that the updates on updatedBanque are not directly saved in db
        em.detach(updatedBanque);
        updatedBanque
            .banque(UPDATED_BANQUE)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .contactTel(UPDATED_CONTACT_TEL)
            .adresseAgence(UPDATED_ADRESSE_AGENCE);

        restBanqueMockMvc.perform(put("/api/banques")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedBanque)))
            .andExpect(status().isOk());

        // Validate the Banque in the database
        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
        Banque testBanque = banqueList.get(banqueList.size() - 1);
        assertThat(testBanque.getBanque()).isEqualTo(UPDATED_BANQUE);
        assertThat(testBanque.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testBanque.getContactTel()).isEqualTo(UPDATED_CONTACT_TEL);
        assertThat(testBanque.getAdresseAgence()).isEqualTo(UPDATED_ADRESSE_AGENCE);

        // Validate the Banque in Elasticsearch
        verify(mockBanqueSearchRepository, times(1)).save(testBanque);
    }

    @Test
    @Transactional
    public void updateNonExistingBanque() throws Exception {
        int databaseSizeBeforeUpdate = banqueRepository.findAll().size();

        // Create the Banque

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBanqueMockMvc.perform(put("/api/banques")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(banque)))
            .andExpect(status().isBadRequest());

        // Validate the Banque in the database
        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Banque in Elasticsearch
        verify(mockBanqueSearchRepository, times(0)).save(banque);
    }

    @Test
    @Transactional
    public void deleteBanque() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        int databaseSizeBeforeDelete = banqueRepository.findAll().size();

        // Delete the banque
        restBanqueMockMvc.perform(delete("/api/banques/{id}", banque.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Banque in Elasticsearch
        verify(mockBanqueSearchRepository, times(1)).deleteById(banque.getId());
    }

    @Test
    @Transactional
    public void searchBanque() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);
        when(mockBanqueSearchRepository.search(queryStringQuery("id:" + banque.getId())))
            .thenReturn(Collections.singletonList(banque));
        // Search the banque
        restBanqueMockMvc.perform(get("/api/_search/banques?query=id:" + banque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banque.getId().intValue())))
            .andExpect(jsonPath("$.[*].banque").value(hasItem(DEFAULT_BANQUE)))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].contactTel").value(hasItem(DEFAULT_CONTACT_TEL)))
            .andExpect(jsonPath("$.[*].adresseAgence").value(hasItem(DEFAULT_ADRESSE_AGENCE)));
    }
}
