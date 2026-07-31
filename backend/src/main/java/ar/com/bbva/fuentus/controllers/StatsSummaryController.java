package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.CountSummaryDTO;
import ar.com.bbva.fuentus.dto.NucleusCoverageStatsSummary;
import ar.com.bbva.fuentus.dto.StatsSummaryDTO;
import ar.com.bbva.fuentus.services.StatsSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/stats-summary")
@RestController
public class StatsSummaryController {

    @Autowired
    private StatsSummaryService statsSummaryService;

    @GetMapping("/filters")
    public ResponseEntity<List<StatsSummaryDTO>> getStatsSummaryByFilters(
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String orgN1,
            @RequestParam(required = false) String orgN2fabrica,
            @RequestParam(defaultValue = "0") int page) {

        List<StatsSummaryDTO> statsSummary = statsSummaryService.getStatsSummaryByFilters(area, orgN1, orgN2fabrica, page);
        return ResponseEntity.ok(statsSummary);
    }

    @GetMapping("/count")
    public ResponseEntity<CountSummaryDTO> getCountSummary(
            @RequestParam(required = false) String vertical,
            @RequestParam(required = false) String fabrica,
            @RequestParam(required = false) String sn1,
            @RequestParam(required = false) String sn2,
            @RequestParam(required = false) String uuaa) {

        CountSummaryDTO countSummary = statsSummaryService.getCountSummary(vertical, fabrica, sn1, sn2, uuaa);
        return ResponseEntity.ok(countSummary);
    }

    /**
     * Obtiene las estadísticas de coverage organizadas por niveles según los filtros
     * @param vertical Nombre de la vertical (opcional)
     * @param uol2 Nombre del UOL2/orgN2fabrica (opcional)
     * @param sn1 Nombre del SN1 (opcional)
     * @param sn2 Nombre del SN2 (opcional)
     * @param uuaa Nombre de la UUAA (opcional)
     * @return Lista de NucleusCoverageStatsSummary con los datos organizados por nivel
     */
    @GetMapping("/coverage-average")
    public ResponseEntity<List<NucleusCoverageStatsSummary>> getCoverageAverage(
            @RequestParam(required = false) String vertical,
            @RequestParam(required = false) String uol2,
            @RequestParam(required = false) String sn1,
            @RequestParam(required = false) String sn2,
            @RequestParam(required = false) String uuaa) {
        
        List<NucleusCoverageStatsSummary> coverageStats = statsSummaryService.getCoverageStatsByLevel(vertical, uol2, sn1, sn2, uuaa);
        return ResponseEntity.ok(coverageStats);
    }
}
