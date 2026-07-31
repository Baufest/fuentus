package ar.com.bbva.fuentus.processors;

import ar.com.bbva.fuentus.dto.ProductividadImportDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.Productividad;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import ar.com.bbva.fuentus.repositories.ProductividadRepository;
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
class ProductividadProcessorTest {

    @Mock
    private ProductividadRepository productividadRepository;

    @Mock
    private NucleusRepository nucleusRepository;

    @InjectMocks
    private ProductividadProcessor productividadProcessor;

    private ProductividadImportDTO validDTO;
    private Nucleus validNucleus;
    private LocalDate testFecha;

    @BeforeEach
    void setUp() {
        testFecha = LocalDate.of(2025, 12, 30);

        validDTO = new ProductividadImportDTO();
        validDTO.setServicioN2("ARQUITECTURA ALPHA");
        validDTO.setFeatures("11");
        validDTO.setFtesDirectos("10,58");
        validDTO.setFtesIndirectos("4,90");

        validNucleus = new Nucleus();
        validNucleus.setId(1L);
        validNucleus.setServiceN2("ARQUITECTURA ALPHA");

        productividadProcessor.setFecha(testFecha);
    }

    @Test
    void process_ShouldCreateProductividad_WhenValidDTOProvided() {
        // Given
        when(nucleusRepository.findByServiceN2("ARQUITECTURA ALPHA")).thenReturn(Optional.of(validNucleus));
        when(productividadRepository.save(any(Productividad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = productividadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(nucleusRepository, times(1)).findByServiceN2("ARQUITECTURA ALPHA");
        verify(productividadRepository, times(1)).save(any(Productividad.class));
    }

    @Test
    void process_ShouldHandleNullNucleus_WhenServiceN2NotFound() {
        // Given
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.empty());
        when(productividadRepository.save(any(Productividad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = productividadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(nucleusRepository, times(1)).findByServiceN2("ARQUITECTURA ALPHA");
        verify(productividadRepository, times(1)).save(argThat(p -> p.getNucleus() == null));
    }

    @Test
    void process_ShouldParseDecimalWithComma_WhenCommaDecimalProvided() {
        // Given
        validDTO.setFtesDirectos("10,58");
        validDTO.setFtesIndirectos("4,90");
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));
        when(productividadRepository.save(any(Productividad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = productividadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(productividadRepository, times(1)).save(argThat(p -> 
            p.getFtesDirectos() == 10.58 && p.getFtesIndirectos() == 4.90
        ));
    }

    @Test
    void process_ShouldHandleNAValue_WhenFeaturesIsNA() {
        // Given
        validDTO.setFeatures("#N/A");
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));
        when(productividadRepository.save(any(Productividad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = productividadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(productividadRepository, times(1)).save(argThat(p -> p.getFeatures() == null));
    }

    @Test
    void process_ShouldHandleNAValue_WhenFtesIsNA() {
        // Given
        validDTO.setFtesDirectos("#N/A");
        validDTO.setFtesIndirectos("#N/A");
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));
        when(productividadRepository.save(any(Productividad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = productividadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(productividadRepository, times(1)).save(argThat(p -> 
            p.getFtesDirectos() == 0.0 && p.getFtesIndirectos() == 0.0
        ));
    }

    @Test
    void process_ShouldHandleEmptyValues_WhenFieldsAreEmpty() {
        // Given
        validDTO.setFeatures("");
        validDTO.setFtesDirectos("");
        validDTO.setFtesIndirectos("");
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));
        when(productividadRepository.save(any(Productividad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = productividadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(productividadRepository, times(1)).save(argThat(p -> 
            p.getFeatures() == null && p.getFtesDirectos() == 0.0 && p.getFtesIndirectos() == 0.0
        ));
    }

    @Test
    void process_ShouldReturnFalse_WhenFechaNotSet() {
        // Given
        productividadProcessor.setFecha(null);
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));

        // When
        boolean result = productividadProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(productividadRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenExceptionThrown() {
        // Given
        when(nucleusRepository.findByServiceN2(anyString())).thenThrow(new RuntimeException("Database error"));

        // When
        boolean result = productividadProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(productividadRepository, never()).save(any());
    }

    @Test
    void process_ShouldThrowException_WhenInvalidDTOTypeProvided() {
        // Given
        Object invalidDTO = new Object();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            productividadProcessor.process(invalidDTO)
        );
    }

    @Test
    void process_ShouldTrimWhitespace_WhenServiceN2HasWhitespace() {
        // Given
        validDTO.setServicioN2("  ARQUITECTURA ALPHA  ");
        when(nucleusRepository.findByServiceN2("ARQUITECTURA ALPHA")).thenReturn(Optional.of(validNucleus));
        when(productividadRepository.save(any(Productividad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = productividadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(nucleusRepository, times(1)).findByServiceN2("ARQUITECTURA ALPHA");
    }

    @Test
    void setFecha_ShouldSetFecha_WhenValidDateProvided() {
        // Given
        LocalDate newFecha = LocalDate.of(2025, 1, 1);

        // When
        productividadProcessor.setFecha(newFecha);
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));
        when(productividadRepository.save(any(Productividad.class))).thenAnswer(i -> i.getArguments()[0]);
        productividadProcessor.process(validDTO);

        // Then
        verify(productividadRepository, times(1)).save(argThat(p -> p.getFecha().equals(newFecha)));
    }

    @Test
    void process_ShouldHandleNullServiceN2_WhenServiceN2IsNull() {
        // Given
        validDTO.setServicioN2(null);
        when(productividadRepository.save(any(Productividad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = productividadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(nucleusRepository, never()).findByServiceN2(anyString());
        verify(productividadRepository, times(1)).save(argThat(p -> p.getNucleus() == null));
    }

    @Test
    void process_ShouldHandleEmptyServiceN2_WhenServiceN2IsEmpty() {
        // Given
        validDTO.setServicioN2("");
        when(productividadRepository.save(any(Productividad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = productividadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(nucleusRepository, never()).findByServiceN2(anyString());
        verify(productividadRepository, times(1)).save(argThat(p -> p.getNucleus() == null));
    }

    @Test
    void process_ShouldHandleInvalidDecimal_WhenInvalidNumberProvided() {
        // Given
        validDTO.setFtesDirectos("invalid");
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));
        when(productividadRepository.save(any(Productividad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = productividadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(productividadRepository, times(1)).save(argThat(p -> p.getFtesDirectos() == 0.0));
    }

    @Test
    void process_ShouldHandleInvalidInteger_WhenInvalidNumberProvided() {
        // Given
        validDTO.setFeatures("invalid");
        when(nucleusRepository.findByServiceN2(anyString())).thenReturn(Optional.of(validNucleus));
        when(productividadRepository.save(any(Productividad.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        boolean result = productividadProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(productividadRepository, times(1)).save(argThat(p -> p.getFeatures() == null));
    }
}
