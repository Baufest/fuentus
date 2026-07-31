package ar.com.bbva.fuentus.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NucleusTest {

    private Nucleus nucleus1;
    private Nucleus nucleus2;

    @BeforeEach
    void setUp() {
        nucleus1 = new Nucleus();
        nucleus2 = new Nucleus();
    }

    @Test
    void testDefaultConstructor() {
        // Given & When
        Nucleus nucleus = new Nucleus();

        // Then
        assertNotNull(nucleus);
        assertNull(nucleus.getId());
        assertNull(nucleus.getServiceN1());
        assertNull(nucleus.getOwnerIdServiceN1());
        assertNull(nucleus.getOwnerServiceN1());
        assertNull(nucleus.getServiceN2());
        assertNull(nucleus.getServiceN2description());
        assertNull(nucleus.getOwnerIdServiceN2());
        assertNull(nucleus.getOwnerServiceN2());
        assertNull(nucleus.getArea());
        assertNull(nucleus.getOrgN1());
        assertNull(nucleus.getOwnerIdOrgN1());
        assertNull(nucleus.getOwnerOrgN1());
        assertNull(nucleus.getOrgN2fabrica());
        assertNull(nucleus.getOwnerIdOrgN2());
        assertNull(nucleus.getOwnerOrg2ftl());
        assertNull(nucleus.getCfs());
        assertNull(nucleus.getSaas());
        assertNull(nucleus.getDisponibilidadCnegocio());
        assertNull(nucleus.getConfidencialidadIcc());
        assertNull(nucleus.getIntegridad());
        assertNull(nucleus.getAutenticidad());
        assertNull(nucleus.getRelevanteResolucion());
        assertNull(nucleus.getCategoria());
        assertNull(nucleus.getUuaa());
        assertNull(nucleus.getEstado());
        assertNull(nucleus.getDescGlobalRels());
        assertNull(nucleus.getAscGlobalRels());
        assertNull(nucleus.getDescRegRels());
        assertNull(nucleus.getAscRegRels());
        assertNull(nucleus.getRelType());
    }

    @Test
    void testGettersAndSetters() {
        // Test ID
        Long testId = 1L;
        nucleus1.setId(testId);
        assertEquals(testId, nucleus1.getId());

        // Test ServiceN1
        String testServiceN1 = "Service N1";
        nucleus1.setServiceN1(testServiceN1);
        assertEquals(testServiceN1, nucleus1.getServiceN1());

        // Test OwnerIdServiceN1
        String testOwnerIdServiceN1 = "Owner ID Service N1";
        nucleus1.setOwnerIdServiceN1(testOwnerIdServiceN1);
        assertEquals(testOwnerIdServiceN1, nucleus1.getOwnerIdServiceN1());

        // Test OwnerServiceN1
        String testOwnerServiceN1 = "Owner Service N1";
        nucleus1.setOwnerServiceN1(testOwnerServiceN1);
        assertEquals(testOwnerServiceN1, nucleus1.getOwnerServiceN1());

        // Test ServiceN2
        String testServiceN2 = "Service N2";
        nucleus1.setServiceN2(testServiceN2);
        assertEquals(testServiceN2, nucleus1.getServiceN2());

        // Test ServiceN2description
        String testServiceN2description = "Service N2 Description";
        nucleus1.setServiceN2description(testServiceN2description);
        assertEquals(testServiceN2description, nucleus1.getServiceN2description());

        // Test OwnerIdServiceN2
        String testOwnerIdServiceN2 = "Owner ID Service N2";
        nucleus1.setOwnerIdServiceN2(testOwnerIdServiceN2);
        assertEquals(testOwnerIdServiceN2, nucleus1.getOwnerIdServiceN2());

        // Test OwnerServiceN2
        String testOwnerServiceN2 = "Owner Service N2";
        nucleus1.setOwnerServiceN2(testOwnerServiceN2);
        assertEquals(testOwnerServiceN2, nucleus1.getOwnerServiceN2());

        // Test Area
        String testArea = "Test Area";
        nucleus1.setArea(testArea);
        assertEquals(testArea, nucleus1.getArea());

        // Test OrgN1
        String testOrgN1 = "Org N1";
        nucleus1.setOrgN1(testOrgN1);
        assertEquals(testOrgN1, nucleus1.getOrgN1());

        // Test OwnerIdOrgN1
        String testOwnerIdOrgN1 = "Owner ID Org N1";
        nucleus1.setOwnerIdOrgN1(testOwnerIdOrgN1);
        assertEquals(testOwnerIdOrgN1, nucleus1.getOwnerIdOrgN1());

        // Test OwnerOrgN1
        String testOwnerOrgN1 = "Owner Org N1";
        nucleus1.setOwnerOrgN1(testOwnerOrgN1);
        assertEquals(testOwnerOrgN1, nucleus1.getOwnerOrgN1());

        // Test OrgN2fabrica
        String testOrgN2fabrica = "Org N2 Fabrica";
        nucleus1.setOrgN2fabrica(testOrgN2fabrica);
        assertEquals(testOrgN2fabrica, nucleus1.getOrgN2fabrica());

        // Test OwnerIdOrgN2
        String testOwnerIdOrgN2 = "Owner ID Org N2";
        nucleus1.setOwnerIdOrgN2(testOwnerIdOrgN2);
        assertEquals(testOwnerIdOrgN2, nucleus1.getOwnerIdOrgN2());

        // Test OwnerOrg2ftl
        String testOwnerOrg2ftl = "Owner Org 2 FTL";
        nucleus1.setOwnerOrg2ftl(testOwnerOrg2ftl);
        assertEquals(testOwnerOrg2ftl, nucleus1.getOwnerOrg2ftl());

        // Test CFS
        String testCfs = "CFS";
        nucleus1.setCfs(testCfs);
        assertEquals(testCfs, nucleus1.getCfs());

        // Test SaaS
        String testSaas = "SaaS";
        nucleus1.setSaas(testSaas);
        assertEquals(testSaas, nucleus1.getSaas());

        // Test DisponibilidadCnegocio
        String testDisponibilidadCnegocio = "Disponibilidad C Negocio";
        nucleus1.setDisponibilidadCnegocio(testDisponibilidadCnegocio);
        assertEquals(testDisponibilidadCnegocio, nucleus1.getDisponibilidadCnegocio());

        // Test ConfidencialidadIcc
        String testConfidencialidadIcc = "Confidencialidad ICC";
        nucleus1.setConfidencialidadIcc(testConfidencialidadIcc);
        assertEquals(testConfidencialidadIcc, nucleus1.getConfidencialidadIcc());

        // Test Integridad
        String testIntegridad = "Integridad";
        nucleus1.setIntegridad(testIntegridad);
        assertEquals(testIntegridad, nucleus1.getIntegridad());

        // Test Autenticidad
        String testAutenticidad = "Autenticidad";
        nucleus1.setAutenticidad(testAutenticidad);
        assertEquals(testAutenticidad, nucleus1.getAutenticidad());

        // Test RelevanteResolucion
        String testRelevanteResolucion = "Relevante Resolucion";
        nucleus1.setRelevanteResolucion(testRelevanteResolucion);
        assertEquals(testRelevanteResolucion, nucleus1.getRelevanteResolucion());

        // Test Categoria
        String testCategoria = "Categoria";
        nucleus1.setCategoria(testCategoria);
        assertEquals(testCategoria, nucleus1.getCategoria());

        // Test UUAA
        String testUuaa = "UUAA";
        nucleus1.setUuaa(testUuaa);
        assertEquals(testUuaa, nucleus1.getUuaa());

        // Test Estado
        String testEstado = "Estado";
        nucleus1.setEstado(testEstado);
        assertEquals(testEstado, nucleus1.getEstado());

        // Test DescGlobalRels
        Integer testDescGlobalRels = 10;
        nucleus1.setDescGlobalRels(testDescGlobalRels);
        assertEquals(testDescGlobalRels, nucleus1.getDescGlobalRels());

        // Test AscGlobalRels
        String testAscGlobalRels = "ASC Global Rels";
        nucleus1.setAscGlobalRels(testAscGlobalRels);
        assertEquals(testAscGlobalRels, nucleus1.getAscGlobalRels());

        // Test DescRegRels
        String testDescRegRels = "DESC Reg Rels";
        nucleus1.setDescRegRels(testDescRegRels);
        assertEquals(testDescRegRels, nucleus1.getDescRegRels());

        // Test AscRegRels
        String testAscRegRels = "ASC Reg Rels";
        nucleus1.setAscRegRels(testAscRegRels);
        assertEquals(testAscRegRels, nucleus1.getAscRegRels());

        // Test RelType
        String testRelType = "Rel Type";
        nucleus1.setRelType(testRelType);
        assertEquals(testRelType, nucleus1.getRelType());
    }

    @Test
    void testGettersAndSetters_NullValues() {
        // Test setting null values
        nucleus1.setId(null);
        assertNull(nucleus1.getId());

        nucleus1.setServiceN1(null);
        assertNull(nucleus1.getServiceN1());

        nucleus1.setOwnerIdServiceN1(null);
        assertNull(nucleus1.getOwnerIdServiceN1());

        nucleus1.setOwnerServiceN1(null);
        assertNull(nucleus1.getOwnerServiceN1());

        nucleus1.setServiceN2(null);
        assertNull(nucleus1.getServiceN2());

        nucleus1.setServiceN2description(null);
        assertNull(nucleus1.getServiceN2description());

        nucleus1.setOwnerIdServiceN2(null);
        assertNull(nucleus1.getOwnerIdServiceN2());

        nucleus1.setOwnerServiceN2(null);
        assertNull(nucleus1.getOwnerServiceN2());

        nucleus1.setArea(null);
        assertNull(nucleus1.getArea());

        nucleus1.setOrgN1(null);
        assertNull(nucleus1.getOrgN1());

        nucleus1.setOwnerIdOrgN1(null);
        assertNull(nucleus1.getOwnerIdOrgN1());

        nucleus1.setOwnerOrgN1(null);
        assertNull(nucleus1.getOwnerOrgN1());

        nucleus1.setOrgN2fabrica(null);
        assertNull(nucleus1.getOrgN2fabrica());

        nucleus1.setOwnerIdOrgN2(null);
        assertNull(nucleus1.getOwnerIdOrgN2());

        nucleus1.setOwnerOrg2ftl(null);
        assertNull(nucleus1.getOwnerOrg2ftl());

        nucleus1.setCfs(null);
        assertNull(nucleus1.getCfs());

        nucleus1.setSaas(null);
        assertNull(nucleus1.getSaas());

        nucleus1.setDisponibilidadCnegocio(null);
        assertNull(nucleus1.getDisponibilidadCnegocio());

        nucleus1.setConfidencialidadIcc(null);
        assertNull(nucleus1.getConfidencialidadIcc());

        nucleus1.setIntegridad(null);
        assertNull(nucleus1.getIntegridad());

        nucleus1.setAutenticidad(null);
        assertNull(nucleus1.getAutenticidad());

        nucleus1.setRelevanteResolucion(null);
        assertNull(nucleus1.getRelevanteResolucion());

        nucleus1.setCategoria(null);
        assertNull(nucleus1.getCategoria());

        nucleus1.setUuaa(null);
        assertNull(nucleus1.getUuaa());

        nucleus1.setEstado(null);
        assertNull(nucleus1.getEstado());

        nucleus1.setDescGlobalRels(null);
        assertNull(nucleus1.getDescGlobalRels());

        nucleus1.setAscGlobalRels(null);
        assertNull(nucleus1.getAscGlobalRels());

        nucleus1.setDescRegRels(null);
        assertNull(nucleus1.getDescRegRels());

        nucleus1.setAscRegRels(null);
        assertNull(nucleus1.getAscRegRels());

        nucleus1.setRelType(null);
        assertNull(nucleus1.getRelType());
    }

    @Test
    void testEquals_SameObject() {
        // When & Then
        assertEquals(nucleus1, nucleus1);
        assertTrue(nucleus1.equals(nucleus1));
    }

    @Test
    void testEquals_NullObject() {
        // When & Then
        assertNotEquals(nucleus1, null);
        assertFalse(nucleus1.equals(null));
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        String differentObject = "not a Nucleus";

        // When & Then
        assertNotEquals(nucleus1, differentObject);
        assertFalse(nucleus1.equals(differentObject));
    }

    @Test
    void testHashCode_SameValues() {
        // Given
        setupSameValues();

        // When & Then
        assertEquals(nucleus1.hashCode(), nucleus2.hashCode());
    }

    @Test
    void testHashCode_NullValues() {
        // Given - all values are null by default

        // When & Then
        assertDoesNotThrow(() -> nucleus1.hashCode());
        assertEquals(nucleus1.hashCode(), nucleus2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        setupSomeValues();

        // When
        String result = nucleus1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("Nucleus"));
    }

    @Test
    void testToString_NullValues() {
        // Given - all values are null by default

        // When
        String result = nucleus1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("Nucleus"));
    }

    @Test
    void testEqualsContract_Reflexive() {
        // Reflexive: x.equals(x) should return true
        assertTrue(nucleus1.equals(nucleus1));
    }

    @Test
    void testEqualsContract_Symmetric() {
        // Symmetric: x.equals(y) should return same as y.equals(x)
        setupSameValues();

        boolean result1 = nucleus1.equals(nucleus2);
        boolean result2 = nucleus2.equals(nucleus1);
        assertEquals(result1, result2);
    }

    @Test
    void testEqualsContract_Consistent() {
        // Consistent: multiple invocations should return same result
        setupSameValues();

        boolean result1 = nucleus1.equals(nucleus2);
        boolean result2 = nucleus1.equals(nucleus2);
        boolean result3 = nucleus1.equals(nucleus2);

        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }

    private void setupSameValues() {
        // Setup nucleus1
        nucleus1.setId(1L);
        nucleus1.setServiceN1("Service N1");
        nucleus1.setOwnerIdServiceN1("Owner ID Service N1");
        nucleus1.setUuaa("UUAA");
        nucleus1.setEstado("Estado");

        // Setup nucleus2 with same values
        nucleus2.setId(1L);
        nucleus2.setServiceN1("Service N1");
        nucleus2.setOwnerIdServiceN1("Owner ID Service N1");
        nucleus2.setUuaa("UUAA");
        nucleus2.setEstado("Estado");
    }

    private void setupSomeValues() {
        nucleus1.setId(1L);
        nucleus1.setServiceN1("Service N1");
        nucleus1.setUuaa("UUAA");
        nucleus1.setEstado("Estado");
    }
}
