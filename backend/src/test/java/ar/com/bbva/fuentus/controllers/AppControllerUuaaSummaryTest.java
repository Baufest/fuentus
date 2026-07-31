package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.UuaaSummaryDTO;
import ar.com.bbva.fuentus.services.AppService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppController.class)
class AppControllerUuaaSummaryTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppService appService;

    private UuaaSummaryDTO mockSummary;

    @BeforeEach
    void setUp() {
        mockSummary = new UuaaSummaryDTO();
        mockSummary.setUuaa("TEST");
        mockSummary.setTotalApps(5);
        mockSummary.setAverageCoverage(75.5);
        mockSummary.setTotalBugs(10L);
        mockSummary.setTotalSastLow(5L);
        mockSummary.setTotalSastMedium(3L);
        mockSummary.setTotalSastHigh(1L);
        mockSummary.setTotalScaLow(4L);
        mockSummary.setTotalScaMedium(2L);
        mockSummary.setTotalScaHigh(1L);
        mockSummary.setTotalScaCritical(0L);
        mockSummary.setRfoId(123L);
        mockSummary.setRfoEstado("EN CURSO");
    }

    @Test
    void getUuaaSummary_ShouldReturnSummary() throws Exception {
        when(appService.getUuaaSummary(anyString())).thenReturn(mockSummary);

        mockMvc.perform(get("/apps/TEST/summary")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuaa").value("TEST"))
                .andExpect(jsonPath("$.totalApps").value(5))
                .andExpect(jsonPath("$.averageCoverage").value(75.5))
                .andExpect(jsonPath("$.totalBugs").value(10))
                .andExpect(jsonPath("$.totalSastVulnerabilities").value(9))
                .andExpect(jsonPath("$.totalScaVulnerabilities").value(7))
                .andExpect(jsonPath("$.totalVulnerabilities").value(16))
                .andExpect(jsonPath("$.rfoEstado").value("EN CURSO"));
    }

    @Test
    void getUuaaSummary_WithLowercaseUuaa_ShouldWork() throws Exception {
        when(appService.getUuaaSummary(anyString())).thenReturn(mockSummary);

        mockMvc.perform(get("/apps/test/summary")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuaa").value("TEST"));
    }
}
