package ar.com.bbva.fuentus.mapper;


import ar.com.bbva.fuentus.dto.SonarParamGetRequestDTO;
import ar.com.bbva.fuentus.entities.SonarParam;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class SonarParamMapper {

    static public SonarParamGetRequestDTO mapSonar2SonarParamGetRequestDTO(SonarParam sonarParam){

        if(sonarParam == null ){
            return null;
        }

        SonarParamGetRequestDTO sonarDTO = new SonarParamGetRequestDTO();
        sonarDTO.setId(sonarParam.getId());
        sonarDTO.setCoverage(sonarParam.getCoverage());
        sonarDTO.setAppId(sonarParam.getAppId());
        sonarDTO.setTotalLines(sonarParam.getTotalLines());
        sonarDTO.setAnalisisDate(sonarParam.getAnalisisDate());
        sonarDTO.setBugs(sonarParam.getBugs());

        return sonarDTO;



    }

}
