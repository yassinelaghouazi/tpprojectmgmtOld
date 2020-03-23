package ma.jconsulting.applications.btpproject.repository;

import ma.jconsulting.applications.btpproject.domain.Caution;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Caution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CautionRepository extends JpaRepository<Caution, Long> {
}
