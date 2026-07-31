package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.GoalCategoryDTO;
import ar.com.bbva.fuentus.dto.GoalDTO;
import ar.com.bbva.fuentus.services.GoalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GoalController.class)
class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GoalService goalService;

    private GoalDTO testGoal1;
    private GoalDTO testGoal2;
    private GoalCategoryDTO testCategory;
    private List<GoalDTO> goalList;
    private List<String> categories;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        testGoal1 = new GoalDTO();
        testGoal1.setId(1L);
        testGoal1.setCategory("Performance");
        testGoal1.setName("Response Time");
        testGoal1.setDescription("Average response time");
        testGoal1.setNumericValue(200.0);
        testGoal1.setUnit("ms");
        testGoal1.setDisplayOrder(1);

        testGoal2 = new GoalDTO();
        testGoal2.setId(2L);
        testGoal2.setCategory("Quality");
        testGoal2.setName("Code Coverage");
        testGoal2.setDescription("Test coverage percentage");
        testGoal2.setNumericValue(80.0);
        testGoal2.setUnit("%");
        testGoal2.setDisplayOrder(2);

        goalList = Arrays.asList(testGoal1, testGoal2);

        testCategory = new GoalCategoryDTO();
        testCategory.setCategory("Performance");
        testCategory.setGoals(Collections.singletonList(testGoal1));

        categories = Arrays.asList("Performance", "Quality", "Security");
    }

    @Test
    void getAllGoals_ShouldReturnAllActiveGoals() throws Exception {
        when(goalService.getAllActiveGoals()).thenReturn(goalList);

        mockMvc.perform(get("/goals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].category").value("Performance"))
                .andExpect(jsonPath("$[0].name").value("Response Time"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].category").value("Quality"));

        verify(goalService).getAllActiveGoals();
    }

    @Test
    void getAllGoals_ShouldReturnEmptyList_WhenNoGoals() throws Exception {
        when(goalService.getAllActiveGoals()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/goals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(goalService).getAllActiveGoals();
    }

    @Test
    void getAllGoals_ShouldReturnInternalServerError_WhenExceptionOccurs() throws Exception {
        when(goalService.getAllActiveGoals()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/goals"))
                .andExpect(status().isInternalServerError());

        verify(goalService).getAllActiveGoals();
    }

    @Test
    void getGoalsGroupedByCategory_ShouldReturnGroupedGoals() throws Exception {
        when(goalService.getGoalsGroupedByCategory()).thenReturn(Collections.singletonList(testCategory));

        mockMvc.perform(get("/goals/grouped"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Performance"))
                .andExpect(jsonPath("$[0].goals[0].name").value("Response Time"));

        verify(goalService).getGoalsGroupedByCategory();
    }

    @Test
    void getGoalsGroupedByCategory_ShouldReturnInternalServerError_WhenExceptionOccurs() throws Exception {
        when(goalService.getGoalsGroupedByCategory()).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/goals/grouped"))
                .andExpect(status().isInternalServerError());

        verify(goalService).getGoalsGroupedByCategory();
    }

    @Test
    void getGoalsByCategory_ShouldReturnGoalsForCategory() throws Exception {
        when(goalService.getGoalsByCategory("Performance")).thenReturn(Collections.singletonList(testGoal1));

        mockMvc.perform(get("/goals/category/Performance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Performance"))
                .andExpect(jsonPath("$[0].name").value("Response Time"));

        verify(goalService).getGoalsByCategory("Performance");
    }

    @Test
    void getGoalsByCategory_ShouldReturnEmptyList_WhenNoCategoryMatch() throws Exception {
        when(goalService.getGoalsByCategory("Unknown")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/goals/category/Unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(goalService).getGoalsByCategory("Unknown");
    }

    @Test
    void getGoalsByCategory_ShouldReturnInternalServerError_WhenExceptionOccurs() throws Exception {
        when(goalService.getGoalsByCategory(anyString())).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/goals/category/Performance"))
                .andExpect(status().isInternalServerError());

        verify(goalService).getGoalsByCategory("Performance");
    }

    @Test
    void getDistinctCategories_ShouldReturnAllCategories() throws Exception {
        when(goalService.getDistinctCategories()).thenReturn(categories);

        mockMvc.perform(get("/goals/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Performance"))
                .andExpect(jsonPath("$[1]").value("Quality"))
                .andExpect(jsonPath("$[2]").value("Security"));

        verify(goalService).getDistinctCategories();
    }

    @Test
    void getDistinctCategories_ShouldReturnInternalServerError_WhenExceptionOccurs() throws Exception {
        when(goalService.getDistinctCategories()).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/goals/categories"))
                .andExpect(status().isInternalServerError());

        verify(goalService).getDistinctCategories();
    }

    @Test
    void createGoal_ShouldReturnCreatedGoal() throws Exception {
        GoalDTO newGoal = new GoalDTO();
        newGoal.setCategory("Security");
        newGoal.setName("Vulnerabilities");
        newGoal.setNumericValue(0.0);

        GoalDTO createdGoal = new GoalDTO();
        createdGoal.setId(3L);
        createdGoal.setCategory("Security");
        createdGoal.setName("Vulnerabilities");
        createdGoal.setNumericValue(0.0);

        when(goalService.createGoal(any(GoalDTO.class))).thenReturn(createdGoal);

        mockMvc.perform(post("/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGoal)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.category").value("Security"))
                .andExpect(jsonPath("$.name").value("Vulnerabilities"));

        verify(goalService).createGoal(any(GoalDTO.class));
    }

    @Test
    void createGoal_ShouldReturnBadRequest_WhenIllegalArgumentException() throws Exception {
        GoalDTO newGoal = new GoalDTO();
        newGoal.setCategory("Invalid");

        when(goalService.createGoal(any(GoalDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        mockMvc.perform(post("/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGoal)))
                .andExpect(status().isBadRequest());

        verify(goalService).createGoal(any(GoalDTO.class));
    }

    @Test
    void createGoal_ShouldReturnInternalServerError_WhenExceptionOccurs() throws Exception {
        GoalDTO newGoal = new GoalDTO();
        newGoal.setCategory("Test");

        when(goalService.createGoal(any(GoalDTO.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGoal)))
                .andExpect(status().isInternalServerError());

        verify(goalService).createGoal(any(GoalDTO.class));
    }

    @Test
    void updateGoal_ShouldReturnUpdatedGoal() throws Exception {
        GoalDTO updatedGoal = new GoalDTO();
        updatedGoal.setId(1L);
        updatedGoal.setCategory("Performance");
        updatedGoal.setName("Response Time");
        updatedGoal.setNumericValue(150.0);

        when(goalService.updateGoal(eq(1L), any(GoalDTO.class))).thenReturn(updatedGoal);

        mockMvc.perform(put("/goals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedGoal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numericValue").value(150.0));

        verify(goalService).updateGoal(eq(1L), any(GoalDTO.class));
    }

    @Test
    void updateGoal_ShouldReturnNotFound_WhenGoalDoesNotExist() throws Exception {
        GoalDTO updatedGoal = new GoalDTO();
        updatedGoal.setId(999L);

        when(goalService.updateGoal(eq(999L), any(GoalDTO.class)))
                .thenThrow(new IllegalArgumentException("Goal not found"));

        mockMvc.perform(put("/goals/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedGoal)))
                .andExpect(status().isNotFound());

        verify(goalService).updateGoal(eq(999L), any(GoalDTO.class));
    }

    @Test
    void updateGoal_ShouldReturnInternalServerError_WhenExceptionOccurs() throws Exception {
        GoalDTO updatedGoal = new GoalDTO();
        updatedGoal.setId(1L);

        when(goalService.updateGoal(eq(1L), any(GoalDTO.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(put("/goals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedGoal)))
                .andExpect(status().isInternalServerError());

        verify(goalService).updateGoal(eq(1L), any(GoalDTO.class));
    }

    @Test
    void deactivateGoal_ShouldReturnNoContent() throws Exception {
        doNothing().when(goalService).deactivateGoal(1L);

        mockMvc.perform(delete("/goals/1"))
                .andExpect(status().isNoContent());

        verify(goalService).deactivateGoal(1L);
    }

    @Test
    void deactivateGoal_ShouldReturnNotFound_WhenGoalDoesNotExist() throws Exception {
        doThrow(new IllegalArgumentException("Goal not found"))
                .when(goalService).deactivateGoal(999L);

        mockMvc.perform(delete("/goals/999"))
                .andExpect(status().isNotFound());

        verify(goalService).deactivateGoal(999L);
    }

    @Test
    void deactivateGoal_ShouldReturnInternalServerError_WhenExceptionOccurs() throws Exception {
        doThrow(new RuntimeException("Database error"))
                .when(goalService).deactivateGoal(1L);

        mockMvc.perform(delete("/goals/1"))
                .andExpect(status().isInternalServerError());

        verify(goalService).deactivateGoal(1L);
    }
}
