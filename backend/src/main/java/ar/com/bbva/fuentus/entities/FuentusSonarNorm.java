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
@Table(name = "fuentus_sonar_norm")
public class FuentusSonarNorm implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "event_info_scm_project")
    private String eventInfoScmProject;
    @Column(name = "event_info_scm_repository")
    private String eventInfoScmRepository;
    @Column(name = "event_info_scm_url")
    private String eventInfoScmUrl;
    @Column(name = "event_info_scm_branch")
    private String eventInfoScmBranch;
    @Basic(optional = false)
    @Column(name = "event_info_ingested_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventInfoIngestedTimestamp;
    @Basic(optional = false)
    @Column(name = "event_info_created_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventInfoCreatedTimestamp;
    @Column(name = "event_info_user_id")
    private String eventInfoUserId;
    @Column(name = "event_info_event_id")
    private String eventInfoEventId;
    @Column(name = "event_info_repo_id")
    private String eventInfoRepoId;
    @Column(name = "project_from_url")
    private String projectFromUrl;
    @Column(name = "repo_from_url")
    private String repoFromUrl;
    @Basic(optional = false)
    @Column(name = "event_ingested_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventIngestedTimestamp;
    @Basic(optional = false)
    @Column(name = "event_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventTimestamp;
    @Column(name = "user")
    private String user;
    @Column(name = "repo")
    private String repo;
    @Column(name = "repo_url")
    private String repoUrl;
    @Column(name = "uuaa")
    private String uuaa;
    @Column(name = "commit")
    private String commit;
    @Column(name = "event_type")
    private String eventType;
    @Column(name = "period_start")
    @Temporal(TemporalType.DATE)
    private Date periodStart;
    @Column(name = "issues_type_bug")
    private Integer issuesTypeBug;
    @Column(name = "issues_type_code_smell")
    private Integer issuesTypeCodeSmell;
    @Column(name = "issues_type_vulnerability")
    private Integer issuesTypeVulnerability;
    @Column(name = "issues_severity_blocker")
    private Integer issuesSeverityBlocker;
    @Column(name = "issues_severity_critical")
    private Integer issuesSeverityCritical;
    @Column(name = "issues_severity_info")
    private Integer issuesSeverityInfo;
    @Column(name = "issues_severity_minor")
    private Integer issuesSeverityMinor;
    @Column(name = "issues_severity_major")
    private Integer issuesSeverityMajor;
    @Column(name = "quality_gate_status")
    private String qualityGateStatus;
    @Column(name = "quality_gate_name")
    private String qualityGateName;
    @Column(name = "ncloc")
    private Integer ncloc;
    @Column(name = "cognitive_complexity")
    private Integer cognitiveComplexity;
    @Column(name = "sqale_debt_ratio")
    private String sqaleDebtRatio;
    @Column(name = "security_rating")
    private Integer securityRating;
    @Column(name = "reliability_rating")
    private Integer reliabilityRating;
    @Column(name = "duplicated_lines")
    private Integer duplicatedLines;
    @Column(name = "duplicated_blocks")
    private Integer duplicatedBlocks;
    @Column(name = "coverage")
    private String coverage;
    @Column(name = "sonar_instance")
    private String sonarInstance;
    @Column(name = "tests")
    private Integer tests;
    @Column(name = "complexity")
    private Integer complexity;
    @Column(name = "new_coverage")
    private String newCoverage;
    @Column(name = "sonar_key")
    private String sonarKey;
    @Column(name = "analysis_key")
    private String analysisKey;
    @Column(name = "project_maturity")
    private String projectMaturity;
    @Column(name = "uncovered_lines")
    private Integer uncoveredLines;
    @Column(name = "lines_to_cover")
    private Integer linesToCover;
    @Column(name = "line_coverage")
    private String lineCoverage;
    @Column(name = "sqale_rating")
    private String sqaleRating;
    @Column(name = "repo_id")
    private String repoId;
    @Column(name = "new_sqale_debt_ratio")
    private String newSqaleDebtRatio;
    @Column(name = "new_technical_debt")
    private String newTechnicalDebt;
    @Column(name = "sqale_index")
    private Integer sqaleIndex;
    @Column(name = "new_code_smells")
    private Integer newCodeSmells;
    @Column(name = "code_smells")
    private Integer codeSmells;

    public FuentusSonarNorm() {
    }

    public FuentusSonarNorm(Long id) {
        this.id = id;
    }

    public FuentusSonarNorm(Long id, Date eventInfoIngestedTimestamp, Date eventInfoCreatedTimestamp, Date eventIngestedTimestamp, Date eventTimestamp) {
        this.id = id;
        this.eventInfoIngestedTimestamp = eventInfoIngestedTimestamp;
        this.eventInfoCreatedTimestamp = eventInfoCreatedTimestamp;
        this.eventIngestedTimestamp = eventIngestedTimestamp;
        this.eventTimestamp = eventTimestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventInfoScmProject() {
        return eventInfoScmProject;
    }

    public void setEventInfoScmProject(String eventInfoScmProject) {
        this.eventInfoScmProject = eventInfoScmProject;
    }

    public String getEventInfoScmRepository() {
        return eventInfoScmRepository;
    }

    public void setEventInfoScmRepository(String eventInfoScmRepository) {
        this.eventInfoScmRepository = eventInfoScmRepository;
    }

    public String getEventInfoScmUrl() {
        return eventInfoScmUrl;
    }

    public void setEventInfoScmUrl(String eventInfoScmUrl) {
        this.eventInfoScmUrl = eventInfoScmUrl;
    }

    public String getEventInfoScmBranch() {
        return eventInfoScmBranch;
    }

    public void setEventInfoScmBranch(String eventInfoScmBranch) {
        this.eventInfoScmBranch = eventInfoScmBranch;
    }

    public Date getEventInfoIngestedTimestamp() {
        return eventInfoIngestedTimestamp;
    }

    public void setEventInfoIngestedTimestamp(Date eventInfoIngestedTimestamp) {
        this.eventInfoIngestedTimestamp = eventInfoIngestedTimestamp;
    }

    public Date getEventInfoCreatedTimestamp() {
        return eventInfoCreatedTimestamp;
    }

    public void setEventInfoCreatedTimestamp(Date eventInfoCreatedTimestamp) {
        this.eventInfoCreatedTimestamp = eventInfoCreatedTimestamp;
    }

    public String getEventInfoUserId() {
        return eventInfoUserId;
    }

    public void setEventInfoUserId(String eventInfoUserId) {
        this.eventInfoUserId = eventInfoUserId;
    }

    public String getEventInfoEventId() {
        return eventInfoEventId;
    }

    public void setEventInfoEventId(String eventInfoEventId) {
        this.eventInfoEventId = eventInfoEventId;
    }

    public String getEventInfoRepoId() {
        return eventInfoRepoId;
    }

    public void setEventInfoRepoId(String eventInfoRepoId) {
        this.eventInfoRepoId = eventInfoRepoId;
    }

    public String getProjectFromUrl() {
        return projectFromUrl;
    }

    public void setProjectFromUrl(String projectFromUrl) {
        this.projectFromUrl = projectFromUrl;
    }

    public String getRepoFromUrl() {
        return repoFromUrl;
    }

    public void setRepoFromUrl(String repoFromUrl) {
        this.repoFromUrl = repoFromUrl;
    }

    public Date getEventIngestedTimestamp() {
        return eventIngestedTimestamp;
    }

    public void setEventIngestedTimestamp(Date eventIngestedTimestamp) {
        this.eventIngestedTimestamp = eventIngestedTimestamp;
    }

    public Date getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(Date eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getUuaa() {
        return uuaa;
    }

    public void setUuaa(String uuaa) {
        this.uuaa = uuaa;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Date getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Date periodStart) {
        this.periodStart = periodStart;
    }

    public Integer getIssuesTypeBug() {
        return issuesTypeBug;
    }

    public void setIssuesTypeBug(Integer issuesTypeBug) {
        this.issuesTypeBug = issuesTypeBug;
    }

    public Integer getIssuesTypeCodeSmell() {
        return issuesTypeCodeSmell;
    }

    public void setIssuesTypeCodeSmell(Integer issuesTypeCodeSmell) {
        this.issuesTypeCodeSmell = issuesTypeCodeSmell;
    }

    public Integer getIssuesTypeVulnerability() {
        return issuesTypeVulnerability;
    }

    public void setIssuesTypeVulnerability(Integer issuesTypeVulnerability) {
        this.issuesTypeVulnerability = issuesTypeVulnerability;
    }

    public Integer getIssuesSeverityBlocker() {
        return issuesSeverityBlocker;
    }

    public void setIssuesSeverityBlocker(Integer issuesSeverityBlocker) {
        this.issuesSeverityBlocker = issuesSeverityBlocker;
    }

    public Integer getIssuesSeverityCritical() {
        return issuesSeverityCritical;
    }

    public void setIssuesSeverityCritical(Integer issuesSeverityCritical) {
        this.issuesSeverityCritical = issuesSeverityCritical;
    }

    public Integer getIssuesSeverityInfo() {
        return issuesSeverityInfo;
    }

    public void setIssuesSeverityInfo(Integer issuesSeverityInfo) {
        this.issuesSeverityInfo = issuesSeverityInfo;
    }

    public Integer getIssuesSeverityMinor() {
        return issuesSeverityMinor;
    }

    public void setIssuesSeverityMinor(Integer issuesSeverityMinor) {
        this.issuesSeverityMinor = issuesSeverityMinor;
    }

    public Integer getIssuesSeverityMajor() {
        return issuesSeverityMajor;
    }

    public void setIssuesSeverityMajor(Integer issuesSeverityMajor) {
        this.issuesSeverityMajor = issuesSeverityMajor;
    }

    public String getQualityGateStatus() {
        return qualityGateStatus;
    }

    public void setQualityGateStatus(String qualityGateStatus) {
        this.qualityGateStatus = qualityGateStatus;
    }

    public String getQualityGateName() {
        return qualityGateName;
    }

    public void setQualityGateName(String qualityGateName) {
        this.qualityGateName = qualityGateName;
    }

    public Integer getNcloc() {
        return ncloc;
    }

    public void setNcloc(Integer ncloc) {
        this.ncloc = ncloc;
    }

    public Integer getCognitiveComplexity() {
        return cognitiveComplexity;
    }

    public void setCognitiveComplexity(Integer cognitiveComplexity) {
        this.cognitiveComplexity = cognitiveComplexity;
    }

    public String getSqaleDebtRatio() {
        return sqaleDebtRatio;
    }

    public void setSqaleDebtRatio(String sqaleDebtRatio) {
        this.sqaleDebtRatio = sqaleDebtRatio;
    }

    public Integer getSecurityRating() {
        return securityRating;
    }

    public void setSecurityRating(Integer securityRating) {
        this.securityRating = securityRating;
    }

    public Integer getReliabilityRating() {
        return reliabilityRating;
    }

    public void setReliabilityRating(Integer reliabilityRating) {
        this.reliabilityRating = reliabilityRating;
    }

    public Integer getDuplicatedLines() {
        return duplicatedLines;
    }

    public void setDuplicatedLines(Integer duplicatedLines) {
        this.duplicatedLines = duplicatedLines;
    }

    public Integer getDuplicatedBlocks() {
        return duplicatedBlocks;
    }

    public void setDuplicatedBlocks(Integer duplicatedBlocks) {
        this.duplicatedBlocks = duplicatedBlocks;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getSonarInstance() {
        return sonarInstance;
    }

    public void setSonarInstance(String sonarInstance) {
        this.sonarInstance = sonarInstance;
    }

    public Integer getTests() {
        return tests;
    }

    public void setTests(Integer tests) {
        this.tests = tests;
    }

    public Integer getComplexity() {
        return complexity;
    }

    public void setComplexity(Integer complexity) {
        this.complexity = complexity;
    }

    public String getNewCoverage() {
        return newCoverage;
    }

    public void setNewCoverage(String newCoverage) {
        this.newCoverage = newCoverage;
    }

    public String getSonarKey() {
        return sonarKey;
    }

    public void setSonarKey(String sonarKey) {
        this.sonarKey = sonarKey;
    }

    public String getAnalysisKey() {
        return analysisKey;
    }

    public void setAnalysisKey(String analysisKey) {
        this.analysisKey = analysisKey;
    }

    public String getProjectMaturity() {
        return projectMaturity;
    }

    public void setProjectMaturity(String projectMaturity) {
        this.projectMaturity = projectMaturity;
    }

    public Integer getUncoveredLines() {
        return uncoveredLines;
    }

    public void setUncoveredLines(Integer uncoveredLines) {
        this.uncoveredLines = uncoveredLines;
    }

    public Integer getLinesToCover() {
        return linesToCover;
    }

    public void setLinesToCover(Integer linesToCover) {
        this.linesToCover = linesToCover;
    }

    public String getLineCoverage() {
        return lineCoverage;
    }

    public void setLineCoverage(String lineCoverage) {
        this.lineCoverage = lineCoverage;
    }

    public String getSqaleRating() {
        return sqaleRating;
    }

    public void setSqaleRating(String sqaleRating) {
        this.sqaleRating = sqaleRating;
    }

    public String getRepoId() {
        return repoId;
    }

    public void setRepoId(String repoId) {
        this.repoId = repoId;
    }

    public String getNewSqaleDebtRatio() {
        return newSqaleDebtRatio;
    }

    public void setNewSqaleDebtRatio(String newSqaleDebtRatio) {
        this.newSqaleDebtRatio = newSqaleDebtRatio;
    }

    public String getNewTechnicalDebt() {
        return newTechnicalDebt;
    }

    public void setNewTechnicalDebt(String newTechnicalDebt) {
        this.newTechnicalDebt = newTechnicalDebt;
    }

    public Integer getSqaleIndex() {
        return sqaleIndex;
    }

    public void setSqaleIndex(Integer sqaleIndex) {
        this.sqaleIndex = sqaleIndex;
    }

    public Integer getNewCodeSmells() {
        return newCodeSmells;
    }

    public void setNewCodeSmells(Integer newCodeSmells) {
        this.newCodeSmells = newCodeSmells;
    }

    public Integer getCodeSmells() {
        return codeSmells;
    }

    public void setCodeSmells(Integer codeSmells) {
        this.codeSmells = codeSmells;
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
        if (!(object instanceof FuentusSonarNorm)) {
            return false;
        }
        FuentusSonarNorm other = (FuentusSonarNorm) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.bbva.scrapper.entities.FuentusSonarNorm[ id=" + id + " ]";
    }
    
}
