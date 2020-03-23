package ma.jconsulting.applications.btpproject.web.rest;

import ma.jconsulting.applications.btpproject.BtpprojectApp;
import ma.jconsulting.applications.btpproject.domain.Opportunity;
import ma.jconsulting.applications.btpproject.domain.MaitreOuvrage;
import ma.jconsulting.applications.btpproject.domain.Caution;
import ma.jconsulting.applications.btpproject.repository.OpportunityRepository;
import ma.jconsulting.applications.btpproject.repository.search.OpportunitySearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link OpportunityResource} REST controller.
 */
@SpringBootTest(classes = BtpprojectApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class OpportunityResourceIT {

    private static final String DEFAULT_NUMERO_APPEL_OFFRE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_APPEL_OFFRE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_REMISE_PLIS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_REMISE_PLIS = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_MONTANT_CAUTION = 1D;
    private static final Double UPDATED_MONTANT_CAUTION = 2D;

    private static final String DEFAULT_OBJET_AFFAIRE = "AAAAAAAAAA";
    private static final String UPDATED_OBJET_AFFAIRE = "BBBBBBBBBB";

    private static final Double DEFAULT_ESTIMATION_BUDGET = 1D;
    private static final Double UPDATED_ESTIMATION_BUDGET = 2D;

    private static final String DEFAULT_COMMENTAIRES = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRES = "BBBBBBBBBB";

    @Autowired
    private OpportunityRepository opportunityRepository;

    /**
     * This repository is mocked in the ma.jconsulting.applications.btpproject.repository.search test package.
     *
     * @see ma.jconsulting.applications.btpproject.repository.search.OpportunitySearchRepositoryMockConfiguration
     */
    @Autowired
    private OpportunitySearchRepository mockOpportunitySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOpportunityMockMvc;

    private Opportunity opportunity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opportunity createEntity(EntityManager em) {
        Opportunity opportunity = new Opportunity()
            .numeroAppelOffre(DEFAULT_NUMERO_APPEL_OFFRE)
            .dateRemisePlis(DEFAULT_DATE_REMISE_PLIS)
            .montantCaution(DEFAULT_MONTANT_CAUTION)
            .objetAffaire(DEFAULT_OBJET_AFFAIRE)
            .estimationBudget(DEFAULT_ESTIMATION_BUDGET)
            .commentaires(DEFAULT_COMMENTAIRES);
        // Add required entity
        MaitreOuvrage maitreOuvrage;
        if (TestUtil.findAll(em, MaitreOuvrage.class).isEmpty()) {
            maitreOuvrage = MaitreOuvrageResourceIT.createEntity(em);
            em.persist(maitreOuvrage);
            em.flush();
        } else {
            maitreOuvrage = TestUtil.findAll(em, MaitreOuvrage.class).get(0);
        }
        opportunity.setMaitreOuvrage(maitreOuvrage);
        // Add required entity
        Caution caution;
        if (TestUtil.findAll(em, Caution.class).isEmpty()) {
            caution = CautionResourceIT.createEntity(em);
            em.persist(caution);
            em.flush();
        } else {
            caution = TestUtil.findAll(em, Caution.class).get(0);
        }
        opportunity.setCaution(caution);
        return opportunity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opportunity createUpdatedEntity(EntityManager em) {
        Opportunity opportunity = new Opportunity()
            .numeroAppelOffre(UPDATED_NUMERO_APPEL_OFFRE)
            .dateRemisePlis(UPDATED_DATE_REMISE_PLIS)
            .montantCaution(UPDATED_MONTANT_CAUTION)
            .objetAffaire(UPDATED_OBJET_AFFAIRE)
            .estimationBudget(UPDATED_ESTIMATION_BUDGET)
            .commentaires(UPDATED_COMMENTAIRES);
        // Add required entity
        MaitreOuvrage maitreOuvrage;
        if (TestUtil.findAll(em, MaitreOuvrage.class).isEmpty()) {
            maitreOuvrage = MaitreOuvrageResourceIT.createUpdatedEntity(em);
            em.persist(maitreOuvrage);
            em.flush();
        } else {
            maitreOuvrage = TestUtil.findAll(em, MaitreOuvrage.class).get(0);
        }
        opportunity.setMaitreOuvrage(maitreOuvrage);
        // Add required entity
        Caution caution;
        if (TestUtil.findAll(em, Caution.class).isEmpty()) {
            caution = CautionResourceIT.createUpdatedEntity(em);
            em.persist(caution);
            em.flush();
        } else {
            caution = TestUtil.findAll(em, Caution.class).get(0);
        }
        opportunity.setCaution(caution);
        return opportunity;
    }

    @BeforeEach
    public void initTest() {
        opportunity = createEntity(em);
    }

    @Test
    @Transactional
    public void createOpportunity() throws Exception {
        int databaseSizeBeforeCreate = opportunityRepository.findAll().size();

        // Create the Opportunity
        restOpportunityMockMvc.perform(post("/api/opportunities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(opportunity)))
            .andExpect(status().isCreated());

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeCreate + 1);
        Opportunity testOpportunity = opportunityList.get(opportunityList.size() - 1);
        assertThat(testOpportunity.getNumeroAppelOffre()).isEqualTo(DEFAULT_NUMERO_APPEL_OFFRE);
        assertThat(testOpportunity.getDateRemisePlis()).isEqualTo(DEFAULT_DATE_REMISE_PLIS);
        assertThat(testOpportunity.getMontantCaution()).isEqualTo(DEFAULT_MONTANT_CAUTION);
        assertThat(testOpportunity.getObjetAffaire()).isEqualTo(DEFAULT_OBJET_AFFAIRE);
        assertThat(testOpportunity.getEstimationBudget()).isEqualTo(DEFAULT_ESTIMATION_BUDGET);
        assertThat(testOpportunity.getCommentaires()).isEqualTo(DEFAULT_COMMENTAIRES);

        // Validate the Opportunity in Elasticsearch
        verify(mockOpportunitySearchRepository, times(1)).save(testOpportunity);
    }

    @Test
    @Transactional
    public void createOpportunityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = opportunityRepository.findAll().size();

        // Create the Opportunity with an existing ID
        opportunity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOpportunityMockMvc.perform(post("/api/opportunities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(opportunity)))
            .andExpect(status().isBadRequest());

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeCreate);

        // Validate the Opportunity in Elasticsearch
        verify(mockOpportunitySearchRepository, times(0)).save(opportunity);
    }


    @Test
    @Transactional
    public void checkNumeroAppelOffreIsRequired() throws Exception {
        int databaseSizeBeforeTest = opportunityRepository.findAll().size();
        // set the field null
        opportunity.setNumeroAppelOffre(null);

        // Create the Opportunity, which fails.

        restOpportunityMockMvc.perform(post("/api/opportunities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(opportunity)))
            .andExpect(status().isBadRequest());

        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOpportunities() throws Exception {
        // Initialize the database
        opportunityRepository.saveAndFlush(opportunity);

        // Get all the opportunityList
        restOpportunityMockMvc.perform(get("/api/opportunities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opportunity.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroAppelOffre").value(hasItem(DEFAULT_NUMERO_APPEL_OFFRE)))
            .andExpect(jsonPath("$.[*].dateRemisePlis").value(hasItem(DEFAULT_DATE_REMISE_PLIS.toString())))
            .andExpect(jsonPath("$.[*].montantCaution").value(hasItem(DEFAULT_MONTANT_CAUTION.doubleValue())))
            .andExpect(jsonPath("$.[*].objetAffaire").value(hasItem(DEFAULT_OBJET_AFFAIRE)))
            .andExpect(jsonPath("$.[*].estimationBudget").value(hasItem(DEFAULT_ESTIMATION_BUDGET.doubleValue())))
            .andExpect(jsonPath("$.[*].commentaires").value(hasItem(DEFAULT_COMMENTAIRES)));
    }
    
    @Test
    @Transactional
    public void getOpportunity() throws Exception {
        // Initialize the database
        opportunityRepository.saveAndFlush(opportunity);

        // Get the opportunity
        restOpportunityMockMvc.perform(get("/api/opportunities/{id}", opportunity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(opportunity.getId().intValue()))
            .andExpect(jsonPath("$.numeroAppelOffre").value(DEFAULT_NUMERO_APPEL_OFFRE))
            .andExpect(jsonPath("$.dateRemisePlis").value(DEFAULT_DATE_REMISE_PLIS.toString()))
            .andExpect(jsonPath("$.montantCaution").value(DEFAULT_MONTANT_CAUTION.doubleValue()))
            .andExpect(jsonPath("$.objetAffaire").value(DEFAULT_OBJET_AFFAIRE))
            .andExpect(jsonPath("$.estimationBudget").value(DEFAULT_ESTIMATION_BUDGET.doubleValue()))
            .andExpect(jsonPath("$.commentaires").value(DEFAULT_COMMENTAIRES));
    }

    @Test
    @Transactional
    public void getNonExistingOpportunity() throws Exception {
        // Get the opportunity
        restOpportunityMockMvc.perform(get("/api/opportunities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOpportunity() throws Exception {
        // Initialize the database
        opportunityRepository.saveAndFlush(opportunity);

        int databaseSizeBeforeUpdate = opportunityRepository.findAll().size();

        // Update the opportunity
        Opportunity updatedOpportunity = opportunityRepository.findById(opportunity.getId()).get();
        // Disconnect from session so that the updates on updatedOpportunity are not directly saved in db
        em.detach(updatedOpportunity);
        updatedOpportunity
            .numeroAppelOffre(UPDATED_NUMERO_APPEL_OFFRE)
            .dateRemisePlis(UPDATED_DATE_REMISE_PLIS)
            .montantCaution(UPDATED_MONTANT_CAUTION)
            .objetAffaire(UPDATED_OBJET_AFFAIRE)
            .estimationBudget(UPDATED_ESTIMATION_BUDGET)
            .commentaires(UPDATED_COMMENTAIRES);

        restOpportunityMockMvc.perform(put("/api/opportunities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedOpportunity)))
            .andExpect(status().isOk());

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeUpdate);
        Opportunity testOpportunity = opportunityList.get(opportunityList.size() - 1);
        assertThat(testOpportunity.getNumeroAppelOffre()).isEqualTo(UPDATED_NUMERO_APPEL_OFFRE);
        assertThat(testOpportunity.getDateRemisePlis()).isEqualTo(UPDATED_DATE_REMISE_PLIS);
        assertThat(testOpportunity.getMontantCaution()).isEqualTo(UPDATED_MONTANT_CAUTION);
        assertThat(testOpportunity.getObjetAffaire()).isEqualTo(UPDATED_OBJET_AFFAIRE);
        assertThat(testOpportunity.getEstimationBudget()).isEqualTo(UPDATED_ESTIMATION_BUDGET);
        assertThat(testOpportunity.getCommentaires()).isEqualTo(UPDATED_COMMENTAIRES);

        // Validate the Opportunity in Elasticsearch
        verify(mockOpportunitySearchRepository, times(1)).save(testOpportunity);
    }

    @Test
    @Transactional
    public void updateNonExistingOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = opportunityRepository.findAll().size();

        // Create the Opportunity

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpportunityMockMvc.perform(put("/api/opportunities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(opportunity)))
            .andExpect(status().isBadRequest());

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Opportunity in Elasticsearch
        verify(mockOpportunitySearchRepository, times(0)).save(opportunity);
    }

    @Test
    @Transactional
    public void deleteOpportunity() throws Exception {
        // Initialize the database
        opportunityRepository.saveAndFlush(opportunity);

        int databaseSizeBeforeDelete = opportunityRepository.findAll().size();

        // Delete the opportunity
        restOpportunityMockMvc.perform(delete("/api/opportunities/{id}", opportunity.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Opportunity in Elasticsearch
        verify(mockOpportunitySearchRepository, times(1)).deleteById(opportunity.getId());
    }

    @Test
    @Transactional
    public void searchOpportunity() throws Exception {
        // Initialize the database
        opportunityRepository.saveAndFlush(opportunity);
        when(mockOpportunitySearchRepository.search(queryStringQuery("id:" + opportunity.getId())))
            .thenReturn(Collections.singletonList(opportunity));
        // Search the opportunity
        restOpportunityMockMvc.perform(get("/api/_search/opportunities?query=id:" + opportunity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opportunity.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroAppelOffre").value(hasItem(DEFAULT_NUMERO_APPEL_OFFRE)))
            .andExpect(jsonPath("$.[*].dateRemisePlis").value(hasItem(DEFAULT_DATE_REMISE_PLIS.toString())))
            .andExpect(jsonPath("$.[*].montantCaution").value(hasItem(DEFAULT_MONTANT_CAUTION.doubleValue())))
            .andExpect(jsonPath("$.[*].objetAffaire").value(hasItem(DEFAULT_OBJET_AFFAIRE)))
            .andExpect(jsonPath("$.[*].estimationBudget").value(hasItem(DEFAULT_ESTIMATION_BUDGET.doubleValue())))
            .andExpect(jsonPath("$.[*].commentaires").value(hasItem(DEFAULT_COMMENTAIRES)));
    }
}
