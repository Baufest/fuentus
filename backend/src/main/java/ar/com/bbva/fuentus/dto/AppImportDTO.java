package ar.com.bbva.fuentus.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.opencsv.bean.CsvBindByName;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppImportDTO {
    
    @CsvBindByName(column = "name")
    @JsonProperty("name")
    private String name;
    
    @CsvBindByName(column = "projectId")
    @JsonProperty("projectId")
    private String projectId;
    
    @CsvBindByName(column = "vertical")
    @JsonProperty("vertical")
    private String vertical;
    
    @CsvBindByName(column = "folder")
    @JsonProperty("folder")
    private String folder;
    
    @CsvBindByName(column = "bitbucketUrl")
    @JsonProperty("bitbucketUrl")
    private String bitbucketUrl;
    
    @CsvBindByName(column = "sonarUrl")
    @JsonProperty("sonarUrl")
    private String sonarUrl;
    
    @CsvBindByName(column = "sonar9Url")
    @JsonProperty("sonar9Url")
    private String sonar9Url;
    
    @CsvBindByName(column = "sonar10Url")
    @JsonProperty("sonar10Url")
    private String sonar10Url;
    
    @CsvBindByName(column = "samuelUrl")
    @JsonProperty("samuelUrl")
    private String samuelUrl;
    
    @CsvBindByName(column = "chimeraUrl")
    @JsonProperty("chimeraUrl")
    private String chimeraUrl;
    
    @CsvBindByName(column = "obsolete")
    @JsonProperty("obsolete")
    private Boolean obsolete;
    
    @CsvBindByName(column = "obsoleteBy")
    @JsonProperty("obsoleteBy")
    private String obsoleteBy;
    
    @CsvBindByName(column = "uuaa")
    @JsonProperty("uuaa")
    private String uuaa;
    
    @CsvBindByName(column = "sn1")
    @JsonProperty("sn1")
    private String sn1;
    
    @CsvBindByName(column = "sn2")
    @JsonProperty("sn2")
    private String sn2;
    
    @CsvBindByName(column = "so")
    @JsonProperty("so")
    private String so;
    
    @CsvBindByName(column = "ao")
    @JsonProperty("ao")
    private String ao;
    
    @CsvBindByName(column = "stratosUol1")
    @JsonProperty("stratosUol1")
    private String stratosUol1;
    
    @CsvBindByName(column = "stratosUol2")
    @JsonProperty("stratosUol2")
    private String stratosUol2;
    
    @CsvBindByName(column = "stratosSl1")
    @JsonProperty("stratosSl1")
    private String stratosSl1;
    
    @CsvBindByName(column = "stratosSl1Owner")
    @JsonProperty("stratosSl1Owner")
    private String stratosSl1Owner;
    
    @CsvBindByName(column = "stratosSl2")
    @JsonProperty("stratosSl2")
    private String stratosSl2;
    
    @CsvBindByName(column = "stratosSl2Owner")
    @JsonProperty("stratosSl2Owner")
    private String stratosSl2Owner;
    
    @CsvBindByName(column = "java")
    @JsonProperty("java")
    private Boolean java;
    
    @CsvBindByName(column = "unitTest")
    @JsonProperty("unitTest")
    private Boolean unitTest;
    
    @CsvBindByName(column = "jest")
    @JsonProperty("jest")
    private Boolean jest;
    
    @CsvBindByName(column = "monolith")
    @JsonProperty("monolith")
    private Boolean monolith;
    
    @CsvBindByName(column = "config")
    @JsonProperty("config")
    private Boolean config;
    
    @CsvBindByName(column = "sonarKey")
    @JsonProperty("sonarKey")
    private String sonarKey;
    
    @CsvBindByName(column = "vtravk")
    @JsonProperty("vtravk")
    private String vtravk;
    
    @CsvBindByName(column = "criticalLocal")
    @JsonProperty("criticalLocal")
    private Integer criticalLocal;
    
    @CsvBindByName(column = "nodeVersion")
    @JsonProperty("nodeVersion")
    private String nodeVersion;
    
    @CsvBindByName(column = "criticalAudit")
    @JsonProperty("criticalAudit")
    private Boolean criticalAudit;
    
    @CsvBindByName(column = "criticalConfidential")
    @JsonProperty("criticalConfidential")
    private Boolean criticalConfidential;
    
    @CsvBindByName(column = "criticalFraud")
    @JsonProperty("criticalFraud")
    private Boolean criticalFraud;
    
    @CsvBindByName(column = "criticalCfs")
    @JsonProperty("criticalCfs")
    private Boolean criticalCfs;
    
    @CsvBindByName(column = "extra")
    @JsonProperty("extra")
    private String extra;
    
    @CsvBindByName(column = "architecture")
    @JsonProperty("architecture")
    private String architecture;
    
    @CsvBindByName(column = "source")
    @JsonProperty("source")
    private String source;
}