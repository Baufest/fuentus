package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.VelocidadDTO;
import ar.com.bbva.fuentus.services.VelocidadService;
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
@RequestMapping("/velocidad")
public class VelocidadController {

    private static final Logger logger = LoggerFactory.getLogger(VelocidadController.class);

    @Autowired
    private VelocidadService velocidadService;

    /**
     * Endpoint para importar datos de velocidad desde un archivo CSV
     * @param file Archivo CSV con los datos de velocidad
     * @return Resultado de la importación
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importVelocidad(
            @RequestParam("file") MultipartFile file) {

        logger.info("Iniciando importación de velocidad");

        Map<String, Object> response = new HashMap<>();

        try {
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "El archivo está vacío");
                return ResponseEntity.badRequest().body(response);
            }

            List<String> errors = velocidadService.importFromCSV(file);

            boolean hasErrors = errors.stream().anyMatch(e -> e.contains("Error"));

            response.put("success", !hasErrors);
            response.put("messages", errors);

            if (hasErrors) {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error en la importación de velocidad", e);
            response.put("success", false);
            response.put("message", "Error al procesar el archivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para obtener todos los registros de velocidad
     */
    @GetMapping
    public ResponseEntity<List<VelocidadDTO>> getAllVelocidad() {
        List<VelocidadDTO> list = velocidadService.getAll();
        return ResponseEntity.ok(list);
    }

    /**
     * Endpoint para obtener registros de velocidad por fecha
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<VelocidadDTO>> getVelocidadByDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<VelocidadDTO> list = velocidadService.getAllByDate(date);
        return ResponseEntity.ok(list);
    }

    /**
     * Endpoint para obtener registros de velocidad por nucleusId (serviceId)
     */
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<VelocidadDTO>> getVelocidadByServiceId(
            @PathVariable("serviceId") Long serviceId) {
        List<VelocidadDTO> list = velocidadService.getByNucleusId(serviceId);
        return ResponseEntity.ok(list);
    }
}
