package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UuaaSummaryDTO {
    
    private String uuaa;
    private Integer totalApps;
    private Double averageCoverage;
    private Long totalBugs;
    
    // Chimera SAST totals
    private Long totalSastLow;
    private Long totalSastMedium;
    private Long totalSastHigh;
    
    // Chimera SCA totals
    private Long totalScaLow;
    private Long totalScaMedium;
    private Long totalScaHigh;
    private Long totalScaCritical;
    
    // RFO Information
    private Long rfoId;
    private String rfoEstado;
    
    // Computed totals
    public Long getTotalSastVulnerabilities() {
        return (totalSastLow != null ? totalSastLow : 0) +
               (totalSastMedium != null ? totalSastMedium : 0) +
               (totalSastHigh != null ? totalSastHigh : 0);
    }
    
    public Long getTotalScaVulnerabilities() {
        return (totalScaLow != null ? totalScaLow : 0) +
               (totalScaMedium != null ? totalScaMedium : 0) +
               (totalScaHigh != null ? totalScaHigh : 0) +
               (totalScaCritical != null ? totalScaCritical : 0);
    }
    
    public Long getTotalVulnerabilities() {
        return getTotalSastVulnerabilities() + getTotalScaVulnerabilities();
    }
}
