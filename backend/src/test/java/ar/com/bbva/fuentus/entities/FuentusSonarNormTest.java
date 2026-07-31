package ar.com.bbva.fuentus.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FuentusSonarNormTest {

    private FuentusSonarNorm fuentusSonarNorm1;
    private FuentusSonarNorm fuentusSonarNorm2;
    private Date testIngestedTimestamp;
    private Date testCreatedTimestamp;
    private Date testEventIngestedTimestamp;
    private Date testEventTimestamp;
    private Date testPeriodStart;

    @BeforeEach
    void setUp() {
        fuentusSonarNorm1 = new FuentusSonarNorm();
        fuentusSonarNorm2 = new FuentusSonarNorm();
        testIngestedTimestamp = new Date();
        testCreatedTimestamp = new Date(testIngestedTimestamp.getTime() + 3600000); // +1 hour
        testEventIngestedTimestamp = new Date(testCreatedTimestamp.getTime() + 3600000); // +2 hours
        testEventTimestamp = new Date(testEventIngestedTimestamp.getTime() + 3600000); // +3 hours
        testPeriodStart = new Date(testEventTimestamp.getTime() + 3600000); // +4 hours
    }

    @Test
    void testDefaultConstructor() {
        // Given & When
        FuentusSonarNorm sonarNorm = new FuentusSonarNorm();

        // Then
        assertNotNull(sonarNorm);
        assertNull(sonarNorm.getId());
        assertNull(sonarNorm.getEventInfoScmProject());
        assertNull(sonarNorm.getEventInfoScmRepository());
        assertNull(sonarNorm.getEventInfoScmUrl());
        assertNull(sonarNorm.getEventInfoScmBranch());
        assertNull(sonarNorm.getEventInfoIngestedTimestamp());
        assertNull(sonarNorm.getEventInfoCreatedTimestamp());
        assertNull(sonarNorm.getEventInfoUserId());
        assertNull(sonarNorm.getEventInfoEventId());
        assertNull(sonarNorm.getEventInfoRepoId());
        assertNull(sonarNorm.getProjectFromUrl());
        assertNull(sonarNorm.getRepoFromUrl());
        assertNull(sonarNorm.getEventIngestedTimestamp());
        assertNull(sonarNorm.getEventTimestamp());
        assertNull(sonarNorm.getUser());
        assertNull(sonarNorm.getRepo());
        assertNull(sonarNorm.getRepoUrl());
        assertNull(sonarNorm.getUuaa());
        assertNull(sonarNorm.getCommit());
        assertNull(sonarNorm.getEventType());
        assertNull(sonarNorm.getPeriodStart());
        assertNull(sonarNorm.getIssuesTypeBug());
        assertNull(sonarNorm.getIssuesTypeCodeSmell());
        assertNull(sonarNorm.getIssuesTypeVulnerability());
        assertNull(sonarNorm.getIssuesSeverityBlocker());
        assertNull(sonarNorm.getIssuesSeverityCritical());
        assertNull(sonarNorm.getIssuesSeverityInfo());
        assertNull(sonarNorm.getIssuesSeverityMinor());
        assertNull(sonarNorm.getIssuesSeverityMajor());
        assertNull(sonarNorm.getQualityGateStatus());
        assertNull(sonarNorm.getQualityGateName());
        assertNull(sonarNorm.getNcloc());
        assertNull(sonarNorm.getCognitiveComplexity());
        assertNull(sonarNorm.getSqaleDebtRatio());
        assertNull(sonarNorm.getSecurityRating());
        assertNull(sonarNorm.getReliabilityRating());
        assertNull(sonarNorm.getDuplicatedLines());
        assertNull(sonarNorm.getDuplicatedBlocks());
        assertNull(sonarNorm.getCoverage());
        assertNull(sonarNorm.getSonarInstance());
        assertNull(sonarNorm.getTests());
        assertNull(sonarNorm.getComplexity());
        assertNull(sonarNorm.getNewCoverage());
        assertNull(sonarNorm.getSonarKey());
        assertNull(sonarNorm.getAnalysisKey());
        assertNull(sonarNorm.getProjectMaturity());
        assertNull(sonarNorm.getUncoveredLines());
        assertNull(sonarNorm.getLinesToCover());
        assertNull(sonarNorm.getLineCoverage());
        assertNull(sonarNorm.getSqaleRating());
        assertNull(sonarNorm.getRepoId());
        assertNull(sonarNorm.getNewSqaleDebtRatio());
        assertNull(sonarNorm.getNewTechnicalDebt());
        assertNull(sonarNorm.getSqaleIndex());
        assertNull(sonarNorm.getNewCodeSmells());
        assertNull(sonarNorm.getCodeSmells());
    }

    @Test
    void testConstructorWithId() {
        // Given
        Long testId = 123L;

        // When
        FuentusSonarNorm sonarNorm = new FuentusSonarNorm(testId);

        // Then
        assertNotNull(sonarNorm);
        assertEquals(testId, sonarNorm.getId());
        assertNull(sonarNorm.getEventInfoScmProject());
        assertNull(sonarNorm.getEventInfoIngestedTimestamp());
    }

    @Test
    void testConstructorWithAllRequiredFields() {
        // Given
        Long testId = 456L;

        // When
        FuentusSonarNorm sonarNorm = new FuentusSonarNorm(testId, testIngestedTimestamp,
            testCreatedTimestamp, testEventIngestedTimestamp, testEventTimestamp);

        // Then
        assertNotNull(sonarNorm);
        assertEquals(testId, sonarNorm.getId());
        assertEquals(testIngestedTimestamp, sonarNorm.getEventInfoIngestedTimestamp());
        assertEquals(testCreatedTimestamp, sonarNorm.getEventInfoCreatedTimestamp());
        assertEquals(testEventIngestedTimestamp, sonarNorm.getEventIngestedTimestamp());
        assertEquals(testEventTimestamp, sonarNorm.getEventTimestamp());
    }

    @Test
    void testGettersAndSetters_BasicFields() {
        // Test ID
        Long testId = 1L;
        fuentusSonarNorm1.setId(testId);
        assertEquals(testId, fuentusSonarNorm1.getId());

        // Test EventInfoScmProject
        String testScmProject = "test-project";
        fuentusSonarNorm1.setEventInfoScmProject(testScmProject);
        assertEquals(testScmProject, fuentusSonarNorm1.getEventInfoScmProject());

        // Test EventInfoScmRepository
        String testScmRepository = "test-repository";
        fuentusSonarNorm1.setEventInfoScmRepository(testScmRepository);
        assertEquals(testScmRepository, fuentusSonarNorm1.getEventInfoScmRepository());

        // Test EventInfoScmUrl
        String testScmUrl = "https://bitbucket.com/bbva/test-repo";
        fuentusSonarNorm1.setEventInfoScmUrl(testScmUrl);
        assertEquals(testScmUrl, fuentusSonarNorm1.getEventInfoScmUrl());

        // Test EventInfoScmBranch
        String testScmBranch = "main";
        fuentusSonarNorm1.setEventInfoScmBranch(testScmBranch);
        assertEquals(testScmBranch, fuentusSonarNorm1.getEventInfoScmBranch());
    }

    @Test
    void testGettersAndSetters_TimestampFields() {
        // Test EventInfoIngestedTimestamp
        fuentusSonarNorm1.setEventInfoIngestedTimestamp(testIngestedTimestamp);
        assertEquals(testIngestedTimestamp, fuentusSonarNorm1.getEventInfoIngestedTimestamp());

        // Test EventInfoCreatedTimestamp
        fuentusSonarNorm1.setEventInfoCreatedTimestamp(testCreatedTimestamp);
        assertEquals(testCreatedTimestamp, fuentusSonarNorm1.getEventInfoCreatedTimestamp());

        // Test EventIngestedTimestamp
        fuentusSonarNorm1.setEventIngestedTimestamp(testEventIngestedTimestamp);
        assertEquals(testEventIngestedTimestamp, fuentusSonarNorm1.getEventIngestedTimestamp());

        // Test EventTimestamp
        fuentusSonarNorm1.setEventTimestamp(testEventTimestamp);
        assertEquals(testEventTimestamp, fuentusSonarNorm1.getEventTimestamp());
    }

    @Test
    void testGettersAndSetters_EventFields() {
        // Test EventInfoUserId
        String testUserId = "user123";
        fuentusSonarNorm1.setEventInfoUserId(testUserId);
        assertEquals(testUserId, fuentusSonarNorm1.getEventInfoUserId());

        // Test EventInfoEventId
        String testEventId = "event-123-456";
        fuentusSonarNorm1.setEventInfoEventId(testEventId);
        assertEquals(testEventId, fuentusSonarNorm1.getEventInfoEventId());

        // Test EventInfoRepoId
        String testRepoId = "repo-789";
        fuentusSonarNorm1.setEventInfoRepoId(testRepoId);
        assertEquals(testRepoId, fuentusSonarNorm1.getEventInfoRepoId());

        // Test ProjectFromUrl
        String testProjectFromUrl = "bbva/test-project";
        fuentusSonarNorm1.setProjectFromUrl(testProjectFromUrl);
        assertEquals(testProjectFromUrl, fuentusSonarNorm1.getProjectFromUrl());

        // Test RepoFromUrl
        String testRepoFromUrl = "bbva/test-repo";
        fuentusSonarNorm1.setRepoFromUrl(testRepoFromUrl);
        assertEquals(testRepoFromUrl, fuentusSonarNorm1.getRepoFromUrl());
    }

    @Test
    void testGettersAndSetters_NullValues() {
        // Test setting null values for all nullable fields
        fuentusSonarNorm1.setId(null);
        assertNull(fuentusSonarNorm1.getId());

        fuentusSonarNorm1.setEventInfoScmProject(null);
        assertNull(fuentusSonarNorm1.getEventInfoScmProject());

        fuentusSonarNorm1.setEventInfoScmRepository(null);
        assertNull(fuentusSonarNorm1.getEventInfoScmRepository());

        fuentusSonarNorm1.setEventInfoScmUrl(null);
        assertNull(fuentusSonarNorm1.getEventInfoScmUrl());

        fuentusSonarNorm1.setEventInfoScmBranch(null);
        assertNull(fuentusSonarNorm1.getEventInfoScmBranch());

        fuentusSonarNorm1.setEventInfoIngestedTimestamp(null);
        assertNull(fuentusSonarNorm1.getEventInfoIngestedTimestamp());

        fuentusSonarNorm1.setEventInfoCreatedTimestamp(null);
        assertNull(fuentusSonarNorm1.getEventInfoCreatedTimestamp());

        fuentusSonarNorm1.setEventInfoUserId(null);
        assertNull(fuentusSonarNorm1.getEventInfoUserId());

        fuentusSonarNorm1.setEventInfoEventId(null);
        assertNull(fuentusSonarNorm1.getEventInfoEventId());

        fuentusSonarNorm1.setEventInfoRepoId(null);
        assertNull(fuentusSonarNorm1.getEventInfoRepoId());

        fuentusSonarNorm1.setProjectFromUrl(null);
        assertNull(fuentusSonarNorm1.getProjectFromUrl());

        fuentusSonarNorm1.setRepoFromUrl(null);
        assertNull(fuentusSonarNorm1.getRepoFromUrl());

        fuentusSonarNorm1.setEventIngestedTimestamp(null);
        assertNull(fuentusSonarNorm1.getEventIngestedTimestamp());

        fuentusSonarNorm1.setEventTimestamp(null);
        assertNull(fuentusSonarNorm1.getEventTimestamp());
    }

    @Test
    void testGettersAndSetters_EdgeCases() {
        // Test with empty strings
        fuentusSonarNorm1.setEventInfoScmProject("");
        assertEquals("", fuentusSonarNorm1.getEventInfoScmProject());

        fuentusSonarNorm1.setEventInfoScmRepository("");
        assertEquals("", fuentusSonarNorm1.getEventInfoScmRepository());

        fuentusSonarNorm1.setEventInfoUserId("");
        assertEquals("", fuentusSonarNorm1.getEventInfoUserId());

        // Test with complex URLs
        String complexUrl = "https://bitbucket.com/bbva/project-with-special-chars_123/test-repo?at=refs%2Fheads%2Ffeature%2FJIRA-123";
        fuentusSonarNorm1.setEventInfoScmUrl(complexUrl);
        assertEquals(complexUrl, fuentusSonarNorm1.getEventInfoScmUrl());

        // Test with long event IDs
        String longEventId = "event-" + new String(new char[100]).replace('\0', 'a');
        fuentusSonarNorm1.setEventInfoEventId(longEventId);
        assertEquals(longEventId, fuentusSonarNorm1.getEventInfoEventId());

        // Test with feature branch names
        String featureBranch = "feature/JIRA-123-implement-new-functionality";
        fuentusSonarNorm1.setEventInfoScmBranch(featureBranch);
        assertEquals(featureBranch, fuentusSonarNorm1.getEventInfoScmBranch());

        // Test with complex project paths
        String complexProjectPath = "group/subgroup/project-name";
        fuentusSonarNorm1.setProjectFromUrl(complexProjectPath);
        assertEquals(complexProjectPath, fuentusSonarNorm1.getProjectFromUrl());
    }

    @Test
    void testTimestampFields_Ordering() {
        // Test that we can set timestamps in different orders
        fuentusSonarNorm1.setEventInfoIngestedTimestamp(testIngestedTimestamp);
        fuentusSonarNorm1.setEventInfoCreatedTimestamp(testCreatedTimestamp);
        fuentusSonarNorm1.setEventIngestedTimestamp(testEventIngestedTimestamp);
        fuentusSonarNorm1.setEventTimestamp(testEventTimestamp);

        // Verify all timestamps are set correctly
        assertEquals(testIngestedTimestamp, fuentusSonarNorm1.getEventInfoIngestedTimestamp());
        assertEquals(testCreatedTimestamp, fuentusSonarNorm1.getEventInfoCreatedTimestamp());
        assertEquals(testEventIngestedTimestamp, fuentusSonarNorm1.getEventIngestedTimestamp());
        assertEquals(testEventTimestamp, fuentusSonarNorm1.getEventTimestamp());
    }

    @Test
    void testEquals_SameObject() {
        // When & Then
        assertEquals(fuentusSonarNorm1, fuentusSonarNorm1);
    }

    @Test
    void testEquals_NullObject() {
        // When & Then
        assertNotEquals(null, fuentusSonarNorm1);
        assertNotEquals(fuentusSonarNorm1, null);
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        String differentObject = "not a FuentusSonarNorm";

        // When & Then
        assertNotEquals(differentObject, fuentusSonarNorm1);
    }

    @Test
    void testHashCode_SameValues() {
        // Given
        setupSameValues();

        // When & Then
        assertEquals(fuentusSonarNorm1.hashCode(), fuentusSonarNorm2.hashCode());
    }

    @Test
    void testHashCode_NullValues() {
        // Given - all nullable values are null by default

        // When & Then
        assertDoesNotThrow(() -> fuentusSonarNorm1.hashCode());
    }

    @Test
    void testToString() {
        // Given
        setupSomeValues();

        // When
        String result = fuentusSonarNorm1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("FuentusSonarNorm"));
    }

    @Test
    void testSerializable() {
        // Given
        setupSomeValues();

        // When & Then - Test that the class implements Serializable properly
        assertInstanceOf(java.io.Serializable.class, fuentusSonarNorm1);
    }

    @Test
    void testEqualsContract_Reflexive() {
        // Reflexive: x.equals(x) should return true
        assertEquals(fuentusSonarNorm1, fuentusSonarNorm1);
    }

    @Test
    void testEqualsContract_Symmetric() {
        // Symmetric: x.equals(y) should return same as y.equals(x)
        setupSameValues();

        boolean result1 = fuentusSonarNorm1.equals(fuentusSonarNorm2);
        boolean result2 = fuentusSonarNorm2.equals(fuentusSonarNorm1);
        assertEquals(result1, result2);
    }

    @Test
    void testEqualsContract_Consistent() {
        // Consistent: multiple invocations should return same result
        setupSameValues();

        boolean result1 = fuentusSonarNorm1.equals(fuentusSonarNorm2);
        boolean result2 = fuentusSonarNorm1.equals(fuentusSonarNorm2);
        boolean result3 = fuentusSonarNorm1.equals(fuentusSonarNorm2);

        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }

    private void setupSameValues() {
        fuentusSonarNorm1.setId(1L);
        fuentusSonarNorm1.setEventInfoScmProject("test-project");
        fuentusSonarNorm1.setEventInfoScmRepository("test-repository");
        fuentusSonarNorm1.setEventInfoScmUrl("https://bitbucket.com/bbva/test-repo");
        fuentusSonarNorm1.setEventInfoScmBranch("main");
        fuentusSonarNorm1.setEventInfoIngestedTimestamp(testIngestedTimestamp);
        fuentusSonarNorm1.setEventInfoCreatedTimestamp(testCreatedTimestamp);

        fuentusSonarNorm2.setId(1L);
        fuentusSonarNorm2.setEventInfoScmProject("test-project");
        fuentusSonarNorm2.setEventInfoScmRepository("test-repository");
        fuentusSonarNorm2.setEventInfoScmUrl("https://bitbucket.com/bbva/test-repo");
        fuentusSonarNorm2.setEventInfoScmBranch("main");
        fuentusSonarNorm2.setEventInfoIngestedTimestamp(testIngestedTimestamp);
        fuentusSonarNorm2.setEventInfoCreatedTimestamp(testCreatedTimestamp);
    }

    private void setupSomeValues() {
        fuentusSonarNorm1.setId(1L);
        fuentusSonarNorm1.setEventInfoScmProject("test-project");
        fuentusSonarNorm1.setEventInfoScmRepository("test-repository");
        fuentusSonarNorm1.setEventInfoScmUrl("https://bitbucket.com/bbva/test-repo");
        fuentusSonarNorm1.setEventInfoScmBranch("main");
        fuentusSonarNorm1.setEventInfoIngestedTimestamp(testIngestedTimestamp);
        fuentusSonarNorm1.setEventInfoCreatedTimestamp(testCreatedTimestamp);
        fuentusSonarNorm1.setEventInfoUserId("user123");
        fuentusSonarNorm1.setEventInfoEventId("event-123-456");
        fuentusSonarNorm1.setEventInfoRepoId("repo-789");
        fuentusSonarNorm1.setProjectFromUrl("bbva/test-project");
        fuentusSonarNorm1.setRepoFromUrl("bbva/test-repo");
        fuentusSonarNorm1.setEventIngestedTimestamp(testEventIngestedTimestamp);
        fuentusSonarNorm1.setEventTimestamp(testEventTimestamp);
        fuentusSonarNorm1.setUser("john.doe");
        fuentusSonarNorm1.setRepo("my-awesome-repo");
        fuentusSonarNorm1.setRepoUrl("https://github.com/bbva/my-repo.git");
        fuentusSonarNorm1.setUuaa("BBVA001");
        fuentusSonarNorm1.setCommit("abc123def456");
        fuentusSonarNorm1.setEventType("PUSH");
        fuentusSonarNorm1.setPeriodStart(testPeriodStart);
        fuentusSonarNorm1.setIssuesTypeBug(5);
        fuentusSonarNorm1.setIssuesTypeCodeSmell(15);
        fuentusSonarNorm1.setIssuesTypeVulnerability(3);
        fuentusSonarNorm1.setIssuesSeverityBlocker(1);
        fuentusSonarNorm1.setIssuesSeverityCritical(2);
        fuentusSonarNorm1.setIssuesSeverityInfo(10);
        fuentusSonarNorm1.setIssuesSeverityMinor(8);
        fuentusSonarNorm1.setIssuesSeverityMajor(4);
        fuentusSonarNorm1.setQualityGateStatus("PASSED");
        fuentusSonarNorm1.setQualityGateName("BBVA Quality Gate");
        fuentusSonarNorm1.setNcloc(1500);
        fuentusSonarNorm1.setCognitiveComplexity(25);
        fuentusSonarNorm1.setSqaleDebtRatio("5.2%");
        fuentusSonarNorm1.setSecurityRating(1);
        fuentusSonarNorm1.setReliabilityRating(2);
        fuentusSonarNorm1.setDuplicatedLines(50);
        fuentusSonarNorm1.setDuplicatedBlocks(3);
        fuentusSonarNorm1.setCoverage("85.5%");
        fuentusSonarNorm1.setSonarInstance("https://sonar.bbva.com");
        fuentusSonarNorm1.setTests(150);
        fuentusSonarNorm1.setComplexity(45);
        fuentusSonarNorm1.setNewCoverage("90.2%");
        fuentusSonarNorm1.setSonarKey("bbva:my-project:main");
        fuentusSonarNorm1.setAnalysisKey("analysis-123-456");
        fuentusSonarNorm1.setProjectMaturity("MATURE");
        fuentusSonarNorm1.setUncoveredLines(120);
        fuentusSonarNorm1.setLinesToCover(800);
        fuentusSonarNorm1.setLineCoverage("88.7%");
        fuentusSonarNorm1.setSqaleRating("A");
        fuentusSonarNorm1.setRepoId("repo-456-789");
        fuentusSonarNorm1.setNewSqaleDebtRatio("2.1%");
        fuentusSonarNorm1.setNewTechnicalDebt("1h 30min");
        fuentusSonarNorm1.setSqaleIndex(125);
        fuentusSonarNorm1.setNewCodeSmells(7);
        fuentusSonarNorm1.setCodeSmells(25);
    }

    @Test
    void testGettersAndSetters_UserAndRepoFields() {
        // Test User
        String testUser = "john.doe";
        fuentusSonarNorm1.setUser(testUser);
        assertEquals(testUser, fuentusSonarNorm1.getUser());

        // Test Repo
        String testRepo = "my-awesome-repo";
        fuentusSonarNorm1.setRepo(testRepo);
        assertEquals(testRepo, fuentusSonarNorm1.getRepo());

        // Test RepoUrl
        String testRepoUrl = "https://github.com/bbva/my-repo.git";
        fuentusSonarNorm1.setRepoUrl(testRepoUrl);
        assertEquals(testRepoUrl, fuentusSonarNorm1.getRepoUrl());

        // Test Uuaa
        String testUuaa = "BBVA001";
        fuentusSonarNorm1.setUuaa(testUuaa);
        assertEquals(testUuaa, fuentusSonarNorm1.getUuaa());

        // Test Commit
        String testCommit = "abc123def456";
        fuentusSonarNorm1.setCommit(testCommit);
        assertEquals(testCommit, fuentusSonarNorm1.getCommit());

        // Test EventType
        String testEventType = "PUSH";
        fuentusSonarNorm1.setEventType(testEventType);
        assertEquals(testEventType, fuentusSonarNorm1.getEventType());

        // Test PeriodStart
        fuentusSonarNorm1.setPeriodStart(testPeriodStart);
        assertEquals(testPeriodStart, fuentusSonarNorm1.getPeriodStart());
    }

    @Test
    void testGettersAndSetters_IssueFields() {
        // Test IssuesTypeBug
        Integer testBugs = 5;
        fuentusSonarNorm1.setIssuesTypeBug(testBugs);
        assertEquals(testBugs, fuentusSonarNorm1.getIssuesTypeBug());

        // Test IssuesTypeCodeSmell
        Integer testCodeSmells = 15;
        fuentusSonarNorm1.setIssuesTypeCodeSmell(testCodeSmells);
        assertEquals(testCodeSmells, fuentusSonarNorm1.getIssuesTypeCodeSmell());

        // Test IssuesTypeVulnerability
        Integer testVulnerabilities = 3;
        fuentusSonarNorm1.setIssuesTypeVulnerability(testVulnerabilities);
        assertEquals(testVulnerabilities, fuentusSonarNorm1.getIssuesTypeVulnerability());

        // Test IssuesSeverityBlocker
        Integer testBlocker = 1;
        fuentusSonarNorm1.setIssuesSeverityBlocker(testBlocker);
        assertEquals(testBlocker, fuentusSonarNorm1.getIssuesSeverityBlocker());

        // Test IssuesSeverityCritical
        Integer testCritical = 2;
        fuentusSonarNorm1.setIssuesSeverityCritical(testCritical);
        assertEquals(testCritical, fuentusSonarNorm1.getIssuesSeverityCritical());

        // Test IssuesSeverityInfo
        Integer testInfo = 10;
        fuentusSonarNorm1.setIssuesSeverityInfo(testInfo);
        assertEquals(testInfo, fuentusSonarNorm1.getIssuesSeverityInfo());

        // Test IssuesSeverityMinor
        Integer testMinor = 8;
        fuentusSonarNorm1.setIssuesSeverityMinor(testMinor);
        assertEquals(testMinor, fuentusSonarNorm1.getIssuesSeverityMinor());

        // Test IssuesSeverityMajor
        Integer testMajor = 4;
        fuentusSonarNorm1.setIssuesSeverityMajor(testMajor);
        assertEquals(testMajor, fuentusSonarNorm1.getIssuesSeverityMajor());
    }

    @Test
    void testGettersAndSetters_QualityGateFields() {
        // Test QualityGateStatus
        String testQGStatus = "PASSED";
        fuentusSonarNorm1.setQualityGateStatus(testQGStatus);
        assertEquals(testQGStatus, fuentusSonarNorm1.getQualityGateStatus());

        // Test QualityGateName
        String testQGName = "BBVA Quality Gate";
        fuentusSonarNorm1.setQualityGateName(testQGName);
        assertEquals(testQGName, fuentusSonarNorm1.getQualityGateName());
    }

    @Test
    void testGettersAndSetters_MetricFields() {
        // Test Ncloc
        Integer testNcloc = 1500;
        fuentusSonarNorm1.setNcloc(testNcloc);
        assertEquals(testNcloc, fuentusSonarNorm1.getNcloc());

        // Test CognitiveComplexity
        Integer testCognitiveComplexity = 25;
        fuentusSonarNorm1.setCognitiveComplexity(testCognitiveComplexity);
        assertEquals(testCognitiveComplexity, fuentusSonarNorm1.getCognitiveComplexity());

        // Test SqaleDebtRatio
        String testSqaleDebtRatio = "5.2%";
        fuentusSonarNorm1.setSqaleDebtRatio(testSqaleDebtRatio);
        assertEquals(testSqaleDebtRatio, fuentusSonarNorm1.getSqaleDebtRatio());

        // Test SecurityRating
        Integer testSecurityRating = 1;
        fuentusSonarNorm1.setSecurityRating(testSecurityRating);
        assertEquals(testSecurityRating, fuentusSonarNorm1.getSecurityRating());

        // Test ReliabilityRating
        Integer testReliabilityRating = 2;
        fuentusSonarNorm1.setReliabilityRating(testReliabilityRating);
        assertEquals(testReliabilityRating, fuentusSonarNorm1.getReliabilityRating());

        // Test DuplicatedLines
        Integer testDuplicatedLines = 50;
        fuentusSonarNorm1.setDuplicatedLines(testDuplicatedLines);
        assertEquals(testDuplicatedLines, fuentusSonarNorm1.getDuplicatedLines());

        // Test DuplicatedBlocks
        Integer testDuplicatedBlocks = 3;
        fuentusSonarNorm1.setDuplicatedBlocks(testDuplicatedBlocks);
        assertEquals(testDuplicatedBlocks, fuentusSonarNorm1.getDuplicatedBlocks());
    }

    @Test
    void testGettersAndSetters_CoverageFields() {
        // Test Coverage
        String testCoverage = "85.5%";
        fuentusSonarNorm1.setCoverage(testCoverage);
        assertEquals(testCoverage, fuentusSonarNorm1.getCoverage());

        // Test NewCoverage
        String testNewCoverage = "90.2%";
        fuentusSonarNorm1.setNewCoverage(testNewCoverage);
        assertEquals(testNewCoverage, fuentusSonarNorm1.getNewCoverage());

        // Test LineCoverage
        String testLineCoverage = "88.7%";
        fuentusSonarNorm1.setLineCoverage(testLineCoverage);
        assertEquals(testLineCoverage, fuentusSonarNorm1.getLineCoverage());

        // Test UncoveredLines
        Integer testUncoveredLines = 120;
        fuentusSonarNorm1.setUncoveredLines(testUncoveredLines);
        assertEquals(testUncoveredLines, fuentusSonarNorm1.getUncoveredLines());

        // Test LinesToCover
        Integer testLinesToCover = 800;
        fuentusSonarNorm1.setLinesToCover(testLinesToCover);
        assertEquals(testLinesToCover, fuentusSonarNorm1.getLinesToCover());

        // Test Tests
        Integer testTests = 150;
        fuentusSonarNorm1.setTests(testTests);
        assertEquals(testTests, fuentusSonarNorm1.getTests());
    }

    @Test
    void testGettersAndSetters_SonarInstanceFields() {
        // Test SonarInstance
        String testSonarInstance = "https://sonar.bbva.com";
        fuentusSonarNorm1.setSonarInstance(testSonarInstance);
        assertEquals(testSonarInstance, fuentusSonarNorm1.getSonarInstance());

        // Test SonarKey
        String testSonarKey = "bbva:my-project:main";
        fuentusSonarNorm1.setSonarKey(testSonarKey);
        assertEquals(testSonarKey, fuentusSonarNorm1.getSonarKey());

        // Test AnalysisKey
        String testAnalysisKey = "analysis-123-456";
        fuentusSonarNorm1.setAnalysisKey(testAnalysisKey);
        assertEquals(testAnalysisKey, fuentusSonarNorm1.getAnalysisKey());
    }

    @Test
    void testGettersAndSetters_ComplexityFields() {
        // Test Complexity
        Integer testComplexity = 45;
        fuentusSonarNorm1.setComplexity(testComplexity);
        assertEquals(testComplexity, fuentusSonarNorm1.getComplexity());

        // Test ProjectMaturity
        String testProjectMaturity = "MATURE";
        fuentusSonarNorm1.setProjectMaturity(testProjectMaturity);
        assertEquals(testProjectMaturity, fuentusSonarNorm1.getProjectMaturity());

        // Test SqaleRating
        String testSqaleRating = "A";
        fuentusSonarNorm1.setSqaleRating(testSqaleRating);
        assertEquals(testSqaleRating, fuentusSonarNorm1.getSqaleRating());

        // Test RepoId
        String testRepoId = "repo-456-789";
        fuentusSonarNorm1.setRepoId(testRepoId);
        assertEquals(testRepoId, fuentusSonarNorm1.getRepoId());
    }

    @Test
    void testGettersAndSetters_TechnicalDebtFields() {
        // Test NewSqaleDebtRatio
        String testNewSqaleDebtRatio = "2.1%";
        fuentusSonarNorm1.setNewSqaleDebtRatio(testNewSqaleDebtRatio);
        assertEquals(testNewSqaleDebtRatio, fuentusSonarNorm1.getNewSqaleDebtRatio());

        // Test NewTechnicalDebt
        String testNewTechnicalDebt = "1h 30min";
        fuentusSonarNorm1.setNewTechnicalDebt(testNewTechnicalDebt);
        assertEquals(testNewTechnicalDebt, fuentusSonarNorm1.getNewTechnicalDebt());

        // Test SqaleIndex
        Integer testSqaleIndex = 125;
        fuentusSonarNorm1.setSqaleIndex(testSqaleIndex);
        assertEquals(testSqaleIndex, fuentusSonarNorm1.getSqaleIndex());

        // Test NewCodeSmells
        Integer testNewCodeSmells = 7;
        fuentusSonarNorm1.setNewCodeSmells(testNewCodeSmells);
        assertEquals(testNewCodeSmells, fuentusSonarNorm1.getNewCodeSmells());

        // Test CodeSmells
        Integer testCodeSmells = 25;
        fuentusSonarNorm1.setCodeSmells(testCodeSmells);
        assertEquals(testCodeSmells, fuentusSonarNorm1.getCodeSmells());
    }

    @Test
    void testGettersAndSetters_ZeroValues() {
        // Test with zero values for integer fields
        fuentusSonarNorm1.setIssuesTypeBug(0);
        assertEquals(Integer.valueOf(0), fuentusSonarNorm1.getIssuesTypeBug());

        fuentusSonarNorm1.setNcloc(0);
        assertEquals(Integer.valueOf(0), fuentusSonarNorm1.getNcloc());

        fuentusSonarNorm1.setTests(0);
        assertEquals(Integer.valueOf(0), fuentusSonarNorm1.getTests());

        fuentusSonarNorm1.setComplexity(0);
        assertEquals(Integer.valueOf(0), fuentusSonarNorm1.getComplexity());
    }

    @Test
    void testGettersAndSetters_NegativeValues() {
        // Test with negative values (edge case)
        fuentusSonarNorm1.setIssuesTypeBug(-1);
        assertEquals(Integer.valueOf(-1), fuentusSonarNorm1.getIssuesTypeBug());

        fuentusSonarNorm1.setDuplicatedLines(-5);
        assertEquals(Integer.valueOf(-5), fuentusSonarNorm1.getDuplicatedLines());
    }

    @Test
    void testGettersAndSetters_LargeValues() {
        // Test with large integer values
        Integer largeValue = Integer.MAX_VALUE;
        fuentusSonarNorm1.setNcloc(largeValue);
        assertEquals(largeValue, fuentusSonarNorm1.getNcloc());

        fuentusSonarNorm1.setSqaleIndex(largeValue);
        assertEquals(largeValue, fuentusSonarNorm1.getSqaleIndex());
    }

    @Test
    void testGettersAndSetters_AllNullableFields() {
        // Test setting null values for all additional nullable fields
        fuentusSonarNorm1.setUser(null);
        assertNull(fuentusSonarNorm1.getUser());

        fuentusSonarNorm1.setRepo(null);
        assertNull(fuentusSonarNorm1.getRepo());

        fuentusSonarNorm1.setRepoUrl(null);
        assertNull(fuentusSonarNorm1.getRepoUrl());

        fuentusSonarNorm1.setUuaa(null);
        assertNull(fuentusSonarNorm1.getUuaa());

        fuentusSonarNorm1.setCommit(null);
        assertNull(fuentusSonarNorm1.getCommit());

        fuentusSonarNorm1.setEventType(null);
        assertNull(fuentusSonarNorm1.getEventType());

        fuentusSonarNorm1.setPeriodStart(null);
        assertNull(fuentusSonarNorm1.getPeriodStart());

        fuentusSonarNorm1.setIssuesTypeBug(null);
        assertNull(fuentusSonarNorm1.getIssuesTypeBug());

        fuentusSonarNorm1.setIssuesTypeCodeSmell(null);
        assertNull(fuentusSonarNorm1.getIssuesTypeCodeSmell());

        fuentusSonarNorm1.setIssuesTypeVulnerability(null);
        assertNull(fuentusSonarNorm1.getIssuesTypeVulnerability());

        fuentusSonarNorm1.setQualityGateStatus(null);
        assertNull(fuentusSonarNorm1.getQualityGateStatus());

        fuentusSonarNorm1.setQualityGateName(null);
        assertNull(fuentusSonarNorm1.getQualityGateName());

        fuentusSonarNorm1.setCoverage(null);
        assertNull(fuentusSonarNorm1.getCoverage());

        fuentusSonarNorm1.setSonarInstance(null);
        assertNull(fuentusSonarNorm1.getSonarInstance());

        fuentusSonarNorm1.setSonarKey(null);
        assertNull(fuentusSonarNorm1.getSonarKey());

        fuentusSonarNorm1.setAnalysisKey(null);
        assertNull(fuentusSonarNorm1.getAnalysisKey());

        fuentusSonarNorm1.setProjectMaturity(null);
        assertNull(fuentusSonarNorm1.getProjectMaturity());

        fuentusSonarNorm1.setLineCoverage(null);
        assertNull(fuentusSonarNorm1.getLineCoverage());

        fuentusSonarNorm1.setSqaleRating(null);
        assertNull(fuentusSonarNorm1.getSqaleRating());

        fuentusSonarNorm1.setRepoId(null);
        assertNull(fuentusSonarNorm1.getRepoId());

        fuentusSonarNorm1.setNewSqaleDebtRatio(null);
        assertNull(fuentusSonarNorm1.getNewSqaleDebtRatio());

        fuentusSonarNorm1.setNewTechnicalDebt(null);
        assertNull(fuentusSonarNorm1.getNewTechnicalDebt());

        fuentusSonarNorm1.setNewCoverage(null);
        assertNull(fuentusSonarNorm1.getNewCoverage());
    }

    @Test
    void testConstructorEdgeCases() {
        // Test constructor with null ID
        FuentusSonarNorm sonarNormWithNullId = new FuentusSonarNorm(null);
        assertNull(sonarNormWithNullId.getId());

        // Test constructor with valid timestamps
        assertDoesNotThrow(() -> {
            new FuentusSonarNorm(1L, testIngestedTimestamp, testCreatedTimestamp, testEventIngestedTimestamp, testEventTimestamp);
        });
    }

    @Test
    void testEquals_DifferentIds() {
        // Given
        fuentusSonarNorm1.setId(1L);
        fuentusSonarNorm2.setId(2L);

        // When & Then
        assertNotEquals(fuentusSonarNorm1, fuentusSonarNorm2);
    }

    @Test
    void testEquals_BothIdsNull() {
        // Given - both objects have null IDs (default state)

        // When & Then
        assertEquals(fuentusSonarNorm1, fuentusSonarNorm2);
    }

    @Test
    void testEquals_OneIdNull() {
        // Given
        fuentusSonarNorm1.setId(1L);
        // fuentusSonarNorm2.setId(null) - default state

        // When & Then
        assertNotEquals(fuentusSonarNorm1, fuentusSonarNorm2);
    }

    @Test
    void testHashCode_DifferentObjects() {
        // Given
        fuentusSonarNorm1.setId(1L);
        fuentusSonarNorm2.setId(2L);

        // When & Then
        assertNotEquals(fuentusSonarNorm1.hashCode(), fuentusSonarNorm2.hashCode());
    }

    @Test
    void testHashCode_ConsistentWithEquals() {
        // Given
        setupSameValues();

        // When & Then - Objects that are equal must have the same hash code
        if (fuentusSonarNorm1.equals(fuentusSonarNorm2)) {
            assertEquals(fuentusSonarNorm1.hashCode(), fuentusSonarNorm2.hashCode());
        }
    }

    @Test
    void testToString_ContainsId() {
        // Given
        Long testId = 12345L;
        fuentusSonarNorm1.setId(testId);

        // When
        String result = fuentusSonarNorm1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains(testId.toString()));
        assertTrue(result.contains("id="));
    }

    @Test
    void testToString_NullId() {
        // Given - ID is null by default

        // When
        String result = fuentusSonarNorm1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("null"));
    }
}
