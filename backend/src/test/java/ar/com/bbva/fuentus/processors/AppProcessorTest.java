package ar.com.bbva.fuentus.processors;

import ar.com.bbva.fuentus.dto.AppImportDTO;
import ar.com.bbva.fuentus.entities.App;
import ar.com.bbva.fuentus.mappers.DataImportMapper;
import ar.com.bbva.fuentus.repositories.AppsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppProcessorTest {

    @Mock
    private AppsRepository appsRepository;

    @Mock
    private DataImportMapper dataImportMapper;

    @InjectMocks
    private AppProcessor appProcessor;

    private AppImportDTO validDTO;
    private App newApp;
    private App existingApp;

    @BeforeEach
    void setUp() {
        // Setup valid DTO
        validDTO = new AppImportDTO();
        validDTO.setName("Test App");
        validDTO.setProjectId("PROJ-123");
        validDTO.setVertical("Payments");
        validDTO.setFolder("apps/test-app");
        validDTO.setBitbucketUrl("https://bitbucket.org/test/repo");
        validDTO.setUuaa("1234");
        validDTO.setJava(true);
        validDTO.setMonolith(false);
        validDTO.setSonarUrl("https://sonar.test/project");

        // Setup new app (to be created)
        newApp = new App();
        newApp.setName("Test App");
        newApp.setProjectId("PROJ-123");
        newApp.setVertical("Payments");
        newApp.setFolder("apps/test-app");
        newApp.setJava(true);

        // Setup existing app (to be updated)
        existingApp = new App();
        existingApp.setId(1L);
        existingApp.setName("Test App");
        existingApp.setProjectId("PROJ-123");
        existingApp.setVertical("Old Vertical");
        existingApp.setJava(false);
    }

    @Test
    void process_ShouldThrowException_WhenDTOIsNotAppImportDTO() {
        // Given
        Object invalidDTO = new Object();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> appProcessor.process(invalidDTO));
        assertTrue(exception.getMessage().contains("Expected AppImportDTO"));
        verify(appsRepository, never()).save(any());
    }

    @Test
    void process_ShouldThrowException_WhenDTOIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> appProcessor.process(null));
        assertTrue(exception.getMessage().contains("Expected AppImportDTO"));
        verify(appsRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenNameIsNull() {
        // Given
        validDTO.setName(null);

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(appsRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenNameIsEmpty() {
        // Given
        validDTO.setName("");

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(appsRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenNameIsBlank() {
        // Given
        validDTO.setName("   ");

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(appsRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenProjectIdIsNull() {
        // Given
        validDTO.setProjectId(null);

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(appsRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenProjectIdIsEmpty() {
        // Given
        validDTO.setProjectId("");

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(appsRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenProjectIdIsBlank() {
        // Given
        validDTO.setProjectId("   ");

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(appsRepository, never()).save(any());
    }

    @Test
    void process_ShouldCreateNewApp_WhenNotExists() {
        // Given
        when(appsRepository.findByNameAndProjectId("Test App", "PROJ-123"))
            .thenReturn(null);
        when(dataImportMapper.toApp(validDTO)).thenReturn(newApp);
        when(appsRepository.save(any(App.class))).thenAnswer(invocation -> {
            App app = invocation.getArgument(0);
            app.setId(10L);
            return app;
        });

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(appsRepository).findByNameAndProjectId("Test App", "PROJ-123");
        verify(dataImportMapper).toApp(validDTO);
        verify(dataImportMapper, never()).updateAppFromDTO(any(), any());
        verify(appsRepository).save(newApp);
    }

    @Test
    void process_ShouldUpdateExistingApp_WhenExists() {
        // Given
        when(appsRepository.findByNameAndProjectId("Test App", "PROJ-123"))
            .thenReturn(existingApp);
        when(appsRepository.save(any(App.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(appsRepository).findByNameAndProjectId("Test App", "PROJ-123");
        verify(dataImportMapper).updateAppFromDTO(validDTO, existingApp);
        verify(dataImportMapper, never()).toApp(any());
        verify(appsRepository).save(existingApp);
    }

    @Test
    void process_ShouldReturnFalse_WhenDataIntegrityViolation() {
        // Given
        when(appsRepository.findByNameAndProjectId(anyString(), anyString()))
            .thenReturn(null);
        when(dataImportMapper.toApp(validDTO)).thenReturn(newApp);
        when(appsRepository.save(any(App.class)))
            .thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(appsRepository).save(newApp);
    }

    @Test
    void process_ShouldReturnFalse_WhenMapperThrowsIllegalArgumentException() {
        // Given
        when(appsRepository.findByNameAndProjectId(anyString(), anyString()))
            .thenReturn(null);
        when(dataImportMapper.toApp(validDTO))
            .thenThrow(new IllegalArgumentException("Invalid data"));

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(appsRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenUnexpectedExceptionOccurs() {
        // Given
        when(appsRepository.findByNameAndProjectId(anyString(), anyString()))
            .thenThrow(new RuntimeException("Database error"));

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(appsRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenMappedAppNameIsNull() {
        // Given
        App invalidApp = new App();
        invalidApp.setName(null);
        invalidApp.setProjectId("PROJ-123");

        when(appsRepository.findByNameAndProjectId(anyString(), anyString()))
            .thenReturn(null);
        when(dataImportMapper.toApp(validDTO)).thenReturn(invalidApp);

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(appsRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenMappedAppNameIsEmpty() {
        // Given
        App invalidApp = new App();
        invalidApp.setName("");
        invalidApp.setProjectId("PROJ-123");

        when(appsRepository.findByNameAndProjectId(anyString(), anyString()))
            .thenReturn(null);
        when(dataImportMapper.toApp(validDTO)).thenReturn(invalidApp);

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(appsRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenMappedAppNameIsBlank() {
        // Given
        App invalidApp = new App();
        invalidApp.setName("   ");
        invalidApp.setProjectId("PROJ-123");

        when(appsRepository.findByNameAndProjectId(anyString(), anyString()))
            .thenReturn(null);
        when(dataImportMapper.toApp(validDTO)).thenReturn(invalidApp);

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(appsRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenMappedAppProjectIdIsNull() {
        // Given
        App invalidApp = new App();
        invalidApp.setName("Test App");
        invalidApp.setProjectId(null);

        when(appsRepository.findByNameAndProjectId(anyString(), anyString()))
            .thenReturn(null);
        when(dataImportMapper.toApp(validDTO)).thenReturn(invalidApp);

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(appsRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenMappedAppProjectIdIsEmpty() {
        // Given
        App invalidApp = new App();
        invalidApp.setName("Test App");
        invalidApp.setProjectId("");

        when(appsRepository.findByNameAndProjectId(anyString(), anyString()))
            .thenReturn(null);
        when(dataImportMapper.toApp(validDTO)).thenReturn(invalidApp);

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(appsRepository, never()).save(any());
    }

    @Test
    void process_ShouldReturnFalse_WhenMappedAppProjectIdIsBlank() {
        // Given
        App invalidApp = new App();
        invalidApp.setName("Test App");
        invalidApp.setProjectId("   ");

        when(appsRepository.findByNameAndProjectId(anyString(), anyString()))
            .thenReturn(null);
        when(dataImportMapper.toApp(validDTO)).thenReturn(invalidApp);

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(appsRepository, never()).save(any());
    }

    @Test
    void process_ShouldSucceed_WhenAllFieldsAreValid() {
        // Given
        when(appsRepository.findByNameAndProjectId("Test App", "PROJ-123"))
            .thenReturn(null);
        when(dataImportMapper.toApp(validDTO)).thenReturn(newApp);
        when(appsRepository.save(any(App.class))).thenAnswer(invocation -> {
            App app = invocation.getArgument(0);
            app.setId(10L);
            return app;
        });

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(appsRepository).save(newApp);
        assertEquals(10L, newApp.getId());
    }

    @Test
    void process_ShouldCallUpdateMapper_WhenAppExists() {
        // Given
        when(appsRepository.findByNameAndProjectId("Test App", "PROJ-123"))
            .thenReturn(existingApp);
        doNothing().when(dataImportMapper).updateAppFromDTO(validDTO, existingApp);
        when(appsRepository.save(existingApp)).thenReturn(existingApp);

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(dataImportMapper).updateAppFromDTO(validDTO, existingApp);
        verify(appsRepository).save(existingApp);
    }

    @Test
    void process_ShouldReturnFalse_WhenUpdateMapperThrowsIllegalArgumentException() {
        // Given
        when(appsRepository.findByNameAndProjectId("Test App", "PROJ-123"))
            .thenReturn(existingApp);
        doThrow(new IllegalArgumentException("Cannot change name"))
            .when(dataImportMapper).updateAppFromDTO(validDTO, existingApp);

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertFalse(result);
        verify(appsRepository, never()).save(any());
    }

    @Test
    void process_ShouldHandleOptionalFields() {
        // Given
        validDTO.setVertical(null);
        validDTO.setFolder(null);
        validDTO.setBitbucketUrl(null);
        validDTO.setUuaa(null);
        
        App minimalApp = new App();
        minimalApp.setName("Test App");
        minimalApp.setProjectId("PROJ-123");

        when(appsRepository.findByNameAndProjectId("Test App", "PROJ-123"))
            .thenReturn(null);
        when(dataImportMapper.toApp(validDTO)).thenReturn(minimalApp);
        when(appsRepository.save(any(App.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        boolean result = appProcessor.process(validDTO);

        // Then
        assertTrue(result);
        verify(appsRepository).save(minimalApp);
    }
}
