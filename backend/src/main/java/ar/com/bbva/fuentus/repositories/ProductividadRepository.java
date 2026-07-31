package ar.com.bbva.fuentus.repositories;

import ar.com.bbva.fuentus.entities.Productividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductividadRepository extends JpaRepository<Productividad, Long> {

    List<Productividad> findByFecha(LocalDate fecha);

    @Query("SELECT p FROM Productividad p WHERE p.nucleus.id = :nucleusId")
    List<Productividad> findByNucleusId(@Param("nucleusId") Long nucleusId);

    @Query("SELECT p FROM Productividad p WHERE p.nucleus.id = :nucleusId AND p.fecha = :fecha")
    List<Productividad> findByNucleusIdAndFecha(@Param("nucleusId") Long nucleusId, @Param("fecha") LocalDate fecha);
}
