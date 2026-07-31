package ar.com.bbva.fuentus.entities;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ChimeraReviewTest {

    @Test
    void constructor_ShouldCreateEmptyEntity_WhenNoArgumentsProvided() {
        // When
        ChimeraReview review = new ChimeraReview();

        // Then
        assertNotNull(review);
        assertNull(review.getId());
        assertNull(review.getStartDate());
        assertNull(review.getEndDate());
    }

    @Test
    void constructor_ShouldCreateEntity_WhenIdProvided() {
        // Given
        Long id = 100L;

        // When
        ChimeraReview review = new ChimeraReview(id);

        // Then
        assertNotNull(review);
        assertEquals(id, review.getId());
    }

    @Test
    void constructor_ShouldCreateEntity_WhenAllRequiredFieldsProvided() {
        // Given
        Long id = 100L;
        long appId = 200L;
        long chimeraId = 300L;
        Date startDate = new Date();
        Date endDate = new Date();

        // When
        ChimeraReview review = new ChimeraReview(id, appId, chimeraId, startDate, endDate);

        // Then
        assertNotNull(review);
        assertEquals(id, review.getId());
        assertEquals(appId, review.getAppId());
        assertEquals(startDate, review.getStartDate());
        assertEquals(endDate, review.getEndDate());
    }

    @Test
    void setId_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();
        Long id = 123L;

        // When
        review.setId(id);

        // Then
        assertEquals(id, review.getId());
    }

    @Test
    void setAppId_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();
        long appId = 456L;

        // When
        review.setAppId(appId);

        // Then
        assertEquals(appId, review.getAppId());
    }

    @Test
    void setStartDate_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();
        Date startDate = new Date();

        // When
        review.setStartDate(startDate);

        // Then
        assertEquals(startDate, review.getStartDate());
    }

    @Test
    void setEndDate_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();
        Date endDate = new Date();

        // When
        review.setEndDate(endDate);

        // Then
        assertEquals(endDate, review.getEndDate());
    }

    @Test
    void setBranch_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setBranch("main");

        // Then
        assertEquals("main", review.getBranch());
    }

    @Test
    void setStatus_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setStatus("completed");

        // Then
        assertEquals("completed", review.getStatus());
    }

    @Test
    void setProjectId_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setProjectId("PROJ-123");

        // Then
        assertEquals("PROJ-123", review.getProjectId());
    }

    @Test
    void setRepository_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setRepository("https://github.com/test/repo");

        // Then
        assertEquals("https://github.com/test/repo", review.getRepository());
    }

    @Test
    void setAnalyzer_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setAnalyzer("sonar");

        // Then
        assertEquals("sonar", review.getAnalyzer());
    }

    @Test
    void setDelta_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setDelta("delta-value");

        // Then
        assertEquals("delta-value", review.getDelta());
    }

    @Test
    void setCommitId_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setCommitId("abc123def456");

        // Then
        assertEquals("abc123def456", review.getCommitId());
    }

    @Test
    void setLanguage_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setLanguage("Java");

        // Then
        assertEquals("Java", review.getLanguage());
    }

    @Test
    void setUser_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setUser("testuser");

        // Then
        assertEquals("testuser", review.getUser());
    }

    @Test
    void setOrigin_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setOrigin("manual");

        // Then
        assertEquals("manual", review.getOrigin());
    }

    @Test
    void setScanned_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setScanned(true);

        // Then
        assertTrue(review.getScanned());
    }

    @Test
    void setDuplicatedId_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setDuplicatedId(789L);

        // Then
        assertEquals(789L, review.getDuplicatedId());
    }

    @Test
    void setFindingsConfirmed_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setFindingsConfirmed(10L);

        // Then
        assertEquals(10L, review.getFindingsConfirmed());
    }

    @Test
    void setFindingsFalsePositive_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setFindingsFalsePositive(5L);

        // Then
        assertEquals(5L, review.getFindingsFalsePositive());
    }

    @Test
    void setFindingsToReview_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setFindingsToReview(8L);

        // Then
        assertEquals(8L, review.getFindingsToReview());
    }

    @Test
    void setFindingsFixed_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setFindingsFixed(15L);

        // Then
        assertEquals(15L, review.getFindingsFixed());
    }

    @Test
    void setVulnerabilitesCount_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setVulnerabilitesCount(20L);

        // Then
        assertEquals(20L, review.getVulnerabilitesCount());
    }

    @Test
    void setTotalInfo_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setTotalInfo(25L);

        // Then
        assertEquals(25L, review.getTotalInfo());
    }

    @Test
    void setTotalLow_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setTotalLow(30L);

        // Then
        assertEquals(30L, review.getTotalLow());
    }

    @Test
    void setTotalMedium_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setTotalMedium(35L);

        // Then
        assertEquals(35L, review.getTotalMedium());
    }

    @Test
    void setTotalHigh_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setTotalHigh(40L);

        // Then
        assertEquals(40L, review.getTotalHigh());
    }

    @Test
    void setTotalCritical_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setTotalCritical(45L);

        // Then
        assertEquals(45L, review.getTotalCritical());
    }

    @Test
    void setConfirmedInfo_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setConfirmedInfo(2L);

        // Then
        assertEquals(2L, review.getConfirmedInfo());
    }

    @Test
    void setConfirmedLow_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setConfirmedLow(3L);

        // Then
        assertEquals(3L, review.getConfirmedLow());
    }

    @Test
    void setConfirmedMedium_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setConfirmedMedium(4L);

        // Then
        assertEquals(4L, review.getConfirmedMedium());
    }

    @Test
    void setConfirmedHigh_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setConfirmedHigh(5L);

        // Then
        assertEquals(5L, review.getConfirmedHigh());
    }

    @Test
    void setConfirmedCritical_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setConfirmedCritical(6L);

        // Then
        assertEquals(6L, review.getConfirmedCritical());
    }

    @Test
    void setToReviewInfo_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setToReviewInfo(7L);

        // Then
        assertEquals(7L, review.getToReviewInfo());
    }

    @Test
    void setToReviewLow_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setToReviewLow(8L);

        // Then
        assertEquals(8L, review.getToReviewLow());
    }

    @Test
    void setToReviewMedium_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setToReviewMedium(9L);

        // Then
        assertEquals(9L, review.getToReviewMedium());
    }

    @Test
    void setToReviewHigh_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setToReviewHigh(10L);

        // Then
        assertEquals(10L, review.getToReviewHigh());
    }

    @Test
    void setToReviewCritical_ShouldSetValue() {
        // Given
        ChimeraReview review = new ChimeraReview();

        // When
        review.setToReviewCritical(11L);

        // Then
        assertEquals(11L, review.getToReviewCritical());
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameObject() {
        // Given
        ChimeraReview review = new ChimeraReview();
        review.setId(100L);

        // When & Then
        assertEquals(review, review);
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameId() {
        // Given
        ChimeraReview review1 = new ChimeraReview();
        review1.setId(100L);
        
        ChimeraReview review2 = new ChimeraReview();
        review2.setId(100L);

        // When & Then
        assertEquals(review1, review2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentId() {
        // Given
        ChimeraReview review1 = new ChimeraReview();
        review1.setId(100L);
        
        ChimeraReview review2 = new ChimeraReview();
        review2.setId(200L);

        // When & Then
        assertNotEquals(review1, review2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparedWithNull() {
        // Given
        ChimeraReview review = new ChimeraReview();
        review.setId(100L);

        // When & Then
        assertNotEquals(review, null);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparedWithDifferentType() {
        // Given
        ChimeraReview review = new ChimeraReview();
        review.setId(100L);

        // When & Then
        assertNotEquals(review, "string");
    }

    @Test
    void equals_ShouldReturnFalse_WhenThisIdIsNull() {
        // Given
        ChimeraReview review1 = new ChimeraReview();
        review1.setId(null);
        
        ChimeraReview review2 = new ChimeraReview();
        review2.setId(100L);

        // When & Then
        assertNotEquals(review1, review2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenOtherIdIsNull() {
        // Given
        ChimeraReview review1 = new ChimeraReview();
        review1.setId(100L);
        
        ChimeraReview review2 = new ChimeraReview();
        review2.setId(null);

        // When & Then
        assertNotEquals(review1, review2);
    }

    @Test
    void hashCode_ShouldReturnSameValue_ForSameId() {
        // Given
        ChimeraReview review1 = new ChimeraReview();
        review1.setId(100L);
        
        ChimeraReview review2 = new ChimeraReview();
        review2.setId(100L);

        // When & Then
        assertEquals(review1.hashCode(), review2.hashCode());
    }

    @Test
    void hashCode_ShouldReturnZero_WhenIdIsNull() {
        // Given
        ChimeraReview review = new ChimeraReview();
        review.setId(null);

        // When
        int hash = review.hashCode();

        // Then
        assertEquals(0, hash);
    }

    @Test
    void toString_ShouldContainId() {
        // Given
        ChimeraReview review = new ChimeraReview();
        review.setId(123L);

        // When
        String result = review.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("123"));
    }

    @Test
    void allFieldsGettersAndSetters_ShouldWork() {
        // Given
        ChimeraReview review = new ChimeraReview();
        Date startDate = new Date();
        Date endDate = new Date();

        // When
        review.setId(1L);
        review.setAppId(2L);
        review.setStartDate(startDate);
        review.setEndDate(endDate);
        review.setBranch("develop");
        review.setStatus("in-progress");
        review.setProjectId("PROJ-999");
        review.setRepository("https://repo.url");
        review.setAnalyzer("checkmarx");
        review.setDelta("delta");
        review.setCommitId("commit123");
        review.setLanguage("Python");
        review.setUser("admin");
        review.setOrigin("api");
        review.setScanned(false);
        review.setDuplicatedId(100L);
        review.setFindingsConfirmed(1L);
        review.setFindingsFalsePositive(2L);
        review.setFindingsToReview(3L);
        review.setFindingsFixed(4L);
        review.setVulnerabilitesCount(5L);
        review.setTotalInfo(6L);
        review.setTotalLow(7L);
        review.setTotalMedium(8L);
        review.setTotalHigh(9L);
        review.setTotalCritical(10L);
        review.setConfirmedInfo(11L);
        review.setConfirmedLow(12L);
        review.setConfirmedMedium(13L);
        review.setConfirmedHigh(14L);
        review.setConfirmedCritical(15L);
        review.setToReviewInfo(16L);
        review.setToReviewLow(17L);
        review.setToReviewMedium(18L);
        review.setToReviewHigh(19L);
        review.setToReviewCritical(20L);

        // Then
        assertEquals(1L, review.getId());
        assertEquals(2L, review.getAppId());
        assertEquals(startDate, review.getStartDate());
        assertEquals(endDate, review.getEndDate());
        assertEquals("develop", review.getBranch());
        assertEquals("in-progress", review.getStatus());
        assertEquals("PROJ-999", review.getProjectId());
        assertEquals("https://repo.url", review.getRepository());
        assertEquals("checkmarx", review.getAnalyzer());
        assertEquals("delta", review.getDelta());
        assertEquals("commit123", review.getCommitId());
        assertEquals("Python", review.getLanguage());
        assertEquals("admin", review.getUser());
        assertEquals("api", review.getOrigin());
        assertFalse(review.getScanned());
        assertEquals(100L, review.getDuplicatedId());
        assertEquals(1L, review.getFindingsConfirmed());
        assertEquals(2L, review.getFindingsFalsePositive());
        assertEquals(3L, review.getFindingsToReview());
        assertEquals(4L, review.getFindingsFixed());
        assertEquals(5L, review.getVulnerabilitesCount());
        assertEquals(6L, review.getTotalInfo());
        assertEquals(7L, review.getTotalLow());
        assertEquals(8L, review.getTotalMedium());
        assertEquals(9L, review.getTotalHigh());
        assertEquals(10L, review.getTotalCritical());
        assertEquals(11L, review.getConfirmedInfo());
        assertEquals(12L, review.getConfirmedLow());
        assertEquals(13L, review.getConfirmedMedium());
        assertEquals(14L, review.getConfirmedHigh());
        assertEquals(15L, review.getConfirmedCritical());
        assertEquals(16L, review.getToReviewInfo());
        assertEquals(17L, review.getToReviewLow());
        assertEquals(18L, review.getToReviewMedium());
        assertEquals(19L, review.getToReviewHigh());
        assertEquals(20L, review.getToReviewCritical());
    }
}
