package ar.com.bbva.fuentus.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VerticalNucleusTest {

    private VerticalNucleus verticalNucleus1;
    private VerticalNucleus verticalNucleus2;

    @BeforeEach
    void setUp() {
        verticalNucleus1 = new VerticalNucleus();
        verticalNucleus2 = new VerticalNucleus();
    }

    @Test
    void testDefaultConstructor() {
        // Given & When
        VerticalNucleus verticalNucleus = new VerticalNucleus();

        // Then
        assertNotNull(verticalNucleus);
        assertNull(verticalNucleus.getId());
        assertNull(verticalNucleus.getVertical());
        assertNull(verticalNucleus.getNucleus());
    }

    @Test
    void testGettersAndSetters_Id() {
        // Given
        Long testId = 1L;

        // When
        verticalNucleus1.setId(testId);

        // Then
        assertEquals(testId, verticalNucleus1.getId());
    }

    @Test
    void testGettersAndSetters_Vertical() {
        // Given
        Vertical testVertical = new Vertical();
        testVertical.setId(10L);
        testVertical.setName("Test Vertical");

        // When
        verticalNucleus1.setVertical(testVertical);

        // Then
        assertNotNull(verticalNucleus1.getVertical());
        assertEquals(testVertical, verticalNucleus1.getVertical());
        assertEquals(10L, verticalNucleus1.getVertical().getId());
        assertEquals("Test Vertical", verticalNucleus1.getVertical().getName());
    }

    @Test
    void testGettersAndSetters_Nucleus() {
        // Given
        Nucleus testNucleus = new Nucleus();
        testNucleus.setId(20L);
        testNucleus.setServiceN1("Test Service");

        // When
        verticalNucleus1.setNucleus(testNucleus);

        // Then
        assertNotNull(verticalNucleus1.getNucleus());
        assertEquals(testNucleus, verticalNucleus1.getNucleus());
        assertEquals(20L, verticalNucleus1.getNucleus().getId());
        assertEquals("Test Service", verticalNucleus1.getNucleus().getServiceN1());
    }

    @Test
    void testGettersAndSetters_AllFields() {
        // Given
        Long testId = 100L;
        Vertical testVertical = new Vertical();
        testVertical.setId(5L);
        testVertical.setName("Banking");

        Nucleus testNucleus = new Nucleus();
        testNucleus.setId(15L);
        testNucleus.setServiceN1("Payment Service");

        // When
        verticalNucleus1.setId(testId);
        verticalNucleus1.setVertical(testVertical);
        verticalNucleus1.setNucleus(testNucleus);

        // Then
        assertEquals(testId, verticalNucleus1.getId());
        assertEquals(testVertical, verticalNucleus1.getVertical());
        assertEquals(testNucleus, verticalNucleus1.getNucleus());
    }

    @Test
    void testGettersAndSetters_NullValues() {
        // Test setting null values
        verticalNucleus1.setId(null);
        assertNull(verticalNucleus1.getId());

        verticalNucleus1.setVertical(null);
        assertNull(verticalNucleus1.getVertical());

        verticalNucleus1.setNucleus(null);
        assertNull(verticalNucleus1.getNucleus());
    }

    @Test
    void testMultipleInstances() {
        // Given
        Long id1 = 1L;
        Vertical vertical1 = new Vertical();
        vertical1.setId(10L);

        Long id2 = 2L;
        Vertical vertical2 = new Vertical();
        vertical2.setId(20L);

        // When
        verticalNucleus1.setId(id1);
        verticalNucleus1.setVertical(vertical1);

        verticalNucleus2.setId(id2);
        verticalNucleus2.setVertical(vertical2);

        // Then
        assertNotEquals(verticalNucleus1.getId(), verticalNucleus2.getId());
        assertNotEquals(verticalNucleus1.getVertical().getId(), verticalNucleus2.getVertical().getId());
        assertEquals(id1, verticalNucleus1.getId());
        assertEquals(id2, verticalNucleus2.getId());
    }

    @Test
    void testSettersReturnVoid() {
        // Test that setters don't return anything and work correctly
        assertDoesNotThrow(() -> {
            verticalNucleus1.setId(1L);
            verticalNucleus1.setVertical(new Vertical());
            verticalNucleus1.setNucleus(new Nucleus());
        });
    }

    @Test
    void testRelationshipIntegrity() {
        // Given
        Vertical vertical = new Vertical();
        vertical.setId(100L);
        vertical.setName("Technology Vertical");

        Nucleus nucleus = new Nucleus();
        nucleus.setId(200L);
        nucleus.setServiceN1("Core Service");

        // When
        verticalNucleus1.setVertical(vertical);
        verticalNucleus1.setNucleus(nucleus);

        // Then
        assertNotNull(verticalNucleus1.getVertical());
        assertNotNull(verticalNucleus1.getNucleus());
        assertSame(vertical, verticalNucleus1.getVertical());
        assertSame(nucleus, verticalNucleus1.getNucleus());
    }

    @Test
    void testUpdateFields() {
        // Given
        Long initialId = 1L;
        Vertical initialVertical = new Vertical();
        initialVertical.setId(10L);

        verticalNucleus1.setId(initialId);
        verticalNucleus1.setVertical(initialVertical);

        // When - Update values
        Long newId = 2L;
        Vertical newVertical = new Vertical();
        newVertical.setId(20L);

        verticalNucleus1.setId(newId);
        verticalNucleus1.setVertical(newVertical);

        // Then
        assertEquals(newId, verticalNucleus1.getId());
        assertEquals(newVertical, verticalNucleus1.getVertical());
        assertNotEquals(initialId, verticalNucleus1.getId());
        assertNotEquals(initialVertical, verticalNucleus1.getVertical());
    }
}

