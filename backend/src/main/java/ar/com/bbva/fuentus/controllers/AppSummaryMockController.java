package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.repositories.AppsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/pruebaApps")
@RestController
public class AppSummaryMockController {

    @Autowired
    private final AppsRepository appsRepository;

    public AppSummaryMockController(AppsRepository appsRepository) {
        this.appsRepository = appsRepository;
    }

//    @GetMapping("/{uuaa}")
//    ResponseEntity<List<AppSummaryMockDTO>> getAppSummaryMock(@PathVariable String uuaa) {
//
//        List<AppSummaryMockDTO> mockList;
//        AppSummaryMockDTO mock1 = new AppSummaryMockDTO();
//
//        mock1.setName("questionnaires-api");
//        mock1.setBitbucketUrl("https://globaldevtools.bbva.com/bitbucket/projects/CANALESINDIVIDUOS/repos/questionnaires-api");
//        mock1.setSonarUrl("https://sonarcloud.io/dashboard?id=bbva%3Aquestionnaires-api");
//        mock1.setBugs(80L);
//        mock1.setCoverage(80.1);
//        mock1.setMonolith(true);
//        mock1.setTotalHigh(30L);
//        mock1.setTotalMedium(20L);
//        mock1.setTotalLow(10L);
//        mock1.setLanguage("Java");
//
//        AppSummaryMockDTO mock2 = new AppSummaryMockDTO();
//
//        mock2.setName("accounts-api");
//        mock2.setBitbucketUrl("https://globaldevtools.bbva.com/bitbucket/projects/CANALESINDIVIDUOS/repos/accounts-api");
//        mock2.setSonarUrl("https://sonarcloud.io/dashboard?id=bbva%3Aaccounts-api");
//        mock2.setBugs(50L);
//        mock2.setCoverage(90.1);
//        mock2.setMonolith(false);
//        mock2.setTotalHigh(20L);
//        mock2.setTotalMedium(10L);
//        mock2.setTotalLow(5L);
//        mock2.setLanguage("Java");
//
//
//        AppSummaryMockDTO mock3 = new AppSummaryMockDTO();
//        mock3.setName("portal-marcas");
//        mock3.setBitbucketUrl("https://globaldevtools.bbva.com/bitbucket/projects/CANALESINDIVIDUOS/repos/portal-marcas");
//        mock3.setSonarUrl("https://sonarcloud.io/dashboard?id=bbva%3Aportal-marcas");
//        mock3.setBugs(20L);
//        mock3.setCoverage(70.1);
//        mock3.setMonolith(true);
//        mock3.setTotalHigh(10L);
//        mock3.setTotalMedium(5L);
//        mock3.setTotalLow(2L);
//        mock3.setLanguage("Java");
//        mock3.setChimeraUrl("https://globaldevtools.bbva.com/bitbucket/projects/CANALESINDIVIDUOS/repos/portal-marcas/browse/chimera");
//
//        if(uuaa.equals("BACI")){
//            mockList = Arrays.asList(mock1, mock2);
//        } else {
//            if(uuaa.equals("AWPM")){
//                mockList = Arrays.asList(mock3);
//            } else {
//                mockList = Collections.emptyList();
//            }
//        }
//
//        return ResponseEntity.ok(mockList);
//
//
//
//    }

    @GetMapping("/{uuaa}")
    public ResponseEntity<?> getConsultas(@PathVariable String uuaa) {
        return ResponseEntity.ok(appsRepository.findAppSummaryByUuaa(uuaa));
    }




}
