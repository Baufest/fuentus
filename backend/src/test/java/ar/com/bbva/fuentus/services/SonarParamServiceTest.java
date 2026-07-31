package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.SonarParamGetRequestDTO;
import ar.com.bbva.fuentus.entities.SonarParam;
import ar.com.bbva.fuentus.repositories.SonarParamsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SonarParamServiceTest {

    @Mock
    private SonarParamsRepository repository;

    @InjectMocks
    private SonarParamService sonarParamService;

    private SonarParam testSonarParam;
    private Date testAnalysisDate;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        testAnalysisDate = new Date();
        
        testSonarParam = new SonarParam();
        testSonarParam.setId(1L);
        testSonarParam.setAppId(100L);
        testSonarParam.setAnalisisDate(testAnalysisDate);
        testSonarParam.setCoverage(85.5);
        testSonarParam.setTotalLines(1500L);
        testSonarParam.setBugs(3L);
        testSonarParam.setBugsRating("A");
        testSonarParam.setVulnerabilities(1L);
        testSonarParam.setVulnerabilitiesRating("A");
        testSonarParam.setDebt("2h");
        testSonarParam.setDebtRating("A");
        testSonarParam.setCodeSmells("5");
        testSonarParam.setDuplications("2.5");
        testSonarParam.setBranch("main");
        testSonarParam.setSonarVersion(10);
    }

    @Test
    void getSonnarDataByAppId_ShouldReturnSonarParamDTO_WhenValidAppIdProvided() {
        // Given
        Long appId = 100L;
        when(repository.findLatestByAppId(appId)).thenReturn(testSonarParam);

        // When
        SonarParamGetRequestDTO result = sonarParamService.getSonnarDataByAppId(appId);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100L, result.getAppId());
        assertEquals(testAnalysisDate, result.getAnalisisDate());
        assertEquals(85.5, result.getCoverage());
        assertEquals(1500L, result.getTotalLines());
        assertEquals(3L, result.getBugs());

        verify(repository, times(1)).findLatestByAppId(appId);
    }

    @Test
    void getSonnarDataByAppId_ShouldReturnNull_WhenNoSonarDataFound() {
        // Given
        Long appId = 999L;
        when(repository.findLatestByAppId(appId)).thenReturn(null);

        // When
        SonarParamGetRequestDTO result = sonarParamService.getSonnarDataByAppId(appId);

        // Then
        assertNull(result);
        verify(repository, times(1)).findLatestByAppId(appId);
    }

    @Test
    void getSonnarDataByAppId_ShouldHandleZeroAppId() {
        // Given
        Long appId = 0L;
        when(repository.findLatestByAppId(appId)).thenReturn(null);

        // When
        SonarParamGetRequestDTO result = sonarParamService.getSonnarDataByAppId(appId);

        // Then
        assertNull(result);
        verify(repository, times(1)).findLatestByAppId(appId);
    }

    @Test
    void getSonnarDataByAppId_ShouldHandleNegativeAppId() {
        // Given
        Long appId = -1L;
        when(repository.findLatestByAppId(appId)).thenReturn(null);

        // When
        SonarParamGetRequestDTO result = sonarParamService.getSonnarDataByAppId(appId);

        // Then
        assertNull(result);
        verify(repository, times(1)).findLatestByAppId(appId);
    }

    @Test
    void getSonnarDataByAppId_ShouldMapCorrectly_WhenSonarParamHasNullValues() {
        // Given
        Long appId = 200L;
        SonarParam sonarParamWithNulls = new SonarParam();
        sonarParamWithNulls.setId(2L);
        sonarParamWithNulls.setAppId(200L);
        sonarParamWithNulls.setAnalisisDate(testAnalysisDate);
        sonarParamWithNulls.setCoverage(null);
        sonarParamWithNulls.setTotalLines(null);
        sonarParamWithNulls.setBugs(null);

        when(repository.findLatestByAppId(appId)).thenReturn(sonarParamWithNulls);

        // When
        SonarParamGetRequestDTO result = sonarParamService.getSonnarDataByAppId(appId);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals(200L, result.getAppId());
        assertEquals(testAnalysisDate, result.getAnalisisDate());
        assertNull(result.getCoverage());
        assertNull(result.getTotalLines());
        assertNull(result.getBugs());

        verify(repository, times(1)).findLatestByAppId(appId);
    }

    @Test
    void getSonnarDataByAppId_ShouldHandleMinimalSonarParam() {
        // Given
        Long appId = 300L;
        SonarParam minimalSonarParam = new SonarParam();
        minimalSonarParam.setId(3L);
        minimalSonarParam.setAppId(300L);
        minimalSonarParam.setAnalisisDate(testAnalysisDate);

        when(repository.findLatestByAppId(appId)).thenReturn(minimalSonarParam);

        // When
        SonarParamGetRequestDTO result = sonarParamService.getSonnarDataByAppId(appId);

        // Then
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals(300L, result.getAppId());
        assertEquals(testAnalysisDate, result.getAnalisisDate());
        assertNull(result.getCoverage());
        assertNull(result.getTotalLines());
        assertNull(result.getBugs());

        verify(repository, times(1)).findLatestByAppId(appId);
    }

    @Test
    void getSonnarDataByAppId_ShouldHandleZeroCoverage() {
        // Given
        Long appId = 400L;
        SonarParam sonarParamZeroCoverage = new SonarParam();
        sonarParamZeroCoverage.setId(4L);
        sonarParamZeroCoverage.setAppId(400L);
        sonarParamZeroCoverage.setAnalisisDate(testAnalysisDate);
        sonarParamZeroCoverage.setCoverage(0.0);
        sonarParamZeroCoverage.setTotalLines(100L);
        sonarParamZeroCoverage.setBugs(0L);

        when(repository.findLatestByAppId(appId)).thenReturn(sonarParamZeroCoverage);

        // When
        SonarParamGetRequestDTO result = sonarParamService.getSonnarDataByAppId(appId);

        // Then
        assertNotNull(result);
        assertEquals(4L, result.getId());
        assertEquals(400L, result.getAppId());
        assertEquals(0.0, result.getCoverage());
        assertEquals(100L, result.getTotalLines());
        assertEquals(0L, result.getBugs());

        verify(repository, times(1)).findLatestByAppId(appId);
    }
}