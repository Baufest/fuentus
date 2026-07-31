package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.OrgN2FabricaResponseDto;
import ar.com.bbva.fuentus.dto.VerticalRequestDto;
import ar.com.bbva.fuentus.dto.VerticalResponseDto;
import ar.com.bbva.fuentus.dto.VerticalUpdateDto;
import ar.com.bbva.fuentus.services.VerticalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VerticalController.class)
class VerticalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VerticalService verticalService;

    private VerticalRequestDto testVerticalRequest;
    private VerticalResponseDto testVerticalResponse1;
    private VerticalResponseDto testVerticalResponse2;
    private VerticalUpdateDto testVerticalUpdate;
    private OrgN2FabricaResponseDto testOrgN2Fabrica;
    private List<VerticalResponseDto> verticalList;
    private List<OrgN2FabricaResponseDto> orgN2FabricaList;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba para VerticalRequestDto
        testVerticalRequest = new VerticalRequestDto();
        testVerticalRequest.setName("Vertical Test");
        testVerticalRequest.setRefVertical("VRT001");
        testVerticalRequest.setOrgN2fabricaList(Arrays.asList("Fabrica1", "Fabrica2"));

        // Configurar datos de prueba para VerticalResponseDto
        VerticalResponseDto.FactoryDTO factory1 = new VerticalResponseDto.FactoryDTO();
        factory1.setName("Fabrica1");
        factory1.setOwnerFactory("Owner1");

        VerticalResponseDto.FactoryDTO factory2 = new VerticalResponseDto.FactoryDTO();
        factory2.setName("Fabrica2");
        factory2.setOwnerFactory("Owner2");

        testVerticalResponse1 = new VerticalResponseDto();
        testVerticalResponse1.setId(1L);
        testVerticalResponse1.setName("Vertical Test");
        testVerticalResponse1.setRefVertical("VRT001");
        testVerticalResponse1.setOrgN2fabrica(Arrays.asList(factory1, factory2));

        testVerticalResponse2 = new VerticalResponseDto();
        testVerticalResponse2.setId(2L);
        testVerticalResponse2.setName("Vertical Test 2");
        testVerticalResponse2.setRefVertical("VRT002");
        testVerticalResponse2.setOrgN2fabrica(Collections.singletonList(factory1));

        verticalList = Arrays.asList(testVerticalResponse1, testVerticalResponse2);

        // Configurar datos de prueba para VerticalUpdateDto
        testVerticalUpdate = new VerticalUpdateDto();
        testVerticalUpdate.setName("Updated Vertical");
        testVerticalUpdate.setRefVertical("VRT001-UPD");
        testVerticalUpdate.setOrgN2fabricaList(Arrays.asList("Fabrica1", "Fabrica3"));

        // Configurar datos de prueba para OrgN2FabricaResponseDto
        testOrgN2Fabrica = new OrgN2FabricaResponseDto();
        testOrgN2Fabrica.setOrgN2("OrgN2-001");
        testOrgN2Fabrica.setRefVertical("VRT001");
        testOrgN2Fabrica.setVerticales(Collections.singletonList(testVerticalResponse1));

        orgN2FabricaList = Collections.singletonList(testOrgN2Fabrica);
    }

    // Tests para createVertical (POST /verticales)

    @Test
    void createVertical_ShouldReturnCreated_WhenValidRequest() throws Exception {
        when(verticalService.createVertical(any(VerticalRequestDto.class))).thenReturn(testVerticalResponse1);

        mockMvc.perform(post("/verticales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVerticalRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Vertical Test"))
                .andExpect(jsonPath("$.refVertical").value("VRT001"))
                .andExpect(jsonPath("$.orgN2fabrica[0].name").value("Fabrica1"))
                .andExpect(jsonPath("$.orgN2fabrica[1].name").value("Fabrica2"));

        verify(verticalService).createVertical(any(VerticalRequestDto.class));
    }

    @Test
    void createVertical_ShouldReturnBadRequest_WhenIllegalArgumentException() throws Exception {
        when(verticalService.createVertical(any(VerticalRequestDto.class)))
                .thenThrow(new IllegalArgumentException("Invalid vertical data"));

        mockMvc.perform(post("/verticales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVerticalRequest)))
                .andExpect(status().isBadRequest());

        verify(verticalService).createVertical(any(VerticalRequestDto.class));
    }

    @Test
    void createVertical_ShouldReturnInternalServerError_WhenExceptionOccurs() throws Exception {
        when(verticalService.createVertical(any(VerticalRequestDto.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/verticales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVerticalRequest)))
                .andExpect(status().isInternalServerError());

        verify(verticalService).createVertical(any(VerticalRequestDto.class));
    }

    // Tests para getAllVerticalesWithOrgN2Fabrica (GET /verticales)

    @Test
    void getAllVerticalesWithOrgN2Fabrica_ShouldReturnAllVerticales() throws Exception {
        when(verticalService.getAllVerticalesWithOrgN2Fabrica()).thenReturn(verticalList);

        mockMvc.perform(get("/verticales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Vertical Test"))
                .andExpect(jsonPath("$[0].refVertical").value("VRT001"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Vertical Test 2"))
                .andExpect(jsonPath("$[1].refVertical").value("VRT002"));

        verify(verticalService).getAllVerticalesWithOrgN2Fabrica();
    }

    @Test
    void getAllVerticalesWithOrgN2Fabrica_ShouldReturnEmptyList_WhenNoVerticales() throws Exception {
        when(verticalService.getAllVerticalesWithOrgN2Fabrica()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/verticales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(verticalService).getAllVerticalesWithOrgN2Fabrica();
    }

    @Test
    void getAllVerticalesWithOrgN2Fabrica_ShouldReturnInternalServerError_WhenExceptionOccurs() throws Exception {
        when(verticalService.getAllVerticalesWithOrgN2Fabrica()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/verticales"))
                .andExpect(status().isInternalServerError());

        verify(verticalService).getAllVerticalesWithOrgN2Fabrica();
    }

    // Tests para updateVertical (PUT /verticales/{id})

    @Test
    void updateVertical_ShouldReturnUpdatedVertical_WhenValidRequest() throws Exception {
        VerticalResponseDto updatedResponse = new VerticalResponseDto();
        updatedResponse.setId(1L);
        updatedResponse.setName("Updated Vertical");
        updatedResponse.setRefVertical("VRT001-UPD");
        
        when(verticalService.updateVertical(eq(1L), any(VerticalUpdateDto.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/verticales/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVerticalUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Vertical"))
                .andExpect(jsonPath("$.refVertical").value("VRT001-UPD"));

        verify(verticalService).updateVertical(eq(1L), any(VerticalUpdateDto.class));
    }

    @Test
    void updateVertical_ShouldReturnBadRequest_WhenIllegalArgumentException() throws Exception {
        when(verticalService.updateVertical(eq(999L), any(VerticalUpdateDto.class)))
                .thenThrow(new IllegalArgumentException("Vertical not found"));

        mockMvc.perform(put("/verticales/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVerticalUpdate)))
                .andExpect(status().isBadRequest());

        verify(verticalService).updateVertical(eq(999L), any(VerticalUpdateDto.class));
    }

    @Test
    void updateVertical_ShouldReturnInternalServerError_WhenExceptionOccurs() throws Exception {
        when(verticalService.updateVertical(eq(1L), any(VerticalUpdateDto.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(put("/verticales/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVerticalUpdate)))
                .andExpect(status().isInternalServerError());

        verify(verticalService).updateVertical(eq(1L), any(VerticalUpdateDto.class));
    }

    // Tests para getAllOrgN1FabricasWithVerticales (GET /verticales/factories)

    @Test
    void getAllOrgN1FabricasWithVerticales_ShouldReturnAllOrgN2Fabricas() throws Exception {
        when(verticalService.getAllOrgN2FabricasWithVerticales()).thenReturn(orgN2FabricaList);

        mockMvc.perform(get("/verticales/factories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orgN2").value("OrgN2-001"))
                .andExpect(jsonPath("$[0].refVertical").value("VRT001"))
                .andExpect(jsonPath("$[0].verticales[0].name").value("Vertical Test"));

        verify(verticalService).getAllOrgN2FabricasWithVerticales();
    }

    @Test
    void getAllOrgN1FabricasWithVerticales_ShouldReturnEmptyList_WhenNoData() throws Exception {
        when(verticalService.getAllOrgN2FabricasWithVerticales()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/verticales/factories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(verticalService).getAllOrgN2FabricasWithVerticales();
    }

    @Test
    void getAllOrgN1FabricasWithVerticales_ShouldReturnInternalServerError_WhenExceptionOccurs() throws Exception {
        when(verticalService.getAllOrgN2FabricasWithVerticales()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/verticales/factories"))
                .andExpect(status().isInternalServerError());

        verify(verticalService).getAllOrgN2FabricasWithVerticales();
    }
}
