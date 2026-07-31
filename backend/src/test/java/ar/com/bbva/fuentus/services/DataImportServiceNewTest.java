package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.DTOParserService;
import ar.com.bbva.fuentus.dto.ImportResultDTO;
import ar.com.bbva.fuentus.dto.NucleusImportDTO;
import ar.com.bbva.fuentus.dto.RfoImportDTO;
import ar.com.bbva.fuentus.enums.EntityType;
import ar.com.bbva.fuentus.processors.EntityProcessor;
import ar.com.bbva.fuentus.processors.ProcessorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataImportServiceNewTest {

    @Mock
    private DTOParserService dtoParserService;

    @Mock
    private ProcessorFactory processorFactory;

    @Mock
    private EntityManager entityManager;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private EntityProcessor entityProcessor;

    @InjectMocks
    private DataImportServiceNew dataImportServiceNew;

    private NucleusImportDTO nucleusDTO1;
    private NucleusImportDTO nucleusDTO2;
    private RfoImportDTO rfoDTO1;

    @BeforeEach
    void setUp() {
        // Configurar DTOs de prueba
        nucleusDTO1 = new NucleusImportDTO();
        nucleusDTO1.setIdFullservice(100L);
        nucleusDTO1.setServiceN1("SERVICE N1");
        nucleusDTO1.setServiceN2("SN2_1");

        nucleusDTO2 = new NucleusImportDTO();
        nucleusDTO2.setIdFullservice(200L);
        nucleusDTO2.setServiceN1("SERVICE N2");
        nucleusDTO2.setServiceN2("SN2_2");

        rfoDTO1 = new RfoImportDTO();
        rfoDTO1.setRfoId(1L);
        rfoDTO1.setSevicioN2("SN2_1");
        rfoDTO1.setEmail("test@test.com");
        rfoDTO1.setEstadoRfo("EN CURSO");

        when(multipartFile.getOriginalFilename()).thenReturn("test.csv");
    }

    @Test
    @SuppressWarnings("unchecked")
    void importData_ShouldImportAllRecordsSuccessfully_WhenCSVFormatAndValidData() throws Exception {
        // Given
        List<NucleusImportDTO> dtos = Arrays.asList(nucleusDTO1, nucleusDTO2);
        when(dtoParserService.parseCsvFile(multipartFile, EntityType.NUCLEUS_SERVICES)).thenReturn((List) dtos);
        when(processorFactory.getProcessor(EntityType.NUCLEUS_SERVICES)).thenReturn(entityProcessor);
        when(entityProcessor.process(any())).thenReturn(true);

        // When
        ImportResultDTO result = dataImportServiceNew.importData(multipartFile, EntityType.NUCLEUS_SERVICES, "csv");

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalProcessed());
        assertEquals(2, result.getSuccessfulImports());
        assertNull(result.getErrorMessage());
        verify(dtoParserService, times(1)).parseCsvFile(multipartFile, EntityType.NUCLEUS_SERVICES);
        verify(entityProcessor, times(2)).process(any());
        verify(entityManager, times(2)).clear();
    }

    @Test
    @SuppressWarnings("unchecked")
    void importData_ShouldImportAllRecordsSuccessfully_WhenJSONFormatAndValidData() throws Exception {
        // Given
        List<RfoImportDTO> dtos = Collections.singletonList(rfoDTO1);
        when(dtoParserService.parseJsonFile(multipartFile, EntityType.RFO)).thenReturn((List) dtos);
        when(processorFactory.getProcessor(EntityType.RFO)).thenReturn(entityProcessor);
        when(entityProcessor.process(any())).thenReturn(true);

        // When
        ImportResultDTO result = dataImportServiceNew.importData(multipartFile, EntityType.RFO, "json");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalProcessed());
        assertEquals(1, result.getSuccessfulImports());
        assertNull(result.getErrorMessage());
        verify(dtoParserService, times(1)).parseJsonFile(multipartFile, EntityType.RFO);
        verify(entityProcessor, times(1)).process(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void importData_ShouldHandlePartialFailure_WhenSomeRecordsFail() throws Exception {
        // Given
        List<NucleusImportDTO> dtos = Arrays.asList(nucleusDTO1, nucleusDTO2);
        when(dtoParserService.parseCsvFile(multipartFile, EntityType.NUCLEUS_SERVICES)).thenReturn((List) dtos);
        when(processorFactory.getProcessor(EntityType.NUCLEUS_SERVICES)).thenReturn(entityProcessor);
        when(entityProcessor.process(nucleusDTO1)).thenReturn(true);
        when(entityProcessor.process(nucleusDTO2)).thenReturn(false);

        // When
        ImportResultDTO result = dataImportServiceNew.importData(multipartFile, EntityType.NUCLEUS_SERVICES, "csv");

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalProcessed());
        assertEquals(1, result.getSuccessfulImports());
        assertNotNull(result.getErrorMessage());
        assertTrue(result.getErrorMessage().contains("1 exitosos"));
        assertTrue(result.getErrorMessage().contains("1 fallidos"));
        verify(entityProcessor, times(2)).process(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void importData_ShouldHandleProcessorException_WhenProcessingThrowsException() throws Exception {
        // Given
        List<NucleusImportDTO> dtos = Arrays.asList(nucleusDTO1, nucleusDTO2);
        when(dtoParserService.parseCsvFile(multipartFile, EntityType.NUCLEUS_SERVICES)).thenReturn((List) dtos);
        when(processorFactory.getProcessor(EntityType.NUCLEUS_SERVICES)).thenReturn(entityProcessor);
        when(entityProcessor.process(nucleusDTO1)).thenReturn(true);
        when(entityProcessor.process(nucleusDTO2)).thenThrow(new RuntimeException("Processing error"));

        // When
        ImportResultDTO result = dataImportServiceNew.importData(multipartFile, EntityType.NUCLEUS_SERVICES, "csv");

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalProcessed());
        assertEquals(1, result.getSuccessfulImports());
        assertNotNull(result.getErrorMessage());
        assertTrue(result.getErrorMessage().contains("1 exitosos"));
        assertTrue(result.getErrorMessage().contains("1 fallidos"));
    }

    @Test
    void importData_ShouldReturnError_WhenUnsupportedFormatProvided() {
        // When
        ImportResultDTO result = dataImportServiceNew.importData(multipartFile, EntityType.NUCLEUS_SERVICES, "xml");

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalProcessed());
        assertEquals(0, result.getSuccessfulImports());
        assertNotNull(result.getErrorMessage());
        assertTrue(result.getErrorMessage().contains("Error durante la importación"));
        verifyNoInteractions(dtoParserService);
    }

    @Test
    void importData_ShouldReturnError_WhenParserThrowsException() throws Exception {
        // Given
        when(dtoParserService.parseCsvFile(multipartFile, EntityType.NUCLEUS_SERVICES))
            .thenThrow(new RuntimeException("File parsing error"));

        // When
        ImportResultDTO result = dataImportServiceNew.importData(multipartFile, EntityType.NUCLEUS_SERVICES, "csv");

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalProcessed());
        assertEquals(0, result.getSuccessfulImports());
        assertNotNull(result.getErrorMessage());
        assertTrue(result.getErrorMessage().contains("Error durante la importación"));
        verify(entityProcessor, never()).process(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void importData_ShouldHandleEmptyFile_WhenNoRecordsToParse() throws Exception {
        // Given
        List<?> emptyList = Collections.emptyList();
        when(dtoParserService.parseCsvFile(multipartFile, EntityType.NUCLEUS_SERVICES)).thenReturn((List) emptyList);
        when(processorFactory.getProcessor(EntityType.NUCLEUS_SERVICES)).thenReturn(entityProcessor);

        // When
        ImportResultDTO result = dataImportServiceNew.importData(multipartFile, EntityType.NUCLEUS_SERVICES, "csv");

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalProcessed());
        assertEquals(0, result.getSuccessfulImports());
        assertNull(result.getErrorMessage());
        verify(entityProcessor, never()).process(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void importData_ShouldClearEntityManager_BeforeProcessingEachRecord() throws Exception {
        // Given
        List<NucleusImportDTO> dtos = Arrays.asList(nucleusDTO1, nucleusDTO2);
        when(dtoParserService.parseCsvFile(multipartFile, EntityType.NUCLEUS_SERVICES)).thenReturn((List) dtos);
        when(processorFactory.getProcessor(EntityType.NUCLEUS_SERVICES)).thenReturn(entityProcessor);
        when(entityProcessor.process(any())).thenReturn(true);

        // When
        dataImportServiceNew.importData(multipartFile, EntityType.NUCLEUS_SERVICES, "csv");

        // Then
        verify(entityManager, times(2)).clear();
    }

    @Test
    @SuppressWarnings("unchecked")
    void importData_ShouldHandleAllFailures_WhenAllRecordsFail() throws Exception {
        // Given
        List<NucleusImportDTO> dtos = Arrays.asList(nucleusDTO1, nucleusDTO2);
        when(dtoParserService.parseCsvFile(multipartFile, EntityType.NUCLEUS_SERVICES)).thenReturn((List) dtos);
        when(processorFactory.getProcessor(EntityType.NUCLEUS_SERVICES)).thenReturn(entityProcessor);
        when(entityProcessor.process(any())).thenReturn(false);

        // When
        ImportResultDTO result = dataImportServiceNew.importData(multipartFile, EntityType.NUCLEUS_SERVICES, "csv");

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalProcessed());
        assertEquals(0, result.getSuccessfulImports());
        assertNotNull(result.getErrorMessage());
        assertTrue(result.getErrorMessage().contains("0 exitosos"));
        assertTrue(result.getErrorMessage().contains("2 fallidos"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void importData_ShouldUseCaseInsensitiveFormatCheck() throws Exception {
        // Given
        List<NucleusImportDTO> dtos = Collections.singletonList(nucleusDTO1);
        when(dtoParserService.parseCsvFile(multipartFile, EntityType.NUCLEUS_SERVICES)).thenReturn((List) dtos);
        when(processorFactory.getProcessor(EntityType.NUCLEUS_SERVICES)).thenReturn(entityProcessor);
        when(entityProcessor.process(any())).thenReturn(true);

        // When - usando "CSV" en mayúsculas
        ImportResultDTO result = dataImportServiceNew.importData(multipartFile, EntityType.NUCLEUS_SERVICES, "CSV");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getSuccessfulImports());
        verify(dtoParserService, times(1)).parseCsvFile(multipartFile, EntityType.NUCLEUS_SERVICES);
    }

    @Test
    @SuppressWarnings("unchecked")
    void importData_ShouldProcessRfoWithNucleusValidation() throws Exception {
        // Given
        List<RfoImportDTO> dtos = Collections.singletonList(rfoDTO1);
        when(dtoParserService.parseCsvFile(multipartFile, EntityType.RFO)).thenReturn((List) dtos);
        when(processorFactory.getProcessor(EntityType.RFO)).thenReturn(entityProcessor);
        when(entityProcessor.process(rfoDTO1)).thenReturn(true);

        // When
        ImportResultDTO result = dataImportServiceNew.importData(multipartFile, EntityType.RFO, "csv");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalProcessed());
        assertEquals(1, result.getSuccessfulImports());
        verify(entityProcessor, times(1)).process(rfoDTO1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void importData_ShouldAggregateErrorMessages_WhenMultipleRecordsFail() throws Exception {
        // Given
        List<NucleusImportDTO> dtos = Arrays.asList(nucleusDTO1, nucleusDTO2);
        when(dtoParserService.parseCsvFile(multipartFile, EntityType.NUCLEUS_SERVICES)).thenReturn((List) dtos);
        when(processorFactory.getProcessor(EntityType.NUCLEUS_SERVICES)).thenReturn(entityProcessor);
        when(entityProcessor.process(any())).thenReturn(false);

        // When
        ImportResultDTO result = dataImportServiceNew.importData(multipartFile, EntityType.NUCLEUS_SERVICES, "csv");

        // Then
        assertNotNull(result);
        assertNotNull(result.getErrorMessage());
        assertTrue(result.getErrorMessage().contains("Error procesando registro"));
    }
}
