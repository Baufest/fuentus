package ar.com.bbva.fuentus.processors;

import ar.com.bbva.fuentus.dto.RfoImportDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.Rfo;
import ar.com.bbva.fuentus.mappers.DataImportMapper;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import ar.com.bbva.fuentus.repositories.RfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RfoProcessorTest {

    @Mock
    private RfoRepository rfoRepository;

    @Mock
    private NucleusRepository nucleusRepository;

    @Mock
    private DataImportMapper dataImportMapper;

    @InjectMocks
    private RfoProcessor rfoProcessor;

    private RfoImportDTO validRfoDTO;
    private Rfo validRfo;
    private Nucleus validNucleus;

    @BeforeEach
    void setUp() {
        // Configurar DTO válido
        validRfoDTO = new RfoImportDTO();
        validRfoDTO.setRfoId(1L);
        validRfoDTO.setSevicioN2("SN2_TEST");
        validRfoDTO.setEmail("juan.perez@test.com");
        validRfoDTO.setEstadoRfo("Activo");
        validRfoDTO.setFechaPuestaProduccion("2025-01-15");

        // Configurar entidad Nucleus válida
        validNucleus = new Nucleus();
        validNucleus.setId(100L);
        validNucleus.setServiceN1("SERVICIO N1 TEST");
        validNucleus.setServiceN2("SN2_TEST");

        // Configurar entidad Rfo válida
        validRfo = new Rfo();
        validRfo.setRfoId(1L);
        validRfo.setEmail("juan.perez@test.com");
        validRfo.setEstadoRfo("Activo");
        validRfo.setFechaPuestaProduccion("2025-01-15");
        validRfo.setNucleus(validNucleus);
    }

    @Test
    void process_ShouldCreateNewRfo_WhenValidDTOAndNucleusExists() {
        // Given
        when(rfoRepository.findByRfoId(1L)).thenReturn(null);
        when(dataImportMapper.toRfo(validRfoDTO)).thenReturn(validRfo);
        when(nucleusRepository.findFirstByServiceN2("SN2_TEST")).thenReturn(Optional.of(validNucleus));
        when(rfoRepository.save(any(Rfo.class))).thenReturn(validRfo);

        // When
        boolean result = rfoProcessor.process(validRfoDTO);

        // Then
        assertTrue(result);
        verify(rfoRepository, times(1)).findByRfoId(1L);
        verify(dataImportMapper, times(1)).toRfo(validRfoDTO);
        verify(nucleusRepository, times(1)).findFirstByServiceN2("SN2_TEST");
        verify(rfoRepository, times(1)).save(any(Rfo.class));
    }

    @Test
    void process_ShouldUpdateExistingRfo_WhenRfoAlreadyExists() {
        // Given
        Rfo existingRfo = new Rfo();
        existingRfo.setRfoId(1L);
        existingRfo.setNucleus(validNucleus);

        when(rfoRepository.findByRfoId(1L)).thenReturn(existingRfo);
        when(nucleusRepository.findFirstByServiceN2("SN2_TEST")).thenReturn(Optional.of(validNucleus));
        when(rfoRepository.save(any(Rfo.class))).thenReturn(existingRfo);

        // When
        boolean result = rfoProcessor.process(validRfoDTO);

        // Then
        assertTrue(result);
        verify(rfoRepository, times(1)).findByRfoId(1L);
        verify(dataImportMapper, times(1)).updateRfoFromDTO(validRfoDTO, existingRfo);
        verify(nucleusRepository, times(1)).findFirstByServiceN2("SN2_TEST");
        verify(rfoRepository, times(1)).save(existingRfo);
    }

    @Test
    void process_ShouldReturnFalse_WhenNucleusDoesNotExist() {
        // Given
        when(rfoRepository.findByRfoId(1L)).thenReturn(null);
        when(dataImportMapper.toRfo(validRfoDTO)).thenReturn(validRfo);
        when(nucleusRepository.findFirstByServiceN2("SN2_TEST")).thenReturn(Optional.empty());

        // When
        boolean result = rfoProcessor.process(validRfoDTO);

        // Then
        assertFalse(result);
        verify(rfoRepository, times(1)).findByRfoId(1L);
        verify(dataImportMapper, times(1)).toRfo(validRfoDTO);
        verify(nucleusRepository, times(1)).findFirstByServiceN2("SN2_TEST");
        verify(rfoRepository, never()).save(any(Rfo.class));
    }

    @Test
    void process_ShouldReturnFalse_WhenSevicioN2IsNull() {
        // Given
        validRfoDTO.setSevicioN2(null);
        when(rfoRepository.findByRfoId(1L)).thenReturn(null);
        when(dataImportMapper.toRfo(validRfoDTO)).thenReturn(validRfo);

        // When
        boolean result = rfoProcessor.process(validRfoDTO);

        // Then
        assertFalse(result);
        verify(rfoRepository, times(1)).findByRfoId(1L);
        verify(dataImportMapper, times(1)).toRfo(validRfoDTO);
        verify(nucleusRepository, never()).findFirstByServiceN2(anyString());
        verify(rfoRepository, never()).save(any(Rfo.class));
    }

    @Test
    void process_ShouldReturnFalse_WhenSevicioN2IsEmpty() {
        // Given
        validRfoDTO.setSevicioN2("   ");
        when(rfoRepository.findByRfoId(1L)).thenReturn(null);
        when(dataImportMapper.toRfo(validRfoDTO)).thenReturn(validRfo);

        // When
        boolean result = rfoProcessor.process(validRfoDTO);

        // Then
        assertFalse(result);
        verify(rfoRepository, times(1)).findByRfoId(1L);
        verify(dataImportMapper, times(1)).toRfo(validRfoDTO);
        verify(nucleusRepository, never()).findFirstByServiceN2(anyString());
        verify(rfoRepository, never()).save(any(Rfo.class));
    }

    @Test
    void process_ShouldReturnFalse_WhenRfoIdIsNull() {
        // Given
        validRfoDTO.setRfoId(null);

        // When
        boolean result = rfoProcessor.process(validRfoDTO);

        // Then
        assertFalse(result);
        verify(rfoRepository, never()).findByRfoId(anyLong());
        verify(dataImportMapper, never()).toRfo(any());
        verify(nucleusRepository, never()).findFirstByServiceN2(anyString());
        verify(rfoRepository, never()).save(any(Rfo.class));
    }

    @Test
    void process_ShouldReturnFalse_WhenRfoIdIsNegative() {
        // Given
        validRfoDTO.setRfoId(-1L);

        // When
        boolean result = rfoProcessor.process(validRfoDTO);

        // Then
        assertFalse(result);
        verify(rfoRepository, never()).findByRfoId(anyLong());
        verify(dataImportMapper, never()).toRfo(any());
        verify(nucleusRepository, never()).findFirstByServiceN2(anyString());
        verify(rfoRepository, never()).save(any(Rfo.class));
    }

    @Test
    void process_ShouldReturnFalse_WhenRfoIdIsZero() {
        // Given
        validRfoDTO.setRfoId(0L);

        // When
        boolean result = rfoProcessor.process(validRfoDTO);

        // Then
        assertFalse(result);
        verify(rfoRepository, never()).findByRfoId(anyLong());
        verify(dataImportMapper, never()).toRfo(any());
        verify(nucleusRepository, never()).findFirstByServiceN2(anyString());
        verify(rfoRepository, never()).save(any(Rfo.class));
    }

    @Test
    void process_ShouldThrowException_WhenDTOIsNotRfoImportDTO() {
        // Given
        Object invalidDTO = new Object();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> rfoProcessor.process(invalidDTO));
        verify(rfoRepository, never()).findByRfoId(anyLong());
        verify(rfoRepository, never()).save(any(Rfo.class));
    }

    @Test
    void process_ShouldThrowException_WhenDTOIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> rfoProcessor.process(null));
        verify(rfoRepository, never()).findByRfoId(anyLong());
        verify(rfoRepository, never()).save(any(Rfo.class));
    }

    @Test
    void process_ShouldReturnFalse_WhenDataIntegrityViolationOccurs() {
        // Given
        when(rfoRepository.findByRfoId(1L)).thenReturn(null);
        when(dataImportMapper.toRfo(validRfoDTO)).thenReturn(validRfo);
        when(nucleusRepository.findFirstByServiceN2("SN2_TEST")).thenReturn(Optional.of(validNucleus));
        when(rfoRepository.save(any(Rfo.class))).thenThrow(new DataIntegrityViolationException("Duplicate key"));

        // When
        boolean result = rfoProcessor.process(validRfoDTO);

        // Then
        assertFalse(result);
        verify(rfoRepository, times(1)).findByRfoId(1L);
        verify(rfoRepository, times(1)).save(any(Rfo.class));
    }

    @Test
    void process_ShouldReturnFalse_WhenUnexpectedExceptionOccurs() {
        // Given
        when(rfoRepository.findByRfoId(1L)).thenThrow(new RuntimeException("Database error"));

        // When
        boolean result = rfoProcessor.process(validRfoDTO);

        // Then
        assertFalse(result);
        verify(rfoRepository, times(1)).findByRfoId(1L);
        verify(rfoRepository, never()).save(any(Rfo.class));
    }

    @Test
    void process_ShouldTrimSevicioN2_BeforeNucleusLookup() {
        // Given
        validRfoDTO.setSevicioN2("  SN2_TEST  ");
        when(rfoRepository.findByRfoId(1L)).thenReturn(null);
        when(dataImportMapper.toRfo(validRfoDTO)).thenReturn(validRfo);
        when(nucleusRepository.findFirstByServiceN2("SN2_TEST")).thenReturn(Optional.of(validNucleus));
        when(rfoRepository.save(any(Rfo.class))).thenReturn(validRfo);

        // When
        boolean result = rfoProcessor.process(validRfoDTO);

        // Then
        assertTrue(result);
        verify(nucleusRepository, times(1)).findFirstByServiceN2("SN2_TEST");
    }

    @Test
    void process_ShouldSetNucleusAssociation_WhenNucleusFound() {
        // Given
        Rfo rfoWithoutNucleus = new Rfo();
        rfoWithoutNucleus.setRfoId(1L);
        rfoWithoutNucleus.setEmail("test@test.com");
        rfoWithoutNucleus.setEstadoRfo("EN CURSO");

        when(rfoRepository.findByRfoId(1L)).thenReturn(null);
        when(dataImportMapper.toRfo(validRfoDTO)).thenReturn(rfoWithoutNucleus);
        when(nucleusRepository.findFirstByServiceN2("SN2_TEST")).thenReturn(Optional.of(validNucleus));
        when(rfoRepository.save(any(Rfo.class))).thenReturn(rfoWithoutNucleus);

        // When
        boolean result = rfoProcessor.process(validRfoDTO);

        // Then
        assertTrue(result);
        verify(rfoRepository, times(1)).save(argThat(rfo -> 
            rfo.getNucleus() != null && 
            rfo.getNucleus().getId().equals(validNucleus.getId())
        ));
    }

    @Test
    void process_ShouldHandleMultipleRfosWithSameNucleus() {
        // Given - First RFO
        RfoImportDTO dto1 = new RfoImportDTO();
        dto1.setRfoId(1L);
        dto1.setSevicioN2("SN2_TEST");
        dto1.setEmail("test1@test.com");
        dto1.setEstadoRfo("EN CURSO");
        
        Rfo rfo1 = new Rfo();
        rfo1.setRfoId(1L);
        rfo1.setEmail("test1@test.com");
        rfo1.setEstadoRfo("EN CURSO");

        // Given - Second RFO
        RfoImportDTO dto2 = new RfoImportDTO();
        dto2.setRfoId(2L);
        dto2.setSevicioN2("SN2_TEST");
        dto2.setEmail("test2@test.com");
        dto2.setEstadoRfo("COMPLETADO");
        
        Rfo rfo2 = new Rfo();
        rfo2.setRfoId(2L);
        rfo2.setEmail("test2@test.com");
        rfo2.setEstadoRfo("COMPLETADO");

        when(rfoRepository.findByRfoId(1L)).thenReturn(null);
        when(dataImportMapper.toRfo(dto1)).thenReturn(rfo1);
        when(nucleusRepository.findFirstByServiceN2("SN2_TEST")).thenReturn(Optional.of(validNucleus));
        when(rfoRepository.save(any(Rfo.class))).thenReturn(rfo1, rfo2);

        when(rfoRepository.findByRfoId(2L)).thenReturn(null);
        when(dataImportMapper.toRfo(dto2)).thenReturn(rfo2);

        // When
        boolean result1 = rfoProcessor.process(dto1);
        boolean result2 = rfoProcessor.process(dto2);

        // Then
        assertTrue(result1);
        assertTrue(result2);
        verify(nucleusRepository, times(2)).findFirstByServiceN2("SN2_TEST");
        verify(rfoRepository, times(2)).save(any(Rfo.class));
    }

    @Test
    void process_ShouldReturnFalse_WhenMapperThrowsException() {
        // Given
        when(rfoRepository.findByRfoId(1L)).thenReturn(null);
        when(dataImportMapper.toRfo(validRfoDTO)).thenThrow(new IllegalArgumentException("Invalid mapping"));

        // When
        boolean result = rfoProcessor.process(validRfoDTO);

        // Then
        assertFalse(result);
        verify(dataImportMapper, times(1)).toRfo(validRfoDTO);
        verify(rfoRepository, never()).save(any(Rfo.class));
    }

    @Test
    void process_ShouldUpdateExistingRfoNucleusAssociation_WhenSevicioN2Changes() {
        // Given
        Nucleus oldNucleus = new Nucleus();
        oldNucleus.setId(200L);
        oldNucleus.setServiceN2("OLD_SN2");

        Rfo existingRfo = new Rfo();
        existingRfo.setRfoId(1L);
        existingRfo.setEmail("old@test.com");
        existingRfo.setEstadoRfo("EN CURSO");
        existingRfo.setNucleus(oldNucleus);

        validRfoDTO.setSevicioN2("SN2_TEST"); // New SN2

        when(rfoRepository.findByRfoId(1L)).thenReturn(existingRfo);
        when(nucleusRepository.findFirstByServiceN2("SN2_TEST")).thenReturn(Optional.of(validNucleus));
        when(rfoRepository.save(any(Rfo.class))).thenReturn(existingRfo);

        // When
        boolean result = rfoProcessor.process(validRfoDTO);

        // Then
        assertTrue(result);
        verify(rfoRepository, times(1)).save(argThat(rfo -> 
            rfo.getNucleus() != null && 
            rfo.getNucleus().getId().equals(validNucleus.getId())
        ));
    }
}
