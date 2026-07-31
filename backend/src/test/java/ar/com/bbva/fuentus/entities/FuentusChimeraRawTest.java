package ar.com.bbva.fuentus.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FuentusChimeraRawTest {

    private FuentusChimeraRaw fuentusChimeraRaw1;
    private FuentusChimeraRaw fuentusChimeraRaw2;
    private Date testDate1;
    private Date testDate2;
    private Date testDate3;

    @BeforeEach
    void setUp() {
        fuentusChimeraRaw1 = new FuentusChimeraRaw();
        fuentusChimeraRaw2 = new FuentusChimeraRaw();
        testDate1 = new Date();
        testDate2 = new Date(testDate1.getTime() + 86400000); // +1 day
        testDate3 = new Date(testDate2.getTime() + 86400000); // +2 days
    }

    @Test
    void testDefaultConstructor() {
        // Given & When
        FuentusChimeraRaw raw = new FuentusChimeraRaw();

        // Then
        assertNotNull(raw);
        assertNull(raw.getId());
        assertNull(raw.getEventInfoCreatedTimestamp());
        assertNull(raw.getEventInfoIngestedTimestamp());
        assertNull(raw.getEventInfoRepository());
        assertNull(raw.getEventInfoUserId());
        assertNull(raw.getEventInfoCountry());
        assertNull(raw.getEventInfoUuaa());
        assertNull(raw.getEventInfoOrigin());
        assertNull(raw.getEventInfoScanStatus());
        assertNull(raw.getEventInfoBranch());
        assertNull(raw.getEventInfoProject());
        assertNull(raw.getEventInfoEventId());
        assertNull(raw.getEventInfoVulnerabilitiesCount());
        assertNull(raw.getEventInfoLinesOfCode());
        assertNull(raw.getEventInfoAnalyzer());
        assertNull(raw.getEventInfoReviewId());
        assertNull(raw.getEventInfoLanguage());
        assertNull(raw.getEventInfoProduct());
        assertNull(raw.getEventInfoArea());
        assertNull(raw.getEventInfoArchitecture());
        assertNull(raw.getEventInfoExecutedAt());
        assertNull(raw.getEventInfoCommitId());
        assertNull(raw.getEventInfoPreset());
        assertNull(raw.getEventInfoDuplicatedId());
        assertNull(raw.getSentTimestamp());
        assertNull(raw.getEventIngestedTimestamp());
        assertNull(raw.getRepo());
        assertNull(raw.getRepoUrl());
        assertNull(raw.getUser());
        assertNull(raw.getPeriodStart());
    }

    @Test
    void testConstructorWithId() {
        // Given
        Long testId = 123L;

        // When
        FuentusChimeraRaw raw = new FuentusChimeraRaw(testId);

        // Then
        assertNotNull(raw);
        assertEquals(testId, raw.getId());
        assertNull(raw.getEventInfoCreatedTimestamp());
        assertNull(raw.getEventInfoIngestedTimestamp());
        assertNull(raw.getEventInfoExecutedAt());
        assertNull(raw.getSentTimestamp());
        assertNull(raw.getEventIngestedTimestamp());
    }

    @Test
    void testConstructorWithMultipleParameters() {
        // Given
        Long testId = 456L;
        Date testCreatedTimestamp = testDate1;
        Date testIngestedTimestamp = testDate2;
        Date testExecutedAt = testDate3;
        Date testSentTimestamp = new Date(testDate1.getTime() + 3600000); // +1 hour
        Date testEventIngestedTimestamp = new Date(testDate2.getTime() + 3600000); // +1 hour

        // When
        FuentusChimeraRaw raw = new FuentusChimeraRaw(testId, testCreatedTimestamp, 
                testIngestedTimestamp, testExecutedAt, testSentTimestamp, testEventIngestedTimestamp);

        // Then
        assertNotNull(raw);
        assertEquals(testId, raw.getId());
        assertEquals(testCreatedTimestamp, raw.getEventInfoCreatedTimestamp());
        assertEquals(testIngestedTimestamp, raw.getEventInfoIngestedTimestamp());
        assertEquals(testExecutedAt, raw.getEventInfoExecutedAt());
        assertEquals(testSentTimestamp, raw.getSentTimestamp());
        assertEquals(testEventIngestedTimestamp, raw.getEventIngestedTimestamp());
        
        // Verify other fields are null
        assertNull(raw.getEventInfoRepository());
        assertNull(raw.getEventInfoUserId());
        assertNull(raw.getRepo());
        assertNull(raw.getUser());
    }

    @Test
    void testConstructorWithNullId() {
        // Given & When
        FuentusChimeraRaw raw = new FuentusChimeraRaw(null);

        // Then
        assertNotNull(raw);
        assertNull(raw.getId());
    }

    @Test
    void testConstructorWithNullDates() {
        // Given
        Long testId = 789L;

        // When
        FuentusChimeraRaw raw = new FuentusChimeraRaw(testId, null, null, null, null, null);

        // Then
        assertNotNull(raw);
        assertEquals(testId, raw.getId());
        assertNull(raw.getEventInfoCreatedTimestamp());
        assertNull(raw.getEventInfoIngestedTimestamp());
        assertNull(raw.getEventInfoExecutedAt());
        assertNull(raw.getSentTimestamp());
        assertNull(raw.getEventIngestedTimestamp());
    }

    @Test
    void testGettersAndSetters_BasicFields() {
        // Test ID
        Long testId = 1L;
        fuentusChimeraRaw1.setId(testId);
        assertEquals(testId, fuentusChimeraRaw1.getId());

        // Test EventInfoCreatedTimestamp
        fuentusChimeraRaw1.setEventInfoCreatedTimestamp(testDate1);
        assertEquals(testDate1, fuentusChimeraRaw1.getEventInfoCreatedTimestamp());

        // Test EventInfoIngestedTimestamp
        fuentusChimeraRaw1.setEventInfoIngestedTimestamp(testDate2);
        assertEquals(testDate2, fuentusChimeraRaw1.getEventInfoIngestedTimestamp());

        // Test EventInfoRepository
        String testRepository = "test-repository";
        fuentusChimeraRaw1.setEventInfoRepository(testRepository);
        assertEquals(testRepository, fuentusChimeraRaw1.getEventInfoRepository());

        // Test EventInfoUserId
        String testUserId = "user123";
        fuentusChimeraRaw1.setEventInfoUserId(testUserId);
        assertEquals(testUserId, fuentusChimeraRaw1.getEventInfoUserId());

        // Test EventInfoCountry
        String testCountry = "ES";
        fuentusChimeraRaw1.setEventInfoCountry(testCountry);
        assertEquals(testCountry, fuentusChimeraRaw1.getEventInfoCountry());

        // Test EventInfoUuaa
        String testUuaa = "TEST";
        fuentusChimeraRaw1.setEventInfoUuaa(testUuaa);
        assertEquals(testUuaa, fuentusChimeraRaw1.getEventInfoUuaa());

        // Test EventInfoOrigin
        String testOrigin = "manual";
        fuentusChimeraRaw1.setEventInfoOrigin(testOrigin);
        assertEquals(testOrigin, fuentusChimeraRaw1.getEventInfoOrigin());

        // Test EventInfoScanStatus
        String testScanStatus = "COMPLETED";
        fuentusChimeraRaw1.setEventInfoScanStatus(testScanStatus);
        assertEquals(testScanStatus, fuentusChimeraRaw1.getEventInfoScanStatus());

        // Test EventInfoBranch
        String testBranch = "main";
        fuentusChimeraRaw1.setEventInfoBranch(testBranch);
        assertEquals(testBranch, fuentusChimeraRaw1.getEventInfoBranch());
    }

    @Test
    void testGettersAndSetters_ProjectFields() {
        // Test EventInfoProject
        String testProject = "test-project";
        fuentusChimeraRaw1.setEventInfoProject(testProject);
        assertEquals(testProject, fuentusChimeraRaw1.getEventInfoProject());

        // Test EventInfoEventId
        String testEventId = "event123";
        fuentusChimeraRaw1.setEventInfoEventId(testEventId);
        assertEquals(testEventId, fuentusChimeraRaw1.getEventInfoEventId());

        // Test EventInfoVulnerabilitiesCount
        Integer testVulnCount = 10;
        fuentusChimeraRaw1.setEventInfoVulnerabilitiesCount(testVulnCount);
        assertEquals(testVulnCount, fuentusChimeraRaw1.getEventInfoVulnerabilitiesCount());

        // Test EventInfoLinesOfCode
        Integer testLinesOfCode = 5000;
        fuentusChimeraRaw1.setEventInfoLinesOfCode(testLinesOfCode);
        assertEquals(testLinesOfCode, fuentusChimeraRaw1.getEventInfoLinesOfCode());

        // Test EventInfoAnalyzer
        String testAnalyzer = "SonarQube";
        fuentusChimeraRaw1.setEventInfoAnalyzer(testAnalyzer);
        assertEquals(testAnalyzer, fuentusChimeraRaw1.getEventInfoAnalyzer());

        // Test EventInfoReviewId
        String testReviewId = "review123";
        fuentusChimeraRaw1.setEventInfoReviewId(testReviewId);
        assertEquals(testReviewId, fuentusChimeraRaw1.getEventInfoReviewId());

        // Test EventInfoLanguage
        String testLanguage = "Java";
        fuentusChimeraRaw1.setEventInfoLanguage(testLanguage);
        assertEquals(testLanguage, fuentusChimeraRaw1.getEventInfoLanguage());

        // Test EventInfoProduct
        String testProduct = "test-product";
        fuentusChimeraRaw1.setEventInfoProduct(testProduct);
        assertEquals(testProduct, fuentusChimeraRaw1.getEventInfoProduct());

        // Test EventInfoArea
        String testArea = "test-area";
        fuentusChimeraRaw1.setEventInfoArea(testArea);
        assertEquals(testArea, fuentusChimeraRaw1.getEventInfoArea());

        // Test EventInfoArchitecture
        String testArchitecture = "microservices";
        fuentusChimeraRaw1.setEventInfoArchitecture(testArchitecture);
        assertEquals(testArchitecture, fuentusChimeraRaw1.getEventInfoArchitecture());
    }

    @Test
    void testGettersAndSetters_ExecutionFields() {
        // Test EventInfoExecutedAt
        fuentusChimeraRaw1.setEventInfoExecutedAt(testDate3);
        assertEquals(testDate3, fuentusChimeraRaw1.getEventInfoExecutedAt());

        // Test EventInfoCommitId
        String testCommitId = "abc123def456";
        fuentusChimeraRaw1.setEventInfoCommitId(testCommitId);
        assertEquals(testCommitId, fuentusChimeraRaw1.getEventInfoCommitId());

        // Test EventInfoPreset
        String testPreset = "default-preset";
        fuentusChimeraRaw1.setEventInfoPreset(testPreset);
        assertEquals(testPreset, fuentusChimeraRaw1.getEventInfoPreset());

        // Test EventInfoDuplicatedId
        String testDuplicatedId = "dup123";
        fuentusChimeraRaw1.setEventInfoDuplicatedId(testDuplicatedId);
        assertEquals(testDuplicatedId, fuentusChimeraRaw1.getEventInfoDuplicatedId());

        // Test SentTimestamp
        fuentusChimeraRaw1.setSentTimestamp(testDate1);
        assertEquals(testDate1, fuentusChimeraRaw1.getSentTimestamp());

        // Test EventIngestedTimestamp
        fuentusChimeraRaw1.setEventIngestedTimestamp(testDate2);
        assertEquals(testDate2, fuentusChimeraRaw1.getEventIngestedTimestamp());
    }

    @Test
    void testGettersAndSetters_AdditionalFields() {
        // Test Repo
        String testRepo = "test-repo";
        fuentusChimeraRaw1.setRepo(testRepo);
        assertEquals(testRepo, fuentusChimeraRaw1.getRepo());

        // Test RepoUrl
        String testRepoUrl = "https://github.com/test/repo";
        fuentusChimeraRaw1.setRepoUrl(testRepoUrl);
        assertEquals(testRepoUrl, fuentusChimeraRaw1.getRepoUrl());

        // Test User
        String testUser = "testUser";
        fuentusChimeraRaw1.setUser(testUser);
        assertEquals(testUser, fuentusChimeraRaw1.getUser());

        // Test PeriodStart
        fuentusChimeraRaw1.setPeriodStart(testDate3);
        assertEquals(testDate3, fuentusChimeraRaw1.getPeriodStart());
    }

    @Test
    void testGettersAndSetters_NullValues() {
        // Test setting null values for nullable fields
        fuentusChimeraRaw1.setId(null);
        assertNull(fuentusChimeraRaw1.getId());

        fuentusChimeraRaw1.setEventInfoCreatedTimestamp(null);
        assertNull(fuentusChimeraRaw1.getEventInfoCreatedTimestamp());

        fuentusChimeraRaw1.setEventInfoIngestedTimestamp(null);
        assertNull(fuentusChimeraRaw1.getEventInfoIngestedTimestamp());

        fuentusChimeraRaw1.setEventInfoRepository(null);
        assertNull(fuentusChimeraRaw1.getEventInfoRepository());

        fuentusChimeraRaw1.setEventInfoUserId(null);
        assertNull(fuentusChimeraRaw1.getEventInfoUserId());

        fuentusChimeraRaw1.setEventInfoCountry(null);
        assertNull(fuentusChimeraRaw1.getEventInfoCountry());

        fuentusChimeraRaw1.setEventInfoUuaa(null);
        assertNull(fuentusChimeraRaw1.getEventInfoUuaa());

        fuentusChimeraRaw1.setEventInfoOrigin(null);
        assertNull(fuentusChimeraRaw1.getEventInfoOrigin());

        fuentusChimeraRaw1.setEventInfoScanStatus(null);
        assertNull(fuentusChimeraRaw1.getEventInfoScanStatus());

        fuentusChimeraRaw1.setEventInfoBranch(null);
        assertNull(fuentusChimeraRaw1.getEventInfoBranch());

        fuentusChimeraRaw1.setEventInfoProject(null);
        assertNull(fuentusChimeraRaw1.getEventInfoProject());

        fuentusChimeraRaw1.setEventInfoEventId(null);
        assertNull(fuentusChimeraRaw1.getEventInfoEventId());

        fuentusChimeraRaw1.setEventInfoVulnerabilitiesCount(null);
        assertNull(fuentusChimeraRaw1.getEventInfoVulnerabilitiesCount());

        fuentusChimeraRaw1.setEventInfoLinesOfCode(null);
        assertNull(fuentusChimeraRaw1.getEventInfoLinesOfCode());

        fuentusChimeraRaw1.setEventInfoAnalyzer(null);
        assertNull(fuentusChimeraRaw1.getEventInfoAnalyzer());

        fuentusChimeraRaw1.setEventInfoReviewId(null);
        assertNull(fuentusChimeraRaw1.getEventInfoReviewId());

        fuentusChimeraRaw1.setEventInfoLanguage(null);
        assertNull(fuentusChimeraRaw1.getEventInfoLanguage());

        fuentusChimeraRaw1.setEventInfoProduct(null);
        assertNull(fuentusChimeraRaw1.getEventInfoProduct());

        fuentusChimeraRaw1.setEventInfoArea(null);
        assertNull(fuentusChimeraRaw1.getEventInfoArea());

        fuentusChimeraRaw1.setEventInfoArchitecture(null);
        assertNull(fuentusChimeraRaw1.getEventInfoArchitecture());

        fuentusChimeraRaw1.setEventInfoExecutedAt(null);
        assertNull(fuentusChimeraRaw1.getEventInfoExecutedAt());

        fuentusChimeraRaw1.setEventInfoCommitId(null);
        assertNull(fuentusChimeraRaw1.getEventInfoCommitId());

        fuentusChimeraRaw1.setEventInfoPreset(null);
        assertNull(fuentusChimeraRaw1.getEventInfoPreset());

        fuentusChimeraRaw1.setEventInfoDuplicatedId(null);
        assertNull(fuentusChimeraRaw1.getEventInfoDuplicatedId());

        fuentusChimeraRaw1.setSentTimestamp(null);
        assertNull(fuentusChimeraRaw1.getSentTimestamp());

        fuentusChimeraRaw1.setEventIngestedTimestamp(null);
        assertNull(fuentusChimeraRaw1.getEventIngestedTimestamp());

        fuentusChimeraRaw1.setRepo(null);
        assertNull(fuentusChimeraRaw1.getRepo());

        fuentusChimeraRaw1.setRepoUrl(null);
        assertNull(fuentusChimeraRaw1.getRepoUrl());

        fuentusChimeraRaw1.setUser(null);
        assertNull(fuentusChimeraRaw1.getUser());

        fuentusChimeraRaw1.setPeriodStart(null);
        assertNull(fuentusChimeraRaw1.getPeriodStart());
    }

    @Test
    void testEquals_SameObject() {
        // When & Then
        assertEquals(fuentusChimeraRaw1, fuentusChimeraRaw1);
        assertTrue(fuentusChimeraRaw1.equals(fuentusChimeraRaw1));
    }

    @Test
    void testEquals_NullObject() {
        // When & Then
        assertNotEquals(fuentusChimeraRaw1, null);
        assertFalse(fuentusChimeraRaw1.equals(null));
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        String differentObject = "not a FuentusChimeraRaw";

        // When & Then
        assertNotEquals(fuentusChimeraRaw1, differentObject);
        assertFalse(fuentusChimeraRaw1.equals(differentObject));
    }

    @Test
    void testEquals_SameId() {
        // Given
        fuentusChimeraRaw1.setId(1L);
        fuentusChimeraRaw2.setId(1L);

        // When & Then
        assertEquals(fuentusChimeraRaw1, fuentusChimeraRaw2);
        assertTrue(fuentusChimeraRaw1.equals(fuentusChimeraRaw2));
        assertTrue(fuentusChimeraRaw2.equals(fuentusChimeraRaw1));
    }

    @Test
    void testEquals_DifferentId() {
        // Given
        fuentusChimeraRaw1.setId(1L);
        fuentusChimeraRaw2.setId(2L);

        // When & Then
        assertNotEquals(fuentusChimeraRaw1, fuentusChimeraRaw2);
        assertFalse(fuentusChimeraRaw1.equals(fuentusChimeraRaw2));
        assertFalse(fuentusChimeraRaw2.equals(fuentusChimeraRaw1));
    }

    @Test
    void testEquals_NullIds() {
        // Given - both objects have null ids by default

        // When & Then
        assertEquals(fuentusChimeraRaw1, fuentusChimeraRaw2);
        assertTrue(fuentusChimeraRaw1.equals(fuentusChimeraRaw2));
    }

    @Test
    void testEquals_OneNullIdOneNotNull() {
        // Given
        fuentusChimeraRaw1.setId(1L);
        // fuentusChimeraRaw2 has null id by default

        // When & Then
        assertNotEquals(fuentusChimeraRaw1, fuentusChimeraRaw2);
        assertFalse(fuentusChimeraRaw1.equals(fuentusChimeraRaw2));
        assertFalse(fuentusChimeraRaw2.equals(fuentusChimeraRaw1));
    }

    @Test
    void testHashCode_SameValues() {
        // Given
        setupSameValues();

        // When & Then
        assertEquals(fuentusChimeraRaw1.hashCode(), fuentusChimeraRaw2.hashCode());
    }

    @Test
    void testHashCode_NullValues() {
        // Given - all nullable values are null by default

        // When & Then
        assertDoesNotThrow(() -> fuentusChimeraRaw1.hashCode());
        assertEquals(0, fuentusChimeraRaw1.hashCode());
    }

    @Test
    void testHashCode_DifferentValues() {
        // Given
        fuentusChimeraRaw1.setId(1L);
        fuentusChimeraRaw2.setId(2L);

        // When & Then
        assertNotEquals(fuentusChimeraRaw1.hashCode(), fuentusChimeraRaw2.hashCode());
    }

    @Test
    void testHashCode_ConsistentWithEquals() {
        // Given
        fuentusChimeraRaw1.setId(1L);
        fuentusChimeraRaw2.setId(1L);

        // When & Then - if objects are equal, hashCodes must be equal
        assertTrue(fuentusChimeraRaw1.equals(fuentusChimeraRaw2));
        assertEquals(fuentusChimeraRaw1.hashCode(), fuentusChimeraRaw2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        setupSomeValues();

        // When
        String result = fuentusChimeraRaw1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("FuentusChimeraRaw"));
        assertTrue(result.contains("id=1"));
    }

    @Test
    void testToString_NullId() {
        // Given - id is null by default

        // When
        String result = fuentusChimeraRaw1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("FuentusChimeraRaw"));
        assertTrue(result.contains("id=null"));
    }

    @Test
    void testToString_DifferentIds() {
        // Given
        fuentusChimeraRaw1.setId(123L);
        fuentusChimeraRaw2.setId(456L);

        // When
        String result1 = fuentusChimeraRaw1.toString();
        String result2 = fuentusChimeraRaw2.toString();

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertTrue(result1.contains("id=123"));
        assertTrue(result2.contains("id=456"));
        assertNotEquals(result1, result2);
    }

    @Test
    void testSerializable() {
        // Given
        setupSomeValues();

        // When & Then - Test that the class implements Serializable properly
        assertTrue(fuentusChimeraRaw1 instanceof java.io.Serializable);
    }

    @Test
    void testEqualsContract_Reflexive() {
        // Reflexive: x.equals(x) should return true
        assertTrue(fuentusChimeraRaw1.equals(fuentusChimeraRaw1));
    }

    @Test
    void testEqualsContract_Symmetric() {
        // Symmetric: x.equals(y) should return same as y.equals(x)
        setupSameValues();

        boolean result1 = fuentusChimeraRaw1.equals(fuentusChimeraRaw2);
        boolean result2 = fuentusChimeraRaw2.equals(fuentusChimeraRaw1);
        assertEquals(result1, result2);
    }

    @Test
    void testEqualsContract_Consistent() {
        // Consistent: multiple invocations should return same result
        setupSameValues();

        boolean result1 = fuentusChimeraRaw1.equals(fuentusChimeraRaw2);
        boolean result2 = fuentusChimeraRaw1.equals(fuentusChimeraRaw2);
        boolean result3 = fuentusChimeraRaw1.equals(fuentusChimeraRaw2);

        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }

    private void setupSameValues() {
        fuentusChimeraRaw1.setId(1L);
        fuentusChimeraRaw1.setEventInfoCreatedTimestamp(testDate1);
        fuentusChimeraRaw1.setEventInfoIngestedTimestamp(testDate2);
        fuentusChimeraRaw1.setEventInfoRepository("test-repository");
        fuentusChimeraRaw1.setEventInfoUserId("user123");
        fuentusChimeraRaw1.setEventInfoProject("test-project");
        fuentusChimeraRaw1.setEventInfoExecutedAt(testDate3);
        fuentusChimeraRaw1.setSentTimestamp(testDate1);
        fuentusChimeraRaw1.setEventIngestedTimestamp(testDate2);
        fuentusChimeraRaw1.setRepo("test-repo");
        fuentusChimeraRaw1.setUser("testUser");

        fuentusChimeraRaw2.setId(1L);
        fuentusChimeraRaw2.setEventInfoCreatedTimestamp(testDate1);
        fuentusChimeraRaw2.setEventInfoIngestedTimestamp(testDate2);
        fuentusChimeraRaw2.setEventInfoRepository("test-repository");
        fuentusChimeraRaw2.setEventInfoUserId("user123");
        fuentusChimeraRaw2.setEventInfoProject("test-project");
        fuentusChimeraRaw2.setEventInfoExecutedAt(testDate3);
        fuentusChimeraRaw2.setSentTimestamp(testDate1);
        fuentusChimeraRaw2.setEventIngestedTimestamp(testDate2);
        fuentusChimeraRaw2.setRepo("test-repo");
        fuentusChimeraRaw2.setUser("testUser");
    }

    private void setupSomeValues() {
        fuentusChimeraRaw1.setId(1L);
        fuentusChimeraRaw1.setEventInfoCreatedTimestamp(testDate1);
        fuentusChimeraRaw1.setEventInfoIngestedTimestamp(testDate2);
        fuentusChimeraRaw1.setEventInfoRepository("test-repository");
        fuentusChimeraRaw1.setEventInfoUserId("user123");
        fuentusChimeraRaw1.setEventInfoCountry("ES");
        fuentusChimeraRaw1.setEventInfoUuaa("TEST");
        fuentusChimeraRaw1.setEventInfoOrigin("manual");
        fuentusChimeraRaw1.setEventInfoScanStatus("COMPLETED");
        fuentusChimeraRaw1.setEventInfoBranch("main");
        fuentusChimeraRaw1.setEventInfoProject("test-project");
        fuentusChimeraRaw1.setEventInfoVulnerabilitiesCount(10);
        fuentusChimeraRaw1.setEventInfoLinesOfCode(5000);
        fuentusChimeraRaw1.setEventInfoAnalyzer("SonarQube");
        fuentusChimeraRaw1.setEventInfoLanguage("Java");
        fuentusChimeraRaw1.setEventInfoExecutedAt(testDate3);
        fuentusChimeraRaw1.setSentTimestamp(testDate1);
        fuentusChimeraRaw1.setEventIngestedTimestamp(testDate2);
        fuentusChimeraRaw1.setRepo("test-repo");
        fuentusChimeraRaw1.setRepoUrl("https://github.com/test/repo");
        fuentusChimeraRaw1.setUser("testUser");
        fuentusChimeraRaw1.setPeriodStart(testDate3);
    }
}
