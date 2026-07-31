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
 * @author seba
 */
@Entity
@Table(name = "fuentus_chimera_raw")
public class FuentusChimeraRaw implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "event_info_created_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventInfoCreatedTimestamp;
    @Basic(optional = false)
    @Column(name = "event_info_ingested_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventInfoIngestedTimestamp;
    @Column(name = "event_info_repository")
    private String eventInfoRepository;
    @Column(name = "event_info_user_id")
    private String eventInfoUserId;
    @Column(name = "event_info_country")
    private String eventInfoCountry;
    @Column(name = "event_info_uuaa")
    private String eventInfoUuaa;
    @Column(name = "event_info_origin")
    private String eventInfoOrigin;
    @Column(name = "event_info_scan_status")
    private String eventInfoScanStatus;
    @Column(name = "event_info_branch")
    private String eventInfoBranch;
    @Column(name = "event_info_project")
    private String eventInfoProject;
    @Column(name = "event_info_event_id")
    private String eventInfoEventId;
    @Column(name = "event_info_vulnerabilities_count")
    private Integer eventInfoVulnerabilitiesCount;
    @Column(name = "event_info_lines_of_code")
    private Integer eventInfoLinesOfCode;
    @Column(name = "event_info_analyzer")
    private String eventInfoAnalyzer;
    @Column(name = "event_info_review_id")
    private String eventInfoReviewId;
    @Column(name = "event_info_language")
    private String eventInfoLanguage;
    @Column(name = "event_info_product")
    private String eventInfoProduct;
    @Column(name = "event_info_area")
    private String eventInfoArea;
    @Column(name = "event_info_architecture")
    private String eventInfoArchitecture;
    @Basic(optional = false)
    @Column(name = "event_info_executed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventInfoExecutedAt;
    @Column(name = "event_info_commit_id")
    private String eventInfoCommitId;
    @Column(name = "event_info_preset")
    private String eventInfoPreset;
    @Column(name = "event_info_duplicated_id")
    private String eventInfoDuplicatedId;
    @Basic(optional = false)
    @Column(name = "sent_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentTimestamp;
    @Basic(optional = false)
    @Column(name = "event_ingested_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventIngestedTimestamp;
    @Column(name = "repo")
    private String repo;
    @Column(name = "repo_url")
    private String repoUrl;
    @Column(name = "user")
    private String user;
    @Column(name = "period_start")
    @Temporal(TemporalType.DATE)
    private Date periodStart;

    public FuentusChimeraRaw() {
    }

    public FuentusChimeraRaw(Long id) {
        this.id = id;
    }

    public FuentusChimeraRaw(Long id, Date eventInfoCreatedTimestamp, Date eventInfoIngestedTimestamp, Date eventInfoExecutedAt, Date sentTimestamp, Date eventIngestedTimestamp) {
        this.id = id;
        this.eventInfoCreatedTimestamp = eventInfoCreatedTimestamp;
        this.eventInfoIngestedTimestamp = eventInfoIngestedTimestamp;
        this.eventInfoExecutedAt = eventInfoExecutedAt;
        this.sentTimestamp = sentTimestamp;
        this.eventIngestedTimestamp = eventIngestedTimestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEventInfoCreatedTimestamp() {
        return eventInfoCreatedTimestamp;
    }

    public void setEventInfoCreatedTimestamp(Date eventInfoCreatedTimestamp) {
        this.eventInfoCreatedTimestamp = eventInfoCreatedTimestamp;
    }

    public Date getEventInfoIngestedTimestamp() {
        return eventInfoIngestedTimestamp;
    }

    public void setEventInfoIngestedTimestamp(Date eventInfoIngestedTimestamp) {
        this.eventInfoIngestedTimestamp = eventInfoIngestedTimestamp;
    }

    public String getEventInfoRepository() {
        return eventInfoRepository;
    }

    public void setEventInfoRepository(String eventInfoRepository) {
        this.eventInfoRepository = eventInfoRepository;
    }

    public String getEventInfoUserId() {
        return eventInfoUserId;
    }

    public void setEventInfoUserId(String eventInfoUserId) {
        this.eventInfoUserId = eventInfoUserId;
    }

    public String getEventInfoCountry() {
        return eventInfoCountry;
    }

    public void setEventInfoCountry(String eventInfoCountry) {
        this.eventInfoCountry = eventInfoCountry;
    }

    public String getEventInfoUuaa() {
        return eventInfoUuaa;
    }

    public void setEventInfoUuaa(String eventInfoUuaa) {
        this.eventInfoUuaa = eventInfoUuaa;
    }

    public String getEventInfoOrigin() {
        return eventInfoOrigin;
    }

    public void setEventInfoOrigin(String eventInfoOrigin) {
        this.eventInfoOrigin = eventInfoOrigin;
    }

    public String getEventInfoScanStatus() {
        return eventInfoScanStatus;
    }

    public void setEventInfoScanStatus(String eventInfoScanStatus) {
        this.eventInfoScanStatus = eventInfoScanStatus;
    }

    public String getEventInfoBranch() {
        return eventInfoBranch;
    }

    public void setEventInfoBranch(String eventInfoBranch) {
        this.eventInfoBranch = eventInfoBranch;
    }

    public String getEventInfoProject() {
        return eventInfoProject;
    }

    public void setEventInfoProject(String eventInfoProject) {
        this.eventInfoProject = eventInfoProject;
    }

    public String getEventInfoEventId() {
        return eventInfoEventId;
    }

    public void setEventInfoEventId(String eventInfoEventId) {
        this.eventInfoEventId = eventInfoEventId;
    }

    public Integer getEventInfoVulnerabilitiesCount() {
        return eventInfoVulnerabilitiesCount;
    }

    public void setEventInfoVulnerabilitiesCount(Integer eventInfoVulnerabilitiesCount) {
        this.eventInfoVulnerabilitiesCount = eventInfoVulnerabilitiesCount;
    }

    public Integer getEventInfoLinesOfCode() {
        return eventInfoLinesOfCode;
    }

    public void setEventInfoLinesOfCode(Integer eventInfoLinesOfCode) {
        this.eventInfoLinesOfCode = eventInfoLinesOfCode;
    }

    public String getEventInfoAnalyzer() {
        return eventInfoAnalyzer;
    }

    public void setEventInfoAnalyzer(String eventInfoAnalyzer) {
        this.eventInfoAnalyzer = eventInfoAnalyzer;
    }

    public String getEventInfoReviewId() {
        return eventInfoReviewId;
    }

    public void setEventInfoReviewId(String eventInfoReviewId) {
        this.eventInfoReviewId = eventInfoReviewId;
    }

    public String getEventInfoLanguage() {
        return eventInfoLanguage;
    }

    public void setEventInfoLanguage(String eventInfoLanguage) {
        this.eventInfoLanguage = eventInfoLanguage;
    }

    public String getEventInfoProduct() {
        return eventInfoProduct;
    }

    public void setEventInfoProduct(String eventInfoProduct) {
        this.eventInfoProduct = eventInfoProduct;
    }

    public String getEventInfoArea() {
        return eventInfoArea;
    }

    public void setEventInfoArea(String eventInfoArea) {
        this.eventInfoArea = eventInfoArea;
    }

    public String getEventInfoArchitecture() {
        return eventInfoArchitecture;
    }

    public void setEventInfoArchitecture(String eventInfoArchitecture) {
        this.eventInfoArchitecture = eventInfoArchitecture;
    }

    public Date getEventInfoExecutedAt() {
        return eventInfoExecutedAt;
    }

    public void setEventInfoExecutedAt(Date eventInfoExecutedAt) {
        this.eventInfoExecutedAt = eventInfoExecutedAt;
    }

    public String getEventInfoCommitId() {
        return eventInfoCommitId;
    }

    public void setEventInfoCommitId(String eventInfoCommitId) {
        this.eventInfoCommitId = eventInfoCommitId;
    }

    public String getEventInfoPreset() {
        return eventInfoPreset;
    }

    public void setEventInfoPreset(String eventInfoPreset) {
        this.eventInfoPreset = eventInfoPreset;
    }

    public String getEventInfoDuplicatedId() {
        return eventInfoDuplicatedId;
    }

    public void setEventInfoDuplicatedId(String eventInfoDuplicatedId) {
        this.eventInfoDuplicatedId = eventInfoDuplicatedId;
    }

    public Date getSentTimestamp() {
        return sentTimestamp;
    }

    public void setSentTimestamp(Date sentTimestamp) {
        this.sentTimestamp = sentTimestamp;
    }

    public Date getEventIngestedTimestamp() {
        return eventIngestedTimestamp;
    }

    public void setEventIngestedTimestamp(Date eventIngestedTimestamp) {
        this.eventIngestedTimestamp = eventIngestedTimestamp;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Date periodStart) {
        this.periodStart = periodStart;
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
        if (!(object instanceof FuentusChimeraRaw)) {
            return false;
        }
        FuentusChimeraRaw other = (FuentusChimeraRaw) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.bbva.scrapper.entities.FuentusChimeraRaw[ id=" + id + " ]";
    }
    
}
