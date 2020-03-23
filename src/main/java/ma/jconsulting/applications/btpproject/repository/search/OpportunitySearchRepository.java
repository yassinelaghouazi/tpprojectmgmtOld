package ma.jconsulting.applications.btpproject.repository.search;

import ma.jconsulting.applications.btpproject.domain.Opportunity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Opportunity} entity.
 */
public interface OpportunitySearchRepository extends ElasticsearchRepository<Opportunity, Long> {
}
