package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.ComboValuesDTO;
import ar.com.bbva.fuentus.dto.NucleusGetRequestDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import ar.com.bbva.fuentus.repositories.VerticalRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NucleusServiceTest {

    @Mock
    private NucleusRepository nucleusRepository;

    @Mock
    private VerticalRepository verticalRepository;

    @InjectMocks
    private NucleusService nucleusService;

    private Nucleus testNucleus1;
    private Nucleus testNucleus2;
    private List<Nucleus> testNucleusList;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        testNucleus1 = new Nucleus();
        testNucleus1.setId(1L);
        testNucleus1.setServiceN1("TestService1");
        testNucleus1.setServiceN2("TestService2");
        testNucleus1.setOwnerServiceN1("TestOwner1");
        testNucleus1.setUuaa("TEST,DEV");
        testNucleus1.setArea("IT");
        testNucleus1.setOrgN1("Organization1");
        testNucleus1.setOrgN2fabrica("Organization2");

        testNucleus2 = new Nucleus();
        testNucleus2.setId(2L);
        testNucleus2.setServiceN1("AnotherService1");
        testNucleus2.setServiceN2("AnotherService2");
        testNucleus2.setOwnerServiceN1("AnotherOwner1");
        testNucleus2.setUuaa("PROD,QA");
        testNucleus2.setArea("Business");
        testNucleus2.setOrgN1("AnotherOrg1");
        testNucleus2.setOrgN2fabrica("AnotherOrg2");

        testNucleusList = Arrays.asList(testNucleus1, testNucleus2);
    }

    @Test
    void getAppsByUUAA_ShouldReturnListOfNucleusDTO_WhenValidUUAAProvided() {
        // Given
        String uuaa = "TEST";
        String searchPattern = "%" + uuaa + "%";
        when(nucleusRepository.findByUuaaLike(searchPattern)).thenReturn(testNucleusList);

        // When
        List<NucleusGetRequestDTO> result = nucleusService.getAppsByUUAA(uuaa);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Verificar primer elemento
        NucleusGetRequestDTO firstDto = result.get(0);
        assertEquals("TestService1", firstDto.getServiceN1());
        assertEquals("TestService2", firstDto.getServiceN2());
        assertEquals("TestOwner1", firstDto.getOwnerServiceN1());
        assertEquals(Arrays.asList("TEST", "DEV"), firstDto.getUuaa());

        // Verificar segundo elemento
        NucleusGetRequestDTO secondDto = result.get(1);
        assertEquals("AnotherService1", secondDto.getServiceN1());
        assertEquals("AnotherService2", secondDto.getServiceN2());
        assertEquals("AnotherOwner1", secondDto.getOwnerServiceN1());
        assertEquals(Arrays.asList("PROD", "QA"), secondDto.getUuaa());

        verify(nucleusRepository, times(1)).findByUuaaLike(searchPattern);
    }

    @Test
    void getAppsByUUAA_ShouldReturnEmptyList_WhenNoMatchingUUAA() {
        // Given
        String uuaa = "NONEXISTENT";
        String searchPattern = "%" + uuaa + "%";
        when(nucleusRepository.findByUuaaLike(searchPattern)).thenReturn(Collections.emptyList());

        // When
        List<NucleusGetRequestDTO> result = nucleusService.getAppsByUUAA(uuaa);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(nucleusRepository, times(1)).findByUuaaLike(searchPattern);
    }

    @Test
    void getAppsByUUAA_ShouldHandleNullUUAAInEntity() {
        // Given
        String uuaa = "TEST";
        String searchPattern = "%" + uuaa + "%";
        
        Nucleus nucleusWithNullUuaa = new Nucleus();
        nucleusWithNullUuaa.setId(3L);
        nucleusWithNullUuaa.setServiceN1("ServiceWithNullUuaa");
        nucleusWithNullUuaa.setUuaa(null);
        
        when(nucleusRepository.findByUuaaLike(searchPattern)).thenReturn(Arrays.asList(nucleusWithNullUuaa));

        // When
        List<NucleusGetRequestDTO> result = nucleusService.getAppsByUUAA(uuaa);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getUuaa().isEmpty());
        verify(nucleusRepository, times(1)).findByUuaaLike(searchPattern);
    }

    @Test
    void findByAnyField_ShouldReturnPageOfNucleusDTO_WhenValidSearchTerm() {
        // Given
        String searchTerm = "test";
        int page = 0;
        Pageable pageable = PageRequest.of(page, 10);
        Page<Nucleus> mockNucleusPage = new PageImpl<>(testNucleusList, pageable, 2);

        when(nucleusRepository.findByAnyFieldContainingPageable(searchTerm, pageable)).thenReturn(mockNucleusPage);

        // When
        Page<NucleusGetRequestDTO> result = nucleusService.findByAnyField(searchTerm, page);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("TestService1", result.getContent().get(0).getServiceN1());
        assertEquals("AnotherService1", result.getContent().get(1).getServiceN1());

        verify(nucleusRepository, times(1)).findByAnyFieldContainingPageable(searchTerm, pageable);
    }

    @Test
    void findByAnyField_ShouldReturnEmptyPage_WhenNoMatches() {
        // Given
        String searchTerm = "nonexistent";
        int page = 0;
        Pageable pageable = PageRequest.of(page, 10);
        Page<Nucleus> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(nucleusRepository.findByAnyFieldContainingPageable(searchTerm, pageable)).thenReturn(emptyPage);

        // When
        Page<NucleusGetRequestDTO> result = nucleusService.findByAnyField(searchTerm, page);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(nucleusRepository, times(1)).findByAnyFieldContainingPageable(searchTerm, pageable);
    }

    @Test
    void searchWithFilters_ShouldUseFilteredQuery_WhenAnyFilterPresent() {
        // Given
        String vertical = "Core";
        String uol2 = "FactoryA";
        String sn1 = null;
        String sn2 = null;
        String searchTerm = "ABCD";
        int page = 0;
        Pageable pageable = PageRequest.of(page, 10);
        Page<Nucleus> mockNucleusPage = new PageImpl<>(Collections.singletonList(testNucleus1), pageable, 1);

        when(nucleusRepository.findByFilterFieldsPageable(eq(vertical), eq(uol2), isNull(), isNull(), eq(searchTerm), any(Pageable.class)))
            .thenReturn(mockNucleusPage);

        // When
        Page<NucleusGetRequestDTO> result = nucleusService.searchWithFilters(searchTerm, vertical, uol2, sn1, sn2, page);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("TestService1", result.getContent().get(0).getServiceN1());

        verify(nucleusRepository, times(1))
            .findByFilterFieldsPageable(eq(vertical), eq(uol2), isNull(), isNull(), eq(searchTerm), any(Pageable.class));
        verify(nucleusRepository, never()).findByAnyFieldContainingPageable(anyString(), any(Pageable.class));
    }

    @Test
    void searchWithFilters_ShouldFallbackToGeneralSearch_WhenNoFiltersPresent() {
        // Given
        String searchTerm = "core";
        int page = 0;
        Pageable pageable = PageRequest.of(page, 10);
        Page<Nucleus> mockNucleusPage = new PageImpl<>(testNucleusList, pageable, 2);

        when(nucleusRepository.findByAnyFieldContainingPageable(anyString(), any(Pageable.class))).thenReturn(mockNucleusPage);

        // When
        Page<NucleusGetRequestDTO> result = nucleusService.searchWithFilters(searchTerm, null, "   ", "", null, page);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("TestService1", result.getContent().get(0).getServiceN1());

        verify(nucleusRepository, times(1)).findByAnyFieldContainingPageable(eq(searchTerm), any(Pageable.class));
        verify(nucleusRepository, never())
                .findByFilterFieldsPageable(anyString(), anyString(), anyString(), anyString(), anyString(), any(Pageable.class));
    }

    @Test
    void searchWithFilters_ShouldPassNullUuaa_WhenSearchTermBlank() {
        // Given
        String vertical = "Core";
        String uol2 = null;
        String sn1 = "ServiceA";
        String sn2 = "ServiceB";
        String searchTerm = "   ";
        int page = 0;
        Pageable pageable = PageRequest.of(page, 10);
        Page<Nucleus> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(nucleusRepository.findByFilterFieldsPageable(eq(vertical), isNull(), eq(sn1), eq(sn2), isNull(), any(Pageable.class)))
            .thenReturn(emptyPage);

        // When
        Page<NucleusGetRequestDTO> result = nucleusService.searchWithFilters(searchTerm, vertical, uol2, sn1, sn2, page);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(nucleusRepository, times(1))
            .findByFilterFieldsPageable(eq(vertical), isNull(), eq(sn1), eq(sn2), isNull(), any(Pageable.class));
        verify(nucleusRepository, never()).findByAnyFieldContainingPageable(anyString(), any(Pageable.class));
    }

    @Test
    void getNucleusByOptionalFields_ShouldReturnPageOfNucleusDTO_WhenAllFieldsProvided() {
        // Given
        String area = "IT";
        String orgN1 = "Organization1";
        String orgN2 = "Organization2";
        int page = 0;
        Pageable pageable = PageRequest.of(page, 10);
        Page<Nucleus> mockNucleusPage = new PageImpl<>(Arrays.asList(testNucleus1), pageable, 1);

        when(nucleusRepository.findByOptionalFieldsPageable(area, orgN1, orgN2, pageable)).thenReturn(mockNucleusPage);

        // When
        Page<NucleusGetRequestDTO> result = nucleusService.getNucleusByOptionalFields(area, orgN1, orgN2, page);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("TestService1", result.getContent().get(0).getServiceN1());

        verify(nucleusRepository, times(1)).findByOptionalFieldsPageable(area, orgN1, orgN2, pageable);
    }

    @Test
    void getNucleusByOptionalFields_ShouldReturnPageOfNucleusDTO_WhenSomeFieldsAreNull() {
        // Given
        String area = "IT";
        String orgN1 = null;
        String orgN2 = null;
        int page = 0;
        Pageable pageable = PageRequest.of(page, 10);
        Page<Nucleus> mockNucleusPage = new PageImpl<>(testNucleusList, pageable, 2);

        when(nucleusRepository.findByOptionalFieldsPageable(area, orgN1, orgN2, pageable)).thenReturn(mockNucleusPage);

        // When
        Page<NucleusGetRequestDTO> result = nucleusService.getNucleusByOptionalFields(area, orgN1, orgN2, page);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        verify(nucleusRepository, times(1)).findByOptionalFieldsPageable(area, orgN1, orgN2, pageable);
    }

    @Test
    void getNucleusByOptionalFields_ShouldReturnEmptyPage_WhenNoMatches() {
        // Given
        String area = "NonExistentArea";
        String orgN1 = "NonExistentOrg";
        String orgN2 = "NonExistentOrg2";
        int page = 0;
        Pageable pageable = PageRequest.of(page, 10);
        Page<Nucleus> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(nucleusRepository.findByOptionalFieldsPageable(area, orgN1, orgN2, pageable)).thenReturn(emptyPage);

        // When
        Page<NucleusGetRequestDTO> result = nucleusService.getNucleusByOptionalFields(area, orgN1, orgN2, page);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(nucleusRepository, times(1)).findByOptionalFieldsPageable(area, orgN1, orgN2, pageable);
    }

    @Test
    void getNucleusByOptionalFields_ShouldHandleHighPageNumber() {
        // Given
        String area = "IT";
        String orgN1 = "Organization1";
        String orgN2 = "Organization2";
        int page = 5;
        Pageable pageable = PageRequest.of(page, 10);
        Page<Nucleus> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(nucleusRepository.findByOptionalFieldsPageable(area, orgN1, orgN2, pageable)).thenReturn(emptyPage);

        // When
        Page<NucleusGetRequestDTO> result = nucleusService.getNucleusByOptionalFields(area, orgN1, orgN2, page);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(nucleusRepository, times(1)).findByOptionalFieldsPageable(area, orgN1, orgN2, pageable);
    }

    @Test
    void getComboValues_ShouldReturnChainedValues_WhenFiltersProvided() {
        // Given
        List<String> verticals = Arrays.asList("Core", "Channels");
        List<String> uol2Values = Collections.singletonList("FactoryA");
        List<String> sn1Values = Collections.singletonList("ServiceN1");
        List<String> sn2Values = Arrays.asList("ServiceN2", "ServiceN2B");

        when(verticalRepository.findDistinctVerticalNames()).thenReturn(verticals);
        when(nucleusRepository.findDistinctUol2ValuesWithFilters("Core")).thenReturn(uol2Values);
        when(nucleusRepository.findDistinctSn1ValuesWithFilters("Core", "FactoryA")).thenReturn(sn1Values);
        when(nucleusRepository.findDistinctSn2ValuesWithFilters("Core", "FactoryA", "ServiceN1"))
                .thenReturn(sn2Values);

        // When
        ComboValuesDTO result = nucleusService.getComboValues("Core", "FactoryA", "ServiceN1");

        // Then
        assertNotNull(result);
        assertEquals(verticals, result.getVerticals());
        assertEquals(uol2Values, result.getUol2Values());
        assertEquals(sn1Values, result.getSn1Values());
        assertEquals(sn2Values, result.getSn2Values());

        verify(verticalRepository, times(1)).findDistinctVerticalNames();
        verify(nucleusRepository, times(1)).findDistinctUol2ValuesWithFilters("Core");
        verify(nucleusRepository, times(1)).findDistinctSn1ValuesWithFilters("Core", "FactoryA");
        verify(nucleusRepository, times(1)).findDistinctSn2ValuesWithFilters("Core", "FactoryA", "ServiceN1");
    }

    @Test
    void getComboValues_ShouldHandleNullFiltersGracefully() {
        // Given
        List<String> verticals = Collections.singletonList("Any");
        List<String> emptyList = Collections.emptyList();

        when(verticalRepository.findDistinctVerticalNames()).thenReturn(verticals);
        when(nucleusRepository.findDistinctUol2ValuesWithFilters(isNull())).thenReturn(emptyList);
        when(nucleusRepository.findDistinctSn1ValuesWithFilters(isNull(), isNull())).thenReturn(emptyList);
        when(nucleusRepository.findDistinctSn2ValuesWithFilters(isNull(), isNull(), isNull())).thenReturn(emptyList);

        // When
        ComboValuesDTO result = nucleusService.getComboValues(null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(verticals, result.getVerticals());
        assertTrue(result.getUol2Values().isEmpty());
        assertTrue(result.getSn1Values().isEmpty());
        assertTrue(result.getSn2Values().isEmpty());

        verify(verticalRepository, times(1)).findDistinctVerticalNames();
        verify(nucleusRepository, times(1)).findDistinctUol2ValuesWithFilters(isNull());
        verify(nucleusRepository, times(1)).findDistinctSn1ValuesWithFilters(isNull(), isNull());
        verify(nucleusRepository, times(1)).findDistinctSn2ValuesWithFilters(isNull(), isNull(), isNull());
    }
}