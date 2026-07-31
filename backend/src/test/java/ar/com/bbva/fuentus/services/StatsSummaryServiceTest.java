package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.AppSummaryDTO;
import ar.com.bbva.fuentus.dto.CountSummaryDTO;
import ar.com.bbva.fuentus.dto.NucleusGetRequestDTO;
import ar.com.bbva.fuentus.dto.NucleusCoverageStatsSummary;
import ar.com.bbva.fuentus.dto.NucleusLevel;
import ar.com.bbva.fuentus.dto.ServerInfoDTO;
import ar.com.bbva.fuentus.dto.StatsSummaryDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.Vertical;
import ar.com.bbva.fuentus.entities.VerticalNucleus;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import ar.com.bbva.fuentus.repositories.SonarParamsRepository;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsSummaryServiceTest {

    @InjectMocks
    StatsSummaryService statsSummaryService;

    @Mock
    NucleusService nucleusService;

    @Mock
    AppService appService;

    @Mock
    NucleusRepository nucleusRepository;

    @Mock
    SonarParamsRepository sonarParamsRepository;

    @Test
    void getStatsByFiltersTest() {
        String area = "ARG";
        String orgN1 = "ORG1";
        String orgN2 = "ORG2";
        int page = 0;

        NucleusGetRequestDTO dto1 = new NucleusGetRequestDTO();
        dto1.setUuaa(Arrays.asList("UAAA", "UUAB"));
        NucleusGetRequestDTO dto2 = new NucleusGetRequestDTO();


        AppSummaryDTO app1 = new AppSummaryDTO();
        app1.setServers(Arrays.asList(new ServerInfoDTO()));
        AppSummaryDTO app2 = new AppSummaryDTO();

        List<AppSummaryDTO> appList = Arrays.asList(app1,app2);
        List<NucleusGetRequestDTO> dtoList = Arrays.asList(dto1, dto2);

        Pageable pageable = PageRequest.of(page, 10);

        Page<NucleusGetRequestDTO> pageResult = new PageImpl<>(dtoList, pageable, dtoList.size());

        when(nucleusService.getNucleusByOptionalFields(area, orgN1, orgN2, page)).thenReturn(pageResult);
        when(appService.getAllAppsByUUAAComplete(anyString())).thenReturn(appList);

        List<StatsSummaryDTO> result = statsSummaryService.getStatsSummaryByFilters(area, orgN1, orgN2, page);

        // Aquí puedes usar pageResult para tus assertions o mocks
        assertNotNull(pageResult);
        assertEquals(2, result.size());
    }

    @Test
    void getCountSummaryTest() {
        String vertical = "VERT";
        String fabrica = "FAB";
        String sn1 = "SN1";
        String sn2 = "SN2";
        String uuaa = "UUAAX";

        Nucleus nucleus1 = new Nucleus();
        nucleus1.setOrgN2fabrica(fabrica);
        nucleus1.setServiceN1(sn1);
        nucleus1.setServiceN2(sn2);
        nucleus1.setUuaa(uuaa);

        Vertical verticalEntity = new Vertical();
        verticalEntity.setId(1L);
        VerticalNucleus verticalNucleus = new VerticalNucleus();
        verticalNucleus.setVertical(verticalEntity);
        nucleus1.setVerticalNucleus(Arrays.asList(verticalNucleus));

        List<Nucleus> nucleusList = Arrays.asList(nucleus1);
        Page<Nucleus> pageResult = new PageImpl<>(nucleusList, PageRequest.of(0, 1000), nucleusList.size());

        when(nucleusRepository.findByFilterFieldsPageable(vertical, fabrica, sn1, sn2, uuaa, PageRequest.of(0, 1000))).thenReturn(pageResult);

        CountSummaryDTO result = statsSummaryService.getCountSummary(vertical, fabrica, sn1, sn2, uuaa);
        assertNotNull(result);
        assertEquals(1, result.getTotalVerticales());
        assertEquals(1, result.getTotalFabricas());
        assertEquals(1, result.getTotalSn1());
        assertEquals(1, result.getTotalSn2());
        assertEquals(1, result.getTotalUuaas());
    }

    @Test
    void getCoverageStatsByLevelTest_vertical() {
        String vertical = null;
        String uol2 = null;
        String sn1 = null;
        String sn2 = null;
        String uuaa = null;

        Object[] row = new Object[]{"VERTICAL", 85.5};
        when(sonarParamsRepository.getCoverageByVertical()).thenReturn(Arrays.<Object[]>asList(row));

        List<NucleusCoverageStatsSummary> result = statsSummaryService.getCoverageStatsByLevel(vertical, uol2, sn1, sn2, uuaa);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("VERTICAL", result.get(0).getLabel());
        assertEquals(85.5, result.get(0).getCoveragePercentage());
        assertEquals(NucleusLevel.VERTICAL, result.get(0).getNucleusLevel());
    }

    @Test
    void getCoverageStatsByLevelTest_uol2() {
        String vertical = "VERTICAL";
        String uol2 = null;
        String sn1 = null;
        String sn2 = null;
        String uuaa = null;

        Object[] row = new Object[]{"UOL2", 90.0};
        when(sonarParamsRepository.getCoverageByUol2ForVertical(vertical)).thenReturn(Arrays.<Object[]>asList(row));

        List<NucleusCoverageStatsSummary> result = statsSummaryService.getCoverageStatsByLevel(vertical, uol2, sn1, sn2, uuaa);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("UOL2", result.get(0).getLabel());
        assertEquals(90.0, result.get(0).getCoveragePercentage());
        assertEquals(NucleusLevel.UOL2, result.get(0).getNucleusLevel());
    }

    @Test
    void getCoverageStatsByLevelTest_sn1() {
        String vertical = "VERTICAL";
        String uol2 = "UOL2";
        String sn1 = null;
        String sn2 = null;
        String uuaa = null;

        Object[] row = new Object[]{"SN1", 95.0};
        when(sonarParamsRepository.getCoverageBySn1ForVerticalAndUol2(vertical, uol2)).thenReturn(Arrays.<Object[]>asList(row));

        List<NucleusCoverageStatsSummary> result = statsSummaryService.getCoverageStatsByLevel(vertical, uol2, sn1, sn2, uuaa);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("SN1", result.get(0).getLabel());
        assertEquals(95.0, result.get(0).getCoveragePercentage());
        assertEquals(NucleusLevel.SN1, result.get(0).getNucleusLevel());
    }

    @Test
    void getCoverageStatsByLevelTest_sn2() {
        String vertical = "VERTICAL";
        String uol2 = "UOL2";
        String sn1 = "SN1";
        String sn2 = null;
        String uuaa = null;

        Object[] row = new Object[]{"SN2", 80.0};
        when(sonarParamsRepository.getCoverageBySn2ForVerticalUol2AndSn1(vertical, uol2, sn1)).thenReturn(Arrays.<Object[]>asList(row));

        List<NucleusCoverageStatsSummary> result = statsSummaryService.getCoverageStatsByLevel(vertical, uol2, sn1, sn2, uuaa);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("SN2", result.get(0).getLabel());
        assertEquals(80.0, result.get(0).getCoveragePercentage());
        assertEquals(NucleusLevel.SN2, result.get(0).getNucleusLevel());
    }

    @Test
    void getStatsSummaryByFiltersTest_withMultiplePages() {
        String area = "ARG";
        String orgN1 = "ORG1";
        String orgN2 = "ORG2";
        int page = 0;

        NucleusGetRequestDTO dto1 = new NucleusGetRequestDTO();
        dto1.setUuaa(Arrays.asList("UAAA"));
        NucleusGetRequestDTO dto2 = new NucleusGetRequestDTO();
        dto2.setUuaa(Arrays.asList("UUAB"));

        Page<NucleusGetRequestDTO> firstPage = new PageImpl<>(Arrays.asList(dto1), PageRequest.of(0, 10), 2);
        Page<NucleusGetRequestDTO> secondPage = new PageImpl<>(Arrays.asList(dto2), PageRequest.of(1, 10), 2);
        Page<NucleusGetRequestDTO> emptyPage = new PageImpl<>(Arrays.<NucleusGetRequestDTO>asList(), PageRequest.of(2, 10), 2);

        AppSummaryDTO app1 = new AppSummaryDTO();
        app1.setName("Test App 1");
        app1.setBitbucketUrl("http://bitbucket.com/test1");
        app1.setLanguage("Java");
        app1.setMonolith(true);

        when(nucleusService.getNucleusByOptionalFields(area, orgN1, orgN2, 0)).thenReturn(firstPage);
        when(appService.getAllAppsByUUAAComplete("UAAA")).thenReturn(Arrays.asList(app1));
        
        List<StatsSummaryDTO> result = statsSummaryService.getStatsSummaryByFilters(area, orgN1, orgN2, page);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(s -> "UAAA".equals(s.getUuaa())));
    }

    @Test
    void getCountSummaryTest_withMultipleUuaasAndNullValues() {
        String vertical = "VERT";
        String fabrica = "FAB";
        String sn1 = "SN1";
        String sn2 = "SN2";
        String uuaa = "UUAAX";

        Nucleus nucleus1 = new Nucleus();
        nucleus1.setOrgN2fabrica(fabrica);
        nucleus1.setServiceN1(sn1);
        nucleus1.setServiceN2(sn2);
        nucleus1.setUuaa("UUAA1,UUAA2,UUAA3"); // Multiple UUAAs

        Nucleus nucleus2 = new Nucleus();
        nucleus2.setOrgN2fabrica(null); // Null values
        nucleus2.setServiceN1("");      // Empty values
        nucleus2.setServiceN2("  ");    // Whitespace values
        nucleus2.setUuaa(null);

        Vertical verticalEntity1 = new Vertical();
        verticalEntity1.setId(1L);
        Vertical verticalEntity2 = new Vertical();
        verticalEntity2.setId(2L);

        VerticalNucleus verticalNucleus1 = new VerticalNucleus();
        verticalNucleus1.setVertical(verticalEntity1);
        VerticalNucleus verticalNucleus2 = new VerticalNucleus();
        verticalNucleus2.setVertical(verticalEntity2);

        nucleus1.setVerticalNucleus(Arrays.asList(verticalNucleus1, verticalNucleus2));
        nucleus2.setVerticalNucleus(Arrays.<VerticalNucleus>asList());

        List<Nucleus> nucleusList = Arrays.asList(nucleus1, nucleus2);
        Page<Nucleus> pageResult = new PageImpl<>(nucleusList, PageRequest.of(0, 1000), nucleusList.size());

        when(nucleusRepository.findByFilterFieldsPageable(vertical, fabrica, sn1, sn2, uuaa, PageRequest.of(0, 1000))).thenReturn(pageResult);

        CountSummaryDTO result = statsSummaryService.getCountSummary(vertical, fabrica, sn1, sn2, uuaa);
        assertNotNull(result);
        assertEquals(2, result.getTotalVerticales()); // 2 different verticals
        assertEquals(1, result.getTotalFabricas());   // Only non-null/non-empty fabrica
        assertEquals(1, result.getTotalSn1());        // Only non-null/non-empty sn1
        assertEquals(1, result.getTotalSn2());        // Only non-null/non-empty sn2
        assertEquals(3, result.getTotalUuaas());      // 3 UUAAs from comma-separated string
    }

    @Test
    void getCoverageStatsByLevelTest_withNullData() {
        String vertical = null;
        String uol2 = null;
        String sn1 = null;
        String sn2 = null;
        String uuaa = null;

        Object[] rowWithNullName = new Object[]{null, 85.5};
        Object[] rowWithNullCoverage = new Object[]{"VERTICAL", null};
        when(sonarParamsRepository.getCoverageByVertical()).thenReturn(Arrays.<Object[]>asList(rowWithNullName, rowWithNullCoverage));

        List<NucleusCoverageStatsSummary> result = statsSummaryService.getCoverageStatsByLevel(vertical, uol2, sn1, sn2, uuaa);
        assertNotNull(result);
        assertEquals(0, result.size()); // No valid data should be returned
    }

    @Test
    void getCoverageStatsByLevelTest_emptyStringParameters() {
        String vertical = "";
        String uol2 = "";
        String sn1 = "";
        String sn2 = "";
        String uuaa = "";

        Object[] row = new Object[]{"VERTICAL", 85.5};
        when(sonarParamsRepository.getCoverageByVertical()).thenReturn(Arrays.<Object[]>asList(row));

        List<NucleusCoverageStatsSummary> result = statsSummaryService.getCoverageStatsByLevel(vertical, uol2, sn1, sn2, uuaa);
        assertNotNull(result);
        assertEquals(1, result.size()); // Empty strings should be treated as null
        assertEquals("VERTICAL", result.get(0).getLabel());
        assertEquals(85.5, result.get(0).getCoveragePercentage());
        assertEquals(NucleusLevel.VERTICAL, result.get(0).getNucleusLevel());
    }

    @Test
    void getCoverageStatsByLevelTest_uuaa() {
        String vertical = "VERTICAL";
        String uol2 = "UOL2";
        String sn1 = "SN1";
        String sn2 = "SN2";
        String uuaa = null;

        // Simular datos con UUAAs concatenadas
        Object[] row1 = new Object[]{"UUAA1", "UUAA2", "UUAA3"};
        when(sonarParamsRepository.getCoverageByUuaaForVerticalUol2Sn1AndSn2(vertical, uol2, sn1, sn2))
            .thenReturn(Arrays.<Object[]>asList(row1));
        
        // Mock para cada UUAA individual
        when(sonarParamsRepository.getCoverageBySpecificUuaa("UUAA1")).thenReturn(75.0);
        when(sonarParamsRepository.getCoverageBySpecificUuaa("UUAA2")).thenReturn(82.5);
        when(sonarParamsRepository.getCoverageBySpecificUuaa("UUAA3")).thenReturn(90.0);

        List<NucleusCoverageStatsSummary> result = statsSummaryService.getCoverageStatsByLevel(vertical, uol2, sn1, sn2, uuaa);
        
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(r -> "UUAA1".equals(r.getLabel()) && r.getCoveragePercentage() == 75.0));
        assertTrue(result.stream().anyMatch(r -> "UUAA2".equals(r.getLabel()) && r.getCoveragePercentage() == 82.5));
        assertTrue(result.stream().anyMatch(r -> "UUAA3".equals(r.getLabel()) && r.getCoveragePercentage() == 90.0));
        assertTrue(result.stream().allMatch(r -> r.getNucleusLevel() == NucleusLevel.UUAA));
    }

    @Test
    void getCoverageStatsByLevelTest_uuaaWithNullCoverage() {
        String vertical = "VERTICAL";
        String uol2 = "UOL2";
        String sn1 = "SN1";
        String sn2 = "SN2";
        String uuaa = null;

        Object[] row1 = new Object[]{"UUAA1", "UUAA2"};
        when(sonarParamsRepository.getCoverageByUuaaForVerticalUol2Sn1AndSn2(vertical, uol2, sn1, sn2))
            .thenReturn(Arrays.<Object[]>asList(row1));
        
        when(sonarParamsRepository.getCoverageBySpecificUuaa("UUAA1")).thenReturn(null);
        when(sonarParamsRepository.getCoverageBySpecificUuaa("UUAA2")).thenReturn(85.0);

        List<NucleusCoverageStatsSummary> result = statsSummaryService.getCoverageStatsByLevel(vertical, uol2, sn1, sn2, uuaa);
        
        assertNotNull(result);
        assertEquals(1, result.size()); // Solo UUAA2 debe estar en el resultado
        assertEquals("UUAA2", result.get(0).getLabel());
        assertEquals(85.0, result.get(0).getCoveragePercentage());
        assertEquals(NucleusLevel.UUAA, result.get(0).getNucleusLevel());
    }

    @Test
    void getCoverageStatsByLevelTest_uuaaWithException() {
        String vertical = "VERTICAL";
        String uol2 = "UOL2";
        String sn1 = "SN1";
        String sn2 = "SN2";
        String uuaa = null;

        Object[] row1 = new Object[]{"UUAA1", "UUAA2"};
        when(sonarParamsRepository.getCoverageByUuaaForVerticalUol2Sn1AndSn2(vertical, uol2, sn1, sn2))
            .thenReturn(Arrays.<Object[]>asList(row1));
        
        when(sonarParamsRepository.getCoverageBySpecificUuaa("UUAA1")).thenThrow(new RuntimeException("DB Error"));
        when(sonarParamsRepository.getCoverageBySpecificUuaa("UUAA2")).thenReturn(88.0);

        List<NucleusCoverageStatsSummary> result = statsSummaryService.getCoverageStatsByLevel(vertical, uol2, sn1, sn2, uuaa);
        
        assertNotNull(result);
        assertEquals(1, result.size()); // Solo UUAA2 debe estar, UUAA1 tuvo excepción
        assertEquals("UUAA2", result.get(0).getLabel());
        assertEquals(88.0, result.get(0).getCoveragePercentage());
    }

    @Test
    void getCoverageStatsByLevelTest_app() {
        String vertical = "VERTICAL";
        String uol2 = "UOL2";
        String sn1 = "SN1";
        String sn2 = "SN2";
        String uuaa = "UUAA1";

        Object[] row1 = new Object[]{"App1", 78.5};
        Object[] row2 = new Object[]{"App2", 92.0};
        when(sonarParamsRepository.getCoverageByAppForUuaa(vertical, uol2, sn1, sn2, uuaa))
            .thenReturn(Arrays.<Object[]>asList(row1, row2));

        List<NucleusCoverageStatsSummary> result = statsSummaryService.getCoverageStatsByLevel(vertical, uol2, sn1, sn2, uuaa);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("App1", result.get(0).getLabel());
        assertEquals(78.5, result.get(0).getCoveragePercentage());
        assertEquals(NucleusLevel.APP, result.get(0).getNucleusLevel());
        assertEquals("App2", result.get(1).getLabel());
        assertEquals(92.0, result.get(1).getCoveragePercentage());
        assertEquals(NucleusLevel.APP, result.get(1).getNucleusLevel());
    }

    @Test
    void getCoverageStatsByLevelTest_appWithNullData() {
        String vertical = "VERTICAL";
        String uol2 = "UOL2";
        String sn1 = "SN1";
        String sn2 = "SN2";
        String uuaa = "UUAA1";

        Object[] rowWithNullName = new Object[]{null, 78.5};
        Object[] rowWithNullCoverage = new Object[]{"App2", null};
        Object[] validRow = new Object[]{"App3", 85.0};
        when(sonarParamsRepository.getCoverageByAppForUuaa(vertical, uol2, sn1, sn2, uuaa))
            .thenReturn(Arrays.<Object[]>asList(rowWithNullName, rowWithNullCoverage, validRow));

        List<NucleusCoverageStatsSummary> result = statsSummaryService.getCoverageStatsByLevel(vertical, uol2, sn1, sn2, uuaa);
        
        assertNotNull(result);
        assertEquals(1, result.size()); // Solo el registro válido
        assertEquals("App3", result.get(0).getLabel());
        assertEquals(85.0, result.get(0).getCoveragePercentage());
        assertEquals(NucleusLevel.APP, result.get(0).getNucleusLevel());
    }
}