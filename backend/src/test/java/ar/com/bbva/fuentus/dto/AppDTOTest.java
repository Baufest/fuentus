package ar.com.bbva.fuentus.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppDTOTest {

    @Test
    void builder_ShouldPopulateCoreMetadata() {
        AppDTO dto = createSampleDto();

        assertAll(
            () -> assertEquals(42L, dto.getId()),
            () -> assertEquals("Fuentus", dto.getName()),
            () -> assertEquals("Retail", dto.getVertical()),
            () -> assertEquals("/srv/fuentus", dto.getFolder()),
            () -> assertEquals("FUENTUS-1", dto.getProjectId()),
            () -> assertEquals("https://bitbucket.local/projects/FUENTUS", dto.getBitbucketUrl()),
            () -> assertEquals("https://sonar.local/projects/fuentus", dto.getSonarUrl()),
            () -> assertEquals("https://sonar9.local/projects/fuentus", dto.getSonar9Url()),
            () -> assertEquals("https://sonar10.local/projects/fuentus", dto.getSonar10Url()),
            () -> assertEquals("https://samuel.local/fuentus", dto.getSamuelUrl()),
            () -> assertEquals("https://chimera.local/fuentus", dto.getChimeraUrl()),
            () -> assertEquals("FUET", dto.getUuaa()),
            () -> assertEquals("SN1", dto.getSn1()),
            () -> assertEquals("SN2", dto.getSn2()),
            () -> assertEquals("SO", dto.getSo()),
            () -> assertEquals("AO", dto.getAo())
        );
    }

    @Test
    void builder_ShouldPopulateStratosFields() {
        AppDTO dto = createSampleDto();

        assertAll(
            () -> assertEquals("UOL1", dto.getStratosUol1()),
            () -> assertEquals("UOL2", dto.getStratosUol2()),
            () -> assertEquals("SL1", dto.getStratosSl1()),
            () -> assertEquals("SL1_OWNER", dto.getStratosSl1Owner()),
            () -> assertEquals("SL2", dto.getStratosSl2()),
            () -> assertEquals("SL2_OWNER", dto.getStratosSl2Owner())
        );
    }

    @Test
    void builder_ShouldPopulateFlagsAndConfig() {
        AppDTO dto = createSampleDto();

        assertAll(
            () -> assertEquals(Boolean.TRUE, dto.getObsolete()),
            () -> assertEquals("obsolete-user", dto.getObsoleteBy()),
            () -> assertEquals(Boolean.TRUE, dto.getJava()),
            () -> assertEquals(Boolean.FALSE, dto.getUnitTest()),
            () -> assertEquals(Boolean.TRUE, dto.getJest()),
            () -> assertEquals(Boolean.FALSE, dto.getMonolith()),
            () -> assertEquals(Boolean.TRUE, dto.getConfig()),
            () -> assertEquals("fuentus-sonar", dto.getSonarKey()),
            () -> assertEquals("VTRAVK-123", dto.getVtravk()),
            () -> assertEquals(3, dto.getCriticalLocal()),
            () -> assertEquals("18.19.0", dto.getNodeVersion()),
            () -> assertEquals(Boolean.TRUE, dto.getCriticalAudit()),
            () -> assertEquals(Boolean.FALSE, dto.getCriticalConfidential()),
            () -> assertEquals(Boolean.TRUE, dto.getCriticalFraud()),
            () -> assertEquals(Boolean.FALSE, dto.getCriticalCfs()),
            () -> assertEquals("extra-config", dto.getExtra()),
            () -> assertEquals("MICROSERVICE", dto.getArchitecture()),
            () -> assertEquals("INTERNAL", dto.getSource())
        );
    }

    @Test
    void builder_ShouldPopulateNucleusFields() {
        AppDTO dto = createSampleDto();

        assertAll(
            () -> assertEquals(100L, dto.getNucleusId()),
            () -> assertEquals("NUCLEUS-N1", dto.getNucleusServiceN1()),
            () -> assertEquals("NUCLEUS-N2", dto.getNucleusServiceN2()),
            () -> assertEquals("NUCLEUS-UOL2", dto.getNucleusUol2()),
            () -> assertEquals("NUCLEUS-VERTICAL", dto.getNucleusVertical())
        );
    }

    private AppDTO createSampleDto() {
        return AppDTO.builder()
            .id(42L)
            .name("Fuentus")
            .vertical("Retail")
            .folder("/srv/fuentus")
            .projectId("FUENTUS-1")
            .bitbucketUrl("https://bitbucket.local/projects/FUENTUS")
            .sonarUrl("https://sonar.local/projects/fuentus")
            .sonar9Url("https://sonar9.local/projects/fuentus")
            .sonar10Url("https://sonar10.local/projects/fuentus")
            .samuelUrl("https://samuel.local/fuentus")
            .chimeraUrl("https://chimera.local/fuentus")
            .obsolete(Boolean.TRUE)
            .obsoleteBy("obsolete-user")
            .uuaa("FUET")
            .sn1("SN1")
            .sn2("SN2")
            .so("SO")
            .ao("AO")
            .stratosUol1("UOL1")
            .stratosUol2("UOL2")
            .stratosSl1("SL1")
            .stratosSl1Owner("SL1_OWNER")
            .stratosSl2("SL2")
            .stratosSl2Owner("SL2_OWNER")
            .java(Boolean.TRUE)
            .unitTest(Boolean.FALSE)
            .jest(Boolean.TRUE)
            .monolith(Boolean.FALSE)
            .config(Boolean.TRUE)
            .sonarKey("fuentus-sonar")
            .vtravk("VTRAVK-123")
            .criticalLocal(3)
            .nodeVersion("18.19.0")
            .criticalAudit(Boolean.TRUE)
            .criticalConfidential(Boolean.FALSE)
            .criticalFraud(Boolean.TRUE)
            .criticalCfs(Boolean.FALSE)
            .extra("extra-config")
            .architecture("MICROSERVICE")
            .source("INTERNAL")
            .nucleusId(100L)
            .nucleusServiceN1("NUCLEUS-N1")
            .nucleusServiceN2("NUCLEUS-N2")
            .nucleusUol2("NUCLEUS-UOL2")
            .nucleusVertical("NUCLEUS-VERTICAL")
            .build();
    }

    @Test
    void setters_ShouldUpdateAndNullifyFields() {
        AppDTO dto = new AppDTO();

        dto.setName("Initial");
        dto.setJava(Boolean.TRUE);
        dto.setCriticalLocal(7);
        dto.setArchitecture("Legacy");
        dto.setNucleusVertical("BANKING");

        assertEquals("Initial", dto.getName());
        assertEquals(Boolean.TRUE, dto.getJava());
        assertEquals(7, dto.getCriticalLocal());
        assertEquals("Legacy", dto.getArchitecture());
        assertEquals("BANKING", dto.getNucleusVertical());

        dto.setName(null);
        dto.setJava(null);
        dto.setCriticalLocal(null);
        dto.setArchitecture(null);
        dto.setNucleusVertical(null);

        assertNull(dto.getName());
        assertNull(dto.getJava());
        assertNull(dto.getCriticalLocal());
        assertNull(dto.getArchitecture());
        assertNull(dto.getNucleusVertical());
    }

    @Test
    void noArgsConstructor_ShouldStartWithNullValues() {
        AppDTO dto = new AppDTO();

        assertAll(
            () -> assertNull(dto.getId()),
            () -> assertNull(dto.getName()),
            () -> assertNull(dto.getProjectId()),
            () -> assertNull(dto.getSonarUrl()),
            () -> assertNull(dto.getObsolete()),
            () -> assertNull(dto.getJava()),
            () -> assertNull(dto.getCriticalLocal()),
            () -> assertNull(dto.getArchitecture()),
            () -> assertNull(dto.getNucleusId())
        );
    }
}
