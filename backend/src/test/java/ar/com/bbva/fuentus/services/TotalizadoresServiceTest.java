package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.RankingDTO;
import ar.com.bbva.fuentus.dto.RankingItemDTO;
import ar.com.bbva.fuentus.dto.TopLevelMetricsDTO;
import ar.com.bbva.fuentus.dto.TotalizadoresDTO;
import ar.com.bbva.fuentus.entities.*;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import ar.com.bbva.fuentus.repositories.ProductividadRepository;
import ar.com.bbva.fuentus.repositories.VelocidadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TotalizadoresServiceTest {

    @Mock
    private NucleusRepository nucleusRepository;

    @Mock
    private ProductividadRepository productividadRepository;

    @Mock
    private VelocidadRepository velocidadRepository;

    @InjectMocks
    private TotalizadoresService totalizadoresService;

    private Nucleus nucleus1;
    private Nucleus nucleus2;
    private Nucleus nucleus3;
    private Productividad productividad1;
    private Productividad productividad2;
    private Productividad productividad3;
    private Velocidad velocidad1;
    private Velocidad velocidad2;
    private Velocidad velocidad3;
    private Vertical vertical1;
    private Vertical vertical2;
    private VerticalNucleus verticalNucleus1;
    private VerticalNucleus verticalNucleus2;
    private VerticalNucleus verticalNucleus3;

    @BeforeEach
    void setUp() {
        // Configurar verticales
        vertical1 = new Vertical();
        vertical1.setId(1L);
        vertical1.setName("Digital");

        vertical2 = new Vertical();
        vertical2.setId(2L);
        vertical2.setName("Core Banking");

        // Configurar nucleus 1
        nucleus1 = new Nucleus();
        nucleus1.setId(1L);
        nucleus1.setServiceN1("ServicioN1-A");
        nucleus1.setServiceN2("ServicioN2-Alpha");
        nucleus1.setOrgN2fabrica("Fabrica1");

        verticalNucleus1 = new VerticalNucleus();
        verticalNucleus1.setId(1L);
        verticalNucleus1.setVertical(vertical1);
        verticalNucleus1.setNucleus(nucleus1);
        nucleus1.setVerticalNucleus(Arrays.asList(verticalNucleus1));

        // Configurar nucleus 2
        nucleus2 = new Nucleus();
        nucleus2.setId(2L);
        nucleus2.setServiceN1("ServicioN1-A");
        nucleus2.setServiceN2("ServicioN2-Beta");
        nucleus2.setOrgN2fabrica("Fabrica1");

        verticalNucleus2 = new VerticalNucleus();
        verticalNucleus2.setId(2L);
        verticalNucleus2.setVertical(vertical1);
        verticalNucleus2.setNucleus(nucleus2);
        nucleus2.setVerticalNucleus(Arrays.asList(verticalNucleus2));

        // Configurar nucleus 3
        nucleus3 = new Nucleus();
        nucleus3.setId(3L);
        nucleus3.setServiceN1("ServicioN1-B");
        nucleus3.setServiceN2("ServicioN2-Gamma");
        nucleus3.setOrgN2fabrica("Fabrica2");

        verticalNucleus3 = new VerticalNucleus();
        verticalNucleus3.setId(3L);
        verticalNucleus3.setVertical(vertical2);
        verticalNucleus3.setNucleus(nucleus3);
        nucleus3.setVerticalNucleus(Arrays.asList(verticalNucleus3));

        // Configurar productividad
        productividad1 = new Productividad();
        productividad1.setId(1L);
        productividad1.setNucleus(nucleus1);
        productividad1.setFeatures(50);
        productividad1.setFtesDirectos(10.0);
        productividad1.setFtesIndirectos(5.0);

        productividad2 = new Productividad();
        productividad2.setId(2L);
        productividad2.setNucleus(nucleus2);
        productividad2.setFeatures(30);
        productividad2.setFtesDirectos(8.0);
        productividad2.setFtesIndirectos(4.0);

        productividad3 = new Productividad();
        productividad3.setId(3L);
        productividad3.setNucleus(nucleus3);
        productividad3.setFeatures(40);
        productividad3.setFtesDirectos(5.0);
        productividad3.setFtesIndirectos(3.0);

        // Configurar velocidad
        velocidad1 = new Velocidad();
        velocidad1.setId(1L);
        velocidad1.setNucleus(nucleus1);
        velocidad1.setLt(200);
        velocidad1.setCt(150);

        velocidad2 = new Velocidad();
        velocidad2.setId(2L);
        velocidad2.setNucleus(nucleus2);
        velocidad2.setLt(180);
        velocidad2.setCt(120);

        velocidad3 = new Velocidad();
        velocidad3.setId(3L);
        velocidad3.setNucleus(nucleus3);
        velocidad3.setLt(220);
        velocidad3.setCt(170);
    }

    @Test
    void getMejoresNiveles_ShouldReturnEmpty_WhenNoDataFound() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        TotalizadoresDTO result = totalizadoresService.getMejoresNiveles(null, null, null, null);

        // Then
        assertNotNull(result);
        assertNotNull(result.getMejoresNiveles());
        assertTrue(result.getMejoresNiveles().isEmpty());
        verify(nucleusRepository, times(1)).findAll();
    }

    @Test
    void getMejoresNiveles_ShouldReturnEmpty_WhenNoMatchingFilters() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2, nucleus3));

        // When
        TotalizadoresDTO result = totalizadoresService.getMejoresNiveles("NonExistentVertical", null, null, null);

        // Then
        assertNotNull(result);
        assertNotNull(result.getMejoresNiveles());
        assertTrue(result.getMejoresNiveles().isEmpty());
        verify(nucleusRepository, times(1)).findAll();
    }

    @Test
    void getMejoresNiveles_ShouldReturnAllLevels_WhenNoFiltersApplied() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2, nucleus3));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2, productividad3));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2, velocidad3));

        // When
        TotalizadoresDTO result = totalizadoresService.getMejoresNiveles(null, null, null, null);

        // Then
        assertNotNull(result);
        assertNotNull(result.getMejoresNiveles());
        assertFalse(result.getMejoresNiveles().isEmpty());
        
        // Debería incluir niveles: VERTICAL, FABRICA, SN1, SN2
        // Para cada nivel, 3 métricas: PRODUCTIVIDAD, LT, CT
        assertTrue(result.getMejoresNiveles().size() > 0);
        
        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getMejoresNiveles_ShouldReturnOnlyVerticalLevel_WhenVerticalFilterApplied() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2, nucleus3));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2, productividad3));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2, velocidad3));

        // When
        TotalizadoresDTO result = totalizadoresService.getMejoresNiveles("Digital", null, null, null);

        // Then
        assertNotNull(result);
        assertNotNull(result.getMejoresNiveles());
        assertFalse(result.getMejoresNiveles().isEmpty());
        
        // Cuando filtrado por vertical, debería mostrar FABRICA, SN1 y SN2
        // Verificar que los niveles no incluyen VERTICAL
        boolean hasVerticalLevel = result.getMejoresNiveles().stream()
                .anyMatch(nivel -> "VERTICAL".equals(nivel.getNivelTipo()));
        assertFalse(hasVerticalLevel);

        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getMejoresNiveles_ShouldFilterByFabrica_WhenFabricaProvided() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2, nucleus3));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2, productividad3));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2, velocidad3));

        // When
        TotalizadoresDTO result = totalizadoresService.getMejoresNiveles(null, "Fabrica1", null, null);

        // Then
        assertNotNull(result);
        assertNotNull(result.getMejoresNiveles());
        assertFalse(result.getMejoresNiveles().isEmpty());
        
        // Cuando filtrado por fábrica, debería mostrar SN1 y SN2
        boolean hasFabricaLevel = result.getMejoresNiveles().stream()
                .anyMatch(nivel -> "FABRICA".equals(nivel.getNivelTipo()));
        assertFalse(hasFabricaLevel);

        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getMejoresNiveles_ShouldFilterBySN1_WhenSN1Provided() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2, nucleus3));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2, productividad3));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2, velocidad3));

        // When
        TotalizadoresDTO result = totalizadoresService.getMejoresNiveles(null, null, "ServicioN1-A", null);

        // Then
        assertNotNull(result);
        assertNotNull(result.getMejoresNiveles());
        assertFalse(result.getMejoresNiveles().isEmpty());
        
        // Cuando filtrado por SN1, debería mostrar solo SN2
        long sn2Count = result.getMejoresNiveles().stream()
                .filter(nivel -> "SN2".equals(nivel.getNivelTipo()))
                .count();
        assertTrue(sn2Count > 0);

        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getMejoresNiveles_ShouldHandleNullProductividad_Gracefully() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2));
        when(productividadRepository.findAll()).thenReturn(Collections.emptyList());
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2));

        // When
        TotalizadoresDTO result = totalizadoresService.getMejoresNiveles(null, null, null, null);

        // Then
        assertNotNull(result);
        assertNotNull(result.getMejoresNiveles());
        
        // Debería devolver métricas de LT y CT, pero no PRODUCTIVIDAD
        long productividadCount = result.getMejoresNiveles().stream()
                .filter(nivel -> "PRODUCTIVIDAD".equals(nivel.getMetricaTipo()))
                .count();
        assertEquals(0, productividadCount);

        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getMejoresNiveles_ShouldHandleNullVelocidad_Gracefully() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2));
        when(velocidadRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        TotalizadoresDTO result = totalizadoresService.getMejoresNiveles(null, null, null, null);

        // Then
        assertNotNull(result);
        assertNotNull(result.getMejoresNiveles());
        
        // Debería devolver métricas de PRODUCTIVIDAD, pero no LT ni CT
        long velocidadCount = result.getMejoresNiveles().stream()
                .filter(nivel -> "LT".equals(nivel.getMetricaTipo()) || "CT".equals(nivel.getMetricaTipo()))
                .count();
        assertEquals(0, velocidadCount);

        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getRankingTop3_ShouldReturnEmpty_WhenNoDataFound() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        RankingDTO result = totalizadoresService.getRankingTop3("SN2", "PRODUCTIVIDAD", null, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals("SN2", result.getNivelTipo());
        assertEquals("PRODUCTIVIDAD", result.getMetricaTipo());
        assertNotNull(result.getRanking());
        assertTrue(result.getRanking().isEmpty());
        verify(nucleusRepository, times(1)).findAll();
    }

    @Test
    void getRankingTop3_ShouldReturnEmpty_WhenNoMatchingFilters() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2, nucleus3));

        // When
        RankingDTO result = totalizadoresService.getRankingTop3("SN2", "PRODUCTIVIDAD", "NonExistentVertical", null, null, null);

        // Then
        assertNotNull(result);
        assertEquals("SN2", result.getNivelTipo());
        assertEquals("PRODUCTIVIDAD", result.getMetricaTipo());
        assertNotNull(result.getRanking());
        assertTrue(result.getRanking().isEmpty());
        verify(nucleusRepository, times(1)).findAll();
    }

    @Test
    void getRankingTop3_ShouldReturnTop3ByProductividad_WhenDataExists() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2, nucleus3));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2, productividad3));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2, velocidad3));

        // When
        RankingDTO result = totalizadoresService.getRankingTop3("SN2", "PRODUCTIVIDAD", null, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals("SN2", result.getNivelTipo());
        assertEquals("PRODUCTIVIDAD", result.getMetricaTipo());
        assertNotNull(result.getRanking());
        assertFalse(result.getRanking().isEmpty());
        
        // Verificar que el ranking está ordenado (mayor a menor para productividad)
        for (int i = 0; i < result.getRanking().size() - 1; i++) {
            assertTrue(result.getRanking().get(i).getValor() >= result.getRanking().get(i + 1).getValor());
        }
        
        // Verificar que las posiciones están asignadas correctamente
        for (int i = 0; i < result.getRanking().size(); i++) {
            assertEquals(i + 1, result.getRanking().get(i).getPosicion());
        }
        
        // Verificar que no retorna más de 3 elementos
        assertTrue(result.getRanking().size() <= 3);

        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getRankingTop3_ShouldReturnTop3ByLT_WhenDataExists() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2, nucleus3));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2, productividad3));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2, velocidad3));

        // When
        RankingDTO result = totalizadoresService.getRankingTop3("SN2", "LT", null, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals("SN2", result.getNivelTipo());
        assertEquals("LT", result.getMetricaTipo());
        assertNotNull(result.getRanking());
        assertFalse(result.getRanking().isEmpty());
        
        // Verificar que el ranking está ordenado (menor a mayor para LT)
        for (int i = 0; i < result.getRanking().size() - 1; i++) {
            assertTrue(result.getRanking().get(i).getValor() <= result.getRanking().get(i + 1).getValor());
        }
        
        // Verificar que no retorna más de 3 elementos
        assertTrue(result.getRanking().size() <= 3);

        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getRankingTop3_ShouldReturnTop3ByCT_WhenDataExists() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2, nucleus3));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2, productividad3));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2, velocidad3));

        // When
        RankingDTO result = totalizadoresService.getRankingTop3("SN2", "CT", null, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals("SN2", result.getNivelTipo());
        assertEquals("CT", result.getMetricaTipo());
        assertNotNull(result.getRanking());
        assertFalse(result.getRanking().isEmpty());
        
        // Verificar que el ranking está ordenado (menor a mayor para CT)
        for (int i = 0; i < result.getRanking().size() - 1; i++) {
            assertTrue(result.getRanking().get(i).getValor() <= result.getRanking().get(i + 1).getValor());
        }
        
        // Verificar que no retorna más de 3 elementos
        assertTrue(result.getRanking().size() <= 3);

        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getRankingTop3_ShouldHandleVerticalLevel_Correctly() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2, nucleus3));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2, productividad3));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2, velocidad3));

        // When
        RankingDTO result = totalizadoresService.getRankingTop3("VERTICAL", "PRODUCTIVIDAD", null, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals("VERTICAL", result.getNivelTipo());
        assertEquals("PRODUCTIVIDAD", result.getMetricaTipo());
        assertNotNull(result.getRanking());
        assertFalse(result.getRanking().isEmpty());
        
        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getRankingTop3_ShouldHandleFabricaLevel_Correctly() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2, nucleus3));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2, productividad3));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2, velocidad3));

        // When
        RankingDTO result = totalizadoresService.getRankingTop3("FABRICA", "PRODUCTIVIDAD", null, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals("FABRICA", result.getNivelTipo());
        assertEquals("PRODUCTIVIDAD", result.getMetricaTipo());
        assertNotNull(result.getRanking());
        assertFalse(result.getRanking().isEmpty());
        
        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getRankingTop3_ShouldHandleSN1Level_Correctly() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2, nucleus3));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2, productividad3));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2, velocidad3));

        // When
        RankingDTO result = totalizadoresService.getRankingTop3("SN1", "PRODUCTIVIDAD", null, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals("SN1", result.getNivelTipo());
        assertEquals("PRODUCTIVIDAD", result.getMetricaTipo());
        assertNotNull(result.getRanking());
        assertFalse(result.getRanking().isEmpty());
        
        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getRankingTop3_ShouldReturnEmpty_WhenUnknownNivelType() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2, nucleus3));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2, productividad3));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2, velocidad3));

        // When
        RankingDTO result = totalizadoresService.getRankingTop3("UNKNOWN_LEVEL", "PRODUCTIVIDAD", null, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals("UNKNOWN_LEVEL", result.getNivelTipo());
        assertEquals("PRODUCTIVIDAD", result.getMetricaTipo());
        assertNotNull(result.getRanking());
        assertTrue(result.getRanking().isEmpty());

        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getRankingTop3_ShouldFilterByVertical_WhenVerticalProvided() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2, nucleus3));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2, productividad3));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2, velocidad3));

        // When
        RankingDTO result = totalizadoresService.getRankingTop3("SN2", "PRODUCTIVIDAD", "Digital", null, null, null);

        // Then
        assertNotNull(result);
        assertEquals("SN2", result.getNivelTipo());
        assertEquals("PRODUCTIVIDAD", result.getMetricaTipo());
        assertNotNull(result.getRanking());
        
        // Solo debería incluir nucleus1 y nucleus2 que pertenecen a Digital
        assertTrue(result.getRanking().size() <= 2);

        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getRankingTop3_ShouldFilterByFabrica_WhenFabricaProvided() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2, nucleus3));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2, productividad3));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2, velocidad3));

        // When
        RankingDTO result = totalizadoresService.getRankingTop3("SN2", "PRODUCTIVIDAD", null, "Fabrica1", null, null);

        // Then
        assertNotNull(result);
        assertEquals("SN2", result.getNivelTipo());
        assertEquals("PRODUCTIVIDAD", result.getMetricaTipo());
        assertNotNull(result.getRanking());
        
        // Solo debería incluir nucleus1 y nucleus2 que pertenecen a Fabrica1
        assertTrue(result.getRanking().size() <= 2);

        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getRankingTop3_ShouldFilterBySN1_WhenSN1Provided() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2, nucleus3));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2, productividad3));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2, velocidad3));

        // When
        RankingDTO result = totalizadoresService.getRankingTop3("SN2", "PRODUCTIVIDAD", null, null, "ServicioN1-A", null);

        // Then
        assertNotNull(result);
        assertEquals("SN2", result.getNivelTipo());
        assertEquals("PRODUCTIVIDAD", result.getMetricaTipo());
        assertNotNull(result.getRanking());
        
        // Solo debería incluir nucleus1 y nucleus2 que pertenecen a ServicioN1-A
        assertTrue(result.getRanking().size() <= 2);

        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getRankingTop3_ShouldFilterBySN2_WhenSN2Provided() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2, nucleus3));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2, productividad3));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2, velocidad3));

        // When
        RankingDTO result = totalizadoresService.getRankingTop3("SN1", "PRODUCTIVIDAD", null, null, null, "ServicioN2-Alpha");

        // Then
        assertNotNull(result);
        assertEquals("SN1", result.getNivelTipo());
        assertEquals("PRODUCTIVIDAD", result.getMetricaTipo());
        assertNotNull(result.getRanking());
        
        // Solo debería incluir nucleus1 que pertenece a ServicioN2-Alpha
        assertTrue(result.getRanking().size() <= 1);

        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getRankingTop3_ShouldHandleNullNucleus_InProductividad() {
        // Given
        Productividad prodWithNullNucleus = new Productividad();
        prodWithNullNucleus.setId(4L);
        prodWithNullNucleus.setNucleus(null);
        prodWithNullNucleus.setFeatures(100);

        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2, prodWithNullNucleus));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2));

        // When
        RankingDTO result = totalizadoresService.getRankingTop3("SN2", "PRODUCTIVIDAD", null, null, null, null);

        // Then
        assertNotNull(result);
        assertNotNull(result.getRanking());
        // No debería fallar, solo ignorar el registro con nucleus null

        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getRankingTop3_ShouldHandleNullNucleus_InVelocidad() {
        // Given
        Velocidad velWithNullNucleus = new Velocidad();
        velWithNullNucleus.setId(4L);
        velWithNullNucleus.setNucleus(null);
        velWithNullNucleus.setLt(100);
        velWithNullNucleus.setCt(80);

        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2, velWithNullNucleus));

        // When
        RankingDTO result = totalizadoresService.getRankingTop3("SN2", "LT", null, null, null, null);

        // Then
        assertNotNull(result);
        assertNotNull(result.getRanking());
        // No debería fallar, solo ignorar el registro con nucleus null

        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getRankingTop3_ShouldHandleNullValues_InMetrics() {
        // Given
        Productividad prodWithNulls = new Productividad();
        prodWithNulls.setId(4L);
        prodWithNulls.setNucleus(nucleus1);
        prodWithNulls.setFeatures(null);
        prodWithNulls.setFtesDirectos(null);
        prodWithNulls.setFtesIndirectos(null);

        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(prodWithNulls, productividad2));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2));

        // When
        RankingDTO result = totalizadoresService.getRankingTop3("SN2", "PRODUCTIVIDAD", null, null, null, null);

        // Then
        assertNotNull(result);
        assertNotNull(result.getRanking());
        // Debería manejar valores null sin fallar

        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getRankingTop3_ShouldReturnLessThan3_WhenOnlyTwoItemsAvailable() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Arrays.asList(nucleus1, nucleus2));
        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad1, productividad2));
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad1, velocidad2));

        // When
        RankingDTO result = totalizadoresService.getRankingTop3("SN2", "PRODUCTIVIDAD", null, null, null, null);

        // Then
        assertNotNull(result);
        assertNotNull(result.getRanking());
        assertEquals(2, result.getRanking().size());
        
        // Verificar posiciones
        assertEquals(1, result.getRanking().get(0).getPosicion());
        assertEquals(2, result.getRanking().get(1).getPosicion());

        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }
}
