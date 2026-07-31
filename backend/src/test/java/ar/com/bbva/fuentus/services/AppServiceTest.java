package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.*;
import ar.com.bbva.fuentus.entities.App;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.Rfo;
import ar.com.bbva.fuentus.repositories.AppsRepository;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import ar.com.bbva.fuentus.repositories.ServerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppServiceTest {

    @Mock
    private AppsRepository appsRepository;

    @Mock
    private ServerRepository serverRepository;

    @Mock
    private NucleusRepository nucleusRepository;

    @InjectMocks
    private AppService appService;

    private AppSummaryDTO testAppSummary;
    private ServerInfoDTO testServerInfo;
    private List<AppSummaryDTO> testAppList;
    private List<ServerInfoDTO> testServerList;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
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

        testServerInfo = new ServerInfoDTO();
        // Configurar propiedades del servidor si es necesario
        
        testAppList = Arrays.asList(testAppSummary);
        testServerList = Arrays.asList(testServerInfo);
    }

    @Test
    void getAppsByUUAA_ShouldReturnListOfApps_WhenValidUUAAProvided() {
        // Given
        String uuaa = "TEST";
        when(appsRepository.findAppSummaryByUuaa(uuaa)).thenReturn(testAppList);

        // When
        List<AppSummaryDTO> result = appService.getAppsByUUAA(uuaa);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test-app", result.get(0).getName());
        verify(appsRepository, times(1)).findAppSummaryByUuaa(uuaa);
    }

    @Test
    void getAppsByUUAA_ShouldReturnEmptyList_WhenNoAppsFound() {
        // Given
        String uuaa = "NONEXISTENT";
        when(appsRepository.findAppSummaryByUuaa(uuaa)).thenReturn(Collections.emptyList());

        // When
        List<AppSummaryDTO> result = appService.getAppsByUUAA(uuaa);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(appsRepository, times(1)).findAppSummaryByUuaa(uuaa);
    }

    @Test
    void getAppsByUUAAPageable_ShouldReturnPageWithServers_WhenValidInput() {
        // Given
        String uuaa = "TEST";
        int page = 0;
        Pageable pageable = PageRequest.of(page, 20);
        Page<AppSummaryDTO> mockPage = new PageImpl<>(testAppList, pageable, 1);

        when(appsRepository.findAppSummaryByUuaaPageable(uuaa, pageable)).thenReturn(mockPage);
        when(serverRepository.findServersByUuaa("test-app")).thenReturn(testServerList);

        // When
        Page<AppSummaryDTO> result = appService.getAppsByUUAAPageable(uuaa, page);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("test-app", result.getContent().get(0).getName());
        
        verify(appsRepository, times(1)).findAppSummaryByUuaaPageable(uuaa, pageable);
        verify(serverRepository, times(1)).findServersByUuaa("test-app");
    }

    @Test
    void getAppsByUUAAPageableWithSearch_ShouldUseSearchRepository_WhenSearchTermProvided() {
        // Given
        String uuaa = "TEST";
        int page = 0;
        String search = "test";
        Pageable pageable = PageRequest.of(page, 20);
        Page<AppSummaryDTO> mockPage = new PageImpl<>(testAppList, pageable, 1);

        when(appsRepository.findAppSummaryByUuaaAndSearchPageable(uuaa, search, pageable)).thenReturn(mockPage);
        when(serverRepository.findServersByName("test-app")).thenReturn(testServerList);

        // When
        Page<AppSummaryDTO> result = appService.getAppsByUUAAPageableWithSearch(uuaa, page, search);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(appsRepository, times(1)).findAppSummaryByUuaaAndSearchPageable(uuaa, search, pageable);
        verify(serverRepository, times(1)).findServersByName("test-app");
        verify(appsRepository, never()).findAppSummaryByUuaaPageable(any(), any());
    }

    @Test
    void getAppsByUUAAPageableWithSearch_ShouldUseRegularRepository_WhenSearchTermIsNull() {
        // Given
        String uuaa = "TEST";
        int page = 0;
        String search = null;
        Pageable pageable = PageRequest.of(page, 20);
        Page<AppSummaryDTO> mockPage = new PageImpl<>(testAppList, pageable, 1);

        when(appsRepository.findAppSummaryByUuaaPageable(uuaa, pageable)).thenReturn(mockPage);
        when(serverRepository.findServersByName("test-app")).thenReturn(testServerList);

        // When
        Page<AppSummaryDTO> result = appService.getAppsByUUAAPageableWithSearch(uuaa, page, search);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(appsRepository, times(1)).findAppSummaryByUuaaPageable(uuaa, pageable);
        verify(serverRepository, times(1)).findServersByName("test-app");
        verify(appsRepository, never()).findAppSummaryByUuaaAndSearchPageable(any(), any(), any());
    }

    @Test
    void getAppsByUUAAPageableWithSearch_ShouldUseRegularRepository_WhenSearchTermIsEmpty() {
        // Given
        String uuaa = "TEST";
        int page = 0;
        String search = "   ";
        Pageable pageable = PageRequest.of(page, 20);
        Page<AppSummaryDTO> mockPage = new PageImpl<>(testAppList, pageable, 1);

        when(appsRepository.findAppSummaryByUuaaPageable(uuaa, pageable)).thenReturn(mockPage);
        when(serverRepository.findServersByName("test-app")).thenReturn(testServerList);

        // When
        Page<AppSummaryDTO> result = appService.getAppsByUUAAPageableWithSearch(uuaa, page, search);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(appsRepository, times(1)).findAppSummaryByUuaaPageable(uuaa, pageable);
        verify(serverRepository, times(1)).findServersByName("test-app");
        verify(appsRepository, never()).findAppSummaryByUuaaAndSearchPageable(any(), any(), any());
    }

    @Test
    void getAppsByUUAAPageable_ShouldHandleEmptyServerList() {
        // Given
        String uuaa = "TEST";
        int page = 0;
        Pageable pageable = PageRequest.of(page, 20);
        Page<AppSummaryDTO> mockPage = new PageImpl<>(testAppList, pageable, 1);

        when(appsRepository.findAppSummaryByUuaaPageable(uuaa, pageable)).thenReturn(mockPage);
        when(serverRepository.findServersByUuaa("test-app")).thenReturn(Collections.emptyList());

        // When
        Page<AppSummaryDTO> result = appService.getAppsByUUAAPageable(uuaa, page);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).getServers().isEmpty());
        
        verify(appsRepository, times(1)).findAppSummaryByUuaaPageable(uuaa, pageable);
        verify(serverRepository, times(1)).findServersByUuaa("test-app");
    }

    @Test
    void getAllAppsByUUAAComplete_ShouldReturnCompleteAppsWithServers_WhenValidUUAAProvided() {
        // Given
        String uuaa = "TEST";
        String expectedUuaaForRepository = "TEST"; // Los primeros 4 caracteres en mayúsculas
        when(appsRepository.findAppSummaryByUuaa(expectedUuaaForRepository)).thenReturn(testAppList);
        when(serverRepository.findServersByName("test-app")).thenReturn(testServerList);

        // When
        List<AppSummaryDTO> result = appService.getAllAppsByUUAAComplete(uuaa);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test-app", result.get(0).getName());
        assertNotNull(result.get(0).getServers());
        assertEquals(1, result.get(0).getServers().size());
        
        verify(appsRepository, times(1)).findAppSummaryByUuaa(expectedUuaaForRepository);
        verify(serverRepository, times(1)).findServersByName("test-app");
    }

    @Test
    void getAllAppsByUUAAComplete_ShouldHandleEmptyAppsList_WhenNoAppsFound() {
        // Given
        String uuaa = "EMPTY";
        String expectedUuaaForRepository = "EMPT"; // Los primeros 4 caracteres en mayúsculas
        when(appsRepository.findAppSummaryByUuaa(expectedUuaaForRepository)).thenReturn(Collections.emptyList());

        // When
        List<AppSummaryDTO> result = appService.getAllAppsByUUAAComplete(uuaa);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(appsRepository, times(1)).findAppSummaryByUuaa(expectedUuaaForRepository);
        verify(serverRepository, never()).findServersByName(any());
    }

    @Test
    void getAllAppsByUUAAComplete_ShouldHandleMultipleApps_WhenMultipleAppsExist() {
        // Given
        String uuaa = "MULTI";
        String expectedUuaaForRepository = "MULT"; // Los primeros 4 caracteres en mayúsculas
        
        AppSummaryDTO app2 = new AppSummaryDTO(
            2L,
            "test-app-2",
            "MULT",
            "https://bitbucket.com/test-app-2",
            "https://sonar.com/test-app-2",
            "https://sonar10.com/test-app-2",
            10L,          // bugs
            75.0,         // coverage
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

        List<AppSummaryDTO> multipleApps = Arrays.asList(testAppSummary, app2);
        List<ServerInfoDTO> servers2 = Arrays.asList(new ServerInfoDTO());

        when(appsRepository.findAppSummaryByUuaa(expectedUuaaForRepository)).thenReturn(multipleApps);
        when(serverRepository.findServersByName("test-app")).thenReturn(testServerList);
        when(serverRepository.findServersByName("test-app-2")).thenReturn(servers2);

        // When
        List<AppSummaryDTO> result = appService.getAllAppsByUUAAComplete(uuaa);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test-app", result.get(0).getName());
        assertEquals("test-app-2", result.get(1).getName());
        assertNotNull(result.get(0).getServers());
        assertNotNull(result.get(1).getServers());
        assertEquals(1, result.get(0).getServers().size());
        assertEquals(1, result.get(1).getServers().size());
        
        verify(appsRepository, times(1)).findAppSummaryByUuaa(expectedUuaaForRepository);
        verify(serverRepository, times(1)).findServersByName("test-app");
        verify(serverRepository, times(1)).findServersByName("test-app-2");
    }

    @Test
    void getAllAppsByUUAAComplete_ShouldHandleAppsWithoutServers_WhenNoServersFound() {
        // Given
        String uuaa = "NOSERV";
        String expectedUuaaForRepository = "NOSE"; // Los primeros 4 caracteres en mayúsculas
        when(appsRepository.findAppSummaryByUuaa(expectedUuaaForRepository)).thenReturn(testAppList);
        when(serverRepository.findServersByName("test-app")).thenReturn(Collections.emptyList());

        // When
        List<AppSummaryDTO> result = appService.getAllAppsByUUAAComplete(uuaa);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test-app", result.get(0).getName());
        assertNotNull(result.get(0).getServers());
        assertTrue(result.get(0).getServers().isEmpty());
        
        verify(appsRepository, times(1)).findAppSummaryByUuaa(expectedUuaaForRepository);
        verify(serverRepository, times(1)).findServersByName("test-app");
    }

    @Test
    void getAllAppsByUUAAComplete_ShouldTruncateUUAAToFourChars_WhenLongUUAAProvided() {
        // Given
        String uuaa = "VERYLONGUUAA"; // Más de 4 caracteres
        String expectedUuaaForRepository = "VERY"; // Los primeros 4 caracteres en mayúsculas
        when(appsRepository.findAppSummaryByUuaa(expectedUuaaForRepository)).thenReturn(testAppList);
        when(serverRepository.findServersByName("test-app")).thenReturn(testServerList);

        // When
        List<AppSummaryDTO> result = appService.getAllAppsByUUAAComplete(uuaa);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        verify(appsRepository, times(1)).findAppSummaryByUuaa(expectedUuaaForRepository);
        verify(serverRepository, times(1)).findServersByName("test-app");
    }

    @Test
    void getAllAppsByUUAAComplete_ShouldConvertToUpperCase_WhenLowerCaseUUAAProvided() {
        // Given
        String uuaa = "test"; // En minúsculas
        String expectedUuaaForRepository = "TEST"; // Debería convertirse a mayúsculas
        when(appsRepository.findAppSummaryByUuaa(expectedUuaaForRepository)).thenReturn(testAppList);
        when(serverRepository.findServersByName("test-app")).thenReturn(testServerList);

        // When
        List<AppSummaryDTO> result = appService.getAllAppsByUUAAComplete(uuaa);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        verify(appsRepository, times(1)).findAppSummaryByUuaa(expectedUuaaForRepository);
        verify(serverRepository, times(1)).findServersByName("test-app");
    }

    @Test
    void getAppsByUUAAPageableWithSearch_ShouldHandleEmptySearchAfterTrim_WhenSearchIsOnlySpaces() {
        // Given
        String uuaa = "TEST";
        int page = 0;
        String search = "     "; // Solo espacios
        Pageable pageable = PageRequest.of(page, 20);
        Page<AppSummaryDTO> mockPage = new PageImpl<>(testAppList, pageable, 1);

        when(appsRepository.findAppSummaryByUuaaPageable(uuaa, pageable)).thenReturn(mockPage);
        when(serverRepository.findServersByName("test-app")).thenReturn(testServerList);

        // When
        Page<AppSummaryDTO> result = appService.getAppsByUUAAPageableWithSearch(uuaa, page, search);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        // Debería usar el repositorio regular (sin búsqueda) porque search.trim().isEmpty() es true
        verify(appsRepository, times(1)).findAppSummaryByUuaaPageable(uuaa, pageable);
        verify(appsRepository, never()).findAppSummaryByUuaaAndSearchPageable(any(), any(), any());
        verify(serverRepository, times(1)).findServersByName("test-app");
    }

    @Test
    void getAppsByUUAAPageable_ShouldHandleMultipleAppsWithDifferentServerCounts() {
        // Given
        String uuaa = "TEST";
        int page = 0;
        Pageable pageable = PageRequest.of(page, 20);
        
        AppSummaryDTO app2 = new AppSummaryDTO(
            2L,
            "test-app-2",
            "TEST",
            "https://bitbucket.com/test-app-2",
            "https://sonar.com/test-app-2",
            "https://sonar10.com/test-app-2",
            10L, 75.0, true, 5L, 3L, 2L, "Python",
            "https://chimera.com/test-app-2", "https://samuel.com/test-app-2",
            2L, 3L, 4L, 1L
        );

        List<AppSummaryDTO> multipleApps = Arrays.asList(testAppSummary, app2);
        Page<AppSummaryDTO> mockPage = new PageImpl<>(multipleApps, pageable, 2);
        List<ServerInfoDTO> moreServers = Arrays.asList(new ServerInfoDTO(), new ServerInfoDTO());

        when(appsRepository.findAppSummaryByUuaaPageable(uuaa, pageable)).thenReturn(mockPage);
        when(serverRepository.findServersByUuaa("test-app")).thenReturn(testServerList); // 1 servidor
        when(serverRepository.findServersByUuaa("test-app-2")).thenReturn(moreServers); // 2 servidores

        // When
        Page<AppSummaryDTO> result = appService.getAppsByUUAAPageable(uuaa, page);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(1, result.getContent().get(0).getServers().size());
        assertEquals(2, result.getContent().get(1).getServers().size());
        
        verify(appsRepository, times(1)).findAppSummaryByUuaaPageable(uuaa, pageable);
        verify(serverRepository, times(1)).findServersByUuaa("test-app");
        verify(serverRepository, times(1)).findServersByUuaa("test-app-2");
    }

    // ====== Tests para searchWithFilters ======
    
    @Test
    void searchWithFilters_ShouldReturnFilteredApps_WhenAllFiltersProvided() {
        // Given
        String searchTerm = "test";
        String vertical = "Retail";
        String uol2 = "UOL2";
        String sn1 = "SN1";
        String sn2 = "SN2";
        int page = 0;
        Pageable pageable = PageRequest.of(page, 20);
        
        App testApp = new App();
        testApp.setId(1L);
        testApp.setName("test-app");
        
        Page<App> appsPage = new PageImpl<>(Arrays.asList(testApp), pageable, 1);
        
        when(appsRepository.findAppsWithFilters(searchTerm, vertical, uol2, sn1, sn2, pageable))
            .thenReturn(appsPage);

        // When
        Page<AppDTO> result = appService.searchWithFilters(searchTerm, vertical, uol2, sn1, sn2, page);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(appsRepository, times(1)).findAppsWithFilters(searchTerm, vertical, uol2, sn1, sn2, pageable);
    }

    @Test
    void searchWithFilters_ShouldHandleNullSearchTerm_WhenNullProvided() {
        // Given
        String searchTerm = null;
        String vertical = "Retail";
        String uol2 = null;
        String sn1 = null;
        String sn2 = null;
        int page = 0;
        Pageable pageable = PageRequest.of(page, 20);
        
        Page<App> emptyPage = Page.empty(pageable);
        when(appsRepository.findAppsWithFilters(searchTerm, vertical, uol2, sn1, sn2, pageable))
            .thenReturn(emptyPage);

        // When
        Page<AppDTO> result = appService.searchWithFilters(searchTerm, vertical, uol2, sn1, sn2, page);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(appsRepository, times(1)).findAppsWithFilters(searchTerm, vertical, uol2, sn1, sn2, pageable);
    }

    // ====== Tests para getAppDetails ======
    
    @Test
    void getAppDetails_ShouldReturnAppWithServers_WhenValidAppIdProvided() {
        // Given
        Long appId = 1L;
        when(appsRepository.findAppDetailsById(appId)).thenReturn(testAppSummary);
        when(serverRepository.findServersByUuaa("test-app")).thenReturn(testServerList);

        // When
        AppSummaryDTO result = appService.getAppDetails(appId);

        // Then
        assertNotNull(result);
        assertEquals("test-app", result.getName());
        assertNotNull(result.getServers());
        assertEquals(1, result.getServers().size());
        verify(appsRepository, times(1)).findAppDetailsById(appId);
        verify(serverRepository, times(1)).findServersByUuaa("test-app");
    }

    @Test
    void getAppDetails_ShouldReturnNull_WhenAppNotFound() {
        // Given
        Long appId = 999L;
        when(appsRepository.findAppDetailsById(appId)).thenReturn(null);

        // When
        AppSummaryDTO result = appService.getAppDetails(appId);

        // Then
        assertNull(result);
        verify(appsRepository, times(1)).findAppDetailsById(appId);
        verify(serverRepository, never()).findServersByUuaa(any());
    }

    @Test
    void getAppDetails_ShouldHandleEmptyServers_WhenNoServersFound() {
        // Given
        Long appId = 1L;
        when(appsRepository.findAppDetailsById(appId)).thenReturn(testAppSummary);
        when(serverRepository.findServersByUuaa("test-app")).thenReturn(Collections.emptyList());

        // When
        AppSummaryDTO result = appService.getAppDetails(appId);

        // Then
        assertNotNull(result);
        assertEquals("test-app", result.getName());
        assertNotNull(result.getServers());
        assertTrue(result.getServers().isEmpty());
        verify(appsRepository, times(1)).findAppDetailsById(appId);
        verify(serverRepository, times(1)).findServersByUuaa("test-app");
    }

    // ====== Tests para getUuaaSummary ======
    
    @Test
    void getUuaaSummary_ShouldReturnSummaryWithMetrics_WhenValidUuaaProvided() {
        // Given
        String uuaa = "TEST";
        
        AppSummaryDTO app1 = createAppSummaryWithMetrics("app1", 80.0, 5L, 1L, 2L, 3L, 0L, 1L, 2L, 0L);
        AppSummaryDTO app2 = createAppSummaryWithMetrics("app2", 90.0, 10L, 2L, 3L, 4L, 1L, 2L, 3L, 1L);
        
        List<AppSummaryDTO> apps = Arrays.asList(app1, app2);
        
        Nucleus nucleus = new Nucleus();
        nucleus.setUuaa("TEST");
        Rfo rfo = new Rfo();
        rfo.setRfoId(1L);
        rfo.setEstadoRfo("Active");
        nucleus.setRfo(rfo);
        
        when(appsRepository.findAppSummaryByUuaa("TEST")).thenReturn(apps);
        when(nucleusRepository.findByUuaaLike("%TEST%")).thenReturn(Arrays.asList(nucleus));

        // When
        UuaaSummaryDTO result = appService.getUuaaSummary(uuaa);

        // Then
        assertNotNull(result);
        assertEquals("TEST", result.getUuaa());
        assertEquals(2, result.getTotalApps());
        assertEquals(85.0, result.getAverageCoverage(), 0.01); // (80 + 90) / 2
        assertEquals(15L, result.getTotalBugs()); // 5 + 10
        assertEquals(3L, result.getTotalSastLow()); // 1 + 2
        assertEquals(5L, result.getTotalSastMedium()); // 2 + 3
        assertEquals(7L, result.getTotalSastHigh()); // 3 + 4
        assertEquals(1L, result.getTotalScaLow()); // 0 + 1
        assertEquals(3L, result.getTotalScaMedium()); // 1 + 2
        assertEquals(5L, result.getTotalScaHigh()); // 2 + 3
        assertEquals(1L, result.getTotalScaCritical()); // 0 + 1
        assertEquals(1L, result.getRfoId());
        assertEquals("Active", result.getRfoEstado());
        
        verify(appsRepository, times(1)).findAppSummaryByUuaa("TEST");
        verify(nucleusRepository, times(1)).findByUuaaLike("%TEST%");
    }

    @Test
    void getUuaaSummary_ShouldHandleNoApps_WhenUuaaHasNoApps() {
        // Given
        String uuaa = "EMPTY";
        when(appsRepository.findAppSummaryByUuaa("EMPT")).thenReturn(Collections.emptyList());
        when(nucleusRepository.findByUuaaLike("%EMPT%")).thenReturn(Collections.emptyList());

        // When
        UuaaSummaryDTO result = appService.getUuaaSummary(uuaa);

        // Then
        assertNotNull(result);
        assertEquals("EMPT", result.getUuaa());
        assertEquals(0, result.getTotalApps());
        assertEquals(0.0, result.getAverageCoverage());
        assertEquals(0L, result.getTotalBugs());
        assertNull(result.getRfoId());
        assertNull(result.getRfoEstado());
    }

    @Test
    void getUuaaSummary_ShouldHandleNullMetrics_WhenAppsHaveNullValues() {
        // Given
        String uuaa = "TEST";
        
        AppSummaryDTO app1 = new AppSummaryDTO();
        app1.setName("app1");
        app1.setCoverage(null);
        app1.setBugs(null);
        app1.setChimeraSast(null);
        app1.setChimeraSca(null);
        
        when(appsRepository.findAppSummaryByUuaa("TEST")).thenReturn(Arrays.asList(app1));
        when(nucleusRepository.findByUuaaLike("%TEST%")).thenReturn(Collections.emptyList());

        // When
        UuaaSummaryDTO result = appService.getUuaaSummary(uuaa);

        // Then
        assertNotNull(result);
        assertEquals("TEST", result.getUuaa());
        assertEquals(1, result.getTotalApps());
        assertEquals(0.0, result.getAverageCoverage()); // No apps with coverage
        assertEquals(0L, result.getTotalBugs());
        assertEquals(0L, result.getTotalSastLow());
        assertEquals(0L, result.getTotalScaHigh());
    }

    @Test
    void getUuaaSummary_ShouldHandleNucleusWithoutRfo_WhenRfoIsNull() {
        // Given
        String uuaa = "TEST";
        
        AppSummaryDTO app1 = createAppSummaryWithMetrics("app1", 80.0, 5L, 1L, 2L, 3L, 0L, 1L, 2L, 0L);
        
        Nucleus nucleus = new Nucleus();
        nucleus.setUuaa("TEST");
        nucleus.setRfo(null); // No RFO
        
        when(appsRepository.findAppSummaryByUuaa("TEST")).thenReturn(Arrays.asList(app1));
        when(nucleusRepository.findByUuaaLike("%TEST%")).thenReturn(Arrays.asList(nucleus));

        // When
        UuaaSummaryDTO result = appService.getUuaaSummary(uuaa);

        // Then
        assertNotNull(result);
        assertEquals("TEST", result.getUuaa());
        assertNull(result.getRfoId());
        assertNull(result.getRfoEstado());
    }

    // ====== Tests para getServiceSummary ======
    
    @Test
    void getServiceSummary_ShouldReturnAggregatedSummary_WhenMultipleUuaasProvided() {
        // Given
        List<String> uuaas = Arrays.asList("TEST", "DEMO");
        String serviceN1 = "Service1";
        String serviceN2 = "Service2";
        String ownerServiceN1 = "Owner1";
        Long rfoId = 1L;
        String rfoEstado = "Active";
        
        AppSummaryDTO app1 = createAppSummaryWithMetrics("app1", 80.0, 5L, 1L, 2L, 3L, 0L, 1L, 2L, 0L);
        AppSummaryDTO app2 = createAppSummaryWithMetrics("app2", 90.0, 10L, 2L, 3L, 4L, 1L, 2L, 3L, 1L);
        
        when(appsRepository.findAppSummaryByUuaa("TEST")).thenReturn(Arrays.asList(app1));
        when(appsRepository.findAppSummaryByUuaa("DEMO")).thenReturn(Arrays.asList(app2));

        // When
        ServiceSummaryDTO result = appService.getServiceSummary(uuaas, serviceN1, serviceN2, ownerServiceN1, rfoId, rfoEstado);

        // Then
        assertNotNull(result);
        assertEquals(serviceN1, result.getServiceN1());
        assertEquals(serviceN2, result.getServiceN2());
        assertEquals(ownerServiceN1, result.getOwnerServiceN1());
        assertEquals(uuaas, result.getUuaas());
        assertEquals(rfoId, result.getRfoId());
        assertEquals(rfoEstado, result.getRfoEstado());
        assertEquals(2, result.getTotalApps());
        assertEquals(85.0, result.getAverageCoverage(), 0.01);
        assertEquals(15L, result.getTotalBugs());
        
        verify(appsRepository, times(1)).findAppSummaryByUuaa("TEST");
        verify(appsRepository, times(1)).findAppSummaryByUuaa("DEMO");
    }

    @Test
    void getServiceSummary_ShouldHandleEmptyUuaasList_WhenNoUuaasProvided() {
        // Given
        List<String> uuaas = Collections.emptyList();
        String serviceN1 = "Service1";

        // When
        ServiceSummaryDTO result = appService.getServiceSummary(uuaas, serviceN1, null, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalApps());
        assertEquals(0.0, result.getAverageCoverage());
        verify(appsRepository, never()).findAppSummaryByUuaa(any());
    }

    // ====== Tests para getAppsByMultipleUUAAsPageable ======
    
    @Test
    void getAppsByMultipleUUAAsPageable_ShouldReturnPaginatedApps_WhenMultipleUuaasProvided() {
        // Given
        List<String> uuaas = Arrays.asList("TEST", "DEMO");
        int page = 0;
        String search = null;
        
        AppSummaryDTO app1 = new AppSummaryDTO();
        app1.setName("app1");
        AppSummaryDTO app2 = new AppSummaryDTO();
        app2.setName("app2");
        
        when(appsRepository.findAppSummaryByUuaa("TEST")).thenReturn(Arrays.asList(app1));
        when(appsRepository.findAppSummaryByUuaa("DEMO")).thenReturn(Arrays.asList(app2));
        when(serverRepository.findServersByName("app1")).thenReturn(Collections.emptyList());
        when(serverRepository.findServersByName("app2")).thenReturn(Collections.emptyList());

        // When
        Page<AppSummaryDTO> result = appService.getAppsByMultipleUUAAsPageable(uuaas, page, search);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        verify(appsRepository, times(1)).findAppSummaryByUuaa("TEST");
        verify(appsRepository, times(1)).findAppSummaryByUuaa("DEMO");
    }

    @Test
    void getAppsByMultipleUUAAsPageable_ShouldFilterBySearch_WhenSearchTermProvided() {
        // Given
        List<String> uuaas = Arrays.asList("TEST");
        int page = 0;
        String search = "app1";
        
        AppSummaryDTO app1 = new AppSummaryDTO();
        app1.setName("app1");
        AppSummaryDTO app2 = new AppSummaryDTO();
        app2.setName("app2");
        
        when(appsRepository.findAppSummaryByUuaa("TEST")).thenReturn(Arrays.asList(app1, app2));
        when(serverRepository.findServersByName("app1")).thenReturn(Collections.emptyList());

        // When
        Page<AppSummaryDTO> result = appService.getAppsByMultipleUUAAsPageable(uuaas, page, search);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("app1", result.getContent().get(0).getName());
        verify(serverRepository, times(1)).findServersByName("app1");
        verify(serverRepository, never()).findServersByName("app2");
    }

    @Test
    void getAppsByMultipleUUAAsPageable_ShouldHandlePagination_WhenManyAppsExist() {
        // Given
        List<String> uuaas = Arrays.asList("TEST");
        int page = 0;
        String search = null;
        
        List<AppSummaryDTO> manyApps = new java.util.ArrayList<>();
        for (int i = 0; i < 25; i++) {
            AppSummaryDTO app = new AppSummaryDTO();
            app.setName("app" + i);
            manyApps.add(app);
        }
        
        when(appsRepository.findAppSummaryByUuaa("TEST")).thenReturn(manyApps);
        for (AppSummaryDTO app : manyApps) {
            when(serverRepository.findServersByName(app.getName())).thenReturn(Collections.emptyList());
        }

        // When
        Page<AppSummaryDTO> result = appService.getAppsByMultipleUUAAsPageable(uuaas, page, search);

        // Then
        assertNotNull(result);
        assertEquals(25, result.getTotalElements());
        assertEquals(20, result.getContent().size()); // Page size is 20
        assertEquals(2, result.getTotalPages());
    }

    @Test
    void getAppsByMultipleUUAAsPageable_ShouldRemoveDuplicateUuaas_WhenDuplicatesProvided() {
        // Given
        List<String> uuaas = Arrays.asList("TEST", "test", "TEST"); // Duplicates
        int page = 0;
        String search = null;
        
        AppSummaryDTO app1 = new AppSummaryDTO();
        app1.setName("app1");
        
        when(appsRepository.findAppSummaryByUuaa("TEST")).thenReturn(Arrays.asList(app1));
        when(serverRepository.findServersByName("app1")).thenReturn(Collections.emptyList());

        // When
        Page<AppSummaryDTO> result = appService.getAppsByMultipleUUAAsPageable(uuaas, page, search);

        // Then
        assertNotNull(result);
        verify(appsRepository, times(1)).findAppSummaryByUuaa("TEST"); // Should only call once
    }

    // ====== Tests para getServiceSummaryById ======
    
    @Test
    void getServiceSummaryById_ShouldReturnSummary_WhenValidServiceIdProvided() {
        // Given
        Long serviceId = 1L;
        
        Nucleus nucleus = new Nucleus();
        nucleus.setServiceN1("Service1");
        nucleus.setServiceN2("Service2");
        nucleus.setOwnerServiceN1("Owner1");
        nucleus.setUuaa("TEST,DEMO");
        
        Rfo rfo = new Rfo();
        rfo.setRfoId(100L);
        rfo.setEstadoRfo("Active");
        nucleus.setRfo(rfo);
        
        AppSummaryDTO app1 = createAppSummaryWithMetrics("app1", 80.0, 5L, 1L, 2L, 3L, 0L, 1L, 2L, 0L);
        
        when(nucleusRepository.findById(serviceId)).thenReturn(Optional.of(nucleus));
        when(appsRepository.findAppSummaryByUuaa("TEST")).thenReturn(Arrays.asList(app1));
        when(appsRepository.findAppSummaryByUuaa("DEMO")).thenReturn(Collections.emptyList());

        // When
        ServiceSummaryDTO result = appService.getServiceSummaryById(serviceId);

        // Then
        assertNotNull(result);
        assertEquals(serviceId, result.getServiceId());
        assertEquals("Service1", result.getServiceN1());
        assertEquals("Service2", result.getServiceN2());
        assertEquals("Owner1", result.getOwnerServiceN1());
        assertEquals(2, result.getUuaas().size());
        assertEquals(100L, result.getRfoId());
        assertEquals("Active", result.getRfoEstado());
        assertEquals(1, result.getTotalApps());
    }

    @Test
    void getServiceSummaryById_ShouldReturnNull_WhenServiceNotFound() {
        // Given
        Long serviceId = 999L;
        when(nucleusRepository.findById(serviceId)).thenReturn(Optional.empty());

        // When
        ServiceSummaryDTO result = appService.getServiceSummaryById(serviceId);

        // Then
        assertNull(result);
        verify(appsRepository, never()).findAppSummaryByUuaa(any());
    }

    @Test
    void getServiceSummaryById_ShouldHandleNullUuaa_WhenUuaaIsNull() {
        // Given
        Long serviceId = 1L;
        
        Nucleus nucleus = new Nucleus();
        nucleus.setServiceN1("Service1");
        nucleus.setUuaa(null); // No UUAA
        nucleus.setRfo(null);
        
        when(nucleusRepository.findById(serviceId)).thenReturn(Optional.of(nucleus));

        // When
        ServiceSummaryDTO result = appService.getServiceSummaryById(serviceId);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalApps());
        verify(appsRepository, never()).findAppSummaryByUuaa(any());
    }

    @Test
    void getServiceSummaryById_ShouldHandleEmptyUuaa_WhenUuaaIsEmpty() {
        // Given
        Long serviceId = 1L;
        
        Nucleus nucleus = new Nucleus();
        nucleus.setServiceN1("Service1");
        nucleus.setUuaa(""); // Empty UUAA
        
        when(nucleusRepository.findById(serviceId)).thenReturn(Optional.of(nucleus));

        // When
        ServiceSummaryDTO result = appService.getServiceSummaryById(serviceId);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalApps());
        verify(appsRepository, never()).findAppSummaryByUuaa(any());
    }

    // ====== Tests para getAppsByServiceIdPageable ======
    
    @Test
    void getAppsByServiceIdPageable_ShouldReturnPaginatedApps_WhenValidServiceIdProvided() {
        // Given
        Long serviceId = 1L;
        int page = 0;
        String search = null;
        
        Nucleus nucleus = new Nucleus();
        nucleus.setUuaa("TEST,DEMO");
        
        AppSummaryDTO app1 = new AppSummaryDTO();
        app1.setName("app1");
        
        when(nucleusRepository.findById(serviceId)).thenReturn(Optional.of(nucleus));
        when(appsRepository.findAppSummaryByUuaa("TEST")).thenReturn(Arrays.asList(app1));
        when(appsRepository.findAppSummaryByUuaa("DEMO")).thenReturn(Collections.emptyList());
        when(serverRepository.findServersByName("app1")).thenReturn(Collections.emptyList());

        // When
        Page<AppSummaryDTO> result = appService.getAppsByServiceIdPageable(serviceId, page, search);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("app1", result.getContent().get(0).getName());
    }

    @Test
    void getAppsByServiceIdPageable_ShouldReturnEmptyPage_WhenServiceNotFound() {
        // Given
        Long serviceId = 999L;
        int page = 0;
        String search = null;
        
        when(nucleusRepository.findById(serviceId)).thenReturn(Optional.empty());

        // When
        Page<AppSummaryDTO> result = appService.getAppsByServiceIdPageable(serviceId, page, search);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.isEmpty());
        verify(appsRepository, never()).findAppSummaryByUuaa(any());
    }

    @Test
    void getAppsByServiceIdPageable_ShouldHandleSearchTerm_WhenSearchProvided() {
        // Given
        Long serviceId = 1L;
        int page = 0;
        String search = "app1";
        
        Nucleus nucleus = new Nucleus();
        nucleus.setUuaa("TEST");
        
        AppSummaryDTO app1 = new AppSummaryDTO();
        app1.setName("app1");
        AppSummaryDTO app2 = new AppSummaryDTO();
        app2.setName("app2");
        
        when(nucleusRepository.findById(serviceId)).thenReturn(Optional.of(nucleus));
        when(appsRepository.findAppSummaryByUuaa("TEST")).thenReturn(Arrays.asList(app1, app2));
        when(serverRepository.findServersByName("app1")).thenReturn(Collections.emptyList());

        // When
        Page<AppSummaryDTO> result = appService.getAppsByServiceIdPageable(serviceId, page, search);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("app1", result.getContent().get(0).getName());
    }

    // ====== Helper methods ======
    
    private AppSummaryDTO createAppSummaryWithMetrics(String name, Double coverage, Long bugs,
                                                      Long sastLow, Long sastMedium, Long sastHigh,
                                                      Long scaLow, Long scaMedium, Long scaHigh, Long scaCritical) {
        AppSummaryDTO app = new AppSummaryDTO();
        app.setName(name);
        app.setCoverage(coverage);
        app.setBugs(bugs);
        
        AppSummaryDTO.ChimeraSast sast = new AppSummaryDTO.ChimeraSast(sastLow, sastMedium, sastHigh);
        app.setChimeraSast(sast);
        
        AppSummaryDTO.ChimeraSca sca = new AppSummaryDTO.ChimeraSca(scaLow, scaMedium, scaHigh, scaCritical);
        app.setChimeraSca(sca);
        
        return app;
    }
}