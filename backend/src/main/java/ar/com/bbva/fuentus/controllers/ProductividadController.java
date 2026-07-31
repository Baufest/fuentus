package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.ProductividadDTO;
import ar.com.bbva.fuentus.services.ProductividadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productividad")
public class ProductividadController {

    private static final Logger logger = LoggerFactory.getLogger(ProductividadController.class);

    @Autowired
    private ProductividadService productividadService;

    /**
     * Endpoint para importar datos de productividad desde un archivo CSV
     * @param file Archivo CSV con los datos de productividad
     * @param fecha Fecha que se asignará a todos los registros del CSV
     * @return Resultado de la importación
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importProductividad(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        logger.info("Iniciando importación de productividad para la fecha: {}", fecha);

        Map<String, Object> response = new HashMap<>();

        try {
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "El archivo está vacío");
                return ResponseEntity.badRequest().body(response);
            }

            List<String> errors = productividadService.importFromCSV(file, fecha);

            boolean hasErrors = errors.stream().anyMatch(e -> e.contains("Error"));

            response.put("success", !hasErrors);
            response.put("messages", errors);

            if (hasErrors) {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error en la importación de productividad", e);
            response.put("success", false);
            response.put("message", "Error al procesar el archivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para obtener todos los registros de productividad
     */
    @GetMapping
    public ResponseEntity<List<ProductividadDTO>> getAllProductividad() {
        List<ProductividadDTO> list = productividadService.getAll();
        return ResponseEntity.ok(list);
    }

    /**
     * Endpoint para obtener registros de productividad por fecha
     */
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<ProductividadDTO>> getProductividadByFecha(
            @PathVariable("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<ProductividadDTO> list = productividadService.getAllByFecha(fecha);
        return ResponseEntity.ok(list);
    }

    /**
     * Endpoint para obtener registros de productividad por nucleusId (serviceId)
     */
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<ProductividadDTO>> getProductividadByServiceId(
            @PathVariable("serviceId") Long serviceId) {
        List<ProductividadDTO> list = productividadService.getByNucleusId(serviceId);
        return ResponseEntity.ok(list);
    }
}
