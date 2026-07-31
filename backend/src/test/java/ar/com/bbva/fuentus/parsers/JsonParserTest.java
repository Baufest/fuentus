package ar.com.bbva.fuentus.parsers;

import ar.com.bbva.fuentus.dto.ChimeraScaImportDTO;
import ar.com.bbva.fuentus.dto.NucleusImportDTO;
import ar.com.bbva.fuentus.enums.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonParserTest {

    private JsonParser jsonParser;

    @BeforeEach
    void setUp() {
        jsonParser = new JsonParser();
    }

    @Test
    void testParseNucleusServices_Success() throws Exception {
        // Given
        String jsonContent = "[{\"Id Fullservice\":12345,\"serviceN1\":\"Service1\",\"serviceN2\":\"Service2\"," +
                "\"ownerServiceN1\":\"Owner1\",\"ownerServiceN2\":\"Owner2\",\"uuaa\":\"1001\"," +
                "\"estado\":\"A\",\"area\":\"TI\"}]";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.json",
                "application/json",
                jsonContent.getBytes()
        );

        // When
        List<?> result = jsonParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof NucleusImportDTO);
        
        NucleusImportDTO dto = (NucleusImportDTO) result.get(0);
        assertEquals(12345L, dto.getIdFullservice());
        assertEquals("Service1", dto.getServiceN1());
        assertEquals("Service2", dto.getServiceN2());
        assertEquals("Owner1", dto.getOwnerServiceN1());
        assertEquals("Owner2", dto.getOwnerServiceN2());
        assertEquals("1001", dto.getUuaa());
        assertEquals("A", dto.getEstado());
        assertEquals("TI", dto.getArea());
    }

    @Test
    void testParseNucleusServices_MultipleRecords() throws Exception {
        // Given
        String jsonContent = "[" +
                "{\"Id Fullservice\":1,\"serviceN1\":\"S1\",\"serviceN2\":\"S2\"," +
                "\"ownerServiceN1\":\"O1\",\"ownerServiceN2\":\"O2\",\"uuaa\":\"1001\"," +
                "\"estado\":\"A\",\"area\":\"TI\"}," +
                "{\"Id Fullservice\":2,\"serviceN1\":\"S3\",\"serviceN2\":\"S4\"," +
                "\"ownerServiceN1\":\"O3\",\"ownerServiceN2\":\"O4\",\"uuaa\":\"1002\"," +
                "\"estado\":\"I\",\"area\":\"OPS\"}" +
                "]";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.json",
                "application/json",
                jsonContent.getBytes()
        );

        // When
        List<?> result = jsonParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0) instanceof NucleusImportDTO);
        assertTrue(result.get(1) instanceof NucleusImportDTO);
        
        NucleusImportDTO dto1 = (NucleusImportDTO) result.get(0);
        assertEquals(1L, dto1.getIdFullservice());
        assertEquals("S1", dto1.getServiceN1());
        
        NucleusImportDTO dto2 = (NucleusImportDTO) result.get(1);
        assertEquals(2L, dto2.getIdFullservice());
        assertEquals("S3", dto2.getServiceN1());
    }

    @Test
    void testParseNucleusServices_EmptyArray() throws Exception {
        // Given
        String jsonContent = "[]";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.json",
                "application/json",
                jsonContent.getBytes()
        );

        // When
        List<?> result = jsonParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseChimeraSca_Success() throws Exception {
        // Given
        String jsonContent = "[{\"projectId\":\"MyProject\",\"appId\":123,\"name\":\"MyApp\"," +
                "\"uuaa\":\"1001\",\"low\":5,\"medium\":3,\"high\":2,\"critical\":1}]";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "chimera_sca.json",
                "application/json",
                jsonContent.getBytes()
        );

        // When
        List<?> result = jsonParser.parse(file, EntityType.CHIMERA_SCA);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof ChimeraScaImportDTO);
        
        ChimeraScaImportDTO dto = (ChimeraScaImportDTO) result.get(0);
        assertEquals("MyProject", dto.getProjectId());
        assertEquals(123L, dto.getAppId());
        assertEquals("MyApp", dto.getName());
        assertEquals("1001", dto.getUuaa());
        assertEquals(5L, dto.getLow());
        assertEquals(3L, dto.getMedium());
        assertEquals(2L, dto.getHigh());
        assertEquals(1L, dto.getCritical());
    }

    @Test
    void testParseChimeraSca_MultipleRecords() throws Exception {
        // Given
        String jsonContent = "[" +
                "{\"projectId\":\"Proj1\",\"appId\":1,\"name\":\"App1\"," +
                "\"uuaa\":\"1001\",\"low\":1,\"medium\":2,\"high\":3,\"critical\":4}," +
                "{\"projectId\":\"Proj2\",\"appId\":2,\"name\":\"App2\"," +
                "\"uuaa\":\"1002\",\"low\":5,\"medium\":6,\"high\":7,\"critical\":8}" +
                "]";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "chimera_sca.json",
                "application/json",
                jsonContent.getBytes()
        );

        // When
        List<?> result = jsonParser.parse(file, EntityType.CHIMERA_SCA);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        
        ChimeraScaImportDTO dto1 = (ChimeraScaImportDTO) result.get(0);
        assertEquals("App1", dto1.getName());
        assertEquals(4L, dto1.getCritical());
        
        ChimeraScaImportDTO dto2 = (ChimeraScaImportDTO) result.get(1);
        assertEquals("App2", dto2.getName());
        assertEquals(8L, dto2.getCritical());
    }

    @Test
    void testParseChimeraSast_ThrowsUnsupportedOperationException() {
        // Given
        String jsonContent = "[]";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "chimera_sast.json",
                "application/json",
                jsonContent.getBytes()
        );

        // When & Then
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> jsonParser.parse(file, EntityType.CHIMERA_SAST)
        );
        
        assertEquals("ChimeraSast JSON parsing not implemented yet", exception.getMessage());
    }

    @Test
    void testParseApps_ThrowsUnsupportedOperationException() {
        // Given
        String jsonContent = "[]";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "apps.json",
                "application/json",
                jsonContent.getBytes()
        );

        // When & Then
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> jsonParser.parse(file, EntityType.APPS)
        );
        
        assertEquals("Apps JSON parsing not implemented yet", exception.getMessage());
    }

    @Test
    void testParseRfo_ThrowsIllegalArgumentException() {
        // Given
        String jsonContent = "[]";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "rfo.json",
                "application/json",
                jsonContent.getBytes()
        );

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> jsonParser.parse(file, EntityType.RFO)
        );
        
        assertTrue(exception.getMessage().contains("Unsupported entity type for JSON"));
        assertTrue(exception.getMessage().contains("RFO"));
    }

    @Test
    void testParseNucleusServices_InvalidJson() {
        // Given
        String invalidJson = "{invalid json content}";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.json",
                "application/json",
                invalidJson.getBytes()
        );

        // When & Then
        assertThrows(Exception.class, () -> jsonParser.parse(file, EntityType.NUCLEUS_SERVICES));
    }

    @Test
    void testParseNucleusServices_MalformedJson() {
        // Given
        String malformedJson = "[{\"Id Fullservice\":\"not a number\"}]";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.json",
                "application/json",
                malformedJson.getBytes()
        );

        // When & Then
        assertThrows(Exception.class, () -> jsonParser.parse(file, EntityType.NUCLEUS_SERVICES));
    }

    @Test
    void testParseChimeraSca_InvalidJson() {
        // Given
        String invalidJson = "not a json array";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "chimera_sca.json",
                "application/json",
                invalidJson.getBytes()
        );

        // When & Then
        assertThrows(Exception.class, () -> jsonParser.parse(file, EntityType.CHIMERA_SCA));
    }

    @Test
    void testParseNucleusServices_WithNullFields() throws Exception {
        // Given
        String jsonContent = "[{\"Id Fullservice\":123,\"serviceN1\":null,\"serviceN2\":\"Service2\"," +
                "\"ownerServiceN1\":null,\"ownerServiceN2\":null,\"uuaa\":null," +
                "\"estado\":null,\"area\":null}]";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.json",
                "application/json",
                jsonContent.getBytes()
        );

        // When
        List<?> result = jsonParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof NucleusImportDTO);
        
        NucleusImportDTO dto = (NucleusImportDTO) result.get(0);
        assertEquals(123L, dto.getIdFullservice());
        assertNull(dto.getServiceN1());
        assertEquals("Service2", dto.getServiceN2());
        assertNull(dto.getOwnerServiceN1());
    }

    @Test
    void testParseChimeraSca_WithNullFields() throws Exception {
        // Given
        String jsonContent = "[{\"projectId\":null,\"appId\":null,\"name\":\"MyApp\"," +
                "\"uuaa\":null,\"low\":0,\"medium\":0,\"high\":0,\"critical\":0}]";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "chimera_sca.json",
                "application/json",
                jsonContent.getBytes()
        );

        // When
        List<?> result = jsonParser.parse(file, EntityType.CHIMERA_SCA);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof ChimeraScaImportDTO);
        
        ChimeraScaImportDTO dto = (ChimeraScaImportDTO) result.get(0);
        assertNull(dto.getProjectId());
        assertNull(dto.getAppId());
        assertEquals("MyApp", dto.getName());
        assertNull(dto.getUuaa());
    }

    @Test
    void testParseNucleusServices_EmptyFile() {
        // Given
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.json",
                "application/json",
                new byte[0]
        );

        // When & Then
        assertThrows(Exception.class, () -> jsonParser.parse(file, EntityType.NUCLEUS_SERVICES));
    }

    @Test
    void testParseChimeraSca_LargeNumbers() throws Exception {
        // Given
        String jsonContent = "[{\"projectId\":\"Proj\",\"appId\":12345,\"name\":\"App\"," +
                "\"uuaa\":\"1001\",\"low\":99999,\"medium\":88888,\"high\":77777,\"critical\":66666}]";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "chimera_sca.json",
                "application/json",
                jsonContent.getBytes()
        );

        // When
        List<?> result = jsonParser.parse(file, EntityType.CHIMERA_SCA);

        // Then
        assertNotNull(result);
        ChimeraScaImportDTO dto = (ChimeraScaImportDTO) result.get(0);
        assertEquals(99999L, dto.getLow());
        assertEquals(88888L, dto.getMedium());
        assertEquals(77777L, dto.getHigh());
        assertEquals(66666L, dto.getCritical());
    }

    @Test
    void testParseNucleusServices_SpecialCharacters() throws Exception {
        // Given
        String jsonContent = "[{\"Id Fullservice\":1,\"serviceN1\":\"Service\",\"serviceN2\":\"Service_@#$%\"," +
                "\"ownerServiceN1\":\"Owner's Name\",\"ownerServiceN2\":\"Owner\\\"Quote\\\"\",\"uuaa\":\"1001\"," +
                "\"estado\":\"A\",\"area\":\"TI & OPS\"}]";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.json",
                "application/json",
                jsonContent.getBytes()
        );

        // When
        List<?> result = jsonParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        NucleusImportDTO dto = (NucleusImportDTO) result.get(0);
        assertEquals("Service_@#$%", dto.getServiceN2());
        assertEquals("Owner's Name", dto.getOwnerServiceN1());
        assertEquals("TI & OPS", dto.getArea());
    }

    @Test
    void testParseChimeraSca_ZeroValues() throws Exception {
        // Given
        String jsonContent = "[{\"projectId\":\"Proj\",\"appId\":1,\"name\":\"App\"," +
                "\"uuaa\":\"1001\",\"low\":0,\"medium\":0,\"high\":0,\"critical\":0}]";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "chimera_sca.json",
                "application/json",
                jsonContent.getBytes()
        );

        // When
        List<?> result = jsonParser.parse(file, EntityType.CHIMERA_SCA);

        // Then
        assertNotNull(result);
        ChimeraScaImportDTO dto = (ChimeraScaImportDTO) result.get(0);
        assertEquals(0L, dto.getLow());
        assertEquals(0L, dto.getMedium());
        assertEquals(0L, dto.getHigh());
        assertEquals(0L, dto.getCritical());
    }
}
