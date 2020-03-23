package ma.jconsulting.applications.btpproject.web.rest;

import ma.jconsulting.applications.btpproject.domain.Caution;
import ma.jconsulting.applications.btpproject.repository.CautionRepository;
import ma.jconsulting.applications.btpproject.repository.search.CautionSearchRepository;
import ma.jconsulting.applications.btpproject.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link ma.jconsulting.applications.btpproject.domain.Caution}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CautionResource {

    private final Logger log = LoggerFactory.getLogger(CautionResource.class);

    private static final String ENTITY_NAME = "caution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CautionRepository cautionRepository;

    private final CautionSearchRepository cautionSearchRepository;

    public CautionResource(CautionRepository cautionRepository, CautionSearchRepository cautionSearchRepository) {
        this.cautionRepository = cautionRepository;
        this.cautionSearchRepository = cautionSearchRepository;
    }

    /**
     * {@code POST  /cautions} : Create a new caution.
     *
     * @param caution the caution to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new caution, or with status {@code 400 (Bad Request)} if the caution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cautions")
    public ResponseEntity<Caution> createCaution(@Valid @RequestBody Caution caution) throws URISyntaxException {
        log.debug("REST request to save Caution : {}", caution);
        if (caution.getId() != null) {
            throw new BadRequestAlertException("A new caution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Caution result = cautionRepository.save(caution);
        cautionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cautions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cautions} : Updates an existing caution.
     *
     * @param caution the caution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caution,
     * or with status {@code 400 (Bad Request)} if the caution is not valid,
     * or with status {@code 500 (Internal Server Error)} if the caution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cautions")
    public ResponseEntity<Caution> updateCaution(@Valid @RequestBody Caution caution) throws URISyntaxException {
        log.debug("REST request to update Caution : {}", caution);
        if (caution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Caution result = cautionRepository.save(caution);
        cautionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caution.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cautions} : get all the cautions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cautions in body.
     */
    @GetMapping("/cautions")
    public List<Caution> getAllCautions() {
        log.debug("REST request to get all Cautions");
        return cautionRepository.findAll();
    }

    /**
     * {@code GET  /cautions/:id} : get the "id" caution.
     *
     * @param id the id of the caution to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the caution, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cautions/{id}")
    public ResponseEntity<Caution> getCaution(@PathVariable Long id) {
        log.debug("REST request to get Caution : {}", id);
        Optional<Caution> caution = cautionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(caution);
    }

    /**
     * {@code DELETE  /cautions/:id} : delete the "id" caution.
     *
     * @param id the id of the caution to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cautions/{id}")
    public ResponseEntity<Void> deleteCaution(@PathVariable Long id) {
        log.debug("REST request to delete Caution : {}", id);
        cautionRepository.deleteById(id);
        cautionSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/cautions?query=:query} : search for the caution corresponding
     * to the query.
     *
     * @param query the query of the caution search.
     * @return the result of the search.
     */
    @GetMapping("/_search/cautions")
    public List<Caution> searchCautions(@RequestParam String query) {
        log.debug("REST request to search Cautions for query {}", query);
        return StreamSupport
            .stream(cautionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
