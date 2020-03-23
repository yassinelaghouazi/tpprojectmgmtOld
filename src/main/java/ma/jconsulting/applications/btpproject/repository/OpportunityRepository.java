package ma.jconsulting.applications.btpproject.repository;

import ma.jconsulting.applications.btpproject.domain.Opportunity;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Opportunity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
}
