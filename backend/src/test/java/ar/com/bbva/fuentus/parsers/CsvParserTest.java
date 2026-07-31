package ar.com.bbva.fuentus.parsers;

import ar.com.bbva.fuentus.dto.NucleusImportDTO;
import ar.com.bbva.fuentus.enums.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvParserTest {

    private CsvParser csvParser;

    @BeforeEach
    void setUp() {
        csvParser = new CsvParser();
    }

    @Test
    void testParseNucleusServices_Success() throws Exception {
        // Given
        String csvContent = "header\n" +
                "Service1,Owner1Id,Owner1,Service2,Service2Desc,Owner2Id,Owner2,Area1,OrgN1,OrgN1Id,OrgN1Owner," +
                "OrgN2,OrgN2Id,OrgN2Owner,CFS,SAAS,Disp,Conf,Int,Auth,Rel,Cat,UUAA1,Active,100,AscGlob,DescReg,AscReg,RelType1";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When
        List<?> result = csvParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof NucleusImportDTO);
        
        NucleusImportDTO dto = (NucleusImportDTO) result.get(0);
        assertEquals("Service1", dto.getServiceN1());
        assertEquals("Owner1Id", dto.getOwnerIdServiceN1());
        assertEquals("Owner1", dto.getOwnerServiceN1());
        assertEquals("Service2", dto.getServiceN2());
        assertEquals("Service2Desc", dto.getServiceN2description());
        assertEquals("Owner2Id", dto.getOwnerIdServiceN2());
        assertEquals("Owner2", dto.getOwnerServiceN2());
        assertEquals("Area1", dto.getArea());
        assertEquals("OrgN1", dto.getOrgN1());
        assertEquals(100, dto.getDescGlobalRels());
    }

    @Test
    void testParseNucleusServices_MultipleRecords() throws Exception {
        // Given
        String csvContent = "header\n" +
                "Service1,Owner1Id,Owner1,Service2,Service2Desc,Owner2Id,Owner2,Area1,OrgN1,OrgN1Id,OrgN1Owner," +
                "OrgN2,OrgN2Id,OrgN2Owner,CFS,SAAS,Disp,Conf,Int,Auth,Rel,Cat,UUAA1,Active,100,AscGlob,DescReg,AscReg,RelType1\n" +
                "Service3,Owner3Id,Owner3,Service4,Service4Desc,Owner4Id,Owner4,Area2,OrgN1B,OrgN1BId,OrgN1BOwner," +
                "OrgN2B,OrgN2BId,OrgN2BOwner,CFS2,SAAS2,Disp2,Conf2,Int2,Auth2,Rel2,Cat2,UUAA2,Inactive,200,AscGlobB,DescRegB,AscRegB,RelTypeB";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When
        List<?> result = csvParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        
        NucleusImportDTO dto1 = (NucleusImportDTO) result.get(0);
        assertEquals("Service1", dto1.getServiceN1());
        assertEquals("UUAA1", dto1.getUuaa());
        
        NucleusImportDTO dto2 = (NucleusImportDTO) result.get(1);
        assertEquals("Service3", dto2.getServiceN1());
        assertEquals("UUAA2", dto2.getUuaa());
    }

    @Test
    void testParseNucleusServices_EmptyFile() throws Exception {
        // Given
        String csvContent = "header";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When
        List<?> result = csvParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseNucleusServices_WithQuotedFields() throws Exception {
        // Given
        String csvContent = "header\n" +
                "\"Service,1\",Owner1Id,Owner1,\"Service,2\",Service2Desc,Owner2Id,Owner2,Area1,OrgN1,OrgN1Id,OrgN1Owner," +
                "OrgN2,OrgN2Id,OrgN2Owner,CFS,SAAS,Disp,Conf,Int,Auth,Rel,Cat,UUAA1,Active,100,AscGlob,DescReg,AscReg,RelType1";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When
        List<?> result = csvParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        NucleusImportDTO dto = (NucleusImportDTO) result.get(0);
        assertEquals("Service,1", dto.getServiceN1());
        assertEquals("Service,2", dto.getServiceN2());
    }

    @Test
    void testParseNucleusServices_WithEmptyFields() throws Exception {
        // Given
        String csvContent = "header\n" +
                "Service1,,Owner1,Service2,,Owner2Id,Owner2,Area1,OrgN1,OrgN1Id,OrgN1Owner," +
                "OrgN2,OrgN2Id,OrgN2Owner,CFS,SAAS,Disp,Conf,Int,Auth,Rel,Cat,UUAA1,Active,,AscGlob,DescReg,AscReg,RelType1";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When
        List<?> result = csvParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        NucleusImportDTO dto = (NucleusImportDTO) result.get(0);
        assertEquals("Service1", dto.getServiceN1());
        assertNull(dto.getOwnerIdServiceN1());
        assertNull(dto.getDescGlobalRels());
    }

    @Test
    void testParseNucleusServices_WithWhitespace() throws Exception {
        // Given
        String csvContent = "header\n" +
                "  Service1  ,  Owner1Id  ,  Owner1  ,  Service2  ,  Service2Desc  ,  Owner2Id  ,  Owner2  ,  Area1  ,  OrgN1  ,  OrgN1Id  ,  OrgN1Owner  ," +
                "  OrgN2  ,  OrgN2Id  ,  OrgN2Owner  ,  CFS  ,  SAAS  ,  Disp  ,  Conf  ,  Int  ,  Auth  ,  Rel  ,  Cat  ,  UUAA1  ,  Active  ,  100  ,  AscGlob  ,  DescReg  ,  AscReg  ,  RelType1  ";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When
        List<?> result = csvParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        NucleusImportDTO dto = (NucleusImportDTO) result.get(0);
        assertEquals("Service1", dto.getServiceN1());
        assertEquals("UUAA1", dto.getUuaa());
    }

    @Test
    void testParseNucleusServices_WithValidNumericField() throws Exception {
        // Given
        String csvContent = "header\n" +
                "Service1,Owner1Id,Owner1,Service2,Service2Desc,Owner2Id,Owner2,Area1,OrgN1,OrgN1Id,OrgN1Owner," +
                "OrgN2,OrgN2Id,OrgN2Owner,CFS,SAAS,Disp,Conf,Int,Auth,Rel,Cat,UUAA1,Active,12345,AscGlob,DescReg,AscReg,RelType1";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When
        List<?> result = csvParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        NucleusImportDTO dto = (NucleusImportDTO) result.get(0);
        assertEquals(12345, dto.getDescGlobalRels());
    }

    @Test
    void testParseNucleusServices_WithInvalidNumericField() throws Exception {
        // Given
        String csvContent = "header\n" +
                "Service1,Owner1Id,Owner1,Service2,Service2Desc,Owner2Id,Owner2,Area1,OrgN1,OrgN1Id,OrgN1Owner," +
                "OrgN2,OrgN2Id,OrgN2Owner,CFS,SAAS,Disp,Conf,Int,Auth,Rel,Cat,UUAA1,Active,notAnumber,AscGlob,DescReg,AscReg,RelType1";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When
        List<?> result = csvParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        NucleusImportDTO dto = (NucleusImportDTO) result.get(0);
        assertNull(dto.getDescGlobalRels());
    }

    @Test
    void testParseNucleusServices_WithQuotedValues() throws Exception {
        // Given
        String csvContent = "header\n" +
                "\"Service1\",\"Owner1Id\",\"Owner1\",\"Service2\",\"Service2Desc\",\"Owner2Id\",\"Owner2\",\"Area1\",\"OrgN1\",\"OrgN1Id\",\"OrgN1Owner\"," +
                "\"OrgN2\",\"OrgN2Id\",\"OrgN2Owner\",\"CFS\",\"SAAS\",\"Disp\",\"Conf\",\"Int\",\"Auth\",\"Rel\",\"Cat\",\"UUAA1\",\"Active\",\"100\",\"AscGlob\",\"DescReg\",\"AscReg\",\"RelType1\"";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When
        List<?> result = csvParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        NucleusImportDTO dto = (NucleusImportDTO) result.get(0);
        assertEquals("Service1", dto.getServiceN1());
        assertEquals(100, dto.getDescGlobalRels());
    }

    @Test
    void testParseChimeraSast_ThrowsUnsupportedOperationException() {
        // Given
        String csvContent = "header\ndata";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "chimera_sast.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When & Then
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> csvParser.parse(file, EntityType.CHIMERA_SAST)
        );
        
        assertEquals("ChimeraSast CSV parsing not implemented yet", exception.getMessage());
    }

    @Test
    void testParseApps_ThrowsUnsupportedOperationException() {
        // Given
        String csvContent = "header\ndata";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "apps.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When & Then
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> csvParser.parse(file, EntityType.APPS)
        );
        
        assertEquals("Apps CSV parsing not implemented yet", exception.getMessage());
    }

    @Test
    void testParseRfo_ThrowsIllegalArgumentException() {
        // Given
        String csvContent = "header\ndata";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "rfo.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> csvParser.parse(file, EntityType.RFO)
        );
        
        assertTrue(exception.getMessage().contains("Unsupported entity type for CSV"));
    }

    @Test
    void testParseNucleusServices_WithMissingFields() throws Exception {
        // Given - CSV with fewer fields than expected
        String csvContent = "header\n" +
                "Service1,Owner1Id,Owner1";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When
        List<?> result = csvParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        NucleusImportDTO dto = (NucleusImportDTO) result.get(0);
        assertEquals("Service1", dto.getServiceN1());
        assertEquals("Owner1Id", dto.getOwnerIdServiceN1());
        assertEquals("Owner1", dto.getOwnerServiceN1());
        // Remaining fields should be null
        assertNull(dto.getServiceN2());
    }

    @Test
    void testParseNucleusServices_AllFieldsNull() throws Exception {
        // Given
        String csvContent = "header\n" +
                ",,,,,,,,,,,,,,,,,,,,,,,,,,,,";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When
        List<?> result = csvParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        NucleusImportDTO dto = (NucleusImportDTO) result.get(0);
        assertNull(dto.getServiceN1());
        assertNull(dto.getServiceN2());
        assertNull(dto.getDescGlobalRels());
    }

    @Test
    void testParseNucleusServices_SpecialCharacters() throws Exception {
        // Given
        String csvContent = "header\n" +
                "Service@#$%,Owner&*()Id,Owner1,Service-2,Service_2Desc,Owner2Id,Owner2,Area/1,OrgN1,OrgN1Id,OrgN1Owner," +
                "OrgN2,OrgN2Id,OrgN2Owner,CFS,SAAS,Disp,Conf,Int,Auth,Rel,Cat,UUAA1,Active,100,AscGlob,DescReg,AscReg,RelType1";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When
        List<?> result = csvParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        NucleusImportDTO dto = (NucleusImportDTO) result.get(0);
        assertEquals("Service@#$%", dto.getServiceN1());
        assertEquals("Owner&*()Id", dto.getOwnerIdServiceN1());
        assertEquals("Area/1", dto.getArea());
    }

    @Test
    void testParseNucleusServices_ComplexQuotedFields() throws Exception {
        // Given - Test with quoted fields containing commas
        String csvContent = "header\n" +
                "\"Service, with comma\",\"Owner Name\",Owner1,Service2,Service2Desc,Owner2Id,Owner2,Area1,OrgN1,OrgN1Id,OrgN1Owner," +
                "OrgN2,OrgN2Id,OrgN2Owner,CFS,SAAS,Disp,Conf,Int,Auth,Rel,Cat,UUAA1,Active,100,AscGlob,DescReg,AscReg,RelType1";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When
        List<?> result = csvParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        NucleusImportDTO dto = (NucleusImportDTO) result.get(0);
        assertEquals("Service, with comma", dto.getServiceN1());
        assertEquals("Owner Name", dto.getOwnerIdServiceN1());
    }

    @Test
    void testParseNucleusServices_LargeFile() throws Exception {
        // Given - CSV with many records
        StringBuilder csvContent = new StringBuilder("header\n");
        for (int i = 0; i < 100; i++) {
            csvContent.append(String.format(
                    "Service%d,Owner%dId,Owner%d,Service%dB,Service%dBDesc,Owner%dBId,Owner%dB,Area%d,OrgN1,%d,OrgN1Owner," +
                    "OrgN2,%dId,OrgN2Owner,CFS,SAAS,Disp,Conf,Int,Auth,Rel,Cat,UUAA%d,Active,%d,AscGlob,DescReg,AscReg,RelType%d\n",
                    i, i, i, i, i, i, i, i, i, i, i, i, i, i
            ));
        }
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.csv",
                "text/csv",
                csvContent.toString().getBytes()
        );

        // When
        List<?> result = csvParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(100, result.size());
        
        NucleusImportDTO firstDto = (NucleusImportDTO) result.get(0);
        assertEquals("Service0", firstDto.getServiceN1());
        
        NucleusImportDTO lastDto = (NucleusImportDTO) result.get(99);
        assertEquals("Service99", lastDto.getServiceN1());
    }

    @Test
    void testParseNucleusServices_ZeroDescGlobalRels() throws Exception {
        // Given
        String csvContent = "header\n" +
                "Service1,Owner1Id,Owner1,Service2,Service2Desc,Owner2Id,Owner2,Area1,OrgN1,OrgN1Id,OrgN1Owner," +
                "OrgN2,OrgN2Id,OrgN2Owner,CFS,SAAS,Disp,Conf,Int,Auth,Rel,Cat,UUAA1,Active,0,AscGlob,DescReg,AscReg,RelType1";
        
        MultipartFile file = new MockMultipartFile(
                "file",
                "nucleus.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When
        List<?> result = csvParser.parse(file, EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        NucleusImportDTO dto = (NucleusImportDTO) result.get(0);
        assertEquals(0, dto.getDescGlobalRels());
    }
}
