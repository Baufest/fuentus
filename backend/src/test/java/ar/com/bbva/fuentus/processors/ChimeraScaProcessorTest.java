package ar.com.bbva.fuentus.processors;

import ar.com.bbva.fuentus.dto.ChimeraScaImportDTO;
import ar.com.bbva.fuentus.entities.ChimeraSca;
import ar.com.bbva.fuentus.repositories.ChimeraScaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChimeraScaProcessorTest {

    @Mock
    private ChimeraScaRepository chimeraScaRepository;

    @InjectMocks
    private ChimeraScaProcessor chimeraScaProcessor;

    private ChimeraScaImportDTO validDTO;
    private ChimeraSca existingEntity;

    @BeforeEach
    void setUp() {
        // Setup valid DTO
        validDTO = new ChimeraScaImportDTO();
        validDTO.setProjectId("PROJ-123");
        validDTO.setAppId(100L);
        validDTO.setName("Test Application");
        validDTO.setUuaa("1234");
        validDTO.setLow(10L);
        validDTO.setMedium(5L);
        validDTO.setHigh(2L);
        validDTO.setCritical(1L);

        // Setup existing entity
        existingEntity = new ChimeraSca();
        existingEntity.setId(1L);
        existingEntity.setProjectId("PROJ-123");
        existingEntity.setName("Test Application");
        existingEntity.setLow(5L);
        existingEntity.setMedium(3L);
    }

    @Test
    void process_ShouldReturnFalse_WhenDTOIsNotChimeraScaImportDTO() {
        // Given
        Object invalidDTO = new Object();

        // When
        boolean result = chimeraScaProcessor.process(invalidDTO);

        // Then
        assertFalse(result);
        verify(chimeraScaRepository, never()).findByProjectIdAndName(anyString(), anyString());
    }

    @Test
    void process_ShouldReturnFalse_WhenProjectIdIsNull() {
        // Given
        validDTO.setProjectId(null);

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(chimeraScaRepository, never()).saveAndFlush(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenProjectIdIsEmpty() {
        // Given
        validDTO.setProjectId("");

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(chimeraScaRepository, never()).saveAndFlush(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenProjectIdIsBlank() {
        // Given
        validDTO.setProjectId("   ");

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(chimeraScaRepository, never()).saveAndFlush(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenNameIsNull() {
        // Given
        validDTO.setName(null);

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(chimeraScaRepository, never()).saveAndFlush(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenNameIsEmpty() {
        // Given
        validDTO.setName("");

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(chimeraScaRepository, never()).saveAndFlush(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenNameIsBlank() {
        // Given
        validDTO.setName("   ");

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(chimeraScaRepository, never()).saveAndFlush(any());
    }

    @Test
    void process_ShouldCreateNewEntity_WhenNotExists() {
        // Given
        when(chimeraScaRepository.findByProjectIdAndName("PROJ-123", "Test Application"))
            .thenReturn(Optional.empty());
        when(chimeraScaRepository.saveAndFlush(any(ChimeraSca.class)))
            .thenAnswer(invocation -> {
                ChimeraSca sca = invocation.getArgument(0);
                sca.setId(10L);
                return sca;
            });

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(chimeraScaRepository).findByProjectIdAndName("PROJ-123", "Test Application");
        verify(chimeraScaRepository).saveAndFlush(any(ChimeraSca.class));
    }

    @Test
    void process_ShouldUpdateExistingEntity_WhenExists() {
        // Given
        when(chimeraScaRepository.findByProjectIdAndName("PROJ-123", "Test Application"))
            .thenReturn(Optional.of(existingEntity));
        when(chimeraScaRepository.saveAndFlush(any(ChimeraSca.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(chimeraScaRepository).findByProjectIdAndName("PROJ-123", "Test Application");
        verify(chimeraScaRepository).saveAndFlush(existingEntity);
        
        // Verify updated values
        assertEquals("PROJ-123", existingEntity.getProjectId());
        assertEquals("Test Application", existingEntity.getName());
        assertEquals(100L, existingEntity.getAppId());
        assertEquals("1234", existingEntity.getUuaa());
        assertEquals(10L, existingEntity.getLow());
        assertEquals(5L, existingEntity.getMedium());
        assertEquals(2L, existingEntity.getHigh());
        assertEquals(1L, existingEntity.getCritical());
    }

    @Test
    void process_ShouldMapAllFields_WhenValid() {
        // Given
        when(chimeraScaRepository.findByProjectIdAndName(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(chimeraScaRepository.saveAndFlush(any(ChimeraSca.class)))
            .thenAnswer(invocation -> {
                ChimeraSca saved = invocation.getArgument(0);
                
                // Verify all fields are mapped
                assertEquals("PROJ-123", saved.getProjectId());
                assertEquals(100L, saved.getAppId());
                assertEquals("Test Application", saved.getName());
                assertEquals("1234", saved.getUuaa());
                assertEquals(10L, saved.getLow());
                assertEquals(5L, saved.getMedium());
                assertEquals(2L, saved.getHigh());
                assertEquals(1L, saved.getCritical());
                
                saved.setId(10L);
                return saved;
            });

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertTrue(result);
    }

    @Test
    void process_ShouldHandleNullOptionalFields() {
        // Given
        validDTO.setAppId(null);
        validDTO.setUuaa(null);
        validDTO.setLow(null);
        validDTO.setMedium(null);
        validDTO.setHigh(null);
        validDTO.setCritical(null);

        when(chimeraScaRepository.findByProjectIdAndName(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(chimeraScaRepository.saveAndFlush(any(ChimeraSca.class)))
            .thenAnswer(invocation -> {
                ChimeraSca saved = invocation.getArgument(0);
                
                // Verify null values are handled
                assertNull(saved.getAppId());
                assertNull(saved.getUuaa());
                assertNull(saved.getLow());
                assertNull(saved.getMedium());
                assertNull(saved.getHigh());
                assertNull(saved.getCritical());
                
                saved.setId(10L);
                return saved;
            });

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertTrue(result);
    }

    @Test
    void process_ShouldReturnFalse_WhenExceptionOccurs() {
        // Given
        when(chimeraScaRepository.findByProjectIdAndName(anyString(), anyString()))
            .thenThrow(new RuntimeException("Database error"));

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(chimeraScaRepository, never()).saveAndFlush(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenSaveThrowsException() {
        // Given
        when(chimeraScaRepository.findByProjectIdAndName(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(chimeraScaRepository.saveAndFlush(any(ChimeraSca.class)))
            .thenThrow(new RuntimeException("Save error"));

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertFalse(result);
    }

    @Test
    void process_ShouldUpdateCounters_WhenUpdating() {
        // Given
        existingEntity.setLow(100L);
        existingEntity.setMedium(50L);
        existingEntity.setHigh(25L);
        existingEntity.setCritical(10L);

        when(chimeraScaRepository.findByProjectIdAndName("PROJ-123", "Test Application"))
            .thenReturn(Optional.of(existingEntity));
        when(chimeraScaRepository.saveAndFlush(any(ChimeraSca.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertTrue(result);
        
        // Verify counters are updated
        assertEquals(10L, existingEntity.getLow());
        assertEquals(5L, existingEntity.getMedium());
        assertEquals(2L, existingEntity.getHigh());
        assertEquals(1L, existingEntity.getCritical());
    }

    @Test
    void process_ShouldUpdateCountersToNull_WhenDTOHasNullValues() {
        // Given
        validDTO.setLow(null);
        validDTO.setMedium(null);
        validDTO.setHigh(null);
        validDTO.setCritical(null);

        existingEntity.setLow(100L);
        existingEntity.setMedium(50L);
        existingEntity.setHigh(25L);
        existingEntity.setCritical(10L);

        when(chimeraScaRepository.findByProjectIdAndName("PROJ-123", "Test Application"))
            .thenReturn(Optional.of(existingEntity));
        when(chimeraScaRepository.saveAndFlush(any(ChimeraSca.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertTrue(result);
        
        // Verify counters are updated to null
        assertNull(existingEntity.getLow());
        assertNull(existingEntity.getMedium());
        assertNull(existingEntity.getHigh());
        assertNull(existingEntity.getCritical());
    }

    @Test
    void process_ShouldHandleZeroValues() {
        // Given
        validDTO.setLow(0L);
        validDTO.setMedium(0L);
        validDTO.setHigh(0L);
        validDTO.setCritical(0L);

        when(chimeraScaRepository.findByProjectIdAndName(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(chimeraScaRepository.saveAndFlush(any(ChimeraSca.class)))
            .thenAnswer(invocation -> {
                ChimeraSca saved = invocation.getArgument(0);
                
                // Verify zero values are handled
                assertEquals(0L, saved.getLow());
                assertEquals(0L, saved.getMedium());
                assertEquals(0L, saved.getHigh());
                assertEquals(0L, saved.getCritical());
                
                saved.setId(10L);
                return saved;
            });

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertTrue(result);
    }

    @Test
    void process_ShouldNotSearchWhenProjectIdIsNull() {
        // Given
        validDTO.setProjectId(null);

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(chimeraScaRepository, never()).findByProjectIdAndName(anyString(), anyString());
    }

    @Test
    void process_ShouldNotSearchWhenNameIsNull() {
        // Given
        validDTO.setName(null);

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(chimeraScaRepository, never()).findByProjectIdAndName(anyString(), anyString());
    }

    @Test
    void process_ShouldHandleLargeCounterValues() {
        // Given
        validDTO.setLow(Long.MAX_VALUE);
        validDTO.setMedium(Long.MAX_VALUE - 1);
        validDTO.setHigh(Long.MAX_VALUE - 2);
        validDTO.setCritical(Long.MAX_VALUE - 3);

        when(chimeraScaRepository.findByProjectIdAndName(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(chimeraScaRepository.saveAndFlush(any(ChimeraSca.class)))
            .thenAnswer(invocation -> {
                ChimeraSca saved = invocation.getArgument(0);
                
                // Verify large values are handled
                assertEquals(Long.MAX_VALUE, saved.getLow());
                assertEquals(Long.MAX_VALUE - 1, saved.getMedium());
                assertEquals(Long.MAX_VALUE - 2, saved.getHigh());
                assertEquals(Long.MAX_VALUE - 3, saved.getCritical());
                
                saved.setId(10L);
                return saved;
            });

        // When
        boolean result = chimeraScaProcessor.process(validDTO);

        // Then
        assertTrue(result);
    }
}
