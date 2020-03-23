package ma.jconsulting.applications.btpproject.repository;

import ma.jconsulting.applications.btpproject.domain.Banque;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Banque entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BanqueRepository extends JpaRepository<Banque, Long> {
}
