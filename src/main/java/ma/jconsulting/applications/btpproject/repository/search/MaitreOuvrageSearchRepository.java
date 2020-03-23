package ma.jconsulting.applications.btpproject.repository.search;

import ma.jconsulting.applications.btpproject.domain.MaitreOuvrage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link MaitreOuvrage} entity.
 */
public interface MaitreOuvrageSearchRepository extends ElasticsearchRepository<MaitreOuvrage, Long> {
}
