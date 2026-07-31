package ar.com.bbva.fuentus.processors;

import ar.com.bbva.fuentus.dto.ChimeraSastImportDTO;
import ar.com.bbva.fuentus.entities.ChimeraSast;
import ar.com.bbva.fuentus.repositories.ChimeraSastRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChimeraSastProcessorTest {

    @Mock
    private ChimeraSastRepository chimeraSastRepository;

    @InjectMocks
    private ChimeraSastProcessor chimeraSastProcessor;

    private ChimeraSastImportDTO validDTO;
    private ChimeraSast existingSast;

    @BeforeEach
    void setUp() {
        // Setup valid DTO
        validDTO = new ChimeraSastImportDTO();
        validDTO.setProjectId("PROJ-123");
        validDTO.setName("Test Application");
        validDTO.setRepoUrl("https://bitbucket.org/test/repo");
        validDTO.setUuaa("1234");
        validDTO.setCountryDetName("AR");
        validDTO.setApplication("TestApp");
        validDTO.setLastScan(LocalDateTime.of(2025, 11, 26, 10, 30));
        validDTO.setChimeraUrl("https://chimera.test/project");
        validDTO.setBranch("main");
        validDTO.setAnalyzer("SonarQube");
        validDTO.setArq("Microservices");
        validDTO.setLanguage("Java");
        validDTO.setStockFlow("stock");
        validDTO.setAssumed1(5);
        validDTO.setAssumed2(3);
        validDTO.setHigh(10);
        validDTO.setCritical(2);
        validDTO.setMedium(15);
        validDTO.setLow(20);
        validDTO.setToReview(8);
        validDTO.setLines(5000);

        // Setup existing entity
        existingSast = new ChimeraSast();
        existingSast.setProjectId("PROJ-123");
        existingSast.setName("Test Application");
        existingSast.setHigh(5);
        existingSast.setCritical(1);
    }

    @Test
    void process_ShouldReturnFalse_WhenDTOIsNotChimeraSastImportDTO() {
        // Given
        Object invalidDTO = new Object();

        // When
        boolean result = chimeraSastProcessor.process(invalidDTO);

        // Then
        assertFalse(result);
        verify(chimeraSastRepository, never()).findByProjectIdAndName(anyString(), anyString());
    }

    @Test
    void process_ShouldReturnFalse_WhenProjectIdIsNull() {
        // Given
        validDTO.setProjectId(null);

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(chimeraSastRepository, never()).saveAndFlush(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenProjectIdIsEmpty() {
        // Given
        validDTO.setProjectId("");

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(chimeraSastRepository, never()).saveAndFlush(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenProjectIdIsBlank() {
        // Given
        validDTO.setProjectId("   ");

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(chimeraSastRepository, never()).saveAndFlush(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenNameIsNull() {
        // Given
        validDTO.setName(null);

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(chimeraSastRepository, never()).saveAndFlush(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenNameIsEmpty() {
        // Given
        validDTO.setName("");

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(chimeraSastRepository, never()).saveAndFlush(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenNameIsBlank() {
        // Given
        validDTO.setName("   ");

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(chimeraSastRepository, never()).saveAndFlush(any());
    }

    @Test
    void process_ShouldCreateNewEntity_WhenNotExists() {
        // Given
        when(chimeraSastRepository.findByProjectIdAndName("PROJ-123", "Test Application"))
            .thenReturn(Optional.empty());
        when(chimeraSastRepository.saveAndFlush(any(ChimeraSast.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(chimeraSastRepository).findByProjectIdAndName("PROJ-123", "Test Application");
        verify(chimeraSastRepository).saveAndFlush(any(ChimeraSast.class));
    }

    @Test
    void process_ShouldUpdateExistingEntity_WhenExists() {
        // Given
        when(chimeraSastRepository.findByProjectIdAndName("PROJ-123", "Test Application"))
            .thenReturn(Optional.of(existingSast));
        when(chimeraSastRepository.saveAndFlush(any(ChimeraSast.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(chimeraSastRepository).findByProjectIdAndName("PROJ-123", "Test Application");
        verify(chimeraSastRepository).saveAndFlush(existingSast);
        
        // Verify updated values
        assertEquals("PROJ-123", existingSast.getProjectId());
        assertEquals("Test Application", existingSast.getName());
        assertEquals(10, existingSast.getHigh());
        assertEquals(2, existingSast.getCritical());
    }

    @Test
    void process_ShouldMapAllFields_WhenValid() {
        // Given
        when(chimeraSastRepository.findByProjectIdAndName(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(chimeraSastRepository.saveAndFlush(any(ChimeraSast.class)))
            .thenAnswer(invocation -> {
                ChimeraSast saved = invocation.getArgument(0);
                
                // Verify all fields are mapped
                assertEquals("PROJ-123", saved.getProjectId());
                assertEquals("Test Application", saved.getName());
                assertEquals("https://bitbucket.org/test/repo", saved.getRepoUrl());
                assertEquals("1234", saved.getUuaa());
                assertEquals("AR", saved.getCountryDetName());
                assertEquals("TestApp", saved.getApplication());
                assertEquals(LocalDateTime.of(2025, 11, 26, 10, 30), saved.getLastScan());
                assertEquals("https://chimera.test/project", saved.getChimeraUrl());
                assertEquals("main", saved.getBranch());
                assertEquals("SonarQube", saved.getAnalyzer());
                assertEquals("Microservices", saved.getArq());
                assertEquals("Java", saved.getLanguage());
                assertEquals(ChimeraSast.StockFlow.stock, saved.getStockFlow());
                assertEquals(5, saved.getAssumed1());
                assertEquals(3, saved.getAssumed2());
                assertEquals(10, saved.getHigh());
                assertEquals(2, saved.getCritical());
                assertEquals(15, saved.getMedium());
                assertEquals(20, saved.getLow());
                assertEquals(8, saved.getToReview());
                assertEquals(5000, saved.getLines());
                
                return saved;
            });

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertTrue(result);
    }

    @Test
    void process_ShouldSetDefaultValues_WhenCountersAreNull() {
        // Given
        validDTO.setAssumed1(null);
        validDTO.setAssumed2(null);
        validDTO.setHigh(null);
        validDTO.setCritical(null);
        validDTO.setMedium(null);
        validDTO.setLow(null);
        validDTO.setToReview(null);
        validDTO.setLines(null);

        when(chimeraSastRepository.findByProjectIdAndName(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(chimeraSastRepository.saveAndFlush(any(ChimeraSast.class)))
            .thenAnswer(invocation -> {
                ChimeraSast saved = invocation.getArgument(0);
                
                // Verify defaults are set to 0
                assertEquals(0, saved.getAssumed1());
                assertEquals(0, saved.getAssumed2());
                assertEquals(0, saved.getHigh());
                assertEquals(0, saved.getCritical());
                assertEquals(0, saved.getMedium());
                assertEquals(0, saved.getLow());
                assertEquals(0, saved.getToReview());
                assertEquals(0, saved.getLines());
                
                return saved;
            });

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertTrue(result);
    }

    @Test
    void process_ShouldHandleStockFlowStock() {
        // Given
        validDTO.setStockFlow("stock");
        
        when(chimeraSastRepository.findByProjectIdAndName(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(chimeraSastRepository.saveAndFlush(any(ChimeraSast.class)))
            .thenAnswer(invocation -> {
                ChimeraSast saved = invocation.getArgument(0);
                assertEquals(ChimeraSast.StockFlow.stock, saved.getStockFlow());
                return saved;
            });

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertTrue(result);
    }

    @Test
    void process_ShouldHandleStockFlowFlow() {
        // Given
        validDTO.setStockFlow("flow");
        
        when(chimeraSastRepository.findByProjectIdAndName(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(chimeraSastRepository.saveAndFlush(any(ChimeraSast.class)))
            .thenAnswer(invocation -> {
                ChimeraSast saved = invocation.getArgument(0);
                assertEquals(ChimeraSast.StockFlow.flow, saved.getStockFlow());
                return saved;
            });

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertTrue(result);
    }

    @Test
    void process_ShouldSetStockFlowToNull_WhenInvalidValue() {
        // Given
        validDTO.setStockFlow("invalid");
        
        when(chimeraSastRepository.findByProjectIdAndName(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(chimeraSastRepository.saveAndFlush(any(ChimeraSast.class)))
            .thenAnswer(invocation -> {
                ChimeraSast saved = invocation.getArgument(0);
                assertNull(saved.getStockFlow());
                return saved;
            });

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertTrue(result);
    }

    @Test
    void process_ShouldSetStockFlowToNull_WhenEmpty() {
        // Given
        validDTO.setStockFlow("");
        
        when(chimeraSastRepository.findByProjectIdAndName(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(chimeraSastRepository.saveAndFlush(any(ChimeraSast.class)))
            .thenAnswer(invocation -> {
                ChimeraSast saved = invocation.getArgument(0);
                assertNull(saved.getStockFlow());
                return saved;
            });

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertTrue(result);
    }

    @Test
    void process_ShouldSetStockFlowToNull_WhenBlank() {
        // Given
        validDTO.setStockFlow("   ");
        
        when(chimeraSastRepository.findByProjectIdAndName(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(chimeraSastRepository.saveAndFlush(any(ChimeraSast.class)))
            .thenAnswer(invocation -> {
                ChimeraSast saved = invocation.getArgument(0);
                assertNull(saved.getStockFlow());
                return saved;
            });

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertTrue(result);
    }

    @Test
    void process_ShouldSetStockFlowToNull_WhenNull() {
        // Given
        validDTO.setStockFlow(null);
        
        when(chimeraSastRepository.findByProjectIdAndName(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(chimeraSastRepository.saveAndFlush(any(ChimeraSast.class)))
            .thenAnswer(invocation -> {
                ChimeraSast saved = invocation.getArgument(0);
                assertNull(saved.getStockFlow());
                return saved;
            });

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertTrue(result);
    }

    @Test
    void process_ShouldHandleNullOptionalFields() {
        // Given
        validDTO.setRepoUrl(null);
        validDTO.setUuaa(null);
        validDTO.setCountryDetName(null);
        validDTO.setApplication(null);
        validDTO.setLastScan(null);
        validDTO.setChimeraUrl(null);
        validDTO.setBranch(null);
        validDTO.setAnalyzer(null);
        validDTO.setArq(null);
        validDTO.setLanguage(null);
        validDTO.setStockFlow(null);

        when(chimeraSastRepository.findByProjectIdAndName(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(chimeraSastRepository.saveAndFlush(any(ChimeraSast.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertTrue(result);
    }

    @Test
    void process_ShouldReturnFalse_WhenExceptionOccurs() {
        // Given
        when(chimeraSastRepository.findByProjectIdAndName(anyString(), anyString()))
            .thenThrow(new RuntimeException("Database error"));

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(chimeraSastRepository, never()).saveAndFlush(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenSaveThrowsException() {
        // Given
        when(chimeraSastRepository.findByProjectIdAndName(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(chimeraSastRepository.saveAndFlush(any(ChimeraSast.class)))
            .thenThrow(new RuntimeException("Save error"));

        // When
        boolean result = chimeraSastProcessor.process(validDTO);

        // Then
        assertFalse(result);
    }
}
