package ar.com.bbva.fuentus.processors;

import ar.com.bbva.fuentus.dto.NucleusImportDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.mappers.DataImportMapper;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
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
class NucleusProcessorTest {

    @Mock
    private NucleusRepository nucleusRepository;

    @Mock
    private DataImportMapper dataImportMapper;

    @InjectMocks
    private NucleusProcessor nucleusProcessor;

    private NucleusImportDTO validNucleusDTO;
    private Nucleus validNucleus;

    @BeforeEach
    void setUp() {
        // Configurar DTO válido
        validNucleusDTO = new NucleusImportDTO();
        validNucleusDTO.setIdFullservice(100L);
        validNucleusDTO.setServiceN1("SERVICIO N1 TEST");
        validNucleusDTO.setServiceN2("SN2_TEST");
        validNucleusDTO.setOwnerServiceN1("Owner N1");
        validNucleusDTO.setOwnerServiceN2("Owner N2");

        // Configurar entidad válida
        validNucleus = new Nucleus();
        validNucleus.setId(100L);
        validNucleus.setServiceN1("SERVICIO N1 TEST");
        validNucleus.setServiceN2("SN2_TEST");
        validNucleus.setOwnerServiceN1("Owner N1");
        validNucleus.setOwnerServiceN2("Owner N2");
    }

    @Test
    void process_ShouldCreateNewNucleus_WhenValidDTOProvided() {
        // Given
        when(nucleusRepository.findById(100L)).thenReturn(Optional.empty());
        when(dataImportMapper.toNucleus(validNucleusDTO)).thenReturn(validNucleus);
        when(nucleusRepository.save(any(Nucleus.class))).thenReturn(validNucleus);

        // When
        boolean result = nucleusProcessor.process(validNucleusDTO);

        // Then
        assertTrue(result);
        verify(nucleusRepository, times(1)).findById(100L);
        verify(dataImportMapper, times(1)).toNucleus(validNucleusDTO);
        verify(nucleusRepository, times(1)).save(validNucleus);
    }

    @Test
    void process_ShouldUpdateExistingNucleus_WhenNucleusAlreadyExists() {
        // Given
        Nucleus existingNucleus = new Nucleus();
        existingNucleus.setId(100L);
        existingNucleus.setServiceN1("OLD SERVICE N1");
        existingNucleus.setServiceN2("OLD_SN2");

        when(nucleusRepository.findById(100L)).thenReturn(Optional.of(existingNucleus));
        when(nucleusRepository.save(any(Nucleus.class))).thenReturn(existingNucleus);

        // When
        boolean result = nucleusProcessor.process(validNucleusDTO);

        // Then
        assertTrue(result);
        verify(nucleusRepository, times(1)).findById(100L);
        verify(dataImportMapper, times(1)).updateNucleusFromDTO(validNucleusDTO, existingNucleus);
        verify(nucleusRepository, times(1)).save(existingNucleus);
    }

