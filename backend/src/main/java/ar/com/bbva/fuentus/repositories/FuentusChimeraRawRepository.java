package ar.com.bbva.fuentus.repositories;


import ar.com.bbva.fuentus.entities.FuentusChimeraRaw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuentusChimeraRawRepository extends JpaRepository<FuentusChimeraRaw, Long> {
}