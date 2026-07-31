package ar.com.bbva.fuentus.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppServerTest {

    private AppServer appServer1;
    private AppServer appServer2;

    @BeforeEach
    void setUp() {
        appServer1 = new AppServer();
        appServer2 = new AppServer();
    }

    @Test
    void testDefaultConstructor() {
        // Given & When
        AppServer appServer = new AppServer();

        // Then
        assertNotNull(appServer);
        assertNull(appServer.getId());
        assertNull(appServer.getAppId());
        assertNull(appServer.getServerId());
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        Long testId = 1L;
        Long testAppId = 100L;
        Long testServerId = 200L;

        // When
        AppServer appServer = new AppServer(testId, testAppId, testServerId);

        // Then
        assertNotNull(appServer);
        assertEquals(testId, appServer.getId());
        assertEquals(testAppId, appServer.getAppId());
        assertEquals(testServerId, appServer.getServerId());
    }

    @Test
    void testNoArgsConstructor() {
        // Given & When
        AppServer appServer = new AppServer();

        // Then
        assertNotNull(appServer);
        assertNull(appServer.getId());
        assertNull(appServer.getAppId());
        assertNull(appServer.getServerId());
    }

    @Test
    void testGettersAndSetters() {
        // Test ID getter and setter
        Long testId = 1L;
        appServer1.setId(testId);
        assertEquals(testId, appServer1.getId());

        // Test AppId getter and setter
        Long testAppId = 100L;
        appServer1.setAppId(testAppId);
        assertEquals(testAppId, appServer1.getAppId());

        // Test ServerId getter and setter
        Long testServerId = 200L;
        appServer1.setServerId(testServerId);
        assertEquals(testServerId, appServer1.getServerId());

        // Test with null values
        appServer1.setId(null);
        assertNull(appServer1.getId());

        appServer1.setAppId(null);
        assertNull(appServer1.getAppId());

        appServer1.setServerId(null);
        assertNull(appServer1.getServerId());
    }

    @Test
    void testEquals_SameObject() {
        // When & Then
        assertEquals(appServer1, appServer1);
        assertTrue(appServer1.equals(appServer1));
    }

    @Test
    void testEquals_NullObject() {
        // When & Then
        assertNotEquals(appServer1, null);
        assertFalse(appServer1.equals(null));
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        String differentObject = "not an AppServer";

        // When & Then
        assertNotEquals(appServer1, differentObject);
        assertFalse(appServer1.equals(differentObject));
    }

    @Test
    void testEquals_SameValues() {
        // Given
        appServer1.setId(1L);
        appServer1.setAppId(100L);
        appServer1.setServerId(200L);

        appServer2.setId(1L);
        appServer2.setAppId(100L);
        appServer2.setServerId(200L);

        // When & Then
        assertEquals(appServer1, appServer2);
        assertTrue(appServer1.equals(appServer2));
        assertTrue(appServer2.equals(appServer1));
    }

    @Test
    void testEquals_DifferentId() {
        // Given
        appServer1.setId(1L);
        appServer1.setAppId(100L);
        appServer1.setServerId(200L);

        appServer2.setId(2L);
        appServer2.setAppId(100L);
        appServer2.setServerId(200L);

        // When & Then
        assertNotEquals(appServer1, appServer2);
        assertFalse(appServer1.equals(appServer2));
        assertFalse(appServer2.equals(appServer1));
    }

    @Test
    void testEquals_DifferentAppId() {
        // Given
        appServer1.setId(1L);
        appServer1.setAppId(100L);
        appServer1.setServerId(200L);

        appServer2.setId(1L);
        appServer2.setAppId(101L);
        appServer2.setServerId(200L);

        // When & Then
        assertNotEquals(appServer1, appServer2);
        assertFalse(appServer1.equals(appServer2));
        assertFalse(appServer2.equals(appServer1));
    }

    @Test
    void testEquals_DifferentServerId() {
        // Given
        appServer1.setId(1L);
        appServer1.setAppId(100L);
        appServer1.setServerId(200L);

        appServer2.setId(1L);
        appServer2.setAppId(100L);
        appServer2.setServerId(201L);

        // When & Then
        assertNotEquals(appServer1, appServer2);
        assertFalse(appServer1.equals(appServer2));
        assertFalse(appServer2.equals(appServer1));
    }

    @Test
    void testEquals_NullValues() {
        // Given
        appServer1.setId(null);
        appServer1.setAppId(null);
        appServer1.setServerId(null);

        appServer2.setId(null);
        appServer2.setAppId(null);
        appServer2.setServerId(null);

        // When & Then
        assertEquals(appServer1, appServer2);
        assertTrue(appServer1.equals(appServer2));
        assertTrue(appServer2.equals(appServer1));
    }

    @Test
    void testHashCode_SameValues() {
        // Given
        appServer1.setId(1L);
        appServer1.setAppId(100L);
        appServer1.setServerId(200L);

        appServer2.setId(1L);
        appServer2.setAppId(100L);
        appServer2.setServerId(200L);

        // When & Then
        assertEquals(appServer1.hashCode(), appServer2.hashCode());
    }

    @Test
    void testHashCode_DifferentValues() {
        // Given
        appServer1.setId(1L);
        appServer1.setAppId(100L);
        appServer1.setServerId(200L);

        appServer2.setId(2L);
        appServer2.setAppId(100L);
        appServer2.setServerId(200L);

        // When & Then
        assertNotEquals(appServer1.hashCode(), appServer2.hashCode());
    }

    @Test
    void testHashCode_NullValues() {
        // Given
        appServer1.setId(null);
        appServer1.setAppId(null);
        appServer1.setServerId(null);

        // When & Then
        assertDoesNotThrow(() -> appServer1.hashCode());
    }

    @Test
    void testToString() {
        // Given
        appServer1.setId(1L);
        appServer1.setAppId(100L);
        appServer1.setServerId(200L);

        // When
        String result = appServer1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("AppServer"));
    }

    @Test
    void testToString_NullValues() {
        // Given
        appServer1.setId(null);
        appServer1.setAppId(null);
        appServer1.setServerId(null);

        // When
        String result = appServer1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("AppServer"));
    }

    @Test
    void testEqualsContract_Reflexive() {
        // Reflexive: x.equals(x) should return true
        assertTrue(appServer1.equals(appServer1));
    }

    @Test
    void testEqualsContract_Symmetric() {
        // Symmetric: x.equals(y) should return same as y.equals(x)
        appServer1.setId(1L);
        appServer2.setId(1L);

        boolean result1 = appServer1.equals(appServer2);
        boolean result2 = appServer2.equals(appServer1);
        assertEquals(result1, result2);
    }

    @Test
    void testEqualsContract_Transitive() {
        // Transitive: if x.equals(y) and y.equals(z), then x.equals(z)
        AppServer appServer3 = new AppServer();
        
        appServer1.setId(1L);
        appServer1.setAppId(100L);
        appServer1.setServerId(200L);
        
        appServer2.setId(1L);
        appServer2.setAppId(100L);
        appServer2.setServerId(200L);
        
        appServer3.setId(1L);
        appServer3.setAppId(100L);
        appServer3.setServerId(200L);

        assertTrue(appServer1.equals(appServer2));
        assertTrue(appServer2.equals(appServer3));
        assertTrue(appServer1.equals(appServer3));
    }

    @Test
    void testEqualsContract_Consistent() {
        // Consistent: multiple invocations should return same result
        appServer1.setId(1L);
        appServer2.setId(1L);

        boolean result1 = appServer1.equals(appServer2);
        boolean result2 = appServer1.equals(appServer2);
        boolean result3 = appServer1.equals(appServer2);

        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }
}
