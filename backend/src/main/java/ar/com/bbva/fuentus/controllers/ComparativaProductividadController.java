package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.ComparativaProductividadDTO;
import ar.com.bbva.fuentus.services.ComparativaProductividadService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para obtener comparativas de productividad por niveles organizacionales
 */
@Log4j2
@RestController
@RequestMapping("/api/comparativa-productividad")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class ComparativaProductividadController {

    @Autowired
    private ComparativaProductividadService comparativaService;

    /**
     * Endpoint para obtener comparativa de productividad según filtros
     * 
     * @param vertical Filtro opcional por vertical
     * @param fabrica Filtro opcional por fábrica
     * @param sn1 Filtro opcional por SN1
     * @param sn2 Filtro opcional por SN2
     * @return ComparativaProductividadDTO con gráficos comparativos
     */
    @GetMapping
    public ResponseEntity<ComparativaProductividadDTO> getComparativa(
            @RequestParam(required = false) String vertical,
            @RequestParam(required = false) String fabrica,
            @RequestParam(required = false) String sn1,
            @RequestParam(required = false) String sn2) {
        
        try {
            log.info("GET /api/comparativa-productividad - Vertical: {}, Fabrica: {}, SN1: {}, SN2: {}", 
                     vertical, fabrica, sn1, sn2);
            
            ComparativaProductividadDTO resultado = comparativaService.getComparativa(vertical, fabrica, sn1, sn2);
            
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            log.error("Error al obtener comparativa de productividad", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