    @Test
    void process_ShouldReturnFalse_WhenIdFullserviceIsNull() {
        // Given
        validNucleusDTO.setIdFullservice(null);

        // When
        boolean result = nucleusProcessor.process(validNucleusDTO);

        // Then
        assertFalse(result);
        verify(nucleusRepository, never()).findById(anyLong());
        verify(dataImportMapper, never()).toNucleus(any());
        verify(nucleusRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenIdFullserviceIsNegative() {
        // Given
        validNucleusDTO.setIdFullservice(-1L);

        // When
        boolean result = nucleusProcessor.process(validNucleusDTO);

        // Then
        assertFalse(result);
        verify(nucleusRepository, never()).findById(anyLong());
        verify(dataImportMapper, never()).toNucleus(any());
        verify(nucleusRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenIdFullserviceIsZero() {
        // Given
        validNucleusDTO.setIdFullservice(0L);

        // When
        boolean result = nucleusProcessor.process(validNucleusDTO);

        // Then
        assertFalse(result);
        verify(nucleusRepository, never()).findById(anyLong());
        verify(dataImportMapper, never()).toNucleus(any());
        verify(nucleusRepository, never()).save(any());
    }

    @Test
    void process_ShouldThrowException_WhenDTOIsNotNucleusImportDTO() {
        // Given
        Object invalidDTO = new Object();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> nucleusProcessor.process(invalidDTO));
        verify(nucleusRepository, never()).findById(anyLong());
        verify(nucleusRepository, never()).save(any());
    }

    @Test
    void process_ShouldThrowException_WhenDTOIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> nucleusProcessor.process(null));
        verify(nucleusRepository, never()).findById(anyLong());
        verify(nucleusRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenServiceN1IsNull() {
        // Given
        Nucleus nucleusWithoutN1 = new Nucleus();
        nucleusWithoutN1.setId(100L);
        nucleusWithoutN1.setServiceN1(null);
        nucleusWithoutN1.setServiceN2("SN2_TEST");

        when(nucleusRepository.findById(100L)).thenReturn(Optional.empty());
        when(dataImportMapper.toNucleus(validNucleusDTO)).thenReturn(nucleusWithoutN1);

        // When
        boolean result = nucleusProcessor.process(validNucleusDTO);

        // Then
        assertFalse(result);
        verify(nucleusRepository, times(1)).findById(100L);
        verify(dataImportMapper, times(1)).toNucleus(validNucleusDTO);
        verify(nucleusRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenServiceN1IsEmpty() {
        // Given
        Nucleus nucleusWithEmptyN1 = new Nucleus();
        nucleusWithEmptyN1.setId(100L);
        nucleusWithEmptyN1.setServiceN1("   ");
        nucleusWithEmptyN1.setServiceN2("SN2_TEST");

        when(nucleusRepository.findById(100L)).thenReturn(Optional.empty());
        when(dataImportMapper.toNucleus(validNucleusDTO)).thenReturn(nucleusWithEmptyN1);

        // When
        boolean result = nucleusProcessor.process(validNucleusDTO);

        // Then
        assertFalse(result);
        verify(nucleusRepository, times(1)).findById(100L);
        verify(dataImportMapper, times(1)).toNucleus(validNucleusDTO);
        verify(nucleusRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenServiceN2IsNull() {
        // Given
        Nucleus nucleusWithoutN2 = new Nucleus();
        nucleusWithoutN2.setId(100L);
        nucleusWithoutN2.setServiceN1("SERVICIO N1 TEST");
        nucleusWithoutN2.setServiceN2(null);

        when(nucleusRepository.findById(100L)).thenReturn(Optional.empty());
        when(dataImportMapper.toNucleus(validNucleusDTO)).thenReturn(nucleusWithoutN2);

        // When
        boolean result = nucleusProcessor.process(validNucleusDTO);

        // Then
        assertFalse(result);
        verify(nucleusRepository, times(1)).findById(100L);
        verify(dataImportMapper, times(1)).toNucleus(validNucleusDTO);
        verify(nucleusRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenServiceN2IsEmpty() {
        // Given
        Nucleus nucleusWithEmptyN2 = new Nucleus();
        nucleusWithEmptyN2.setId(100L);
        nucleusWithEmptyN2.setServiceN1("SERVICIO N1 TEST");
        nucleusWithEmptyN2.setServiceN2("   ");

        when(nucleusRepository.findById(100L)).thenReturn(Optional.empty());
        when(dataImportMapper.toNucleus(validNucleusDTO)).thenReturn(nucleusWithEmptyN2);

        // When
        boolean result = nucleusProcessor.process(validNucleusDTO);

        // Then
        assertFalse(result);
        verify(nucleusRepository, times(1)).findById(100L);
        verify(dataImportMapper, times(1)).toNucleus(validNucleusDTO);
        verify(nucleusRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenDataIntegrityViolationOccurs() {
        // Given
        when(nucleusRepository.findById(100L)).thenReturn(Optional.empty());
        when(dataImportMapper.toNucleus(validNucleusDTO)).thenReturn(validNucleus);
        when(nucleusRepository.save(any(Nucleus.class))).thenThrow(new DataIntegrityViolationException("Duplicate key"));

        // When
        boolean result = nucleusProcessor.process(validNucleusDTO);

        // Then
        assertFalse(result);
        verify(nucleusRepository, times(1)).findById(100L);
        verify(nucleusRepository, times(1)).save(validNucleus);
    }

    @Test
    void process_ShouldReturnFalse_WhenUnexpectedExceptionOccurs() {
        // Given
        when(nucleusRepository.findById(100L)).thenThrow(new RuntimeException("Database error"));

        // When
        boolean result = nucleusProcessor.process(validNucleusDTO);

        // Then
        assertFalse(result);
        verify(nucleusRepository, times(1)).findById(100L);
        verify(nucleusRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenMapperThrowsException() {
        // Given
        when(nucleusRepository.findById(100L)).thenReturn(Optional.empty());
        when(dataImportMapper.toNucleus(validNucleusDTO)).thenThrow(new IllegalArgumentException("Invalid mapping"));

        // When
        boolean result = nucleusProcessor.process(validNucleusDTO);

        // Then
        assertFalse(result);
        verify(dataImportMapper, times(1)).toNucleus(validNucleusDTO);
        verify(nucleusRepository, never()).save(any());
    }

    @Test
    void process_ShouldHandleMultipleNucleusRecords() {
        // Given - First Nucleus
        NucleusImportDTO dto1 = new NucleusImportDTO();
        dto1.setIdFullservice(100L);
        dto1.setServiceN1("SERVICE N1");
        dto1.setServiceN2("SN2_1");

        Nucleus nucleus1 = new Nucleus();
        nucleus1.setId(100L);
        nucleus1.setServiceN1("SERVICE N1");
        nucleus1.setServiceN2("SN2_1");

        // Given - Second Nucleus
        NucleusImportDTO dto2 = new NucleusImportDTO();
        dto2.setIdFullservice(200L);
        dto2.setServiceN1("SERVICE N2");
        dto2.setServiceN2("SN2_2");

        Nucleus nucleus2 = new Nucleus();
        nucleus2.setId(200L);
        nucleus2.setServiceN1("SERVICE N2");
        nucleus2.setServiceN2("SN2_2");

        when(nucleusRepository.findById(100L)).thenReturn(Optional.empty());
        when(dataImportMapper.toNucleus(dto1)).thenReturn(nucleus1);
        when(nucleusRepository.save(nucleus1)).thenReturn(nucleus1);

        when(nucleusRepository.findById(200L)).thenReturn(Optional.empty());
        when(dataImportMapper.toNucleus(dto2)).thenReturn(nucleus2);
        when(nucleusRepository.save(nucleus2)).thenReturn(nucleus2);

        // When
        boolean result1 = nucleusProcessor.process(dto1);
        boolean result2 = nucleusProcessor.process(dto2);

        // Then
        assertTrue(result1);
        assertTrue(result2);
        verify(nucleusRepository, times(1)).save(nucleus1);
        verify(nucleusRepository, times(1)).save(nucleus2);
    }

    @Test
    void process_ShouldUpdateOnlyChangedFields_WhenUpdatingExistingNucleus() {
        // Given
        Nucleus existingNucleus = new Nucleus();
        existingNucleus.setId(100L);
        existingNucleus.setServiceN1("OLD SERVICE N1");
        existingNucleus.setServiceN2("OLD_SN2");
        existingNucleus.setOwnerServiceN1("OLD OWNER N1");

        validNucleusDTO.setServiceN1("NEW SERVICE N1");
        validNucleusDTO.setServiceN2("NEW_SN2");
        validNucleusDTO.setOwnerServiceN1("NEW OWNER N1");

        when(nucleusRepository.findById(100L)).thenReturn(Optional.of(existingNucleus));
        when(nucleusRepository.save(any(Nucleus.class))).thenReturn(existingNucleus);

        // When
        boolean result = nucleusProcessor.process(validNucleusDTO);

        // Then
        assertTrue(result);
        verify(dataImportMapper, times(1)).updateNucleusFromDTO(validNucleusDTO, existingNucleus);
        verify(nucleusRepository, times(1)).save(existingNucleus);
    }

    @Test
    void process_ShouldPreserveIdWhenUpdating() {
        // Given
        Nucleus existingNucleus = new Nucleus();
        existingNucleus.setId(100L);
        existingNucleus.setServiceN1("SERVICE N1");
        existingNucleus.setServiceN2("SN2_TEST");

        when(nucleusRepository.findById(100L)).thenReturn(Optional.of(existingNucleus));
        when(nucleusRepository.save(any(Nucleus.class))).thenAnswer(invocation -> {
            Nucleus savedNucleus = invocation.getArgument(0);
            assertEquals(100L, savedNucleus.getId());
            return savedNucleus;
        });

        // When
        boolean result = nucleusProcessor.process(validNucleusDTO);

        // Then
        assertTrue(result);
        verify(nucleusRepository, times(1)).save(argThat(nucleus -> nucleus.getId().equals(100L)));
    }
}
