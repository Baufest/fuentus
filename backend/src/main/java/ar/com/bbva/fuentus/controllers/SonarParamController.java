package ar.com.bbva.fuentus.controllers;


import ar.com.bbva.fuentus.services.SonarParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/sonar/")
@RestController
public class SonarParamController {


    @Autowired
    private SonarParamService sonarParamService;

    @GetMapping("/{appId}")
    public ResponseEntity<?> getSonarDataByAppId(@PathVariable Long appId){
       return ResponseEntity.ok( sonarParamService.getSonnarDataByAppId(appId));
    }


}
