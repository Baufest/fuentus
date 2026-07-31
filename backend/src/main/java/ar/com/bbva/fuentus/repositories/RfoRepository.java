package ar.com.bbva.fuentus.repositories;

import ar.com.bbva.fuentus.entities.Rfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RfoRepository extends JpaRepository<Rfo, Long>, JpaSpecificationExecutor<Rfo> {
    
    // Método para buscar por RFO ID (criterio de identificación para import)
    Rfo findByRfoId(Long rfoId);
}