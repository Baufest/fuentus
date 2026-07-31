package ar.com.bbva.fuentus.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@Component
@Getter
@Setter
public class SonarParamGetRequestDTO {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
//    @Column(name = "id")
//    private Long id;
//    @Basic(optional = false)
//    @Column(name = "app_id")
//    private long appId;
//    @Basic(optional = false)
//    @Column(name = "analisis_date")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date analisisDate;
//    @Column(name = "total_lines")
//    private Long totalLines;
//    @Column(name = "uncovered_lines")
//    private Long uncoveredLines;
//    @Column(name = "bugs")
//    private Long bugs;
//    @Column(name = "bugs_rating")
//    private String bugsRating;
//    @Column(name = "vulnerabilities")
//    private Long vulnerabilities;
//    @Column(name = "vulnerabilities_rating")
//    private String vulnerabilitiesRating;
//    @Column(name = "debt")
//    private String debt;
//    @Column(name = "debt_rating")
//    private String debtRating;
//    @Column(name = "code_smells")
//    private String codeSmells;
//    @Column(name = "duplications")
//    private String duplications;
//    @Column(name = "duplicated_blocks")
//    private String duplicatedBlocks;
//    @Column(name = "coverage")
//    private Double coverage;
//    @Column(name = "branch")
//    private String branch;
//    @Column(name = "sonar_version")
//    private Integer sonarVersion;

     private Long id;

     private Long appId;

     private Date analisisDate;

     private Double Coverage;

     private Long totalLines;

     private Long bugs;


}
