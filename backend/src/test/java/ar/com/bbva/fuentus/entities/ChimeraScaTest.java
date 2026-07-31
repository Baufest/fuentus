package ar.com.bbva.fuentus.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChimeraScaTest {

    private ChimeraSca chimeraSca1;
    private ChimeraSca chimeraSca2;

    @BeforeEach
    void setUp() {
        chimeraSca1 = new ChimeraSca();
        chimeraSca2 = new ChimeraSca();
    }

    @Test
    void testDefaultConstructor() {
        // Given & When
        ChimeraSca chimeraSca = new ChimeraSca();

        // Then
        assertNotNull(chimeraSca);
        assertNull(chimeraSca.getId());
        assertNull(chimeraSca.getProjectId());
        assertNull(chimeraSca.getAppId());
        assertNull(chimeraSca.getName());
        assertNull(chimeraSca.getUuaa());
        assertNull(chimeraSca.getLow());
        assertNull(chimeraSca.getMedium());
        assertNull(chimeraSca.getHigh());
        assertNull(chimeraSca.getCritical());
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        Long testId = 1L;
        String testProjectId = "PROJECT_123";
        Long testAppId = 100L;
        String testName = "Test App";
        String testUuaa = "TEST";
        Long testLow = 5L;
        Long testMedium = 3L;
        Long testHigh = 2L;
        Long testCritical = 1L;

        // When
        ChimeraSca chimeraSca = new ChimeraSca(testId, testProjectId, testAppId, testName, testUuaa, testLow, testMedium, testHigh, testCritical);

        // Then
        assertNotNull(chimeraSca);
        assertEquals(testId, chimeraSca.getId());
        assertEquals(testProjectId, chimeraSca.getProjectId());
        assertEquals(testAppId, chimeraSca.getAppId());
        assertEquals(testName, chimeraSca.getName());
        assertEquals(testUuaa, chimeraSca.getUuaa());
        assertEquals(testLow, chimeraSca.getLow());
        assertEquals(testMedium, chimeraSca.getMedium());
        assertEquals(testHigh, chimeraSca.getHigh());
        assertEquals(testCritical, chimeraSca.getCritical());
    }

    @Test
    void testNoArgsConstructor() {
        // Given & When
        ChimeraSca chimeraSca = new ChimeraSca();

        // Then
        assertNotNull(chimeraSca);
        assertNull(chimeraSca.getId());
        assertNull(chimeraSca.getProjectId());
        assertNull(chimeraSca.getAppId());
        assertNull(chimeraSca.getName());
        assertNull(chimeraSca.getUuaa());
        assertNull(chimeraSca.getLow());
        assertNull(chimeraSca.getMedium());
        assertNull(chimeraSca.getHigh());
        assertNull(chimeraSca.getCritical());
    }

    @Test
    void testGettersAndSetters() {
        // Test ID
        Long testId = 1L;
        chimeraSca1.setId(testId);
        assertEquals(testId, chimeraSca1.getId());

        // Test ProjectId
        String testProjectId = "PROJECT_123";
        chimeraSca1.setProjectId(testProjectId);
        assertEquals(testProjectId, chimeraSca1.getProjectId());

        // Test AppId
        Long testAppId = 100L;
        chimeraSca1.setAppId(testAppId);
        assertEquals(testAppId, chimeraSca1.getAppId());

        // Test Name
        String testName = "Test App";
        chimeraSca1.setName(testName);
        assertEquals(testName, chimeraSca1.getName());

        // Test Uuaa
        String testUuaa = "TEST";
        chimeraSca1.setUuaa(testUuaa);
        assertEquals(testUuaa, chimeraSca1.getUuaa());

        // Test Low
        Long testLow = 5L;
        chimeraSca1.setLow(testLow);
        assertEquals(testLow, chimeraSca1.getLow());

        // Test Medium
        Long testMedium = 3L;
        chimeraSca1.setMedium(testMedium);
        assertEquals(testMedium, chimeraSca1.getMedium());

        // Test High
        Long testHigh = 2L;
        chimeraSca1.setHigh(testHigh);
        assertEquals(testHigh, chimeraSca1.getHigh());

        // Test Critical
        Long testCritical = 1L;
        chimeraSca1.setCritical(testCritical);
        assertEquals(testCritical, chimeraSca1.getCritical());

        // Test with null values
        chimeraSca1.setId(null);
        assertNull(chimeraSca1.getId());

        chimeraSca1.setProjectId(null);
        assertNull(chimeraSca1.getProjectId());

        chimeraSca1.setAppId(null);
        assertNull(chimeraSca1.getAppId());

        chimeraSca1.setName(null);
        assertNull(chimeraSca1.getName());

        chimeraSca1.setUuaa(null);
        assertNull(chimeraSca1.getUuaa());

        chimeraSca1.setLow(null);
        assertNull(chimeraSca1.getLow());

        chimeraSca1.setMedium(null);
        assertNull(chimeraSca1.getMedium());

        chimeraSca1.setHigh(null);
        assertNull(chimeraSca1.getHigh());

        chimeraSca1.setCritical(null);
        assertNull(chimeraSca1.getCritical());
    }

    @Test
    void testEquals_SameObject() {
        // When & Then
        assertEquals(chimeraSca1, chimeraSca1);
        assertTrue(chimeraSca1.equals(chimeraSca1));
    }

    @Test
    void testEquals_NullObject() {
        // When & Then
        assertNotEquals(chimeraSca1, null);
        assertFalse(chimeraSca1.equals(null));
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        String differentObject = "not a ChimeraSca";

        // When & Then
        assertNotEquals(chimeraSca1, differentObject);
        assertFalse(chimeraSca1.equals(differentObject));
    }

    @Test
    void testEquals_SameValues() {
        // Given
        setupSameValues();

        // When & Then
        assertEquals(chimeraSca1, chimeraSca2);
        assertTrue(chimeraSca1.equals(chimeraSca2));
        assertTrue(chimeraSca2.equals(chimeraSca1));
    }

    @Test
    void testEquals_DifferentValues() {
        // Given
        setupSameValues();
        chimeraSca2.setId(2L); // Make them different

        // When & Then
        assertNotEquals(chimeraSca1, chimeraSca2);
        assertFalse(chimeraSca1.equals(chimeraSca2));
        assertFalse(chimeraSca2.equals(chimeraSca1));
    }

    @Test
    void testEquals_NullValues() {
        // Given - both objects have all null values by default

        // When & Then
        assertEquals(chimeraSca1, chimeraSca2);
        assertTrue(chimeraSca1.equals(chimeraSca2));
        assertTrue(chimeraSca2.equals(chimeraSca1));
    }

    @Test
    void testHashCode_SameValues() {
        // Given
        setupSameValues();

        // When & Then
        assertEquals(chimeraSca1.hashCode(), chimeraSca2.hashCode());
    }

    @Test
    void testHashCode_DifferentValues() {
        // Given
        setupSameValues();
        chimeraSca2.setId(2L); // Make them different

        // When & Then
        assertNotEquals(chimeraSca1.hashCode(), chimeraSca2.hashCode());
    }

    @Test
    void testHashCode_NullValues() {
        // Given - all values are null by default

        // When & Then
        assertDoesNotThrow(() -> chimeraSca1.hashCode());
        assertEquals(chimeraSca1.hashCode(), chimeraSca2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        setupSameValues();

        // When
        String result = chimeraSca1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("ChimeraSca"));
    }

    @Test
    void testToString_NullValues() {
        // Given - all values are null by default

        // When
        String result = chimeraSca1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("ChimeraSca"));
    }

    @Test
    void testEqualsContract_Reflexive() {
        // Reflexive: x.equals(x) should return true
        assertTrue(chimeraSca1.equals(chimeraSca1));
    }

    @Test
    void testEqualsContract_Symmetric() {
        // Symmetric: x.equals(y) should return same as y.equals(x)
        setupSameValues();

        boolean result1 = chimeraSca1.equals(chimeraSca2);
        boolean result2 = chimeraSca2.equals(chimeraSca1);
        assertEquals(result1, result2);
    }

    @Test
    void testEqualsContract_Transitive() {
        // Transitive: if x.equals(y) and y.equals(z), then x.equals(z)
        ChimeraSca chimeraSca3 = new ChimeraSca();
        
        setupSameValues();
        chimeraSca3.setId(1L);
        chimeraSca3.setProjectId("PROJECT_123");
        chimeraSca3.setAppId(100L);
        chimeraSca3.setName("Test App");
        chimeraSca3.setUuaa("TEST");
        chimeraSca3.setLow(5L);
        chimeraSca3.setMedium(3L);
        chimeraSca3.setHigh(2L);
        chimeraSca3.setCritical(1L);

        assertTrue(chimeraSca1.equals(chimeraSca2));
        assertTrue(chimeraSca2.equals(chimeraSca3));
        assertTrue(chimeraSca1.equals(chimeraSca3));
    }

    @Test
    void testEqualsContract_Consistent() {
        // Consistent: multiple invocations should return same result
        setupSameValues();

        boolean result1 = chimeraSca1.equals(chimeraSca2);
        boolean result2 = chimeraSca1.equals(chimeraSca2);
        boolean result3 = chimeraSca1.equals(chimeraSca2);

        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }

    private void setupSameValues() {
        chimeraSca1.setId(1L);
        chimeraSca1.setProjectId("PROJECT_123");
        chimeraSca1.setAppId(100L);
        chimeraSca1.setName("Test App");
        chimeraSca1.setUuaa("TEST");
        chimeraSca1.setLow(5L);
        chimeraSca1.setMedium(3L);
        chimeraSca1.setHigh(2L);
        chimeraSca1.setCritical(1L);

        chimeraSca2.setId(1L);
        chimeraSca2.setProjectId("PROJECT_123");
        chimeraSca2.setAppId(100L);
        chimeraSca2.setName("Test App");
        chimeraSca2.setUuaa("TEST");
        chimeraSca2.setLow(5L);
        chimeraSca2.setMedium(3L);
        chimeraSca2.setHigh(2L);
        chimeraSca2.setCritical(1L);
    }
}
