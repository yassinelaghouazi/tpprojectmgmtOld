package ma.jconsulting.applications.btpproject.web.rest;

import ma.jconsulting.applications.btpproject.domain.MaitreOuvrage;
import ma.jconsulting.applications.btpproject.repository.MaitreOuvrageRepository;
import ma.jconsulting.applications.btpproject.repository.search.MaitreOuvrageSearchRepository;
import ma.jconsulting.applications.btpproject.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link ma.jconsulting.applications.btpproject.domain.MaitreOuvrage}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MaitreOuvrageResource {

    private final Logger log = LoggerFactory.getLogger(MaitreOuvrageResource.class);

    private static final String ENTITY_NAME = "maitreOuvrage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaitreOuvrageRepository maitreOuvrageRepository;

    private final MaitreOuvrageSearchRepository maitreOuvrageSearchRepository;

    public MaitreOuvrageResource(MaitreOuvrageRepository maitreOuvrageRepository, MaitreOuvrageSearchRepository maitreOuvrageSearchRepository) {
        this.maitreOuvrageRepository = maitreOuvrageRepository;
        this.maitreOuvrageSearchRepository = maitreOuvrageSearchRepository;
    }

    /**
     * {@code POST  /maitre-ouvrages} : Create a new maitreOuvrage.
     *
     * @param maitreOuvrage the maitreOuvrage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new maitreOuvrage, or with status {@code 400 (Bad Request)} if the maitreOuvrage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/maitre-ouvrages")
    public ResponseEntity<MaitreOuvrage> createMaitreOuvrage(@RequestBody MaitreOuvrage maitreOuvrage) throws URISyntaxException {
        log.debug("REST request to save MaitreOuvrage : {}", maitreOuvrage);
        if (maitreOuvrage.getId() != null) {
            throw new BadRequestAlertException("A new maitreOuvrage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaitreOuvrage result = maitreOuvrageRepository.save(maitreOuvrage);
        maitreOuvrageSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/maitre-ouvrages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /maitre-ouvrages} : Updates an existing maitreOuvrage.
     *
     * @param maitreOuvrage the maitreOuvrage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maitreOuvrage,
     * or with status {@code 400 (Bad Request)} if the maitreOuvrage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the maitreOuvrage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/maitre-ouvrages")
    public ResponseEntity<MaitreOuvrage> updateMaitreOuvrage(@RequestBody MaitreOuvrage maitreOuvrage) throws URISyntaxException {
        log.debug("REST request to update MaitreOuvrage : {}", maitreOuvrage);
        if (maitreOuvrage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaitreOuvrage result = maitreOuvrageRepository.save(maitreOuvrage);
        maitreOuvrageSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, maitreOuvrage.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /maitre-ouvrages} : get all the maitreOuvrages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of maitreOuvrages in body.
     */
    @GetMapping("/maitre-ouvrages")
    public List<MaitreOuvrage> getAllMaitreOuvrages() {
        log.debug("REST request to get all MaitreOuvrages");
        return maitreOuvrageRepository.findAll();
    }

    /**
     * {@code GET  /maitre-ouvrages/:id} : get the "id" maitreOuvrage.
     *
     * @param id the id of the maitreOuvrage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the maitreOuvrage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/maitre-ouvrages/{id}")
    public ResponseEntity<MaitreOuvrage> getMaitreOuvrage(@PathVariable Long id) {
        log.debug("REST request to get MaitreOuvrage : {}", id);
        Optional<MaitreOuvrage> maitreOuvrage = maitreOuvrageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(maitreOuvrage);
    }

    /**
     * {@code DELETE  /maitre-ouvrages/:id} : delete the "id" maitreOuvrage.
     *
     * @param id the id of the maitreOuvrage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/maitre-ouvrages/{id}")
    public ResponseEntity<Void> deleteMaitreOuvrage(@PathVariable Long id) {
        log.debug("REST request to delete MaitreOuvrage : {}", id);
        maitreOuvrageRepository.deleteById(id);
        maitreOuvrageSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/maitre-ouvrages?query=:query} : search for the maitreOuvrage corresponding
     * to the query.
     *
     * @param query the query of the maitreOuvrage search.
     * @return the result of the search.
     */
    @GetMapping("/_search/maitre-ouvrages")
    public List<MaitreOuvrage> searchMaitreOuvrages(@RequestParam String query) {
        log.debug("REST request to search MaitreOuvrages for query {}", query);
        return StreamSupport
            .stream(maitreOuvrageSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
