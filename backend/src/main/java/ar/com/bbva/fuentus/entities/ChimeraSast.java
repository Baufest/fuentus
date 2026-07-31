package ar.com.bbva.fuentus.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chimera_sast")
@Data
@NoArgsConstructor 
@AllArgsConstructor
public class ChimeraSast {

    @Id
    @Column(name = "project_id", length = 50, nullable = false)
    private String projectId;
    
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    
    @Column(name = "repo_url", length = 255)
    private String repoUrl;
    
    @Column(name = "uuaa", length = 15)
    private String uuaa;
    
    @Column(name = "country_det_name", length = 10)
    private String countryDetName;
    
    @Column(name = "application", length = 100)
    private String application;
    
    @Column(name = "last_scan")
    private LocalDateTime lastScan;
    
    @Column(name = "chimera_url", length = 255)
    private String chimeraUrl;
    
    @Column(name = "branch", length = 100)
    private String branch;
    
    @Column(name = "analyzer", length = 100)
    private String analyzer;
    
    @Column(name = "arq", length = 50)
    private String arq;
    
    @Column(name = "language", length = 50)
    private String language;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "stock_flow")
    private StockFlow stockFlow;
    
    @Column(name = "assumed_1", columnDefinition = "int(11) default 0")
    private Integer assumed1 = 0;
    
    @Column(name = "assumed_2", columnDefinition = "int(11) default 0")
    private Integer assumed2 = 0;
    
    @Column(name = "high", columnDefinition = "int(11) default 0")
    private Integer high = 0;
    
    @Column(name = "critical", columnDefinition = "int(11) default 0")
    private Integer critical = 0;
    
    @Column(name = "medium", columnDefinition = "int(11) default 0")
    private Integer medium = 0;
    
    @Column(name = "low", columnDefinition = "int(11) default 0")
    private Integer low = 0;
    
    @Column(name = "to_review", columnDefinition = "int(11) default 0")
    private Integer toReview = 0;
    
    @Column(name = "`lines`", columnDefinition = "int(11) default 0")
    private Integer lines = 0;
    
    public enum StockFlow {
        stock, flow
    }
}