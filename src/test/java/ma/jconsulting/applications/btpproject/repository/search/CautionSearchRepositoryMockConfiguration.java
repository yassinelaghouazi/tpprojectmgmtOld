package ma.jconsulting.applications.btpproject.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link CautionSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CautionSearchRepositoryMockConfiguration {

    @MockBean
    private CautionSearchRepository mockCautionSearchRepository;

}
