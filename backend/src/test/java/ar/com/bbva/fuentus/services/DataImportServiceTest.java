package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.ImportResultDTO;
import ar.com.bbva.fuentus.dto.NucleusImportDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.enums.EntityType;
import ar.com.bbva.fuentus.enums.FileType;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataImportServiceTest {

    @Mock
    private NucleusRepository nucleusRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private DataImportService dataImportService;

    private MockMultipartFile jsonFile;
    private MockMultipartFile csvFile;

    @BeforeEach
    void setUp() {
        String jsonContent = "[{\"serviceN1\":\"Service1\",\"serviceN2\":\"SN2_1\"}]";
        jsonFile = new MockMultipartFile(
            "file",
            "test.json",
            "application/json",
            jsonContent.getBytes()
        );

        String csvContent = "Service_N1,Service_N2\nService1,SN2_1";
        csvFile = new MockMultipartFile(
            "file",
            "test.csv",
            "text/csv",
            csvContent.getBytes()
        );
    }

    @Test
    void importData_ShouldThrowException_WhenEntityTypeIsNotNucleus() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            dataImportService.importData(jsonFile, EntityType.APPS, FileType.JSON)
        );
    }

    @Test
    void importData_ShouldThrowException_WhenFileTypeIsNotSupported() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            dataImportService.importData(jsonFile, EntityType.NUCLEUS_SERVICES, null)
        );
    }

    @Test
    void importData_ShouldThrowException_WhenFileIsEmpty() {
        // Given
        MockMultipartFile emptyFile = new MockMultipartFile(
            "file",
            "empty.json",
            "application/json",
            new byte[0]
        );

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            dataImportService.importData(emptyFile, EntityType.NUCLEUS_SERVICES, FileType.JSON)
        );
    }

    @Test
    void importData_ShouldProcessJsonFile_WhenValidDataProvided() throws Exception {
        // Given
        Nucleus nucleus = new Nucleus();
        nucleus.setId(1L);
        
        when(entityManager.createNativeQuery(anyString(), eq(Nucleus.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());
        when(nucleusRepository.saveAndFlush(any(Nucleus.class))).thenReturn(nucleus);

        // When
        ImportResultDTO result = dataImportService.importData(
            jsonFile, 
            EntityType.NUCLEUS_SERVICES, 
            FileType.JSON
        );

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalProcessed());
        assertEquals(1, result.getSuccessfulImports());
        verify(nucleusRepository, times(1)).saveAndFlush(any(Nucleus.class));
    }



    @Test
    void importData_ShouldUpdateExistingRecord_WhenRecordExists() throws Exception {
        // Given
        Nucleus existingNucleus = new Nucleus();
        existingNucleus.setId(1L);
        existingNucleus.setServiceN1("Service1");
        existingNucleus.setServiceN2("SN2_1");
        
        List<Nucleus> existingList = new ArrayList<>();
        existingList.add(existingNucleus);
        
        when(entityManager.createNativeQuery(anyString(), eq(Nucleus.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(existingList);
        when(nucleusRepository.saveAndFlush(any(Nucleus.class))).thenReturn(existingNucleus);

        // When
        ImportResultDTO result = dataImportService.importData(
            jsonFile, 
            EntityType.NUCLEUS_SERVICES, 
            FileType.JSON
        );

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalProcessed());
        assertEquals(1, result.getSuccessfulImports());
        verify(nucleusRepository, times(1)).saveAndFlush(any(Nucleus.class));
    }

    @Test
    void importData_ShouldSkipRecord_WhenServiceN1IsEmpty() throws Exception {
        // Given
        String jsonContent = "[{\"serviceN1\":\"\",\"serviceN2\":\"SN2_1\"}]";
        MockMultipartFile invalidFile = new MockMultipartFile(
            "file",
            "test.json",
            "application/json",
            jsonContent.getBytes()
        );

        // When
        ImportResultDTO result = dataImportService.importData(
            invalidFile, 
            EntityType.NUCLEUS_SERVICES, 
            FileType.JSON
        );

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalProcessed());
        assertEquals(0, result.getSuccessfulImports());
        verify(nucleusRepository, never()).saveAndFlush(any(Nucleus.class));
    }

    @Test
    void importData_ShouldSkipRecord_WhenServiceN2IsEmpty() throws Exception {
        // Given
        String jsonContent = "[{\"serviceN1\":\"Service1\",\"serviceN2\":\"\"}]";
        MockMultipartFile invalidFile = new MockMultipartFile(
            "file",
            "test.json",
            "application/json",
            jsonContent.getBytes()
        );

        // When
        ImportResultDTO result = dataImportService.importData(
            invalidFile, 
            EntityType.NUCLEUS_SERVICES, 
            FileType.JSON
        );

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalProcessed());
        assertEquals(0, result.getSuccessfulImports());
        verify(nucleusRepository, never()).saveAndFlush(any(Nucleus.class));
    }

    @Test
    void importData_ShouldSkipRecord_WhenServiceN1IsNull() throws Exception {
        // Given
        String jsonContent = "[{\"serviceN2\":\"SN2_1\"}]";
        MockMultipartFile invalidFile = new MockMultipartFile(
            "file",
            "test.json",
            "application/json",
            jsonContent.getBytes()
        );

        // When
        ImportResultDTO result = dataImportService.importData(
            invalidFile, 
            EntityType.NUCLEUS_SERVICES, 
            FileType.JSON
        );

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalProcessed());
        assertEquals(0, result.getSuccessfulImports());
        verify(nucleusRepository, never()).saveAndFlush(any(Nucleus.class));
    }

    @Test
    void importData_ShouldContinueOnError_WhenOneRecordFails() throws Exception {
        // Given
        String jsonContent = "[{\"serviceN1\":\"Service1\",\"serviceN2\":\"SN2_1\"}," +
                           "{\"serviceN1\":\"Service2\",\"serviceN2\":\"SN2_2\"}]";
        MockMultipartFile multiFile = new MockMultipartFile(
            "file",
            "test.json",
            "application/json",
            jsonContent.getBytes()
        );
        
        Nucleus nucleus = new Nucleus();
        nucleus.setId(1L);
        
        when(entityManager.createNativeQuery(anyString(), eq(Nucleus.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());
        when(nucleusRepository.saveAndFlush(any(Nucleus.class)))
            .thenThrow(new RuntimeException("Database error"))
            .thenReturn(nucleus);

        // When
        ImportResultDTO result = dataImportService.importData(
            multiFile, 
            EntityType.NUCLEUS_SERVICES, 
            FileType.JSON
        );

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalProcessed());
        assertEquals(1, result.getSuccessfulImports());
        verify(nucleusRepository, times(2)).saveAndFlush(any(Nucleus.class));
        verify(entityManager, times(2)).clear();
    }

    @Test
    void importData_ShouldThrowException_WhenNoDataInFile() {
        // Given
        String jsonContent = "[]";
        MockMultipartFile emptyDataFile = new MockMultipartFile(
            "file",
            "test.json",
            "application/json",
            jsonContent.getBytes()
        );

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            dataImportService.importData(
                emptyDataFile, 
                EntityType.NUCLEUS_SERVICES, 
                FileType.JSON
            )
        );
    }

    @Test
    void importData_ShouldThrowException_WhenInvalidJson() {
        // Given
        String jsonContent = "{invalid json}";
        MockMultipartFile invalidFile = new MockMultipartFile(
            "file",
            "test.json",
            "application/json",
            jsonContent.getBytes()
        );

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            dataImportService.importData(
                invalidFile, 
                EntityType.NUCLEUS_SERVICES, 
                FileType.JSON
            )
        );
    }

    @Test
    void getSupportedEntityTypes_ShouldReturnAllEntityTypes() {
        // When
        EntityType[] result = dataImportService.getSupportedEntityTypes();

        // Then
        assertNotNull(result);
        assertTrue(result.length > 0);
        assertEquals(EntityType.values().length, result.length);
    }

    @Test
    void getSupportedFileTypes_ShouldReturnAllFileTypes() {
        // When
        FileType[] result = dataImportService.getSupportedFileTypes();

        // Then
        assertNotNull(result);
        assertTrue(result.length > 0);
        assertEquals(FileType.values().length, result.length);
    }

    @Test
    void importData_ShouldHandleWhitespaceInServiceN1() throws Exception {
        // Given
        String jsonContent = "[{\"serviceN1\":\"   \",\"serviceN2\":\"SN2_1\"}]";
        MockMultipartFile invalidFile = new MockMultipartFile(
            "file",
            "test.json",
            "application/json",
            jsonContent.getBytes()
        );

        // When
        ImportResultDTO result = dataImportService.importData(
            invalidFile, 
            EntityType.NUCLEUS_SERVICES, 
            FileType.JSON
        );

        // Then
        assertEquals(0, result.getSuccessfulImports());
        verify(nucleusRepository, never()).saveAndFlush(any(Nucleus.class));
    }

    @Test
    void importData_ShouldCallEntityManagerClear_AfterEachRecord() throws Exception {
        // Given
        Nucleus nucleus = new Nucleus();
        nucleus.setId(1L);
        
        when(entityManager.createNativeQuery(anyString(), eq(Nucleus.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());
        when(nucleusRepository.saveAndFlush(any(Nucleus.class))).thenReturn(nucleus);

        // When
        dataImportService.importData(jsonFile, EntityType.NUCLEUS_SERVICES, FileType.JSON);

        // Then
        verify(entityManager, atLeastOnce()).clear();
    }

    @Test
    void importData_ShouldMapAllFieldsCorrectly() throws Exception {
        // Given
        String jsonContent = "[{" +
            "\"serviceN1\":\"Service1\"," +
            "\"serviceN2\":\"SN2_1\"," +
            "\"serviceN2description\":\"Description\"," +
            "\"area\":\"IT\"," +
            "\"uuaa\":\"1234\"" +
            "}]";
        MockMultipartFile detailedFile = new MockMultipartFile(
            "file",
            "test.json",
            "application/json",
            jsonContent.getBytes()
        );
        
        Nucleus nucleus = new Nucleus();
        nucleus.setId(1L);
        
        when(entityManager.createNativeQuery(anyString(), eq(Nucleus.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());
        when(nucleusRepository.saveAndFlush(any(Nucleus.class))).thenReturn(nucleus);

        // When
        ImportResultDTO result = dataImportService.importData(
            detailedFile, 
            EntityType.NUCLEUS_SERVICES, 
            FileType.JSON
        );

        // Then
        assertEquals(1, result.getSuccessfulImports());
        verify(nucleusRepository).saveAndFlush(argThat(n -> 
            "Service1".equals(n.getServiceN1()) &&
            "SN2_1".equals(n.getServiceN2()) &&
            "Description".equals(n.getServiceN2description()) &&
            "IT".equals(n.getArea()) &&
            "1234".equals(n.getUuaa())
        ));
    }
}
