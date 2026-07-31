package ar.com.bbva.fuentus.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VerticalTest {

    private Vertical vertical1;
    private Vertical vertical2;

    @BeforeEach
    void setUp() {
        vertical1 = new Vertical();
        vertical2 = new Vertical();
    }

    @Test
    void testDefaultConstructor() {
        // Given & When
        Vertical vertical = new Vertical();

        // Then
        assertNotNull(vertical);
        assertNull(vertical.getId());
        assertNull(vertical.getName());
        assertNull(vertical.getVerticalNucleusList());
    }

    @Test
    void testGettersAndSetters_BasicFields() {
        // Test ID
        Long testId = 1L;
        vertical1.setId(testId);
        assertEquals(testId, vertical1.getId());

        // Test Name
        String testName = "Banking Vertical";
        vertical1.setName(testName);
        assertEquals(testName, vertical1.getName());
    }

    @Test
    void testGettersAndSetters_VerticalNucleusList() {
        // Given
        List<VerticalNucleus> testList = new ArrayList<>();
        VerticalNucleus verticalNucleus1 = new VerticalNucleus();
        VerticalNucleus verticalNucleus2 = new VerticalNucleus();
        testList.add(verticalNucleus1);
        testList.add(verticalNucleus2);

        // When
        vertical1.setVerticalNucleusList(testList);

        // Then
        assertNotNull(vertical1.getVerticalNucleusList());
        assertEquals(2, vertical1.getVerticalNucleusList().size());
        assertEquals(testList, vertical1.getVerticalNucleusList());
    }

    @Test
    void testGettersAndSetters_EmptyList() {
        // Given
        List<VerticalNucleus> emptyList = new ArrayList<>();

        // When
        vertical1.setVerticalNucleusList(emptyList);

        // Then
        assertNotNull(vertical1.getVerticalNucleusList());
        assertTrue(vertical1.getVerticalNucleusList().isEmpty());
        assertEquals(0, vertical1.getVerticalNucleusList().size());
    }

    @Test
    void testGettersAndSetters_NullValues() {
        // Test setting null values
        vertical1.setId(null);
        assertNull(vertical1.getId());

        vertical1.setName(null);
        assertNull(vertical1.getName());

        vertical1.setVerticalNucleusList(null);
        assertNull(vertical1.getVerticalNucleusList());
    }

    @Test
    void testGettersAndSetters_AllFields() {
        // Given
        Long testId = 100L;
        String testName = "Technology";
        String testDescription = "Technology and innovation vertical";
        List<VerticalNucleus> testList = new ArrayList<>();

        // When
        vertical1.setId(testId);
        vertical1.setName(testName);
        vertical1.setVerticalNucleusList(testList);

        // Then
        assertEquals(testId, vertical1.getId());
        assertEquals(testName, vertical1.getName());
        assertEquals(testList, vertical1.getVerticalNucleusList());
    }

    @Test
    void testMultipleInstances() {
        // Given
        Long id1 = 1L;
        String name1 = "Vertical One";
        Long id2 = 2L;
        String name2 = "Vertical Two";

        // When
        vertical1.setId(id1);
        vertical1.setName(name1);
        vertical2.setId(id2);
        vertical2.setName(name2);

        // Then
        assertNotEquals(vertical1.getId(), vertical2.getId());
        assertNotEquals(vertical1.getName(), vertical2.getName());
        assertEquals(id1, vertical1.getId());
        assertEquals(name1, vertical1.getName());
        assertEquals(id2, vertical2.getId());
        assertEquals(name2, vertical2.getName());
    }

    @Test
    void testSettersReturnVoid() {
        // Test that setters don't return anything and work correctly
        assertDoesNotThrow(() -> {
            vertical1.setId(1L);
            vertical1.setName("Test");
            vertical1.setVerticalNucleusList(new ArrayList<>());
        });
    }
}