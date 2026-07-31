package ar.com.bbva.fuentus.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChimeraScaImportDTOTest {

    @Test
    void constructor_ShouldCreateEmptyDTO_WhenNoArgumentsProvided() {
        // When
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO();

        // Then
        assertNotNull(dto);
        assertNull(dto.getProjectId());
        assertNull(dto.getAppId());
        assertNull(dto.getName());
        assertNull(dto.getUuaa());
        assertNull(dto.getLow());
        assertNull(dto.getMedium());
        assertNull(dto.getHigh());
        assertNull(dto.getCritical());
    }

    @Test
    void constructor_ShouldCreateDTO_WhenAllArgumentsProvided() {
        // Given
        String projectId = "PROJ-123";
        Long appId = 100L;
        String name = "Test Application";
        String uuaa = "1234";
        Long low = 10L;
        Long medium = 5L;
        Long high = 3L;
        Long critical = 1L;

        // When
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO(
            projectId, appId, name, uuaa, low, medium, high, critical
        );

        // Then
        assertNotNull(dto);
        assertEquals(projectId, dto.getProjectId());
        assertEquals(appId, dto.getAppId());
        assertEquals(name, dto.getName());
        assertEquals(uuaa, dto.getUuaa());
        assertEquals(low, dto.getLow());
        assertEquals(medium, dto.getMedium());
        assertEquals(high, dto.getHigh());
        assertEquals(critical, dto.getCritical());
    }

    @Test
    void setProjectId_ShouldSetValue() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO();
        String projectId = "PROJ-456";

        // When
        dto.setProjectId(projectId);

        // Then
        assertEquals(projectId, dto.getProjectId());
    }

    @Test
    void setAppId_ShouldSetValue() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO();
        Long appId = 200L;

        // When
        dto.setAppId(appId);

        // Then
        assertEquals(appId, dto.getAppId());
    }

    @Test
    void setName_ShouldSetValue() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO();
        String name = "New Application Name";

        // When
        dto.setName(name);

        // Then
        assertEquals(name, dto.getName());
    }

    @Test
    void setUuaa_ShouldSetValue() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO();
        String uuaa = "5678";

        // When
        dto.setUuaa(uuaa);

        // Then
        assertEquals(uuaa, dto.getUuaa());
    }

    @Test
    void setLow_ShouldSetValue() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO();
        Long low = 15L;

        // When
        dto.setLow(low);

        // Then
        assertEquals(low, dto.getLow());
    }

    @Test
    void setMedium_ShouldSetValue() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO();
        Long medium = 8L;

        // When
        dto.setMedium(medium);

        // Then
        assertEquals(medium, dto.getMedium());
    }

    @Test
    void setHigh_ShouldSetValue() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO();
        Long high = 4L;

        // When
        dto.setHigh(high);

        // Then
        assertEquals(high, dto.getHigh());
    }

    @Test
    void setCritical_ShouldSetValue() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO();
        Long critical = 2L;

        // When
        dto.setCritical(critical);

        // Then
        assertEquals(critical, dto.getCritical());
    }

    @Test
    void toString_ShouldReturnFormattedString_WhenAllFieldsSet() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO(
            "PROJ-789", 300L, "App Name", "9999", 20L, 10L, 5L, 2L
        );

        // When
        String result = dto.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("ChimeraScaImportDTO{"));
        assertTrue(result.contains("projectId='PROJ-789'"));
        assertTrue(result.contains("appId=300"));
        assertTrue(result.contains("name='App Name'"));
        assertTrue(result.contains("uuaa='9999'"));
        assertTrue(result.contains("low=20"));
        assertTrue(result.contains("medium=10"));
        assertTrue(result.contains("high=5"));
        assertTrue(result.contains("critical=2"));
    }

    @Test
    void toString_ShouldReturnFormattedString_WhenFieldsAreNull() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO();

        // When
        String result = dto.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("ChimeraScaImportDTO{"));
        assertTrue(result.contains("projectId='null'"));
        assertTrue(result.contains("appId=null"));
        assertTrue(result.contains("name='null'"));
        assertTrue(result.contains("uuaa='null'"));
        assertTrue(result.contains("low=null"));
        assertTrue(result.contains("medium=null"));
        assertTrue(result.contains("high=null"));
        assertTrue(result.contains("critical=null"));
    }

    @Test
    void setters_ShouldAllowNullValues() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO(
            "PROJ-123", 100L, "Test", "1234", 10L, 5L, 3L, 1L
        );

        // When
        dto.setProjectId(null);
        dto.setAppId(null);
        dto.setName(null);
        dto.setUuaa(null);
        dto.setLow(null);
        dto.setMedium(null);
        dto.setHigh(null);
        dto.setCritical(null);

        // Then
        assertNull(dto.getProjectId());
        assertNull(dto.getAppId());
        assertNull(dto.getName());
        assertNull(dto.getUuaa());
        assertNull(dto.getLow());
        assertNull(dto.getMedium());
        assertNull(dto.getHigh());
        assertNull(dto.getCritical());
    }

    @Test
    void setters_ShouldAllowZeroValues() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO();

        // When
        dto.setAppId(0L);
        dto.setLow(0L);
        dto.setMedium(0L);
        dto.setHigh(0L);
        dto.setCritical(0L);

        // Then
        assertEquals(0L, dto.getAppId());
        assertEquals(0L, dto.getLow());
        assertEquals(0L, dto.getMedium());
        assertEquals(0L, dto.getHigh());
        assertEquals(0L, dto.getCritical());
    }

    @Test
    void setters_ShouldAllowEmptyStrings() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO();

        // When
        dto.setProjectId("");
        dto.setName("");
        dto.setUuaa("");

        // Then
        assertEquals("", dto.getProjectId());
        assertEquals("", dto.getName());
        assertEquals("", dto.getUuaa());
    }

    @Test
    void setters_ShouldAllowWhitespaceStrings() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO();

        // When
        dto.setProjectId("   ");
        dto.setName("   ");
        dto.setUuaa("   ");

        // Then
        assertEquals("   ", dto.getProjectId());
        assertEquals("   ", dto.getName());
        assertEquals("   ", dto.getUuaa());
    }

    @Test
    void setters_ShouldAllowNegativeValues() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO();

        // When
        dto.setAppId(-1L);
        dto.setLow(-10L);
        dto.setMedium(-5L);
        dto.setHigh(-3L);
        dto.setCritical(-1L);

        // Then
        assertEquals(-1L, dto.getAppId());
        assertEquals(-10L, dto.getLow());
        assertEquals(-5L, dto.getMedium());
        assertEquals(-3L, dto.getHigh());
        assertEquals(-1L, dto.getCritical());
    }

    @Test
    void constructor_ShouldAllowNullValues() {
        // When
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO(
            null, null, null, null, null, null, null, null
        );

        // Then
        assertNotNull(dto);
        assertNull(dto.getProjectId());
        assertNull(dto.getAppId());
        assertNull(dto.getName());
        assertNull(dto.getUuaa());
        assertNull(dto.getLow());
        assertNull(dto.getMedium());
        assertNull(dto.getHigh());
        assertNull(dto.getCritical());
    }

    @Test
    void getters_ShouldReturnSetValues() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO();
        
        // When
        dto.setProjectId("TEST-001");
        dto.setAppId(500L);
        dto.setName("Test App");
        dto.setUuaa("0000");
        dto.setLow(100L);
        dto.setMedium(50L);
        dto.setHigh(25L);
        dto.setCritical(10L);

        // Then
        assertEquals("TEST-001", dto.getProjectId());
        assertEquals(500L, dto.getAppId());
        assertEquals("Test App", dto.getName());
        assertEquals("0000", dto.getUuaa());
        assertEquals(100L, dto.getLow());
        assertEquals(50L, dto.getMedium());
        assertEquals(25L, dto.getHigh());
        assertEquals(10L, dto.getCritical());
    }

    @Test
    void toString_ShouldHandleLargeNumbers() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO(
            "PROJ-999", Long.MAX_VALUE, "Large Numbers", "9999",
            999999L, 888888L, 777777L, 666666L
        );

        // When
        String result = dto.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("appId=" + Long.MAX_VALUE));
        assertTrue(result.contains("low=999999"));
        assertTrue(result.contains("medium=888888"));
        assertTrue(result.contains("high=777777"));
        assertTrue(result.contains("critical=666666"));
    }

    @Test
    void toString_ShouldHandleSpecialCharactersInStrings() {
        // Given
        ChimeraScaImportDTO dto = new ChimeraScaImportDTO(
            "PROJ-123!@#", 100L, "Name with 'quotes' and \"double quotes\"",
            "12@#$", 10L, 5L, 3L, 1L
        );

        // When
        String result = dto.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("PROJ-123!@#"));
        assertTrue(result.contains("Name with 'quotes' and \"double quotes\""));
        assertTrue(result.contains("12@#$"));
    }
}
