package ar.com.bbva.fuentus.processors;

import ar.com.bbva.fuentus.dto.VelocidadImportDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.Velocidad;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import ar.com.bbva.fuentus.repositories.VelocidadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VelocidadProcessorTest {

    @Mock
    private VelocidadRepository velocidadRepository;

    @Mock
    private NucleusRepository nucleusRepository;

    @InjectMocks
    private VelocidadProcessor velocidadProcessor;

    private VelocidadImportDTO validDTO;
    private Nucleus validNucleus;

    @BeforeEach
    void setUp() {
        validDTO = new VelocidadImportDTO();
        validDTO.setServiceN2("ETPB");
        validDTO.setLt("203");
        validDTO.setCt("173");
        validDTO.setNewDate("1 ene 2025");
        validDTO.setDeployedDate("15 ene 2025");

        validNucleus = new Nucleus();
        validNucleus.setId(1L);
        validNucleus.setServiceN2("ETPB");
    }

    @Test
    void process_ShouldCreateVelocidad_WhenValidDTOProvided() {
        // Given
        when(nucleusRepository.findByServiceN2("ETPB")).thenReturn(Optional.of(validNucleus));
        when(velocidadRepository.save(any(Velocidad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = velocidadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(nucleusRepository, times(1)).findByServiceN2("ETPB");
        verify(velocidadRepository, times(1)).save(any(Velocidad.class));
    }

    @Test
    void process_ShouldFindMostRecentDate_WhenMultipleDatesProvided() {
        // Given
        validDTO.setNewDate("1 ene 2025");
        validDTO.setAnalyzingDate("5 ene 2025");
        validDTO.setDeployedDate("15 ene 2025");
        validDTO.setAcceptedDate("10 ene 2025");
        
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));
        when(velocidadRepository.save(any(Velocidad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = velocidadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(velocidadRepository, times(1)).save(argThat(v -> 
            v.getDate().equals(LocalDate.of(2025, 1, 15))
        ));
    }

    @Test
    void process_ShouldHandleDifferentDateFormats_WhenVariousFormatsProvided() {
        // Given
        validDTO.setNewDate("1 ene 2025");
        validDTO.setAnalyzingDate("05 feb 2025");
        validDTO.setDeployedDate("2025-03-15");
        
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));
        when(velocidadRepository.save(any(Velocidad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = velocidadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(velocidadRepository, times(1)).save(argThat(v -> 
            v.getDate().equals(LocalDate.of(2025, 3, 15))
        ));
    }

    @Test
    void process_ShouldReturnFalse_WhenNoDatesProvided() {
        // Given
        validDTO.setNewDate(null);
        validDTO.setAnalyzingDate(null);
        validDTO.setReadyDate(null);
        validDTO.setInProgressDate(null);
        validDTO.setTestDate(null);
        validDTO.setReadyToVerifyDate(null);
        validDTO.setToReworkDate(null);
        validDTO.setBlockedDate(null);
        validDTO.setAcceptedDate(null);
        validDTO.setDiscardedDate(null);
        validDTO.setReadyToDeployDate(null);
        validDTO.setDeployedDate(null);
        
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));

        // When
        boolean result = velocidadProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(velocidadRepository, never()).save(any());
    }

    @Test
    void process_ShouldHandleNullNucleus_WhenServiceN2NotFound() {
        // Given
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.empty());
        when(velocidadRepository.save(any(Velocidad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = velocidadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(nucleusRepository, times(1)).findByServiceN2("ETPB");
        verify(velocidadRepository, times(1)).save(argThat(v -> v.getNucleus() == null));
    }

    @Test
    void process_ShouldHandleNAValue_WhenLTIsNA() {
        // Given
        validDTO.setLt("#N/A");
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));
        when(velocidadRepository.save(any(Velocidad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = velocidadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(velocidadRepository, times(1)).save(argThat(v -> v.getLt() == null));
    }

    @Test
    void process_ShouldHandleNAValue_WhenCTIsNA() {
        // Given
        validDTO.setCt("#N/A");
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));
        when(velocidadRepository.save(any(Velocidad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = velocidadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(velocidadRepository, times(1)).save(argThat(v -> v.getCt() == null));
    }

    @Test
    void process_ShouldHandleEmptyValues_WhenFieldsAreEmpty() {
        // Given
        validDTO.setLt("");
        validDTO.setCt("");
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));
        when(velocidadRepository.save(any(Velocidad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = velocidadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(velocidadRepository, times(1)).save(argThat(v -> 
            v.getLt() == null && v.getCt() == null
        ));
    }

    @Test
    void process_ShouldReturnFalse_WhenExceptionThrown() {
        // Given
        when(nucleusRepository.findByServiceN2(anyString())).thenThrow(new RuntimeException("Database error"));

        // When
        boolean result = velocidadProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(velocidadRepository, never()).save(any());
    }

    @Test
    void process_ShouldThrowException_WhenInvalidDTOTypeProvided() {
        // Given
        Object invalidDTO = new Object();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            velocidadProcessor.process(invalidDTO)
        );
    }

    @Test
    void process_ShouldTrimWhitespace_WhenServiceN2HasWhitespace() {
        // Given
        validDTO.setServiceN2("  ETPB  ");
        when(nucleusRepository.findByServiceN2("ETPB")).thenReturn(Optional.of(validNucleus));
        when(velocidadRepository.save(any(Velocidad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = velocidadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(nucleusRepository, times(1)).findByServiceN2("ETPB");
    }

    @Test
    void process_ShouldHandleNullServiceN2_WhenServiceN2IsNull() {
        // Given
        validDTO.setServiceN2(null);
        when(velocidadRepository.save(any(Velocidad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = velocidadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(nucleusRepository, never()).findByServiceN2(anyString());
        verify(velocidadRepository, times(1)).save(argThat(v -> v.getNucleus() == null));
    }

    @Test
    void process_ShouldHandleEmptyServiceN2_WhenServiceN2IsEmpty() {
        // Given
        validDTO.setServiceN2("");
        when(velocidadRepository.save(any(Velocidad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = velocidadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(nucleusRepository, never()).findByServiceN2(anyString());
        verify(velocidadRepository, times(1)).save(argThat(v -> v.getNucleus() == null));
    }

    @Test
    void process_ShouldHandleInvalidInteger_WhenInvalidNumberProvided() {
        // Given
        validDTO.setLt("invalid");
        validDTO.setCt("invalid");
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));
        when(velocidadRepository.save(any(Velocidad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = velocidadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(velocidadRepository, times(1)).save(argThat(v -> 
            v.getLt() == null && v.getCt() == null
        ));
    }

    @Test
    void process_ShouldIgnoreInvalidDates_WhenUnparsableDatesProvided() {
        // Given
        validDTO.setNewDate("invalid date");
        validDTO.setAnalyzingDate("not a date");
        validDTO.setDeployedDate("15 ene 2025"); // Solo esta es válida
        
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));
        when(velocidadRepository.save(any(Velocidad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = velocidadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(velocidadRepository, times(1)).save(argThat(v -> 
            v.getDate().equals(LocalDate.of(2025, 1, 15))
        ));
    }

    @Test
    void process_ShouldHandleAllDateColumns_WhenAllDatesProvided() {
        // Given
        validDTO.setNewDate("1 ene 2025");
        validDTO.setAnalyzingDate("2 ene 2025");
        validDTO.setReadyDate("3 ene 2025");
        validDTO.setInProgressDate("4 ene 2025");
        validDTO.setTestDate("5 ene 2025");
        validDTO.setReadyToVerifyDate("6 ene 2025");
        validDTO.setToReworkDate("7 ene 2025");
        validDTO.setBlockedDate("8 ene 2025");
        validDTO.setAcceptedDate("9 ene 2025");
        validDTO.setDiscardedDate("10 ene 2025");
        validDTO.setReadyToDeployDate("11 ene 2025");
        validDTO.setDeployedDate("12 ene 2025");
        
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));
        when(velocidadRepository.save(any(Velocidad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = velocidadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(velocidadRepository, times(1)).save(argThat(v -> 
            v.getDate().equals(LocalDate.of(2025, 1, 12)) // La más reciente
        ));
    }

    @Test
    void process_ShouldHandleEmptyDates_WhenEmptyStringsProvided() {
        // Given
        validDTO.setNewDate("");
        validDTO.setAnalyzingDate("");
        validDTO.setReadyDate("");
        validDTO.setInProgressDate("");
        validDTO.setTestDate("");
        validDTO.setReadyToVerifyDate("");
        validDTO.setToReworkDate("");
        validDTO.setBlockedDate("");
        validDTO.setAcceptedDate("");
        validDTO.setDiscardedDate("");
        validDTO.setReadyToDeployDate("");
        validDTO.setDeployedDate("15 ene 2025");
        
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));
        when(velocidadRepository.save(any(Velocidad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = velocidadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(velocidadRepository, times(1)).save(argThat(v -> 
            v.getDate().equals(LocalDate.of(2025, 1, 15))
        ));
    }
}
