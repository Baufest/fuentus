package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.VelocidadDTO;
import ar.com.bbva.fuentus.dto.VelocidadImportDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.Velocidad;
import ar.com.bbva.fuentus.processors.VelocidadProcessor;
import ar.com.bbva.fuentus.repositories.VelocidadRepository;
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
class VelocidadServiceTest {

    @Mock
    private VelocidadRepository velocidadRepository;

    @Mock
    private VelocidadProcessor velocidadProcessor;

    @InjectMocks
    private VelocidadService velocidadService;

    private MultipartFile mockFile;
    private LocalDate testDate;
    private String csvContent;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2025, 1, 15);
        
        csvContent = "service_n2,LT,CT,new_date,analyzing_date,ready_date,in_progress_date,test_date,ready_to_verify_date,to_rework_date,blocked_date,accepted_date,discarded_date,ready_to_deploy_date,deployed_date\n" +
                    "ETPB,203,173,1 ene 2025,5 ene 2025,,,,,,,,,,15 ene 2025\n" +
                    "CCOO,180,150,2 ene 2025,6 ene 2025,,,,,,,,,,14 ene 2025\n";
        
        mockFile = mock(MultipartFile.class);
    }

    @Test
    void importFromCSV_ShouldProcessAllRecords_WhenValidCSVProvided() throws IOException {
        // Given
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));
        when(velocidadProcessor.process(any(VelocidadImportDTO.class))).thenReturn(true);

        // When
        List<String> result = velocidadService.importFromCSV(mockFile);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).contains("2 registros procesados"));
        verify(velocidadProcessor, times(2)).process(any(VelocidadImportDTO.class));
    }

    @Test
    void importFromCSV_ShouldHandlePartialFailures_WhenSomeRecordsFail() throws IOException {
        // Given
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));
        when(velocidadProcessor.process(any(VelocidadImportDTO.class)))
            .thenReturn(true)  // First record succeeds
            .thenReturn(false); // Second record fails

        // When
        List<String> result = velocidadService.importFromCSV(mockFile);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).contains("1 exitosos"));
        assertTrue(result.get(0).contains("1 errores"));
        verify(velocidadProcessor, times(2)).process(any(VelocidadImportDTO.class));
    }

    @Test
    void importFromCSV_ShouldHandleException_WhenProcessorThrowsException() throws IOException {
        // Given
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));
        when(velocidadProcessor.process(any(VelocidadImportDTO.class)))
            .thenThrow(new RuntimeException("Processing error"));

        // When
        List<String> result = velocidadService.importFromCSV(mockFile);

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
        List<String> result = velocidadService.importFromCSV(mockFile);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).contains("Error al procesar el archivo"));
    }

    @Test
    void getAllByDate_ShouldReturnListOfDTOs_WhenRecordsExist() {
        // Given
        Nucleus nucleus = new Nucleus();
        nucleus.setId(1L);
        nucleus.setServiceN2("ETPB");

        Velocidad velocidad1 = new Velocidad();
        velocidad1.setId(1L);
        velocidad1.setNucleus(nucleus);
        velocidad1.setLt(203);
        velocidad1.setCt(173);
        velocidad1.setDate(testDate);

        Velocidad velocidad2 = new Velocidad();
        velocidad2.setId(2L);
        velocidad2.setNucleus(nucleus);
        velocidad2.setLt(180);
        velocidad2.setCt(150);
        velocidad2.setDate(testDate);

        List<Velocidad> velocidadList = Arrays.asList(velocidad1, velocidad2);
        when(velocidadRepository.findByDate(testDate)).thenReturn(velocidadList);

        // When
        List<VelocidadDTO> result = velocidadService.getAllByDate(testDate);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(velocidadRepository, times(1)).findByDate(testDate);
    }

    @Test
    void getAllByDate_ShouldReturnEmptyList_WhenNoRecordsExist() {
        // Given
        when(velocidadRepository.findByDate(testDate)).thenReturn(Arrays.asList());

        // When
        List<VelocidadDTO> result = velocidadService.getAllByDate(testDate);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(velocidadRepository, times(1)).findByDate(testDate);
    }

    @Test
    void getAll_ShouldReturnListOfDTOs_WhenRecordsExist() {
        // Given
        Nucleus nucleus = new Nucleus();
        nucleus.setId(1L);

        Velocidad velocidad = new Velocidad();
        velocidad.setId(1L);
        velocidad.setNucleus(nucleus);
        velocidad.setLt(203);
        velocidad.setCt(173);
        velocidad.setDate(testDate);

        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad));

        // When
        List<VelocidadDTO> result = velocidadService.getAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoRecordsExist() {
        // Given
        when(velocidadRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<VelocidadDTO> result = velocidadService.getAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(velocidadRepository, times(1)).findAll();
    }

    @Test
    void getAllByDate_ShouldHandleNullNucleus_WhenNucleusIsNull() {
        // Given
        Velocidad velocidad = new Velocidad();
        velocidad.setId(1L);
        velocidad.setNucleus(null);
        velocidad.setLt(203);
        velocidad.setCt(173);
        velocidad.setDate(testDate);

        when(velocidadRepository.findByDate(testDate)).thenReturn(Arrays.asList(velocidad));

        // When
        List<VelocidadDTO> result = velocidadService.getAllByDate(testDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0).getNucleusId());
        verify(velocidadRepository, times(1)).findByDate(testDate);
    }

    @Test
    void importFromCSV_ShouldHandleEmptyCSV_WhenNoDataRowsProvided() throws IOException {
        // Given
        String emptyCSV = "service_n2,LT,CT,new_date,analyzing_date,ready_date,in_progress_date,test_date,ready_to_verify_date,to_rework_date,blocked_date,accepted_date,discarded_date,ready_to_deploy_date,deployed_date\n";
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(emptyCSV.getBytes()));

        // When
        List<String> result = velocidadService.importFromCSV(mockFile);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).contains("0 registros procesados"));
        verify(velocidadProcessor, never()).process(any(VelocidadImportDTO.class));
    }

    @Test
    void importFromCSV_ShouldLogProcessingDetails_WhenProcessingRecords() throws IOException {
        // Given
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));
        when(velocidadProcessor.process(any(VelocidadImportDTO.class))).thenReturn(true);

        // When
        List<String> result = velocidadService.importFromCSV(mockFile);

        // Then
        assertNotNull(result);
        verify(velocidadProcessor, times(2)).process(any(VelocidadImportDTO.class));
    }

    @Test
    void getAllByDate_ShouldConvertToDTO_WhenRecordsExist() {
        // Given
        Nucleus nucleus = new Nucleus();
        nucleus.setId(1L);
        nucleus.setServiceN2("ETPB");

        Velocidad velocidad = new Velocidad();
        velocidad.setId(1L);
        velocidad.setNucleus(nucleus);
        velocidad.setLt(203);
        velocidad.setCt(173);
        velocidad.setDate(testDate);

        when(velocidadRepository.findByDate(testDate)).thenReturn(Arrays.asList(velocidad));

        // When
        List<VelocidadDTO> result = velocidadService.getAllByDate(testDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(1L, result.get(0).getNucleusId());
        assertEquals(203, result.get(0).getLt());
        assertEquals(173, result.get(0).getCt());
        assertEquals(testDate, result.get(0).getDate());
    }

    @Test
    void getAll_ShouldConvertToDTO_WhenRecordsExist() {
        // Given
        Nucleus nucleus = new Nucleus();
        nucleus.setId(1L);
        nucleus.setServiceN2("ETPB");

        Velocidad velocidad = new Velocidad();
        velocidad.setId(1L);
        velocidad.setNucleus(nucleus);
        velocidad.setLt(203);
        velocidad.setCt(173);
        velocidad.setDate(testDate);

        when(velocidadRepository.findAll()).thenReturn(Arrays.asList(velocidad));

        // When
        List<VelocidadDTO> result = velocidadService.getAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(1L, result.get(0).getNucleusId());
        assertEquals(203, result.get(0).getLt());
        assertEquals(173, result.get(0).getCt());
        assertEquals(testDate, result.get(0).getDate());
    }
}
