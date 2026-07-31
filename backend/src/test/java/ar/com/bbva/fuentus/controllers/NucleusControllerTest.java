package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.NucleusGetRequestDTO;
import ar.com.bbva.fuentus.services.NucleusService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NucleusController.class)
class NucleusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NucleusService nucleusService;

    private NucleusGetRequestDTO testNucleusDTO;
    private List<NucleusGetRequestDTO> testNucleusList;
    private Page<NucleusGetRequestDTO> testNucleusPage;

    @BeforeEach
    void setUp() {
        testNucleusDTO = new NucleusGetRequestDTO();
        testNucleusDTO.setServiceN1("TestService1");
        testNucleusDTO.setServiceN2("TestService2");
        testNucleusDTO.setOwnerServiceN1("TestOwner1");
        testNucleusDTO.setUuaa(Arrays.asList("TEST", "DEV"));

        testNucleusList = Arrays.asList(testNucleusDTO);
        testNucleusPage = new PageImpl<>(testNucleusList, PageRequest.of(0, 10), 1);
    }

    @Test
    void getAppsByUUAA_ShouldReturnListOfNucleus_WhenValidUUAAProvided() throws Exception {
        // Given
        String uuaa = "TEST";
        when(nucleusService.getAppsByUUAA(uuaa)).thenReturn(testNucleusList);

        // When & Then
        mockMvc.perform(get("/nucleus/{uuaa}", uuaa))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].serviceN1").value("TestService1"))
                .andExpect(jsonPath("$[0].serviceN2").value("TestService2"))
                .andExpect(jsonPath("$[0].ownerServiceN1").value("TestOwner1"))
                .andExpect(jsonPath("$[0].uuaa").isArray())
                .andExpect(jsonPath("$[0].uuaa.length()").value(2))
                .andExpect(jsonPath("$[0].uuaa[0]").value("TEST"))
                .andExpect(jsonPath("$[0].uuaa[1]").value("DEV"));
    }

    @Test
    void getAppsByUUAA_ShouldReturnEmptyList_WhenNoNucleusFound() throws Exception {
        // Given
        String uuaa = "NONEXISTENT";
        when(nucleusService.getAppsByUUAA(uuaa)).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/nucleus/{uuaa}", uuaa))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAppsByUUAA_ShouldHandleSpecialCharacters_InUUAAParam() throws Exception {
        // Given
        String uuaa = "TEST-123_ABC";
        when(nucleusService.getAppsByUUAA(uuaa)).thenReturn(testNucleusList);

        // When & Then
        mockMvc.perform(get("/nucleus/{uuaa}", uuaa))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void searchNucleus_ShouldReturnPageOfNucleus_WhenValidSearchTermProvided() throws Exception {
        // Given
        String searchTerm = "test";
        when(nucleusService.searchWithFilters(eq(searchTerm), isNull(), isNull(), isNull(), isNull(), eq(0))).thenReturn(testNucleusPage);

        // When & Then
        mockMvc.perform(get("/nucleus/search")
                        .param("q", searchTerm))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].serviceN1").value("TestService1"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    void searchNucleus_ShouldReturnPageWithPageParam_WhenPageParamProvided() throws Exception {
        // Given
        String searchTerm = "test";
        int page = 2;
        // Crear página con 31 elementos totales (para que en página 2 con size 10 tenga sentido)
        Page<NucleusGetRequestDTO> pageTwo = new PageImpl<>(testNucleusList, PageRequest.of(page, 10), 31);
        when(nucleusService.searchWithFilters(eq("test"), isNull(), isNull(), isNull(), isNull(), eq(2))).thenReturn(pageTwo);

        // When & Then
        mockMvc.perform(get("/nucleus/search")
                        .param("q", searchTerm)
                        .param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(31))
                .andExpect(jsonPath("$.number").value(page));
    }

    @Test
    void searchNucleus_ShouldReturnEmptyPage_WhenNoMatchesFound() throws Exception {
        // Given
        String searchTerm = "nonexistent";
        Page<NucleusGetRequestDTO> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        when(nucleusService.searchWithFilters(eq(searchTerm), isNull(), isNull(), isNull(), isNull(), eq(0))).thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(get("/nucleus/search")
                        .param("q", searchTerm))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0));
    }

    @Test
    void getNucleusByOptionalFields_ShouldReturnPage_WhenAllFiltersProvided() throws Exception {
        // Given
        String area = "IT";
        String orgN1 = "Organization1";
        String orgN2fabrica = "Fabrica1";
        when(nucleusService.getNucleusByOptionalFields(eq(area), eq(orgN1), eq(orgN2fabrica), eq(0)))
                .thenReturn(testNucleusPage);

        // When & Then
        mockMvc.perform(get("/nucleus/filters")
                        .param("area", area)
                        .param("orgN1", orgN1)
                        .param("orgN2fabrica", orgN2fabrica))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].serviceN1").value("TestService1"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getNucleusByOptionalFields_ShouldReturnPage_WhenSomeFiltersProvided() throws Exception {
        // Given
        String area = "IT";
        when(nucleusService.getNucleusByOptionalFields(eq(area), isNull(), isNull(), eq(0)))
                .thenReturn(testNucleusPage);

        // When & Then
        mockMvc.perform(get("/nucleus/filters")
                        .param("area", area))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getNucleusByOptionalFields_ShouldReturnPage_WhenNoFiltersProvided() throws Exception {
        // Given
        when(nucleusService.getNucleusByOptionalFields(isNull(), isNull(), isNull(), eq(0)))
                .thenReturn(testNucleusPage);

        // When & Then
        mockMvc.perform(get("/nucleus/filters"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getNucleusByOptionalFields_ShouldReturnPageWithPageParam_WhenPageParamProvided() throws Exception {
        // Given
        String area = "IT";
        int page = 3;
        Page<NucleusGetRequestDTO> pageThree = new PageImpl<>(testNucleusList, PageRequest.of(page, 10), 40);
        when(nucleusService.getNucleusByOptionalFields(eq(area), isNull(), isNull(), eq(page)))
                .thenReturn(pageThree);

        // When & Then
        mockMvc.perform(get("/nucleus/filters")
                        .param("area", area)
                        .param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(40))
                .andExpect(jsonPath("$.number").value(page));
    }

    @Test
    void getNucleusByOptionalFields_ShouldReturnEmptyPage_WhenNoMatchesFound() throws Exception {
        // Given
        String area = "NonExistentArea";
        Page<NucleusGetRequestDTO> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        when(nucleusService.getNucleusByOptionalFields(eq(area), isNull(), isNull(), eq(0)))
                .thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(get("/nucleus/filters")
                        .param("area", area))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void searchNucleus_ShouldHandleSpecialCharactersInSearchTerm() throws Exception {
        // Given
        String searchTerm = "test-service_123";
        when(nucleusService.searchWithFilters(eq(searchTerm), isNull(), isNull(), isNull(), isNull(), eq(0))).thenReturn(testNucleusPage);

        // When & Then
        mockMvc.perform(get("/nucleus/search")
                        .param("q", searchTerm))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}