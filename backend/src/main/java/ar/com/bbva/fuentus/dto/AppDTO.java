package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppDTO {

    private Long id;
    private String name;
    private String vertical;
    private String folder;
    private String projectId;
    private String bitbucketUrl;
    private String sonarUrl;
    private String sonar9Url;
    private String sonar10Url;
    private String samuelUrl;
    private String chimeraUrl;
    private Boolean obsolete;
    private String obsoleteBy;
    private String uuaa;
    private String sn1;
    private String sn2;
    private String so;
    private String ao;
    private String stratosUol1;
    private String stratosUol2;
    private String stratosSl1;
    private String stratosSl1Owner;
    private String stratosSl2;
    private String stratosSl2Owner;
    private Boolean java;
    private Boolean unitTest;
    private Boolean jest;
    private Boolean monolith;
    private Boolean config;
    private String sonarKey;
    private String vtravk;
    private Integer criticalLocal;
    private String nodeVersion;
    private Boolean criticalAudit;
    private Boolean criticalConfidential;
    private Boolean criticalFraud;
    private Boolean criticalCfs;
    private String extra;
    private String architecture;
    private String source;

    // Campos de Nucleus
    private Long nucleusId;
    private String nucleusServiceN1;
    private String nucleusServiceN2;
    private String nucleusUol2;
    private String nucleusVertical;

}
