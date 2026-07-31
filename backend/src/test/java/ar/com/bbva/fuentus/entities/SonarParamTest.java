package ar.com.bbva.fuentus.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SonarParamTest {

    private SonarParam sonarParam1;
    private SonarParam sonarParam2;
    private Date testDate;

    @BeforeEach
    void setUp() {
        testDate = new Date();
        
        sonarParam1 = new SonarParam();
        sonarParam1.setId(1L);
        sonarParam1.setAppId(100L);
        sonarParam1.setAnalisisDate(testDate);
        sonarParam1.setCoverage(85.5);
        sonarParam1.setBugs(3L);
        sonarParam1.setBranch("main");
        sonarParam1.setSonarVersion(10);

        sonarParam2 = new SonarParam();
        sonarParam2.setId(1L);
        sonarParam2.setAppId(100L);
        sonarParam2.setAnalisisDate(testDate);
        sonarParam2.setCoverage(85.5);
        sonarParam2.setBugs(3L);
        sonarParam2.setBranch("main");
        sonarParam2.setSonarVersion(10);
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameId() {
        // Given - Both objects have same ID (1L)

        // When & Then
        assertEquals(sonarParam1, sonarParam2);
        assertTrue(sonarParam1.equals(sonarParam2));
        assertTrue(sonarParam2.equals(sonarParam1));
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentId() {
        // Given
        sonarParam2.setId(2L);

        // When & Then
        assertNotEquals(sonarParam1, sonarParam2);
        assertFalse(sonarParam1.equals(sonarParam2));
        assertFalse(sonarParam2.equals(sonarParam1));
    }

    @Test
    void equals_ShouldReturnFalse_WhenOneIdIsNull() {
        // Given
        sonarParam1.setId(null);
        sonarParam2.setId(1L);

        // When & Then
        assertNotEquals(sonarParam1, sonarParam2);
        assertFalse(sonarParam1.equals(sonarParam2));
        assertFalse(sonarParam2.equals(sonarParam1));
    }

    @Test
    void equals_ShouldReturnTrue_WhenBothIdsAreNull() {
        // Given
        sonarParam1.setId(null);
        sonarParam2.setId(null);

        // When & Then
        assertEquals(sonarParam1, sonarParam2);
        assertTrue(sonarParam1.equals(sonarParam2));
        assertTrue(sonarParam2.equals(sonarParam1));
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparedWithNull() {
        // When & Then
        assertNotEquals(sonarParam1, null);
        assertFalse(sonarParam1.equals(null));
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparedWithDifferentClass() {
        // Given
        String differentObject = "not a SonarParam";

        // When & Then
        assertNotEquals(sonarParam1, differentObject);
        assertFalse(sonarParam1.equals(differentObject));
    }

    @Test
    void equals_ShouldReturnTrue_WhenComparedWithSelf() {
        // When & Then
        assertEquals(sonarParam1, sonarParam1);
        assertTrue(sonarParam1.equals(sonarParam1));
    }

    @Test
    void hashCode_ShouldReturnSameValue_ForEqualObjects() {
        // Given - Both objects have same ID (1L)

        // When & Then
        assertEquals(sonarParam1.hashCode(), sonarParam2.hashCode());
    }

    @Test
    void hashCode_ShouldReturnDifferentValue_ForDifferentIds() {
        // Given
        sonarParam2.setId(2L);

        // When & Then
        assertNotEquals(sonarParam1.hashCode(), sonarParam2.hashCode());
    }

    @Test
    void hashCode_ShouldHandleNullId() {
        // Given
        sonarParam1.setId(null);

        // When & Then
        assertDoesNotThrow(() -> sonarParam1.hashCode());
        assertEquals(0, sonarParam1.hashCode());
    }

    @Test
    void toString_ShouldContainIdValue() {
        // When
        String result = sonarParam1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("SonarParam"));
    }

    @Test
    void toString_ShouldHandleNullId() {
        // Given
        sonarParam1.setId(null);

        // When
        String result = sonarParam1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("id=null"));
        assertTrue(result.contains("SonarParam"));
    }

    @Test
    void toString_ShouldContainCorrectFormat() {
        // Given
        sonarParam1.setId(123L);

        // When
        String result = sonarParam1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("id=123"));
        assertTrue(result.contains("SonarParam"));
        assertTrue(result.contains("["));
        assertTrue(result.contains("]"));
        // Verify the complete expected format
        assertEquals("ar.com.bbva.scrapper.entities.SonarParam[ id=123 ]", result);
    }

    @Test
    void toString_ShouldBeConsistent() {
        // Given
        sonarParam1.setId(456L);

        // When
        String result1 = sonarParam1.toString();
        String result2 = sonarParam1.toString();

        // Then
        assertEquals(result1, result2);
    }

    @Test
    void constructors_ShouldWorkCorrectly() {
        // Test default constructor
        SonarParam defaultParam = new SonarParam();
        assertNotNull(defaultParam);
        assertNull(defaultParam.getId());
        assertEquals(0L, defaultParam.getAppId()); // primitive long defaults to 0
        assertNull(defaultParam.getAnalisisDate());
        assertNull(defaultParam.getTotalLines());
        assertNull(defaultParam.getUncoveredLines());
        assertNull(defaultParam.getBugs());
        assertNull(defaultParam.getBugsRating());
        assertNull(defaultParam.getVulnerabilities());
        assertNull(defaultParam.getVulnerabilitiesRating());
        assertNull(defaultParam.getDebt());
        assertNull(defaultParam.getDebtRating());
        assertNull(defaultParam.getCodeSmells());
        assertNull(defaultParam.getDuplications());
        assertNull(defaultParam.getDuplicatedBlocks());
        assertNull(defaultParam.getCoverage());
        assertNull(defaultParam.getBranch());
        assertNull(defaultParam.getSonarVersion());

        // Test constructor with ID
        SonarParam paramWithId = new SonarParam(5L);
        assertNotNull(paramWithId);
        assertEquals(5L, paramWithId.getId());
        assertEquals(0L, paramWithId.getAppId()); // primitive long defaults to 0
        assertNull(paramWithId.getAnalisisDate());

        // Test constructor with ID, appId, and date
        Date date = new Date();
        SonarParam paramWithDetails = new SonarParam(10L, 200L, date);
        assertNotNull(paramWithDetails);
        assertEquals(10L, paramWithDetails.getId());
        assertEquals(200L, paramWithDetails.getAppId());
        assertEquals(date, paramWithDetails.getAnalisisDate());
        // Other fields should still be null/default
        assertNull(paramWithDetails.getTotalLines());
        assertNull(paramWithDetails.getUncoveredLines());
        assertNull(paramWithDetails.getBugs());

        // Test constructor with null values
        SonarParam paramWithNulls = new SonarParam(null, 300L, null);
        assertNotNull(paramWithNulls);
        assertNull(paramWithNulls.getId());
        assertEquals(300L, paramWithNulls.getAppId());
        assertNull(paramWithNulls.getAnalisisDate());
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Test ID
        sonarParam1.setId(999L);
        assertEquals(999L, sonarParam1.getId());
        sonarParam1.setId(null);
        assertNull(sonarParam1.getId());

        // Test AppId
        sonarParam1.setAppId(500L);
        assertEquals(500L, sonarParam1.getAppId());

        // Test AnalisisDate
        Date newDate = new Date();
        sonarParam1.setAnalisisDate(newDate);
        assertEquals(newDate, sonarParam1.getAnalisisDate());
        sonarParam1.setAnalisisDate(null);
        assertNull(sonarParam1.getAnalisisDate());

        // Test TotalLines
        sonarParam1.setTotalLines(1000L);
        assertEquals(1000L, sonarParam1.getTotalLines());
        sonarParam1.setTotalLines(null);
        assertNull(sonarParam1.getTotalLines());

        // Test UncoveredLines
        sonarParam1.setUncoveredLines(50L);
        assertEquals(50L, sonarParam1.getUncoveredLines());
        sonarParam1.setUncoveredLines(null);
        assertNull(sonarParam1.getUncoveredLines());

        // Test Bugs
        sonarParam1.setBugs(5L);
        assertEquals(5L, sonarParam1.getBugs());
        sonarParam1.setBugs(null);
        assertNull(sonarParam1.getBugs());

        // Test BugsRating
        sonarParam1.setBugsRating("A");
        assertEquals("A", sonarParam1.getBugsRating());
        sonarParam1.setBugsRating(null);
        assertNull(sonarParam1.getBugsRating());

        // Test Vulnerabilities
        sonarParam1.setVulnerabilities(2L);
        assertEquals(2L, sonarParam1.getVulnerabilities());
        sonarParam1.setVulnerabilities(null);
        assertNull(sonarParam1.getVulnerabilities());

        // Test VulnerabilitiesRating
        sonarParam1.setVulnerabilitiesRating("B");
        assertEquals("B", sonarParam1.getVulnerabilitiesRating());
        sonarParam1.setVulnerabilitiesRating(null);
        assertNull(sonarParam1.getVulnerabilitiesRating());

        // Test Debt
        sonarParam1.setDebt("1h 30m");
        assertEquals("1h 30m", sonarParam1.getDebt());
        sonarParam1.setDebt(null);
        assertNull(sonarParam1.getDebt());

        // Test DebtRating
        sonarParam1.setDebtRating("C");
        assertEquals("C", sonarParam1.getDebtRating());
        sonarParam1.setDebtRating(null);
        assertNull(sonarParam1.getDebtRating());

        // Test CodeSmells
        sonarParam1.setCodeSmells("10");
        assertEquals("10", sonarParam1.getCodeSmells());
        sonarParam1.setCodeSmells(null);
        assertNull(sonarParam1.getCodeSmells());

        // Test Duplications
        sonarParam1.setDuplications("5.0%");
        assertEquals("5.0%", sonarParam1.getDuplications());
        sonarParam1.setDuplications(null);
        assertNull(sonarParam1.getDuplications());

        // Test DuplicatedBlocks
        sonarParam1.setDuplicatedBlocks("3");
        assertEquals("3", sonarParam1.getDuplicatedBlocks());
        sonarParam1.setDuplicatedBlocks(null);
        assertNull(sonarParam1.getDuplicatedBlocks());

        // Test Coverage
        sonarParam1.setCoverage(95.0);
        assertEquals(95.0, sonarParam1.getCoverage());
        sonarParam1.setCoverage(null);
        assertNull(sonarParam1.getCoverage());

        // Test Branch
        sonarParam1.setBranch("develop");
        assertEquals("develop", sonarParam1.getBranch());
        sonarParam1.setBranch(null);
        assertNull(sonarParam1.getBranch());

        // Test SonarVersion
        sonarParam1.setSonarVersion(9);
        assertEquals(9, sonarParam1.getSonarVersion());
        sonarParam1.setSonarVersion(null);
        assertNull(sonarParam1.getSonarVersion());
    }

    @Test
    void gettersAndSetters_ShouldHandleEdgeCases() {
        // Test with boundary values for numeric fields
        sonarParam1.setId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, sonarParam1.getId());
        
        sonarParam1.setId(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, sonarParam1.getId());

        sonarParam1.setAppId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, sonarParam1.getAppId());

        sonarParam1.setTotalLines(0L);
        assertEquals(0L, sonarParam1.getTotalLines());

        sonarParam1.setUncoveredLines(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, sonarParam1.getUncoveredLines());

        sonarParam1.setBugs(0L);
        assertEquals(0L, sonarParam1.getBugs());

        sonarParam1.setVulnerabilities(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, sonarParam1.getVulnerabilities());

        // Test coverage with boundary values
        sonarParam1.setCoverage(0.0);
        assertEquals(0.0, sonarParam1.getCoverage());

        sonarParam1.setCoverage(100.0);
        assertEquals(100.0, sonarParam1.getCoverage());

        sonarParam1.setCoverage(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, sonarParam1.getCoverage());

        // Test sonar version with boundary values
        sonarParam1.setSonarVersion(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, sonarParam1.getSonarVersion());

        sonarParam1.setSonarVersion(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, sonarParam1.getSonarVersion());

        // Test string fields with special characters
        sonarParam1.setBugsRating("A+");
        assertEquals("A+", sonarParam1.getBugsRating());

        sonarParam1.setVulnerabilitiesRating("E-");
        assertEquals("E-", sonarParam1.getVulnerabilitiesRating());

        sonarParam1.setDebt("0min");
        assertEquals("0min", sonarParam1.getDebt());

        sonarParam1.setDebtRating("F");
        assertEquals("F", sonarParam1.getDebtRating());

        sonarParam1.setCodeSmells("0");
        assertEquals("0", sonarParam1.getCodeSmells());

        sonarParam1.setDuplications("0.0%");
        assertEquals("0.0%", sonarParam1.getDuplications());

        sonarParam1.setDuplicatedBlocks("0");
        assertEquals("0", sonarParam1.getDuplicatedBlocks());

        // Test with empty strings
        sonarParam1.setBranch("");
        assertEquals("", sonarParam1.getBranch());

        sonarParam1.setBugsRating("");
        assertEquals("", sonarParam1.getBugsRating());
    }

    @Test
    void gettersAndSetters_ShouldHandleSpecialStringValues() {
        // Test with special string values
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("a");
        }
        String longString = sb.toString();
        sonarParam1.setDebt(longString);
        assertEquals(longString, sonarParam1.getDebt());

        String stringWithSpaces = "feature/JIRA-123 branch name";
        sonarParam1.setBranch(stringWithSpaces);
        assertEquals(stringWithSpaces, sonarParam1.getBranch());

        String stringWithNumbers = "123456789";
        sonarParam1.setCodeSmells(stringWithNumbers);
        assertEquals(stringWithNumbers, sonarParam1.getCodeSmells());

        String percentageString = "99.99%";
        sonarParam1.setDuplications(percentageString);
        assertEquals(percentageString, sonarParam1.getDuplications());
    }

    @Test
    void appId_ShouldHandlePrimitiveLong() {
        // AppId is a primitive long, so it can't be null
        // Test various values
        sonarParam1.setAppId(0L);
        assertEquals(0L, sonarParam1.getAppId());

        sonarParam1.setAppId(-1L);
        assertEquals(-1L, sonarParam1.getAppId());

        sonarParam1.setAppId(9223372036854775807L); // Long.MAX_VALUE
        assertEquals(9223372036854775807L, sonarParam1.getAppId());
    }

    @Test
    void date_ShouldHandleDifferentDateValues() {
        // Test with current date
        Date now = new Date();
        sonarParam1.setAnalisisDate(now);
        assertEquals(now, sonarParam1.getAnalisisDate());

        // Test with past date
        Date pastDate = new Date(0); // January 1, 1970
        sonarParam1.setAnalisisDate(pastDate);
        assertEquals(pastDate, sonarParam1.getAnalisisDate());

        // Test with future date
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // Tomorrow
        sonarParam1.setAnalisisDate(futureDate);
        assertEquals(futureDate, sonarParam1.getAnalisisDate());
    }

    @Test
    void equalsContract_ShouldBeReflexive() {
        // Reflexive: x.equals(x) should return true
        assertTrue(sonarParam1.equals(sonarParam1));
    }

    @Test
    void equalsContract_ShouldBeSymmetric() {
        // Symmetric: x.equals(y) should return same as y.equals(x)
        boolean result1 = sonarParam1.equals(sonarParam2);
        boolean result2 = sonarParam2.equals(sonarParam1);
        assertEquals(result1, result2);
    }

    @Test
    void equalsContract_ShouldBeTransitive() {
        // Transitive: if x.equals(y) and y.equals(z), then x.equals(z)
        SonarParam sonarParam3 = new SonarParam();
        sonarParam3.setId(1L);

        assertTrue(sonarParam1.equals(sonarParam2));
        assertTrue(sonarParam2.equals(sonarParam3));
        assertTrue(sonarParam1.equals(sonarParam3));
    }

    @Test
    void equalsContract_ShouldBeConsistent() {
        // Consistent: multiple invocations should return same result
        boolean result1 = sonarParam1.equals(sonarParam2);
        boolean result2 = sonarParam1.equals(sonarParam2);
        boolean result3 = sonarParam1.equals(sonarParam2);

        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }
}
