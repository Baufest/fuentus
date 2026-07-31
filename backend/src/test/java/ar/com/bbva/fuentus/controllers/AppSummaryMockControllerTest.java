package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.AppSummaryDTO;
import ar.com.bbva.fuentus.repositories.AppsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppSummaryMockController.class)
class AppSummaryMockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppsRepository appsRepository;

    private AppSummaryDTO testAppSummary1;
    private AppSummaryDTO testAppSummary2;
    private List<AppSummaryDTO> testAppList;

    @BeforeEach
    void setUp() {
        testAppSummary1 = new AppSummaryDTO(
            1L,
            "test-app-1",
            "TEST",
                "https://bitbucket.com/test-app-1",
                "https://sonar.com/test-app-1",
                "https://sonar10.com/test-app-1",
                5L,           // bugs
                85.5,         // coverage
                false,        // monolith
                3L,           // totalHigh
                2L,           // totalMedium
                1L,           // totalLow
                "Java",       // language
                "https://chimera.com/test-app-1",
                "https://samuel.com/test-app-1",
                1L,           // totalHighSca
                2L,           // totalMediumSca
                3L,           // totalLowSca
                0L            // totalCriticalSca
        );

        testAppSummary2 = new AppSummaryDTO(
            2L,
            "test-app-2",
            "TEST",
                "https://bitbucket.com/test-app-2",
                "https://sonar.com/test-app-2",
                "https://sonar10.com/test-app-2",
                10L,          // bugs
                90.0,         // coverage
                true,         // monolith
                5L,           // totalHigh
                3L,           // totalMedium
                2L,           // totalLow
                "Python",     // language
                "https://chimera.com/test-app-2",
                "https://samuel.com/test-app-2",
                2L,           // totalHighSca
                3L,           // totalMediumSca
                4L,           // totalLowSca
                1L            // totalCriticalSca
        );

        testAppList = Arrays.asList(testAppSummary1, testAppSummary2);
    }

    @Test
    void getConsultas_ShouldReturnAppSummaryList_WhenValidUUAAProvided() throws Exception {
        // Given
        String uuaa = "TEST";
        when(appsRepository.findAppSummaryByUuaa(uuaa)).thenReturn(testAppList);

        // When & Then
        mockMvc.perform(get("/pruebaApps/{uuaa}", uuaa))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("test-app-1"))
                .andExpect(jsonPath("$[0].language").value("Java"))
                .andExpect(jsonPath("$[0].coverage").value(85.5))
                .andExpect(jsonPath("$[0].bugs").value(5))
                .andExpect(jsonPath("$[1].name").value("test-app-2"))
                .andExpect(jsonPath("$[1].language").value("Python"))
                .andExpect(jsonPath("$[1].coverage").value(90.0))
                .andExpect(jsonPath("$[1].bugs").value(10));
    }

    @Test
    void getConsultas_ShouldReturnEmptyList_WhenUUAANotFound() throws Exception {
        // Given
        String uuaa = "NOTFOUND";
        when(appsRepository.findAppSummaryByUuaa(uuaa)).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/pruebaApps/{uuaa}", uuaa))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getConsultas_ShouldReturnSingleApp_WhenUUAAHasOneApp() throws Exception {
        // Given
        String uuaa = "SINGLE";
        List<AppSummaryDTO> singleAppList = Arrays.asList(testAppSummary1);
        when(appsRepository.findAppSummaryByUuaa(uuaa)).thenReturn(singleAppList);

        // When & Then
        mockMvc.perform(get("/pruebaApps/{uuaa}", uuaa))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("test-app-1"))
                .andExpect(jsonPath("$[0].monolith").value(false));
    }
}
