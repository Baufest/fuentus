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
@Table(name = "sonar_params")
public class SonarParam implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "app_id")
    private long appId;
    @Basic(optional = false)
    @Column(name = "analisis_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date analisisDate;
    @Column(name = "total_lines")
    private Long totalLines;
    @Column(name = "uncovered_lines")
    private Long uncoveredLines;
    @Column(name = "bugs")
    private Long bugs;
    @Column(name = "bugs_rating")
    private String bugsRating;
    @Column(name = "vulnerabilities")
    private Long vulnerabilities;
    @Column(name = "vulnerabilities_rating")
    private String vulnerabilitiesRating;
    @Column(name = "debt")
    private String debt;
    @Column(name = "debt_rating")
    private String debtRating;
    @Column(name = "code_smells")
    private String codeSmells;
    @Column(name = "duplications")
    private String duplications;
    @Column(name = "duplicated_blocks")
    private String duplicatedBlocks;
    @Column(name = "coverage")
    private Double coverage;
    @Column(name = "branch")
    private String branch;
    @Column(name = "sonar_version")
    private Integer sonarVersion;

    public SonarParam() {
    }

    public SonarParam(Long id) {
        this.id = id;
    }

    public SonarParam(Long id, long appId, Date analisisDate) {
        this.id = id;
        this.appId = appId;
        this.analisisDate = analisisDate;
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

    public Date getAnalisisDate() {
        return analisisDate;
    }

    public void setAnalisisDate(Date analisisDate) {
        this.analisisDate = analisisDate;
    }

    public Long getTotalLines() {
        return totalLines;
    }

    public void setTotalLines(Long totalLines) {
        this.totalLines = totalLines;
    }

    public Long getUncoveredLines() {
        return uncoveredLines;
    }

    public void setUncoveredLines(Long uncoveredLines) {
        this.uncoveredLines = uncoveredLines;
    }

    public Long getBugs() {
        return bugs;
    }

    public void setBugs(Long bugs) {
        this.bugs = bugs;
    }

    public String getBugsRating() {
        return bugsRating;
    }

    public void setBugsRating(String bugsRating) {
        this.bugsRating = bugsRating;
    }

    public void setVulnerabilities(Long vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

    public Long getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilitiesRating(String vulnerabilitiesRating) {
        this.vulnerabilitiesRating = vulnerabilitiesRating;
    }

    public String getVulnerabilitiesRating() {
        return vulnerabilitiesRating;
    }

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public String getDebtRating() {
        return debtRating;
    }

    public void setDebtRating(String debtRating) {
        this.debtRating = debtRating;
    }

    public String getCodeSmells() {
        return codeSmells;
    }

    public void setCodeSmells(String codeSmells) {
        this.codeSmells = codeSmells;
    }

    public String getDuplications() {
        return duplications;
    }

    public void setDuplications(String duplications) {
        this.duplications = duplications;
    }

    public String getDuplicatedBlocks() {
        return duplicatedBlocks;
    }

    public void setDuplicatedBlocks(String duplicatedBlocks) {
        this.duplicatedBlocks = duplicatedBlocks;
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
        if (!(object instanceof SonarParam)) {
            return false;
        }
        SonarParam other = (SonarParam) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.bbva.scrapper.entities.SonarParam[ id=" + id + " ]";
    }

    public Double getCoverage() {
        return coverage;
    }

    public void setCoverage(Double coverage) {
        this.coverage = coverage;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Integer getSonarVersion() {
        return sonarVersion;
    }

    public void setSonarVersion(Integer sonarVersion) {
        this.sonarVersion = sonarVersion;
    }

}
