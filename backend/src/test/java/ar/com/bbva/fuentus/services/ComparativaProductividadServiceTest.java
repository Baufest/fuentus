package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.ComparativaProductividadDTO;
import ar.com.bbva.fuentus.dto.GraficoNivelDTO;
import ar.com.bbva.fuentus.dto.ItemGraficoDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.Productividad;
import ar.com.bbva.fuentus.entities.Velocidad;
import ar.com.bbva.fuentus.entities.Vertical;
import ar.com.bbva.fuentus.entities.VerticalNucleus;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import ar.com.bbva.fuentus.repositories.ProductividadRepository;
import ar.com.bbva.fuentus.repositories.VelocidadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComparativaProductividadServiceTest {

    @Mock
    private NucleusRepository nucleusRepository;

    @Mock
    private ProductividadRepository productividadRepository;

    @Mock
    private VelocidadRepository velocidadRepository;

    @InjectMocks
    private ComparativaProductividadService comparativaProductividadService;

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
        // Setup Verticals
        vertical1 = new Vertical();
        vertical1.setId(1L);
        vertical1.setName("BANCA DIGITAL");

        vertical2 = new Vertical();
        vertical2.setId(2L);
        vertical2.setName("BANCA EMPRESAS");

        // Setup Nucleus 1
        nucleus1 = new Nucleus();
        nucleus1.setId(1L);
        nucleus1.setServiceN1("ARQ");
        nucleus1.setServiceN2("ARQUITECTURA ALPHA");
        nucleus1.setOrgN2fabrica("FABRICA A");
        
        verticalNucleus1 = new VerticalNucleus();
        verticalNucleus1.setId(1L);
        verticalNucleus1.setNucleus(nucleus1);
        verticalNucleus1.setVertical(vertical1);
        nucleus1.setVerticalNucleus(new ArrayList<>(Collections.singletonList(verticalNucleus1)));

        // Setup Nucleus 2
        nucleus2 = new Nucleus();
        nucleus2.setId(2L);
        nucleus2.setServiceN1("DEV");
        nucleus2.setServiceN2("DESARROLLO BETA");
        nucleus2.setOrgN2fabrica("FABRICA A");
        
        verticalNucleus2 = new VerticalNucleus();
        verticalNucleus2.setId(2L);
        verticalNucleus2.setNucleus(nucleus2);
        verticalNucleus2.setVertical(vertical1);
        nucleus2.setVerticalNucleus(new ArrayList<>(Collections.singletonList(verticalNucleus2)));

        // Setup Nucleus 3
        nucleus3 = new Nucleus();
        nucleus3.setId(3L);
        nucleus3.setServiceN1("DEV");
        nucleus3.setServiceN2("DESARROLLO GAMMA");
        nucleus3.setOrgN2fabrica("FABRICA B");
        
        verticalNucleus3 = new VerticalNucleus();
        verticalNucleus3.setId(3L);
        verticalNucleus3.setNucleus(nucleus3);
        verticalNucleus3.setVertical(vertical2);
        nucleus3.setVerticalNucleus(new ArrayList<>(Collections.singletonList(verticalNucleus3)));

        // Setup Productividad
        productividad1 = new Productividad();
        productividad1.setId(1L);
        productividad1.setNucleus(nucleus1);
        productividad1.setFeatures(11);
        productividad1.setFtesDirectos(10.58);
        productividad1.setFtesIndirectos(4.90);
        productividad1.setFecha(LocalDate.of(2025, 12, 30));

        productividad2 = new Productividad();
        productividad2.setId(2L);
        productividad2.setNucleus(nucleus2);
        productividad2.setFeatures(8);
        productividad2.setFtesDirectos(7.25);
        productividad2.setFtesIndirectos(3.15);
        productividad2.setFecha(LocalDate.of(2025, 12, 30));

        productividad3 = new Productividad();
        productividad3.setId(3L);
        productividad3.setNucleus(nucleus3);
        productividad3.setFeatures(15);
        productividad3.setFtesDirectos(12.00);
        productividad3.setFtesIndirectos(5.00);
        productividad3.setFecha(LocalDate.of(2025, 12, 30));

        // Setup Velocidad
        velocidad1 = new Velocidad();
        velocidad1.setId(1L);
        velocidad1.setNucleus(nucleus1);
        velocidad1.setLt(203);
        velocidad1.setCt(173);

        velocidad2 = new Velocidad();
        velocidad2.setId(2L);
        velocidad2.setNucleus(nucleus2);
        velocidad2.setLt(180);
        velocidad2.setCt(150);

        velocidad3 = new Velocidad();
        velocidad3.setId(3L);
        velocidad3.setNucleus(nucleus3);
        velocidad3.setLt(220);
        velocidad3.setCt(190);
    }

    @Test
    void getComparativa_ShouldReturnEmptyGraficos_WhenNoNucleusFound() {
        // Given
        when(nucleusRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        ComparativaProductividadDTO result = comparativaProductividadService.getComparativa(null, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals("NINGUNO", result.getNivelFiltrado());
        assertTrue(result.getGraficos().isEmpty());
        verify(nucleusRepository, times(1)).findAll();
    }

    @Test
    void getComparativa_ShouldReturnAllLevels_WhenNoFiltersApplied() {
        // Given
        List<Nucleus> allNucleus = Arrays.asList(nucleus1, nucleus2, nucleus3);
        List<Productividad> allProductividad = Arrays.asList(productividad1, productividad2, productividad3);
        List<Velocidad> allVelocidad = Arrays.asList(velocidad1, velocidad2, velocidad3);

        when(nucleusRepository.findAll()).thenReturn(allNucleus);
        when(productividadRepository.findAll()).thenReturn(allProductividad);
        when(velocidadRepository.findAll()).thenReturn(allVelocidad);

        // When
        ComparativaProductividadDTO result = comparativaProductividadService.getComparativa(null, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals("NINGUNO", result.getNivelFiltrado());
        assertFalse(result.getGraficos().isEmpty());
        
        // Should have 4 levels: VERTICAL, FABRICA, SN1, SN2
        assertTrue(result.getGraficos().size() <= 4);
        
        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getComparativa_ShouldFilterByVertical_WhenVerticalProvided() {
        // Given
        List<Nucleus> allNucleus = Arrays.asList(nucleus1, nucleus2, nucleus3);
        List<Productividad> allProductividad = Arrays.asList(productividad1, productividad2, productividad3);
        List<Velocidad> allVelocidad = Arrays.asList(velocidad1, velocidad2, velocidad3);

        when(nucleusRepository.findAll()).thenReturn(allNucleus);
        when(productividadRepository.findAll()).thenReturn(allProductividad);
        when(velocidadRepository.findAll()).thenReturn(allVelocidad);

        // When
        ComparativaProductividadDTO result = comparativaProductividadService.getComparativa("BANCA DIGITAL", null, null, null);

        // Then
        assertNotNull(result);
        assertEquals("VERTICAL", result.getNivelFiltrado());
        assertFalse(result.getGraficos().isEmpty());
        
        // Should filter to only nucleus1 and nucleus2 (vertical1)
        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getComparativa_ShouldFilterByFabrica_WhenFabricaProvided() {
        // Given
        List<Nucleus> allNucleus = Arrays.asList(nucleus1, nucleus2, nucleus3);
        List<Productividad> allProductividad = Arrays.asList(productividad1, productividad2, productividad3);
        List<Velocidad> allVelocidad = Arrays.asList(velocidad1, velocidad2, velocidad3);

        when(nucleusRepository.findAll()).thenReturn(allNucleus);
        when(productividadRepository.findAll()).thenReturn(allProductividad);
        when(velocidadRepository.findAll()).thenReturn(allVelocidad);

        // When
        ComparativaProductividadDTO result = comparativaProductividadService.getComparativa(null, "FABRICA A", null, null);

        // Then
        assertNotNull(result);
        assertEquals("FABRICA", result.getNivelFiltrado());
        assertFalse(result.getGraficos().isEmpty());
        
        // Should show SN1 and SN2 levels
        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getComparativa_ShouldFilterBySN1_WhenSN1Provided() {
        // Given
        List<Nucleus> allNucleus = Arrays.asList(nucleus1, nucleus2, nucleus3);
        List<Productividad> allProductividad = Arrays.asList(productividad1, productividad2, productividad3);
        List<Velocidad> allVelocidad = Arrays.asList(velocidad1, velocidad2, velocidad3);

        when(nucleusRepository.findAll()).thenReturn(allNucleus);
        when(productividadRepository.findAll()).thenReturn(allProductividad);
        when(velocidadRepository.findAll()).thenReturn(allVelocidad);

        // When
        ComparativaProductividadDTO result = comparativaProductividadService.getComparativa(null, null, "DEV", null);

        // Then
        assertNotNull(result);
        assertEquals("SN1", result.getNivelFiltrado());
        assertFalse(result.getGraficos().isEmpty());
        
        // Should show only SN2 level
        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getComparativa_ShouldCalculateCorrectMetrics_WhenDataIsComplete() {
        // Given
        List<Nucleus> allNucleus = Collections.singletonList(nucleus1);
        List<Productividad> allProductividad = Collections.singletonList(productividad1);
        List<Velocidad> allVelocidad = Collections.singletonList(velocidad1);

        when(nucleusRepository.findAll()).thenReturn(allNucleus);
        when(productividadRepository.findAll()).thenReturn(allProductividad);
        when(velocidadRepository.findAll()).thenReturn(allVelocidad);

        // When
        ComparativaProductividadDTO result = comparativaProductividadService.getComparativa(null, null, null, null);

        // Then
        assertNotNull(result);
        assertFalse(result.getGraficos().isEmpty());
        
        // Verify that graficos contain items with proper metrics
        boolean hasItemsWithMetrics = result.getGraficos().stream()
            .anyMatch(grafico -> grafico.getItems().stream()
                .anyMatch(item -> item.getProductividad() != null 
                    || item.getPromedioLT() != null 
                    || item.getPromedioCT() != null));
        
        assertTrue(hasItemsWithMetrics);
    }

    @Test
    void getComparativa_ShouldHandleNullProductividad_Gracefully() {
        // Given
        List<Nucleus> allNucleus = Collections.singletonList(nucleus1);
        List<Productividad> allProductividad = Collections.emptyList();
        List<Velocidad> allVelocidad = Collections.singletonList(velocidad1);

        when(nucleusRepository.findAll()).thenReturn(allNucleus);
        when(productividadRepository.findAll()).thenReturn(allProductividad);
        when(velocidadRepository.findAll()).thenReturn(allVelocidad);

        // When
        ComparativaProductividadDTO result = comparativaProductividadService.getComparativa(null, null, null, null);

        // Then
        assertNotNull(result);
        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getComparativa_ShouldHandleNullVelocidad_Gracefully() {
        // Given
        List<Nucleus> allNucleus = Collections.singletonList(nucleus1);
        List<Productividad> allProductividad = Collections.singletonList(productividad1);
        List<Velocidad> allVelocidad = Collections.emptyList();

        when(nucleusRepository.findAll()).thenReturn(allNucleus);
        when(productividadRepository.findAll()).thenReturn(allProductividad);
        when(velocidadRepository.findAll()).thenReturn(allVelocidad);

        // When
        ComparativaProductividadDTO result = comparativaProductividadService.getComparativa(null, null, null, null);

        // Then
        assertNotNull(result);
        verify(nucleusRepository, times(1)).findAll();
        verify(productividadRepository, times(1)).findAll();
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getComparativa_ShouldAggregateMultipleNucleus_PerLevel() {
        // Given
        List<Nucleus> allNucleus = Arrays.asList(nucleus1, nucleus2);
        List<Productividad> allProductividad = Arrays.asList(productividad1, productividad2);
        List<Velocidad> allVelocidad = Arrays.asList(velocidad1, velocidad2);

        when(nucleusRepository.findAll()).thenReturn(allNucleus);
        when(productividadRepository.findAll()).thenReturn(allProductividad);
        when(velocidadRepository.findAll()).thenReturn(allVelocidad);

        // When
        ComparativaProductividadDTO result = comparativaProductividadService.getComparativa(null, "FABRICA A", null, null);

        // Then
        assertNotNull(result);
        assertEquals("FABRICA", result.getNivelFiltrado());
        assertFalse(result.getGraficos().isEmpty());
        
        // Both nucleus1 and nucleus2 belong to FABRICA A and have different SN1 values
        boolean hasSN1Grafico = result.getGraficos().stream()
            .anyMatch(g -> "SN1".equals(g.getNivelTipo()));
        
        assertTrue(hasSN1Grafico);
    }

    @Test
    void getComparativa_ShouldHandleEmptyFilters_AsNullFilters() {
        // Given
        List<Nucleus> allNucleus = Arrays.asList(nucleus1, nucleus2, nucleus3);
        List<Productividad> allProductividad = Arrays.asList(productividad1, productividad2, productividad3);
        List<Velocidad> allVelocidad = Arrays.asList(velocidad1, velocidad2, velocidad3);

        when(nucleusRepository.findAll()).thenReturn(allNucleus);
        when(productividadRepository.findAll()).thenReturn(allProductividad);
        when(velocidadRepository.findAll()).thenReturn(allVelocidad);

        // When
        ComparativaProductividadDTO result = comparativaProductividadService.getComparativa("", "", "", "");

        // Then
        assertNotNull(result);
        assertEquals("NINGUNO", result.getNivelFiltrado());
        verify(nucleusRepository, times(1)).findAll();
    }

    @Test
    void getComparativa_ShouldGroupByVertical_WhenMultipleVerticalsExist() {
        // Given
        List<Nucleus> allNucleus = Arrays.asList(nucleus1, nucleus2, nucleus3);
        List<Productividad> allProductividad = Arrays.asList(productividad1, productividad2, productividad3);
        List<Velocidad> allVelocidad = Arrays.asList(velocidad1, velocidad2, velocidad3);

        when(nucleusRepository.findAll()).thenReturn(allNucleus);
        when(productividadRepository.findAll()).thenReturn(allProductividad);
        when(velocidadRepository.findAll()).thenReturn(allVelocidad);

        // When
        ComparativaProductividadDTO result = comparativaProductividadService.getComparativa(null, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals("NINGUNO", result.getNivelFiltrado());
        
        // Should have vertical level in graficos
        boolean hasVerticalGrafico = result.getGraficos().stream()
            .anyMatch(g -> "VERTICAL".equals(g.getNivelTipo()));
        
        assertTrue(hasVerticalGrafico);
    }

    @Test
    void getComparativa_ShouldCalculateProductividad_FromFTEs() {
        // Given
        List<Nucleus> allNucleus = Collections.singletonList(nucleus1);
        List<Productividad> allProductividad = Collections.singletonList(productividad1);
        List<Velocidad> allVelocidad = Collections.singletonList(velocidad1);

        when(nucleusRepository.findAll()).thenReturn(allNucleus);
        when(productividadRepository.findAll()).thenReturn(allProductividad);
        when(velocidadRepository.findAll()).thenReturn(allVelocidad);

        // When
        ComparativaProductividadDTO result = comparativaProductividadService.getComparativa(null, null, null, null);

        // Then
        assertNotNull(result);
        assertFalse(result.getGraficos().isEmpty());
        
        // Verify productividad calculation: features / (ftesDirectos + ftesIndirectos)
        // 11 / (10.58 + 4.90) = 11 / 15.48
        double expectedProductividad = 11.0 / 15.48;
        
        boolean hasCorrectProductividad = result.getGraficos().stream()
            .flatMap(g -> g.getItems().stream())
            .anyMatch(item -> item.getProductividad() != null 
                && Math.abs(item.getProductividad() - expectedProductividad) < 0.01);
        
        assertTrue(hasCorrectProductividad);
    }

    @Test
    void getComparativa_ShouldCalculateAverageLTandCT_FromVelocidad() {
        // Given
        List<Nucleus> allNucleus = Collections.singletonList(nucleus1);
        List<Productividad> allProductividad = Collections.singletonList(productividad1);
        
        // Multiple velocidad entries for same nucleus
        Velocidad velocidad1a = new Velocidad();
        velocidad1a.setId(4L);
        velocidad1a.setNucleus(nucleus1);
        velocidad1a.setLt(200);
        velocidad1a.setCt(170);
        
        List<Velocidad> allVelocidad = Arrays.asList(velocidad1, velocidad1a);

        when(nucleusRepository.findAll()).thenReturn(allNucleus);
        when(productividadRepository.findAll()).thenReturn(allProductividad);
        when(velocidadRepository.findAll()).thenReturn(allVelocidad);

        // When
        ComparativaProductividadDTO result = comparativaProductividadService.getComparativa(null, null, null, null);

        // Then
        assertNotNull(result);
        assertFalse(result.getGraficos().isEmpty());
        
        // Verify average calculation: (203 + 200) / 2 = 201.5 for LT, (173 + 170) / 2 = 171.5 for CT
        boolean hasCorrectAverages = result.getGraficos().stream()
            .flatMap(g -> g.getItems().stream())
            .anyMatch(item -> item.getPromedioLT() != null 
                && Math.abs(item.getPromedioLT() - 201.5) < 0.1
                && item.getPromedioCT() != null
                && Math.abs(item.getPromedioCT() - 171.5) < 0.1);
        
        assertTrue(hasCorrectAverages);
    }

    @Test
    void getComparativa_ShouldNotIncludeItemsWithoutMetrics() {
        // Given
        Nucleus nucleusWithoutData = new Nucleus();
        nucleusWithoutData.setId(4L);
        nucleusWithoutData.setServiceN1("TEST");
        nucleusWithoutData.setServiceN2("TEST SERVICE");
        nucleusWithoutData.setOrgN2fabrica("FABRICA C");
        
        VerticalNucleus verticalNucleusTest = new VerticalNucleus();
        verticalNucleusTest.setId(4L);
        verticalNucleusTest.setNucleus(nucleusWithoutData);
        verticalNucleusTest.setVertical(vertical1);
        nucleusWithoutData.setVerticalNucleus(new ArrayList<>(Collections.singletonList(verticalNucleusTest)));
        
        List<Nucleus> allNucleus = Arrays.asList(nucleus1, nucleusWithoutData);
        List<Productividad> allProductividad = Collections.singletonList(productividad1);
        List<Velocidad> allVelocidad = Collections.singletonList(velocidad1);

        when(nucleusRepository.findAll()).thenReturn(allNucleus);
        when(productividadRepository.findAll()).thenReturn(allProductividad);
        when(velocidadRepository.findAll()).thenReturn(allVelocidad);

        // When
        ComparativaProductividadDTO result = comparativaProductividadService.getComparativa(null, null, null, null);

        // Then
        assertNotNull(result);
        
        // Items without any metrics should be filtered out
        result.getGraficos().forEach(grafico -> 
            grafico.getItems().forEach(item -> 
                assertTrue(item.getProductividad() != null 
                    || item.getPromedioLT() != null 
                    || item.getPromedioCT() != null,
                    "Item should have at least one metric")));
    }

    @Test
    void getComparativa_ShouldSortItemsByProductividad_Descending() {
        // Given
        List<Nucleus> allNucleus = Arrays.asList(nucleus1, nucleus2, nucleus3);
        List<Productividad> allProductividad = Arrays.asList(productividad1, productividad2, productividad3);
        List<Velocidad> allVelocidad = Arrays.asList(velocidad1, velocidad2, velocidad3);

        when(nucleusRepository.findAll()).thenReturn(allNucleus);
        when(productividadRepository.findAll()).thenReturn(allProductividad);
        when(velocidadRepository.findAll()).thenReturn(allVelocidad);

        // When
        ComparativaProductividadDTO result = comparativaProductividadService.getComparativa(null, "FABRICA A", null, null);

        // Then
        assertNotNull(result);
        
        // Check that items in each grafico are sorted by productividad descending
        result.getGraficos().forEach(grafico -> {
            List<ItemGraficoDTO> items = grafico.getItems();
            if (items.size() > 1) {
                for (int i = 0; i < items.size() - 1; i++) {
                    Double current = items.get(i).getProductividad();
                    Double next = items.get(i + 1).getProductividad();
                    
                    if (current != null && next != null) {
                        assertTrue(current >= next, 
                            "Items should be sorted by productividad descending");
                    }
                }
            }
        });
    }

    @Test
    void getComparativa_ShouldHandleNullNucleusInProductividad() {
        // Given
        Productividad productividadWithNullNucleus = new Productividad();
        productividadWithNullNucleus.setId(10L);
        productividadWithNullNucleus.setNucleus(null);
        productividadWithNullNucleus.setFeatures(5);
        
        List<Nucleus> allNucleus = Collections.singletonList(nucleus1);
        List<Productividad> allProductividad = Arrays.asList(productividad1, productividadWithNullNucleus);
        List<Velocidad> allVelocidad = Collections.singletonList(velocidad1);

        when(nucleusRepository.findAll()).thenReturn(allNucleus);
        when(productividadRepository.findAll()).thenReturn(allProductividad);
        when(velocidadRepository.findAll()).thenReturn(allVelocidad);

        // When & Then - should not throw exception
        assertDoesNotThrow(() -> {
            ComparativaProductividadDTO result = comparativaProductividadService.getComparativa(null, null, null, null);
            assertNotNull(result);
        });
    }

    @Test
    void getComparativa_ShouldHandleNullNucleusInVelocidad() {
        // Given
        Velocidad velocidadWithNullNucleus = new Velocidad();
        velocidadWithNullNucleus.setId(10L);
        velocidadWithNullNucleus.setNucleus(null);
        velocidadWithNullNucleus.setLt(100);
        
        List<Nucleus> allNucleus = Collections.singletonList(nucleus1);
        List<Productividad> allProductividad = Collections.singletonList(productividad1);
        List<Velocidad> allVelocidad = Arrays.asList(velocidad1, velocidadWithNullNucleus);

        when(nucleusRepository.findAll()).thenReturn(allNucleus);
        when(productividadRepository.findAll()).thenReturn(allProductividad);
        when(velocidadRepository.findAll()).thenReturn(allVelocidad);

        // When & Then - should not throw exception
        assertDoesNotThrow(() -> {
            ComparativaProductividadDTO result = comparativaProductividadService.getComparativa(null, null, null, null);
            assertNotNull(result);
        });
    }
}
