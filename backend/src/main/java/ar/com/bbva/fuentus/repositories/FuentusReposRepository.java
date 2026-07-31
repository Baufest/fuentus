package ar.com.bbva.fuentus.repositories;

import ar.com.bbva.fuentus.entities.FuentusRepos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuentusReposRepository extends JpaRepository<FuentusRepos, Long> {
    // Additional query methods can be defined here
}
