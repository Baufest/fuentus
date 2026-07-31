/*
 */
package ar.com.bbva.fuentus.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author o002349
 */
@Entity
@Table(name = "app")
public class App implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "vertical")
    private String vertical;
    @Column(name = "folder")
    private String folder;
    @Column(name = "project_id")
    private String projectId;
    @Column(name = "bitbucket_url")
    private String bitbucketUrl;
    @Column(name = "sonar_url")
    private String sonarUrl;
    @Column(name = "sonar9_url")
    private String sonar9Url;
    @Column(name = "sonar10_url")
    private String sonar10Url;
    @Column(name = "samuel_url")
    private String samuelUrl;
    @Column(name = "chimera_url")
    private String chimeraUrl;
    @Column(name = "obsolete")
    private Boolean obsolete;
    @Column(name = "obsolete_by")
    private String obsoleteBy;
    @Column(name = "uuaa")
    private String uuaa;
    @Column(name = "sn1")
    private String sn1;
    @Column(name = "sn2")
    private String sn2;
    @Column(name = "so")
    private String so;
    @Column(name = "ao")
    private String ao;
    @Column(name = "stratos_uol1")
    private String stratosUol1;
    @Column(name = "stratos_uol2")
    private String stratosUol2;
    @Column(name = "stratos_sl1")
    private String stratosSl1;
    @Column(name = "stratos_sl1_owner")
    private String stratosSl1Owner;
    @Column(name = "stratos_sl2")
    private String stratosSl2;
    @Column(name = "stratos_sl2_owner")
    private String stratosSl2Owner;
    @Column(name = "java")
    private Boolean java;
    @Column(name = "unit_test")
    private Boolean unitTest;
    @Column(name = "jest")
    private Boolean jest;
    @Column(name = "monolith")
    private Boolean monolith;
    @Column(name = "config")
    private Boolean config;
    @Column(name = "sonar_key")
    private String sonarKey;
    @Column(name = "vtravk")
    private String vtravk;
    @Column(name = "critical_local")
    private Integer criticalLocal;
    @Column(name = "node_version")
    private String nodeVersion;
    @Column(name = "critical_audit")
    private Boolean criticalAudit;
    @Column(name = "critical_confidential")
    private Boolean criticalConfidential;
    @Column(name = "critical_fraud")
    private Boolean criticalFraud;
    @Column(name = "critical_cfs")
    private Boolean criticalCfs;
    @Lob
    @Column(name = "extra")
    private String extra;
    @Column(name = "architecture")
    private String architecture;
    @Column(name = "source")
    private String source;
    @ManyToOne(fetch = FetchType.LAZY)
    private Nucleus nucleus;

    public App() {
    }

    public App(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVertical() {
        return vertical;
    }

    public void setVertical(String vertical) {
        this.vertical = vertical;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getBitbucketUrl() {
        return bitbucketUrl;
    }

    public void setBitbucketUrl(String bitbucketUrl) {
        this.bitbucketUrl = bitbucketUrl;
    }

    public String getSonarUrl() {
        return sonarUrl;
    }

    public void setSonarUrl(String sonarUrl) {
        this.sonarUrl = sonarUrl;
    }

    public String getSonar9Url() {
        return sonar9Url;
    }

    public void setSonar9Url(String sonar9Url) {
        this.sonar9Url = sonar9Url;
    }

    public String getSonar10Url() {
        return sonar10Url;
    }

    public void setSonar10Url(String sonar10Url) {
        this.sonar10Url = sonar10Url;
    }

    public String getSamuelUrl() {
        return samuelUrl;
    }

    public void setSamuelUrl(String samuelUrl) {
        this.samuelUrl = samuelUrl;
    }

    public String getChimeraUrl() {
        return chimeraUrl;
    }

    public void setChimeraUrl(String chimeraUrl) {
        this.chimeraUrl = chimeraUrl;
    }

    public Boolean getObsolete() {
        return obsolete;
    }

    public void setObsolete(Boolean obsolete) {
        this.obsolete = obsolete;
    }

    public String getObsoleteBy() {
        return obsoleteBy;
    }

    public void setObsoleteBy(String obsoleteBy) {
        this.obsoleteBy = obsoleteBy;
    }

    public String getUuaa() {
        return uuaa;
    }

    public void setUuaa(String uuaa) {
        this.uuaa = uuaa;
    }

    public String getSn1() {
        return sn1;
    }

    public void setSn1(String sn1) {
        this.sn1 = sn1;
    }

    public String getSn2() {
        return sn2;
    }

    public void setSn2(String sn2) {
        this.sn2 = sn2;
    }

    public String getSo() {
        return so;
    }

    public void setSo(String so) {
        this.so = so;
    }

    public String getAo() {
        return ao;
    }

    public void setAo(String ao) {
        this.ao = ao;
    }

    public String getStratosUol1() {
        return stratosUol1;
    }

    public void setStratosUol1(String stratosUol1) {
        this.stratosUol1 = stratosUol1;
    }

    public String getStratosUol2() {
        return stratosUol2;
    }

    public void setStratosUol2(String stratosUol2) {
        this.stratosUol2 = stratosUol2;
    }

    public String getStratosSl1() {
        return stratosSl1;
    }

    public void setStratosSl1(String stratosSl1) {
        this.stratosSl1 = stratosSl1;
    }

    public String getStratosSl2() {
        return stratosSl2;
    }

    public void setStratosSl2(String stratosSl2) {
        this.stratosSl2 = stratosSl2;
    }

    public Boolean getJava() {
        return java;
    }

    public void setJava(Boolean java) {
        this.java = java;
    }

    public Boolean getUnitTest() {
        return unitTest;
    }

    public void setUnitTest(Boolean unitTest) {
        this.unitTest = unitTest;
    }

    public Boolean getJest() {
        return jest;
    }

    public void setJest(Boolean jest) {
        this.jest = jest;
    }

    public Boolean getMonolith() {
        return monolith;
    }

    public void setMonolith(Boolean monolith) {
        this.monolith = monolith;
    }

    public Boolean getConfig() {
        return config;
    }

    public void setConfig(Boolean config) {
        this.config = config;
    }

    public String getSonarKey() {
        return sonarKey;
    }

    public void setSonarKey(String sonarKey) {
        this.sonarKey = sonarKey;
    }

    public String getVtravk() {
        return vtravk;
    }

    public void setVtravk(String vtravk) {
        this.vtravk = vtravk;
    }

    public Integer getCriticalLocal() {
        return criticalLocal;
    }

    public void setCriticalLocal(Integer criticalLocal) {
        this.criticalLocal = criticalLocal;
    }

    public String getNodeVersion() {
        return nodeVersion;
    }

    public void setNodeVersion(String nodeVersion) {
        this.nodeVersion = nodeVersion;
    }

    public Boolean getCriticalAudit() {
        return criticalAudit;
    }

    public void setCriticalAudit(Boolean criticalAudit) {
        this.criticalAudit = criticalAudit;
    }

    public Boolean getCriticalConfidential() {
        return criticalConfidential;
    }

    public void setCriticalConfidential(Boolean criticalConfidential) {
        this.criticalConfidential = criticalConfidential;
    }

    public Boolean getCriticalFraud() {
        return criticalFraud;
    }

    public void setCriticalFraud(Boolean criticalFraud) {
        this.criticalFraud = criticalFraud;
    }

    public Boolean getCriticalCfs() {
        return criticalCfs;
    }

    public void setCriticalCfs(Boolean criticalCfs) {
        this.criticalCfs = criticalCfs;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
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
        if (!(object instanceof App)) {
            return false;
        }
        App other = (App) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.bbva.scrapper.entities.App[ id=" + id + " ]";
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStratosSl1Owner() {
        return stratosSl1Owner;
    }

    public void setStratosSl1Owner(String stratosSl1Owner) {
        this.stratosSl1Owner = stratosSl1Owner;
    }

    public String getStratosSl2Owner() {
        return stratosSl2Owner;
    }

    public void setStratosSl2Owner(String stratosSl2Owner) {
        this.stratosSl2Owner = stratosSl2Owner;
    }

    public Nucleus getNucleus() {
        return nucleus;
    }

    public void setNucleus(Nucleus nucleus) {
        this.nucleus = nucleus;
    }

}
