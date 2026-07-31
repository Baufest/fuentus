/*
 */
package ar.com.bbva.fuentus.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author o002349
 */
@Entity
@Table(name = "chimera_reviews")
public class ChimeraReview implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "app_id")
    private long appId;
//    @Basic(optional = false)
//    @Column(name = "chimera_id")
//    private long chimeraId;
    @Basic(optional = false)
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Basic(optional = false)
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "branch")
    private String branch;
    @Column(name = "status")
    private String status;
    @Column(name = "project_id")
    private String projectId;
    @Column(name = "repository")
    private String repository;
    @Column(name = "analyzer")
    private String analyzer;
    @Column(name = "delta")
    private String delta;
    @Column(name = "commit_id")
    private String commitId;
    @Column(name = "language")
    private String language;
    @Column(name = "user")
    private String user;
    @Column(name = "origin")
    private String origin;
    @Column(name = "scanned")
    private Boolean scanned;
    @Column(name = "duplicated_id")
    private Long duplicatedId;
    @Column(name = "findings_confirmed")
    private Long findingsConfirmed;
    @Column(name = "findings_false_positive")
    private Long findingsFalsePositive;
    @Column(name = "findings_to_review")
    private Long findingsToReview;
    @Column(name = "findings_fixed")
    private Long findingsFixed;
    @Column(name = "vulnerabilites_count")
    private Long vulnerabilitesCount;
    @Column(name = "total_info")
    private Long totalInfo;
    @Column(name = "total_low")
    private Long totalLow;
    @Column(name = "total_medium")
    private Long totalMedium;
    @Column(name = "total_high")
    private Long totalHigh;
    @Column(name = "total_critical")
    private Long totalCritical;
    @Column(name = "confirmed_info")
    private Long confirmedInfo;
    @Column(name = "confirmed_low")
    private Long confirmedLow;
    @Column(name = "confirmed_medium")
    private Long confirmedMedium;
    @Column(name = "confirmed_high")
    private Long confirmedHigh;
    @Column(name = "confirmed_critical")
    private Long confirmedCritical;
    @Column(name = "to_review_info")
    private Long toReviewInfo;
    @Column(name = "to_review_low")
    private Long toReviewLow;
    @Column(name = "to_review_medium")
    private Long toReviewMedium;
    @Column(name = "to_review_high")
    private Long toReviewHigh;
    @Column(name = "to_review_critical")
    private Long toReviewCritical;

    public ChimeraReview() {
    }

    public ChimeraReview(Long id) {
        this.id = id;
    }

    public ChimeraReview(Long id, long appId, long chimeraId, Date startDate, Date endDate) {
        this.id = id;
        this.appId = appId;
    //    this.chimeraId = chimeraId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

//    public long getChimeraId() {
//        return chimeraId;
//    }

//    public void setChimeraId(long chimeraId) {
//        this.chimeraId = chimeraId;
//    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
    }

    public String getDelta() {
        return delta;
    }

    public void setDelta(String delta) {
        this.delta = delta;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public Long getDuplicatedId() {
        return duplicatedId;
    }

    public void setDuplicatedId(Long duplicatedId) {
        this.duplicatedId = duplicatedId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getFindingsConfirmed() {
        return findingsConfirmed;
    }

    public void setFindingsConfirmed(Long findingsConfirmed) {
        this.findingsConfirmed = findingsConfirmed;
    }

    public Long getFindingsFalsePositive() {
        return findingsFalsePositive;
    }

    public void setFindingsFalsePositive(Long findingsFalsePositive) {
        this.findingsFalsePositive = findingsFalsePositive;
    }

    public void setFindingsFixed(Long findingsFixed) {
        this.findingsFixed = findingsFixed;
    }

    public Long getFindingsFixed() {
        return findingsFixed;
    }
    
    public Long getFindingsToReview() {
        return findingsToReview;
    }

    public void setFindingsToReview(Long findingsToReview) {
        this.findingsToReview = findingsToReview;
    }

    public Long getVulnerabilitesCount() {
        return vulnerabilitesCount;
    }

    public void setVulnerabilitesCount(Long vulnerabilitesCount) {
        this.vulnerabilitesCount = vulnerabilitesCount;
    }

    public Long getTotalInfo() {
        return totalInfo;
    }

    public void setTotalInfo(Long totalInfo) {
        this.totalInfo = totalInfo;
    }

    public Long getTotalLow() {
        return totalLow;
    }

    public void setTotalLow(Long totalLow) {
        this.totalLow = totalLow;
    }

    public Long getTotalMedium() {
        return totalMedium;
    }

    public void setTotalMedium(Long totalMedium) {
        this.totalMedium = totalMedium;
    }

    public Long getTotalHigh() {
        return totalHigh;
    }

    public void setTotalHigh(Long totalHigh) {
        this.totalHigh = totalHigh;
    }

    public Long getTotalCritical() {
        return totalCritical;
    }

    public void setTotalCritical(Long totalCritical) {
        this.totalCritical = totalCritical;
    }

    public Long getConfirmedInfo() {
        return confirmedInfo;
    }

    public void setConfirmedInfo(Long confirmedInfo) {
        this.confirmedInfo = confirmedInfo;
    }

    public Long getConfirmedLow() {
        return confirmedLow;
    }

    public void setConfirmedLow(Long confirmedLow) {
        this.confirmedLow = confirmedLow;
    }

    public Long getConfirmedMedium() {
        return confirmedMedium;
    }

    public void setConfirmedMedium(Long confirmedMedium) {
        this.confirmedMedium = confirmedMedium;
    }

    public Long getConfirmedHigh() {
        return confirmedHigh;
    }

    public void setConfirmedHigh(Long confirmedHigh) {
        this.confirmedHigh = confirmedHigh;
    }

    public Long getConfirmedCritical() {
        return confirmedCritical;
    }

    public void setConfirmedCritical(Long confirmedCritical) {
        this.confirmedCritical = confirmedCritical;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Boolean getScanned() {
        return scanned;
    }

    public void setScanned(Boolean scanned) {
        this.scanned = scanned;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChimeraReview)) {
            return false;
        }
        ChimeraReview other = (ChimeraReview) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.bbva.scrapper.entities.ChimeraReview[ id=" + id + " ]";
    }

    public Long getToReviewInfo() {
        return toReviewInfo;
    }

    public void setToReviewInfo(Long toReviewInfo) {
        this.toReviewInfo = toReviewInfo;
    }

    public Long getToReviewLow() {
        return toReviewLow;
    }

    public void setToReviewLow(Long toReviewLow) {
        this.toReviewLow = toReviewLow;
    }

    public Long getToReviewMedium() {
        return toReviewMedium;
    }

    public void setToReviewMedium(Long toReviewMedium) {
        this.toReviewMedium = toReviewMedium;
    }

    public Long getToReviewHigh() {
        return toReviewHigh;
    }

    public void setToReviewHigh(Long toReviewHigh) {
        this.toReviewHigh = toReviewHigh;
    }

    public Long getToReviewCritical() {
        return toReviewCritical;
    }

    public void setToReviewCritical(Long toReviewCritical) {
        this.toReviewCritical = toReviewCritical;
    }
    
}
