package ma.jconsulting.applications.btpproject.web.rest;

import ma.jconsulting.applications.btpproject.BtpprojectApp;
import ma.jconsulting.applications.btpproject.domain.Caution;
import ma.jconsulting.applications.btpproject.domain.Banque;
import ma.jconsulting.applications.btpproject.domain.MaitreOuvrage;
import ma.jconsulting.applications.btpproject.repository.CautionRepository;
import ma.jconsulting.applications.btpproject.repository.search.CautionSearchRepository;

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

import ma.jconsulting.applications.btpproject.domain.enumeration.TypeCaution;
import ma.jconsulting.applications.btpproject.domain.enumeration.StatusCaution;
/**
 * Integration tests for the {@link CautionResource} REST controller.
 */
@SpringBootTest(classes = BtpprojectApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CautionResourceIT {

    private static final String DEFAULT_NUMERO_CAUTION = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_CAUTION = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_APPEL_OFFRE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_APPEL_OFFRE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_MARCHE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_MARCHE = "BBBBBBBBBB";

    private static final String DEFAULT_OBJET_CAUTION = "AAAAAAAAAA";
    private static final String UPDATED_OBJET_CAUTION = "BBBBBBBBBB";

    private static final TypeCaution DEFAULT_TYPE_CAUTION = TypeCaution.PROVISOIRE;
    private static final TypeCaution UPDATED_TYPE_CAUTION = TypeCaution.DEFINITIVE;

    private static final Double DEFAULT_MONTANT_CAUTION = 1D;
    private static final Double UPDATED_MONTANT_CAUTION = 2D;

    private static final StatusCaution DEFAULT_STATUS_CAUTION = StatusCaution.DEMANDEE;
    private static final StatusCaution UPDATED_STATUS_CAUTION = StatusCaution.DEPOSEE;

    private static final LocalDate DEFAULT_DATE_DEMANDE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEMANDE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_RETRAIT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RETRAIT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_DEPOT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEPOT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private CautionRepository cautionRepository;

    /**
     * This repository is mocked in the ma.jconsulting.applications.btpproject.repository.search test package.
     *
     * @see ma.jconsulting.applications.btpproject.repository.search.CautionSearchRepositoryMockConfiguration
     */
    @Autowired
    private CautionSearchRepository mockCautionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCautionMockMvc;

    private Caution caution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caution createEntity(EntityManager em) {
        Caution caution = new Caution()
            .numeroCaution(DEFAULT_NUMERO_CAUTION)
            .numeroAppelOffre(DEFAULT_NUMERO_APPEL_OFFRE)
            .numeroMarche(DEFAULT_NUMERO_MARCHE)
            .objetCaution(DEFAULT_OBJET_CAUTION)
            .typeCaution(DEFAULT_TYPE_CAUTION)
            .montantCaution(DEFAULT_MONTANT_CAUTION)
            .statusCaution(DEFAULT_STATUS_CAUTION)
            .dateDemande(DEFAULT_DATE_DEMANDE)
            .dateRetrait(DEFAULT_DATE_RETRAIT)
            .dateDepot(DEFAULT_DATE_DEPOT);
        // Add required entity
        Banque banque;
        if (TestUtil.findAll(em, Banque.class).isEmpty()) {
            banque = BanqueResourceIT.createEntity(em);
            em.persist(banque);
            em.flush();
        } else {
            banque = TestUtil.findAll(em, Banque.class).get(0);
        }
        caution.setBanque(banque);
        // Add required entity
        MaitreOuvrage maitreOuvrage;
        if (TestUtil.findAll(em, MaitreOuvrage.class).isEmpty()) {
            maitreOuvrage = MaitreOuvrageResourceIT.createEntity(em);
            em.persist(maitreOuvrage);
            em.flush();
        } else {
            maitreOuvrage = TestUtil.findAll(em, MaitreOuvrage.class).get(0);
        }
        caution.setMaitreOuvrage(maitreOuvrage);
        return caution;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caution createUpdatedEntity(EntityManager em) {
        Caution caution = new Caution()
            .numeroCaution(UPDATED_NUMERO_CAUTION)
            .numeroAppelOffre(UPDATED_NUMERO_APPEL_OFFRE)
            .numeroMarche(UPDATED_NUMERO_MARCHE)
            .objetCaution(UPDATED_OBJET_CAUTION)
            .typeCaution(UPDATED_TYPE_CAUTION)
            .montantCaution(UPDATED_MONTANT_CAUTION)
            .statusCaution(UPDATED_STATUS_CAUTION)
            .dateDemande(UPDATED_DATE_DEMANDE)
            .dateRetrait(UPDATED_DATE_RETRAIT)
            .dateDepot(UPDATED_DATE_DEPOT);
        // Add required entity
        Banque banque;
        if (TestUtil.findAll(em, Banque.class).isEmpty()) {
            banque = BanqueResourceIT.createUpdatedEntity(em);
            em.persist(banque);
            em.flush();
        } else {
            banque = TestUtil.findAll(em, Banque.class).get(0);
        }
        caution.setBanque(banque);
        // Add required entity
        MaitreOuvrage maitreOuvrage;
        if (TestUtil.findAll(em, MaitreOuvrage.class).isEmpty()) {
            maitreOuvrage = MaitreOuvrageResourceIT.createUpdatedEntity(em);
            em.persist(maitreOuvrage);
            em.flush();
        } else {
            maitreOuvrage = TestUtil.findAll(em, MaitreOuvrage.class).get(0);
        }
        caution.setMaitreOuvrage(maitreOuvrage);
        return caution;
    }

    @BeforeEach
    public void initTest() {
        caution = createEntity(em);
    }

    @Test
    @Transactional
    public void createCaution() throws Exception {
        int databaseSizeBeforeCreate = cautionRepository.findAll().size();

        // Create the Caution
        restCautionMockMvc.perform(post("/api/cautions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(caution)))
            .andExpect(status().isCreated());

        // Validate the Caution in the database
        List<Caution> cautionList = cautionRepository.findAll();
        assertThat(cautionList).hasSize(databaseSizeBeforeCreate + 1);
        Caution testCaution = cautionList.get(cautionList.size() - 1);
        assertThat(testCaution.getNumeroCaution()).isEqualTo(DEFAULT_NUMERO_CAUTION);
        assertThat(testCaution.getNumeroAppelOffre()).isEqualTo(DEFAULT_NUMERO_APPEL_OFFRE);
        assertThat(testCaution.getNumeroMarche()).isEqualTo(DEFAULT_NUMERO_MARCHE);
        assertThat(testCaution.getObjetCaution()).isEqualTo(DEFAULT_OBJET_CAUTION);
        assertThat(testCaution.getTypeCaution()).isEqualTo(DEFAULT_TYPE_CAUTION);
        assertThat(testCaution.getMontantCaution()).isEqualTo(DEFAULT_MONTANT_CAUTION);
        assertThat(testCaution.getStatusCaution()).isEqualTo(DEFAULT_STATUS_CAUTION);
        assertThat(testCaution.getDateDemande()).isEqualTo(DEFAULT_DATE_DEMANDE);
        assertThat(testCaution.getDateRetrait()).isEqualTo(DEFAULT_DATE_RETRAIT);
        assertThat(testCaution.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);

        // Validate the Caution in Elasticsearch
        verify(mockCautionSearchRepository, times(1)).save(testCaution);
    }

    @Test
    @Transactional
    public void createCautionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cautionRepository.findAll().size();

        // Create the Caution with an existing ID
        caution.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCautionMockMvc.perform(post("/api/cautions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(caution)))
            .andExpect(status().isBadRequest());

        // Validate the Caution in the database
        List<Caution> cautionList = cautionRepository.findAll();
        assertThat(cautionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Caution in Elasticsearch
        verify(mockCautionSearchRepository, times(0)).save(caution);
    }


    @Test
    @Transactional
    public void getAllCautions() throws Exception {
        // Initialize the database
        cautionRepository.saveAndFlush(caution);

        // Get all the cautionList
        restCautionMockMvc.perform(get("/api/cautions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caution.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroCaution").value(hasItem(DEFAULT_NUMERO_CAUTION)))
            .andExpect(jsonPath("$.[*].numeroAppelOffre").value(hasItem(DEFAULT_NUMERO_APPEL_OFFRE)))
            .andExpect(jsonPath("$.[*].numeroMarche").value(hasItem(DEFAULT_NUMERO_MARCHE)))
            .andExpect(jsonPath("$.[*].objetCaution").value(hasItem(DEFAULT_OBJET_CAUTION)))
            .andExpect(jsonPath("$.[*].typeCaution").value(hasItem(DEFAULT_TYPE_CAUTION.toString())))
            .andExpect(jsonPath("$.[*].montantCaution").value(hasItem(DEFAULT_MONTANT_CAUTION.doubleValue())))
            .andExpect(jsonPath("$.[*].statusCaution").value(hasItem(DEFAULT_STATUS_CAUTION.toString())))
            .andExpect(jsonPath("$.[*].dateDemande").value(hasItem(DEFAULT_DATE_DEMANDE.toString())))
            .andExpect(jsonPath("$.[*].dateRetrait").value(hasItem(DEFAULT_DATE_RETRAIT.toString())))
            .andExpect(jsonPath("$.[*].dateDepot").value(hasItem(DEFAULT_DATE_DEPOT.toString())));
    }
    
    @Test
    @Transactional
    public void getCaution() throws Exception {
        // Initialize the database
        cautionRepository.saveAndFlush(caution);

        // Get the caution
        restCautionMockMvc.perform(get("/api/cautions/{id}", caution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(caution.getId().intValue()))
            .andExpect(jsonPath("$.numeroCaution").value(DEFAULT_NUMERO_CAUTION))
            .andExpect(jsonPath("$.numeroAppelOffre").value(DEFAULT_NUMERO_APPEL_OFFRE))
            .andExpect(jsonPath("$.numeroMarche").value(DEFAULT_NUMERO_MARCHE))
            .andExpect(jsonPath("$.objetCaution").value(DEFAULT_OBJET_CAUTION))
            .andExpect(jsonPath("$.typeCaution").value(DEFAULT_TYPE_CAUTION.toString()))
            .andExpect(jsonPath("$.montantCaution").value(DEFAULT_MONTANT_CAUTION.doubleValue()))
            .andExpect(jsonPath("$.statusCaution").value(DEFAULT_STATUS_CAUTION.toString()))
            .andExpect(jsonPath("$.dateDemande").value(DEFAULT_DATE_DEMANDE.toString()))
            .andExpect(jsonPath("$.dateRetrait").value(DEFAULT_DATE_RETRAIT.toString()))
            .andExpect(jsonPath("$.dateDepot").value(DEFAULT_DATE_DEPOT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCaution() throws Exception {
        // Get the caution
        restCautionMockMvc.perform(get("/api/cautions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCaution() throws Exception {
        // Initialize the database
        cautionRepository.saveAndFlush(caution);

        int databaseSizeBeforeUpdate = cautionRepository.findAll().size();

        // Update the caution
        Caution updatedCaution = cautionRepository.findById(caution.getId()).get();
        // Disconnect from session so that the updates on updatedCaution are not directly saved in db
        em.detach(updatedCaution);
        updatedCaution
            .numeroCaution(UPDATED_NUMERO_CAUTION)
            .numeroAppelOffre(UPDATED_NUMERO_APPEL_OFFRE)
            .numeroMarche(UPDATED_NUMERO_MARCHE)
            .objetCaution(UPDATED_OBJET_CAUTION)
            .typeCaution(UPDATED_TYPE_CAUTION)
            .montantCaution(UPDATED_MONTANT_CAUTION)
            .statusCaution(UPDATED_STATUS_CAUTION)
            .dateDemande(UPDATED_DATE_DEMANDE)
            .dateRetrait(UPDATED_DATE_RETRAIT)
            .dateDepot(UPDATED_DATE_DEPOT);

        restCautionMockMvc.perform(put("/api/cautions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCaution)))
            .andExpect(status().isOk());

        // Validate the Caution in the database
        List<Caution> cautionList = cautionRepository.findAll();
        assertThat(cautionList).hasSize(databaseSizeBeforeUpdate);
        Caution testCaution = cautionList.get(cautionList.size() - 1);
        assertThat(testCaution.getNumeroCaution()).isEqualTo(UPDATED_NUMERO_CAUTION);
        assertThat(testCaution.getNumeroAppelOffre()).isEqualTo(UPDATED_NUMERO_APPEL_OFFRE);
        assertThat(testCaution.getNumeroMarche()).isEqualTo(UPDATED_NUMERO_MARCHE);
        assertThat(testCaution.getObjetCaution()).isEqualTo(UPDATED_OBJET_CAUTION);
        assertThat(testCaution.getTypeCaution()).isEqualTo(UPDATED_TYPE_CAUTION);
        assertThat(testCaution.getMontantCaution()).isEqualTo(UPDATED_MONTANT_CAUTION);
        assertThat(testCaution.getStatusCaution()).isEqualTo(UPDATED_STATUS_CAUTION);
        assertThat(testCaution.getDateDemande()).isEqualTo(UPDATED_DATE_DEMANDE);
        assertThat(testCaution.getDateRetrait()).isEqualTo(UPDATED_DATE_RETRAIT);
        assertThat(testCaution.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);

        // Validate the Caution in Elasticsearch
        verify(mockCautionSearchRepository, times(1)).save(testCaution);
    }

    @Test
    @Transactional
    public void updateNonExistingCaution() throws Exception {
        int databaseSizeBeforeUpdate = cautionRepository.findAll().size();

        // Create the Caution

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCautionMockMvc.perform(put("/api/cautions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(caution)))
            .andExpect(status().isBadRequest());

        // Validate the Caution in the database
        List<Caution> cautionList = cautionRepository.findAll();
        assertThat(cautionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Caution in Elasticsearch
        verify(mockCautionSearchRepository, times(0)).save(caution);
    }

    @Test
    @Transactional
    public void deleteCaution() throws Exception {
        // Initialize the database
        cautionRepository.saveAndFlush(caution);

        int databaseSizeBeforeDelete = cautionRepository.findAll().size();

        // Delete the caution
        restCautionMockMvc.perform(delete("/api/cautions/{id}", caution.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Caution> cautionList = cautionRepository.findAll();
        assertThat(cautionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Caution in Elasticsearch
        verify(mockCautionSearchRepository, times(1)).deleteById(caution.getId());
    }

    @Test
    @Transactional
    public void searchCaution() throws Exception {
        // Initialize the database
        cautionRepository.saveAndFlush(caution);
        when(mockCautionSearchRepository.search(queryStringQuery("id:" + caution.getId())))
            .thenReturn(Collections.singletonList(caution));
        // Search the caution
        restCautionMockMvc.perform(get("/api/_search/cautions?query=id:" + caution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caution.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroCaution").value(hasItem(DEFAULT_NUMERO_CAUTION)))
            .andExpect(jsonPath("$.[*].numeroAppelOffre").value(hasItem(DEFAULT_NUMERO_APPEL_OFFRE)))
            .andExpect(jsonPath("$.[*].numeroMarche").value(hasItem(DEFAULT_NUMERO_MARCHE)))
            .andExpect(jsonPath("$.[*].objetCaution").value(hasItem(DEFAULT_OBJET_CAUTION)))
            .andExpect(jsonPath("$.[*].typeCaution").value(hasItem(DEFAULT_TYPE_CAUTION.toString())))
            .andExpect(jsonPath("$.[*].montantCaution").value(hasItem(DEFAULT_MONTANT_CAUTION.doubleValue())))
            .andExpect(jsonPath("$.[*].statusCaution").value(hasItem(DEFAULT_STATUS_CAUTION.toString())))
            .andExpect(jsonPath("$.[*].dateDemande").value(hasItem(DEFAULT_DATE_DEMANDE.toString())))
            .andExpect(jsonPath("$.[*].dateRetrait").value(hasItem(DEFAULT_DATE_RETRAIT.toString())))
            .andExpect(jsonPath("$.[*].dateDepot").value(hasItem(DEFAULT_DATE_DEPOT.toString())));
    }
}
