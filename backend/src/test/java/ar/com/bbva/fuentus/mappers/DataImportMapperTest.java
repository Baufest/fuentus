package ar.com.bbva.fuentus.mappers;

import ar.com.bbva.fuentus.dto.AppImportDTO;
import ar.com.bbva.fuentus.dto.NucleusImportDTO;
import ar.com.bbva.fuentus.dto.RfoImportDTO;
import ar.com.bbva.fuentus.entities.App;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.Rfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DataImportMapperTest {

    @InjectMocks
    private DataImportMapper dataImportMapper;

    private NucleusImportDTO validNucleusDTO;
    private RfoImportDTO validRfoDTO;
    private AppImportDTO validAppDTO;

    @BeforeEach
    void setUp() {
        // Configurar Nucleus DTO válido
        validNucleusDTO = new NucleusImportDTO();
        validNucleusDTO.setIdFullservice(100L);
        validNucleusDTO.setServiceN1("SERVICE N1 TEST");
        validNucleusDTO.setServiceN2("SN2_TEST");
        validNucleusDTO.setOwnerServiceN1("Owner N1");
        validNucleusDTO.setOwnerServiceN2("Owner N2");
        validNucleusDTO.setArea("Test Area");
        validNucleusDTO.setUuaa("TEST");

        // Configurar RFO DTO válido
        validRfoDTO = new RfoImportDTO();
        validRfoDTO.setRfoId(1L);
        validRfoDTO.setSevicioN2("SN2_TEST");
        validRfoDTO.setEmail("juan.perez@test.com");
        validRfoDTO.setEstadoRfo("Activo");
        validRfoDTO.setFechaPuestaProduccion("2025-01-15");

        // Configurar App DTO válido
        validAppDTO = new AppImportDTO();
        validAppDTO.setName("Test App");
        validAppDTO.setProjectId("PROJ-123");
        validAppDTO.setVertical("Vertical Test");
        validAppDTO.setFolder("test-folder");
        validAppDTO.setBitbucketUrl("https://bitbucket.org/test/repo");
        validAppDTO.setSonarUrl("https://sonar.test/project");
        validAppDTO.setSonar9Url("https://sonar9.test/project");
        validAppDTO.setSonar10Url("https://sonar10.test/project");
        validAppDTO.setSamuelUrl("https://samuel.test/app");
        validAppDTO.setChimeraUrl("https://chimera.test/app");
        validAppDTO.setObsolete(false);
        validAppDTO.setObsoleteBy("N/A");
        validAppDTO.setUuaa("UUAA_TEST");
        validAppDTO.setSn1("SN1_TEST");
        validAppDTO.setSn2("SN2_TEST");
        validAppDTO.setSo("SO_TEST");
        validAppDTO.setAo("AO_TEST");
        validAppDTO.setStratosUol1("UOL1_TEST");
        validAppDTO.setStratosUol2("UOL2_TEST");
        validAppDTO.setStratosSl1("SL1_TEST");
        validAppDTO.setStratosSl1Owner("Owner SL1");
        validAppDTO.setStratosSl2("SL2_TEST");
        validAppDTO.setStratosSl2Owner("Owner SL2");
        validAppDTO.setJava(true);
        validAppDTO.setUnitTest(true);
        validAppDTO.setJest(false);
        validAppDTO.setMonolith(false);
        validAppDTO.setConfig(true);
        validAppDTO.setSonarKey("test-sonar-key");
        validAppDTO.setVtravk("VTRAVK_TEST");
        validAppDTO.setCriticalLocal(5);
        validAppDTO.setNodeVersion("16.0.0");
        validAppDTO.setCriticalAudit(true);
        validAppDTO.setCriticalConfidential(false);
        validAppDTO.setCriticalFraud(true);
        validAppDTO.setCriticalCfs(false);
        validAppDTO.setExtra("Extra info");
        validAppDTO.setArchitecture("Microservices");
        validAppDTO.setSource("Git");
    }

    // ===== NUCLEUS MAPPING TESTS =====

    @Test
    void toNucleus_ShouldMapAllFields_WhenValidDTOProvided() {
        // When
        Nucleus result = dataImportMapper.toNucleus(validNucleusDTO);

        // Then
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("SERVICE N1 TEST", result.getServiceN1());
        assertEquals("SN2_TEST", result.getServiceN2());
        assertEquals("Owner N1", result.getOwnerServiceN1());
        assertEquals("Owner N2", result.getOwnerServiceN2());
        assertEquals("Test Area", result.getArea());
        assertEquals("TEST", result.getUuaa());
    }

    @Test
    void toNucleus_ShouldReturnNull_WhenDTOIsNull() {
        // When
        Nucleus result = dataImportMapper.toNucleus(null);

        // Then
        assertNull(result);
    }

    @Test
    void toNucleus_ShouldThrowException_WhenIdFullserviceIsNull() {
        // Given
        validNucleusDTO.setIdFullservice(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> dataImportMapper.toNucleus(validNucleusDTO));
        assertTrue(exception.getMessage().contains("obligatorio"));
    }

    @Test
    void toNucleus_ShouldThrowException_WhenIdFullserviceIsNegative() {
        // Given
        validNucleusDTO.setIdFullservice(-1L);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> dataImportMapper.toNucleus(validNucleusDTO));
        assertTrue(exception.getMessage().contains("positivo"));
    }

    @Test
    void toNucleus_ShouldThrowException_WhenIdFullserviceIsZero() {
        // Given
        validNucleusDTO.setIdFullservice(0L);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> dataImportMapper.toNucleus(validNucleusDTO));
        assertTrue(exception.getMessage().contains("positivo"));
    }

    @Test
    void updateNucleusFromDTO_ShouldUpdateAllFieldsExceptId() {
        // Given
        Nucleus existingNucleus = new Nucleus();
        existingNucleus.setId(100L);
        existingNucleus.setServiceN1("OLD SERVICE N1");
        existingNucleus.setServiceN2("OLD_SN2");

        validNucleusDTO.setServiceN1("NEW SERVICE N1");
        validNucleusDTO.setServiceN2("NEW_SN2");

        // When
        dataImportMapper.updateNucleusFromDTO(validNucleusDTO, existingNucleus);

        // Then
        assertEquals(100L, existingNucleus.getId()); // ID should not change
        assertEquals("NEW SERVICE N1", existingNucleus.getServiceN1());
        assertEquals("NEW_SN2", existingNucleus.getServiceN2());
    }

    @Test
    void updateNucleusFromDTO_ShouldDoNothing_WhenDTOIsNull() {
        // Given
        Nucleus existingNucleus = new Nucleus();
        existingNucleus.setId(100L);
        existingNucleus.setServiceN1("SERVICE N1");

        String originalServiceN1 = existingNucleus.getServiceN1();

        // When
        dataImportMapper.updateNucleusFromDTO(null, existingNucleus);

        // Then
        assertEquals(originalServiceN1, existingNucleus.getServiceN1());
    }

    @Test
    void updateNucleusFromDTO_ShouldDoNothing_WhenEntityIsNull() {
        // When & Then
        assertDoesNotThrow(() -> dataImportMapper.updateNucleusFromDTO(validNucleusDTO, null));
    }

    @Test
    void updateNucleusFromDTO_ShouldThrowException_WhenIdFullserviceIsNull() {
        // Given
        Nucleus existingNucleus = new Nucleus();
        existingNucleus.setId(100L);
        validNucleusDTO.setIdFullservice(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> dataImportMapper.updateNucleusFromDTO(validNucleusDTO, existingNucleus));
        assertTrue(exception.getMessage().contains("obligatorio"));
    }

    @Test
    void updateNucleusFromDTO_ShouldThrowException_WhenIdMismatch() {
        // Given
        Nucleus existingNucleus = new Nucleus();
        existingNucleus.setId(100L);
        validNucleusDTO.setIdFullservice(200L); // Different ID

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> dataImportMapper.updateNucleusFromDTO(validNucleusDTO, existingNucleus));
        assertTrue(exception.getMessage().contains("cambiar"));
    }

    // ===== RFO MAPPING TESTS =====

    @Test
    void toRfo_ShouldMapAllFields_WhenValidDTOProvided() {
        // When
        Rfo result = dataImportMapper.toRfo(validRfoDTO);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getRfoId());
        assertEquals("juan.perez@test.com", result.getEmail());
        assertEquals("Activo", result.getEstadoRfo());
        assertEquals("2025-01-15", result.getFechaPuestaProduccion());
        assertNull(result.getNucleus()); // Nucleus es seteado por el processor
    }

    @Test
    void toRfo_ShouldReturnNull_WhenDTOIsNull() {
        // When
        Rfo result = dataImportMapper.toRfo(null);

        // Then
        assertNull(result);
    }

    @Test
    void toRfo_ShouldThrowException_WhenRfoIdIsNull() {
        // Given
        validRfoDTO.setRfoId(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> dataImportMapper.toRfo(validRfoDTO));
        assertTrue(exception.getMessage().contains("obligatorio"));
    }

    @Test
    void toRfo_ShouldThrowException_WhenRfoIdIsNegative() {
        // Given
        validRfoDTO.setRfoId(-1L);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> dataImportMapper.toRfo(validRfoDTO));
        assertTrue(exception.getMessage().contains("positivo"));
    }

    @Test
    void toRfo_ShouldThrowException_WhenRfoIdIsZero() {
        // Given
        validRfoDTO.setRfoId(0L);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> dataImportMapper.toRfo(validRfoDTO));
        assertTrue(exception.getMessage().contains("positivo"));
    }

    @Test
    void updateRfoFromDTO_ShouldUpdateAllFieldsExceptId() {
        // Given
        Rfo existingRfo = new Rfo();
        existingRfo.setRfoId(1L);
        existingRfo.setEmail("old.email@test.com");
        existingRfo.setEstadoRfo("OLD STATUS");
        existingRfo.setFechaPuestaProduccion("2024-01-01");

        validRfoDTO.setEmail("new.email@test.com");
        validRfoDTO.setEstadoRfo("NEW STATUS");
        validRfoDTO.setFechaPuestaProduccion("2025-01-15");

        // When
        dataImportMapper.updateRfoFromDTO(validRfoDTO, existingRfo);

        // Then
        assertEquals(1L, existingRfo.getRfoId()); // ID should not change
        assertEquals("new.email@test.com", existingRfo.getEmail());
        assertEquals("NEW STATUS", existingRfo.getEstadoRfo());
        assertEquals("2025-01-15", existingRfo.getFechaPuestaProduccion());
    }

    @Test
    void updateRfoFromDTO_ShouldDoNothing_WhenDTOIsNull() {
        // Given
        Rfo existingRfo = new Rfo();
        existingRfo.setRfoId(1L);
        existingRfo.setEmail("original@test.com");

        String originalEmail = existingRfo.getEmail();

        // When
        dataImportMapper.updateRfoFromDTO(null, existingRfo);

        // Then
        assertEquals(originalEmail, existingRfo.getEmail());
    }

    @Test
    void updateRfoFromDTO_ShouldDoNothing_WhenEntityIsNull() {
        // When & Then
        assertDoesNotThrow(() -> dataImportMapper.updateRfoFromDTO(validRfoDTO, null));
    }

    @Test
    void updateRfoFromDTO_ShouldThrowException_WhenRfoIdIsNull() {
        // Given
        Rfo existingRfo = new Rfo();
        existingRfo.setRfoId(1L);
        validRfoDTO.setRfoId(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> dataImportMapper.updateRfoFromDTO(validRfoDTO, existingRfo));
        assertTrue(exception.getMessage().contains("obligatorio"));
    }

    @Test
    void updateRfoFromDTO_ShouldThrowException_WhenIdMismatch() {
        // Given
        Rfo existingRfo = new Rfo();
        existingRfo.setRfoId(1L);
        validRfoDTO.setRfoId(2L); // Different ID

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> dataImportMapper.updateRfoFromDTO(validRfoDTO, existingRfo));
        assertTrue(exception.getMessage().contains("cambiar"));
    }

    @Test
    void toRfo_ShouldHandleNullOptionalFields() {
        // Given
        validRfoDTO.setEmail(null);
        validRfoDTO.setEstadoRfo(null);

        // When
        Rfo result = dataImportMapper.toRfo(validRfoDTO);

        // Then
        assertNotNull(result);
        assertNull(result.getEmail());
        assertNull(result.getEstadoRfo());
        assertEquals(1L, result.getRfoId());
    }

    @Test
    void toNucleus_ShouldHandleNullOptionalFields() {
        // Given
        validNucleusDTO.setArea(null);
        validNucleusDTO.setOwnerServiceN1(null);

        // When
        Nucleus result = dataImportMapper.toNucleus(validNucleusDTO);

        // Then
        assertNotNull(result);
        assertNull(result.getArea());
        assertNull(result.getOwnerServiceN1());
        assertEquals(100L, result.getId());
    }

    @Test
    void updateNucleusFromDTO_ShouldUpdateToNullValues() {
        // Given
        Nucleus existingNucleus = new Nucleus();
        existingNucleus.setId(100L);
        existingNucleus.setServiceN1("SERVICE N1");
        existingNucleus.setServiceN2("SN2_TEST");
        existingNucleus.setArea("Old Area");
        existingNucleus.setOwnerServiceN1("Old Owner");

        validNucleusDTO.setArea(null);
        validNucleusDTO.setOwnerServiceN1(null);

        // When
        dataImportMapper.updateNucleusFromDTO(validNucleusDTO, existingNucleus);

        // Then
        assertNull(existingNucleus.getArea());
        assertNull(existingNucleus.getOwnerServiceN1());
    }

    @Test
    void updateRfoFromDTO_ShouldUpdateToNullValues() {
        // Given
        Rfo existingRfo = new Rfo();
        existingRfo.setRfoId(1L);
        existingRfo.setEmail("old@test.com");
        existingRfo.setEstadoRfo("Old Status");
        existingRfo.setFechaPuestaProduccion("2024-01-01");

        validRfoDTO.setEmail(null);
        validRfoDTO.setEstadoRfo(null);

        // When
        dataImportMapper.updateRfoFromDTO(validRfoDTO, existingRfo);

        // Then
        assertNull(existingRfo.getEmail());
        assertNull(existingRfo.getEstadoRfo());
    }

    // ===== APP MAPPING TESTS =====

    @Test
    void toApp_ShouldMapAllFields_WhenValidDTOProvided() {
        // When
        App result = dataImportMapper.toApp(validAppDTO);

        // Then
        assertNotNull(result);
        assertEquals("Test App", result.getName());
        assertEquals("PROJ-123", result.getProjectId());
        assertEquals("Vertical Test", result.getVertical());
        assertEquals("test-folder", result.getFolder());
        assertEquals("https://bitbucket.org/test/repo", result.getBitbucketUrl());
        assertEquals("https://sonar.test/project", result.getSonarUrl());
        assertEquals("https://sonar9.test/project", result.getSonar9Url());
        assertEquals("https://sonar10.test/project", result.getSonar10Url());
        assertEquals("https://samuel.test/app", result.getSamuelUrl());
        assertEquals("https://chimera.test/app", result.getChimeraUrl());
        assertEquals(false, result.getObsolete());
        assertEquals("N/A", result.getObsoleteBy());
        assertEquals("UUAA_TEST", result.getUuaa());
        assertEquals("SN1_TEST", result.getSn1());
        assertEquals("SN2_TEST", result.getSn2());
        assertEquals("SO_TEST", result.getSo());
        assertEquals("AO_TEST", result.getAo());
        assertEquals("UOL1_TEST", result.getStratosUol1());
        assertEquals("UOL2_TEST", result.getStratosUol2());
        assertEquals("SL1_TEST", result.getStratosSl1());
        assertEquals("Owner SL1", result.getStratosSl1Owner());
        assertEquals("SL2_TEST", result.getStratosSl2());
        assertEquals("Owner SL2", result.getStratosSl2Owner());
        assertEquals(true, result.getJava());
        assertEquals(true, result.getUnitTest());
        assertEquals(false, result.getJest());
        assertEquals(false, result.getMonolith());
        assertEquals(true, result.getConfig());
        assertEquals("test-sonar-key", result.getSonarKey());
        assertEquals("VTRAVK_TEST", result.getVtravk());
        assertEquals(5, result.getCriticalLocal());
        assertEquals("16.0.0", result.getNodeVersion());
        assertEquals(true, result.getCriticalAudit());
        assertEquals(false, result.getCriticalConfidential());
        assertEquals(true, result.getCriticalFraud());
        assertEquals(false, result.getCriticalCfs());
        assertEquals("Extra info", result.getExtra());
        assertEquals("Microservices", result.getArchitecture());
        assertEquals("Git", result.getSource());
    }

    @Test
    void toApp_ShouldReturnNull_WhenDTOIsNull() {
        // When
        App result = dataImportMapper.toApp(null);

        // Then
        assertNull(result);
    }

    @Test
    void toApp_ShouldThrowException_WhenNameIsNull() {
        // Given
        validAppDTO.setName(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> dataImportMapper.toApp(validAppDTO));
        assertTrue(exception.getMessage().contains("name es obligatorio"));
    }

    @Test
    void toApp_ShouldThrowException_WhenNameIsEmpty() {
        // Given
        validAppDTO.setName("");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> dataImportMapper.toApp(validAppDTO));
        assertTrue(exception.getMessage().contains("name es obligatorio"));
    }

    @Test
    void toApp_ShouldThrowException_WhenNameIsBlank() {
        // Given
        validAppDTO.setName("   ");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> dataImportMapper.toApp(validAppDTO));
        assertTrue(exception.getMessage().contains("name es obligatorio"));
    }

    @Test
    void toApp_ShouldThrowException_WhenProjectIdIsNull() {
        // Given
        validAppDTO.setProjectId(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> dataImportMapper.toApp(validAppDTO));
        assertTrue(exception.getMessage().contains("projectId es obligatorio"));
    }

    @Test
    void toApp_ShouldThrowException_WhenProjectIdIsEmpty() {
        // Given
        validAppDTO.setProjectId("");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> dataImportMapper.toApp(validAppDTO));
        assertTrue(exception.getMessage().contains("projectId es obligatorio"));
    }

    @Test
    void toApp_ShouldThrowException_WhenProjectIdIsBlank() {
        // Given
        validAppDTO.setProjectId("   ");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> dataImportMapper.toApp(validAppDTO));
        assertTrue(exception.getMessage().contains("projectId es obligatorio"));
    }

    @Test
    void toApp_ShouldHandleNullOptionalFields() {
        // Given
        validAppDTO.setVertical(null);
        validAppDTO.setFolder(null);
        validAppDTO.setBitbucketUrl(null);
        validAppDTO.setObsolete(null);
        validAppDTO.setJava(null);

        // When
        App result = dataImportMapper.toApp(validAppDTO);

        // Then
        assertNotNull(result);
        assertNull(result.getVertical());
        assertNull(result.getFolder());
        assertNull(result.getBitbucketUrl());
        assertNull(result.getObsolete());
        assertNull(result.getJava());
        assertEquals("Test App", result.getName());
        assertEquals("PROJ-123", result.getProjectId());
    }

    @Test
    void updateAppFromDTO_ShouldUpdateAllFieldsExceptIdNameAndProjectId() {
        // Given
        App existingApp = new App();
        existingApp.setId(1L);
        existingApp.setName("Test App");
        existingApp.setProjectId("PROJ-123");
        existingApp.setVertical("Old Vertical");
        existingApp.setFolder("old-folder");
        existingApp.setUuaa("OLD_UUAA");

        validAppDTO.setVertical("New Vertical");
        validAppDTO.setFolder("new-folder");
        validAppDTO.setUuaa("NEW_UUAA");

        // When
        dataImportMapper.updateAppFromDTO(validAppDTO, existingApp);

        // Then
        assertEquals(1L, existingApp.getId()); // ID should not change
        assertEquals("Test App", existingApp.getName()); // Name should not change
        assertEquals("PROJ-123", existingApp.getProjectId()); // ProjectId should not change
        assertEquals("New Vertical", existingApp.getVertical());
        assertEquals("new-folder", existingApp.getFolder());
        assertEquals("NEW_UUAA", existingApp.getUuaa());
    }

    @Test
    void updateAppFromDTO_ShouldDoNothing_WhenDTOIsNull() {
        // Given
        App existingApp = new App();
        existingApp.setId(1L);
        existingApp.setName("Test App");
        existingApp.setProjectId("PROJ-123");
        existingApp.setVertical("Original Vertical");

        String originalVertical = existingApp.getVertical();

        // When
        dataImportMapper.updateAppFromDTO(null, existingApp);

        // Then
        assertEquals(originalVertical, existingApp.getVertical());
    }

    @Test
    void updateAppFromDTO_ShouldDoNothing_WhenEntityIsNull() {
        // When & Then
        assertDoesNotThrow(() -> dataImportMapper.updateAppFromDTO(validAppDTO, null));
    }

    @Test
    void updateAppFromDTO_ShouldThrowException_WhenNameIsNull() {
        // Given
        App existingApp = new App();
        existingApp.setId(1L);
        existingApp.setName("Test App");
        existingApp.setProjectId("PROJ-123");
        
        validAppDTO.setName(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> dataImportMapper.updateAppFromDTO(validAppDTO, existingApp));
        assertTrue(exception.getMessage().contains("cambiar el name"));
    }

    @Test
    void updateAppFromDTO_ShouldThrowException_WhenNameMismatch() {
        // Given
        App existingApp = new App();
        existingApp.setId(1L);
        existingApp.setName("Test App");
        existingApp.setProjectId("PROJ-123");
        
        validAppDTO.setName("Different App");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> dataImportMapper.updateAppFromDTO(validAppDTO, existingApp));
        assertTrue(exception.getMessage().contains("cambiar el name"));
    }

    @Test
    void updateAppFromDTO_ShouldThrowException_WhenProjectIdIsNull() {
        // Given
        App existingApp = new App();
        existingApp.setId(1L);
        existingApp.setName("Test App");
        existingApp.setProjectId("PROJ-123");
        
        validAppDTO.setProjectId(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> dataImportMapper.updateAppFromDTO(validAppDTO, existingApp));
        assertTrue(exception.getMessage().contains("cambiar el projectId"));
    }

    @Test
    void updateAppFromDTO_ShouldThrowException_WhenProjectIdMismatch() {
        // Given
        App existingApp = new App();
        existingApp.setId(1L);
        existingApp.setName("Test App");
        existingApp.setProjectId("PROJ-123");
        
        validAppDTO.setProjectId("PROJ-999");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> dataImportMapper.updateAppFromDTO(validAppDTO, existingApp));
        assertTrue(exception.getMessage().contains("cambiar el projectId"));
    }

    @Test
    void updateAppFromDTO_ShouldUpdateToNullValues() {
        // Given
        App existingApp = new App();
        existingApp.setId(1L);
        existingApp.setName("Test App");
        existingApp.setProjectId("PROJ-123");
        existingApp.setVertical("Old Vertical");
        existingApp.setFolder("old-folder");
        existingApp.setBitbucketUrl("https://old.url");
        existingApp.setObsolete(true);

        validAppDTO.setVertical(null);
        validAppDTO.setFolder(null);
        validAppDTO.setBitbucketUrl(null);
        validAppDTO.setObsolete(null);

        // When
        dataImportMapper.updateAppFromDTO(validAppDTO, existingApp);

        // Then
        assertNull(existingApp.getVertical());
        assertNull(existingApp.getFolder());
        assertNull(existingApp.getBitbucketUrl());
        assertNull(existingApp.getObsolete());
    }

    @Test
    void updateAppFromDTO_ShouldUpdateAllBooleanFields() {
        // Given
        App existingApp = new App();
        existingApp.setId(1L);
        existingApp.setName("Test App");
        existingApp.setProjectId("PROJ-123");
        existingApp.setJava(false);
        existingApp.setUnitTest(false);
        existingApp.setJest(false);
        existingApp.setMonolith(false);
        existingApp.setConfig(false);

        validAppDTO.setJava(true);
        validAppDTO.setUnitTest(true);
        validAppDTO.setJest(true);
        validAppDTO.setMonolith(true);
        validAppDTO.setConfig(true);

        // When
        dataImportMapper.updateAppFromDTO(validAppDTO, existingApp);

        // Then
        assertEquals(true, existingApp.getJava());
        assertEquals(true, existingApp.getUnitTest());
        assertEquals(true, existingApp.getJest());
        assertEquals(true, existingApp.getMonolith());
        assertEquals(true, existingApp.getConfig());
    }

    @Test
    void updateAppFromDTO_ShouldUpdateAllUrlFields() {
        // Given
        App existingApp = new App();
        existingApp.setId(1L);
        existingApp.setName("Test App");
        existingApp.setProjectId("PROJ-123");
        existingApp.setBitbucketUrl("https://old-bitbucket.url");
        existingApp.setSonarUrl("https://old-sonar.url");
        existingApp.setSonar9Url("https://old-sonar9.url");
        existingApp.setSonar10Url("https://old-sonar10.url");
        existingApp.setSamuelUrl("https://old-samuel.url");
        existingApp.setChimeraUrl("https://old-chimera.url");

        validAppDTO.setBitbucketUrl("https://new-bitbucket.url");
        validAppDTO.setSonarUrl("https://new-sonar.url");
        validAppDTO.setSonar9Url("https://new-sonar9.url");
        validAppDTO.setSonar10Url("https://new-sonar10.url");
        validAppDTO.setSamuelUrl("https://new-samuel.url");
        validAppDTO.setChimeraUrl("https://new-chimera.url");

        // When
        dataImportMapper.updateAppFromDTO(validAppDTO, existingApp);

        // Then
        assertEquals("https://new-bitbucket.url", existingApp.getBitbucketUrl());
        assertEquals("https://new-sonar.url", existingApp.getSonarUrl());
        assertEquals("https://new-sonar9.url", existingApp.getSonar9Url());
        assertEquals("https://new-sonar10.url", existingApp.getSonar10Url());
        assertEquals("https://new-samuel.url", existingApp.getSamuelUrl());
        assertEquals("https://new-chimera.url", existingApp.getChimeraUrl());
    }
}

