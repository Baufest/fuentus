package ar.com.bbva.fuentus.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FuentusReposTest {

    private FuentusRepos fuentusRepos1;
    private FuentusRepos fuentusRepos2;
    private Date testDate;

    @BeforeEach
    void setUp() {
        fuentusRepos1 = new FuentusRepos();
        fuentusRepos2 = new FuentusRepos();
        testDate = new Date();
    }

    @Test
    void testDefaultConstructor() {
        // Given & When
        FuentusRepos repos = new FuentusRepos();

        // Then
        assertNotNull(repos);
        assertNull(repos.getId());
        assertNull(repos.getPeriodStart());
        assertNull(repos.getArchitecture());
        assertNull(repos.getGeo());
        assertNull(repos.getRepo());
        assertNull(repos.getRepoUrl());
        assertNull(repos.getBrowseUrl());
        assertNull(repos.getConsoleManaged());
        assertNull(repos.getLanguagesList());
        assertNull(repos.getLanguagesCount());
        assertNull(repos.getIsFrontend());
        assertNull(repos.getTechnology());
        assertNull(repos.getCategory());
        assertNull(repos.getCategoryGroup());
        assertNull(repos.getPeriodType());
        assertNull(repos.getType());
        assertNull(repos.getIsEther());
        assertNull(repos.getArchitectureInPeriod());
        assertNull(repos.getCommits());
        assertNull(repos.getPushes());
        assertNull(repos.getCommitters());
        assertNull(repos.getCommitsInPerimeter());
        assertNull(repos.getPushesInPerimeter());
        assertNull(repos.getTopPusherHash());
        assertNull(repos.getTopPushes());
        assertNull(repos.getTopCommiterHash());
        assertNull(repos.getTopCommits());
        assertNull(repos.getGeoInPeriod());
        assertNull(repos.getTopPerimeter());
        assertNull(repos.getPushesPct50());
        assertNull(repos.getCommitsPct50());
        assertNull(repos.getCommittersOverThreshold());
        assertNull(repos.getCommittersInPerimeter());
        assertNull(repos.getRank());
        assertNull(repos.getHasUnitTests());
        assertNull(repos.getHasIntegrationTests());
    }

    @Test
    void testParameterizedConstructor() {
        // Given
        Long testId = 123L;

        // When
        FuentusRepos repos = new FuentusRepos(testId);

        // Then
        assertNotNull(repos);
        assertEquals(testId, repos.getId());
    }

    @Test
    void testGettersAndSetters_BasicFields() {
        // Test ID
        Long testId = 1L;
        fuentusRepos1.setId(testId);
        assertEquals(testId, fuentusRepos1.getId());

        // Test PeriodStart
        fuentusRepos1.setPeriodStart(testDate);
        assertEquals(testDate, fuentusRepos1.getPeriodStart());

        // Test Architecture
        String testArchitecture = "microservices";
        fuentusRepos1.setArchitecture(testArchitecture);
        assertEquals(testArchitecture, fuentusRepos1.getArchitecture());

        // Test Geo
        String testGeo = "ES";
        fuentusRepos1.setGeo(testGeo);
        assertEquals(testGeo, fuentusRepos1.getGeo());

        // Test Repo
        String testRepo = "test-repository";
        fuentusRepos1.setRepo(testRepo);
        assertEquals(testRepo, fuentusRepos1.getRepo());

        // Test RepoUrl
        String testRepoUrl = "https://bitbucket.com/test/test-repo";
        fuentusRepos1.setRepoUrl(testRepoUrl);
        assertEquals(testRepoUrl, fuentusRepos1.getRepoUrl());

        // Test BrowseUrl
        String testBrowseUrl = "https://bitbucket.com/test/test-repo/browse";
        fuentusRepos1.setBrowseUrl(testBrowseUrl);
        assertEquals(testBrowseUrl, fuentusRepos1.getBrowseUrl());
    }

    @Test
    void testGettersAndSetters_BooleanFields() {
        // Test ConsoleManaged
        Boolean testConsoleManaged = true;
        fuentusRepos1.setConsoleManaged(testConsoleManaged);
        assertEquals(testConsoleManaged, fuentusRepos1.getConsoleManaged());

        fuentusRepos1.setConsoleManaged(false);
        assertEquals(false, fuentusRepos1.getConsoleManaged());

        fuentusRepos1.setConsoleManaged(null);
        assertNull(fuentusRepos1.getConsoleManaged());

        // Test IsFrontend
        Boolean testIsFrontend = true;
        fuentusRepos1.setIsFrontend(testIsFrontend);
        assertEquals(testIsFrontend, fuentusRepos1.getIsFrontend());

        fuentusRepos1.setIsFrontend(false);
        assertEquals(false, fuentusRepos1.getIsFrontend());

        fuentusRepos1.setIsFrontend(null);
        assertNull(fuentusRepos1.getIsFrontend());

        // Test IsEther
        Boolean testIsEther = true;
        fuentusRepos1.setIsEther(testIsEther);
        assertEquals(testIsEther, fuentusRepos1.getIsEther());

        fuentusRepos1.setIsEther(false);
        assertEquals(false, fuentusRepos1.getIsEther());

        fuentusRepos1.setIsEther(null);
        assertNull(fuentusRepos1.getIsEther());

        // Test HasUnitTests
        Boolean testHasUnitTests = true;
        fuentusRepos1.setHasUnitTests(testHasUnitTests);
        assertEquals(testHasUnitTests, fuentusRepos1.getHasUnitTests());

        fuentusRepos1.setHasUnitTests(false);
        assertEquals(false, fuentusRepos1.getHasUnitTests());

        fuentusRepos1.setHasUnitTests(null);
        assertNull(fuentusRepos1.getHasUnitTests());

        // Test HasIntegrationTests
        Boolean testHasIntegrationTests = true;
        fuentusRepos1.setHasIntegrationTests(testHasIntegrationTests);
        assertEquals(testHasIntegrationTests, fuentusRepos1.getHasIntegrationTests());

        fuentusRepos1.setHasIntegrationTests(false);
        assertEquals(false, fuentusRepos1.getHasIntegrationTests());

        fuentusRepos1.setHasIntegrationTests(null);
        assertNull(fuentusRepos1.getHasIntegrationTests());
    }

    @Test
    void testGettersAndSetters_LobFields() {
        // Test LanguagesList (LOB field)
        String testLanguagesList = "Java,JavaScript,TypeScript,Python,SQL";
        fuentusRepos1.setLanguagesList(testLanguagesList);
        assertEquals(testLanguagesList, fuentusRepos1.getLanguagesList());

        // Test with very long text for LOB field
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("Java,JavaScript,TypeScript,Python,SQL,");
        }
        String longLanguagesList = sb.toString();
        fuentusRepos1.setLanguagesList(longLanguagesList);
        assertEquals(longLanguagesList, fuentusRepos1.getLanguagesList());

        // Test LanguagesCount (LOB field)
        String testLanguagesCount = "Java:1500,JavaScript:800,TypeScript:600,Python:200,SQL:100";
        fuentusRepos1.setLanguagesCount(testLanguagesCount);
        assertEquals(testLanguagesCount, fuentusRepos1.getLanguagesCount());

        // Test with very long text for LOB field
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb2.append("Java:1500,JavaScript:800,");
        }
        String longLanguagesCount = sb2.toString();
        fuentusRepos1.setLanguagesCount(longLanguagesCount);
        assertEquals(longLanguagesCount, fuentusRepos1.getLanguagesCount());
    }

    @Test
    void testGettersAndSetters_CategoryFields() {
        // Test Technology
        String testTechnology = "Spring Boot";
        fuentusRepos1.setTechnology(testTechnology);
        assertEquals(testTechnology, fuentusRepos1.getTechnology());

        // Test Category
        String testCategory = "Backend Service";
        fuentusRepos1.setCategory(testCategory);
        assertEquals(testCategory, fuentusRepos1.getCategory());

        // Test CategoryGroup
        String testCategoryGroup = "Microservices";
        fuentusRepos1.setCategoryGroup(testCategoryGroup);
        assertEquals(testCategoryGroup, fuentusRepos1.getCategoryGroup());

        // Test PeriodType
        String testPeriodType = "monthly";
        fuentusRepos1.setPeriodType(testPeriodType);
        assertEquals(testPeriodType, fuentusRepos1.getPeriodType());

        // Test Type
        String testType = "application";
        fuentusRepos1.setType(testType);
        assertEquals(testType, fuentusRepos1.getType());

        // Test ArchitectureInPeriod
        String testArchitectureInPeriod = "microservices-active";
        fuentusRepos1.setArchitectureInPeriod(testArchitectureInPeriod);
        assertEquals(testArchitectureInPeriod, fuentusRepos1.getArchitectureInPeriod());

        // Test TopPusherHash
        String testTopPusherHash = "abc123def456";
        fuentusRepos1.setTopPusherHash(testTopPusherHash);
        assertEquals(testTopPusherHash, fuentusRepos1.getTopPusherHash());

        // Test TopCommiterHash
        String testTopCommiterHash = "def456ghi789";
        fuentusRepos1.setTopCommiterHash(testTopCommiterHash);
        assertEquals(testTopCommiterHash, fuentusRepos1.getTopCommiterHash());

        // Test GeoInPeriod
        String testGeoInPeriod = "EU-WEST";
        fuentusRepos1.setGeoInPeriod(testGeoInPeriod);
        assertEquals(testGeoInPeriod, fuentusRepos1.getGeoInPeriod());

        // Test TopPerimeter
        String testTopPerimeter = "frontend";
        fuentusRepos1.setTopPerimeter(testTopPerimeter);
        assertEquals(testTopPerimeter, fuentusRepos1.getTopPerimeter());
    }

    @Test
    void testGettersAndSetters_IntegerFields() {
        // Test Commits
        Integer testCommits = 150;
        fuentusRepos1.setCommits(testCommits);
        assertEquals(testCommits, fuentusRepos1.getCommits());

        // Test Pushes
        Integer testPushes = 75;
        fuentusRepos1.setPushes(testPushes);
        assertEquals(testPushes, fuentusRepos1.getPushes());

        // Test Committers
        Integer testCommitters = 5;
        fuentusRepos1.setCommitters(testCommitters);
        assertEquals(testCommitters, fuentusRepos1.getCommitters());

        // Test CommitsInPerimeter
        Integer testCommitsInPerimeter = 120;
        fuentusRepos1.setCommitsInPerimeter(testCommitsInPerimeter);
        assertEquals(testCommitsInPerimeter, fuentusRepos1.getCommitsInPerimeter());

        // Test PushesInPerimeter
        Integer testPushesInPerimeter = 60;
        fuentusRepos1.setPushesInPerimeter(testPushesInPerimeter);
        assertEquals(testPushesInPerimeter, fuentusRepos1.getPushesInPerimeter());

        // Test TopPushes
        Integer testTopPushes = 25;
        fuentusRepos1.setTopPushes(testTopPushes);
        assertEquals(testTopPushes, fuentusRepos1.getTopPushes());

        // Test TopCommits
        Integer testTopCommits = 45;
        fuentusRepos1.setTopCommits(testTopCommits);
        assertEquals(testTopCommits, fuentusRepos1.getTopCommits());

        // Test PushesPct50
        Integer testPushesPct50 = 10;
        fuentusRepos1.setPushesPct50(testPushesPct50);
        assertEquals(testPushesPct50, fuentusRepos1.getPushesPct50());

        // Test CommitsPct50
        Integer testCommitsPct50 = 20;
        fuentusRepos1.setCommitsPct50(testCommitsPct50);
        assertEquals(testCommitsPct50, fuentusRepos1.getCommitsPct50());

        // Test CommittersOverThreshold
        Integer testCommittersOverThreshold = 3;
        fuentusRepos1.setCommittersOverThreshold(testCommittersOverThreshold);
        assertEquals(testCommittersOverThreshold, fuentusRepos1.getCommittersOverThreshold());

        // Test CommittersInPerimeter
        Integer testCommittersInPerimeter = 4;
        fuentusRepos1.setCommittersInPerimeter(testCommittersInPerimeter);
        assertEquals(testCommittersInPerimeter, fuentusRepos1.getCommittersInPerimeter());

        // Test Rank
        Integer testRank = 1;
        fuentusRepos1.setRank(testRank);
        assertEquals(testRank, fuentusRepos1.getRank());

        // Test with null values
        fuentusRepos1.setCommits(null);
        assertNull(fuentusRepos1.getCommits());

        fuentusRepos1.setPushes(null);
        assertNull(fuentusRepos1.getPushes());

        fuentusRepos1.setRank(null);
        assertNull(fuentusRepos1.getRank());

        // Test with negative values
        fuentusRepos1.setCommits(-1);
        assertEquals(-1, fuentusRepos1.getCommits());

        // Test with zero values
        fuentusRepos1.setPushes(0);
        assertEquals(0, fuentusRepos1.getPushes());

        // Test with large values
        fuentusRepos1.setCommits(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, fuentusRepos1.getCommits());
    }

    @Test
    void testGettersAndSetters_NullValues() {
        // Test setting null values for all nullable fields
        fuentusRepos1.setId(null);
        assertNull(fuentusRepos1.getId());

        fuentusRepos1.setPeriodStart(null);
        assertNull(fuentusRepos1.getPeriodStart());

        fuentusRepos1.setArchitecture(null);
        assertNull(fuentusRepos1.getArchitecture());

        fuentusRepos1.setGeo(null);
        assertNull(fuentusRepos1.getGeo());

        fuentusRepos1.setRepo(null);
        assertNull(fuentusRepos1.getRepo());

        fuentusRepos1.setRepoUrl(null);
        assertNull(fuentusRepos1.getRepoUrl());

        fuentusRepos1.setBrowseUrl(null);
        assertNull(fuentusRepos1.getBrowseUrl());

        fuentusRepos1.setConsoleManaged(null);
        assertNull(fuentusRepos1.getConsoleManaged());

        fuentusRepos1.setLanguagesList(null);
        assertNull(fuentusRepos1.getLanguagesList());

        fuentusRepos1.setLanguagesCount(null);
        assertNull(fuentusRepos1.getLanguagesCount());

        fuentusRepos1.setIsFrontend(null);
        assertNull(fuentusRepos1.getIsFrontend());

        fuentusRepos1.setTechnology(null);
        assertNull(fuentusRepos1.getTechnology());

        fuentusRepos1.setCategory(null);
        assertNull(fuentusRepos1.getCategory());

        fuentusRepos1.setCategoryGroup(null);
        assertNull(fuentusRepos1.getCategoryGroup());

        fuentusRepos1.setPeriodType(null);
        assertNull(fuentusRepos1.getPeriodType());

        fuentusRepos1.setType(null);
        assertNull(fuentusRepos1.getType());

        fuentusRepos1.setIsEther(null);
        assertNull(fuentusRepos1.getIsEther());

        fuentusRepos1.setArchitectureInPeriod(null);
        assertNull(fuentusRepos1.getArchitectureInPeriod());

        fuentusRepos1.setCommits(null);
        assertNull(fuentusRepos1.getCommits());

        fuentusRepos1.setPushes(null);
        assertNull(fuentusRepos1.getPushes());

        fuentusRepos1.setCommitters(null);
        assertNull(fuentusRepos1.getCommitters());

        fuentusRepos1.setCommitsInPerimeter(null);
        assertNull(fuentusRepos1.getCommitsInPerimeter());

        fuentusRepos1.setPushesInPerimeter(null);
        assertNull(fuentusRepos1.getPushesInPerimeter());

        fuentusRepos1.setTopPusherHash(null);
        assertNull(fuentusRepos1.getTopPusherHash());

        fuentusRepos1.setTopPushes(null);
        assertNull(fuentusRepos1.getTopPushes());

        fuentusRepos1.setTopCommiterHash(null);
        assertNull(fuentusRepos1.getTopCommiterHash());

        fuentusRepos1.setTopCommits(null);
        assertNull(fuentusRepos1.getTopCommits());

        fuentusRepos1.setGeoInPeriod(null);
        assertNull(fuentusRepos1.getGeoInPeriod());

        fuentusRepos1.setTopPerimeter(null);
        assertNull(fuentusRepos1.getTopPerimeter());

        fuentusRepos1.setPushesPct50(null);
        assertNull(fuentusRepos1.getPushesPct50());

        fuentusRepos1.setCommitsPct50(null);
        assertNull(fuentusRepos1.getCommitsPct50());

        fuentusRepos1.setCommittersOverThreshold(null);
        assertNull(fuentusRepos1.getCommittersOverThreshold());

        fuentusRepos1.setCommittersInPerimeter(null);
        assertNull(fuentusRepos1.getCommittersInPerimeter());

        fuentusRepos1.setRank(null);
        assertNull(fuentusRepos1.getRank());

        fuentusRepos1.setHasUnitTests(null);
        assertNull(fuentusRepos1.getHasUnitTests());

        fuentusRepos1.setHasIntegrationTests(null);
        assertNull(fuentusRepos1.getHasIntegrationTests());
    }

    @Test
    void testGettersAndSetters_EdgeCases() {
        // Test with empty strings
        fuentusRepos1.setRepo("");
        assertEquals("", fuentusRepos1.getRepo());

        fuentusRepos1.setArchitecture("");
        assertEquals("", fuentusRepos1.getArchitecture());

        fuentusRepos1.setTechnology("");
        assertEquals("", fuentusRepos1.getTechnology());

        // Test with special characters in URLs
        String specialUrl = "https://bitbucket.com/test/repo-with-special-chars_123/browse?at=refs%2Fheads%2Ffeature%2Ftest";
        fuentusRepos1.setRepoUrl(specialUrl);
        assertEquals(specialUrl, fuentusRepos1.getRepoUrl());

        fuentusRepos1.setBrowseUrl(specialUrl);
        assertEquals(specialUrl, fuentusRepos1.getBrowseUrl());

        // Test with very long repository names
        StringBuilder sb3 = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb3.append("very-long-repository-name-");
        }
        String longRepoName = sb3.toString();
        fuentusRepos1.setRepo(longRepoName);
        assertEquals(longRepoName, fuentusRepos1.getRepo());

        // Test with special characters in hash values
        String specialHash = "abc123!@#$%^&*()_+-=[]{}|;':\",./<>?";
        fuentusRepos1.setTopPusherHash(specialHash);
        assertEquals(specialHash, fuentusRepos1.getTopPusherHash());

        fuentusRepos1.setTopCommiterHash(specialHash);
        assertEquals(specialHash, fuentusRepos1.getTopCommiterHash());

        // Test with extreme integer values
        fuentusRepos1.setCommits(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, fuentusRepos1.getCommits());

        fuentusRepos1.setPushes(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, fuentusRepos1.getPushes());

        // Test with whitespace only strings
        fuentusRepos1.setType("   ");
        assertEquals("   ", fuentusRepos1.getType());

        fuentusRepos1.setGeoInPeriod("\t\n\r");
        assertEquals("\t\n\r", fuentusRepos1.getGeoInPeriod());
    }

    @Test
    void testEquals_DifferentIds() {
        // Given
        fuentusRepos1.setId(1L);
        fuentusRepos2.setId(2L);

        // When & Then
        assertNotEquals(fuentusRepos1, fuentusRepos2);
        assertFalse(fuentusRepos1.equals(fuentusRepos2));
    }

    @Test
    void testEquals_BothNullIds() {
        // Given - both have null ids (default)

        // When & Then
        assertEquals(fuentusRepos1, fuentusRepos2);
        assertTrue(fuentusRepos1.equals(fuentusRepos2));
    }

    @Test
    void testEquals_OneNullId() {
        // Given
        fuentusRepos1.setId(1L);
        // fuentusRepos2.setId(null); // already null by default

        // When & Then
        assertNotEquals(fuentusRepos1, fuentusRepos2);
        assertFalse(fuentusRepos1.equals(fuentusRepos2));
    }

    @Test
    void testHashCode_DifferentValues() {
        // Given
        fuentusRepos1.setId(1L);
        fuentusRepos2.setId(2L);

        // When & Then
        assertNotEquals(fuentusRepos1.hashCode(), fuentusRepos2.hashCode());
    }

    @Test
    void testToString_WithNullId() {
        // Given - id is null by default

        // When
        String result = fuentusRepos1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("FuentusRepos"));
        assertTrue(result.contains("id=null"));
    }

    @Test
    void testToString_WithSpecificId() {
        // Given
        fuentusRepos1.setId(123L);

        // When
        String result = fuentusRepos1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("FuentusRepos"));
        assertTrue(result.contains("id=123"));
    }

    @Test
    void testEquals_SameObject() {
        // When & Then
        assertEquals(fuentusRepos1, fuentusRepos1);
        assertTrue(fuentusRepos1.equals(fuentusRepos1));
    }

    @Test
    void testEquals_NullObject() {
        // When & Then
        assertNotEquals(fuentusRepos1, null);
        assertFalse(fuentusRepos1.equals(null));
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        String differentObject = "not a FuentusRepos";

        // When & Then
        assertNotEquals(fuentusRepos1, differentObject);
        assertFalse(fuentusRepos1.equals(differentObject));
    }

    @Test
    void testHashCode_SameValues() {
        // Given
        setupSameValues();

        // When & Then
        assertEquals(fuentusRepos1.hashCode(), fuentusRepos2.hashCode());
    }

    @Test
    void testHashCode_NullValues() {
        // Given - all nullable values are null by default

        // When & Then
        assertDoesNotThrow(() -> fuentusRepos1.hashCode());
    }

    @Test
    void testToString() {
        // Given
        setupSomeValues();

        // When
        String result = fuentusRepos1.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("FuentusRepos"));
    }

    @Test
    void testSerializable() {
        // Given
        setupSomeValues();

        // When & Then - Test that the class implements Serializable properly
        assertTrue(fuentusRepos1 instanceof java.io.Serializable);
    }

    @Test
    void testEqualsContract_Reflexive() {
        // Reflexive: x.equals(x) should return true
        assertTrue(fuentusRepos1.equals(fuentusRepos1));
    }

    @Test
    void testEqualsContract_Symmetric() {
        // Symmetric: x.equals(y) should return same as y.equals(x)
        setupSameValues();

        boolean result1 = fuentusRepos1.equals(fuentusRepos2);
        boolean result2 = fuentusRepos2.equals(fuentusRepos1);
        assertEquals(result1, result2);
    }

    @Test
    void testEqualsContract_Consistent() {
        // Consistent: multiple invocations should return same result
        setupSameValues();

        boolean result1 = fuentusRepos1.equals(fuentusRepos2);
        boolean result2 = fuentusRepos1.equals(fuentusRepos2);
        boolean result3 = fuentusRepos1.equals(fuentusRepos2);

        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }

    private void setupSameValues() {
        fuentusRepos1.setId(1L);
        fuentusRepos1.setPeriodStart(testDate);
        fuentusRepos1.setArchitecture("microservices");
        fuentusRepos1.setGeo("ES");
        fuentusRepos1.setRepo("test-repository");
        fuentusRepos1.setRepoUrl("https://bitbucket.com/test/test-repo");
        fuentusRepos1.setConsoleManaged(true);
        fuentusRepos1.setIsFrontend(false);
        fuentusRepos1.setType("application");
        fuentusRepos1.setIsEther(false);
        fuentusRepos1.setCommits(100);
        fuentusRepos1.setPushes(50);
        fuentusRepos1.setCommitters(5);
        fuentusRepos1.setRank(1);
        fuentusRepos1.setHasUnitTests(true);
        fuentusRepos1.setHasIntegrationTests(false);

        fuentusRepos2.setId(1L);
        fuentusRepos2.setPeriodStart(testDate);
        fuentusRepos2.setArchitecture("microservices");
        fuentusRepos2.setGeo("ES");
        fuentusRepos2.setRepo("test-repository");
        fuentusRepos2.setRepoUrl("https://bitbucket.com/test/test-repo");
        fuentusRepos2.setConsoleManaged(true);
        fuentusRepos2.setIsFrontend(false);
        fuentusRepos2.setType("application");
        fuentusRepos2.setIsEther(false);
        fuentusRepos2.setCommits(100);
        fuentusRepos2.setPushes(50);
        fuentusRepos2.setCommitters(5);
        fuentusRepos2.setRank(1);
        fuentusRepos2.setHasUnitTests(true);
        fuentusRepos2.setHasIntegrationTests(false);
    }

    private void setupSomeValues() {
        fuentusRepos1.setId(1L);
        fuentusRepos1.setPeriodStart(testDate);
        fuentusRepos1.setArchitecture("microservices");
        fuentusRepos1.setGeo("ES");
        fuentusRepos1.setRepo("test-repository");
        fuentusRepos1.setRepoUrl("https://bitbucket.com/test/test-repo");
        fuentusRepos1.setBrowseUrl("https://bitbucket.com/test/test-repo/browse");
        fuentusRepos1.setConsoleManaged(true);
        fuentusRepos1.setLanguagesList("Java,JavaScript,TypeScript");
        fuentusRepos1.setLanguagesCount("Java:1500,JavaScript:800");
        fuentusRepos1.setIsFrontend(false);
        fuentusRepos1.setTechnology("Spring Boot");
        fuentusRepos1.setCategory("Backend Service");
        fuentusRepos1.setCategoryGroup("Microservices");
        fuentusRepos1.setPeriodType("monthly");
        fuentusRepos1.setType("application");
        fuentusRepos1.setIsEther(false);
        fuentusRepos1.setArchitectureInPeriod("microservices-active");
        fuentusRepos1.setCommits(150);
        fuentusRepos1.setPushes(75);
        fuentusRepos1.setCommitters(5);
        fuentusRepos1.setCommitsInPerimeter(120);
        fuentusRepos1.setPushesInPerimeter(60);
        fuentusRepos1.setTopPusherHash("abc123def456");
        fuentusRepos1.setTopPushes(25);
        fuentusRepos1.setTopCommiterHash("def456ghi789");
        fuentusRepos1.setTopCommits(45);
        fuentusRepos1.setGeoInPeriod("EU-WEST");
        fuentusRepos1.setTopPerimeter("frontend");
        fuentusRepos1.setPushesPct50(10);
        fuentusRepos1.setCommitsPct50(20);
        fuentusRepos1.setCommittersOverThreshold(3);
        fuentusRepos1.setCommittersInPerimeter(4);
        fuentusRepos1.setRank(1);
        fuentusRepos1.setHasUnitTests(true);
        fuentusRepos1.setHasIntegrationTests(false);
    }
}
