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
@Table(name = "fuentus_sonar")
public class FuentusSonar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "analysis_key")
    private String analysisKey;
    @Column(name = "period_start")
    @Temporal(TemporalType.DATE)
    private Date periodStart;
    @Basic(optional = false)
    @Column(name = "event_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventTimestamp;
    @Column(name = "sonar_key")
    private String sonarKey;
    @Column(name = "sonar_instance")
    private String sonarInstance;
    @Column(name = "repo_url")
    private String repoUrl;
    @Column(name = "repo")
    private String repo;
    @Column(name = "repo_from_url")
    private String repoFromUrl;
    @Column(name = "branch")
    private String branch;
    @Column(name = "quality_gate_name")
    private String qualityGateName;
    @Column(name = "quality_gate_status")
    private String qualityGateStatus;
    @Column(name = "commit")
    private String commit;
    @Column(name = "geo")
    private String geo;
    @Column(name = "architecture")
    private String architecture;
    @Column(name = "test")
    private String test;
    @Column(name = "coverage")
    private String coverage;
    @Column(name = "new_code_coverage")
    private String newCodeCoverage;
    @Column(name = "duplicated_blocks")
    private Integer duplicatedBlocks;
    @Column(name = "duplicated_lines")
    private Integer duplicatedLines;
    @Column(name = "reliability_rating")
    private String reliabilityRating;
    @Column(name = "security_rating")
    private String securityRating;
    @Column(name = "sqale_debt_ratio")
    private String sqaleDebtRatio;
    @Column(name = "cognitive_complexity")
    private Integer cognitiveComplexity;
    @Column(name = "ncloc")
    private Integer ncloc;
    @Column(name = "issues_severity_blocker")
    private Integer issuesSeverityBlocker;
    @Column(name = "issues_severity_critical")
    private Integer issuesSeverityCritical;
    @Column(name = "issues_severity_major")
    private Integer issuesSeverityMajor;
    @Column(name = "issues_severity_minor")
    private Integer issuesSeverityMinor;
    @Column(name = "issues_severity_info")
    private Integer issuesSeverityInfo;
    @Column(name = "issues_type_bug")
    private Integer issuesTypeBug;
    @Column(name = "vulnerabilities")
    private Integer vulnerabilities;
    @Column(name = "issues_type_code_smell")
    private Integer issuesTypeCodeSmell;

    public FuentusSonar() {
    }

    public FuentusSonar(Long id) {
        this.id = id;
    }

    public FuentusSonar(Long id, Date eventTimestamp) {
        this.id = id;
        this.eventTimestamp = eventTimestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnalysisKey() {
        return analysisKey;
    }

    public void setAnalysisKey(String analysisKey) {
        this.analysisKey = analysisKey;
    }

    public Date getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Date periodStart) {
        this.periodStart = periodStart;
    }

    public Date getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(Date eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public String getSonarKey() {
        return sonarKey;
    }

    public void setSonarKey(String sonarKey) {
        this.sonarKey = sonarKey;
    }

    public String getSonarInstance() {
        return sonarInstance;
    }

    public void setSonarInstance(String sonarInstance) {
        this.sonarInstance = sonarInstance;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getRepoFromUrl() {
        return repoFromUrl;
    }

    public void setRepoFromUrl(String repoFromUrl) {
        this.repoFromUrl = repoFromUrl;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getQualityGateName() {
        return qualityGateName;
    }

    public void setQualityGateName(String qualityGateName) {
        this.qualityGateName = qualityGateName;
    }

    public String getQualityGateStatus() {
        return qualityGateStatus;
    }

    public void setQualityGateStatus(String qualityGateStatus) {
        this.qualityGateStatus = qualityGateStatus;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getNewCodeCoverage() {
        return newCodeCoverage;
    }

    public void setNewCodeCoverage(String newCodeCoverage) {
        this.newCodeCoverage = newCodeCoverage;
    }

    public Integer getDuplicatedBlocks() {
        return duplicatedBlocks;
    }

    public void setDuplicatedBlocks(Integer duplicatedBlocks) {
        this.duplicatedBlocks = duplicatedBlocks;
    }

    public Integer getDuplicatedLines() {
        return duplicatedLines;
    }

    public void setDuplicatedLines(Integer duplicatedLines) {
        this.duplicatedLines = duplicatedLines;
    }

    public String getReliabilityRating() {
        return reliabilityRating;
    }

    public void setReliabilityRating(String reliabilityRating) {
        this.reliabilityRating = reliabilityRating;
    }

    public String getSecurityRating() {
        return securityRating;
    }

    public void setSecurityRating(String securityRating) {
        this.securityRating = securityRating;
    }

    public String getSqaleDebtRatio() {
        return sqaleDebtRatio;
    }

    public void setSqaleDebtRatio(String sqaleDebtRatio) {
        this.sqaleDebtRatio = sqaleDebtRatio;
    }

    public Integer getCognitiveComplexity() {
        return cognitiveComplexity;
    }

    public void setCognitiveComplexity(Integer cognitiveComplexity) {
        this.cognitiveComplexity = cognitiveComplexity;
    }

    public Integer getNcloc() {
        return ncloc;
    }

    public void setNcloc(Integer ncloc) {
        this.ncloc = ncloc;
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

    public Integer getIssuesSeverityMajor() {
        return issuesSeverityMajor;
    }

    public void setIssuesSeverityMajor(Integer issuesSeverityMajor) {
        this.issuesSeverityMajor = issuesSeverityMajor;
    }

    public Integer getIssuesSeverityMinor() {
        return issuesSeverityMinor;
    }

    public void setIssuesSeverityMinor(Integer issuesSeverityMinor) {
        this.issuesSeverityMinor = issuesSeverityMinor;
    }

    public Integer getIssuesSeverityInfo() {
        return issuesSeverityInfo;
    }

    public void setIssuesSeverityInfo(Integer issuesSeverityInfo) {
        this.issuesSeverityInfo = issuesSeverityInfo;
    }

    public Integer getIssuesTypeBug() {
        return issuesTypeBug;
    }

    public void setIssuesTypeBug(Integer issuesTypeBug) {
        this.issuesTypeBug = issuesTypeBug;
    }

    public Integer getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilities(Integer vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

    public Integer getIssuesTypeCodeSmell() {
        return issuesTypeCodeSmell;
    }

    public void setIssuesTypeCodeSmell(Integer issuesTypeCodeSmell) {
        this.issuesTypeCodeSmell = issuesTypeCodeSmell;
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
        if (!(object instanceof FuentusSonar)) {
            return false;
        }
        FuentusSonar other = (FuentusSonar) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.bbva.scrapper.entities.FuentusSonar[ id=" + id + " ]";
    }
    
}
