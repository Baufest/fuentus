package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.RankingDTO;
import ar.com.bbva.fuentus.dto.TotalizadoresDTO;
import ar.com.bbva.fuentus.services.TotalizadoresService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para obtener los totalizadores con los mejores niveles organizacionales
 */
@Log4j2
@RestController
@RequestMapping("/api/totalizadores")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class TotalizadoresController {

    @Autowired
    private TotalizadoresService totalizadoresService;

    /**
     * Endpoint para obtener los mejores niveles organizacionales según filtros
     * 
     * @param vertical Filtro opcional por vertical
     * @param fabrica Filtro opcional por fábrica
     * @param sn1 Filtro opcional por SN1
     * @param sn2 Filtro opcional por SN2
     * @return TotalizadoresDTO con los mejores niveles según filtros
     */
    @GetMapping
    public ResponseEntity<TotalizadoresDTO> getMejoresNiveles(
            @RequestParam(required = false) String vertical,
            @RequestParam(required = false) String fabrica,
            @RequestParam(required = false) String sn1,
            @RequestParam(required = false) String sn2) {
        
        try {
            log.info("GET /api/totalizadores - Vertical: {}, Fabrica: {}, SN1: {}, SN2: {}", 
                     vertical, fabrica, sn1, sn2);
            
            TotalizadoresDTO resultado = totalizadoresService.getMejoresNiveles(vertical, fabrica, sn1, sn2);
            
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            log.error("Error al obtener totalizadores", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para obtener el ranking top 3 de un nivel organizacional específico
     * 
     * @param nivelTipo Tipo de nivel (VERTICAL, FABRICA, SN1, SN2)
     * @param metricaTipo Tipo de métrica (PRODUCTIVIDAD, LT, CT)
     * @param vertical Filtro opcional por vertical
     * @param fabrica Filtro opcional por fábrica
     * @param sn1 Filtro opcional por SN1
     * @param sn2 Filtro opcional por SN2
     * @return RankingDTO con el top 3
     */
    @GetMapping("/ranking")
    public ResponseEntity<RankingDTO> getRankingTop3(
            @RequestParam String nivelTipo,
            @RequestParam String metricaTipo,
            @RequestParam(required = false) String vertical,
            @RequestParam(required = false) String fabrica,
            @RequestParam(required = false) String sn1,
            @RequestParam(required = false) String sn2) {
        
        try {
            log.info("GET /api/totalizadores/ranking - Nivel: {}, Métrica: {}, Filtros - Vertical: {}, Fabrica: {}, SN1: {}, SN2: {}", 
                     nivelTipo, metricaTipo, vertical, fabrica, sn1, sn2);
            
            RankingDTO resultado = totalizadoresService.getRankingTop3(nivelTipo, metricaTipo, vertical, fabrica, sn1, sn2);
            
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            log.error("Error al obtener ranking", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
