package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.OrgN2FabricaResponseDto;
import ar.com.bbva.fuentus.dto.VerticalRequestDto;
import ar.com.bbva.fuentus.dto.VerticalResponseDto;
import ar.com.bbva.fuentus.dto.VerticalUpdateDto;
import ar.com.bbva.fuentus.services.VerticalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/verticales")
@RestController
public class VerticalController {
    
    private final VerticalService verticalService;
    
    public VerticalController(VerticalService verticalService) {
        this.verticalService = verticalService;
    }
    
    /**
     * Endpoint para crear una nueva vertical
     */
    @PostMapping
    public ResponseEntity<VerticalResponseDto> createVertical(@RequestBody VerticalRequestDto verticalRequestDto) {
        try {
            VerticalResponseDto createdVertical = verticalService.createVertical(verticalRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVertical);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Endpoint para obtener todas las verticales con sus orgN2fabrica distintos
     */
    @GetMapping
    public ResponseEntity<List<VerticalResponseDto>> getAllVerticalesWithOrgN2Fabrica() {
        try {
            List<VerticalResponseDto> verticales = verticalService.getAllVerticalesWithOrgN2Fabrica();
            return ResponseEntity.ok(verticales);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Endpoint para actualizar una vertical
     * Actualiza el nombre de la vertical y asocia las fabricas con las entidades Nucleus
     */
    @PutMapping("/{id}")
    public ResponseEntity<VerticalResponseDto> updateVertical(
            @PathVariable Long id, 
            @RequestBody VerticalUpdateDto updateDto) {
        try {
            VerticalResponseDto updatedVertical = verticalService.updateVertical(id, updateDto);
            return ResponseEntity.ok(updatedVertical);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Endpoint para obtener todas las OrgN1Fabricas (ServiceN1) con sus Verticales asociadas
     * GET /verticales/org-n1-fabricas
     */
    @GetMapping("/factories")
    public ResponseEntity<List<OrgN2FabricaResponseDto>> getAllOrgN1FabricasWithVerticales() {
        try {
            List<OrgN2FabricaResponseDto> orgN2Fabricas = verticalService.getAllOrgN2FabricasWithVerticales();
            return ResponseEntity.ok(orgN2Fabricas);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

