package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.ProductividadDTO;
import ar.com.bbva.fuentus.services.ProductividadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductividadController.class)
class ProductividadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductividadService productividadService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductividadDTO testDTO;
    private LocalDate testFecha;

    @BeforeEach
    void setUp() {
        testFecha = LocalDate.of(2025, 12, 30);
        
        testDTO = new ProductividadDTO();
        testDTO.setId(1L);
        testDTO.setNucleusId(100L);
        testDTO.setFeatures(11);
        testDTO.setFtesDirectos(10.58);
        testDTO.setFtesIndirectos(4.90);
        testDTO.setFecha(testFecha);
    }

    @Test
    void importProductividad_ShouldReturnSuccess_WhenValidFileProvided() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "productividad.csv",
            "text/csv",
            "Servicio N2,Features,FTEs Directos,FTEs Indirectos\nETPB,11,10.58,4.90".getBytes()
        );
        
        List<String> successMessages = Arrays.asList("Importación exitosa: 1 registros procesados");
        when(productividadService.importFromCSV(any(), any(LocalDate.class))).thenReturn(successMessages);

        // When & Then
        mockMvc.perform(multipart("/productividad/import")
                .file(file)
                .param("fecha", "2025-12-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.messages").isArray())
                .andExpect(jsonPath("$.messages[0]").value("Importación exitosa: 1 registros procesados"));

        verify(productividadService, times(1)).importFromCSV(any(), eq(testFecha));
    }

    @Test
    void importProductividad_ShouldReturnPartialContent_WhenSomeRecordsFail() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "productividad.csv",
            "text/csv",
            "Servicio N2,Features,FTEs Directos,FTEs Indirectos\nETPB,11,10.58,4.90".getBytes()
        );
        
        List<String> errorMessages = Arrays.asList(
            "Importación parcial: 1 exitosos, 1 errores",
            "Error en fila 2: ETPB"
        );
        when(productividadService.importFromCSV(any(), any(LocalDate.class))).thenReturn(errorMessages);

        // When & Then
        mockMvc.perform(multipart("/productividad/import")
                .file(file)
                .param("fecha", "2025-12-30"))
                .andExpect(status().isPartialContent())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.messages").isArray())
                .andExpect(jsonPath("$.messages[0]").value("Importación parcial: 1 exitosos, 1 errores"));
    }

    @Test
    void importProductividad_ShouldReturnBadRequest_WhenEmptyFileProvided() throws Exception {
        // Given
        MockMultipartFile emptyFile = new MockMultipartFile(
            "file",
            "productividad.csv",
            "text/csv",
            new byte[0]
        );

        // When & Then
        mockMvc.perform(multipart("/productividad/import")
                .file(emptyFile)
                .param("fecha", "2025-12-30"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("El archivo está vacío"));

        verify(productividadService, never()).importFromCSV(any(), any(LocalDate.class));
    }

    @Test
    void importProductividad_ShouldReturnInternalServerError_WhenExceptionThrown() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "productividad.csv",
            "text/csv",
            "Servicio N2,Features,FTEs Directos,FTEs Indirectos\nETPB,11,10.58,4.90".getBytes()
        );
        
        when(productividadService.importFromCSV(any(), any(LocalDate.class)))
            .thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        mockMvc.perform(multipart("/productividad/import")
                .file(file)
                .param("fecha", "2025-12-30"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getAllProductividad_ShouldReturnListOfDTOs_WhenRecordsExist() throws Exception {
        // Given
        List<ProductividadDTO> productividadList = Arrays.asList(testDTO);
        when(productividadService.getAll()).thenReturn(productividadList);

        // When & Then
        mockMvc.perform(get("/productividad"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nucleusId").value(100))
                .andExpect(jsonPath("$[0].features").value(11))
                .andExpect(jsonPath("$[0].ftesDirectos").value(10.58))
                .andExpect(jsonPath("$[0].ftesIndirectos").value(4.90))
                .andExpect(jsonPath("$[0].fecha").value("2025-12-30"));

        verify(productividadService, times(1)).getAll();
    }

    @Test
    void getAllProductividad_ShouldReturnEmptyList_WhenNoRecordsExist() throws Exception {
        // Given
        when(productividadService.getAll()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/productividad"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(productividadService, times(1)).getAll();
    }

    @Test
    void getProductividadByFecha_ShouldReturnListOfDTOs_WhenRecordsExist() throws Exception {
        // Given
        List<ProductividadDTO> productividadList = Arrays.asList(testDTO);
        when(productividadService.getAllByFecha(testFecha)).thenReturn(productividadList);

        // When & Then
        mockMvc.perform(get("/productividad/fecha/{fecha}", "2025-12-30"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fecha").value("2025-12-30"));

        verify(productividadService, times(1)).getAllByFecha(testFecha);
    }

    @Test
    void getProductividadByFecha_ShouldReturnEmptyList_WhenNoRecordsExist() throws Exception {
        // Given
        when(productividadService.getAllByFecha(testFecha)).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/productividad/fecha/{fecha}", "2025-12-30"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(productividadService, times(1)).getAllByFecha(testFecha);
    }

    @Test
    void importProductividad_ShouldParseDateParameter_WhenValidDateProvided() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "productividad.csv",
            "text/csv",
            "Servicio N2,Features,FTEs Directos,FTEs Indirectos\nETPB,11,10.58,4.90".getBytes()
        );
        
        List<String> successMessages = Arrays.asList("Importación exitosa: 1 registros procesados");
        when(productividadService.importFromCSV(any(), any(LocalDate.class))).thenReturn(successMessages);

        // When & Then
        mockMvc.perform(multipart("/productividad/import")
                .file(file)
                .param("fecha", "2025-01-15"))
                .andExpect(status().isOk());

        verify(productividadService, times(1)).importFromCSV(any(), eq(LocalDate.of(2025, 1, 15)));
    }

    @Test
    void getProductividadByFecha_ShouldHandleMultipleRecords_WhenMultipleRecordsExist() throws Exception {
        // Given
        ProductividadDTO dto1 = new ProductividadDTO();
        dto1.setId(1L);
        dto1.setFeatures(11);
        dto1.setFecha(testFecha);

        ProductividadDTO dto2 = new ProductividadDTO();
        dto2.setId(2L);
        dto2.setFeatures(8);
        dto2.setFecha(testFecha);

        List<ProductividadDTO> productividadList = Arrays.asList(dto1, dto2);
        when(productividadService.getAllByFecha(testFecha)).thenReturn(productividadList);

        // When & Then
        mockMvc.perform(get("/productividad/fecha/{fecha}", "2025-12-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }
}
