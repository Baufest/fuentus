package ar.com.bbva.fuentus.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ChimeraSastImportDTOTest {

    @Test
    void constructor_ShouldCreateEmptyDTO_WhenNoArgumentsProvided() {
        // When
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // Then
        assertNotNull(dto);
        assertNull(dto.getProjectId());
        assertNull(dto.getName());
        assertNull(dto.getRepoUrl());
        assertNull(dto.getUuaa());
        assertNull(dto.getCountryDetName());
        assertNull(dto.getApplication());
        assertNull(dto.getLastScan());
        assertNull(dto.getChimeraUrl());
        assertNull(dto.getBranch());
        assertNull(dto.getAnalyzer());
        assertNull(dto.getArq());
        assertNull(dto.getLanguage());
        assertNull(dto.getStockFlow());
        assertNull(dto.getAssumed1());
        assertNull(dto.getAssumed2());
        assertNull(dto.getHigh());
        assertNull(dto.getCritical());
        assertNull(dto.getMedium());
        assertNull(dto.getLow());
        assertNull(dto.getToReview());
        assertNull(dto.getLines());
    }

    @Test
    void constructor_ShouldCreateDTO_WhenAllArgumentsProvided() {
        // Given
        String projectId = "PROJ-123";
        String name = "Test Application";
        String repoUrl = "https://github.com/test/repo";
        String uuaa = "1234";
        String countryDetName = "Argentina";
        String application = "TestApp";
        LocalDateTime lastScan = LocalDateTime.of(2025, 11, 26, 10, 30, 0);
        String chimeraUrl = "https://chimera.test.com";
        String branch = "main";
        String analyzer = "sonar";
        String arq = "microservices";
        String language = "Java";
        String stockFlow = "stock";
        Integer assumed1 = 5;
        Integer assumed2 = 3;
        Integer high = 10;
        Integer critical = 2;
        Integer medium = 15;
        Integer low = 20;
        Integer toReview = 8;
        Integer lines = 5000;

        // When
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO(
            projectId, name, repoUrl, uuaa, countryDetName, application,
            lastScan, chimeraUrl, branch, analyzer, arq, language,
            stockFlow, assumed1, assumed2, high, critical, medium, low, toReview, lines
        );

        // Then
        assertNotNull(dto);
        assertEquals(projectId, dto.getProjectId());
        assertEquals(name, dto.getName());
        assertEquals(repoUrl, dto.getRepoUrl());
        assertEquals(uuaa, dto.getUuaa());
        assertEquals(countryDetName, dto.getCountryDetName());
        assertEquals(application, dto.getApplication());
        assertEquals(lastScan, dto.getLastScan());
        assertEquals(chimeraUrl, dto.getChimeraUrl());
        assertEquals(branch, dto.getBranch());
        assertEquals(analyzer, dto.getAnalyzer());
        assertEquals(arq, dto.getArq());
        assertEquals(language, dto.getLanguage());
        assertEquals(stockFlow, dto.getStockFlow());
        assertEquals(assumed1, dto.getAssumed1());
        assertEquals(assumed2, dto.getAssumed2());
        assertEquals(high, dto.getHigh());
        assertEquals(critical, dto.getCritical());
        assertEquals(medium, dto.getMedium());
        assertEquals(low, dto.getLow());
        assertEquals(toReview, dto.getToReview());
        assertEquals(lines, dto.getLines());
    }

    @Test
    void setProjectId_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setProjectId("PROJ-456");

        // Then
        assertEquals("PROJ-456", dto.getProjectId());
    }

    @Test
    void setName_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setName("New Application");

        // Then
        assertEquals("New Application", dto.getName());
    }

    @Test
    void setRepoUrl_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setRepoUrl("https://github.com/new/repo");

        // Then
        assertEquals("https://github.com/new/repo", dto.getRepoUrl());
    }

    @Test
    void setUuaa_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setUuaa("5678");

        // Then
        assertEquals("5678", dto.getUuaa());
    }

    @Test
    void setCountryDetName_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setCountryDetName("España");

        // Then
        assertEquals("España", dto.getCountryDetName());
    }

    @Test
    void setApplication_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setApplication("MyApp");

        // Then
        assertEquals("MyApp", dto.getApplication());
    }

    @Test
    void setLastScan_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();
        LocalDateTime lastScan = LocalDateTime.of(2025, 12, 1, 15, 45, 30);

        // When
        dto.setLastScan(lastScan);

        // Then
        assertEquals(lastScan, dto.getLastScan());
    }

    @Test
    void setChimeraUrl_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setChimeraUrl("https://chimera.example.com/report");

        // Then
        assertEquals("https://chimera.example.com/report", dto.getChimeraUrl());
    }

    @Test
    void setBranch_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setBranch("develop");

        // Then
        assertEquals("develop", dto.getBranch());
    }

    @Test
    void setAnalyzer_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setAnalyzer("checkmarx");

        // Then
        assertEquals("checkmarx", dto.getAnalyzer());
    }

    @Test
    void setArq_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setArq("monolith");

        // Then
        assertEquals("monolith", dto.getArq());
    }

    @Test
    void setLanguage_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setLanguage("Python");

        // Then
        assertEquals("Python", dto.getLanguage());
    }

    @Test
    void setStockFlow_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setStockFlow("flow");

        // Then
        assertEquals("flow", dto.getStockFlow());
    }

    @Test
    void setAssumed1_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setAssumed1(10);

        // Then
        assertEquals(10, dto.getAssumed1());
    }

    @Test
    void setAssumed2_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setAssumed2(7);

        // Then
        assertEquals(7, dto.getAssumed2());
    }

    @Test
    void setHigh_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setHigh(15);

        // Then
        assertEquals(15, dto.getHigh());
    }

    @Test
    void setCritical_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setCritical(5);

        // Then
        assertEquals(5, dto.getCritical());
    }

    @Test
    void setMedium_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setMedium(25);

        // Then
        assertEquals(25, dto.getMedium());
    }

    @Test
    void setLow_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setLow(50);

        // Then
        assertEquals(50, dto.getLow());
    }

    @Test
    void setToReview_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setToReview(12);

        // Then
        assertEquals(12, dto.getToReview());
    }

    @Test
    void setLines_ShouldSetValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setLines(10000);

        // Then
        assertEquals(10000, dto.getLines());
    }

    @Test
    void setters_ShouldAllowNullValues() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();
        dto.setProjectId("PROJ-123");
        dto.setName("Test");
        dto.setLastScan(LocalDateTime.now());

        // When
        dto.setProjectId(null);
        dto.setName(null);
        dto.setRepoUrl(null);
        dto.setUuaa(null);
        dto.setCountryDetName(null);
        dto.setApplication(null);
        dto.setLastScan(null);
        dto.setChimeraUrl(null);
        dto.setBranch(null);
        dto.setAnalyzer(null);
        dto.setArq(null);
        dto.setLanguage(null);
        dto.setStockFlow(null);
        dto.setAssumed1(null);
        dto.setAssumed2(null);
        dto.setHigh(null);
        dto.setCritical(null);
        dto.setMedium(null);
        dto.setLow(null);
        dto.setToReview(null);
        dto.setLines(null);

        // Then
        assertNull(dto.getProjectId());
        assertNull(dto.getName());
        assertNull(dto.getRepoUrl());
        assertNull(dto.getUuaa());
        assertNull(dto.getCountryDetName());
        assertNull(dto.getApplication());
        assertNull(dto.getLastScan());
        assertNull(dto.getChimeraUrl());
        assertNull(dto.getBranch());
        assertNull(dto.getAnalyzer());
        assertNull(dto.getArq());
        assertNull(dto.getLanguage());
        assertNull(dto.getStockFlow());
        assertNull(dto.getAssumed1());
        assertNull(dto.getAssumed2());
        assertNull(dto.getHigh());
        assertNull(dto.getCritical());
        assertNull(dto.getMedium());
        assertNull(dto.getLow());
        assertNull(dto.getToReview());
        assertNull(dto.getLines());
    }

    @Test
    void setters_ShouldAllowZeroValues() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setAssumed1(0);
        dto.setAssumed2(0);
        dto.setHigh(0);
        dto.setCritical(0);
        dto.setMedium(0);
        dto.setLow(0);
        dto.setToReview(0);
        dto.setLines(0);

        // Then
        assertEquals(0, dto.getAssumed1());
        assertEquals(0, dto.getAssumed2());
        assertEquals(0, dto.getHigh());
        assertEquals(0, dto.getCritical());
        assertEquals(0, dto.getMedium());
        assertEquals(0, dto.getLow());
        assertEquals(0, dto.getToReview());
        assertEquals(0, dto.getLines());
    }

    @Test
    void setters_ShouldAllowNegativeValues() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setAssumed1(-1);
        dto.setAssumed2(-2);
        dto.setHigh(-3);
        dto.setCritical(-4);
        dto.setMedium(-5);
        dto.setLow(-6);
        dto.setToReview(-7);
        dto.setLines(-8);

        // Then
        assertEquals(-1, dto.getAssumed1());
        assertEquals(-2, dto.getAssumed2());
        assertEquals(-3, dto.getHigh());
        assertEquals(-4, dto.getCritical());
        assertEquals(-5, dto.getMedium());
        assertEquals(-6, dto.getLow());
        assertEquals(-7, dto.getToReview());
        assertEquals(-8, dto.getLines());
    }

    @Test
    void setters_ShouldAllowEmptyStrings() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setProjectId("");
        dto.setName("");
        dto.setRepoUrl("");
        dto.setUuaa("");
        dto.setCountryDetName("");
        dto.setApplication("");
        dto.setChimeraUrl("");
        dto.setBranch("");
        dto.setAnalyzer("");
        dto.setArq("");
        dto.setLanguage("");
        dto.setStockFlow("");

        // Then
        assertEquals("", dto.getProjectId());
        assertEquals("", dto.getName());
        assertEquals("", dto.getRepoUrl());
        assertEquals("", dto.getUuaa());
        assertEquals("", dto.getCountryDetName());
        assertEquals("", dto.getApplication());
        assertEquals("", dto.getChimeraUrl());
        assertEquals("", dto.getBranch());
        assertEquals("", dto.getAnalyzer());
        assertEquals("", dto.getArq());
        assertEquals("", dto.getLanguage());
        assertEquals("", dto.getStockFlow());
    }

    @Test
    void setters_ShouldAllowWhitespaceStrings() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setProjectId("   ");
        dto.setName("   ");
        dto.setBranch("   ");

        // Then
        assertEquals("   ", dto.getProjectId());
        assertEquals("   ", dto.getName());
        assertEquals("   ", dto.getBranch());
    }

    @Test
    void toString_ShouldReturnNonNullString() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        String result = dto.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("ChimeraSastImportDTO"));
    }

    @Test
    void toString_ShouldContainAllFields() {
        // Given
        LocalDateTime lastScan = LocalDateTime.of(2025, 11, 26, 10, 30, 0);
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO(
            "PROJ-123", "TestApp", "https://repo.url", "1234", "Argentina",
            "Application", lastScan, "https://chimera.url", "main", "sonar",
            "micro", "Java", "stock", 5, 3, 10, 2, 15, 20, 8, 5000
        );

        // When
        String result = dto.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("PROJ-123"));
        assertTrue(result.contains("TestApp"));
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameObject() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();
        dto.setProjectId("PROJ-123");

        // When & Then
        assertEquals(dto, dto);
    }

    @Test
    void equals_ShouldReturnTrue_WhenEqualObjects() {
        // Given
        LocalDateTime lastScan = LocalDateTime.of(2025, 11, 26, 10, 30, 0);
        ChimeraSastImportDTO dto1 = new ChimeraSastImportDTO(
            "PROJ-123", "TestApp", "https://repo.url", "1234", "Argentina",
            "Application", lastScan, "https://chimera.url", "main", "sonar",
            "micro", "Java", "stock", 5, 3, 10, 2, 15, 20, 8, 5000
        );
        ChimeraSastImportDTO dto2 = new ChimeraSastImportDTO(
            "PROJ-123", "TestApp", "https://repo.url", "1234", "Argentina",
            "Application", lastScan, "https://chimera.url", "main", "sonar",
            "micro", "Java", "stock", 5, 3, 10, 2, 15, 20, 8, 5000
        );

        // When & Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentObjects() {
        // Given
        ChimeraSastImportDTO dto1 = new ChimeraSastImportDTO();
        dto1.setProjectId("PROJ-123");
        
        ChimeraSastImportDTO dto2 = new ChimeraSastImportDTO();
        dto2.setProjectId("PROJ-456");

        // When & Then
        assertNotEquals(dto1, dto2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparedWithNull() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When & Then
        assertNotEquals(null, dto);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparedWithDifferentType() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When & Then
        assertNotEquals("string", dto);
    }

    @Test
    void hashCode_ShouldReturnSameValue_ForEqualObjects() {
        // Given
        ChimeraSastImportDTO dto1 = new ChimeraSastImportDTO();
        dto1.setProjectId("PROJ-123");
        dto1.setName("Test");
        
        ChimeraSastImportDTO dto2 = new ChimeraSastImportDTO();
        dto2.setProjectId("PROJ-123");
        dto2.setName("Test");

        // When & Then
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void hashCode_ShouldReturnConsistentValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();
        dto.setProjectId("PROJ-123");

        // When
        int hash1 = dto.hashCode();
        int hash2 = dto.hashCode();

        // Then
        assertEquals(hash1, hash2);
    }

    @Test
    void constructor_ShouldAllowNullValues() {
        // When
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO(
            null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null,
            null, null, null, null, null
        );

        // Then
        assertNotNull(dto);
        assertNull(dto.getProjectId());
        assertNull(dto.getName());
        assertNull(dto.getLastScan());
    }

    @Test
    void lastScan_ShouldHandleDifferentDateTimes() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When - midnight
        LocalDateTime midnight = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        dto.setLastScan(midnight);
        assertEquals(midnight, dto.getLastScan());

        // When - end of day
        LocalDateTime endOfDay = LocalDateTime.of(2025, 12, 31, 23, 59, 59);
        dto.setLastScan(endOfDay);
        assertEquals(endOfDay, dto.getLastScan());
    }

    @Test
    void stockFlow_ShouldAcceptAnyValue() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When & Then - stock
        dto.setStockFlow("stock");
        assertEquals("stock", dto.getStockFlow());

        // When & Then - flow
        dto.setStockFlow("flow");
        assertEquals("flow", dto.getStockFlow());

        // When & Then - any other value
        dto.setStockFlow("invalid");
        assertEquals("invalid", dto.getStockFlow());
    }

    @Test
    void setters_ShouldAllowLargeIntegerValues() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setAssumed1(Integer.MAX_VALUE);
        dto.setAssumed2(Integer.MAX_VALUE);
        dto.setHigh(Integer.MAX_VALUE);
        dto.setCritical(Integer.MAX_VALUE);
        dto.setMedium(Integer.MAX_VALUE);
        dto.setLow(Integer.MAX_VALUE);
        dto.setToReview(Integer.MAX_VALUE);
        dto.setLines(Integer.MAX_VALUE);

        // Then
        assertEquals(Integer.MAX_VALUE, dto.getAssumed1());
        assertEquals(Integer.MAX_VALUE, dto.getAssumed2());
        assertEquals(Integer.MAX_VALUE, dto.getHigh());
        assertEquals(Integer.MAX_VALUE, dto.getCritical());
        assertEquals(Integer.MAX_VALUE, dto.getMedium());
        assertEquals(Integer.MAX_VALUE, dto.getLow());
        assertEquals(Integer.MAX_VALUE, dto.getToReview());
        assertEquals(Integer.MAX_VALUE, dto.getLines());
    }

    @Test
    void setters_ShouldHandleSpecialCharactersInStrings() {
        // Given
        ChimeraSastImportDTO dto = new ChimeraSastImportDTO();

        // When
        dto.setProjectId("PROJ-123!@#$%");
        dto.setName("Name with 'quotes' and \"double quotes\"");
        dto.setRepoUrl("https://github.com/test?param=value&other=123");
        dto.setCountryDetName("España ñ áéíóú");

        // Then
        assertEquals("PROJ-123!@#$%", dto.getProjectId());
        assertEquals("Name with 'quotes' and \"double quotes\"", dto.getName());
        assertEquals("https://github.com/test?param=value&other=123", dto.getRepoUrl());
        assertEquals("España ñ áéíóú", dto.getCountryDetName());
    }
}
