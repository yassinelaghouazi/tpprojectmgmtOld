package ma.jconsulting.applications.btpproject.repository.search;

import ma.jconsulting.applications.btpproject.domain.Caution;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Caution} entity.
 */
public interface CautionSearchRepository extends ElasticsearchRepository<Caution, Long> {
}
