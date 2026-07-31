package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class AppSummaryDTO {

     private Long id;

     private String name;

     private String uuaa;

     private String bitbucketUrl;

     private String sonarUrl;

     private String sonar10Url;

     private String chimeraUrl;

     private String samuelUrl;

     private Boolean monolith;

     private Double coverage;

     private Long bugs;

     private String language;

     private ChimeraSast chimeraSast;

     private ChimeraSca chimeraSca;

     private List<ServerInfoDTO> servers = new ArrayList<>();





     public AppSummaryDTO(){}

     @SuppressWarnings("java:S107")
     public AppSummaryDTO(Long id, String name, String uuaa, String bitbucketUrl, String sonarUrl,String sonar10Url, Long bugs, Double coverage, Boolean monolith, Long totalHigh, Long totalMedium, Long totalLow, String language, String chimeraUrl,String samuelUrl, Long totalHighSca, Long totalMediumSca, Long totalLowSca, Long totalCriticalSca) {
          this.id = id;
          this.name = name;
          this.uuaa = uuaa;
          this.bitbucketUrl = bitbucketUrl;
          this.sonarUrl = sonarUrl;
          this.sonar10Url = sonar10Url;
          this.bugs = bugs;
          this.coverage = coverage;
          this.monolith = monolith;
          this.language = language;
          this.chimeraUrl = chimeraUrl;
          this.samuelUrl = samuelUrl;
          this.chimeraSast = new ChimeraSast(totalLow, totalMedium, totalHigh);
          this.chimeraSca = new ChimeraSca(totalLowSca, totalMediumSca, totalHighSca, totalCriticalSca);
     }


     @Getter
     @Setter
     @AllArgsConstructor
     public static class ChimeraSast{
            private Long totalLow;
            private Long totalMedium;
            private Long totalHigh;

     }

     @Getter
     @Setter
     @AllArgsConstructor
     public static class ChimeraSca {
          private Long totalLow;
          private Long totalMedium;
          private Long totalHigh;
          private Long totalCritical;
     }


     public void addServer(ServerInfoDTO serverInfo) {
          this.servers.add(serverInfo);
     }



}
