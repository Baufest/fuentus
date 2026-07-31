package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.GoalCategoryDTO;
import ar.com.bbva.fuentus.dto.GoalDTO;
import ar.com.bbva.fuentus.entities.Goal;
import ar.com.bbva.fuentus.repositories.GoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private GoalService goalService;

    private Goal testGoal1;
    private Goal testGoal2;
    private Goal testGoal3;
    private List<Goal> testGoalList;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        testGoal1 = new Goal();
        testGoal1.setId(1L);
        testGoal1.setCategory("Performance");
        testGoal1.setName("Response Time");
        testGoal1.setDescription("Average response time");
        testGoal1.setNumericValue(200.0);
        testGoal1.setCategoricalValue(null);
        testGoal1.setUnit("ms");
        testGoal1.setDisplayOrder(1);
        testGoal1.setIsActive(true);

        testGoal2 = new Goal();
        testGoal2.setId(2L);
        testGoal2.setCategory("Performance");
        testGoal2.setName("Throughput");
        testGoal2.setDescription("Requests per second");
        testGoal2.setNumericValue(1000.0);
        testGoal2.setCategoricalValue(null);
        testGoal2.setUnit("req/s");
        testGoal2.setDisplayOrder(2);
        testGoal2.setIsActive(true);

        testGoal3 = new Goal();
        testGoal3.setId(3L);
        testGoal3.setCategory("Quality");
        testGoal3.setName("Code Coverage");
        testGoal3.setDescription("Unit test coverage");
        testGoal3.setNumericValue(80.0);
        testGoal3.setCategoricalValue(null);
        testGoal3.setUnit("%");
        testGoal3.setDisplayOrder(1);
        testGoal3.setIsActive(true);

        testGoalList = Arrays.asList(testGoal1, testGoal2, testGoal3);
    }

    @Test
    void getAllActiveGoals_ShouldReturnListOfGoalDTO_WhenGoalsExist() {
        // Given
        when(goalRepository.findAllActiveOrderedByDisplayOrder()).thenReturn(testGoalList);

        // When
        List<GoalDTO> result = goalService.getAllActiveGoals();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        
        // Verificar primer elemento
        GoalDTO firstDto = result.get(0);
        assertEquals(1L, firstDto.getId());
        assertEquals("Performance", firstDto.getCategory());
        assertEquals("Response Time", firstDto.getName());
        assertEquals("Average response time", firstDto.getDescription());
        assertEquals(200.0, firstDto.getNumericValue());
        assertEquals("ms", firstDto.getUnit());
        assertEquals(1, firstDto.getDisplayOrder());

        verify(goalRepository, times(1)).findAllActiveOrderedByDisplayOrder();
    }

    @Test
    void getAllActiveGoals_ShouldReturnEmptyList_WhenNoGoalsExist() {
        // Given
        when(goalRepository.findAllActiveOrderedByDisplayOrder()).thenReturn(Collections.emptyList());

        // When
        List<GoalDTO> result = goalService.getAllActiveGoals();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(goalRepository, times(1)).findAllActiveOrderedByDisplayOrder();
    }

    @Test
    void getGoalsGroupedByCategory_ShouldReturnGroupedGoals_WhenGoalsExist() {
        // Given
        when(goalRepository.findAllActiveOrderedByDisplayOrder()).thenReturn(testGoalList);

        // When
        List<GoalCategoryDTO> result = goalService.getGoalsGroupedByCategory();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Verificar categoría Performance
        GoalCategoryDTO performanceCategory = result.get(0);
        assertEquals("Performance", performanceCategory.getCategory());
        assertEquals(2, performanceCategory.getGoals().size());
        assertEquals("Response Time", performanceCategory.getGoals().get(0).getName());
        assertEquals("Throughput", performanceCategory.getGoals().get(1).getName());
        
        // Verificar categoría Quality
        GoalCategoryDTO qualityCategory = result.get(1);
        assertEquals("Quality", qualityCategory.getCategory());
        assertEquals(1, qualityCategory.getGoals().size());
        assertEquals("Code Coverage", qualityCategory.getGoals().get(0).getName());

        verify(goalRepository, times(1)).findAllActiveOrderedByDisplayOrder();
    }

    @Test
    void getGoalsGroupedByCategory_ShouldReturnEmptyList_WhenNoGoalsExist() {
        // Given
        when(goalRepository.findAllActiveOrderedByDisplayOrder()).thenReturn(Collections.emptyList());

        // When
        List<GoalCategoryDTO> result = goalService.getGoalsGroupedByCategory();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(goalRepository, times(1)).findAllActiveOrderedByDisplayOrder();
    }

    @Test
    void getGoalsByCategory_ShouldReturnFilteredGoals_WhenCategoryExists() {
        // Given
        String category = "Performance";
        List<Goal> performanceGoals = Arrays.asList(testGoal1, testGoal2);
        when(goalRepository.findByCategoryAndIsActiveTrueOrderByDisplayOrder(category))
                .thenReturn(performanceGoals);

        // When
        List<GoalDTO> result = goalService.getGoalsByCategory(category);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Response Time", result.get(0).getName());
        assertEquals("Throughput", result.get(1).getName());
        
        verify(goalRepository, times(1))
                .findByCategoryAndIsActiveTrueOrderByDisplayOrder(category);
    }

    @Test
    void getGoalsByCategory_ShouldReturnEmptyList_WhenCategoryHasNoGoals() {
        // Given
        String category = "NonExistent";
        when(goalRepository.findByCategoryAndIsActiveTrueOrderByDisplayOrder(category))
                .thenReturn(Collections.emptyList());

        // When
        List<GoalDTO> result = goalService.getGoalsByCategory(category);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(goalRepository, times(1))
                .findByCategoryAndIsActiveTrueOrderByDisplayOrder(category);
    }

    @Test
    void getDistinctCategories_ShouldReturnListOfCategories_WhenCategoriesExist() {
        // Given
        List<String> categories = Arrays.asList("Performance", "Quality", "Security");
        when(goalRepository.findDistinctCategories()).thenReturn(categories);

        // When
        List<String> result = goalService.getDistinctCategories();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("Performance"));
        assertTrue(result.contains("Quality"));
        assertTrue(result.contains("Security"));
        
        verify(goalRepository, times(1)).findDistinctCategories();
    }

    @Test
    void getDistinctCategories_ShouldReturnEmptyList_WhenNoCategoriesExist() {
        // Given
        when(goalRepository.findDistinctCategories()).thenReturn(Collections.emptyList());

        // When
        List<String> result = goalService.getDistinctCategories();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(goalRepository, times(1)).findDistinctCategories();
    }

    @Test
    void createGoal_ShouldReturnCreatedGoalDTO_WhenValidGoalProvided() {
        // Given
        GoalDTO inputDto = GoalDTO.builder()
                .category("Reliability")
                .name("Uptime")
                .description("System uptime")
                .numericValue(99.9)
                .categoricalValue(null)
                .unit("%")
                .displayOrder(1)
                .build();

        Goal savedGoal = new Goal();
        savedGoal.setId(4L);
        savedGoal.setCategory(inputDto.getCategory());
        savedGoal.setName(inputDto.getName());
        savedGoal.setDescription(inputDto.getDescription());
        savedGoal.setNumericValue(inputDto.getNumericValue());
        savedGoal.setCategoricalValue(inputDto.getCategoricalValue());
        savedGoal.setUnit(inputDto.getUnit());
        savedGoal.setDisplayOrder(inputDto.getDisplayOrder());
        savedGoal.setIsActive(true);

        when(goalRepository.save(any(Goal.class))).thenReturn(savedGoal);

        // When
        GoalDTO result = goalService.createGoal(inputDto);

        // Then
        assertNotNull(result);
        assertEquals(4L, result.getId());
        assertEquals("Reliability", result.getCategory());
        assertEquals("Uptime", result.getName());
        assertEquals("System uptime", result.getDescription());
        assertEquals(99.9, result.getNumericValue());
        assertEquals("%", result.getUnit());
        assertEquals(1, result.getDisplayOrder());
        
        verify(goalRepository, times(1)).save(any(Goal.class));
    }

    @Test
    void updateGoal_ShouldReturnUpdatedGoalDTO_WhenGoalExists() {
        // Given
        Long goalId = 1L;
        GoalDTO updateDto = GoalDTO.builder()
                .category("Performance")
                .name("Updated Response Time")
                .description("Updated description")
                .numericValue(150.0)
                .categoricalValue(null)
                .unit("ms")
                .displayOrder(1)
                .build();

        Goal existingGoal = new Goal();
        existingGoal.setId(goalId);
        existingGoal.setCategory("Performance");
        existingGoal.setName("Response Time");
        existingGoal.setDescription("Average response time");
        existingGoal.setNumericValue(200.0);
        existingGoal.setUnit("ms");
        existingGoal.setDisplayOrder(1);
        existingGoal.setIsActive(true);

        Goal updatedGoal = new Goal();
        updatedGoal.setId(goalId);
        updatedGoal.setCategory(updateDto.getCategory());
        updatedGoal.setName(updateDto.getName());
        updatedGoal.setDescription(updateDto.getDescription());
        updatedGoal.setNumericValue(updateDto.getNumericValue());
        updatedGoal.setUnit(updateDto.getUnit());
        updatedGoal.setDisplayOrder(updateDto.getDisplayOrder());
        updatedGoal.setIsActive(true);

        when(goalRepository.findById(goalId)).thenReturn(Optional.of(existingGoal));
        when(goalRepository.save(any(Goal.class))).thenReturn(updatedGoal);

        // When
        GoalDTO result = goalService.updateGoal(goalId, updateDto);

        // Then
        assertNotNull(result);
        assertEquals(goalId, result.getId());
        assertEquals("Updated Response Time", result.getName());
        assertEquals("Updated description", result.getDescription());
        assertEquals(150.0, result.getNumericValue());
        
        verify(goalRepository, times(1)).findById(goalId);
        verify(goalRepository, times(1)).save(any(Goal.class));
    }

    @Test
    void updateGoal_ShouldThrowException_WhenGoalNotFound() {
        // Given
        Long goalId = 999L;
        GoalDTO updateDto = GoalDTO.builder()
                .category("Performance")
                .name("Updated Goal")
                .build();

        when(goalRepository.findById(goalId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> goalService.updateGoal(goalId, updateDto)
        );
        
        assertEquals("Goal not found with id: 999", exception.getMessage());
        verify(goalRepository, times(1)).findById(goalId);
        verify(goalRepository, never()).save(any(Goal.class));
    }

    @Test
    void deactivateGoal_ShouldSetIsActiveFalse_WhenGoalExists() {
        // Given
        Long goalId = 1L;
        Goal goal = new Goal();
        goal.setId(goalId);
        goal.setCategory("Performance");
        goal.setName("Response Time");
        goal.setIsActive(true);

        when(goalRepository.findById(goalId)).thenReturn(Optional.of(goal));
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        // When
        goalService.deactivateGoal(goalId);

        // Then
        assertFalse(goal.getIsActive());
        verify(goalRepository, times(1)).findById(goalId);
        verify(goalRepository, times(1)).save(goal);
    }

    @Test
    void deactivateGoal_ShouldThrowException_WhenGoalNotFound() {
        // Given
        Long goalId = 999L;
        when(goalRepository.findById(goalId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> goalService.deactivateGoal(goalId)
        );
        
        assertEquals("Goal not found with id: 999", exception.getMessage());
        verify(goalRepository, times(1)).findById(goalId);
        verify(goalRepository, never()).save(any(Goal.class));
    }

    @Test
    void createGoal_ShouldSetIsActiveTrue_ByDefault() {
        // Given
        GoalDTO inputDto = GoalDTO.builder()
                .category("Security")
                .name("Vulnerabilities")
                .description("Critical vulnerabilities")
                .numericValue(0.0)
                .unit("count")
                .displayOrder(1)
                .build();

        when(goalRepository.save(any(Goal.class))).thenAnswer(invocation -> {
            Goal savedGoal = invocation.getArgument(0);
            savedGoal.setId(5L);
            return savedGoal;
        });

        // When
        GoalDTO result = goalService.createGoal(inputDto);

        // Then
        verify(goalRepository, times(1)).save(argThat(goal -> 
            goal.getIsActive() != null && goal.getIsActive()
        ));
    }
}
