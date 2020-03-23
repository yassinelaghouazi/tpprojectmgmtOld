package ma.jconsulting.applications.btpproject.repository.search;

import ma.jconsulting.applications.btpproject.domain.Banque;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Banque} entity.
 */
public interface BanqueSearchRepository extends ElasticsearchRepository<Banque, Long> {
}
