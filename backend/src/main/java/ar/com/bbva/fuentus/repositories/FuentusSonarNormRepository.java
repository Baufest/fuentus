package ar.com.bbva.fuentus.repositories;

import ar.com.bbva.fuentus.entities.FuentusSonarNorm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuentusSonarNormRepository extends JpaRepository<FuentusSonarNorm, Long> {
}
