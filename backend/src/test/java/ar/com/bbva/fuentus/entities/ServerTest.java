package ar.com.bbva.fuentus.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    private Server server1;
    private Server server2;

    @BeforeEach
    void setUp() {
        server1 = new Server();
        server2 = new Server();
    }

    @Test
    void testDefaultConstructor() {
        // Given & When
        Server server = new Server();

        // Then
        assertNotNull(server);
        assertNull(server.getId());
        assertNull(server.getName());
    }

    @Test
    void testGettersAndSetters() {
        // Test ID getter and setter
        Long testId = 1L;
        server1.setId(testId);
        assertEquals(testId, server1.getId());

        // Test Name getter and setter
        String testName = "test-server";
        server1.setName(testName);
        assertEquals(testName, server1.getName());

        // Test with null values
        server1.setId(null);
        assertNull(server1.getId());

        server1.setName(null);
        assertNull(server1.getName());
    }

    @Test
    void testEquals_SameObject() {
        // When & Then
        assertEquals(server1, server1);
        assertTrue(server1.equals(server1));
    }

    @Test
    void testEquals_NullObject() {
        // When & Then
        assertNotEquals(server1, null);
        assertFalse(server1.equals(null));
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        String differentObject = "not a server";

        // When & Then
        assertNotEquals(server1, differentObject);
        assertFalse(server1.equals(differentObject));
    }

    @Test
    void testEquals_SameId() {
        // Given
        Long testId = 1L;
        server1.setId(testId);
        server2.setId(testId);

        // When & Then
        assertEquals(server1, server2);
        assertTrue(server1.equals(server2));
        assertTrue(server2.equals(server1));
    }

    @Test
    void testEquals_DifferentId() {
        // Given
        server1.setId(1L);
        server2.setId(2L);

        // When & Then
        assertNotEquals(server1, server2);
        assertFalse(server1.equals(server2));
        assertFalse(server2.equals(server1));
    }

    @Test
    void testEquals_OneIdNull() {
        // Given
        server1.setId(1L);
        server2.setId(null);

        // When & Then
        assertNotEquals(server1, server2);
        assertFalse(server1.equals(server2));
        assertFalse(server2.equals(server1));
    }

    @Test
    void testEquals_BothIdsNull() {
        // Given
        server1.setId(null);
        server2.setId(null);

        // When & Then
        assertEquals(server1, server2);
        assertTrue(server1.equals(server2));
        assertTrue(server2.equals(server1));
    }

    @Test
    void testHashCode_SameId() {
        // Given
        Long testId = 1L;
        server1.setId(testId);
        server2.setId(testId);

        // When & Then
        assertEquals(server1.hashCode(), server2.hashCode());
    }

    @Test
    void testHashCode_DifferentId() {
        // Given
        server1.setId(1L);
        server2.setId(2L);

        // When & Then
        assertNotEquals(server1.hashCode(), server2.hashCode());
    }

    @Test
    void testHashCode_NullId() {
        // Given
        server1.setId(null);

        // When & Then
        assertDoesNotThrow(() -> server1.hashCode());
        assertNotNull(server1.hashCode());
    }

    @Test
    void testToString() {
        // Given
        server1.setId(1L);
        server1.setName("test-server");

        // When
        String result = server1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("Server"));
        assertTrue(result.contains("id=1"));
    }

    @Test
    void testToString_NullValues() {
        // Given
        server1.setId(null);
        server1.setName(null);

        // When
        String result = server1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("Server"));
        assertTrue(result.contains("id=null"));
    }

    @Test
    void testEqualsContract_Reflexive() {
        // Reflexive: x.equals(x) should return true
        assertTrue(server1.equals(server1));
    }

    @Test
    void testEqualsContract_Symmetric() {
        // Symmetric: x.equals(y) should return same as y.equals(x)
        server1.setId(1L);
        server2.setId(1L);

        boolean result1 = server1.equals(server2);
        boolean result2 = server2.equals(server1);
        assertEquals(result1, result2);
    }

    @Test
    void testEqualsContract_Transitive() {
        // Transitive: if x.equals(y) and y.equals(z), then x.equals(z)
        Server server3 = new Server();
        server1.setId(1L);
        server2.setId(1L);
        server3.setId(1L);

        assertTrue(server1.equals(server2));
        assertTrue(server2.equals(server3));
        assertTrue(server1.equals(server3));
    }

    @Test
    void testEqualsContract_Consistent() {
        // Consistent: multiple invocations should return same result
        server1.setId(1L);
        server2.setId(1L);

        boolean result1 = server1.equals(server2);
        boolean result2 = server1.equals(server2);
        boolean result3 = server1.equals(server2);

        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }
}
