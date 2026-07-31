package ar.com.bbva.fuentus.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    private App app1;
    private App app2;

    @BeforeEach
    void setUp() {
        app1 = new App();
        app2 = new App();
    }

    @Test
    void testDefaultConstructor() {
        // Given & When
        App app = new App();

        // Then
        assertNotNull(app);
        assertNull(app.getId());
        assertNull(app.getName());
        assertNull(app.getVertical());
        assertNull(app.getFolder());
        assertNull(app.getProjectId());
        assertNull(app.getBitbucketUrl());
        assertNull(app.getSonarUrl());
        assertNull(app.getSonar9Url());
        assertNull(app.getSonar10Url());
        assertNull(app.getSamuelUrl());
        assertNull(app.getChimeraUrl());
        assertNull(app.getObsolete());
        assertNull(app.getObsoleteBy());
        assertNull(app.getUuaa());
        assertNull(app.getSn1());
        assertNull(app.getSn2());
        assertNull(app.getSo());
        assertNull(app.getAo());
        assertNull(app.getStratosUol1());
        assertNull(app.getStratosUol2());
        assertNull(app.getStratosSl1());
        assertNull(app.getStratosSl1Owner());
        assertNull(app.getStratosSl2());
        assertNull(app.getStratosSl2Owner());
        assertNull(app.getJava());
        assertNull(app.getUnitTest());
        assertNull(app.getJest());
        assertNull(app.getMonolith());
        assertNull(app.getConfig());
        assertNull(app.getSonarKey());
        assertNull(app.getVtravk());
        assertNull(app.getCriticalLocal());
        assertNull(app.getNodeVersion());
        assertNull(app.getCriticalAudit());
        assertNull(app.getCriticalConfidential());
        assertNull(app.getCriticalFraud());
        assertNull(app.getCriticalCfs());
        assertNull(app.getExtra());
        assertNull(app.getArchitecture());
        assertNull(app.getSource());
    }

    @Test
    void testConstructorWithId() {
        // Given
        Long testId = 1L;

        // When
        App app = new App(testId);

        // Then
        assertNotNull(app);
        assertEquals(testId, app.getId());
        assertNull(app.getName());
        assertNull(app.getVertical());
        assertNull(app.getFolder());
        assertNull(app.getProjectId());
        assertNull(app.getSn1());
        assertNull(app.getSn2());
        assertNull(app.getSo());
        assertNull(app.getAo());
        assertNull(app.getObsolete());
        assertNull(app.getJava());
        assertNull(app.getArchitecture());
        assertNull(app.getSource());
    }

    @Test
    void testConstructorWithNullId() {
        // Given & When
        App app = new App(null);

        // Then
        assertNotNull(app);
        assertNull(app.getId());
        assertNull(app.getName());
        assertNull(app.getVertical());
    }

    @Test
    void testConstructors_Coverage() {
        // Test default constructor
        App app1 = new App();
        assertNotNull(app1);
        assertNull(app1.getId());

        // Test constructor with ID
        App app2 = new App(100L);
        assertNotNull(app2);
        assertEquals(Long.valueOf(100L), app2.getId());

        // Test constructor with negative ID
        App app3 = new App(-1L);
        assertNotNull(app3);
        assertEquals(Long.valueOf(-1L), app3.getId());
    }

    @Test
    void testGettersAndSetters_BasicFields() {
        // Test ID
        Long testId = 1L;
        app1.setId(testId);
        assertEquals(testId, app1.getId());

        // Test Name
        String testName = "Test Application";
        app1.setName(testName);
        assertEquals(testName, app1.getName());

        // Test Vertical
        String testVertical = "Banking";
        app1.setVertical(testVertical);
        assertEquals(testVertical, app1.getVertical());

        // Test Folder
        String testFolder = "/apps/test-app";
        app1.setFolder(testFolder);
        assertEquals(testFolder, app1.getFolder());

        // Test ProjectId
        String testProjectId = "PROJECT_123";
        app1.setProjectId(testProjectId);
        assertEquals(testProjectId, app1.getProjectId());
    }

    @Test
    void testGettersAndSetters_UrlFields() {
        // Test BitbucketUrl
        String testBitbucketUrl = "https://bitbucket.com/bbva/test-app";
        app1.setBitbucketUrl(testBitbucketUrl);
        assertEquals(testBitbucketUrl, app1.getBitbucketUrl());

        // Test SonarUrl
        String testSonarUrl = "https://sonar.bbva.com/dashboard?id=test-app";
        app1.setSonarUrl(testSonarUrl);
        assertEquals(testSonarUrl, app1.getSonarUrl());

        // Test Sonar9Url
        String testSonar9Url = "https://sonar9.bbva.com/dashboard?id=test-app";
        app1.setSonar9Url(testSonar9Url);
        assertEquals(testSonar9Url, app1.getSonar9Url());

        // Test Sonar10Url
        String testSonar10Url = "https://sonar10.bbva.com/dashboard?id=test-app";
        app1.setSonar10Url(testSonar10Url);
        assertEquals(testSonar10Url, app1.getSonar10Url());

        // Test SamuelUrl
        String testSamuelUrl = "https://samuel.bbva.com/app/test-app";
        app1.setSamuelUrl(testSamuelUrl);
        assertEquals(testSamuelUrl, app1.getSamuelUrl());

        // Test ChimeraUrl
        String testChimeraUrl = "https://chimera.bbva.com/app/test-app";
        app1.setChimeraUrl(testChimeraUrl);
        assertEquals(testChimeraUrl, app1.getChimeraUrl());
    }

    @Test
    void testGettersAndSetters_BooleanFields() {
        // Test Obsolete
        Boolean testObsolete = true;
        app1.setObsolete(testObsolete);
        assertEquals(testObsolete, app1.getObsolete());

        app1.setObsolete(false);
        assertEquals(false, app1.getObsolete());

        app1.setObsolete(null);
        assertNull(app1.getObsolete());

        // Test Java
        Boolean testJava = true;
        app1.setJava(testJava);
        assertEquals(testJava, app1.getJava());

        // Test UnitTest
        Boolean testUnitTest = true;
        app1.setUnitTest(testUnitTest);
        assertEquals(testUnitTest, app1.getUnitTest());

        // Test Jest
        Boolean testJest = false;
        app1.setJest(testJest);
        assertEquals(testJest, app1.getJest());

        // Test Monolith
        Boolean testMonolith = false;
        app1.setMonolith(testMonolith);
        assertEquals(testMonolith, app1.getMonolith());

        // Test Config
        Boolean testConfig = true;
        app1.setConfig(testConfig);
        assertEquals(testConfig, app1.getConfig());
    }

    @Test
    void testGettersAndSetters_StratosFields() {
        // Test StratosUol1
        String testStratosUol1 = "UOL1";
        app1.setStratosUol1(testStratosUol1);
        assertEquals(testStratosUol1, app1.getStratosUol1());

        // Test StratosUol2
        String testStratosUol2 = "UOL2";
        app1.setStratosUol2(testStratosUol2);
        assertEquals(testStratosUol2, app1.getStratosUol2());

        // Test StratosSl1
        String testStratosSl1 = "SL1";
        app1.setStratosSl1(testStratosSl1);
        assertEquals(testStratosSl1, app1.getStratosSl1());

        // Test StratosSl1Owner
        String testStratosSl1Owner = "SL1 Owner";
        app1.setStratosSl1Owner(testStratosSl1Owner);
        assertEquals(testStratosSl1Owner, app1.getStratosSl1Owner());

        // Test StratosSl2
        String testStratosSl2 = "SL2";
        app1.setStratosSl2(testStratosSl2);
        assertEquals(testStratosSl2, app1.getStratosSl2());

        // Test StratosSl2Owner
        String testStratosSl2Owner = "SL2 Owner";
        app1.setStratosSl2Owner(testStratosSl2Owner);
        assertEquals(testStratosSl2Owner, app1.getStratosSl2Owner());
    }

    @Test
    void testGettersAndSetters_CriticalFields() {
        // Test CriticalLocal
        Integer testCriticalLocal = 5;
        app1.setCriticalLocal(testCriticalLocal);
        assertEquals(testCriticalLocal, app1.getCriticalLocal());

        // Test CriticalAudit
        Boolean testCriticalAudit = true;
        app1.setCriticalAudit(testCriticalAudit);
        assertEquals(testCriticalAudit, app1.getCriticalAudit());

        // Test CriticalConfidential
        Boolean testCriticalConfidential = true;
        app1.setCriticalConfidential(testCriticalConfidential);
        assertEquals(testCriticalConfidential, app1.getCriticalConfidential());

        // Test CriticalFraud
        Boolean testCriticalFraud = false;
        app1.setCriticalFraud(testCriticalFraud);
        assertEquals(testCriticalFraud, app1.getCriticalFraud());

        // Test CriticalCfs
        Boolean testCriticalCfs = true;
        app1.setCriticalCfs(testCriticalCfs);
        assertEquals(testCriticalCfs, app1.getCriticalCfs());
    }

    @Test
    void testGettersAndSetters_AdditionalFields() {
        // Test ObsoleteBy
        String testObsoleteBy = "admin";
        app1.setObsoleteBy(testObsoleteBy);
        assertEquals(testObsoleteBy, app1.getObsoleteBy());

        // Test Uuaa
        String testUuaa = "TEST";
        app1.setUuaa(testUuaa);
        assertEquals(testUuaa, app1.getUuaa());

        // Test SonarKey
        String testSonarKey = "com.bbva:test-app";
        app1.setSonarKey(testSonarKey);
        assertEquals(testSonarKey, app1.getSonarKey());

        // Test Vtravk
        String testVtravk = "vtravk123";
        app1.setVtravk(testVtravk);
        assertEquals(testVtravk, app1.getVtravk());

        // Test NodeVersion
        String testNodeVersion = "16.14.0";
        app1.setNodeVersion(testNodeVersion);
        assertEquals(testNodeVersion, app1.getNodeVersion());

        // Test Extra (LOB field)
        String testExtra = "Extra information about the application";
        app1.setExtra(testExtra);
        assertEquals(testExtra, app1.getExtra());

        // Test Architecture
        String testArchitecture = "microservices";
        app1.setArchitecture(testArchitecture);
        assertEquals(testArchitecture, app1.getArchitecture());

        // Test Source
        String testSource = "bitbucket";
        app1.setSource(testSource);
        assertEquals(testSource, app1.getSource());
    }

    @Test
    void testGettersAndSetters_MissingFields() {
        // Test Sn1
        String testSn1 = "SN1_VALUE";
        app1.setSn1(testSn1);
        assertEquals(testSn1, app1.getSn1());

        // Test Sn2
        String testSn2 = "SN2_VALUE";
        app1.setSn2(testSn2);
        assertEquals(testSn2, app1.getSn2());

        // Test So
        String testSo = "SO_VALUE";
        app1.setSo(testSo);
        assertEquals(testSo, app1.getSo());

        // Test Ao
        String testAo = "AO_VALUE";
        app1.setAo(testAo);
        assertEquals(testAo, app1.getAo());
    }

    @Test
    void testGettersAndSetters_NullValues() {
        // Test setting null values for all nullable fields
        app1.setId(null);
        assertNull(app1.getId());

        app1.setName(null);
        assertNull(app1.getName());

        app1.setVertical(null);
        assertNull(app1.getVertical());

        app1.setObsolete(null);
        assertNull(app1.getObsolete());

        app1.setJava(null);
        assertNull(app1.getJava());

        app1.setCriticalLocal(null);
        assertNull(app1.getCriticalLocal());

        app1.setExtra(null);
        assertNull(app1.getExtra());

        // Test newly added fields
        app1.setSn1(null);
        assertNull(app1.getSn1());

        app1.setSn2(null);
        assertNull(app1.getSn2());

        app1.setSo(null);
        assertNull(app1.getSo());

        app1.setAo(null);
        assertNull(app1.getAo());

        app1.setArchitecture(null);
        assertNull(app1.getArchitecture());

        app1.setSource(null);
        assertNull(app1.getSource());
    }

    @Test
    void testEquals_SameObject() {
        // When & Then
        assertEquals(app1, app1);
        assertTrue(app1.equals(app1));
    }

    @Test
    void testEquals_NullObject() {
        // When & Then
        assertNotEquals(app1, null);
        assertFalse(app1.equals(null));
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        String differentObject = "not an App";

        // When & Then
        assertNotEquals(app1, differentObject);
        assertFalse(app1.equals(differentObject));
    }

    @Test
    void testEquals_SameId() {
        // Given
        Long testId = 1L;
        app1.setId(testId);
        app2.setId(testId);

        // When & Then
        assertEquals(app1, app2);
        assertTrue(app1.equals(app2));
        assertTrue(app2.equals(app1));
    }

    @Test
    void testEquals_DifferentId() {
        // Given
        app1.setId(1L);
        app2.setId(2L);

        // When & Then
        assertNotEquals(app1, app2);
        assertFalse(app1.equals(app2));
        assertFalse(app2.equals(app1));
    }

    @Test
    void testHashCode_SameId() {
        // Given
        Long testId = 1L;
        app1.setId(testId);
        app2.setId(testId);

        // When & Then
        assertEquals(app1.hashCode(), app2.hashCode());
    }

    @Test
    void testHashCode_DifferentId() {
        // Given
        app1.setId(1L);
        app2.setId(2L);

        // When & Then
        assertNotEquals(app1.hashCode(), app2.hashCode());
    }

    @Test
    void testHashCode_NullId() {
        // Given
        app1.setId(null);

        // When & Then
        assertDoesNotThrow(() -> app1.hashCode());
        assertEquals(0, app1.hashCode());
    }

    @Test
    void testToString() {
        // Given
        app1.setId(1L);
        app1.setName("Test App");

        // When
        String result = app1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("App"));
        assertTrue(result.contains("id=1"));
    }

    @Test
    void testSerializable() {
        // Given
        setupSomeValues();

        // When & Then - Test that the class implements Serializable properly
        assertTrue(app1 instanceof java.io.Serializable);
    }

    @Test
    void testEqualsContract_Reflexive() {
        // Reflexive: x.equals(x) should return true
        assertTrue(app1.equals(app1));
    }

    @Test
    void testEqualsContract_Symmetric() {
        // Symmetric: x.equals(y) should return same as y.equals(x)
        app1.setId(1L);
        app2.setId(1L);

        boolean result1 = app1.equals(app2);
        boolean result2 = app2.equals(app1);
        assertEquals(result1, result2);
    }

    @Test
    void testEqualsContract_Transitive() {
        // Transitive: if x.equals(y) and y.equals(z), then x.equals(z)
        App app3 = new App();
        app1.setId(1L);
        app2.setId(1L);
        app3.setId(1L);

        assertTrue(app1.equals(app2));
        assertTrue(app2.equals(app3));
        assertTrue(app1.equals(app3));
    }

    @Test
    void testEqualsContract_Consistent() {
        // Consistent: multiple invocations should return same result
        app1.setId(1L);
        app2.setId(1L);

        boolean result1 = app1.equals(app2);
        boolean result2 = app1.equals(app2);
        boolean result3 = app1.equals(app2);

        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }

    @Test
    void testGettersAndSetters_EdgeCases() {
        // Test empty strings
        app1.setName("");
        assertEquals("", app1.getName());

        app1.setSn1("");
        assertEquals("", app1.getSn1());

        app1.setSn2("");
        assertEquals("", app1.getSn2());

        app1.setSo("");
        assertEquals("", app1.getSo());

        app1.setAo("");
        assertEquals("", app1.getAo());

        // Test very long strings
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("a");
        }
        String longString = sb.toString();
        app1.setExtra(longString);
        assertEquals(longString, app1.getExtra());

        // Test special characters
        String specialChars = "!@#$%^&*()_+-={}[]|\\:;\"'<>?,./";
        app1.setUuaa(specialChars);
        assertEquals(specialChars, app1.getUuaa());

        // Test Unicode characters
        String unicode = "测试应用程序";
        app1.setName(unicode);
        assertEquals(unicode, app1.getName());

        // Test Boolean edge cases
        app1.setObsolete(Boolean.TRUE);
        assertEquals(Boolean.TRUE, app1.getObsolete());

        app1.setObsolete(Boolean.FALSE);
        assertEquals(Boolean.FALSE, app1.getObsolete());

        // Test Integer edge cases
        app1.setCriticalLocal(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, app1.getCriticalLocal());

        app1.setCriticalLocal(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, app1.getCriticalLocal());

        app1.setCriticalLocal(0);
        assertEquals(Integer.valueOf(0), app1.getCriticalLocal());
    }

    @Test
    void testAllFieldsGettersAndSetters_Comprehensive() {
        // Given
        App app = new App();
        
        // When - Set all fields
        app.setId(1L);
        app.setName("Test App");
        app.setVertical("Banking");
        app.setFolder("/test/folder");
        app.setProjectId("PRJ123");
        app.setBitbucketUrl("https://bitbucket.com/test");
        app.setSonarUrl("https://sonar.com/test");
        app.setSonar9Url("https://sonar9.com/test");
        app.setSonar10Url("https://sonar10.com/test");
        app.setSamuelUrl("https://samuel.com/test");
        app.setChimeraUrl("https://chimera.com/test");
        app.setObsolete(true);
        app.setObsoleteBy("admin");
        app.setUuaa("TEST");
        app.setSn1("SN1_TEST");
        app.setSn2("SN2_TEST");
        app.setSo("SO_TEST");
        app.setAo("AO_TEST");
        app.setStratosUol1("UOL1_TEST");
        app.setStratosUol2("UOL2_TEST");
        app.setStratosSl1("SL1_TEST");
        app.setStratosSl1Owner("SL1_OWNER");
        app.setStratosSl2("SL2_TEST");
        app.setStratosSl2Owner("SL2_OWNER");
        app.setJava(true);
        app.setUnitTest(false);
        app.setJest(true);
        app.setMonolith(false);
        app.setConfig(true);
        app.setSonarKey("test:sonar");
        app.setVtravk("vtravk123");
        app.setCriticalLocal(5);
        app.setNodeVersion("16.0.0");
        app.setCriticalAudit(true);
        app.setCriticalConfidential(false);
        app.setCriticalFraud(true);
        app.setCriticalCfs(false);
        app.setExtra("Extra info");
        app.setArchitecture("microservices");
        app.setSource("bitbucket");

        // Then - Verify all fields
        assertEquals(Long.valueOf(1L), app.getId());
        assertEquals("Test App", app.getName());
        assertEquals("Banking", app.getVertical());
        assertEquals("/test/folder", app.getFolder());
        assertEquals("PRJ123", app.getProjectId());
        assertEquals("https://bitbucket.com/test", app.getBitbucketUrl());
        assertEquals("https://sonar.com/test", app.getSonarUrl());
        assertEquals("https://sonar9.com/test", app.getSonar9Url());
        assertEquals("https://sonar10.com/test", app.getSonar10Url());
        assertEquals("https://samuel.com/test", app.getSamuelUrl());
        assertEquals("https://chimera.com/test", app.getChimeraUrl());
        assertEquals(Boolean.TRUE, app.getObsolete());
        assertEquals("admin", app.getObsoleteBy());
        assertEquals("TEST", app.getUuaa());
        assertEquals("SN1_TEST", app.getSn1());
        assertEquals("SN2_TEST", app.getSn2());
        assertEquals("SO_TEST", app.getSo());
        assertEquals("AO_TEST", app.getAo());
        assertEquals("UOL1_TEST", app.getStratosUol1());
        assertEquals("UOL2_TEST", app.getStratosUol2());
        assertEquals("SL1_TEST", app.getStratosSl1());
        assertEquals("SL1_OWNER", app.getStratosSl1Owner());
        assertEquals("SL2_TEST", app.getStratosSl2());
        assertEquals("SL2_OWNER", app.getStratosSl2Owner());
        assertEquals(Boolean.TRUE, app.getJava());
        assertEquals(Boolean.FALSE, app.getUnitTest());
        assertEquals(Boolean.TRUE, app.getJest());
        assertEquals(Boolean.FALSE, app.getMonolith());
        assertEquals(Boolean.TRUE, app.getConfig());
        assertEquals("test:sonar", app.getSonarKey());
        assertEquals("vtravk123", app.getVtravk());
        assertEquals(Integer.valueOf(5), app.getCriticalLocal());
        assertEquals("16.0.0", app.getNodeVersion());
        assertEquals(Boolean.TRUE, app.getCriticalAudit());
        assertEquals(Boolean.FALSE, app.getCriticalConfidential());
        assertEquals(Boolean.TRUE, app.getCriticalFraud());
        assertEquals(Boolean.FALSE, app.getCriticalCfs());
        assertEquals("Extra info", app.getExtra());
        assertEquals("microservices", app.getArchitecture());
        assertEquals("bitbucket", app.getSource());
    }

    private void setupSomeValues() {
        app1.setId(1L);
        app1.setName("Test Application");
        app1.setVertical("Banking");
        app1.setProjectId("PROJECT_123");
        app1.setUuaa("TEST");
        app1.setJava(true);
        app1.setUnitTest(true);
        app1.setObsolete(false);
        app1.setArchitecture("microservices");
        app1.setSource("bitbucket");
    }
}