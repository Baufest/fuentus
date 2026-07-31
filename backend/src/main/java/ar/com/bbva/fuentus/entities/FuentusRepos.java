package ar.com.bbva.fuentus.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author seba
 */
@Entity
@Table(name = "fuentus_repos")
public class FuentusRepos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "period_start")
    @Temporal(TemporalType.DATE)
    private Date periodStart;
    @Column(name = "architecture")
    private String architecture;
    @Column(name = "geo")
    private String geo;
    @Column(name = "repo")
    private String repo;
    @Column(name = "repo_url")
    private String repoUrl;
    @Column(name = "browse_url")
    private String browseUrl;
    @Column(name = "console_managed")
    private Boolean consoleManaged;
    @Lob
    @Column(name = "languages_list")
    private String languagesList;
    @Lob
    @Column(name = "languages_count")
    private String languagesCount;
    @Column(name = "is_frontend")
    private Boolean isFrontend;
    @Column(name = "technology")
    private String technology;
    @Column(name = "category")
    private String category;
    @Column(name = "category_group")
    private String categoryGroup;
    @Column(name = "period_type")
    private String periodType;
    @Column(name = "type")
    private String type;
    @Column(name = "is_ether")
    private Boolean isEther;
    @Column(name = "architecture_in_period")
    private String architectureInPeriod;
    @Column(name = "commits")
    private Integer commits;
    @Column(name = "pushes")
    private Integer pushes;
    @Column(name = "committers")
    private Integer committers;
    @Column(name = "commits_in_perimeter")
    private Integer commitsInPerimeter;
    @Column(name = "pushes_in_perimeter")
    private Integer pushesInPerimeter;
    @Column(name = "top_pusher_hash")
    private String topPusherHash;
    @Column(name = "top_pushes")
    private Integer topPushes;
    @Column(name = "top_commiter_hash")
    private String topCommiterHash;
    @Column(name = "top_commits")
    private Integer topCommits;
    @Column(name = "geo_in_period")
    private String geoInPeriod;
    @Column(name = "top_perimeter")
    private String topPerimeter;
    @Column(name = "pushes_pct50")
    private Integer pushesPct50;
    @Column(name = "commits_pct50")
    private Integer commitsPct50;
    @Column(name = "committers_over_threshold")
    private Integer committersOverThreshold;
    @Column(name = "committers_in_perimeter")
    private Integer committersInPerimeter;
    @Column(name = "rank")
    private Integer rank;
    @Column(name = "has_unit_tests")
    private Boolean hasUnitTests;
    @Column(name = "has_integration_tests")
    private Boolean hasIntegrationTests;

    public FuentusRepos() {
    }

    public FuentusRepos(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Date periodStart) {
        this.periodStart = periodStart;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
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

    public String getBrowseUrl() {
        return browseUrl;
    }

    public void setBrowseUrl(String browseUrl) {
        this.browseUrl = browseUrl;
    }

    public Boolean getConsoleManaged() {
        return consoleManaged;
    }

    public void setConsoleManaged(Boolean consoleManaged) {
        this.consoleManaged = consoleManaged;
    }

    public String getLanguagesList() {
        return languagesList;
    }

    public void setLanguagesList(String languagesList) {
        this.languagesList = languagesList;
    }

    public String getLanguagesCount() {
        return languagesCount;
    }

    public void setLanguagesCount(String languagesCount) {
        this.languagesCount = languagesCount;
    }

    public Boolean getIsFrontend() {
        return isFrontend;
    }

    public void setIsFrontend(Boolean isFrontend) {
        this.isFrontend = isFrontend;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryGroup() {
        return categoryGroup;
    }

    public void setCategoryGroup(String categoryGroup) {
        this.categoryGroup = categoryGroup;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsEther() {
        return isEther;
    }

    public void setIsEther(Boolean isEther) {
        this.isEther = isEther;
    }

    public String getArchitectureInPeriod() {
        return architectureInPeriod;
    }

    public void setArchitectureInPeriod(String architectureInPeriod) {
        this.architectureInPeriod = architectureInPeriod;
    }

    public Integer getCommits() {
        return commits;
    }

    public void setCommits(Integer commits) {
        this.commits = commits;
    }

    public Integer getPushes() {
        return pushes;
    }

    public void setPushes(Integer pushes) {
        this.pushes = pushes;
    }

    public Integer getCommitters() {
        return committers;
    }

    public void setCommitters(Integer committers) {
        this.committers = committers;
    }

    public Integer getCommitsInPerimeter() {
        return commitsInPerimeter;
    }

    public void setCommitsInPerimeter(Integer commitsInPerimeter) {
        this.commitsInPerimeter = commitsInPerimeter;
    }

    public Integer getPushesInPerimeter() {
        return pushesInPerimeter;
    }

    public void setPushesInPerimeter(Integer pushesInPerimeter) {
        this.pushesInPerimeter = pushesInPerimeter;
    }

    public String getTopPusherHash() {
        return topPusherHash;
    }

    public void setTopPusherHash(String topPusherHash) {
        this.topPusherHash = topPusherHash;
    }

    public Integer getTopPushes() {
        return topPushes;
    }

    public void setTopPushes(Integer topPushes) {
        this.topPushes = topPushes;
    }

    public String getTopCommiterHash() {
        return topCommiterHash;
    }

    public void setTopCommiterHash(String topCommiterHash) {
        this.topCommiterHash = topCommiterHash;
    }

    public Integer getTopCommits() {
        return topCommits;
    }

    public void setTopCommits(Integer topCommits) {
        this.topCommits = topCommits;
    }

    public String getGeoInPeriod() {
        return geoInPeriod;
    }

    public void setGeoInPeriod(String geoInPeriod) {
        this.geoInPeriod = geoInPeriod;
    }

    public String getTopPerimeter() {
        return topPerimeter;
    }

    public void setTopPerimeter(String topPerimeter) {
        this.topPerimeter = topPerimeter;
    }

    public Integer getPushesPct50() {
        return pushesPct50;
    }

    public void setPushesPct50(Integer pushesPct50) {
        this.pushesPct50 = pushesPct50;
    }

    public Integer getCommitsPct50() {
        return commitsPct50;
    }

    public void setCommitsPct50(Integer commitsPct50) {
        this.commitsPct50 = commitsPct50;
    }

    public Integer getCommittersOverThreshold() {
        return committersOverThreshold;
    }

    public void setCommittersOverThreshold(Integer committersOverThreshold) {
        this.committersOverThreshold = committersOverThreshold;
    }

    public Integer getCommittersInPerimeter() {
        return committersInPerimeter;
    }

    public void setCommittersInPerimeter(Integer committersInPerimeter) {
        this.committersInPerimeter = committersInPerimeter;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Boolean getHasUnitTests() {
        return hasUnitTests;
    }

    public void setHasUnitTests(Boolean hasUnitTests) {
        this.hasUnitTests = hasUnitTests;
    }

    public Boolean getHasIntegrationTests() {
        return hasIntegrationTests;
    }

    public void setHasIntegrationTests(Boolean hasIntegrationTests) {
        this.hasIntegrationTests = hasIntegrationTests;
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
        if (!(object instanceof FuentusRepos)) {
            return false;
        }
        FuentusRepos other = (FuentusRepos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.bbva.scrapper.entities.FuentusRepos[ id=" + id + " ]";
    }
    
}
