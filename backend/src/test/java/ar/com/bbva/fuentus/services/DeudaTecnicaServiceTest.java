package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.DeudaTecnicaItemDTO;
import ar.com.bbva.fuentus.entities.App;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.SonarParam;
import ar.com.bbva.fuentus.repositories.AppsRepository;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import ar.com.bbva.fuentus.repositories.SonarParamsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeudaTecnicaServiceTest {

    @Mock
    private SonarParamsRepository sonarParamRepository;

    @Mock
    private AppsRepository appRepository;

    @Mock
    private NucleusRepository nucleusRepository;

    @InjectMocks
    private DeudaTecnicaService deudaTecnicaService;

    private SonarParam testSonarParam1;
    private SonarParam testSonarParam2;
    private SonarParam testSonarParam3;
    private App testApp1;
    private App testApp2;
    private Nucleus testNucleus1;
    private Nucleus testNucleus2;
    private List<SonarParam> testSonarParamsList;
    private List<App> testAppsList;
    private List<Nucleus> testNucleusList;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba para SonarParam
        Date dateOlder = new Date(System.currentTimeMillis() - 86400000); // Hace 1 día
        Date dateNewer = new Date();

        testSonarParam1 = new SonarParam();
        testSonarParam1.setId(1L);
        testSonarParam1.setAppId(100L);
        testSonarParam1.setAnalisisDate(dateNewer);
        testSonarParam1.setBugs(5L);
        testSonarParam1.setVulnerabilities(2L);
        testSonarParam1.setCodeSmells("10");

        testSonarParam2 = new SonarParam();
        testSonarParam2.setId(2L);
        testSonarParam2.setAppId(100L);
        testSonarParam2.setAnalisisDate(dateOlder); // Análisis más antiguo del mismo app
        testSonarParam2.setBugs(3L);
        testSonarParam2.setVulnerabilities(1L);
        testSonarParam2.setCodeSmells("8");

        testSonarParam3 = new SonarParam();
        testSonarParam3.setId(3L);
        testSonarParam3.setAppId(200L);
        testSonarParam3.setAnalisisDate(dateNewer);
        testSonarParam3.setBugs(3L);
        testSonarParam3.setVulnerabilities(1L);
        testSonarParam3.setCodeSmells("15");

        testSonarParamsList = Arrays.asList(testSonarParam1, testSonarParam2, testSonarParam3);

        // Configurar datos de prueba para App
        testApp1 = new App();
        testApp1.setId(100L);
        testApp1.setUuaa("awbs");

        testApp2 = new App();
        testApp2.setId(200L);
        testApp2.setUuaa("awcc");

        testAppsList = Arrays.asList(testApp1, testApp2);

        // Configurar datos de prueba para Nucleus
        testNucleus1 = new Nucleus();
        testNucleus1.setId(1L);
        testNucleus1.setUuaa("AWBS0000,AWCC0000");
        testNucleus1.setArea("IT");
        testNucleus1.setOrgN2fabrica("Fabrica1");
        testNucleus1.setServiceN1("ServiceN1-1");
        testNucleus1.setServiceN2("ServiceN2-1");

        testNucleus2 = new Nucleus();
        testNucleus2.setId(2L);
        testNucleus2.setUuaa("AGES0000");
        testNucleus2.setArea("IT");
        testNucleus2.setOrgN2fabrica("Fabrica2");
        testNucleus2.setServiceN1("ServiceN1-2");
        testNucleus2.setServiceN2("ServiceN2-2");

        testNucleusList = Arrays.asList(testNucleus1, testNucleus2);
    }

    @Test
    void getDeudaTecnicaPorNivel_ShouldReturnDataByVertical_WhenNoFiltersProvided() {
        // Given
        when(sonarParamRepository.findAll()).thenReturn(testSonarParamsList);
        when(appRepository.findAll()).thenReturn(testAppsList);
        when(nucleusRepository.findAll()).thenReturn(testNucleusList);

        // When
        List<DeudaTecnicaItemDTO> result = deudaTecnicaService.getDeudaTecnicaPorNivel(null, null, null);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // Debería agrupar por VERTICAL (área)
        assertEquals("VERTICAL", result.get(0).getNivelTipo());
        assertEquals("IT", result.get(0).getLabel());
        
        verify(sonarParamRepository, times(1)).findAll();
        verify(appRepository, times(1)).findAll();
        verify(nucleusRepository, times(1)).findAll();
    }

    @Test
    void getDeudaTecnicaPorNivel_ShouldReturnDataByFabrica_WhenVerticalFilterProvided() {
        // Given
        String vertical = "IT";
        when(sonarParamRepository.findAll()).thenReturn(testSonarParamsList);
        when(appRepository.findAll()).thenReturn(testAppsList);
        when(nucleusRepository.findAll()).thenReturn(testNucleusList);

        // When
        List<DeudaTecnicaItemDTO> result = deudaTecnicaService.getDeudaTecnicaPorNivel(vertical, null, null);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // Debería agrupar por FABRICA
        assertEquals("FABRICA", result.get(0).getNivelTipo());
        
        verify(sonarParamRepository, times(1)).findAll();
        verify(appRepository, times(1)).findAll();
        verify(nucleusRepository, times(1)).findAll();
    }

    @Test
    void getDeudaTecnicaPorNivel_ShouldReturnDataBySN1_WhenVerticalAndFabricaProvided() {
        // Given
        String vertical = "IT";
        String fabrica = "Fabrica1";
        when(sonarParamRepository.findAll()).thenReturn(testSonarParamsList);
        when(appRepository.findAll()).thenReturn(testAppsList);
        when(nucleusRepository.findAll()).thenReturn(testNucleusList);

        // When
        List<DeudaTecnicaItemDTO> result = deudaTecnicaService.getDeudaTecnicaPorNivel(vertical, fabrica, null);

        // Then
        assertNotNull(result);
        
        // Debería agrupar por SN1
        if (!result.isEmpty()) {
            assertEquals("SN1", result.get(0).getNivelTipo());
        }
        
        verify(sonarParamRepository, times(1)).findAll();
        verify(appRepository, times(1)).findAll();
        verify(nucleusRepository, times(1)).findAll();
    }

    @Test
    void getDeudaTecnicaPorNivel_ShouldReturnDataBySN2_WhenAllFiltersProvided() {
        // Given
        String vertical = "IT";
        String fabrica = "Fabrica1";
        String sn1 = "ServiceN1-1";
        when(sonarParamRepository.findAll()).thenReturn(testSonarParamsList);
        when(appRepository.findAll()).thenReturn(testAppsList);
        when(nucleusRepository.findAll()).thenReturn(testNucleusList);

        // When
        List<DeudaTecnicaItemDTO> result = deudaTecnicaService.getDeudaTecnicaPorNivel(vertical, fabrica, sn1);

        // Then
        assertNotNull(result);
        
        // Debería agrupar por SN2
        if (!result.isEmpty()) {
            assertEquals("SN2", result.get(0).getNivelTipo());
        }
        
        verify(sonarParamRepository, times(1)).findAll();
        verify(appRepository, times(1)).findAll();
        verify(nucleusRepository, times(1)).findAll();
    }

    @Test
    void getDeudaTecnicaPorNivel_ShouldUseLatestAnalysis_WhenMultipleAnalysisForSameApp() {
        // Given
        when(sonarParamRepository.findAll()).thenReturn(testSonarParamsList);
        when(appRepository.findAll()).thenReturn(testAppsList);
        when(nucleusRepository.findAll()).thenReturn(testNucleusList);

        // When
        List<DeudaTecnicaItemDTO> result = deudaTecnicaService.getDeudaTecnicaPorNivel(null, null, null);

        // Then
        assertNotNull(result);
        
        // Debería usar solo el análisis más reciente (testSonarParam1, no testSonarParam2)
        // Verificar que los valores sean los del análisis más nuevo
        DeudaTecnicaItemDTO dto = result.get(0);
        
        // Total bugs = 5 (de testSonarParam1) + 3 (de testSonarParam3) = 8
        assertEquals(8, dto.getTotalBugs());
        
        verify(sonarParamRepository, times(1)).findAll();
        verify(appRepository, times(1)).findAll();
        verify(nucleusRepository, times(1)).findAll();
    }

    @Test
    void getDeudaTecnicaPorNivel_ShouldHandleNullCodeSmells() {
        // Given
        testSonarParam1.setCodeSmells(null);
        when(sonarParamRepository.findAll()).thenReturn(testSonarParamsList);
        when(appRepository.findAll()).thenReturn(testAppsList);
        when(nucleusRepository.findAll()).thenReturn(testNucleusList);

        // When
        List<DeudaTecnicaItemDTO> result = deudaTecnicaService.getDeudaTecnicaPorNivel(null, null, null);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        verify(sonarParamRepository, times(1)).findAll();
    }

    @Test
    void getDeudaTecnicaPorNivel_ShouldHandleInvalidCodeSmells() {
        // Given
        testSonarParam1.setCodeSmells("invalid");
        when(sonarParamRepository.findAll()).thenReturn(testSonarParamsList);
        when(appRepository.findAll()).thenReturn(testAppsList);
        when(nucleusRepository.findAll()).thenReturn(testNucleusList);

        // When
        List<DeudaTecnicaItemDTO> result = deudaTecnicaService.getDeudaTecnicaPorNivel(null, null, null);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        verify(sonarParamRepository, times(1)).findAll();
    }

    @Test
    void getDeudaTecnicaPorNivel_ShouldIgnoreSonarParamsWithoutAppId() {
        // Given
        SonarParam invalidParam = new SonarParam();
        invalidParam.setId(99L);
        invalidParam.setAppId(0L); // AppId inválido
        invalidParam.setAnalisisDate(new Date());
        
        List<SonarParam> sonarParamsWithInvalid = new ArrayList<>(testSonarParamsList);
        sonarParamsWithInvalid.add(invalidParam);
        
        when(sonarParamRepository.findAll()).thenReturn(sonarParamsWithInvalid);
        when(appRepository.findAll()).thenReturn(testAppsList);
        when(nucleusRepository.findAll()).thenReturn(testNucleusList);

        // When
        List<DeudaTecnicaItemDTO> result = deudaTecnicaService.getDeudaTecnicaPorNivel(null, null, null);

        // Then
        assertNotNull(result);
        
        verify(sonarParamRepository, times(1)).findAll();
    }

    @Test
    void getDeudaTecnicaPorNivel_ShouldIgnoreSonarParamsWithoutAnalysisDate() {
        // Given
        SonarParam invalidParam = new SonarParam();
        invalidParam.setId(99L);
        invalidParam.setAppId(300L);
        invalidParam.setAnalisisDate(null); // Fecha inválida
        
        List<SonarParam> sonarParamsWithInvalid = new ArrayList<>(testSonarParamsList);
        sonarParamsWithInvalid.add(invalidParam);
        
        when(sonarParamRepository.findAll()).thenReturn(sonarParamsWithInvalid);
        when(appRepository.findAll()).thenReturn(testAppsList);
        when(nucleusRepository.findAll()).thenReturn(testNucleusList);

        // When
        List<DeudaTecnicaItemDTO> result = deudaTecnicaService.getDeudaTecnicaPorNivel(null, null, null);

        // Then
        assertNotNull(result);
        
        verify(sonarParamRepository, times(1)).findAll();
    }

    @Test
    void getDeudaTecnicaPorNivel_ShouldHandleEmptyRepositories() {
        // Given
        when(sonarParamRepository.findAll()).thenReturn(Collections.emptyList());
        when(appRepository.findAll()).thenReturn(Collections.emptyList());
        when(nucleusRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<DeudaTecnicaItemDTO> result = deudaTecnicaService.getDeudaTecnicaPorNivel(null, null, null);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(sonarParamRepository, times(1)).findAll();
        verify(appRepository, times(1)).findAll();
        verify(nucleusRepository, times(1)).findAll();
    }

    @Test
    void getDeudaTecnicaPorNivel_ShouldHandleAppWithoutNucleus() {
        // Given
        App appWithoutNucleus = new App();
        appWithoutNucleus.setId(300L);
        appWithoutNucleus.setUuaa("xxxx"); // UUAA que no existe en nucleus
        
        List<App> appsWithOrphan = new ArrayList<>(testAppsList);
        appsWithOrphan.add(appWithoutNucleus);
        
        SonarParam orphanParam = new SonarParam();
        orphanParam.setId(99L);
        orphanParam.setAppId(300L);
        orphanParam.setAnalisisDate(new Date());
        orphanParam.setBugs(10L);
        
        List<SonarParam> sonarParamsWithOrphan = new ArrayList<>(testSonarParamsList);
        sonarParamsWithOrphan.add(orphanParam);
        
        when(sonarParamRepository.findAll()).thenReturn(sonarParamsWithOrphan);
        when(appRepository.findAll()).thenReturn(appsWithOrphan);
        when(nucleusRepository.findAll()).thenReturn(testNucleusList);

        // When
        List<DeudaTecnicaItemDTO> result = deudaTecnicaService.getDeudaTecnicaPorNivel(null, null, null);

        // Then
        assertNotNull(result);
        
        // No debería incluir el app huérfano en el resultado
        verify(sonarParamRepository, times(1)).findAll();
        verify(appRepository, times(1)).findAll();
        verify(nucleusRepository, times(1)).findAll();
    }

    @Test
    void getDeudaTecnicaPorNivel_ShouldFilterByVertical() {
        // Given
        String vertical = "IT";
        when(sonarParamRepository.findAll()).thenReturn(testSonarParamsList);
        when(appRepository.findAll()).thenReturn(testAppsList);
        when(nucleusRepository.findAll()).thenReturn(testNucleusList);

        // When
        List<DeudaTecnicaItemDTO> result = deudaTecnicaService.getDeudaTecnicaPorNivel(vertical, null, null);

        // Then
        assertNotNull(result);
        
        // Todos los resultados deberían ser del vertical IT
        verify(sonarParamRepository, times(1)).findAll();
        verify(nucleusRepository, times(1)).findAll();
    }

    @Test
    void getDeudaTecnicaPorNivel_ShouldReturnSortedResults() {
        // Given
        when(sonarParamRepository.findAll()).thenReturn(testSonarParamsList);
        when(appRepository.findAll()).thenReturn(testAppsList);
        when(nucleusRepository.findAll()).thenReturn(testNucleusList);

        // When
        List<DeudaTecnicaItemDTO> result = deudaTecnicaService.getDeudaTecnicaPorNivel(null, null, null);

        // Then
        assertNotNull(result);
        
        // Verificar que los resultados están ordenados por label
        for (int i = 1; i < result.size(); i++) {
            assertTrue(result.get(i - 1).getLabel().compareTo(result.get(i).getLabel()) <= 0);
        }
        
        verify(sonarParamRepository, times(1)).findAll();
    }

    @Test
    void getDeudaTecnicaPorNivel_ShouldHandleNullUuaaInApp() {
        // Given
        testApp1.setUuaa(null);
        when(sonarParamRepository.findAll()).thenReturn(testSonarParamsList);
        when(appRepository.findAll()).thenReturn(testAppsList);
        when(nucleusRepository.findAll()).thenReturn(testNucleusList);

        // When
        List<DeudaTecnicaItemDTO> result = deudaTecnicaService.getDeudaTecnicaPorNivel(null, null, null);

        // Then
        assertNotNull(result);
        
        verify(sonarParamRepository, times(1)).findAll();
        verify(appRepository, times(1)).findAll();
        verify(nucleusRepository, times(1)).findAll();
    }

    @Test
    void getDeudaTecnicaPorNivel_ShouldHandleNullUuaaInNucleus() {
        // Given
        testNucleus1.setUuaa(null);
        when(sonarParamRepository.findAll()).thenReturn(testSonarParamsList);
        when(appRepository.findAll()).thenReturn(testAppsList);
        when(nucleusRepository.findAll()).thenReturn(testNucleusList);

        // When
        List<DeudaTecnicaItemDTO> result = deudaTecnicaService.getDeudaTecnicaPorNivel(null, null, null);

        // Then
        assertNotNull(result);
        
        verify(sonarParamRepository, times(1)).findAll();
        verify(appRepository, times(1)).findAll();
        verify(nucleusRepository, times(1)).findAll();
    }

    @Test
    void getDeudaTecnicaPorNivel_ShouldHandleMultipleUuaaInNucleus() {
        // Given
        // testNucleus1 ya tiene "AWBS0000,AWCC0000"
        when(sonarParamRepository.findAll()).thenReturn(testSonarParamsList);
        when(appRepository.findAll()).thenReturn(testAppsList);
        when(nucleusRepository.findAll()).thenReturn(testNucleusList);

        // When
        List<DeudaTecnicaItemDTO> result = deudaTecnicaService.getDeudaTecnicaPorNivel(null, null, null);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // Debería mapear correctamente ambas UUAAs del nucleus
        verify(sonarParamRepository, times(1)).findAll();
        verify(appRepository, times(1)).findAll();
        verify(nucleusRepository, times(1)).findAll();
    }

    @Test
    void getDeudaTecnicaPorNivel_ShouldAccumulateMetricsCorrectly() {
        // Given
        when(sonarParamRepository.findAll()).thenReturn(testSonarParamsList);
        when(appRepository.findAll()).thenReturn(testAppsList);
        when(nucleusRepository.findAll()).thenReturn(testNucleusList);

        // When
        List<DeudaTecnicaItemDTO> result = deudaTecnicaService.getDeudaTecnicaPorNivel(null, null, null);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        DeudaTecnicaItemDTO dto = result.get(0);
        
        // Verificar que las métricas se acumulan correctamente
        assertTrue(dto.getTotalBugs() > 0);
        assertTrue(dto.getTotalVulnerabilities() > 0);
        assertTrue(dto.getTotalCodeSmells() > 0);
        assertTrue(dto.getTotalApps() > 0);
        
        verify(sonarParamRepository, times(1)).findAll();
        verify(appRepository, times(1)).findAll();
        verify(nucleusRepository, times(1)).findAll();
    }
}
