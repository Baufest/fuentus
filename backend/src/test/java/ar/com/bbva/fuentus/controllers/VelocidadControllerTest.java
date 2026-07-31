package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.VelocidadDTO;
import ar.com.bbva.fuentus.services.VelocidadService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VelocidadController.class)
class VelocidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VelocidadService velocidadService;

    @Autowired
    private ObjectMapper objectMapper;

    private VelocidadDTO testDTO;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2025, 1, 15);
        
        testDTO = new VelocidadDTO();
        testDTO.setId(1L);
        testDTO.setNucleusId(100L);
        testDTO.setLt(203);
        testDTO.setCt(173);
        testDTO.setDate(testDate);
    }

    @Test
    void importVelocidad_ShouldReturnSuccess_WhenValidFileProvided() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "velocidad.csv",
            "text/csv",
            "service_n2,LT,CT,deployed_date\nETPB,203,173,15 ene 2025".getBytes()
        );
        
        List<String> successMessages = Arrays.asList("Importación exitosa: 1 registros procesados");
        when(velocidadService.importFromCSV(any())).thenReturn(successMessages);

        // When & Then
        mockMvc.perform(multipart("/velocidad/import")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.messages").isArray())
                .andExpect(jsonPath("$.messages[0]").value("Importación exitosa: 1 registros procesados"));

        verify(velocidadService, times(1)).importFromCSV(any());
    }

    @Test
    void importVelocidad_ShouldReturnPartialContent_WhenSomeRecordsFail() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "velocidad.csv",
            "text/csv",
            "service_n2,LT,CT,deployed_date\nETPB,203,173,15 ene 2025".getBytes()
        );
        
        List<String> errorMessages = Arrays.asList(
            "Importación parcial: 1 exitosos, 1 errores",
            "Error en fila 2: ETPB"
        );
        when(velocidadService.importFromCSV(any())).thenReturn(errorMessages);

        // When & Then
        mockMvc.perform(multipart("/velocidad/import")
                .file(file))
                .andExpect(status().isPartialContent())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.messages").isArray())
                .andExpect(jsonPath("$.messages[0]").value("Importación parcial: 1 exitosos, 1 errores"));
    }

    @Test
    void importVelocidad_ShouldReturnBadRequest_WhenEmptyFileProvided() throws Exception {
        // Given
        MockMultipartFile emptyFile = new MockMultipartFile(
            "file",
            "velocidad.csv",
            "text/csv",
            new byte[0]
        );

        // When & Then
        mockMvc.perform(multipart("/velocidad/import")
                .file(emptyFile))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("El archivo está vacío"));

        verify(velocidadService, never()).importFromCSV(any());
    }

    @Test
    void importVelocidad_ShouldReturnInternalServerError_WhenExceptionThrown() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "velocidad.csv",
            "text/csv",
            "service_n2,LT,CT,deployed_date\nETPB,203,173,15 ene 2025".getBytes()
        );
        
        when(velocidadService.importFromCSV(any()))
            .thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        mockMvc.perform(multipart("/velocidad/import")
                .file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getAllVelocidad_ShouldReturnListOfDTOs_WhenRecordsExist() throws Exception {
        // Given
        List<VelocidadDTO> velocidadList = Arrays.asList(testDTO);
        when(velocidadService.getAll()).thenReturn(velocidadList);

        // When & Then
        mockMvc.perform(get("/velocidad"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nucleusId").value(100))
                .andExpect(jsonPath("$[0].lt").value(203))
                .andExpect(jsonPath("$[0].ct").value(173))
                .andExpect(jsonPath("$[0].date").value("2025-01-15"));

        verify(velocidadService, times(1)).getAll();
    }

    @Test
    void getAllVelocidad_ShouldReturnEmptyList_WhenNoRecordsExist() throws Exception {
        // Given
        when(velocidadService.getAll()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/velocidad"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(velocidadService, times(1)).getAll();
    }

    @Test
    void getVelocidadByDate_ShouldReturnListOfDTOs_WhenRecordsExist() throws Exception {
        // Given
        List<VelocidadDTO> velocidadList = Arrays.asList(testDTO);
        when(velocidadService.getAllByDate(testDate)).thenReturn(velocidadList);

        // When & Then
        mockMvc.perform(get("/velocidad/date/{date}", "2025-01-15"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].date").value("2025-01-15"));

        verify(velocidadService, times(1)).getAllByDate(testDate);
    }

    @Test
    void getVelocidadByDate_ShouldReturnEmptyList_WhenNoRecordsExist() throws Exception {
        // Given
        when(velocidadService.getAllByDate(testDate)).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/velocidad/date/{date}", "2025-01-15"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(velocidadService, times(1)).getAllByDate(testDate);
    }

    @Test
    void importVelocidad_ShouldNotRequireDateParameter_WhenImporting() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "velocidad.csv",
            "text/csv",
            "service_n2,LT,CT,deployed_date\nETPB,203,173,15 ene 2025".getBytes()
        );
        
        List<String> successMessages = Arrays.asList("Importación exitosa: 1 registros procesados");
        when(velocidadService.importFromCSV(any())).thenReturn(successMessages);

        // When & Then
        mockMvc.perform(multipart("/velocidad/import")
                .file(file))
                .andExpect(status().isOk());

        verify(velocidadService, times(1)).importFromCSV(any());
    }

    @Test
    void getVelocidadByDate_ShouldHandleMultipleRecords_WhenMultipleRecordsExist() throws Exception {
        // Given
        VelocidadDTO dto1 = new VelocidadDTO();
        dto1.setId(1L);
        dto1.setLt(203);
        dto1.setDate(testDate);

        VelocidadDTO dto2 = new VelocidadDTO();
        dto2.setId(2L);
        dto2.setLt(180);
        dto2.setDate(testDate);

        List<VelocidadDTO> velocidadList = Arrays.asList(dto1, dto2);
        when(velocidadService.getAllByDate(testDate)).thenReturn(velocidadList);

        // When & Then
        mockMvc.perform(get("/velocidad/date/{date}", "2025-01-15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void getVelocidadByDate_ShouldParseDateParameter_WhenValidDateProvided() throws Exception {
        // Given
        when(velocidadService.getAllByDate(any(LocalDate.class))).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/velocidad/date/{date}", "2025-03-20"))
                .andExpect(status().isOk());

        verify(velocidadService, times(1)).getAllByDate(LocalDate.of(2025, 3, 20));
    }

    @Test
    void importVelocidad_ShouldAcceptCSVWithMultipleDateColumns_WhenCompleteCSVProvided() throws Exception {
        // Given
        String csvContent = "service_n2,LT,CT,new_date,analyzing_date,deployed_date\n" +
                           "ETPB,203,173,1 ene 2025,5 ene 2025,15 ene 2025";
        
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "velocidad.csv",
            "text/csv",
            csvContent.getBytes()
        );
        
        List<String> successMessages = Arrays.asList("Importación exitosa: 1 registros procesados");
        when(velocidadService.importFromCSV(any())).thenReturn(successMessages);

        // When & Then
        mockMvc.perform(multipart("/velocidad/import")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(velocidadService, times(1)).importFromCSV(any());
    }
}
