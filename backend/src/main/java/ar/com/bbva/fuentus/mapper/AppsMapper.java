package ar.com.bbva.fuentus.mapper;

import ar.com.bbva.fuentus.dto.AppDTO;
import ar.com.bbva.fuentus.dto.AppGetRequestDTO;
import ar.com.bbva.fuentus.entities.App;
import org.springframework.stereotype.Component;

@Component
public class AppsMapper {

    static public AppDTO mapApp2DTO(App app) {
        if (app == null) {
            return null;
        }
        
        AppDTO.AppDTOBuilder builder = AppDTO.builder()
                .id(app.getId())
                .name(app.getName())
                .vertical(app.getVertical())
                .folder(app.getFolder())
                .projectId(app.getProjectId())
                .bitbucketUrl(app.getBitbucketUrl())
                .sonarUrl(app.getSonarUrl())
                .sonar9Url(app.getSonar9Url())
                .sonar10Url(app.getSonar10Url())
                .samuelUrl(app.getSamuelUrl())
                .chimeraUrl(app.getChimeraUrl())
                .obsolete(app.getObsolete())
                .obsoleteBy(app.getObsoleteBy())
                .uuaa(app.getUuaa())
                .sn1(app.getSn1())
                .sn2(app.getSn2())
                .so(app.getSo())
                .ao(app.getAo())
                .stratosUol1(app.getStratosUol1())
                .stratosUol2(app.getStratosUol2())
                .stratosSl1(app.getStratosSl1())
                .stratosSl1Owner(app.getStratosSl1Owner())
                .stratosSl2(app.getStratosSl2())
                .stratosSl2Owner(app.getStratosSl2Owner())
                .java(app.getJava())
                .unitTest(app.getUnitTest())
                .jest(app.getJest())
                .monolith(app.getMonolith())
                .config(app.getConfig())
                .sonarKey(app.getSonarKey())
                .vtravk(app.getVtravk())
                .criticalLocal(app.getCriticalLocal())
                .nodeVersion(app.getNodeVersion())
                .criticalAudit(app.getCriticalAudit())
                .criticalConfidential(app.getCriticalConfidential())
                .criticalFraud(app.getCriticalFraud())
                .criticalCfs(app.getCriticalCfs())
                .extra(app.getExtra())
                .architecture(app.getArchitecture())
                .source(app.getSource());
        
        // Agregar campos de Nucleus si existe la relación
        if (app.getNucleus() != null) {
            builder.nucleusId(app.getNucleus().getId())
                   .nucleusServiceN1(app.getNucleus().getServiceN1())
                   .nucleusServiceN2(app.getNucleus().getServiceN2())
                   .nucleusUol2(app.getNucleus().getOrgN2fabrica());
            
            // Obtener la vertical desde VerticalNucleus
            if (app.getNucleus().getVerticalNucleus() != null && !app.getNucleus().getVerticalNucleus().isEmpty()) {
                builder.nucleusVertical(app.getNucleus().getVerticalNucleus().get(0).getVertical() != null 
                    ? app.getNucleus().getVerticalNucleus().get(0).getVertical().getName() 
                    : null);
            }
        }
        
        return builder.build();
    }

    static public AppGetRequestDTO mapApp2GetRequestDTO(App app) {
        if (app == null) {
            return null;
        }
        AppGetRequestDTO appGetRequestDTO = new AppGetRequestDTO();
        appGetRequestDTO.setSo(app.getSo());
        appGetRequestDTO.setSn1(app.getSn1());
        appGetRequestDTO.setSn2(app.getSn2());
        appGetRequestDTO.setUuaa(app.getUuaa());

        return appGetRequestDTO;
    }

}
