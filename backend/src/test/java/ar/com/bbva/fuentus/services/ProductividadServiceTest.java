package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.ProductividadDTO;
import ar.com.bbva.fuentus.dto.ProductividadImportDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.Productividad;
import ar.com.bbva.fuentus.processors.ProductividadProcessor;
import ar.com.bbva.fuentus.repositories.ProductividadRepository;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductividadServiceTest {

    @Mock
    private ProductividadRepository productividadRepository;

    @Mock
    private ProductividadProcessor productividadProcessor;

    @InjectMocks
    private ProductividadService productividadService;

    private MultipartFile mockFile;
    private LocalDate testFecha;
    private String csvContent;

    @BeforeEach
    void setUp() {
        testFecha = LocalDate.of(2025, 12, 30);
        
        csvContent = "Geografía Servicio,Servicio N1,Servicio N2,Full Service Id,UOL1,UOL2,Features,FTEs Directos,FTEs Indirectos,FTEs Totales,Productividad,LT,CT\n" +
                    "ARGENTINA,ARQ,ARQUITECTURA ALPHA,1,UOL1,UOL2,11,10.58,4.90,15.48,0.71,203,173\n" +
                    "ARGENTINA,DEV,DESARROLLO BETA,2,UOL1,UOL2,8,7.25,3.15,10.40,0.77,180,150\n";
        
        mockFile = mock(MultipartFile.class);
    }

    @Test
    void importFromCSV_ShouldProcessAllRecords_WhenValidCSVProvided() throws IOException {
        // Given
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));
        when(productividadProcessor.process(any(ProductividadImportDTO.class))).thenReturn(true);

        // When
        List<String> result = productividadService.importFromCSV(mockFile, testFecha);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).contains("2 registros procesados"));
        verify(productividadProcessor, times(1)).setFecha(testFecha);
        verify(productividadProcessor, times(2)).process(any(ProductividadImportDTO.class));
    }

    @Test
    void importFromCSV_ShouldHandlePartialFailures_WhenSomeRecordsFail() throws IOException {
        // Given
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));
        when(productividadProcessor.process(any(ProductividadImportDTO.class)))
            .thenReturn(true)  // First record succeeds
            .thenReturn(false); // Second record fails

        // When
        List<String> result = productividadService.importFromCSV(mockFile, testFecha);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).contains("1 exitosos"));
        assertTrue(result.get(0).contains("1 errores"));
        verify(productividadProcessor, times(2)).process(any(ProductividadImportDTO.class));
    }

    @Test
    void importFromCSV_ShouldHandleException_WhenProcessorThrowsException() throws IOException {
        // Given
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));
        when(productividadProcessor.process(any(ProductividadImportDTO.class)))
            .thenThrow(new RuntimeException("Processing error"));

        // When
        List<String> result = productividadService.importFromCSV(mockFile, testFecha);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(s -> s.contains("errores")));
    }

    @Test
    void importFromCSV_ShouldHandleIOException_WhenFileReadFails() throws IOException {
        // Given
        when(mockFile.getInputStream()).thenThrow(new IOException("File read error"));

        // When
        List<String> result = productividadService.importFromCSV(mockFile, testFecha);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).contains("Error al procesar el archivo"));
    }

    @Test
    void getAllByFecha_ShouldReturnListOfDTOs_WhenRecordsExist() {
        // Given
        Nucleus nucleus = new Nucleus();
        nucleus.setId(1L);
        nucleus.setServiceN2("ARQUITECTURA ALPHA");

        Productividad productividad1 = new Productividad();
        productividad1.setId(1L);
        productividad1.setNucleus(nucleus);
        productividad1.setFeatures(11);
        productividad1.setFtesDirectos(10.58);
        productividad1.setFtesIndirectos(4.90);
        productividad1.setFecha(testFecha);

        Productividad productividad2 = new Productividad();
        productividad2.setId(2L);
        productividad2.setNucleus(nucleus);
        productividad2.setFeatures(8);
        productividad2.setFtesDirectos(7.25);
        productividad2.setFtesIndirectos(3.15);
        productividad2.setFecha(testFecha);

        List<Productividad> productividadList = Arrays.asList(productividad1, productividad2);
        when(productividadRepository.findByFecha(testFecha)).thenReturn(productividadList);

        // When
        List<ProductividadDTO> result = productividadService.getAllByFecha(testFecha);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productividadRepository, times(1)).findByFecha(testFecha);
    }

    @Test
    void getAllByFecha_ShouldReturnEmptyList_WhenNoRecordsExist() {
        // Given
        when(productividadRepository.findByFecha(testFecha)).thenReturn(Arrays.asList());

        // When
        List<ProductividadDTO> result = productividadService.getAllByFecha(testFecha);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productividadRepository, times(1)).findByFecha(testFecha);
    }

    @Test
    void getAll_ShouldReturnListOfDTOs_WhenRecordsExist() {
        // Given
        Nucleus nucleus = new Nucleus();
        nucleus.setId(1L);

        Productividad productividad = new Productividad();
        productividad.setId(1L);
        productividad.setNucleus(nucleus);
        productividad.setFeatures(11);
        productividad.setFtesDirectos(10.58);
        productividad.setFtesIndirectos(4.90);
        productividad.setFecha(testFecha);

        when(productividadRepository.findAll()).thenReturn(Arrays.asList(productividad));

        // When
        List<ProductividadDTO> result = productividadService.getAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productividadRepository, times(1)).findAll();
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoRecordsExist() {
        // Given
        when(productividadRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<ProductividadDTO> result = productividadService.getAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productividadRepository, times(1)).findAll();
    }

    @Test
    void importFromCSV_ShouldSetFechaOnProcessor_WhenImportStarts() throws IOException {
        // Given
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));
        when(productividadProcessor.process(any(ProductividadImportDTO.class))).thenReturn(true);

        // When
        productividadService.importFromCSV(mockFile, testFecha);

        // Then
        verify(productividadProcessor, times(1)).setFecha(testFecha);
    }

    @Test
    void getAllByFecha_ShouldHandleNullNucleus_WhenNucleusIsNull() {
        // Given
        Productividad productividad = new Productividad();
        productividad.setId(1L);
        productividad.setNucleus(null);
        productividad.setFeatures(11);
        productividad.setFtesDirectos(10.58);
        productividad.setFtesIndirectos(4.90);
        productividad.setFecha(testFecha);

        when(productividadRepository.findByFecha(testFecha)).thenReturn(Arrays.asList(productividad));

        // When
        List<ProductividadDTO> result = productividadService.getAllByFecha(testFecha);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0).getNucleusId());
        verify(productividadRepository, times(1)).findByFecha(testFecha);
    }

    @Test
    void importFromCSV_ShouldHandleEmptyCSV_WhenNoDataRowsProvided() throws IOException {
        // Given
        String emptyCSV = "Geografía Servicio,Servicio N1,Servicio N2,Full Service Id,UOL1,UOL2,Features,FTEs Directos,FTEs Indirectos,FTEs Totales,Productividad,LT,CT\n";
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(emptyCSV.getBytes()));

        // When
        List<String> result = productividadService.importFromCSV(mockFile, testFecha);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).contains("0 registros procesados"));
        verify(productividadProcessor, never()).process(any(ProductividadImportDTO.class));
    }

    @Test
    void importFromCSV_ShouldLogProcessingDetails_WhenProcessingRecords() throws IOException {
        // Given
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));
        when(productividadProcessor.process(any(ProductividadImportDTO.class))).thenReturn(true);

        // When
        List<String> result = productividadService.importFromCSV(mockFile, testFecha);

        // Then
        assertNotNull(result);
        verify(productividadProcessor, times(1)).setFecha(testFecha);
        verify(productividadProcessor, times(2)).process(any(ProductividadImportDTO.class));
    }
}
