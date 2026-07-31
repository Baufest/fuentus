package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.GoalCategoryDTO;
import ar.com.bbva.fuentus.dto.GoalDTO;
import ar.com.bbva.fuentus.entities.Goal;
import ar.com.bbva.fuentus.repositories.GoalRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoalService {
    
    private final GoalRepository goalRepository;
    
    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }
    
    /**
     * Obtiene todos los objetivos activos
     */
    public List<GoalDTO> getAllActiveGoals() {
        return goalRepository.findAllActiveOrderedByDisplayOrder()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene los objetivos agrupados por categoría
     */
    public List<GoalCategoryDTO> getGoalsGroupedByCategory() {
        List<Goal> goals = goalRepository.findAllActiveOrderedByDisplayOrder();
        
        // Agrupar por categoría manteniendo el orden
        Map<String, List<GoalDTO>> groupedGoals = new LinkedHashMap<>();
        
        for (Goal goal : goals) {
            String category = goal.getCategory();
            GoalDTO dto = mapToDTO(goal);
            
            groupedGoals.computeIfAbsent(category, k -> new ArrayList<>()).add(dto);
        }
        
        // Convertir a lista de GoalCategoryDTO
        return groupedGoals.entrySet().stream()
                .map(entry -> GoalCategoryDTO.builder()
                        .category(entry.getKey())
                        .goals(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene los objetivos de una categoría específica
     */
    public List<GoalDTO> getGoalsByCategory(String category) {
        return goalRepository.findByCategoryAndIsActiveTrueOrderByDisplayOrder(category)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todas las categorías distintas
     */
    public List<String> getDistinctCategories() {
        return goalRepository.findDistinctCategories();
    }
    
    /**
     * Crea un nuevo objetivo
     */
    public GoalDTO createGoal(GoalDTO goalDTO) {
        Goal goal = new Goal();
        goal.setCategory(goalDTO.getCategory());
        goal.setName(goalDTO.getName());
        goal.setDescription(goalDTO.getDescription());
        goal.setNumericValue(goalDTO.getNumericValue());
        goal.setCategoricalValue(goalDTO.getCategoricalValue());
        goal.setUnit(goalDTO.getUnit());
        goal.setDisplayOrder(goalDTO.getDisplayOrder());
        goal.setIsActive(true);
        
        Goal savedGoal = goalRepository.save(goal);
        return mapToDTO(savedGoal);
    }
    
    /**
     * Actualiza un objetivo existente
     */
    public GoalDTO updateGoal(Long id, GoalDTO goalDTO) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found with id: " + id));
        
        goal.setCategory(goalDTO.getCategory());
        goal.setName(goalDTO.getName());
        goal.setDescription(goalDTO.getDescription());
        goal.setNumericValue(goalDTO.getNumericValue());
        goal.setCategoricalValue(goalDTO.getCategoricalValue());
        goal.setUnit(goalDTO.getUnit());
        goal.setDisplayOrder(goalDTO.getDisplayOrder());
        
        Goal updatedGoal = goalRepository.save(goal);
        return mapToDTO(updatedGoal);
    }
    
    /**
     * Desactiva un objetivo (soft delete)
     */
    public void deactivateGoal(Long id) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found with id: " + id));
        goal.setIsActive(false);
        goalRepository.save(goal);
    }
    
    private GoalDTO mapToDTO(Goal goal) {
        return GoalDTO.builder()
                .id(goal.getId())
                .category(goal.getCategory())
                .name(goal.getName())
                .description(goal.getDescription())
                .numericValue(goal.getNumericValue())
                .categoricalValue(goal.getCategoricalValue())
                .unit(goal.getUnit())
                .displayOrder(goal.getDisplayOrder())
                .build();
    }
}
