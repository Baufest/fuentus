package ar.com.bbva.fuentus.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StatsSummaryDTO {
    
    private String uuaa;
    private List<RepositoryStatsDTO> repositories = new ArrayList<>();
    
    public StatsSummaryDTO() {}
    
    public StatsSummaryDTO(String uuaa) {
        this.uuaa = uuaa;
    }
    
    public void addRepository(RepositoryStatsDTO repository) {
        this.repositories.add(repository);
    }
    
    @Getter
    @Setter
    public static class RepositoryStatsDTO {
        private String name;
        private String bitbucketUrl;
        private String language;
        private Boolean monolith;
        private List<ServerInfoDTO> servers = new ArrayList<>();
        private SonarInfoDTO sonarInfo;
        
        public RepositoryStatsDTO() {}
        
        public RepositoryStatsDTO(String name, String bitbucketUrl, String language, Boolean monolith) {
            this.name = name;
            this.bitbucketUrl = bitbucketUrl;
            this.language = language;
            this.monolith = monolith;
        }
        
        public void addServer(ServerInfoDTO server) {
            this.servers.add(server);
        }
    }
    
    @Getter
    @Setter
    public static class SonarInfoDTO {
        private String sonarUrl;
        private String sonar10Url;
        private Double coverage;
        private Long bugs;
        private ChimeraSast chimeraSast;
        private ChimeraSca chimeraSca;
        
        public SonarInfoDTO() {}
        
        public SonarInfoDTO(String sonarUrl, String sonar10Url, Double coverage, Long bugs, 
                           Long totalLow, Long totalMedium, Long totalHigh,
                           Long totalLowSca, Long totalMediumSca, Long totalHighSca, Long totalCriticalSca) {
            this.sonarUrl = sonarUrl;
            this.sonar10Url = sonar10Url;
            this.coverage = coverage;
            this.bugs = bugs;
            this.chimeraSast = new ChimeraSast(totalLow, totalMedium, totalHigh);
            this.chimeraSca = new ChimeraSca(totalLowSca, totalMediumSca, totalHighSca, totalCriticalSca);
        }
    }
    
    @Getter
    @Setter
    public static class ChimeraSast {
        private Long totalLow;
        private Long totalMedium;
        private Long totalHigh;
        
        public ChimeraSast() {}
        
        public ChimeraSast(Long totalLow, Long totalMedium, Long totalHigh) {
            this.totalLow = totalLow;
            this.totalMedium = totalMedium;
            this.totalHigh = totalHigh;
        }
    }
    
    @Getter
    @Setter
    public static class ChimeraSca {
        private Long totalLow;
        private Long totalMedium;
        private Long totalHigh;
        private Long totalCritical;
        
        public ChimeraSca() {}
        
        public ChimeraSca(Long totalLow, Long totalMedium, Long totalHigh, Long totalCritical) {
            this.totalLow = totalLow;
            this.totalMedium = totalMedium;
            this.totalHigh = totalHigh;
            this.totalCritical = totalCritical;
        }
    }
}
