package ar.com.bbva.fuentus.repositories;

import ar.com.bbva.fuentus.entities.Velocidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VelocidadRepository extends JpaRepository<Velocidad, Long> {

    List<Velocidad> findByDate(LocalDate date);

    @Query("SELECT v FROM Velocidad v WHERE v.nucleus.id = :nucleusId")
    List<Velocidad> findByNucleusId(@Param("nucleusId") Long nucleusId);

    @Query("SELECT v FROM Velocidad v WHERE v.nucleus.id = :nucleusId AND v.date = :date")
    List<Velocidad> findByNucleusIdAndDate(@Param("nucleusId") Long nucleusId, @Param("date") LocalDate date);
}
