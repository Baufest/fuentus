package ar.com.bbva.fuentus.repositories;

import ar.com.bbva.fuentus.entities.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    
    /**
     * Encuentra todos los objetivos activos ordenados por orden de visualización y categoría
     */
    @Query("SELECT g FROM Goal g WHERE g.isActive = true ORDER BY g.displayOrder, g.category, g.name")
    List<Goal> findAllActiveOrderedByDisplayOrder();
    
    /**
     * Encuentra todos los objetivos por categoría
     */
    List<Goal> findByCategoryAndIsActiveTrueOrderByDisplayOrder(String category);
    
    /**
     * Obtiene todas las categorías distintas
     */
    @Query("SELECT DISTINCT g.category FROM Goal g WHERE g.isActive = true ORDER BY g.category")
    List<String> findDistinctCategories();
}
