package ma.jconsulting.applications.btpproject.repository;

import ma.jconsulting.applications.btpproject.domain.MaitreOuvrage;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the MaitreOuvrage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaitreOuvrageRepository extends JpaRepository<MaitreOuvrage, Long> {
}
