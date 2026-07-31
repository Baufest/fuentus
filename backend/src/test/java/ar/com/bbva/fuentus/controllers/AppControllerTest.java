package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.AppSummaryDTO;
import ar.com.bbva.fuentus.services.AppService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppController.class)
class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppService appService;

    private AppSummaryDTO testAppSummary;
    private Page<AppSummaryDTO> testPage;

    @BeforeEach
    void setUp() {
        testAppSummary = new AppSummaryDTO(
            1L,
            "test-app",
            "TEST",
                "https://bitbucket.com/test-app",
                "https://sonar.com/test-app",
                "https://sonar10.com/test-app",
                5L,           // bugs
                85.5,         // coverage
                false,        // monolith
                3L,           // totalHigh
                2L,           // totalMedium
                1L,           // totalLow
                "Java",       // language
                "https://chimera.com/test-app",
                "https://samuel.com/test-app",
                1L,           // totalHighSca
                2L,           // totalMediumSca
                3L,           // totalLowSca
                0L            // totalCriticalSca
        );

        testPage = new PageImpl<>(Arrays.asList(testAppSummary), PageRequest.of(0, 20), 1);
    }

    @Test
    void getAppsByUUAAPageable_ShouldReturnAppsPage_WhenValidUUAAProvided() throws Exception {
        // Given
        String uuaa = "TEST";
        when(appService.getAppsByUUAAPageableWithSearch(uuaa, 0, null)).thenReturn(testPage);

        // When & Then
        mockMvc.perform(get("/apps/{uuaa}", uuaa))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("test-app"))
                .andExpect(jsonPath("$.content[0].language").value("Java"))
                .andExpect(jsonPath("$.content[0].coverage").value(85.5))
                .andExpect(jsonPath("$.content[0].bugs").value(5))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.numberOfElements").value(1));
    }

    @Test
    void getAppsByUUAAPageable_ShouldReturnAppsPageWithSearch_WhenSearchParamProvided() throws Exception {
        // Given
        String uuaa = "TEST";
        String search = "test-app";
        when(appService.getAppsByUUAAPageableWithSearch(uuaa, 0, search)).thenReturn(testPage);

        // When & Then
        mockMvc.perform(get("/apps/{uuaa}", uuaa)
                        .param("search", search))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("test-app"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getAppsByUUAAPageable_ShouldReturnAppsPageWithPageParam_WhenPageParamProvided() throws Exception {
        // Given
        String uuaa = "TEST";
        int page = 2;
        Page<AppSummaryDTO> pageTwo = new PageImpl<>(Arrays.asList(testAppSummary), PageRequest.of(page, 20), 50);
        when(appService.getAppsByUUAAPageableWithSearch(uuaa, page, null)).thenReturn(pageTwo);

        // When & Then
        mockMvc.perform(get("/apps/{uuaa}", uuaa)
                        .param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(41))
                .andExpect(jsonPath("$.number").value(page));
    }

    @Test
    void getAppsByUUAAPageable_ShouldReturnEmptyPage_WhenNoAppsFound() throws Exception {
        // Given
        String uuaa = "NONEXISTENT";
        Page<AppSummaryDTO> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 20), 0);
        when(appService.getAppsByUUAAPageableWithSearch(uuaa, 0, null)).thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(get("/apps/{uuaa}", uuaa))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.numberOfElements").value(0));
    }

    @Test
    void getAppsByUUAAPageable_ShouldHandleSpecialCharacters_InUUAAParam() throws Exception {
        // Given
        String uuaa = "TEST-123_ABC";
        when(appService.getAppsByUUAAPageableWithSearch(uuaa, 0, null)).thenReturn(testPage);

        // When & Then
        mockMvc.perform(get("/apps/{uuaa}", uuaa))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getAppsByUUAAPageable_ShouldHandleEmptySearchParam() throws Exception {
        // Given
        String uuaa = "TEST";
        String search = "";
        when(appService.getAppsByUUAAPageableWithSearch(uuaa, 0, search)).thenReturn(testPage);

        // When & Then
        mockMvc.perform(get("/apps/{uuaa}", uuaa)
                        .param("search", search))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getAppsByUUAAPageable_ShouldReturnAllFields_InResponseJSON() throws Exception {
        // Given
        String uuaa = "TEST";
        when(appService.getAppsByUUAAPageableWithSearch(uuaa, 0, null)).thenReturn(testPage);

        // When & Then
        mockMvc.perform(get("/apps/{uuaa}", uuaa))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content[0].name").value("test-app"))
                .andExpect(jsonPath("$.content[0].bitbucketUrl").value("https://bitbucket.com/test-app"))
                .andExpect(jsonPath("$.content[0].sonarUrl").value("https://sonar.com/test-app"))
                .andExpect(jsonPath("$.content[0].sonar10Url").value("https://sonar10.com/test-app"))
                .andExpect(jsonPath("$.content[0].chimeraUrl").value("https://chimera.com/test-app"))
                .andExpect(jsonPath("$.content[0].samuelUrl").value("https://samuel.com/test-app"))
                .andExpect(jsonPath("$.content[0].monolith").value(false))
                .andExpect(jsonPath("$.content[0].language").value("Java"));
    }
}