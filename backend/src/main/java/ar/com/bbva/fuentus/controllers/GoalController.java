package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.GoalCategoryDTO;
import ar.com.bbva.fuentus.dto.GoalDTO;
import ar.com.bbva.fuentus.services.GoalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/goals")
@RestController
public class GoalController {
    
    private final GoalService goalService;
    
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }
    
    /**
     * Obtiene todos los objetivos activos
     * GET /goals
     */
    @GetMapping
    public ResponseEntity<List<GoalDTO>> getAllGoals() {
        try {
            List<GoalDTO> goals = goalService.getAllActiveGoals();
            return ResponseEntity.ok(goals);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Obtiene los objetivos agrupados por categoría
     * GET /goals/grouped
     */
    @GetMapping("/grouped")
    public ResponseEntity<List<GoalCategoryDTO>> getGoalsGroupedByCategory() {
        try {
            List<GoalCategoryDTO> groupedGoals = goalService.getGoalsGroupedByCategory();
            return ResponseEntity.ok(groupedGoals);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Obtiene los objetivos de una categoría específica
     * GET /goals/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<GoalDTO>> getGoalsByCategory(@PathVariable String category) {
        try {
            List<GoalDTO> goals = goalService.getGoalsByCategory(category);
            return ResponseEntity.ok(goals);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Obtiene todas las categorías distintas
     * GET /goals/categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getDistinctCategories() {
        try {
            List<String> categories = goalService.getDistinctCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Crea un nuevo objetivo
     * POST /goals
     */
    @PostMapping
    public ResponseEntity<GoalDTO> createGoal(@RequestBody GoalDTO goalDTO) {
        try {
            GoalDTO createdGoal = goalService.createGoal(goalDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGoal);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Actualiza un objetivo existente
     * PUT /goals/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<GoalDTO> updateGoal(@PathVariable Long id, @RequestBody GoalDTO goalDTO) {
        try {
            GoalDTO updatedGoal = goalService.updateGoal(id, goalDTO);
            return ResponseEntity.ok(updatedGoal);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Desactiva un objetivo (soft delete)
     * DELETE /goals/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateGoal(@PathVariable Long id) {
        try {
            goalService.deactivateGoal(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
