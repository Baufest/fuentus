package ar.com.bbva.fuentus.mapper;

import ar.com.bbva.fuentus.dto.AppDTO;
import ar.com.bbva.fuentus.dto.AppGetRequestDTO;
import ar.com.bbva.fuentus.entities.App;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.Vertical;
import ar.com.bbva.fuentus.entities.VerticalNucleus;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class AppsMapperTest {

    @Test
    void mapApp2DTO_ReturnsNull_WhenAppIsNull() {
        assertNull(AppsMapper.mapApp2DTO(null));
    }

    @Test
    void mapApp2DTO_MapsAllSimpleFields() {
        App app = buildFullApp();

        AppDTO dto = AppsMapper.mapApp2DTO(app);

        assertAll(
                () -> assertEquals(app.getId(), dto.getId()),
                () -> assertEquals(app.getName(), dto.getName()),
                () -> assertEquals(app.getVertical(), dto.getVertical()),
                () -> assertEquals(app.getFolder(), dto.getFolder()),
                () -> assertEquals(app.getProjectId(), dto.getProjectId()),
                () -> assertEquals(app.getBitbucketUrl(), dto.getBitbucketUrl()),
                () -> assertEquals(app.getSonarUrl(), dto.getSonarUrl()),
                () -> assertEquals(app.getSonar9Url(), dto.getSonar9Url()),
                () -> assertEquals(app.getSonar10Url(), dto.getSonar10Url()),
                () -> assertEquals(app.getSamuelUrl(), dto.getSamuelUrl()),
                () -> assertEquals(app.getChimeraUrl(), dto.getChimeraUrl()),
                () -> assertEquals(app.getObsolete(), dto.getObsolete()),
                () -> assertEquals(app.getObsoleteBy(), dto.getObsoleteBy()),
                () -> assertEquals(app.getUuaa(), dto.getUuaa()),
                () -> assertEquals(app.getSn1(), dto.getSn1()),
                () -> assertEquals(app.getSn2(), dto.getSn2()),
                () -> assertEquals(app.getSo(), dto.getSo()),
                () -> assertEquals(app.getAo(), dto.getAo()),
                () -> assertEquals(app.getStratosUol1(), dto.getStratosUol1()),
                () -> assertEquals(app.getStratosUol2(), dto.getStratosUol2()),
                () -> assertEquals(app.getStratosSl1(), dto.getStratosSl1()),
                () -> assertEquals(app.getStratosSl1Owner(), dto.getStratosSl1Owner()),
                () -> assertEquals(app.getStratosSl2(), dto.getStratosSl2()),
                () -> assertEquals(app.getStratosSl2Owner(), dto.getStratosSl2Owner()),
                () -> assertEquals(app.getJava(), dto.getJava()),
                () -> assertEquals(app.getUnitTest(), dto.getUnitTest()),
                () -> assertEquals(app.getJest(), dto.getJest()),
                () -> assertEquals(app.getMonolith(), dto.getMonolith()),
                () -> assertEquals(app.getConfig(), dto.getConfig()),
                () -> assertEquals(app.getSonarKey(), dto.getSonarKey()),
                () -> assertEquals(app.getVtravk(), dto.getVtravk()),
                () -> assertEquals(app.getCriticalLocal(), dto.getCriticalLocal()),
                () -> assertEquals(app.getNodeVersion(), dto.getNodeVersion()),
                () -> assertEquals(app.getCriticalAudit(), dto.getCriticalAudit()),
                () -> assertEquals(app.getCriticalConfidential(), dto.getCriticalConfidential()),
                () -> assertEquals(app.getCriticalFraud(), dto.getCriticalFraud()),
                () -> assertEquals(app.getCriticalCfs(), dto.getCriticalCfs()),
                () -> assertEquals(app.getExtra(), dto.getExtra()),
                () -> assertEquals(app.getArchitecture(), dto.getArchitecture()),
                () -> assertEquals(app.getSource(), dto.getSource()),
                () -> assertNull(dto.getNucleusId()),
                () -> assertNull(dto.getNucleusServiceN1()),
                () -> assertNull(dto.getNucleusServiceN2()),
                () -> assertNull(dto.getNucleusUol2()),
                () -> assertNull(dto.getNucleusVertical())
        );
    }

    @Test
    void mapApp2DTO_MapsNucleusFieldsWhenPresent() {
        App app = buildFullApp();
        Nucleus nucleus = new Nucleus();
        nucleus.setId(200L);
        nucleus.setServiceN1("Service N1");
        nucleus.setServiceN2("Service N2");
        nucleus.setOrgN2fabrica("ORG_N2");

        Vertical vertical = new Vertical();
        vertical.setName("Retail");

        VerticalNucleus verticalNucleus = new VerticalNucleus();
        verticalNucleus.setVertical(vertical);
        verticalNucleus.setNucleus(nucleus);
        nucleus.setVerticalNucleus(Collections.singletonList(verticalNucleus));
        app.setNucleus(nucleus);

        AppDTO dto = AppsMapper.mapApp2DTO(app);

        assertAll(
                () -> assertEquals(200L, dto.getNucleusId()),
                () -> assertEquals("Service N1", dto.getNucleusServiceN1()),
                () -> assertEquals("Service N2", dto.getNucleusServiceN2()),
                () -> assertEquals("ORG_N2", dto.getNucleusUol2()),
                () -> assertEquals("Retail", dto.getNucleusVertical())
        );
    }

    @Test
    void mapApp2DTO_SetsVerticalToNullWhenListEmpty() {
        App app = buildFullApp();
        Nucleus nucleus = new Nucleus();
        nucleus.setId(300L);
        nucleus.setServiceN1("Service N1");
        nucleus.setServiceN2("Service N2");
        nucleus.setOrgN2fabrica("ORG_N2");
        nucleus.setVerticalNucleus(Collections.emptyList());
        app.setNucleus(nucleus);

        AppDTO dto = AppsMapper.mapApp2DTO(app);

        assertAll(
                () -> assertEquals(300L, dto.getNucleusId()),
                () -> assertNull(dto.getNucleusVertical())
        );
    }

    @Test
    void mapApp2GetRequestDTO_ReturnsNull_WhenAppIsNull() {
        assertNull(AppsMapper.mapApp2GetRequestDTO(null));
    }

    @Test
    void mapApp2GetRequestDTO_MapsFilterFields() {
        App app = new App();
        app.setSo("SO_TEST");
        app.setSn1("SN1_TEST");
        app.setSn2("SN2_TEST");
        app.setUuaa("UUAA_TEST");

        AppGetRequestDTO dto = AppsMapper.mapApp2GetRequestDTO(app);

        assertAll(
                () -> assertEquals("SO_TEST", dto.getSo()),
                () -> assertEquals("SN1_TEST", dto.getSn1()),
                () -> assertEquals("SN2_TEST", dto.getSn2()),
                () -> assertEquals("UUAA_TEST", dto.getUuaa())
        );
    }

    private App buildFullApp() {
        App app = new App();
        app.setId(1L);
        app.setName("Test App");
        app.setVertical("Vertical");
        app.setFolder("folder");
        app.setProjectId("PROJ-1");
        app.setBitbucketUrl("https://bitbucket");
        app.setSonarUrl("https://sonar");
        app.setSonar9Url("https://sonar9");
        app.setSonar10Url("https://sonar10");
        app.setSamuelUrl("https://samuel");
        app.setChimeraUrl("https://chimera");
        app.setObsolete(false);
        app.setObsoleteBy("nobody");
        app.setUuaa("UUAA");
        app.setSn1("SN1");
        app.setSn2("SN2");
        app.setSo("SO");
        app.setAo("AO");
        app.setStratosUol1("UOL1");
        app.setStratosUol2("UOL2");
        app.setStratosSl1("SL1");
        app.setStratosSl1Owner("SL1_OWNER");
        app.setStratosSl2("SL2");
        app.setStratosSl2Owner("SL2_OWNER");
        app.setJava(true);
        app.setUnitTest(true);
        app.setJest(false);
        app.setMonolith(false);
        app.setConfig(true);
        app.setSonarKey("SONAR_KEY");
        app.setVtravk("VTRAVK");
        app.setCriticalLocal(5);
        app.setNodeVersion("20");
        app.setCriticalAudit(true);
        app.setCriticalConfidential(false);
        app.setCriticalFraud(true);
        app.setCriticalCfs(false);
        app.setExtra("extra");
        app.setArchitecture("arch");
        app.setSource("source");
        return app;
    }
}
