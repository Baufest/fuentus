package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceSummaryDTO {
    
    // Service identification
    private Long serviceId;
    
    // Service information
    private String serviceN1;
    private String serviceN2;
    private String ownerServiceN1;
    private List<String> uuaas;
    
    // Aggregated metrics
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
    
    // RFO Information (from the service)
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
