package ar.com.bbva.fuentus.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FuentusSonarTest {

    private FuentusSonar fuentusSonar1;
    private FuentusSonar fuentusSonar2;
    private Date testDate;
    private Date testEventTimestamp;

    @BeforeEach
    void setUp() {
        fuentusSonar1 = new FuentusSonar();
        fuentusSonar2 = new FuentusSonar();
        testDate = new Date();
        testEventTimestamp = new Date(testDate.getTime() + 3600000); // +1 hour
    }

    @Test
    void testDefaultConstructor() {
        // Given & When
        FuentusSonar sonar = new FuentusSonar();

        // Then
        assertNotNull(sonar);
        assertNull(sonar.getId());
        assertNull(sonar.getAnalysisKey());
        assertNull(sonar.getPeriodStart());
        assertNull(sonar.getEventTimestamp());
        assertNull(sonar.getSonarKey());
        assertNull(sonar.getSonarInstance());
        assertNull(sonar.getRepoUrl());
        assertNull(sonar.getRepo());
        assertNull(sonar.getRepoFromUrl());
        assertNull(sonar.getBranch());
        assertNull(sonar.getQualityGateName());
        assertNull(sonar.getQualityGateStatus());
        assertNull(sonar.getCommit());
        assertNull(sonar.getGeo());
        assertNull(sonar.getArchitecture());
        assertNull(sonar.getTest());
        assertNull(sonar.getCoverage());
        assertNull(sonar.getNewCodeCoverage());
        assertNull(sonar.getDuplicatedBlocks());
        assertNull(sonar.getDuplicatedLines());
        assertNull(sonar.getReliabilityRating());
        assertNull(sonar.getSecurityRating());
        assertNull(sonar.getSqaleDebtRatio());
        assertNull(sonar.getCognitiveComplexity());
        assertNull(sonar.getNcloc());
        assertNull(sonar.getIssuesSeverityBlocker());
        assertNull(sonar.getIssuesSeverityCritical());
        assertNull(sonar.getIssuesSeverityMajor());
        assertNull(sonar.getIssuesSeverityMinor());
        assertNull(sonar.getIssuesSeverityInfo());
        assertNull(sonar.getIssuesTypeBug());
        assertNull(sonar.getVulnerabilities());
        assertNull(sonar.getIssuesTypeCodeSmell());
    }

    @Test
    void testConstructorWithId() {
        // Given
        Long testId = 123L;

        // When
        FuentusSonar sonar = new FuentusSonar(testId);

        // Then
        assertNotNull(sonar);
        assertEquals(testId, sonar.getId());
        assertNull(sonar.getEventTimestamp());
    }

    @Test
    void testConstructorWithIdAndTimestamp() {
        // Given
        Long testId = 456L;
        Date testTimestamp = new Date();

        // When
        FuentusSonar sonar = new FuentusSonar(testId, testTimestamp);

        // Then
        assertNotNull(sonar);
        assertEquals(testId, sonar.getId());
        assertEquals(testTimestamp, sonar.getEventTimestamp());
    }

    @Test
    void testGettersAndSetters_BasicFields() {
        // Test ID
        Long testId = 1L;
        fuentusSonar1.setId(testId);
        assertEquals(testId, fuentusSonar1.getId());

        // Test AnalysisKey
        String testAnalysisKey = "analysis-key-123";
        fuentusSonar1.setAnalysisKey(testAnalysisKey);
        assertEquals(testAnalysisKey, fuentusSonar1.getAnalysisKey());

        // Test PeriodStart
        fuentusSonar1.setPeriodStart(testDate);
        assertEquals(testDate, fuentusSonar1.getPeriodStart());

        // Test EventTimestamp
        fuentusSonar1.setEventTimestamp(testEventTimestamp);
        assertEquals(testEventTimestamp, fuentusSonar1.getEventTimestamp());

        // Test SonarKey
        String testSonarKey = "com.bbva:test-project";
        fuentusSonar1.setSonarKey(testSonarKey);
        assertEquals(testSonarKey, fuentusSonar1.getSonarKey());

        // Test SonarInstance
        String testSonarInstance = "sonar.bbva.com";
        fuentusSonar1.setSonarInstance(testSonarInstance);
        assertEquals(testSonarInstance, fuentusSonar1.getSonarInstance());
    }

    @Test
    void testGettersAndSetters_RepositoryFields() {
        // Test RepoUrl
        String testRepoUrl = "https://bitbucket.com/bbva/test-repo";
        fuentusSonar1.setRepoUrl(testRepoUrl);
        assertEquals(testRepoUrl, fuentusSonar1.getRepoUrl());

        // Test Repo
        String testRepo = "test-repository";
        fuentusSonar1.setRepo(testRepo);
        assertEquals(testRepo, fuentusSonar1.getRepo());

        // Test RepoFromUrl
        String testRepoFromUrl = "bbva/test-repo";
        fuentusSonar1.setRepoFromUrl(testRepoFromUrl);
        assertEquals(testRepoFromUrl, fuentusSonar1.getRepoFromUrl());

        // Test Branch
        String testBranch = "main";
        fuentusSonar1.setBranch(testBranch);
        assertEquals(testBranch, fuentusSonar1.getBranch());

        // Test Commit
        String testCommit = "abc123def456";
        fuentusSonar1.setCommit(testCommit);
        assertEquals(testCommit, fuentusSonar1.getCommit());
    }

    @Test
    void testGettersAndSetters_TestingAndCoverageFields() {
        // Test Test field
        String testTestField = "Unit tests: 85%";
        fuentusSonar1.setTest(testTestField);
        assertEquals(testTestField, fuentusSonar1.getTest());

        // Test Coverage
        String testCoverage = "75.5%";
        fuentusSonar1.setCoverage(testCoverage);
        assertEquals(testCoverage, fuentusSonar1.getCoverage());

        // Test NewCodeCoverage
        String testNewCodeCoverage = "80.2%";
        fuentusSonar1.setNewCodeCoverage(testNewCodeCoverage);
        assertEquals(testNewCodeCoverage, fuentusSonar1.getNewCodeCoverage());
    }

    @Test
    void testGettersAndSetters_DuplicationFields() {
        // Test DuplicatedBlocks
        Integer testDuplicatedBlocks = 5;
        fuentusSonar1.setDuplicatedBlocks(testDuplicatedBlocks);
        assertEquals(testDuplicatedBlocks, fuentusSonar1.getDuplicatedBlocks());

        // Test DuplicatedLines
        Integer testDuplicatedLines = 150;
        fuentusSonar1.setDuplicatedLines(testDuplicatedLines);
        assertEquals(testDuplicatedLines, fuentusSonar1.getDuplicatedLines());

        // Test with zero values
        fuentusSonar1.setDuplicatedBlocks(0);
        assertEquals(Integer.valueOf(0), fuentusSonar1.getDuplicatedBlocks());

        fuentusSonar1.setDuplicatedLines(0);
        assertEquals(Integer.valueOf(0), fuentusSonar1.getDuplicatedLines());
    }

    @Test
    void testGettersAndSetters_RatingFields() {
        // Test ReliabilityRating
        String testReliabilityRating = "A";
        fuentusSonar1.setReliabilityRating(testReliabilityRating);
        assertEquals(testReliabilityRating, fuentusSonar1.getReliabilityRating());

        // Test SecurityRating
        String testSecurityRating = "B";
        fuentusSonar1.setSecurityRating(testSecurityRating);
        assertEquals(testSecurityRating, fuentusSonar1.getSecurityRating());

        // Test SqaleDebtRatio
        String testSqaleDebtRatio = "10.5%";
        fuentusSonar1.setSqaleDebtRatio(testSqaleDebtRatio);
        assertEquals(testSqaleDebtRatio, fuentusSonar1.getSqaleDebtRatio());

        // Test different rating values
        fuentusSonar1.setReliabilityRating("E");
        assertEquals("E", fuentusSonar1.getReliabilityRating());

        fuentusSonar1.setSecurityRating("C");
        assertEquals("C", fuentusSonar1.getSecurityRating());
    }

    @Test
    void testGettersAndSetters_ComplexityAndSizeFields() {
        // Test CognitiveComplexity
        Integer testCognitiveComplexity = 150;
        fuentusSonar1.setCognitiveComplexity(testCognitiveComplexity);
        assertEquals(testCognitiveComplexity, fuentusSonar1.getCognitiveComplexity());

        // Test Ncloc (Non-comment lines of code)
        Integer testNcloc = 2500;
        fuentusSonar1.setNcloc(testNcloc);
        assertEquals(testNcloc, fuentusSonar1.getNcloc());

        // Test with zero complexity
        fuentusSonar1.setCognitiveComplexity(0);
        assertEquals(Integer.valueOf(0), fuentusSonar1.getCognitiveComplexity());

        // Test with large values
        fuentusSonar1.setNcloc(100000);
        assertEquals(Integer.valueOf(100000), fuentusSonar1.getNcloc());
    }

    @Test
    void testGettersAndSetters_IssuesSeverityFields() {
        // Test IssuesSeverityBlocker
        Integer testBlocker = 2;
        fuentusSonar1.setIssuesSeverityBlocker(testBlocker);
        assertEquals(testBlocker, fuentusSonar1.getIssuesSeverityBlocker());

        // Test IssuesSeverityCritical
        Integer testCritical = 5;
        fuentusSonar1.setIssuesSeverityCritical(testCritical);
        assertEquals(testCritical, fuentusSonar1.getIssuesSeverityCritical());

        // Test IssuesSeverityMajor
        Integer testMajor = 15;
        fuentusSonar1.setIssuesSeverityMajor(testMajor);
        assertEquals(testMajor, fuentusSonar1.getIssuesSeverityMajor());

        // Test IssuesSeverityMinor
        Integer testMinor = 25;
        fuentusSonar1.setIssuesSeverityMinor(testMinor);
        assertEquals(testMinor, fuentusSonar1.getIssuesSeverityMinor());

        // Test IssuesSeverityInfo
        Integer testInfo = 10;
        fuentusSonar1.setIssuesSeverityInfo(testInfo);
        assertEquals(testInfo, fuentusSonar1.getIssuesSeverityInfo());

        // Test with zero values
        fuentusSonar1.setIssuesSeverityBlocker(0);
        assertEquals(Integer.valueOf(0), fuentusSonar1.getIssuesSeverityBlocker());
    }

    @Test
    void testGettersAndSetters_IssuesTypeFields() {
        // Test IssuesTypeBug
        Integer testBugs = 8;
        fuentusSonar1.setIssuesTypeBug(testBugs);
        assertEquals(testBugs, fuentusSonar1.getIssuesTypeBug());

        // Test Vulnerabilities
        Integer testVulnerabilities = 3;
        fuentusSonar1.setVulnerabilities(testVulnerabilities);
        assertEquals(testVulnerabilities, fuentusSonar1.getVulnerabilities());

        // Test IssuesTypeCodeSmell
        Integer testCodeSmells = 45;
        fuentusSonar1.setIssuesTypeCodeSmell(testCodeSmells);
        assertEquals(testCodeSmells, fuentusSonar1.getIssuesTypeCodeSmell());

        // Test with negative values (edge case)
        fuentusSonar1.setVulnerabilities(0);
        assertEquals(Integer.valueOf(0), fuentusSonar1.getVulnerabilities());
    }

    @Test
    void testGettersAndSetters_QualityGateFields() {
        // Test QualityGateName
        String testQualityGateName = "BBVA Default";
        fuentusSonar1.setQualityGateName(testQualityGateName);
        assertEquals(testQualityGateName, fuentusSonar1.getQualityGateName());

        // Test QualityGateStatus
        String testQualityGateStatus = "OK";
        fuentusSonar1.setQualityGateStatus(testQualityGateStatus);
        assertEquals(testQualityGateStatus, fuentusSonar1.getQualityGateStatus());

        // Test different statuses
        fuentusSonar1.setQualityGateStatus("ERROR");
        assertEquals("ERROR", fuentusSonar1.getQualityGateStatus());

        fuentusSonar1.setQualityGateStatus("WARN");
        assertEquals("WARN", fuentusSonar1.getQualityGateStatus());
    }

    @Test
    void testGettersAndSetters_LocationFields() {
        // Test Geo
        String testGeo = "ES";
        fuentusSonar1.setGeo(testGeo);
        assertEquals(testGeo, fuentusSonar1.getGeo());

        // Test Architecture
        String testArchitecture = "microservices";
        fuentusSonar1.setArchitecture(testArchitecture);
        assertEquals(testArchitecture, fuentusSonar1.getArchitecture());

        // Test different architectures
        fuentusSonar1.setArchitecture("monolith");
        assertEquals("monolith", fuentusSonar1.getArchitecture());

        fuentusSonar1.setArchitecture("serverless");
        assertEquals("serverless", fuentusSonar1.getArchitecture());
    }

    @Test
    void testGettersAndSetters_NullValues() {
        // Test setting null values for all nullable fields
        fuentusSonar1.setId(null);
        assertNull(fuentusSonar1.getId());

        fuentusSonar1.setAnalysisKey(null);
        assertNull(fuentusSonar1.getAnalysisKey());

        fuentusSonar1.setPeriodStart(null);
        assertNull(fuentusSonar1.getPeriodStart());

        fuentusSonar1.setEventTimestamp(null);
        assertNull(fuentusSonar1.getEventTimestamp());

        fuentusSonar1.setSonarKey(null);
        assertNull(fuentusSonar1.getSonarKey());

        fuentusSonar1.setSonarInstance(null);
        assertNull(fuentusSonar1.getSonarInstance());

        fuentusSonar1.setRepoUrl(null);
        assertNull(fuentusSonar1.getRepoUrl());

        fuentusSonar1.setRepo(null);
        assertNull(fuentusSonar1.getRepo());

        fuentusSonar1.setRepoFromUrl(null);
        assertNull(fuentusSonar1.getRepoFromUrl());

        fuentusSonar1.setBranch(null);
        assertNull(fuentusSonar1.getBranch());

        fuentusSonar1.setQualityGateName(null);
        assertNull(fuentusSonar1.getQualityGateName());

        fuentusSonar1.setQualityGateStatus(null);
        assertNull(fuentusSonar1.getQualityGateStatus());

        fuentusSonar1.setCommit(null);
        assertNull(fuentusSonar1.getCommit());

        fuentusSonar1.setGeo(null);
        assertNull(fuentusSonar1.getGeo());

        fuentusSonar1.setArchitecture(null);
        assertNull(fuentusSonar1.getArchitecture());

        // Test null values for new fields
        fuentusSonar1.setTest(null);
        assertNull(fuentusSonar1.getTest());

        fuentusSonar1.setCoverage(null);
        assertNull(fuentusSonar1.getCoverage());

        fuentusSonar1.setNewCodeCoverage(null);
        assertNull(fuentusSonar1.getNewCodeCoverage());

        fuentusSonar1.setDuplicatedBlocks(null);
        assertNull(fuentusSonar1.getDuplicatedBlocks());

        fuentusSonar1.setDuplicatedLines(null);
        assertNull(fuentusSonar1.getDuplicatedLines());

        fuentusSonar1.setReliabilityRating(null);
        assertNull(fuentusSonar1.getReliabilityRating());

        fuentusSonar1.setSecurityRating(null);
        assertNull(fuentusSonar1.getSecurityRating());

        fuentusSonar1.setSqaleDebtRatio(null);
        assertNull(fuentusSonar1.getSqaleDebtRatio());

        fuentusSonar1.setCognitiveComplexity(null);
        assertNull(fuentusSonar1.getCognitiveComplexity());

        fuentusSonar1.setNcloc(null);
        assertNull(fuentusSonar1.getNcloc());

        fuentusSonar1.setIssuesSeverityBlocker(null);
        assertNull(fuentusSonar1.getIssuesSeverityBlocker());

        fuentusSonar1.setIssuesSeverityCritical(null);
        assertNull(fuentusSonar1.getIssuesSeverityCritical());

        fuentusSonar1.setIssuesSeverityMajor(null);
        assertNull(fuentusSonar1.getIssuesSeverityMajor());

        fuentusSonar1.setIssuesSeverityMinor(null);
        assertNull(fuentusSonar1.getIssuesSeverityMinor());

        fuentusSonar1.setIssuesSeverityInfo(null);
        assertNull(fuentusSonar1.getIssuesSeverityInfo());

        fuentusSonar1.setIssuesTypeBug(null);
        assertNull(fuentusSonar1.getIssuesTypeBug());

        fuentusSonar1.setVulnerabilities(null);
        assertNull(fuentusSonar1.getVulnerabilities());

        fuentusSonar1.setIssuesTypeCodeSmell(null);
        assertNull(fuentusSonar1.getIssuesTypeCodeSmell());
    }

    @Test
    void testGettersAndSetters_EdgeCases() {
        // Test with empty strings
        fuentusSonar1.setAnalysisKey("");
        assertEquals("", fuentusSonar1.getAnalysisKey());

        fuentusSonar1.setSonarKey("");
        assertEquals("", fuentusSonar1.getSonarKey());

        fuentusSonar1.setRepo("");
        assertEquals("", fuentusSonar1.getRepo());

        // Test with special characters in URLs
        String specialUrl = "https://bitbucket.com/bbva/test-repo_with-special-chars?at=refs%2Fheads%2Ffeature%2Ftest";
        fuentusSonar1.setRepoUrl(specialUrl);
        assertEquals(specialUrl, fuentusSonar1.getRepoUrl());

        // Test with long commit hash
        String longCommitHash = "abcdef1234567890abcdef1234567890abcdef12";
        fuentusSonar1.setCommit(longCommitHash);
        assertEquals(longCommitHash, fuentusSonar1.getCommit());

        // Test with complex sonar key
        String complexSonarKey = "com.bbva.microservices:test-service-api:feature/JIRA-123";
        fuentusSonar1.setSonarKey(complexSonarKey);
        assertEquals(complexSonarKey, fuentusSonar1.getSonarKey());

        // Test edge cases for numeric fields
        fuentusSonar1.setDuplicatedBlocks(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, fuentusSonar1.getDuplicatedBlocks().intValue());

        fuentusSonar1.setNcloc(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, fuentusSonar1.getNcloc().intValue());

        // Test empty strings for new fields
        fuentusSonar1.setCoverage("");
        assertEquals("", fuentusSonar1.getCoverage());

        fuentusSonar1.setReliabilityRating("");
        assertEquals("", fuentusSonar1.getReliabilityRating());

        fuentusSonar1.setTest("");
        assertEquals("", fuentusSonar1.getTest());
    }

    @Test
    void testEquals_WithDifferentIds() {
        // Given
        fuentusSonar1.setId(1L);
        fuentusSonar2.setId(2L);

        // When & Then
        assertNotEquals(fuentusSonar1, fuentusSonar2);
        assertFalse(fuentusSonar1.equals(fuentusSonar2));
    }

    @Test
    void testEquals_WithNullIds() {
        // Given
        fuentusSonar1.setId(null);
        fuentusSonar2.setId(1L);

        // When & Then
        assertNotEquals(fuentusSonar1, fuentusSonar2);
        assertFalse(fuentusSonar1.equals(fuentusSonar2));

        // And vice versa
        fuentusSonar1.setId(1L);
        fuentusSonar2.setId(null);
        assertNotEquals(fuentusSonar1, fuentusSonar2);
        assertFalse(fuentusSonar1.equals(fuentusSonar2));

        // Both null
        fuentusSonar1.setId(null);
        fuentusSonar2.setId(null);
        assertEquals(fuentusSonar1, fuentusSonar2);
        assertTrue(fuentusSonar1.equals(fuentusSonar2));
    }

    @Test
    void testEquals_SameObject() {
        // When & Then
        assertEquals(fuentusSonar1, fuentusSonar1);
        assertTrue(fuentusSonar1.equals(fuentusSonar1));
    }

    @Test
    void testEquals_NullObject() {
        // When & Then
        assertNotEquals(fuentusSonar1, null);
        assertFalse(fuentusSonar1.equals(null));
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        String differentObject = "not a FuentusSonar";

        // When & Then
        assertNotEquals(fuentusSonar1, differentObject);
        assertFalse(fuentusSonar1.equals(differentObject));
    }

    @Test
    void testHashCode_SameValues() {
        // Given
        setupSameValues();

        // When & Then
        assertEquals(fuentusSonar1.hashCode(), fuentusSonar2.hashCode());
    }

    @Test
    void testHashCode_NullValues() {
        // Given - all nullable values are null by default

        // When & Then
        assertDoesNotThrow(() -> fuentusSonar1.hashCode());
    }

    @Test
    void testToString() {
        // Given
        setupSomeValues();

        // When
        String result = fuentusSonar1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("FuentusSonar"));
    }

    @Test
    void testSerializable() {
        // Given
        setupSomeValues();

        // When & Then - Test that the class implements Serializable properly
        assertTrue(fuentusSonar1 instanceof java.io.Serializable);
    }

    @Test
    void testEqualsContract_Reflexive() {
        // Reflexive: x.equals(x) should return true
        assertTrue(fuentusSonar1.equals(fuentusSonar1));
    }

    @Test
    void testEqualsContract_Symmetric() {
        // Symmetric: x.equals(y) should return same as y.equals(x)
        setupSameValues();

        boolean result1 = fuentusSonar1.equals(fuentusSonar2);
        boolean result2 = fuentusSonar2.equals(fuentusSonar1);
        assertEquals(result1, result2);
    }

    @Test
    void testEqualsContract_Consistent() {
        // Consistent: multiple invocations should return same result
        setupSameValues();

        boolean result1 = fuentusSonar1.equals(fuentusSonar2);
        boolean result2 = fuentusSonar1.equals(fuentusSonar2);
        boolean result3 = fuentusSonar1.equals(fuentusSonar2);

        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }

    private void setupSameValues() {
        fuentusSonar1.setId(1L);
        fuentusSonar1.setAnalysisKey("analysis-key-123");
        fuentusSonar1.setPeriodStart(testDate);
        fuentusSonar1.setEventTimestamp(testEventTimestamp);
        fuentusSonar1.setSonarKey("com.bbva:test-project");
        fuentusSonar1.setSonarInstance("sonar.bbva.com");
        fuentusSonar1.setRepoUrl("https://bitbucket.com/bbva/test-repo");
        fuentusSonar1.setRepo("test-repository");

        fuentusSonar2.setId(1L);
        fuentusSonar2.setAnalysisKey("analysis-key-123");
        fuentusSonar2.setPeriodStart(testDate);
        fuentusSonar2.setEventTimestamp(testEventTimestamp);
        fuentusSonar2.setSonarKey("com.bbva:test-project");
        fuentusSonar2.setSonarInstance("sonar.bbva.com");
        fuentusSonar2.setRepoUrl("https://bitbucket.com/bbva/test-repo");
        fuentusSonar2.setRepo("test-repository");
    }

    private void setupSomeValues() {
        fuentusSonar1.setId(1L);
        fuentusSonar1.setAnalysisKey("analysis-key-123");
        fuentusSonar1.setPeriodStart(testDate);
        fuentusSonar1.setEventTimestamp(testEventTimestamp);
        fuentusSonar1.setSonarKey("com.bbva:test-project");
        fuentusSonar1.setSonarInstance("sonar.bbva.com");
        fuentusSonar1.setRepoUrl("https://bitbucket.com/bbva/test-repo");
        fuentusSonar1.setRepo("test-repository");
        fuentusSonar1.setRepoFromUrl("bbva/test-repo");
        fuentusSonar1.setBranch("main");
        fuentusSonar1.setQualityGateName("BBVA Default");
        fuentusSonar1.setQualityGateStatus("OK");
        fuentusSonar1.setCommit("abc123def456");
        fuentusSonar1.setGeo("ES");
        fuentusSonar1.setArchitecture("microservices");
        fuentusSonar1.setTest("Unit tests: 85%");
        fuentusSonar1.setCoverage("75.5%");
        fuentusSonar1.setNewCodeCoverage("80.2%");
        fuentusSonar1.setDuplicatedBlocks(5);
        fuentusSonar1.setDuplicatedLines(150);
        fuentusSonar1.setReliabilityRating("A");
        fuentusSonar1.setSecurityRating("B");
        fuentusSonar1.setSqaleDebtRatio("10.5%");
        fuentusSonar1.setCognitiveComplexity(150);
        fuentusSonar1.setNcloc(2500);
        fuentusSonar1.setIssuesSeverityBlocker(2);
        fuentusSonar1.setIssuesSeverityCritical(5);
        fuentusSonar1.setIssuesSeverityMajor(15);
        fuentusSonar1.setIssuesSeverityMinor(25);
        fuentusSonar1.setIssuesSeverityInfo(10);
        fuentusSonar1.setIssuesTypeBug(8);
        fuentusSonar1.setVulnerabilities(3);
        fuentusSonar1.setIssuesTypeCodeSmell(45);
    }
}
