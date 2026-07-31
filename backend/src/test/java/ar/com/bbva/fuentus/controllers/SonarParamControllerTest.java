package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.SonarParamGetRequestDTO;
import ar.com.bbva.fuentus.services.SonarParamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SonarParamController.class)
class SonarParamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SonarParamService sonarParamService;

    private SonarParamGetRequestDTO testSonarParam;

    @BeforeEach
    void setUp() {
        testSonarParam = new SonarParamGetRequestDTO();
        testSonarParam.setId(1L);
        testSonarParam.setAppId(100L);
        testSonarParam.setAnalisisDate(new Date());
        testSonarParam.setCoverage(85.5);
        testSonarParam.setTotalLines(1000L);
        testSonarParam.setBugs(5L);
    }

    @Test
    void getSonarDataByAppId_ShouldReturnSonarData_WhenValidAppIdProvided() throws Exception {
        // Given
        Long appId = 100L;
        when(sonarParamService.getSonnarDataByAppId(eq(appId))).thenReturn(testSonarParam);

        // When & Then
        mockMvc.perform(get("/sonar/{appId}", appId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.appId").value(100))
                .andExpect(jsonPath("$.coverage").value(85.5))
                .andExpect(jsonPath("$.totalLines").value(1000))
                .andExpect(jsonPath("$.bugs").value(5))
                .andExpect(jsonPath("$.analisisDate").exists());
    }

    @Test
    void getSonarDataByAppId_ShouldReturnNull_WhenAppIdNotFound() throws Exception {
        // Given
        Long appId = 999L;
        when(sonarParamService.getSonnarDataByAppId(eq(appId))).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/sonar/{appId}", appId))
                .andExpect(status().isOk());
    }

    @Test
    void getSonarDataByAppId_ShouldHandleZeroCoverage_WhenAppHasNoCoverage() throws Exception {
        // Given
        Long appId = 200L;
        SonarParamGetRequestDTO noCoverageParam = new SonarParamGetRequestDTO();
        noCoverageParam.setId(2L);
        noCoverageParam.setAppId(200L);
        noCoverageParam.setAnalisisDate(new Date());
        noCoverageParam.setCoverage(0.0);
        noCoverageParam.setTotalLines(500L);
        noCoverageParam.setBugs(0L);

        when(sonarParamService.getSonnarDataByAppId(eq(appId))).thenReturn(noCoverageParam);

        // When & Then
        mockMvc.perform(get("/sonar/{appId}", appId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.appId").value(200))
                .andExpect(jsonPath("$.coverage").value(0.0))
                .andExpect(jsonPath("$.totalLines").value(500))
                .andExpect(jsonPath("$.bugs").value(0));
    }

    @Test
    void getSonarDataByAppId_ShouldHandleHighBugCount_WhenAppHasManyBugs() throws Exception {
        // Given
        Long appId = 300L;
        SonarParamGetRequestDTO highBugsParam = new SonarParamGetRequestDTO();
        highBugsParam.setId(3L);
        highBugsParam.setAppId(300L);
        highBugsParam.setAnalisisDate(new Date());
        highBugsParam.setCoverage(45.0);
        highBugsParam.setTotalLines(2000L);
        highBugsParam.setBugs(150L);

        when(sonarParamService.getSonnarDataByAppId(eq(appId))).thenReturn(highBugsParam);

        // When & Then
        mockMvc.perform(get("/sonar/{appId}", appId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.appId").value(300))
                .andExpect(jsonPath("$.coverage").value(45.0))
                .andExpect(jsonPath("$.totalLines").value(2000))
                .andExpect(jsonPath("$.bugs").value(150));
    }
}
