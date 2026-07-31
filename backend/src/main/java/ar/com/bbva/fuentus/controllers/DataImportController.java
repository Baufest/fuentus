package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.services.DataImportServiceNew;
import ar.com.bbva.fuentus.dto.ImportResultDTO;
import ar.com.bbva.fuentus.enums.FileType;
import ar.com.bbva.fuentus.enums.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/data-import")
public class DataImportController {

    @Autowired
    private DataImportServiceNew dataImportService;

    /**
     * Endpoint único para importar cualquier tipo de dato
     * Ejemplos:
     * - POST /api/data-import/nucleus-services/json
     * - POST /api/data-import/nucleus-services/csv
     * - POST /api/data-import/chimera-reviews/json
     * - POST /api/data-import/chimera-sca/csv
     */
    @PostMapping("/{entity}/{format}")
    public ResponseEntity<ImportResultDTO> importData(
            @RequestParam("file") MultipartFile file,
            @PathVariable("entity") String entityType,
            @PathVariable("format") String fileFormat) {
        
        try {
            // Parsear entity type (convertir nucleus-services -> NUCLEUS_SERVICES)
            EntityType entity = EntityType.valueOf(
                entityType.toUpperCase().replace("-", "_")
            );
            
            // Parsear file type
            FileType format = FileType.valueOf(fileFormat.toUpperCase());
            
            // Importar datos usando el string del formato
            ImportResultDTO result = dataImportService.importData(file, entity, format.name().toLowerCase());
            return ResponseEntity.ok(result);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ImportResultDTO(0, 0, "Parámetros inválidos: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ImportResultDTO(0, 0, "Error de importación: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener información sobre tipos soportados
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getImportInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("supportedEntities", Arrays.asList("nucleus-services"));
        info.put("supportedFormats", Arrays.asList("csv", "json"));
        info.put("implemented", Arrays.asList(
            "POST /api/fuentus/data-import/nucleus-services/json",
            "POST /api/fuentus/data-import/nucleus-services/csv"
        ));
        info.put("planned", Arrays.asList(
            "POST /api/fuentus/data-import/chimera-reviews/json",
            "POST /api/fuentus/data-import/chimera-sast/json",
            "POST /api/fuentus/data-import/apps/json"
        ));
        return ResponseEntity.ok(info);
    }
}