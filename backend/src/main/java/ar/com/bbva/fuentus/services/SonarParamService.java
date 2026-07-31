package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.SonarParamGetRequestDTO;
import ar.com.bbva.fuentus.entities.SonarParam;
import ar.com.bbva.fuentus.mapper.SonarParamMapper;
import ar.com.bbva.fuentus.repositories.SonarParamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;

@Service
public class SonarParamService {


    @Autowired
    private SonarParamsRepository repository;


    public SonarParamGetRequestDTO getSonnarDataByAppId(Long appId) {
          SonarParam sonarParamDB = repository.findLatestByAppId(appId);
         return  SonarParamMapper.mapSonar2SonarParamGetRequestDTO(sonarParamDB);
    }

}
