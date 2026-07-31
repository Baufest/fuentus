package ar.com.bbva.fuentus.dto;

import ar.com.bbva.fuentus.enums.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DTOParserServiceTest {

    private DTOParserService dtoParserService;

    @BeforeEach
    void setUp() {
        dtoParserService = new DTOParserService();
    }

    @Test
    void parseCsvFile_ShouldParseNucleusCSV_WhenValidFileProvided() throws Exception {
        // Given
        String csvContent = "Id Fullservice;Service N1;Service N2\n" +
                           "100;SERVICE_N1;SN2_TEST\n" +
                           "200;SERVICE_N1_2;SN2_TEST_2";
        
        MultipartFile file = new MockMultipartFile(
            "file",
            "nucleus.csv",
            "text/csv",
            csvContent.getBytes(StandardCharsets.UTF_8)
        );

        // When
        List<?> result = dtoParserService.parseCsvFile(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0) instanceof NucleusImportDTO);
        
        NucleusImportDTO dto1 = (NucleusImportDTO) result.get(0);
        assertEquals(100L, dto1.getIdFullservice());
        assertEquals("SERVICE_N1", dto1.getServiceN1());
        assertEquals("SN2_TEST", dto1.getServiceN2());
    }

    @Test
    void parseCsvFile_ShouldParseRfoCSV_WhenValidFileProvided() throws Exception {
        // Given
        String csvContent = "RFO ID,SERVICIO N1,SERVICE OWNER,SEVICIO N2,ROL,NOMBRE,EMAIL,ESTADO RFO,FECHA puesta en producción\n" +
                           "1,SERVICIO N1 TEST,Owner Test,SN2_TEST,Developer,Juan Perez,juan@test.com,Activo,2025-01-15";
        
        MultipartFile file = new MockMultipartFile(
            "file",
            "rfo.csv",
            "text/csv",
            csvContent.getBytes(StandardCharsets.UTF_8)
        );

        // When
        List<?> result = dtoParserService.parseCsvFile(file, EntityType.RFO);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof RfoImportDTO);
        
        RfoImportDTO dto = (RfoImportDTO) result.get(0);
        assertEquals(1L, dto.getRfoId());
        assertEquals("SN2_TEST", dto.getSevicioN2());
        assertEquals("juan@test.com", dto.getEmail());
    }

    @Test
    void parseCsvFile_ShouldHandleInvalidCSVFormat_WithoutMatching() throws Exception {
        // Given - CSV with columns that don't match any DTO fields
        String invalidCsvContent = "Invalid,CSV,Content\nno,matching,columns";
        
        MultipartFile file = new MockMultipartFile(
            "file",
            "invalid.csv",
            "text/csv",
            invalidCsvContent.getBytes(StandardCharsets.UTF_8)
        );

        // When
        List<?> result = dtoParserService.parseCsvFile(file, EntityType.NUCLEUS_SERVICES);
        
        // Then - OpenCSV parses but fields will be null/default values
        assertNotNull(result);
        assertEquals(1, result.size());
        NucleusImportDTO dto = (NucleusImportDTO) result.get(0);
        assertNull(dto.getIdFullservice());
    }

    @Test
    void parseCsvFile_ShouldHandleEmptyFile() throws Exception {
        // Given
        String emptyContent = "Id Fullservice;Service N1;Service N2\n";
        
        MultipartFile file = new MockMultipartFile(
            "file",
            "empty.csv",
            "text/csv",
            emptyContent.getBytes(StandardCharsets.UTF_8)
        );

        // When
        List<?> result = dtoParserService.parseCsvFile(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void parseCsvFile_ShouldIgnoreLeadingWhitespace() throws Exception {
        // Given
        String csvContent = "Id Fullservice;Service N1;Service N2\n" +
                           "  100  ;  SERVICE_N1  ;  SN2_TEST  ";
        
        MultipartFile file = new MockMultipartFile(
            "file",
            "whitespace.csv",
            "text/csv",
            csvContent.getBytes(StandardCharsets.UTF_8)
        );

        // When
        List<?> result = dtoParserService.parseCsvFile(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        NucleusImportDTO dto = (NucleusImportDTO) result.get(0);
        assertEquals(100L, dto.getIdFullservice());
    }

    @Test
    void parseJsonFile_ShouldParseNucleusJSON_WhenValidFileProvided() throws Exception {
        // Given
        String jsonContent = "[" +
            "{\"Id Fullservice\":100,\"serviceN1\":\"SERVICE_N1\",\"serviceN2\":\"SN2_TEST\"}," +
            "{\"Id Fullservice\":200,\"serviceN1\":\"SERVICE_N1_2\",\"serviceN2\":\"SN2_TEST_2\"}" +
            "]";
        
        MultipartFile file = new MockMultipartFile(
            "file",
            "nucleus.json",
            "application/json",
            jsonContent.getBytes(StandardCharsets.UTF_8)
        );

        // When
        List<?> result = dtoParserService.parseJsonFile(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0) instanceof NucleusImportDTO);
        
        NucleusImportDTO dto1 = (NucleusImportDTO) result.get(0);
        assertEquals(100L, dto1.getIdFullservice());
        assertEquals("SERVICE_N1", dto1.getServiceN1());
    }

    @Test
    void parseJsonFile_ShouldParseRfoJSON_WhenValidFileProvided() throws Exception {
        // Given
        String jsonContent = "[{" +
            "\"rfoId\":1," +
            "\"sevicioN2\":\"SN2_TEST\"," +
            "\"email\":\"juan@test.com\",\"estadoRfo\":\"Activo\",\"fechaPuestaProduccion\":\"2025-01-15\"}" +
            "]";
        
        MultipartFile file = new MockMultipartFile(
            "file",
            "rfo.json",
            "application/json",
            jsonContent.getBytes(StandardCharsets.UTF_8)
        );

        // When
        List<?> result = dtoParserService.parseJsonFile(file, EntityType.RFO);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof RfoImportDTO);
        
        RfoImportDTO dto = (RfoImportDTO) result.get(0);
        assertEquals(1L, dto.getRfoId());
        assertEquals("SN2_TEST", dto.getSevicioN2());
    }

    @Test
    void parseJsonFile_ShouldThrowException_WhenInvalidJSONFormat() {
        // Given
        String invalidJsonContent = "{invalid json";
        
        MultipartFile file = new MockMultipartFile(
            "file",
            "invalid.json",
            "application/json",
            invalidJsonContent.getBytes(StandardCharsets.UTF_8)
        );

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            dtoParserService.parseJsonFile(file, EntityType.NUCLEUS_SERVICES));
    }

    @Test
    void parseJsonFile_ShouldHandleEmptyArray() throws Exception {
        // Given
        String emptyJsonContent = "[]";
        
        MultipartFile file = new MockMultipartFile(
            "file",
            "empty.json",
            "application/json",
            emptyJsonContent.getBytes(StandardCharsets.UTF_8)
        );

        // When
        List<?> result = dtoParserService.parseJsonFile(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void parseCsvFile_ShouldReturnCorrectDTOClass_ForAllEntityTypes() throws Exception {
        // Test for each entity type
        testEntityTypeCsvMapping(EntityType.NUCLEUS_SERVICES, NucleusImportDTO.class, 
            "Id Fullservice;Service N1;Service N2\n100;SN1;SN2");
        testEntityTypeCsvMapping(EntityType.RFO, RfoImportDTO.class,
            "RFO ID,SERVICIO N1,SERVICE OWNER,SEVICIO N2,ROL,NOMBRE,EMAIL,ESTADO RFO,FECHA puesta en producción\n1,SN1,Owner,SN2,Rol,Nombre,email@test.com,Estado,2025-01-01");
    }

    @Test
    void parseJsonFile_ShouldReturnCorrectDTOClass_ForAllEntityTypes() throws Exception {
        // Test for NUCLEUS_SERVICES
        testEntityTypeJsonMapping(EntityType.NUCLEUS_SERVICES, NucleusImportDTO.class,
            "[{\"Id Fullservice\":100,\"serviceN1\":\"SN1\",\"serviceN2\":\"SN2\"}]");
        
        // Test for RFO
        testEntityTypeJsonMapping(EntityType.RFO, RfoImportDTO.class,
            "[{\"rfoId\":1,\"sevicioN2\":\"SN2\",\"email\":\"test@test.com\"}]");
    }

    @Test
    void parseCsvFile_ShouldHandleMultipleRecordsWithDifferentValues() throws Exception {
        // Given
        String csvContent = "Id Fullservice;Service N1;Service N2;Owner Service N1\n" +
                           "100;SERVICE_A;SN2_A;Owner A\n" +
                           "200;SERVICE_B;SN2_B;Owner B\n" +
                           "300;SERVICE_C;SN2_C;Owner C";
        
        MultipartFile file = new MockMultipartFile(
            "file",
            "multiple.csv",
            "text/csv",
            csvContent.getBytes(StandardCharsets.UTF_8)
        );

        // When
        List<?> result = dtoParserService.parseCsvFile(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertEquals(3, result.size());
        
        NucleusImportDTO dto1 = (NucleusImportDTO) result.get(0);
        NucleusImportDTO dto2 = (NucleusImportDTO) result.get(1);
        NucleusImportDTO dto3 = (NucleusImportDTO) result.get(2);
        
        assertEquals("SERVICE_A", dto1.getServiceN1());
        assertEquals("SERVICE_B", dto2.getServiceN1());
        assertEquals("SERVICE_C", dto3.getServiceN1());
    }

    @Test
    void parseJsonFile_ShouldHandleMultipleRecordsWithDifferentValues() throws Exception {
        // Given
        String jsonContent = "[" +
            "{\"Id Fullservice\":100,\"serviceN1\":\"SERVICE_A\",\"serviceN2\":\"SN2_A\"}," +
            "{\"Id Fullservice\":200,\"serviceN1\":\"SERVICE_B\",\"serviceN2\":\"SN2_B\"}," +
            "{\"Id Fullservice\":300,\"serviceN1\":\"SERVICE_C\",\"serviceN2\":\"SN2_C\"}" +
            "]";
        
        MultipartFile file = new MockMultipartFile(
            "file",
            "multiple.json",
            "application/json",
            jsonContent.getBytes(StandardCharsets.UTF_8)
        );

        // When
        List<?> result = dtoParserService.parseJsonFile(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertEquals(3, result.size());
        
        NucleusImportDTO dto1 = (NucleusImportDTO) result.get(0);
        NucleusImportDTO dto2 = (NucleusImportDTO) result.get(1);
        NucleusImportDTO dto3 = (NucleusImportDTO) result.get(2);
        
        assertEquals("SERVICE_A", dto1.getServiceN1());
        assertEquals("SERVICE_B", dto2.getServiceN1());
        assertEquals("SERVICE_C", dto3.getServiceN1());
    }

    // Helper methods
    private void testEntityTypeCsvMapping(EntityType entityType, Class<?> expectedClass, String csvContent) throws Exception {
        MultipartFile file = new MockMultipartFile(
            "file",
            "test.csv",
            "text/csv",
            csvContent.getBytes(StandardCharsets.UTF_8)
        );

        List<?> result = dtoParserService.parseCsvFile(file, entityType);
        
        if (!result.isEmpty()) {
            assertTrue(expectedClass.isInstance(result.get(0)));
        }
    }

    private void testEntityTypeJsonMapping(EntityType entityType, Class<?> expectedClass, String jsonContent) throws Exception {
        MultipartFile file = new MockMultipartFile(
            "file",
            "test.json",
            "application/json",
            jsonContent.getBytes(StandardCharsets.UTF_8)
        );

        List<?> result = dtoParserService.parseJsonFile(file, entityType);
        
        if (!result.isEmpty()) {
            assertTrue(expectedClass.isInstance(result.get(0)));
        }
    }
}
